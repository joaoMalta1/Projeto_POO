package model;

import java.util.ArrayList;

import controller.CorPeao;
import controller.Observador;
import controller.Observado;
import controller.PartidaEvent;
import controller.ResultadoTransacao;

public class CentralPartida implements Observado<PartidaEvent> {
	private static CentralPartida ctrl = null;
	private static ArrayList<Jogador> jogadores = null;
	private int jogadorAtual = 0; // no turno
	private static Tabuleiro tabuleiro = null;
	private static Banco banco = null;
	private ArrayList<Observador<PartidaEvent>> observers = null;
	private int[] ultimoDados = null;

	private CentralPartida() {
		banco = new Banco();
		tabuleiro = new Tabuleiro(banco);
		jogadores = new ArrayList<Jogador>();
		observers = new ArrayList<>();
	}

	static CentralPartida getInstance() {
		if (ctrl == null) {
			ctrl = new CentralPartida();
		}
		return ctrl;
	}

	void criaJogadores(ArrayList<String> nomeJogadores, ArrayList<CorPeao> corJogadores) {
		for (int i = 0; i < nomeJogadores.size(); i++) {
			jogadores.add(new Jogador(nomeJogadores.get(i), new Peao(corJogadores.get(i))));
		}
		notifyObservers(PartidaEvent.info("players_created"));
	}

	boolean ehPropriedade(int posicao) {
		return tabuleiro.ehPropriedade(posicao);
	}

	int getJogadorAtual() {
		return jogadorAtual;
	}

	void proxJogador() {
		jogadorAtual = (++jogadorAtual) % jogadores.size();
		for (boolean atualFalido = jogadores.get(jogadorAtual).isFaliu(); atualFalido; atualFalido = jogadores
				.get(jogadorAtual).isFaliu()) {
			jogadorAtual = (++jogadorAtual) % jogadores.size();
		}
		notifyObservers(PartidaEvent.nextPlayer());
	}

	CorPeao getCorJogadorAtual() {
		return jogadores.get(jogadorAtual).getPeao().getCor();
	}

	int getPosJogadorAtual() {
		return jogadores.get(jogadorAtual).getPeao().getPosicao();
	}

	// numero de jogadores na partida
	int getQtdJogadores() {
		return jogadores.size();
	}

	// posicao (0-based) do jogador pelo indice
	int getPosicaoJogador(int indice) {
		return jogadores.get(indice).getPeao().getPosicao();
	}

	// cor do peao do jogador pelo indice
	CorPeao getCorJogador(int indice) {
		return jogadores.get(indice).getPeao().getCor();
	}

	int getTamanhoTabuleiro() {
		return tabuleiro.getTamanho();
	}

	Tabuleiro getTabuleiro() {
		return tabuleiro;
	}

	int andarJogadorAtual(int[] dados) {
		if (dados == null || dados.length != 2) {
			throw new IllegalArgumentException("Dados têm tamanho errado");
		}

		this.ultimoDados = new int[] { dados[0], dados[1] };
		int posAntes = jogadores.get(jogadorAtual).getPeao().getPosicao();

		jogadorAtualJogouDados(dados);

		int novaPos = tabuleiro.moverJogador(jogadores.get(jogadorAtual), posAntes, dados);

		novaPos = jogadores.get(jogadorAtual).getPeao().getPosicao();

		System.out.println("posAntes: "+posAntes+"\n");

		System.out.println("novaPos: "+novaPos+"\n \n");
		notifyObservers(PartidaEvent.diceRolled(new int[] { dados[0], dados[1] }));
		notifyObservers(PartidaEvent.move(novaPos, new int[] { dados[0], dados[1] }));

		if (ehPropriedade(novaPos)) {
			notifyObservers(PartidaEvent.propertyLanded(novaPos));
		}
		return novaPos;
	}

	void jogadorAtualJogouDados(int[] dados) {
		if (dados.length != 2) {
			throw new IllegalArgumentException("Tamanho dos dados jogados inválido");
		}

		jogadores.get(jogadorAtual).jogouDados(tabuleiro.getPosicaoPrisao(), dados);
	}

	boolean propriedadeDisponivel(int posicao) {
		if (!tabuleiro.ehPropriedade(posicao))
			return false;
		Propriedade prop = (Propriedade) tabuleiro.getCampo(posicao);
		return prop.dono == null;
	}

	/**
	 * Faz a compra da propriedade na posição para o jogador atual.
	 * Debita o saldo, atribui o dono e notifica observadores.
	 * Retorna true se a compra foi bem sucedida.
	 */
	boolean comprarPropriedadeAtualJogador() {
		Jogador j = jogadores.get(jogadorAtual);
		int posicao = j.getPeao().getPosicao();

		if (!propriedadeDisponivel(posicao))
			return false;

		Propriedade prop = (Propriedade) tabuleiro.getCampo(posicao);

		ResultadoTransacao resultado = prop.comprar(j, banco);

		if (resultado != ResultadoTransacao.SUCESSO) {
			System.out.println("Compra não efetuada por motivos de " + ResultadoTransacao.SUCESSO.toString());
			return false;
		}

		notifyObservers(PartidaEvent.purchased_property());
		return true;
	}

