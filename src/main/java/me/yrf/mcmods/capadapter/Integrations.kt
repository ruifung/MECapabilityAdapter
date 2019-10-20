package me.yrf.mcmods.capadapter

import me.yrf.mcmods.capadapter.ae2.AE2Handler
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Loader
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent

object Integrations {
    fun preinit(event: FMLPreInitializationEvent) {
        //AE2
        if(Loader.isModLoaded("appliedenergistics2")) {
            AE2Handler.preinit(event)
            MinecraftForge.EVENT_BUS.register(AE2Handler)
        }
    }
}