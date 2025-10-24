package view;

import java.awt.*;

import javax.swing.*;

class TituloEstilizado extends JLabel {
    private static final long serialVersionUID = 1L;
	
	TituloEstilizado(String texto){
    	super(texto);
        setFont(new Font("Arial", Font.BOLD, 28));
        setForeground(new Color(44, 62, 80));
    }
}
