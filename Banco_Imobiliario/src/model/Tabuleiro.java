package model;

import java.util.ArrayList;
import java.util.List;

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
    	String[] nomesDosCampos;
    	double[] precosPassagem;
    	
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

//    	converte string de nomes (da tabela) em array de nomes (String)
    	nomesDosCampos  = nomes.split("\\r?\\n");
    	for(int i = 0; i < nomesDosCampos.length; i++) {
    		nomesDosCampos[i] = nomesDosCampos[i].trim();    	
    	}
    	
//    	converte string de precos (da tabela) em array de precos (double)
    	String[] linhas = precos.split("\\r?\\n");
    	precosPassagem  = new double[linhas.length];
    	for(int i = 0; i < linhas.length; i++) {
    		precosPassagem[i] = Double.parseDouble(linhas[i].trim());
    	}

    	
//    	cria campos seguindo a tabela
        for(int i = 0; i < nomesDosCampos.length; i++){
        	String nome  = nomesDosCampos[i];
        	double precoPassagem = precosPassagem[i];
        	
            if(nome == "PRISÃO") {
            	System.out.println(nome);
            	campos.add(new Prisao(nome, precoPassagem));}
            else if(nome == "PARTIDA" || nome == "PARADA LIVRE" || 
            		nome == "Lucros ou Dividendos ($$)" || 
            		nome == "Imposto de Renda ($$)"){
            	System.out.println(nome);
            	campos.add(new Campo(nome, precoPassagem));}
            else if(nome == "VÁ PARA A PRISÃO") {
            	System.out.println(nome);
            	campos.add(new vaParaPrisao(nome, precoPassagem));}
//          ASSUMIMOS, como não consta em uma tabela o preço de compra de um terreno, que ele é
//            igual a 5 vezes o preço que se paga por casa ao passar pelo terreno
            else if(nome.startsWith("Companhia")) {
            	System.out.println(nome);
            	campos.add(new Empresa(nome, precoPassagem, 5*precoPassagem));}
            else {
            	System.out.println(nome);
            	campos.add(new Terreno(nome, precoPassagem, 5*precoPassagem));}
        }
        tamanho = campos.size();
        System.out.println(tamanho);
    }

    // Retorna o objeto do campo passado como parâmetro
    Campo getCampo(int posicao) {
        if (posicao < 0 || posicao >= tamanho) {
            throw new IllegalArgumentException("Posição inválida no tabuleiro!");
        }
        return campos.get(posicao);
    }
    
    Campo getCampo(String nome) {
    	for(Campo campo : campos) {
    		if(campo.nome == nome) {
    			return campo;
    		}
    	}
    	return null;
    }

    // Calcula a nova posição após o movimento e lida com pagamentos
    int moverJogador(Jogador jogador, int posicaoAtual, int passos) {
        int novaPosicao = (posicaoAtual + passos) % tamanho;
        
        if(novaPosicao == this.getPosicaoVaParaPrisao() 
        		&& campos.get(novaPosicao) instanceof vaParaPrisao) {
        	vaParaPrisao campoVPP = (vaParaPrisao)campos.get(novaPosicao);
        	campoVPP.caiuNoCampo(jogador, this);
        }
        else {
        	campos.get(novaPosicao).caiuNoCampo(jogador, banco);        	
        }
        
        if(posicaoAtual + passos > tamanho) {
        	campos.get(0).caiuNoCampo(jogador, banco);
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
        	if(campos.get(i) instanceof vaParaPrisao) {
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
}
