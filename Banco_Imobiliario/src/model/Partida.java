package model;
import java.util.List;


class Partida {
//	TODO:  transformar isso em ArrayList
	private Jogador[] jogadores;
	private int qtd_jogadores;
	private int jogadorAtual;
	private String[] cores = {"azul","vermelho","verde","amarelo","preto","branco"};

	Partida(int qtd_jogadores) {
		this.qtd_jogadores = qtd_jogadores;
		
//		TODO: integrar jogada de dados com movimento dos peões

		for(int i = 0; i < qtd_jogadores; i++)
		{
			this.jogadores[i]=new Jogador(new Peao(cores[i]));
		}
		this.jogadorAtual = 0;
		if(qtd_jogadores < 2 || qtd_jogadores > 6){
			 //erro
		}
	}
}
