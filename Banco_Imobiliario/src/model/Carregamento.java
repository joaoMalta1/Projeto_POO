package model;

import java.io.*;
import java.util.*;
import controller.CorPeao;

/*SINGLETON*/
public class Carregamento {
    private static Carregamento ctrl = null;
    private Carregamento() {}
    static Carregamento getInstance() {
    	if(ctrl == null) {
    		ctrl = new Carregamento();
    	}
    	return ctrl;
    }
	private CentralPartida central;
    
	CentralPartida carregarJogo(String caminhoArquivo) throws IOException {
        
        // Resetar a CentralPartida se existir
        if (CentralPartida.getInstance() != null) {
            CentralPartida.getInstance().reset();
        }
        central = CentralPartida.getInstance();
        
        List<String> linhas = lerArquivo(caminhoArquivo);
        
        // Carregar banco
        Banco banco = carregarBanco(linhas);
        central.setBanco(banco);
        
        // Carregar baralho
        Baralho baralho = carregarBaralho(linhas);
        central.setBaralho(baralho);
        
        // Carregar tabuleiro
        Tabuleiro tabuleiro = carregarTabuleiro(linhas, banco);
        central.setTabuleiro(tabuleiro);
        
        // Carregar jogadores
        ArrayList<Jogador> jogadores = carregarJogadores(linhas, tabuleiro, baralho);
        central.setJogadores(jogadores);
        
        // Carregar jogador atual
        int jogadorAtual = carregarJogadorAtual(linhas);
        central.setJogadorAtual(jogadorAtual);
        
        if (central.getQtdJogadores() == 0) {
            throw new IOException("Nenhum jogador foi carregado do arquivo!");
        }
        
        return central;
    }
    
