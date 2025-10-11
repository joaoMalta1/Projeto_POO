package model;

import java.util.Scanner;

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
		System.err.println("Saldo insuficiente!");
		return 2;
	}
	
//	S = true ; N = false;
	protected boolean scannerSN(String mensagem) {
		Scanner scanner = new Scanner(System.in);
		String resposta;
        
        do {
            System.out.print(mensagem + " (S/N): ");
            resposta = scanner.nextLine().trim().toUpperCase();
            
            switch (resposta) {
                case "S" -> {
                    System.out.println("Você escolheu SIM");
                    scanner.close();
                    return true;
                }
                case "N" -> {
                    System.out.println("Você escolheu NÃO");
                    scanner.close();
                    return false;
                }
                default -> System.out.println("Resposta inválida! Digite apenas S ou N.");
            }
//            faz scan até receber S ou N
        } while (true);
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
			comprador.removerValor(precoCasa);
			banco.recebeDinheiro(precoCasa);
			qtdCasas++;
			
			return 0;
		}
		
//		caso não tenha saldo suficiente
		System.err.println("Saldo insuficiente!");
		return 2;
	}
	
	void caiuNoCampo(Jogador jogador, Banco banco) {
		double precoAPagar = precoPassagem * qtdCasas;
//		CASO: ninguém possui (pode ser comprado OU apenas pagará taxa de passagem ao banco)
		if(this.dono == null) {
//			Jogador decide pelo console se deseja comprar terreno
	        boolean vaiComprar = scannerSN("Deseja comprar o Terreno?");
			if(vaiComprar) {
				this.comprar(jogador, banco);
			}
			else {
				jogador.removerValor(precoPassagem);
				banco.recebeDinheiro(precoPassagem);				
			}
			return;
		}
//		CASO: já possui dono e o jogador que está no campo não é o dono
		else if(jogador != dono) {
			jogador.removerValor(precoAPagar);
			this.dono.adicionarValor(precoAPagar);
			return;
		}
//		CASO: é o dono (pode comprar casa)
		else {
			boolean vaiComprarCasa = scannerSN("Deseja comprar uma Casa?");
			if(vaiComprarCasa) {
				this.construirCasa(jogador, banco);
			}
		}
		return;
	}
}

class Empresa extends Propriedade {
	
	Empresa(String nome, double precoPassagem, double precoCompra) {
		super(nome, precoPassagem, precoCompra);
	}
	
	void caiuNoCampo(Jogador jogador, Banco banco) {
//		CASO: empresa não tem dono (pode ser comprada ou apenas será pago o preço de passagem)
		if(this.dono == null) {
//			Lê do teclado se o jogador quer comprar a empresa
			boolean querComprar = this.scannerSN("Quer comprar a empresa?");
			if(querComprar) {
				this.comprar(jogador, banco);
			}
			else {
				jogador.removerValor(precoPassagem);
				banco.recebeDinheiro(precoPassagem);				
			}
		}
		else if(jogador != dono) {
			jogador.removerValor(precoPassagem);
			this.dono.adicionarValor(precoPassagem);
		}
		return;
	}
	
}