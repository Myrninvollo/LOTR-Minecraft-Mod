package lotr.client.gui;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lotr.common.LOTRLevelData;
import lotr.common.LOTROptions;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

public class LOTRGuiOptions extends LOTRGuiMenu
{
	private LOTRGuiButtonOptions buttonFriendlyFire;
	private LOTRGuiButtonOptions buttonHiredDeathMessages;
	private LOTRGuiButtonOptions buttonShield;
	private LOTRGuiButtonOptions buttonAlignment;
	private LOTRGuiButtonOptions buttonMapLocation;
	
	@Override
    public void initGui()
    {
		super.initGui();
		buttonList.add(buttonFriendlyFire = new LOTRGuiButtonOptions(LOTROptions.FRIENDLY_FIRE, guiLeft + xSize / 2 - 100, guiTop + 40, 200, 20, "lotr.gui.options.friendlyFire"));
		buttonList.add(buttonHiredDeathMessages = new LOTRGuiButtonOptions(LOTROptions.HIRED_DEATH_MESSAGES, guiLeft + xSize / 2 - 100, guiTop + 64, 200, 20, "lotr.gui.options.hiredDeathMessages"));
		buttonList.add(buttonShield = new LOTRGuiButtonOptions(LOTROptions.ENABLE_SHIELD, guiLeft + xSize / 2 - 100, guiTop + 88, 200, 20, "lotr.gui.options.enableShield"));
		buttonList.add(buttonAlignment = new LOTRGuiButtonOptions(LOTROptions.SHOW_ALIGNMENT, guiLeft + xSize / 2 - 100, guiTop + 112, 200, 20, "lotr.gui.options.showAlignment"));
		buttonList.add(buttonMapLocation = new LOTRGuiButtonOptions(LOTROptions.SHOW_MAP_LOCATION, guiLeft + xSize / 2 - 100, guiTop + 136, 200, 20, "lotr.gui.options.showMapLocation"));
	}
	
	@Override
	public void drawScreen(int i, int j, float f)
	{
		drawDefaultBackground();
		GL11.glColor4f(1F, 1F, 1F, 1F);
		String s = StatCollector.translateToLocal("lotr.gui.options.title");
		fontRendererObj.drawString(s, guiLeft + 100 - fontRendererObj.getStringWidth(s) / 2, guiTop - 30, 0xFFFFFF);
		s = StatCollector.translateToLocal("lotr.gui.options.worldSettings");
		fontRendererObj.drawString(s, guiLeft + 100 - fontRendererObj.getStringWidth(s) / 2, guiTop + 10, 0xFFFFFF);

		buttonFriendlyFire.setState(LOTRLevelData.getData(mc.thePlayer).getFriendlyFire());
		buttonHiredDeathMessages.setState(LOTRLevelData.getData(mc.thePlayer).getEnableHiredDeathMessages());
		buttonShield.setState(LOTRLevelData.getData(mc.thePlayer).getEnableShield());
		buttonAlignment.setState(!LOTRLevelData.getData(mc.thePlayer).getHideAlignment());
		buttonMapLocation.setState(!LOTRLevelData.getData(mc.thePlayer).getHideMapLocation());
		
		super.drawScreen(i, j, f);
		
		for (int k = 0; k < buttonList.size(); k++)
		{
			GuiButton button = (GuiButton)buttonList.get(k);
			if (button instanceof LOTRGuiButtonOptions)
			{
				((LOTRGuiButtonOptions)button).drawTooltip(mc, i, j);
			}
		}
	}
	
	@Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.enabled)
        {
			if (button instanceof LOTRGuiButtonOptions)
			{
	        	ByteBuf data = Unpooled.buffer();
	        	
	        	data.writeInt(mc.thePlayer.getEntityId());
	        	data.writeByte((byte)mc.thePlayer.dimension);
	        	data.writeByte((byte)button.id);
	        	
	        	C17PacketCustomPayload packet = new C17PacketCustomPayload("lotr.setOption", data);
	        	mc.thePlayer.sendQueue.addToSendQueue(packet);
			}
			
			else
			{
				super.actionPerformed(button);
			}
		}
	}
}
