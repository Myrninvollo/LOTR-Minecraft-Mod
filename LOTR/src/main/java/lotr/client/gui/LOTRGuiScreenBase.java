package lotr.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;

import org.lwjgl.input.Keyboard;

public abstract class LOTRGuiScreenBase extends GuiScreen
{
	@Override
	public void updateScreen()
	{
		super.updateScreen();
		
        if (!mc.thePlayer.isEntityAlive() || mc.thePlayer.isDead)
        {
            mc.thePlayer.closeScreen();
        }
	}
	
	@Override
    protected void keyTyped(char c, int i)
    {
        if (i == Keyboard.KEY_ESCAPE || i == mc.gameSettings.keyBindInventory.getKeyCode())
        {
            mc.thePlayer.closeScreen();
        }
	}
	
	@Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }
	
	protected void drawCenteredString(String s, int x, int y, int c)
	{
		fontRendererObj.drawString(s, x - fontRendererObj.getStringWidth(s) / 2, y, c);
	}
	
	@Override
    public void drawTexturedModalRect(int x, int y, int u, int v, int width, int height)
    {
		drawTexturedModalRect(x, y, u, v, width, height, 256);
    }
		
	public void drawTexturedModalRect(int x, int y, int u, int v, int width, int height, int imageWidth)
	{
        float f = 1F / (float)imageWidth;
        float f1 = 1F / (float)imageWidth;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x + 0, y + height, zLevel, (float)(u + 0) * f, (float)(v + height) * f1);
        tessellator.addVertexWithUV(x + width, y + height, zLevel, (float)(u + width) * f, (float)(v + height) * f1);
        tessellator.addVertexWithUV(x + width, y + 0, zLevel, (float)(u + width) * f, (float)(v + 0) * f1);
        tessellator.addVertexWithUV(x + 0, y + 0, zLevel, (float)(u + 0) * f, (float)(v + 0) * f1);
        tessellator.draw();
    }
}
