package net.cocofish.yuushya.item;

import net.cocofish.yuushya.blockentity.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.MessageType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.logging.Log;

import java.io.Console;

public class PosTransItem extends AbstractYuushyaItem{
    public PosTransItem(Settings settings, int linecount) {
        super(settings, linecount);
    }
    private int direction=0;
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        if (!world.isClient && playerEntity != null) {
            if (hand == Hand.OFF_HAND) {
                ItemStack itemStack = playerEntity.getStackInHand(hand);
                readNbt(itemStack);
                changeIntDirection();
                sendMessageSwitchState(playerEntity,itemStack);
                return TypedActionResult.success(playerEntity.getStackInHand(hand));
            }
        }
        return TypedActionResult.pass(playerEntity.getStackInHand(hand));
    }

    public ActionResult useOnBlock(ItemUsageContext context) {
        PlayerEntity playerEntity=context.getPlayer();
        Hand hand= context.getHand();
        World world=context.getWorld();

        BlockPos pos=context.getBlockPos();

        if (!world.isClient && playerEntity != null&&hand==Hand.MAIN_HAND) {
            readNbt(playerEntity.getStackInHand(hand));
            Block block=world.getBlockState(pos).getBlock();
                if(block instanceof ShowBlock ||block instanceof MixedBlock) {
                AbstractShowFrameBlock blockEntity = (AbstractShowFrameBlock) world.getBlockEntity(pos);
               switch (direction){
                   case 0: blockEntity.setShowPos(((int)blockEntity.getShowX()+1)%17,0);break;
                   case 1: blockEntity.setShowPos(((int)blockEntity.getShowX()-1)%17,0);break;
                   case 2: blockEntity.setShowPos(((int)blockEntity.getShowY()+1)%17,1);break;
                   case 3: blockEntity.setShowPos(((int)blockEntity.getShowY()-1)%17,1);break;
                   case 4: blockEntity.setShowPos(((int)blockEntity.getShowZ()+1)%17,2);break;
                   case 5: blockEntity.setShowPos(((int)blockEntity.getShowZ()-1)%17,2);break;
                   case 6: blockEntity.setShowPos((blockEntity.getShowRotationX()+22.5)<360?(blockEntity.getShowRotationX()+22.5):0,3);break;
                   case 7: blockEntity.setShowPos((blockEntity.getShowRotationY()+22.5)<360?(blockEntity.getShowRotationY()+22.5):0,4);break;
                   case 8: blockEntity.setShowPos((blockEntity.getShowRotationZ()+22.5)<360?(blockEntity.getShowRotationZ()+22.5):0,5);break;
               }
                sendMessage(playerEntity, new TranslatableText(this.getTranslationKey() + ".switch",new Object[]{blockEntity.getShowX(),blockEntity.getShowY(),blockEntity.getShowZ(),blockEntity.getShowRotationX(),blockEntity.getShowRotationY(),blockEntity.getShowRotationZ()}));

            }
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

    @Override
    public void writeNbt(ItemStack itemStack){
        NbtCompound compoundTag = itemStack.getOrCreateNbt();
        compoundTag.putInt("TransDirection",direction);

    }
    @Override
    public void readNbt(ItemStack itemStack){
        NbtCompound compoundTag = itemStack.getOrCreateNbt();
        direction=compoundTag.getInt("TransDirection");
    }
    public int getDirection() {return direction;}
    private int changeIntDirection(){
        direction++;
        if(direction>8)direction=0;
        return direction;
    }

    private void sendMessageSwitchState(PlayerEntity player,ItemStack itemStack){
        writeNbt(itemStack);
        sendMessage(player, new TranslatableText(this.getTranslationKey() + "."+direction));
    }
    private static void sendMessage(PlayerEntity player, Text message) {
        ((ServerPlayerEntity)player).sendMessage(message, MessageType.GAME_INFO, Util.NIL_UUID);
    }
}
