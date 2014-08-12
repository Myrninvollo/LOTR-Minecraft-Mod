package lotr.common;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.*;

import lotr.common.inventory.LOTRSlotAlignmentReward;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S3FPacketCustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

import com.google.common.base.Charsets;

public class LOTRPlayerData
{
	private UUID playerUUID;
	
	private Map alignments = new HashMap();
	private LOTRFaction viewingFaction;
	private boolean checkedAlignments = false;
	private boolean hideAlignment = false;
	
	private boolean hideOnMap = false;
	private int waypointToggleMode;

	private List achievements = new ArrayList();
	private boolean checkedMenu = false;
	
	private LOTRCapes cape;
	private boolean enableCape = true;
	
	private boolean friendlyFire = true;
	private boolean hiredDeathMessages = true;
	
	private ChunkCoordinates deathPoint;
	private int fastTravelTimer;
	private boolean structuresBanned = false;
	private boolean askedForGandalf = false;
	
	private int alcoholTolerance;
	
	public LOTRPlayerData(UUID uuid)
	{
		playerUUID = uuid;
	}
	
	private EntityPlayer getPlayer()
	{
		World[] worlds = null;
		if (LOTRMod.proxy.isClient())
		{
			worlds = new World[] {LOTRMod.proxy.getClientWorld()};
		}
		else
		{
			worlds = MinecraftServer.getServer().worldServers;
		}
		
		for (World world : worlds)
		{
			EntityPlayer entityplayer = world.func_152378_a(playerUUID);
			if (entityplayer != null)
			{
				return entityplayer;
			}
		}
		
		return null;
	}
	
	public void save(NBTTagCompound playerData)
	{
		NBTTagList taglist = new NBTTagList();
		Iterator it = alignments.keySet().iterator();
		while (it.hasNext())
		{
			LOTRFaction faction = (LOTRFaction)it.next();
			int alignment = (Integer)alignments.get(faction);
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setString("Faction", faction.name());
			nbt.setInteger("Alignment", alignment);
			taglist.appendTag(nbt);
		}
		playerData.setTag("AlignmentMap", taglist);
		
		if (viewingFaction != null)
		{
			playerData.setInteger("ViewingFaction", viewingFaction.ordinal());
		}
		playerData.setBoolean("CheckedAlignments", checkedAlignments);
		playerData.setBoolean("HideAlignment", hideAlignment);
		
		playerData.setBoolean("HideOnMap", hideOnMap);
		playerData.setInteger("WPToggle", waypointToggleMode);
		
		taglist = new NBTTagList();
		it = achievements.iterator();
		while (it.hasNext())
		{
			LOTRAchievement achievement = (LOTRAchievement)it.next();
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setString("Category", achievement.category.name());
			nbt.setInteger("ID", achievement.ID);
			taglist.appendTag(nbt);
		}
		playerData.setTag("Achievements", taglist);
		
		playerData.setBoolean("CheckedMenu", checkedMenu);
		
		if (cape != null)
		{
			playerData.setString("Cape", cape.name());
			playerData.setBoolean("EnableCape", enableCape);
		}
		
		playerData.setBoolean("FriendlyFire", friendlyFire);
		playerData.setBoolean("HiredDeathMessages", hiredDeathMessages);
		
		if (deathPoint != null)
		{
			playerData.setInteger("DeathX", deathPoint.posX);
			playerData.setInteger("DeathY", deathPoint.posY);
			playerData.setInteger("DeathZ", deathPoint.posZ);
		}
		playerData.setInteger("FTTimer", fastTravelTimer);
		playerData.setBoolean("StructuresBanned", structuresBanned);
		playerData.setBoolean("AskedForGandalf", askedForGandalf);
		
		playerData.setInteger("Alcohol", alcoholTolerance);
	}
	
