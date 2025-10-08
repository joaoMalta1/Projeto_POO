package model;

class Campo {
	String nome;
	
	Campo(String nome) {
		this.nome = nome;
	}
}

class Propriedade extends Campo {
	protected Jogador dono = null;
	protected double preco;
	
	Propriedade(String nome, double preco) {
		super(nome);
		this.dono = null;
		this.preco = preco;
	}
	
	int comprar(Jogador comprador){
//		já tem dono, logo não pode comprar
		if(this.dono != null) {
			return 1;
		}
		
//		pode comprar
		if(comprador.getSaldo() >= this.preco) {
			comprador.removerValor(preco);
			this.dono = comprador;
			
			return 0;
		}

//		não tem saldo para comprar
		return 2;
	}
	
}

class Terreno extends Propriedade {
	protected int qtdCasas;
//	protected int qtdHoteis;
	
	Terreno(String nome, double preco) {
		super(nome, preco);
		this.preco = preco;
		qtdCasas   = 0;
//		qtdHoteis  = 0;
	}
	
/* Função para a construção de uma casa
 * retorna 0 caso a compra seja efetuadada
 * retorna 1 caso o comprador não seja o dono
 * retorna 2 caso o comprador não tenha dinheiro suficiente
 * */
	int construirCasa(Jogador comprador){
		if(comprador != this.dono) {
			return 1;
		}
		
//		caso comprador seja o dono e tenha a quantia suficiente
		if(comprador.getSaldo() >= preco) {
			comprador.removerValor(preco);
			qtdCasas++;
			
			return 0;
		}
		
//		caso não tenha a quantia suficiente
		return 2;
	}
	
}

class Empresa extends Propriedade {
	
	Empresa(String nome, double preco) {
		super(nome, preco);
	}
	
}

class Prisao extends Campo {
	
	Prisao(String nome) {
		super(nome);
	}
	
}

