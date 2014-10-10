package lotr.common;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

import lotr.common.block.LOTRBlockCraftingTable;
import lotr.common.entity.npc.LOTREntityNPC;
import lotr.common.inventory.LOTRSlotAlignmentReward;
import lotr.common.quest.LOTRMiniQuest;
import lotr.common.world.LOTRChunkProviderUtumno.UtumnoLevel;
import lotr.common.world.biome.LOTRBiome;
import lotr.common.world.biome.LOTRBiomeGenMistyMountains;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.*;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.S3FPacketCustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.util.Constants;

import com.google.common.base.Charsets;

public class LOTRPlayerData
{
	private UUID playerUUID;
	
	private Map<LOTRFaction, Integer> alignments = new HashMap();
	private LOTRFaction viewingFaction;
	private boolean checkedAlignments = false;
	private boolean hideAlignment = false;
	private Set<LOTRFaction> takenAlignmentRewards = new HashSet();
	
	private boolean hideOnMap = false;
	private int waypointToggleMode;

	private List<LOTRAchievement> achievements = new ArrayList();
	private boolean checkedMenu = false;
	
	private LOTRShields shield;
	private boolean enableShield = true;
	
	private boolean friendlyFire = false;
	private boolean hiredDeathMessages = true;
	
	private ChunkCoordinates deathPoint;
	private int deathDim;
	
	private int alcoholTolerance;
	
	private List<LOTRMiniQuest> miniQuests = new ArrayList();
	private Map<LOTRFaction, Integer> completedMiniQuests = new HashMap();
	
	private Map<LOTRGuiMessageTypes, Boolean> sentMessageTypes = new HashMap();
	
	private LOTRTitle.PlayerTitle playerTitle;
	
	private int fastTravelTimer;
	private boolean structuresBanned = false;
	private boolean askedForGandalf = false;
	
	public LOTRPlayerData(UUID uuid)
	{
		playerUUID = uuid;
	}
	
