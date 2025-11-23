package controller;

public class PartidaEvent {
    public enum Tipo {
        DICE_ROLLED,
        MOVE,
        PROPERTY_LANDED,
        PURCHASED_PROPERTY,
        INFO,
        PROPERTY_SOLD,
        NEXT_PLAYER,
        GAME_ENDED,
        PURCHASED_HOUSE,
        PURCHASED_HOTEL,
        SORTE_OU_REVES,
    }

    public final Tipo type;
    public final Object payload;

    public PartidaEvent(Tipo type, Object payload) {
        this.type = type;
        this.payload = payload;
    }

    public static PartidaEvent move(int pos, int[] dados) {
        return new PartidaEvent(Tipo.MOVE, new Object[] { pos, dados });
    }

    public static PartidaEvent diceRolled(int[] dados) {
        return new PartidaEvent(Tipo.DICE_ROLLED, dados);
    }

    public static PartidaEvent propertyLanded(int posOrId) {
        return new PartidaEvent(Tipo.PROPERTY_LANDED, posOrId);
    }

    public static PartidaEvent nextPlayer() {
        return new PartidaEvent(Tipo.NEXT_PLAYER, null);
    }

    public static PartidaEvent purchased_property() {
        return new PartidaEvent(Tipo.PURCHASED_PROPERTY, null);
    }
    
    public static PartidaEvent purchased_house() {
        return new PartidaEvent(Tipo.PURCHASED_HOUSE, null);
    }
    
    public static PartidaEvent purchased_hotel() {
        return new PartidaEvent(Tipo.PURCHASED_HOTEL, null);
    }
    
    public static PartidaEvent sorteOuReves(String nome) {
        return new PartidaEvent(Tipo.SORTE_OU_REVES, nome);
    }

    public static PartidaEvent info(String msg) {
        return new PartidaEvent(Tipo.INFO, msg);
    }
    
    public static PartidaEvent fimDeJogo() {
    	return new PartidaEvent(Tipo.GAME_ENDED, null);
    }

    public static PartidaEvent propertySold() {
        return new PartidaEvent(Tipo.PROPERTY_SOLD, null);
    }
}
