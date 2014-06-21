package lotr.client.gui;

import lotr.common.entity.npc.LOTREntityNPC;
import net.minecraft.client.gui.GuiScreen;

public abstract class LOTRGuiNPCInteract extends GuiScreen
{
	public LOTREntityNPC theEntity;
	
	public LOTRGuiNPCInteract(LOTREntityNPC entity)
	{
		theEntity = entity;
	}
	
	@Override
	public void drawScreen(int i, int j, float f)
	{
		drawDefaultBackground();
		
		String s = theEntity.getCommandSenderName();
		fontRendererObj.drawString(s, (width - fontRendererObj.getStringWidth(s)) / 2, (height / 5 * 3) - 20, 0xFFFFFF);

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
		
		if (theEntity == null || !theEntity.isEntityAlive() || theEntity.getDistanceSqToEntity(mc.thePlayer) > 100D)
        {
            mc.thePlayer.closeScreen();
        }
	}
	
	@Override
    protected void keyTyped(char c, int i)
    {
        if (i == 1 || i == mc.gameSettings.keyBindInventory.getKeyCode())
        {
            mc.thePlayer.closeScreen();
        }
	}
	
	@Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }
}
