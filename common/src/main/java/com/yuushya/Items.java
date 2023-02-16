package com.yuushya;

import com.yuushya.items.showblocktools.*;
import net.minecraft.world.item.Item;

public interface Items {
    RegistryObject<Item> GET_BLOCK_STATE_ITEM = new RegistryObject<>(() -> new GetBlockStateItem(properties -> properties.stacksTo(1)));
    RegistryObject<Item> GET_SHOW_BLOCK_ENTITY_ITEM = new RegistryObject<>(GetShowBlockEntityItem::new);
    RegistryObject<Item> MICRO_POS_TRANS_ITEM = new RegistryObject<>(MicroPosTransItem::new);
    RegistryObject<Item> POS_TRANS_ITEM = new RegistryObject<>(PosTransItem::new);
    RegistryObject<Item> ROT_TRANS_ITEM = new RegistryObject<>(RotTransItem::new);
    RegistryObject<Item> SCALE_TRANS_ITEM = new RegistryObject<>(ScaleTransItem::new);
}