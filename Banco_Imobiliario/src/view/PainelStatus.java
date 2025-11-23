package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import controller.Observador;
import controller.PartidaEvent;
import model.FacadeModel;

public class PainelStatus extends JPanel implements Observador<PartidaEvent> {

    private static final long serialVersionUID = 1L;
	private JLabel lblNomeJogador;
    private JLabel lblSaldo;
    private JLabel lblPosicao;
    private JLabel lblStatusMensagem;
    private JPanel painelListaProps;

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
        lblStatusMensagem.setForeground(new Color(57, 181, 74)); //verde zoado pra mostrar compra efetivada 
        lblStatusMensagem.setAlignmentX(Component.LEFT_ALIGNMENT);

        // propriedades-
        lblPropFixo = new JLabel("Minhas Propriedades:");
        lblPropFixo.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblPropFixo.setFont(new Font("Arial", Font.BOLD, 13));

        painelListaProps = new JPanel();
        painelListaProps.setLayout(new BoxLayout(painelListaProps, BoxLayout.Y_AXIS));
        painelListaProps.setOpaque(false);

        JScrollPane scrollPropriedades = new JScrollPane(painelListaProps);
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

    private void atualizarListaPropriedades() {
        painelListaProps.removeAll();
        java.util.List<Integer> ids = FacadeModel.getInstance().getIndicesPropriedadesJogadorAtual();

        if (ids == null || ids.isEmpty()) {
            JLabel vazio = new JLabel("(Nenhuma)");
            vazio.setFont(new Font("Arial", Font.ITALIC, 12));
            vazio.setForeground(isCorEscura(getBackground()) ? Color.WHITE : Color.BLACK);
            painelListaProps.add(vazio);
        } else {
            for (int id : ids) {
                final int pos = id;

                String nomeTmp;
                try {
                    nomeTmp = FacadeModel.getInstance().getNomeDoCampo(pos);
                } catch (Exception ex) {
                    nomeTmp = "ID " + pos;
                }
                final String nome = nomeTmp; // efetivamente final para uso no lambda

                JPanel linha = new JPanel();
                linha.setLayout(new BoxLayout(linha, BoxLayout.X_AXIS));
                linha.setOpaque(false);

                JLabel lbl = new JLabel(String.format("%02d - %s", pos, nome));
                lbl.setFont(new Font("Arial", Font.PLAIN, 12));
                lbl.setForeground(isCorEscura(getBackground()) ? Color.WHITE : Color.BLACK);

                JButton btVender = new JButton("Vender");
                btVender.setFont(new Font("Arial", Font.PLAIN, 11));
                btVender.addActionListener(e -> {
                    int r = JOptionPane.showConfirmDialog(this,
                            "Vender " + nome + " (ID " + pos + ") por 90% do valor?",
                            "Confirmar venda",
                            JOptionPane.YES_NO_OPTION);
                    if (r == JOptionPane.YES_OPTION) {
                        boolean ok = FacadeModel.getInstance().venderPropriedadeAtualJogador(pos);
                        if (!ok) {
                            JOptionPane.showMessageDialog(this, "Falha na venda.", "Erro", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });

                linha.add(lbl);
                linha.add(Box.createHorizontalStrut(8));
                linha.add(btVender);
                linha.setAlignmentX(Component.LEFT_ALIGNMENT);
                painelListaProps.add(linha);
                painelListaProps.add(Box.createVerticalStrut(4));
            }
        }
        painelListaProps.revalidate();
        painelListaProps.repaint();
    }

    private void atualizarInformacoes() {
        try {
            String nome = FacadeModel.getInstance().getNomeJogadorAtual();
            double saldo = FacadeModel.getInstance().getSaldoJogadorAtual();
            int pos = FacadeModel.getInstance().getPosJogadorAtual();

            lblNomeJogador.setText(nome);
            lblSaldo.setText(String.format("Saldo: R$ %.2f", saldo));
            lblPosicao.setText("Posição: " + (pos + 1));

            atualizarListaPropriedades();
            atualizarFundo();
        } catch (Exception e) {
            // silencioso
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
                lblStatusMensagem.setText("Propriedade comprada!");
                atualizarInformacoes();
                break;
            case PURCHASED_HOUSE:
            	lblStatusMensagem.setText("Casa comprada!");
                atualizarInformacoes();
                break;
            case PURCHASED_HOTEL:
            	lblStatusMensagem.setText("Hotel comprado!");
                atualizarInformacoes();
                break;
            case SORTE_OU_REVES:
            	lblStatusMensagem.setText("Retirou uma carta!");
                atualizarInformacoes();
                break;
            case PROPERTY_SOLD:
                lblStatusMensagem.setText("Venda Efetuada!");
                atualizarInformacoes();
                break;
            default:
                atualizarInformacoes();
                break;
        }
    }
}