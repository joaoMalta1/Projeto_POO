
package model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import controller.CorPeao;

public class JogadorTest {

    private Jogador jogador;
    private Peao peao;

    @Before
    public void setUp() {
        // Garante que o estado é redefinido antes de cada teste
        peao = new Peao(CorPeao.VERMELHA);
        jogador = new Jogador("AAA", peao);
    }

    // Teste de inicialização do jogador
    @Test
    public void testInicializacao() {
        assertEquals(peao, jogador.getPeao());
        // O saldo inicial é 4000.00, não um inteiro, mas para fins de teste pode ser
        // comparado se o método getSaldo() retorna um double.
        assertEquals(4000.0, jogador.getSaldo(), 0.001);
        assertFalse(jogador.isFaliu());
        assertFalse(jogador.getIsNaPrisao());
        assertEquals(0, jogador.getRodadasPreso());
    }

    // Teste do método adicionarValor(double)
    @Test
    public void testAdicionarValorPositivo() {
        jogador.adicionarValor(500);
        assertEquals(4500.0, jogador.getSaldo(), 0.001);
    }

    // Teste do método adicionarValor(double) com valor negativo
    @Test
    public void testAdicionarValorNegativoNaoAfetaSaldo() {
        // Correção: o método adicionarValor(double) ignora valores <= 0.
        // O saldo deve permanecer o inicial (4000.00).
        jogador.adicionarValor(-200);
        assertEquals(4000.0, jogador.getSaldo(), 0.001);
    }

    // Teste do método removerValor(double) com valor válido
    @Test
    public void testRemoverValorValido() {
        boolean sucesso = jogador.removerValor(1000);
        assertTrue(sucesso);
        assertEquals(3000.0, jogador.getSaldo(), 0.001);
        assertFalse(jogador.isFaliu());
    }

    // Teste de falencia do jogador quando o saldo não é suficiente
    @Test
    public void testFalencia() {

        Banco banco = new Banco(); 
        Propriedade prop1 = new Propriedade("Avenida Ficticia", 50.0, 200.0);
        Terreno terr1 = new Terreno("Rua Ficticia", 20.0, 100.0);

        jogador.adicionarPropriedade(prop1);
        jogador.adicionarPropriedade(terr1); 
        prop1.dono = jogador;
        terr1.dono = jogador;

        double saldoInicialJogador = jogador.getSaldo();
        double saldoBancoAntes = banco.getSaldo();
        boolean sucessoRemocao = jogador.removerValor(5000.0);

        assertFalse(sucessoRemocao);
        assertTrue(jogador.isFaliu());
        assertEquals(saldoInicialJogador, jogador.getSaldo(), 0.001);

//        jogador.falencia(banco);
        assertEquals(0.0, jogador.getSaldo(), 0.001);
        assertEquals(saldoBancoAntes + saldoInicialJogador, banco.getSaldo(), 0.001);
        assertTrue(jogador.getPropriedades().isEmpty());
        assertNull(prop1.dono);
        assertNull(terr1.dono);
        assertEquals(0, ((Terreno) terr1).qtdCasas);
    }

    // Teste do método removerValor(double) com valor igual ao saldo
    @Test
    public void testRemoverValorIgualAoSaldo() {
        boolean sucesso = jogador.removerValor(4000);
        assertTrue(sucesso);
        assertEquals(0.0, jogador.getSaldo(), 0.001);
        assertFalse(jogador.isFaliu());
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