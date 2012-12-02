package ljdp.colorcarts.common;

import cpw.mods.fml.common.network.IGuiHandler;
import ljdp.colorcarts.client.GuiMinecartColorDetector;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class CommonProxy implements IGuiHandler {

	public static String CART_PNG  = "/ljdp/colorcarts/cart.png";
	public static String ITEMS_PNG  = "/ljdp/colorcarts/items.png";
	public static String BLOCKS_PNG = "/ljdp/colorcarts/blocks.png";
	
	// Client stuff
	public void registerRenderers() {
		// Nothing here as this is the server side proxy
	}
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
        if(tileEntity instanceof TileEntityMinecartColorDetector){
                return new GuiMinecartColorDetector(player, (TileEntityMinecartColorDetector)tileEntity);
        }
        return null;
	}
}
