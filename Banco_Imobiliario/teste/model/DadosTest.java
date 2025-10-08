package model;

import static org.junit.Assert.*;

import org.junit.Test;

public class DadosTest {

	@Test
	public void testeIntervalo() {
		int[] dados = Dados.jogar();
		
		assertTrue(dados[0] > 0);
		assertTrue(dados[0] < 7);
		assertTrue(dados[1] > 0);
		assertTrue(dados[1] < 7);
	}
	
	@Test
	public void testeTamanho() {
		int[] dados = Dados.jogar();
		
		assertTrue(dados.length == 2);
	}
	
	/*
	 * Este teste se baseia na conclusão estatística que a chance de não cair 6 ou 1 no dado 
	 * em 500 iterações é tão baixa que pode ser considerada 0
	 * */
	@Test
	public void testeExtremos() {
		boolean temExtremos = false;
		
		for(int i = 0; i < 500; i++) {
			int[] dados = Dados.jogar();
			if(dados[0] == 6 || dados[1] == 6) {
				temExtremos = true;
				break;
			}
		}
		
		for(int i = 0; i < 500; i++){
			int[] dados = Dados.jogar();
			if(dados[0] == 1 || dados[1] == 1) {
				temExtremos = true;
				break;
			}
		}
		
		assertTrue(temExtremos);
	}

}
