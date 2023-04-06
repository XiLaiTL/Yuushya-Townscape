package com.yuushya.block;

import com.yuushya.block.blockstate.YuushyaBlockStates;
import com.yuushya.collision.CollisionFileReader;
import com.yuushya.registries.YuushyaRegistries;
import com.yuushya.registries.YuushyaRegistryData;
import com.yuushya.utils.YuushyaUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yuushya.utils.YuushyaUtils.toBlockMaterial;
import static com.yuushya.utils.YuushyaUtils.toSound;
import static com.yuushya.collision.utils.VoxelShapeFromFace.getVoxelShape;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class YuushyaBlockFactory{

    public static final Map<BlockState,VoxelShape> YuushyaVoxelShapes =new HashMap<>();

    public static class BlockWithClassType extends AbstractYuushyaBlock{
        public String classType;
        public boolean autoCollision;
        public BlockWithClassType(Properties properties, Integer tipLines, String classType, boolean autoCollision) {
            super(properties, tipLines);
            this.classType=classType;
            this.autoCollision=autoCollision;
        }
        public boolean isTheSameType(BlockWithClassType block){
            return classType.equals(block.classType);
        }
        @Override
        public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
            //if (autoCollision){}
            BlockState blockState1=blockGetter.getBlockState(blockPos);
            if (!(blockState1.getBlock() instanceof AirBlock)) {
                if (YuushyaVoxelShapes.get(blockState)==null){
                    System.out.println(Registry.BLOCK.getKey(blockState.getBlock()));
                    CollisionFileReader.readone(Registry.BLOCK.getKey(blockState.getBlock()).toString());
                    //YuushyaVoxelShapes.put(blockState,getVoxelShape(blockState));
                }
                return YuushyaVoxelShapes.get(blockState);
            }
            return Shapes.block();
        }
//        @Override
//        public VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
//            return this.hasCollision ? Shapes.block() : Shapes.empty();
//        }
    }

    public static BlockBehaviour.Properties getBlockProperties(YuushyaRegistryData.Block.Properties yuushyaBlockProperties){
        BlockBehaviour.Properties blockProperties = BlockBehaviour.Properties
                .of(toBlockMaterial(yuushyaBlockProperties.material))
                .strength(yuushyaBlockProperties.hardness,yuushyaBlockProperties.resistance);
        if ( yuushyaBlockProperties.sound!=null&&!yuushyaBlockProperties.sound.isEmpty()) blockProperties=blockProperties.sound(toSound(yuushyaBlockProperties.sound));
        if (yuushyaBlockProperties.lightLevel!=0) blockProperties=blockProperties.lightLevel((state)->yuushyaBlockProperties.lightLevel);
        if (!yuushyaBlockProperties.hasCollision) blockProperties=blockProperties.noCollission();
        if (yuushyaBlockProperties.isDelicate) blockProperties=blockProperties.instabreak();
        if (!yuushyaBlockProperties.isSolid) blockProperties=blockProperties.noOcclusion();
        return blockProperties;
    }
    private static List<? extends Property<?>> getBlockStateProperties(YuushyaRegistryData.Block.BlockState yuushyaBlockState){
        if (yuushyaBlockState==null||yuushyaBlockState.states==null) return List.of();
        return yuushyaBlockState.states.stream().map(YuushyaBlockStates::toBlockStateProperty).toList();
    }

    public static Block create(YuushyaRegistryData.Block yuushyaBlock){
        return create(getBlockProperties(yuushyaBlock.properties),yuushyaBlock);
    }
    public static Block create(BlockBehaviour.Properties properties,YuushyaRegistryData.Block yuushyaBlock){
        //套装，依然可以用在自定义的classType里
        if (yuushyaBlock.autoGenerated==null) {
            yuushyaBlock.autoGenerated=new YuushyaRegistryData.Block.AutoGenerated();
            yuushyaBlock.autoGenerated.collision=true;
        }
        if (yuushyaBlock.blockstate.kit !=null&&!yuushyaBlock.blockstate.kit.isEmpty()){
            switch (yuushyaBlock.blockstate.kit){
                case "normal"->{
                    return new NormalBlock(properties,yuushyaBlock.properties.lines, yuushyaBlock.classType, yuushyaBlock.autoGenerated.collision);}
                case "attachment" -> {
                    return new AttachmentBlock(properties,yuushyaBlock.properties.lines, yuushyaBlock.classType, yuushyaBlock.autoGenerated.collision);}
                case "line" -> {
                    return new LineBlock(properties,yuushyaBlock.properties.lines, yuushyaBlock.classType, yuushyaBlock.autoGenerated.collision);}
                case "face" -> {
                    return new FaceBlock(properties,yuushyaBlock.properties.lines, yuushyaBlock.classType, yuushyaBlock.autoGenerated.collision);}
                case "pole" -> {
                    return new PoleBlock(properties,yuushyaBlock.properties.lines, yuushyaBlock.classType, yuushyaBlock.autoGenerated.collision);}
                case "tri_part"->{
                    return new TriPartBlock(properties,yuushyaBlock.properties.lines,yuushyaBlock.classType, yuushyaBlock.autoGenerated.collision);}
                case "tube"->{
                    return new TubeBlock(properties,yuushyaBlock.properties.lines,yuushyaBlock.classType, yuushyaBlock.autoGenerated.collision);}
                case "block"->{}
                case "VanillaDoorBlock"->{
                    return new DoorBlock(properties){
                        @Override//注释栏数
                        public void appendHoverText(@NotNull ItemStack itemStack, @Nullable BlockGetter blockGetter, @NotNull List<Component> tooltips, @NotNull TooltipFlag tooltipFlag) {
                            for(int i=1;i<=yuushyaBlock.properties.lines;i++) tooltips.add(new TranslatableComponent(this.getDescriptionId()+".line"+i));
                        }};}
                case "VanillaStairBlock"->{
                    BlockState blockState= Blocks.OAK_PLANKS.defaultBlockState();
                    if (yuushyaBlock.properties.parent!=null){
                        //TODO:这里可能会崩
                        if (yuushyaBlock.properties.parent.contains(":"))
                            blockState= Registry.BLOCK.get(new ResourceLocation(yuushyaBlock.properties.parent)).defaultBlockState();
                        else
                            blockState= YuushyaRegistries.BLOCKS.get(yuushyaBlock.properties.parent).get().defaultBlockState();
                    }
                    return new StairBlock(blockState,properties){
                        @Override//注释栏数
                        public void appendHoverText(@NotNull ItemStack itemStack, @Nullable BlockGetter blockGetter, @NotNull List<Component> tooltips, @NotNull TooltipFlag tooltipFlag) {
                            for(int i=1;i<=yuushyaBlock.properties.lines;i++) tooltips.add(new TranslatableComponent(this.getDescriptionId()+".line"+i));
                        }};}
            }
        }
        List<? extends Property<?>> blockStateProperties=getBlockStateProperties(yuushyaBlock.blockstate);
        //classType 用于一些内定的方块//TODO:还是算了，直接让kit承担内定方块的重任
        switch (yuushyaBlock.classType){
            case "CableBlock"->{return new CableBlock(properties,yuushyaBlock.properties.lines,"CableBlock", yuushyaBlock.autoGenerated.collision);}
            case "" -> {return new Block(properties);}
        }
        return new BlockWithClassType(properties,yuushyaBlock.properties.lines, yuushyaBlock.classType, yuushyaBlock.autoGenerated.collision){
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
    public static boolean isTheSameLine(BlockState blockState1,BlockState blockState2){return blockState1.getValue(HORIZONTAL_FACING)==blockState2.getValue(HORIZONTAL_FACING)||blockState1.getValue(HORIZONTAL_FACING)==blockState2.getValue(HORIZONTAL_FACING).getOpposite();}
}