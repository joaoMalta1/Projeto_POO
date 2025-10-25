package view;

import javax.swing.*;
import java.awt.*;

public class CartaPropriedadeView extends JPanel {
    private static final long serialVersionUID = 1L;

    private Image imagemCarta;

    public CartaPropriedadeView(Image imagemCarta) {
        this.imagemCarta = imagemCarta;
        setPreferredSize(new Dimension(229, 229));
        setOpaque(false);
    }

    public CartaPropriedadeView(Image imagemCarta, String nome, double precoCompra, double precoPassagem) {
        this(imagemCarta);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // fundo da carta
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
        g2.setColor(Color.GRAY);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);

        // desenha a imagem centralizada
        if (imagemCarta != null) {
            int imgWidth = imagemCarta.getWidth(this);
            int imgHeight = imagemCarta.getHeight(this);
            int x = (getWidth() - imgWidth) / 2;
            int y = (getHeight() - imgHeight) / 2;
            g2.drawImage(imagemCarta, x, y, this);
        }


        g2.dispose();
    }
}
