package com.yuushya.showblock;

import com.yuushya.mappings.BakedModelMapper;
import com.yuushya.mappings.BakedQuadMapper;
import com.yuushya.mappings.UnbakedModelMapper;
import com.yuushya.showblock.data.TransformData;
import com.yuushya.showblock.data.math.Vector4f;
import com.yuushya.showblock.data.vertex.CustomPoseStack;
import com.yuushya.utils.YuushyaUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;

public class ShowBlockModel implements UnbakedModelMapper, BakedModelMapper {
    public List<BakedQuad> getQuads(BlockState state, Direction side, ShowBlock.ShowBlockEntity blockEntity) {
        int vertexSize = YuushyaUtils.vertexSize();
        BlockRenderDispatcher blockRenderDispatcher = Minecraft.getInstance().getBlockRenderer();
        List<BakedQuad> finalQuads = new ArrayList<>();
        if (side != null) {
            return Collections.emptyList();
        }
        ArrayList<Direction> directions = new ArrayList<>(Arrays.asList(Direction.values()));
        directions.add(null);
        for (TransformData transformData : blockEntity.getTransformDatas()) {
            if (transformData.isShown) {
                BlockState blockState = transformData.blockState;
                BakedModel blockModel = blockRenderDispatcher.getBlockModel(blockState);
                for (Direction value : directions) {
                    List<BakedQuad> blockModelQuads = blockModel.getQuads(blockState, value, null);
                    for (BakedQuad bakedQuad : blockModelQuads) {
                        int[] vertex = bakedQuad.getVertices().clone();
                        CustomPoseStack stack = new CustomPoseStack();
                        stack.pushPose();
                        {
                            YuushyaUtils.scale(stack, transformData.scales);
                            YuushyaUtils.translate(stack, transformData.pos);
                            YuushyaUtils.rotate(stack, transformData.rot);
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
                        }
                        stack.popPose();
                        if (bakedQuad.getTintIndex() > -1)
                            finalQuads.add(new BakedQuad(vertex, YuushyaUtils.encodeTintWithState(bakedQuad.getTintIndex(), blockState), bakedQuad.getDirection(), ((BakedQuadMapper) bakedQuad).getSprite(), bakedQuad.isShade()));
                        else
                            finalQuads.add(new BakedQuad(vertex, bakedQuad.getTintIndex(), bakedQuad.getDirection(), ((BakedQuadMapper) bakedQuad).getSprite(), bakedQuad.isShade()));
                    }
                }
            }
        }
        return finalQuads;
    }

    @Override
    public List<BakedQuad> getQuadsMapper(BlockState blockState, Direction direction) {
        return Collections.emptyList();
    }

    @Override
    public BakedModel bakeMapper() {
        return this;
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
}