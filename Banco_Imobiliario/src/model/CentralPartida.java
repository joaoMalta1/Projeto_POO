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
    
	private CentralPartida() {}
    
	static CentralPartida getInstance() {
		if(ctrl == null) {
			ctrl = new CentralPartida();
			banco = new Banco();
			tabuleiro = new Tabuleiro(banco);
			jogadores = new ArrayList<Jogador>();
			ctrl.observers = new ArrayList<>();
		}
		return ctrl;
	}
    
	void criaJogadores(ArrayList<String> nomeJogadores, ArrayList<CorPeao> corJogadores) {
		for(int i = 0; i < nomeJogadores.size(); i++) {
			jogadores.add(new Jogador(nomeJogadores.get(i), new Peao(corJogadores.get(i))));
		}
		// notify that players were created
		notifyObservers(PartidaEvent.info("players_created"));
	}
    
	boolean ehPropriedade(int posicao) {
		return tabuleiro.ehPropriedade(posicao);
	}
    
//	TODO: TRANSFERIR PRA CÁ LÓGICA DE JOGADOR ATUAL, TURNO, ETC
	int getJogadorAtual() {
		return jogadorAtual;
	}
    
	void proxJogador() {
		jogadorAtual = (++jogadorAtual)%jogadores.size();
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
    

	// retorna nova posição (0-based)
	int andarJogadorAtual(int[] dados) {
		if (dados == null || dados.length != 2) {
			throw new IllegalArgumentException("Dados têm tamanho errado");
		}
		this.ultimoDados = new int[] { dados[0], dados[1] };
		int soma = dados[0] + dados[1];
		int posAtual = jogadores.get(jogadorAtual).getPeao().getPosicao(); // 0-based
		int novaPos = (posAtual + soma) % 40; // mantém em 0..39
		jogadores.get(jogadorAtual).getPeao().setPosicao(novaPos);
		// notify dice rolled and move (positions are 0-based)
		notifyObservers(PartidaEvent.diceRolled(new int[] { dados[0], dados[1] }));
		notifyObservers(PartidaEvent.move(novaPos, new int[] { dados[0], dados[1] }));
		if (ehPropriedade(novaPos)) {
			notifyObservers(PartidaEvent.propertyLanded(novaPos));
		}
		return novaPos;
	}

	// Observado interface implementation
	@Override
	public void add(Observador<PartidaEvent> o) {
		if(o == null) return;
		if(observers == null) observers = new ArrayList<>();
		if(!observers.contains(o)) observers.add(o);
	}

	@Override
	public void remove(Observador<PartidaEvent> o) {
		if(o == null || observers == null) return;
		observers.remove(o);
	}

	@Override
	public void notifyObservers(PartidaEvent event) {
		if(observers == null) return;
		for(Observador<PartidaEvent> o : new ArrayList<>(observers)) {
			try {
				o.notify(event);
			} catch(Exception e) {
				System.err.println("Erro notificando observer: " + e.getMessage());
			}
		}
	}

	@Override
	public PartidaEvent get(int index) {
		// compat API: return a generic info event with minimal data
		return PartidaEvent.info("central_partida");
	}

	// retorna os ultimos dados jogados (ou {0,0} se nao houve jogada ainda)
	int[] getUltimosDados() {
		if(ultimoDados == null) return new int[] {0,0};
		return new int[] { ultimoDados[0], ultimoDados[1] };
	}
}
