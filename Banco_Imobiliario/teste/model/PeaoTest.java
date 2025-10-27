package model;

import static org.junit.Assert.*;
import org.junit.Test;
import controller.CorPeao;

public class PeaoTest {

    //Testa se o peão é inicializado corretamente com a cor e a posição padrão (0).
    @Test
    public void testeInicializacao() {

        Peao peao = new Peao(CorPeao.VERMELHA);
        assertEquals(CorPeao.VERMELHA, peao.getCor());
        assertEquals(0, peao.getPosicao());
    }

    //Testa o método mover() do peão, verificando se a posição é
    @Test
    public void testeMoverPeao() {
        Peao peao = new Peao(CorPeao.AZUL);
        int resultado1 = peao.mover(5);
        assertEquals(1, resultado1);
        assertEquals(5, peao.getPosicao());

        int resultado2 = peao.mover(3);
        assertEquals(1, resultado2);
        assertEquals(8, peao.getPosicao());
    }

    //Testa o método setPosicao() para garantir que a posição do peão
    @Test
    public void testeDefinirPosicao() {
        Peao peao = new Peao(CorPeao.CINZA);
        peao.setPosicao(10);
        assertEquals(10, peao.getPosicao());
    }

    //Testa o método mover() com um valor negativo para garantir que a posição não seja alterada.
    @Test
    public void testeMoverNegativo() {
        Peao peao = new Peao(CorPeao.LARANJA);
        int resultado = peao.mover(-2); // deve ser ignorado
        assertEquals(0, resultado);     // retorno esperado
    }
}
