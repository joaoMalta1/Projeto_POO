package view;

import controller.CorPeao;
import controller.CriacaoJogadores;
import controller.Turno;
import model.FacadeModel;

// Facade + Singleton
public class FacadeView {
	private static FacadeView fv = null;
	private static CriacaoJogadores ctrl;

	private FacadeView() {
	}

	public static FacadeView getInstance() {
		if (fv == null) {
			fv = new FacadeView();
			ctrl = CriacaoJogadores.getInstance();
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

	int[] jogarDados() {
		return Turno.getInstance().jogarDados();
	}

	CorPeao getCorJogadorAtual() {
		return Turno.getInstance().getCorJogadorAtual();
	}

	boolean posicaoAtualEhPropriedade() {
		return FacadeModel.getInstance().ehPropriedade(FacadeModel.getInstance().getPosJogadorAtual());
	}

	boolean ehPropriedade(int posicao) {
		return FacadeModel.getInstance().ehPropriedade(posicao);
	}

	int getPosJogadorAtual() {
		return Turno.getInstance().getPosJogadorAtual();
	}

	public boolean propriedadeDisponivelAtual() {
		return FacadeModel.getInstance().propriedadeDisponivel(getPosJogadorAtual());

	}

	// public int getPrecoPosicaoAtual() {
	// 	return FacadeModel.getInstance().getPrecoPropriedade(getPosJogadorAtual());
	// }

	// public boolean comprarPropriedadeAtual() {
	// 	return FacadeModel.getInstance().comprarPropriedadeAtualJogador(getPosJogadorAtual());
	// }
}
