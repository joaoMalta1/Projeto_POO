package model;


public class Main 
{
    public static void main(String[] args) {
        
        Peao peaoAzul = new Peao(Peao.CorPeao.AZUL);

        // Exibir o estado inicial
        System.out.println(peaoAzul);
        peaoAzul.setPosicao(4);
        System.out.println(peaoAzul.getPosicao());
        peaoAzul.mover(56);
        System.out.println(peaoAzul.getPosicao());
    }
}
