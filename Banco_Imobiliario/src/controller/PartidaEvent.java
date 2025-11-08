package controller;

public class PartidaEvent {
    public enum Type {
        MOVE, // player moved; payload: position (Integer) and dice (int[])
        DICE_ROLLED, // dice rolled; payload: dice
        PROPERTY_LANDED, // landed on property; payload: property id/name (String or Integer)
        NEXT_PLAYER, // next player turn
        INFO // generic info/message
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

    public static PartidaEvent info(String msg) {
        return new PartidaEvent(Type.INFO, msg);
    }
}
