package model;

import java.io.*;
import java.util.*;
import controller.CorPeao; // Importe seu Enum de cor

public class Save {

    private static final String ARQUIVO_TXT = "jogo_salvo.txt";
    public static void salvarJogo() {

        try (PrintWriter out = new PrintWriter(new FileWriter(ARQUIVO_TXT))) {
            out.printf(Locale.US, "%d%n", FacadeModel.getInstance().getQtdJogadores());
            out.printf(Locale.US, "%d%n", FacadeModel.getInstance().getJogadorAtual());
            for (int i = 0; i < FacadeModel.getInstance().getQtdJogadores(); i++) 
                {
                String nome = FacadeModel.getInstance().getNomeJogador(i); // Método a criar
                String cor = FacadeModel.getInstance().getCorJogador(i).toString();
                int pos = FacadeModel.getInstance().getPosicaoJogador(i);
                double saldo = FacadeModel.getInstance().getSaldoJogadorAtual(); // Método a criar ou adaptar
                boolean preso = getSaldoJogadorAtual.isJogadorPreso(i); // Método a criar
                int rodadas = central.getRodadasPreso(i);  // Método a criar
                
                // Escreve dados básicos do jogador
                out.println(nome);
                out.println(cor);
                out.println(pos);
                out.printf(Locale.US, "%.2f%n", saldo); // Formatação segura
                out.println(preso);
                out.println(rodadas);

                // Salva as propriedades (Precisamos dos índices delas no tabuleiro)
                ArrayList<Integer> indicesPropriedades = central.getIndicesPropriedadesJogador(i);
                out.println(indicesPropriedades.size()); // Quantas propriedades ele tem
                for (Integer indice : indicesPropriedades) {
                    out.println(indice);
                }
            }
            
            System.out.println("Jogo salvo em " + ARQUIVO_TXT);

        } catch (IOException e) {
            System.err.println("Erro ao salvar arquivo: " + e.getMessage());
        }
    }

    // --- CARREGAR (LEITURA) ---
    public static void carregarJogo() {
        File arquivo = new File(ARQUIVO_TXT);
        if (!arquivo.exists()) return;

        // Scanner com BufferedReader conforme slide 20 e 23 [cite: 239, 298]
        try (Scanner s = new Scanner(new BufferedReader(new FileReader(arquivo)))) {
            s.useLocale(Locale.US); // Importante para ler double com ponto [cite: 298]

            if (!s.hasNext()) return;

            int qtdJogadores = s.nextInt();
            int jogadorAtualIndex = s.nextInt();

            ArrayList<String> nomes = new ArrayList<>();
            ArrayList<CorPeao> cores = new ArrayList<>();
            
            // Estruturas temporárias para armazenar dados e aplicar DEPOIS de criar os jogadores
            class DadosTemp {
                int pos; double saldo; boolean preso; int rodadas; ArrayList<Integer> props = new ArrayList<>();
            }
            ArrayList<DadosTemp> listaDados = new ArrayList<>();

            // 1. Lê o arquivo e coleta os dados
            for (int i = 0; i < qtdJogadores; i++) {
                DadosTemp d = new DadosTemp();
                
                s.nextLine(); // Consumir quebra de linha pendente
                String nome = s.nextLine(); // Lê nome
                String corStr = s.nextLine(); // Lê cor
                
                d.pos = s.nextInt();
                d.saldo = s.nextDouble(); // Lê double formatado 
                d.preso = s.nextBoolean();
                d.rodadas = s.nextInt();
                
                int qtdProps = s.nextInt();
                for(int k=0; k<qtdProps; k++) {
                    d.props.add(s.nextInt());
                }

                nomes.add(nome);
                // Converter String para Enum CorPeao (Implementar valueOf ou lógica similar)
                cores.add(CorPeao.valueOf(corStr)); 
                listaDados.add(d);
            }

            // 2. Reconstrói o estado na CentralPartida
            CentralPartida central = CentralPartida.getInstance();
            central.reset(); // Limpa estado anterior
            CentralPartida.getInstance(); // Recria
            
            // Recria jogadores (isso zera o saldo para 4000, então precisamos sobrescrever depois)
            central.criaJogadores(nomes, cores);

            // 3. Aplica os valores lidos (posição, saldo, propriedades)
            central.restaurarEstadoDeTexto(jogadorAtualIndex, listaDados);

            System.out.println("Jogo carregado via arquivo TXT!");

        } catch (IOException e) {
            System.err.println("Erro ao ler arquivo: " + e.getMessage());
        }
    }
}