    private List<String> lerArquivo(String caminhoArquivo) throws IOException {
        List<String> linhas = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                linhas.add(linha);
            }
        }
        return linhas;
    }
    
    private Banco carregarBanco(List<String> linhas) {
        Banco banco = new Banco();
        for (int i = 0; i < linhas.size(); i++) {
            if (linhas.get(i).equals("BANCO:")) {
                String saldoLine = linhas.get(i + 1);
                if (saldoLine.startsWith("SALDO:")) {
                    double saldo = Double.parseDouble(saldoLine.substring(6));
                    banco.setSaldo(saldo);
                    break;
                }
            }
        }
        return banco;
    }
    
    private Baralho carregarBaralho(List<String> linhas) {
        Queue<Carta> cartas = new LinkedList<>();
        boolean inBaralhoSection = false;
        boolean inCarta = false;
        
        Carta currentCarta = null;
        String nome = null;
        double preco = 0;
        String tipo = null;
        
        for (String linha : linhas) {
            if (linha.equals("BARALHO:")) {
                inBaralhoSection = true;
                continue;
            }
            
            if (!inBaralhoSection) continue;
            
            if (linha.equals("CARTA_INICIO")) {
                inCarta = true;
                nome = null;
                preco = 0;
                tipo = null;
                continue;
            }
            
            if (linha.equals("CARTA_FIM")) {
                inCarta = false;
                if (nome != null) {
                    switch (tipo) {
                        case "LIVRE_DA_PRISAO":
                            currentCarta = new CartaLivreDaPrisao(nome, null);
                            break;
                        case "VA_PARA_PRISAO":
                            currentCarta = new CartaVaParaPrisao(nome, null);
                            break;
                        case "RECEBER_DE_JOGADORES":
                            currentCarta = new CartaReceberDeJogadores(nome, null);
                            break;
                        case "PONTO_DE_PARTIDA":
                            currentCarta = new CartaPontoDePartida(nome, preco, null);
                            break;
                        default:
                            currentCarta = new Carta(nome, preco, null);
                            break;
                    }
                    cartas.add(currentCarta);
                }
                continue;
            }
            
            if (inCarta) {
                if (linha.startsWith("NOME:")) {
                    nome = linha.substring(5);
                } else if (linha.startsWith("PRECO_A_RECEBER:")) {
                    preco = Double.parseDouble(linha.substring(16));
                } else if (linha.startsWith("TIPO:")) {
                    tipo = linha.substring(5);
                }
            }
            
            if (linha.startsWith("TABULEIRO:")) {
                break; // Sai da seção do baralho
            }
        }
        
        Baralho baralho = new Baralho(cartas);
        
        // Configurar o baralho nas cartas
        for (Carta carta : cartas) {
            carta.baralho = baralho;
            if (carta instanceof CartaLivreDaPrisao) {
                ((CartaLivreDaPrisao) carta).setBaralho(baralho);
            }
        }
        
        return baralho;
    }
    
    private Tabuleiro carregarTabuleiro(List<String> linhas, Banco banco) {
        List<Campo> campos = new ArrayList<>();
        boolean inTabuleiroSection = false;
        boolean inCampo = false;
        
        Campo currentCampo = null;
        String nome = null;
        double precoPassagem = 0;
        String tipo = null;
        double precoCompra = 0;
        String donoNome = null;
        int qtdCasas = 0;
        int qtdHoteis = 0;
        
        for (String linha : linhas) {
            if (linha.equals("TABULEIRO:")) {
                inTabuleiroSection = true;
                continue;
            }
            
            if (!inTabuleiroSection) continue;
            
            if (linha.equals("CAMPO_INICIO")) {
                inCampo = true;
                nome = null;
                precoPassagem = 0;
                tipo = null;
                precoCompra = 0;
                donoNome = null;
                qtdCasas = 0;
                qtdHoteis = 0;
                continue;
            }
            
            if (linha.equals("CAMPO_FIM")) {
                inCampo = false;
                if (nome != null) {
                    switch (tipo) {
                        case "TERRENO":
                            currentCampo = new Terreno(nome, precoPassagem);
                            ((Terreno) currentCampo).setPrecoCompra(precoCompra);
                            ((Terreno) currentCampo).setQtdCasas(qtdCasas);
                            ((Terreno) currentCampo).setQtdHotel(qtdHoteis);
                            break;
                        case "EMPRESA":
                            currentCampo = new Empresa(nome, precoPassagem);
                            ((Empresa) currentCampo).setPrecoCompra(precoCompra);
                            break;
                        case "SORTE_OU_REVES":
                            currentCampo = new SorteOuReves(nome, precoPassagem);
                            break;
                        case "PRISAO":
                            currentCampo = new Prisao(nome, precoPassagem);
                            break;
                        case "VA_PARA_PRISAO":
                            currentCampo = new VaParaPrisao(nome, precoPassagem);
                            break;
                        default:
                            currentCampo = new Campo(nome, precoPassagem);
                            break;
                    }
                    
                    if (currentCampo instanceof Propriedade) {
                        Propriedade prop = (Propriedade) currentCampo;
                        prop.setPrecoCompra(precoCompra);
                        // O dono será associado posteriormente ao carregar jogadores
                    }
                    
                    campos.add(currentCampo);
                }
                continue;
            }
            
            if (inCampo) {
                if (linha.startsWith("NOME:")) {
                    nome = linha.substring(5);
                } else if (linha.startsWith("PRECO_PASSAGEM:")) {
                    precoPassagem = Double.parseDouble(linha.substring(15));
                } else if (linha.startsWith("TIPO:")) {
                    tipo = linha.substring(5);
                } else if (linha.startsWith("PRECO_COMPRA:")) {
                    precoCompra = Double.parseDouble(linha.substring(13));
                } else if (linha.startsWith("DONO:")) {
                    donoNome = linha.substring(5);
                } else if (linha.startsWith("QTD_CASAS:")) {
                    qtdCasas = Integer.parseInt(linha.substring(10));
                } else if (linha.startsWith("QTD_HOTEIS:")) {
                    qtdHoteis = Integer.parseInt(linha.substring(11));
                }
            }
            
            if (linha.startsWith("JOGADORES:")) {
                break; // Sai da seção do tabuleiro
            }
        }
        
        return new Tabuleiro(campos, banco);
    }
    
    private ArrayList<Jogador> carregarJogadores(List<String> linhas, Tabuleiro tabuleiro, Baralho baralho) {
        ArrayList<Jogador> jogadores = new ArrayList<>();
        boolean inJogadoresSection = false;
        boolean inJogador = false;
        boolean inPropriedades = false;
        
        Jogador currentJogador = null;
        String nome = null;
        String corPeaoStr = null;
        int posicaoPeao = 0;
        double saldo = 0;
        boolean faliu = false;
        boolean naPrisao = false;
        int rodadasPreso = 0;
        String ultimoDadosStr = null;
        String penultimoDadosStr = null;
        ArrayList<String> propriedadesNomes = new ArrayList<>();
        String cartaLivrePrisaoNome = null;
        
        for (String linha : linhas) {
            if (linha.equals("JOGADORES:")) {
                inJogadoresSection = true;
                continue;
            }
            
            if (!inJogadoresSection) continue;
            
            if (linha.startsWith("JOGADOR_INICIO")) {
                inJogador = true;
                nome = null;
                corPeaoStr = null;
                posicaoPeao = 0;
                saldo = 0;
                faliu = false;
                naPrisao = false;
                rodadasPreso = 0;
                ultimoDadosStr = null;
                penultimoDadosStr = null;
                propriedadesNomes.clear();
                cartaLivrePrisaoNome = null;
                continue;
            }
            
            if (linha.startsWith("JOGADOR_FIM")) {
                inJogador = false;
                if (nome != null && corPeaoStr != null) {
                    CorPeao corPeao = CorPeao.valueOf(corPeaoStr);
                    Peao peao = new Peao(corPeao);
                    peao.setPosicao(posicaoPeao);
                    
                    currentJogador = new Jogador(nome, peao);
                    currentJogador.setSaldo(saldo);
                    currentJogador.setFaliu(faliu);
                    currentJogador.setNaPrisao(naPrisao);
                    currentJogador.setRodadasPreso(rodadasPreso);
                    
                    // Configurar dados
                    if (ultimoDadosStr != null && !ultimoDadosStr.equals("null")) {
                        int[] ultimoDados = parseDados(ultimoDadosStr);
                        currentJogador.setUltimoParDados(ultimoDados);
                    }
                    
                    if (penultimoDadosStr != null && !penultimoDadosStr.equals("null")) {
                        int[] penultimoDados = parseDados(penultimoDadosStr);
                        currentJogador.setPenultimoParDados(penultimoDados);
                    }
                    
//                    // Associar propriedades
//                    for (String propNome : propriedadesNomes) {
//                        Campo campo = tabuleiro.getCampo(propNome);
//                        if (campo instanceof Propriedade) {
//                            Propriedade propriedade = (Propriedade) campo;
//                            propriedade.setDono(currentJogador);
//                            currentJogador.adicionarPropriedade(propriedade);
//                        }
//                    }
                    
                    for (String propNome : propriedadesNomes) {
                        // CORREÇÃO: Buscar pelo nome do campo no tabuleiro
                        Campo campo = null;
                        for (int i = 0; i < tabuleiro.getTamanho(); i++) {
                            Campo c = tabuleiro.getCampo(i);
                            if (c.nome.equals(propNome)) {
                                campo = c;
                                break;
                            }
                        }
                        
                        if (campo instanceof Propriedade) {
                            Propriedade propriedade = (Propriedade) campo;
                            propriedade.setDono(currentJogador);
                            currentJogador.adicionarPropriedade(propriedade);
                        } else {
                        }
                    }
                    
                    // Associar carta livre da prisão
                    if (cartaLivrePrisaoNome != null && !cartaLivrePrisaoNome.equals("null")) {
                        for (Carta carta : baralho.getCartas()) {
                            if (carta instanceof CartaLivreDaPrisao && 
                                carta.getNome().equals(cartaLivrePrisaoNome)) {
                                CartaLivreDaPrisao cartaLP = (CartaLivreDaPrisao) carta;
                                currentJogador.setCartaLivreDaPrisão(cartaLP);
                                cartaLP.setDono(currentJogador);
                                break;
                            }
                        }
                    }
                    
                    jogadores.add(currentJogador);
                }
                continue;
            }
            
            if (inJogador) {
                if (linha.startsWith("NOME:")) {
                    nome = linha.substring(5);
                } else if (linha.startsWith("COR_PEAO:")) {
                    corPeaoStr = linha.substring(9);
                } else if (linha.startsWith("POSICAO_PEAO:")) {
                    posicaoPeao = Integer.parseInt(linha.substring(13));
                } else if (linha.startsWith("SALDO:")) {
                    saldo = Double.parseDouble(linha.substring(6));
                } else if (linha.startsWith("FALIU:")) {
                    faliu = Boolean.parseBoolean(linha.substring(6));
                } else if (linha.startsWith("NA_PRISAO:")) {
                    naPrisao = Boolean.parseBoolean(linha.substring(10));
                } else if (linha.startsWith("RODADAS_PRESO:")) {
                    rodadasPreso = Integer.parseInt(linha.substring(14));
                } else if (linha.startsWith("ULTIMO_DADOS:")) {
                    ultimoDadosStr = linha.substring(13);
                } else if (linha.startsWith("PENULTIMO_DADOS:")) {
                    penultimoDadosStr = linha.substring(16);
                } else if (linha.startsWith("PROPRIEDADES_INICIO")) {
                    inPropriedades = true;
                    continue;
                } else if (linha.startsWith("PROPRIEDADES_FIM")) {
                    inPropriedades = false;
                    continue;
                } else if (linha.startsWith("CARTA_LIVRE_PRISAO:")) {
                    cartaLivrePrisaoNome = linha.substring(19);
                }
                
                if (inPropriedades && !linha.startsWith("PROPRIEDADES_INICIO")) {
                    propriedadesNomes.add(linha);
                }
            }
        }
        
        return jogadores;
    }
    
    private int carregarJogadorAtual(List<String> linhas) {
        for (String linha : linhas) {
            if (linha.startsWith("JOGADOR_ATUAL:")) {
                return Integer.parseInt(linha.substring(14));
            }
        }
        return 0;
    }
    
    private int[] parseDados(String dadosStr) {
        // Formato: [1, 2]
        String cleanStr = dadosStr.replace("[", "").replace("]", "").replace(" ", "");
        String[] parts = cleanStr.split(",");
        int[] dados = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            dados[i] = Integer.parseInt(parts[i]);
        }
        return dados;
    }
}