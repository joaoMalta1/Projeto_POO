package model;

class Propriedade extends Campo {
	protected Jogador dono;
	protected double precoCompra;
	
	Propriedade(String nome, double precoPassagem, double precoCompra) {
		super(nome, precoPassagem);
		this.dono = null;
		this.precoCompra = precoCompra;
	}
	
	int comprar(Jogador comprador, Banco banco){
//		já tem dono, logo não pode comprar
		if(this.dono != null) {
			return 1;
		}
		
//		pode comprar
		if(comprador.getSaldo() >= this.precoCompra) {
			comprador.removerValor(precoCompra);
			banco.recebeDinheiro(precoCompra);
			this.dono = comprador;
			
			return 0;
		}

//		não tem saldo para comprar
		return 2;
	}
	
}

class Terreno extends Propriedade {
	protected int qtdCasas;
//	ASSUMIMOS, como não há uma tabela indicando o preço das casas, que o preço de construção
//	de uma casa é igual a duas vezes o preço que se paga ao passar por ela
	protected double precoCasa = precoPassagem * 2;
	
	
	Terreno(String nome, double precoPassagem, double precoCompra) {
		super(nome, precoPassagem, precoCompra);
		this.precoCompra = precoCompra;
		qtdCasas   = 0;
	}

	
	
/* Função para a construção de uma casa
 * retorna 0 caso a compra seja efetuadada
 * retorna 1 caso o comprador não seja o dono
 * retorna 2 caso o comprador não tenha dinheiro suficiente
 * */
	int construirCasa(Jogador comprador, Banco banco){
		if(comprador != this.dono) {
			return 1;
		}
		
//		caso comprador seja o dono e tenha a quantia suficiente
		if(comprador.getSaldo() >= precoCasa) {
//			não implementaremos o banqueiro, portanto, apenas subtrairemos ou adicionaremos 
//			dinheiro do jogador sem destiná-lo a lugar algum 
//			(assumindo que o banco tem dinheiro infinito)
			comprador.removerValor(precoCasa);
			banco.recebeDinheiro(precoCasa);
			qtdCasas++;
			
			return 0;
		}
		
//		caso não tenha a quantia suficiente
		return 2;
	}
	
	
	
	void caiuNoCampo(Jogador pagador, Banco banco) {
		double precoAPagar = precoPassagem * qtdCasas;
		pagador.removerValor(precoAPagar);
		if(this.dono == null) {
			banco.recebeDinheiro(precoAPagar);
			return;
		}
		this.dono.adicionarValor(precoAPagar);
		return;
	}
}

class Empresa extends Propriedade {
	
	Empresa(String nome, double precoPassagem, double precoCompra) {
		super(nome, precoPassagem, precoCompra);
	}
	
	void caiuNoCampo(Jogador pagador, Banco banco) {
		pagador.removerValor(precoPassagem);
		if(this.dono == null) {
			banco.recebeDinheiro(precoPassagem);
			return;
		}
		this.dono.adicionarValor(precoPassagem);
		return;
	}
	
}