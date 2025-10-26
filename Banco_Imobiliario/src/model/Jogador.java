package model;

import java.util.ArrayList;
import java.util.List;

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

    Jogador(String nome, Peao peao) {
    	this.nome = nome;
        this.peao = peao;
        this.saldo = 4000; // saldo inicial
        this.faliu = false;
        this.naPrisao = false;
        this.rodadasPreso = 0;
        this.propriedades = new ArrayList<>();
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
        if (valor > 0 && saldo >= valor) {
            saldo -= valor;
            return true; // sucesso
        } else {
            faliu = true;
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
    void falencia(Banco banco) {
        if (this.faliu) {
            banco.recebeDinheiro(this.saldo);
            for (Propriedade p : new ArrayList<>(this.propriedades)) {
                p.dono = null; 
                if (p instanceof Terreno) {
                    ((Terreno) p).qtdCasas = 0; // é isso ?
                }
            }
            this.propriedades.clear();
            this.saldo = 0.0; 
            System.out.println("jogador faliu");
        }
    }

    @Override
    public String toString() {
        return "Peão: " + peao +
               "\nSaldo: R$" + String.format("%.2f", saldo) +
               "\nNa prisão: " + naPrisao +
               "\nRodadas preso: " + rodadasPreso +
               "\nFalido: " + faliu;
    }
}
