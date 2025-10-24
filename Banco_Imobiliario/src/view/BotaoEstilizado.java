package view;

import java.awt.*;

import javax.swing.*;

class BotaoEstilizado extends JButton {
    private static final long serialVersionUID = 1L;
    
    BotaoEstilizado(String texto, int tam_x, int tam_y){
    	super(texto);
    	
    	Font fonte = new Font("Arial", Font.BOLD, 20);
        setFont(fonte);
        
        Color cor = new Color(70, 130, 180);
        setBackground(cor);
        setForeground(Color.WHITE);
        
        setMaximumSize(new Dimension(tam_x, tam_y));
    }
}
