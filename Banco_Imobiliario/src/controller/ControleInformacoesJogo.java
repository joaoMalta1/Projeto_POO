package controller;

import controller.CorPeao;

import java.util.ArrayList;

// Singleton
public class ControleInformacoesJogo {
	private int qtd_jogadores;
	private static ControleInformacoesJogo ctrl = null;
	private ArrayList<String> nome_jogadores;
	private ArrayList<CorPeao> cor_jogadores;
	private int jogadorAtual;
	
	private ControleInformacoesJogo(){
		qtd_jogadores = 3;
		jogadorAtual = 0;
		nome_jogadores = new ArrayList<>();
		cor_jogadores = new ArrayList<>();
	}
	
	public static ControleInformacoesJogo getInstance() {
		if(ctrl == null) {
			ctrl = new ControleInformacoesJogo();}
		return ctrl;
	}
	
	public void setQtdJogadores(int qtd) {
		qtd_jogadores = qtd;
	}
	
	public int getQtdJogadores() {
		return qtd_jogadores;
	}
	
	// [0, qtd_jogadores]
	public int getJogadorAtual() {
		return jogadorAtual;
	}
	
	public void proxJogador() {
		jogadorAtual = (++jogadorAtual)%qtd_jogadores;
	}
	
	public boolean ehUltimoJogador() {
		if(jogadorAtual == qtd_jogadores-1)
			return true;
		return false;
	}
	
	public boolean nomeJaExiste(String nome) {
		for(String n : nome_jogadores) {
			if(n.equals(nome)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean corJaExiste(CorPeao cor) {
		for(CorPeao c : cor_jogadores) {
			if(c.equals(cor)) {
				return true;
			}
		}
		return false;
	}
	
//	true = adicionado
	public boolean addJogador(int i, String nome, CorPeao cor) {
		if(i > qtd_jogadores-1)
			throw new IllegalArgumentException("Indice fora do escopo");
		
		if(nomeJaExiste(nome) || corJaExiste(cor))
			return false;
		
		if(i == nome_jogadores.size())
			return addJogador(nome, cor);
		nome_jogadores.add(i, nome);
		cor_jogadores.add(i, cor);
		return true;
	}
	
	public boolean addJogador(String nome, CorPeao cor) {
		if(nome_jogadores.size() >= qtd_jogadores)
			return false;
		nome_jogadores.add(nome);
		cor_jogadores.add(cor);
		return true;
	}
	
	public CorPeao getCorJogador(String nome) {
		for(int i = 0; i < nome_jogadores.size(); i++)
			if(nome_jogadores.get(i).equals(nome))
				return cor_jogadores.get(i);
		
		throw new IllegalArgumentException("Nome Inexistente");
	}
	
	public CorPeao getCorJogador(int n) {
		if(n >= qtd_jogadores || n < 0)
			throw new IllegalArgumentException("Indice fora do escopo");
		return cor_jogadores.get(n);
	}
	
	public String getNomeJogador(int n) {
		if(n > qtd_jogadores)
			throw new IllegalArgumentException("Indice fora do escopo");
		return nome_jogadores.get(n);
	}
	
	public String getNomeJogador(CorPeao cor) {
		for(int i = 0; i < nome_jogadores.size(); i++)
			if(cor_jogadores.get(i).equals(cor))
				return nome_jogadores.get(i);
		throw new IllegalArgumentException("Cor inexistente");			
	} 
}
