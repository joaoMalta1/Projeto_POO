package controller;

import java.util.ArrayList;

import model.FacadeModel;

// Singleton
class Partida {
	private static Partida ctrl = null;
	private ArrayList<String> nomeJogadores = null;
	private ArrayList<CorPeao> corJogadores = null;
	
	private Partida() {}
	
	static Partida getInstance() {
		if(ctrl == null) {
			ctrl = new Partida();}
		return ctrl;
	}
	
	void setJogadores(ArrayList<String> nomes, ArrayList<CorPeao> cores) {
		nomeJogadores = new ArrayList<String>(nomes);
		corJogadores = new ArrayList<CorPeao>(cores);
		FacadeModel.getInstance().criaJogadores(nomes, cores);
	}
	
	ArrayList<String> getNomesJogadores(){
		return nomeJogadores;
	}
	
	ArrayList<CorPeao> getCorJogadores(){
		return corJogadores;
	}
}
