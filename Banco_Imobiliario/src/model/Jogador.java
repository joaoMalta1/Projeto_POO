package model;
import java.util.ArrayList;
import java.util.List;

class Jogador {
    private Peao peao;
    private double saldo; 
    // private List<TituloDePropriedade> titulos;
    // private List<Carta> cartas;

    Jogador(Peao peao) {
        this.peao = peao;
        this.saldo = 4000; //valor inicial do jogador
        // this.titulos = new ArrayList<>();
        // this.cartas = new ArrayList<>();
    }

    Peao getPeao() {
        return peao;
    }

    double getSaldo() {
        return saldo;
    }

    // metodos de negocio (dinheiro)
    void adicionarValor(double valor) {
        if (valor > 0) {
            saldo += valor;
        }
    }

    int removerValor(double valor) {
        if (valor > 0 && saldo >= valor) {
            saldo -= valor;
            return 1; // sucesso
        } 
        else 
            return 0; // falha e jogador perdeu
    }

    // metodos de titulos e cartas
    // public List<TituloDePropriedade> getTitulos() {
    //     return titulos;
    // }

    // public List<Carta> getCartas() {
    //     return cartas;
    // }

    // public void adicionarTitulo(TituloDePropriedade titulo) {
    //     titulos.add(titulo);
    // }

    // public void removerTitulo(TituloDePropriedade titulo) {
    //     titulos.remove(titulo);
    // }

    // public void adicionarCarta(Carta carta) {
    //     cartas.add(carta);
    // }

    // public void removerCarta(Carta carta) {
    //     cartas.remove(carta);
    //}

    @Override
    public String toString() {
        return "Peão: " + peao +
                "\nSaldo: R$" + String.format("%.2f", saldo); //+
                // "\nPropriedades: " + titulos.size() +
                // "\nCartas: " + cartas.size();
    }
}
