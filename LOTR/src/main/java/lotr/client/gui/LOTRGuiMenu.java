package lotr.client.gui;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lotr.client.LOTRTickHandlerClient;
import lotr.common.LOTRCommonProxy;
import lotr.common.LOTRMod;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.util.StatCollector;

public abstract class LOTRGuiMenu extends LOTRGuiScreenBase
{
	public static int guiIndex = 0;
	public static Class[] guiClasses = {LOTRGuiMap.class, LOTRGuiAlignment.class, LOTRGuiAchievements.class, LOTRGuiTitles.class, LOTRGuiShields.class, LOTRGuiOptions.class};
	private static String[] guiTitles = {"map", "alignment", "achievements", "titles", "shields", "options"};
	private static String[] guiTitlesTranslated;
	public static RenderItem renderItem = new RenderItem();
	
	public int xSize = 200;
    public int ySize = 256;
    public int guiLeft;
    public int guiTop;
	private boolean sentCheckPacket = false;
	private GuiButton pageLeft;
	private GuiButton pageRight;
	
	private void setGuiTitles()
	{
		guiTitlesTranslated = new String[guiTitles.length];
		for (int i = 0; i < guiTitles.length; i++)
		{
			guiTitlesTranslated[i] = StatCollector.translateToLocal("lotr.gui." + guiTitles[i]);
		}
	}
	
	@Override
    public void initGui()
    {
		setGuiTitles();
		
        guiLeft = (width - xSize) / 2;
		guiTop = (height - ySize) / 2;
		
		int i = 120;
		int j = 20;
		buttonList.add(pageLeft = new LOTRGuiButtonLeftRight(1000, true, guiLeft - 160, guiTop + (ySize + j) / 4, ""));
		buttonList.add(pageRight = new LOTRGuiButtonLeftRight(1001, false, guiLeft + xSize + 160 - i, guiTop + (ySize + j) / 4, ""));
		
		if (guiIndex == 0)
		{
			pageLeft.displayString = guiTitlesTranslated[guiClasses.length - 1];
		}
		else
		{
			pageLeft.displayString = guiTitlesTranslated[guiIndex - 1];
		}
		
		if (guiIndex == guiClasses.length - 1)
		{
			pageRight.displayString = guiTitlesTranslated[0];
		}
		else
		{
			pageRight.displayString = guiTitlesTranslated[guiIndex + 1];
		}
	}
	
	@Override
	public void updateScreen()
	{
		if (!sentCheckPacket)
		{
			LOTRTickHandlerClient.renderMenuPrompt = false;
			
        	ByteBuf data = Unpooled.buffer();
        	
        	data.writeInt(mc.thePlayer.getEntityId());
        	data.writeByte((byte)mc.thePlayer.dimension);
        	
        	C17PacketCustomPayload packet = new C17PacketCustomPayload("lotr.checkMenu", data);
        	mc.thePlayer.sendQueue.addToSendQueue(packet);
        	
        	sentCheckPacket = true;
		}
		
		super.updateScreen();
	}
	
	@Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.enabled)
        {
			boolean flag = false;
			
			if (button == pageLeft)
			{
				guiIndex--;
				if (guiIndex < 0)
				{
					guiIndex = guiClasses.length - 1;
				}
				flag = true;
			}
			
			else if (button == pageRight)
			{
				guiIndex++;
				if (guiIndex >= guiClasses.length)
				{
					guiIndex = 0;
				}
				flag = true;
			}
			
			if (flag)
			{
				mc.thePlayer.openGui(LOTRMod.instance, LOTRCommonProxy.GUI_ID_LOTR, mc.theWorld, 0, 0, 0);
			}
		}
	}
}
