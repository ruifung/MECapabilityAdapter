package me.yrf.mcmods.capadapter.ae2;

import me.yrf.mcmods.capadapter.CapabilityAdapter;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;

public class ItemAECapAdapter extends ItemBlock {
    public ItemAECapAdapter(String unlocalizedName, Block block) {
        super(block);
        setCreativeTab(CreativeTabs.MISC);
        setTranslationKey(unlocalizedName);
        setNoRepair();
        setHasSubtypes(false);
        setRegistryName(CapabilityAdapter.MODID, "aecapabilityadapter");
    }
}
