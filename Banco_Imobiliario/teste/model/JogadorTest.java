
package model;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class JogadorTest {

    private Jogador jogador;
    private Peao peao;

    @Before
    public void setUp() {
        peao = new Peao(Peao.CorPeao.VERMELHA);
        jogador = new Jogador(peao);
    }

    @Test
    public void testInicializacao() {
        assertEquals(peao, jogador.getPeao());
        assertEquals(4000.0, jogador.getSaldo(), 0.001); 
        assertFalse(jogador.isFaliu());
        assertFalse(jogador.isNaPrisao());
        assertEquals(0, jogador.getRodadasPreso());
    }

    @Test
    public void testAdicionarValorPositivo() {
        jogador.adicionarValor(500);
        assertEquals(4500.0, jogador.getSaldo(), 0.001);
    }

    @Test
    public void testAdicionarValorNegativoNaoAfetaSaldo() {
        jogador.adicionarValor(-200);
        assertEquals(4000.0, jogador.getSaldo(), 0.001);
    }

    @Test
    public void testRemoverValorValido() {
        boolean sucesso = jogador.removerValor(1000);
        assertTrue(sucesso);
        assertEquals(3000.0, jogador.getSaldo(), 0.001);
        assertFalse(jogador.isFaliu());
    }

    @Test
    public void testRemoverValorMaiorQueSaldoCausaFalencia() {
        boolean sucesso = jogador.removerValor(5000);
        assertFalse(sucesso);
        assertTrue(jogador.isFaliu());
        assertEquals(4000.0, jogador.getSaldo(), 0.001);
    }

    @Test
    public void testRemoverValorIgualAoSaldo() {
        boolean sucesso = jogador.removerValor(4000);
        assertTrue(sucesso);
        assertEquals(0.0, jogador.getSaldo(), 0.001);
        assertFalse(jogador.isFaliu());
    }

    @Test
    public void testIrParaPrisao() {
        jogador.irParaPrisao();
        assertTrue(jogador.isNaPrisao());
        assertEquals(0, jogador.getRodadasPreso());
    }

    @Test
    public void testSairDaPrisao() {
        jogador.irParaPrisao();
        jogador.incrementaRodadasPreso();
        jogador.sairDaPrisao();
        assertFalse(jogador.isNaPrisao());
        assertEquals(0, jogador.getRodadasPreso());
    }

    @Test
    public void testIncrementaRodadasPreso() {
        // Sem estar na prisão, não deve incrementar
        jogador.incrementaRodadasPreso();
        assertEquals(0, jogador.getRodadasPreso());
        jogador.irParaPrisao();
        jogador.incrementaRodadasPreso();
        jogador.incrementaRodadasPreso();
        assertEquals(2, jogador.getRodadasPreso());
    }

    @Test
    public void testPodeSairDaPrisaoPorDadoDuplo() {
        jogador.irParaPrisao();
        // 3 e 3 são duplos
        assertTrue(jogador.podeSairDaPrisao(3, 3));
    }

    @Test
    public void testPodeSairDaPrisaoPorTempo() {
        jogador.irParaPrisao();
        jogador.incrementaRodadasPreso(); // 1
        jogador.incrementaRodadasPreso(); // 2
        jogador.incrementaRodadasPreso(); // 3
        // rodadasPreso >= 3, mesmo sem duplos (2 != 5)
        assertTrue(jogador.podeSairDaPrisao(2, 5));
    }

    @Test
    public void testNaoPodeSairDaPrisaoAinda() {
        jogador.irParaPrisao();
        jogador.incrementaRodadasPreso(); // 1
        jogador.incrementaRodadasPreso(); // 2
        // Não é duplo (1 != 2) e rodadasPreso < 3
        assertFalse(jogador.podeSairDaPrisao(1, 2));
    }

    @Test
    public void testToStringContemInformacoesBasicas() {
        String output = jogador.toString();
        // Verifica se os campos chave estão presentes
        assertTrue(output.contains("Peão"));
        assertTrue(output.contains("Saldo"));
        assertTrue(output.contains("Na prisão: false"));
        assertTrue(output.contains("Rodadas preso: 0"));
        assertTrue(output.contains("Falido: false"));
    }
}