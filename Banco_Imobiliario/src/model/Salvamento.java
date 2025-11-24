package model;

import java.io.*;
import java.util.*;

/*SINGLETON*/
public class Salvamento {
    private static Salvamento ctrl;
	private Salvamento() {}

	static Salvamento getInstance() {
		if(ctrl == null) {
			ctrl = new Salvamento();
		}
		return ctrl;
	}
	
    void salvarJogo(String caminhoArquivo) throws IOException {
        CentralPartida central = CentralPartida.getInstance();
        try (PrintWriter writer = new PrintWriter(new FileWriter(caminhoArquivo))) {
            // Salvar versão do formato
            writer.println("VERSION:1.0");
            
            // Salvar dados do Banco
            writer.println("BANCO:");
            writer.println("SALDO:" + central.getBanco().getSaldo());
            
            // Salvar dados do Baralho
            writer.println("BARALHO:");
            salvarBaralho(writer, central.getBaralho());
            
            // Salvar dados do Tabuleiro
            writer.println("TABULEIRO:");
            salvarTabuleiro(writer, central.getTabuleiro());
            
            // Salvar dados dos Jogadores
            writer.println("JOGADORES:");
            salvarJogadores(writer, central);
            
            // Salvar jogador atual
            writer.println("JOGADOR_ATUAL:" + central.getJogadorAtual());
        }
    }
    
    private void salvarBaralho(PrintWriter writer, Baralho baralho) {
        Queue<Carta> cartas = baralho.getCartas();
        writer.println("QUANTIDADE_CARTAS:" + cartas.size());
        
        for (Carta carta : cartas) {
            writer.println("CARTA_INICIO");
            writer.println("NOME:" + carta.getNome());
            writer.println("PRECO_A_RECEBER:" + carta.getPrecoAReceber());
            writer.println("TIPO:" + getTipoCarta(carta));
            writer.println("CARTA_FIM");
        }
    }
    
    private String getTipoCarta(Carta carta) {
        if (carta instanceof CartaLivreDaPrisao) return "LIVRE_DA_PRISAO";
        if (carta instanceof CartaVaParaPrisao) return "VA_PARA_PRISAO";
        if (carta instanceof CartaReceberDeJogadores) return "RECEBER_DE_JOGADORES";
        if (carta instanceof CartaPontoDePartida) return "PONTO_DE_PARTIDA";
        return "NORMAL";
    }
    
    private void salvarTabuleiro(PrintWriter writer, Tabuleiro tabuleiro) {
        writer.println("TAMANHO:" + tabuleiro.getTamanho());
        writer.println("CAMPOS:");
        
        for (int i = 0; i < tabuleiro.getTamanho(); i++) {
            Campo campo = tabuleiro.getCampo(i);
            writer.println("CAMPO_INICIO");
            writer.println("NOME:" + campo.nome);
            writer.println("PRECO_PASSAGEM:" + campo.precoPassagem);
            writer.println("TIPO:" + getTipoCampo(campo));
            
            if (campo instanceof Propriedade) {
                Propriedade prop = (Propriedade) campo;
                writer.println("PRECO_COMPRA:" + prop.precoCompra);
                writer.println("DONO:" + (prop.dono != null ? prop.dono.getNome() : "null"));
                
                if (campo instanceof Terreno) {
                    Terreno terreno = (Terreno) campo;
                    writer.println("QTD_CASAS:" + terreno.qtdCasas);
                    writer.println("QTD_HOTEIS:" + terreno.qtdHotel);
                }
            }
            
            writer.println("CAMPO_FIM");
        }
    }
    
    private String getTipoCampo(Campo campo) {
        if (campo instanceof Terreno) return "TERRENO";
        if (campo instanceof Empresa) return "EMPRESA";
        if (campo instanceof SorteOuReves) return "SORTE_OU_REVES";
        if (campo instanceof Prisao) return "PRISAO";
        if (campo instanceof VaParaPrisao) return "VA_PARA_PRISAO";
        return "CAMPO_NORMAL";
    }
    
    private void salvarJogadores(PrintWriter writer, CentralPartida central) {
        int qtdJogadores = central.getQtdJogadores();
        writer.println("QUANTIDADE_JOGADORES:" + qtdJogadores);
        
        System.out.println("[DEBUG SALVAMENTO] Salvando " + qtdJogadores + " jogadores");
        
        // VERIFICAÇÃO CRÍTICA: Se não há jogadores, logar detalhes e retornar
        if (qtdJogadores == 0) {
            System.err.println("[DEBUG SALVAMENTO] ERRO: Nenhum jogador para salvar!");
            System.err.println("[DEBUG SALVAMENTO] Verifique se:");
            System.err.println("[DEBUG SALVAMENTO] 1. Jogadores foram criados com central.criaJogadores()");
            System.err.println("[DEBUG SALVAMENTO] 2. A instância CentralPartida é a mesma usada para criar jogadores");
            return; // Não tenta salvar jogadores se não existem
        }
        
        for (int i = 0; i < qtdJogadores; i++) {
            Jogador jogador = central.getJogador(i);
            
            // VERIFICAÇÃO ADICIONADA: Checar se jogador é null
            if (jogador == null) {
                System.err.println("[DEBUG SALVAMENTO] AVISO: Jogador " + i + " é null!");
                continue;
            }
            
            System.out.println("[DEBUG SALVAMENTO] Salvando jogador " + i + ": " + jogador.getNome());
            
            writer.println("JOGADOR_INICIO");
            writer.println("NOME:" + jogador.getNome());
            writer.println("COR_PEAO:" + jogador.getPeao().getCorString());
            writer.println("POSICAO_PEAO:" + jogador.getPeao().getPosicao());
            writer.println("SALDO:" + jogador.getSaldo());
            writer.println("FALIU:" + jogador.isFaliu());
            writer.println("NA_PRISAO:" + jogador.getIsNaPrisao());
            writer.println("RODADAS_PRESO:" + jogador.getRodadasPreso());
            
            // Salvar dados dos últimos lançamentos
            int[] ultimoDados = jogador.getUltimoParDados();
            int[] penultimoDados = jogador.getPenultimoParDados();
            
            writer.println("ULTIMO_DADOS:" + (ultimoDados != null ? 
                Arrays.toString(ultimoDados) : "null"));
            writer.println("PENULTIMO_DADOS:" + (penultimoDados != null ? 
                Arrays.toString(penultimoDados) : "null"));
            
            // Salvar propriedades
            writer.println("PROPRIEDADES_INICIO");
            List<Propriedade> propriedades = jogador.getPropriedades();
            System.out.println("[DEBUG SALVAMENTO] Jogador " + jogador.getNome() + " tem " + 
                propriedades.size() + " propriedades");
            
            for (Propriedade prop : propriedades) {
                if (prop != null) {
                    writer.println(prop.nome);
                }
            }
            writer.println("PROPRIEDADES_FIM");
            
            // Salvar carta livre da prisão
            CartaLivreDaPrisao cartaLivre = jogador.getCartaLivreDaPrisao();
            writer.println("CARTA_LIVRE_PRISAO:" + 
                (cartaLivre != null ? cartaLivre.getNome() : "null"));
            
            writer.println("JOGADOR_FIM");
        }
    }
}