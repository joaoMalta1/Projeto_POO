package model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.util.Scanner;
import controller.CorPeao;

public class TabuleiroTest {
    private Tabuleiro tabuleiro;
    private Banco banco;
    private Jogador jogador;
    private Peao peao;
    private int TAMANHO = 34;

    @Before
    public void setUp() {
        banco = new Banco();
        tabuleiro = new Tabuleiro(banco);
        peao = new Peao(CorPeao.VERMELHA);
        jogador = new Jogador("AAA", peao);
    }

    @Test
    public void testGetCampoPorPosicaoValida() {
        Campo campo = tabuleiro.getCampo(0);
        assertNotNull(campo);
        assertEquals("PARTIDA", campo.nome);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCampoPorPosicaoInvalida() {
        tabuleiro.getCampo(-1);
    }

    @Test
    public void testGetCampoPorNomeExistente() {
        Campo campo = tabuleiro.getCampo("Leblon");
        assertNotNull(campo);
        assertTrue(campo instanceof Terreno);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCampoPorNomeInexistente() {
        tabuleiro.getCampo("NomeInexistente");
    }

    @Test
    public void testGetPosicaoPrisao() {
        int posicaoPrisao = tabuleiro.getPosicaoPrisao();
        assertEquals(9, posicaoPrisao);
        assertTrue(tabuleiro.getCampo(posicaoPrisao) instanceof Prisao);
    }

    @Test
    public void testGetPosicaoVaParaPrisao() {
        int posicaoVPP = tabuleiro.getPosicaoVaParaPrisao();
        assertEquals(25, posicaoVPP);
        assertTrue(tabuleiro.getCampo(posicaoVPP) instanceof VaParaPrisao);
    }

    @Test
    public void testMoverJogadorNormal() {
        int posicaoInicial = 0;
        int[] dados = {3, 4};
        String input = "N\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        
        int novaPosicao = tabuleiro.moverJogador(jogador, posicaoInicial, dados, scanner);
        
        assertEquals(7, novaPosicao);
        assertFalse(jogador.getIsNaPrisao());
        
        scanner.close();
    }

    @Test
    public void testMoverJogadorPassandoInicio() {
        int posicaoInicial = 32;
        int[] dados = {5, 6};
        String input = "N\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        
        double saldoInicial = jogador.getSaldo();
        int novaPosicao = tabuleiro.moverJogador(jogador, posicaoInicial, dados, scanner);
        
        assertEquals(9, novaPosicao);
        // Deve receber bônus por passar pelo início
        assertEquals(saldoInicial + 200, jogador.getSaldo(), 0.001);
        
        scanner.close();
    }

    @Test
    public void testMoverJogadorCaindoVaParaPrisao() {
        int posicaoInicial = 20;
        int[] dados = {4, 1}; // Para cair exatamente na posição 25
        String input = "N\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        
        int novaPosicao = tabuleiro.moverJogador(jogador, posicaoInicial, dados, scanner);
        
        assertEquals(tabuleiro.getPosicaoPrisao(), novaPosicao);
        assertTrue(jogador.getIsNaPrisao());
        
        scanner.close();
    }

    @Test
    public void testMoverJogadorPresoComDadosIguais() {
        jogador.setNaPrisao(true);
        int[] dados = {2, 2};
        String input = "N\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        
        int novaPosicao = tabuleiro.moverJogador(jogador, 9, dados, scanner);
        
        assertFalse(jogador.getIsNaPrisao());
        assertEquals(0, jogador.getRodadasPreso());
        assertTrue(novaPosicao > 9 && novaPosicao < tabuleiro.getTamanho());
        
        scanner.close();
    }

    @Test
    public void testMoverJogadorPresoExcedeuRodadas() {
        jogador.setNaPrisao(true);
        jogador.setRodadasPreso(4);
        int[] dados = {1, 2};
        String input = "N\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        
        int novaPosicao = tabuleiro.moverJogador(jogador, 9, dados, scanner);
        
        assertFalse(jogador.getIsNaPrisao());
        assertEquals(0, jogador.getRodadasPreso());
        assertEquals(9 + 3, novaPosicao);
        
        scanner.close();
    }

    @Test
    public void testMoverJogadorPresoPermanecePreso() {
        jogador.setNaPrisao(true);
        jogador.setRodadasPreso(2);
        int[] dados = {1, 2};
        String input = "N\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        
        int novaPosicao = tabuleiro.moverJogador(jogador, 9, dados, scanner);
        
        assertTrue(jogador.getIsNaPrisao());
        assertEquals(3, jogador.getRodadasPreso());
        assertEquals(9, novaPosicao);
        
        scanner.close();
    }

    @Test
    public void testMoverJogadorCaindoTerrenoSemDono() {
        int posicaoInicial = 0;
        int[] dados = {1, 1}; // Para cair na Av. Presidente Vargas (posição 2)
        String input = "S\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        
        double saldoInicial = jogador.getSaldo();
        int novaPosicao = tabuleiro.moverJogador(jogador, posicaoInicial, dados, scanner);
        
        assertEquals(2, novaPosicao);
        // Deve ter comprado o terreno (preço 5x passagem = 500)
        assertTrue(jogador.getSaldo() < saldoInicial);
        
        scanner.close();
    }

    @Test
    public void testMoverJogadorCaindoEmpresa() {
        int posicaoInicial = 2;
        int[] dados = {1, 1}; // Para cair na Companhia Ferroviária (posição 4)
        String input = "S\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        
        double saldoInicial = jogador.getSaldo();
        int novaPosicao = tabuleiro.moverJogador(jogador, posicaoInicial, dados, scanner);
        
        assertEquals(4, novaPosicao);
        // Deve ter comprado a empresa (preço 5x passagem = 1000)
        assertTrue(jogador.getSaldo() < saldoInicial);
        
        scanner.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMoverJogadorDadosInvalidos() {
        int[] dadosInvalidos = {1};
        String input = "S\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        tabuleiro.moverJogador(jogador, 0, dadosInvalidos, scanner);
    }

    @Test
    public void testGetTamanho() {
        assertEquals(TAMANHO, tabuleiro.getTamanho());
    }

    @Test
    public void testInicializacaoCampos() {
        // Testa alguns campos específicos
        Campo partida = tabuleiro.getCampo(0);
        assertTrue(partida instanceof Campo);
        assertEquals("PARTIDA", partida.nome);

        Campo prisao = tabuleiro.getCampo(tabuleiro.getPosicaoPrisao());
        assertTrue(prisao instanceof Prisao);
        assertEquals("PRISÃO", prisao.nome);

        Campo vaParaPrisao = tabuleiro.getCampo(tabuleiro.getPosicaoVaParaPrisao());
        assertTrue(vaParaPrisao instanceof VaParaPrisao);
        assertEquals("VÁ PARA A PRISÃO", vaParaPrisao.nome);

        Campo terreno = tabuleiro.getCampo(1);
        assertTrue(terreno instanceof Terreno);
        assertEquals("Leblon", terreno.nome);

        Campo empresa = tabuleiro.getCampo(4);
        assertTrue(empresa instanceof Empresa);
        assertEquals("Companhia Ferroviária", empresa.nome);
    }
}