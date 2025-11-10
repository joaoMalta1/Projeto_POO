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
            // Ao finalizar a movimentação, encerra o turno atual e passa para o próximo
            // (poderíamos adicionar regras futuras aqui, como jogadas extras por duplos)
            FacadeModel.getInstance().proxJogador();
        }
        return dados;
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

    public controller.CorPeao getCorJogadorAtual() {
        return FacadeModel.getInstance().getCorJogadorAtual();
    }
}