	// retorna jogador mais rico de acordo com a formatação para string da função
	// getNomeCorString da classe Jogador
	int jogadorMaisRico() {
		int maisRico = 0;
		for (int i = 0; i < jogadores.size(); i++) {
			Jogador j = jogadores.get(i);
			if (j.getSaldo() > jogadores.get(maisRico).getSaldo()) {
				maisRico = i;
			}
		}
		return maisRico;
	}

	// quando um jogador ganha a partida, ele automaticamente vira o jogador atual
	void fimDeJogo() {
		jogadorAtual = jogadorMaisRico();
		System.out.println(jogadorAtual);
		notifyObservers(PartidaEvent.fimDeJogo());
	}

	// checa se jogo chegou ao fim (se todos os jogadores faliram menos um, não
	// checa para clique no botão de fim)
	void checaFimJogo() {
		int qtd_falidos = 0;
		for (Jogador j : jogadores) {
			if (j.isFaliu()) {
				qtd_falidos++;
			}
		}
		if (qtd_falidos + 1 == jogadores.size()) {
			fimDeJogo();
		}
		if (qtd_falidos >= jogadores.size()) {
			throw new IllegalStateException("Algo deu errado na checagem de fim de jogo");
		}
	}

	public void reset() {
		ctrl = null;
	}

	// Observado interface implementation
	@Override
	public void add(Observador<PartidaEvent> o) {
		if (o == null)
			return;
		if (observers == null)
			observers = new ArrayList<>();
		if (!observers.contains(o))
			observers.add(o);
	}

	@Override
	public void remove(Observador<PartidaEvent> o) {
		if (o == null || observers == null)
			return;
		observers.remove(o);
	}

	@Override
	public void notifyObservers(PartidaEvent event) {
		if (observers == null)
			return;
		for (Observador<PartidaEvent> o : new ArrayList<>(observers)) {
			try {
				o.notify(event);
			} catch (Exception e) {
				System.err.println("Erro notificando observer: " + e.getMessage());
			}
		}
	}

	@Override
	public PartidaEvent get(int index) {
		return PartidaEvent.info("central_partida");
	}

	// retorna os ultimos dados jogados (ou {0,0} se nao houve jogada ainda)
	int[] getUltimosDados() {
		if (ultimoDados == null)
			return new int[] { 0, 0 };
		return new int[] { ultimoDados[0], ultimoDados[1] };
	}

	boolean atualPodeComprarCasa() {
		int pos = jogadores.get(jogadorAtual).getPeao().getPosicao();

		Campo campo = tabuleiro.getCampo(pos);

		if (campo instanceof Terreno) {
			Terreno prop = (Terreno) campo;
			return prop.podeComprarCasa(jogadores.get(jogadorAtual));
		}

		return false;
	}

	boolean atualPodeComprarHotel() {
		int pos = jogadores.get(jogadorAtual).getPeao().getPosicao();

		Campo campo = tabuleiro.getCampo(pos);

		if (campo instanceof Terreno) {
			Terreno prop = (Terreno) campo;
			return prop.podeComprarHotel(jogadores.get(jogadorAtual));
		}

		return false;
	}

	boolean atualComprarCasa() {
		int pos = jogadores.get(jogadorAtual).getPeao().getPosicao();

		Campo campo = tabuleiro.getCampo(pos);

		if (campo instanceof Terreno) {
			Terreno prop = (Terreno) campo;
			ResultadoTransacao resultado = prop.construirCasa(jogadores.get(jogadorAtual), banco);
			if (resultado == ResultadoTransacao.SUCESSO) {
				return true;
			}
		}
		return false;
	}

	boolean atualComprarHotel() {
		int pos = jogadores.get(jogadorAtual).getPeao().getPosicao();

		Campo campo = tabuleiro.getCampo(pos);

		if (campo instanceof Terreno) {
			Terreno prop = (Terreno) campo;
			ResultadoTransacao resultado = prop.construirHotel(jogadores.get(jogadorAtual), banco);
			if (resultado == ResultadoTransacao.SUCESSO) {
				return true;
			}
		}
		return false;
	}

	public String getNomeJogadorAtual() {
		return jogadores.get(jogadorAtual).getNome();
	}

	public double getSaldoJogadorAtual() {
		return jogadores.get(jogadorAtual).getSaldo();
	}

	public ArrayList<String> getNomesPropriedadesJogadorAtual() {
		ArrayList<String> nomes = new ArrayList<>();
		for (Propriedade p : jogadores.get(jogadorAtual).getPropriedades()) {
			nomes.add(p.nome);
		}
		return nomes;
	}
}
