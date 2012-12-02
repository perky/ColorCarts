package ljdp.colorcarts.client;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiButton;

@SideOnly(Side.CLIENT)
public class GuiColorToggle extends GuiButton {
	
	public boolean active;
	
	public GuiColorToggle(int id, int x, int y, boolean active) {
		super(id, x, y, 16, 16, "NULL");
		this.active = active;
	}
	
	public boolean onToggle() {
		active = !active;
		return active;
	}
	
	public void drawButton(Minecraft par1Minecraft, int par2, int par3) {
		if(this.drawButton) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, par1Minecraft.renderEngine.getTexture(ljdp.colorcarts.common.CommonProxy.COLORBUTTONS_PNG));
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            int u = this.id * 16;
            int v = this.active ? 0 : 16;
            drawTexturedModalRect(this.xPosition, this.yPosition, u, v, 16, 16);
		}
	}

}
