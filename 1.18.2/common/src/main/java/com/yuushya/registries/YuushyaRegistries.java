package com.yuushya.registries;

import com.google.gson.JsonObject;
import com.yuushya.block.YuushyaBlockFactory;
import com.yuushya.entity.ChairEntity;
import com.yuushya.item.FormTransItem;
import com.yuushya.item.TemplateBlockItem;
import com.yuushya.item.TemplateChildBlockItem;
import com.yuushya.item.YuushyaItemFactory;
import com.yuushya.particle.YuushyaParticleBlock;
import com.yuushya.utils.YuushyaUtils;
import dev.architectury.registry.client.rendering.ColorHandlerRegistry;
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

import java.util.*;

import static com.yuushya.registries.YuushyaRegistryConfig.*;
import static com.yuushya.utils.GsonTools.NormalGSON;
import static com.yuushya.utils.GsonTools.combineYuushyaDataBlockJson;


public class YuushyaRegistries {
    public static final YuushyaDeferredRegister<Block> BLOCKS = new YuushyaDeferredRegister<>(Registry.BLOCK_REGISTRY);
    public static final YuushyaDeferredRegister<Item> ITEMS = new YuushyaDeferredRegister<>(Registry.ITEM_REGISTRY);
    public static final YuushyaDeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = new YuushyaDeferredRegister<>(Registry.BLOCK_ENTITY_TYPE_REGISTRY);
    public static final YuushyaDeferredRegister<EntityType<?>> ENTITIES = new YuushyaDeferredRegister<>(Registry.ENTITY_TYPE_REGISTRY);
    public static final YuushyaDeferredRegister<ParticleType<?>> PARTICLE_TYPES = new YuushyaDeferredRegister<>(Registry.PARTICLE_TYPE_REGISTRY);

    public static final Map<String,YuushyaRegistryData.Block> BlockALL=new HashMap<>();
    public static final Map<String,YuushyaRegistryData.Block> BlockTemplate=new LinkedHashMap<>();
    public static final Map<String,YuushyaRegistryData.Block> BlockOnly=new HashMap<>();
    public static final Map<String,YuushyaRegistryData.Block> BlockRemain=new HashMap<>();
    public static final Map<String,YuushyaRegistryData.Block> TextureTypeMap=new HashMap<>();
    public static final List<Runnable> RegisterList = new ArrayList<>();

