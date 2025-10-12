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
		if (precoPassagem < 0) {
            pagador.adicionarValor(-precoPassagem);
            banco.daDinheiro(-precoPassagem); // banco perde esse valor
        } else if (precoPassagem > 0){
            pagador.removerValor(precoPassagem);
            banco.recebeDinheiro(precoPassagem);
        }
	}
}

class Prisao extends Campo {
	
	Prisao(String nome, double precoPassagem) {
		super(nome, precoPassagem);
	}
    
    boolean podeSairDaPrisao(Jogador jogador, int dado1, int dado2) {
        return (dado1 == dado2) || (jogador.getRodadasPreso() > 3);
    }
    
    void sairDaPrisao(Jogador jogador) {
        jogador.setNaPrisao(false);
        jogador.setRodadasPreso(0);
    }
    
    void passouUmaRodadaPreso(Jogador jogador) {
        if (!jogador.getIsNaPrisao()) {
            System.out.println("Jogador não está preso. Nenhuma rodada descontada.");
        }
        if (jogador.getIsNaPrisao()) {
        	int rodadasFaltantes = jogador.getRodadasPreso() + 1;
        	jogador.setRodadasPreso(rodadasFaltantes);
        }
    }
    
	void caiuNoCampo(Jogador jogador, Banco banco) {}
}

class VaParaPrisao extends Campo {
	
	VaParaPrisao(String nome, double precoPassagem) {
		super(nome, precoPassagem);
	}
	
	void caiuNoCampo(Jogador jogador, Tabuleiro tabuleiro) {
		jogador.getPeao().setPosicao(tabuleiro.getPosicaoPrisao());
		this.irParaPrisao(jogador);
	}
	
    void irParaPrisao(Jogador jogador) {
        jogador.setNaPrisao(true);
        jogador.setRodadasPreso(0);
    }
}