package com.yuushya.item.showblocktool;

import com.mojang.math.Vector3f;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

public class ScaleTransItem extends PosTransItem{
    public ScaleTransItem(Properties properties, Integer tipLines) {
        super(properties, tipLines);
        MAX_FORMS=1;
    }
    @Override
    public InteractionResult inMainHandRightClickOnBlock(Player player, BlockState blockState, Level level, BlockPos blockPos, ItemStack handItemStack){
        //右手右键 向后位移
        return translateData(player,blockState,level,blockPos,handItemStack,(transformData)->{
            Vector3f scales = transformData.scales;
            switch (getForm()){
                case 0-> scales.add(0.1f,0.1f,0.1f);
            }
            player.displayClientMessage(new TranslatableComponent(this.getDescriptionId()+".switch",scales.x(),scales.y(),scales.z()),true);
        });
    }
    @Override
    public InteractionResult inMainHandLeftClickOnBlock(Player player, BlockState blockState, Level level, BlockPos blockPos, ItemStack handItemStack){
        //右手左键 向前位移
        return translateData(player,blockState,level,blockPos,handItemStack,(transformData)->{
            Vector3f scales = transformData.scales;
            switch (getForm()){
                case 0-> scales.add(-0.1f,-0.1f,-0.1f);
            }
            player.displayClientMessage(new TranslatableComponent(this.getDescriptionId()+".switch",scales.x(),scales.y(),scales.z()),true);
        });
    }

}
