package com.yuushya.item;

import com.yuushya.registries.YuushyaCreativeModeTab;
import com.yuushya.registries.YuushyaRegistryData;
import com.yuushya.utils.YuushyaUtils;
import net.minecraft.world.item.Item;

public class YuushyaItemFactory {
    public static AbstractYuushyaItem create(YuushyaRegistryData.Item item){
        return switch (item.classType){
            default -> new AbstractYuushyaItem(getItemProperties(item),item.properties.lines);
        };
    }
    public static Item.Properties getItemProperties(YuushyaRegistryData.Item item){
        Item.Properties properties=new Item.Properties()
                .tab(YuushyaCreativeModeTab.toGroup(item.itemGroup))
                .stacksTo(item.properties.maxCout);
        if (item.properties.rarity!=null&&!item.properties.rarity.isEmpty()) properties=properties.rarity(YuushyaUtils.toRarity(item.properties.rarity));
        if (item.properties.maxDamage!=0) properties=properties.durability(item.properties.maxDamage);
        if (item.properties.fireProof) properties=properties.fireResistant();
        return properties;
    }
}
