package ljdp.colorcarts.common;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.BlockContainer;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumFacing;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;

public class BlockMinecartColorDetector extends BlockContainer {
	
	private static final int sideBottom = 0;
	private static final int sideTop = 1;
	private static final int sidesTextureId = 0;
	private static final int bottomTextureId = 2;
	private static final int topTextureId = 1;
	
	public BlockMinecartColorDetector(int id) {
		super(id, Material.circuits);
		setHardness(2.0F);
        setResistance(5.0F);
		setBlockName("blockMinecartColorDetector");
		setCreativeTab(CreativeTabs.tabTransport);
		setTickRandomly(true);
	}
	
	@Override
	public String getTextureFile() {
		return CommonProxy.BLOCKS_PNG;
	}
	
	@Override
	public int getBlockTextureFromSide(int side) {
		if (side == sideBottom) {
			return bottomTextureId;
		} else if (side == sideTop) {
			return topTextureId;
		} else {
			return sidesTextureId;
		}
	}
	
	public void notifyNeighbors(World world, int x, int y, int z)
    {
        world.notifyBlocksOfNeighborChange(x, y + 1, z, this.blockID); //above
        world.notifyBlocksOfNeighborChange(x, y - 1, z, this.blockID); //below
        world.notifyBlocksOfNeighborChange(x - 1, y, z, this.blockID); //
        world.notifyBlocksOfNeighborChange(x + 1, y, z, this.blockID); //
        world.notifyBlocksOfNeighborChange(x, y, z - 1, this.blockID); //
        world.notifyBlocksOfNeighborChange(x, y, z + 1, this.blockID); //
    }
	
	@Override
	public boolean isBlockSolidOnSide(World world, int x, int y, int z,
			ForgeDirection side) {
		return true;
	}
	
	@Override
	public boolean isOpaqueCube() {
		return true;
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, 
			int par6, float par7, float par8, float par9) {
		 TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
         if (tileEntity == null || player.isSneaking()) {
                 return false;
         }
         player.openGui(ColorCarts.instance, 1, world, x, y, z);
         return true;
	}
	
	
	@Override
	public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z,
			int side) {
		return true;
	}
	
	@Override
	public boolean canProvidePower() {
		return true;
	}
	
	@Override
	public boolean isProvidingStrongPower(IBlockAccess par1iBlockAccess,
			int x, int y, int z, int par5) {
		TileEntityMinecartColorDetector detector = (TileEntityMinecartColorDetector)par1iBlockAccess.getBlockTileEntity(x, y, z);
		return detector.isPowering();
	}
	
	@Override
	public boolean isProvidingWeakPower(IBlockAccess par1iBlockAccess,
			int x, int y, int z, int par5) {
		TileEntityMinecartColorDetector detector = (TileEntityMinecartColorDetector)par1iBlockAccess.getBlockTileEntity(x, y, z);
		return detector.isPowering();
	}
	
	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityMinecartColorDetector();
	}
	

}
