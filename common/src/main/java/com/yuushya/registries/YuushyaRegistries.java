package com.yuushya.registries;

import com.yuushya.Yuushya;
import com.yuushya.block.YuushyaBlockFactory;
import com.yuushya.block.blockstate.YuushyaBlockStates;
import com.yuushya.blockentity.showblock.ShowBlock;
import com.yuushya.blockentity.showblock.ShowBlockEntity;
import com.yuushya.datagen.BlockStateData;
import com.yuushya.datagen.ModelData;
import com.yuushya.datagen.YuushyaDataProvider;
import com.yuushya.item.TemplateBlockItem;
import com.yuushya.item.YuushyaDebugStickItem;
import com.yuushya.item.showblocktool.*;
import com.yuushya.utils.YuushyaUtils;
import dev.architectury.registry.client.rendering.ColorHandlerRegistry;
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import dev.architectury.registry.registries.RegistrySupplier;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DebugStickItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yuushya.registries.YuushyaRegistryConfig.*;


public class YuushyaRegistries {
    public static final YuushyaDeferredRegister<Block> BLOCKS = new YuushyaDeferredRegister<>(Registry.BLOCK_REGISTRY);
    public static final YuushyaDeferredRegister<Item> ITEMS = new YuushyaDeferredRegister<>(Registry.ITEM_REGISTRY);
    public static final YuushyaDeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = new YuushyaDeferredRegister<>(Registry.BLOCK_ENTITY_TYPE_REGISTRY);

    public static final Map<String,YuushyaRegistryData.Block> BlockTemplate=new HashMap<>();
    public static final Map<String,YuushyaRegistryData.Block> BlockOnly=new HashMap<>();

