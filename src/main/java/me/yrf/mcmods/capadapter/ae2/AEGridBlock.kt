/*
 * This program is free software. It comes without any warranty, to
 * the extent permitted by applicable law. You can redistribute it
 * and/or modify it under the terms of the Do What The Fuck You Want
 * To Public License, Version 2, as published by Sam Hocevar. See
 * http://www.wtfpl.net/ for more details.
 */

package me.yrf.mcmods.capadapter.ae2

import appeng.api.networking.*
import appeng.api.util.AEColor
import appeng.api.util.DimensionalCoord
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing
import java.util.*

class AEGridBlock(private val host: ILocationAwareGridHost,
                  private val display: ItemStack?,
                  private val worldAccessible: Boolean,
                  private val idlePowerUsage: Double,
                  private val gridFlags: EnumSet<GridFlags> = EnumSet.noneOf(GridFlags::class.java)) : IGridBlock {

    override fun getIdlePowerUsage(): Double {
        return 1.0
    }

    override fun getFlags(): EnumSet<GridFlags> {
        return gridFlags
    }

    override fun isWorldAccessible(): Boolean {
        return worldAccessible
    }

    override fun getLocation(): DimensionalCoord {
        return this.host.location
    }

    override fun getGridColor(): AEColor {
        return AEColor.TRANSPARENT
    }

    override fun onGridNotification(gridNotification: GridNotification) {
        //Nothing to do here.
    }

    override fun setNetworkStatus(iGrid: IGrid, i: Int) {
        //Do nothing
    }

    override fun getConnectableSides(): EnumSet<EnumFacing> {
        return EnumSet.allOf(EnumFacing::class.java)
    }

    override fun getMachine(): IGridHost {
        return host
    }

    override fun gridChanged() {

    }

    override fun getMachineRepresentation(): ItemStack {
        return display ?: ItemStack.EMPTY
    }
}
