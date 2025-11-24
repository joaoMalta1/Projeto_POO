package model;

import java.util.ArrayList;
import java.util.List;

class Tabuleiro {
    private List<Campo> campos;
    private int tamanho;
    private Banco banco;
	private final List<String> nomesDosCampos = new ArrayList<>();

    Tabuleiro(Banco banco)
	{
        this.campos = new ArrayList<>();
        inicializarCasas();
        this.banco = banco;
    }
    
    Tabuleiro(List<Campo> campos, Banco banco) {
        this.campos = campos;
        this.tamanho = campos.size();
        this.banco = banco;
    }

    private void inicializarCasas() {
        ArrayList<Double> precosPassagem = new ArrayList<>();
        
    	final String nomes = """	
    			PARTIDA
				Leblon
				SORTE OU REVES
				Av. Presidente Vargas
				Av. Nossa Sra. De Copacabana
				Companhia Ferroviária
				Av. Brigadeiro Faria Lima
				Companhia de Viação
				Av. Rebouças
				Av. 9 de Julho
				PRISÃO
				Av. Europa
				SORTE OU REVES
				Rua Augusta
				Av. Pacaembú
				Companhia de Táxi
				SORTE OU REVES
				Interlagos
				Lucros ou Dividendos ($$)
				Morumbi
				PARADA LIVRE
				Flamengo
				SORTE OU REVES
				Botafogo
				Imposto de Renda ($$)
				Companhia de Navegação
				Av. Brasil
				SORTE OU REVES
				Av. Paulista
				Jardim Europa
				VÁ PARA A PRISÃO
				Copacabana
				Companhia de Aviação
				Av. Vieira Souto
				Av. Atlântica
				Companhia de Táxi Aéreo
				Ipanema
				SORTE OU REVES
				Jardim Paulista
				Brooklin
				""";
    	
    	final String precos = """		    	
				-200
				100
				0
				60
				60
				200
				240
				200
				220
				220
				0
				200
				0
				180
				180
				150
				0
				350
				-200
				400
				0
				120
				0
				100
				200
				150
				160
				0
				140
				140
				0
				260
				200
				320
				300
				200
				300
				0
				280
				260
				""";

//    	converte string de nomes (da tabela) em arraylist de nomes (String)
    	String[] linhasNomes = nomes.split("\\r?\\n");
        for (String linha : linhasNomes) {
            nomesDosCampos.add(linha.trim());
        }
    	
//    	converte string de precos (da tabela) em array de precos (double)
        String[] linhasPrecos = precos.split("\\r?\\n");
        for (String linha : linhasPrecos) {
            precosPassagem.add(Double.parseDouble(linha.trim()));
        }

// 		verifica se os tamanhos dos arraylist sao iguais
        if (nomesDosCampos.size() != precosPassagem.size()) {
        	throw new IllegalArgumentException("Erro na inicialização do Tabuleiro!"); // aborta programa
        }

    	
//    	cria campos seguindo a tabela
        for(int i = 0; i < nomesDosCampos.size(); i++){
        	String nome  = nomesDosCampos.get(i);
        	double precoPassagem = precosPassagem.get(i);
        	if(nome.equals("SORTE OU REVES")) {
        		campos.add(new SorteOuReves(nome, precoPassagem));
        	}
        	else if(nome.equals("PRISÃO")) {
            	campos.add(new Prisao(nome, precoPassagem));}
            else if(nome.equals("PARTIDA" ) || nome.equals("PARADA LIVRE" ) || 
            		nome.equals("Lucros ou Dividendos ($$)") || 
            		nome.equals("Imposto de Renda ($$)")){
            	campos.add(new Campo(nome, precoPassagem));}
            else if(nome.equals("VÁ PARA A PRISÃO")) {
            	campos.add(new VaParaPrisao(nome, precoPassagem));}
            else if(nome.startsWith("Companhia")) {
            	campos.add(new Empresa(nome, precoPassagem));}
            else {
            	campos.add(new Terreno(nome, precoPassagem));}
        }
        tamanho = campos.size();
    }

    // Retorna o objeto do campo passado como parâmetro
    Campo getCampo(int posicao) {
        if (posicao < 0 || posicao >= tamanho) {
        	throw new IllegalArgumentException("Posição fora do intervalo!");
        }
        return campos.get(posicao);
    }
    
