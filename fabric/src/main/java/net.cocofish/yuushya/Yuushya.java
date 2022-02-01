package net.cocofish.yuushya;

import com.google.gson.*;
import net.cocofish.yuushya.blockentity.*;
import net.cocofish.yuushya.entity.ChairEntity;
import net.cocofish.yuushya.entity.ExhibitionEntity;
import net.cocofish.yuushya.particle.LeafParticleBlock;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.resource.ResourceType;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Yuushya implements ModInitializer {

    protected static final File CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve("net.cocofish.yuushya/register.json").toFile();
    public JsonObject yuushyaRegisterObject=new JsonObject();
    private static final Gson GSON = new GsonBuilder().create();
    public final String version= YuushyaUtils.getModVersion();
    public static final String MODID = "yuushya";

    public static EntityType<Entity> exhibitionEntity =Registry.register(
    Registry.ENTITY_TYPE,
            new Identifier("yuushya", "exhibition_entity"),
                FabricEntityTypeBuilder.create(SpawnGroup.MISC,(entitytype, World)->{return new ExhibitionEntity((EntityType<ExhibitionEntity>) (Object)entitytype,World);}).build()
        );
    public static final EntityType<Entity> chairEntity =Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier("yuushya", "chair_entity"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC,ChairEntity::new).dimensions(EntityDimensions.fixed(0.75f, 0.75f)).build()
    );;
//(entitytype, World)->{return new ChairEntity((EntityType<ChairEntity>) (Object)entitytype,World);}
    public static final YuushyaElements yuushyaElements=new YuushyaElements();
    public static final ItemGroup yuushyaextrashapes = FabricItemGroupBuilder.build(new Identifier("yuushya", "extra_shapes"),() -> new ItemStack(yuushyaElements.blocksregister.get("020")));
    public static final ItemGroup yuushyafurniture = FabricItemGroupBuilder.build(new Identifier("yuushya", "furniture"),() -> new ItemStack(yuushyaElements.blocksregister.get("130")));
    public static final ItemGroup yuushyadecoration = FabricItemGroupBuilder.build(new Identifier("yuushya", "decoration"),() -> new ItemStack(yuushyaElements.blocksregister.get("550")));
    public static final ItemGroup yuushyasigns = FabricItemGroupBuilder.build(new Identifier("yuushya", "signs"),() -> new ItemStack(yuushyaElements.blocksregister.get("362")));
    public static final ItemGroup yuushyalife = FabricItemGroupBuilder.build(new Identifier("yuushya", "life"),() -> new ItemStack(yuushyaElements.blocksregister.get("412")));
    public static final ItemGroup yuushyaextrablocks = FabricItemGroupBuilder.build(new Identifier("yuushya", "extra_blocks"),() -> new ItemStack(yuushyaElements.blocksregister.get("24")));
    public static final ItemGroup yuushyainfrastructure = FabricItemGroupBuilder.build(new Identifier("yuushya", "infrastructure"),() -> new ItemStack(yuushyaElements.blocksregister.get("523")));
    public static final ItemGroup yuushyaitem = FabricItemGroupBuilder.build(new Identifier("yuushya", "item"),() -> new ItemStack(yuushyaElements.itemsregister.get("pos_trans_item")));
    public static final ItemGroup yuushyastructure = FabricItemGroupBuilder.build(new Identifier("yuushya", "structure"),() -> new ItemStack(yuushyaElements.itemsregister.get("maple_tree")));

    public static final YuushyaUtils YM=new YuushyaUtils();
    public static DefaultParticleType leafparticle;

    public static BlockEntityType<ModelFrameBlockEntity> modelframeblockentity;
    public static final ModelFrameBlock modelframeblock = new ModelFrameBlock(Block.Settings.of(Material.METAL).nonOpaque().strength(4.0f),"modelframe",1.0f,1);
    public static BlockEntityType<ShowBlockEntity> showblockentity;
    public static final ShowBlock showblock = new ShowBlock(Block.Settings.of(Material.METAL).noCollision().nonOpaque().strength(4.0f).luminance(YuushyaUtils.createLightLevelFromBlockState(15)),"showblock",1.0f,1);
    public static BlockEntityType<ShelfBlockEntity> shelfblockentity;
    public static final ShelfBlock shelfblock = new ShelfBlock(Block.Settings.of(Material.METAL).nonOpaque().strength(4.0f),"shelf",1.0f,1);
    public static BlockEntityType<MixedBlockEntity> mixedblockentity;
    public static final MixedBlock mixedblock = new MixedBlock(Block.Settings.of(Material.METAL).nonOpaque().strength(4.0f).luminance(YuushyaUtils.createLightLevelFromBlockState(15)),"mixedblock",1.0f,1);
    public LeafParticleBlock leafparticleblock;

    @Override
    public void onInitialize() {

        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        YuushyaRegister yr=new YuushyaRegister();
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(yr);
        readregister();

        yuushyaElements.blockread(yuushyaRegisterObject,0);
        yuushyaElements.blockRegister(yuushyaElements.yuushyablock);

        yuushyaElements.itemread(yuushyaRegisterObject,0);
        yuushyaElements.itemRegister(yuushyaElements.yuushyaitem);
        yuushyaElements.other_registry();

        Registry.register(Registry.BLOCK, new Identifier("yuushya","modelframe"),modelframeblock);
        Registry.register(Registry.ITEM, new Identifier("yuushya", "modelframe"), new BlockItem(modelframeblock, new Item.Settings().group(Yuushya.yuushyaitem)));
        modelframeblockentity = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier("yuushya","modelframeentity"), FabricBlockEntityTypeBuilder.create(ModelFrameBlockEntity::new, modelframeblock).build(null));

        Registry.register(Registry.BLOCK, new Identifier("yuushya","showblock"),showblock);
        Registry.register(Registry.ITEM, new Identifier("yuushya", "showblock"), new BlockItem(showblock, new Item.Settings().group(Yuushya.yuushyaitem)));
        showblockentity = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier("yuushya","showblockentity"), FabricBlockEntityTypeBuilder.create(ShowBlockEntity::new, showblock).build(null));

        Registry.register(Registry.BLOCK, new Identifier("yuushya","mixedblock"),mixedblock);
        Registry.register(Registry.ITEM, new Identifier("yuushya", "mixedblock"), new BlockItem(mixedblock, new Item.Settings().group(Yuushya.yuushyaitem)));
        mixedblockentity = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier("yuushya","mixedblockentity"), FabricBlockEntityTypeBuilder.create(MixedBlockEntity::new, mixedblock).build(null));

