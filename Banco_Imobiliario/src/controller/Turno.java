package controller;

import model.FacadeModel;


// sipa não é singleton...
// Singleton
public class Turno {
	private static Turno ctrl = null;
	
	private Turno() {}
	
	public static Turno getInstance() {
		if(ctrl == null) {
			ctrl = new Turno();
		}
		return ctrl;
	}
	
// TODO: PASSAR ESSA LÓGICA PARA CONTROLEPARTIDA 	
	public int getJogadorAtual() {
		return FacadeModel.getInstance().getJogadorAtual();
	}
		
	public void proxJogador() {
		FacadeModel.getInstance().proxJogador();
	}
	
	public CorPeao getCorJogadorAtual() {
		return FacadeModel.getInstance().getCorJogadorAtual();
	}
	
	public int[] jogarDados() {
		int[] dados = FacadeModel.getInstance().jogarDados();
		FacadeModel.getInstance().andarJogadorAtual(dados);
		return dados;
	}
	
	public int getPosJogadorAtual() {
		return FacadeModel.getInstance().getPosJogadorAtual();
	}
	
	public boolean posicaoAtualEhPropriedade() {
		return FacadeModel.getInstance().ehPropriedade(getPosJogadorAtual());
	}
}
