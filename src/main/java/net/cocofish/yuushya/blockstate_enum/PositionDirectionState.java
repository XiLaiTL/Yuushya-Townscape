package net.cocofish.yuushya.blockstate_enum;
import net.minecraft.util.StringIdentifiable;

public enum PositionDirectionState implements StringIdentifiable {
    NORTH("north"),
    SOUTH("south"),
    WEST("west"),
    EAST("east"),
    MIDDLE("middle"),
    NONE("none");
    private final String name;
    private PositionDirectionState(String name) {
        this.name = name;
    }
    public String asString() {
        return this.name;
    }
}