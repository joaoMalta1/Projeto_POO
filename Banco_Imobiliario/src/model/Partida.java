package model;

import java.util.List;

import model.Peao.CorPeao;

import java.util.ArrayList;


class Partida {
//	TODO:  transformar isso em ArrayList
	private Jogador[] jogadores;
	private int qtd_jogadores;
	private int jogadorAtual;
	private Peao.CorPeao[] cores = {Peao.CorPeao.AZUL,Peao.CorPeao.VERMELHA,Peao.CorPeao.AMARELA,CorPeao.CINZA, Peao.CorPeao.LARANJA, Peao.CorPeao.ROXA};

	Partida(int qtd_jogadores) {
		this.qtd_jogadores = qtd_jogadores;
		
//		TODO: integrar jogada de dados com movimento dos peões

		for(int i = 0; i < qtd_jogadores; i++)
		{
			this.jogadores[i]=new Jogador(new Peao(cores[i]));
		}
		this.jogadorAtual = 0;
		if(qtd_jogadores < 2 || qtd_jogadores > 6){
			throw new IllegalArgumentException("A quantidade de jogadores deve ser entre 2 e 6");
		}
		this.qtd_jogadores = qtd_jogadores;
		this.jogadores = new Jogador[qtd_jogadores];

	    for (int i = 0; i < qtd_jogadores; i++) {
	        this.jogadores[i] = new Jogador(new Peao((cores[i])));
	    }
	    
		this.jogadorAtual = 0;
	}
    
    public int proximoJogador() {
        if (isFimDeJogo()) {
            return jogadorAtual; 
        }

        int proximo = (jogadorAtual + 1) % qtd_jogadores;
        
        while (jogadores[proximo].isFaliu()) {
            proximo = (proximo + 1) % qtd_jogadores;

            if (proximo == (jogadorAtual + 1) % qtd_jogadores) {
                jogadorAtual = proximo; 
                return jogadorAtual;
            }
        }
        
        jogadorAtual = proximo;
        return jogadorAtual;
    }
    
    public boolean isFimDeJogo() {
        return getQtdJogadoresAtivos() <= 1;
    }
    
    public Jogador getVencedor() {
        if (isFimDeJogo()) {
            return jogadores[jogadorAtual];
        }
        return null;
    }

    public Jogador getJogadorAtual() {
        return jogadores[jogadorAtual];
    }
    
    public int getQtdJogadoresAtivos() {
        int ativos = 0;
        for (Jogador jogador : jogadores) {
            if (!jogador.isFaliu()) {
                ativos++;
            }
        }
        return ativos;
    }
}