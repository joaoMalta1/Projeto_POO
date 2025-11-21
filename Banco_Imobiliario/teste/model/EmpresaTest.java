package model;

import static org.junit.Assert.*;
import controller.CorPeao;
import controller.ResultadoTransacao;

import org.junit.Before;
import org.junit.Test;


public class EmpresaTest {
    private Jogador jogador;
    private Jogador outroJogador;
    private Banco banco;
    private Empresa empresa;

    @Before
    public void setUp() {
        jogador = new Jogador("AAA", new Peao(CorPeao.VERMELHA));
        outroJogador = new Jogador("AAA", new Peao(CorPeao.AZUL));
        banco = new Banco();
        empresa = new Empresa("Companhia Teste", 150); // precoCompra = 5 * precoPassagem
    }
    
    @Test
    public void testComprarEmpresa_Sucesso() {
        ResultadoTransacao resultado = empresa.comprar(jogador, banco);
        
        assertEquals(ResultadoTransacao.SUCESSO, resultado);
        assertEquals(jogador, empresa.dono);
        assertEquals(3250, jogador.getSaldo(), 0.001); // 4000 - 750
    }

    @Test
    public void testComprarEmpresa_SaldoInsuficiente() {
        Jogador jogadorPobre = new Jogador("AAA", new Peao(CorPeao.AMARELA));
        jogadorPobre.removerValor(3999); // Saldo 1
        
        ResultadoTransacao resultado = empresa.comprar(jogadorPobre, banco);
        
        assertEquals(ResultadoTransacao.SALDO_INSUFICIENTE, resultado);
        assertNull(empresa.dono);
    }
    
    @Test
    public void testComprarEmpresa_JaTemDono() {
        empresa.comprar(outroJogador, banco);
        
        ResultadoTransacao resultado = empresa.comprar(jogador, banco);
        
        assertEquals(ResultadoTransacao.JA_TEM_DONO, resultado);
        assertEquals(outroJogador, empresa.dono);
    }
    
    @Test
    public void testEmpresaInicializacao() {
        // Assert
        assertEquals("Companhia Teste", empresa.nome);
        assertEquals(150, empresa.precoPassagem, 0.001);
        assertEquals(750, empresa.precoCompra, 0.001);
        assertNull(empresa.dono);
    }
}