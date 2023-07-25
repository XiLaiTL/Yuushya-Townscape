package com.yuushya.block.blockstate;

import net.minecraft.Util;
import net.minecraft.core.Direction;
import net.minecraft.world.item.DebugStickItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.*;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class YuushyaBlockStates {
    public static final EnumProperty<PositionDirectionState> POS_DIRECTION = EnumProperty.create("pos",PositionDirectionState.class);
    public static final EnumProperty<PositionDirectionState> XPOS = EnumProperty.create("xpos",PositionDirectionState.class);
    public static final EnumProperty<PositionDirectionState> ZPOS = EnumProperty.create("zpos",PositionDirectionState.class);
    public static final EnumProperty<PositionHorizonState> POS_HORIZON=EnumProperty.create("pos",PositionHorizonState.class);
    public static final EnumProperty<PositionVerticalState> POS_VERTICAL=EnumProperty.create("pos",PositionVerticalState.class);
    public static final IntegerProperty FORM = IntegerProperty.create("form",0,7);
    public static final IntegerProperty LIT = IntegerProperty.create("lit",0,15);
    public static final IntegerProperty DISTANCE = IntegerProperty.create("distance",0,15);
    public static final BooleanProperty ISEND = BooleanProperty.create("isend");
    public static final BooleanProperty ISHUB = BooleanProperty.create("ishub");

    public static Property<?> toBlockStateProperty(String name){
        return switch (name){
            case "xpos"->(YuushyaBlockStates.XPOS);
            case "zpos"->(YuushyaBlockStates.ZPOS);
            case "pos_direction"->(YuushyaBlockStates.POS_DIRECTION);
            case "pos_horizon"->(YuushyaBlockStates.POS_HORIZON);
            case "pos_vertical"->(YuushyaBlockStates.POS_VERTICAL);
            case "form"->(YuushyaBlockStates.FORM);
            case "powered"->(BlockStateProperties.POWERED);
            case "face"->(BlockStateProperties.ATTACH_FACE);
            case "horizontal_facing"->(BlockStateProperties.HORIZONTAL_FACING);
            case "facing"->(BlockStateProperties.FACING);
            case "ishub"->(YuushyaBlockStates.ISHUB);
            default -> throw new IllegalStateException("Unexpected value: " + name);
        };
    }

    public static BlockState getDefaultBlockState(BlockState defaultState){
        List<Property<?>> properties= (List<Property<?>>) defaultState.getBlock().getStateDefinition().getProperties();
        if (properties.contains(BlockStateProperties.POWERED)) defaultState= defaultState.setValue(BlockStateProperties.POWERED,false);
        if (properties.contains(BlockStateProperties.FACING)) defaultState=defaultState.setValue(BlockStateProperties.FACING, Direction.NORTH);
        if (properties.contains(BlockStateProperties.ATTACH_FACE)) defaultState=defaultState.setValue(BlockStateProperties.ATTACH_FACE,AttachFace.FLOOR);
        if (properties.contains(BlockStateProperties.HORIZONTAL_FACING)) defaultState=defaultState.setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH);
        return defaultState;
    }


    public static <T extends Comparable<T>> BlockState cycleState(BlockState blockState, Property<T> property, boolean doGetPre) {
        return blockState.setValue(property, getRelative(property.getPossibleValues(), blockState.getValue(property), doGetPre));
    }

    public static <T> T getRelative(Iterable<T> iterable, @Nullable T object, boolean doGetPre) {
        return doGetPre ? Util.findPreviousInIterable(iterable, object) : Util.findNextInIterable(iterable, object);
    }
}
