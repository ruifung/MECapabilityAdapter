package me.yrf.mcmods.capadapter.recipes

import com.google.gson.JsonObject
import net.minecraft.item.Item
import net.minecraft.util.ResourceLocation
import net.minecraftforge.common.crafting.IConditionFactory
import net.minecraftforge.common.crafting.JsonContext
import net.minecraftforge.fml.common.registry.GameRegistry
import java.util.function.BooleanSupplier

class ItemConditional : IConditionFactory {
    override fun parse(context: JsonContext?, json: JsonObject?): BooleanSupplier = BooleanSupplier {
        val resultItem = json?.getAsJsonObject("result")
                ?.get("item")
                ?.takeIf { it.isJsonPrimitive }
                ?.asString
                ?.let { GameRegistry.findRegistry(Item::class.java)?.getValue(ResourceLocation(it)) }
        resultItem != null
    }
}