//        Registry.register(Registry.BLOCK, new Identifier("yuushya","shelf"),shelfblock);
//        BlockRenderLayerMap.INSTANCE.putBlock(shelfblock, RenderLayer.getCutout());
//        Registry.register(Registry.ITEM, new Identifier("yuushya", "shelf"), new BlockItem(shelfblock, new Item.Settings().group(Yuushya.yuushyaitem)));
        Block[] abb=new Block[yuushyaElements.shelfarray.size()];
        shelfblockentity = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier("yuushya","shelf"), FabricBlockEntityTypeBuilder.create(ShelfBlockEntity::new,yuushyaElements.shelfarray.toArray(abb)).build(null));

        leafparticle= Registry.register(Registry.PARTICLE_TYPE,new Identifier("yuushya","leaf_particle"), FabricParticleTypes.simple(true));
        leafparticleblock = new LeafParticleBlock(Block.Settings.of(Material.LEAVES).nonOpaque().strength(4.0f),"leafparticleblock",1.0f,1,leafparticle);
        Registry.register(Registry.BLOCK, new Identifier("yuushya","leafparticleblock"),leafparticleblock);
        Registry.register(Registry.ITEM, new Identifier("yuushya", "leafparticleblock"), new BlockItem(leafparticleblock, new Item.Settings().group(Yuushya.yuushyaitem)));

        //YM.getNumber();
        //System.out.println("Look!#C51162="+YM.String16toInt("#C51162"));
        //System.out.println("Hello Fabric world!");
    }
    @Environment(EnvType.CLIENT)
    public void clientRegister(){

    }


    public void readregist()
    {
        try {
            CONFIG_FILE.getParentFile().mkdirs();
            CONFIG_FILE.createNewFile();
            yuushyaRegisterObject=yuushyaElements.readJson();
            yuushyaRegisterObject.add("item",yuushyaElements.readItemJson().get("item"));
            yuushyaRegisterObject.add("version",GSON.toJsonTree(version));
            BufferedWriter bw = new BufferedWriter(new FileWriter(CONFIG_FILE));
            String s= YM.FormatJsonString(yuushyaRegisterObject.toString());
            bw.write(s);
            bw.close();
        } catch (IOException e) { }
    }

    public void readregister()
    {
        if (!CONFIG_FILE.exists()) {
            readregist();
        }
        else {
            try {

                BufferedReader br = new BufferedReader(new FileReader(CONFIG_FILE));
                JsonElement je = new JsonParser().parse(br);
                JsonObject root = JsonHelper.asObject(je, "root");
                yuushyaRegisterObject=root;
                br.close();
                //load the existed file
                JsonArray array=root.getAsJsonArray("block");
                JsonArray iarray=root.getAsJsonArray("item");
                String fileversion=root.get("version").getAsString();
                if(fileversion.equals(version))
                {
                    List<String> name=new ArrayList<>();
                    JsonObject mainobject=new JsonObject();
                    for(JsonElement jsonElement1:array)
                    {
                        JsonObject object1=JsonHelper.asObject(jsonElement1,"normal");
                        String object2=object1.get("name").getAsString();
                        mainobject.add(object2,jsonElement1);
                        name.add(object2);
                    }
                    JsonObject object3= yuushyaElements.readJson();
                    JsonArray array2=object3.getAsJsonArray("block");
                    for(JsonElement jsonElement1:array2)
                    {
                        JsonObject object1=JsonHelper.asObject(jsonElement1,"normal");
                        String object2=object1.get("name").getAsString();
                        mainobject.add(object2,jsonElement1);
                        name.add(object2);
                    }
                    //let the file in jar be loaded
                    JsonArray array1=new JsonArray();
                    for(String name1:name)
                    {
                        JsonObject object=mainobject.getAsJsonObject(name1);
                        if(object!=null) array1.add(object);
                        mainobject.remove(name1);
                    }
                    root.add("block",array1);

                    List<String> iname=new ArrayList<>();
                    JsonObject imainobject=new JsonObject();
                    for(JsonElement jsonElement1:iarray)
                    {
                        JsonObject object1=JsonHelper.asObject(jsonElement1,"normal");
                        String object2=object1.get("name").getAsString();
                        imainobject.add(object2,jsonElement1);
                        iname.add(object2);
                    }
                    JsonObject iobject3= yuushyaElements.readItemJson();
                    JsonArray iarray2=iobject3.getAsJsonArray("item");
                    for(JsonElement jsonElement1:iarray2)
                    {
                        JsonObject object1=JsonHelper.asObject(jsonElement1,"normal");
                        String object2=object1.get("name").getAsString();
                        imainobject.add(object2,jsonElement1);
                        iname.add(object2);
                    }
                    //let the file in jar be loaded
                    JsonArray iarray1=new JsonArray();
                    for(String name1:iname)
                    {
                        JsonObject object=imainobject.getAsJsonObject(name1);
                        if(object!=null) iarray1.add(object);
                        imainobject.remove(name1);
                    }
                    root.add("item",iarray1);
                    try {
                        BufferedWriter bw = new BufferedWriter(new FileWriter(CONFIG_FILE));
                        String s= YM.FormatJsonString(root.toString());
                        bw.write(s);
                        bw.close();
                    }
                    catch (Throwable e) {}
                }
                else {
                    readregist();
                }


            } catch (IOException | JsonParseException e) {
                readregist();
            }
        }
    }
}


