package net.cocofish.yuushya.item;

import net.minecraft.block.BlockState;
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
import net.minecraft.world.World;

import static net.cocofish.yuushya.block.AbstractYuushyaBlock.FORM;

public class FormTransItem extends AbstractYuushyaItem{
    public FormTransItem(Settings settings, String registname,int linecount) {
        super(settings, registname,linecount);
    }
    private int form=0;
    private int maxform=3;
    private int minform=0;
    private int state=0;

    public ActionResult useOnBlock(ItemUsageContext context) {
        PlayerEntity playerEntity=context.getPlayer();
        Hand hand= context.getHand();
        World world=context.getWorld();

        BlockPos pos=context.getBlockPos();
        if (!world.isClient && playerEntity != null && state==0&&hand==Hand.MAIN_HAND&&state==0) {
            BlockState block=world.getBlockState(pos);
            if(block.getBlock().getStateManager().getProperty("form")!=null)
            {
                form=block.get(FORM);
                changeIntForm();
                world.setBlockState(pos,block.with(FORM,form),3);
                sendMessageSwitchState(playerEntity,hand,"form",form);
                return ActionResult.CONSUME;
            }
        }
        return ActionResult.PASS;
    }
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        if (!world.isClient && playerEntity != null) {
            fromTag(playerEntity.getStackInHand(hand));
            if (hand == Hand.OFF_HAND) {
                changeIntState();
                sendMessageSwitchState(playerEntity,hand,"state",state);
            }
            else if(hand==Hand.MAIN_HAND){
                if(state==1){
                    minform=changeIntFormRange(minform,0);
                    sendMessageSwitchState(playerEntity,hand,"min",minform);
                }else if(state==2){
                    maxform=changeIntFormRange(maxform,1);
                    sendMessageSwitchState(playerEntity,hand,"max",maxform);
                }

            }
            return TypedActionResult.success(playerEntity.getStackInHand(hand));
        }
        return TypedActionResult.pass(playerEntity.getStackInHand(hand));
    }

    private int changeIntForm(){
        form++;
        if(form>maxform)form=minform;
        return form;
    }
    private void changeIntState(){
        state++;
        if(state>2)state=0;
    }
    private int changeIntFormRange(int form,int mode){
        form++;
        if(mode==0){
            if(form>=maxform)
                return 0;
        }
        else {
            if(form>15)
                return minform+1>15?minform:minform+1;
        }
        return form;
    }

    private void sendMessageSwitchState(PlayerEntity player,Hand hand,String info,int num){
        toTag(player.getStackInHand(hand));
        sendMessage(player, new TranslatableText(this.getTranslationKey() +"."+info+ "."+num));
    }
    private static void sendMessage(PlayerEntity player, Text message) {
        ((ServerPlayerEntity)player).sendMessage(message, MessageType.GAME_INFO, Util.NIL_UUID);
    }
    @Override
    public void toTag(ItemStack itemStack){
        CompoundTag compoundTag=itemStack.getOrCreateTag();
        compoundTag.putInt("MaxForm",maxform);
        compoundTag.putInt("MinForm",minform);
        compoundTag.putInt("TransDirection",state);
    }
    @Override
    public void fromTag(ItemStack itemStack){
        CompoundTag compoundTag=itemStack.getOrCreateTag();
        maxform=compoundTag.getInt("MaxForm");
        minform=compoundTag.getInt("MinForm");
        state=compoundTag.getInt("TransDirection");
    }
}
