package com.yuushya.block.blockstate;

import net.minecraft.util.StringRepresentable;

public enum ConnectedHorizonState implements StringRepresentable {
    LEFT("left"),
    RIGHT("right"),
    NONE("none");
    private final String name;
    ConnectedHorizonState(String name) {
        this.name = name;
    }
    public String getSerializedName() {
        return this.name;
    }
}