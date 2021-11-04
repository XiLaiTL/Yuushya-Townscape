package net.cocofish.yuushya.item;

import net.cocofish.yuushya.Yuushya;
import net.cocofish.yuushya.blockentity.MixedBlock;
import net.cocofish.yuushya.blockentity.MixedBlockEntity;
import net.cocofish.yuushya.blockentity.ShowBlock;
import net.cocofish.yuushya.blockentity.ShowBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import static net.minecraft.state.property.Properties.POWERED;
public class GetMixedBlockEntityItem extends AbstractYuushyaItem {
    public GetMixedBlockEntityItem(Settings settings, String registname, int linecount) {
        super(settings, registname, linecount);
    }


    public ActionResult useOnBlock(ItemUsageContext context) {
        PlayerEntity playerEntity=context.getPlayer();
        Hand hand= context.getHand();
        World world=context.getWorld();
        BlockPos pos=context.getBlockPos();
        BlockState blockState=world.getBlockState(pos);
        Block block=blockState.getBlock();

//        ItemStack mainhand= playerEntity.getMainHandStack();
//        ItemStack offhand=playerEntity.getOffHandStack();
//        Item item = offhand.getItem();

        if(block instanceof MixedBlock||block instanceof ShowBlock) {
            return ActionResult.PASS;
        }else {
            world.setBlockState(pos, Yuushya.mixedblock.getDefaultState(),35);
            MixedBlockEntity blockEntity=(MixedBlockEntity)world.getBlockEntity(pos);
            blockEntity.setBasicBlock(blockState);
            return ActionResult.SUCCESS;
        }
        //return ActionResult.PASS;
    }

}

