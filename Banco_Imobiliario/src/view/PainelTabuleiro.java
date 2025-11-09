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
import java.util.Map;
import java.awt.Point;
import java.util.HashMap;

import model.FacadeModel;
import controller.Observador;
import controller.CorPeao;
import controller.ControlePartida;
import controller.PartidaEvent;

public class PainelTabuleiro extends JPanel implements Observador<PartidaEvent> {

    private static final long serialVersionUID = 1L;
    private final Janela janelaPrincipal;
    private BotaoEstilizado botaoDados;
    private BotaoEstilizado botaoComprar; // novo botão para comprar propriedade

    private int[] dados = { 1, 1 };
    private Image imagemMapa;
    private Image[] imagensDados;
    private boolean dadosVisiveis = false;
    private Image imagemPeao;
    private java.util.Map<CorPeao, Image> imagensPinos;
    private final int TAMANHO_PEAO = 25;
    private Map<Integer, Point> coordenadasCasas;

    private int posicaoDeTeste = 1;

    private Image imagemCartaPropriedade = null;
    private final int LARGURA_CARTA = 229;
    private final int ALTURA_CARTA = 229;

    // simensao mapa
    private final int LARGURA_MAPA = 700;
    private final int ALTURA_MAPA = 700;

    public PainelTabuleiro(Janela janela) {
        this.janelaPrincipal = janela;
        setBackground(Cores.getInstance().corCorrespondente(FacadeView.getInstance().getCorJogadorAtual()));
        carregarImagemDoMapa();
        carregarImagensDados();
        criarBotaoDados();

        inicializarCoordenadasCasas();
        carregarImagensPinos();
        FacadeModel.getInstance().addObserver(this);
    }

    private void criarBotaoDados() {
        botaoDados = new BotaoEstilizado("Jogar Dados", 300, 200);
        botaoDados.setAlignmentX(CENTER_ALIGNMENT);
        botaoDados.setAlignmentY(CENTER_ALIGNMENT);

        botaoDados.addActionListener(e -> {
            if (botaoDados.getParent() != null) {
                remove(botaoDados);
                revalidate();
                repaint();
            }
            int[] resultado = ControlePartida.getInstance().jogarDadosEAndar();
            if (resultado != null && resultado.length == 2) {
                this.dados = new int[] { resultado[0], resultado[1] };
                this.dadosVisiveis = true;
            }
        });

        // se parou em propriedade disponível, mostrar botão comprar
        if (FacadeView.getInstance().propriedadeDisponivelAtual()) 
        {
            System.out.println("AAAAAAAAAAAAAAAAAAAAA");
            criarBotaoComprarPropriedade();
        } else {
            
            System.out.println("BBBBBBBBBBBBBBBBBBB");
            removerBotaoComprarSeExistir(); //após clicar uma vez o botao ta sumindo, ajustar essa logica
        }
        add(botaoDados);
        revalidate();
        repaint();
    }

    private void criarBotaoComprarPropriedade() {
        if (botaoComprar != null && botaoComprar.getParent() != null)
            return; 
        botaoComprar = new BotaoEstilizado("Comprar Propriedade", 200, 120);
        botaoComprar.addActionListener(ev -> {
            boolean sucesso = true; //funcionou a compra FacadeView.getInstance().comprarPropriedadeAtual();
            if (sucesso) {
                removerBotaoComprarSeExistir();
                atualizarPeao();
                repaint();
                System.out.println("PROPRIEDADE COMPRADA");
                // opcional: mostrar diálogo informando compra e saldo
            } else {
                System.out.println("SALDO ISUFICIENTE");// opcional: mostrar mensagem "saldo insuficiente" ou "já comprada"
            }
        });
        add(botaoComprar);
        revalidate();
        repaint();
    }

    private void removerBotaoComprarSeExistir() {
        if (botaoComprar != null && botaoComprar.getParent() != null) {
            remove(botaoComprar);
            revalidate();
            repaint();
        }
    }

