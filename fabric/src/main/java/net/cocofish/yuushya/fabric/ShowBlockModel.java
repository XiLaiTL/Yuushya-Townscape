package net.cocofish.yuushya.fabric;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Vector4f;
import net.cocofish.yuushya.blockentity.TransformData;
import net.cocofish.yuushya.blockentity.showblock.ShowBlockEntity;
import net.cocofish.yuushya.utils.YuushyaLogger;
import net.cocofish.yuushya.utils.YuushyaUtils;
import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;
import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

import static net.cocofish.yuushya.utils.YuushyaUtils.*;

public class ShowBlockModel implements UnbakedModel,BakedModel, FabricBakedModel {
    @Override
    public boolean isVanillaAdapter() {
        return false;
    }

    @Override
    public void emitBlockQuads(BlockAndTintGetter blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
        ShowBlockEntity blockEntity=(ShowBlockEntity) blockView.getBlockEntity(pos);
        context.fallbackConsumer().accept(new ShowBlockModel() {
            @Override
            public boolean isVanillaAdapter() {
                return true;
            }

            @Override
            public List<BakedQuad> getQuads(@Nullable BlockState blockState, @Nullable Direction side, Random rand) {
                if (blockEntity != null) {
                    PoseStack stack = new PoseStack();
                    // 一个顶点用多少位int表示，原版和开了光影的OptiFine不同所以得在这算出来
                    int vertexSize = DefaultVertexFormat.BLOCK.getVertexSize() / 4;
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
                                        finalQuads.add(new BakedQuad(vertex, YuushyaUtils.encodeTintWithState(coreQuad.getTintIndex(), core),
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
        });
        //        QuadEmitter emitter = context.getEmitter();
//
//        ShowBlockEntity blockEntity= (ShowBlockEntity) blockView.getBlockEntity(pos);
//        PoseStack stack = new PoseStack();
//        // 一个顶点用多少位int表示，原版和开了光影的OptiFine不同所以得在这算出来
//        int vertexSize = DefaultVertexFormat.BLOCK.getVertexSize() / 4;
//        if (blockEntity != null) {
//            BlockRenderDispatcher blockRenderDispatcher = Minecraft.getInstance().getBlockRenderer();
//            List<BakedQuad> finalQuads = new ArrayList<>();
//
//            ArrayList<Direction> directions = new ArrayList<>(Arrays.asList(Direction.values()));
//            //directions.add(null); // 加个null
//
//            for(TransformData transformData:blockEntity.getTransformDatas()){
//                BlockState core = transformData.blockState;
//                BakedModel coreModel = blockRenderDispatcher.getBlockModel(core);
////                for (Direction value : directions) {
////                    List<BakedQuad> coreQuads = coreModel.getQuads(core, value, randomSupplier.get());
////
////                    for (BakedQuad coreQuad : coreQuads) {
////
//////                        emitter.fromVanilla(coreQuad,null,null);
//////                        emitter.spriteBake(0, coreQuad.getSprite(), MutableQuadView.BAKE_LOCK_UV);
//////                        //                        emitter.spriteColor(0, -1, -1, -1, -1);
//////                        //emitter.fromVanilla(new BakedQuad(vertex,coreQuad.getTintIndex(),coreQuad.getDirection(),coreQuad.getSprite(),false), (RenderMaterial) RenderMaterial.MATERIAL_STANDARD,null);
//////                        emitter.emit();
////                    }
////                }
//                context.fallbackConsumer().accept(coreModel);
//
//
//            }
//        }


    }

    @Override
    public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {

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
    public Collection<Material> getMaterials(Function<ResourceLocation, UnbakedModel> function, Set<Pair<String, String>> set) {
        return Collections.emptyList();
    }

    @Nullable
    @Override
    public BakedModel bake(ModelBakery modelBakery, Function<Material, TextureAtlasSprite> function, ModelState modelState, ResourceLocation resourceLocation) {
        return this;
    }
}