	private EntityPlayer getPlayer()
	{
		World[] searchWorlds = null;
		if (LOTRMod.proxy.isClient())
		{
			searchWorlds = new World[] {LOTRMod.proxy.getClientWorld()};
		}
		else
		{
			searchWorlds = MinecraftServer.getServer().worldServers;
		}
		
		for (World world : searchWorlds)
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
		NBTTagList alignmentTags = new NBTTagList();
		for (Entry<LOTRFaction, Integer> entry : alignments.entrySet())
		{
			LOTRFaction faction = entry.getKey();
			int alignment = entry.getValue();
			
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setString("Faction", faction.name());
			nbt.setInteger("Alignment", alignment);
			alignmentTags.appendTag(nbt);
		}
		playerData.setTag("AlignmentMap", alignmentTags);
		
		if (viewingFaction != null)
		{
			playerData.setInteger("ViewingFaction", viewingFaction.ordinal());
		}
		playerData.setBoolean("CheckedAlignments", checkedAlignments);
		playerData.setBoolean("HideAlignment", hideAlignment);
		
		NBTTagList takenRewardsTags = new NBTTagList();
		for (LOTRFaction faction : takenAlignmentRewards)
		{
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setString("Faction", faction.name());
			takenRewardsTags.appendTag(nbt);
		}
		playerData.setTag("TakenAlignmentRewards", takenRewardsTags);
		
		playerData.setBoolean("HideOnMap", hideOnMap);
		playerData.setInteger("WPToggle", waypointToggleMode);
		
		NBTTagList achievementTags = new NBTTagList();
		for (LOTRAchievement achievement : achievements)
		{
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setString("Category", achievement.category.name());
			nbt.setInteger("ID", achievement.ID);
			achievementTags.appendTag(nbt);
		}
		playerData.setTag("Achievements", achievementTags);
		
		playerData.setBoolean("CheckedMenu", checkedMenu);
		
		if (shield != null)
		{
			playerData.setString("Shield", shield.name());
		}
		playerData.setBoolean("EnableShield", enableShield);
		
		playerData.setBoolean("FriendlyFire", friendlyFire);
		playerData.setBoolean("HiredDeathMessages", hiredDeathMessages);
		
		if (deathPoint != null)
		{
			playerData.setInteger("DeathX", deathPoint.posX);
			playerData.setInteger("DeathY", deathPoint.posY);
			playerData.setInteger("DeathZ", deathPoint.posZ);
			playerData.setInteger("DeathDim", deathDim);
		}
		
		playerData.setInteger("Alcohol", alcoholTolerance);
		
		NBTTagList miniquestTags = new NBTTagList();
		for (LOTRMiniQuest quest : miniQuests)
		{
			NBTTagCompound nbt = new NBTTagCompound();
			quest.writeToNBT(nbt);
			miniquestTags.appendTag(nbt);
		}
		playerData.setTag("MiniQuests", miniquestTags);
		
		NBTTagList completedMiniquestTags = new NBTTagList();
		for (Entry<LOTRFaction, Integer> entry : completedMiniQuests.entrySet())
		{
			LOTRFaction faction = entry.getKey();
			int completed = entry.getValue();
			
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setString("Faction", faction.name());
			nbt.setInteger("Completed", completed);
			completedMiniquestTags.appendTag(nbt);
		}
		playerData.setTag("CompletedMiniQuests", completedMiniquestTags);
		
		NBTTagList sentMessageTags = new NBTTagList();
		for (Entry<LOTRGuiMessageTypes, Boolean> entry : sentMessageTypes.entrySet())
		{
			LOTRGuiMessageTypes message = entry.getKey();
			boolean sent = entry.getValue();
			
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setString("Message", message.getSaveName());
			nbt.setBoolean("Sent", sent);
			sentMessageTags.appendTag(nbt);
		}
		playerData.setTag("SentMessageTypes", sentMessageTags);
		
		if (playerTitle != null)
		{
			playerData.setString("PlayerTitle", playerTitle.getTitle().getTitleName());
			playerData.setInteger("PlayerTitleColor", playerTitle.getColor().getFormattingCode());
		}
		
		playerData.setInteger("FTTimer", fastTravelTimer);
		playerData.setBoolean("StructuresBanned", structuresBanned);
		playerData.setBoolean("AskedForGandalf", askedForGandalf);
	}
	