	public void load(NBTTagCompound playerData)
	{
		NBTTagList taglist = playerData.getTagList("AlignmentMap", new NBTTagCompound().getId());
		for (int i = 0; i < taglist.tagCount(); i++)
		{
			NBTTagCompound nbt = taglist.getCompoundTagAt(i);
			LOTRFaction faction = LOTRFaction.forName(nbt.getString("Faction"));
			if (faction != null)
			{
				int alignment = nbt.getInteger("Alignment");
				alignments.put(faction, alignment);
			}
		}
		
		viewingFaction = LOTRFaction.forID(playerData.getInteger("ViewingFaction"));
		checkedAlignments = playerData.getBoolean("CheckedAlignments");
		hideAlignment = playerData.getBoolean("HideAlignment");
		
		hideOnMap = playerData.getBoolean("HideOnMap");
		waypointToggleMode = playerData.getInteger("WPToggle");
		
		taglist = playerData.getTagList("Achievements", new NBTTagCompound().getId());
		for (int i = 0; i < taglist.tagCount(); i++)
		{
			NBTTagCompound nbt = taglist.getCompoundTagAt(i);
			String category = nbt.getString("Category");
			int ID = nbt.getInteger("ID");
			LOTRAchievement achievement = LOTRAchievement.achievementForCategoryAndID(LOTRAchievement.categoryForName(category), ID);
			if (achievement != null && !achievements.contains(achievement))
			{
				achievements.add(achievement);
			}
		}
		
		checkedMenu = playerData.getBoolean("CheckedMenu");
		
		LOTRCapes lotrcapes = LOTRCapes.capeForName(playerData.getString("Cape"));
		if (lotrcapes != null)
		{
			cape = lotrcapes;
		}
		enableCape = playerData.getBoolean("EnableCape");
		
		friendlyFire = playerData.getBoolean("FriendlyFire");
		hiredDeathMessages = playerData.getBoolean("HiredDeathMessages");
		
		if (playerData.hasKey("DeathX") && playerData.hasKey("DeathY") && playerData.hasKey("DeathZ"))
		{
			deathPoint = new ChunkCoordinates(playerData.getInteger("DeathX"), playerData.getInteger("DeathY"), playerData.getInteger("DeathZ"));
		}
		fastTravelTimer = playerData.getInteger("FTTimer");
		structuresBanned = playerData.getBoolean("StructuresBanned");
		askedForGandalf = playerData.getBoolean("AskedForGandalf");
		
		alcoholTolerance = playerData.getInteger("Alcohol");
	}
	
	public int getAlignment(LOTRFaction faction)
	{
		if (faction == LOTRFaction.UNALIGNED)
		{
			return 0;
		}
		else if (!faction.allowPlayer)
		{
			return -1;
		}
		
		Object obj = alignments.get(faction);
		return obj != null ? (Integer)obj : 0;
	}
	
	public void setAlignment(LOTRFaction faction, int alignment)
	{
		if (faction.allowPlayer)
		{
			int prevAlignment = getAlignment(faction);
			
			alignments.put(faction, alignment);
			LOTRLevelData.markDirty();
			
			EntityPlayer entityplayer = getPlayer();
			if (entityplayer != null && !entityplayer.worldObj.isRemote)
			{
				LOTRLevelData.sendAlignmentToAllPlayersInWorld(entityplayer, entityplayer.worldObj);
			}
			
			checkAlignmentAchievements(faction, prevAlignment);
		}
	}
	
	public void addAlignment(LOTRAlignmentValues.Bonus alignmentSource, LOTRFaction faction, Entity entity)
	{
		addAlignment(alignmentSource, faction, entity.posX, entity.boundingBox.minY + (double)entity.height / 1.5D, entity.posZ);
	}
	
