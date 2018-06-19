package me.yrf.mcmods.capadapter;

import appeng.api.AEPlugin;
import appeng.api.IAppEngApi;
import net.minecraftforge.fml.common.Optional;

@AEPlugin
public class CapabilityAdapterAEPlugin {
    public static CapabilityAdapterAEPlugin INSTANCE;
    public final IAppEngApi api;

    public CapabilityAdapterAEPlugin(IAppEngApi api) {
        this.api = api;
        INSTANCE = this;
    }
}
