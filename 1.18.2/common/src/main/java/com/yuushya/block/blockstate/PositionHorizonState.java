package com.yuushya.block.blockstate;

import net.minecraft.util.StringRepresentable;

public enum PositionHorizonState implements StringRepresentable {
    LEFT("left"),
    MIDDLE("middle"),
    RIGHT("right"),
    NONE("none");
    private final String name;
    PositionHorizonState(String name) {
        this.name = name;
    }
    public String getSerializedName() {
        return this.name;
    }
}
