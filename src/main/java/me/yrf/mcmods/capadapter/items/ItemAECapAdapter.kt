/*
 * This program is free software. It comes without any warranty, to
 * the extent permitted by applicable law. You can redistribute it
 * and/or modify it under the terms of the Do What The Fuck You Want
 * To Public License, Version 2, as published by Sam Hocevar. See
 * http://www.wtfpl.net/ for more details.
 */

package me.yrf.mcmods.capadapter.items

import me.yrf.mcmods.capadapter.CapabilityAdapter
import me.yrf.mcmods.capadapter.blocks.BlockAECapAdapter
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.ItemBlock

object ItemAECapAdapter : ItemBlock(BlockAECapAdapter) {
    init {
        creativeTab = CreativeTabs.MISC
        translationKey = "aecapabilityadapter"
        setNoRepair()
        setHasSubtypes(false)
        setRegistryName(CapabilityAdapter.MODID, "aecapabilityadapter")
    }
}
