package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class PainelTabuleiro extends JPanel {

    private static final long serialVersionUID = 1L;
    private final Janela janelaPrincipal;
    private BotaoEstilizado botaoDados;
    private int[] dados = {1, 1};
    private Image imagemMapa;
    private Image[] imagensDados;
    private boolean dadosVisiveis = false;

    private Image imagemCartaPropriedade = null;
    private final int LARGURA_CARTA = 229;
    private final int ALTURA_CARTA = 229;

    private final int LARGURA_MAPA = 700;
    private final int ALTURA_MAPA = 700;

    public PainelTabuleiro(Janela janela) {
        this.janelaPrincipal = janela;
        setBackground(Cores.getInstance().corCorrespondente(FacadeView.getInstance().getCorJogadorAtual()));
        carregarImagemDoMapa();
        carregarImagensDados();
        criarBotaoDados();
        this.exibirCartaPropriedade("1");
    }

    private void criarBotaoDados() {
        botaoDados = new BotaoEstilizado("Jogar Dados", 300, 200);
        botaoDados.setAlignmentX(CENTER_ALIGNMENT);
        botaoDados.setAlignmentY(CENTER_ALIGNMENT);

        botaoDados.addActionListener(e -> {
            dados = FacadeView.getInstance().jogarDados();
            if (dados != null && dados.length >= 2) {
                dadosVisiveis = true;
            }
            remove(botaoDados);
            repaint();
        });

        add(botaoDados);
    }

    private void carregarImagensDados() {
        imagensDados = new Image[7];
        for (int i = 1; i <= 6; i++) {
            try {
                File file = new File("src/resources/Dados/" + i + ".png");
                if (file.exists()) {
                    Image imagemOriginal = ImageIO.read(file);
                    imagensDados[i] = imagemOriginal.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                } else {
                    System.err.println("ERRO: Imagem do dado " + i + " não encontrada: " + file.getAbsolutePath());
                }
            } catch (IOException e) {
                System.err.println("Erro ao carregar imagem do dado " + i + ": " + e.getMessage());
            }
        }
    }

    private void desenharDados(Graphics2D g2d) {
        if (!dadosVisiveis || dados[0] == 0 || dados[1] == 0) return;
        if (imagensDados[dados[0]] == null || imagensDados[dados[1]] == null) return;

        int larguraDado = imagensDados[dados[0]].getWidth(this);
        int alturaDado = imagensDados[dados[0]].getHeight(this);
        int distanciaCentros = 100;
        int centroX = getWidth() / 2;
        int centroY = getHeight() / 2;
        int x1 = centroX - distanciaCentros / 2 - larguraDado / 2;
        int x2 = centroX + distanciaCentros / 2 - larguraDado / 2;
        int y = centroY - alturaDado / 2;

        g2d.drawImage(imagensDados[dados[0]], x1, y, this);
        g2d.drawImage(imagensDados[dados[1]], x2, y, this);
    }

    private void carregarImagemDoMapa() {
        try {
            File file = new File("Imagens-01/tabuleiro.png");
            if (file.exists()) {
                imagemMapa = ImageIO.read(file);
                imagemMapa = imagemMapa.getScaledInstance(LARGURA_MAPA, ALTURA_MAPA, Image.SCALE_SMOOTH);
            } else {
                System.err.println("ERRO: Imagem do mapa não encontrada: " + file.getAbsolutePath());
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar imagem do mapa: " + e.getMessage());
        }
    }

    public void exibirCartaPropriedade(String nomeArquivo) {
        try {
            File file = new File("src/resources/Propriedades/" + nomeArquivo + ".png");
            if (file.exists()) {
                imagemCartaPropriedade = ImageIO.read(file)
                        .getScaledInstance(LARGURA_CARTA, ALTURA_CARTA, Image.SCALE_SMOOTH);
                repaint();
            } else {
                System.err.println("ERRO: Carta não encontrada: " + file.getAbsolutePath());
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar carta de propriedade: " + e.getMessage());
        }
    }

    public void ocultarCartaPropriedade() {
        imagemCartaPropriedade = null;
        repaint();
    }

    private void desenharCartaPropriedade(Graphics2D g2d) {
        if (imagemCartaPropriedade == null) return;

        int margem = 20;
        int x = getWidth() - LARGURA_CARTA - margem;
        int y = margem;
        g2d.drawImage(imagemCartaPropriedade, x, y, this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        if (imagemMapa != null) {
            int mapaLargura = imagemMapa.getWidth(this);
            int mapaAltura = imagemMapa.getHeight(this);
            int x = (getWidth() - mapaLargura) / 2;
            int y = (getHeight() - mapaAltura) / 2;
            g2d.drawImage(imagemMapa, x, y, this);
        } else {
            g2d.setColor(Color.RED);
            g2d.fillRect(0, 0, getWidth(), getHeight());
            g2d.setColor(Color.WHITE);
            g2d.drawString("Erro: Imagem do Mapa não Carregada!", 50, 50);
        }

        desenharDados(g2d);
        desenharCartaPropriedade(g2d);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(janelaPrincipal.LARG_DEFAULT, janelaPrincipal.ALT_DEFAULT);
    }
}
