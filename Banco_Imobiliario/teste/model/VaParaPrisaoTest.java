package model;

import static org.junit.Assert.*;

import org.junit.Test;

import org.junit.Before;

public class VaParaPrisaoTest {
    private Tabuleiro tabuleiro;
    private Banco banco;
    private Jogador jogador;
    private Prisao prisao;

    @Before
    public void setUp() {
        banco = new Banco();
        tabuleiro = new Tabuleiro(banco);
        jogador = new Jogador(new Peao(Peao.CorPeao.AZUL));
    }

// 		Teste 1: Verifica se o jogador é movido para a posição da prisão 
//  	(integração mínima com tabuleiro simulando um campo novo)
    @Test
    public void testCaiuNoCampo_MoveJogadorParaPrisao() {
    	VaParaPrisao vaParaPrisao = new VaParaPrisao("VÁ PARA A PRISÃO", 0);
        int posicaoPrisao = tabuleiro.getPosicaoPrisao();

        vaParaPrisao.caiuNoCampo(jogador, tabuleiro);
        
        assertEquals(posicaoPrisao, jogador.getPeao().getPosicao());
    }

// 		Teste 2: Verifica se o jogador é marcado como preso
//  	(integração mínima com tabuleiro simulando um campo novo)
    @Test
    public void testCaiuNoCampo_MarcaJogadorComoPreso() {

    	VaParaPrisao vaParaPrisao = new VaParaPrisao("VÁ PARA A PRISÃO", 0);

        vaParaPrisao.caiuNoCampo(jogador, tabuleiro);

        assertTrue(jogador.getIsNaPrisao());
    }

    // Teste 3: Verifica se o contador de rodadas preso é resetado
    @Test
    public void testCaiuNoCampo_ResetaRodadasPreso() {
    	Campo campo = tabuleiro.getCampo("VÁ PARA A PRISÃO");
        if(campo instanceof VaParaPrisao) {
        	VaParaPrisao vaParaPrisao = (VaParaPrisao) campo;        	
        	// Simula que o jogador estava preso anteriormente por 2 rodadas
        	vaParaPrisao.irParaPrisao(jogador);
        	prisao.passouUmaRodadaPreso(jogador);
        	prisao.passouUmaRodadaPreso(jogador);
        	prisao.passouUmaRodadaPreso(jogador);
      
        	vaParaPrisao.caiuNoCampo(jogador, tabuleiro);
        	assertEquals(0, jogador.getRodadasPreso());
        }

        else {
        	fail("VÁ PARA A PRISÃO não encontrado no tabuleiro");
        }
    }

    // Teste 4: Verifica interação com tabuleiro real (integração)
    @Test
    public void testCaiuNoCampo_NoTabuleiroReal() {
        int posicaoVaParaPrisao = tabuleiro.getPosicaoVaParaPrisao();
        Campo campo = tabuleiro.getCampo(posicaoVaParaPrisao);

        assertTrue(campo instanceof VaParaPrisao);

        ((VaParaPrisao) campo).caiuNoCampo(jogador, tabuleiro);

        assertEquals(tabuleiro.getPosicaoPrisao(), jogador.getPeao().getPosicao());
        assertTrue(jogador.getIsNaPrisao());
    }

    // Teste 5: Verifica múltiplos jogadores
    @Test
    public void testCaiuNoCampo_MultiplosJogadores() {
        Jogador jogador2 = new Jogador(new Peao(Peao.CorPeao.AMARELA));
        VaParaPrisao vaParaPrisao = new VaParaPrisao("VÁ PARA A PRISÃO", 0);

        vaParaPrisao.caiuNoCampo(jogador, tabuleiro);
        vaParaPrisao.caiuNoCampo(jogador2, tabuleiro);

        // Verifica jogador 1
        assertEquals(tabuleiro.getPosicaoPrisao(), jogador.getPeao().getPosicao());
        assertTrue(jogador.getIsNaPrisao());

        // Verifica jogador 2
        assertEquals(tabuleiro.getPosicaoPrisao(), jogador2.getPeao().getPosicao());
        assertTrue(jogador2.getIsNaPrisao());
    }

    // Teste 6: Verifica se não afeta saldo do jogador
	@Test
	public void testCaiuNoCampo_NaoAlteraSaldo() {
        double saldoInicial = jogador.getSaldo();
        VaParaPrisao vaParaPrisao = new VaParaPrisao("VÁ PARA A PRISÃO", 0);

        vaParaPrisao.caiuNoCampo(jogador, tabuleiro);

        assertEquals(saldoInicial, jogador.getSaldo(), 0.0001);
    }
}