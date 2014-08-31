package lotr.common;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.*;

import org.apache.commons.lang3.StringUtils;

import lotr.common.inventory.LOTRSlotAlignmentReward;
import lotr.common.world.LOTRTravellingTraderSpawner;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.S3FPacketCustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import com.google.common.base.Charsets;
import com.mojang.authlib.GameProfile;

public class LOTRLevelData
{
	private static int FAST_TRAVEL_COOLDOWN_DEFAULT = 72000;
	
	public static int madePortal;
	public static int madeMiddleEarthPortal;
	public static int overworldPortalX;
	public static int overworldPortalY;
	public static int overworldPortalZ;
	public static int middleEarthPortalX;
	public static int middleEarthPortalY;
	public static int middleEarthPortalZ;
	public static int beaconState;
	public static int structuresBanned;
	public static int fastTravelCooldown;
	public static boolean gollumSpawned;
	
	public static List beaconTowerLocations = new ArrayList();
	public static List gondorFortressLocations = new ArrayList();
	public static List dwarvenTowerLocations = new ArrayList();
	public static List urukCampLocations = new ArrayList();
	public static List rohanFortressLocations = new ArrayList();
	public static List woodElvenTowerLocations = new ArrayList();
	public static List mordorTowerLocations = new ArrayList();
	public static List nurnFarmLocations = new ArrayList();
	public static List gundabadCampLocations = new ArrayList();
	public static List dunlandHillFortLocations = new ArrayList();
	public static List blueMountainsStrongholdLocations = new ArrayList();
	public static List rangerCampLocations = new ArrayList();
	public static List nearHaradTowerLocations = new ArrayList();
	public static List nearHaradFortressLocations = new ArrayList();
	public static List nearHaradCampLocations = new ArrayList();
	public static List rangerWatchtowerLocations = new ArrayList();
	
	private static Map<UUID, LOTRPlayerData> playerData = new HashMap();
	
	public static boolean needsLoad = true;
	private static boolean needsSave = false;
	
	private static Random rand = new Random();
	
	public static void markDirty()
	{
		needsSave = true;
	}
	
	public static boolean needsSaving()
	{
		return needsSave;
	}
	
