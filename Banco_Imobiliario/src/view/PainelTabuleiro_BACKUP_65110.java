package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.Map;
import java.awt.Point;
import java.util.HashMap;
import java.util.List;
import javax.swing.*;

import model.FacadeModel;
import controller.Observador;
import controller.CorPeao;
import controller.ControlePartida;
import controller.PartidaEvent;

public class PainelTabuleiro extends JPanel implements Observador<PartidaEvent> {

    private static final long serialVersionUID = 1L;
    private final Janela janelaPrincipal;
    private BotaoEstilizado botaoDados, botaoSetarDados, botaoComprarProp, 
    	botaoComprarCasa, bFimJogo, bFimTurno, botaoComprarHotel, bSalvarJogo;

    private int[] dados = { 1, 1 };
    private Image imagemMapa;
    private Image[] imagensDados;
    private boolean dadosVisiveis = false;
    private boolean dadosJogadosTurno = false;
    private Image imagemPeao;
    private java.util.Map<CorPeao, Image> imagensPinos;
    private final int TAMANHO_PEAO = 25;
    private Map<Integer, Point> coordenadasCasas;

    private int posicaoDeTeste = 1;

    private Image imagemCartaPropriedade = null;
    private Image imagemCartaSorteReves = null;
    private final int LARGURA_CARTA_PROPRIEDADE = 229;
    private final int ALTURA_CARTA_PROPRIEDADE = 229;

    private final int LARGURA_CARTA_SORTEREVES = 208;
    private final int ALTURA_CARTA_SORTEREVES = 238;


    // simensao mapa
    private final int LARGURA_MAPA = 700;
    private final int ALTURA_MAPA = 700;
    
    
    public PainelTabuleiro(Janela janela) {
        this.janelaPrincipal = janela;
        
        setBackground(Cores.getInstance().corCorrespondente(FacadeView.getInstance().getCorJogadorAtual()));
        
        carregarImagemDoMapa();
        carregarImagensDados();
        carregarImagensPinos();

        inicializarCoordenadasCasas();

        criarBotaoDados();
        criarBotaoSetarDados();
        criarBotaoFimJogo();
        criarBotaoFimTurno();
//        criarBotaoVendaBanco();
        
        FacadeModel.getInstance().addObserver(this);
    }
    
    private void criarBotaoFimTurno(){
    	bFimTurno = new BotaoEstilizado("Encerrar Turno", 300, 200);
    	bFimTurno.addActionListener(e-> {
    		if(dadosJogadosTurno) {
    			FacadeView.getInstance().proxJogador();    
    			remove(bFimTurno);
    			revalidate();
    			repaint();
    		}
    	});
    }
    
    private void criarBotaoFimJogo() {
    	bFimJogo = new BotaoEstilizado("Encerrar Partida", 300, 200);
    	bFimJogo.addActionListener(e -> {

            int resposta = JOptionPane.showConfirmDialog(
                this, 
                "Você tem certeza que deseja encerrar a partida?", 
                "Confirmação de Encerramento", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.QUESTION_MESSAGE
            );
            
            if (resposta == JOptionPane.YES_OPTION) {
                FacadeView.getInstance().botaoFimDeJogoApertado();
            }
    		});
    	add(bFimJogo);
    }
    
//    private void criarBotaoVendaBanco() {
//    	bVendaBanco = new BotaoEstilizado("Vender para o banco", 300, 700);
//    	
//    	bVendaBanco.addActionListener(e -> {
//    		int resposta = JOptionPane.showConfirmDialog(
//                    this, 
//                    "Você tem certeza que deseja vender o imóvel ao banco?", 
//                    "Confirmação de Venda", 
//                    JOptionPane.YES_NO_OPTION, 
//                    JOptionPane.QUESTION_MESSAGE
//                );
//    		
//            if (resposta == JOptionPane.YES_OPTION) {
//               
//            }
//    	});
//    	
//    	add(bVendaBanco);
//    }

	private void criarBotaoSetarDados() {
	    if (botaoSetarDados != null && botaoSetarDados.getParent() != null) return;
	    botaoSetarDados = new BotaoEstilizado("Escolher Dados", 300, 200);
	    botaoSetarDados.addActionListener(ev -> abrirSelecaoDeDados());
	    this.add(botaoSetarDados);
	}
	
