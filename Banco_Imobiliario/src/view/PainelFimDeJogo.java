package view;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

class PainelFimDeJogo extends JPanel {
	private static final long serialVersionUID = 1L;
	private final Janela janelaPrincipal;
	private final String nomeVencedor;
	private TituloEstilizado titulo;
	private BotaoEstilizado bVoltarAoInicio;
	
	PainelFimDeJogo(Janela janela){
		janelaPrincipal = janela;
		
		System.out.println(FacadeView.getInstance().getJogadorAtual());
		
		nomeVencedor = FacadeView.getInstance().getNomeJogadorAtual();
		
		inicializarComponentes();
		configurarLayout();
		configurarEventos();
	}
	
	
	private void inicializarComponentes() {
		titulo = new TituloEstilizado("Parábens, " + nomeVencedor + " você ganhou! :D");
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		bVoltarAoInicio = new BotaoEstilizado("Voltar ao inicio", 300, 100);
                
		bVoltarAoInicio.setAlignmentX(Component.CENTER_ALIGNMENT);
	}
	
	private void configurarLayout() {
		// fundo: cor estilizada do jogador atual
		setBackground(Cores.getInstance().corCorrespondente(FacadeView.getInstance().getCorJogadorAtual()));
		
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(40, 20, 40, 20));
        
        add(Box.createRigidArea(new Dimension(0, 70)));
        
        add(titulo);
        
        add(Box.createRigidArea(new Dimension(0, 100)));
        
        
        add(bVoltarAoInicio);
    }
	
	private void configurarEventos() {
		bVoltarAoInicio.addActionListener(e -> { 
        	FacadeView.getInstance().reset();
			janelaPrincipal.mostrarTela(Telas.INICIO);
        	});
    }
}
