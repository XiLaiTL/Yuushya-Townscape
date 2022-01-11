package net.cocofish.yuushya;

import net.cocofish.yuushya.block.TubeBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;

public class YuushyaBlock {
    public String classtype="";
    public String rendertype="";
    public String name="";
    public String itemgroup="";
    public String colortype="";
    public String colorstring="";

    public Block.Settings settings;
    public int hardness;
    public int resistance;
    public int lightlevel;
    public float ambientocclusionlightlevel;
    public String sound="";
    public String material="";
    public boolean issolid;
    public boolean havecollision;
    public boolean isdelicate;
    public boolean TubeBlock_ishub;
    public int lines;
    YuushyaBlock()
    {
        classtype="";
        name="";
        itemgroup="";
        rendertype="";
        colortype="null";
        colorstring="";

        hardness=2;
        resistance=2;
        ambientocclusionlightlevel=1.0F;
        material="iron";
        issolid=false;
        isdelicate=false;
        havecollision=true;
        lightlevel=0;
        sound="";
        TubeBlock_ishub=false;
        lines=1;

    }
    public void constructsetting()
    {
        settings= settings.of(toMaterial()).strength(hardness,resistance);
        if (lightlevel!=0)settings=settings.luminance((state) -> { return lightlevel; });
        if(isdelicate)settings=settings.breakInstantly();
        if(issolid==false)settings=settings.nonOpaque();
        if(havecollision==false)settings=settings.noCollision();
        if(!sound.equals(""))settings=settings.sounds(toSound());
    }
    private Material toMaterial()
    {
        switch (material)
        {
            case "air": return Material.AIR;
            case "structure_void": return Material.STRUCTURE_VOID;
            case "portal": return Material.PORTAL;
            case "carpet": return Material.CARPET;
            case "plants": return Material.PLANT;
            case "underwater_plant": return Material.UNDERWATER_PLANT;
            case "tall_plants": return Material.REPLACEABLE_PLANT;
            case "nether_plants": return Material.NETHER_SHOOTS;
            case "sea_grass": return Material.REPLACEABLE_UNDERWATER_PLANT;
            case "water": return Material.WATER;
            case "bubble_column": return Material.BUBBLE_COLUMN;
            case "lava": return Material.LAVA;
            case "snow_layer": return Material.SNOW_LAYER;
            case "fire": return Material.FIRE;
            case "miscellaneous": return Material.DECORATION;
            case "web": return Material.COBWEB;
            case "redstone_lamp": return Material.REDSTONE_LAMP;
            case "clay": return Material.ORGANIC_PRODUCT;
            case "soil": return Material.SOIL;
            case "solid_organic": return Material.SOLID_ORGANIC;
            case "packed_ice": return Material.DENSE_ICE;
            case "sand": return Material.AGGREGATE;
            case "sponge": return Material.SPONGE;
            case "shulker": return Material.SHULKER_BOX;
            case "wood": return Material.WOOD;
            case "nether_wood": return Material.NETHER_WOOD;
            case "bamboo_sapling": return Material.BAMBOO_SAPLING;
            case "bamboo": return Material.BAMBOO;
            case "wool": return Material.WOOL;
            case "tnt": return Material.TNT;
            case "leaves": return Material.LEAVES;
            case "glass": return Material.GLASS;
            case "ice": return Material.ICE;
            case "cactus": return Material.CACTUS;
            case "stone": return Material.STONE;
            case "iron": return Material.METAL;
            case "snow_block": return Material.SNOW_BLOCK;
            case "anvil": return Material.REPAIR_STATION;
            case "barrier": return Material.BARRIER;
            case "piston": return Material.PISTON;
            case "coral": return Material.MOSS_BLOCK;
            case "gourd": return Material.GOURD;
            case "dragon_egg": return Material.EGG;
            case "cake": return Material.CAKE;
            default:return Material.METAL;
        }
    }
    private BlockSoundGroup toSound()
    {
        switch (sound)
        {
            case "wood": return BlockSoundGroup.WOOD;
            case "gravel": return BlockSoundGroup.GRAVEL;
            case "plant": return BlockSoundGroup.GRASS;
            case "lily_pads": return BlockSoundGroup.LILY_PAD;
            case "stone": return BlockSoundGroup.STONE;
            case "metal": return BlockSoundGroup.METAL;
            case "glass": return BlockSoundGroup.GLASS;
            case "wool": return BlockSoundGroup.WOOL;
            case "sand": return BlockSoundGroup.SAND;
            case "snow": return BlockSoundGroup.SNOW;
            case "ladder": return BlockSoundGroup.LADDER;
            case "anvil": return BlockSoundGroup.ANVIL;
            case "slime": return BlockSoundGroup.SLIME;
            case "honey": return BlockSoundGroup.HONEY;
            case "wet_grass": return BlockSoundGroup.WET_GRASS;
            case "coral": return BlockSoundGroup.CORAL;
            case "bamboo": return BlockSoundGroup.BAMBOO;
            case "bamboo_sapling": return BlockSoundGroup.BAMBOO_SAPLING;
            case "scaffolding": return BlockSoundGroup.SCAFFOLDING;
            case "sweet_berry_bush": return BlockSoundGroup.SWEET_BERRY_BUSH;
            case "crop": return BlockSoundGroup.CROP;
            case "stem": return BlockSoundGroup.STEM;
            case "vine": return BlockSoundGroup.VINE;
            case "nether_wart": return BlockSoundGroup.NETHER_WART;
            case "lantern": return BlockSoundGroup.LANTERN;
            case "nether_stem": return BlockSoundGroup.NETHER_STEM;
            case "nylium": return BlockSoundGroup.NYLIUM;
            case "fungus": return BlockSoundGroup.FUNGUS;
            case "root": return BlockSoundGroup.ROOTS;
            case "shroomlight": return BlockSoundGroup.SHROOMLIGHT;
            case "nether_vine": return BlockSoundGroup.WEEPING_VINES;
            case "nether_vine_lower_pitch": return BlockSoundGroup.WEEPING_VINES_LOW_PITCH;
            case "soul_sand": return BlockSoundGroup.SOUL_SAND;
            case "soul_soil": return BlockSoundGroup.SOUL_SOIL;
            case "basalt": return BlockSoundGroup.BASALT;
            case "wart": return BlockSoundGroup.WART_BLOCK;
            case "netherrack": return BlockSoundGroup.NETHERRACK;
            case "nether_brick": return BlockSoundGroup.NETHER_BRICKS;
            case "nether_sprout": return BlockSoundGroup.NETHER_SPROUTS;
            case "nether_ore": return BlockSoundGroup.NETHER_ORE;
            case "bone": return BlockSoundGroup.BONE;
            case "netherite": return BlockSoundGroup.NETHERITE;
            case "ancient_debris": return BlockSoundGroup.ANCIENT_DEBRIS;
            case "lodestone": return BlockSoundGroup.LODESTONE;
            case "chain": return BlockSoundGroup.CHAIN;
            case "nether_gold": return BlockSoundGroup.NETHER_GOLD_ORE;
            case "gilded_blackstone": return BlockSoundGroup.GILDED_BLACKSTONE;
            default:return BlockSoundGroup.METAL;
        }
    }

}
