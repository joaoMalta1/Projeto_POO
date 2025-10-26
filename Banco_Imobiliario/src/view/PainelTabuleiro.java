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
    private int[] dados = {1,1};
    private Image imagemMapa; // A imagem do seu mapa
    private Image[] imagensDados;
    private boolean dadosVisiveis = false;
    
    // Dimensões que você espera que o mapa tenha (opcional, se quiser forçar o tamanho)
    private final int LARGURA_MAPA = 700; 
    private final int ALTURA_MAPA = 700;

    public PainelTabuleiro(Janela janela) {
        this.janelaPrincipal = janela;
        // Cor de fundo do JPanel (visível se a imagem for menor ou transparente)
        setBackground(Color.DARK_GRAY); 
        
        carregarImagemDoMapa();
        carregarImagensDados();
        criarBotaoDados();
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
    }
    
    // Garante que o painel ocupe todo o espaço da Janela, se o CardLayout permitir.
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(janelaPrincipal.LARG_DEFAULT, janelaPrincipal.ALT_DEFAULT);
    }
}