package model;

import static org.junit.Assert.*;

import org.junit.Test;

public class CampoTest {
    private Campo campo;

    @Test
    public void testCriacaoCampoComNomeValido() {
        campo = new Campo("São Paulo", 50.0);
        assertEquals("São Paulo", campo.nome);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCriacaoCampoComNomeNuloDeveLancarExcecao() {
        new Campo(null, 50.0);
    }

    @Test
    public void testCriacaoPrecoPassagemCerto() {
        campo = new Campo("Campo Teste", 100.0);
        assertEquals(100.0, campo.precoPassagem, 0.001);
    }

}