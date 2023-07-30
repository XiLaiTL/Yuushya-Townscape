package com.yuushya.block.blockstate;

import net.minecraft.util.StringRepresentable;

public enum PositionDirectionXState implements StringRepresentable {
    WEST("west"),
    EAST("east"),
    MIDDLE("middle"),
    NONE("none");
    private final String name;
    PositionDirectionXState(String name) {
        this.name = name;
    }
    public String getSerializedName() {
        return this.name;
    }
}
