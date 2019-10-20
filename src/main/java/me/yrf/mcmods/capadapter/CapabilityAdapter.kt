package me.yrf.mcmods.capadapter

import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.SidedProxy
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent

@Mod(modid = CapabilityAdapter.MODID, useMetadata = true,
        dependencies = "required:forgelin@[1.8.0,)",
        modLanguageAdapter = "net.shadowfacts.forgelin.KotlinAdapter")
object CapabilityAdapter {
    const val MODID = "rf-capability-adapter"

    @SidedProxy(modId = CapabilityAdapter.MODID,
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