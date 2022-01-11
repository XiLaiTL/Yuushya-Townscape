package net.cocofish.yuushya.item;

import net.cocofish.yuushya.blockentity.AbstractShowFrameBlock;
import net.cocofish.yuushya.blockentity.MixedBlock;
import net.cocofish.yuushya.blockentity.ShowBlock;
import net.minecraft.block.Block;
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

public class MicroPosTransItem extends AbstractYuushyaItem{
    public MicroPosTransItem(Settings settings, int linecount) {
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
                   case 0: blockEntity.setShowPos((blockEntity.getShowX()+0.001),0);break;
                   case 1: blockEntity.setShowPos((blockEntity.getShowX()-0.001),0);break;
                   case 2: blockEntity.setShowPos((blockEntity.getShowY()+0.001),1);break;
                   case 3: blockEntity.setShowPos((blockEntity.getShowY()-0.001),1);break;
                   case 4: blockEntity.setShowPos((blockEntity.getShowZ()+0.001),2);break;
                   case 5: blockEntity.setShowPos((blockEntity.getShowZ()-0.001),2);break;
                   case 6: blockEntity.setShowPos(Math.floor(blockEntity.getShowX())%16,Math.floor(blockEntity.getShowY())%16,Math.floor(blockEntity.getShowZ())%16,blockEntity.getShowRotationX(),blockEntity.getShowRotationY(),blockEntity.getShowRotationZ());
                            blockEntity.markDirty();blockEntity.sync();break;
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
        if(direction>6)direction=0;
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
