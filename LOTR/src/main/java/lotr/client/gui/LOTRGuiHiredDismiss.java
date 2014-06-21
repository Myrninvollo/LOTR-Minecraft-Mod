package lotr.client.gui;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lotr.common.entity.npc.LOTREntityNPC;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.util.StatCollector;

public class LOTRGuiHiredDismiss extends LOTRGuiNPCInteract
{
	public LOTRGuiHiredDismiss(LOTREntityNPC entity)
	{
		super(entity);
	}
	
	@Override
    public void initGui()
    {
		buttonList.add(new GuiButton(0, (width / 2) - 65, (height / 5 * 3) + 40, 60, 20, StatCollector.translateToLocal("lotr.gui.dismiss.dismiss")));
		buttonList.add(new GuiButton(1, (width / 2) + 5, (height / 5 * 3) + 40, 60, 20, StatCollector.translateToLocal("lotr.gui.dismiss.cancel")));
	}
	
	@Override
	public void drawScreen(int i, int j, float f)
	{
		super.drawScreen(i, j, f);
		
		String s = StatCollector.translateToLocal("lotr.gui.dismiss.warning1");
		fontRendererObj.drawString(s, (width - fontRendererObj.getStringWidth(s)) / 2, (height / 5 * 3), 0xFFFFFF);
		
		s = StatCollector.translateToLocal("lotr.gui.dismiss.warning2");
		fontRendererObj.drawString(s, (width - fontRendererObj.getStringWidth(s)) / 2, (height / 5 * 3) + fontRendererObj.FONT_HEIGHT, 0xFFFFFF);
	}
	
	@Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.enabled)
        {
        	if (button.id == 1)
        	{
        		mc.displayGuiScreen(new LOTRGuiHiredInteract(theEntity));
        		return;
        	}
        	
        	ByteBuf data = Unpooled.buffer();
        	
        	data.writeInt(mc.thePlayer.getEntityId());
        	data.writeByte((byte)mc.thePlayer.dimension);
        	data.writeInt(theEntity.getEntityId());
        	data.writeByte((byte)button.id);
        	
        	C17PacketCustomPayload packet = new C17PacketCustomPayload("lotr.hDismiss", data);
        	mc.thePlayer.sendQueue.addToSendQueue(packet);
		}
	}
}
