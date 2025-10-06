package model;

public class Peao {
    private String cor;
    private int posicao;

    public Peao(String cor) {
        this.cor = cor;
        this.posicao = 0; 
    }

    public String getCor() {
        return cor;
    }

    public int getPosicao() {
        return posicao;
    }

    public void setPosicao(int novaPosicao) {
        this.posicao = novaPosicao;
    }

    public void mover(int casas) {
        this.posicao += casas;
    }

    @Override
    public String toString() {
        return "Peão " + cor + " na posição " + posicao;
    }
}
