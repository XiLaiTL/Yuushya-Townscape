package com.yuushya.block;

import com.yuushya.block.blockstate.YuushyaBlockStates;
import com.yuushya.registries.YuushyaRegistryData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yuushya.utils.YuushyaUtils.getBlockMaterial;
import static com.yuushya.utils.YuushyaUtils.getSound;
import static com.yuushya.utils.YuushyaVoxelShape.getVoxelShape;

public class YuushyaBlockFactory{

    private static final Map<BlockState,VoxelShape> YuushyaVoxelShapes =new HashMap<>();

    public static class BlockWithClassType extends AbstractYuushyaBlock{
        public String classType;
        public BlockWithClassType(Properties properties, Integer tipLines, String classType) {
            super(properties, tipLines);
            this.classType=classType;
        }
        public boolean isTheSameType(BlockWithClassType block){
            return classType.equals(block.classType);
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
        @Override
        public VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
            return this.hasCollision ? Shapes.block() : Shapes.empty();
        }
    }

    private static BlockBehaviour.Properties getBlockProperties(YuushyaRegistryData.Block.Properties yuushyaBlockProperties){
        BlockBehaviour.Properties blockProperties = BlockBehaviour.Properties
                .of(getBlockMaterial(yuushyaBlockProperties.material))
                .strength(yuushyaBlockProperties.hardness,yuushyaBlockProperties.resistance);
        if ( yuushyaBlockProperties.sound!=null&&!yuushyaBlockProperties.sound.isEmpty()) blockProperties=blockProperties.sound(getSound(yuushyaBlockProperties.sound));
        if (yuushyaBlockProperties.lightLevel!=0) blockProperties=blockProperties.lightLevel((state)->yuushyaBlockProperties.lightLevel);
        if (!yuushyaBlockProperties.hasCollision) blockProperties=blockProperties.noCollission();
        if (yuushyaBlockProperties.isDelicate) blockProperties=blockProperties.instabreak();
        if (yuushyaBlockProperties.isSolid) blockProperties=blockProperties.noOcclusion();
        return blockProperties;
    }
    private static List<? extends Property<?>> getBlockStateProperties(YuushyaRegistryData.Block.BlockState yuushyaBlockState){
        return yuushyaBlockState.states.stream().map(YuushyaBlockStates::toBlockStateProperty).toList();
    }

    public static Block create(YuushyaRegistryData.Block yuushyaBlock){
        return new BlockWithClassType(getBlockProperties(yuushyaBlock.properties),yuushyaBlock.properties.lines, yuushyaBlock.classType){
            @Override
            protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
                stateBuilder.add(getBlockStateProperties(yuushyaBlock.blockstate).toArray(Property[]::new));
            }
        };
    }
}
