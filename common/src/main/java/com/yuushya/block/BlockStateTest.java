package com.yuushya.block;

import com.mojang.math.Vector3f;
import com.yuushya.utils.YuushyaUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.*;

import static com.yuushya.utils.YuushyaVoxelShape.getVoxelShape;
import static net.minecraft.world.level.block.BeehiveBlock.FACING;
import static net.minecraft.world.level.block.FrostedIceBlock.AGE;

public class BlockStateTest{
    BlockBehaviour.Properties properties;
    int a;
    public BlockStateTest(BlockBehaviour.Properties properties, Integer tipLines) {
        this.properties=properties;
        a=tipLines;



    }

    Map<BlockState,VoxelShape> YuushyaVoxelShapes =new HashMap<>();
    public Block create(){
        return new Block(properties){
            @Override
            protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> arg) {
                if (a==1)
                    arg.add(AGE);
                else
                    arg.add(FACING);
            }

            @Override
            public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
                BlockState blockState1=blockGetter.getBlockState(blockPos);
                if (!(blockState1.getBlock() instanceof AirBlock)) {
                    if (YuushyaVoxelShapes.get(blockState)==null){
                        YuushyaVoxelShapes.put(blockState,getVoxelShape(blockState));
                    }else {
                        return YuushyaVoxelShapes.get(blockState);
                    }
                }
                return Shapes.block();
            }
        };
    }
}
