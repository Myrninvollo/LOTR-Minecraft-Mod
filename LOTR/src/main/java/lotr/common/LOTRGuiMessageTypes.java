package lotr.common;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.*;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.S3FPacketCustomPayload;
import net.minecraft.util.StatCollector;

public enum LOTRGuiMessageTypes
{
	FRIENDLY_FIRE("friendlyFire"),
	UTUMNO_WARN("utumnoWarn");
	
	public final String messageName;
	
	private LOTRGuiMessageTypes(String s)
	{
		messageName = s;
	}
	
	public String getMessage()
	{
		return StatCollector.translateToLocal("lotr.gui.message." + messageName);
	}
	
	public String getSaveName()
	{
		return messageName;
	}
	
	public static LOTRGuiMessageTypes forSaveName(String name)
	{
		for (LOTRGuiMessageTypes message : values())
		{
			if (message.getSaveName().equals(name))
			{
				return message;
			}
		}
		return null;
	}
}
