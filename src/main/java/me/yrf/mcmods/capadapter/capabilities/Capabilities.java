package me.yrf.mcmods.capadapter.capabilities;

import me.yrf.mcmods.capadapter.ae2.IAEGridProxyCapability;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class Capabilities {
    public static void register() {
    }

    public static <T> Capability.IStorage<T> noOpStorage()
    {
        return new Capability.IStorage<T>()
        {
            @Override
            public NBTBase writeNBT(Capability<T> capability, T instance, EnumFacing side ) {
                return null;
            }

            @Override
            public void readNBT( Capability<T> capability, T instance, EnumFacing side, NBTBase nbt ) { }
        };
    }
}
