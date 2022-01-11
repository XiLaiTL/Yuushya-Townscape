package net.cocofish.yuushya.item;

import net.cocofish.yuushya.Yuushya;
import net.cocofish.yuushya.blockentity.MixedBlock;
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

public class GetShowBlockEntityItem extends AbstractYuushyaItem {
    public GetShowBlockEntityItem(Settings settings, int linecount) {
        super(settings, linecount);
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

        if(block instanceof ShowBlock||block instanceof MixedBlock) {
            return ActionResult.PASS;
        }else {
            world.setBlockState(pos, Yuushya.showblock.getDefaultState(),35);
            ShowBlockEntity blockEntity=(ShowBlockEntity)world.getBlockEntity(pos);
            blockEntity.setBlock(blockState);
            return ActionResult.SUCCESS;
        }
        //return ActionResult.PASS;
    }

}

