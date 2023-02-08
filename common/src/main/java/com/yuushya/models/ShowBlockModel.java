package com.yuushya.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Vector4f;
import com.yuushya.blocks.ShowBlock;
import com.yuushya.data.TransformData;
import com.yuushya.mappings.BakedModelMapper;
import com.yuushya.mappings.BakedQuadMapper;
import com.yuushya.mappings.RandomMapper;
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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;
import java.util.function.Function;

public class ShowBlockModel implements UnbakedModel, BakedModelMapper {
    public List<BakedQuad> getQuadsMapper(BlockState state, Direction side, RandomMapper rand, ShowBlock.TileEntityShowBlock blockEntity) {
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
                            finalQuads.add(new BakedQuad(vertex, Utils.encodeTintWithState(bakedQuad.getTintIndex(), blockState), bakedQuad.getDirection(), ((BakedQuadMapper) bakedQuad).getSprite(), bakedQuad.isShade()));
                        } else {
                            finalQuads.add(new BakedQuad(vertex, bakedQuad.getTintIndex(), bakedQuad.getDirection(), ((BakedQuadMapper) bakedQuad).getSprite(), bakedQuad.isShade()));
                        }
                    }
                }
            }
        }
        return finalQuads;
    }

    @Override
    public <T> List<BakedQuad> getQuadsMapper(BlockState blockState, Direction direction, T random) {
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
        return Minecraft.getInstance().getBlockRenderer().getBlockModel(Blocks.IRON_BLOCK.defaultBlockState()).getParticleIcon();
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
        return Collections.emptyList();
    }

    @Override
    public Collection<Material> getMaterials(Function<ResourceLocation, UnbakedModel> function, Set<Pair<String, String>> set) {
        return Collections.emptyList();
    }

    @Override
    public BakedModel bake(ModelBakery modelBakery, Function<Material, TextureAtlasSprite> function, ModelState modelState, ResourceLocation resourceLocation) {
        return this;
    }
}