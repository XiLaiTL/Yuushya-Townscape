package com.yuushya.block.blockstate;

import net.minecraft.util.StringRepresentable;

public enum PositionDirectionZState implements StringRepresentable {
    NORTH("north"),
    SOUTH("south"),
    MIDDLE("middle"),
    NONE("none");
    private final String name;
    PositionDirectionZState(String name) {
        this.name = name;
    }
    public String getSerializedName() {
        return this.name;
    }
}
