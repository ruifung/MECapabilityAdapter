/*
 * This program is free software. It comes without any warranty, to
 * the extent permitted by applicable law. You can redistribute it
 * and/or modify it under the terms of the Do What The Fuck You Want
 * To Public License, Version 2, as published by Sam Hocevar. See
 * http://www.wtfpl.net/ for more details.
 */

package me.yrf.mcmods.capadapter.tiles

import appeng.api.exceptions.FailedConnectionException
import appeng.api.networking.GridFlags
import appeng.api.networking.IGridConnection
import appeng.api.networking.IGridHost
import appeng.api.networking.IGridNode
import appeng.api.networking.ticking.IGridTickable
import appeng.api.networking.ticking.TickRateModulation
import appeng.api.networking.ticking.TickingRequest
import appeng.api.util.AECableType
import appeng.api.util.AEPartLocation
import appeng.api.util.DimensionalCoord
import me.yrf.mcmods.capadapter.CapabilityAdapter
import me.yrf.mcmods.capadapter.ae2.AE2Plugin
import me.yrf.mcmods.capadapter.ae2.AEGridBlock
import me.yrf.mcmods.capadapter.ae2.ILocationAwareGridHost
import me.yrf.mcmods.capadapter.ae2.grid
import me.yrf.mcmods.capadapter.capabilities.IAEGridProxyCapability
import me.yrf.mcmods.capadapter.items.ItemAECapAdapter
import me.yrf.mcmods.capadapter.util.Platform
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.CapabilityInject
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class TileAECapAdapter : TileEntity(), ILocationAwareGridHost, IGridTickable {

    private val gridBlock = AEGridBlock(this,
            ItemStack(ItemAECapAdapter),
            true,
            1.0,
            EnumSet.of(GridFlags.DENSE_CAPACITY, GridFlags.PREFERRED))
    private val linkGridBlock = AEGridBlock(this,
            null,
            false,
            0.0,
            EnumSet.of(GridFlags.DENSE_CAPACITY, GridFlags.PREFERRED))
    private val node: IGridNode by lazy {
        val node = AE2Plugin.api.grid.createGridNode(gridBlock)
        if (::nbt.isInitialized)
            node.loadFromNBT("extNode", nbt)
        node
    }
    private val remoteLinkNode: IGridNode by lazy {
        val node = AE2Plugin.api.grid.createGridNode(linkGridBlock)
        if (::nbt.isInitialized)
            node.loadFromNBT("intNode", nbt)
        node
    }
    var intExtConnection: IGridConnection? = null

    lateinit var nbt: NBTTagCompound
    private val cap = IAEGridProxyCapability { this.remoteLinkNode }

    private val capabilityNodes = ConcurrentHashMap<IGridNode, IGridConnection>()

    override val location: DimensionalCoord
        get() = DimensionalCoord(this)

    private val searchSet = HashSet<IGridNode>(6) //So it isn't constantly reallocated.
    private var initialNotify = false

    override fun hasCapability(capability: Capability<*>, facing: EnumFacing?): Boolean {
        return capability === GridProxyCapability || super.hasCapability(capability, facing)
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> getCapability(capability: Capability<T>, facing: EnumFacing?): T? {
        return if (capability === GridProxyCapability) cap as T else super.getCapability(capability, facing)
    }

    fun setPlayer(player: EntityPlayer?) {
        if (player == null)
            return
        if (Platform.isServer) {
            val id = AE2Plugin.api.registries().players().getID(player)
            node.playerID = id
            remoteLinkNode.playerID = id
            markDirty()
        }
    }

    fun onBlockUpdate() {
        if (Platform.isServer) {
            updateConnectedNodes()
        }
    }

    override fun invalidate() {
        super.invalidate()
        if (Platform.isServer) {
            this.intExtConnection = null
            this.node.destroy()
            this.remoteLinkNode.destroy()
        }
    }

    private fun connectIntExtNodes() {
        intExtConnection = try {
            intExtConnection ?: AE2Plugin.api.grid.createGridConnection(this.node, this.remoteLinkNode)
        } catch (ex: FailedConnectionException) {
            securityBreak()
            null
        }
    }

    private fun updateConnectedNodes(): Boolean {
        connectIntExtNodes()

        var changes = false
        for (f in EnumFacing.VALUES) {
            val pos = getPos().offset(f)
            if (!world.isBlockLoaded(pos))
                continue
            val te = world.getTileEntity(pos)
            try {
                if (te != null && te.hasCapability(GridProxyCapability, f.opposite) && te !is IGridHost) {
                    val remoteCap = te.getCapability(GridProxyCapability, f.opposite)
                    if (remoteCap != null && remoteCap.proxiedObject != null && remoteCap.proxiedObject !== remoteLinkNode) {
                        searchSet.add(remoteCap.proxiedObject)
                    }
                }
            } catch (ex: Exception) {
                // Do nothing here. Likely CM hasn't loaded yet. This will be checked again when AE2 ticks it.
                CapabilityAdapter.logger.warn("NPE thrown while updating CapabilityAdapter connections.", ex)
                return false
            }
        }

        //Purge potentially stale nodes.
        if (!capabilityNodes.isEmpty()) {
            for (n in capabilityNodes.keys) {
                if (!searchSet.contains(n)) {
                    val conn = capabilityNodes.remove(n)
                    if (node.connections.contains(conn)) {
                        changes = true
                        conn?.destroy()
                    }
                }
            }
        }

        //Connect to new nodes
        val gh = AE2Plugin.api.grid()
        for (remoteNode in searchSet) {
            if (capabilityNodes.containsKey(remoteNode))
                continue
            try {
                val conn = gh.createGridConnection(remoteLinkNode, remoteNode)
                capabilityNodes[remoteNode] = conn
                changes = true
            } catch (e: FailedConnectionException) {
                //Do nothing! It's just failing to connect.
            }
        }

        if (changes) {
            this.remoteLinkNode.updateState()
            this.markDirty()
        }

        searchSet.clear()
        return true
    }

    override fun onChunkUnload() {
        if (Platform.isServer) {
            node.destroy()
            remoteLinkNode.destroy()
        }
        super.onChunkUnload()
    }

    override fun onLoad() {
        if (Platform.isServer) {
            connectIntExtNodes()
            node.updateState()
            remoteLinkNode.updateState()
            try {
                this.getWorld().notifyNeighborsOfStateChange(this.pos, this.blockType, false)
                this.initialNotify = true
            } catch (ex: Exception) {
                // Do nothing.
            }
        }
    }

    override fun getGridNode(dir: AEPartLocation): IGridNode? = if (Platform.isServer) {
        when (dir) {
            AEPartLocation.INTERNAL -> remoteLinkNode
            else -> node
        }
    } else null

    override fun getCableConnectionType(aePartLocation: AEPartLocation): AECableType {
        return AECableType.SMART
    }

    override fun securityBreak() {}

    override fun tickingRequest(node: IGridNode, ticksSinceLastCall: Int): TickRateModulation {
        if (node != this.node)
            return TickRateModulation.SAME

        if (!initialNotify) {
            try {
                this.getWorld().notifyNeighborsOfStateChange(this.pos, this.blockType, false)
                this.initialNotify = true
            } catch (ex: Exception) {
                CapabilityAdapter.logger.warn("Error doing initial notify.", ex)
                // Do nothing.
            }
        }

        return if (updateConnectedNodes()) TickRateModulation.IDLE else TickRateModulation.FASTER
    }

    override fun getTickingRequest(node: IGridNode): TickingRequest =
            TickingRequest(20, 60, false, false)

    override fun readFromNBT(compound: NBTTagCompound) {
        super.readFromNBT(compound)
        if (compound.hasKey("nodes"))
            this.nbt = compound.getCompoundTag("nodes")
    }

    override fun writeToNBT(compound: NBTTagCompound): NBTTagCompound {
        if (Platform.isServer) {
            val tag = NBTTagCompound()
            this.node.saveToNBT("extNode", tag)
            this.remoteLinkNode.saveToNBT("intNode", tag)
            compound.setTag("nodes", tag)
        }
        return super.writeToNBT(compound)
    }

    companion object {
        @CapabilityInject(IAEGridProxyCapability::class)
        lateinit var GridProxyCapability: Capability<IAEGridProxyCapability>
    }
}
