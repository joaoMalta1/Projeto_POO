package model;

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
	
	void caiuNoCampo(Jogador pagador, Banco banco) {
		pagador.removerValor(precoPassagem);
		banco.recebeDinheiro(precoPassagem);
	}
}

class Prisao extends Campo {
	
	Prisao(String nome, double precoPassagem) {
		super(nome, precoPassagem);
	}
	
//	TODO: função caiuNoCampo() e regras de negócio
	
}

class VaParaPrisao extends Campo {
	
	VaParaPrisao(String nome, double precoPassagem) {
		super(nome, precoPassagem);
	}
	
	void caiuNoCampo(Jogador jogador, Tabuleiro tabuleiro) {
		jogador.getPeao().setPosicao(tabuleiro.getPosicaoPrisao());
		jogador.irParaPrisao();
	}
}