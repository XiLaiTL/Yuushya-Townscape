package com.yuushya.item;

import com.yuushya.Yuushya;
import com.yuushya.block.blockstate.YuushyaBlockStates;
import com.yuushya.utils.YuushyaLogger;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DebugStickItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

public class FormTransItem extends AbstractToolItem{
    public FormTransItem(Properties properties, Integer tipLines) {
        super(properties, tipLines);
    }
    //对方块主手右键
    @Override
    public InteractionResult inMainHandRightClickOnBlock(Player player, BlockState blockState, Level level, BlockPos blockPos, ItemStack handItemStack){
        return transformOneProperty(player,blockState,level,blockPos,handItemStack,YuushyaBlockStates.FORM,false);
    }
    //对方块主手左键
    @Override
    public InteractionResult inMainHandLeftClickOnBlock(Player player, BlockState blockState, Level level, BlockPos blockPos, ItemStack handItemStack){
        return transformOneProperty(player,blockState,level,blockPos,handItemStack,YuushyaBlockStates.FORM,true);
    }

    private static <T extends Comparable<T>> InteractionResult transformOneProperty(Player player, BlockState blockState, Level level, BlockPos blockPos, ItemStack handItemStack, Property<T> property,boolean doGetPre){
        if (blockState.hasProperty(property)){
            BlockState blockState2 = YuushyaBlockStates.cycleState(blockState, property, doGetPre);
            level.setBlock(blockPos, blockState2, 18);
            player.displayClientMessage(Component.translatable(handItemStack.getDescriptionId()+".select", property.getName(),property.getName(blockState2.getValue(property))),true);
            return InteractionResult.CONSUME;
        }
        return InteractionResult.PASS;
    }
}
