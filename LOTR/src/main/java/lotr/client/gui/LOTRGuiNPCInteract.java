package lotr.client.gui;

import lotr.common.entity.npc.LOTREntityNPC;
import net.minecraft.client.gui.GuiScreen;

public abstract class LOTRGuiNPCInteract extends LOTRGuiScreenBase
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
		
		if (theEntity == null || !theEntity.isEntityAlive() || theEntity.getDistanceSqToEntity(mc.thePlayer) > 100D)
        {
            mc.thePlayer.closeScreen();
        }
	}
}
