package model;

import static org.junit.Assert.*;

import org.junit.Test;

public class DadosTest {

	@Test
	public void testjogar() {
		int[] dados = Dados.jogar();
		
		assertTrue(dados[0] > 0);
		assertTrue(dados[0] < 7);
		assertTrue(dados[1] > 0);
		assertTrue(dados[1] < 7);
		assertTrue(dados.length == 2);
	}

}
