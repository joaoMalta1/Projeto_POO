package model;

import static org.junit.Assert.*;
import org.junit.Test;

public class PeaoTest {

    @Test
    public void testeInicializacao() {
        Peao peao = new Peao(Peao.CorPeao.VERMELHA);
        assertEquals(Peao.CorPeao.VERMELHA, peao.getCor());
        assertEquals(0, peao.getPosicao());
    }

    @Test
    public void testeMoverPeao() {
        Peao peao = new Peao(Peao.CorPeao.AZUL);
        int resultado1 = peao.mover(5);
        assertEquals(1, resultado1);
        assertEquals(5, peao.getPosicao());

        int resultado2 = peao.mover(3);
        assertEquals(1, resultado2);
        assertEquals(8, peao.getPosicao());
    }

    @Test
    public void testeDefinirPosicao() {
        Peao peao = new Peao(Peao.CorPeao.CINZA);
        peao.setPosicao(10);
        assertEquals(10, peao.getPosicao());
    }

    @Test
    public void testeMoverNegativo() {
        Peao peao = new Peao(Peao.CorPeao.LARANJA);
        int resultado = peao.mover(-2); // deve ser ignorado
        assertEquals(0, resultado);     // retorno esperado
    }
}
