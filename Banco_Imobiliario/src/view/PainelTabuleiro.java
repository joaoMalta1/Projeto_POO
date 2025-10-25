package view;

import model.Tabuleiro;
import controller.TabuleiroController;
import controller.CorPeao;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.lang.reflect.Method;

public class PainelTabuleiro extends JPanel implements PropertyChangeListener {
    private static final long serialVersionUID = 1L;
    private final Tabuleiro model;
    private final TabuleiroController controller;
    private BufferedImage boardImg;
    private Point[] coordsCasas;
    private final int NUM_CASAS = 40;
    private final int PER_SIDE = 10;

    public PainelTabuleiro(Tabuleiro model, TabuleiroController controller) {
        this.model = model;
        this.controller = controller;
        // tenta inscrever-se em eventuais listeners do model (se houver)
        try {
            model.getClass().getMethod("addPropertyChangeListener", PropertyChangeListener.class)
                    .invoke(model, this);
        } catch (Exception ignore) { }
        setBackground(Color.WHITE);
        carregarImagemTabuleiro();
    }

    private void carregarImagemTabuleiro() {
        try {
            File f = new File("imagens-01/tabuleiro.png");
            if (f.exists()) {
                boardImg = ImageIO.read(f);
                return;
            }
        } catch (Exception ignored) { }

        try {
            boardImg = ImageIO.read(getClass().getResourceAsStream("/imagens-01/tabuleiro.png"));
        } catch (Exception ignored) { }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int w = getWidth();
        int h = getHeight();

        int drawX = 10, drawY = 10, drawW = w - 20, drawH = h - 20;

        if (boardImg != null) {
            double imgRatio = (double) boardImg.getWidth() / boardImg.getHeight();
            double panelRatio = (double) drawW / drawH;
            if (panelRatio > imgRatio) drawW = (int) (drawH * imgRatio);
            else drawH = (int) (drawW / imgRatio);
            drawX = (w - drawW) / 2;
            drawY = (h - drawH) / 2;
            g.drawImage(boardImg, drawX, drawY, drawW, drawH, null);
        } else {
            g.setColor(new Color(220, 220, 255));
            g.fillRect(drawX, drawY, drawW, drawH);
        }

        calcularCoordenadas(drawX, drawY, drawW, drawH);
        desenharCasas((Graphics2D) g, drawX, drawY);
        desenharPeoes((Graphics2D) g, drawX, drawY);
    }

    private void calcularCoordenadas(int ox, int oy, int w, int h) {
        coordsCasas = new Point[NUM_CASAS];
        int margin = Math.max(16, Math.min(w, h) / 12);
        int usableW = w - 2 * margin;
        int usableH = h - 2 * margin;
        int cellW = (PER_SIDE - 1) > 0 ? usableW / (PER_SIDE - 1) : usableW;
        int cellH = (PER_SIDE - 1) > 0 ? usableH / (PER_SIDE - 1) : usableH;

        int idx = 0;
        for (int i = 0; i < PER_SIDE; i++) {
            int x = ox + margin + usableW - i * cellW;
            int y = oy + margin + usableH;
            coordsCasas[idx++] = new Point(x, y);
        }
        for (int i = 1; i < PER_SIDE; i++) {
            int x = ox + margin;
            int y = oy + margin + usableH - i * cellH;
            coordsCasas[idx++] = new Point(x, y);
        }
        for (int i = 1; i < PER_SIDE; i++) {
            int x = ox + margin + i * cellW;
            int y = oy + margin;
            coordsCasas[idx++] = new Point(x, y);
        }
        for (int i = 1; i < PER_SIDE - 1; i++) {
            int x = ox + margin + usableW;
            int y = oy + margin + i * cellH;
            coordsCasas[idx++] = new Point(x, y);
        }
        while (idx < NUM_CASAS) {
            coordsCasas[idx++] = new Point(ox + w - margin, oy + h - margin);
        }
    }

    private void desenharCasas(Graphics2D g2, int ox, int oy) {
        g2.setColor(new Color(0, 0, 0, 60));
        if (coordsCasas == null) return;
        for (int i = 0; i < NUM_CASAS; i++) {
            Point p = coordsCasas[i];
            g2.fillOval(p.x - 6, p.y - 6, 12, 12);
        }
    }

    private void desenharPeoes(Graphics2D g2, int ox, int oy) {
        int[] posicoes = controller.getPosicoes();
        if (posicoes == null || posicoes.length == 0) return;

        java.util.Map<Integer, java.util.List<Integer>> mapa = new java.util.HashMap<>();
        for (int i = 0; i < posicoes.length; i++) {
            int pos = Math.max(0, Math.min(NUM_CASAS - 1, posicoes[i]));
            mapa.computeIfAbsent(pos, k -> new java.util.ArrayList<>()).add(i);
        }

        for (java.util.Map.Entry<Integer, java.util.List<Integer>> e : mapa.entrySet()) {
            int casa = e.getKey();
            Point base = coordsCasas[casa];
            java.util.List<Integer> lista = e.getValue();
            for (int j = 0; j < lista.size(); j++) {
                int jogadorIdx = lista.get(j);
                int dx = (j % 2) * 18;
                int dy = (j / 2) * 18;
                int x = base.x - 14 + dx;
                int y = base.y - 14 + dy;

                Color c = corDoJogador(jogadorIdx);
                g2.setColor(c);
                g2.fillOval(x, y, 28, 28);
                g2.setColor(Color.BLACK);
                g2.drawOval(x, y, 28, 28);
            }
        }
    }

    private Color corDoJogador(int idxJogador) {
        try {
            Class<?> ctrlClass = Class.forName("controller.ControleInformacoesJogo");
            Object ctrl = ctrlClass.getMethod("getInstance").invoke(null);
            Method m = ctrlClass.getMethod("getCorJogador", int.class);
            Object corObj = m.invoke(ctrl, idxJogador);
            if (corObj instanceof CorPeao) return mapCorPeao((CorPeao) corObj);
        } catch (Exception ignored) { }
        CorPeao[] vals = CorPeao.values();
        return mapCorPeao(vals[idxJogador % vals.length]);
    }

    private Color mapCorPeao(CorPeao c) {
        if (c == null) return Color.MAGENTA;
        switch (c) {
            case VERMELHO: return Color.RED;
            case AZUL: return Color.BLUE;
            case VERDE: return Color.GREEN;
            case AMARELO: return Color.YELLOW;
            case PRETO: return Color.DARK_GRAY;
            case BRANCO: return Color.LIGHT_GRAY;
            default: return Color.MAGENTA;
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        repaint();
    }

    public TabuleiroController getController() {
        return controller;
    }
}