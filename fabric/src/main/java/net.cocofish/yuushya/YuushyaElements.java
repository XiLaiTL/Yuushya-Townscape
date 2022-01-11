package net.cocofish.yuushya;

import net.cocofish.yuushya.block.*;
import net.cocofish.yuushya.block.PlantBlock;
import net.cocofish.yuushya.block.SlabBlock;
import net.cocofish.yuushya.block.cable.CableBlock;
import net.cocofish.yuushya.block.cable.CablePostBlock;
import net.cocofish.yuushya.blockentity.ShelfBlock;
import net.cocofish.yuushya.item.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.*;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.color.world.FoliageColors;
import net.minecraft.client.color.world.GrassColors;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import com.google.gson.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockRenderView;


import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class YuushyaElements {
    //public static final NormalBlock normalblock = new NormalBlock(Block.Settings.of(Material.STONE).strength(4.0f,3.0f));

    //the Item and Block have been registered
    public HashMap<String,Item> itemsregister=new HashMap<>();
    public HashMap<String,Block> blocksregister=new HashMap<>();

    //the Item and Block should be registered
    public List<Block> shelfarray=new ArrayList<>();
    public HashMap<Integer,YuushyaBlock> yuushyablock=new HashMap<>();
    public HashMap<Integer,YuushyaItem> yuushyaitem=new HashMap<>();

    //the count of things should be registered
    public int yuushyablockitem=0;
    public int yuushyaitemcount=0;

    public static final YuushyaUtils YM=new YuushyaUtils();

    //read the hashmap of YuushyaBlock and register
    public void blockRegister(HashMap<Integer,YuushyaBlock> yuushyablockmap)
    {
        yuushyablockmap.forEach((name,yuushyaBlock)->{
            Block  normalblock;
            //new the Block class
            switch (yuushyaBlock.classtype)
            {
                case "NormalBlock":
                    normalblock = new NormalBlock(yuushyaBlock.settings, yuushyaBlock.name,yuushyaBlock.ambientocclusionlightlevel,yuushyaBlock.lines);break;
                case "TableBlock":
                    normalblock =new TableBlock(yuushyaBlock.settings, yuushyaBlock.name,yuushyaBlock.ambientocclusionlightlevel,yuushyaBlock.lines);break;
                case "TubeBlock":
                    normalblock =new TubeBlock(yuushyaBlock.settings, yuushyaBlock.name,yuushyaBlock.ambientocclusionlightlevel,yuushyaBlock.TubeBlock_ishub,yuushyaBlock.lines);break;
                case "TallFurnitureBlock":
                    normalblock =new TallFurnitureBlock(yuushyaBlock.settings, yuushyaBlock.name,yuushyaBlock.ambientocclusionlightlevel,yuushyaBlock.lines);break;
                case "BeamBlock":
                    normalblock =new BeamBlock(yuushyaBlock.settings, yuushyaBlock.name,yuushyaBlock.ambientocclusionlightlevel,yuushyaBlock.lines);break;
                case "BoardBlock":
                    normalblock = new BoardBlock(yuushyaBlock.settings, yuushyaBlock.name,yuushyaBlock.ambientocclusionlightlevel,yuushyaBlock.lines);break;
                case "CableBlock":
                    normalblock = new CableBlock(yuushyaBlock.settings, yuushyaBlock.name,yuushyaBlock.ambientocclusionlightlevel,yuushyaBlock.lines);break;
                case "CablePostBlock":
                    normalblock = new CablePostBlock(yuushyaBlock.settings, yuushyaBlock.name,yuushyaBlock.ambientocclusionlightlevel,yuushyaBlock.lines);break;
                case "ChairBlock":
                    normalblock =new ChairBlock(yuushyaBlock.settings, yuushyaBlock.name,yuushyaBlock.ambientocclusionlightlevel,yuushyaBlock.lines);break;
                case "DecoBlock":
                    normalblock = new DecoBlock(yuushyaBlock.settings, yuushyaBlock.name,yuushyaBlock.ambientocclusionlightlevel,yuushyaBlock.lines);break;
                case "LeafBlock":
                    normalblock = new LeafBlock(yuushyaBlock.settings, yuushyaBlock.name,yuushyaBlock.ambientocclusionlightlevel,yuushyaBlock.lines);break;
                case "LayerBlock":
                    normalblock = new LayerBlock(yuushyaBlock.settings, yuushyaBlock.name,yuushyaBlock.ambientocclusionlightlevel,yuushyaBlock.lines);break;
                case "PlatformBlock":
                    normalblock = new PlatformBlock(yuushyaBlock.settings, yuushyaBlock.name,yuushyaBlock.ambientocclusionlightlevel,yuushyaBlock.lines);break;
                case "PlainBlock":
                    normalblock = new PlainBlock(yuushyaBlock.settings, yuushyaBlock.name,yuushyaBlock.ambientocclusionlightlevel,yuushyaBlock.lines);break;
                case "PoleBlock":
                    normalblock = new PoleBlock(yuushyaBlock.settings, yuushyaBlock.name,yuushyaBlock.ambientocclusionlightlevel,yuushyaBlock.lines);break;
                case "PostBlock":
                    normalblock = new PostBlock(yuushyaBlock.settings, yuushyaBlock.name,yuushyaBlock.ambientocclusionlightlevel,yuushyaBlock.lines);break;
                case "RoofBlock":
                    normalblock = new RoofBlock(yuushyaBlock.settings, yuushyaBlock.name,yuushyaBlock.ambientocclusionlightlevel,yuushyaBlock.lines);break;
                case "RoadBlock":
                    normalblock = new RoadBlock(yuushyaBlock.settings, yuushyaBlock.name,yuushyaBlock.ambientocclusionlightlevel,yuushyaBlock.lines);break;
                case "SemiBlock":
                    normalblock = new SemiBlock(yuushyaBlock.settings, yuushyaBlock.name,yuushyaBlock.ambientocclusionlightlevel,yuushyaBlock.lines);break;
                case "SemiWallBlock":
                    normalblock = new SemiWallBlock(yuushyaBlock.settings, yuushyaBlock.name,yuushyaBlock.ambientocclusionlightlevel,yuushyaBlock.lines);break;
                case "ShopSignBlock":
                    normalblock = new ShopSignBlock(yuushyaBlock.settings, yuushyaBlock.name,yuushyaBlock.ambientocclusionlightlevel,yuushyaBlock.lines);break;
                case "StairBlock":
                    normalblock = new StairBlock(yuushyaBlock.settings, yuushyaBlock.name,yuushyaBlock.ambientocclusionlightlevel,yuushyaBlock.lines);break;
                case "IntactBlock":
                    normalblock = new IntactBlock(yuushyaBlock.settings, yuushyaBlock.name,yuushyaBlock.ambientocclusionlightlevel,yuushyaBlock.lines);break;
                case "PlantBlock":
                    normalblock = new PlantBlock(yuushyaBlock.settings, yuushyaBlock.name,yuushyaBlock.ambientocclusionlightlevel,yuushyaBlock.lines);break;
                case "SlabBlock":
                    normalblock = new SlabBlock(yuushyaBlock.settings, yuushyaBlock.name,yuushyaBlock.ambientocclusionlightlevel,yuushyaBlock.lines);break;
                case "VerticalBlock":
                    normalblock = new VerticalBlock(yuushyaBlock.settings, yuushyaBlock.name,yuushyaBlock.ambientocclusionlightlevel,yuushyaBlock.lines);break;
                case "Vanilla_StairsBlock":
                    normalblock=new Vanilla_StairsBlock(yuushyaBlock.settings, yuushyaBlock.name,yuushyaBlock.ambientocclusionlightlevel,yuushyaBlock.lines);break;
                case "Vanilla_DoorBlock":
                    normalblock=new Vanilla_DoorBlock(yuushyaBlock.settings, yuushyaBlock.name,yuushyaBlock.ambientocclusionlightlevel,yuushyaBlock.lines);break;
                case "ShelfBlock":
                    normalblock=new ShelfBlock(yuushyaBlock.settings, yuushyaBlock.name,yuushyaBlock.ambientocclusionlightlevel,yuushyaBlock.lines);
                    shelfarray.add(normalblock);
                    break;
                default:
                    normalblock = new NormalBlock(yuushyaBlock.settings, yuushyaBlock.name,yuushyaBlock.ambientocclusionlightlevel,yuushyaBlock.lines);
            }
            blocksregister.put(yuushyaBlock.name,Registry.register(Registry.BLOCK, new Identifier("yuushya", yuushyaBlock.name), normalblock));
            /*
        switch (yuushyaBlock.itemgroup)
            {
                case "yuushya_extrablocks":
                    Registry.register(Registry.ITEM, new Identifier("yuushya", yuushyaBlock.name), new BlockItem(normalblock, new Item.Settings().group(Yuushya.yuushyaextrablocks)));break;
                case "yuushya_furniture":
                    Registry.register(Registry.ITEM, new Identifier("yuushya", yuushyaBlock.name), new BlockItem(normalblock, new Item.Settings().group(Yuushya.yuushyafurniture)));break;
                case "yuushya_decoration":
                    Registry.register(Registry.ITEM, new Identifier("yuushya", yuushyaBlock.name), new BlockItem(normalblock, new Item.Settings().group(Yuushya.yuushyadecoration)));break;
                case "yuushya_signs":
                    Registry.register(Registry.ITEM, new Identifier("yuushya", yuushyaBlock.name), new BlockItem(normalblock, new Item.Settings().group(Yuushya.yuushyasigns)));break;
                case "yuushya_life":
                    Registry.register(Registry.ITEM, new Identifier("yuushya", yuushyaBlock.name), new BlockItem(normalblock, new Item.Settings().group(Yuushya.yuushyalife)));break;
                case "yuushya_extrashapes":
                    Registry.register(Registry.ITEM, new Identifier("yuushya", yuushyaBlock.name), new BlockItem(normalblock, new Item.Settings().group(Yuushya.yuushyaextrashapes)));break;
                case "yuushya_infrastructure":
                    Registry.register(Registry.ITEM, new Identifier("yuushya", yuushyaBlock.name), new BlockItem(normalblock, new Item.Settings().group(Yuushya.yuushyainfrastructure)));break;
                case "yuushya_item":
                    Registry.register(Registry.ITEM, new Identifier("yuushya", yuushyaBlock.name), new BlockItem(normalblock, new Item.Settings().group(Yuushya.yuushyaitem)));break;

            }

*/
            YuushyaItem YI=new YuushyaItem();
            Registry.register(Registry.ITEM, new Identifier("yuushya", yuushyaBlock.name), new BlockItem(normalblock, new Item.Settings().group(YI.toGroup(yuushyaBlock.itemgroup))));

            //render type
        });
    }

    //read the hashmap of YuushyaItem and register
    public void itemRegister(HashMap<Integer,YuushyaItem> yuushyaitemmap)
    {
        yuushyaitemmap.forEach((name,yuushyaItem)->{
            Item normalitem;
            switch (yuushyaItem.classtype)
            {
                case "AbstractYuushyaItem":
                    normalitem=new AbstractYuushyaItem(yuushyaItem.settings, yuushyaItem.lines);break;
                case "HatItem":
                    normalitem=new HatItem(yuushyaItem.settings, yuushyaItem.lines);break;
                case "StructureCreatorItem":
                    normalitem=new StructureCreatorItem(yuushyaItem.settings, yuushyaItem.lines,yuushyaItem.createNbt,yuushyaItem.cancelNbt);break;
                default:
                    normalitem=new AbstractYuushyaItem(yuushyaItem.settings, yuushyaItem.lines);
            }
            itemsregister.put(yuushyaItem.name,Registry.register(Registry.ITEM, new Identifier("yuushya", yuushyaItem.name),normalitem));

        });
    }

    //read the Block from JsonObject
    public void blockread(JsonObject object,int i)
    {
        JsonArray array=object.getAsJsonArray("block");
        for(JsonElement jsonElement:array)
        {
            i++;
            YuushyaBlock yuushyaBlock=new YuushyaBlock();
            try { yuushyaBlock.classtype=JsonHelper.getString((JsonObject) jsonElement,"classtype"); }catch (Throwable e){}
            try { yuushyaBlock.rendertype=JsonHelper.getString((JsonObject) jsonElement,"rendertype"); }catch (Throwable e) {}
            try { yuushyaBlock.name=JsonHelper.getString((JsonObject) jsonElement,"name"); }catch (Throwable e) {}
            try { yuushyaBlock.itemgroup=JsonHelper.getString((JsonObject) jsonElement,"itemgroup"); }catch (Throwable e) {}

            try {
                JsonObject jsonObject=(JsonObject) jsonElement;
                JsonObject jsonObjectSon= jsonObject.getAsJsonObject ("properties");
                try { yuushyaBlock.havecollision=JsonHelper.getBoolean(jsonObjectSon,"havecollision"); }catch (Throwable e) {}
                try { yuushyaBlock.issolid=JsonHelper.getBoolean(jsonObjectSon,"issolid"); }catch (Throwable e) {}
                try { yuushyaBlock.isdelicate=JsonHelper.getBoolean(jsonObjectSon,"isdelicate"); }catch (Throwable e) {}
                try { yuushyaBlock.hardness=JsonHelper.getInt(jsonObjectSon,"hardness"); }catch (Throwable e) {}
                try { yuushyaBlock.ambientocclusionlightlevel=JsonHelper.getFloat(jsonObjectSon,"ambientocclusionlightlevel"); }catch (Throwable e) {}
                try { yuushyaBlock.resistance=JsonHelper.getInt(jsonObjectSon,"resistance"); }catch (Throwable e) {}
                try { yuushyaBlock.lightlevel=JsonHelper.getInt(jsonObjectSon,"lightlevel"); }catch (Throwable e) {}
                try { yuushyaBlock.sound=JsonHelper.getString(jsonObjectSon,"sound"); }catch (Throwable e) {}
                try { yuushyaBlock.material=JsonHelper.getString(jsonObjectSon,"material"); }catch (Throwable e) {}
                try { yuushyaBlock.lines=JsonHelper.getInt(jsonObjectSon,"lines"); }catch (Throwable e) {}
                try { yuushyaBlock.TubeBlock_ishub=JsonHelper.getBoolean(jsonObjectSon,"ishub"); }catch (Throwable e) {}
                try {
                    JsonObject colortint= jsonObjectSon.getAsJsonObject ("colortint");
                    try { yuushyaBlock.colortype=JsonHelper.getString(colortint,"colortype"); }catch (Throwable e) {}
                    try { yuushyaBlock.colorstring=JsonHelper.getString(colortint,"colorvalue"); }catch (Throwable e) {}

                }catch (Throwable e){}

            }catch (Throwable e){}
            yuushyaBlock.constructsetting();
            yuushyablock.put(i,yuushyaBlock);
        }
        yuushyablockitem=i;
    }

    //read the Item from JsonObject
    public void itemread(JsonObject object,int i)
    {
        JsonArray array=object.getAsJsonArray("item");
        for(JsonElement jsonElement:array)
        {
            i++;
            YuushyaItem yuushyaItem=new YuushyaItem();
            try { yuushyaItem.classtype=JsonHelper.getString((JsonObject) jsonElement,"classtype"); }catch (Throwable e){}
            try { yuushyaItem.name=JsonHelper.getString((JsonObject) jsonElement,"name"); }catch (Throwable e) {}
            try { yuushyaItem.itemgroup=JsonHelper.getString((JsonObject) jsonElement,"itemgroup"); }catch (Throwable e) {}

            try {
                JsonObject jsonObject=(JsonObject) jsonElement;
                JsonObject jsonObjectSon= jsonObject.getAsJsonObject ("properties");
                try { yuushyaItem.fireproof=JsonHelper.getBoolean(jsonObjectSon,"fireproof"); }catch (Throwable e) {}
                try { yuushyaItem.maxcout=JsonHelper.getInt(jsonObjectSon,"maxcount"); }catch (Throwable e) {}
                try { yuushyaItem.maxdamage=JsonHelper.getInt(jsonObjectSon,"maxdamage"); }catch (Throwable e) {}
                try { yuushyaItem.rarity=JsonHelper.getString(jsonObjectSon,"rarity"); }catch (Throwable e) {}
                try { yuushyaItem.equipment=JsonHelper.getString(jsonObjectSon,"equipmentslot"); }catch (Throwable e) {}
                try { yuushyaItem.createNbt=JsonHelper.getString(jsonObjectSon,"createnbt"); }catch (Throwable e) {}
                try { yuushyaItem.cancelNbt=JsonHelper.getString(jsonObjectSon,"cancelnbt"); }catch (Throwable e) {}

            }catch (Throwable e){}
            yuushyaItem.constructsetting();
            yuushyaitem.put(i,yuushyaItem);
        }
        yuushyaitemcount=i;
    }


    //read the selected files of item and block
    Object var4;
    JsonObject novar=new JsonObject();
    public JsonObject readJson()
    {
        try {
            /*
            Identifier identifier=new Identifier("regist/block.json");
            ResourceManager resourceManager =new ReloadableResourceManagerImpl(ResourceType.CLIENT_RESOURCES);
            Resource resource=null;
            resource = resourceManager.getResource(identifier);
            JsonObject jsonObject = JsonHelper.deserialize((Reader)(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)));
*/

            InputStream inputStream = YuushyaElements.class.getResourceAsStream("/assets/yuushya/regist/block.json");
            Throwable var1 = null;

            JsonObject var2;
            try {
                if (inputStream != null) {
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    Throwable var3 = null;
                    try {
                        try {
                            var4 = JsonHelper.deserialize((Reader) inputStreamReader);
                            //var4= new JsonReader(inputStreamReader);
                            return (JsonObject) var4;
                        } catch (Throwable var30) {
                            var4 = var30;
                            var3 = var30;
                            throw var30;
                        }
                    } finally {
                        if (inputStreamReader != null) {
                            if (var3 != null) {
                                try {
                                    inputStreamReader.close();
                                } catch (Throwable var29) {
                                    var3.addSuppressed(var29);
                                }
                            } else {
                                inputStreamReader.close();
                            }
                        }

                    }
                }
                var2 = novar;
            } catch (Throwable var32) {
                var1 = var32;
                throw var32;
            } finally {
                if (inputStream != null) {
                    if (var1 != null) {
                        try {
                            inputStream.close();
                        } catch (Throwable var28) {
                            var1.addSuppressed(var28);
                        }
                    } else {
                        inputStream.close();
                    }
                }

            }
            return var2;

        } catch (JsonParseException | IOException var34) {
            throw new IllegalStateException("Error in reading the yuushya block regist", var34);
        }
    }
    Object var41;
    JsonObject novar1=new JsonObject();
    public JsonObject readItemJson()
    {
        try {

            InputStream inputStream = YuushyaElements.class.getResourceAsStream("/assets/yuushya/regist/item.json");
            Throwable var1 = null;

            JsonObject var2;
            try {
                if (inputStream != null) {
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    Throwable var3 = null;
                    try {
                        try {
                            var41 = JsonHelper.deserialize((Reader) inputStreamReader);
                            //var41= new JsonReader(inputStreamReader);
                            return (JsonObject) var41;
                        } catch (Throwable var30) {
                            var41 = var30;
                            var3 = var30;
                            throw var30;
                        }
                    } finally {
                        if (inputStreamReader != null) {
                            if (var3 != null) {
                                try {
                                    inputStreamReader.close();
                                } catch (Throwable var29) {
                                    var3.addSuppressed(var29);
                                }
                            } else {
                                inputStreamReader.close();
                            }
                        }

                    }
                }
                var2 = novar1;
            } catch (Throwable var32) {
                var1 = var32;
                throw var32;
            } finally {
                if (inputStream != null) {
                    if (var1 != null) {
                        try {
                            inputStream.close();
                        } catch (Throwable var28) {
                            var1.addSuppressed(var28);
                        }
                    } else {
                        inputStream.close();
                    }
                }

            }
            return var2;

        } catch (JsonParseException | IOException var34) {
            throw new IllegalStateException("Error in reading the yuushya block regist", var34);
        }
    }
    public Block.Settings newSetting(JsonObject jsonObject)
    {
        Block.Settings sett = null;
        sett.of(Material.STONE);
        return sett;
    }

    public void other_registry()
    {
        itemsregister.put("get_entity_item",Registry.register(Registry.ITEM, new Identifier("yuushya", "get_entity_tool"),new GetEntityItem(new FabricItemSettings().group(Yuushya.yuushyaitem))));
        itemsregister.put("get_as_entity_item",Registry.register(Registry.ITEM, new Identifier("yuushya", "get_as_entity_tool"),new GetASEntityItem(new FabricItemSettings().group(Yuushya.yuushyaitem))));
        itemsregister.put("pos_trans_item",Registry.register(Registry.ITEM, new Identifier("yuushya", "pos_trans_item"),new PosTransItem(new FabricItemSettings().group(Yuushya.yuushyaitem), 4)));
        itemsregister.put("micro_pos_trans_item",Registry.register(Registry.ITEM, new Identifier("yuushya", "micro_pos_trans_item"),new MicroPosTransItem(new FabricItemSettings().group(Yuushya.yuushyaitem), 4)));
        itemsregister.put("form_trans_item",Registry.register(Registry.ITEM, new Identifier("yuushya", "form_trans_item"),new FormTransItem(new FabricItemSettings().group(Yuushya.yuushyaitem), 4)));
        itemsregister.put("get_showblock_item",Registry.register(Registry.ITEM, new Identifier("yuushya", "get_showblock_item"),new GetShowBlockEntityItem(new FabricItemSettings().group(Yuushya.yuushyaitem), 4)));
        itemsregister.put("get_mixedblock_item",Registry.register(Registry.ITEM, new Identifier("yuushya", "get_mixedblock_item"),new GetMixedBlockEntityItem(new FabricItemSettings().group(Yuushya.yuushyaitem), 4)));
        itemsregister.put("get_blockstate_item",Registry.register(Registry.ITEM, new Identifier("yuushya", "get_blockstate_item"),new GetBlockStateItem(new FabricItemSettings().group(Yuushya.yuushyaitem), 4)));
        itemsregister.put("get_lit_item",Registry.register(Registry.ITEM, new Identifier("yuushya", "get_lit_item"),new GetLitItem(new FabricItemSettings().group(Yuushya.yuushyaitem), 4)));

    }
    @Environment(EnvType.CLIENT)
    public void clientRegister(YuushyaBlock yuushyaBlock, Block  normalblock)
    {
        switch (yuushyaBlock.rendertype)
        {
            case "cutout":
                BlockRenderLayerMap.INSTANCE.putBlock(normalblock, RenderLayer.getCutout());break;
            case "cutoutmipped":
                BlockRenderLayerMap.INSTANCE.putBlock(normalblock, RenderLayer.getCutoutMipped());break;
            case "translucent":
                BlockRenderLayerMap.INSTANCE.putBlock(normalblock, RenderLayer.getTranslucent());break;
            case "solid":
                BlockRenderLayerMap.INSTANCE.putBlock(normalblock, RenderLayer.getSolid());break;
            default:
                BlockRenderLayerMap.INSTANCE.putBlock(normalblock, RenderLayer.getCutout());break;
        }
        //tint color
        switch (yuushyaBlock.colortype)
        {
            case "null":break;
            case "singlecolor":
                ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> Color.getColor(yuushyaBlock.colorstring).getRGB(), normalblock);break;
            case "vanilla":
                switch (yuushyaBlock.colorstring)
                {
                    case "tall_plant":
                        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
                            return world != null && pos != null ? BiomeColors.getGrassColor(world, state.get(TallPlantBlock.HALF) == DoubleBlockHalf.UPPER ? pos.down() : pos) : -1;
                        }, normalblock);
                        break;
                    case "grass":
                        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
                            return world != null && pos != null ? BiomeColors.getGrassColor(world, pos) : GrassColors.getColor(0.5D, 1.0D);
                        }, normalblock);
                        break;
                    case "spruce":
                        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
                            return FoliageColors.getSpruceColor();
                        }, normalblock);
                        break;
                    case "birch":
                        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
                            return FoliageColors.getBirchColor();
                        }, normalblock);
                        break;
                    case "leaves":
                        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
                            return world != null && pos != null ? BiomeColors.getFoliageColor(world, pos) : FoliageColors.getDefaultColor();
                        },normalblock);
                        break;
                    case "water":
                        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
                            return world != null && pos != null ? BiomeColors.getWaterColor(world, pos) : -1;
                        }, normalblock);
                        break;
                    case "redstone":
                        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
                            return RedstoneWireBlock.getWireColor((Integer)state.get(RedstoneWireBlock.POWER));
                        }, normalblock);
                        break;
                    case "sugar_cane" :
                        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
                            return world != null && pos != null ? BiomeColors.getGrassColor(world, pos) : -1;
                        }, normalblock);
                        break;
                    case "attached_stem":
                        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
                            return 14731036;
                        },normalblock);
                        break;
                    case "stem_with_age":
                        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
                            int i = (Integer)state.get(StemBlock.AGE);
                            int j = i * 32;
                            int k = 255 - i * 8;
                            int l = i * 4;
                            return j << 16 | k << 8 | l;
                        }, normalblock);
                        break;
                    case "lily_pad":
                        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
                            return world != null && pos != null ? 2129968 : 7455580;
                        }, normalblock);
                        break;
                }
                break;
            default:break;
        }
    }

}
