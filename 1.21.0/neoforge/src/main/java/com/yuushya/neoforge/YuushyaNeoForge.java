package com.yuushya.neoforge;

import com.yuushya.Yuushya;
import com.yuushya.item.data_component.Structure;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.util.ExtraCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.yuushya.registries.YuushyaRegistries.STRUCTURE;
import static com.yuushya.registries.YuushyaRegistries.TRANS_DIRECTION;

@Mod(Yuushya.MOD_ID)
public class YuushyaNeoForge {

    public YuushyaNeoForge(IEventBus modBus) {
        Yuushya.init();
    }

//    public static final DeferredRegister.DataComponents REGISTRAR = DeferredRegister.createDataComponents(Yuushya.MOD_ID);
//
//    private static final DeferredHolder<DataComponentType<?>, DataComponentType<Structure>> _STRUCTURE = REGISTRAR.registerComponentType(
//            "structure",
//            builder -> builder
//                    // The codec to read/write the data to disk
//                    .persistent(Structure.CODEC)
//                    // The codec to read/write the data across the network
//                    .networkSynchronized(Structure.STREAM_CODEC)
//    );
//
//    private static final DeferredHolder<DataComponentType<?>,DataComponentType<Integer>> _TRANS_DIRECTION = REGISTRAR.registerComponentType(
//            "mode", builder -> builder.persistent(ExtraCodecs.NON_NEGATIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT));
//
//    static {
//        STRUCTURE = (RegistrySupplier<DataComponentType<?>>) _STRUCTURE.getDelegate();
//        TRANS_DIRECTION = (RegistrySupplier<DataComponentType<?>>) _TRANS_DIRECTION.getDelegate();
//    }
}