	private void abrirSelecaoDeDados() {
	    try {
	        String d1Str = JOptionPane.showInputDialog(this, "Valor do dado 1 (1 a 6):");
	        String d2Str = JOptionPane.showInputDialog(this, "Valor do dado 2 (1 a 6):");
	
	        if (d1Str == null || d2Str == null) {
	            return;
	        }
	        int d1 = Integer.parseInt(d1Str);
	        int d2 = Integer.parseInt(d2Str);
	        FacadeModel.getInstance().setDadosDeTeste(d1, d2);
	        JOptionPane.showMessageDialog(this, "Dados teste definidos!");
	    } catch (Exception e) {
	        JOptionPane.showMessageDialog(this, "Valores inválidos! Use números entre 1 e 6.");
	    }
	}


    private void criarBotaoDados() {
        botaoDados = new BotaoEstilizado("Jogar Dados", 300, 200);
        botaoDados.setAlignmentX(CENTER_ALIGNMENT);
        botaoDados.setAlignmentY(CENTER_ALIGNMENT);

        botaoDados.addActionListener(e -> {
            if (botaoDados.getParent() != null) {
                dadosJogadosTurno = true;
                remove(botaoDados);
                remove(botaoSetarDados);
                add(bFimTurno);
                revalidate();
                repaint();
            }
            int[] resultado = ControlePartida.getInstance().jogarDadosEAndar();

            if (resultado != null && resultado.length == 2) {
                this.dados = new int[] { resultado[0], resultado[1] };
                this.dadosVisiveis = true;
            }
        });

        add(botaoDados);
        revalidate();
        repaint();
    }

    private void criarBotaoComprarPropriedade() {
        if (botaoComprarProp != null && botaoComprarProp.getParent() != null)
            return; 
        botaoComprarProp  = new BotaoEstilizado("Comprar Propriedade", 200, 120);
        botaoComprarProp.addActionListener(ev -> {
//            System.out.println(FacadeModel.getInstance().getPosJogadorAtual());
            boolean sucesso = FacadeView.getInstance().comprarPropriedadeAtualJogador();
            if (sucesso) {
                removerBotaoComprarPropSeExistir();
                atualizarFundo();
                repaint();
            } else {
            	removerBotaoComprarPropSeExistir();
                atualizarFundo();
                repaint();
                System.out.println("COMPRA NÃO EFETUADA");
            }
        });
        add(botaoComprarProp);
        revalidate();
        repaint();
    }
    
    private void criarBotaoComprarCasa() {
        if (botaoComprarCasa != null && botaoComprarCasa.getParent() != null)
            return; 
        botaoComprarCasa  = new BotaoEstilizado("Comprar Casa", 200, 120);
        botaoComprarCasa.addActionListener(ev -> {
//            System.out.println(FacadeModel.getInstance().getPosJogadorAtual());
            boolean sucesso = FacadeView.getInstance().atualComprarCasa();
            if (sucesso) {
                removerBotaoComprarCasaSeExistir();
                atualizarFundo();
                repaint();
            }
        });
        add(botaoComprarCasa);
        revalidate();
        repaint();
    }
    
    private void criarBotaoComprarHotel() {
        if (botaoComprarHotel != null && botaoComprarHotel.getParent() != null)
            return; 
        botaoComprarHotel  = new BotaoEstilizado("Comprar Hotel", 200, 120);
        botaoComprarHotel.addActionListener(ev -> {
//            System.out.println(FacadeModel.getInstance().getPosJogadorAtual());
            boolean sucesso = FacadeView.getInstance().atualComprarHotel();
            if (sucesso) {
                removerBotaoComprarHotelSeExistir();
                atualizarFundo();
                repaint();
            }
        });
        add(botaoComprarHotel);
        revalidate();
        repaint();
    }
    
//    private void criarBotaoSalvarJogo() {
//        if (bSalvarJogo != null && bSalvarJogo.getParent() != null)
//            return; 
//        bSalvarJogo = new BotaoEstilizado("Salvar Jogo", 200, 120);
//        bSalvarJogo.addActionListener(ev -> {
////            System.out.println(FacadeModel.getInstance().getPosJogadorAtual());
//            boolean sucesso = FacadeView.getInstance().salvarJogo();
//            if (sucesso) {
//            	
//            }
//        });
//        add(bSalvarJogo);
//        revalidate();
//        repaint();
//    }
    