    public static void registerRegistries(){
        for (YuushyaRegistryData.Item item: YuushyaRawItemMap.values()){
            RegisterList.add(()->ITEMS.register(item.name,()-> YuushyaItemFactory.create(item))) ;
        }
        for (YuushyaRegistryData.Block block:YuushyaRawBlockMap.values()){
            switch (block.classType){
                case "_comment","class"->{}
                case "remain"->{BlockRemain.put(block.name, block);}
                case "template"->{
                    RegisterList.add(()->ITEMS.register(block.name,()->new TemplateBlockItem(new Item.Properties().tab(YuushyaCreativeModeTab.toGroup(block.itemGroup)),1,block.name)));
                    BlockTemplate.put(block.name, block);
                }
                //case "block"->{BlockOnly.put(block.name,block);}
                default -> {
                    BlockALL.put(block.name, block);
                    RegisterList.add(()->BLOCKS.register(block.name, ()->YuushyaBlockFactory.create(block)));
                    RegisterList.add(()->ITEMS.register(block.name, ()->new BlockItem(BLOCKS.get(block.name).get(),new Item.Properties().tab(YuushyaCreativeModeTab.toGroup(block.itemGroup)))));
                    if (block.texture!=null&&block.texture.type!=null&&!block.texture.type.isEmpty()){
                        TextureTypeMap.put(block.name, block);}
                }
            }
        }
        for (YuushyaRegistryData.Block blockRemain:BlockRemain.values()){
            ResourceLocation blockResourceLocation =new ResourceLocation(blockRemain.name);
            if (blockRemain.texture==null){blockRemain.texture=new YuushyaRegistryData.Block.Texture();blockRemain.texture.type="all";}
            if (blockRemain.texture.value==null||blockRemain.texture.value.isEmpty()) {
                blockRemain.texture.value= new ResourceLocation(blockResourceLocation.getNamespace(),"block/"+blockResourceLocation.getPath()).toString();}
            if (blockRemain.texture.type!=null&&!blockRemain.texture.type.isEmpty()){
                TextureTypeMap.put(blockRemain.name, blockRemain);}
        }
        for (YuushyaRegistryData.Block block:BlockOnly.values()){
            if (block.renderType==null||block.renderType.isEmpty()) block.renderType="cutout";
            if (block.itemGroup == null) block.itemGroup="yuushya_extrablocks";
            RegisterList.add(()->BLOCKS.register(block.name,()->new YuushyaBlockFactory.BlockWithClassType(BlockBehaviour.Properties.of(Material.METAL),1,"block","block",null )));
            RegisterList.add(()->ITEMS.register(block.name,()->new BlockItem(BLOCKS.get(block.name).get(),new Item.Properties().tab(YuushyaCreativeModeTab.toGroup(block.itemGroup)))));
            if (block.texture==null){ block.texture=new YuushyaRegistryData.Block.Texture();block.texture.type="all";}
            if (block.texture.type!=null&&!block.texture.type.isEmpty()){
                TextureTypeMap.put(block.name, block);}
            BlockALL.put(block.name, block);
        }
        for (YuushyaRegistryData.Block templateBlock:BlockTemplate.values()){
            JsonObject templateBlockJson=NormalGSON.toJsonTree(templateBlock,YuushyaRegistryData.Block.class).getAsJsonObject();
            List<YuushyaRegistryData.Block> list=getTemplateUsageList(templateBlock);
            for(YuushyaRegistryData.Block block:list){
                JsonObject blockJson=NormalGSON.toJsonTree(block,YuushyaRegistryData.Block.class).getAsJsonObject();
                YuushyaRegistryData.Block blockNew=combineYuushyaDataBlockJson(blockJson,templateBlockJson);
                blockNew.properties.parent=block.name;
                String name; BlockBehaviour.Properties properties;
                if (block.classType.equals("remain")){
                    ResourceLocation blockResourceLocation =new ResourceLocation(block.name);
                    properties =YuushyaBlockFactory.getBlockProperties(BlockBehaviour.Properties.copy(Registry.BLOCK.get(blockResourceLocation)),blockNew.properties);
                    name=blockResourceLocation.getPath();
                }
                else{
                    properties=YuushyaBlockFactory.getBlockProperties(blockNew.properties);
                    name= block.name;
                }
                blockNew.name=templateBlock.name+"_"+name;
                blockNew.classType = "template:"+templateBlock.name;
                RegisterList.add(()->BLOCKS.register(blockNew.name, ()->YuushyaBlockFactory.create(properties,blockNew)));
                RegisterList.add(()->ITEMS.register(blockNew.name,()->new TemplateChildBlockItem(BLOCKS.get(blockNew.name).get(),new Item.Properties(),block.classType,templateBlock.name,block.name)));
                BlockALL.put(blockNew.name,blockNew);
            }
        }
        //TODP: add particle to normal block
        for(YuushyaRegistryData.Particle particle: YuushyaRawParticleMap.values()){
            if (particle.spawner==null) particle.spawner=new YuushyaRegistryData.Block();
            if (particle.spawner.properties==null) {particle.spawner.properties=new YuushyaRegistryData.Block.Properties();}
            if (particle.spawner.name==null||particle.spawner.name.isEmpty()) particle.spawner.name=particle.name+"_spawner";
            RegisterList.add(()->BLOCKS.register(particle.spawner.name, () -> new YuushyaParticleBlock(YuushyaBlockFactory.getBlockProperties(particle.spawner.properties), particle.spawner.properties.lines,"ParticleBlock","block",particle.spawner.usage,()-> (SimpleParticleType) PARTICLE_TYPES.get(particle.name).get())));
            RegisterList.add(()->ITEMS.register(particle.spawner.name, () -> new BlockItem(BLOCKS.get(particle.spawner.name).get(), new Item.Properties().tab(YuushyaCreativeModeTab.toGroup(particle.spawner.itemGroup)))));
            RegisterList.add(()->PARTICLE_TYPES.register(particle.name, YuushyaParticleBlock.YuushyaParticleType::create));
        }
    }
    public static List<YuushyaRegistryData.Block> getTemplateUsageList(YuushyaRegistryData.Block templateBlock){
        List<YuushyaRegistryData.Block> list=new ArrayList<>();
        if (templateBlock.texture==null||(templateBlock.texture.forClass==null&&templateBlock.texture.forSpecified==null) ){
            list.addAll(TextureTypeMap.values());}
        else{
            if (templateBlock.texture.forClass!=null){
                list.addAll(TextureTypeMap.values().stream().filter((e)->templateBlock.texture.forClass.contains(e.texture.type)).toList());}
            if (templateBlock.texture.forSpecified!=null){
                templateBlock.texture.forSpecified.forEach((name)->{
                    ResourceLocation resourceLocation=ResourceLocation.tryParse(name);
                    if (resourceLocation!=null){
                        if (TextureTypeMap.containsKey(resourceLocation.toString())) list.add(TextureTypeMap.get(resourceLocation.toString()));
                        if (TextureTypeMap.containsKey(resourceLocation.getPath())) list.add(TextureTypeMap.get(resourceLocation.getPath()));
                    }
                });
            }
        }
        return list;
    }



