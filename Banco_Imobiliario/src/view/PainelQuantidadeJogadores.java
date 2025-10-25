package view;

import java.awt.*;

import javax.swing.*;

class PainelQuantidadeJogadores extends JPanel {
    private static final long serialVersionUID = 1L;
    
	private TituloEstilizado titulo;
	private Janela janela;
	private JComboBox<Integer> dropdown;
	private BotaoEstilizado voltar, confirmar;
    
    PainelQuantidadeJogadores(Janela janela){
    	this.janela = janela;
    	inicializarComponentes();
    	configurarLayout();
        configurarEventos();
    }
    
    private void inicializarComponentes() {
    	titulo = new TituloEstilizado("Quantos Jogadores?");
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        confirmar = new BotaoEstilizado("Confirmar", 300, 100);
        voltar 	  = new BotaoEstilizado("Voltar", 300, 100);
        confirmar.setAlignmentX(Component.CENTER_ALIGNMENT);
        voltar.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        Integer[] opcoes = {3, 4, 5, 6};
        dropdown = new JComboBox<>(opcoes);
        dropdown.setMaximumSize(new Dimension(300, 50));
        dropdown.setFont(new Font("Arial", Font.PLAIN, 20));
        dropdown.setAlignmentX(Component.CENTER_ALIGNMENT);
    }
    
    private void configurarLayout() {
    	setBackground(Color.WHITE);	
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(40, 20, 40, 20));
        
        add(Box.createRigidArea(new Dimension(0, 70)));
        
        add(titulo);
        
        add(Box.createRigidArea(new Dimension(0, 80)));
        
        add(dropdown);
        
        add(Box.createRigidArea(new Dimension(0, 30)));
        
        add(confirmar);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(voltar);
    }
    
    private void configurarEventos() {
    	voltar.addActionListener(e -> {
            janela.mostrarTela(Telas.INICIO);
        });

        confirmar.addActionListener(e -> {
            int qtd_jogadores = (int) dropdown.getSelectedItem();
            FacadeView.getInstance().setQtdJogadores(qtd_jogadores);
            janela.mostrarTela(Telas.CRIACAO_JOGADORES);
        });
    }
}