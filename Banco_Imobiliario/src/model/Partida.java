// package model;

// import controller.CorPeao;

// // FUTURAMENTE SERÁ SUBSTITUÍDA POR CENTRALPARTIDA
// class Partida {
//     private Jogador[] jogadores;
//     private int qtd_jogadores;
//     private int jogadorAtual;
//     private CorPeao[] cores = {
//         CorPeao.AZUL, CorPeao.VERMELHA,
//         CorPeao.AMARELA, CorPeao.CINZA,
//         CorPeao.LARANJA, CorPeao.ROXA
//     };
//     VaParaPrisao vaParaPrisao;

//     Partida(int qtd_jogadores) {
//         if (qtd_jogadores < 2 || qtd_jogadores > 6) {
//             throw new IllegalArgumentException("A quantidade de jogadores deve ser entre 2 e 6");
//         }

//         this.qtd_jogadores = qtd_jogadores;
//         this.jogadores = new Jogador[qtd_jogadores];

//         for (int i = 0; i < qtd_jogadores; i++) {
//             this.jogadores[i] = new Jogador("AA", new Peao(cores[i]));
//         }

//         this.jogadorAtual = 0;
//     }

//     int proximoJogador() {
//         if (isFimDeJogo()) {
//             return jogadorAtual;
//         }

//         int proximo = (jogadorAtual + 1) % qtd_jogadores;

//         while (jogadores[proximo].isFaliu()) {
//             proximo = (proximo + 1) % qtd_jogadores;
//             if (proximo == (jogadorAtual + 1) % qtd_jogadores) {
//                 jogadorAtual = proximo;
//                 return jogadorAtual;
//             }
//         }

//         jogadorAtual = proximo;
//         return jogadorAtual;
//     }

//     boolean isFimDeJogo() {
//         return getQtdJogadoresAtivos() <= 1;
//     }

//     Jogador getVencedor() {
//         if (isFimDeJogo()) {
//             return jogadores[jogadorAtual];
//         }
//         return null;
//     }

//     Jogador getJogadorAtual() {
//         return jogadores[jogadorAtual];
//     }

//     int getQtdJogadoresAtivos() {
//         int ativos = 0;
//         for (Jogador jogador : jogadores) {
//             if (!jogador.isFaliu()) {
//                 ativos++;
//             }
//         }
//         return ativos;
//     }

//     int[] jogarDados() {
//         int counter = 0;
//         int[] resultado = new int[2];

//         while (counter < 3) {
//             resultado = Dados.getInstance().jogar();

//             if (resultado[0] != resultado[1]) {
//                 return resultado;
//             }

//             counter++;
//         }

//         if (vaParaPrisao != null) {
//             vaParaPrisao.irParaPrisao(getJogadorAtual());
//         }

//         return resultado;
//     }
// }
