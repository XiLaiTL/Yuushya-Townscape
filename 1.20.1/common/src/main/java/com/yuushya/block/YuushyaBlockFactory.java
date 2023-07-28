package com.yuushya.block;

import com.yuushya.block.blockstate.YuushyaBlockStates;
import com.yuushya.collision.CollisionFileReader;
import com.yuushya.entity.ChairEntityUtils;
import com.yuushya.registries.YuushyaRegistries;
import com.yuushya.registries.YuushyaRegistryData;
import com.yuushya.utils.YuushyaUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yuushya.utils.YuushyaUtils.toSound;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class YuushyaBlockFactory{

    private static final Map<Integer,VoxelShape> yuushyaVoxelShapes =new HashMap<>();
    public static Map<Integer, VoxelShape> getYuushyaVoxelShapes() {
        return yuushyaVoxelShapes;
    }

    public static class BlockWithClassType extends AbstractYuushyaBlock{
        public String classType;
        public String autoCollision;
        public YuushyaRegistryData.Block.Usage usage;
        private final Map<BlockState,VoxelShape> voxelShapeCache = new HashMap<>();
        public BlockWithClassType(Properties properties, Integer tipLines, String classType, String autoCollision, YuushyaRegistryData.Block.Usage usage) {
            super(properties, tipLines);
            this.classType=classType;
            this.autoCollision=autoCollision;
            this.usage=usage;
        }

        @Override
        public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
            if(usage!=null){
                if(usage.sitPos!=null&&usage.sitPos.size()==3&&player.getItemInHand(hand).isEmpty()){
                    return ChairEntityUtils.use(new Vec3(usage.sitPos.get(0),usage.sitPos.get(1),usage.sitPos.get(2)) ,state,level,pos,player,hand,hit);
                }
            }
            return super.use(state,level,pos,player,hand,hit);

        }

        public boolean isTheSameType(BlockWithClassType block){
            return classType.equals(block.classType);
        }

        @Override
        public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
            if(!voxelShapeCache.containsKey(blockState)){
                if(!getYuushyaVoxelShapes().containsKey(Block.getId(blockState))){
                    CollisionFileReader.readCollisionToVoxelShape(voxelShapeCache,blockState.getBlock(), BuiltInRegistries.BLOCK.getKey(blockState.getBlock()).toString());
                }
                VoxelShape shape = getYuushyaVoxelShapes().getOrDefault(Block.getId(blockState),Shapes.empty());
                voxelShapeCache.put(blockState,shape);
                return shape;
            }
            return voxelShapeCache.get(blockState);
        }

//        @Override
//        public VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
//            return Shapes.create(this.getCollisionShape(state,level,pos,context).bounds());
//        }

