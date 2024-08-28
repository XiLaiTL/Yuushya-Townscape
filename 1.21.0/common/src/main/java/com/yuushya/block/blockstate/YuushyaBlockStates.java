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
    public static final EnumProperty<PositionDirectionXState> XPOS = EnumProperty.create("xpos",PositionDirectionXState.class);
    public static final EnumProperty<PositionDirectionZState> ZPOS = EnumProperty.create("zpos",PositionDirectionZState.class);
    public static final EnumProperty<PositionVerticalState> YPOS=EnumProperty.create("ypos",PositionVerticalState.class);
    public static final EnumProperty<PositionHorizonState> POS_HORIZON=EnumProperty.create("pos",PositionHorizonState.class);
    public static final EnumProperty<PositionVerticalState> POS_VERTICAL=EnumProperty.create("pos",PositionVerticalState.class);
    public static final EnumProperty<ConnectedHorizonState> FRONT = EnumProperty.create("front",ConnectedHorizonState.class);
    public static final EnumProperty<ConnectedHorizonState> BACK = EnumProperty.create("back",ConnectedHorizonState.class);

    public static final EnumProperty<ShapeState> SHAPE=EnumProperty.create("shape",ShapeState.class);

    public static final IntegerProperty X = IntegerProperty.create("x",0,11);
    public static final IntegerProperty Y = IntegerProperty.create("y",0,11);
    public static final IntegerProperty Z = IntegerProperty.create("z",0,11);
    public static final IntegerProperty FORM8 = IntegerProperty.create("form",0,7);
    public static final IntegerProperty FORM7 = IntegerProperty.create("form",0,6);

    public static final IntegerProperty FORM6 = IntegerProperty.create("form",0,5);

    public static final IntegerProperty FORM5 = IntegerProperty.create("form",0,4);

    public static final IntegerProperty FORM4 = IntegerProperty.create("form",0,3);

    public static final IntegerProperty FORM3 = IntegerProperty.create("form",0,2);

    public static final IntegerProperty FORM2 = IntegerProperty.create("form",0,1);

    public static final IntegerProperty LIT = IntegerProperty.create("lit",0,15);
    public static final IntegerProperty DISTANCE = IntegerProperty.create("distance",0,15);
    public static final BooleanProperty ISEND = BooleanProperty.create("isend");
    public static final BooleanProperty ISHUB = BooleanProperty.create("ishub");

    public static Property<?> forms(int n){
        return switch (n){
            case 2->FORM2;
            case 3->FORM3;
            case 4->FORM4;
            case 5->FORM5;
            case 6->FORM6;
            case 7->FORM7;
            default -> FORM8;
        };
    }

    public static Property<?> toBlockStateProperty(String name){
        return switch (name){
            case "xpos"-> XPOS;
            case "zpos"->ZPOS;
            case "pos_horizon"->POS_HORIZON;
            case "pos_vertical"->POS_VERTICAL;
            case "form"->FORM8;
            case "powered"->BlockStateProperties.POWERED;
            case "face"->BlockStateProperties.ATTACH_FACE;
            case "horizontal_facing"->BlockStateProperties.HORIZONTAL_FACING;
            case "facing"->BlockStateProperties.FACING;
            case "ishub"->ISHUB;
            case "shape"->SHAPE;
            case "front"->FRONT;
            case "back"->BACK;
            default -> throw new IllegalStateException("Unexpected value: " + name);
        };
    }

    public static BlockState getDefaultBlockState(BlockState defaultState){
        List<Property<?>> properties= (List<Property<?>>) defaultState.getBlock().getStateDefinition().getProperties();
        if (properties.contains(BlockStateProperties.POWERED)) defaultState= defaultState.setValue(BlockStateProperties.POWERED,false);
        if (properties.contains(BlockStateProperties.FACING)) defaultState=defaultState.setValue(BlockStateProperties.FACING, Direction.NORTH);
        if (properties.contains(BlockStateProperties.ATTACH_FACE)) defaultState=defaultState.setValue(BlockStateProperties.ATTACH_FACE,AttachFace.FLOOR);
        if (properties.contains(BlockStateProperties.HORIZONTAL_FACING)) defaultState=defaultState.setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH);
        if (properties.contains(BlockStateProperties.WATERLOGGED)) defaultState=defaultState.setValue(BlockStateProperties.WATERLOGGED, false);
        if (properties.contains(POS_VERTICAL)) defaultState = defaultState.setValue(POS_VERTICAL,PositionVerticalState.NONE);
        if (properties.contains(YPOS)) defaultState = defaultState.setValue(YPOS,PositionVerticalState.NONE);
        if (properties.contains(POS_HORIZON)) defaultState = defaultState.setValue(POS_HORIZON,PositionHorizonState.NONE);
        if (properties.contains(FRONT)) defaultState = defaultState.setValue(FRONT,ConnectedHorizonState.NONE);
        if (properties.contains(BACK)) defaultState = defaultState.setValue(BACK,ConnectedHorizonState.NONE);
        if (properties.contains(XPOS)) defaultState = defaultState.setValue(XPOS,PositionDirectionXState.NONE);
        if (properties.contains(ZPOS)) defaultState = defaultState.setValue(ZPOS,PositionDirectionZState.NONE);
        if (properties.contains(SHAPE)) defaultState = defaultState.setValue(SHAPE,ShapeState.STRAIGHT);
        return defaultState;
    }


    public static <T extends Comparable<T>> BlockState cycleState(BlockState blockState, Property<T> property, boolean doGetPre) {
        return blockState.setValue(property, getRelative(property.getPossibleValues(), blockState.getValue(property), doGetPre));
    }

    public static <T> T getRelative(Iterable<T> iterable, @Nullable T object, boolean doGetPre) {
        return doGetPre ? Util.findPreviousInIterable(iterable, object) : Util.findNextInIterable(iterable, object);
    }
}
