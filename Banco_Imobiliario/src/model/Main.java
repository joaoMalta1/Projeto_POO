package model;

import model.Peao.Peao;

public class Main 
{
    public static void main(String[] args) {
        
        Peao peaoAzul = new Peao("azul");

        // Exibir o estado inicial
        System.out.println(peaoAzul);
        peaoAzul.setPosicao(4);
        System.out.println(peaoAzul.getPosicao());
        peaoAzul.mover(56);
        System.out.println(peaoAzul.getPosicao());
    }
}
