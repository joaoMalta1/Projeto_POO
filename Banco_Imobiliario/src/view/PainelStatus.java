package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import controller.Observador;
import controller.PartidaEvent;
import model.FacadeModel;

public class PainelStatus extends JPanel implements Observador<PartidaEvent> {

    private JLabel lblNomeJogador;
    private JLabel lblSaldo;
    private JLabel lblPosicao;
    private JLabel lblStatusMensagem;
    private JList<String> listaPropriedades;
    private DefaultListModel<String> listModel;

    private JLabel lblTituloFixo;
    private JLabel lblPropFixo;

    public PainelStatus() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(15, 15, 15, 15));
        setOpaque(false); // para poder pintar
        setPreferredSize(new Dimension(220, 0));  
        inicializarComponentes();
        FacadeModel.getInstance().addObserver(this); // registra como observador para receber eventos

        atualizarFundo(); 
        atualizarInformacoes();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        int w = getWidth();
        int h = getHeight();
        Color corBase = getBackground();
        Color corEscura = corBase.darker().darker(); //efeito de sombra
        GradientPaint gp = new GradientPaint(0, 0, corBase, 0, h, corEscura); //gradiente no efeito de sombra
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, w, h);
    }

    public void atualizarFundo() {
        Color corAtual = Cores.getInstance().corCorrespondente(FacadeView.getInstance().getCorJogadorAtual());
        this.setBackground(corAtual);
        Color corTexto = isCorEscura(corAtual) ? Color.WHITE : Color.BLACK;
        
        // atualiza cor texto
        if(lblNomeJogador != null) lblNomeJogador.setForeground(corTexto);
        if(lblSaldo != null) lblSaldo.setForeground(corTexto);
        if(lblPosicao != null) lblPosicao.setForeground(corTexto);
        if(lblTituloFixo != null) lblTituloFixo.setForeground(corTexto);
        if(lblPropFixo != null) lblPropFixo.setForeground(corTexto);
        this.repaint();
    }

    private boolean isCorEscura(Color color) {
        //decide a cor da fonte se vai ser clara ou escura 
        double brightness = (0.299 * color.getRed() + 0.587 * color.getGreen() + 0.114 * color.getBlue()) / 255; //snippet para calcular brilho
        return brightness < 0.5;
    }

    private void inicializarComponentes() {
        Font fonteTitulo = new Font("Arial", Font.BOLD, 18);
        Font fonteTexto = new Font("Arial", Font.PLAIN, 14);

        // jogador atual
        lblTituloFixo = new JLabel("JOGADOR ATUAL");
        lblTituloFixo.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblTituloFixo.setFont(new Font("Arial", Font.BOLD, 12));

        lblNomeJogador = new JLabel("---");
        lblNomeJogador.setFont(fonteTitulo);
        lblNomeJogador.setAlignmentX(Component.LEFT_ALIGNMENT);

        // saldos
        lblSaldo = new JLabel("Saldo: R$ 0.00");
        lblSaldo.setFont(fonteTexto);
        lblSaldo.setAlignmentX(Component.LEFT_ALIGNMENT);

        // posicoes
        lblPosicao = new JLabel("Posição: Início");
        lblPosicao.setFont(fonteTexto);
        lblPosicao.setAlignmentX(Component.LEFT_ALIGNMENT);

        // status mensagem
        lblStatusMensagem = new JLabel(" ");
        lblStatusMensagem.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 14));
        lblStatusMensagem.setForeground(new Color(0, 255, 0)); //verde zoado pra mostrar compra efetivada 
        lblStatusMensagem.setAlignmentX(Component.LEFT_ALIGNMENT);

        // propriedades-
        lblPropFixo = new JLabel("Minhas Propriedades:");
        lblPropFixo.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblPropFixo.setFont(new Font("Arial", Font.BOLD, 13));

        listModel = new DefaultListModel<>();
        listaPropriedades = new JList<>(listModel);
        listaPropriedades.setVisibleRowCount(12);

        listaPropriedades.setOpaque(false);
        listaPropriedades.setBackground(new Color(255, 255, 255, 150)); // branco meio transparente
        listaPropriedades.setSelectionBackground(new Color(255, 255, 255, 200));
        listaPropriedades.setSelectionForeground(Color.BLACK);
        
        JScrollPane scrollPropriedades = new JScrollPane(listaPropriedades);
        scrollPropriedades.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPropriedades.setOpaque(false);
        scrollPropriedades.getViewport().setOpaque(false);
        scrollPropriedades.setBorder(BorderFactory.createLineBorder(new Color(255,255,255,100)));

        add(lblTituloFixo);
        add(lblNomeJogador);
        add(Box.createVerticalStrut(15));
        add(lblSaldo);
        add(Box.createVerticalStrut(5));
        add(lblPosicao);
        add(Box.createVerticalStrut(15));
        add(lblStatusMensagem);
        add(Box.createVerticalStrut(15));
        add(lblPropFixo);
        add(Box.createVerticalStrut(5));
        add(scrollPropriedades);
    }

    private void atualizarInformacoes() {
        try {
            String nome = FacadeModel.getInstance().getNomeJogadorAtual();
            double saldo = FacadeModel.getInstance().getSaldoJogadorAtual();
            int pos = FacadeModel.getInstance().getPosJogadorAtual();
            ArrayList<String> props = FacadeModel.getInstance().getNomesPropriedadesJogadorAtual();
            
            lblNomeJogador.setText(nome);
            lblSaldo.setText(String.format("Saldo: R$ %.2f", saldo));
            lblPosicao.setText("Posição: " + (pos + 1));

            listModel.clear();
            if (props.isEmpty()) {
                listModel.addElement("(Nenhuma)");
            } else {
                for (String p : props) {
                    listModel.addElement(p);
                }
            }
            atualizarFundo(); 

        } catch (Exception e) {
        }
    }

    @Override
    public void notify(PartidaEvent event) {
        if (event == null) return;

        switch (event.type) {
            case NEXT_PLAYER:
                lblStatusMensagem.setText(" ");
                atualizarInformacoes(); 
                break;
            case PURCHASED_PROPERTY:
                lblStatusMensagem.setText("Compra Efetuada!");
                atualizarInformacoes();
                break;
            default:
                atualizarInformacoes();
                break;
        }
    }
}