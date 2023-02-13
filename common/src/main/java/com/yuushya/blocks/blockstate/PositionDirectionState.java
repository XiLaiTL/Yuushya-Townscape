package com.yuushya.blocks.blockstate;

import net.minecraft.util.StringRepresentable;

public enum PositionDirectionState implements StringRepresentable {
    NORTH("north"),
    SOUTH("south"),
    WEST("west"),
    EAST("east"),
    MIDDLE("middle"),
    NONE("none");
    private final String name;

    PositionDirectionState(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}