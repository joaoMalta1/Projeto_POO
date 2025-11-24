package view;

import java.awt.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

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
        	JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("Arquivos de jogo (*.txt)", "txt"));
            
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                boolean sucesso = FacadeView.getInstance().carregarJogo(fileChooser.getSelectedFile().getAbsolutePath());
                if(sucesso) {
                	JOptionPane.showMessageDialog(this, "Jogo carregado com sucesso!");
                	janela.mostrarTela(Telas.TABULEIRO);
                }
                else {
                	JOptionPane.showMessageDialog(this, "Erro ao carregar jogo...");
                }
            }
        });
    }
  }
