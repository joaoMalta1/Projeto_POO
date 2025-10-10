package model;

import static org.junit.Assert.*;

import org.junit.Test;

public class BancoTest {
	private final double saldo_inicial = 200000;
	
	@Test
	public void testeInicializacaoEGetSaldo() {
		Banco banco = new Banco();
		assertTrue(banco.getSaldo() == saldo_inicial);
	}
	
	@Test
	public void testRecebeDinheiro() {
		final double adicao = 500;
		Banco banco = new Banco();
		banco.recebeDinheiro(adicao);
		assertTrue(banco.getSaldo() == saldo_inicial + adicao);
	}
	
	@Test
	public void testDaDinheiro() {
		Banco banco = new Banco();
		assertTrue(banco.daDinheiro(saldo_inicial + 500) == 1);
		assertTrue(banco.daDinheiro(saldo_inicial) == 0);
	}
}
