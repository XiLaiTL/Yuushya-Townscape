package com.yuushya.item.showblocktool;

import com.yuushya.block.blockstate.YuushyaBlockStates;
import com.yuushya.item.AbstractToolItem;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;

public class GetLitItem extends AbstractToolItem {
    public GetLitItem(Properties properties, Integer tipLines) {
        super(properties, tipLines);
    }
    //对方块主手右键
    @Override
    public InteractionResult inMainHandRightClickOnBlock(Player player, BlockState blockState, Level level, BlockPos blockPos, ItemStack handItemStack){
        return transformTwoProperty(player,blockState,level,blockPos,handItemStack,YuushyaBlockStates.LIT,BlockStateProperties.LIT,player.isSecondaryUseActive());
    }
    //对方块主手左键
    @Override
    public InteractionResult inMainHandLeftClickOnBlock(Player player, BlockState blockState, Level level, BlockPos blockPos, ItemStack handItemStack){
        return transformTwoProperty(player,blockState,level,blockPos,handItemStack,YuushyaBlockStates.LIT,BlockStateProperties.LIT,!player.isSecondaryUseActive());
    }

    private static <T extends Comparable<T>,K extends Comparable<K>> InteractionResult transformTwoProperty(Player player, BlockState blockState, Level level, BlockPos blockPos, ItemStack handItemStack, Property<T> property,Property<K> property2, boolean doGetPre){
        if (blockState.hasProperty(property)){
            BlockState blockState2 = YuushyaBlockStates.cycleState(blockState, property, doGetPre);
            level.setBlock(blockPos, blockState2, 18);
            player.displayClientMessage(new TranslatableComponent(handItemStack.getDescriptionId()+".select", property.getName(),property.getName(blockState2.getValue(property))),true);
            return InteractionResult.CONSUME;
        }
        else if (blockState.hasProperty(property2)){
            BlockState blockState2 = YuushyaBlockStates.cycleState(blockState, property2, doGetPre);
            level.setBlock(blockPos, blockState2, 18);
            player.displayClientMessage(new TranslatableComponent(handItemStack.getDescriptionId()+".select", property2.getName(),property2.getName(blockState2.getValue(property2))),true);
            return InteractionResult.CONSUME;
        }
        return InteractionResult.PASS;
    }
}
