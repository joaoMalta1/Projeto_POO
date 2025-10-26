package model;

import java.util.Random;

/*
 * Classe que implementa o jogar de dois dados de seis lados
 * Singleton
 * */
class Dados {
	private static Dados ctrl = null;
	
	private Dados() {}
	
	static Dados getInstance() {
		if(ctrl == null) {
			ctrl = new Dados();
		}
		return ctrl;
	}
	
	/*
	 * Retorna um vetor de tamanho 2 com valores de 1 (inclusive) até 6 (inclusive)
	 * */
	int[] jogar() {
		Random random = new Random();
		
		int[] resultado = new int[2];
		int max = 7, min = 1;
		resultado[0] = random.nextInt(max - min) + min;
		resultado[1] = random.nextInt(max - min) + min;
		return resultado;
	}
}