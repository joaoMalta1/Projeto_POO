package controller;

import model.Tabuleiro;

/**
 * Controller simples para expor posições dos jogadores ao view.
 * Tenta obter informações via controller.ControleInformacoesJogo por reflexão.
 */
public class TabuleiroController {
    private final Tabuleiro model;

    public TabuleiroController(Tabuleiro model) {
        this.model = model;
    }

    public Tabuleiro getModel() {
        return model;
    }

    /**
     * Tenta obter um array com a posição de cada jogador consultando
     * controller.ControleInformacoesJogo via reflexão.
     * Retorna array vazio em caso de falha.
     */
    public int[] getPosicoes() {
        try {
            Class<?> ctrlClass = Class.forName("controller.ControleInformacoesJogo");
            Object ctrl = ctrlClass.getMethod("getInstance").invoke(null);

            int qtd = 0;
            try {
                Object qtdObj = ctrlClass.getMethod("getQtdJogadores").invoke(ctrl);
                qtd = (qtdObj instanceof Number) ? ((Number) qtdObj).intValue() : 0;
            } catch (NoSuchMethodException ns) {
                return new int[0];
            }

            if (qtd <= 0) return new int[0];

            int[] pos = new int[qtd];
            for (int i = 0; i < qtd; i++) {
                try {
                    Object pObj = ctrlClass.getMethod("getPosicaoJogador", int.class).invoke(ctrl, i);
                    pos[i] = (pObj instanceof Number) ? ((Number) pObj).intValue() : 0;
                } catch (NoSuchMethodException ns) {
                    try {
                        Object pObj = ctrlClass.getMethod("getPosicao", int.class).invoke(ctrl, i);
                        pos[i] = (pObj instanceof Number) ? ((Number) pObj).intValue() : 0;
                    } catch (Exception e) {
                        pos[i] = 0;
                    }
                } catch (Exception e) {
                    pos[i] = 0;
                }
            }
            return pos;
        } catch (Exception ex) {
            return new int[0];
        }
    }
}