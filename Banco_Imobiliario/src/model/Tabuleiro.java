package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Tabuleiro {
    private List<Campo> campos;
    private int tamanho;
    private Banco banco;

    Tabuleiro(Banco banco)
	{
        this.campos = new ArrayList<>();
        inicializarCasas();
        this.banco = banco;
    }

    private void inicializarCasas() {
    	ArrayList<String> nomesDosCampos = new ArrayList<>();
        ArrayList<Double> precosPassagem = new ArrayList<>();
        
    	final String nomes = """
    			PARTIDA
    			Leblon
    			Av. Presidente Vargas
    			Av. Nossa Sra. De Copacabana
    			Companhia Ferroviária
    			Av. Brigadeiro Faria Lima
    			Companhia de Viação
    			Av. Rebouças
    			Av. 9 de Julho
    			PRISÃO
    			Av. Europa
    			Rua Augusta
    			Av. Pacaembú
    			Companhia de Táxi
    			Interlagos
    			Lucros ou Dividendos ($$)
    			Morumbi
    			PARADA LIVRE
    			Flamengo
    			Botafogo
    			Imposto de Renda ($$)
    			Companhia de Navegação
    			Av. Brasil
    			Av. Paulista
    			Jardim Europa
    			VÁ PARA A PRISÃO
    			Copacabana
    			Companhia de Aviação
    			Av. Vieira Souto
    			Av. Atlântica
    			Companhia de Táxi Aéreo
    			Ipanema
    			Jardim Paulista
    			Brooklin""";
    	
    	final String precos = """
		    	-200
		    	100
		    	60
		    	60
		    	200
		    	240
		    	200
		    	220
		    	220
		    	0
		    	200
		    	180
		    	180
		    	150
		    	350
		    	-200
		    	400
		    	0
		    	120
		    	100
		    	200
		    	150
		    	160
		    	140
		    	140
		    	0
		    	260
		    	200
		    	320
		    	300
		    	200
		    	300
		    	280
		    	260""";

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
        	
            if(nome.equals("PRISÃO")) {
            	campos.add(new Prisao(nome, precoPassagem));}
            else if(nome.equals("PARTIDA" ) || nome.equals("PARADA LIVRE" ) || 
            		nome.equals("Lucros ou Dividendos ($$)") || 
            		nome.equals("Imposto de Renda ($$)")){
            	campos.add(new Campo(nome, precoPassagem));}
            else if(nome.equals("VÁ PARA A PRISÃO")) {
            	campos.add(new VaParaPrisao(nome, precoPassagem));}
//          ASSUMIMOS, como não consta em uma tabela o preço de compra de um terreno, que ele é
//            igual a 5 vezes o preço que se paga por casa ao passar pelo terreno
            else if(nome.startsWith("Companhia")) {
            	campos.add(new Empresa(nome, precoPassagem, 5*precoPassagem));}
            else {
            	campos.add(new Terreno(nome, precoPassagem, 5*precoPassagem));}
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

    // Calcula a nova posição após o movimento e lida com pagamentos
    int moverJogador(Jogador jogador, int posicaoAtual, int[] dados, Scanner scanner) {
    	if(dados.length != 2) { throw new IllegalArgumentException("Jogada de dados inválida!");}
    	if(jogador.getIsNaPrisao()) {
    		Campo campo = campos.get(this.getPosicaoPrisao());
    		if(campo instanceof Prisao) {
    			Prisao prisao = (Prisao) campo; 

//    			PODE SAIR DA PRISAO
        		if(prisao.podeSairDaPrisao(jogador, dados[0], dados[1])) {
        			if(jogador.getRodadasPreso() >= 4) {
        				int novaPosicao = posicaoAtual + dados[0] + dados[1];
            			prisao.sairDaPrisao(jogador);
        				return novaPosicao;
        			}
        			prisao.sairDaPrisao(jogador);
            		return this.moverJogador(jogador, posicaoAtual, Dados.getInstance().jogar(), scanner);
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
        int novaPosicao = (posicaoAtual + passos) % tamanho;
        
//      caso jogador passe, mas não pare no campo inicial (recebe dinheiro)
        if((posicaoAtual + passos) > tamanho) {
            campos.get(0).caiuNoCampo(jogador, banco);
        }
        
        if(novaPosicao == this.getPosicaoVaParaPrisao() 
        		&& campos.get(novaPosicao) instanceof VaParaPrisao) {
        	VaParaPrisao campoVPP = (VaParaPrisao)campos.get(novaPosicao);
            campoVPP.caiuNoCampo(jogador, this);
            return this.getPosicaoPrisao();
        }
        if(campos.get(novaPosicao) instanceof Propriedade) {
        	Propriedade prop = (Propriedade)campos.get(novaPosicao); 
            prop.caiuNoCampo(jogador, banco, scanner);
        } 
        
        else {
//          caso não seja nem propriedade nem vaParaPrisao
            campos.get(novaPosicao).caiuNoCampo(jogador, banco);
        }
            	               
        return novaPosicao;
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
    	return campos.get(posicao) instanceof Propriedade;
    }
}
