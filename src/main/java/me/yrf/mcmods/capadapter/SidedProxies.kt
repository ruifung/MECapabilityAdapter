/*
 * This program is free software. It comes without any warranty, to
 * the extent permitted by applicable law. You can redistribute it
 * and/or modify it under the terms of the Do What The Fuck You Want
 * To Public License, Version 2, as published by Sam Hocevar. See
 * http://www.wtfpl.net/ for more details.
 */

package me.yrf.mcmods.capadapter

import me.yrf.mcmods.capadapter.capabilities.Capabilities
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent

open class CommonProxy {
    open fun preinit(event: FMLPreInitializationEvent) {
        MinecraftForge.EVENT_BUS.register(this)
        Capabilities.register()
        Integrations.preinit(event)
    }

    open fun init(event: FMLInitializationEvent) {}
    open fun postinit(event: FMLPostInitializationEvent) {}
}

@Suppress("CanSealedSubClassBeObject")
class ClientProxy : CommonProxy()