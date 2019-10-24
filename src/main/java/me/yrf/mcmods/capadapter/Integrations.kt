/*
 * This program is free software. It comes without any warranty, to
 * the extent permitted by applicable law. You can redistribute it
 * and/or modify it under the terms of the Do What The Fuck You Want
 * To Public License, Version 2, as published by Sam Hocevar. See
 * http://www.wtfpl.net/ for more details.
 */

package me.yrf.mcmods.capadapter

import me.yrf.mcmods.capadapter.ae2.AE2Handler
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Loader
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent

object Integrations {
    fun preinit(event: FMLPreInitializationEvent) {
        //AE2
        if (Loader.isModLoaded("appliedenergistics2")) {
            AE2Handler.preinit(event)
            MinecraftForge.EVENT_BUS.register(AE2Handler)
        }
    }
}