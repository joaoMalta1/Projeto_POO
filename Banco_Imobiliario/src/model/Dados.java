package model;

import java.util.Random;

/*
 * Classe que implementa o jogar de dois dados de seis lados
 * Não deve ser instanciada
 * Apenas uma função de jogar dados
 * */
class Dados {
	/*
	 * Retorna um vetor de tamanho 2 com valores de 1 (inclusive) até 6 (inclusive)
	 * */
	static int[] jogar() {
		Random random = new Random();
		
		int[] resultado = new int[2];
		
		resultado[0] = random.nextInt(1, 7);
		resultado[1] = random.nextInt(1, 7);
		
		return resultado;
	}
}