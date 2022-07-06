package com.yuushya.registries;

import com.yuushya.Yuushya;
import com.yuushya.block.BlockStateTest;
import com.yuushya.blockentity.showblock.ShowBlock;
import com.yuushya.blockentity.showblock.ShowBlockEntity;
import com.yuushya.item.showblocktool.*;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;


public class YuushyaRegistries {
    public static final YuushyaDeferredRegister<Block> BLOCKS = new YuushyaDeferredRegister<>(Registry.BLOCK_REGISTRY);
    public static final YuushyaDeferredRegister<Item> ITEMS = new YuushyaDeferredRegister<>(Registry.ITEM_REGISTRY);
    public static final YuushyaDeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = new YuushyaDeferredRegister<>(Registry.BLOCK_ENTITY_TYPE_REGISTRY);

    public static void registerAll(){
        ITEMS.register("example_item",()->new Item(new Item.Properties().tab(Yuushya.EXAMPLE_TAB)));
//        ITEMS.register("get_blockstate_item", () -> new GetBlockStateItem(new Item.Properties().tab(Yuushya.EXAMPLE_TAB), 1));
        ITEMS.register("pos_trans_item",()->new PosTransItem(new Item.Properties().tab(Yuushya.EXAMPLE_TAB),4));
        ITEMS.register("micro_pos_trans_item",()->new MicroPosTransItem(new Item.Properties().tab(Yuushya.EXAMPLE_TAB),4));
        ITEMS.register("rot_trans_item",()->new RotTransItem(new Item.Properties().tab(Yuushya.EXAMPLE_TAB),4));
        ITEMS.register("scale_trans_item",()->new ScaleTransItem(new Item.Properties().tab(Yuushya.EXAMPLE_TAB),4));
        ITEMS.register("slot_trans_item",()->new SlotTransItem(new Item.Properties().tab(Yuushya.EXAMPLE_TAB),4));
        ITEMS.register("get_showblock_item",()->new GetShowBlockEntityItem(new Item.Properties().tab(Yuushya.EXAMPLE_TAB),4));
        ITEMS.register("move_transformdata_item",()->new MoveTransformDataItem(new Item.Properties().tab(Yuushya.EXAMPLE_TAB),4));

        BLOCKS.register("test",()->new BlockStateTest(BlockBehaviour.Properties.of(Material.METAL),1).create());
        ITEMS.register("test",()->new BlockItem(BLOCKS.get("test").get(),new Item.Properties().tab(Yuushya.EXAMPLE_TAB)));
        SHOW_BLOCK= BLOCKS.register("showblock",()->new ShowBlock(BlockBehaviour.Properties.of(Material.METAL).noCollission().noOcclusion().strength(4.0f),1));
        ITEMS.register("showblock",()->new BlockItem(BLOCKS.get("showblock").get(),new Item.Properties().tab(Yuushya.EXAMPLE_TAB)));
        SHOW_BLOCK_ENTITY= BLOCK_ENTITIES.register("showblockentity",()->BlockEntityType.Builder.of(ShowBlockEntity::new,BLOCKS.get("showblock").get()).build(null));//Util.fetchChoiceType(References.BLOCK_ENTITY,"yuushya:showblockentity")
    }

    public static RegistrySupplier<Block> SHOW_BLOCK = null;
    public static RegistrySupplier<BlockEntityType<?>> SHOW_BLOCK_ENTITY = null;


}
