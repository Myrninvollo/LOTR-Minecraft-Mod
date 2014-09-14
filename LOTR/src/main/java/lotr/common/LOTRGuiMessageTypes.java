package lotr.common;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

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
	
	private String message;
	public Set playersRecievedMessage = new HashSet();
	
	private LOTRGuiMessageTypes(String s)
	{
		message = s;
	}
	
	public String getMessage()
	{
		return StatCollector.translateToLocal("lotr.gui.message." + message);
	}
	
	public void sendMessageIfNotReceived(EntityPlayer entityplayer)
	{
		if (entityplayer.worldObj.isRemote)
		{
			return;
		}
		
		if (!playersRecievedMessage.contains(entityplayer.getUniqueID()))
		{
			playersRecievedMessage.add(entityplayer.getUniqueID());
			
			ByteBuf data = Unpooled.buffer();
			
			data.writeByte((byte)ordinal());
			
			((EntityPlayerMP)entityplayer).playerNetServerHandler.sendPacket(new S3FPacketCustomPayload("lotr.message", data));
		}
	}
	
	public static void saveAll(NBTTagCompound levelData)
	{
		NBTTagList tags = new NBTTagList();
		
		for (LOTRGuiMessageTypes type : LOTRGuiMessageTypes.values())
		{
			NBTTagCompound typeData = new NBTTagCompound();
			typeData.setString("Type", type.name());
			NBTTagList playerList = new NBTTagList();
			Iterator i = type.playersRecievedMessage.iterator();
			while (i.hasNext())
			{
				Object obj = i.next();
				if (obj instanceof UUID)
				{
					NBTTagCompound nbt = new NBTTagCompound();
					nbt.setLong("UUIDMost", ((UUID)obj).getMostSignificantBits());
					nbt.setLong("UUIDLeast", ((UUID)obj).getLeastSignificantBits());
					playerList.appendTag(nbt);
				}
			}
			typeData.setTag("Players", playerList);
			tags.appendTag(typeData);
		}
		
		levelData.setTag("Messages", tags);
	}
	
	public static void loadAll(NBTTagCompound levelData)
	{
		for (LOTRGuiMessageTypes type : LOTRGuiMessageTypes.values())
		{
			type.playersRecievedMessage.clear();
		}
		
		NBTTagList tags = levelData.getTagList("Messages", new NBTTagCompound().getId());
		if (tags != null)
		{
			for (int i = 0; i < tags.tagCount(); i++)
			{
				NBTTagCompound typeData = (NBTTagCompound)tags.getCompoundTagAt(i);
				String name = typeData.getString("Type");
				LOTRGuiMessageTypes type = null;
				
				for (LOTRGuiMessageTypes t : values())
				{
					if (t.name().equals(name))
					{
						type = t;
						break;
					}
				}
				
				if (type != null)
				{
					NBTTagList playerList = typeData.getTagList("Players", new NBTTagCompound().getId());
					if (playerList != null)
					{
						for (int j = 0; j < playerList.tagCount(); j++)
						{
							NBTTagCompound nbt = (NBTTagCompound)playerList.getCompoundTagAt(j);
							type.playersRecievedMessage.add(new UUID(nbt.getLong("UUIDMost"), nbt.getLong("UUIDLeast")));
						}
					}
				}
			}
		}
	}
}
