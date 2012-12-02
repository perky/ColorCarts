package ljdp.colorcarts.common;

import java.util.ArrayList;
import java.util.List;

import railcraft.common.api.carts.CartBase;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.BlockRail;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityMinecart;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.minecart.MinecartInteractEvent;
import net.minecraftforge.event.entity.minecart.MinecartUpdateEvent;

public class EntityColorMinecart extends CartBase {
	
	public EnumCartColor minecartColor;
	private final EnumCartColor[] colors = EnumCartColor.values();
	
	public EntityColorMinecart(World par1World) {
		super(par1World);
	}

	public EntityColorMinecart(World world, double x, double y, double z,
			EnumCartColor minecartColor) {
		this(world);
        this.setPosition(x, y + (double)this.yOffset, z);
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        this.prevPosX = x;
        this.prevPosY = y;
        this.prevPosZ = z;
        this.minecartType = 0;
        setMinecartColor(minecartColor);
	}
	
	@Override
	public boolean doInteract(EntityPlayer player) {
		ItemStack equippedItem = player.getCurrentEquippedItem();
		if (this.minecartType == 0 && equippedItem != null) {
			if (equippedItem.itemID == Block.chest.blockID) {
				insertChest(player);
				return false;
			} else if (equippedItem.itemID == Block.stoneOvenIdle.blockID) {
				insertFurnace(player);
				return false;
			}
		}
		return useMinecart(player);
	}
	
	private boolean useMinecart(EntityPlayer player) {
		if (MinecraftForge.EVENT_BUS.post(new MinecartInteractEvent(this, player)))
        {
            return true;
        }

        if (canBeRidden())
        {
            if (this.riddenByEntity != null && this.riddenByEntity instanceof EntityPlayer && this.riddenByEntity != player)
            {
                return true;
            }

            if (!this.worldObj.isRemote)
            {
            	player.mountEntity(this);
            }
        }
        else if (getSizeInventory() > 0)
        {
            if (!this.worldObj.isRemote)
            {
            	player.displayGUIChest(this);
            }
        }
        else if (this.minecartType == 2 && getClass() == EntityColorMinecart.class)
        {
            ItemStack var2 = player.inventory.getCurrentItem();

            if (var2 != null && var2.itemID == Item.coal.shiftedIndex)
            {
                if (--var2.stackSize == 0)
                {
                	player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);
                }

                this.fuel += 3600;
            }

            this.pushX = this.posX - player.posX;
            this.pushZ = this.posZ - player.posZ;
        }

