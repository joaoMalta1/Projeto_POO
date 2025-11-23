package model;

class SorteOuReves extends Campo{
	SorteOuReves(String nome, double precoPassagem){
		super(nome, 0);
	}
	
//	retorna a posicao do jogador apos cair no campo
	int CaiuNoCampo(Jogador jogador, Banco banco, Baralho baralho) {
		return baralho.comprarCarta(jogador, banco);	
	}
}