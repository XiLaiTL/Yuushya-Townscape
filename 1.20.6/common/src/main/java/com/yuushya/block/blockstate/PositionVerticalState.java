package com.yuushya.block.blockstate;

import net.minecraft.util.StringRepresentable;

public enum PositionVerticalState implements StringRepresentable {
    TOP("top"),
    MIDDLE("middle"),
    BOTTOM("bottom"),
    NONE("none");
    private final String name;
    PositionVerticalState(String name) {
        this.name = name;
    }
    public String getSerializedName() {
        return this.name;
    }
}
