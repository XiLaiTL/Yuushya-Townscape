package net.cocofish.yuushya;

import net.fabricmc.fabric.api.item.v1.EquipmentSlotProvider;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Rarity;

public class YuushyaItem {
    public String classtype="";
    public String name="";
    public String itemgroup="";
    public int maxcout;
    public int maxdamage;
    public String rarity;
    public String equipment;
    public boolean fireproof;
    public int lines;
    public FabricItemSettings settings;

    public String createNbt="";
    public String cancelNbt="";
    YuushyaItem() {
        classtype = "";
        name = "";
        itemgroup = "";
        maxcout=64;
        maxdamage=0;
        lines=1;
        rarity="";
        equipment="";
        fireproof=false;
        settings=new FabricItemSettings();
        createNbt="";
        cancelNbt="";
    }
    public void constructsetting()
    {
        if(fireproof==true) settings=settings.fireproof();
        settings=settings.maxCount(maxcout);
        if(maxdamage!=0)settings=settings.maxDamageIfAbsent(maxdamage);
        if(!rarity.equals("")) settings=settings.rarity(toRarity());
        if(!equipment.equals("")) settings=settings.equipmentSlot(toEquipmentSlotProvider());
        settings=settings.group(toGroup(itemgroup));
    }
    public Rarity toRarity()
    {
        switch (rarity)
        {
            case "common":return Rarity.COMMON;
            case "uncommon":return Rarity.UNCOMMON;
            case "rare":return Rarity.RARE;
            case "epic":return Rarity.EPIC;
            default:return Rarity.COMMON;
        }
    }
    public EquipmentSlotProvider toEquipmentSlotProvider()
    {
        switch (equipment)
        {
            case "head":return (itemStack)-> EquipmentSlot.HEAD;
            case "chest":return (itemStack)-> EquipmentSlot.CHEST;
            case "feet":return (itemStack)-> EquipmentSlot.FEET;
            case "legs":return (itemStack)-> EquipmentSlot.LEGS;
            default: return (itemStack)-> EquipmentSlot.HEAD;
        }
    }
    public ItemGroup toGroup(String itemgroup)
    {
        switch (itemgroup)
        {
            case "yuushya_extrablocks":
                return Yuushya.yuushyaextrablocks;
            case "yuushya_furniture":
                return Yuushya.yuushyafurniture;
            case "yuushya_decoration":
                return Yuushya.yuushyadecoration;
            case "yuushya_signs":
                return Yuushya.yuushyasigns;
            case "yuushya_life":
                return Yuushya.yuushyalife;
            case "yuushya_extrashapes":
                return Yuushya.yuushyaextrashapes;
            case "yuushya_infrastructure":
                return Yuushya.yuushyainfrastructure;
            case "yuushya_item":
                return Yuushya.yuushyaitem;
            case "yuushya_structure":
                return Yuushya.yuushyastructure;
            default:
                return Yuushya.yuushyaitem;
        }
    }
}
