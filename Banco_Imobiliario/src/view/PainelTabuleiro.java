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



import javax.swing.Timer;
import java.awt.event.ActionEvent;     //  para o Timer
import java.awt.event.ActionListener;

public class PainelTabuleiro extends JPanel {

    private static final long serialVersionUID = 1L;
    private final Janela janelaPrincipal;
    private BotaoEstilizado botaoDados;
    private int[] dados = {1,1};
    private Image imagemMapa; // A imagem do seu mapa
    private Image[] imagensDados;
    private boolean dadosVisiveis = false;
    private Image imagemPeao;
    private final int TAMANHO_PEAO = 25;
    private Map<Integer, Point> coordenadasCasas;
    
    
    
    private Timer testeTimer; // timer
    private int posicaoDeTeste = 1;
    
    // simensao mapa 
    private final int LARGURA_MAPA = 700; 
    private final int ALTURA_MAPA = 700;

    public PainelTabuleiro(Janela janela) {
        this.janelaPrincipal = janela;
        // Cor de fundo do JPanel (visível se a imagem for menor ou transparente)
        setBackground(Cores.getInstance().corCorrespondente(FacadeView.getInstance().getCorJogadorAtual())); 
        
        carregarImagemDoMapa();
        carregarImagensDados();
        criarBotaoDados();

        inicializarCoordenadasCasas(); 
        carregarImagemDoPeao();
        iniciarTesteDeLoop();
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
        imagensDados = new Image[7]; // índice 0 não usado, 1-6 para os valores
        //        setBackground(Cores.getInstance().corCorrespondente(FacadeView.getInstance().getCorJogadorAtual()));  cor do jogador 

        for (int i = 1; i <= 6; i++) {
            try {
                // Caminho relativo: volta uma pasta (../) pois estamos em /view
                // e as imagens estão em /resources/Dados
                File file = new File("src/resources/Dados/" + i + ".png");
                
                if (file.exists()) {
                    Image imagemOriginal = ImageIO.read(file);
                    // Redimensiona para um tamanho adequado (ajuste conforme necessário)
                    imagensDados[i] = imagemOriginal.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                } else {
                    System.err.println("ERRO: Imagem do dado " + i + " não encontrada: " + file.getAbsolutePath());
                    imagensDados[i] = null;
                }
            } catch (IOException e) {
                System.err.println("Erro ao carregar imagem do dado " + i + ": " + e.getMessage());
                imagensDados[i] = null;
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
    
    
    private void iniciarTesteDeLoop() {
        int velocidadeDoTeste = 500; // 500ms (meio segundo) por casa. Mude se quiser mais rápido/lento.

        ActionListener loopListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 1. Avança para a próxima posição
                posicaoDeTeste++;
                
                // 2. Se passar da 40 (última casa), volta para a 1
                if (posicaoDeTeste > 40) {
                    posicaoDeTeste = 1;
                }
                
                // 3. Manda o painel se redesenhar (o que vai chamar o desenharPeao)
                repaint();
            }
        };
        
        // Cria e inicia o Timer
        testeTimer = new Timer(velocidadeDoTeste, loopListener);
        testeTimer.start();
    }
    
    

    private void desenharPeao(Graphics2D g2d) {
        if (imagemPeao == null || coordenadasCasas == null) {
            return;
        }
        
        // ASSUME-SE que FacadeView.getInstance().getPosicaoJogadorAtual() retorna o ID da casa (ex: 1, 2, 3...)
        int posicaoAtual = this.posicaoDeTeste ; //FacadeView.getInstance().getPosicaoJogadorAtual();
        Point coordsRelativas = coordenadasCasas.get(posicaoAtual);
        
        if (coordsRelativas == null) {
            System.err.println("AVISO: Coordenadas da casa " + posicaoAtual + " não encontradas. Peão não desenhado.");
            return;
        }
        
        // Calcular o deslocamento do mapa (para centralizar o peão na tela)
        int mapaLargura = LARGURA_MAPA; 
        int mapaAltura = ALTURA_MAPA; 
        int deslocamentoXMapa = (getWidth() - mapaLargura) / 2;
        int deslocamentoYMapa = (getHeight() - mapaAltura) / 2;
        
        // Calcular a posição ABSOLUTA do canto superior esquerdo do peão
        // coordsRelativas armazena o CENTRO da casa (relativo ao canto do mapa)
        int xPeao = deslocamentoXMapa + coordsRelativas.x - TAMANHO_PEAO / 2;
        int yPeao = deslocamentoYMapa + coordsRelativas.y - TAMANHO_PEAO / 2;
        
        // Desenhar o peão
        g2d.drawImage(imagemPeao, xPeao, yPeao, TAMANHO_PEAO, TAMANHO_PEAO, this);
    }
    
    

        private void carregarImagemDoPeao() {
            Object corPeaoEnum = FacadeView.getInstance().getCorJogadorAtual(); 
            //passa enum pra string lowercase
            String corJogador = corPeaoEnum.toString().toLowerCase();
            
            try {
            	File file = new File("src/resources/Pinos/" + corJogador + ".png"); 
                
                if (file.exists()) {
                    Image imagemOriginal = ImageIO.read(file);
                    //ajusta tamanho peao
                    imagemPeao = imagemOriginal.getScaledInstance(
                        TAMANHO_PEAO, TAMANHO_PEAO, Image.SCALE_SMOOTH);
                } else {
                    System.err.println("ERRO: Imagem do peão não encontrada para a cor " + corJogador + " no caminho: " + file.getAbsolutePath());
                    imagemPeao = null;
                }
            } catch (IOException e) {
                System.err.println("Erro ao carregar imagem do peão: " + e.getMessage());
                imagemPeao = null;
            }
        }
    
        
    /**
     * Desenha os dois dados no centro do painel com distância de 100 pixels entre os centros
     */
    private void desenharDados(Graphics2D g2d) {
        if (!dadosVisiveis || dados[0] == 0 || dados[1]== 0) {
            return;
        }
        
        // Verifica se as imagens dos dados estão carregadas
        if (imagensDados[dados[0]] == null || imagensDados[dados[1]] == null) {
            System.err.println("Erro: Imagens dos dados não carregadas para os valores " + dados[0] + " e " + dados[1]);
            return;
        }
        
        // Calcula posições para centralizar os dados
        int larguraDado = imagensDados[dados[0]].getWidth(this);
        int alturaDado = imagensDados[dados[0]].getHeight(this);
        
        // Distância entre centros = 100 pixels
        int distanciaCentros = 100;
        
        // Posição X do centro do conjunto de dados
        int centroX = getWidth() / 2;
        
        // Posição Y do centro (verticalmente)
        int centroY = getHeight() / 2;
        
        // Calcula posições X para cada dado
        int x1 = centroX - distanciaCentros / 2 - larguraDado / 2;
        int x2 = centroX + distanciaCentros / 2 - larguraDado / 2;
        
        // Posição Y (mesma para ambos)
        int y = centroY - alturaDado / 2;
        
        // Desenha os dados
        g2d.drawImage(imagensDados[dados[0]], x1, y, this);
        g2d.drawImage(imagensDados[dados[1]], x2, y, this);
    }
    
    /**
     * Carrega a imagem do mapa a partir do disco.
     */
    private void carregarImagemDoMapa() {
        try {
            // *** 1. MUDE ESTE CAMINHO PARA O LOCAL REAL DA SUA IMAGEM DO MAPA ***
            File file = new File("Imagens-01/tabuleiro.png");
            
            if (file.exists()) {
                imagemMapa = ImageIO.read(file); 
                
                // Redimensiona a imagem para o tamanho desejado (opcional, pode remover)
                imagemMapa = imagemMapa.getScaledInstance(
                    LARGURA_MAPA, ALTURA_MAPA, Image.SCALE_SMOOTH);
            } else {
                System.err.println("ERRO: Imagem do mapa não encontrada no caminho especificado: " + file.getAbsolutePath());
                imagemMapa = null;
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar imagem do mapa: " + e.getMessage());
            imagemMapa = null;
        }
    }

    /**
     * Método principal para desenhar o conteúdo do painel.
     */
    @Override
    protected void paintComponent(Graphics g) {
        // 1. LIMPAR O PAINEL
        // Chama a implementação do JPanel, que preenche o painel com o background.
        // Isso garante que o que estava na tela antes seja "limpo".
        super.paintComponent(g); 
        
        // 2. Obtém o contexto Graphics2D
        Graphics2D g2d = (Graphics2D) g;

        // 3. Desenha a Imagem do Mapa
        if (imagemMapa != null) {
            
            // Centraliza o mapa no Painel
            int mapaLargura = imagemMapa.getWidth(this);
            int mapaAltura = imagemMapa.getHeight(this);
            int x = (getWidth() - mapaLargura) / 2;
            int y = (getHeight() - mapaAltura) / 2;
            
            // Método drawImage(): Desenha a imagem do mapa na posição calculada
            g2d.drawImage(
                imagemMapa, 
                x, 
                y, 
                this // ImageObserver
            );
            
        } else {
            // Mensagem de fallback se a imagem não carregar
            g2d.setColor(Color.RED);
            g2d.fillRect(0, 0, getWidth(), getHeight());
            g2d.setColor(Color.WHITE);
            g2d.drawString("Erro: Imagem do Mapa não Carregada!", 50, 50);
        }
        
        // Desenha os dados se estiverem visíveis
        desenharDados(g2d);
        desenharPeao(g2d);
    }
    
    // Garante que o painel ocupe todo o espaço da Janela, se o CardLayout permitir.
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(janelaPrincipal.LARG_DEFAULT, janelaPrincipal.ALT_DEFAULT);
    }
}