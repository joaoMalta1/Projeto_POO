package model;

class Jogador {

    private Peao peao;
    private double saldo;
    private boolean faliu;

    // ---- Prisão ----
    private boolean naPrisao;
    private int rodadasPreso;
//    TODO: adicionar fila dos últimos dados lançados
    // private List<TituloDePropriedade> titulos;
    // private List<Carta> cartas;

    Jogador(Peao peao) {
        this.peao = peao;
        this.saldo = 4000; // saldo inicial
        this.faliu = false;
        this.naPrisao = false;
        this.rodadasPreso = 0;
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

    @Override
    public String toString() {
        return "Peão: " + peao +
               "\nSaldo: R$" + String.format("%.2f", saldo) +
               "\nNa prisão: " + naPrisao +
               "\nRodadas preso: " + rodadasPreso +
               "\nFalido: " + faliu;
    }
}
