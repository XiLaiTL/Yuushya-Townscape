package net.cocofish.yuushya.registries;

import com.google.common.base.Suppliers;
import dev.architectury.injectables.annotations.PlatformOnly;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.Registries;
import dev.architectury.registry.registries.RegistrySupplier;
import net.cocofish.yuushya.Yuushya;
import net.cocofish.yuushya.blockentity.showblock.ShowBlock;
import net.cocofish.yuushya.blockentity.showblock.ShowBlockEntity;
import net.cocofish.yuushya.item.showblocktool.*;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

import java.util.function.Supplier;

import static net.cocofish.yuushya.Yuushya.MOD_ID;


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


        SHOW_BLOCK= BLOCKS.register("showblock",()->new ShowBlock(BlockBehaviour.Properties.of(Material.METAL).noCollission().noOcclusion().strength(4.0f),1));
        ITEMS.register("showblock",()->new BlockItem(BLOCKS.get("showblock").get(),new Item.Properties().tab(Yuushya.EXAMPLE_TAB)));
        SHOW_BLOCK_ENTITY= BLOCK_ENTITIES.register("showblockentity",()->BlockEntityType.Builder.of(ShowBlockEntity::new,BLOCKS.get("showblock").get()).build(null));//Util.fetchChoiceType(References.BLOCK_ENTITY,"yuushya:showblockentity")
    }

    public static RegistrySupplier<Block> SHOW_BLOCK = null;
    public static RegistrySupplier<BlockEntityType<?>> SHOW_BLOCK_ENTITY = null;


}
