package model;

import controller.CorPeao;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import controller.ResultadoTransacao;

public class TerrenoTest {
    private Jogador jogador;
    private Jogador outroJogador;
    private Banco banco;
    private Terreno terreno;
	
    @Before
    public void setUp() {
        jogador = new Jogador("AAA", new Peao(CorPeao.VERMELHA));
        outroJogador = new Jogador("AAA", new Peao(CorPeao.AZUL));
        banco = new Banco();
        terreno = new Terreno("Terreno Teste", 100);
    }
    
    @After
    public void restoreSystemIn() {
    	System.setIn(System.in);
    }
    
    @Test
    public void testConstruirCasa_Sucesso() {
        terreno.comprar(jogador, banco);
        double saldoInicial = jogador.getSaldo();
        
        ResultadoTransacao resultado = terreno.construirCasa(jogador, banco);
        
        assertEquals(ResultadoTransacao.SUCESSO, resultado);
        assertEquals(1, terreno.qtdCasas);
        assertEquals(saldoInicial - terreno.precoCasa, jogador.getSaldo(), 0.001);
    }
    
    @Test
    public void testConstruirCasa_NaoEDono() {
        terreno.comprar(outroJogador, banco); // Outro jogador compra
        
        ResultadoTransacao resultado = terreno.construirCasa(jogador, banco);
        
        assertEquals(ResultadoTransacao.NAO_EH_DONO, resultado);
        assertEquals(0, terreno.qtdCasas);
    }
    
    @Test
    public void testConstruirCasa_SaldoInsuficiente() {
        terreno.comprar(jogador, banco);
        jogador.removerValor(jogador.getSaldo() - 1); // Quase zera o saldo (para jogador não falir)
        
        ResultadoTransacao resultado = terreno.construirCasa(jogador, banco);
        
        assertEquals(ResultadoTransacao.SALDO_INSUFICIENTE, resultado);
        assertEquals(0, terreno.qtdCasas);
    }
    
    @Test
    public void testMultiplasConstrucoes() {
        terreno.comprar(jogador, banco);
        
        terreno.construirCasa(jogador, banco);
        terreno.construirCasa(jogador, banco);
        ResultadoTransacao resultado = terreno.construirCasa(jogador, banco);
        
        assertEquals(ResultadoTransacao.SUCESSO, resultado);
        assertEquals(3, terreno.qtdCasas);
    }
    
    @Test
    public void testTerrenoInicializacao() {
        // Assert
        assertEquals(0, terreno.qtdCasas);
        assertEquals(200, terreno.precoCasa, 0.001); // 100 * 2
    }
    
//    @Test
//    public void testCaiuNoCampo_SemDono_CompraAceita() {
//        Scanner scanner = criarScannerComInput("S\n"); // Simula usuário escolhendo comprar
//        double saldoInicial = jogador.getSaldo();
//        
//        terreno.caiuNoCampo(jogador, banco, scanner);
//        
//        assertEquals(jogador, terreno.dono);
//        assertEquals(saldoInicial - terreno.precoCompra, jogador.getSaldo(), 0.001);
//        
//        scanner.close();
//    }
    
//    @Test
//    public void testCaiuNoCampo_SemDono_CompraRecusada() {
//    	Scanner scanner = criarScannerComInput("N\n"); // Simula usuário recusando compra
//        double saldoInicial = jogador.getSaldo();
//        
//        terreno.caiuNoCampo(jogador, banco, scanner);
//        
//        assertNull(terreno.dono);
//        assertEquals(saldoInicial - terreno.precoPassagem, jogador.getSaldo(), 0.001);
//        
//        scanner.close();
//    }
    
    @Test
    public void testCaiuNoCampo_ComDonoOutroJogador_PagaAluguel() {
        terreno.comprar(outroJogador, banco); // Outro jogador compra primeiro
        double saldoInicialJogador = jogador.getSaldo();
        double saldoInicialDono = outroJogador.getSaldo();  
        
        terreno.caiuNoCampo(jogador, banco);
        
        
        assertEquals(saldoInicialJogador, jogador.getSaldo(), 0.001);
        assertEquals(saldoInicialDono, outroJogador.getSaldo(), 0.001);
        
    }
    
    @Test
    public void testCaiuNoCampo_ComDonoOutroJogador_ComCasas_PagaAluguelMultiplicado() {
        terreno.comprar(outroJogador, banco);
        terreno.construirCasa(outroJogador, banco); // Adiciona uma casa
        terreno.construirCasa(outroJogador, banco); // Adiciona segunda casa
        
        double saldoInicialJogador = jogador.getSaldo();
        double saldoInicialDono = outroJogador.getSaldo();
        double aluguelEsperado = terreno.precoPassagem * 2; // 2 casas

        terreno.caiuNoCampo(jogador, banco);// Não precisa simular entrada
        
        assertEquals(saldoInicialJogador - aluguelEsperado, jogador.getSaldo(), 0.001);
        assertEquals(saldoInicialDono + aluguelEsperado, outroJogador.getSaldo(), 0.001);
        
    }
    
    
//    @Test
//    public void testCaiuNoCampo_ProprioDono_CompraCasaAceita() {
//        terreno.comprar(jogador, banco);
//        Scanner scanner = criarScannerComInput("S\n"); // Aceita comprar casa
//        double saldoInicial = jogador.getSaldo();
//        
//        terreno.caiuNoCampo(jogador, banco, scanner);
//        
//        assertEquals(1, terreno.qtdCasas);
//        assertEquals(saldoInicial - terreno.precoCasa, jogador.getSaldo(), 0.001);
//        
//        
//        scanner.close();
//    }
    
    
//    @Test
//    public void testCaiuNoCampo_ProprioDono_CompraCasaRecusada() {
//        terreno.comprar(jogador, banco);
//        Scanner scanner = criarScannerComInput("N\n"); // Recusa comprar casa
//        double saldoInicial = jogador.getSaldo();
//        
//        terreno.caiuNoCampo(jogador, banco, scanner);
//        
//        assertEquals(0, terreno.qtdCasas);
//        assertEquals(saldoInicial, jogador.getSaldo(), 0.001); // Saldo não muda
//        
//        scanner.close();
//    }
    
    
//    @Test
//    public void testCaiuNoCampo_EntradaInvalidaSeguidadeTentativaValida() {
//        simulaInput.adicionarEntrada("X"); // Entrada inválida
//        mockScanner.adicionarEntrada("S"); // Entrada válida
//        double saldoInicial = jogador.getSaldo();
//        
//        // Act
//        terreno.caiuNoCampo(jogador, banco, mockScanner);
//        
//        // Assert
//        assertEquals(jogador, terreno.dono);
//        assertEquals(saldoInicial - terreno.precoCompra, jogador.getSaldo(), 0.001);
//    }
}