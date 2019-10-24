/*
 * This program is free software. It comes without any warranty, to
 * the extent permitted by applicable law. You can redistribute it
 * and/or modify it under the terms of the Do What The Fuck You Want
 * To Public License, Version 2, as published by Sam Hocevar. See
 * http://www.wtfpl.net/ for more details.
 */

package me.yrf.mcmods.capadapter.capabilities

import net.minecraft.nbt.NBTBase
import net.minecraft.util.EnumFacing
import net.minecraftforge.common.capabilities.Capability

object Capabilities {
    fun register() {}

    object NoOpStorage : Capability.IStorage<Any> {
        override fun writeNBT(capability: Capability<Any>?, instance: Any?, side: EnumFacing?): NBTBase? {
            return null
        }

        override fun readNBT(capability: Capability<Any>?, instance: Any?, side: EnumFacing?, nbt: NBTBase?) {}

        @Suppress("UNCHECKED_CAST")
        inline fun <reified T> type() = this as Capability.IStorage<T>
    }
}