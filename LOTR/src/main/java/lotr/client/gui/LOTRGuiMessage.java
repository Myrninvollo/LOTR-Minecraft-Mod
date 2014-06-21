package lotr.client.gui;

import lotr.common.LOTRGuiMessageTypes;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

public class LOTRGuiMessage extends GuiScreen
{
	private static ResourceLocation guiTexture = new ResourceLocation("lotr:gui/message.png");

	private LOTRGuiMessageTypes type;
	public int xSize = 240;
    public int ySize = 160;
	private int border = 10;
    private int guiLeft;
    private int guiTop;
	private int buttonTimer = 60;
	
	public LOTRGuiMessage(LOTRGuiMessageTypes t)
	{
		type = t;
	}
	
	@Override
    public void initGui()
    {
        guiLeft = (width - xSize) / 2;
		guiTop = (height - ySize) / 2;
		buttonList.add(new GuiButton(0, guiLeft + xSize / 2 - 40, guiTop + ySize + 20, 80, 20, "Dismiss"));
	}
	
	@Override
	public void drawScreen(int i, int j, float f)
	{
		drawDefaultBackground();
		
		mc.getTextureManager().bindTexture(guiTexture);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		String s = type.getMessage();
		fontRendererObj.drawSplitString(s, guiLeft + border, guiTop + border, xSize - (border * 2), 0x404040);
		s = StatCollector.translateToLocal("lotr.gui.message.notDisplayedAgain");
		fontRendererObj.drawString(s, guiLeft + border, guiTop + ySize - (border / 2) - fontRendererObj.FONT_HEIGHT, 0x6C6C6C);
		
		if (buttonTimer > 0)
		{
			buttonTimer--;
		}
		
		((GuiButton)buttonList.get(0)).enabled = buttonTimer == 0;
		
		super.drawScreen(i, j, f);
	}
	
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
    protected void actionPerformed(GuiButton button)
    {
        if (button.enabled)
        {
			if (button.id == 0)
			{
				mc.thePlayer.closeScreen();
			}
		}
	}
	
	@Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }
}
