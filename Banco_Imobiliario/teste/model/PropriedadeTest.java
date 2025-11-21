package model;

import controller.CorPeao;
import controller.ResultadoTransacao;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class PropriedadeTest {
    private Jogador jogador;
    private Banco banco;
    private Propriedade propriedadeTest; // Usando Terreno para testar métodos da classe base

    @Before
    public void setUp() {
        jogador = new Jogador("AAAA", new Peao(CorPeao.VERMELHA));
        banco = new Banco();
        propriedadeTest = new Propriedade("Teste", 100);
    }
    
    @Test
    public void testComprarPropriedade_Sucesso() {
        ResultadoTransacao resultado = propriedadeTest.comprar(jogador, banco);
        
        assertEquals(ResultadoTransacao.SUCESSO, resultado);
        assertEquals(jogador, propriedadeTest.dono);
        assertEquals(3500, jogador.getSaldo(), 0.001); // 4000 - 500
    }
    
    @Test
    public void testComprarPropriedade_SaldoInsuficiente() {
        Jogador jogadorPobre = new Jogador("AAAA", new Peao(CorPeao.AZUL));
        jogadorPobre.removerValor(3999);
        
        ResultadoTransacao resultado = propriedadeTest.comprar(jogadorPobre, banco);
        
        assertEquals(ResultadoTransacao.SALDO_INSUFICIENTE, resultado);
        assertNull(propriedadeTest.dono);
    }
    
    @Test
    public void testComprarPropriedade_JaTemDono() {
        Jogador outroJogador = new Jogador("AAAA", new Peao(CorPeao.AMARELA));
        propriedadeTest.comprar(outroJogador, banco); // Primeira compra
        
        ResultadoTransacao resultado = propriedadeTest.comprar(jogador, banco);
        
        assertEquals(ResultadoTransacao.JA_TEM_DONO, resultado);
        assertEquals(outroJogador, propriedadeTest.dono); // Dono continua sendo o primeiro
    }
    
    @Test
    public void testPropriedadeInicializacao() {
        // Assert
        assertEquals("Teste", propriedadeTest.nome);
        assertEquals(100, propriedadeTest.precoPassagem, 0.001);
        assertEquals(500, propriedadeTest.precoCompra, 0.001);
        assertNull(propriedadeTest.dono);
    }
}