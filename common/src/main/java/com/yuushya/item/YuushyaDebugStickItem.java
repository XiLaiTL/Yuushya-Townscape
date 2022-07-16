package com.yuushya.item;


import com.yuushya.block.blockstate.YuushyaBlockStates;
import com.yuushya.blockentity.showblock.ShowBlock;
import com.yuushya.blockentity.showblock.ShowBlockEntity;
import com.yuushya.utils.YuushyaUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DebugStickItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.Collection;

public class YuushyaDebugStickItem extends AbstractToolItem {
    public YuushyaDebugStickItem(Properties properties, Integer tipLines) {
        super(properties, tipLines);
    }

    //对方块主手右键
    @Override
    public InteractionResult inMainHandRightClickOnBlock(Player player, BlockState blockState, Level level, BlockPos blockPos, ItemStack handItemStack) {
        return !this.handleInteraction(player, level.getBlockState(blockPos), level, blockPos, true, handItemStack)
                ? InteractionResult.FAIL : InteractionResult.SUCCESS;
    }

    //对方块主手左键
    @Override
    public InteractionResult inMainHandLeftClickOnBlock(Player player, BlockState blockState, Level level, BlockPos blockPos, ItemStack handItemStack) {
        this.handleInteraction(player, blockState, level, blockPos, false, player.getItemInHand(InteractionHand.MAIN_HAND));
        return InteractionResult.PASS;
    }

    private boolean handleInteraction(Player player, BlockState blockState, LevelAccessor levelAccessor, BlockPos blockPos, boolean isRightClicked, ItemStack itemStack) {
        if (!player.canUseGameMasterBlocks()) {
            return false;
        }
        boolean isShowBlock = blockState.getBlock() instanceof ShowBlock;
        blockState = YuushyaUtils.getBlockState(blockState, levelAccessor, blockPos);
        StateDefinition<Block, BlockState> stateDefinition = blockState.getBlock().getStateDefinition();
        Collection<Property<?>> collection = stateDefinition.getProperties();
        String blockName = Registry.BLOCK.getKey(blockState.getBlock()).toString();
        if (collection.isEmpty()) {
            player.displayClientMessage(new TranslatableComponent(this.getDescriptionId() + ".empty", blockName), true);
            return false;
        }
        CompoundTag compoundTag = itemStack.getOrCreateTagElement("DebugProperty");
        String propertyName = compoundTag.getString(blockName);
        Property<?> property = stateDefinition.getProperty(propertyName);
        if (isRightClicked) {
            if (property == null) {
                property = collection.iterator().next();
            }
            BlockState blockStateNew = YuushyaBlockStates.cycleState(blockState, property, player.isSecondaryUseActive());
            if (isShowBlock) {
                ShowBlockEntity showBlockEntity = (ShowBlockEntity) levelAccessor.getBlockEntity(blockPos);
                showBlockEntity.setSlotBlockState(0, blockStateNew);
                showBlockEntity.saveChanged();
            } else {
                levelAccessor.setBlock(blockPos, blockStateNew, 18);
            }
            player.displayClientMessage(new TranslatableComponent(this.getDescriptionId() + ".update", property.getName(), getNameHelper(blockStateNew, property)), true);
        } else {
            property = YuushyaBlockStates.getRelative(collection, property, player.isSecondaryUseActive());
            String propertyNameNew = property.getName();
            compoundTag.putString(blockName, propertyNameNew);
            player.displayClientMessage(new TranslatableComponent(this.getDescriptionId() + ".select", propertyNameNew, getNameHelper(blockState, property)), true);
        }
        return true;
    }

    private static <T extends Comparable<T>> String getNameHelper(BlockState blockState, Property<T> property) {
        return property.getName(blockState.getValue(property));
    }
}
