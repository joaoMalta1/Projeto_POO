package model;

import controller.ResultadoTransacao;

class Propriedade extends Campo {
	protected Jogador dono;
	protected final double precoCompra;
	protected final int proporcaoPassagemCompra = 2;
	
	Propriedade(String nome, double precoPassagem) {
		super(nome, 0); /* IMPORTANTE: 	pelo que entendemos das regras oficiais do jogo, quando um terreno não tem dono,
		 								qualquer um que por ele passar não pagará nada. Portanto, construímos a propriedade
		 								com precoPassagem = 0*/
		this.dono = null;
//		this.precoCompra = precoCompra;
		this.precoCompra = proporcaoPassagemCompra * precoPassagem; /* Como não há regra especificada para o preço de compra de uma propriedade, a 
		 										definimos como 2 vezes o que está na tabela. */
	}
	
	ResultadoTransacao comprar(Jogador comprador, Banco banco){
		if(this.dono != null) {
			return ResultadoTransacao.JA_TEM_DONO;
		}
		
		if(comprador.getSaldo() >= this.precoCompra) {
			comprador.removerValor(precoCompra);
			banco.recebeDinheiro(precoCompra);
			this.dono = comprador;
			
			comprador.adicionarPropriedade(this);
			
			System.out.println("[DEBUG] Jogador: " + comprador.getNome() + " comprou a popriedade");
			
			return ResultadoTransacao.SUCESSO;
		}

		System.err.println("Saldo insuficiente!");
		return ResultadoTransacao.SALDO_INSUFICIENTE;
	}
}

class Terreno extends Propriedade {
	protected int qtdCasas, qtdHotel;
	protected double precoCasa = (precoCompra/proporcaoPassagemCompra) * 0.5;
	protected double precoHotel= (precoCompra/proporcaoPassagemCompra);
	
	
	Terreno(String nome, double precoPassagem) {
		super(nome, precoPassagem);
		qtdCasas  = 0;
		qtdHotel = 0;
	}

	
	ResultadoTransacao construirCasa(Jogador comprador, Banco banco){
		if(comprador != this.dono) {
			System.err.println("Não é dono!");
			return ResultadoTransacao.NAO_EH_DONO;
		}
		
		if(qtdCasas >= 4) {
			return ResultadoTransacao.LIMITE_ATINGIDO;
		}
		
		if(comprador.getSaldo() >= precoCasa) {
			comprador.removerValor(precoCasa);
			banco.recebeDinheiro(precoCasa);
			qtdCasas++;
			
			System.out.println("[DEBUG] Jogador: " + comprador.getNome() + " comprou uma casa");
			
			return ResultadoTransacao.SUCESSO;
		}
		
		System.err.println("Saldo insuficiente!");
		return ResultadoTransacao.SALDO_INSUFICIENTE;
	}
	
	ResultadoTransacao construirHotel(Jogador comprador, Banco banco){
		if(comprador != this.dono) {
			System.err.println("Não é dono!");
			return ResultadoTransacao.NAO_EH_DONO;
		}
		
		if(qtdHotel == 1) {
			return ResultadoTransacao.LIMITE_ATINGIDO;
		}
		
		if(comprador.getSaldo() >= precoHotel) {
			comprador.removerValor(precoHotel);
			banco.recebeDinheiro(precoHotel);
			qtdHotel++;
			
			System.out.println("[DEBUG] Jogador: " + comprador.getNome() + " comprou um hotel");
			
			return ResultadoTransacao.SUCESSO;
		}
		
		System.err.println("Saldo insuficiente!");
		return ResultadoTransacao.SALDO_INSUFICIENTE;
	}
	
	@Override
	void caiuNoCampo(Jogador jogador, Banco banco) {
		System.out.println("[DEBUG] Caiu em um Terreno");
		
		double precoAPagar = precoPassagem * (0.1 + 0.15 * qtdCasas + 0.3 * qtdHotel);
		
//		CASO: ninguém possui
		if(dono == null) {
			jogador.removerValor(precoPassagem);
			banco.recebeDinheiro(precoPassagem);
			return;
		}
		
//		CASO: já possui dono e o jogador que está no campo não é o dono
		if(jogador != dono) {
			jogador.removerValor(precoAPagar);
			this.dono.adicionarValor(precoAPagar);
			return;
		}
		return;
	}


	boolean podeComprarCasa(Jogador jogador) {
		return jogador == dono && qtdCasas <= 4 && jogador.getSaldo() >= precoCasa;
	}
	
	boolean podeComprarHotel(Jogador jogador) {
		return jogador == dono && qtdHotel == 0 && qtdCasas >= 1 && jogador.getSaldo() >= precoHotel;
	}
}

class Empresa extends Propriedade {
	
	Empresa(String nome, double precoPassagem) {
		super(nome, precoPassagem);
		this.precoPassagem = precoPassagem;
	}

	@Override
	void caiuNoCampo(Jogador jogador, Banco banco) {
		System.out.println("[DEBUG] Caiu em uma Empresa");
		if(dono == null) {
			jogador.removerValor(precoPassagem);
			banco.recebeDinheiro(precoPassagem);
			return;
		}
		if(jogador != dono) {
			jogador.removerValor(precoPassagem);
			this.dono.adicionarValor(precoPassagem);
		}
		return;
	}
	
}