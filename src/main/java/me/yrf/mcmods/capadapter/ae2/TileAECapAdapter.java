package me.yrf.mcmods.capadapter.ae2;

import appeng.api.exceptions.FailedConnectionException;
import appeng.api.networking.*;
import appeng.api.util.AECableType;
import appeng.api.util.AEColor;
import appeng.api.util.AEPartLocation;
import appeng.api.util.DimensionalCoord;
import me.yrf.mcmods.capadapter.CapabilityAdapterAEPlugin;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fml.common.FMLCommonHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class TileAECapAdapter extends TileEntity implements IGridHost, ITickable {

    @CapabilityInject(IAEGridProxyCapability.class)
    private static Capability<IAEGridProxyCapability> GridProxyCapability;

    private final AEGridProxy gridProxy = new AEGridProxy();
    private IGridNode node;
    private IAEGridProxyCapability cap = new ProxyCapability();
    private short tickCounter = 0;

    private ConcurrentHashMap<IGridNode, IGridConnection> capabilityNodes = new ConcurrentHashMap<>();

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == GridProxyCapability || super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == GridProxyCapability ? (T) cap : super.getCapability(capability, facing);
    }

    public void setPlayer(EntityPlayer player) {
        if (player == null)
            return;
        int id = CapabilityAdapterAEPlugin.INSTANCE.api.registries().players().getID(player);
        node.setPlayerID(id);
    }

    public void onBlockUpdate() {
        node.updateState();
        updateConnectedNodes();
    }

    private Set<IGridNode> searchSet = new HashSet<>(6); //So it isn't constantly reallocated.
    public void updateConnectedNodes() {
        for (EnumFacing f : EnumFacing.VALUES) {
            BlockPos pos = getPos().offset(f);
            if (!world.isBlockLoaded(pos))
                continue;
            TileEntity te = world.getTileEntity(pos);
            if (te != null && te.hasCapability(GridProxyCapability, f.getOpposite()) && !(te instanceof IGridHost)) {
                IAEGridProxyCapability remoteCap = te.getCapability(GridProxyCapability, f.getOpposite());
                if (remoteCap != null && remoteCap.getProxiedObject() != null && remoteCap.getProxiedObject() != node) {
                    searchSet.add(remoteCap.getProxiedObject());
                }
            }
        }

        //Purge potentially stale nodes.
        if (!capabilityNodes.isEmpty()) {
            for (IGridNode n: capabilityNodes.keySet()) {
                if (!searchSet.contains(n)) {
                    IGridConnection conn = capabilityNodes.remove(n);
                    if (node.getConnections().contains(conn)) {
                        conn.destroy();
                    }
                }
            }
        }

        //Connect to new nodes
        IGridHelper gh = CapabilityAdapterAEPlugin.INSTANCE.api.grid();
        for (IGridNode remoteNode : searchSet) {
            if (capabilityNodes.containsKey(remoteNode))
                continue;
            try {
                IGridConnection conn = gh.createGridConnection(node, remoteNode);
                capabilityNodes.put(remoteNode, conn);
            } catch (FailedConnectionException e) {
                //Do nothing! It's just failing to connect.
            }
        }

        searchSet.clear();
    }

    @Override
    public void validate() {
        super.validate();
        if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
            node = CapabilityAdapterAEPlugin.INSTANCE.api.grid().createGridNode(gridProxy);
        }
    }

    @Override
    public void invalidate() {
        super.invalidate();
        if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
            node.destroy();
            node = null;
        }
    }



    @Override
    public void onChunkUnload() {
        node.destroy();
        super.onChunkUnload();
    }

    @Override
    public void onLoad() {
        node.updateState();
    }

    @Override
    public IGridNode getGridNode(AEPartLocation aePartLocation) {
        return node;
    }

    @Override
    public AECableType getCableConnectionType(AEPartLocation aePartLocation) {
        return AECableType.SMART;
    }

    @Override
    public void securityBreak() {
    }

    @Override
    public void update() {
        if (tickCounter == 0) {
            updateConnectedNodes();
            tickCounter++;
        } else {
            tickCounter++;
        }
        if (tickCounter == 60) {
            tickCounter = 0;
        }
    }


    private class ProxyCapability implements IAEGridProxyCapability {
        @Override
        public IGridNode getProxiedObject() {
            return node;
        }
    }

    private class AEGridProxy implements IGridBlock {

        private ItemStack display;

        @Override
        public double getIdlePowerUsage() {
            return 1;
        }

        @Override
        public EnumSet<GridFlags> getFlags() {
            return EnumSet.noneOf(GridFlags.class);
        }

        @Override
        public boolean isWorldAccessible() {
            return true;
        }

        @Override
        public DimensionalCoord getLocation() {
            return new DimensionalCoord(getWorld(), getPos());
        }

        @Override
        public AEColor getGridColor() {
            return AEColor.TRANSPARENT;
        }

        @Override
        public void onGridNotification(GridNotification gridNotification) {
            //Nothing to do here.
        }

        @Override
        public void setNetworkStatus(IGrid iGrid, int i) {

        }

        @Override
        public EnumSet<EnumFacing> getConnectableSides() {
            return EnumSet.allOf(EnumFacing.class);
        }

        @Override
        public IGridHost getMachine() {
            return TileAECapAdapter.this;
        }

        @Override
        public void gridChanged() {

        }

        @Override
        public ItemStack getMachineRepresentation() {
            if (display == null)
                display = new ItemStack(Item.getItemFromBlock(world.getBlockState(pos).getBlock()));
            return display;
        }
    }
}
