package com.yuushya.item;

import com.mojang.datafixers.util.Pair;
import com.yuushya.registries.YuushyaCreativeModeTab;
import com.yuushya.registries.YuushyaRegistryData;
import com.yuushya.utils.YuushyaUtils;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.CustomData;

public class YuushyaItemFactory {
    public static AbstractYuushyaItem create(YuushyaRegistryData.Item item){
        return switch (item.classType){
            case "HatItem"->new HatItem(getItemProperties(item),item.properties.lines);
            case "StructureCreatorItem"->new StructureCreatorItem(getItemProperties(item), item.properties.lines,item.usage.createNbt,item.usage.cancelNbt);
            case "BlueprintItem" -> new BlueprintItem(getItemProperties(item),item.properties.lines);
            default -> new AbstractYuushyaItem(getItemProperties(item),item.properties.lines);
        };
    }
    public static Item.Properties getItemProperties(YuushyaRegistryData.Item item){
        Item.Properties properties=new Item.Properties()
                .arch$tab(YuushyaCreativeModeTab.toGroup(item.itemGroup))
                .stacksTo(item.properties.maxCount);
        if (item.properties.rarity!=null&&!item.properties.rarity.isEmpty()) properties=properties.rarity(YuushyaUtils.toRarity(item.properties.rarity));
        if (item.properties.maxDamage!=0) properties=properties.durability(item.properties.maxDamage);
        if (item.properties.fireProof) properties=properties.fireResistant();
        return properties;
    }
}
