package view;

import java.awt.*;

import javax.swing.*;

class Janela extends JFrame {
	private static final long serialVersionUID = 1L;
	public final int LARG_DEFAULT=1280;
	public final int ALT_DEFAULT=800;
	
	private CardLayout cardLayout;
    private JPanel cardPanel;
	
	public Janela(String nome) {	
		super(nome);
//		setSize(LARG_DEFAULT, ALT_DEFAULT);
//		setDefaultCloseOperation(EXIT_ON_CLOSE);
//		setLocationRelativeTo(null); // Centralizado
		
		inicializarComponentes();
        configurarJanela();
	}
	
	void inicializarComponentes() {
		cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        
        cardPanel.add(new PainelJanelaInicial(this), Telas.INICIO);
//        cardPanel.add(new PainelTabuleiro(this), Telas.TABULEIRO);
        
        add(cardPanel);
	}
	
	void configurarJanela() {
		setSize(LARG_DEFAULT, ALT_DEFAULT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centralizado
	}
}