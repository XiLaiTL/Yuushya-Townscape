package net.cocofish.yuushya.blockstate_enum;
import net.minecraft.util.StringIdentifiable;

public enum PositionVerticalState implements StringIdentifiable {
    TOP("top"),
    MIDDLE("middle"),
    BOTTOM("bottom"),
    NONE("none");
    private final String name;
    private PositionVerticalState(String name) {
        this.name = name;
    }
    public String asString() {
        return this.name;
    }
}