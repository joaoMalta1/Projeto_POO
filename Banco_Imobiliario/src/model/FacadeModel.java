package model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import controller.CorPeao;

public class FacadeModel {
	private static FacadeModel fm = null;

	private FacadeModel() {
	}

	public static FacadeModel getInstance() {
		if (fm == null) {
			fm = new FacadeModel();
		}
		return fm;
	}

	// Método existente modificado para incluir verificação de duplas
	public int[] jogarDados() {
		int[] dados = Dados.getInstance().jogar();

		// CentralPartida.getInstance().jogadorAtualJogouDados(dados);

		return dados;
	}

	/**
	 * permite definir valores fixos para os dados (ou null para reativar
	 * aleatoriedade)
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

	public void andarJogadorAtual(int[] dados) {
		if (dados.length != 2) {
			throw new IllegalArgumentException("Dados têm tamanho errado");
		}
		CentralPartida.getInstance().andarJogadorAtual(dados);
	}

	public void addObserver(controller.Observador<controller.PartidaEvent> o) {
		CentralPartida.getInstance().add(o);
	}

	public void removeObserver(controller.Observador<controller.PartidaEvent> o) {
		CentralPartida.getInstance().remove(o);
	}

	public int getQtdJogadores() {
		return CentralPartida.getInstance().getQtdJogadores();
	}

	public int getPosicaoJogador(int indice) {
		return CentralPartida.getInstance().getPosicaoJogador(indice);
	}

	public boolean propriedadeDisponivelAtual() {
		return CentralPartida.getInstance().propriedadeDisponivel(getPosJogadorAtual());
	}

	public controller.CorPeao getCorJogador(int indice) {
		return CentralPartida.getInstance().getCorJogador(indice);
	}

	public boolean comprarPropriedadeAtualJogador() {
		return CentralPartida.getInstance().comprarPropriedadeAtualJogador();
	}

	public boolean venderPropriedadeAtualJogador(int pos) {
		return CentralPartida.getInstance().venderPropriedadeJogadorAtual(pos);
	}


	public boolean propriedadeDisponivel(int posicao) {
		return CentralPartida.getInstance().propriedadeDisponivel(posicao);
	}

	public void fimDeJogo() {
		CentralPartida.getInstance().fimDeJogo();
	}

	public void reset() {
		CentralPartida.getInstance().reset();
	}

	public boolean atualPodeComprarCasa() {
		return CentralPartida.getInstance().atualPodeComprarCasa();
	}

	public boolean atualPodeComprarHotel() {
		return CentralPartida.getInstance().atualPodeComprarHotel();
	}

	public boolean atualComprarCasa() {
		return CentralPartida.getInstance().atualComprarCasa();
	}

	public boolean atualComprarHotel() {
		return CentralPartida.getInstance().atualComprarHotel();
	}

	public String getNomeJogadorAtual() {
		return CentralPartida.getInstance().getNomeJogadorAtual();
	}

	public double getSaldoJogadorAtual() {
		return CentralPartida.getInstance().getSaldoJogadorAtual();
	}

	public ArrayList<String> getNomesPropriedadesJogadorAtual() {
		return CentralPartida.getInstance().getNomesPropriedadesJogadorAtual();
	}

	public String getNomeDoCampo(Number id) {
		int index = id.intValue();
		List<String> nomes = CentralPartida.getInstance().getNomesDosCampos();

		if (index < 0 || index >= nomes.size()) {
			throw new IllegalArgumentException("ID inválido: " + id);
		}

		return nomes.get(index);
	}

	public ArrayList<Integer> getIndicesPropriedadesJogadorAtual() {
		List<String> todosOsCampos = CentralPartida.getInstance().getNomesDosCampos();
		ArrayList<String> nomesPropriedadesJogadorAtual = 
				CentralPartida.getInstance().getNomesPropriedadesJogadorAtual();

		ArrayList<Integer> ids = new ArrayList<>();

		for (String nome : nomesPropriedadesJogadorAtual) {
			int index = todosOsCampos.indexOf(nome);
			if (index != -1) {
				ids.add(index);
			}
		}

		return ids;
	}
	
	public boolean salvarJogo(String caminho) {
		try {
			Salvamento.getInstance().salvarJogo(caminho);
			return true;
			} 
		catch (IOException e){
			System.err.println("[DEBUG] ERRO NO SALVAMENTO DO JOGO: " + e.getMessage());
			return false;
		}
	}
}