	public static void save()
	{
		try
		{
			File file = new File(DimensionManager.getCurrentSaveRootDirectory(), "LOTR.dat");
			if (!file.exists())
			{
				FileOutputStream outputStream = new FileOutputStream(file);
				CompressedStreamTools.writeCompressed(new NBTTagCompound(), outputStream);
				outputStream.close();
			}
			
			FileOutputStream outputStream = new FileOutputStream(file);
			NBTTagCompound levelData = new NBTTagCompound();
			
			levelData.setInteger("MadePortal", madePortal);
			levelData.setInteger("MadeMiddlePortal", madeMiddleEarthPortal);
			levelData.setInteger("OverworldX", overworldPortalX);
			levelData.setInteger("OverworldY", overworldPortalY);
			levelData.setInteger("OverworldZ", overworldPortalZ);
			levelData.setInteger("MiddleEarthX", middleEarthPortalX);
			levelData.setInteger("MiddleEarthY", middleEarthPortalY);
			levelData.setInteger("MiddleEarthZ", middleEarthPortalZ);
			levelData.setInteger("BeaconState", beaconState);
			levelData.setInteger("StructuresBanned", structuresBanned);
			levelData.setInteger("FastTravel", fastTravelCooldown);
			levelData.setBoolean("GollumSpawned", gollumSpawned);
			
			NBTTagCompound travellingTraderData = new NBTTagCompound();
			for (int i = 0; i < LOTRTickHandlerServer.travellingTraders.size(); i++)
			{
				LOTRTravellingTraderSpawner obj = (LOTRTravellingTraderSpawner)LOTRTickHandlerServer.travellingTraders.get(i);
				NBTTagCompound nbt = new NBTTagCompound();
				obj.writeToNBT(nbt);
				travellingTraderData.setTag(obj.name, nbt);
			}
			levelData.setTag("TravellingTraders", travellingTraderData);
			
			NBTTagCompound structureLocations = new NBTTagCompound();
			saveStructureLocations(structureLocations, beaconTowerLocations, "BeaconTower");
			saveStructureLocations(structureLocations, gondorFortressLocations, "GondorFortress");
			saveStructureLocations(structureLocations, dwarvenTowerLocations, "DwarvenTower");
			saveStructureLocations(structureLocations, urukCampLocations, "UrukCamp");
			saveStructureLocations(structureLocations, rohanFortressLocations, "RohanFortress");
			saveStructureLocations(structureLocations, woodElvenTowerLocations, "WoodElvenTower");
			saveStructureLocations(structureLocations, mordorTowerLocations, "MordorTower");
			saveStructureLocations(structureLocations, nurnFarmLocations, "NurnFarm");
			saveStructureLocations(structureLocations, gundabadCampLocations, "GundabadCamp");
			saveStructureLocations(structureLocations, dunlandHillFortLocations, "DunlandFort");
			saveStructureLocations(structureLocations, blueMountainsStrongholdLocations, "BlueMountainsStronghold");
			saveStructureLocations(structureLocations, rangerCampLocations, "RangerCamp");
			saveStructureLocations(structureLocations, nearHaradTowerLocations, "NearHaradTower");
			saveStructureLocations(structureLocations, nearHaradFortressLocations, "NearHaradFortress");
			saveStructureLocations(structureLocations, nearHaradCampLocations, "NearHaradCamp");
			saveStructureLocations(structureLocations, rangerWatchtowerLocations, "RangerWatchtower");
			levelData.setTag("StructureLocations", structureLocations);
			
			NBTTagList playerDataTags = new NBTTagList();
			for (UUID uuid : playerData.keySet())
			{
				NBTTagCompound nbt = new NBTTagCompound();
				nbt.setLong("UUIDMost", uuid.getMostSignificantBits());
				nbt.setLong("UUIDLeast", uuid.getLeastSignificantBits());
				LOTRPlayerData pd = playerData.get(uuid);
				pd.save(nbt);
				playerDataTags.appendTag(nbt);
			}
			levelData.setTag("PlayerData", playerDataTags);
			
			NBTTagCompound takenAlignmentRewards = new NBTTagCompound();
			for (LOTRFaction faction : LOTRFaction.values())
			{
				if (faction.allowPlayer)
				{
					takenAlignmentRewards.setTag(faction.name(), savePlayerSet(faction.playersTakenRewardItem));
				}
			}
			levelData.setTag("TakenAlignmentRewards", takenAlignmentRewards);
			
			LOTRGuiMessageTypes.saveAll(levelData);
			
			LOTRWaypoint.save(levelData);
			
			LOTRTime.saveDates(levelData);
			
			CompressedStreamTools.writeCompressed(levelData, outputStream);
			outputStream.close();
			
			needsSave = false;
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
	}
	
	private static void saveStructureLocations(NBTTagCompound structureLocations, List structures, String name)
	{
		NBTTagList tags = new NBTTagList();
		Iterator i = structures.iterator();
		while (i.hasNext())
		{
			Object obj = i.next();
			if (obj instanceof ChunkCoordinates)
			{
				ChunkCoordinates coords = (ChunkCoordinates)obj;
				NBTTagCompound nbt = new NBTTagCompound();
				nbt.setInteger("XPos", coords.posX);
				nbt.setInteger("YPos", coords.posY);
				nbt.setInteger("ZPos", coords.posZ);
				tags.appendTag(nbt);
			}
		}
		
		structureLocations.setTag(name, tags);
	}
	
	private static NBTTagList savePlayerSet(Set<UUID> playerSet)
	{
		NBTTagList tags = new NBTTagList();
		Iterator<UUID> i = playerSet.iterator();
		while (i.hasNext())
		{
			UUID uuid = i.next();
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setLong("UUIDMost", uuid.getMostSignificantBits());
			nbt.setLong("UUIDLeast", uuid.getLeastSignificantBits());
			tags.appendTag(nbt);
		}
		return tags;
	}

	public static void load()
	{
		try
		{
			File file = new File(DimensionManager.getCurrentSaveRootDirectory(), "LOTR.dat");
			if (!file.exists())
			{
				FileOutputStream outputStream = new FileOutputStream(file);
				CompressedStreamTools.writeCompressed(new NBTTagCompound(), outputStream);
				outputStream.close();
			}
			
			FileInputStream inputStream = new FileInputStream(file);
			NBTTagCompound levelData = CompressedStreamTools.readCompressed(inputStream);
			
			madePortal = levelData.getInteger("MadePortal");
			madeMiddleEarthPortal = levelData.getInteger("MadeMiddlePortal");
			overworldPortalX = levelData.getInteger("OverworldX");
			overworldPortalY = levelData.getInteger("OverworldY");
			overworldPortalZ = levelData.getInteger("OverworldZ");
			middleEarthPortalX = levelData.getInteger("MiddleEarthX");
			middleEarthPortalY = levelData.getInteger("MiddleEarthY");
			middleEarthPortalZ = levelData.getInteger("MiddleEarthZ");
			beaconState = levelData.getInteger("BeaconState");
			structuresBanned = levelData.getInteger("StructuresBanned");
			if (levelData.hasKey("FastTravel"))
			{
				fastTravelCooldown = levelData.getInteger("FastTravel");
			}
			else
			{
				fastTravelCooldown = FAST_TRAVEL_COOLDOWN_DEFAULT;
			}
			gollumSpawned = levelData.getBoolean("GollumSpawned");
			
			NBTTagCompound travellingTraderData = levelData.getCompoundTag("TravellingTraders");
			for (int i = 0; i < LOTRTickHandlerServer.travellingTraders.size(); i++)
			{
				LOTRTravellingTraderSpawner obj = (LOTRTravellingTraderSpawner)LOTRTickHandlerServer.travellingTraders.get(i);
				NBTTagCompound nbt = travellingTraderData.getCompoundTag(obj.name);
				obj.readFromNBT(nbt);
			}
			
			loadStructureLocations(levelData, beaconTowerLocations, "BeaconTower");
			loadStructureLocations(levelData, gondorFortressLocations, "GondorFortress");
			loadStructureLocations(levelData, dwarvenTowerLocations, "DwarvenTower");
			loadStructureLocations(levelData, urukCampLocations, "UrukCamp");
			loadStructureLocations(levelData, rohanFortressLocations, "RohanFortress");
			loadStructureLocations(levelData, woodElvenTowerLocations, "WoodElvenTower");
			loadStructureLocations(levelData, mordorTowerLocations, "MordorTower");
			loadStructureLocations(levelData, nurnFarmLocations, "NurnFarm");
			loadStructureLocations(levelData, gundabadCampLocations, "GundabadCamp");
			loadStructureLocations(levelData, dunlandHillFortLocations, "DunlandFort");
			loadStructureLocations(levelData, blueMountainsStrongholdLocations, "BlueMountainsStronghold");
			loadStructureLocations(levelData, rangerCampLocations, "RangerCamp");
			loadStructureLocations(levelData, nearHaradTowerLocations, "NearHaradTower");
			loadStructureLocations(levelData, nearHaradFortressLocations, "NearHaradFortress");
			loadStructureLocations(levelData, nearHaradCampLocations, "NearHaradCamp");
			loadStructureLocations(levelData, rangerWatchtowerLocations, "RangerWatchtower");
			
			clearAllPlayerData();
			if (levelData.hasKey("PlayerData"))
			{
				NBTTagList playerDataTags = levelData.getTagList("PlayerData", new NBTTagCompound().getId());
				for (int i = 0; i < playerDataTags.tagCount(); i++)
				{
					NBTTagCompound nbt = playerDataTags.getCompoundTagAt(i);
					UUID uuid = new UUID(nbt.getLong("UUIDMost"), nbt.getLong("UUIDLeast"));
					LOTRPlayerData pd = new LOTRPlayerData(uuid);
					pd.load(nbt);
					playerData.put(uuid, pd);
				}
			}
			else
			{
				//BEGIN OBSOLETE LOAD
				
				NBTTagList alignmentTags = levelData.getTagList("Alignments", new NBTTagCompound().getId());
				if (alignmentTags != null)
				{
					for (int i = 0; i < alignmentTags.tagCount(); i++)
					{
						NBTTagCompound nbt = (NBTTagCompound)alignmentTags.getCompoundTagAt(i);
						
						UUID player = new UUID(nbt.getLong("UUIDMost"), nbt.getLong("UUIDLeast"));
						LOTRPlayerData pd = getData(player);
						
						NBTTagList list = nbt.getTagList("AlignmentMap", new NBTTagCompound().getId());
						if (list != null)
						{
							for (int j = 0; j < list.tagCount(); j++)
							{
								NBTTagCompound nbt1 = (NBTTagCompound)list.getCompoundTagAt(j);
								String factionName = nbt1.getString("Faction");
								LOTRFaction faction = LOTRFaction.forName(factionName);
								if (faction != null)
								{
									int alignment = nbt1.getInteger("Alignment");
									pd.setAlignment(faction, alignment);
								}
							}
						}
					}
				}
				
				NBTTagList achievementTags = levelData.getTagList("Achievements", new NBTTagCompound().getId());
				if (achievementTags != null)
				{
					for (int i = 0; i < achievementTags.tagCount(); i++)
					{
						NBTTagCompound nbt = (NBTTagCompound)achievementTags.getCompoundTagAt(i);
						
						UUID player = new UUID(nbt.getLong("UUIDMost"), nbt.getLong("UUIDLeast"));
						LOTRPlayerData pd = getData(player);
						
						NBTTagList list = nbt.getTagList("List", new NBTTagCompound().getId());
						if (list != null)
						{
							for (int j = 0; j < list.tagCount(); j++)
							{
								NBTTagCompound nbt1 = (NBTTagCompound)list.getCompoundTagAt(j);
								String categoryName = nbt1.getString("Category");
								LOTRAchievement.Category category = LOTRAchievement.categoryForName(categoryName);
								if (category != null)
								{
									int ID = nbt1.getInteger("ID");
									LOTRAchievement achievement = LOTRAchievement.achievementForCategoryAndID(category, ID);
									if (achievement != null)
									{
										pd.addAchievement(achievement);
									}
								}
							}
						}
					}
				}
				
				NBTTagList friendlyFireTags = levelData.getTagList("FriendlyFire", new NBTTagCompound().getId());
				if (friendlyFireTags != null)
				{
					for (int i = 0; i < friendlyFireTags.tagCount(); i++)
					{
						NBTTagCompound nbt = (NBTTagCompound)friendlyFireTags.getCompoundTagAt(i);
						UUID player = new UUID(nbt.getLong("UUIDMost"), nbt.getLong("UUIDLeast"));
						LOTRPlayerData pd = getData(player);
						pd.setFriendlyFire(nbt.getBoolean("Enabled"));
					}
				}
				
				NBTTagList hiredDeathMessagesTags = levelData.getTagList("HiredDeathMessages", new NBTTagCompound().getId());
				if (hiredDeathMessagesTags != null)
				{
					for (int i = 0; i < hiredDeathMessagesTags.tagCount(); i++)
					{
						NBTTagCompound nbt = (NBTTagCompound)hiredDeathMessagesTags.getCompoundTagAt(i);
						UUID player = new UUID(nbt.getLong("UUIDMost"), nbt.getLong("UUIDLeast"));
						LOTRPlayerData pd = getData(player);
						pd.setEnableHiredDeathMessages(nbt.getBoolean("Enabled"));
					}
				}
				
				NBTTagList playerDeathPointsTags = levelData.getTagList("PlayerDeathPoints", new NBTTagCompound().getId());
				if (playerDeathPointsTags != null)
				{
					for (int i = 0; i < playerDeathPointsTags.tagCount(); i++)
					{
						NBTTagCompound nbt = (NBTTagCompound)playerDeathPointsTags.getCompoundTagAt(i);
						UUID player = new UUID(nbt.getLong("UUIDMost"), nbt.getLong("UUIDLeast"));
						LOTRPlayerData pd = getData(player);
						pd.setDeathPoint(nbt.getInteger("XPos"), nbt.getInteger("YPos"), nbt.getInteger("ZPos"));
					}
				}
				
				NBTTagList fastTravelTimersTags = levelData.getTagList("FastTravelTimers", new NBTTagCompound().getId());
				if (fastTravelTimersTags != null)
				{
					for (int i = 0; i < fastTravelTimersTags.tagCount(); i++)
					{
						NBTTagCompound nbt = (NBTTagCompound)fastTravelTimersTags.getCompoundTagAt(i);
						UUID player = new UUID(nbt.getLong("UUIDMost"), nbt.getLong("UUIDLeast"));
						LOTRPlayerData pd = getData(player);
						pd.setFTTimer(nbt.getInteger("Timer"));
					}
				}

				NBTTagList viewingFactionsTags = levelData.getTagList("ViewingFactions", new NBTTagCompound().getId());
				if (viewingFactionsTags != null)
				{
					for (int i = 0; i < viewingFactionsTags.tagCount(); i++)
					{
						NBTTagCompound nbt = (NBTTagCompound)viewingFactionsTags.getCompoundTagAt(i);
						int factionID = nbt.getInteger("Faction");
						LOTRFaction faction = LOTRFaction.forID(factionID);
						if (faction != null && faction.allowPlayer)
						{
							UUID player = new UUID(nbt.getLong("UUIDMost"), nbt.getLong("UUIDLeast"));
							LOTRPlayerData pd = getData(player);
							pd.setViewingFaction(faction);
						}
					}
				}
				
				Set<UUID> set = new HashSet<UUID>();
				
				loadPlayerSet(levelData, set, "PlayersCheckedAchievements");
				for (UUID uuid : set)
				{
					getData(uuid).setCheckedMenu(true);
				}
				
				loadPlayerSet(levelData, set, "PlayersCheckedAlignments");
				for (UUID uuid : set)
				{
					getData(uuid).setCheckedAlignments(true);
				}
				
				loadPlayerSet(levelData, set, "BannedStructurePlayers");
				for (UUID uuid : set)
				{
					getData(uuid).setStructuresBanned(true);
				}
				
				loadPlayerSet(levelData, set, "HiddenAlignments");
				for (UUID uuid : set)
				{
					getData(uuid).setHideAlignment(true);
				}
				
				loadPlayerSet(levelData, set, "HiddenMapPlayers");
				for (UUID uuid : set)
				{
					getData(uuid).setHideMapLocation(true);
				}
				
				//END OBSOLETE LOAD
			}
			
			NBTTagCompound takenAlignmentRewards = levelData.getCompoundTag("TakenAlignmentRewards");
			for (LOTRFaction faction : LOTRFaction.values())
			{
				if (faction.allowPlayer)
				{
					loadPlayerSet(takenAlignmentRewards, faction.playersTakenRewardItem, faction.name());
				}
			}
			
			LOTRGuiMessageTypes.loadAll(levelData);
			
			LOTRWaypoint.load(levelData);
			
			LOTRTime.loadDates(levelData);
			
			inputStream.close();
			
			needsLoad = false;
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
	}
	
	private static void loadStructureLocations(NBTTagCompound levelData, List structures, String name)
	{
		structures.clear();
		
		NBTTagList tags = null;
		NBTTagCompound structureLocations = levelData.getCompoundTag("StructureLocations");
		if (structureLocations.hasKey(name))
		{
			tags = structureLocations.getTagList(name, new NBTTagCompound().getId());
		}
		else if (levelData.hasKey(name + "Locations"))
		{
			tags = levelData.getTagList(name + "Locations", new NBTTagCompound().getId());
		}
		
		if (tags != null)
		{
			for (int i = 0; i < tags.tagCount(); i++)
			{
				NBTTagCompound nbt = (NBTTagCompound)tags.getCompoundTagAt(i);
				int posX = nbt.getInteger("XPos");
				int posY = nbt.getInteger("YPos");
				int posZ = nbt.getInteger("ZPos");
				ChunkCoordinates coords = new ChunkCoordinates(posX, posY, posZ);
				structures.add(coords);
			}
		}
	}
	
	private static void loadPlayerSet(NBTTagCompound levelData, Set<UUID> playerSet, String name)
	{
		playerSet.clear();
		NBTTagList tags = levelData.getTagList(name, new NBTTagCompound().getId());
		if (tags != null)
		{
			for (int i = 0; i < tags.tagCount(); i++)
			{
				NBTTagCompound nbt = (NBTTagCompound)tags.getCompoundTagAt(i);
				playerSet.add(new UUID(nbt.getLong("UUIDMost"), nbt.getLong("UUIDLeast")));
			}
		}
	}
	
	public static void setMadePortal(int i)
	{
		madePortal = i;
		markDirty();
	}
	
	public static void setMadeMiddleEarthPortal(int i)
	{
		madeMiddleEarthPortal = i;
		markDirty();
	}
	
	public static void markOverworldPortalLocation(int i, int j, int k)
	{
		overworldPortalX = i;
		overworldPortalY = j;
		overworldPortalZ = k;
		markDirty();
	}
	
	public static void markMiddleEarthPortalLocation(int i, int j, int k)
	{
    	ByteBuf data = Unpooled.buffer();
    	
    	data.writeInt(i);
    	data.writeInt(j);
    	data.writeInt(k);
    	
    	S3FPacketCustomPayload packet = new S3FPacketCustomPayload("lotr.portalPos", data);
    	MinecraftServer.getServer().getConfigurationManager().sendPacketToAllPlayers(packet);

    	markDirty();
	}
	
	public static void setBeaconState(int i)
	{
		if (beaconState != i)
		{
			beaconState = i;
			markDirty();
		}
	}
	
	public static void sendLoginPacket(EntityPlayerMP entityplayer)
	{
    	ByteBuf data = Unpooled.buffer();
    	
    	data.writeInt(middleEarthPortalX);
    	data.writeInt(middleEarthPortalY);
    	data.writeInt(middleEarthPortalZ);
    	data.writeByte((byte)beaconState);
    	    	
    	entityplayer.playerNetServerHandler.sendPacket(new S3FPacketCustomPayload("lotr.login", data));
	}
	
	public static void sendPlayerData(EntityPlayerMP entityplayer)
	{
		try
		{
	    	ByteBuf data = Unpooled.buffer();
	    	
	    	LOTRPlayerData pd = getData(entityplayer);
	    	pd.sendPlayerData(entityplayer);
		}
		catch (Exception e)
		{
			System.out.println("Failed to send player data to player");
			e.printStackTrace();
		}
	}
	
	public static LOTRPlayerData getData(EntityPlayer entityplayer)
	{
		return getData(entityplayer.getUniqueID());
	}
	
	public static LOTRPlayerData getData(UUID player)
	{
		LOTRPlayerData pd = playerData.get(player);
		if (pd == null)
		{
			pd = new LOTRPlayerData(player);
			playerData.put(player, pd);
		}
		return pd;
	}
	
	public static void clearAllPlayerData()
	{
		playerData.clear();
	}
	
	public static Collection<LOTRPlayerData> getPlayerDataEntries()
	{
		return playerData.values();
	}

	public static void setPlayerBannedForStructures(String username, boolean flag)
	{
		UUID uuid = UUID.fromString(PreYggdrasilConverter.func_152719_a(username));
		if (uuid != null)
		{
			getData(uuid).setStructuresBanned(flag);
		}
	}
	
	public static boolean isPlayerBannedForStructures(EntityPlayer entityplayer)
	{
		return getData(entityplayer).getStructuresBanned();
	}
	
	public static Set<String> getBannedStructurePlayersUsernames()
	{
		Set<String> players = new HashSet<String>();
		
		Iterator<UUID> it = playerData.keySet().iterator();
		while (it.hasNext())
		{
			UUID uuid = it.next();
			if (getData(uuid).getStructuresBanned())
			{
				GameProfile profile = MinecraftServer.getServer().func_152358_ax().func_152652_a(uuid);
				if (StringUtils.isEmpty(profile.getName()))
				{
					MinecraftServer.getServer().func_147130_as().fillProfileProperties(profile, true);
				}
				
				String username = profile.getName();
				if (!StringUtils.isEmpty(username))
				{
					players.add(username);
				}
			}
		}
		
		return players;
	}
	
	public static void sendAlignmentToAllPlayersInWorld(EntityPlayer entityplayer, World world)
	{
		for (int i = 0; i < world.playerEntities.size(); i++)
		{
			EntityPlayer worldPlayer = (EntityPlayer)world.playerEntities.get(i);
			ByteBuf data = Unpooled.buffer();
			
			data.writeLong(entityplayer.getUniqueID().getMostSignificantBits());
			data.writeLong(entityplayer.getUniqueID().getLeastSignificantBits());

			for (LOTRFaction faction : LOTRFaction.values())
			{
				int alignment = getData(entityplayer).getAlignment(faction);
				data.writeInt(alignment);
			}
			
			data.writeBoolean(getData(entityplayer).getHideAlignment());
			
			S3FPacketCustomPayload packet = new S3FPacketCustomPayload("lotr.alignment", data);
			((EntityPlayerMP)worldPlayer).playerNetServerHandler.sendPacket(packet);
		}
	}
	
	public static void sendAllAlignmentsInWorldToPlayer(EntityPlayer entityplayer, World world)
	{
		for (int i = 0; i < world.playerEntities.size(); i++)
		{
			EntityPlayer worldPlayer = (EntityPlayer)world.playerEntities.get(i);
			ByteBuf data = Unpooled.buffer();
			
			data.writeLong(worldPlayer.getUniqueID().getMostSignificantBits());
			data.writeLong(worldPlayer.getUniqueID().getLeastSignificantBits());

			for (LOTRFaction faction : LOTRFaction.values())
			{
				int alignment = getData(worldPlayer).getAlignment(faction);
				data.writeInt(alignment);
			}
			
			data.writeBoolean(getData(worldPlayer).getHideAlignment());
			
			S3FPacketCustomPayload packet = new S3FPacketCustomPayload("lotr.alignment", data);
			((EntityPlayerMP)entityplayer).playerNetServerHandler.sendPacket(packet);
		}
	}
	
	public static void selectDefaultShieldForPlayer(EntityPlayer entityplayer)
	{
		if (getData(entityplayer).getShield() == null)
		{
			for (LOTRShields shield : LOTRShields.values())
			{
				if (shield.canPlayerWear(entityplayer))
				{
					getData(entityplayer).setShield(shield);
					return;
				}
			}
		}
	}
	
	public static void sendShieldToAllPlayersInWorld(EntityPlayer entityplayer, World world)
	{
		LOTRShields shield = getData(entityplayer).getShield();
		if (shield != null)
		{
			boolean enableShield = getData(entityplayer).getEnableShield();
			for (int i = 0; i < world.playerEntities.size(); i++)
			{
				EntityPlayer worldPlayer = (EntityPlayer)world.playerEntities.get(i);
				
				ByteBuf data = Unpooled.buffer();
				
				data.writeLong(entityplayer.getUniqueID().getMostSignificantBits());
				data.writeLong(entityplayer.getUniqueID().getLeastSignificantBits());
				
				data.writeByte((byte)shield.shieldID);
				data.writeByte((byte)shield.shieldType.ordinal());
				data.writeBoolean(enableShield);
				
				S3FPacketCustomPayload packet = new S3FPacketCustomPayload("lotr.updateShld", data);
				((EntityPlayerMP)worldPlayer).playerNetServerHandler.sendPacket(packet);
			}
		}
	}
	
	public static void sendAllShieldsInWorldToPlayer(EntityPlayer entityplayer, World world)
	{
		for (int i = 0; i < world.playerEntities.size(); i++)
		{
			EntityPlayer worldPlayer = (EntityPlayer)world.playerEntities.get(i);
			LOTRShields shield = getData(worldPlayer).getShield();
			if (shield != null)
			{
				ByteBuf data = Unpooled.buffer();
				
				data.writeLong(worldPlayer.getUniqueID().getMostSignificantBits());
				data.writeLong(worldPlayer.getUniqueID().getLeastSignificantBits());
				
				data.writeByte((byte)shield.shieldID);
				data.writeByte((byte)shield.shieldType.ordinal());
				data.writeBoolean(getData(worldPlayer).getEnableShield());
				
				S3FPacketCustomPayload packet = new S3FPacketCustomPayload("lotr.updateShld", data);
				((EntityPlayerMP)entityplayer).playerNetServerHandler.sendPacket(packet);
			}
		}
	}
	
	public static void setFastTravelCooldown(int i)
	{
		if (i < 0)
		{
			i = 0;
		}
		fastTravelCooldown = i;
		markDirty();
	}

	public static void sendPlayerLocationsToPlayer(EntityPlayer entityplayer, World world)
	{
		((EntityPlayerMP)entityplayer).playerNetServerHandler.sendPacket(new S3FPacketCustomPayload("lotr.clearMap", Unpooled.buffer(0)));
		
		boolean isOp = MinecraftServer.getServer().getConfigurationManager().func_152596_g(entityplayer.getGameProfile());
		
		for (int i = 0; i < world.playerEntities.size(); i++)
		{
			EntityPlayer worldPlayer = (EntityPlayer)world.playerEntities.get(i);
			if (worldPlayer == entityplayer)
			{
				continue;
			}
			
			if (isOp || !getData(worldPlayer).getHideMapLocation())
			{
				ByteBuf data = Unpooled.buffer();
				
				data.writeLong(worldPlayer.getUniqueID().getMostSignificantBits());
				data.writeLong(worldPlayer.getUniqueID().getLeastSignificantBits());
				
				data.writeDouble(worldPlayer.posX);
				data.writeDouble(worldPlayer.posZ);
				
				String username = worldPlayer.getCommandSenderName();
				byte[] usernameData = username.getBytes(Charsets.UTF_8);
				data.writeShort(usernameData.length);
				data.writeBytes(usernameData);
				
				S3FPacketCustomPayload packet = new S3FPacketCustomPayload("lotr.playerPos", data);
				((EntityPlayerMP)entityplayer).playerNetServerHandler.sendPacket(packet);
			}
		}
	}
	
	public static boolean hasTakenAlignmentRewardItem(EntityPlayer entityplayer, LOTRFaction faction)
	{
		if (!faction.allowPlayer)
		{
			return false;
		}
		
		return faction.playersTakenRewardItem.contains(entityplayer.getUniqueID());
	}
	
	public static void setTakenAlignmentRewardItem(EntityPlayer entityplayer, LOTRFaction faction, boolean flag)
	{
		if (!faction.allowPlayer)
		{
			return;
		}
		
		if (flag)
		{
			faction.playersTakenRewardItem.add(entityplayer.getUniqueID());
		}
		else
		{
			faction.playersTakenRewardItem.remove(entityplayer.getUniqueID());
		}
		
		if (!entityplayer.worldObj.isRemote)
		{
			markDirty();
			sendTakenAlignmentRewardItemPacket(entityplayer, faction, flag);
		}
	}
	
	private static void sendTakenAlignmentRewardItemPacket(EntityPlayer entityplayer, LOTRFaction faction, boolean flag)
	{
		ByteBuf data = Unpooled.buffer();
		
		data.writeByte((byte)faction.ordinal());
		data.writeBoolean(flag);

		S3FPacketCustomPayload packet = new S3FPacketCustomPayload("lotr.rewardItem", data);
		((EntityPlayerMP)entityplayer).playerNetServerHandler.sendPacket(packet);
	}
	
	public static void sendTakenAlignmentRewardsToPlayer(EntityPlayer entityplayer)
	{
		for (LOTRFaction faction : LOTRFaction.values())
		{
			if (!faction.allowPlayer)
			{
				continue;
			}
			
			boolean flag = hasTakenAlignmentRewardItem(entityplayer, faction);
			sendTakenAlignmentRewardItemPacket(entityplayer, faction, flag);
		}
	}
	
	public static String getHMSTime(int i)
	{
		int hours = i / 72000;
		int minutes = (i % 72000) / 1200;
		int seconds = ((i % 72000) % 1200) / 20;
		return hours + "h " + minutes + "m " + seconds + "s";
	}
}
