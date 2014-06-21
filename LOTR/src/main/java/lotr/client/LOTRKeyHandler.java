package lotr.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lotr.common.LOTRCommonProxy;
import lotr.common.LOTRFaction;
import lotr.common.LOTRMod;
import lotr.common.entity.animal.LOTREntityCamel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.play.client.C17PacketCustomPayload;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;

public class LOTRKeyHandler
{
	public static KeyBinding keyBindingMenu = new KeyBinding("Menu", Keyboard.KEY_L, "LOTR");
	public static KeyBinding keyBindingMapTeleport = new KeyBinding("Map Teleport", Keyboard.KEY_M, "LOTR");
	public static KeyBinding keyBindingFastTravel = new KeyBinding("Fast Travel", Keyboard.KEY_F, "LOTR");
	public static KeyBinding keyBindingAlignmentCycleLeft = new KeyBinding("Alignment Cycle Left", Keyboard.KEY_LEFT, "LOTR");
	public static KeyBinding keyBindingAlignmentCycleRight = new KeyBinding("Alignment Cycle Right", Keyboard.KEY_RIGHT, "LOTR");
	
	private Minecraft mc = Minecraft.getMinecraft();
	
	public LOTRKeyHandler()
	{
		FMLCommonHandler.instance().bus().register(this);
		
		ClientRegistry.registerKeyBinding(keyBindingMenu);
		ClientRegistry.registerKeyBinding(keyBindingMapTeleport);
		ClientRegistry.registerKeyBinding(keyBindingFastTravel);
		ClientRegistry.registerKeyBinding(keyBindingAlignmentCycleLeft);
		ClientRegistry.registerKeyBinding(keyBindingAlignmentCycleRight);
	}
	
	@SubscribeEvent
	public void KeyInputEvent(KeyInputEvent event)
	{
		boolean usedAlignmentKeys = false;
		
		if (keyBindingMenu.getIsKeyPressed() && mc.currentScreen == null)
		{
			mc.thePlayer.openGui(LOTRMod.instance, LOTRCommonProxy.GUI_ID_LOTR, mc.theWorld, 0, 0, 0);
		}
		
		if (keyBindingAlignmentCycleLeft.getIsKeyPressed() && mc.currentScreen == null)
		{
			int i = LOTRTickHandlerClient.currentAlignmentFaction.ordinal();
			if (i > 0)
			{
				i--;
			}
			else
			{
				i = LOTRFaction.values().length - 1;
				while (!LOTRFaction.values()[i].allowPlayer)
				{
					i--;
				}
			}
			
			LOTRTickHandlerClient.currentAlignmentFaction = LOTRFaction.values()[i];
			usedAlignmentKeys = true;
		}
		
		if (keyBindingAlignmentCycleRight.getIsKeyPressed() && mc.currentScreen == null)
		{
			int i = LOTRTickHandlerClient.currentAlignmentFaction.ordinal();
			if (i < LOTRFaction.values().length)
			{
				i++;
				while (!LOTRFaction.values()[i].allowPlayer)
				{
					i++;
					if (i >= LOTRFaction.values().length)
					{
						i = 0;
					}
				}
			}
			else
			{
				i = 0;
			}
			
			LOTRTickHandlerClient.currentAlignmentFaction = LOTRFaction.values()[i];
			usedAlignmentKeys = true;
		}
		
		if (usedAlignmentKeys && LOTRTickHandlerClient.renderAlignmentPrompt)
		{
			LOTRTickHandlerClient.renderAlignmentPrompt = false;
			
        	ByteBuf data = Unpooled.buffer();
        	
        	data.writeInt(mc.thePlayer.getEntityId());
        	data.writeByte((byte)mc.thePlayer.dimension);
        	
        	C17PacketCustomPayload packet = new C17PacketCustomPayload("lotr.checkAl", data);
        	mc.thePlayer.sendQueue.addToSendQueue(packet);
		}
		
		if (usedAlignmentKeys)
		{
        	ByteBuf data = Unpooled.buffer();
        	
        	data.writeInt(mc.thePlayer.getEntityId());
        	data.writeByte((byte)mc.thePlayer.dimension);
        	data.writeByte((byte)LOTRTickHandlerClient.currentAlignmentFaction.ordinal());
        	
        	C17PacketCustomPayload packet = new C17PacketCustomPayload("lotr.viewingF", data);
        	mc.thePlayer.sendQueue.addToSendQueue(packet);
		}
	}
}
