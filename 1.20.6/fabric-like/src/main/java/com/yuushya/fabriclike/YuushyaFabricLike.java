package com.yuushya.fabriclike;

import com.mojang.serialization.Codec;
import com.yuushya.Yuushya;
import com.yuushya.item.data_component.Structure;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.component.CustomData;

import static com.yuushya.registries.YuushyaRegistries.STRUCTURE;
import static com.yuushya.registries.YuushyaRegistries.TRANS_DIRECTION;

public class YuushyaFabricLike {
    public static void init(){
        Yuushya.init();
        //STRUCTURE = ()-> Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, new ResourceLocation(Yuushya.MOD_ID,"structure"),DataComponentType.<Structure>builder().persistent(Structure.CODEC).networkSynchronized(Structure.STREAM_CODEC).build());
        //TRANS_DIRECTION = ()-> Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE,new ResourceLocation(Yuushya.MOD_ID,"mode"), DataComponentType.<Integer>builder().persistent(ExtraCodecs.NON_NEGATIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT).build());
        //ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, resourceManager, success) -> CollisionFileReader.readAllFileSelf());
    }
}
