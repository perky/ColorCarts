package ljdp.colorcarts.common;

import org.lwjgl.opengl.GL11;

public enum EnumCartColor {
	BLACK ("Black", 0.08F, 0.08F, 0.08F), 
	RED ("Red", 1.0F, 0.0F, 0.0F), 
	GREEN ("Green", 0.0F, 0.5F, 0.0F), 
	BROWN ("Brown", 0.4F, 0.2F, 0.0F), 
	BLUE ("Blue", 0.0F, 0.0F, 1.0F), 
	PURPLE ("Purple", 0.4F, 0.0F, 0.8F), 
	CYAN ("Cyan", 0.0F, 1.0F, 1.0F), 
	LIGHTGRAY ("Light Gray", 0.6F, 0.6F, 0.6F), 
	GRAY ("Gray", 0.3F, 0.3F, 0.3F), 
	PINK ("Pink", 1.0F, 0.5F, 0.8F), 
	LIME ("Lime", 0.0F, 1.0F, 0.0F), 
	YELLOW ("Yellow", 1.0F, 1.0F, 0.0F),
	LIGHTBLUE ("Light Blue", 0.4F, 0.7F, 1.0F),
	MAGENTA ("Magenta", 1.0F, 0.0F, 1.0F),
	ORANGE ("Orange", 1.0F, 0.5F, 0.0F),
	WHITE ("White", 1.0F, 1.0F, 1.0F);
	
	private final String descriptiveName;
	private final float red;
	private final float blue;
	private final float green;
	EnumCartColor(String name, float r, float g, float b) {
		this.descriptiveName = name;
		this.red = r;
		this.green = g;
		this.blue = b;
	}
	
	public String descriptiveName() {
		return descriptiveName;
	}
	
	public float getRed() {
		return red;
	}
	
	public float getGreen() {
		return green;
	}
	
	public float getBlue() {
		return blue;
	}
}