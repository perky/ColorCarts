package ljdp.colorcarts.client;

import cpw.mods.fml.client.registry.RenderingRegistry;
import ljdp.colorcarts.common.CommonProxy;
import ljdp.colorcarts.common.EntityColorMinecart;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {
	
	@Override
	public void registerRenderers() {
		MinecraftForgeClient.preloadTexture(CART_PNG);
		MinecraftForgeClient.preloadTexture(ITEMS_PNG);
		MinecraftForgeClient.preloadTexture(BLOCKS_PNG);
		RenderingRegistry.registerEntityRenderingHandler(EntityColorMinecart.class, new RenderColorMinecart());
	}
	
}
