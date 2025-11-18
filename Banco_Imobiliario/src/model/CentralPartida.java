package model;

import java.util.ArrayList;

import controller.CorPeao;
import controller.Observador;
import controller.Observado;
import controller.PartidaEvent;

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

	// TODO: TRANSFERIR PRA CÁ LÓGICA DE JOGADOR ATUAL, TURNO, ETC
	int getJogadorAtual() {
		return jogadorAtual;
	}

	void proxJogador() {
		jogadorAtual = (++jogadorAtual) % jogadores.size();
		notifyObservers(PartidaEvent.nextPlayer());
		
//		DEBUG
		for(Jogador j : jogadores) {
			System.out.println("Nome: " + j.getNome() + "\nSaldo: " + String.format("%.2f\n\n", j.getSaldo()));
		}
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

	// retorna nova posição (0-based)
	int andarJogadorAtual(int[] dados) {
		if (dados == null || dados.length != 2) {
			throw new IllegalArgumentException("Dados têm tamanho errado");
		}
		this.ultimoDados = new int[] { dados[0], dados[1] };
		int soma = dados[0] + dados[1];
		int posAtual = jogadores.get(jogadorAtual).getPeao().getPosicao(); // 0-based
		System.err.println("posi atual: " + posAtual + ", soma dados: " + soma);

		int novaPos = (posAtual + soma) % 40; // mantém em 0..39
		jogadores.get(jogadorAtual).getPeao().setPosicao(novaPos);
		notifyObservers(PartidaEvent.diceRolled(new int[] { dados[0], dados[1] }));
		notifyObservers(PartidaEvent.move(novaPos, new int[] { dados[0], dados[1] }));
		if (ehPropriedade(novaPos)) {
			notifyObservers(PartidaEvent.propertyLanded(novaPos));
		}
		return novaPos;
	}

	boolean propriedadeDisponivel(int posicao) {
		if (!tabuleiro.ehPropriedade(posicao))
			return false;
		Propriedade prop = (Propriedade) tabuleiro.getCampo(posicao);
		return prop.dono == null;
	}

	/**
	 * Retorna o preço de compra da propriedade (ou -1 se não for propriedade).
	 */
	float getPrecoPropriedade(int posicao) {
		if (!tabuleiro.ehPropriedade(posicao))
			return -1;
		Propriedade prop = (Propriedade) tabuleiro.getCampo(posicao);
		// adaptar para o nome do campo/ método de preço de compra que existir
		return (float) prop.precoCompra; // ou prop.getPreco()
	}

	/**
	 * Faz a compra da propriedade na posição para o jogador atual.
	 * Debita o saldo, atribui o dono e notifica observadores.
	 * Retorna true se a compra foi bem sucedida.
	 */
	// aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
	boolean comprarPropriedadeAtualJogador(int posicao) 
	{
		if (!propriedadeDisponivel(posicao))
			return false;

		Jogador j = jogadores.get(jogadorAtual);
		Propriedade prop = (Propriedade) tabuleiro.getCampo(posicao);
		prop.comprar(j, banco);
		j.adicionarPropriedade(prop);
		System.out.println("nome da propriedade: " + prop.nome);
		System.out.println("Jogador " + j.getPeao().getCor() + " comprou propriedade na pos " + posicao + "saldo restante: " + j.getSaldo());
		System.out.println("propriedades do jogador " + j.getPropriedades().get(0).nome);
		notifyObservers(PartidaEvent.purchased_property());
		return true;
	}
	
//	retorna jogador mais rico de acordo com a formatação para string da função getNomeCorString da classe Jogador
	int jogadorMaisRico() {
		int maisRico = 0;
		for(int i = 0; i < jogadores.size(); i++) {
			Jogador j  = jogadores.get(i);
			if(j.getSaldo() > jogadores.get(maisRico).getSaldo()) {
				maisRico = i;
			}
		}
		return maisRico;
	}
	
//	quando um jogador ganha a partida, ele automaticamente vira o jogador atual
	void fimDeJogo() {
		jogadorAtual = jogadorMaisRico();
		System.out.println(jogadorAtual);
		notifyObservers(PartidaEvent.fimDeJogo());
	}
	
//	checa se jogo chegou ao fim (se todos os jogadores faliram menos um, não checa para clique no botão de fim)
	void checaFimJogo() {
		int qtd_falidos = 0;
		for(Jogador j : jogadores) {
			if(j.isFaliu()) {
				qtd_falidos++;
			}
		}
		if(qtd_falidos + 1 == jogadores.size()) {
			fimDeJogo();
		}
		if(qtd_falidos >= jogadores.size()) {
			throw new IllegalStateException("Algo deu errado na checagem de fim de jogo");
		}
	}
	
	public void reset() {
//	    banco = new Banco();
//	    tabuleiro = new Tabuleiro(banco);
//	    jogadores.clear();
//	    jogadorAtual = 0;
//	    ultimoDados = null;
	    // Mantém os observers para reuse entre partidas
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
}
