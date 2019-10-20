package me.yrf.mcmods.capadapter.ae2

import appeng.api.AEPlugin
import appeng.api.IAppEngApi
import appeng.api.definitions.IDefinitions
import appeng.api.features.IRegistryContainer
import appeng.api.networking.IGridHelper
import appeng.api.parts.IPartHelper
import appeng.api.parts.IPartModels
import appeng.api.storage.IStorageHelper
import appeng.api.util.IClientHelper

@AEPlugin
@Suppress("UNUSED_PARAMETER")
class AE2Plugin(
        api: IAppEngApi
) {
    init {
        _api = api
    }

    companion object {
        private lateinit var _api: IAppEngApi
        val api: IAppEngApi
            get() = _api
    }
}

inline val IAppEngApi.registries: IRegistryContainer
    get() = this.registries()

inline val IAppEngApi.storage: IStorageHelper
    get() = this.storage()

inline val IAppEngApi.grid: IGridHelper
    get() = this.grid()

inline val IAppEngApi.partHelper: IPartHelper
    get() = this.partHelper()

inline val IAppEngApi.definitions: IDefinitions
    get() = this.definitions()

inline val IAppEngApi.client: IClientHelper
    get() = this.client()