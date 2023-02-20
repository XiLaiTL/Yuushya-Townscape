package com.yuushya.forge;

import com.yuushya.*;
import com.yuushya.client.GetBlockStateItemForge;
import com.yuushya.client.ShowBlockModelForge;
import com.yuushya.items.ItemBlockEnchanted;
import com.yuushya.items.ItemWithCreativeTabBase;
import com.yuushya.mappings.BlockEntityMapper;
import com.yuushya.mappings.DeferredRegisterHolder;
import com.yuushya.mappings.ForgeUtilities;
import com.yuushya.mappings.RegistryUtilities;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
#if MC_VERSION >= "11902"
import net.minecraftforge.client.event.ModelEvent;
#else
import net.minecraftforge.client.event.ModelBakeEvent;
#endif
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(YuushyaMain.MOD_ID)
public class YuushyaMainForge {
    private static final DeferredRegisterHolder<Item> ITEMS = new DeferredRegisterHolder<>(YuushyaMain.MOD_ID, ForgeUtilities.registryGetItem());
    private static final DeferredRegisterHolder<Block> BLOCKS = new DeferredRegisterHolder<>(YuushyaMain.MOD_ID, ForgeUtilities.registryGetBlock());
    private static final DeferredRegisterHolder<BlockEntityType<?>> BLOCK_ENTITY_TYPES = new DeferredRegisterHolder<>(YuushyaMain.MOD_ID, ForgeUtilities.registryGetBlockEntityType());
    private static final DeferredRegisterHolder<EntityType<?>> ENTITY_TYPES = new DeferredRegisterHolder<>(YuushyaMain.MOD_ID, ForgeUtilities.registryGetEntityType());
    private static final DeferredRegisterHolder<SoundEvent> SOUND_EVENTS = new DeferredRegisterHolder<>(YuushyaMain.MOD_ID, ForgeUtilities.registryGetSoundEvent());

    static {
        YuushyaMain.init(YuushyaMainForge::registerItem, YuushyaMainForge::registerBlock, YuushyaMainForge::registerBlock, YuushyaMainForge::registerEnchantedBlock, YuushyaMainForge::registerBlockEntityType, YuushyaMainForge::registerEntityType, YuushyaMainForge::registerSoundEvent);
        RegistryObject<Item> GET_BLOCK_STATE_ITEM = new RegistryObject<>(GetBlockStateItemForge::new);
        registerItem("get_block_state_item", GET_BLOCK_STATE_ITEM);
    }

    public YuushyaMainForge() {
        final IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ForgeUtilities.registerModEventBus(YuushyaMain.MOD_ID, eventBus);

        ITEMS.register();
        BLOCKS.register();
        BLOCK_ENTITY_TYPES.register();
        ENTITY_TYPES.register();
        SOUND_EVENTS.register();

        eventBus.register(YuushyaForgeRegistry.class);
        eventBus.register(ForgeUtilities.RegisterCreativeTabs.class);
    }

    private static void registerItem(String path, RegistryObject<Item> item) {
        ITEMS.register(path, () -> {
            final Item itemObject = item.get();
            if (itemObject instanceof ItemWithCreativeTabBase) {
                Registry.registerCreativeModeTab(((ItemWithCreativeTabBase) itemObject).creativeModeTab.resourceLocation, itemObject);
            } else if (itemObject instanceof ItemWithCreativeTabBase.ItemPlaceOnWater) {
                Registry.registerCreativeModeTab(((ItemWithCreativeTabBase.ItemPlaceOnWater) itemObject).creativeModeTab.resourceLocation, itemObject);
            }
            return itemObject;
        });
    }

    private static void registerBlock(String path, RegistryObject<Block> block) {
        BLOCKS.register(path, block::get);
    }

    private static void registerBlock(String path, RegistryObject<Block> block, CreativeModeTabs.Wrapper creativeModeTabWrapper) {
        registerBlock(path, block);
        ITEMS.register(path, () -> {
            final BlockItem blockItem = new BlockItem(block.get(), RegistryUtilities.createItemProperties(creativeModeTabWrapper::get));
            Registry.registerCreativeModeTab(creativeModeTabWrapper.resourceLocation, blockItem);
            return blockItem;
        });
    }

    private static void registerEnchantedBlock(String path, RegistryObject<Block> block, CreativeModeTabs.Wrapper creativeModeTab) {
        registerBlock(path, block);
        ITEMS.register(path, () -> {
            final ItemBlockEnchanted itemBlockEnchanted = new ItemBlockEnchanted(block.get(), RegistryUtilities.createItemProperties(creativeModeTab::get));
            Registry.registerCreativeModeTab(creativeModeTab.resourceLocation, itemBlockEnchanted);
            return itemBlockEnchanted;
        });
    }

    private static void registerBlockEntityType(String path, RegistryObject<? extends BlockEntityType<? extends BlockEntityMapper>> blockEntityType) {
        BLOCK_ENTITY_TYPES.register(path, blockEntityType::get);
    }

    private static void registerEntityType(String path, RegistryObject<? extends EntityType<? extends Entity>> entityType) {
        ENTITY_TYPES.register(path, entityType::get);
    }

    private static void registerSoundEvent(String path, SoundEvent soundEvent) {
        SOUND_EVENTS.register(path, () -> soundEvent);
    }

    private static class YuushyaForgeRegistry {
        @SubscribeEvent
        public static void onClientSetupEvent(FMLClientSetupEvent event) {
            YuushyaMainClient.init();
            event.enqueueWork(YuushyaMainClient::initItemModelPredicate);
        }
        @SubscribeEvent
        #if MC_VERSION >= "11903"
        public static void onModelBaked(ModelEvent.ModifyBakingResult event){
            for(BlockState blockState : Blocks.SHOW_BLOCK.get().getStateDefinition().getPossibleStates()) {
                event.getModels().put(BlockModelShaper.stateToModelLocation(blockState), new ShowBlockModelForge());
            }
        }
        #elif MC_VERSION >= "11902"
        public static void onModelBaked(ModelEvent.BakingCompleted event){
            for(BlockState blockState : Blocks.SHOW_BLOCK.get().getStateDefinition().getPossibleStates()) {
                event.getModels().put(BlockModelShaper.stateToModelLocation(blockState), new ShowBlockModelForge());
            }
        }
        #else
        public static void onModelBaked(ModelBakeEvent event) {
            for (BlockState blockState : Blocks.SHOW_BLOCK.get().getStateDefinition().getPossibleStates()) {
                event.getModelRegistry().put(BlockModelShaper.stateToModelLocation(blockState), new ShowBlockModelForge());
            }
        }
        #endif
    }
}