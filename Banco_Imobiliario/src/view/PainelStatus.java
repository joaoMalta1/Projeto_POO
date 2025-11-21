package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import controller.Observador;
import controller.PartidaEvent;
import model.CentralPartida;
import model.FacadeModel;

public class PainelStatus extends JPanel implements Observador<PartidaEvent> {

    private JLabel lblNomeJogador;
    private JLabel lblSaldo;
    private JLabel lblPosicao;
    private JLabel lblStatusMensagem;
    private JList<String> listaPropriedades;
    private DefaultListModel<String> listModel;

    public PainelStatus() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setPreferredSize(new Dimension(280, 0)); // Largura fixa de 280px
        setBackground(new Color(245, 245, 245)); // Cor de fundo leve

        inicializarComponentes();
        
        // Regista-se para receber atualizações do jogo
        FacadeModel.getInstance().addObserver(this);
        atualizarInformacoes();
    }

    private void inicializarComponentes() {
        Font fonteTitulo = new Font("Arial", Font.BOLD, 18);
        Font fonteTexto = new Font("Arial", Font.PLAIN, 14);

        // -- Cabeçalho --
        JLabel lblTitulo = new JLabel("JOGADOR ATUAL");
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 12));
        
        lblNomeJogador = new JLabel("---");
        lblNomeJogador.setFont(fonteTitulo);
        lblNomeJogador.setForeground(new Color(0, 51, 102)); // Azul escuro
        lblNomeJogador.setAlignmentX(Component.LEFT_ALIGNMENT);

        // -- Saldo --
        lblSaldo = new JLabel("Saldo: R$ 0.00");
        lblSaldo.setFont(fonteTexto);
        lblSaldo.setAlignmentX(Component.LEFT_ALIGNMENT);

        // -- Posição --
        lblPosicao = new JLabel("Posição: Início");
        lblPosicao.setFont(fonteTexto);
        lblPosicao.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // -- Mensagens de Status (ex: Compra efetuada) --
        lblStatusMensagem = new JLabel(" ");
        lblStatusMensagem.setFont(new Font("Arial", Font.ITALIC, 13));
        lblStatusMensagem.setForeground(new Color(34, 139, 34)); // Verde
        lblStatusMensagem.setAlignmentX(Component.LEFT_ALIGNMENT);

        // -- Lista de Propriedades --
        JLabel lblProp = new JLabel("Minhas Propriedades:");
        lblProp.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblProp.setFont(new Font("Arial", Font.BOLD, 13));
        
        listModel = new DefaultListModel<>();
        listaPropriedades = new JList<>(listModel);
        listaPropriedades.setVisibleRowCount(15);
        JScrollPane scrollPropriedades = new JScrollPane(listaPropriedades);
        scrollPropriedades.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Adiciona tudo ao painel com espaçamentos
        add(lblTitulo);
        add(lblNomeJogador);
        add(Box.createVerticalStrut(15));
        add(lblSaldo);
        add(Box.createVerticalStrut(5));
        add(lblPosicao);
        add(Box.createVerticalStrut(15));
        add(lblStatusMensagem);
        add(Box.createVerticalStrut(15));
        add(lblProp);
        add(Box.createVerticalStrut(5));
        add(scrollPropriedades);
    }

    private void atualizarInformacoes() {
        try {
            // Busca dados atualizados da Facade
            String nome = FacadeModel.getInstance().getNomeJogadorAtual();
            double saldo = FacadeModel.getInstance().getJogadorAtual();
            int pos = FacadeModel.getInstance().getPosJogadorAtual();
            ArrayList<String> props = FacadeModel.getInstance().getNomesPropriedadesJogadorAtual();
            lblNomeJogador.setText(nome);
            lblSaldo.setText(String.format("Saldo: R$ %.2f", saldo));
            lblPosicao.setText("Posição: " + (pos + 1)); // Mostra 1 a 40

            // Atualiza lista de propriedades
            listModel.clear();
            if (props.isEmpty()) {
                listModel.addElement("(Nenhuma)");
            } else {
                for (String p : props) {
                    listModel.addElement(p);
                }
            }
            repaint();
        } catch (Exception e) {
            // Ignora erros na inicialização se o jogo ainda não tiver começado
        }
    }

    @Override
    public void notify(PartidaEvent event) {
        if (event == null) return;
        
        switch (event.type) {
            case NEXT_PLAYER:
                lblStatusMensagem.setText(" "); // Limpa mensagem ao mudar turno
                atualizarInformacoes();
                break;
            case PURCHASED_PROPERTY:
                lblStatusMensagem.setText("Compra Efetuada!");
                atualizarInformacoes();
                break;
            case MOVE:
            case DICE_ROLLED:
                atualizarInformacoes();
                break;
            default:
                atualizarInformacoes();
                break;
        }
    }
}