package lotr.client.gui;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lotr.common.entity.npc.LOTREntityNPC;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.util.StatCollector;

public class LOTRGuiHiredInteract extends LOTRGuiNPCInteract
{
	public LOTRGuiHiredInteract(LOTREntityNPC entity)
	{
		super(entity);
	}
	
	@Override
    public void initGui()
    {
		buttonList.add(new GuiButton(0, (width / 2) - 65, (height / 5 * 3), 60, 20, StatCollector.translateToLocal("lotr.gui.npc.talk")));
		buttonList.add(new GuiButton(1, (width / 2) + 5, (height / 5 * 3), 60, 20, StatCollector.translateToLocal("lotr.gui.npc.command")));
		buttonList.add(new GuiButton(2, (width / 2) - 65, (height / 5 * 3) + 30, 130, 20, StatCollector.translateToLocal("lotr.gui.npc.dismiss")));
		
		((GuiButton)buttonList.get(0)).enabled = theEntity.getSpeechBank(mc.thePlayer) != null;
	}
	
	@Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.enabled)
        {
        	if (button.id == 2)
        	{
        		mc.displayGuiScreen(new LOTRGuiHiredDismiss(theEntity));
        		return;
        	}
        	
    		ByteBuf data = Unpooled.buffer();
    		
    		data.writeInt(mc.thePlayer.getEntityId());
    		data.writeByte((byte)mc.thePlayer.dimension);
    		data.writeInt(theEntity.getEntityId());
    		data.writeByte((byte)button.id);

    		C17PacketCustomPayload packet = new C17PacketCustomPayload("lotr.hInteract", data);
    		mc.thePlayer.sendQueue.addToSendQueue(packet);
		}
	}
}
