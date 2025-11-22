package model;

class Carta {
	protected double precoAReceber;
	Carta(double precoAReceber){  // se for negativo, é para pagar
		this.precoAReceber = precoAReceber;
	}
	
//	retorna a posição para a qual o jogador caso retire esta carta
	int retirada(Jogador j, Banco banco) {
		if(precoAReceber >= 0) {
			banco.daDinheiro(precoAReceber);
			j.adicionarValor(precoAReceber);
		}
		else {
			banco.recebeDinheiro(-precoAReceber);
			j.removerValor(-precoAReceber);
		}
		
		return j.getPeao().getPosicao();
	}
}

class CartaLivreDaPrisao extends Carta {
	CartaLivreDaPrisao(double precoAReceber){
		super(precoAReceber);
	}
	
	@Override
	int retirada(Jogador j, Banco banco) {
		
		return j.getPeao().getPosicao();
	}
	
}

class CartaVaParaPrisao extends Carta {
	CartaVaParaPrisao(double precoAReceber){
		super(precoAReceber);
	}
}

class CartaReceberDeJogadores extends Carta {
	CartaReceberDeJogadores(double precoAReceber){
		super(precoAReceber);
	}
}

class CartaPontoDePartida extends Carta {
	CartaPontoDePartida(double precoAReceber){
		super(precoAReceber);
	}
	
	@Override
	int retirada(Jogador j, Banco banco) {
		super.retirada(j, banco);
		return 0; // por definição, o ponto de partida é na posição 0
	}
}