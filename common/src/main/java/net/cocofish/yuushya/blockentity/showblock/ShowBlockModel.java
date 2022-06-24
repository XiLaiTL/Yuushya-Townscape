package net.cocofish.yuushya.blockentity.showblock;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Vector4f;
import dev.architectury.injectables.annotations.PlatformOnly;
import net.cocofish.yuushya.blockentity.TransformData;
import net.cocofish.yuushya.utils.YuushyaUtils;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;

import static net.cocofish.yuushya.utils.YuushyaUtils.*;

public class ShowBlockModel implements BakedModel, UnbakedModel {
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull Random rand, @NotNull ShowBlockEntity blockEntity) {
        BlockRenderDispatcher blockRenderDispatcher =Minecraft.getInstance().getBlockRenderer();
        List<BakedQuad> finalQuads = new ArrayList<>();
        if (side != null) {return Collections.emptyList();}
        ArrayList<Direction> directions = new ArrayList<>(Arrays.asList(Direction.values()));directions.add(null); // 加个null
        for(TransformData transformData:blockEntity.getTransformDatas()){
            BlockState blockState = transformData.blockState;
            BakedModel blockModel = blockRenderDispatcher.getBlockModel(blockState);
            for (Direction value : directions) {
                List<BakedQuad> blockModelQuads = blockModel.getQuads(blockState, value, rand);
                for (BakedQuad bakedQuad : blockModelQuads) {
                    int[] vertex = bakedQuad.getVertices().clone();
                    // 执行核心方块的位移和旋转
                    PoseStack stack = new PoseStack();
                    stack.pushPose();{
                        scale(stack, transformData.scales);
                        translate(stack,transformData.pos);
                        rotate(stack,transformData.rot);
                        for (int i = 0; i < 4; i++) {
                            Vector4f vector4f = new Vector4f(// 顶点的原坐标
                                    Float.intBitsToFloat(vertex[vertexSize*i]),
                                    Float.intBitsToFloat(vertex[vertexSize*i+1]),
                                    Float.intBitsToFloat(vertex[vertexSize*i+2]), 1);
                            vector4f.transform(stack.last().pose());
                            vertex[vertexSize*i] = Float.floatToRawIntBits(vector4f.x());
                            vertex[vertexSize*i+1] = Float.floatToRawIntBits(vector4f.y());
                            vertex[vertexSize*i+2] = Float.floatToRawIntBits(vector4f.z());
                        }
                    }stack.popPose();
                    if (bakedQuad.getTintIndex() > -1)//将方块状态和颜色编码到tintindex上，在渲染时解码找到对应颜色
                        finalQuads.add(new BakedQuad(vertex, YuushyaUtils.encodeTintWithState(bakedQuad.getTintIndex(), blockState), bakedQuad.getDirection(), bakedQuad.getSprite(), bakedQuad.isShade()));
                    else
                        finalQuads.add(new BakedQuad(vertex, bakedQuad.getTintIndex(), bakedQuad.getDirection(), bakedQuad.getSprite(), bakedQuad.isShade()));
                }
            }
        }
        return finalQuads;
    }



    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState blockState, @Nullable Direction side, Random rand) {
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
    public Collection<Material> getMaterials(Function<ResourceLocation, UnbakedModel> function, Set<Pair<String, String>> set) {return Collections.emptyList();}

    @Nullable
    @Override
    public BakedModel bake(ModelBakery modelBakery, Function<Material, TextureAtlasSprite> function, ModelState modelState, ResourceLocation resourceLocation) {return this;}

}
