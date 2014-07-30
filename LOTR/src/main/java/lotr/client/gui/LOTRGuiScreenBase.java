package lotr.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

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
}
