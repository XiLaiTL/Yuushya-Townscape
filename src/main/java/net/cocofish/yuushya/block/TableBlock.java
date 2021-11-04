package net.cocofish.yuushya.block;

import net.cocofish.yuushya.YuushyaMethod;
import net.cocofish.yuushya.blockstate_enum.PositionDirectionState;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;

public class TableBlock extends AbstractYuushyaBlock {
    public static final EnumProperty<PositionDirectionState> XPOS = EnumProperty.of("xpos",PositionDirectionState.class);
    public static final EnumProperty<PositionDirectionState> ZPOS = EnumProperty.of("zpos",PositionDirectionState.class);
    public TableBlock(Settings settings,String registname,float ambientocclusionlightlevel,int linecount) {
        super(settings,registname,ambientocclusionlightlevel,linecount);
    }
    private YuushyaMethod YM=new YuushyaMethod();
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return VoxelShapes.cuboid(0f, 0f, 0f, 1f, 1.0f, 1f);
    }
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(XPOS).add(ZPOS);
    }
    public BlockState getPlacementState(ItemPlacementContext context) {
        return this.getDefaultState().with(XPOS,getPosition(this.getDefaultState(),context.getWorld(),context.getBlockPos(),'X')).with(ZPOS,getPosition(this.getDefaultState(),context.getWorld(),context.getBlockPos(),'Z'));
    }
    public PositionDirectionState getPosition(BlockState state, BlockView worldIn, BlockPos pos,char XorZ)
    {
        BlockState nblockstate=YM.getBlockState(worldIn.getBlockState(pos.north()),(WorldAccess) worldIn,pos.north());
        BlockState sblockstate=YM.getBlockState(worldIn.getBlockState(pos.south()),(WorldAccess) worldIn,pos.south());
        BlockState wblockstate=YM.getBlockState(worldIn.getBlockState(pos.west()),(WorldAccess) worldIn,pos.west());
        BlockState eblockstate=YM.getBlockState(worldIn.getBlockState(pos.east()),(WorldAccess) worldIn,pos.east());
        switch (XorZ)
        {
            case 'X':
                if(isTableBlock(wblockstate) && isDifferentTable(wblockstate,state))
                {
                    if(isTableBlock(eblockstate) && isDifferentTable(eblockstate,state))
                        return PositionDirectionState.MIDDLE;
                    else
                        return PositionDirectionState.EAST;
                }
                else if(isTableBlock(eblockstate) && isDifferentTable(eblockstate,state))
                {
                    return PositionDirectionState.WEST;
                }
                else
                {
                    return PositionDirectionState.NONE;
                }
            case 'Z':
                if(isTableBlock(nblockstate) && isDifferentTable(nblockstate,state))
                {
                    if(isTableBlock(sblockstate) && isDifferentTable(sblockstate,state))
                        return PositionDirectionState.MIDDLE;
                    else
                        return PositionDirectionState.SOUTH;
                }
                else if(isTableBlock(sblockstate) && isDifferentTable(sblockstate,state))
                {
                    return PositionDirectionState.NORTH;
                }
                else
                {
                    return PositionDirectionState.NONE;
                }
            default:return PositionDirectionState.NONE;
        }
    }
    private static boolean isDifferentTable(BlockState state1, BlockState state2) {return state2.getBlock()==state1.getBlock(); }

    public static boolean isTableBlock(BlockState state) {
        return state.getBlock() instanceof TableBlock;
    }

    public BlockState getStateForNeighborUpdate(BlockState stateIn, Direction facing, BlockState facingState, WorldAccess worldIn, BlockPos currentPos, BlockPos facingPos) {
        return stateIn.with(XPOS, getPosition(stateIn, worldIn, currentPos,'X')).with(ZPOS,getPosition(stateIn, worldIn, currentPos,'Z'));
    }
}
