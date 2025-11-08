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
    
	/**
	 * Permite ao testador definir valores fixos para os dados (ou null para reativar aleatoriedade)
	 */
	public void setDadosDeTeste(Integer d1, Integer d2) {
		Dados.getInstance().setDadosDeTeste(d1, d2);
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
    
	// Observer relay methods: permite que a view se registre para receber atualizacoes do modelo
	public void addObserver(controller.Observador<controller.PartidaEvent> o) {
		CentralPartida.getInstance().add(o);
	}

	public void removeObserver(controller.Observador<controller.PartidaEvent> o) {
		CentralPartida.getInstance().remove(o);
	}
    
	public int[] getUltimosDados() {
		return CentralPartida.getInstance().getUltimosDados();
	}

	public int getQtdJogadores() {
		return CentralPartida.getInstance().getQtdJogadores();
	}

	public int getPosicaoJogador(int indice) {
		return CentralPartida.getInstance().getPosicaoJogador(indice);
	}

	public controller.CorPeao getCorJogador(int indice) {
		return CentralPartida.getInstance().getCorJogador(indice);
	}
}
