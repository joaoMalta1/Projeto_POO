package view;

import java.awt.*;

import javax.swing.*;

class PainelJanelaInicial extends JPanel{
    private static final long serialVersionUID = 1L;
    
	private BotaoEstilizado bNovoJogo, bCarregar; 
	private TituloEstilizado titulo;
	private Janela janela;
	
	PainelJanelaInicial(Janela janela){
		this.janela = janela;
		inicializarComponentes();
        configurarLayout();
        configurarEventos();
	}
	
	private void inicializarComponentes() {
		titulo = new TituloEstilizado("Banco Imobiliário");
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		bNovoJogo = new BotaoEstilizado("Novo Jogo", 300, 100);
		bCarregar = new BotaoEstilizado("Carregar Jogo", 300, 100);
                
        bNovoJogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        bCarregar.setAlignmentX(Component.CENTER_ALIGNMENT);
	}
	
	
    private void configurarLayout() {
		setBackground(Color.WHITE);	
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(40, 20, 40, 20));
        
        add(Box.createRigidArea(new Dimension(0, 70)));
        
        add(titulo);
        
        add(Box.createRigidArea(new Dimension(0, 50)));
        
        
        add(bNovoJogo);
        
        add(Box.createRigidArea(new Dimension(0, 20)));
        
        add(bCarregar);
    }
    
    
    private void configurarEventos() {
        bNovoJogo.addActionListener(e -> { 
        	janela.mostrarTela(Telas.QUANTIDADE_JOGADORES);  });
        
        bCarregar.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, 
                "Funcionalidade em desenvolvimento", 
                "Carregar Jogo", 
                JOptionPane.INFORMATION_MESSAGE);
        });
    }
  }
