package view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class PainelCartasPropriedades extends JPanel {
    private static final long serialVersionUID = 1L;
    private Janela janela;
    private int posicao;
    
    public PainelCartasPropriedades(Janela janela, int posicao) {
        this.janela = janela;
        this.posicao = posicao;
        
        setLayout(new FlowLayout(FlowLayout.CENTER, 40, 30));
        setBackground(new Color(220, 220, 220));

        carregarCartas(posicao);
    }

    private void carregarCartas(int posicao) {
        try {
        	Image imagem = ImageIO.read(getClass().getResource("/"+posicao+".png"));
            add(new CartaPropriedadeView(imagem));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
