package net.cocofish.yuushya.forge;


import net.cocofish.yuushya.blockentity.showblock.ShowBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.extensions.IForgeBakedModel;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

public class ShowBlockModel implements IForgeBakedModel, BakedModel {
    public static ModelProperty<BlockState> BASE_BLOCK = new ModelProperty<>();
    BakedModel defaultModel;
    public ShowBlockModel(BakedModel existingModel) {
        defaultModel=existingModel;
    }
    @NotNull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull Random rand, @NotNull IModelData extraData) {
        BakedModel bakedModel = defaultModel;
        if (extraData.hasProperty(BASE_BLOCK)){
            BlockState blockState=extraData.getData(BASE_BLOCK);
            if(blockState!=null){
                BlockRenderDispatcher blockRenderDispatcher =Minecraft.getInstance().getBlockRenderer();
                 bakedModel= blockRenderDispatcher.getBlockModel(blockState);
            }
        }
        return bakedModel.getQuads(state,side,rand,extraData);

    }
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction arg2, Random random) {
        return null;
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



    @NotNull
    @Override
    public IModelData getModelData(@NotNull BlockAndTintGetter level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull IModelData modelData) {
        ShowBlockEntity showBlockEntity = (ShowBlockEntity) level.getBlockEntity(pos);
        BlockState blockState =showBlockEntity.getTransformDatas().get(showBlockEntity.slot).blockState;
        ModelDataMap modelDataMap=new ModelDataMap.Builder().withInitial(BASE_BLOCK,null).build();
        if(blockState.getBlock() instanceof AirBlock){return modelDataMap;}
        modelDataMap.setData(BASE_BLOCK,blockState);
        return modelData;
    }

    @Override
    public TextureAtlasSprite getParticleIcon(@NotNull IModelData data) {
        return defaultModel.getParticleIcon();
    }
}
