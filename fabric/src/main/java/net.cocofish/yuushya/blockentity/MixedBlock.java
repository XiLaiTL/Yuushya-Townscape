package net.cocofish.yuushya.blockentity;

import net.cocofish.yuushya.block.AbstractYuushyaBlock;
import net.cocofish.yuushya.block.ChairBlock;
import net.cocofish.yuushya.block.TableBlock;
import net.cocofish.yuushya.item.GetBlockStateItem;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.MessageType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import static net.minecraft.block.WallMountedBlock.FACE;
import static net.minecraft.state.property.Properties.*;

public class MixedBlock extends AbstractYuushyaBlock implements BlockEntityProvider {
    public MixedBlock(Settings settings, String registname, float ambientocclusionlightlevel, int linecount) {
        super(settings, registname, ambientocclusionlightlevel, linecount);
        this.setDefaultState(getDefaultState().with(POWERED,false).with(LIT,false));
    }
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(POWERED).add(LIT);
    }
    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new MixedBlockEntity(pos,state);
    }
    @Override
    public ActionResult onUse (BlockState blockState, World world, BlockPos blockPos, PlayerEntity player, Hand hand, BlockHitResult blockHitResult) {
        if (world.isClient) return ActionResult.SUCCESS;
        MixedBlockEntity blockEntity = (MixedBlockEntity) world.getBlockEntity(blockPos);


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
            return ActionResult.PASS;
//            if (!(blockEntity.getBlock(0).getBlock() instanceof AirBlock)) {
//                if(player.getStackInHand(hand).getItem() instanceof GetBlockStateItem)
//                    blockEntity.removeBlock(0);
//            }else {
//                return ActionResult.PASS;
//            }
        }
        blockEntity.markDirty();
        blockEntity.sync();
        return ActionResult.SUCCESS;
    }
    private static void sendMessage(PlayerEntity player, Text message) {
        ((ServerPlayerEntity)player).sendMessage(message, MessageType.GAME_INFO, Util.NIL_UUID);
    }
    public BlockState getStateForNeighborUpdate(BlockState stateIn, Direction facing, BlockState facingState, WorldAccess worldIn, BlockPos currentPos, BlockPos facingPos) {
        MixedBlockEntity blockEntity =(MixedBlockEntity) worldIn.getBlockEntity(currentPos);
        BlockState blockState=blockEntity.getBasicBlock();
        Block block=blockState.getBlock();

        if(facingState.getBlock() instanceof  MixedBlock)
            return stateIn;
        if(!(block instanceof AirBlock)) {
            blockEntity.setBasicBlock(block.getStateForNeighborUpdate(blockState, facing, facingState, worldIn, currentPos, facingPos));
            return stateIn.with(POWERED,!stateIn.get(POWERED));
        }
            return stateIn.with(POWERED,!stateIn.get(POWERED));
    }


}
