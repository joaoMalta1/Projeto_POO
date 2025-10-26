package model;

public class FacadeModel {
	private static FacadeModel fm = null;
	
	private FacadeModel() {}
	
	public static FacadeModel getInstance() {
		if(fm == null) {
			fm = new FacadeModel();
		}
		return fm;
	}
	
	public int[] jogarDados() {
		return Dados.getInstance().jogar();
	}
}
