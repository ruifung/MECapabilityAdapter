package me.yrf.mcmods.capadapter.ae2;

import me.yrf.mcmods.capadapter.CapabilityAdapter;
import me.yrf.mcmods.capadapter.capabilities.Capabilities;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@GameRegistry.ObjectHolder(CapabilityAdapter.MODID)
public class AE2Adapter {
    @GameRegistry.ObjectHolder("aecapabilityadapter")
    private static BlockAECapAdapter capabilityAdapter;
    public static AE2Adapter INSTANCE = new AE2Adapter();

    private AE2Adapter() { }

    public void preinit(FMLPreInitializationEvent event) {
        CapabilityManager.INSTANCE.register(IAEGridProxyCapability.class, Capabilities.noOpStorage(), () -> null);
        GameRegistry.registerTileEntity(TileAECapAdapter.class, CapabilityAdapter.MODID + ":tileaecapabilityadapter");
    }

    @SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Block> evt) {
        evt.getRegistry().register(new BlockAECapAdapter("aecapabilityadapter"));
    }

    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> evt) {
        evt.getRegistry().register(new ItemAECapAdapter("aecapabilityadapter", capabilityAdapter));
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void registerModels(ModelRegistryEvent evt) {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(capabilityAdapter), 0,
                new ModelResourceLocation(CapabilityAdapter.MODID + ":aecapabilityadapter", "inventory"));
    }
}
