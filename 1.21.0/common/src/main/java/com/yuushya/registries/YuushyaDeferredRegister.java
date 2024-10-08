package com.yuushya.registries;

import com.google.common.base.Suppliers;
import com.yuushya.Yuushya;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrarManager;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class YuushyaDeferredRegister <T>{
    private final Map<String, RegistrySupplier<T>> OBJECT_MAP = new LinkedHashMap<>();

    private static final Supplier<RegistrarManager> REGISTRIES = Suppliers.memoize(() -> RegistrarManager.get(Yuushya.MOD_ID));
    private final Registrar<T>  REGISTER;
//    private final DeferredRegister<T> REGISTER;

    public YuushyaDeferredRegister(ResourceKey<Registry<T>> key){
        REGISTER = REGISTRIES.get().get(key);
        //REGISTER =DeferredRegister.create(MOD_ID,key);
    }
    //set
    public <I extends T> RegistrySupplier<T> register(String name, Supplier<I> sup){
        if(OBJECT_MAP.containsKey(name)) return OBJECT_MAP.get(name);
        RegistrySupplier<T> registryObject = REGISTER.register(ResourceLocation.fromNamespaceAndPath(Yuushya.MOD_ID, name), (Supplier<T>) sup);
        OBJECT_MAP.put(name,registryObject);
        return registryObject;
    }

    //get
    @Nullable
    public T getValue(String name){return OBJECT_MAP.get(name).get();}
    @Nullable
    public RegistrySupplier<T> get(String name){return OBJECT_MAP.get(name);}
    @NotNull
    public T getInstanceOrDefault(String name,T defaultObject){
        RegistrySupplier<T> registryObject = get(name);
        if(registryObject==null){ return defaultObject;}
        else return registryObject.get();
    }
}
