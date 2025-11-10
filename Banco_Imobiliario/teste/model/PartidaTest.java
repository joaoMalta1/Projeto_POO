// package model;

// import static org.junit.Assert.*;
// import org.junit.Before;
// import org.junit.Test;

// public class PartidaTest {

//     private Partida partida;

//     @Before
//     public void setUp() {
//         partida = new Partida(4);
//     }


//     @Test
//     public void testConstrutorInicializaJogadoresCorretamente() {
//         assertNotNull(partida);
//         assertEquals(4, partida.getQtdJogadoresAtivos());
//         assertNotNull(partida.getJogadorAtual());
//     }

//     @Test(expected = IllegalArgumentException.class)
//     public void testConstrutorComMenosDe2Jogadores() {
//         new Partida(1);
//     }

//     @Test(expected = IllegalArgumentException.class)
//     public void testConstrutorComMaisDe6Jogadores() {
//         new Partida(7);
//     }


//     @Test
//     public void testProximoJogadorAlternaCorretamente() {
//         int jogador1 = partida.getJogadorAtual().getPeao().getCor().ordinal();
//         partida.proximoJogador();
//         int jogador2 = partida.getJogadorAtual().getPeao().getCor().ordinal();
//         assertNotEquals(jogador1, jogador2);
//     }

//     @Test
//     public void testProximoJogadorPulaFalidos() {
//         // falir jogador 1 (índice 1)
//         partida.proximoJogador();
//         Jogador jogador1 = partida.getJogadorAtual();
//         jogador1.falencia(new Banco());

//         int anterior = partida.proximoJogador();
//         assertEquals(2, anterior);
//     }

//     @Test
//     public void testJogarDadosRetornaDoisValores() {
//         int[] resultado = partida.jogarDados();
//         assertNotNull(resultado);
//         assertEquals(2, resultado.length);
//         assertTrue(resultado[0] >= 1 && resultado[0] <= 6);
//         assertTrue(resultado[1] >= 1 && resultado[1] <= 6);
//     }
// }
