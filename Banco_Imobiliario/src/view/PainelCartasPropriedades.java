package view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class PainelCartasPropriedades extends JPanel {
    private static final long serialVersionUID = 1L;
    private Janela janela;

    public PainelCartasPropriedades(Janela janela) {
        this.janela = janela;
        setLayout(new FlowLayout(FlowLayout.CENTER, 40, 30));
        setBackground(new Color(220, 220, 220));

        carregarCartas();
    }

    private void carregarCartas() {
        try {
        	Image imagem = ImageIO.read(getClass().getResource("/39.png"));
            add(new CartaPropriedadeView(imagem));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
