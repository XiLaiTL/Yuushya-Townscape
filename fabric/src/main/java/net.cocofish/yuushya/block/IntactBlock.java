package net.cocofish.yuushya.block;

import com.mojang.datafixers.types.templates.Tag;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.block.enums.WallMountLocation;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import static net.minecraft.block.WallMountedBlock.*;
import static net.minecraft.block.enums.WallMountLocation.*;
import static net.minecraft.state.property.Properties.*;

public class IntactBlock extends AbstractYuushyaBlock {
    public IntactBlock(Settings settings, String registname, float ambientocclusionlightlevel,int linecount) {
        super(settings,registname,ambientocclusionlightlevel,linecount);
        setDefaultState(getDefaultState().with(POWERED,false));
    }
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return VoxelShapes.cuboid(0f, 0f, 0f, 1f, 1.0f, 1f);
    }
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(POWERED).add(HORIZONTAL_FACING).add(FACE);
    }
    public BlockState getPlacementState(ItemPlacementContext context) {
        //return (BlockState)this.getDefaultState().with(HORIZONTAL_FACING, ctx.getSide());
        Direction direction=context.getSide();
        BlockState blockstate = context.getWorld().getBlockState(context.getBlockPos().offset(direction.getOpposite()));
        if(direction==Direction.DOWN||direction==Direction.UP)
        {direction=context.getPlayerFacing().getOpposite();}

        else if(blockstate.isOf(this) && blockstate.get(HORIZONTAL_FACING) == direction && blockstate.get(FACE)!=WALL)
        {direction=direction.getOpposite();}

        if(context.getSide()==Direction.UP)
        {
            return this.getDefaultState().with(HORIZONTAL_FACING, direction).with(FACE,FLOOR);
        }
        else if(context.getSide()==Direction.DOWN)
        {
            return this.getDefaultState().with(HORIZONTAL_FACING, direction).with(FACE,CEILING);
        }
        else
        {
            return this.getDefaultState().with(HORIZONTAL_FACING, direction).with(FACE,WALL);
        }
    }
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return (BlockState)state.with(HORIZONTAL_FACING, rotation.rotate((Direction)state.get(HORIZONTAL_FACING)));
    }
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation((Direction)state.get(HORIZONTAL_FACING)));
    }
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getStackInHand(hand);
        //Item item = itemStack.getItem();
        //Block block = ((item instanceof BlockItem)) ? ((BlockItem)item).getBlock(): Blocks.AIR;

        if(itemStack.isEmpty()&&hand==Hand.MAIN_HAND){
            if(world.isClient) return ActionResult.CONSUME;
            if(state.get(POWERED)==false) {
                world.setBlockState(pos, (BlockState)state.with(POWERED, true), 3);
            }
            else {
                world.setBlockState(pos, (BlockState)state.with(POWERED, false), 3);
            }
            return ActionResult.CONSUME;
        }
        else {
            return ActionResult.PASS;
        }
    }
}
