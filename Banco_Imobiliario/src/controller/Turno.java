package controller;

import java.util.ArrayList;

import model.FacadeModel;

// Singleton
public class Turno {
	private static Turno ctrl = null;
	private static ArrayList<String> nomeJogadores = null;
	private static ArrayList<CorPeao> corJogadores = null;
	private int jogadorAtual = 0;
	private static Partida partida = null;
	
	private Turno() {}
	
	public static Turno getInstance() {
		if(ctrl == null) {
			ctrl = new Turno();
			partida = Partida.getInstance();
			setJogadores(partida.getNomesJogadores(), partida.getCorJogadores());
		}
		return ctrl;
	}
	
	static void setJogadores(ArrayList<String> nomes, ArrayList<CorPeao> cores) {
		nomeJogadores = new ArrayList<String>(nomes);
		corJogadores = new ArrayList<CorPeao>(cores);
	}
	
	public int getJogadorAtual() {
		return jogadorAtual;
	}
		
	public void proxJogador() {
		jogadorAtual = (++jogadorAtual)%nomeJogadores.size();
	}
	
	public CorPeao getCorAtual() {
		return corJogadores.get(jogadorAtual);
	}
	
	public int[] jogarDados() {
		return FacadeModel.getInstance().jogarDados();
	}
}
