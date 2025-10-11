package model;

/* TODO: testes JUnit */

class Campo {
	String nome;
	protected double precoPassagem;
	
	Campo(String nome, double precoPassagem) {
		if(nome == null) {
			throw new IllegalArgumentException("Nome invalido!");
		}
		this.nome = nome;
		this.precoPassagem = precoPassagem;
	}
	
//	função genérica a todas as subclasses a ser implementada por cada uma com suas especificidades
	void caiuNoCampo(Jogador pagador, Banco banco) {}
}

class Prisao extends Campo {
	
	Prisao(String nome, double precoPassagem) {
		super(nome, precoPassagem);
	}
	
//	TODO: função caiuNoCampo() e regras de negócio
	
}



class vaParaPrisao extends Campo {
	
	vaParaPrisao(String nome, double precoPassagem) {
		super(nome, precoPassagem);
	}
	
	void caiuNoCampo(Jogador jogador, Tabuleiro tabuleiro) {
		jogador.getPeao().setPosicao(tabuleiro.getPosicaoPrisao());
		jogador.setNaPrisao(true);
	}
}