	public void addAlignment(LOTRAlignmentValues.Bonus alignmentSource, LOTRFaction faction, double posX, double posY, double posZ)
	{
		if (!faction.allowPlayer)
		{
			return;
		}

		int bonus = alignmentSource.bonus;
		
		if (alignmentSource.isKill)
		{
			Iterator it = faction.killPositives.iterator();
			while (it.hasNext())
			{
				LOTRFaction nextFaction = (LOTRFaction)it.next();
				int alignment = getAlignment(nextFaction);
				int factionBonus = Math.abs(bonus);
				
				alignment += factionBonus;
				setAlignment(nextFaction, alignment);
				
				sendAlignmentBonusPacket(alignmentSource, factionBonus, nextFaction, posX, posY, posZ);
			}
			
			it = faction.killNegatives.iterator();
			while (it.hasNext())
			{
				LOTRFaction nextFaction = (LOTRFaction)it.next();
				int alignment = getAlignment(nextFaction);
				int factionBonus = -Math.abs(bonus);
				
				factionBonus = LOTRAlignmentValues.Bonus.scalePenalty(factionBonus, alignment);
				
				alignment += factionBonus;
				setAlignment(nextFaction, alignment);
				
				sendAlignmentBonusPacket(alignmentSource, factionBonus, nextFaction, posX, posY, posZ);
			}
		}
		else
		{
			int alignment = getAlignment(faction);
			alignment += bonus;
			setAlignment(faction, alignment);
			
			sendAlignmentBonusPacket(alignmentSource, bonus, faction, posX, posY, posZ);
		}
	}
	
	private void sendAlignmentBonusPacket(LOTRAlignmentValues.Bonus alignmentSource, int bonus, LOTRFaction faction, double posX, double posY, double posZ)
	{
		EntityPlayer entityplayer = getPlayer();
		if (entityplayer != null)
		{
			ByteBuf data = Unpooled.buffer();
			byte[] nameData = alignmentSource.name.getBytes(Charsets.UTF_8);
			
			data.writeInt(bonus);
			data.writeDouble(posX);
			data.writeDouble(posY);
			data.writeDouble(posZ);
			data.writeByte((byte)faction.ordinal());
			data.writeBoolean(alignmentSource.needsTranslation);
			data.writeBoolean(alignmentSource.isKill);
			data.writeShort(nameData.length);
			data.writeBytes(nameData);
			
			Packet packet = new S3FPacketCustomPayload("lotr.alignBonus", data);
			((EntityPlayerMP)entityplayer).playerNetServerHandler.sendPacket(packet);
		}
	}
	
	public void addAlignmentFromCommand(LOTRFaction faction, int add)
	{
		int alignment = getAlignment(faction);
		alignment += add;
		setAlignment(faction, alignment);
	}
	
