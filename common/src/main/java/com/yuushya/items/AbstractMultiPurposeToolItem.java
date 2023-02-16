package com.yuushya.items;

import com.yuushya.CreativeModeTabs;
import com.yuushya.mappings.Text;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class AbstractMultiPurposeToolItem extends AbstractToolItem {
    private static final String KEY_TRANS_DIRECTION = "TransDirection";

    /**
     * creativeModeTab: 创造物品栏
     * tipLines: 注释栏行数
     */
    public AbstractMultiPurposeToolItem(CreativeModeTabs.Wrapper creativeModeTab, Integer tipLines) {
        super(creativeModeTab, properties -> properties.stacksTo(1), tipLines);
    }

    private int _form_now = 0;
    /**
     * 子类需要为MAX_FORMS赋值，用于规定工具有几种形态
     */
    protected int MAX_FORMS;

    public int getForm() {
        return _form_now;
    }

    public void changeForm() {
        if (++_form_now >= MAX_FORMS) _form_now = 0;
    }

    public void getTag(ItemStack itemStack) {
        CompoundTag compoundTag = itemStack.getOrCreateTag();
        _form_now = compoundTag.getInt(KEY_TRANS_DIRECTION);
    }

    public void setTag(ItemStack itemStack) {
        CompoundTag compoundTag = itemStack.getOrCreateTag();
        compoundTag.putInt(KEY_TRANS_DIRECTION, _form_now);
        itemStack.setTag(compoundTag);
    }

    @Override
    public InteractionResult inOffHandRightClickInAir(Player player, BlockState blockStateTarget, Level level, BlockPos blockPos, ItemStack handItemStack) {
        getTag(handItemStack);
        changeForm();
        setTag(handItemStack);
        player.displayClientMessage(Text.translatable(this.getDescriptionId() + "." + _form_now), true);
        return InteractionResult.PASS;
    }
}