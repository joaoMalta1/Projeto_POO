package model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class PrisaoTest {

    private Jogador jogador;
    private Prisao prisao;
    private Banco banco;
    private VaParaPrisao vaParaPrisao;

    @Before
    public void setUp() {
        banco = new Banco();
        jogador = new Jogador(new Peao(Peao.CorPeao.ROXA));
        prisao = new Prisao("PRISÃO", 0);
        vaParaPrisao = new VaParaPrisao("Vá para prisão", 0);
    }

    @Test
    public void testPodeSairDaPrisao_QuandoDadosIguais() {
    	vaParaPrisao.irParaPrisao(jogador);
    	prisao.passouUmaRodadaPreso(jogador);

        boolean resultado = prisao.podeSairDaPrisao(jogador, 3, 3);

        assertTrue(resultado);
    }

    @Test
    public void testPodeSairDaPrisao_AposMaisDeTresRodadas() {
    	vaParaPrisao.irParaPrisao(jogador);
    	
    	prisao.passouUmaRodadaPreso(jogador);
    	prisao.passouUmaRodadaPreso(jogador);
    	prisao.passouUmaRodadaPreso(jogador);
    	prisao.passouUmaRodadaPreso(jogador);

        boolean resultado = prisao.podeSairDaPrisao(jogador, 1, 2);

        assertTrue(resultado);
    }

    @Test
    public void testNaoPodeSairDaPrisao_SeDadosDiferentesEMenosDeTresRodadas() {
    	vaParaPrisao.irParaPrisao(jogador);
    	
    	prisao.passouUmaRodadaPreso(jogador);
    	prisao.passouUmaRodadaPreso(jogador);

        boolean resultado = prisao.podeSairDaPrisao(jogador, 1, 4);

        assertFalse(resultado);
    }

    @Test
    public void testSairDaPrisao_ResetaStatusECounter() {
    	vaParaPrisao.irParaPrisao(jogador);
    	prisao.passouUmaRodadaPreso(jogador);

        prisao.sairDaPrisao(jogador);

        assertFalse(jogador.getIsNaPrisao());
        assertEquals(0, jogador.getRodadasPreso());
    }

    @Test
    public void testPassouUmaRodadaPreso_IncrementaRodadas() {
    	vaParaPrisao.irParaPrisao(jogador);
    	prisao.passouUmaRodadaPreso(jogador);

        prisao.passouUmaRodadaPreso(jogador);

        assertEquals(2, jogador.getRodadasPreso());
    }

    @Test
    public void testPassouUmaRodadaPreso_JogadorNaoPresoNaoIncrementa() {
        prisao.passouUmaRodadaPreso(jogador);

        assertEquals(0, jogador.getRodadasPreso());
    }


    @Test
    public void testCaiuNoCampo_NaoAfetaJogador() {
        double saldoInicial = jogador.getSaldo();
        int posicaoInicial = jogador.getPeao().getPosicao();

        prisao.caiuNoCampo(jogador, banco);

        assertEquals(saldoInicial, jogador.getSaldo(), 0.0001);
        assertEquals(posicaoInicial, jogador.getPeao().getPosicao());
    }
}
