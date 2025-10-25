package view;

import controller.CorPeao;
import controller.ControleCriacaoJogadores;

// Facade + Singleton
public class FacadeView {
	private static FacadeView fv = null;
	private static ControleCriacaoJogadores ctrl;
	
	private FacadeView() {}
	
	public static FacadeView getInstance() {
		if(fv == null) {
			fv = new FacadeView();
			ctrl = ControleCriacaoJogadores.getInstance();
		}
		return fv;
	}
	
	void setQtdJogadores(int qtd) {
		ctrl.setQtdJogadores(qtd);
	}
	
	int getJogadorAtual() {
		return ctrl.getJogadorAtual();
	}
	
	boolean nomeJaExiste(String nome) {
		return ctrl.nomeJaExiste(nome);
	}
	
	boolean corJaExiste(CorPeao cor) {
		return ctrl.corJaExiste(cor);
	}
	
	boolean addJogador(int i, String nome, CorPeao cor) {
		return ctrl.addJogador(i, nome, cor);
	}
	
	boolean ehUltimoJogador() {
		return ctrl.ehUltimoJogador();
	}
	
	void finalizaCriacao() {
		ctrl.finalizaCriacao();
	}
	
	int getQtdJogadores() {
		return ctrl.getQtdJogadores();
	}
	
	String getNomeJogador(int i) {
		return ctrl.getNomeJogador(i);
	}
	
	void proxJogador() {
		ctrl.proxJogador();
	}
}
