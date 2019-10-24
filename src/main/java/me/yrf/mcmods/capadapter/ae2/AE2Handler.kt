/*
 * This program is free software. It comes without any warranty, to
 * the extent permitted by applicable law. You can redistribute it
 * and/or modify it under the terms of the Do What The Fuck You Want
 * To Public License, Version 2, as published by Sam Hocevar. See
 * http://www.wtfpl.net/ for more details.
 */

package me.yrf.mcmods.capadapter.ae2

import me.yrf.mcmods.capadapter.blocks.BlockAECapAdapter
import me.yrf.mcmods.capadapter.capabilities.Capabilities
import me.yrf.mcmods.capadapter.capabilities.IAEGridProxyCapability
import me.yrf.mcmods.capadapter.items.ItemAECapAdapter
import me.yrf.mcmods.capadapter.items.ItemAECapAdapterPart
import me.yrf.mcmods.capadapter.modResource
import me.yrf.mcmods.capadapter.parts.models.PartAECapAdapterModel
import me.yrf.mcmods.capadapter.tiles.TileAECapAdapter
import net.minecraft.block.Block
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.item.Item
import net.minecraftforge.client.event.ModelRegistryEvent
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.common.capabilities.CapabilityManager
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

object AE2Handler {
    private val blocks: List<Block> = listOf(
            BlockAECapAdapter
    )

    private val items: List<Item> = listOf(
            ItemAECapAdapter,
            ItemAECapAdapterPart
    )

    @Suppress("UNUSED_PARAMETER")
    fun preinit(event: FMLPreInitializationEvent) {
        CapabilityManager.INSTANCE.register(IAEGridProxyCapability::class.java, Capabilities.NoOpStorage.type()) { null }
        GameRegistry.registerTileEntity(TileAECapAdapter::class.java, modResource("tileaecapabilityadapter"))
    }

    @Suppress("UNUSED_PARAMETER")
    @SubscribeEvent
    fun registerBlocks(evt: RegistryEvent.Register<Block>) {
        blocks.forEach { evt.registry.register(it) }
    }

    @Suppress("UNUSED_PARAMETER")
    @SubscribeEvent
    fun registerItems(evt: RegistryEvent.Register<Item>) {
        items.forEach { evt.registry.register(it) }
    }

    @Suppress("UNUSED_PARAMETER")
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    fun registerModels(evt: ModelRegistryEvent) {
        ModelLoader.setCustomModelResourceLocation(ItemAECapAdapter, 0,
                ModelResourceLocation(modResource("aecapabilityadapter"), "inventory"))
        ModelLoader.setCustomModelResourceLocation(ItemAECapAdapterPart, 0,
                ModelResourceLocation(modResource("parts/aecapabilityadapter"), "inventory"))
        AE2Plugin.api.registries().partModels().registerModels(PartAECapAdapterModel.models)
    }
}