    private void criarBotaoSalvarJogo() {
        if (bSalvarJogo != null && bSalvarJogo.getParent() != null)
            return; 
        bSalvarJogo = new BotaoEstilizado("Salvar Jogo", 200, 120);
        bSalvarJogo.addActionListener(ev -> {
            // Criar JFileChooser
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Salvar Jogo");
            
            // Configurar para salvar apenas arquivos .txt
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Arquivos de texto (*.txt)", "txt"));
            
            // Sugerir um nome padrão
            fileChooser.setSelectedFile(new File("jogo_salvo.txt"));
            
            // Mostrar diálogo de salvamento
            int userSelection = fileChooser.showSaveDialog(this);
            
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                
                // Garantir que tenha extensão .txt
                String filePath = fileToSave.getAbsolutePath();
                if (!filePath.toLowerCase().endsWith(".txt")) {
                    filePath += ".txt";
                    fileToSave = new File(filePath);
                }
                
                // Verificar se o arquivo já existe
                if (fileToSave.exists()) {
                    int overwrite = JOptionPane.showConfirmDialog(this,
                        "O arquivo já existe. Deseja sobrescrever?",
                        "Arquivo Existente",
                        JOptionPane.YES_NO_OPTION);
                    
                    if (overwrite != JOptionPane.YES_OPTION) {
                        return; // Usuário não quer sobrescrever
                    }
                }
                
                try {
                    // Tentar salvar o jogo
                    boolean sucesso = FacadeModel.getInstance().salvarJogo(filePath);
                    
                    if (sucesso) {
                        JOptionPane.showMessageDialog(this,
                            "Jogo salvo com sucesso em:\n" + filePath,
                            "Salvamento Concluído",
                            JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this,
                            "Erro ao salvar o jogo. Tente novamente.",
                            "Erro no Salvamento",
                            JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,
                        "Erro ao salvar o jogo:\n" + ex.getMessage(),
                        "Erro no Salvamento",
                        JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });
        add(bSalvarJogo);
        revalidate();
        repaint();
    }

    private void removerBotaoComprarPropSeExistir() {
        if (botaoComprarProp != null && botaoComprarProp.getParent() != null) {
            remove(botaoComprarProp);
            revalidate();
            repaint();
        }
    }
    
    private void removerBotaoComprarCasaSeExistir() {
        if (botaoComprarCasa != null && botaoComprarCasa.getParent() != null) {
            remove(botaoComprarCasa);
            revalidate();
            repaint();
        }
    }
    
    private void removerBotaoComprarHotelSeExistir() {
        if (botaoComprarHotel != null && botaoComprarHotel.getParent() != null) {
            remove(botaoComprarHotel);
            revalidate();
            repaint();
        }
    }

    private void removerBotaoSalvarJogo() {
        if (bSalvarJogo != null && bSalvarJogo.getParent() != null) {
        	remove(bSalvarJogo);
        	revalidate();
        	repaint();        	
        }
    }
    
    private void carregarImagensDados() {
        imagensDados = new Image[7]; // índice 0 não usado, 1-6 para os valores

        for (int i = 1; i <= 6; i++) {
            try {
                File file = new File("src/resources/Dados/" + i + ".png");
                if (file.exists()) {
                    Image imagemOriginal = ImageIO.read(file);
                    imagensDados[i] = imagemOriginal.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                } else {
                    System.err.println("ERRO: Imagem do dado " + i + " não encontrada: " + file.getAbsolutePath());
                }
            } catch (IOException e) {
                System.err.println("Erro ao carregar imagem do dado " + i + ": " + e.getMessage());
            }
        }
    }

    public void atualizarFundo() {
//        carregarImagemDoPeao();
        setBackground(Cores.getInstance().corCorrespondente(FacadeView.getInstance().getCorJogadorAtual()));
        repaint();
    }

    private void inicializarCoordenadasCasas() {
        coordenadasCasas = new HashMap<>();
        coordenadasCasas.put(1, new Point(670, 650));
        coordenadasCasas.put(2, new Point(585, 650));
        coordenadasCasas.put(3, new Point(535, 650));
        coordenadasCasas.put(4, new Point(484, 650));
        coordenadasCasas.put(5, new Point(422, 650));
        coordenadasCasas.put(6, new Point(360, 650));
        coordenadasCasas.put(7, new Point(298, 650));
        coordenadasCasas.put(8, new Point(236, 650));
        coordenadasCasas.put(9, new Point(174, 650));
        coordenadasCasas.put(10, new Point(112, 650));
        coordenadasCasas.put(11, new Point(50, 650));

        coordenadasCasas.put(12, new Point(50, 590));
        coordenadasCasas.put(13, new Point(50, 530));
        coordenadasCasas.put(14, new Point(50, 470));
        coordenadasCasas.put(15, new Point(50, 410));
        coordenadasCasas.put(16, new Point(50, 350));
        coordenadasCasas.put(17, new Point(50, 290));
        coordenadasCasas.put(18, new Point(50, 230));
        coordenadasCasas.put(19, new Point(50, 170));
        coordenadasCasas.put(20, new Point(50, 110));
        coordenadasCasas.put(21, new Point(50, 50));

        coordenadasCasas.put(22, new Point(112, 50));
        coordenadasCasas.put(23, new Point(174, 50));
        coordenadasCasas.put(24, new Point(236, 50));
        coordenadasCasas.put(25, new Point(298, 50));
        coordenadasCasas.put(26, new Point(360, 50));
        coordenadasCasas.put(27, new Point(422, 50));
        coordenadasCasas.put(28, new Point(484, 50));
        coordenadasCasas.put(29, new Point(546, 50));
        coordenadasCasas.put(30, new Point(608, 50));
        coordenadasCasas.put(31, new Point(670, 50));

        coordenadasCasas.put(32, new Point(670, 110));
        coordenadasCasas.put(33, new Point(670, 170));
        coordenadasCasas.put(34, new Point(670, 230));
        coordenadasCasas.put(35, new Point(670, 290));
        coordenadasCasas.put(36, new Point(670, 350));
        coordenadasCasas.put(37, new Point(670, 410));
        coordenadasCasas.put(38, new Point(670, 470));
        coordenadasCasas.put(39, new Point(670, 530));
        coordenadasCasas.put(40, new Point(670, 590));
    }

    private void desenharPeao(Graphics2D g2d) {
        if (coordenadasCasas == null)
            return;

        int mapaLargura = LARGURA_MAPA;
        int mapaAltura = ALTURA_MAPA;
        int deslocamentoXMapa = (getWidth() - mapaLargura) / 2;
        int deslocamentoYMapa = (getHeight() - mapaAltura) / 2;

        int qtd = 0;
        try {
            qtd = FacadeModel.getInstance().getQtdJogadores();
        } catch (Exception e) {
            qtd = 0;
        }
        
     // DEBUG: Print das posições durante o desenho
//        System.out.println("DEBUG [DESENHAR_PEAO] - Quantidade de jogadores: " + qtd);
        

        if (qtd <= 0) {
            // fallback: draw test pawn
            int posicaoAtual;
            try {
                int modeloPos = FacadeModel.getInstance().getPosJogadorAtual();
                posicaoAtual = modeloPos + 1;
            } catch (Exception ex) {
                posicaoAtual = this.posicaoDeTeste;
            }
            Point coordsRelativas = coordenadasCasas.get(posicaoAtual);
            if (coordsRelativas == null)
                return;
            
            int xPeao = deslocamentoXMapa + coordsRelativas.x - TAMANHO_PEAO / 2;
            int yPeao = deslocamentoYMapa + coordsRelativas.y - TAMANHO_PEAO / 2;
            if (imagemPeao != null)
                g2d.drawImage(imagemPeao, xPeao, yPeao, TAMANHO_PEAO, TAMANHO_PEAO, this);
            return;
        }

        // Group pawns by their map position so we can offset multiple pawns on the same
        // square
        java.util.Map<Integer, java.util.List<Integer>> posToPlayers = new java.util.HashMap<>();
        for (int i = 0; i < qtd; i++) {
            int posModelo = FacadeModel.getInstance().getPosicaoJogador(i); // 0-based
            posToPlayers.computeIfAbsent(posModelo, k -> new java.util.ArrayList<>()).add(i);
            
         // DEBUG: Print da posição de cada jogador
//            System.out.println("DEBUG [DESENHAR_PEAO] - Jogador " + i + " na posição: " + posModelo);
        }

        for (Map.Entry<Integer, List<Integer>> e : posToPlayers.entrySet()) {
            int posModelo = e.getKey();
            java.util.List<Integer> playersHere = e.getValue();
            int countHere = playersHere.size();
            int posMapa = posModelo + 1; // 1-based for coordenadasCasas
            Point coordsRelativas = coordenadasCasas.get(posMapa);
            if (coordsRelativas == null)
                continue;

            // base position (center of the house)
            int baseX = deslocamentoXMapa + coordsRelativas.x;
            int baseY = deslocamentoYMapa + coordsRelativas.y;

            // compute offsets depending on how many pawns are on the same house
            for (int idx = 0; idx < countHere; idx++) {
                int playerIndex = playersHere.get(idx);
                int offsetX = 0;
                int offsetY = 0;

                if (countHere == 1) {
                    offsetX = 0;
                    offsetY = 0;
                } else if (countHere == 2) {
                    // left / right
                    offsetX = (idx == 0) ? -TAMANHO_PEAO / 2 : TAMANHO_PEAO / 2;
                    offsetY = 0;
                } else if (countHere == 3) {
                    // triangle: top, bottom-left, bottom-right
                    if (idx == 0) {
                        offsetX = 0;
                        offsetY = -TAMANHO_PEAO / 2;
                    }
                    if (idx == 1) {
                        offsetX = -TAMANHO_PEAO / 2;
                        offsetY = TAMANHO_PEAO / 4;
                    }
                    if (idx == 2) {
                        offsetX = TAMANHO_PEAO / 2;
                        offsetY = TAMANHO_PEAO / 4;
                    }
                } else if (countHere == 4) {
                    // grid 2x2
                    if (idx == 0) {
                        offsetX = -TAMANHO_PEAO / 2;
                        offsetY = -TAMANHO_PEAO / 2;
                    }
                    if (idx == 1) {
                        offsetX = TAMANHO_PEAO / 2;
                        offsetY = -TAMANHO_PEAO / 2;
                    }
                    if (idx == 2) {
                        offsetX = -TAMANHO_PEAO / 2;
                        offsetY = TAMANHO_PEAO / 2;
                    }
                    if (idx == 3) {
                        offsetX = TAMANHO_PEAO / 2;
                        offsetY = TAMANHO_PEAO / 2;
                    }
                } else {
                    // more than 4: place in a small circle
                    double angle = (2 * Math.PI * idx) / countHere;
                    int radius = TAMANHO_PEAO;
                    offsetX = (int) Math.round(Math.cos(angle) * radius);
                    offsetY = (int) Math.round(Math.sin(angle) * radius);
                }

                int xPeao = baseX + offsetX - TAMANHO_PEAO / 2;
                int yPeao = baseY + offsetY - TAMANHO_PEAO / 2;
                CorPeao cor = FacadeModel.getInstance().getCorJogador(playerIndex);
                Image img = imagensPinos != null ? imagensPinos.get(cor) : null;
                if (img != null) {
                    g2d.drawImage(img, xPeao, yPeao, TAMANHO_PEAO, TAMANHO_PEAO, this);
                }
            }
        }
    }

    private void carregarImagensPinos() {
        imagensPinos = new HashMap<>();
        for (controller.CorPeao cor : controller.CorPeao.values()) {
            String nome = cor.toString().toLowerCase();
            try {
                File file = new File("src/resources/Pinos/" + nome + ".png");
                if (file.exists()) {
                    Image imagemOriginal = ImageIO.read(file);
                    imagensPinos.put(cor,
                            imagemOriginal.getScaledInstance(TAMANHO_PEAO, TAMANHO_PEAO, Image.SCALE_SMOOTH));
                }
            } catch (IOException e) {
                System.err.println("Erro ao carregar imagem do pino " + nome + ": " + e.getMessage());
            }
        }
    }

    /**
     * Desenha os dois dados no centro do painel com distância de 100 pixels entre
     * os centros
     */
    private void desenharDados(Graphics2D g2d) {
        if (!dadosVisiveis || dados[0] == 0 || dados[1] == 0)
            return;
        if (imagensDados[dados[0]] == null || imagensDados[dados[1]] == null)
            return;

        int larguraDado = imagensDados[dados[0]].getWidth(this);
        int alturaDado = imagensDados[dados[0]].getHeight(this);
        int distanciaCentros = 100;
        int centroX = getWidth() / 2;
        int centroY = getHeight() / 2;
        int x1 = centroX - distanciaCentros / 2 - larguraDado / 2;
        int x2 = centroX + distanciaCentros / 2 - larguraDado / 2;
        int y = centroY - alturaDado / 2;

        g2d.drawImage(imagensDados[dados[0]], x1, y, this);
        g2d.drawImage(imagensDados[dados[1]], x2, y, this);
    }

    private void carregarImagemDoMapa() {
        try {
            File file = new File("Imagens-01/tabuleiro.png");
            if (file.exists()) {
                imagemMapa = ImageIO.read(file);
                imagemMapa = imagemMapa.getScaledInstance(LARGURA_MAPA, ALTURA_MAPA, Image.SCALE_SMOOTH);
            } else {
                System.err.println("ERRO: Imagem do mapa não encontrada: " + file.getAbsolutePath());
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar imagem do mapa: " + e.getMessage());
        }
    }

    public void exibirCartaPropriedade(String nomeArquivo) {
        try {
            File file = new File("src/resources/Propriedades/" + nomeArquivo + ".png");
            if (file.exists()) {
                imagemCartaPropriedade = ImageIO.read(file).getScaledInstance(LARGURA_CARTA_PROPRIEDADE, ALTURA_CARTA_PROPRIEDADE, Image.SCALE_SMOOTH);
                repaint();
            } else {
                System.err.println("ERRO: Carta não encontrada: " + file.getAbsolutePath());
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar carta de propriedade: " + e.getMessage());
        }
    }

    public void exibirCartaSorteReves(String nomeArquivo) {
        try {
            File file = new File("src/resources/SorteReves/" + nomeArquivo + ".png");
            if (file.exists()) {
                imagemCartaSorteReves = ImageIO.read(file).getScaledInstance(LARGURA_CARTA_SORTEREVES, ALTURA_CARTA_SORTEREVES, Image.SCALE_SMOOTH);
                repaint();
            } else {
                System.err.println("ERRO: Carta  de Sorte ou Revés não encontrada: " + file.getAbsolutePath());
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar carta de propriedade: " + e.getMessage());
       }
    }

    public void ocultarCartaPropriedade() {
        imagemCartaPropriedade = null;
        repaint();
    }

    public void ocultarCartaSorteReves() {
        imagemCartaSorteReves = null;
        repaint();
    }

    private void desenharCartaPropriedade(Graphics2D g2d) {
        if (imagemCartaPropriedade == null) return;
        double escala = 0.8;
        int largura = (int) (imagemCartaPropriedade.getWidth(this) * escala);
        int altura = (int) (imagemCartaPropriedade.getHeight(this) * escala);

        int x = getWidth() - largura +15;
        int y = 20;
        g2d.drawImage(imagemCartaPropriedade, x, y, largura, altura, this);
    }

    private void desenharCartaSorteReves(Graphics2D g2d) {
        if (imagemCartaSorteReves == null) return;
        double escala = 0.8;
        int largura = (int) (imagemCartaSorteReves.getWidth(this) * escala);
        int altura  = (int) (imagemCartaSorteReves.getHeight(this) * escala);

        int x = getWidth() - largura;
        int y = 20;
        g2d.drawImage(imagemCartaSorteReves, x, y, largura, altura, this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        if (imagemMapa != null) {
            int mapaLargura = imagemMapa.getWidth(this);
            int mapaAltura  = imagemMapa.getHeight(this);
            int x = (getWidth() - mapaLargura) / 2;
            int y = (getHeight() - mapaAltura) / 2;
            g2d.drawImage(imagemMapa, x, y, this);
        } else {
            g2d.setColor(Color.RED);
            g2d.fillRect(0, 0, getWidth(), getHeight());
            g2d.setColor(Color.WHITE);
            g2d.drawString("Erro: Imagem do Mapa não Carregada!", 50, 50);
        }

        desenharDados(g2d);
        desenharPeao(g2d);
        desenharCartaPropriedade(g2d);
        desenharCartaSorteReves(g2d);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(janelaPrincipal.LARG_DEFAULT, janelaPrincipal.ALT_DEFAULT);
    }

    @Override
    public void notify(PartidaEvent event) {
        if (event == null) return;

        switch (event.type) {
            case DICE_ROLLED:
                try {
                    int[] dadosRolados = (int[]) event.payload;
                    if (dadosRolados != null && dadosRolados.length == 2) {
                        this.dados = new int[] { dadosRolados[0], dadosRolados[1] };
                        this.dadosVisiveis = true;
                        removerBotaoSalvarJogo();
                    }
                } catch (Exception ex) {}
                break;

            case PURCHASED_PROPERTY:
                removerBotaoComprarPropSeExistir();
                if(FacadeView.getInstance().atualPodeComprarCasa()) {
                	criarBotaoComprarCasa();
                }
                if(FacadeView.getInstance().atualPodeComprarHotel()) {
                	criarBotaoComprarHotel();
                }
                atualizarFundo();
                revalidate();
                repaint();
                break;

                
            case MOVE:
                try {
                    Object[] payload = (Object[]) event.payload;
                    Integer pos = (Integer) payload[0];
                    repaint();
                    if (!FacadeModel.getInstance().ehPropriedade(pos)) {
                        ocultarCartaPropriedade();
                    }
                } catch (Exception ex) {
                    System.err.println("ERRO no processamento do movimento: " + ex.getMessage());
                }
                break;

            case PROPERTY_LANDED:
                try {
                    Integer pos = (Integer) event.payload;
                    ocultarCartaSorteReves();
                    exibirCartaPropriedade(Integer.toString(pos));
                    boolean disponivel      = FacadeModel.getInstance().propriedadeDisponivel(pos);
                    boolean casaDisponivel  = FacadeModel.getInstance().atualPodeComprarCasa();
                    boolean hotelDisponivel = FacadeModel.getInstance().atualPodeComprarHotel();
                    if (casaDisponivel)  criarBotaoComprarCasa();  else removerBotaoComprarCasaSeExistir();
                    if (hotelDisponivel) criarBotaoComprarHotel(); else removerBotaoComprarHotelSeExistir();
                    if (disponivel)      criarBotaoComprarPropriedade(); else removerBotaoComprarPropSeExistir();
                } catch (Exception ignored) {}
                break;

            case SORTE_OU_REVES:
                try {
                    String nomeCarta = (String) event.payload;

                    ocultarCartaPropriedade();
                    removerBotaoComprarPropSeExistir();
                    removerBotaoComprarCasaSeExistir();
                    removerBotaoComprarHotelSeExistir();
                    exibirCartaSorteReves(nomeCarta);
                } catch (Exception ignored) {}
                break;

            case NEXT_PLAYER:
                ocultarCartaPropriedade();
                ocultarCartaSorteReves();
                if (botaoDados != null && botaoDados.getParent() == null) {
                    add(botaoDados);
<<<<<<< Updated upstream
                    if (botaoSetarDados != null) add(botaoSetarDados);
                    if (botaoComprarProp != null) remove(botaoComprarProp);
                    if (bFimTurno != null) remove(bFimTurno);
=======
                    
                    if(botaoSetarDados != null) {
                    	add(botaoSetarDados);                    	
                    }
                    if(botaoComprarProp != null){
                    	remove(botaoComprarProp);
                    }
                    if(bFimTurno != null){
                    	remove(bFimTurno);
                    }
                    criarBotaoSalvarJogo();
>>>>>>> Stashed changes
                    dadosJogadosTurno = false;
                    revalidate();
                }
                atualizarFundo();
                repaint();
                break;
        }
        repaint();
    }
}