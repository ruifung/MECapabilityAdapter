/*
 * This program is free software. It comes without any warranty, to
 * the extent permitted by applicable law. You can redistribute it
 * and/or modify it under the terms of the Do What The Fuck You Want
 * To Public License, Version 2, as published by Sam Hocevar. See
 * http://www.wtfpl.net/ for more details.
 */

package me.yrf.mcmods.capadapter.util

import net.minecraftforge.fml.common.FMLCommonHandler

object Platform {
    val isServer: Boolean
        get() = FMLCommonHandler.instance().effectiveSide.isServer

    val isClient: Boolean
        get() = FMLCommonHandler.instance().effectiveSide.isClient
}