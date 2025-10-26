package view;

import java.awt.Color;
import controller.CorPeao;

// Singleton
class Cores {
	static final Color VERMELHA = new Color(230, 100, 100);
	static final Color AZUL 	= new Color(173, 216, 230);
	static final Color LARANJA = new Color(255, 200, 150);
	static final Color AMARELA	= new Color(255, 249, 196);
	static final Color ROXA		= new Color(200, 180, 255);
	static final Color CINZA	= new Color(220, 220, 220);
	private static Cores ctrl = null;
	
	private Cores() {}
	
	Cores getInstance() {
		if(ctrl == null) {
			ctrl = new Cores();
		}
		return ctrl;
	}
	
	Color corCorrespondente(CorPeao cor) {
		switch (cor) {
			case VERMELHA:
				return VERMELHA;
			case AZUL:
				return AZUL;
			case LARANJA:
				return LARANJA;
			case AMARELA:
				return AMARELA;
			case ROXA:
				return ROXA;
			case CINZA:
				return CINZA;
			default:
				return CINZA;
		}
	}
}
