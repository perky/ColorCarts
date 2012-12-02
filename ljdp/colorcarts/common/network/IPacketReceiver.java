package ljdp.colorcarts.common.network;

import java.io.DataInputStream;
import java.io.IOException;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.INetworkManager;

public interface IPacketReceiver {
	public void handleDescriptionPacketData(INetworkManager network, EntityPlayer player, DataInputStream data) throws Exception;
}
