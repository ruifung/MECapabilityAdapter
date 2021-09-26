/*
 * This program is free software. It comes without any warranty, to
 * the extent permitted by applicable law. You can redistribute it
 * and/or modify it under the terms of the Do What The Fuck You Want
 * To Public License, Version 2, as published by Sam Hocevar. See
 * http://www.wtfpl.net/ for more details.
 */

package me.yrf.mcmods.capadapter.blocks

import me.yrf.mcmods.capadapter.tiles.TileAECapAdapter
import net.minecraft.block.Block
import net.minecraft.block.BlockContainer
import net.minecraft.block.material.MapColor
import net.minecraft.block.material.Material
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumBlockRenderType
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

object AECapAdapterMaterial: Material(MapColor.STONE) {
    init {
        setImmovableMobility()
    }
}

object BlockAECapAdapter : BlockContainer(AECapAdapterMaterial, MapColor.STONE) {
    init {
        translationKey = "aecapabilityadapter"
        setHardness(1f)
        setRegistryName("aecapabilityadapter")
    }

    override fun getRenderType(state: IBlockState?): EnumBlockRenderType {
        return EnumBlockRenderType.MODEL
    }

    override fun createNewTileEntity(worldIn: World, meta: Int): TileEntity? {
        return if (!worldIn.isRemote) {
            TileAECapAdapter()
        } else {
            null
        }
    }

    override fun onBlockPlacedBy(worldIn: World, pos: BlockPos?, state: IBlockState?, placer: EntityLivingBase?, stack: ItemStack?) {
        if (!worldIn.isRemote && placer is EntityPlayer) {
            val te = worldIn.getTileEntity(pos!!)
            if (te is TileAECapAdapter) {
                te.setPlayer(placer as EntityPlayer?)
            }
        }
    }

    override fun createBlockState(): BlockStateContainer {
        return BlockStateContainer(this)
    }

    override fun neighborChanged(state: IBlockState?, worldIn: World, pos: BlockPos?, blockIn: Block?, fromPos: BlockPos?) {
        if (!worldIn.isRemote) {
            val te = worldIn.getTileEntity(pos!!)
            if (te is TileAECapAdapter) {
                te.onBlockUpdate()
            }
        }
    }
}