	private void checkAlignmentAchievements(LOTRFaction faction, int prevAlignment)
	{
		int alignment = getAlignment(faction);
		
		if (faction.createAlignmentReward() != null)
		{
			EntityPlayer entityplayer = getPlayer();
			if (entityplayer != null)
			{
				if (alignment >= LOTRSlotAlignmentReward.ALIGNMENT_REQUIRED && prevAlignment < LOTRSlotAlignmentReward.ALIGNMENT_REQUIRED && !LOTRLevelData.hasTakenAlignmentRewardItem(entityplayer, faction))
				{
					entityplayer.addChatMessage(new ChatComponentTranslation("chat.lotr.alignmentRewardItem", new Object[] {LOTRSlotAlignmentReward.ALIGNMENT_REQUIRED, faction.factionName()}));
				}
			}
		}
		
		if (alignment >= 10)
		{
			if (faction == LOTRFaction.HOBBIT)
			{
				addAchievement(LOTRAchievement.alignmentGood10_HOBBIT);
			}
			if (faction == LOTRFaction.RANGER_NORTH)
			{
				addAchievement(LOTRAchievement.alignmentGood10_RANGER_NORTH);
			}
			if (faction == LOTRFaction.BLUE_MOUNTAINS)
			{
				addAchievement(LOTRAchievement.alignmentGood10_BLUE_MOUNTAINS);
			}
			if (faction == LOTRFaction.HIGH_ELF)
			{
				addAchievement(LOTRAchievement.alignmentGood10_HIGH_ELF);
			}
			if (faction == LOTRFaction.GUNDABAD)
			{
				addAchievement(LOTRAchievement.alignmentGood10_GUNDABAD);
			}
			if (faction == LOTRFaction.ANGMAR)
			{
				addAchievement(LOTRAchievement.alignmentGood10_ANGMAR);
			}
			if (faction == LOTRFaction.WOOD_ELF)
			{
				addAchievement(LOTRAchievement.alignmentGood10_WOOD_ELF);
			}
			if (faction == LOTRFaction.DOL_GULDUR)
			{
				addAchievement(LOTRAchievement.alignmentGood10_DOL_GULDUR);
			}
			if (faction == LOTRFaction.DWARF)
			{
				addAchievement(LOTRAchievement.alignmentGood10_DWARF);
			}
			if (faction == LOTRFaction.GALADHRIM)
			{
				addAchievement(LOTRAchievement.alignmentGood10_GALADHRIM);
			}
			if (faction == LOTRFaction.DUNLAND)
			{
				addAchievement(LOTRAchievement.alignmentGood10_DUNLAND);
			}
			if (faction == LOTRFaction.URUK_HAI)
			{
				addAchievement(LOTRAchievement.alignmentGood10_URUK_HAI);
			}
			if (faction == LOTRFaction.FANGORN)
			{
				addAchievement(LOTRAchievement.alignmentGood10_FANGORN);
			}
			if (faction == LOTRFaction.ROHAN)
			{
				addAchievement(LOTRAchievement.alignmentGood10_ROHAN);
			}
			if (faction == LOTRFaction.GONDOR)
			{
				addAchievement(LOTRAchievement.alignmentGood10_GONDOR);
			}
			if (faction == LOTRFaction.MORDOR)
			{
				addAchievement(LOTRAchievement.alignmentGood10_MORDOR);
			}
			if (faction == LOTRFaction.NEAR_HARAD)
			{
				addAchievement(LOTRAchievement.alignmentGood10_NEAR_HARAD);
			}
			if (faction == LOTRFaction.FAR_HARAD)
			{
				addAchievement(LOTRAchievement.alignmentGood10_FAR_HARAD);
			}
			if (faction == LOTRFaction.HALF_TROLL)
			{
				addAchievement(LOTRAchievement.alignmentGood10_HALF_TROLL);
			}
		}
		
		if (alignment >= 100)
		{
			if (faction == LOTRFaction.HOBBIT)
			{
				addAchievement(LOTRAchievement.alignmentGood100_HOBBIT);
			}
			if (faction == LOTRFaction.RANGER_NORTH)
			{
				addAchievement(LOTRAchievement.alignmentGood100_RANGER_NORTH);
			}
			if (faction == LOTRFaction.BLUE_MOUNTAINS)
			{
				addAchievement(LOTRAchievement.alignmentGood100_BLUE_MOUNTAINS);
			}
			if (faction == LOTRFaction.HIGH_ELF)
			{
				addAchievement(LOTRAchievement.alignmentGood100_HIGH_ELF);
			}
			if (faction == LOTRFaction.GUNDABAD)
			{
				addAchievement(LOTRAchievement.alignmentGood100_GUNDABAD);
			}
			if (faction == LOTRFaction.ANGMAR)
			{
				addAchievement(LOTRAchievement.alignmentGood100_ANGMAR);
			}
			if (faction == LOTRFaction.WOOD_ELF)
			{
				addAchievement(LOTRAchievement.alignmentGood100_WOOD_ELF);
			}
			if (faction == LOTRFaction.DOL_GULDUR)
			{
				addAchievement(LOTRAchievement.alignmentGood100_DOL_GULDUR);
			}
			if (faction == LOTRFaction.DWARF)
			{
				addAchievement(LOTRAchievement.alignmentGood100_DWARF);
			}
			if (faction == LOTRFaction.GALADHRIM)
			{
				addAchievement(LOTRAchievement.alignmentGood100_GALADHRIM);
			}
			if (faction == LOTRFaction.DUNLAND)
			{
				addAchievement(LOTRAchievement.alignmentGood100_DUNLAND);
			}
			if (faction == LOTRFaction.URUK_HAI)
			{
				addAchievement(LOTRAchievement.alignmentGood100_URUK_HAI);
			}
			if (faction == LOTRFaction.FANGORN)
			{
				addAchievement(LOTRAchievement.alignmentGood100_FANGORN);
			}
			if (faction == LOTRFaction.ROHAN)
			{
				addAchievement(LOTRAchievement.alignmentGood100_ROHAN);
			}
			if (faction == LOTRFaction.GONDOR)
			{
				addAchievement(LOTRAchievement.alignmentGood100_GONDOR);
			}
			if (faction == LOTRFaction.MORDOR)
			{
				addAchievement(LOTRAchievement.alignmentGood100_MORDOR);
			}
			if (faction == LOTRFaction.NEAR_HARAD)
			{
				addAchievement(LOTRAchievement.alignmentGood100_NEAR_HARAD);
			}
			if (faction == LOTRFaction.FAR_HARAD)
			{
				addAchievement(LOTRAchievement.alignmentGood100_FAR_HARAD);
			}
			if (faction == LOTRFaction.HALF_TROLL)
			{
				addAchievement(LOTRAchievement.alignmentGood100_HALF_TROLL);
			}
		}
		
		if (alignment >= 1000)
		{
			if (faction == LOTRFaction.HOBBIT)
			{
				addAchievement(LOTRAchievement.alignmentGood1000_HOBBIT);
			}
			if (faction == LOTRFaction.RANGER_NORTH)
			{
				addAchievement(LOTRAchievement.alignmentGood1000_RANGER_NORTH);
			}
			if (faction == LOTRFaction.BLUE_MOUNTAINS)
			{
				addAchievement(LOTRAchievement.alignmentGood1000_BLUE_MOUNTAINS);
			}
			if (faction == LOTRFaction.HIGH_ELF)
			{
				addAchievement(LOTRAchievement.alignmentGood1000_HIGH_ELF);
			}
			if (faction == LOTRFaction.GUNDABAD)
			{
				addAchievement(LOTRAchievement.alignmentGood1000_GUNDABAD);
			}
			if (faction == LOTRFaction.ANGMAR)
			{
				addAchievement(LOTRAchievement.alignmentGood1000_ANGMAR);
			}
			if (faction == LOTRFaction.WOOD_ELF)
			{
				addAchievement(LOTRAchievement.alignmentGood1000_WOOD_ELF);
			}
			if (faction == LOTRFaction.DOL_GULDUR)
			{
				addAchievement(LOTRAchievement.alignmentGood1000_DOL_GULDUR);
			}
			if (faction == LOTRFaction.DWARF)
			{
				addAchievement(LOTRAchievement.alignmentGood1000_DWARF);
			}
			if (faction == LOTRFaction.GALADHRIM)
			{
				addAchievement(LOTRAchievement.alignmentGood1000_GALADHRIM);
			}
			if (faction == LOTRFaction.DUNLAND)
			{
				addAchievement(LOTRAchievement.alignmentGood1000_DUNLAND);
			}
			if (faction == LOTRFaction.URUK_HAI)
			{
				addAchievement(LOTRAchievement.alignmentGood1000_URUK_HAI);
			}
			if (faction == LOTRFaction.FANGORN)
			{
				addAchievement(LOTRAchievement.alignmentGood1000_FANGORN);
			}
			if (faction == LOTRFaction.ROHAN)
			{
				addAchievement(LOTRAchievement.alignmentGood1000_ROHAN);
			}
			if (faction == LOTRFaction.GONDOR)
			{
				addAchievement(LOTRAchievement.alignmentGood1000_GONDOR);
			}
			if (faction == LOTRFaction.MORDOR)
			{
				addAchievement(LOTRAchievement.alignmentGood1000_MORDOR);
			}
			if (faction == LOTRFaction.NEAR_HARAD)
			{
				addAchievement(LOTRAchievement.alignmentGood1000_NEAR_HARAD);
			}
			if (faction == LOTRFaction.FAR_HARAD)
			{
				addAchievement(LOTRAchievement.alignmentGood1000_FAR_HARAD);
			}
			if (faction == LOTRFaction.HALF_TROLL)
			{
				addAchievement(LOTRAchievement.alignmentGood1000_HALF_TROLL);
			}
		}
	}
	
