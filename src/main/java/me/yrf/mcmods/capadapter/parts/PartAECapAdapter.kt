/*
 * This program is free software. It comes without any warranty, to
 * the extent permitted by applicable law. You can redistribute it
 * and/or modify it under the terms of the Do What The Fuck You Want
 * To Public License, Version 2, as published by Sam Hocevar. See
 * http://www.wtfpl.net/ for more details.
 */

package me.yrf.mcmods.capadapter.parts

import appeng.api.exceptions.FailedConnectionException
import appeng.api.networking.GridFlags
import appeng.api.networking.IGridConnection
import appeng.api.networking.IGridHost
import appeng.api.networking.IGridNode
import appeng.api.networking.ticking.IGridTickable
import appeng.api.networking.ticking.TickRateModulation
import appeng.api.networking.ticking.TickingRequest
import appeng.api.parts.*
import appeng.api.util.AECableType
import appeng.api.util.AEPartLocation
import appeng.api.util.DimensionalCoord
import me.yrf.mcmods.capadapter.ae2.*
import me.yrf.mcmods.capadapter.capabilities.IAEGridProxyCapability
import me.yrf.mcmods.capadapter.items.ItemAECapAdapterPart
import me.yrf.mcmods.capadapter.parts.models.PartAECapAdapterModel
import me.yrf.mcmods.capadapter.util.Platform
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.CapabilityInject
import java.util.*

class PartAECapAdapter : IPart by DefaultPartDelegate, ILocationAwareGridHost, IGridTickable {
    private lateinit var partLocation: AEPartLocation
    private lateinit var host: IPartHost
    private lateinit var te: TileEntity
    private lateinit var nbt: NBTTagCompound
    private val node: IGridNode by lazy {
        val gridBlock = AEGridBlock(this, ItemStack(ItemAECapAdapterPart, 1), false, 1.0,
                EnumSet.of(GridFlags.DENSE_CAPACITY, GridFlags.PREFERRED))
        val node = AE2Plugin.api.grid.createGridNode(gridBlock)
        if (::nbt.isInitialized) {
            node.loadFromNBT("extNode", nbt)
        }
        this.owner
                ?.let { AE2Plugin.api.registries.players().getID(it) }
                ?.let { node.playerID = it }

        this.host.markForSave()
        node
    }
    private val remoteLinkNode: IGridNode by lazy {
        val gridBlock = AEGridBlock(this, null, false, 0.0,
                EnumSet.of(GridFlags.DENSE_CAPACITY, GridFlags.PREFERRED))
        val node = AE2Plugin.api.grid.createGridNode(gridBlock)
        if (::nbt.isInitialized) {
            node.loadFromNBT("intNode", nbt)
        }
        this.owner
                ?.let { AE2Plugin.api.registries.players().getID(it) }
                ?.let { node.playerID = it }
        AE2Plugin.api.grid.createGridConnection(this.node, node)

        this.host.markForSave()
        node
    }

    /**
     * This is really just used to hold the owner until the nodes are initialized.
     */
    private var owner: EntityPlayer? = null

    private var remoteConnection: IGridConnection? = null

    private val proxy = IAEGridProxyCapability { this.remoteLinkNode }

    override val location: DimensionalCoord
        get() = DimensionalCoord(te)

    override fun getItemStack(partItemStack: PartItemStack): ItemStack? {
        return ItemStack(ItemAECapAdapterPart, 1)
    }

    override fun isSolid(): Boolean {
        return true
    }

    override fun onNeighborChanged(iBlockAccess: IBlockAccess, blockPos: BlockPos, blockPos1: BlockPos) {
        if (Platform.isServer && ::te.isInitialized)
            updateConnectedNode()
    }

    override fun removeFromWorld() {
        if (Platform.isServer) {
            this.node.destroy()
            this.remoteLinkNode.destroy()
        }
    }

    override fun addToWorld() {
        if (Platform.isServer) {
            this.node.updateState()
            this.remoteLinkNode.updateState()
        }
    }

    override fun setPartHostInfo(aePartLocation: AEPartLocation, iPartHost: IPartHost, tileEntity: TileEntity) {
        this.partLocation = aePartLocation
        this.host = iPartHost
        this.te = tileEntity
    }

