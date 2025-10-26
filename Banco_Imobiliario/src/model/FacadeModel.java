package model;

import java.util.ArrayList;

import controller.CorPeao;

public class FacadeModel {
	private static FacadeModel fm = null;
	
	private FacadeModel() {}
	
	public static FacadeModel getInstance() {
		if(fm == null) {
			fm = new FacadeModel();
		}
		return fm;
	}
	
	public int[] jogarDados() {
		return Dados.getInstance().jogar();
	}
	
	public boolean ehPropriedade(int posicao) {
		return CentralPartida.getInstance().ehPropriedade(posicao);
	}
	
	public void criaJogadores(ArrayList<String> nomeJogadores, ArrayList<CorPeao> corJogadores) {
		CentralPartida.getInstance().criaJogadores(nomeJogadores, corJogadores);
	}
	
	public int getJogadorAtual() {
		return CentralPartida.getInstance().getJogadorAtual();
	}
	
	public void proxJogador() {
		CentralPartida.getInstance().proxJogador();
	}
	
	public CorPeao getCorJogadorAtual() {
		return CentralPartida.getInstance().getCorJogadorAtual();
	}
	
	public int getPosJogadorAtual() {
		return CentralPartida.getInstance().getPosJogadorAtual();
	}
	
	public int andarJogadorAtual(int[] dados) {
		if(dados.length != 2) {
			throw new IllegalArgumentException("Dados têm tamanho errado");
		}
		return CentralPartida.getInstance().andarJogadorAtual(dados);
	}
}
