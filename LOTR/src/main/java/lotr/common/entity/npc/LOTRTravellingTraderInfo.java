package lotr.common.entity.npc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;

public class LOTRTravellingTraderInfo
{
	private LOTREntityNPC theEntity;
	private LOTRTravellingTrader theTrader;
	public int timeUntilDespawn = -1;
	private List escortUUIDs = new ArrayList();
	
	public LOTRTravellingTraderInfo(LOTRTravellingTrader entity)
	{
		theEntity = (LOTREntityNPC)entity;
		theTrader = entity;
	}
	
	public void startVisiting(EntityPlayer entityplayer)
	{
		timeUntilDespawn = 24000;
		if (theEntity.worldObj.playerEntities.size() <= 1)
		{
			IChatComponent componentName = new ChatComponentText(theEntity.getCommandSenderName());
			componentName.getChatStyle().setColor(EnumChatFormatting.YELLOW);
			LOTRSpeech.messageAllPlayers(new ChatComponentTranslation("lotr.travellingTrader.arrive", new Object[] {componentName}));
		}
		else
		{
			IChatComponent componentName = new ChatComponentText(theEntity.getCommandSenderName());
			componentName.getChatStyle().setColor(EnumChatFormatting.YELLOW);
			LOTRSpeech.messageAllPlayers(new ChatComponentTranslation("lotr.travellingTrader.arriveMP", new Object[] {componentName, entityplayer.getCommandSenderName()}));
		}
		
		int i = MathHelper.floor_double(theEntity.posX);
		int j = MathHelper.floor_double(theEntity.boundingBox.minY);
		int k = MathHelper.floor_double(theEntity.posZ);
		theEntity.setHomeArea(i, j, k, 16);
		
		int escorts = 2 + theEntity.worldObj.rand.nextInt(3);
		for (int l = 0; l < escorts; l++)
		{
			LOTREntityNPC escort = theTrader.createTravellingEscort();
			if (escort != null)
			{
				escort.setLocationAndAngles(theEntity.posX, theEntity.posY, theEntity.posZ, theEntity.rotationYaw, theEntity.rotationPitch);
				escort.isNPCPersistent = true;
				escort.liftSpawnRestrictions = true;
				escort.spawnRidingHorse = false;
				theEntity.worldObj.spawnEntityInWorld(escort);
				escort.setHomeArea(i, j, k, 16);
				escortUUIDs.add(escort.getUniqueID());
			}
		}
	}
	
	private void removeEscorts()
	{
		for (Object obj : theEntity.worldObj.loadedEntityList)
		{
			Entity entity = (Entity)obj;
			UUID entityUUID = entity.getUniqueID();
			for (Object uuid : escortUUIDs)
			{
				if (entityUUID.equals(uuid))
				{
					entity.setDead();
				}
			}
		}
	}
	
	public void onUpdate()
	{
		if (!theEntity.worldObj.isRemote)
		{
			if (timeUntilDespawn > 0)
			{
				timeUntilDespawn--;
			}
			
			if (timeUntilDespawn == 2400)
			{
				LOTRSpeech.messageAllPlayers(LOTRSpeech.getNamedSpeech(theEntity, theTrader.getDepartureSpeech()));
			}
			
			if (timeUntilDespawn == 0)
			{
				IChatComponent componentName = new ChatComponentText(theEntity.getCommandSenderName());
				componentName.getChatStyle().setColor(EnumChatFormatting.YELLOW);
				LOTRSpeech.messageAllPlayers(new ChatComponentTranslation("lotr.travellingTrader.depart", new Object[] {componentName}));
				theEntity.setDead();
				removeEscorts();
			}
		}
	}
	
	public void onDeath()
	{
		if (!theEntity.worldObj.isRemote)
		{
			if (timeUntilDespawn >= 0)
			{
				LOTRSpeech.messageAllPlayers(theEntity.func_110142_aN().func_151521_b());
				removeEscorts();
			}
		}
	}
	
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("DespawnTime", timeUntilDespawn);

		NBTTagList escortTags = new NBTTagList();
		Iterator i = escortUUIDs.iterator();
		while (i.hasNext())
		{
			Object obj = i.next();
			if (obj instanceof UUID)
			{
				NBTTagCompound escortData = new NBTTagCompound();
				escortData.setLong("UUIDMost", ((UUID)obj).getMostSignificantBits());
				escortData.setLong("UUIDLeast", ((UUID)obj).getLeastSignificantBits());
				escortTags.appendTag(escortData);
			}
		}
		nbt.setTag("Escorts", escortTags);
	}
	
	public void readFromNBT(NBTTagCompound nbt)
	{
		timeUntilDespawn = nbt.getInteger("DespawnTime");
		
		escortUUIDs.clear();
		NBTTagList tags = nbt.getTagList("Escorts", new NBTTagCompound().getId());
		if (tags != null)
		{
			for (int i = 0; i < tags.tagCount(); i++)
			{
				NBTTagCompound escortData = (NBTTagCompound)tags.getCompoundTagAt(i);
				escortUUIDs.add(new UUID(escortData.getLong("UUIDMost"), escortData.getLong("UUIDLeast")));
			}
		}
	}
}
