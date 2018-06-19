package me.yrf.mcmods.capadapter;

import me.yrf.mcmods.capadapter.ae2.AE2Adapter;
import me.yrf.mcmods.capadapter.capabilities.Capabilities;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = CapabilityAdapter.MODID, version = CapabilityAdapter.VERSION)
public class CapabilityAdapter {
    public static final String MODID = "rf-capability-adapter";
    public static final String VERSION = "1.0";

    @SidedProxy(modId = MODID,
            clientSide = "me.yrf.mcmods.capadapter.CapabilityAdapter$ClientProxy",
            serverSide = "me.yrf.mcmods.capadapter.CapabilityAdapter$ServerProxy")
    public static SidedModProxy proxy;

    @EventHandler
    void preinit(FMLPreInitializationEvent event) {
        proxy.preinit(event);
    }
    @EventHandler
    void init(FMLInitializationEvent event) {
        proxy.init(event);
    }
    @EventHandler
    void postinit(FMLPostInitializationEvent event){
        proxy.postinit(event);
    }

    public interface SidedModProxy {
        void preinit(FMLPreInitializationEvent event);
        void init(FMLInitializationEvent event);
        void postinit(FMLPostInitializationEvent event);
    }

    public static abstract class CommonProxy implements SidedModProxy {

        @Override
        public void preinit(FMLPreInitializationEvent event) {
            MinecraftForge.EVENT_BUS.register(this);
            event.getModMetadata().version = VERSION;
            RegistryHelper.registerTileEntities();
            Capabilities.register();

            if (Loader.isModLoaded("appliedenergistics2")) {
                AE2Adapter.INSTANCE.preinit(event);
                MinecraftForge.EVENT_BUS.register(AE2Adapter.INSTANCE);
            }

        }

        @Override
        public void init(FMLInitializationEvent event) {

        }

        @Override
        public void postinit(FMLPostInitializationEvent event) {

        }

        @SubscribeEvent
        public void registerBlocks(RegistryEvent.Register<Block> event) {
            RegistryHelper.registerBlocks(event.getRegistry());
        }

        @SubscribeEvent
        public void registerItems(RegistryEvent.Register<Item> event) {
            RegistryHelper.registerItems(event.getRegistry());
        }
    }

    public static class ServerProxy extends CommonProxy {

    }

    public static class ClientProxy extends CommonProxy {
        @SubscribeEvent
        public void registerModels(ModelRegistryEvent event) {
            RegistryHelper.registerModels(event);
        }
    }

}
