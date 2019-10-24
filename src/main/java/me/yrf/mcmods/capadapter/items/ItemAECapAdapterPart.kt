/*
 * This program is free software. It comes without any warranty, to
 * the extent permitted by applicable law. You can redistribute it
 * and/or modify it under the terms of the Do What The Fuck You Want
 * To Public License, Version 2, as published by Sam Hocevar. See
 * http://www.wtfpl.net/ for more details.
 */

package me.yrf.mcmods.capadapter.items

import appeng.api.parts.IPartItem
import me.yrf.mcmods.capadapter.ae2.AE2Plugin
import me.yrf.mcmods.capadapter.ae2.partHelper
import me.yrf.mcmods.capadapter.modResource
import me.yrf.mcmods.capadapter.parts.PartAECapAdapter
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

object ItemAECapAdapterPart : Item(), IPartItem<PartAECapAdapter> {
    init {
        creativeTab = CreativeTabs.MISC
        translationKey = "aecapabilityadapter"
        setNoRepair()
        setHasSubtypes(false)
        registryName = modResource("aecapabilityadapter_part")
        maxDamage = 0
        maxStackSize = 64
    }


    override fun createPartFromItemStack(`is`: ItemStack): PartAECapAdapter? {
        return PartAECapAdapter()
    }


    override fun onItemUse(player: EntityPlayer, worldIn: World, pos: BlockPos, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult {
        return AE2Plugin.api.partHelper.placeBus(player.getHeldItem(hand), pos, facing, player, hand, worldIn)
    }
}