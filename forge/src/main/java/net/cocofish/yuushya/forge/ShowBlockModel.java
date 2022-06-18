package net.cocofish.yuushya.forge;


import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector4f;
import net.cocofish.yuushya.blockentity.TransformData;
import net.cocofish.yuushya.blockentity.showblock.ShowBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.extensions.IForgeBakedModel;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import static net.cocofish.yuushya.utils.YuushyaUtils.*;

public class ShowBlockModel implements IForgeBakedModel, BakedModel {
    public static ModelProperty<ShowBlockEntity> BASE_BLOCK_ENTITY = new ModelProperty<>();
    public ShowBlockModel() {}

    @NotNull
    @Override
    public IModelData getModelData(@NotNull BlockAndTintGetter level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull IModelData modelData) {
//        ShowBlockEntity showBlockEntity = (ShowBlockEntity) level.getBlockEntity(pos);
//        BlockState blockState =showBlockEntity.getTransformDatas().get(showBlockEntity.slot).blockState;
//        ModelDataMap modelDataMap=new ModelDataMap.Builder().withInitial(BASE_BLOCK,null).build();
//        if(blockState.getBlock() instanceof AirBlock){return modelDataMap;}
//        modelDataMap.setData(BASE_BLOCK,blockState);
//        return modelData;
        if (level.getBlockEntity(pos) == null) {
            return new ModelDataMap.Builder().build();
        } else {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof ShowBlockEntity blockEntity1) {
                return new ModelDataMap.Builder().withInitial(BASE_BLOCK_ENTITY, blockEntity1).build();
            } else {
                return new ModelDataMap.Builder().build();
            }
        }
    }




    @NotNull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull Random rand, @NotNull IModelData extraData) {
//        BakedModel bakedModel;
//        ShowBlockEntity blockEntity=extraData.getData(BASE_BLOCK_ENTITY);
//        if (blockEntity!=null){
//            BlockRenderDispatcher blockRenderDispatcher =Minecraft.getInstance().getBlockRenderer();
//            bakedModel= blockRenderDispatcher.getBlockModel(blockEntity.getTransFormDataNow().blockState);
//            return bakedModel.getQuads(state,side,rand,extraData);
//        }
//        else {
//            return Collections.emptyList();
//        }

//        if (extraData.hasProperty(BASE_BLOCK)){
//            BlockState blockState=extraData.getData(BASE_BLOCK);
//            if(blockState!=null){
//                BlockRenderDispatcher blockRenderDispatcher =Minecraft.getInstance().getBlockRenderer();
//                 bakedModel= blockRenderDispatcher.getBlockModel(blockState);
//            }
//        }


        ShowBlockEntity blockEntity=extraData.getData(BASE_BLOCK_ENTITY);

        PoseStack stack = new PoseStack();
        // 一个顶点用多少位int表示，原版和开了光影的OptiFine不同所以得在这算出来

        int vertexSize = DefaultVertexFormat.BLOCK.getVertexSize() / 4;
        if (blockEntity != null) {
            BlockRenderDispatcher blockRenderDispatcher =Minecraft.getInstance().getBlockRenderer();
            List<BakedQuad> finalQuads = new ArrayList<>();
            if (side != null) {
                return Collections.emptyList();
            } else {

                ArrayList<Direction> directions = new ArrayList<>(Arrays.asList(Direction.values()));
                directions.add(null); // 加个null
                for (Direction value : directions) {
                    for(TransformData transformData:blockEntity.getTransformDatas()){
                        BlockState core = transformData.blockState;
                        BakedModel coreModel = blockRenderDispatcher.getBlockModel(core);
                        List<BakedQuad> coreQuads = coreModel.getQuads(core, value, rand);
                        for (BakedQuad coreQuad : coreQuads) {
                            int[] vertex = coreQuad.getVertices().clone();
                            // 执行核心方块的位移和旋转
                            stack.pushPose();{
                                scale(stack, transformData.scales);
                                translate(stack,transformData.pos);
                                rotate(stack,transformData.rot);
                                for (int i = 0; i < 4; i++) {
                                    // 顶点的原坐标
                                    Vector4f vector4f = new Vector4f(Float.intBitsToFloat(vertex[vertexSize*i]), Float.intBitsToFloat(vertex[vertexSize*i+1]), Float.intBitsToFloat(vertex[vertexSize*i+2]), 1);
                                    vector4f.transform(stack.last().pose());
                                    vertex[vertexSize*i] = Float.floatToRawIntBits(vector4f.x());
                                    vertex[vertexSize*i+1] = Float.floatToRawIntBits(vector4f.y());
                                    vertex[vertexSize*i+2] = Float.floatToRawIntBits(vector4f.z());
                                }
                            }stack.popPose();
                            if (coreQuad.getTintIndex() > -1) {
                                finalQuads.add(new BakedQuad(vertex, YuushyaClientForge.encodeTintWithState(coreQuad.getTintIndex(), core),
                                        coreQuad.getDirection(), coreQuad.getSprite(), coreQuad.isShade()));
                            } else {
                                finalQuads.add(new BakedQuad(vertex, coreQuad.getTintIndex(),
                                        coreQuad.getDirection(), coreQuad.getSprite(), coreQuad.isShade()));
                            }
                        }
                    }
                }
            }
            return finalQuads;
        } else {
            return Collections.emptyList();
        }

    }
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction arg2, Random random) {
        return Collections.emptyList();
    }

    @Override
    public boolean useAmbientOcclusion() {return false;}

    @Override
    public boolean isGui3d() {return true;}

    @Override
    public boolean usesBlockLight() {return false;}

    @Override
    public boolean isCustomRenderer() {return false;}

    @Override
    public TextureAtlasSprite getParticleIcon() {return null;}

    @Override
    public ItemOverrides getOverrides() {return null;}

    @Override
    public TextureAtlasSprite getParticleIcon(@NotNull IModelData data) {
        return Minecraft.getInstance().getBlockRenderer().getBlockModel(Blocks.IRON_BLOCK.defaultBlockState()).getParticleIcon(data);
    }
}
