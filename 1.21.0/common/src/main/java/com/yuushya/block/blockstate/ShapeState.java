package com.yuushya.block.blockstate;

import net.minecraft.util.StringRepresentable;

public enum ShapeState implements StringRepresentable {
    STRAIGHT("straight"),
    INNER("inner"),
    OUTER("outer");
    private final String name;
    ShapeState(String name) {
        this.name = name;
    }
    public String getSerializedName() {
        return this.name;
    }
}