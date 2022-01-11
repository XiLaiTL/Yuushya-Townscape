package net.cocofish.yuushya.item;

import net.cocofish.yuushya.Yuushya;
import net.cocofish.yuushya.blockentity.*;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GetBlockStateItem extends AbstractYuushyaItem {

    public GetBlockStateItem(Settings settings, int linecount) {
        super(settings, linecount);
        blockState= Blocks.AIR.getDefaultState();

    }
    private BlockState blockState;
    public ActionResult useOnBlock(ItemUsageContext context) {

        World world=context.getWorld();
        BlockPos pos=context.getBlockPos();
        BlockState blockState1=world.getBlockState(pos);
        Block block=blockState1.getBlock();
        PlayerEntity player = context.getPlayer();
        Hand hand= context.getHand();
        readNbt(player.getStackInHand(hand));
        if(hand==Hand.OFF_HAND){
            //左手右键可以把状态放进融合方块和实体方块中
            if(blockState.getBlock() instanceof AirBlock){
                if (player != null) {player.sendMessage(new TranslatableText("item.yuushya.get_blockstate_item.offhand.fail"), true);}
                return ActionResult.SUCCESS;
            }
            if(block instanceof MixedBlock||block instanceof  ShowBlock) {
                AbstractShowFrameBlock blockEntity=(AbstractShowFrameBlock) world.getBlockEntity(pos);
                blockEntity.setBlock(blockState);
            }
            else {
                return ActionResult.PASS;
            }

            if (player != null) {player.sendMessage(new TranslatableText("item.yuushya.get_blockstate_item.offhand.success"), true);}
            return ActionResult.SUCCESS;
        }
        else{
            //右手右键复制方块状态，如果遇到实体方块和融合方块的话，复制状态并清掉里面的物品
            if(block instanceof MixedBlock || block instanceof ShowBlock) {
                AbstractShowFrameBlock blockEntity=(AbstractShowFrameBlock)world.getBlockEntity(pos);
                if(!(blockEntity.getBlock().getBlock() instanceof  AirBlock)){
                    blockState1=blockEntity.getBlock();
                    blockEntity.removeBlock(0);
                    blockEntity.markDirty();
                    blockEntity.sync();
                }
                else{
                    if (player != null) {player.sendMessage(new TranslatableText("item.yuushya.get_blockstate_item.mainhand.pass"), true);}
                    return ActionResult.PASS;
                }
            }
            blockState=blockState1;
            writeNbt(player.getStackInHand(hand));
            if (player != null) {player.sendMessage(new TranslatableText("item.yuushya.get_blockstate_item.mainhand.success"), true);}
            return ActionResult.SUCCESS;

        }
    }
    @Override
    public void writeNbt(ItemStack itemStack){
        NbtCompound compoundTag = itemStack.getOrCreateNbt();
        compoundTag.put("BlockState", NbtHelper.fromBlockState(blockState));

    }
    @Override
    public void readNbt(ItemStack itemStack){
        NbtCompound compoundTag = itemStack.getOrCreateNbt();
        blockState=NbtHelper.toBlockState(compoundTag.getCompound("BlockState"));
    }
}

