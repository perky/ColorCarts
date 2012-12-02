package ljdp.colorcarts.client;

import ljdp.colorcarts.common.EnumCartColor;
import ljdp.colorcarts.common.CommonProxy;
import ljdp.colorcarts.common.EntityColorMinecart;

import org.lwjgl.opengl.GL11;

import net.minecraft.src.Block;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityMinecart;
import net.minecraft.src.MathHelper;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.RenderMinecart;
import net.minecraft.src.Vec3;

public class RenderColorMinecart extends RenderMinecart {

	public RenderColorMinecart() {
		super();
	}
	
	public void setColor(EnumCartColor color)
	{
		GL11.glColor4f(color.getRed(), color.getGreen(), color.getBlue(), 1.0F);
	}
	
	/**
     * Renders the Minecart.
     */
	public void renderTheColorMinecart(EntityColorMinecart entityColorMinecart, double par2, double par4, double par6, float par8, float par9)
    {
        GL11.glPushMatrix();
        long var10 = (long)entityColorMinecart.entityId * 493286711L;
        var10 = var10 * var10 * 4392167121L + var10 * 98761L;
        float var12 = (((float)(var10 >> 16 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        float var13 = (((float)(var10 >> 20 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        float var14 = (((float)(var10 >> 24 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        GL11.glTranslatef(var12, var13, var14);
        double var15 = entityColorMinecart.lastTickPosX + (entityColorMinecart.posX - entityColorMinecart.lastTickPosX) * (double)par9;
        double var17 = entityColorMinecart.lastTickPosY + (entityColorMinecart.posY - entityColorMinecart.lastTickPosY) * (double)par9;
        double var19 = entityColorMinecart.lastTickPosZ + (entityColorMinecart.posZ - entityColorMinecart.lastTickPosZ) * (double)par9;
        double var21 = 0.30000001192092896D;
        Vec3 var23 = entityColorMinecart.func_70489_a(var15, var17, var19);
        float var24 = entityColorMinecart.prevRotationPitch + (entityColorMinecart.rotationPitch - entityColorMinecart.prevRotationPitch) * par9;

        if (var23 != null)
        {
            Vec3 var25 = entityColorMinecart.func_70495_a(var15, var17, var19, var21);
            Vec3 var26 = entityColorMinecart.func_70495_a(var15, var17, var19, -var21);

            if (var25 == null)
            {
                var25 = var23;
            }

            if (var26 == null)
            {
                var26 = var23;
            }

            par2 += var23.xCoord - var15;
            par4 += (var25.yCoord + var26.yCoord) / 2.0D - var17;
            par6 += var23.zCoord - var19;
            Vec3 var27 = var26.addVector(-var25.xCoord, -var25.yCoord, -var25.zCoord);

            if (var27.lengthVector() != 0.0D)
            {
                var27 = var27.normalize();
                par8 = (float)(Math.atan2(var27.zCoord, var27.xCoord) * 180.0D / Math.PI);
                var24 = (float)(Math.atan(var27.yCoord) * 73.0D);
            }
        }

        GL11.glTranslatef((float)par2, (float)par4, (float)par6);
        GL11.glRotatef(180.0F - par8, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-var24, 0.0F, 0.0F, 1.0F);
        float var28 = (float)entityColorMinecart.func_70496_j() - par9;
        float var30 = (float)entityColorMinecart.getDamage() - par9;

        if (var30 < 0.0F)
        {
            var30 = 0.0F;
        }

        if (var28 > 0.0F)
        {
            GL11.glRotatef(MathHelper.sin(var28) * var28 * var30 / 10.0F * (float)entityColorMinecart.func_70493_k(), 1.0F, 0.0F, 0.0F);
        }
        
        int minecartType = entityColorMinecart.getMinecartType();
        if (minecartType != 3)
        {
            this.loadTexture("/terrain.png");
            float var29 = 0.75F;
            GL11.glScalef(var29, var29, var29);

            if (minecartType == 1)
            {
                GL11.glTranslatef(0.0F, 0.5F, 0.0F);
                (new RenderBlocks()).renderBlockAsItem(Block.chest, 0, entityColorMinecart.getBrightness(par9));
                GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
                GL11.glTranslatef(0.5F, 0.0F, -0.5F);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            }
            else if (minecartType == 2)
            {
                GL11.glTranslatef(0.0F, 0.3125F, 0.0F);
                (new RenderBlocks()).renderBlockAsItem(Block.stoneOvenIdle, 0, entityColorMinecart.getBrightness(par9));
                GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
                GL11.glTranslatef(0.0F, -0.3125F, 0.0F);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            }
            
            GL11.glScalef(1.0F / var29, 1.0F / var29, 1.0F / var29);
        }
        
        this.loadTexture(ljdp.colorcarts.common.CommonProxy.CART_PNG);
        GL11.glScalef(-1.0F, -1.0F, 1.0F);
        this.setColor(entityColorMinecart.getMinecartColor());
        this.modelMinecart.render(entityColorMinecart, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        GL11.glPopMatrix();
    }
	
	@Override
	public void doRender(Entity entity, double par2, double par4,
			double par6, float par8, float par9) {
		this.renderTheColorMinecart((EntityColorMinecart)entity, par2, par4, par6, par8, par9);
	}

}
