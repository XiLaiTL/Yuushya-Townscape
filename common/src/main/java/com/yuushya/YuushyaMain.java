package com.yuushya;

import com.yuushya.mappings.BlockEntityMapper;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.BiConsumer;

public class YuushyaMain {
    public static final String MOD_ID = "yuushya";

    public static void init(BiConsumer<String, RegistryObject<Item>> registerItem,
                            BiConsumer<String, RegistryObject<Block>> registerBlock,
                            RegisterBlockItem registerBlockItem,
                            RegisterBlockItem registerEnchantedBlockItem,
                            BiConsumer<String, RegistryObject<? extends BlockEntityType<? extends BlockEntityMapper>>> registerBlockEntityType,
                            BiConsumer<String, RegistryObject<? extends EntityType<? extends Entity>>> registerEntityType,
                            BiConsumer<String, SoundEvent> registerSoundEvent
    ) {
        registerItem.accept("get_show_block_item", Items.GET_SHOW_BLOCK_ENTITY_ITEM);
        registerItem.accept("micro_pos_trans_item", Items.MICRO_POS_TRANS_ITEM);
        registerItem.accept("pos_trans_item", Items.POS_TRANS_ITEM);
        registerItem.accept("rot_trans_item", Items.ROT_TRANS_ITEM);
        registerItem.accept("scale_trans_item", Items.SCALE_TRANS_ITEM);

        registerBlockItem.accept("show_block", Blocks.SHOW_BLOCK, CreativeModeTabs.TEST_TABS);

        registerBlockEntityType.accept("show_block", BlockEntityTypes.SHOW_BLOCK_ENTITY);
    }

    @FunctionalInterface
    public interface RegisterBlockItem {
        void accept(String string, RegistryObject<Block> block, CreativeModeTabs.Wrapper tab);
    }
}