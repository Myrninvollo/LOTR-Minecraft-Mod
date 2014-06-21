package lotr.client.gui;

import java.util.Iterator;
import java.util.List;

import lotr.common.LOTRAbstractWaypoint;
import lotr.common.entity.npc.LOTRSpeech;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.StatCollector;
import net.minecraft.world.chunk.EmptyChunk;

public class LOTRGuiFastTravel extends GuiScreen
{
	private LOTRAbstractWaypoint theWaypoint;
	private String message;
	
    public LOTRGuiFastTravel(LOTRAbstractWaypoint waypoint)
    {
        theWaypoint = waypoint;
		message = LOTRSpeech.getRandomSpeech("fastTravel");
    }
	
	@Override
    public void updateScreen()
    {
		int chunkX = theWaypoint.getXCoord() >> 4;
		int chunkZ = theWaypoint.getZCoord() >> 4;
		if (!(mc.theWorld.getChunkProvider().provideChunk(chunkX, chunkZ) instanceof EmptyChunk))
		{
			mc.displayGuiScreen(null);
		}
    }

    @Override
    protected void keyTyped(char c, int i) {}

    @Override
    public void drawScreen(int i, int j, float f)
    {
        drawBackground(0);
		
		String s = StatCollector.translateToLocalFormatted("lotr.gui.fastTravel.travel", new Object[] {theWaypoint.getDisplayName()});
		
        drawCenteredString(fontRendererObj, s, width / 2, height / 2 - 50, 0xFFFFFF);
		
		List messageParts = fontRendererObj.listFormattedStringToWidth(message, width - 100);
		int y = height - 50 - (messageParts.size() * fontRendererObj.FONT_HEIGHT);
        for (Iterator iterator = messageParts.iterator(); iterator.hasNext(); y += fontRendererObj.FONT_HEIGHT)
        {
            String s1 = (String)iterator.next();
            drawCenteredString(fontRendererObj, s1, width / 2, y, 0xFFFFFF);
        }

        super.drawScreen(i, j, f);
    }
	
	@Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }
}
