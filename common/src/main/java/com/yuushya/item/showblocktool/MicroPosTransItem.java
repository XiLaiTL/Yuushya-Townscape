package com.yuushya.item.showblocktool;

import com.mojang.math.Vector3d;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

public class MicroPosTransItem extends PosTransItem{
    public MicroPosTransItem(Properties properties, Integer tipLines) {
        super(properties, tipLines);
        MAX_FORMS=4;//x:0,y:1,z:2,floor:3;
    }
    @Override
    public InteractionResult inMainHandRightClickOnBlock(Player player, BlockState blockState, Level level, BlockPos blockPos, ItemStack handItemStack){
        //右手右键 向后位移
        return translateData(player,blockState,level,blockPos,handItemStack,(transformData)->{
            Vector3d pos = transformData.pos;
            switch (getForm()){
                case 0-> pos.x-=0.001;
                case 1-> pos.y-=0.001;
                case 2-> pos.z-=0.001;
                case 3-> pos.set(Math.round(pos.x)%17,Math.round(pos.y)%17,Math.round(pos.z)%17);
            }
            player.displayClientMessage(new TranslatableComponent(this.getDescriptionId()+".switch",pos.x,pos.y,pos.z),true);
        });
    }
    @Override
    public InteractionResult inMainHandLeftClickOnBlock(Player player, BlockState blockState, Level level, BlockPos blockPos, ItemStack handItemStack){
        //右手左键 向前位移
        return translateData(player,blockState,level,blockPos,handItemStack,(transformData)->{
            Vector3d pos = transformData.pos;
            switch (getForm()){
                case 0-> pos.x+=0.001;
                case 1-> pos.y+=0.001;
                case 2-> pos.z+=0.001;
            }
            player.displayClientMessage(new TranslatableComponent(this.getDescriptionId()+".switch",pos.x,pos.y,pos.z),true);
        });
    }

}
