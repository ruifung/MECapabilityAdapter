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