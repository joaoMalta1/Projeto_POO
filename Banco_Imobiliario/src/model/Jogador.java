package model;

import java.util.ArrayList;
import java.util.List;

class Jogador {
	private String nome;
    private Peao peao;
    private double saldo;
    private boolean faliu;
    private List<Propriedade> propriedades;
    private CartaLivreDaPrisao cartaLivre;

    // ---- Prisão ----
    private boolean naPrisao;
    private int rodadasPreso;    
    // private List<TituloDePropriedade> titulos;
    // private List<Carta> cartas;

    // ---- Controle de duplas consecutivas ----
    private int[] ultimoParDados;
    private int[] penultimoParDados;
    
    Jogador(String nome, Peao peao) {
    	this.nome 		  = nome;
        this.peao 		  = peao;
        this.saldo 		  = 4000; // saldo inicial
        this.faliu 		  = false;
        this.naPrisao 	  = false;
        this.rodadasPreso = 0;
        this.propriedades = new ArrayList<>();
        
        ultimoParDados 	  = null;
        penultimoParDados = null;
    }

//    TODO: função de jogar dados e mover peão (integrar com função moverJogador da classe Tabuleiro

    // ---- Getters ----
    Peao getPeao() {
        return peao;
    }

    double getSaldo() {
        return saldo;
    }

    boolean isFaliu() {
        return faliu;
    }

    boolean getIsNaPrisao() {
        return naPrisao;
    }

    int getRodadasPreso() {
        return rodadasPreso;
    }

    List<Propriedade> getPropriedades() {
        return propriedades;
    }

    // ---- Setters ----
    void setNaPrisao(boolean estaNaPrisao) {
    	this.naPrisao = estaNaPrisao;
    }
    
    void setRodadasPreso(int rodadasPreso) {
    	this.rodadasPreso = rodadasPreso;
    }
    
    // ---- Métodos de dinheiro ----
    void adicionarValor(double valor) {
    	System.out.println("[DEBUG] Jogador: " + nome + " Saldo antes: " + saldo);
        if (valor > 0) {
            saldo += valor;
        }
        System.out.println("[DEBUG] Jogador: " + nome + " Saldo depois: " + saldo);
    }

    boolean removerValor(double valor) {
    	System.out.println("[DEBUG] Jogador: " + nome + " Saldo antes: " + saldo);
    	System.out.println("[DEBUG] Valor: " + valor);
    	if(valor <= 0) {
    		return false;
    	}
    	else if (saldo >= valor) {
            saldo -= valor;
            System.out.println("[DEBUG] Jogador: " + nome + " Saldo depois: " + saldo);
            return true; // sucesso
        } else {
            faliu = true;
            falencia();
            return false; // jogador perdeu
        }
    }

     // ---- Métodos de Propriedade ----
    
    void adicionarPropriedade(Propriedade propriedade) {
        if (propriedade != null && !this.propriedades.contains(propriedade)) {
            this.propriedades.add(propriedade);
        }
    }

    // ---- Falência ----
    void falencia() {
        if (this.faliu) {
            for (Propriedade p : new ArrayList<>(this.propriedades)) {
                p.dono = null; 
                if (p instanceof Terreno) {
                    ((Terreno) p).qtdCasas = 0; // é isso ?
                }
            }
            this.propriedades.clear();
            this.saldo = 0.0; 
            System.out.println("[DEBUG] Jogador" + nome + " faliu");
            CentralPartida.getInstance().checaFimJogo();
        }
    }
    
    String getNome() {
    	return nome;
    }
    
    void setCartaLivreDaPrisão(CartaLivreDaPrisao carta) {
    	cartaLivre = carta;
    }
    
//    ---- Métodos de verificação dos 3 dados consecutivos para ida à prisão -----
    
