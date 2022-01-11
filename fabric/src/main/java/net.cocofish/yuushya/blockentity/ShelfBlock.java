package net.cocofish.yuushya.blockentity;

import net.cocofish.yuushya.block.AbstractYuushyaBlock;
import net.cocofish.yuushya.block.PlatformBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.state.StateManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import static net.minecraft.block.WallMountedBlock.FACE;
import static net.minecraft.state.property.Properties.HORIZONTAL_FACING;
import static net.minecraft.state.property.Properties.POWERED;

public class ShelfBlock extends PlatformBlock implements BlockEntityProvider {
    public ShelfBlock(Settings settings, String registname, float ambientocclusionlightlevel,int linecount) {
        super(settings, registname, ambientocclusionlightlevel,linecount);
    }


    @Override
    public BlockEntity createBlockEntity(BlockPos blockPos,BlockState blockState) {
        return new ShelfBlockEntity(blockPos,blockState);
    }

    @Override
    public ActionResult onUse (BlockState blockState, World world, BlockPos blockPos, PlayerEntity player, Hand hand, BlockHitResult blockHitResult) {
        if (world.isClient) return ActionResult.SUCCESS;
        Inventory blockEntity = (Inventory) world.getBlockEntity(blockPos);
        ShelfBlockEntity blockEntity2 = (ShelfBlockEntity) world.getBlockEntity(blockPos);


        if (!player.getStackInHand(hand).isEmpty()) {
            // Check what is the first open slot and put an item from the player's hand there
            if (blockEntity.getStack(0).isEmpty()) {
                // Put the stack the player is holding into the inventory
                blockEntity.setStack(0, player.getStackInHand(hand).copy());
                // Remove the stack from the player's hand
                player.getStackInHand(hand).setCount(0);
            } else if (blockEntity.getStack(1).isEmpty()) {
                blockEntity.setStack(1, player.getStackInHand(hand).copy());
                player.getStackInHand(hand).setCount(0);
            } else {
                // If the inventory is full we'll print it's contents
                System.out.println("The first slot holds "
                        + blockEntity.getStack(0) + " and the second slot holds " + blockEntity.getStack(1));
            }



        }else{
            // If the player is not holding anything we'll get give him the items in the block entity one by one

            // Find the first slot that has an item and give it to the player
            if (!blockEntity.getStack(1).isEmpty()) {
                // Give the player the stack in the inventory
                player.getInventory().offerOrDrop( blockEntity.getStack(1));
                // Remove the stack from the inventory
                blockEntity.removeStack(1);
            } else if (!blockEntity.getStack(0).isEmpty()) {
                player.getInventory().offerOrDrop( blockEntity.getStack(0));
                blockEntity.removeStack(0);
            }
        }
        blockEntity.markDirty();
        //blockEntity2.sync();
        return ActionResult.SUCCESS;
    }
}




