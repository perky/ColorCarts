package ljdp.colorcarts.common;

import ljdp.colorcarts.client.GuiMinecartColorDetector;
import ljdp.colorcarts.common.network.PacketHandler;
import railcraft.common.api.carts.CartTools;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid="ljdpColorCarts", name="Color Carts", version="0.1.5")
@NetworkMod(clientSideRequired=true, serverSideRequired=false, channels={"ljdpColorCarts"}, packetHandler=PacketHandler.class)
public class ColorCarts {
	@Instance("ljdpColorCarts")
	public static ColorCarts instance;
	
	@SidedProxy(clientSide="ljdp.colorcarts.client.ClientProxy", serverSide="ljdp.colorcarts.CommonProxy")
	public static CommonProxy proxy;
	
	public static Block blockMinecartColorDetector;
	public static Item itemColorMinecart;
	public static int blockMinecartColorDetectorID;
	public static int itemColorMinecartID;
	public static int colorDetectorTickRate;
	
	@PreInit
	public void preInit(FMLPreInitializationEvent event) {
		NetworkRegistry.instance().registerGuiHandler(this, this.proxy);
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		blockMinecartColorDetectorID = config.getBlock("minecartColorDetector", 2900).getInt();
		itemColorMinecartID = config.getItem("colorMinecart", 4900).getInt();
		colorDetectorTickRate = config.get(config.CATEGORY_GENERAL, "colorDetectorTickRate", 2).getInt();
		config.save();
	}
	
	@Init
	public void init(FMLInitializationEvent event) {
		
		itemColorMinecart = new ItemColorMinecart(itemColorMinecartID);
		blockMinecartColorDetector = new BlockMinecartColorDetector(blockMinecartColorDetectorID);
		
		CartTools.registerMinecart(this, EntityColorMinecart.class, "entityColorMinecart", 500);
		GameRegistry.registerBlock(blockMinecartColorDetector);
		GameRegistry.registerTileEntity(TileEntityMinecartColorDetector.class, "tileEntityMinecartColorDetector");
		
		proxy.registerRenderers();
		Recipes.RegisterRecipes();
		
		LanguageRegistry.addName(itemColorMinecart, "Color Minecart");
		LanguageRegistry.addName(blockMinecartColorDetector, "Minecart Color Detector");
	}
	
	@PostInit
	public void postInit(FMLPostInitializationEvent event) {
		
	}

	
}
