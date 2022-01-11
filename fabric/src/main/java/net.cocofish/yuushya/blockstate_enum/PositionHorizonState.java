package net.cocofish.yuushya.blockstate_enum;
import net.minecraft.util.StringIdentifiable;

public enum PositionHorizonState implements StringIdentifiable {
    LEFT("left"),
    MIDDLE("middle"),
    RIGHT("right"),
    NONE("none");
    private final String name;
    private PositionHorizonState(String name) {
        this.name = name;
    }
    public String asString() {
        return this.name;
    }
}