    public static void registerAll(){
        RegisterList.forEach(Runnable::run);
//        ITEMS.register("get_blockstate_item", () -> new GetBlockStateItem(new Item.Properties().tab(YuushyaCreativeModeTab.YUUSHYA_ITEM).stacksTo(1), 1));
        ITEMS.register("form_trans_item",()->new FormTransItem(new Item.Properties().tab(YuushyaCreativeModeTab.YUUSHYA_ITEM).stacksTo(1),2));
        //ITEMS.register("ghost_light",()->new BlockItem(Blocks.LIGHT,new Item.Properties().rarity(Rarity.EPIC).tab(YuushyaCreativeModeTab.YUUSHYA_ITEM)));

//        // 树叶粒子
//        BLOCKS.register("leafparticleblock", () -> new YuushyaParticleBlock(BlockBehaviour.Properties.of(Material.LEAVES).strength(4.0f), 1,"LeafParticleBlock", ()-> (SimpleParticleType) PARTICLE_TYPES.get("leaf_particle").get()));
//        ITEMS.register("leafparticleblock", () -> new BlockItem(BLOCKS.get("leafparticleblock").get(), new Item.Properties().tab(YuushyaCreativeModeTab.YUUSHYA_ITEM)));
//        PARTICLE_TYPES.register("leaf_particle", YuushyaParticleBlock.YuushyaParticleType::create);

        CHAIR_ENTITY = ENTITIES.register("ride_entity",()->EntityType.Builder.of(ChairEntity::new, MobCategory.MISC).sized(ChairEntity.WIDTH,ChairEntity.HEIGHT).build("ride_entity"));
    }


    public static void registerClient(){

        BlockALL.values().forEach((block)->{
            RenderTypeRegistry.register(YuushyaUtils.toRenderType(block.renderType),BLOCKS.get(block.name).get());
            if (block.colorTint!=null&&block.colorTint.colorType!=null&&!block.colorTint.colorType.isEmpty()&& !block.colorTint.colorType.equals("null")){
                ColorHandlerRegistry.registerBlockColors(YuushyaUtils.toBlockColor(block.colorTint.colorType,block.colorTint.colorString),BLOCKS.get(block.name));
            }
        });
    }

    public static RegistrySupplier<EntityType<?>> CHAIR_ENTITY = null;

}
