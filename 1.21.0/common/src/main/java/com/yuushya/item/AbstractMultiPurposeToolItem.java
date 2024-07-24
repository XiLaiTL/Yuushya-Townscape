package com.yuushya.item;

import com.yuushya.registries.YuushyaRegistries;
import com.yuushya.utils.YuushyaLogger;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

//子类需要初始化MAX_FORMS，用于规定工具有几种形态 child class should provide the max forms.
public class AbstractMultiPurposeToolItem extends AbstractToolItem{
    public AbstractMultiPurposeToolItem(Properties properties, Integer tipLines) {
        super(properties, tipLines);
    }

    //工具的形态涉及的成员变量
    private int _form_now=0;
    protected int MAX_FORMS;
    public int getForm(){
        return _form_now;
    }
    public void changeForm(){if (++_form_now>=MAX_FORMS) _form_now=0;}

    //method for readNbt and writeNbt
    public void getTag(ItemStack itemStack){
        _form_now = (int) itemStack.getOrDefault(YuushyaRegistries.TRANS_DIRECTION.get(),0);
    }
    public void setTag(ItemStack itemStack){
        itemStack.set((DataComponentType<Integer>) YuushyaRegistries.TRANS_DIRECTION.get(),_form_now);
    }


    //TODO:find the reason why many tools just use on server side
    @Override
    public InteractionResult inOffHandRightClickInAir(Player player, BlockState blockStateTarget, Level level, BlockPos blockPos, ItemStack handItemStack){
        //左手右键用于切换工具状态
        getTag(handItemStack);
        changeForm();
        setTag(handItemStack);
        player.displayClientMessage(Component.translatable(this.getDescriptionId() + "." + _form_now),true);
        return InteractionResult.PASS;
    }
}



