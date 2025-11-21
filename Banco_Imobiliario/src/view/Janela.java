package view;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.*;

class Janela extends JFrame {
    private static final long serialVersionUID = 1L;
    public final int LARG_DEFAULT = 1280;
    public final int ALT_DEFAULT = 800;

    private CardLayout cardLayout;
    private JPanel cardPanel;
    private String telaAtual;

    private PainelTabuleiro painelTabuleiro;

    Janela(String nome) {
        super(nome);

        inicializarComponentes();
        configurarJanela();
        mostrarTelaInicial();
    }

    void inicializarComponentes() {
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        add(cardPanel);
    }

    void configurarJanela() {
        setSize(LARG_DEFAULT, ALT_DEFAULT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centralizado
        setResizable(false);

        // impedir que a janela seja extendida para um tamanho maior que 1280x800
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
                        Math.min(size.height, maxSize.height));
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

    private void removeTelaAtual() {
        Component[] components = cardPanel.getComponents();
        for (Component comp : components) {
            if (telaAtual.equals(((JPanel) comp).getName())) {
                cardPanel.remove(comp);
                break;
            }
        }
    }

    // Método para trocar de tela criando uma nova instância
    void mostrarTela(String nomeTela) {
        JPanel novaTela = criarTela(nomeTela);
        if (novaTela == null)
            return;

        if (telaAtual != null && !telaAtual.equals(nomeTela)) {
            removeTelaAtual();
        }

        // Adiciona e mostra a nova tela
        novaTela.setName(nomeTela);
        cardPanel.add(novaTela, nomeTela);
        cardLayout.show(cardPanel, nomeTela);
        telaAtual = nomeTela;
        cardPanel.repaint();
    }

    PainelTabuleiro getPainelTabuleiro() {
        return painelTabuleiro;
    }

    private JPanel criarTela(String nomeTela) {
        // DEBUG
        // nomeTela = Telas.CARTAS_PROPRIEDADES;
        switch (nomeTela) {
            case Telas.INICIO:

                return new PainelJanelaInicial(this);
            case Telas.QUANTIDADE_JOGADORES:
                return new PainelQuantidadeJogadores(this);
            case Telas.CRIACAO_JOGADORES:
                return new PainelCriacaoJogadores(this);
            case Telas.TABULEIRO:
                JPanel containerJogo = new JPanel(new BorderLayout());
                painelTabuleiro = new PainelTabuleiro(this);
                PainelStatus painelStatus = new PainelStatus();
                containerJogo.add(painelTabuleiro, BorderLayout.CENTER);
                containerJogo.add(painelStatus, BorderLayout.WEST); //painel status 

                return containerJogo;
            case Telas.FIM_DE_JOGO:
                return new PainelFimDeJogo(this);
            default:
                return null;
        }
    }
}
