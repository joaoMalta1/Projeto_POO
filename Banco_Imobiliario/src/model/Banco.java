package model;

class Banco {
	private double saldo;
	private static double valor_inicial = 200000;
	
	Banco(){
		saldo = valor_inicial;
	}
	
	void recebeDinheiro(double quantia) {
		saldo += quantia;
	}
	
	int daDinheiro(double quantia) {
		if(saldo >= quantia) {
			saldo -= quantia;
			return 0;
		}
		return 1;
	}
	
	double getSaldo() {
		return saldo;
	}

	void setSaldo(double saldo) {
		this.saldo = saldo;
	}
}