    public static void registerRegistries(){
        for (YuushyaRegistryData.Block block:YuushyaData.block){
            switch (block.classType){
                case "class"->{YuushyaData.block.remove(block);}
                case "template"->{BlockTemplate.put(block.name, block);YuushyaData.block.remove(block);}
                case "block"->{BlockOnly.put(block.name,block);YuushyaData.block.remove(block);}
                default -> {
                    BLOCKS.register(block.name, ()->YuushyaBlockFactory.create(block));
                    ITEMS.register(block.name, ()->new BlockItem(BLOCKS.get(block.name).get(),new Item.Properties().tab(YuushyaCreativeModeTab.toGroup(block.itemGroup))));
                    if(block.autoGenerated.blockstate){
                        YuushyaDataProvider.of(block.name).type(YuushyaDataProvider.DataType.BlockState).add(block).save();}
                    if (block.autoGenerated.lootTable){
                        YuushyaDataProvider.of(block.name).type(YuushyaDataProvider.DataType.LootTable).add(block).save();}
                }
            }
        }
        for (Map.Entry<String,YuushyaRegistryData.Block> blockEntry:BlockOnly.entrySet()){
            YuushyaRegistryData.Block block=blockEntry.getValue();
            BLOCKS.register(block.name,()->new YuushyaBlockFactory.BlockWithClassType(BlockBehaviour.Properties.of(Material.METAL),1,"block:"+block.name));
            ITEMS.register(block.name,()->new BlockItem(BLOCKS.get(block.name).get(),new Item.Properties().tab(YuushyaCreativeModeTab.YUUSHYA_EXTRA_BLOCKS)));
            YuushyaDataProvider dataProvider= YuushyaDataProvider.of(block.name);
            dataProvider.type(YuushyaDataProvider.DataType.BlockState).json(()->BlockStateData.genSimpleBlock(BLOCKS.get(block.name).get(),new ResourceLocation(Yuushya.MOD_ID,"block/"+block.name))).save();
            dataProvider.type(YuushyaDataProvider.DataType.BlockModel).json(()->ModelData.genSimpleCubeBlockModel(new ResourceLocation(block.texture))).save();
            dataProvider.type(YuushyaDataProvider.DataType.LootTable).add(block).save();
        }
        for (Map.Entry<String,YuushyaRegistryData.Block> blockTemplateEntry:BlockTemplate.entrySet()){
            YuushyaRegistryData.Block templateBlock=blockTemplateEntry.getValue();
            ITEMS.register(templateBlock.name,()->new TemplateBlockItem(new Item.Properties().tab(YuushyaCreativeModeTab.YUUSHYA_EXTRA_BLOCKS),1,templateBlock.name));
            List<String> templateModels=BlockStateData.getModelListFromData(templateBlock.blockstate.models);
            templateBlock.blockstate.forms.forEach((form)->{templateModels.addAll(BlockStateData.getModelListFromData(form));});
            templateModels.forEach(ModelData::setModelTemplate);

            for (Map.Entry<String,YuushyaRegistryData.Block> blockEntry:BlockOnly.entrySet()){
                YuushyaRegistryData.Block block=templateBlock.clone();
//                block.classType=templateBlock.name;
//                block.renderType=templateBlock.renderType;
//                block.properties=templateBlock.properties;
                block.name=templateBlock.name+"_"+blockEntry.getValue().name;
                block.blockstate=new YuushyaRegistryData.Block.BlockState();
                block.blockstate.suit=templateBlock.blockstate.suit;
                block.blockstate.states=templateBlock.blockstate.states;
                block.blockstate.models=templateBlock.blockstate.models.stream().map((s)->s+"_"+block.name).toList();
                block.blockstate.forms=templateBlock.blockstate.forms.stream().map((list)->list.stream().map((s)->s+"_"+block.name).toList()).toList();
                block.colorTint=templateBlock.colorTint;

                BLOCKS.register(block.name, ()->YuushyaBlockFactory.create(block));
                ITEMS.register(block.name,()->new BlockItem(BLOCKS.get(block.name).get(),new Item.Properties()));

                YuushyaDataProvider dataProvider= YuushyaDataProvider.of(block.name);
                dataProvider.type(YuushyaDataProvider.DataType.BlockState).add(block).save();
                dataProvider.type(YuushyaDataProvider.DataType.LootTable).add(block).save();

                YuushyaDataProvider modelDataProvoder= YuushyaDataProvider.of(YuushyaDataProvider.DataType.BlockModel);
                templateModels.forEach((s)->{
                    modelDataProvoder.id(new ResourceLocation(s+"_"+block.name)).json(()->ModelData.genTemplateModel(s,new ResourceLocation(block.texture))).save();
                });
                YuushyaData.block.add(block);
            }
        }
    }
    public static void registerAll(){
//        ITEMS.register("get_blockstate_item", () -> new GetBlockStateItem(new Item.Properties().tab(YuushyaCreativeModeTab.YUUSHYA_ITEM), 1));
        ITEMS.register("pos_trans_item",()->new PosTransItem(new Item.Properties().tab(YuushyaCreativeModeTab.YUUSHYA_ITEM),4));
        ITEMS.register("micro_pos_trans_item",()->new MicroPosTransItem(new Item.Properties().tab(YuushyaCreativeModeTab.YUUSHYA_ITEM),4));
        ITEMS.register("rot_trans_item",()->new RotTransItem(new Item.Properties().tab(YuushyaCreativeModeTab.YUUSHYA_ITEM),4));
        ITEMS.register("scale_trans_item",()->new ScaleTransItem(new Item.Properties().tab(YuushyaCreativeModeTab.YUUSHYA_ITEM),4));
        ITEMS.register("slot_trans_item",()->new SlotTransItem(new Item.Properties().tab(YuushyaCreativeModeTab.YUUSHYA_ITEM),4));
        ITEMS.register("get_showblock_item",()->new GetShowBlockEntityItem(new Item.Properties().tab(YuushyaCreativeModeTab.YUUSHYA_ITEM),4));
        ITEMS.register("move_transformdata_item",()->new MoveTransformDataItem(new Item.Properties().tab(YuushyaCreativeModeTab.YUUSHYA_ITEM),4));
        ITEMS.register("debug_stick_item",()->new YuushyaDebugStickItem(new Item.Properties().tab(YuushyaCreativeModeTab.YUUSHYA_ITEM),4));
        ITEMS.register("get_lit_item",()->new GetLitItem(new Item.Properties().tab(YuushyaCreativeModeTab.YUUSHYA_ITEM),2));
        ITEMS.register("form_trans_item",()->new GetLitItem(new Item.Properties().tab(YuushyaCreativeModeTab.YUUSHYA_ITEM),2));


        BLOCKS.register("test",()->new Block(BlockBehaviour.Properties.of(Material.METAL)));
        ITEMS.register("test",()->new BlockItem(BLOCKS.get("test").get(),new Item.Properties().tab(YuushyaCreativeModeTab.YUUSHYA_ITEM)));
        YuushyaDataProvider yuushyaDataProvider= YuushyaDataProvider.of(new ResourceLocation(Yuushya.MOD_ID,"test"));
        yuushyaDataProvider.type(YuushyaDataProvider.DataType.BlockState).json(()->BlockStateData.genSimpleBlock(BLOCKS.get("test").get(),new ResourceLocation(Yuushya.MOD_ID,"block/test"))).save();
        yuushyaDataProvider.type(YuushyaDataProvider.DataType.BlockModel).json(()->ModelData.genSimpleCubeBlockModel(new ResourceLocation(Yuushya.MOD_ID,"block/test"))).save();
        SHOW_BLOCK= BLOCKS.register("showblock",()->new ShowBlock(BlockBehaviour.Properties.of(Material.METAL).noCollission().noOcclusion().strength(4.0f).lightLevel(blockState ->blockState.getValue(YuushyaBlockStates.LIT)),1));
        ITEMS.register("showblock",()->new BlockItem(BLOCKS.get("showblock").get(),new Item.Properties().tab(YuushyaCreativeModeTab.YUUSHYA_ITEM)));
        SHOW_BLOCK_ENTITY= BLOCK_ENTITIES.register("showblockentity",()->BlockEntityType.Builder.of(ShowBlockEntity::new,BLOCKS.get("showblock").get()).build(null));//Util.fetchChoiceType(References.BLOCK_ENTITY,"yuushya:showblockentity")
    }


    public static void registerClient(){
        BlockOnly.entrySet().forEach((entry)->{
            RenderTypeRegistry.register(RenderType.cutout(),BLOCKS.get(entry.getKey()).get());
        });
        YuushyaData.block.forEach((block)->{
            RenderTypeRegistry.register(YuushyaUtils.toRenderType(block.renderType),BLOCKS.get(block.name).get());
            if (block.colorTint!=null&&block.colorTint.colorType!=null&&!block.colorTint.colorType.isEmpty()&& !block.colorTint.colorType.equals("null")){
                ColorHandlerRegistry.registerBlockColors(YuushyaUtils.toBlockColor(block.colorTint.colorType,block.colorTint.colorString),BLOCKS.get(block.name));
            }
        });
    }


    public static RegistrySupplier<Block> SHOW_BLOCK = null;
    public static RegistrySupplier<BlockEntityType<?>> SHOW_BLOCK_ENTITY = null;


}
