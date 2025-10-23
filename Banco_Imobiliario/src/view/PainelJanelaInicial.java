package view;

import java.awt.*;

import javax.swing.*;

class PainelJanelaInicial extends JPanel{
    private static final long serialVersionUID = 1L;
    
	private JButton bNovoJogo; 
	private JButton bCarregar; 
	private JLabel titulo;
	private Janela janela;
	
	PainelJanelaInicial(Janela janela){
		this.janela = janela;
		inicializarComponentes();
        configurarLayout();
        configurarEventos();
        
		add(bNovoJogo);
		add(bCarregar);
		setBackground(Color.CYAN);
	}
	
	private void inicializarComponentes() {
		titulo = new JLabel("Banco Imobiliário");
        titulo.setFont(new Font("Arial", Font.BOLD, 28));
        titulo.setForeground(new Color(44, 62, 80));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		bNovoJogo = new JButton("Novo Jogo");
        bCarregar = new JButton("Carregar Jogo");
        
        Font fonteBotoes = new Font("Arial", Font.BOLD, 20);
        bNovoJogo.setFont(fonteBotoes);
        bCarregar.setFont(fonteBotoes);
        
        Color corBotoes = new Color(70, 130, 180);
        bNovoJogo.setBackground(corBotoes);
        bCarregar.setBackground(corBotoes);
        bNovoJogo.setForeground(Color.WHITE);
        bCarregar.setForeground(Color.WHITE);
        
        bNovoJogo.setFocusPainted(false);
        bCarregar.setFocusPainted(false);
        bNovoJogo.setBorder(BorderFactory.createEmptyBorder(12, 40, 12, 40));
        bCarregar.setBorder(BorderFactory.createEmptyBorder(12, 40, 12, 40));
	}
    private void configurarLayout() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(240, 248, 255));
        
        // Cria espaço flexível no topo para centralizar verticalmente
        add(Box.createVerticalGlue());
        
        // Adiciona título
        add(titulo);
        
        // Separação
        add(Box.createRigidArea(new Dimension(0, 80)));
        
        bNovoJogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        bNovoJogo.setMinimumSize(new Dimension(180, 45));
        bNovoJogo.setPreferredSize(new Dimension(300, 70));
        bNovoJogo.setMaximumSize(new Dimension(400, 80));
        add(bNovoJogo);
        
        // Separação
        add(Box.createRigidArea(new Dimension(0, 100)));
        
        bCarregar.setAlignmentX(Component.CENTER_ALIGNMENT);
        bCarregar.setMinimumSize(new Dimension(180, 45));
        bCarregar.setPreferredSize(new Dimension(300, 70));
        bCarregar.setMaximumSize(new Dimension(400, 80));
        add(bCarregar);
        
        // Cria espaço flexível na base para centralizar verticalmente
        add(Box.createVerticalGlue());
        
        // Adiciona borda interna
        setBorder(BorderFactory.createEmptyBorder(40, 20, 40, 20));
    }
    private void configurarEventos() {
        bNovoJogo.addActionListener(e -> {
//            System.out.println("Iniciando novo jogo...");
//            janela.mostrarTela(Telas.TABULEIRO);
        	JOptionPane.showMessageDialog(this, 
                    "Funcionalidade em desenvolvimento", 
                    "Novo Jogo", 
                    JOptionPane.INFORMATION_MESSAGE);
            });
        
        bCarregar.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, 
                "Funcionalidade em desenvolvimento", 
                "Carregar Jogo", 
                JOptionPane.INFORMATION_MESSAGE);
        });
    }
  }
