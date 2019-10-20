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
