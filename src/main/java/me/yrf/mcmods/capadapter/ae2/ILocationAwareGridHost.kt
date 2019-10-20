package me.yrf.mcmods.capadapter.ae2

import appeng.api.networking.IGridHost
import appeng.api.util.DimensionalCoord

interface ILocationAwareGridHost : IGridHost {
    val location: DimensionalCoord
}