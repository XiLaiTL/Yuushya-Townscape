package com.yuushya.utils;

import com.google.common.collect.Maps;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3d;
import com.mojang.math.Vector3f;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Material;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

public class YuushyaUtils {
    public static final Gson NormalGSON =new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_DASHES).setPrettyPrinting().create();
    public static int vertexSize = DefaultVertexFormat.BLOCK.getVertexSize() / 4;// 一个顶点用多少位int表示，原版和开了光影的OptiFine不同所以得在这算出来

    public static void scale(PoseStack arg, Vector3f scales){
        if(scales.x()!=1||scales.y()!=1||scales.z()!=1){
            arg.translate(0.5,0.5,0.5);
            arg.scale(scales.x(),scales.y(),scales.z());
            arg.translate(-0.5,-0.5,-0.5);
        }
    }
    public static void translate(PoseStack arg, Vector3d pos){
        if (pos.x!=0.0||pos.y!=0.0||pos.z!=0.0)
            arg.translate(pos.x/16,pos.y/16,pos.z/16);
    }
    public static void rotate(PoseStack arg,Vector3f rot) {
        float roll = rot.z(),yaw = rot.y(),pitch= rot.x();
        if(roll!=0.0F||yaw != 0.0F||pitch != 0.0F){
            arg.translate(0.5,0.5,0.5);
            if (roll != 0.0F)
                arg.mulPose(Vector3f.ZP.rotationDegrees(roll));
            if (yaw != 0.0F)
                arg.mulPose(Vector3f.YP.rotationDegrees(yaw));
            if (pitch != 0.0F)
                arg.mulPose(Vector3f.XP.rotationDegrees(pitch));
            arg.translate(-0.5,-0.5,-0.5);
        }
    }

    public static int encodeTintWithState(int tint, BlockState state) {
        // 最高位依然可以保留负数信息，但tint的有效位数很低了，原版够用，mod一般也不会用这个东西
        return Block.getId(state) << 8 | tint;
    }

    public static Material toBlockMaterial(String material){
        return switch (material){
            case "air"->Material.AIR;
            case "structure_void"->Material.STRUCTURAL_AIR;
            case "portal"->Material.PORTAL;
            case "carpet"->Material.CLOTH_DECORATION;
            case "plants"->Material.PLANT;
            case "underwater_plant"->Material.WATER_PLANT;
            case "tall_plants"->Material.REPLACEABLE_PLANT;
            case "nether_plants"->Material.REPLACEABLE_FIREPROOF_PLANT;
            case "sea_grass"->Material.REPLACEABLE_WATER_PLANT;
            case "water"->Material.WATER;
            case "bubble_column"->Material.BUBBLE_COLUMN;
            case "lava"->Material.LAVA;
            case "snow_layer"->Material.TOP_SNOW;
            case "fire"->Material.FIRE;
            case "miscellaneous"->Material.DECORATION;
            case "web"->Material.WEB;
            case "sculk"->Material.SCULK;
            case "redstone_lamp"->Material.BUILDABLE_GLASS;
            case "clay"->Material.CLAY;
            case "soil"->Material.DIRT;
            case "solid_organic"->Material.GRASS;
            case "packed_ice"->Material.ICE_SOLID;
            case "sand"->Material.SAND;
            case "sponge"->Material.SPONGE;
            case "shulker"->Material.SHULKER_SHELL;
            case "wood"->Material.WOOD;
            case "nether_wood"->Material.NETHER_WOOD;
            case "bamboo_sapling"->Material.BAMBOO_SAPLING;
            case "bamboo"->Material.BAMBOO;
            case "wool"->Material.WOOL;
            case "tnt"->Material.EXPLOSIVE;
            case "leaves"->Material.LEAVES;
            case "glass"->Material.GLASS;
            case "ice"->Material.ICE;
            case "cactus"->Material.CACTUS;
            case "stone"->Material.STONE;
            case "iron"->Material.METAL;
            case "snow_block"->Material.SNOW;
            case "anvil"->Material.HEAVY_METAL;
            case "barrier"->Material.BARRIER;
            case "piston"->Material.PISTON;
            case "coral"->Material.MOSS;
            case "gourd"->Material.VEGETABLE;
            case "dragon_egg"->Material.EGG;
            case "cake"->Material.CAKE;
            case "amethyst"->Material.AMETHYST;
            case "powder_snow"->Material.POWDER_SNOW;
            default->Material.METAL;
        };
    }
    public static SoundType toSound(String sound){
        return switch (sound){
            case "wood"-> SoundType.WOOD;
            case "gravel"-> SoundType.GRAVEL;
            case "plant"-> SoundType.GRASS;
            case "lily_pads"-> SoundType.LILY_PAD;
            case "stone"-> SoundType.STONE;
            case "metal"-> SoundType.METAL;
            case "glass"-> SoundType.GLASS;
            case "wool"-> SoundType.WOOL;
            case "sand"-> SoundType.SAND;
            case "snow"-> SoundType.SNOW;
            case "ladder"-> SoundType.LADDER;
            case "anvil"-> SoundType.ANVIL;
            case "slime"-> SoundType.SLIME_BLOCK;
            case "honey"-> SoundType.HONEY_BLOCK;
            case "wet_grass"-> SoundType.WET_GRASS;
            case "coral"-> SoundType.CORAL_BLOCK;
            case "bamboo"-> SoundType.BAMBOO;
            case "bamboo_sapling"-> SoundType.BAMBOO_SAPLING;
            case "scaffolding"-> SoundType.SCAFFOLDING;
            case "sweet_berry_bush"-> SoundType.SWEET_BERRY_BUSH;
            case "crop"-> SoundType.CROP;
            case "stem"-> SoundType.HARD_CROP;
            case "vine"-> SoundType.VINE;
            case "nether_wart"-> SoundType.NETHER_WART;
            case "lantern"-> SoundType.LANTERN;
            case "nether_stem"-> SoundType.STEM;
            case "nylium"-> SoundType.NYLIUM;
            case "fungus"-> SoundType.FUNGUS;
            case "root"-> SoundType.ROOTS;
            case "shroomlight"-> SoundType.SHROOMLIGHT;
            case "nether_vine"-> SoundType.WEEPING_VINES;
            case "nether_vine_lower_pitch"-> SoundType.TWISTING_VINES;
            case "soul_sand"-> SoundType.SOUL_SAND;
            case "soul_soil"-> SoundType.SOUL_SOIL;
            case "basalt"-> SoundType.BASALT;
            case "wart"-> SoundType.WART_BLOCK;
            case "netherrack"-> SoundType.NETHERRACK;
            case "nether_brick"-> SoundType.NETHER_BRICKS;
            case "nether_sprout"-> SoundType.NETHER_SPROUTS;
            case "nether_ore"-> SoundType.NETHER_ORE;
            case "bone"-> SoundType.BONE_BLOCK;
            case "netherite"-> SoundType.NETHERITE_BLOCK;
            case "ancient_debris"-> SoundType.ANCIENT_DEBRIS;
            case "lodestone"-> SoundType.LODESTONE;
            case "chain"-> SoundType.CHAIN;
            case "nether_gold"-> SoundType.NETHER_GOLD_ORE;
            case "gilded_blackstone"-> SoundType.GILDED_BLACKSTONE;
            case "candle"-> SoundType.CANDLE;
            case "amethyst"-> SoundType.AMETHYST;
            case "amethyst_cluster"-> SoundType.AMETHYST_CLUSTER;
            case "small_amethyst_bud"-> SoundType.SMALL_AMETHYST_BUD;
            case "medium_amethyst_bud"-> SoundType.MEDIUM_AMETHYST_BUD;
            case "large_amethyst_bud"-> SoundType.LARGE_AMETHYST_BUD;
            case "tuff"-> SoundType.TUFF;
            case "calcite"-> SoundType.CALCITE;
            case "dripstone_block"-> SoundType.DRIPSTONE_BLOCK;
            case "pointed_dripstone"-> SoundType.POINTED_DRIPSTONE;
            case "copper"-> SoundType.COPPER;
            case "cave_vines"-> SoundType.CAVE_VINES;
            case "spore_blossom"-> SoundType.SPORE_BLOSSOM;
            case "azalea"-> SoundType.AZALEA;
            case "flowering_azalea"-> SoundType.FLOWERING_AZALEA;
            case "moss_carpet"-> SoundType.MOSS_CARPET;
            case "moss"-> SoundType.MOSS;
            case "big_dripleaf"-> SoundType.BIG_DRIPLEAF;
            case "small_dripleaf"-> SoundType.SMALL_DRIPLEAF;
            case "rooted_dirt"-> SoundType.ROOTED_DIRT;
            case "hanging_roots"-> SoundType.HANGING_ROOTS;
            case "azalea_leaves"-> SoundType.AZALEA_LEAVES;
            case "sculk_sensor"-> SoundType.SCULK_SENSOR;
            case "glow_lichen"-> SoundType.GLOW_LICHEN;
            case "deepslate"-> SoundType.DEEPSLATE;
            case "deepslate_bricks"-> SoundType.DEEPSLATE_BRICKS;
            case "deepslate_tiles"-> SoundType.DEEPSLATE_TILES;
            case "polished_deepslate"-> SoundType.POLISHED_DEEPSLATE;
            default->SoundType.METAL;
        };
    }
    public static Rarity toRarity(String rarity) {
        return switch (rarity) {
            case "common" -> Rarity.COMMON;
            case "uncommon" -> Rarity.UNCOMMON;
            case "rare" -> Rarity.RARE;
            case "epic" -> Rarity.EPIC;
            default -> Rarity.COMMON;
        };
    }

    public static BlockBehaviour.OffsetType toOffsetType(String offset){
        return switch (offset){
            case "xz"-> BlockBehaviour.OffsetType.XZ;
            case "xyz"-> BlockBehaviour.OffsetType.XYZ;
            default -> BlockBehaviour.OffsetType.NONE;
        };
    }

}