	public List getAchievements()
	{
		return achievements;
	}
	
	public List getEarnedAchievements()
	{
		List earnedAchievements = new ArrayList();
		EntityPlayer entityplayer = getPlayer();
		if (entityplayer != null)
		{
			Iterator it = achievements.iterator();
			while (it.hasNext())
			{
				LOTRAchievement achievement = (LOTRAchievement)it.next();
				if (achievement.canPlayerEarn(entityplayer))
				{
					earnedAchievements.add(achievement);
				}
			}
		}
		return earnedAchievements;
	}
	
	public void addAchievement(LOTRAchievement achievement)
	{
		if (hasAchievement(achievement))
		{
			return;
		}
		
		achievements.add(achievement);
		LOTRLevelData.markDirty();
		
		EntityPlayer entityplayer = getPlayer();
		if (entityplayer != null && !entityplayer.worldObj.isRemote && achievement.canPlayerEarn(entityplayer))
		{
			ByteBuf data = Unpooled.buffer();
			
			data.writeByte((byte)achievement.category.ordinal());
			data.writeByte((byte)achievement.ID);
			
			S3FPacketCustomPayload packet = new S3FPacketCustomPayload("lotr.achieve", data);
			((EntityPlayerMP)entityplayer).playerNetServerHandler.sendPacket(packet);
			
			MinecraftServer.getServer().getConfigurationManager().sendChatMsg(new ChatComponentTranslation("chat.lotr.achievement", new Object[] {entityplayer.func_145748_c_(), achievement.getChatComponentForEarn()}));
			
			List earnedAchievements = getEarnedAchievements();
			int biomes = 0;
			for (int i = 0; i < earnedAchievements.size(); i++)
			{
				LOTRAchievement earnedAchievement = (LOTRAchievement)earnedAchievements.get(i);
				if (earnedAchievement.isBiomeAchievement)
				{
					biomes++;
				}
			}
			if (biomes >= 10)
			{
				addAchievement(LOTRAchievement.travel10);
			}
			if (biomes >= 20)
			{
				addAchievement(LOTRAchievement.travel20);
			}
			if (biomes >= 30)
			{
				addAchievement(LOTRAchievement.travel30);
			}
			if (biomes >= 40)
			{
				addAchievement(LOTRAchievement.travel40);
			}
			if (biomes >= 50)
			{
				addAchievement(LOTRAchievement.travel50);
			}
		}
	}
	
