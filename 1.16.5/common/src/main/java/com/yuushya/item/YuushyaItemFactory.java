package com.yuushya.item;

import com.yuushya.registries.YuushyaCreativeModeTab;
import com.yuushya.registries.YuushyaRegistryData;
import com.yuushya.utils.YuushyaUtils;
import net.minecraft.world.item.Item;

public class YuushyaItemFactory {
    public static AbstractYuushyaItem create(YuushyaRegistryData.Item item){
        switch (item.classType) {
            case "HatItem":
                return new HatItem(getItemProperties(item), item.properties.lines);
            case "StructureCreatorItem":
                return new StructureCreatorItem(getItemProperties(item), item.properties.lines, item.usage.createNbt, item.usage.cancelNbt);
            case "BlueprintItem":
                return new BlueprintItem(getItemProperties(item), item.properties.lines);
            default:
                return new AbstractYuushyaItem(getItemProperties(item), item.properties.lines);
        }
    }
    public static Item.Properties getItemProperties(YuushyaRegistryData.Item item){
        Item.Properties properties=new Item.Properties()
                .tab(YuushyaCreativeModeTab.toGroup(item.itemGroup))
                .stacksTo(item.properties.maxCount);
        if (item.properties.rarity!=null&&!item.properties.rarity.isEmpty()) properties=properties.rarity(YuushyaUtils.toRarity(item.properties.rarity));
        if (item.properties.maxDamage!=0) properties=properties.durability(item.properties.maxDamage);
        if (item.properties.fireProof) properties=properties.fireResistant();
        return properties;
    }
}
