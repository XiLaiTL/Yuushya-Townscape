package com.yuushya.block.blockstate;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.*;

public class YuushyaBlockStates {
    public static final EnumProperty<PositionDirectionState> POS_DIRECTION = EnumProperty.create("pos",PositionDirectionState.class);
    public static final EnumProperty<PositionHorizonState> POS_HORIZON=EnumProperty.create("pos",PositionHorizonState.class);
    public static final EnumProperty<PositionVerticalState> POS_VERTICAL=EnumProperty.create("pos",PositionVerticalState.class);
    public static final IntegerProperty FORM = IntegerProperty.create("form",0,15);
    
    public static Property<?> toBlockStateProperty(String name){
        return switch (name){
            case "pos_direction"->(YuushyaBlockStates.POS_DIRECTION);
            case "pos_horizon"->(YuushyaBlockStates.POS_HORIZON);
            case "pos_vertical"->(YuushyaBlockStates.POS_VERTICAL);
            case "form"->(YuushyaBlockStates.FORM);
            case "powered"->(BlockStateProperties.POWERED);
            case "face"->(BlockStateProperties.ATTACH_FACE);
            case "horizontal_facing"->(BlockStateProperties.HORIZONTAL_FACING);
            case "facing"->(BlockStateProperties.FACING);
            default -> throw new IllegalStateException("Unexpected value: " + name);
        };
    }
}
