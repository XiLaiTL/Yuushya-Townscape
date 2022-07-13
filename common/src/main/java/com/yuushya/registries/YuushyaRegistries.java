package com.yuushya.registries;

import com.yuushya.Yuushya;
import com.yuushya.block.YuushyaBlockFactory;
import com.yuushya.blockentity.showblock.ShowBlock;
import com.yuushya.blockentity.showblock.ShowBlockEntity;
import com.yuushya.datagen.BlockStateData;
import com.yuushya.datagen.ModelData;
import com.yuushya.datagen.YuushyaDataProvider;
import com.yuushya.item.showblocktool.*;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

import java.util.HashMap;
import java.util.Map;

import static com.yuushya.registries.YuushyaRegistryConfig.*;


public class YuushyaRegistries {
    public static final YuushyaDeferredRegister<Block> BLOCKS = new YuushyaDeferredRegister<>(Registry.BLOCK_REGISTRY);
    public static final YuushyaDeferredRegister<Item> ITEMS = new YuushyaDeferredRegister<>(Registry.ITEM_REGISTRY);
    public static final YuushyaDeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = new YuushyaDeferredRegister<>(Registry.BLOCK_ENTITY_TYPE_REGISTRY);

    public static final Map<String,YuushyaRegistryData.Block> BlockTemplate=new HashMap<>();
    public static final Map<String,YuushyaRegistryData.Block> BlockOnly=new HashMap<>();

    public static void readingRegistries(){
        for (YuushyaRegistryData.Block block:YuushyaData.block){
            switch (block.classType){
                case "class"->{YuushyaData.block.remove(block);}
                case "template"->{BlockTemplate.put(block.name, block);YuushyaData.block.remove(block);}
                case "block"->{BlockOnly.put(block.name,block);YuushyaData.block.remove(block);}
                default -> {
                    BLOCKS.register(block.name, ()->YuushyaBlockFactory.create(block));
                    ITEMS.register(block.name, ()->new BlockItem(BLOCKS.get(block.name).get(),new Item.Properties().tab(YuushyaCreativeModeTab.toGroup(block.itemGroup))));
                    if(block.autoGenerated.blockstate){
                        YuushyaDataProvider.of(block.name).type(YuushyaDataProvider.DataType.BlockState).add(block);}
                    if (block.autoGenerated.lootTable){
                        YuushyaDataProvider.of(block.name).type(YuushyaDataProvider.DataType.LootTable).add(block);}
                }
            }
        }

        for (Map.Entry<String,YuushyaRegistryData.Block> blockEntry:BlockTemplate.entrySet()){

        }
    }
    public static void registerAll(){
        ITEMS.register("example_item",()->new Item(new Item.Properties().tab(YuushyaCreativeModeTab.YUUSHYA_ITEM)));
//        ITEMS.register("get_blockstate_item", () -> new GetBlockStateItem(new Item.Properties().tab(YuushyaCreativeModeTab.YUUSHYA_ITEM), 1));
        ITEMS.register("pos_trans_item",()->new PosTransItem(new Item.Properties().tab(YuushyaCreativeModeTab.YUUSHYA_ITEM),4));
        ITEMS.register("micro_pos_trans_item",()->new MicroPosTransItem(new Item.Properties().tab(YuushyaCreativeModeTab.YUUSHYA_ITEM),4));
        ITEMS.register("rot_trans_item",()->new RotTransItem(new Item.Properties().tab(YuushyaCreativeModeTab.YUUSHYA_ITEM),4));
        ITEMS.register("scale_trans_item",()->new ScaleTransItem(new Item.Properties().tab(YuushyaCreativeModeTab.YUUSHYA_ITEM),4));
        ITEMS.register("slot_trans_item",()->new SlotTransItem(new Item.Properties().tab(YuushyaCreativeModeTab.YUUSHYA_ITEM),4));
        ITEMS.register("get_showblock_item",()->new GetShowBlockEntityItem(new Item.Properties().tab(YuushyaCreativeModeTab.YUUSHYA_ITEM),4));
        ITEMS.register("move_transformdata_item",()->new MoveTransformDataItem(new Item.Properties().tab(YuushyaCreativeModeTab.YUUSHYA_ITEM),4));

        BLOCKS.register("test",()->new Block(BlockBehaviour.Properties.of(Material.METAL)));
        ITEMS.register("test",()->new BlockItem(BLOCKS.get("test").get(),new Item.Properties().tab(YuushyaCreativeModeTab.YUUSHYA_ITEM)));
        YuushyaDataProvider yuushyaDataProvider= YuushyaDataProvider.of(new ResourceLocation(Yuushya.MOD_ID,"test"));
        yuushyaDataProvider.type(YuushyaDataProvider.DataType.BlockState).json(BlockStateData.genSimpleBlock(BLOCKS.get("test").get(),new ResourceLocation(Yuushya.MOD_ID,"block/test"))).save();
        yuushyaDataProvider.type(YuushyaDataProvider.DataType.BlockModel).json(ModelData.genSimpleCubeBlockModel(new ResourceLocation(Yuushya.MOD_ID,"block/test"))).save();
        SHOW_BLOCK= BLOCKS.register("showblock",()->new ShowBlock(BlockBehaviour.Properties.of(Material.METAL).noCollission().noOcclusion().strength(4.0f),1));
        ITEMS.register("showblock",()->new BlockItem(BLOCKS.get("showblock").get(),new Item.Properties().tab(YuushyaCreativeModeTab.YUUSHYA_ITEM)));
        SHOW_BLOCK_ENTITY= BLOCK_ENTITIES.register("showblockentity",()->BlockEntityType.Builder.of(ShowBlockEntity::new,BLOCKS.get("showblock").get()).build(null));//Util.fetchChoiceType(References.BLOCK_ENTITY,"yuushya:showblockentity")
    }



    public static RegistrySupplier<Block> SHOW_BLOCK = null;
    public static RegistrySupplier<BlockEntityType<?>> SHOW_BLOCK_ENTITY = null;


}
