package me.yrf.mcmods.capadapter;

import me.yrf.mcmods.capadapter.ae2.BlockAECapAdapter;
import me.yrf.mcmods.capadapter.ae2.ItemAECapAdapter;
import me.yrf.mcmods.capadapter.ae2.TileAECapAdapter;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

@GameRegistry.ObjectHolder(CapabilityAdapter.MODID)
public class RegistryHelper {

    static void registerBlocks(IForgeRegistry<Block> registry) {
    }

    static void registerItems(IForgeRegistry<Item> registry) {
    }

    static void registerTileEntities() {
    }

    @SideOnly(Side.CLIENT)
    static void registerModels(ModelRegistryEvent event) {
    }
}
