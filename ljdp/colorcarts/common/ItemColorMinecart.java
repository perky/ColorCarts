package ljdp.colorcarts.common;

import java.util.List;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

import railcraft.common.api.carts.CartTools;
import railcraft.common.api.core.items.IMinecartItem;
import net.minecraft.src.BlockRail;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityMinecart;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemMinecart;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class ItemColorMinecart extends ItemMinecart implements IMinecartItem {
	
	public static int minecartType = 0;
	private final EnumCartColor[] colors = EnumCartColor.values();
	
	public ItemColorMinecart(int id) {
		super(id, minecartType);
		setHasSubtypes(true);
		setMaxStackSize(3);
		setCreativeTab(CreativeTabs.tabTransport);
		setIconIndex(0);
		setItemName("colorCart");
	}
	
	@Override
	public String getItemDisplayName(ItemStack itemStack) {
		ItemColorMinecart item = (ItemColorMinecart)itemStack.getItem();
		EnumCartColor cartColor = getMinecartColor(itemStack);
		return cartColor.descriptiveName() + " Minecart";
	};
	
	private EnumCartColor getMinecartColor(ItemStack itemStack) {
		return colors[itemStack.getItemDamage()];
	}
	
	@Override
	public String getTextureFile() {
		return CommonProxy.ITEMS_PNG;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getIconFromDamage(int damage) {
		return damage;
	}
	
	/**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
	@Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World world, int i, int j, int k, int par7, float par8, float par9, float par10)
    {
        int blockId = world.getBlockId(i, j, k);

        if (BlockRail.isRailBlock(blockId))
        {
            if (!world.isRemote)
            {
            	EnumCartColor cartColor = getMinecartColor(itemStack);
                world.spawnEntityInWorld(new EntityColorMinecart(world, (double)((float)i + 0.5F), (double)((float)j + 0.5F), (double)((float)k + 0.5F), cartColor));
            }

            --itemStack.stackSize;
            return true;
        }
        else
        {
            return false;
        }
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs,
			List par3List) {
		
		for(int i = 0; i < 16; ++i) {
			ItemStack colorCartStack = new ItemStack(par1, 1, i);
			ItemColorMinecart colorMinecart = (ItemColorMinecart)colorCartStack.getItem();
			par3List.add(colorCartStack);
		}
	}
	
	@Override
	public boolean canBePlacedByNonPlayer(ItemStack cart) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public EntityMinecart placeCart(String owner, ItemStack cart, World world,
			int i, int j, int k) {
		// TODO Auto-generated method stub
		return null;
	}
}