        return true;
	}
	
	private void insertChest(EntityPlayer player) {
		int currentSlot = player.inventory.currentItem;
		player.inventory.decrStackSize(currentSlot, 1);
		this.minecartType = 1;
		this.dataWatcher.updateObject(21, 1);
	}
	
	private void insertFurnace(EntityPlayer player) {
		int currentSlot = player.inventory.currentItem;
		player.inventory.decrStackSize(currentSlot, 1);
		this.minecartType = 2;
		this.dataWatcher.updateObject(21, 2);
	}
	
	public int getMinecartType() {
		return this.dataWatcher.getWatchableObjectInt(21);
	}
	
	@Override
	public List<ItemStack> getItemsDropped()
    {
        List<ItemStack> items = new ArrayList<ItemStack>();
        items.add(new ItemStack(ColorCarts.itemColorMinecart, 1, getMinecartColor().ordinal()));
        
        switch(minecartType)
        {
            case 1:
                items.add(new ItemStack(Block.chest));
                break;
            case 2:
                items.add(new ItemStack(Block.stoneOvenIdle));
                break;
        }
        return items;
    }
	
	@Override
	public boolean canBeRidden()
    {
        if(minecartType == 0 && getClass() == EntityColorMinecart.class)
        {
            return true;
        }
        return false;
    }
	
	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataWatcher.addObject(20, new Integer(0));
		this.dataWatcher.addObject(21, new Integer(3));
	}
	
	public EnumCartColor getMinecartColor() {
		this.minecartColor = colors[this.dataWatcher.getWatchableObjectInt(20)];
		return this.minecartColor;
	}
	
	public void setMinecartColor(EnumCartColor color) {
		this.dataWatcher.updateObject(20, color.ordinal());
		this.minecartColor = color;
	}
	
	@Override
	protected void writeEntityToNBT(NBTTagCompound par1nbtTagCompound) {
		super.writeEntityToNBT(par1nbtTagCompound);
		par1nbtTagCompound.setInteger("minecartColor", this.getMinecartColor().ordinal());
	}
	
	@Override
	protected void readEntityFromNBT(NBTTagCompound par1nbtTagCompound) {
		super.readEntityFromNBT(par1nbtTagCompound);
		this.dataWatcher.updateObject(21, this.minecartType);
		EnumCartColor color = colors[par1nbtTagCompound.getInteger("minecartColor")];
		setMinecartColor(color);
	}
	
	/**
     * Returns the number of slots in the inventory.
     */
	@Override
    public int getSizeInventory()
    {
        return (minecartType == 1 && this instanceof EntityMinecart ? 27 : 0);
    }
	
	/**
     * Returns true if this cart is self propelled.
     * @return True if powered.
     */
	@Override
    public boolean isPoweredCart()
    {
        return minecartType == 2 && this instanceof EntityMinecart;
    }

    /** 
     * Returns true if this cart is a storage cart
     * Some carts may have inventories but not be storage carts
     * and some carts without inventories may be storage carts.
     * @return True if this cart should be classified as a storage cart.
     */
    public boolean isStorageCart()
    {
        return minecartType == 1 && this instanceof EntityMinecart;
    }
    
    @Override
    public void onUpdate()
    {
        if (this.field_82344_g != null)
        {
            this.field_82344_g.update();
        }

        if (this.func_70496_j() > 0)
        {
            this.func_70497_h(this.func_70496_j() - 1);
        }

        if (this.getDamage() > 0)
        {
            this.setDamage(this.getDamage() - 1);
        }

        if (this.posY < -64.0D)
        {
            this.kill();
        }

        if (this.isMinecartPowered() && this.rand.nextInt(4) == 0 && minecartType == 2 && this instanceof EntityMinecart)
        {
            this.worldObj.spawnParticle("largesmoke", this.posX, this.posY + 0.8D, this.posZ, 0.0D, 0.0D, 0.0D);
        }

        if (this.worldObj.isRemote)
        {
            if (this.turnProgress > 0)
            {
                double var45 = this.posX + (this.minecartX - this.posX) / (double)this.turnProgress;
                double var46 = this.posY + (this.minecartY - this.posY) / (double)this.turnProgress;
                double var5 = this.posZ + (this.minecartZ - this.posZ) / (double)this.turnProgress;
                double var7 = MathHelper.wrapAngleTo180_double(this.minecartYaw - (double)this.rotationYaw);
                this.rotationYaw = (float)((double)this.rotationYaw + var7 / (double)this.turnProgress);
                this.rotationPitch = (float)((double)this.rotationPitch + (this.minecartPitch - (double)this.rotationPitch) / (double)this.turnProgress);
                --this.turnProgress;
                this.setPosition(var45, var46, var5);
                this.setRotation(this.rotationYaw, this.rotationPitch);
            }
            else
            {
                this.setPosition(this.posX, this.posY, this.posZ);
                this.setRotation(this.rotationYaw, this.rotationPitch);
            }
        }
        else
        {
            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;
            this.motionY -= 0.03999999910593033D;
            int var1 = MathHelper.floor_double(this.posX);
            int var2 = MathHelper.floor_double(this.posY);
            int var3 = MathHelper.floor_double(this.posZ);

            if (BlockRail.isRailBlockAt(this.worldObj, var1, var2 - 1, var3))
            {
                --var2;
            }

            double var4 = 0.4D;
            double var6 = 0.0078125D;
            int var8 = this.worldObj.getBlockId(var1, var2, var3);

            if (canUseRail() && BlockRail.isRailBlock(var8))
            {
                Vec3 var9 = this.func_70489_a(this.posX, this.posY, this.posZ);
                int var10 = ((BlockRail)Block.blocksList[var8]).getBasicRailMetadata(worldObj, this, var1, var2, var3);
                this.posY = (double)var2;
                boolean var11 = false;
                boolean var12 = false;

                if (var8 == Block.railPowered.blockID)
                {
                    var11 = (worldObj.getBlockMetadata(var1, var2, var3) & 8) != 0;
                    var12 = !var11;
                }

                if (((BlockRail)Block.blocksList[var8]).isPowered())
                {
                    var10 &= 7;
                }

                if (var10 >= 2 && var10 <= 5)
                {
                    this.posY = (double)(var2 + 1);
                }

                adjustSlopeVelocities(var10);

                int[][] var13 = field_70500_g[var10];
                double var14 = (double)(var13[1][0] - var13[0][0]);
                double var16 = (double)(var13[1][2] - var13[0][2]);
                double var18 = Math.sqrt(var14 * var14 + var16 * var16);
                double var20 = this.motionX * var14 + this.motionZ * var16;

                if (var20 < 0.0D)
                {
                    var14 = -var14;
                    var16 = -var16;
                }

                double var22 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
                this.motionX = var22 * var14 / var18;
                this.motionZ = var22 * var16 / var18;
                double var24;
                double var26;

                if (this.riddenByEntity != null)
                {
                    var24 = this.riddenByEntity.motionX * this.riddenByEntity.motionX + this.riddenByEntity.motionZ * this.riddenByEntity.motionZ;
                    var26 = this.motionX * this.motionX + this.motionZ * this.motionZ;

                    if (var24 > 1.0E-4D && var26 < 0.01D)
                    {
                        this.motionX += this.riddenByEntity.motionX * 0.1D;
                        this.motionZ += this.riddenByEntity.motionZ * 0.1D;
                        var12 = false;
                    }
                }

                if (var12 && shouldDoRailFunctions())
                {
                    var24 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);

                    if (var24 < 0.03D)
                    {
                        this.motionX *= 0.0D;
                        this.motionY *= 0.0D;
                        this.motionZ *= 0.0D;
                    }
                    else
                    {
                        this.motionX *= 0.5D;
                        this.motionY *= 0.0D;
                        this.motionZ *= 0.5D;
                    }
                }

                var24 = 0.0D;
                var26 = (double)var1 + 0.5D + (double)var13[0][0] * 0.5D;
                double var28 = (double)var3 + 0.5D + (double)var13[0][2] * 0.5D;
                double var30 = (double)var1 + 0.5D + (double)var13[1][0] * 0.5D;
                double var32 = (double)var3 + 0.5D + (double)var13[1][2] * 0.5D;
                var14 = var30 - var26;
                var16 = var32 - var28;
                double var34;
                double var36;

                if (var14 == 0.0D)
                {
                    this.posX = (double)var1 + 0.5D;
                    var24 = this.posZ - (double)var3;
                }
                else if (var16 == 0.0D)
                {
                    this.posZ = (double)var3 + 0.5D;
                    var24 = this.posX - (double)var1;
                }
                else
                {
                    var34 = this.posX - var26;
                    var36 = this.posZ - var28;
                    var24 = (var34 * var14 + var36 * var16) * 2.0D;
                }

                this.posX = var26 + var14 * var24;
                this.posZ = var28 + var16 * var24;
                this.setPosition(this.posX, this.posY + (double)this.yOffset, this.posZ);

                moveMinecartOnRail(var1, var2, var3);

                if (var13[0][1] != 0 && MathHelper.floor_double(this.posX) - var1 == var13[0][0] && MathHelper.floor_double(this.posZ) - var3 == var13[0][2])
                {
                    this.setPosition(this.posX, this.posY + (double)var13[0][1], this.posZ);
                }
                else if (var13[1][1] != 0 && MathHelper.floor_double(this.posX) - var1 == var13[1][0] && MathHelper.floor_double(this.posZ) - var3 == var13[1][2])
                {
                    this.setPosition(this.posX, this.posY + (double)var13[1][1], this.posZ);
                }

                applyDragAndPushForces();

                Vec3 var52 = this.func_70489_a(this.posX, this.posY, this.posZ);

                if (var52 != null && var9 != null)
                {
                    double var39 = (var9.yCoord - var52.yCoord) * 0.05D;
                    var22 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);

                    if (var22 > 0.0D)
                    {
                        this.motionX = this.motionX / var22 * (var22 + var39);
                        this.motionZ = this.motionZ / var22 * (var22 + var39);
                    }

                    this.setPosition(this.posX, var52.yCoord, this.posZ);
                }

                int var51 = MathHelper.floor_double(this.posX);
                int var53 = MathHelper.floor_double(this.posZ);

                if (var51 != var1 || var53 != var3)
                {
                    var22 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
                    this.motionX = var22 * (double)(var51 - var1);
                    this.motionZ = var22 * (double)(var53 - var3);
                }

                double var41;

                updatePushForces();
                
                if(shouldDoRailFunctions())
                {
                    ((BlockRail)Block.blocksList[var8]).onMinecartPass(worldObj, this, var1, var2, var3);
                }

                if (var11 && shouldDoRailFunctions())
                {
                    var41 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);

                    if (var41 > 0.01D)
                    {
                        double var43 = 0.06D;
                        this.motionX += this.motionX / var41 * var43;
                        this.motionZ += this.motionZ / var41 * var43;
                    }
                    else if (var10 == 1)
                    {
                        if (this.worldObj.isBlockNormalCube(var1 - 1, var2, var3))
                        {
                            this.motionX = 0.02D;
                        }
                        else if (this.worldObj.isBlockNormalCube(var1 + 1, var2, var3))
                        {
                            this.motionX = -0.02D;
                        }
                    }
                    else if (var10 == 0)
                    {
                        if (this.worldObj.isBlockNormalCube(var1, var2, var3 - 1))
                        {
                            this.motionZ = 0.02D;
                        }
                        else if (this.worldObj.isBlockNormalCube(var1, var2, var3 + 1))
                        {
                            this.motionZ = -0.02D;
                        }
                    }
                }
            }
            else
            {
                moveMinecartOffRail(var1, var2, var3);
            }

            this.doBlockCollisions();
            this.rotationPitch = 0.0F;
            double var47 = this.prevPosX - this.posX;
            double var48 = this.prevPosZ - this.posZ;

            if (var47 * var47 + var48 * var48 > 0.001D)
            {
                this.rotationYaw = (float)(Math.atan2(var48, var47) * 180.0D / Math.PI);

                if (this.field_70499_f)
                {
                    this.rotationYaw += 180.0F;
                }
            }

            double var49 = (double)MathHelper.wrapAngleTo180_float(this.rotationYaw - this.prevRotationYaw);

            if (var49 < -170.0D || var49 >= 170.0D)
            {
                this.rotationYaw += 180.0F;
                this.field_70499_f = !this.field_70499_f;
            }

            this.setRotation(this.rotationYaw, this.rotationPitch);

            AxisAlignedBB box = null;
            if (getCollisionHandler() != null)
            {
                box = getCollisionHandler().getMinecartCollisionBox(this);
            }
            else
            {
                box = boundingBox.expand(0.2D, 0.0D, 0.2D);
            }

            List var15 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, box);

            if (var15 != null && !var15.isEmpty())
            {
                for (int var50 = 0; var50 < var15.size(); ++var50)
                {
                    Entity var17 = (Entity)var15.get(var50);

                    if (var17 != this.riddenByEntity && var17.canBePushed() && var17 instanceof EntityMinecart)
                    {
                        var17.applyEntityCollision(this);
                    }
                }
            }

            if (this.riddenByEntity != null && this.riddenByEntity.isDead)
            {
                if (this.riddenByEntity.ridingEntity == this)
                {
                    this.riddenByEntity.ridingEntity = null;
                }

                this.riddenByEntity = null;
            }

            updateFuel();
            MinecraftForge.EVENT_BUS.post(new MinecartUpdateEvent(this, var1, var2, var3));
        }
    }

}
