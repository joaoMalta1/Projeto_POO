package model;

import java.util.ArrayList;

import controller.CorPeao;

class CentralPartida {
	private static CentralPartida ctrl = null;
	private static ArrayList<Jogador> jogadores = null;
	private int jogadorAtual = 0; // no turno
	private static Tabuleiro tabuleiro = null;
	private static Banco banco = null;
	
	private CentralPartida() {}
	
	static CentralPartida getInstance() {
		if(ctrl == null) {
			ctrl = new CentralPartida();
			banco = new Banco();
			tabuleiro = new Tabuleiro(banco);
			jogadores = new ArrayList<Jogador>();
		}
		return ctrl;
	}
	
	void criaJogadores(ArrayList<String> nomeJogadores, ArrayList<CorPeao> corJogadores) {
		for(int i = 0; i < nomeJogadores.size(); i++) {
			jogadores.add(new Jogador(nomeJogadores.get(i), new Peao(corJogadores.get(i))));
		}
	}
	
	boolean ehPropriedade(int posicao) {
		return tabuleiro.ehPropriedade(posicao);
	}
	
//	TODO: TRANSFERIR PRA CÁ LÓGICA DE JOGADOR ATUAL, TURNO, ETC
	int getJogadorAtual() {
		return jogadorAtual;
	}
	
	void proxJogador() {
		jogadorAtual = (++jogadorAtual)%jogadores.size();
	}
	
	CorPeao getCorJogadorAtual() {
		return jogadores.get(jogadorAtual).getPeao().getCor();
	}
	
	int getPosJogadorAtual() {
		return jogadores.get(jogadorAtual).getPeao().getPosicao();
	}
	
//	retorna nova posição
	int andarJogadorAtual(int[] dados) {
		if(dados.length != 2) {
			throw new IllegalArgumentException("Dados têm tamanho errado");
		}
//		 TODO: INTEGRAR COM FUNÇÃO NOVAPOSIÇÃO 
		jogadores.get(jogadorAtual).getPeao().setPosicao(dados[0] + dados[1]);
		return getPosJogadorAtual();
	}
}
