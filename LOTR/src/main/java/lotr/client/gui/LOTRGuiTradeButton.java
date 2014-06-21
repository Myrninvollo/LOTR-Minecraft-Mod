package lotr.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

import org.lwjgl.opengl.GL11;

public class LOTRGuiTradeButton extends GuiButton
{
	public LOTRGuiTradeButton(int i, int j, int k)
	{
		super(i, j, k, 18, 18, "Trade");
	}
	
	@Override
    public void drawButton(Minecraft mc, int i, int j)
    {
		GL11.glDisable(GL11.GL_LIGHTING);
		mc.getTextureManager().bindTexture(LOTRGuiTrade.guiTexture);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		field_146123_n = i >= xPosition && j >= yPosition && i < xPosition + width && j < yPosition + height;
		int hoverState = getHoverState(field_146123_n);
		drawTexturedModalRect(xPosition, yPosition, 176, hoverState * 18, width, height);
		mouseDragged(mc, i, j);
		GL11.glEnable(GL11.GL_LIGHTING);
	}
}