	public boolean hasAchievement(LOTRAchievement achievement)
	{
		Iterator it = achievements.iterator();
		while (it.hasNext())
		{
			LOTRAchievement a = (LOTRAchievement)it.next();
			if (a.category == achievement.category && a.ID == achievement.ID)
			{
				return true;
			}
		}
		return false;
	}
	
	public boolean getCheckedAlignments()
	{
		return checkedAlignments;
	}
	
	public void setCheckedAlignments(boolean flag)
	{
		checkedAlignments = flag;
		LOTRLevelData.markDirty();
	}
	
	public boolean getCheckedMenu()
	{
		return checkedMenu;
	}
	
	public void setCheckedMenu(boolean flag)
	{
		checkedMenu = flag;
		LOTRLevelData.markDirty();
	}
	
	public void setCape(LOTRCapes lotrcape)
	{
		cape = lotrcape;
		LOTRLevelData.markDirty();
	}
	
	public LOTRCapes getCape()
	{
		return cape;
	}
	
	public void setEnableCape(boolean flag)
	{
		enableCape = flag;
		LOTRLevelData.markDirty();
	}
	
	public boolean getEnableCape()
	{
		return enableCape;
	}
	
	public void setStructuresBanned(boolean flag)
	{
		structuresBanned = flag;
		LOTRLevelData.markDirty();
	}
	
	public boolean getStructuresBanned()
	{
		return structuresBanned;
	}
	
