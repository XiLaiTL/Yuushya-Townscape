package com.yuushya.utils;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StemBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Material;

import java.awt.*;
import java.util.Arrays;

import static com.yuushya.block.blockstate.YuushyaBlockStates.*;

public class YuushyaUtils {


    public static int vertexSize() { return DefaultVertexFormat.BLOCK.getVertexSize() / 4;} // 一个顶点用多少位int表示，原版和开了光影的OptiFine不同所以得在这算出来

    public static Material toBlockMaterial(String material){
        switch (material){
            case "air": return Material.AIR;
            case "structure_void": return Material.STRUCTURAL_AIR;
            case "portal": return Material.PORTAL;
            case "carpet": return Material.CLOTH_DECORATION;
            case "plants": return Material.PLANT;
            case "underwater_plant": return Material.WATER_PLANT;
            case "tall_plants": return Material.REPLACEABLE_PLANT;
            case "nether_plants": return Material.REPLACEABLE_FIREPROOF_PLANT;
            case "sea_grass": return Material.REPLACEABLE_WATER_PLANT;
            case "water": return Material.WATER;
            case "bubble_column": return Material.BUBBLE_COLUMN;
            case "lava": return Material.LAVA;
            case "snow_layer": return Material.TOP_SNOW;
            case "fire": return Material.FIRE;
            case "miscellaneous": return Material.DECORATION;
            case "web": return Material.WEB;
            //case "sculk": return Material.SCULK;
            case "redstone_lamp": return Material.BUILDABLE_GLASS;
            case "clay": return Material.CLAY;
            case "soil": return Material.DIRT;
            case "solid_organic": return Material.GRASS;
            case "packed_ice": return Material.ICE_SOLID;
            case "sand": return Material.SAND;
            case "sponge": return Material.SPONGE;
            case "shulker": return Material.SHULKER_SHELL;
            case "wood": return Material.WOOD;
            case "nether_wood": return Material.NETHER_WOOD;
            case "bamboo_sapling": return Material.BAMBOO_SAPLING;
            case "bamboo": return Material.BAMBOO;
            case "wool": return Material.WOOL;
            case "tnt": return Material.EXPLOSIVE;
            case "leaves": return Material.LEAVES;
            case "glass": return Material.GLASS;
            case "ice": return Material.ICE;
            case "cactus": return Material.CACTUS;
            case "stone": return Material.STONE;
            case "iron": return Material.METAL;
            case "snow_block": return Material.SNOW;
            case "anvil": return Material.HEAVY_METAL;
            case "barrier": return Material.BARRIER;
            case "piston": return Material.PISTON;
            // case "coral": return Material.MOSS;
            case "gourd": return Material.VEGETABLE;
            case "dragon_egg": return Material.EGG;
            case "cake": return Material.CAKE;
            //case "amethyst": return Material.AMETHYST;
            //case "powder_snow": return Material.POWDER_SNOW;
            default: return Material.METAL;
        }
    }
    public static SoundType toSound(String sound){
         switch (sound){
            case "wood": return SoundType.WOOD;
            case "gravel": return SoundType.GRAVEL;
            case "plant": return SoundType.GRASS;
            case "lily_pads": return SoundType.LILY_PAD;
            case "stone": return SoundType.STONE;
            case "metal": return SoundType.METAL;
            case "glass": return SoundType.GLASS;
            case "wool": return SoundType.WOOL;
            case "sand": return SoundType.SAND;
            case "snow": return SoundType.SNOW;
            case "ladder": return SoundType.LADDER;
            case "anvil": return SoundType.ANVIL;
            case "slime": return SoundType.SLIME_BLOCK;
            case "honey": return SoundType.HONEY_BLOCK;
            case "wet_grass": return SoundType.WET_GRASS;
            case "coral": return SoundType.CORAL_BLOCK;
            case "bamboo": return SoundType.BAMBOO;
            case "bamboo_sapling": return SoundType.BAMBOO_SAPLING;
            case "scaffolding": return SoundType.SCAFFOLDING;
            case "sweet_berry_bush": return SoundType.SWEET_BERRY_BUSH;
            case "crop": return SoundType.CROP;
            case "stem": return SoundType.HARD_CROP;
            case "vine": return SoundType.VINE;
            case "nether_wart": return SoundType.NETHER_WART;
            case "lantern": return SoundType.LANTERN;
            case "nether_stem": return SoundType.STEM;
            case "nylium": return SoundType.NYLIUM;
            case "fungus": return SoundType.FUNGUS;
            case "root": return SoundType.ROOTS;
            case "shroomlight": return SoundType.SHROOMLIGHT;
            case "nether_vine": return SoundType.WEEPING_VINES;
            case "nether_vine_lower_pitch": return SoundType.TWISTING_VINES;
            case "soul_sand": return SoundType.SOUL_SAND;
            case "soul_soil": return SoundType.SOUL_SOIL;
            case "basalt": return SoundType.BASALT;
            case "wart": return SoundType.WART_BLOCK;
            case "netherrack": return SoundType.NETHERRACK;
            case "nether_brick": return SoundType.NETHER_BRICKS;
            case "nether_sprout": return SoundType.NETHER_SPROUTS;
            case "nether_ore": return SoundType.NETHER_ORE;
            case "bone": return SoundType.BONE_BLOCK;
            case "netherite": return SoundType.NETHERITE_BLOCK;
            case "ancient_debris": return SoundType.ANCIENT_DEBRIS;
            case "lodestone": return SoundType.LODESTONE;
            case "chain": return SoundType.CHAIN;
            case "nether_gold": return SoundType.NETHER_GOLD_ORE;
            case "gilded_blackstone": return SoundType.GILDED_BLACKSTONE;
//            case "candle": return SoundType.CANDLE;
//            case "amethyst": return SoundType.AMETHYST;
//            case "amethyst_cluster": return SoundType.AMETHYST_CLUSTER;
//            case "small_amethyst_bud": return SoundType.SMALL_AMETHYST_BUD;
//            case "medium_amethyst_bud": return SoundType.MEDIUM_AMETHYST_BUD;
//            case "large_amethyst_bud": return SoundType.LARGE_AMETHYST_BUD;
//            case "tuff": return SoundType.TUFF;
//            case "calcite": return SoundType.CALCITE;
//            case "dripstone_block": return SoundType.DRIPSTONE_BLOCK;
//            case "pointed_dripstone": return SoundType.POINTED_DRIPSTONE;
//            case "copper": return SoundType.COPPER;
//            case "cave_vines": return SoundType.CAVE_VINES;
//            case "spore_blossom": return SoundType.SPORE_BLOSSOM;
//            case "azalea": return SoundType.AZALEA;
//            case "flowering_azalea": return SoundType.FLOWERING_AZALEA;
//            case "moss_carpet": return SoundType.MOSS_CARPET;
//            case "moss": return SoundType.MOSS;
//            case "big_dripleaf": return SoundType.BIG_DRIPLEAF;
//            case "small_dripleaf": return SoundType.SMALL_DRIPLEAF;
//            case "rooted_dirt": return SoundType.ROOTED_DIRT;
//            case "hanging_roots": return SoundType.HANGING_ROOTS;
//            case "azalea_leaves": return SoundType.AZALEA_LEAVES;
//            case "sculk_sensor": return SoundType.SCULK_SENSOR;
//            case "glow_lichen": return SoundType.GLOW_LICHEN;
//            case "deepslate": return SoundType.DEEPSLATE;
//            case "deepslate_bricks": return SoundType.DEEPSLATE_BRICKS;
//            case "deepslate_tiles": return SoundType.DEEPSLATE_TILES;
//            case "polished_deepslate": return SoundType.POLISHED_DEEPSLATE;
            default: return SoundType.METAL;
        }
    }
    public static IntegerProperty getFormFromState(BlockState blockState){
        if(blockState.hasProperty(FORM2)) return FORM2;
        else if(blockState.hasProperty(FORM3)) return FORM3;
        else if(blockState.hasProperty(FORM4)) return FORM4;
        else if(blockState.hasProperty(FORM5)) return FORM5;
        else if(blockState.hasProperty(FORM6)) return FORM6;
        else if(blockState.hasProperty(FORM7)) return FORM7;
        else if(blockState.hasProperty(FORM8)) return FORM8;
        return null;
    }
    public static Rarity toRarity(String rarity) {
        switch (rarity) {
            case "common" : return Rarity.COMMON;
            case "uncommon" : return Rarity.UNCOMMON;
            case "rare" : return Rarity.RARE;
            case "epic" : return Rarity.EPIC;
            default : return Rarity.COMMON;
        }
    }

