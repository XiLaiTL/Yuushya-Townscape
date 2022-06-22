package net.cocofish.yuushya.fabric;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Vector4f;
import net.cocofish.yuushya.utils.YuushyaLogger;
import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;
import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.builders.MaterialDefinition;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.AtlasSet;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.material.MaterialRuleList;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

import static net.cocofish.yuushya.utils.YuushyaUtils.*;

public class TestBlockModel implements UnbakedModel, BakedModel, FabricBakedModel {
    private Mesh mesh;
    private static final Material[] SPRITE_IDS = new Material[]{
            new Material( TextureAtlas.LOCATION_BLOCKS, new ResourceLocation("minecraft:block/furnace_front_on")),
            new Material(TextureAtlas.LOCATION_BLOCKS, new ResourceLocation("minecraft:block/furnace_top"))
    };
    private TextureAtlasSprite[] SPRITES = new TextureAtlasSprite[2];
    @Override
    public boolean isVanillaAdapter() {
        return false;
    }

    @Override
    public void emitBlockQuads(BlockAndTintGetter blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
        // Render function
        int vertexSize = DefaultVertexFormat.BLOCK.getVertexSize() / 4;
        QuadEmitter emitter = context.getEmitter();
        BlockRenderDispatcher blockRenderDispatcher = Minecraft.getInstance().getBlockRenderer();
        BakedModel bakedModel= blockRenderDispatcher.getBlockModel(Blocks.SPRUCE_STAIRS.defaultBlockState());
        for (Direction value : Direction.values()) {
            List<BakedQuad> bakedQuads= bakedModel.getQuads(Blocks.SPRUCE_STAIRS.defaultBlockState(),value,randomSupplier.get());
            for (BakedQuad bakedQuad : bakedQuads){

                //emitter.fromVanilla(bakedQuad.getVertices(),0,false);
                int[] vertex = bakedQuad.getVertices().clone();
                // 执行核心方块的位移和旋转

                for (int i = 0; i < 4; i++) {
                    // 顶点的原坐标

                    Vector4f vector4f = new Vector4f(Float.intBitsToFloat(vertex[vertexSize*i]), Float.intBitsToFloat(vertex[vertexSize*i+1]), Float.intBitsToFloat(vertex[vertexSize*i+2]), 1);
                    //vector4f.transform(stack.last().pose());
                    vertex[vertexSize*i] = Float.floatToRawIntBits(vector4f.x());
                    vertex[vertexSize*i+1] = Float.floatToRawIntBits(vector4f.y());
                    vertex[vertexSize*i+2] = Float.floatToRawIntBits(vector4f.z());
                }
                //emitter.fromVanilla(vertex,0,false);
                emitter.spriteBake(0, bakedQuad.getSprite(), MutableQuadView.BAKE_LOCK_UV);
//                // Enable texture usage
                emitter.spriteColor(0, -1, -1, -1, -1);

                emitter.emit();




            }
        }
        emitter.emit();
//        for(Direction direction : Direction.values()) {
//            int spriteIdx = direction == Direction.UP || direction == Direction.DOWN ? 1 : 0;
//            // Add a new face to the mesh
//            emitter.square(direction, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f);
//            // Set the sprite of the face, must be called after .square()
//            // We haven't specified any UV coordinates, so we want to use the whole texture. BAKE_LOCK_UV does exactly that.
//            emitter.spriteBake(0, SPRITES[spriteIdx], MutableQuadView.BAKE_LOCK_UV);
//            // Enable texture usage
//            emitter.spriteColor(0, -1, -1, -1, -1);
//            // Add the quad to the mesh
//            emitter.emit();
//        }
        // We just render the mesh
    }

    @Override
    public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {

    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState blockState, @Nullable Direction direction, Random random) {
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
        return SPRITES[1]; // Block break particle, let's use furnace_top
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
        return Arrays.asList(SPRITE_IDS);
    }

    @Nullable
    @Override
    public BakedModel bake(ModelBakery modelBakery, Function<Material, TextureAtlasSprite> function, ModelState modelState, ResourceLocation resourceLocation) {
        // Get the sprites
        for(int i = 0; i < 2; ++i) {
            SPRITES[i] = function.apply(SPRITE_IDS[i]);
        }
        // Build the mesh using the Renderer API
//        Renderer renderer = RendererAccess.INSTANCE.getRenderer();
//        MeshBuilder builder = renderer.meshBuilder();
//        QuadEmitter emitter = builder.getEmitter();
//
//        for(Direction direction : Direction.values()) {
//            int spriteIdx = direction == Direction.UP || direction == Direction.DOWN ? 1 : 0;
//            // Add a new face to the mesh
//            emitter.square(direction, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f);
//            // Set the sprite of the face, must be called after .square()
//            // We haven't specified any UV coordinates, so we want to use the whole texture. BAKE_LOCK_UV does exactly that.
//            emitter.spriteBake(0, SPRITES[spriteIdx], MutableQuadView.BAKE_LOCK_UV);
//            // Enable texture usage
//            emitter.spriteColor(0, -1, -1, -1, -1);
//            // Add the quad to the mesh
//            emitter.emit();
//        }
//        mesh = builder.build();

        return this;
    }
}