 // Novo método para verificar três duplas consecutivas
    public boolean verificarTresDuplasConsecutivas(int[] novosDados) {
        if (novosDados.length != 2) {
        	throw new IllegalArgumentException("Tamanho do vetor de dados incorreto");
        }
    	
    	if (naPrisao) {
            // Se já está na prisão, não verifica duplas
            resetarControleDuplas();
            return false;
        }
        
        // Verifica se temos três pares de dados consecutivos
        if (ultimoParDados != null && penultimoParDados != null 
        		&& ultimoParDados.length == 2 && penultimoParDados.length == 2 ) {
            boolean ultimaDupla = (ultimoParDados[0] == ultimoParDados[1]);
            boolean penultimaDupla = (penultimoParDados[0] == penultimoParDados[1]);
            boolean novaDupla = (novosDados[0] == novosDados[1]);

            // Atualiza o histórico de dados
            penultimoParDados = ultimoParDados;
            ultimoParDados = novosDados;
            
            // Se todos os três pares forem duplas
            if (ultimaDupla && penultimaDupla && novaDupla) {
                return true;
            }
        }
        
     // Atualiza o histórico de dados para caso de apenas um último dado
        penultimoParDados = ultimoParDados;
        ultimoParDados = novosDados;
        
        return false;
    }
    
    // Método para resetar o controle de duplas
    public void resetarControleDuplas() {
        this.ultimoParDados = null;
        this.penultimoParDados = null;
    }
    
    // Método para ir para a prisão por três duplas
    public void jogouDados(int posicaoPrisao, int[] dados) {
    	if (dados.length != 2) {
        	throw new IllegalArgumentException("Tamanho do vetor de dados incorreto");
        }
    	
    	if(verificarTresDuplasConsecutivas(dados)) {
    		if(cartaLivre != null) {
    			usarCartaLivreDaPrisao();
        	}
    		else {
    			vaiParaPrisao();
	    		this.peao.setPosicao(posicaoPrisao);
	    		System.out.println("DEBUG: Jogador " + nome + " foi para a prisão em " + posicaoPrisao);
    		}
    	}
    }
    
    boolean temCartaLivreDaPrisao() {
    	return cartaLivre != null;
    }
    
    void usarCartaLivreDaPrisao() {
    	System.out.println("[DEBUG] "+ nome + " usou carta Livre da Prisão");
    	cartaLivre.foiUsada();
		cartaLivre = null;
    }
    
    void vaiParaPrisao() {
    	if(cartaLivre == null) {
    		this.naPrisao = true;
    		this.rodadasPreso = 0;
    		resetarControleDuplas();    		
    	}
    }
    
//    @Override
//    public String toString() {
//        return "Nome: " + nome + 
//        	   "\nPeão: " + peao.toString() +
//               "\nSaldo: R$" + String.format("%.2f", saldo) +
//               "\nNa prisão: " + naPrisao +
//               "\nRodadas preso: " + rodadasPreso +
//               "\nFalido: " + faliu;
//    }

	boolean venderPropriedade(Propriedade propASerVendida, Banco banco) {
		for (int i = 0; i < propriedades.size(); i++) {
	        Propriedade p = propriedades.get(i);
	        if (p == propASerVendida) {
	            double valorVenda = p.valorDeVenda();
	            p.vendida();
	            propriedades.remove(i); // Remove by index
	            this.adicionarValor(valorVenda);
	            banco.daDinheiro(valorVenda);
	            return true;
	        }
	    }
		return false;
	}

	int[] getUltimoParDados() {
		return ultimoParDados;
	}

	int[] getPenultimoParDados() {
		return penultimoParDados;
	}

	CartaLivreDaPrisao getCartaLivreDaPrisao() {
		return cartaLivre;
	}

	void setSaldo(double saldo) {
		this.saldo = saldo;
	}

	void setFaliu(boolean faliu) {
		this.faliu = faliu;
	}

	void setUltimoParDados(int[] ultimoDados) {
		this.ultimoParDados = ultimoDados;
	}

	void setPenultimoParDados(int[] penultimoDados) {
		this.penultimoParDados = penultimoDados;
	}
	
	
}