    public static RenderType toRenderType(String renderType){
        switch (renderType){
            case "cutout": return RenderType.cutout();
            case "cutoutmipped": return RenderType.cutoutMipped();
            case "translucent": return RenderType.translucent();
            case "solid": return RenderType.solid();
            default : return  RenderType.cutout();
        }
    }
    public static BlockColor toBlockColor(String type, String value){
        switch (type) {
            case "null" : return  (blockState, blockAndTintGetter, blockPos, i) -> 0;
            case "singlecolor" : return  (state, world, pos, tintIndex) -> Color.getColor(value).getRGB();
            case "vanilla" : {
                switch (value) {
                    case "tall_plant" : return  (blockState, blockAndTintGetter, blockPos, i) -> blockAndTintGetter == null || blockPos == null ? -1 : BiomeColors.getAverageGrassColor(blockAndTintGetter, blockState.getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.UPPER ? blockPos.below() : blockPos);
                    case "grass" : return  (blockState, blockAndTintGetter, blockPos, i) -> blockAndTintGetter == null || blockPos == null ? GrassColor.get(0.5, 1.0) : BiomeColors.getAverageGrassColor(blockAndTintGetter, blockPos);
                    case "spruce" : return  (blockState, blockAndTintGetter, blockPos, i) -> FoliageColor.getEvergreenColor();
                    case "birch" : return  (blockState, blockAndTintGetter, blockPos, i) -> FoliageColor.getBirchColor();
                    case "leaves" : return  (blockState, blockAndTintGetter, blockPos, i) -> blockAndTintGetter == null || blockPos == null ? FoliageColor.getDefaultColor() : BiomeColors.getAverageFoliageColor(blockAndTintGetter, blockPos);
                    case "water" : return  (blockState, blockAndTintGetter, blockPos, i) -> blockAndTintGetter == null || blockPos == null ? -1 : BiomeColors.getAverageWaterColor(blockAndTintGetter, blockPos);
                    case "redstone" : return  (blockState, blockAndTintGetter, blockPos, i) -> RedStoneWireBlock.getColorForPower(blockState.getValue(RedStoneWireBlock.POWER));
                    case "sugar_cane" : return  (blockState, blockAndTintGetter, blockPos, i) -> blockAndTintGetter == null || blockPos == null ? -1 : BiomeColors.getAverageGrassColor(blockAndTintGetter, blockPos);
                    case "attached_stem" : return  (blockState, blockAndTintGetter, blockPos, i) -> 14731036;
                    case "stem_with_age" : return  (blockState, blockAndTintGetter, blockPos, i) -> {
                        int j = blockState.getValue(StemBlock.AGE);
                        int k = j * 32;
                        int l = 255 - j * 8;
                        int m = j * 4;
                        return k << 16 | l << 8 | m;
                    };
                    case "lily_pad" : return  (blockState, blockAndTintGetter, blockPos, i) -> blockAndTintGetter == null || blockPos == null ? 7455580 : 2129968;
                    default : return  (blockState, blockAndTintGetter, blockPos, i) -> 0;
                }
            }
            default : return  (blockState, blockAndTintGetter, blockPos, i) -> 0;
        }
    }

    public static BlockBehaviour.OffsetType toOffsetType(String offset){
        if (offset==null) return BlockBehaviour.OffsetType.NONE;
        switch (offset){
            case "xz": return  BlockBehaviour.OffsetType.XZ;
            case "xyz": return  BlockBehaviour.OffsetType.XYZ;
            default : return  BlockBehaviour.OffsetType.NONE;
        }
    }

    //TODO
    public static BlockState getBlockState(BlockState blockState, LevelAccessor world, BlockPos blockPos){
        return blockState;
    }


    public static <T> ListTag toListTag(T... values){
        ListTag listTag=new ListTag();
        Arrays.stream(values).forEach((e)->{
            if(e instanceof Float)
                listTag.add(FloatTag.valueOf((Float) e));
            else if( e instanceof Double )
                listTag.add(DoubleTag.valueOf((Double) e));
        });
        return listTag;
    }
}
