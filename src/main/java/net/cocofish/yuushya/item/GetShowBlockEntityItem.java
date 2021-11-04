package net.cocofish.yuushya.item;

import net.cocofish.yuushya.Yuushya;
import net.cocofish.yuushya.YuushyaElements;
import net.cocofish.yuushya.YuushyaItem;
import net.cocofish.yuushya.blockentity.MixedBlock;
import net.cocofish.yuushya.blockentity.ShowBlock;
import net.cocofish.yuushya.blockentity.ShowBlockEntity;
import net.cocofish.yuushya.entity.ExhibitionEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.MessageType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Property;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public class GetShowBlockEntityItem extends AbstractYuushyaItem {
    public GetShowBlockEntityItem(Settings settings, String registname, int linecount) {
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

