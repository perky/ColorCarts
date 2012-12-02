package ljdp.colorcarts.common;

import net.minecraft.src.Block;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import cpw.mods.fml.common.registry.GameRegistry;

public abstract class Recipes {
	
	public static void RegisterRecipes() {
		ItemStack minecart = new ItemStack(Item.minecartEmpty, 1);
		for(EnumCartColor color : EnumCartColor.values()) {
			addColorCartRecipe(color);
			addWashColorCarRecipe(color);
		}
		addColorDetectorRecipe();
	}
	
	private static void addColorCartRecipe(EnumCartColor color) {
		ItemStack minecart = new ItemStack(Item.minecartEmpty, 1);
		ItemStack colorCart = new ItemStack(ColorCarts.itemColorMinecart, 1, color.ordinal());
		ItemStack dye = new ItemStack(Item.dyePowder, 1, color.ordinal());
		GameRegistry.addShapelessRecipe(colorCart, dye, minecart);
	}
	
	private static void addWashColorCarRecipe(EnumCartColor color) {
		ItemStack waterBucket = new ItemStack(Item.bucketWater, 1);
		ItemStack colorCart = new ItemStack(ColorCarts.itemColorMinecart, 1, color.ordinal());
		ItemStack minecart = new ItemStack(Item.minecartEmpty, 1);
		GameRegistry.addShapelessRecipe(minecart, colorCart, waterBucket);
	}
	
	private static void addColorDetectorRecipe() {
		ItemStack colorDetector = new ItemStack(ColorCarts.blockMinecartColorDetector, 1);
		ItemStack glassPane = new ItemStack(Block.thinGlass);
		ItemStack redDye = new ItemStack(Item.dyePowder, 1, EnumCartColor.RED.ordinal());
		ItemStack greenDye = new ItemStack(Item.dyePowder, 1, EnumCartColor.GREEN.ordinal());
		ItemStack blueDye = new ItemStack(Item.dyePowder, 1, EnumCartColor.BLUE.ordinal());
		ItemStack woodPlank = new ItemStack(Block.planks);
		GameRegistry.addRecipe(colorDetector, 
				"PPP",
				"RGB",
				"WWW",
				'P', glassPane,
				'R', redDye,
				'G', greenDye,
				'B', blueDye,
				'W', woodPlank);
	}

}
