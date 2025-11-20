package view;

import java.awt.*;

import javax.swing.*;

import controller.CorPeao;

class PainelCriacaoJogadores extends JPanel {
    private static final long serialVersionUID = 1L;
    
	private TituloEstilizado perguntaNome, perguntaCor;
	private Janela janela;
	private JComboBox<CorPeao> dropdown;
	private BotaoEstilizado voltar, confirmar;
	private JTextField campoNome;
    
	PainelCriacaoJogadores(Janela janela){
    	this.janela = janela;
    	inicializarComponentes();
    	configurarLayout();
        configurarEventos();
    }
    
    private void inicializarComponentes() {
    	perguntaNome = new TituloEstilizado("Qual o nome do Jogador nr" + 
    ((Integer)(FacadeView.getInstance().getJogadorAtual()+1)).toString() 
    +  "?");
    	perguntaNome.setAlignmentX(Component.CENTER_ALIGNMENT);
    	
    	perguntaCor = new TituloEstilizado("Qual a cor do Peão?");
    	perguntaCor.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        confirmar = new BotaoEstilizado("Confirmar", 300, 100);
        voltar 	  = new BotaoEstilizado("Voltar", 300, 100);
        confirmar.setAlignmentX(Component.CENTER_ALIGNMENT);
        voltar.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        dropdown = new JComboBox<>(CorPeao.values());
        dropdown.setMaximumSize(new Dimension(300, 50));
        dropdown.setFont(new Font("Arial", Font.PLAIN, 20));
        dropdown.setAlignmentX(Component.CENTER_ALIGNMENT);
        
     // Novo campo de texto
        campoNome = new JTextField("Nome único de tamanho [1,8]");
        campoNome.setMaximumSize(new Dimension(300, 50));
        campoNome.setFont(new Font("Arial", Font.PLAIN, 20));
        campoNome.setAlignmentX(Component.CENTER_ALIGNMENT);
        campoNome.setHorizontalAlignment(JTextField.CENTER);
    }
    
    private void configurarLayout() {
    	setBackground(Color.WHITE);	
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(40, 20, 40, 20));
        
        add(Box.createRigidArea(new Dimension(0, 70)));
        add(perguntaNome);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(campoNome);
        
        add(Box.createRigidArea(new Dimension(0, 80)));
        add(perguntaCor);
        add(Box.createRigidArea(new Dimension(0, 20)));
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
        	eventoConfirmar() ;
        });
    }
    
    private void recarregarPainel() {
    perguntaNome.setText("Qual o nome do Jogador nr" + 
            (FacadeView.getInstance().getJogadorAtual() + 1) + "?");
        
        campoNome.setText("Nome único de tamanho [1,8]");
                
        repaint();
    }
    
    boolean controleJaExiste(String nome, CorPeao cor) {
    	FacadeView ctrl = FacadeView.getInstance();
    	if(ctrl.nomeJaExiste(nome) && ctrl.corJaExiste(cor)) {
    		JOptionPane.showMessageDialog(this, 
                    "Nome e Cor já existem", 
                    "Entrada Incorreta", 
                    JOptionPane.INFORMATION_MESSAGE);
    		return true;
    	}
    	if(ctrl.nomeJaExiste(nome)) {
    		JOptionPane.showMessageDialog(this, 
                    "Nome já existe", 
                    "Entrada Incorreta", 
                    JOptionPane.INFORMATION_MESSAGE);
    		return true;
    	}
    	if(ctrl.corJaExiste(cor)) {
    		JOptionPane.showMessageDialog(this, 
                    "Cor já existe", 
                    "Entrada Incorreta", 
                    JOptionPane.INFORMATION_MESSAGE);
    		return true;
    	}
    	return false;
    }
    
    boolean controleNomeValido(String nome) {
    	if(nome.length() >= 1 && nome.length() <= 8)
    		return true;
    	JOptionPane.showMessageDialog(this, 
                "Nome deve ter tamanho no intervalo [1,8]", 
                "Entrada Incorreta", 
                JOptionPane.INFORMATION_MESSAGE);
    	return false;
    }
    
    void eventoConfirmar() {
    	FacadeView ctrl = FacadeView.getInstance();
    	String nome = campoNome.getText();
    	CorPeao cor = (CorPeao)dropdown.getSelectedItem();
    	
    	if(!controleNomeValido(nome)) {
    		recarregarPainel(); 
    		return;    		
    	}
    	
    	if(controleJaExiste(nome, cor)) {
    		recarregarPainel(); 
    		return;    		
    	}
    	
    	if(!ctrl.addJogador(ctrl.getJogadorAtual(), nome, cor)) {
    		JOptionPane.showMessageDialog(this, 
                    "Não foi possível adicionar jogador", 
                    "Erro", 
                    JOptionPane.INFORMATION_MESSAGE);
    		return;
    	}
    	
//        caso já tenha adicionado todos os jogadores e pode ir para o jogo
        if(ctrl.ehUltimoJogador()) {
        	ctrl.finalizaCriacao();
//        	String msg = "";
//        	for(int i = 0; i < ctrl.getQtdJogadores(); i++) {
//        		msg += ctrl.getNomeJogador(i) + " ";
//        	}
        	janela.mostrarTela(Telas.TABULEIRO);
        	return;
        }
        
        ctrl.proxJogadorASerCriado();
        
        recarregarPainel();
        return;
    }
}