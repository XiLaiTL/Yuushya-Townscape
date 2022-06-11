package net.cocofish.yuushya.registries;

import com.google.common.base.Suppliers;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.Registries;
import dev.architectury.registry.registries.RegistrySupplier;
import net.cocofish.yuushya.Yuushya;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static net.cocofish.yuushya.Yuushya.MOD_ID;

public class YuushyaDeferredRegister <T>{
    private final Map<String, RegistrySupplier<T>> OBJECT_MAP = new HashMap<>();

    private static final Supplier<Registries> REGISTRIES = Suppliers.memoize(() -> Registries.get(MOD_ID));
    private final Registrar<T>  REGISTER;
//    private final DeferredRegister<T> REGISTER;

    public YuushyaDeferredRegister(ResourceKey<Registry<T>> key){
        REGISTER = REGISTRIES.get().get(key);
        //REGISTER =DeferredRegister.create(MOD_ID,key);
    }
    //set
    public <I extends T> RegistrySupplier<T> register(String name, Supplier<I> sup){
        RegistrySupplier<T> registryObject = REGISTER.register(new ResourceLocation(MOD_ID, name), (Supplier<T>) sup);
        OBJECT_MAP.put(name,registryObject);
        return registryObject;
    }

    //get
    @Nullable
    public RegistrySupplier<T> get(String name){return OBJECT_MAP.get(name);}
    @NotNull
    public T getInstanceOrDefault(String name,T defaultObject){
        RegistrySupplier<T> registryObject = get(name);
        if(registryObject==null){ return defaultObject;}
        else return registryObject.get();
    }
}
