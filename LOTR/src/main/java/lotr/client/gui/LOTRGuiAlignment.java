package lotr.client.gui;

import lotr.client.LOTRClientProxy;
import lotr.client.LOTRTickHandlerClient;
import lotr.common.LOTRFaction;
import lotr.common.LOTRLevelData;
import net.minecraft.util.StatCollector;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class LOTRGuiAlignment extends LOTRGui
{
	private static int currentFactionIndex = 0;
	private LOTRFaction currentFaction = LOTRFaction.values()[currentFactionIndex];
	private static final int maxAlignmentsDisplayed = 5;
	
	@Override
    public void initGui()
    {
		xSize = 220;
		super.initGui();
		
		currentFactionIndex = Math.min(LOTRTickHandlerClient.currentAlignmentFaction.ordinal(), LOTRFaction.totalPlayerFactions - maxAlignmentsDisplayed);
    }
	
	@Override
    public void updateScreen()
    {
        super.updateScreen();
		
        updateCurrentFaction();
    }
	
	private void updateCurrentFaction()
	{
		currentFaction = LOTRFaction.values()[currentFactionIndex];
	}
	
	@Override
	public void drawScreen(int i, int j, float f)
	{
		drawDefaultBackground();
		GL11.glColor4f(1F, 1F, 1F, 1F);

		String s = StatCollector.translateToLocal("lotr.gui.alignment.title");
		fontRendererObj.drawString(s, guiLeft + xSize / 2 - fontRendererObj.getStringWidth(s) / 2, guiTop - 30, 0xFFFFFF);

		super.drawScreen(i, j, f);
		
		int x = guiLeft + xSize / 2;
		int y = guiTop;
		for (int l = currentFactionIndex; l < currentFactionIndex + maxAlignmentsDisplayed; l++)
		{
			if (l >= LOTRFaction.values().length)
			{
				break;
			}
			
			LOTRFaction faction = LOTRFaction.values()[l];
			
			if (!faction.allowPlayer)
			{
				continue;
			}
			
			int alignment = LOTRLevelData.getData(mc.thePlayer).getAlignment(faction);
			
			GL11.glColor4f(1F, 1F, 1F, 1F);
			mc.getTextureManager().bindTexture(LOTRClientProxy.alignmentTexture);
	        drawTexturedModalRect(x - 116, y, 0, 0, 232, 18);
	        
	        GL11.glColor4f(faction.factionColors[0], faction.factionColors[1], faction.factionColors[2], 1F);
	        drawTexturedModalRect(x - 116, y, 0, 34, 232, 18);

	        GL11.glColor4f(1F, 1F, 1F, 1F);
			drawTexturedModalRect(x - 8 + LOTRTickHandlerClient.calculateAlignmentDisplay(alignment), y + 1, 0, 18, 16, 16);
			
			y += 22;
			s = faction.factionName();
			LOTRTickHandlerClient.drawTextWithShadow(fontRendererObj, x - fontRendererObj.getStringWidth(s) / 2, y, s, 1F);
			
			int max = LOTRTickHandlerClient.calculateMaxDisplayValue(alignment);
			String sMax = "+" + String.valueOf(max);
			String sMin = "-" + String.valueOf(max);
			GL11.glScalef(0.5F, 0.5F, 0.5F);
			int xMax = (x * 2) - 220 - fontRendererObj.getStringWidth(sMax) / 2;
			int xMin = (x * 2) + 220 - fontRendererObj.getStringWidth(sMax) / 2;
			y *= 2;
			LOTRTickHandlerClient.drawTextWithShadow(fontRendererObj, xMax, y, sMax, 1F);
			LOTRTickHandlerClient.drawTextWithShadow(fontRendererObj, xMin, y, sMin, 1F);
			GL11.glScalef(2F, 2F, 2F);
			y /= 2;
			
			y += fontRendererObj.FONT_HEIGHT + 4;
			s = String.valueOf(alignment);
			if (alignment > 0)
			{
				s = "+" + s;
			}
			LOTRTickHandlerClient.drawTextWithShadow(fontRendererObj, x - fontRendererObj.getStringWidth(s) / 2, y, s, 1F);
				
			y += 20;
		}
	}
	
	@Override
    protected void keyTyped(char c, int i)
    {
		if (i == Keyboard.KEY_DOWN)
		{
			increaseFactionIndex();
			return;
		}
		
		if (i == Keyboard.KEY_UP)
		{
			decreaseFactionIndex();
			return;
		}
		
		super.keyTyped(c, i);
	}
	
	@Override
    public void handleMouseInput()
    {
        super.handleMouseInput();
        int i = Mouse.getEventDWheel();

        if (i != 0)
        {
            if (i < 0)
            {
            	increaseFactionIndex();
            }

            if (i > 0)
            {
            	decreaseFactionIndex();
            }
        }
    }
	
	private void decreaseFactionIndex()
	{
		currentFactionIndex = Math.max(currentFactionIndex - 1, 0);
	}
	
	private void increaseFactionIndex()
	{
		currentFactionIndex = Math.min(currentFactionIndex + 1, LOTRFaction.totalPlayerFactions - maxAlignmentsDisplayed);
	}
}
