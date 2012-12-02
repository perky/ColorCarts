package ljdp.colorcarts.common;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import ljdp.colorcarts.common.network.IPacketReceiver;
import ljdp.colorcarts.common.network.PacketHandler;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.INetworkManager;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.Packet;
import net.minecraft.src.TileEntity;
import net.minecraftforge.common.MinecraftForge;

public class TileEntityMinecartColorDetector extends TileEntity implements IPacketReceiver {
	
	public Set<EnumCartColor> detectorColors = EnumSet.noneOf(EnumCartColor.class);
	private final EnumCartColor[] colors = EnumCartColor.values();
	public boolean powering;
	public boolean didUpdateBlock;
	
	public TileEntityMinecartColorDetector() {
		powering = false;
		didUpdateBlock = false;
	}
	
	public void clearDetectorColors() {
		detectorColors = EnumSet.noneOf(EnumCartColor.class);
	}
	
	public boolean minecartIsCorrectColor(EntityColorMinecart minecart) {
		EnumCartColor color = minecart.getMinecartColor();
		return hasColor(color);
	}
	
	public void addDetectorColor(EnumCartColor color) {
		detectorColors.add(color);
	}
	
	public void removeDetectorColor(EnumCartColor color) {
		detectorColors.remove(color);
	}
	
	public boolean hasColor(EnumCartColor color) {
		return detectorColors.contains(color);
	}
	
	private void emitRedstoneSignal() {
		powering = true;
		if (!didUpdateBlock) {
			updateBlock();
			didUpdateBlock = true;
		}
	}
	
	private void stopRedstoneSignal() {
		powering = false;
		if (didUpdateBlock) {
			updateBlock();
			didUpdateBlock = false;
		}
	}
	
	private void updateBlock() {
		worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, worldObj.getBlockId(xCoord, yCoord, zCoord));
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		BlockMinecartColorDetector blockColorDetector = (BlockMinecartColorDetector)ColorCarts.blockMinecartColorDetector;
		blockColorDetector.notifyNeighbors(worldObj, xCoord, yCoord, zCoord);
	}
	
	public boolean isPowering() {
		return powering;
	}
	
	@Override
	public void updateEntity() {
		super.updateEntity();
		List detectedMinecarts = worldObj.getEntitiesWithinAABB(EntityColorMinecart.class, 
				AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 1.0D, yCoord + 1.0D, zCoord + 1.0D).expand(0.0D, 2.0D, 0.0D));
		if(detectedMinecarts.size() > 0) {
			EntityColorMinecart minecart = (EntityColorMinecart)detectedMinecarts.get(0);
			if(minecartIsCorrectColor(minecart)) {
				emitRedstoneSignal();
			}
		} else {
			stopRedstoneSignal();
		}
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbtTagCompound) {
		super.writeToNBT(nbtTagCompound);
		for(EnumCartColor color : EnumCartColor.values()) {
			nbtTagCompound.setBoolean(color.name(), hasColor(color));
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbtTagCompound) {
		super.readFromNBT(nbtTagCompound);
		clearDetectorColors();
		for(EnumCartColor color : EnumCartColor.values()) {
			boolean activeColor = nbtTagCompound.getBoolean(color.name());
			if (activeColor) {
				addDetectorColor(color);
			}
		}
	}
	
	@Override
	public Packet getDescriptionPacket() {
		return PacketHandler.getColorDetectorPacket(this);
	}

	@Override
	public void handleDescriptionPacketData(INetworkManager network, EntityPlayer player,
			DataInputStream data) throws Exception {
		
		clearDetectorColors();
		for(EnumCartColor color : EnumCartColor.values()) {
			boolean activeColor = data.readBoolean();
			if (activeColor) {
				addDetectorColor(color);
			}
		}
		
	}

}
