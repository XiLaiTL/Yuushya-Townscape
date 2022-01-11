package net.cocofish.yuushya.item;

import net.cocofish.yuushya.Yuushya;
import net.cocofish.yuushya.blockentity.MixedBlock;
import net.cocofish.yuushya.blockentity.MixedBlockEntity;
import net.cocofish.yuushya.blockentity.ShowBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.state.property.Properties;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GetLitItem extends AbstractYuushyaItem {

    public GetLitItem(Settings settings, int linecount) {
        super(settings, linecount);
    }


    public ActionResult useOnBlock(ItemUsageContext context) {
//        PlayerEntity playerEntity=context.getPlayer();
//        Hand hand= context.getHand();
        World world=context.getWorld();
        BlockPos pos=context.getBlockPos();
        BlockState blockState=world.getBlockState(pos);
        Block block=blockState.getBlock();
        PlayerEntity player = context.getPlayer();

        if(blockState.get(Properties.LIT)!=null) {
            world.setBlockState(pos, blockState.with(Properties.LIT, !blockState.get(Properties.LIT)));
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

}

