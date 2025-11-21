package model;

class Propriedade extends Campo {
	protected Jogador dono;
	protected final double precoCompra;
	
	Propriedade(String nome, double precoPassagem, double precoCompra) {
		super(nome, precoPassagem);
		this.dono = null;
		this.precoCompra = precoCompra;
	}
	
	ResultadoTransacao comprar(Jogador comprador, Banco banco){
//		já tem dono, logo não pode comprar
		if(this.dono != null) {
			return ResultadoTransacao.JA_TEM_DONO;
		}
		
//		pode comprar
		if(comprador.getSaldo() >= this.precoCompra) {
			comprador.removerValor(precoCompra);
			banco.recebeDinheiro(precoCompra);
			this.dono = comprador;
			return ResultadoTransacao.SUCESSO;
		}
//		não tem saldo para comprar
		System.err.println("Saldo insuficiente!");
		return ResultadoTransacao.SALDO_INSUFICIENTE;
	}
		
	//Enum para resultados consistentes
	enum ResultadoTransacao {
	 SUCESSO,
	 JA_TEM_DONO,
	 SALDO_INSUFICIENTE,
	 NAO_EH_DONO
	}
	
}

class Terreno extends Propriedade {
	protected int qtdCasas;
//	ASSUMIMOS, como não há uma tabela indicando o preço das casas, que o preço de construção
//	de uma casa é igual a duas vezes o preço que se paga ao passar por ela
	protected double precoCasa = precoPassagem * 2;
	
	
	Terreno(String nome, double precoPassagem, double precoCompra) {
		super(nome, precoPassagem, precoCompra);
		qtdCasas   = 0;
	}

	
	ResultadoTransacao construirCasa(Jogador comprador, Banco banco){
		if(comprador != this.dono) {
			System.err.println("Não é dono!");
			return ResultadoTransacao.NAO_EH_DONO;
		}
		
//		caso comprador seja o dono e tenha a quantia suficiente
		if(comprador.getSaldo() >= precoCasa) {
			comprador.removerValor(precoCasa);
			banco.recebeDinheiro(precoCasa);
			qtdCasas++;
			
			return ResultadoTransacao.SUCESSO;
		}
		
//		caso não tenha saldo suficiente
		System.err.println("Saldo insuficiente!");
		return ResultadoTransacao.SALDO_INSUFICIENTE;
	}
	
//	IMPORTANTE:
//	Por enquanto estamos utilizando o Scanner dentro desta função para fazer a leitura.
//	Como isso é uma interação com o usuário deverá ser implementado pelo módulo View.
//	Posteriormente, quando criarmos este módulo, esta integração será mudada.
//	Esta é uma solução PROVISÓRIA
	@Override
	void caiuNoCampo(Jogador jogador, Banco banco) {
		double precoAPagar = precoPassagem * qtdCasas;
//		CASO: ninguém possui (pode ser comprado OU apenas pagará taxa de passagem ao banco)
//		if(this.dono == null) {
////			Jogador decide pelo console se deseja comprar terreno
//	        boolean vaiComprar = scannerSN("Deseja comprar o Terreno?", scanner);
//			if(vaiComprar) {
//				this.comprar(jogador, banco);
//			}
//			else {
//				jogador.removerValor(precoPassagem);
//				banco.recebeDinheiro(precoPassagem);				
//			}
//			return;
//		}
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
//		CASO: é o dono (pode comprar casa)
//		else {
//			boolean vaiComprarCasa = scannerSN("Deseja comprar uma Casa?", scanner);
//			if(vaiComprarCasa) {
//				this.construirCasa(jogador, banco);
//			}
//		}
		return;
	}
}

class Empresa extends Propriedade {
	
	Empresa(String nome, double precoPassagem, double precoCompra) {
		super(nome, precoPassagem, precoCompra);
	}

//	IMPORTANTE:
//	Por enquanto estamos utilizando o Scanner dentro desta função para fazer a leitura.
//	Como isso é uma interação com o usuário deverá ser implementado pelo módulo View.
//	Posteriormente, quando criarmos este módulo, esta integração será mudada.
//	Esta é uma solução PROVISÓRIA
	@Override
	void caiuNoCampo(Jogador jogador, Banco banco) {
//		CASO: empresa não tem dono (pode ser comprada ou apenas será pago o preço de passagem)
//		if(this.dono == null) {
////			Lê do teclado se o jogador quer comprar a empresa
//			boolean querComprar = this.scannerSN("Quer comprar a empresa?", scanner);
//			if(querComprar) {
//				this.comprar(jogador, banco);
//			}
//			else {
//				jogador.removerValor(precoPassagem);
//				banco.recebeDinheiro(precoPassagem);				
//			}
//			return;
//		}
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