package com.yuushya.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Vector4f;
import com.yuushya.blocks.ShowBlock;
import com.yuushya.data.TransformData;
import com.yuushya.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
#if MC_VERSION >= "11902"
import net.minecraft.util.RandomSource;
#endif
import java.util.*;
import java.util.function.Function;

public class ShowBlockModel implements UnbakedModel, BakedModel {
#if MC_VERSION >= "11902"
    public List<BakedQuad> getQuads(BlockState state, Direction side, RandomSource rand, ShowBlock.TileEntityShowBlock blockEntity) {
#else
    public List<BakedQuad> getQuads(BlockState state, Direction side, Random rand, ShowBlock.TileEntityShowBlock blockEntity) {
#endif
        int vertexSize = Utils.vertexSize();
        BlockRenderDispatcher blockRenderDispatcher = Minecraft.getInstance().getBlockRenderer();
        List<BakedQuad> finalQuads = new ArrayList<>();
        if (side != null) {
            return Collections.emptyList();
        }
        List<Direction> directions = new ArrayList<>(Arrays.asList(Direction.values()));
        directions.add(null);
        for (TransformData transformData : blockEntity.getTransformDatas()) {
            if (transformData.isShown) {
                BlockState blockState = transformData.blockState;
                BakedModel blockModel = blockRenderDispatcher.getBlockModel(blockState);
                for (Direction value : directions) {
                    List<BakedQuad> blockModelQuads = blockModel.getQuads(blockState, value, rand);
                    for (BakedQuad bakedQuad : blockModelQuads) {
                        int[] vertex = bakedQuad.getVertices().clone();
                        PoseStack stack = new PoseStack();
                        stack.pushPose();
                        ;
                        Utils.scale(stack, transformData.scales);
                        Utils.translate(stack, transformData.pos);
                        Utils.rotate(stack, transformData.rot);
                        for (int i = 0; i < 4; i++) {
                            Vector4f vector4f = new Vector4f(
                                    Float.intBitsToFloat(vertex[vertexSize * i]),
                                    Float.intBitsToFloat(vertex[vertexSize * i + 1]),
                                    Float.intBitsToFloat(vertex[vertexSize * i + 2]), 1);
                            vector4f.transform(stack.last().pose());
                            vertex[vertexSize * i] = Float.floatToRawIntBits(vector4f.x());
                            vertex[vertexSize * i + 1] = Float.floatToRawIntBits(vector4f.y());
                            vertex[vertexSize * i + 2] = Float.floatToRawIntBits(vector4f.z());
                        }
                        stack.popPose();
                        if (bakedQuad.getTintIndex() > -1) {
                            finalQuads.add(new BakedQuad(vertex, Utils.encodeTintWithState(bakedQuad.getTintIndex(), blockState), bakedQuad.getDirection(), bakedQuad.getSprite(), bakedQuad.isShade()));
                        } else {
                            finalQuads.add(new BakedQuad(vertex, bakedQuad.getTintIndex(), bakedQuad.getDirection(), bakedQuad.getSprite(), bakedQuad.isShade()));
                        }
                    }
                }
            }
        }
        return finalQuads;
    }

    @Override
#if MC_VERSION >= "11902"
    public List<BakedQuad> getQuads(BlockState state, Direction side, RandomSource rand) {
#else
    public List<BakedQuad> getQuads(BlockState state, Direction side, Random rand) {
#endif
        return Collections.emptyList();
    }

    @Override
    public boolean useAmbientOcclusion() {
        return false;
    }

    @Override
    public boolean isGui3d() {
        return false;
    }

    @Override
    public boolean usesBlockLight() {
        return false;
    }

    @Override
    public boolean isCustomRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return null;
    }

    @Override
    public ItemTransforms getTransforms() {
        return null;
    }

    @Override
    public ItemOverrides getOverrides() {
        return null;
    }

    @Override
    public Collection<ResourceLocation> getDependencies() {
        return null;
    }

    @Override
    public Collection<Material> getMaterials(Function<ResourceLocation, UnbakedModel> function, Set<Pair<String, String>> set) {
        return null;
    }

    @Override
    public BakedModel bake(ModelBakery modelBakery, Function<Material, TextureAtlasSprite> function, ModelState modelState, ResourceLocation resourceLocation) {
        return null;
    }
}