package me.yrf.mcmods.capadapter

import me.yrf.mcmods.capadapter.capabilities.Capabilities
import me.yrf.mcmods.capadapter.ae2.AE2Handler
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Loader
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
class ClientProxy : CommonProxy() {

}