package model;

class SorteOuReves extends Campo{
	SorteOuReves(String nome, double precoPassagem){
		super(nome, 0);
	}
	
	void CaiuNoCampo(Jogador pagador, Banco banco) {
		System.out.println("[DEBUG] Caiu em Sorte ou Revés");	
	}
}