	public void load(NBTTagCompound playerData)
	{
		NBTTagList alignmentTags = playerData.getTagList("AlignmentMap", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < alignmentTags.tagCount(); i++)
		{
			NBTTagCompound nbt = alignmentTags.getCompoundTagAt(i);
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
		
		NBTTagList takenRewardsTags = playerData.getTagList("TakenAlignmentRewards", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < takenRewardsTags.tagCount(); i++)
		{
			NBTTagCompound nbt = takenRewardsTags.getCompoundTagAt(i);
			LOTRFaction faction = LOTRFaction.forName(nbt.getString("Faction"));
			if (faction != null)
			{
				takenAlignmentRewards.add(faction);
			}
		}
		
		hideOnMap = playerData.getBoolean("HideOnMap");
		waypointToggleMode = playerData.getInteger("WPToggle");
		
		NBTTagList achievementTags = playerData.getTagList("Achievements", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < achievementTags.tagCount(); i++)
		{
			NBTTagCompound nbt = achievementTags.getCompoundTagAt(i);
			String category = nbt.getString("Category");
			int ID = nbt.getInteger("ID");
			LOTRAchievement achievement = LOTRAchievement.achievementForCategoryAndID(LOTRAchievement.categoryForName(category), ID);
			if (achievement != null && !achievements.contains(achievement))
			{
				achievements.add(achievement);
			}
		}
		
		checkedMenu = playerData.getBoolean("CheckedMenu");
		
		LOTRShields lotrshield = LOTRShields.shieldForName(playerData.getString("Shield"));
		if (lotrshield != null)
		{
			shield = lotrshield;
		}
		if (playerData.hasKey("EnableShield"))
		{
			enableShield = playerData.getBoolean("EnableShield");
		}
		
		friendlyFire = playerData.getBoolean("FriendlyFire");
		hiredDeathMessages = playerData.getBoolean("HiredDeathMessages");
		
		if (playerData.hasKey("DeathX") && playerData.hasKey("DeathY") && playerData.hasKey("DeathZ"))
		{
			deathPoint = new ChunkCoordinates(playerData.getInteger("DeathX"), playerData.getInteger("DeathY"), playerData.getInteger("DeathZ"));
			if (playerData.hasKey("DeathDim"))
			{
				deathDim = playerData.getInteger("DeathDim");
			}
			else
			{
				deathDim = LOTRDimension.MIDDLE_EARTH.dimensionID; 
			}
		}
		
		alcoholTolerance = playerData.getInteger("Alcohol");
		
		NBTTagList miniquestTags = playerData.getTagList("MiniQuests", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < miniquestTags.tagCount(); i++)
		{
			NBTTagCompound nbt = miniquestTags.getCompoundTagAt(i);
			LOTRMiniQuest quest = LOTRMiniQuest.loadQuestFromNBT(nbt, this);
			if (quest != null)
			{
				miniQuests.add(quest);
			}
		}
		
		NBTTagList completedMiniquestTags = playerData.getTagList("CompletedMiniQuests", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < completedMiniquestTags.tagCount(); i++)
		{
			NBTTagCompound nbt = completedMiniquestTags.getCompoundTagAt(i);
			LOTRFaction faction = LOTRFaction.forName(nbt.getString("Faction"));
			if (faction != null)
			{
				int completed = nbt.getInteger("Completed");
				completedMiniQuests.put(faction, completed);
			}
		}
		
		NBTTagList sentMessageTags = playerData.getTagList("SentMessageTypes", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < sentMessageTags.tagCount(); i++)
		{
			NBTTagCompound nbt = sentMessageTags.getCompoundTagAt(i);
			LOTRGuiMessageTypes message = LOTRGuiMessageTypes.forSaveName(nbt.getString("Message"));
			if (message != null)
			{
				boolean sent = nbt.getBoolean("Sent");
				sentMessageTypes.put(message, sent);
			}
		}
		
		if (playerData.hasKey("PlayerTitle"))
		{
			LOTRTitle title = LOTRTitle.forName(playerData.getString("PlayerTitle"));
			if (title != null)
			{
				int colorCode = playerData.getInteger("PlayerTitleColor");
				EnumChatFormatting color = LOTRTitle.PlayerTitle.colorForID(colorCode);
				playerTitle = new LOTRTitle.PlayerTitle(title, color);
			}
		}
		
		fastTravelTimer = playerData.getInteger("FTTimer");
		structuresBanned = playerData.getBoolean("StructuresBanned");
		askedForGandalf = playerData.getBoolean("AskedForGandalf");
	}
	
	public void sendPlayerData(EntityPlayerMP entityplayer) throws IOException
	{
		ByteBuf data = Unpooled.buffer();
		
		NBTTagCompound nbt = new NBTTagCompound();
		save(nbt);
		nbt.removeTag("Achievements");
		nbt.removeTag("MiniQuests");
		new PacketBuffer(data).writeNBTTagCompoundToBuffer(nbt);

		S3FPacketCustomPayload packet = new S3FPacketCustomPayload("lotr.loginPD", data);
		((EntityPlayerMP)entityplayer).playerNetServerHandler.sendPacket(packet);
		
		for (LOTRAchievement achievement : achievements)
		{
			sendAchievementPacket(entityplayer, achievement, false);
		}
		
		for (LOTRMiniQuest quest : miniQuests)
		{
			sendMiniQuestPacket(entityplayer, quest);
		}
	}
	
	public void onUpdate(EntityPlayerMP entityplayer, World world)
	{
		runAchievementChecks(entityplayer, world);
		
		if (playerTitle != null && !playerTitle.getTitle().canPlayerUse(entityplayer))
		{
			setPlayerTitle(null);
		}
		
		int ftInterval = 20;
		if (fastTravelTimer > 0 && world.getWorldTime() % ftInterval == 0L)
		{
			fastTravelTimer -= ftInterval;
			setFTTimer(fastTravelTimer);
		}
		
		if (world.getWorldTime() % (long)(10 * 60 * 20) == 0L)
		{
			if (alcoholTolerance > 0)
			{
				alcoholTolerance--;
				setAlcoholTolerance(alcoholTolerance);
			}
		}
		
		if (entityplayer.dimension == LOTRDimension.MIDDLE_EARTH.dimensionID)
		{
			if (!getCheckedMenu())
			{
				entityplayer.playerNetServerHandler.sendPacket(new S3FPacketCustomPayload("lotr.promptAch", Unpooled.buffer(0)));
			}
			else if (!getCheckedAlignments())
			{
				entityplayer.playerNetServerHandler.sendPacket(new S3FPacketCustomPayload("lotr.promptAl", Unpooled.buffer(0)));
			}
		}
	}
	
	public int getAlignment(LOTRFaction faction)
	{
		if (faction.hasFixedAlignment)
		{
			return faction.fixedAlignment;
		}
		
		Integer alignment = alignments.get(faction);
		return alignment != null ? alignment.intValue() : 0;
	}
	
	public void setAlignment(LOTRFaction faction, int alignment)
	{
		if (faction.allowPlayer && !faction.hasFixedAlignment)
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
	
	public void addAlignment(LOTRAlignmentValues.AlignmentBonus alignmentSource, LOTRFaction faction, Entity entity)
	{
		addAlignment(alignmentSource, faction, entity.posX, entity.boundingBox.minY + (double)entity.height / 1.5D, entity.posZ);
	}
	
	public void addAlignment(LOTRAlignmentValues.AlignmentBonus alignmentSource, LOTRFaction faction, double posX, double posY, double posZ)
	{
		if (!faction.allowPlayer)
		{
			return;
		}

		int bonus = alignmentSource.bonus;
		
		if (alignmentSource.isKill)
		{
			Iterator it = faction.killBonuses.iterator();
			while (it.hasNext())
			{
				LOTRFaction nextFaction = (LOTRFaction)it.next();
				int alignment = getAlignment(nextFaction);
				int factionBonus = Math.abs(bonus);
				
				alignment += factionBonus;
				setAlignment(nextFaction, alignment);
				
				sendAlignmentBonusPacket(alignmentSource, factionBonus, nextFaction, posX, posY, posZ);
			}
			
			it = faction.killPenalties.iterator();
			while (it.hasNext())
			{
				LOTRFaction nextFaction = (LOTRFaction)it.next();
				int alignment = getAlignment(nextFaction);
				int factionBonus = -Math.abs(bonus);
				
				factionBonus = LOTRAlignmentValues.AlignmentBonus.scalePenalty(factionBonus, alignment);
				
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
	
	private void sendAlignmentBonusPacket(LOTRAlignmentValues.AlignmentBonus alignmentSource, int bonus, LOTRFaction faction, double posX, double posY, double posZ)
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
			if (entityplayer != null && !entityplayer.worldObj.isRemote)
			{
				if (alignment >= LOTRSlotAlignmentReward.ALIGNMENT_REQUIRED && prevAlignment < LOTRSlotAlignmentReward.ALIGNMENT_REQUIRED && !hasTakenAlignmentReward(faction))
				{
					entityplayer.addChatMessage(new ChatComponentTranslation("chat.lotr.alignmentRewardItem", new Object[] {LOTRSlotAlignmentReward.ALIGNMENT_REQUIRED, faction.factionName()}));
				}
			}
		}
		
		faction.checkAlignmentAchievements(this, alignment);
	}
	
	public boolean hasTakenAlignmentReward(LOTRFaction faction)
	{
		return takenAlignmentRewards.contains(faction);
	}
	
	public void setTakenAlignmentReward(LOTRFaction faction, boolean flag)
	{
		if (!faction.allowPlayer)
		{
			return;
		}
		
		if (flag)
		{
			takenAlignmentRewards.add(faction);
		}
		else
		{
			takenAlignmentRewards.remove(faction);
		}
		
		EntityPlayer entityplayer = getPlayer();
		if (entityplayer != null && !entityplayer.worldObj.isRemote)
		{
			LOTRLevelData.markDirty();
			
			ByteBuf data = Unpooled.buffer();
			
			data.writeByte(faction.ordinal());
			data.writeBoolean(flag);

			S3FPacketCustomPayload packet = new S3FPacketCustomPayload("lotr.rewardItem", data);
			((EntityPlayerMP)entityplayer).playerNetServerHandler.sendPacket(packet);
		}
	}
	
	public List getAchievements()
	{
		return achievements;
	}
	
	public List getEarnedAchievements(LOTRDimension dimension)
	{
		List earnedAchievements = new ArrayList();
		EntityPlayer entityplayer = getPlayer();
		if (entityplayer != null)
		{
			Iterator it = achievements.iterator();
			while (it.hasNext())
			{
				LOTRAchievement achievement = (LOTRAchievement)it.next();
				if (achievement.getDimension() == dimension && achievement.canPlayerEarn(entityplayer))
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
			sendAchievementPacket((EntityPlayerMP)entityplayer, achievement, true);
			
			MinecraftServer.getServer().getConfigurationManager().sendChatMsg(new ChatComponentTranslation("chat.lotr.achievement", new Object[] {entityplayer.func_145748_c_(), achievement.getDimension().getDimensionName(), achievement.getChatComponentForEarn()}));
			
			List earnedAchievements = getEarnedAchievements(LOTRDimension.MIDDLE_EARTH);
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
	
	private void runAchievementChecks(EntityPlayer entityplayer, World world)
	{
		int i = MathHelper.floor_double(entityplayer.posX);
		int j = MathHelper.floor_double(entityplayer.boundingBox.minY);
		int k = MathHelper.floor_double(entityplayer.posZ);
		
		BiomeGenBase biome = world.getBiomeGenForCoords(i, k);
		
		if (biome instanceof LOTRBiome)
		{
			LOTRBiome lotrbiome = (LOTRBiome)biome;
			if (lotrbiome.getBiomeAchievement() != null)
			{
				addAchievement(lotrbiome.getBiomeAchievement());
			}
			
			if (lotrbiome.getBiomeWaypoints() != null)
			{
				lotrbiome.getBiomeWaypoints().unlockForPlayer(entityplayer);
			}
		}
		
		if (entityplayer.dimension == LOTRDimension.MIDDLE_EARTH.dimensionID)
		{
			addAchievement(LOTRAchievement.enterMiddleEarth);
		}
		
		if (entityplayer.dimension == LOTRDimension.UTUMNO.dimensionID)
		{
			addAchievement(LOTRAchievement.enterUtumnoIce);
			
			int y = MathHelper.floor_double(entityplayer.boundingBox.minY);
			UtumnoLevel level = UtumnoLevel.forY(y);
			
			if (level == UtumnoLevel.OBSIDIAN)
			{
				addAchievement(LOTRAchievement.enterUtumnoObsidian);
			}
			else if (level == UtumnoLevel.FIRE)
			{
				addAchievement(LOTRAchievement.enterUtumnoFire);
			}
		}
		
		if (entityplayer.inventory.hasItem(LOTRMod.pouch))
		{
			addAchievement(LOTRAchievement.getPouch);
		}
		
		Set tables = new HashSet();
		int crossbowBolts = 0;
		for (ItemStack item : entityplayer.inventory.mainInventory)
		{
			if (item != null && item.getItem() instanceof ItemBlock)
			{
				Block block = Block.getBlockFromItem(item.getItem());
				if (block instanceof LOTRBlockCraftingTable)
				{
					tables.add(block);
				}
			}
			
			if (item != null && item.getItem() == LOTRMod.crossbowBolt)
			{
				crossbowBolts += item.stackSize;
			}
		}
		
		if (tables.size() >= 5)
		{
			addAchievement(LOTRAchievement.collectCraftingTables);
		}
		
		if (crossbowBolts >= 128)
		{
			addAchievement(LOTRAchievement.collectCrossbowBolts);
		}
		
		if (world.getWorldTime() % 5L == 0L && !hasAchievement(LOTRAchievement.hundreds))
		{
			int hiredUnits = 0;
			List<LOTREntityNPC> nearbyNPCs = world.getEntitiesWithinAABB(LOTREntityNPC.class, entityplayer.boundingBox.expand(64D, 64D, 64D));
			for (LOTREntityNPC npc : nearbyNPCs)
			{
				if (npc.hiredNPCInfo.isActive && npc.hiredNPCInfo.getHiringPlayer() == entityplayer)
				{
					hiredUnits++;
				}
			}
			
			if (hiredUnits >= 100)
			{
				addAchievement(LOTRAchievement.hundreds);
			}
		}
		
		if (isPlayerWearingFull(entityplayer, LOTRMod.armorMithril))
		{
			addAchievement(LOTRAchievement.wearFullMithril);
		}
		
		if (isPlayerWearingFull(entityplayer, LOTRMod.armorWarg))
		{
			addAchievement(LOTRAchievement.wearFullWargFur);
		}
		
		if (isPlayerWearingFull(entityplayer, LOTRMod.armorBlueDwarven))
		{
			addAchievement(LOTRAchievement.wearFullBlueDwarven);
		}
		
		if (isPlayerWearingFull(entityplayer, LOTRMod.armorHighElven))
		{
			addAchievement(LOTRAchievement.wearFullHighElven);
		}
		
		if (isPlayerWearingFull(entityplayer, LOTRMod.armorRanger))
		{
			addAchievement(LOTRAchievement.wearFullRanger);
		}
		
		if (isPlayerWearingFull(entityplayer, LOTRMod.armorAngmar))
		{
			addAchievement(LOTRAchievement.wearFullAngmar);
		}
		
		if (isPlayerWearingFull(entityplayer, LOTRMod.armorWoodElvenScout))
		{
			addAchievement(LOTRAchievement.wearFullWoodElvenScout);
		}
		
		if (isPlayerWearingFull(entityplayer, LOTRMod.armorWoodElven))
		{
			addAchievement(LOTRAchievement.wearFullWoodElven);
		}
		
		if (isPlayerWearingFull(entityplayer, LOTRMod.armorDolGuldur))
		{
			addAchievement(LOTRAchievement.wearFullDolGuldur);
		}
		
		if (isPlayerWearingFull(entityplayer, LOTRMod.armorDwarven))
		{
			addAchievement(LOTRAchievement.wearFullDwarven);
		}
		
		if (biome instanceof LOTRBiomeGenMistyMountains && entityplayer.posY > 192D)
		{
			addAchievement(LOTRAchievement.climbMistyMountains);
		}
		
		if (isPlayerWearingFull(entityplayer, LOTRMod.armorElven))
		{
			addAchievement(LOTRAchievement.wearFullElven);
		}
		
		if (isPlayerWearingFull(entityplayer, LOTRMod.armorUruk))
		{
			addAchievement(LOTRAchievement.wearFullUruk);
		}
		
		if (isPlayerWearingFull(entityplayer, LOTRMod.armorRohan))
		{
			addAchievement(LOTRAchievement.wearFullRohirric);
		}
		
		if (isPlayerWearingFull(entityplayer, LOTRMod.armorDunlending))
		{
			addAchievement(LOTRAchievement.wearFullDunlending);
		}
		
		if (isPlayerWearingFull(entityplayer, LOTRMod.armorGondor))
		{
			addAchievement(LOTRAchievement.wearFullGondorian);
		}
		
		if (isPlayerWearingFull(entityplayer, LOTRMod.armorOrc))
		{
			addAchievement(LOTRAchievement.wearFullOrc);
		}
		
		if (isPlayerWearingFull(entityplayer, LOTRMod.armorNearHarad))
		{
			addAchievement(LOTRAchievement.wearFullNearHarad);
		}
	}
	
	private boolean isPlayerWearingFull(EntityPlayer entityplayer, ArmorMaterial material)
	{
		for (ItemStack itemstack : entityplayer.inventory.armorInventory)
		{
			if (itemstack == null || !(itemstack.getItem() instanceof ItemArmor) || ((ItemArmor)itemstack.getItem()).getArmorMaterial() != material)
			{
				return false;
			}
		}
		return true;
	}
	
	private void sendAchievementPacket(EntityPlayerMP entityplayer, LOTRAchievement achievement, boolean display)
	{
		ByteBuf data = Unpooled.buffer();
		
		data.writeByte(achievement.category.ordinal());
		data.writeByte(achievement.ID);
		data.writeBoolean(display);
		
		S3FPacketCustomPayload packet = new S3FPacketCustomPayload("lotr.achieve", data);
		entityplayer.playerNetServerHandler.sendPacket(packet);
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
	
	public void setShield(LOTRShields lotrshield)
	{
		shield = lotrshield;
		LOTRLevelData.markDirty();
	}
	
	public LOTRShields getShield()
	{
		return shield;
	}
	
	public void setEnableShield(boolean flag)
	{
		enableShield = flag;
		LOTRLevelData.markDirty();
	}
	
	public boolean getEnableShield()
	{
		return enableShield;
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
	
	public int getDeathDimension()
	{
		return deathDim;
	}
	
	public void setDeathDimension(int dim)
	{
		deathDim = dim;
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
			if (entityplayer != null && !entityplayer.worldObj.isRemote)
			{
				addAchievement(LOTRAchievement.gainHighAlcoholTolerance);
			}
		}
	}
	
	public List<LOTRMiniQuest> getMiniQuests()
	{
		return miniQuests;
	}
	
	public void addMiniQuest(LOTRMiniQuest quest)
	{
		miniQuests.add(quest);
		updateMiniQuest(quest);
	}
	
	public void removeMiniQuest(LOTRMiniQuest quest, boolean addToCompleted)
	{
		if (miniQuests.remove(quest))
		{
			if (addToCompleted)
			{
				completeMiniQuest(quest);
			}
			LOTRLevelData.markDirty();
			
			EntityPlayer entityplayer = getPlayer();
			if (entityplayer != null && !entityplayer.worldObj.isRemote)
			{
				ByteBuf data = Unpooled.buffer();
				
				data.writeLong(quest.entityUUID.getMostSignificantBits());
				data.writeLong(quest.entityUUID.getLeastSignificantBits());
				data.writeBoolean(addToCompleted);

				S3FPacketCustomPayload packet = new S3FPacketCustomPayload("lotr.mqRemove", data);
				((EntityPlayerMP)entityplayer).playerNetServerHandler.sendPacket(packet);
			}
		}
		else
		{
			System.out.println("Warning: Attempted to remove a mini-quest which does not belong to the player data");
		}
	}
	
	public void updateMiniQuest(LOTRMiniQuest quest)
	{
		LOTRLevelData.markDirty();
		
		EntityPlayer entityplayer = getPlayer();
		if (entityplayer != null && !entityplayer.worldObj.isRemote)
		{
			try
			{
				sendMiniQuestPacket((EntityPlayerMP)entityplayer, quest);
			}
			catch (IOException e)
			{
				System.out.println("Error sending miniquest packet to player " + entityplayer.getCommandSenderName());
				e.printStackTrace();
			}
		}
	}
	
	private void sendMiniQuestPacket(EntityPlayerMP entityplayer, LOTRMiniQuest quest) throws IOException
	{
		ByteBuf data = Unpooled.buffer();
		
		NBTTagCompound nbt = new NBTTagCompound();
		quest.writeToNBT(nbt);
		new PacketBuffer(data).writeNBTTagCompoundToBuffer(nbt);

		S3FPacketCustomPayload packet = new S3FPacketCustomPayload("lotr.miniquest", data);
		((EntityPlayerMP)entityplayer).playerNetServerHandler.sendPacket(packet);
	}
	
	public List getMiniQuestsForEntity(LOTREntityNPC npc, boolean activeOnly)
	{
		List list = new ArrayList();
		for (LOTRMiniQuest quest : miniQuests)
		{
			if (quest.entityUUID.equals(npc.getUniqueID()))
			{
				if (activeOnly)
				{
					if (quest.isActive())
					{
						list.add(quest);
					}
				}
				else
				{
					list.add(quest);
				}
			}
		}
		return list;
	}
	
	public List getMiniQuestsForFaction(LOTRFaction f, boolean activeOnly)
	{
		List list = new ArrayList();
		for (LOTRMiniQuest quest : miniQuests)
		{
			if (quest.entityFaction == f)
			{
				if (activeOnly)
				{
					if (quest.isActive())
					{
						list.add(quest);
					}
				}
				else
				{
					list.add(quest);
				}
			}
		}
		return list;
	}
	
	public List getActiveMiniQuests()
	{
		List list = new ArrayList();
		for (LOTRMiniQuest quest : miniQuests)
		{
			if (quest.isActive())
			{
				list.add(quest);
			}
		}
		return list;
	}
	
	public int getCompletedMiniQuestsTotal()
	{
		int i = 0;
		for (int completed : completedMiniQuests.values())
		{
			i += completed;
		}
		return i;
	}
	
	public int getCompletedMiniQuests(LOTRFaction faction)
	{
		Integer completed = completedMiniQuests.get(faction);
		return completed != null ? completed.intValue() : 0;
	}
	
	private void completeMiniQuest(LOTRMiniQuest quest)
	{
		LOTRFaction faction = quest.entityFaction;
		int completed = getCompletedMiniQuests(faction);
		completed++;
		completedMiniQuests.put(faction, completed);
	}
	
	public void sendMessageIfNotReceived(LOTRGuiMessageTypes message)
	{
		EntityPlayer entityplayer = getPlayer();
		if (entityplayer != null && !entityplayer.worldObj.isRemote)
		{
			Boolean sent = sentMessageTypes.get(message);
			if (sent == null)
			{
				sent = false;
				sentMessageTypes.put(message, sent);
			}
			
			if (!sent)
			{
				sentMessageTypes.put(message, true);
				LOTRLevelData.markDirty();
				
				ByteBuf data = Unpooled.buffer();
				
				data.writeByte(message.ordinal());
				
				((EntityPlayerMP)entityplayer).playerNetServerHandler.sendPacket(new S3FPacketCustomPayload("lotr.message", data));
			}
		}
	}
	
	public LOTRTitle.PlayerTitle getPlayerTitle()
	{
		return playerTitle;
	}
	
	public void setPlayerTitle(LOTRTitle.PlayerTitle title)
	{
		playerTitle = title;
		LOTRLevelData.markDirty();
		
		EntityPlayer entityplayer = getPlayer();
		if (entityplayer != null && !entityplayer.worldObj.isRemote)
		{
			ByteBuf data = Unpooled.buffer();
			
			if (playerTitle == null)
			{
				data.writeInt(-1);
				data.writeInt(-1);
			}
			else
			{
				data.writeInt(playerTitle.getTitle().titleID);
				data.writeInt(playerTitle.getColor().getFormattingCode());
			}
			
			((EntityPlayerMP)entityplayer).playerNetServerHandler.sendPacket(new S3FPacketCustomPayload("lotr.title", data));
		}
	}
}
