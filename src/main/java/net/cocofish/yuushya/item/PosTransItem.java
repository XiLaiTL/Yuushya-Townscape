package net.cocofish.yuushya.item;

import net.cocofish.yuushya.blockentity.MixedBlock;
import net.cocofish.yuushya.blockentity.MixedBlockEntity;
import net.cocofish.yuushya.blockentity.ShowBlock;
import net.cocofish.yuushya.blockentity.ShowBlockEntity;
import net.cocofish.yuushya.entity.ExhibitionEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.MessageType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PosTransItem extends AbstractYuushyaItem{
    public PosTransItem(Settings settings, String registname,int linecount) {
        super(settings, registname,linecount);
    }
    private int direction=0;
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        if (!world.isClient && playerEntity != null) {
            if (hand == Hand.OFF_HAND) {
                ItemStack itemStack = playerEntity.getStackInHand(hand);
                fromTag(itemStack);
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
            fromTag(playerEntity.getStackInHand(hand));
            if(world.getBlockState(pos).getBlock() instanceof MixedBlock) {
                MixedBlockEntity blockEntity = (MixedBlockEntity) world.getBlockEntity(pos);
                switch (direction){
                    case 0: blockEntity.setShowPos(((int)blockEntity.getShowX()+1)%17,0);break;
                    case 1: blockEntity.setShowPos(((int)blockEntity.getShowX()-1)%17,0);break;
                    case 2: blockEntity.setShowPos(((int)blockEntity.getShowY()+1)%17,1);break;
                    case 3: blockEntity.setShowPos(((int)blockEntity.getShowY()-1)%17,1);break;
                    case 4: blockEntity.setShowPos(((int)blockEntity.getShowZ()+1)%17,2);break;
                    case 5: blockEntity.setShowPos(((int)blockEntity.getShowZ()-1)%17,2);break;
                    case 6: blockEntity.setShowPos((blockEntity.getShowYaw()+22.5)<360?(blockEntity.getShowYaw()+22.5):0,3);break;
                    case 7: blockEntity.setShowPos((blockEntity.getShowPitch()+22.5)<360?(blockEntity.getShowPitch()+22.5):0,4);break;
                }
                sendMessage(playerEntity, new TranslatableText(this.getTranslationKey() + ".switch",new Object[]{blockEntity.getShowX(),blockEntity.getShowY(),blockEntity.getShowZ(),blockEntity.getShowYaw(),blockEntity.getShowPitch()}));

            }
            else if(world.getBlockState(pos).getBlock() instanceof ShowBlock) {
                ShowBlockEntity blockEntity = (ShowBlockEntity) world.getBlockEntity(pos);
               switch (direction){
                   case 0: blockEntity.setShowPos(((int)blockEntity.getShowX()+1)%16,0);break;
                   case 1: blockEntity.setShowPos(((int)blockEntity.getShowX()-1)%16,0);break;
                   case 2: blockEntity.setShowPos(((int)blockEntity.getShowY()+1)%16,1);break;
                   case 3: blockEntity.setShowPos(((int)blockEntity.getShowY()-1)%16,1);break;
                   case 4: blockEntity.setShowPos(((int)blockEntity.getShowZ()+1)%16,2);break;
                   case 5: blockEntity.setShowPos(((int)blockEntity.getShowZ()-1)%16,2);break;
                   case 6: blockEntity.setShowPos((blockEntity.getShowYaw()+22.5)<360?(blockEntity.getShowYaw()+22.5):0,3);break;
                   case 7: blockEntity.setShowPos((blockEntity.getShowPitch()+22.5)<360?(blockEntity.getShowPitch()+22.5):0,4);break;
               }
                sendMessage(playerEntity, new TranslatableText(this.getTranslationKey() + ".switch",new Object[]{blockEntity.getShowX(),blockEntity.getShowY(),blockEntity.getShowZ(),blockEntity.getShowYaw(),blockEntity.getShowPitch()}));

            }
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

    @Override
    public void toTag(ItemStack itemStack){
        CompoundTag compoundTag = itemStack.getOrCreateTag();
        compoundTag.putInt("TransDirection",direction);

    }
    @Override
    public void fromTag(ItemStack itemStack){
        CompoundTag compoundTag = itemStack.getOrCreateTag();
        direction=compoundTag.getInt("TransDirection");
    }
    public int getDirection() {return direction;}
    private int changeIntDirection(){
        direction++;
        if(direction>7)direction=0;
        return direction;
    }

    private void sendMessageSwitchState(PlayerEntity player,ItemStack itemStack){
        toTag(itemStack);
        sendMessage(player, new TranslatableText(this.getTranslationKey() + "."+direction));
    }
    private static void sendMessage(PlayerEntity player, Text message) {
        ((ServerPlayerEntity)player).sendMessage(message, MessageType.GAME_INFO, Util.NIL_UUID);
    }
}
