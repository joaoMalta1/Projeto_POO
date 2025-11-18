package view;

import controller.ControlePartida;
import controller.CorPeao;
import controller.CriacaoJogadores;
import controller.Turno;
import model.FacadeModel;

// Facade + Singleton
public class FacadeView {
	private static FacadeView fv = null;

	private FacadeView() {
	}

	public static FacadeView getInstance() {
		if (fv == null) {
			fv = new FacadeView();
		}
		return fv;
	}

	void setQtdJogadores(int qtd) {
		CriacaoJogadores.getInstance().setQtdJogadores(qtd);
	}

	int getJogadorAtual() {
		return FacadeModel.getInstance().getJogadorAtual();
	}

	boolean nomeJaExiste(String nome) {
		return CriacaoJogadores.getInstance().nomeJaExiste(nome);
	}

	boolean corJaExiste(CorPeao cor) {
		return CriacaoJogadores.getInstance().corJaExiste(cor);
	}

	boolean addJogador(int i, String nome, CorPeao cor) {
		return CriacaoJogadores.getInstance().addJogador(i, nome, cor);
	}

	boolean ehUltimoJogador() {
		return CriacaoJogadores.getInstance().ehUltimoJogador();
	}

	void finalizaCriacao() {
		CriacaoJogadores.getInstance().finalizaCriacao();
	}

	int getQtdJogadores() {
		return CriacaoJogadores.getInstance().getQtdJogadores();
	}

	String getNomeJogador(int i) {
		return CriacaoJogadores.getInstance().getNomeJogador(i);
	}

	void proxJogador() {
		CriacaoJogadores.getInstance().proxJogador();
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

	public boolean comprarPropriedadeAtual() {
		int pos = FacadeModel.getInstance().getPosJogadorAtual();
		return FacadeModel.getInstance().comprarPropriedadeAtualJogador(pos);
	}
	
	void botaoFimDeJogoApertado() {
		FacadeModel.getInstance().fimDeJogo();
	}
	
	void reset() {
		FacadeModel.getInstance().reset();
		ControlePartida.getInstance().reset();
	}
}
