package me.yrf.mcmods.capadapter.parts

import appeng.api.networking.IGridNode
import appeng.api.parts.*
import appeng.api.util.AECableType
import appeng.api.util.AEPartLocation
import io.netty.buffer.ByteBuf
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import java.util.*

object DefaultPartDelegate : IPart {
    override fun getLightLevel(): Int = 0

    override fun getExternalFacingNode(): IGridNode? = null

    override fun onShiftActivate(player: EntityPlayer?, hand: EnumHand?, pos: Vec3d?): Boolean = false

    override fun isSolid(): Boolean = false

    override fun readFromNBT(data: NBTTagCompound?) {}

    override fun writeToNBT(data: NBTTagCompound?) {}

    override fun getItemStack(type: PartItemStack?): ItemStack = ItemStack.EMPTY

    override fun readFromStream(data: ByteBuf?): Boolean = false

    override fun canConnectRedstone(): Boolean = false

    override fun onActivate(player: EntityPlayer?, hand: EnumHand?, pos: Vec3d?): Boolean = false

    override fun randomDisplayTick(world: World?, pos: BlockPos?, r: Random?) {}

    override fun addToWorld() {}

    override fun isLadder(entity: EntityLivingBase?): Boolean = false

    override fun isProvidingStrongPower(): Int = 0

    override fun getDrops(drops: MutableList<ItemStack>?, wrenched: Boolean) {}

    override fun setPartHostInfo(side: AEPartLocation?, host: IPartHost?, tile: TileEntity?) {}

    override fun onNeighborChanged(w: IBlockAccess?, pos: BlockPos?, neighbor: BlockPos?) {}

    override fun writeToStream(data: ByteBuf?) {}

    override fun canBePlacedOn(what: BusSupport?): Boolean = false

    override fun getGridNode(): IGridNode? = null

    override fun requireDynamicRender(): Boolean = false

    override fun onEntityCollision(entity: Entity?) {}

    override fun getBoxes(boxes: IPartCollisionHelper?) {}

    override fun onPlacement(player: EntityPlayer?, hand: EnumHand?, held: ItemStack?, side: AEPartLocation?) {}

    override fun removeFromWorld() {}

    override fun isProvidingWeakPower(): Int = 0

    override fun getCableConnectionLength(cable: AECableType?): Float = 0F
}