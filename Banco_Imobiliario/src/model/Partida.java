package model;
import java.util.List;


class Partida {
	private Jogador[] jogadores;
	private int qtd_jogadores;
	private int jogadorAtual;
	private String[] cores = {"azul","vermelho","verde","amarelo","preto","branco"};

	public Partida(int qtd_jogadores) {
		this.qtd_jogadores = qtd_jogadores;

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
