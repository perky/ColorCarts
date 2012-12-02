package ljdp.colorcarts.client;

import ljdp.colorcarts.common.EnumCartColor;
import ljdp.colorcarts.common.TileEntityMinecartColorDetector;
import ljdp.colorcarts.common.network.PacketHandler;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.network.PacketDispatcher;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.Packet250CustomPayload;

public class GuiMinecartColorDetector extends GuiScreen {
	
	private EntityPlayer player;
	private TileEntityMinecartColorDetector detector;
	private final EnumCartColor[] colors = EnumCartColor.values();
	public final int panelWidth = 248;
	public final int panelHeight = 166;
	public int panelXPos;
	public int panelYPos;
	
	public GuiMinecartColorDetector(EntityPlayer player, TileEntityMinecartColorDetector tileEntity) {
		this.player = player;
		this.detector = tileEntity;
	}
	
	@Override
	public void initGui() {
		super.initGui();
		panelXPos = (width - panelWidth) / 2;
		panelYPos = (height - panelHeight) / 2;
		for(EnumCartColor color : EnumCartColor.values()) {
			int id = color.ordinal();
			int row = id < 8 ? 0 : 1;
			int x = (panelXPos + 48) + (20 * (id % 8));
			int y = (panelYPos + 60) + (20 * row);
			boolean active = detector.hasColor(color);
			controlList.add(new GuiColorToggle(id, x, y, active));
		}
	}
	
	private void drawBackgroundLayer() {
		int panel = mc.renderEngine.getTexture("/gui/demo_bg.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(panel);
		drawTexturedModalRect(panelXPos, panelYPos, 0, 0, panelWidth, panelHeight);
		fontRenderer.drawString("Minecart Color Detector", panelXPos + 64, panelYPos + 10, 4210752);
		fontRenderer.drawString("Tip: Sneak + click to place a rail on top.", panelXPos + 28, panelYPos + 145, 4210752);
	}
	
	@Override
	public void drawScreen(int par1, int par2, float par3) {
		drawBackgroundLayer();
		super.drawScreen(par1, par2, par3);
	}
	
	@Override
	protected void actionPerformed(GuiButton guiButton) {
		if(guiButton instanceof GuiColorToggle) {
			GuiColorToggle colorToggle = (GuiColorToggle)guiButton;
			boolean active = colorToggle.onToggle();
			if (active) {
				detector.addDetectorColor(colors[colorToggle.id]);
			} else {
				detector.removeDetectorColor(colors[colorToggle.id]);
			}
		}
	}
	
	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		Packet250CustomPayload packet = PacketHandler.getColorDetectorPacket(detector);
		PacketDispatcher.sendPacketToServer(packet);
	}
	
}
