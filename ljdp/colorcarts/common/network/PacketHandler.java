package ljdp.colorcarts.common.network;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ljdp.colorcarts.common.EnumCartColor;
import ljdp.colorcarts.common.TileEntityMinecartColorDetector;

import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.INetworkManager;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class PacketHandler implements IPacketHandler {
	
	public enum PacketID {
		TILE_ENTITY
	};
	
	public PacketID[] packetIDs = PacketID.values();

	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
		try {
			DataInputStream data = new DataInputStream(new ByteArrayInputStream(packet.data));
			PacketID packetID = packetIDs[data.readByte()];
			int x = data.readInt();
			int y = data.readInt();
			int z = data.readInt();
			EntityPlayer entityPlayer = (EntityPlayer)player;
			World world = entityPlayer.worldObj;
			if (world != null) {
				TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
				if (tileEntity instanceof IPacketReceiver) {
					sendPacketDataToTileEntity((IPacketReceiver)tileEntity, packetID, manager, entityPlayer, data);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void sendPacketDataToTileEntity(IPacketReceiver tileEntity, PacketID packetID, INetworkManager manager,
			EntityPlayer player, DataInputStream data) throws Exception  {
		switch(packetID) {
		case TILE_ENTITY:
			tileEntity.handleDescriptionPacketData(manager, player, data);
			break;
		}
	}
	
	public static Packet250CustomPayload getColorDetectorPacket(TileEntityMinecartColorDetector colorDetector) {
		ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
		DataOutputStream data = new DataOutputStream(byteArray);
		
		try {
			data.writeByte(PacketID.TILE_ENTITY.ordinal());
			data.writeInt(colorDetector.xCoord);
			data.writeInt(colorDetector.yCoord);
			data.writeInt(colorDetector.zCoord);
			for(EnumCartColor color : EnumCartColor.values()) {
				data.writeBoolean(colorDetector.hasColor(color));
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = "ljdpColorCarts";
		packet.data = byteArray.toByteArray();
		packet.length = byteArray.size();
		packet.isChunkDataPacket = true;
		return packet;
	}
	

}
