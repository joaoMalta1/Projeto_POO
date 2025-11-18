package view;

/* Classe para definir nomes das diferentes telas
 * Não usamos enum, pois a função que usa estas constantes leva string 
 * como argumento*/
final class Telas {
    static final String INICIO = "INICIO";
    static final String TABULEIRO = "TABULEIRO";
    static final String CRIACAO_JOGADORES = "CRIACAO_JOGADORES";
    static final String QUANTIDADE_JOGADORES = "QUANTIDADE_JOGADORES";
    static final String CARTAS_PROPRIEDADES = "CARTAS_PROPRIEDADES";
    static final String FIM_DE_JOGO = "FIM_DE_JOGO";
    
    private Telas() {}
}