    Campo getCampo(String nome) {
    	for(Campo campo : campos) {
    		if(campo.nome.equals(nome)) {
    			return campo;
    		}
    	}
    	throw new IllegalArgumentException("Nome inexistente!");
    }

    
    // Calcula a nova posição após o movimento
    int moverJogador(Jogador jogador, int posicaoAtual, int[] dados, Baralho baralho) {
        if (dados == null || dados.length != 2) {
            throw new IllegalArgumentException("Dados inválidos");
        }
        
        // Caso esteja na prisão, lógica existente (mantida)
        if (jogador.getIsNaPrisao()) {
            Campo campo = campos.get(this.getPosicaoPrisao());
            if (campo instanceof Prisao) {
                Prisao prisao = (Prisao) campo; 

//    			PODE SAIR DA PRISAO
        		if(prisao.podeSairDaPrisao(jogador, dados[0], dados[1])) {
        			int novaPosicao = getPosicaoPrisao() + dados[0] + dados[1];
            		prisao.sairDaPrisao(jogador);
            		jogador.getPeao().setPosicao(novaPosicao);
        			return novaPosicao;
        		}

//        		CONTINUA PRESO
        		else {
        			prisao.passouUmaRodadaPreso(jogador);
        			return posicaoAtual;
        		} 
    		}
    		else { throw new IllegalStateException("Erro: Prisão não encontrada!");}
        }
//        CASO: jogador NÃO está na prisão
        
        int passos = dados[0] + dados[1];
        int tamanho = getTamanho();
        int soma = posicaoAtual + passos;
        int novaPosicao = soma % tamanho;
        
        // Atualiza posição base
        jogador.getPeao().setPosicao(novaPosicao);

        // Passou ou caiu exatamente na PARTIDA (recebe bônus)
        if (soma >= tamanho) {
            campos.get(0).caiuNoCampo(jogador, banco);
        }

        // Vá para a prisão
        if (novaPosicao == this.getPosicaoVaParaPrisao()
                && campos.get(novaPosicao) instanceof VaParaPrisao) {
            VaParaPrisao campoVPP = (VaParaPrisao) campos.get(novaPosicao);
//            CASO: não tenha carta livre da prisão
            if(campoVPP.caiuNoCampo(jogador, this)) { 
            	return getPosicaoPrisao();            	
            }
            return novaPosicao;
        }

        // Sorte/Reves pode alterar posição via efeitos próprios
        if (campos.get(novaPosicao) instanceof SorteOuReves) {
            SorteOuReves sorteReves = (SorteOuReves) campos.get(novaPosicao);
            novaPosicao = sorteReves.CaiuNoCampo(jogador, banco, baralho);
            jogador.getPeao().setPosicao(novaPosicao);
            // Se a carta mover o jogador, ela deve chamar setPosicao internamente
        } else if (campos.get(novaPosicao) instanceof Propriedade) {
            Propriedade prop = (Propriedade) campos.get(novaPosicao);
            prop.caiuNoCampo(jogador, banco);
        } else {
            campos.get(novaPosicao).caiuNoCampo(jogador, banco);
        }

        return jogador.getPeao().getPosicao();
    }
    
    int getPosicaoPrisao() {
        for(int i = 0; i < campos.size(); i++) {
        	if(campos.get(i) instanceof Prisao) {
        		return i;
        	}
        }
        return -1;
    }
    
    int getPosicaoVaParaPrisao() {
    	for(int i = 0; i < campos.size(); i++) {
        	if(campos.get(i) instanceof VaParaPrisao) {
        		return i;
        	}
        }
        return -1;
    }
    
    int getTamanho() {
    	return tamanho;
    }

    // Exibe o tabuleiro (para depuração)
    void mostrarTabuleiro() {
        for (int i = 0; i < campos.size(); i++) {
            System.out.println(i + ": " + campos.get(i).nome);
        }
    }
    
    boolean ehPropriedade(int posicao) {
    	if(posicao > 40) {
    		throw new IllegalArgumentException("Posição out of bounds");
    	}
    	return campos.get(posicao) instanceof Propriedade;
    }

	List<String> getNomesDosCampos() {
		return this.nomesDosCampos;
	}
	
	boolean ehSorteOuReves(int novaPos) {
		return campos.get(novaPos) instanceof SorteOuReves;
	}
}