	private void sendOptionsPacket(int option, boolean flag)
	{
		EntityPlayer entityplayer = getPlayer();
		if (entityplayer != null && !entityplayer.worldObj.isRemote)
		{
			ByteBuf data = Unpooled.buffer();
			
			data.writeByte((byte)option);
			data.writeBoolean(flag);
			
			S3FPacketCustomPayload packet = new S3FPacketCustomPayload("lotr.options", data);
			((EntityPlayerMP)entityplayer).playerNetServerHandler.sendPacket(packet);
		}
	}
	
	public boolean getFriendlyFire()
	{
		return friendlyFire;
	}
	
	public void setFriendlyFire(boolean flag)
	{
		friendlyFire = flag;
		LOTRLevelData.markDirty();
		sendOptionsPacket(LOTROptions.FRIENDLY_FIRE, flag);
	}
	
	public boolean getEnableHiredDeathMessages()
	{
		return hiredDeathMessages;
	}
	
	public void setEnableHiredDeathMessages(boolean flag)
	{
		hiredDeathMessages = flag;
		LOTRLevelData.markDirty();
		sendOptionsPacket(LOTROptions.HIRED_DEATH_MESSAGES, flag);
	}
	
	public boolean getHideAlignment()
	{
		return hideAlignment;
	}
	
	public void setHideAlignment(boolean flag)
	{
		hideAlignment = flag;
		LOTRLevelData.markDirty();
		
		EntityPlayer entityplayer = getPlayer();
		if (entityplayer != null && !entityplayer.worldObj.isRemote)
		{
			LOTRLevelData.sendAlignmentToAllPlayersInWorld(entityplayer, entityplayer.worldObj);
		}
	}
	
	public int getFTTimer()
	{
		return fastTravelTimer;
	}
	
	public void setFTTimer(int i)
	{
		i = Math.max(0, i);
		fastTravelTimer = i;
		LOTRLevelData.markDirty();
		
		EntityPlayer entityplayer = getPlayer();
		if (entityplayer != null)
		{
			if (!entityplayer.worldObj.isRemote)
			{
				ByteBuf data = Unpooled.buffer();
				
				data.writeInt(i);
	
				S3FPacketCustomPayload packet = new S3FPacketCustomPayload("lotr.ftTimer", data);
				((EntityPlayerMP)entityplayer).playerNetServerHandler.sendPacket(packet);
			}
		}
	}
	
	public LOTRFaction getViewingFaction()
	{
		return viewingFaction;
	}
	
	public void setViewingFaction(LOTRFaction faction)
	{
		viewingFaction = faction;
		LOTRLevelData.markDirty();
	}
	
	public int getWaypointToggleMode()
	{
		return waypointToggleMode;
	}
	
	public void setWaypointToggleMode(int i)
	{
		waypointToggleMode = i;
		LOTRLevelData.markDirty();
	}
	
	public boolean getHideMapLocation()
	{
		return hideOnMap;
	}
	
	public void setHideMapLocation(boolean flag)
	{
		hideOnMap = flag;
		LOTRLevelData.markDirty();
		sendOptionsPacket(LOTROptions.SHOW_MAP_LOCATION, flag);
	}
	
	public boolean getAskedForGandalf()
	{
		return askedForGandalf;
	}
	
	public void setAskedForGandalf(boolean flag)
	{
		askedForGandalf = flag;
		LOTRLevelData.markDirty();
	}
	
	public ChunkCoordinates getDeathPoint()
	{
		return deathPoint;
	}
	
	public void setDeathPoint(int i, int j, int k)
	{
		deathPoint = new ChunkCoordinates(i, j, k);
		LOTRLevelData.markDirty();
	}
	
	public int getAlcoholTolerance()
	{
		return alcoholTolerance;
	}
	
	public void setAlcoholTolerance(int i)
	{
		alcoholTolerance = i;
		LOTRLevelData.markDirty();
		
		if (alcoholTolerance >= 250)
		{
			EntityPlayer entityplayer = getPlayer();
			if (entityplayer != null)
			{
				addAchievement(LOTRAchievement.gainHighAlcoholTolerance);
			}
		}
	}
}
