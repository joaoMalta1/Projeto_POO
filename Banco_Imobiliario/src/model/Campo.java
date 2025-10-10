package model;

/* TODO: testes JUnit */

class Campo {
	String nome;
	protected double precoPassagem;
	
	Campo(String nome, double precoPassagem) {
		this.nome = nome;
	}
	
	void caiuNoCampo(Jogador pagador, Banco banco) {
//		caso o campo seja um no qual o jogador recebe dinheiro ao cair nele 
//		"removeremos" um valor negativo, e ele receberá um valor positivo
		pagador.removerValor(precoPassagem);
		banco.recebeDinheiro(precoPassagem);
	}
}

class Prisao extends Campo {
	
	Prisao(String nome, double precoPassagem) {
		super(nome, precoPassagem);
	}
	
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