//        @Override
//        public VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
//            return this.hasCollision ? Shapes.block() : Shapes.empty();
//        }
    }

    public static BlockBehaviour.Properties getBlockProperties(BlockBehaviour.Properties blockProperties, YuushyaRegistryData.Block.Properties yuushyaBlockProperties){
        blockProperties = blockProperties.strength(yuushyaBlockProperties.hardness,yuushyaBlockProperties.resistance);
        if ( yuushyaBlockProperties.sound!=null&&!yuushyaBlockProperties.sound.isEmpty()) blockProperties=blockProperties.sound(toSound(yuushyaBlockProperties.sound));
        if (yuushyaBlockProperties.lightLevel!=0) blockProperties=blockProperties.lightLevel((state)->yuushyaBlockProperties.lightLevel);
        if (!yuushyaBlockProperties.hasCollision) blockProperties=blockProperties.noCollission();
        if (yuushyaBlockProperties.isDelicate) blockProperties=blockProperties.instabreak();
        if (!yuushyaBlockProperties.isSolid) blockProperties=blockProperties.noOcclusion();
        if(yuushyaBlockProperties.offset!=null) blockProperties=blockProperties.offsetType(YuushyaUtils.toOffsetType(yuushyaBlockProperties.offset));
        return blockProperties;
    }

    public static BlockBehaviour.Properties getBlockProperties(YuushyaRegistryData.Block.Properties yuushyaBlockProperties){
        if(yuushyaBlockProperties == null) return BlockBehaviour.Properties.of();
        BlockBehaviour.Properties blockProperties = BlockBehaviour.Properties
                .of().sound(toSound(yuushyaBlockProperties.material));
        return getBlockProperties(blockProperties,yuushyaBlockProperties);
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
            yuushyaBlock.autoGenerated.collision="block";
        }
        if (yuushyaBlock.blockstate.kit !=null&&!yuushyaBlock.blockstate.kit.isEmpty()){
            switch (yuushyaBlock.blockstate.kit){
                case "normal"->{
                    return new NormalBlock(properties,yuushyaBlock.properties.lines, yuushyaBlock.classType, yuushyaBlock.autoGenerated.collision, yuushyaBlock.usage);}
                case "attachment" -> {
                    return new AttachmentBlock(properties,yuushyaBlock.properties.lines, yuushyaBlock.classType, yuushyaBlock.autoGenerated.collision, yuushyaBlock.usage);}
                case "line" -> {
                    return new LineBlock(properties,yuushyaBlock.properties.lines, yuushyaBlock.classType, yuushyaBlock.autoGenerated.collision, yuushyaBlock.usage);}
                case "face" -> {
                    return new FaceBlock(properties,yuushyaBlock.properties.lines, yuushyaBlock.classType, yuushyaBlock.autoGenerated.collision, yuushyaBlock.usage);}
                case "pole" -> {
                    return new PoleBlock(properties,yuushyaBlock.properties.lines, yuushyaBlock.classType, yuushyaBlock.autoGenerated.collision, yuushyaBlock.usage);}
                case "tri_part"->{
                    return new TriPartBlock(properties,yuushyaBlock.properties.lines,yuushyaBlock.classType, yuushyaBlock.autoGenerated.collision, yuushyaBlock.usage);}
                case "tube"->{
                    return new TubeBlock(properties,yuushyaBlock.properties.lines,yuushyaBlock.classType, yuushyaBlock.autoGenerated.collision, yuushyaBlock.usage);}
                case "block"->{}
                case "VanillaDoorBlock"->{
                    return new DoorBlock(properties, YuushyaUtils.toBlockSetType(yuushyaBlock.properties)){
                        @Override//注释栏数
                        public void appendHoverText(@NotNull ItemStack itemStack, @Nullable BlockGetter blockGetter, @NotNull List<Component> tooltips, @NotNull TooltipFlag tooltipFlag) {
                            for(int i=1;i<=yuushyaBlock.properties.lines;i++) tooltips.add(Component.translatable(this.getDescriptionId()+".line"+i));
                        }};}
                case "VanillaStairBlock"->{
                    BlockState blockState= Blocks.OAK_PLANKS.defaultBlockState();
                    if (yuushyaBlock.properties.parent!=null){
                        //TODO:这里可能会崩
                        if (yuushyaBlock.properties.parent.contains(":"))
                            blockState= BuiltInRegistries.BLOCK.get(new ResourceLocation(yuushyaBlock.properties.parent)).defaultBlockState();
                        else
                            blockState= YuushyaRegistries.BLOCKS.get(yuushyaBlock.properties.parent).get().defaultBlockState();
                    }
                    return new StairBlock(blockState,properties){
                        @Override//注释栏数
                        public void appendHoverText(@NotNull ItemStack itemStack, @Nullable BlockGetter blockGetter, @NotNull List<Component> tooltips, @NotNull TooltipFlag tooltipFlag) {
                            for(int i=1;i<=yuushyaBlock.properties.lines;i++) tooltips.add(Component.translatable(this.getDescriptionId()+".line"+i));
                        }};
                }
            }
        }
        List<? extends Property<?>> blockStateProperties=getBlockStateProperties(yuushyaBlock.blockstate);
        //classType 用于一些内定的方块//TODO:还是算了，直接让kit承担内定方块的重任
        switch (yuushyaBlock.classType){
            case "CableBlock"->{return new CableBlock(properties,yuushyaBlock.properties.lines,"CableBlock", yuushyaBlock.autoGenerated.collision, yuushyaBlock.usage);}
            case "" -> {return new Block(properties);}
        }
        return new BlockWithClassType(properties,yuushyaBlock.properties.lines, yuushyaBlock.classType, yuushyaBlock.autoGenerated.collision, yuushyaBlock.usage){
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

        };
    }
    public static boolean isTheSameBlock(BlockState state1, BlockState state2) {return state2.getBlock()==state1.getBlock(); }
    public static boolean isTheSameFacing(BlockState blockState1,BlockState blockState2){return blockState1.getValue(HORIZONTAL_FACING)==blockState2.getValue(HORIZONTAL_FACING);}
    public static boolean isTheSameLine(BlockState blockState1,BlockState blockState2){return blockState1.getValue(HORIZONTAL_FACING)==blockState2.getValue(HORIZONTAL_FACING)||blockState1.getValue(HORIZONTAL_FACING)==blockState2.getValue(HORIZONTAL_FACING).getOpposite();}
}
