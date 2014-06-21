package lotr.client.gui;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lotr.common.entity.npc.LOTREntityNPC;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.util.StatCollector;

public class LOTRGuiTradeUnitTradeInteract extends LOTRGuiNPCInteract
{
	public LOTRGuiTradeUnitTradeInteract(LOTREntityNPC entity)
	{
		super(entity);
	}
	
	@Override
    public void initGui()
    {
		buttonList.add(new GuiButton(0, (width / 2) - 100, (height / 5 * 3), 60, 20, StatCollector.translateToLocal("lotr.gui.npc.talk")));
		buttonList.add(new GuiButton(1, (width / 2) - 30, (height / 5 * 3), 60, 20, StatCollector.translateToLocal("lotr.gui.npc.trade")));
		buttonList.add(new GuiButton(2, (width / 2) + 40, (height / 5 * 3), 60, 20, StatCollector.translateToLocal("lotr.gui.npc.hire")));
	}
	
	@Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.enabled)
        {
        	ByteBuf data = Unpooled.buffer();
        	
        	data.writeInt(mc.thePlayer.getEntityId());
        	data.writeByte((byte)mc.thePlayer.dimension);
        	data.writeInt(theEntity.getEntityId());
        	
        	if (button.id == 2)
        	{
        		data.writeByte((byte)1);
        		
	    		C17PacketCustomPayload packet = new C17PacketCustomPayload("lotr.utInteract", data);
	    		mc.thePlayer.sendQueue.addToSendQueue(packet);
        	}
        	else
        	{
	        	data.writeByte((byte)button.id);
	
	    		C17PacketCustomPayload packet = new C17PacketCustomPayload("lotr.tInteract", data);
	    		mc.thePlayer.sendQueue.addToSendQueue(packet);
        	}
		}
	}
}
