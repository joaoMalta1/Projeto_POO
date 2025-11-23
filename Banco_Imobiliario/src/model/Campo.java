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
		System.out.println("[DEBUG] Caiu em um campo normal");
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
        	jogador.setRodadasPreso(jogador.getRodadasPreso() + 1);
        }
    }
    
	void caiuNoCampo(Jogador jogador, Banco banco) {
		System.out.println("[DEBUG] Caiu na prisão");
	}
}

class VaParaPrisao extends Campo {
	
	VaParaPrisao(String nome, double precoPassagem) {
		super(nome, precoPassagem);
	}
	
	boolean caiuNoCampo(Jogador jogador, Tabuleiro tabuleiro) {
		System.out.println("[DEBUG] Caiu no Vá para a prisão");
		
		if(jogador.temCartaLivreDaPrisao()) {
			jogador.usarCartaLivreDaPrisao();
			return false;
		}
		
		jogador.getPeao().setPosicao(tabuleiro.getPosicaoPrisao());
		jogador.vaiParaPrisao();
		return true;
	}
}