package model.Peao;

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
        this.posicao = novaPosicao; // classe util para quando for pra cadeia independente da posicao
    }

    public int mover(int casas) {
        if (casas < 0) {
            return 0;
        } else {
            this.posicao += casas;
            return 1;
        }
    }

    @Override // metodo pra printar o objeto
    public String toString() {
        return "Peão " + cor + " na posição " + posicao;
    }
}