    override fun getCableConnectionLength(aeCableType: AECableType?): Float {
        return 3f
    }

    override fun onPlacement(entityPlayer: EntityPlayer, enumHand: EnumHand, itemStack: ItemStack, aePartLocation: AEPartLocation) {
        this.owner = entityPlayer
    }

    override fun canBePlacedOn(busSupport: BusSupport?): Boolean {
        return busSupport == BusSupport.CABLE
    }

    override fun getStaticModels(): IPartModel = PartAECapAdapterModel

    override fun hasCapability(capabilityClass: Capability<*>?): Boolean {
        return capabilityClass == GridProxyCapability
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> getCapability(capabilityClass: Capability<T>?): T? {
        return when (capabilityClass) {
            GridProxyCapability -> proxy as T
            else -> null
        }
    }

    override fun getBoxes(boxes: IPartCollisionHelper) {
        boxes.addBox(2.0, 2.0, 16.0, 14.0, 14.0, 14.0)
        boxes.addBox(5.0, 5.0, 14.0, 11.0, 11.0, 13.0)
    }

    private fun updateConnectedNode() {
        val world = this.te.world
        val pos = this.te.pos
                .offset(this.partLocation.facing)
        val capFacing = partLocation.opposite.facing

        var foundNode: IGridNode? = null

        if (world.isBlockLoaded(pos)) {
            val target = world.getTileEntity(pos)
            if (target?.hasCapability(GridProxyCapability, capFacing) == true && target !is IGridHost) {
                val remoteCap = target.getCapability(GridProxyCapability, capFacing)
                if (remoteCap?.proxiedObject != null && remoteCap.proxiedObject !== this.remoteLinkNode) {
                    foundNode = remoteCap.proxiedObject
                }
            }
        }

        //Connect to new node
        if (foundNode != null && this.remoteConnection?.getOtherSide(this.remoteLinkNode) != foundNode) {
            this.remoteConnection?.destroy()
            try {
                this.remoteConnection = AE2Plugin.api.grid.createGridConnection(this.remoteLinkNode, foundNode)
            } catch (e: FailedConnectionException) {
                //Do nothing! It's just failing to connect.
            }
            this.remoteLinkNode.updateState()
            this.host.markForSave()
        } else if (foundNode == null && this.remoteConnection != null) {
            this.remoteConnection?.destroy()
            this.remoteConnection = null
            this.remoteLinkNode.updateState()
            this.host.markForSave()
        }
    }

    override fun getGridNode(): IGridNode? {
        return if (Platform.isServer) this.node else null
    }

    override fun getGridNode(dir: AEPartLocation): IGridNode? {
        return if (Platform.isServer) this.node else null
    }

    override fun getCableConnectionType(dir: AEPartLocation): AECableType = AECableType.GLASS

    override fun securityBreak() {}

    override fun tickingRequest(node: IGridNode, ticksSinceLastCall: Int): TickRateModulation {

        if (node != this.node)
            return TickRateModulation.SLEEP
        updateConnectedNode()
        return TickRateModulation.IDLE
    }

    override fun getTickingRequest(node: IGridNode): TickingRequest =
            TickingRequest(20, 60, false, false)

    override fun readFromNBT(data: NBTTagCompound) {
        if (data.hasKey("nodes")) {
            this.nbt = data.getCompoundTag("nodes")
        }
    }

    override fun writeToNBT(data: NBTTagCompound) {
        if (Platform.isServer) {
            val tag = NBTTagCompound()
            this.node.saveToNBT("extNode", tag)
            this.remoteLinkNode.saveToNBT("intNode", tag)
            data.setTag("nodes", tag)
        }
    }

//    override fun readFromStream(data: ByteBuf): Boolean {
//        val old = this.clientFlags
//        this.clientFlags = data.readInt()
//        return old != this.clientFlags
//    }
//
//    override fun writeToStream(data: ByteBuf) {
//        data.writeInt(clientFlags)
//    }

    companion object {
        @CapabilityInject(IAEGridProxyCapability::class)
        private lateinit var GridProxyCapability: Capability<IAEGridProxyCapability>
    }
}
