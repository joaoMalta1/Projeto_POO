package view;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.*;

class Janela extends JFrame {
	private static final long serialVersionUID = 1L;
	public final int LARG_DEFAULT=1280;
	public final int ALT_DEFAULT=800;
	
	private CardLayout cardLayout;
    private JPanel cardPanel;
    private String telaAtual;
	
	public Janela(String nome) {	
		super(nome);
		
		inicializarComponentes();
        configurarJanela();
        mostrarTelaInicial();
	}	
	
	void inicializarComponentes() {
		cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        
//        cardPanel.add(new PainelJanelaInicial(this), Telas.INICIO);
////        cardPanel.add(new PainelTabuleiro(this), Telas.TABULEIRO);
//        cardPanel.add(new PainelQuantidadeJogadores(this), Telas.CRIACAO_JOGADORES);
//        
//        mostrarTela(Telas.INICIO);
        add(cardPanel);
	}
	
	void configurarJanela() {
		setSize(LARG_DEFAULT, ALT_DEFAULT);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null); // Centralizado

//		impedir que a janela seja extendida para um tamanho maior que 1280x800
		Dimension maxSize = new Dimension(LARG_DEFAULT, ALT_DEFAULT); 
		setMaximumSize(maxSize);
		// Add listener to enforce maximum size
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Window window = (Window) e.getComponent();
                Dimension size = window.getSize();
                Dimension enforcedSize = new Dimension(
                    Math.min(size.width, maxSize.width),
                    Math.min(size.height, maxSize.height)
                );
                if (!size.equals(enforcedSize)) {
                    window.setSize(enforcedSize);
                }
            }
        });
	}
	
	void mostrarTelaInicial() {
    	cardPanel.add(new PainelJanelaInicial(this), Telas.INICIO);
        cardLayout.show(cardPanel, Telas.INICIO);
        telaAtual = Telas.INICIO;
    }
	
//	TODO: melhorar isso para descarregar janela antiga e carregar nova
//	void mostrarTela(String nomeTela) {
//		cardLayout.show(cardPanel, nomeTela);
//	}
	

//    Método para trocar de tela criando uma nova instância
    public void mostrarTela(String nomeTela) {
    	JPanel novaTela = criarTela(nomeTela);
    	if(novaTela == null)
    		return;

//        remove tela atual
    	if (telaAtual != null && !telaAtual.equals(nomeTela)) {
            Component[] components = cardPanel.getComponents();
            for (Component comp : components) {
                if (telaAtual.equals(((JPanel)comp).getName())) {
                    cardPanel.remove(comp);
                    break;
                }
            }
        }
        
//        Adiciona e mostra a nova tela
        novaTela.setName(nomeTela);
        cardPanel.add(novaTela, nomeTela);
        cardLayout.show(cardPanel, nomeTela);
        telaAtual = nomeTela;
        cardPanel.repaint();	
    }
    
    private JPanel criarTela(String nomeTela) {
        switch (nomeTela) {
            case Telas.INICIO:
                return new PainelJanelaInicial(this);
            case Telas.QUANTIDADE_JOGADORES:
                return new PainelQuantidadeJogadores(this);
            case Telas.CRIACAO_JOGADORES:
            	return new PainelCriacaoJogadores(this);
            case Telas.TABULEIRO:
                return null;
            default:
                return null;
        }
    }
}





















