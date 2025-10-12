package model;

class Jogador {

    private Peao peao;
    private double saldo;
    private boolean faliu;

    // ---- Prisão ----
    private boolean naPrisao;
    private int rodadasPreso;
    // manter uma array lista de proiedades para comprar, vender  e associar as funcoes da classe de propriedade e fazer a de quando falir 
    // TODO: adicionar fila dos últimos dados lançados para verificar trinca de dados iguais; 
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

    boolean isNaPrisao() {
        return naPrisao;
    }

    int getRodadasPreso() {
        return rodadasPreso;
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

    // ---- Métodos da prisão ----
    void irParaPrisao() {
        naPrisao = true;
        rodadasPreso = 0;
    }

    void sairDaPrisao() {
        naPrisao = false;
        rodadasPreso = 0;
    }

    void incrementaRodadasPreso() {
        if (naPrisao) {
            rodadasPreso++;
        }
    }

    boolean podeSairDaPrisao(int dado1, int dado2) {
        return (dado1 == dado2) || (rodadasPreso >= 3);
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
