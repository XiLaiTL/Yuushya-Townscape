package net.cocofish.yuushya.blockentity;

import net.cocofish.yuushya.block.AbstractYuushyaBlock;
import net.cocofish.yuushya.block.PlatformBlock;
import net.cocofish.yuushya.item.PosTransItem;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.MessageType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import static net.minecraft.block.WallMountedBlock.FACE;
import static net.minecraft.state.property.Properties.HORIZONTAL_FACING;
import static net.minecraft.state.property.Properties.POWERED;

public class ShowBlock extends AbstractYuushyaBlock implements BlockEntityProvider {
    public ShowBlock(Settings settings, String registname, float ambientocclusionlightlevel, int linecount) {
        super(settings, registname, ambientocclusionlightlevel,linecount);
    }

    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new ShowBlockEntity();
    }

    @Override
    public ActionResult onUse (BlockState blockState, World world, BlockPos blockPos, PlayerEntity player, Hand hand, BlockHitResult blockHitResult) {
        if (world.isClient) return ActionResult.SUCCESS;
        ShowBlockEntity blockEntity = (ShowBlockEntity) world.getBlockEntity(blockPos);


        if (!player.getStackInHand(hand).isEmpty()) {
            if (blockEntity.getBlock(0).getBlock() instanceof AirBlock) {
                ItemStack itemStack = player.getStackInHand(hand).copy();
                if (!itemStack.isEmpty()&&itemStack.getItem() instanceof BlockItem) {
                    BlockState blockState1=((BlockItem) itemStack.getItem()).getBlock().getDefaultState();
                    blockEntity.setBlock(0, blockState1);
                }
                else {
                    return ActionResult.PASS;

                }

            }
            else {
                return ActionResult.PASS;
            }
        }else{
            if (!(blockEntity.getBlock(0).getBlock() instanceof AirBlock)) {
                blockEntity.removeBlock(0);
            }else {
                return ActionResult.PASS;
            }
        }
        blockEntity.markDirty();
        blockEntity.sync();
        return ActionResult.SUCCESS;
    }
    private static void sendMessage(PlayerEntity player, Text message) {
        ((ServerPlayerEntity)player).sendMessage(message, MessageType.GAME_INFO, Util.NIL_UUID);
    }
}




