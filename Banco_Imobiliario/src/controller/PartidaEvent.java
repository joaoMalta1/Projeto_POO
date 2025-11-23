package controller;

public class PartidaEvent {
    public enum Type {
        MOVE, 
        DICE_ROLLED,
        PROPERTY_LANDED, 
        NEXT_PLAYER, 
        INFO,
        PURCHASED_PROPERTY,
        FIM_DE_JOGO,
        PURCHASED_HOUSE,
        PURCHASED_HOTEL,
        SORTE_OU_REVES,
    }

    public final Type type;
    public final Object payload;

    public PartidaEvent(Type type, Object payload) {
        this.type = type;
        this.payload = payload;
    }

    public static PartidaEvent move(int pos, int[] dados) {
        return new PartidaEvent(Type.MOVE, new Object[] { pos, dados });
    }

    public static PartidaEvent diceRolled(int[] dados) {
        return new PartidaEvent(Type.DICE_ROLLED, dados);
    }

    public static PartidaEvent propertyLanded(int posOrId) {
        return new PartidaEvent(Type.PROPERTY_LANDED, posOrId);
    }

    public static PartidaEvent nextPlayer() {
        return new PartidaEvent(Type.NEXT_PLAYER, null);
    }

    public static PartidaEvent purchased_property() {
        return new PartidaEvent(Type.PURCHASED_PROPERTY, null);
    }
    
    public static PartidaEvent purchased_house() {
        return new PartidaEvent(Type.PURCHASED_HOUSE, null);
    }
    
    public static PartidaEvent purchased_hotel() {
        return new PartidaEvent(Type.PURCHASED_HOTEL, null);
    }
    
    public static PartidaEvent sorteOuReves(String nome) {
        return new PartidaEvent(Type.SORTE_OU_REVES, nome);
    }

    public static PartidaEvent info(String msg) {
        return new PartidaEvent(Type.INFO, msg);
    }
    
    public static PartidaEvent fimDeJogo() {
    	return new PartidaEvent(Type.FIM_DE_JOGO, null);
    }
}
