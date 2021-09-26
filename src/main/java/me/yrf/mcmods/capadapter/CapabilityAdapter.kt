/*
 * This program is free software. It comes without any warranty, to
 * the extent permitted by applicable law. You can redistribute it
 * and/or modify it under the terms of the Do What The Fuck You Want
 * To Public License, Version 2, as published by Sam Hocevar. See
 * http://www.wtfpl.net/ for more details.
 */

package me.yrf.mcmods.capadapter

import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.SidedProxy
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

@Mod(modid = CapabilityAdapter.MODID, useMetadata = true,
        dependencies = "required:forgelin@[1.8.0,)",
        modLanguageAdapter = "net.shadowfacts.forgelin.KotlinAdapter")
object CapabilityAdapter {
    const val MODID = "rf-capability-adapter"
    val logger: Logger = LogManager.getLogger(MODID)

    @SidedProxy(modId = MODID,
            clientSide = "me.yrf.mcmods.capadapter.ClientProxy",
            serverSide = "me.yrf.mcmods.capadapter.CommonProxy")
    lateinit var proxy: CommonProxy

    @Mod.EventHandler
    fun preinit(event: FMLPreInitializationEvent) {
        proxy.preinit(event)
    }

    @Mod.EventHandler
    fun init(event: FMLInitializationEvent) {
        proxy.init(event)
    }

    @Mod.EventHandler
    fun postinit(event: FMLPostInitializationEvent) {
        proxy.postinit(event)
    }
}

fun modResource(resource: String) = ResourceLocation(CapabilityAdapter.MODID, resource)