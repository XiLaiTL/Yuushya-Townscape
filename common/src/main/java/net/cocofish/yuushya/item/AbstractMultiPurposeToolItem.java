package net.cocofish.yuushya.item;

import net.cocofish.yuushya.blockentity.showblock.ShowBlock;
import net.cocofish.yuushya.blockentity.showblock.ShowBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
    public int getForm(){return _form_now;}
    public void changeForm(){if (++_form_now>=MAX_FORMS) _form_now=0;}

    //method for readNbt and writeNbt
    public void getTag(ItemStack itemStack){
        CompoundTag compoundTag = itemStack.getOrCreateTag();
        _form_now=compoundTag.getInt("TransDirection");
    }
    public void setTag(ItemStack itemStack){
        CompoundTag compoundTag = itemStack.getOrCreateTag();
        compoundTag.putInt("TransDirection",_form_now);
        itemStack.setTag(compoundTag);
    }



    @Override
    public InteractionResult inOffHandRightClickInAir(Player player, BlockState blockStateTarget, LevelAccessor level, BlockPos blockPos, ItemStack handItemStack){
        //左手右键用于切换工具状态
        getTag(handItemStack);
        changeForm();
        setTag(handItemStack);
        player.displayClientMessage(new TranslatableComponent(this.getDescriptionId()+"."+_form_now),true);
        return InteractionResult.PASS;
    }
}



