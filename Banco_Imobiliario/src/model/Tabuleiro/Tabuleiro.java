package model.Tabuleiro;

import java.util.ArrayList;
import java.util.List;

public class Tabuleiro {
    private List<String> casas;
    private int tamanho;
    private int posicaoPrisao;

    public Tabuleiro(int tamanho, int posicaoPrisao)
	{
        this.tamanho = tamanho;
        this.posicaoPrisao = posicaoPrisao;
        this.casas = new ArrayList<>();
        inicializarCasas();
    }

    private void inicializarCasas() {
        for (int i = 0; i < tamanho; i++) {
            if (i == posicaoPrisao) {
                casas.add("Prisão");
            } else {
                casas.add("Casa " + i);
            }
        }
    }

    // Retorna o nome da casa atual
    public String getCasa(int posicao) {
        if (posicao < 0 || posicao >= tamanho) {
            throw new IllegalArgumentException("Posição inválida no tabuleiro!");
        }
        return casas.get(posicao);
    }

    // Calcula a nova posição após o movimento
    public int moverJogador(int posicaoAtual, int passos) {
        int novaPosicao = (posicaoAtual + passos) % tamanho;
        return novaPosicao;
    }

    // Getter da prisão (útil para mandar o jogador pra lá)
    public int getPosicaoPrisao() {
        return posicaoPrisao;
    }

    // Exibe o tabuleiro (para depuração)
    public void mostrarTabuleiro() {
        for (int i = 0; i < casas.size(); i++) {
            System.out.println(i + ": " + casas.get(i));
        }
    }
}
