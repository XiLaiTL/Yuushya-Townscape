package com.yuushya.block;

import com.yuushya.block.blockstate.YuushyaBlockStates;
import com.yuushya.registries.YuushyaRegistryData;
import com.yuushya.utils.YuushyaUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yuushya.utils.YuushyaUtils.toBlockMaterial;
import static com.yuushya.utils.YuushyaUtils.toSound;
import static com.yuushya.utils.YuushyaVoxelShape.getVoxelShape;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

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
                .of(toBlockMaterial(yuushyaBlockProperties.material))
                .strength(yuushyaBlockProperties.hardness,yuushyaBlockProperties.resistance);
        if ( yuushyaBlockProperties.sound!=null&&!yuushyaBlockProperties.sound.isEmpty()) blockProperties=blockProperties.sound(toSound(yuushyaBlockProperties.sound));
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
        //套装，依然可以用在自定义的classType里
        if (yuushyaBlock.blockstate.suit!=null&&!yuushyaBlock.blockstate.suit.isEmpty()){
            switch (yuushyaBlock.blockstate.suit){
                case "normal" -> {
                    return new NormalBlock(getBlockProperties(yuushyaBlock.properties),yuushyaBlock.properties.lines, yuushyaBlock.classType);}
                case "line" -> {
                    return new LineBlock(getBlockProperties(yuushyaBlock.properties),yuushyaBlock.properties.lines, yuushyaBlock.classType);}
                case "face" -> {
                    return new FaceBlock(getBlockProperties(yuushyaBlock.properties),yuushyaBlock.properties.lines, yuushyaBlock.classType);}
                case "pole" -> {
                    return new PoleBlock(getBlockProperties(yuushyaBlock.properties),yuushyaBlock.properties.lines, yuushyaBlock.classType);}
            }
        }
        List<? extends Property<?>> blockStateProperties=getBlockStateProperties(yuushyaBlock.blockstate);
        //classType 用于一些内定的方块
        if (yuushyaBlock.classType.equals("")){
            return new Block(getBlockProperties(yuushyaBlock.properties));
        }
        return new BlockWithClassType(getBlockProperties(yuushyaBlock.properties),yuushyaBlock.properties.lines, yuushyaBlock.classType){
            {
                this.registerDefaultState(YuushyaBlockStates.getDefaultBlockState(this.stateDefinition.any()));
            }
            @Override
            protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
                stateBuilder.add(blockStateProperties.toArray(Property[]::new));
            }

            @Override
            @Nullable
            public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {

                BlockState res=this.defaultBlockState();
                //from FaceAttachedHorizontalDirectionalBlock
                if (blockStateProperties.contains(BlockStateProperties.ATTACH_FACE)&&blockStateProperties.contains(BlockStateProperties.HORIZONTAL_FACING)){
                    Direction direction = blockPlaceContext.getNearestLookingDirection();
                    res= direction.getAxis() == Direction.Axis.Y
                            ? res
                            .setValue(BlockStateProperties.ATTACH_FACE, direction == Direction.UP ? AttachFace.CEILING : AttachFace.FLOOR)
                            .setValue(BlockStateProperties.HORIZONTAL_FACING, blockPlaceContext.getHorizontalDirection())
                            : res
                            .setValue(BlockStateProperties.ATTACH_FACE, AttachFace.WALL)
                            .setValue(BlockStateProperties.HORIZONTAL_FACING, direction.getOpposite());
                }

                return res;
            }

            @Override
            public BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState2, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos2) {

                return blockState;
            }

            @Override
            public OffsetType getOffsetType() {
                return YuushyaUtils.toOffsetType(yuushyaBlock.properties.offset);
            }
        };
    }
    public static boolean isTheSameBlock(BlockState state1, BlockState state2) {return state2.getBlock()==state1.getBlock(); }
    public static boolean isTheSameFacing(BlockState blockState1,BlockState blockState2){return blockState1.getValue(HORIZONTAL_FACING)==blockState2.getValue(HORIZONTAL_FACING);}
}
