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
    
    private Image imagemMapa; // A imagem do seu mapa
    
    // Dimensões que você espera que o mapa tenha (opcional, se quiser forçar o tamanho)
    private final int LARGURA_MAPA = 700; 
    private final int ALTURA_MAPA = 700;

    public PainelTabuleiro(Janela janela) {
        this.janelaPrincipal = janela;
        // Cor de fundo do JPanel (visível se a imagem for menor ou transparente)
        setBackground(Color.DARK_GRAY); 
        
        carregarImagemDoMapa();
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
        
        // Se necessário, adicione outros elementos do jogo (peças, texto, etc.) aqui.
    }
    
    // Garante que o painel ocupe todo o espaço da Janela, se o CardLayout permitir.
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(janelaPrincipal.LARG_DEFAULT, janelaPrincipal.ALT_DEFAULT);
    }
}