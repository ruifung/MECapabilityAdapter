package me.yrf.mcmods.capadapter.util

import net.minecraftforge.fml.common.FMLCommonHandler

object Platform {
    val isServer: Boolean
        get() = FMLCommonHandler.instance().effectiveSide.isServer

    val isClient: Boolean
        get() = FMLCommonHandler.instance().effectiveSide.isClient
}