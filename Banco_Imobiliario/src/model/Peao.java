package model;

class Peao {
    private CorPeao cor;
    private int posicao;

    Peao(CorPeao cor) {
        this.cor = cor;
        this.posicao = 0;
    }

    CorPeao getCor() {
        return cor;
    }

    int getPosicao() {
        return posicao;
    }

    void setPosicao(int novaPosicao) {
        this.posicao = novaPosicao; // classe util para quando for pra cadeia independente da posicao
    }

    int mover(int casas) {
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
    
    enum CorPeao {
        VERMELHA,
        AZUL,
        LARANJA,
        AMARELA,
        ROXA,
        CINZA
    }
}
