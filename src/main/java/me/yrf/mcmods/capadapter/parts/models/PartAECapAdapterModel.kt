package me.yrf.mcmods.capadapter.parts.models

import appeng.api.parts.IPartModel
import me.yrf.mcmods.capadapter.modResource
import net.minecraft.util.ResourceLocation
import java.util.*

object PartAECapAdapterModel : IPartModel {
    override fun getModels(): List<ResourceLocation> = Collections.singletonList(modResource("parts/aecapabilityadapter"))
}