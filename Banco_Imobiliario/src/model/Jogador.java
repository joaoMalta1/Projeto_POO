package model;

import java.util.ArrayList;
import java.util.List;

import controller.ControlePartida;

class Jogador {
	private String nome;
    private Peao peao;
    private double saldo;
    private boolean faliu;
    private List<Propriedade> propriedades;

    // ---- Prisão ----
    private boolean naPrisao;
    private int rodadasPreso;    
    // private List<TituloDePropriedade> titulos;
    // private List<Carta> cartas;

    // ---- Controle de duplas consecutivas ----
    private int[] ultimoParDados;
    private int[] penultimoParDados;
    
    Jogador(String nome, Peao peao) {
    	this.nome 		  = nome;
        this.peao 		  = peao;
        this.saldo 		  = 4000; // saldo inicial
        this.faliu 		  = false;
        this.naPrisao 	  = false;
        this.rodadasPreso = 0;
        this.propriedades = new ArrayList<>();
        
        ultimoParDados 	  = null;
        penultimoParDados = null;
    }

//    TODO: função de jogar dados e mover peão (integrar com função moverJogador da classe Tabuleiro

    // ---- Getters ----
    Peao getPeao() {
        return peao;
    }

    double getSaldo() {
        return saldo;
    }

    boolean isFaliu() {
        return faliu;
    }

    boolean getIsNaPrisao() {
        return naPrisao;
    }

    int getRodadasPreso() {
        return rodadasPreso;
    }

    List<Propriedade> getPropriedades() {
        return propriedades;
    }

    // ---- Setters ----
    void setNaPrisao(boolean estaNaPrisao) {
    	this.naPrisao = estaNaPrisao;
    }
    
    void setRodadasPreso(int rodadasPreso) {
    	this.rodadasPreso = rodadasPreso;
    }
    
    // ---- Métodos de dinheiro ----
    void adicionarValor(double valor) {
        if (valor > 0) {
            saldo += valor;
        }
    }

    boolean removerValor(double valor) {
    	if(valor <= 0) {
    		return false;
    	}
    	else if (saldo >= valor) {
            saldo -= valor;
            return true; // sucesso
        } else {
            faliu = true;
            falencia();
            return false; // jogador perdeu
        }
    }

     // ---- Métodos de Propriedade ----
    
    void adicionarPropriedade(Propriedade propriedade) {
        if (propriedade != null && !this.propriedades.contains(propriedade)) {
            this.propriedades.add(propriedade);
        }
    }

    // Remove a propriedade da lista do jogador (após venda ou falência)
    void removerPropriedade(Propriedade propriedade) {
        this.propriedades.remove(propriedade);
    }

    // ---- Falência ----
    void falencia() {
        if (this.faliu) {
            for (Propriedade p : new ArrayList<>(this.propriedades)) {
                p.dono = null; 
                if (p instanceof Terreno) {
                    ((Terreno) p).qtdCasas = 0; // é isso ?
                }
            }
            this.propriedades.clear();
            this.saldo = 0.0; 
            System.out.println("jogador faliu");
            CentralPartida.getInstance().checaFimJogo();
        }
    }
    
    String getNome() {
    	return nome;
    }
    
//    ---- Métodos de verificação dos 3 dados consecutivos para ida à prisão -----
    
 // Novo método para verificar três duplas consecutivas
    public boolean verificarTresDuplasConsecutivas(int[] novosDados) {
        if (novosDados.length != 2) {
        	throw new IllegalArgumentException("Tamanho do vetor de dados incorreto");
        }
    	
    	if (naPrisao) {
            // Se já está na prisão, não verifica duplas
            resetarControleDuplas();
            return false;
        }
        
        // Verifica se temos três pares de dados consecutivos
        if (ultimoParDados != null && penultimoParDados != null 
        		&& ultimoParDados.length == 2 && penultimoParDados.length == 2 ) {
            boolean ultimaDupla = (ultimoParDados[0] == ultimoParDados[1]);
            boolean penultimaDupla = (penultimoParDados[0] == penultimoParDados[1]);
            boolean novaDupla = (novosDados[0] == novosDados[1]);

            // Atualiza o histórico de dados
            penultimoParDados = ultimoParDados;
            ultimoParDados = novosDados;
            
            // Se todos os três pares forem duplas
            if (ultimaDupla && penultimaDupla && novaDupla) {
                return true;
            }
        }
        
     // Atualiza o histórico de dados para caso de apenas um último dado
        penultimoParDados = ultimoParDados;
        ultimoParDados = novosDados;
        
        return false;
    }
    
    // Método para resetar o controle de duplas
    public void resetarControleDuplas() {
        this.ultimoParDados = null;
        this.penultimoParDados = null;
    }
    
    // Método para ir para a prisão por três duplas
    public void jogouDados(int posicaoPrisao, int[] dados) {
    	if (dados.length != 2) {
        	throw new IllegalArgumentException("Tamanho do vetor de dados incorreto");
        }
    	
    	if(verificarTresDuplasConsecutivas(dados)) {
    		this.naPrisao = true;
            this.rodadasPreso = 0;
            this.peao.setPosicao(posicaoPrisao);
            resetarControleDuplas();
            System.out.println("DEBUG: Jogador " + nome + " foi para a prisão em " + posicaoPrisao);
    	}
    }
    
    @Override
    public String toString() {
        return "Nome: " + nome + 
        	   "\nPeão: " + peao.toString() +
               "\nSaldo: R$" + String.format("%.2f", saldo) +
               "\nNa prisão: " + naPrisao +
               "\nRodadas preso: " + rodadasPreso +
               "\nFalido: " + faliu;
    }
}