    private void carregarImagensDados() {
        imagensDados = new Image[7]; // índice 0 não usado, 1-6 para os valores
        // setBackground(Cores.getInstance().corCorrespondente(FacadeView.getInstance().getCorJogadorAtual()));
        // cor do jogador

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

    public void atualizarPeao() {
        carregarImagemDoPeao();
        // Opcional: Atualiza o fundo se ele depender da cor do jogador
        setBackground(Cores.getInstance().corCorrespondente(FacadeView.getInstance().getCorJogadorAtual()));
        repaint();
    }

    private void inicializarCoordenadasCasas() {
        coordenadasCasas = new HashMap<>();
        // ajustar coordenadas
        coordenadasCasas.put(1, new Point(670, 650));
        coordenadasCasas.put(2, new Point(585, 650));
        coordenadasCasas.put(3, new Point(535, 650));
        coordenadasCasas.put(4, new Point(484, 650));
        coordenadasCasas.put(5, new Point(422, 650));
        coordenadasCasas.put(6, new Point(360, 650));
        coordenadasCasas.put(7, new Point(298, 650));
        coordenadasCasas.put(8, new Point(236, 650));
        coordenadasCasas.put(9, new Point(174, 650));
        coordenadasCasas.put(10, new Point(112, 650));
        coordenadasCasas.put(11, new Point(50, 650));

        coordenadasCasas.put(12, new Point(50, 590));
        coordenadasCasas.put(13, new Point(50, 530));
        coordenadasCasas.put(14, new Point(50, 470));
        coordenadasCasas.put(15, new Point(50, 410));
        coordenadasCasas.put(16, new Point(50, 350));
        coordenadasCasas.put(17, new Point(50, 290));
        coordenadasCasas.put(18, new Point(50, 230));
        coordenadasCasas.put(19, new Point(50, 170));
        coordenadasCasas.put(20, new Point(50, 110));
        coordenadasCasas.put(21, new Point(50, 50));

        coordenadasCasas.put(22, new Point(112, 50));
        coordenadasCasas.put(23, new Point(174, 50));
        coordenadasCasas.put(24, new Point(236, 50));
        coordenadasCasas.put(25, new Point(298, 50));
        coordenadasCasas.put(26, new Point(360, 50));
        coordenadasCasas.put(27, new Point(422, 50));
        coordenadasCasas.put(28, new Point(484, 50));
        coordenadasCasas.put(29, new Point(546, 50));
        coordenadasCasas.put(30, new Point(608, 50));
        coordenadasCasas.put(31, new Point(670, 50));

        coordenadasCasas.put(32, new Point(670, 110));
        coordenadasCasas.put(33, new Point(670, 170));
        coordenadasCasas.put(34, new Point(670, 230));
        coordenadasCasas.put(35, new Point(670, 290));
        coordenadasCasas.put(36, new Point(670, 350));
        coordenadasCasas.put(37, new Point(670, 410));
        coordenadasCasas.put(38, new Point(670, 470));
        coordenadasCasas.put(39, new Point(670, 530));
        coordenadasCasas.put(40, new Point(670, 590));
    }

    private void desenharPeao(Graphics2D g2d) {
        if (coordenadasCasas == null)
            return;

        int mapaLargura = LARGURA_MAPA;
        int mapaAltura = ALTURA_MAPA;
        int deslocamentoXMapa = (getWidth() - mapaLargura) / 2;
        int deslocamentoYMapa = (getHeight() - mapaAltura) / 2;

        int qtd = 0;
        try {
            qtd = FacadeModel.getInstance().getQtdJogadores();
        } catch (Exception e) {
            qtd = 0;
        }

        if (qtd <= 0) {
            // fallback: draw test pawn
            int posicaoAtual;
            try {
                int modeloPos = FacadeModel.getInstance().getPosJogadorAtual();
                posicaoAtual = modeloPos + 1;
            } catch (Exception ex) {
                posicaoAtual = this.posicaoDeTeste;
            }
            Point coordsRelativas = coordenadasCasas.get(posicaoAtual);
            if (coordsRelativas == null)
                return;
            int xPeao = deslocamentoXMapa + coordsRelativas.x - TAMANHO_PEAO / 2;
            int yPeao = deslocamentoYMapa + coordsRelativas.y - TAMANHO_PEAO / 2;
            if (imagemPeao != null)
                g2d.drawImage(imagemPeao, xPeao, yPeao, TAMANHO_PEAO, TAMANHO_PEAO, this);
            return;
        }

        // Group pawns by their map position so we can offset multiple pawns on the same
        // square
        java.util.Map<Integer, java.util.List<Integer>> posToPlayers = new java.util.HashMap<>();
        for (int i = 0; i < qtd; i++) {
            int posModelo = FacadeModel.getInstance().getPosicaoJogador(i); // 0-based
            posToPlayers.computeIfAbsent(posModelo, k -> new java.util.ArrayList<>()).add(i);
        }

        for (java.util.Map.Entry<Integer, java.util.List<Integer>> e : posToPlayers.entrySet()) {
            int posModelo = e.getKey();
            java.util.List<Integer> playersHere = e.getValue();
            int countHere = playersHere.size();
            int posMapa = posModelo + 1; // 1-based for coordenadasCasas
            Point coordsRelativas = coordenadasCasas.get(posMapa);
            if (coordsRelativas == null)
                continue;

            // base position (center of the house)
            int baseX = deslocamentoXMapa + coordsRelativas.x;
            int baseY = deslocamentoYMapa + coordsRelativas.y;

            // compute offsets depending on how many pawns are on the same house
            for (int idx = 0; idx < countHere; idx++) {
                int playerIndex = playersHere.get(idx);
                int offsetX = 0;
                int offsetY = 0;

                if (countHere == 1) {
                    offsetX = 0;
                    offsetY = 0;
                } else if (countHere == 2) {
                    // left / right
                    offsetX = (idx == 0) ? -TAMANHO_PEAO / 2 : TAMANHO_PEAO / 2;
                    offsetY = 0;
                } else if (countHere == 3) {
                    // triangle: top, bottom-left, bottom-right
                    if (idx == 0) {
                        offsetX = 0;
                        offsetY = -TAMANHO_PEAO / 2;
                    }
                    if (idx == 1) {
                        offsetX = -TAMANHO_PEAO / 2;
                        offsetY = TAMANHO_PEAO / 4;
                    }
                    if (idx == 2) {
                        offsetX = TAMANHO_PEAO / 2;
                        offsetY = TAMANHO_PEAO / 4;
                    }
                } else if (countHere == 4) {
                    // grid 2x2
                    if (idx == 0) {
                        offsetX = -TAMANHO_PEAO / 2;
                        offsetY = -TAMANHO_PEAO / 2;
                    }
                    if (idx == 1) {
                        offsetX = TAMANHO_PEAO / 2;
                        offsetY = -TAMANHO_PEAO / 2;
                    }
                    if (idx == 2) {
                        offsetX = -TAMANHO_PEAO / 2;
                        offsetY = TAMANHO_PEAO / 2;
                    }
                    if (idx == 3) {
                        offsetX = TAMANHO_PEAO / 2;
                        offsetY = TAMANHO_PEAO / 2;
                    }
                } else {
                    // more than 4: place in a small circle
                    double angle = (2 * Math.PI * idx) / countHere;
                    int radius = TAMANHO_PEAO;
                    offsetX = (int) Math.round(Math.cos(angle) * radius);
                    offsetY = (int) Math.round(Math.sin(angle) * radius);
                }

                int xPeao = baseX + offsetX - TAMANHO_PEAO / 2;
                int yPeao = baseY + offsetY - TAMANHO_PEAO / 2;
                CorPeao cor = FacadeModel.getInstance().getCorJogador(playerIndex);
                Image img = imagensPinos != null ? imagensPinos.get(cor) : null;
                if (img != null) {
                    g2d.drawImage(img, xPeao, yPeao, TAMANHO_PEAO, TAMANHO_PEAO, this);
                }
            }
        }
    }

    private void carregarImagemDoPeao() {
        // deprecated: we now use imagensPinos map
    }

    private void carregarImagensPinos() {
        imagensPinos = new HashMap<>();
        for (controller.CorPeao cor : controller.CorPeao.values()) {
            String nome = cor.toString().toLowerCase();
            try {
                File file = new File("src/resources/Pinos/" + nome + ".png");
                if (file.exists()) {
                    Image imagemOriginal = ImageIO.read(file);
                    imagensPinos.put(cor,
                            imagemOriginal.getScaledInstance(TAMANHO_PEAO, TAMANHO_PEAO, Image.SCALE_SMOOTH));
                }
            } catch (IOException e) {
                System.err.println("Erro ao carregar imagem do pino " + nome + ": " + e.getMessage());
            }
        }
    }

    /**
     * Desenha os dois dados no centro do painel com distância de 100 pixels entre
     * os centros
     */
    private void desenharDados(Graphics2D g2d) {
        if (!dadosVisiveis || dados[0] == 0 || dados[1] == 0)
            return;
        if (imagensDados[dados[0]] == null || imagensDados[dados[1]] == null)
            return;

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
                imagemCartaPropriedade = ImageIO.read(file).getScaledInstance(LARGURA_CARTA, ALTURA_CARTA, Image.SCALE_SMOOTH);
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
        if (imagemCartaPropriedade == null)
            return;

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
        desenharPeao(g2d);
        desenharCartaPropriedade(g2d);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(janelaPrincipal.LARG_DEFAULT, janelaPrincipal.ALT_DEFAULT);
    }

    @Override
    public void notify(PartidaEvent event) {
        if (event == null)
            return;

        switch (event.type) {
            case DICE_ROLLED:
                try {
                    int[] dadosRolados = (int[]) event.payload;
                    if (dadosRolados != null && dadosRolados.length == 2) {
                        this.dados = new int[] { dadosRolados[0], dadosRolados[1] };
                        this.dadosVisiveis = true;
                    }
                } catch (Exception ex) {
                    // ignore malformed payload
                }
                break;
            case MOVE:
                // payload is Object[] { pos(Integer), dados(int[]) }
                try {
                    Object[] payload = (Object[]) event.payload;
                    Integer pos = (Integer) payload[0];
                    // atualiza peao e redesenha
                    atualizarPeao();
                    // if landed on property, property event will follow; otherwise hide
                    if (!FacadeModel.getInstance().ehPropriedade(pos)) {
                        ocultarCartaPropriedade();
                    }
                } catch (Exception ex) {
                    atualizarPeao();
                }
                break;
            case PROPERTY_LANDED:
                try {
                    Integer pos = (Integer) event.payload;
                    // resources are numbered 1-based; convert
                    exibirCartaPropriedade(Integer.toString(pos));
                } catch (Exception ex) {
                    // ignore
                }
                break;
            case NEXT_PLAYER:
                // re-enable the roll button for the next player
                if (botaoDados.getParent() == null) {
                    add(botaoDados);
                    revalidate();
                    repaint();
                }
                // also update pawn image/background
                atualizarPeao();
                break;
            case INFO:
                // ignore or log
                break;
        }
        // sempre repinta ao final de um evento
        repaint();
    }
}