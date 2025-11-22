package controller;

import model.FacadeModel;

// Singleton controller that centralizes the sequence of events of a partida
public class ControlePartida {
    private static ControlePartida instancia = null;

    private ControlePartida() {}

    public static ControlePartida getInstance() {
        if (instancia == null) {
            instancia = new ControlePartida();
        }
        return instancia;
    }

    public int[] jogarDadosEAndar() {
    	int[] dados = FacadeModel.getInstance().jogarDados(); 
        if (dados != null && dados.length == 2) {
            FacadeModel.getInstance().andarJogadorAtual(dados);
        }
        return dados;
    }
    
    public boolean comprarPropriedadeAtualJogador() {
    	return FacadeModel.getInstance().comprarPropriedadeAtualJogador();
    }
    

    public void proxJogador() {
        FacadeModel.getInstance().proxJogador();
    }

    public int getJogadorAtual() {
        return FacadeModel.getInstance().getJogadorAtual();
    }

    public int getPosJogadorAtual() {
        return FacadeModel.getInstance().getPosJogadorAtual();
    }

    public boolean posicaoAtualEhPropriedade() {
        return FacadeModel.getInstance().ehPropriedade(getPosJogadorAtual());
    }

    public CorPeao getCorJogadorAtual() {
        return FacadeModel.getInstance().getCorJogadorAtual();
    }
    
    public void reset() {
    	Partida.getInstance().reset();
    	CriacaoJogadores.getInstance().reset();
    }

	public boolean atualPodeComprarCasa() {
		return FacadeModel.getInstance().atualPodeComprarCasa();
	}
	
	public boolean atualPodeComprarHotel() {
		return FacadeModel.getInstance().atualPodeComprarHotel();
	}

	public boolean atualComprarCasa() {
		return FacadeModel.getInstance().atualComprarCasa();
	}
	
	public boolean atualComprarHotel() {
		return FacadeModel.getInstance().atualComprarHotel();
	}
}
