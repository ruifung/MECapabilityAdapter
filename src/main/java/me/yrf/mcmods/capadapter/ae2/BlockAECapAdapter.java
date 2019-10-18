package me.yrf.mcmods.capadapter.ae2;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class BlockAECapAdapter extends BlockContainer {
    public BlockAECapAdapter(String unlocalizedName) {
        super(Material.PISTON, MapColor.STONE);
        setTranslationKey(unlocalizedName);
        setHardness(1f);
        setRegistryName("aecapabilityadapter");
    }

    @Override
    @MethodsReturnNonnullByDefault
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        if (!worldIn.isRemote) {
            return new TileAECapAdapter();
        } else {
            return null;
        }
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        if (!worldIn.isRemote && placer instanceof EntityPlayer) {
            TileEntity te = worldIn.getTileEntity(pos);
            if (te instanceof TileAECapAdapter) {
                ((TileAECapAdapter) te).setPlayer((EntityPlayer) placer);
            }
        }
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this);
    }

    @Override
    @SuppressWarnings("deprecated")
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (!worldIn.isRemote) {
            TileEntity te = worldIn.getTileEntity(pos);
            if (te instanceof TileAECapAdapter) {
                ((TileAECapAdapter) te).onBlockUpdate();
            }
        }
    }
}
