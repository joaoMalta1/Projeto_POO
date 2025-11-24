package model;

class Carta {
	private double precoAReceber;
	private String nome;
	protected Baralho baralho;
	
	Carta(String nome, double precoAReceber, Baralho baralho){  // se for negativo, é para pagar
		this.precoAReceber = precoAReceber;
		this.nome = nome;
		this.baralho = baralho;
	}
	
//	retorna a posição para a qual o jogador caso retire esta carta
	int retirada(Jogador j, Banco banco) {
		System.out.println(nome);
		if(precoAReceber >= 0) {
			banco.daDinheiro(precoAReceber);
			j.adicionarValor(precoAReceber);
		}
		else {
			banco.recebeDinheiro(-precoAReceber);
			j.removerValor(-precoAReceber);
		}
		baralho.voltaParaBaralho(this);
		return j.getPeao().getPosicao();
	}
	
	String getNome() {
		return nome;
	}

	String getPrecoAReceber() {
		return ((Double)precoAReceber).toString();
	}
	
	void setBaralho(Baralho baralho) {
	    this.baralho = baralho;
	}
}

class CartaLivreDaPrisao extends Carta {
	private Jogador dono = null;
	CartaLivreDaPrisao(String nome, Baralho baralho){
		super(nome, 0, baralho);
	}
	
	@Override
	int retirada(Jogador j, Banco banco) {
		if(dono == null){
			dono = j;
			j.setCartaLivreDaPrisão(this);
		}
		System.out.println(super.getNome());
		return j.getPeao().getPosicao();
	}
	
	void foiUsada() {
		dono = null;
		baralho.voltaParaBaralho(this);
	}
	
	void setDono(Jogador j) {
		this.dono = j;
	}
}

class CartaVaParaPrisao extends Carta {
	CartaVaParaPrisao(String nome, Baralho baralho){
		super(nome, 0, baralho);
	}
	
	@Override
	int retirada(Jogador j, Banco b) {
		System.out.println(super.getNome());
		if(j.temCartaLivreDaPrisao()) { // usa carta de livre da prisão
			j.usarCartaLivreDaPrisao();
			return j.getPeao().getPosicao();
		}
//		não tem carta de livre da prisão
		j.vaiParaPrisao();
		
		baralho.voltaParaBaralho(this);
		return CentralPartida.getInstance().getPosicaoPrisao();
	}
}

class CartaReceberDeJogadores extends Carta {
	CartaReceberDeJogadores(String nome, Baralho baralho){
		super(nome, 0, baralho);
	}
	
	@Override
	int retirada(Jogador j, Banco banco) {
		System.out.println(super.getNome());
		CentralPartida.getInstance().receberDeJogadores(j, 50);
		baralho.voltaParaBaralho(this);
		return j.getPeao().getPosicao();
	}
}

class CartaPontoDePartida extends Carta {
	CartaPontoDePartida(String nome, double precoAReceber, Baralho baralho){
		super(nome, precoAReceber, baralho);
	}
	
	@Override
	int retirada(Jogador j, Banco banco) {
		System.out.println(super.getNome());
		super.retirada(j, banco);
		baralho.voltaParaBaralho(this);
		return 0; // por definição, o ponto de partida é na posição 0
	}
}