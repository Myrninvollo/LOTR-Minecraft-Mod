package lotr.common;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import lotr.common.inventory.LOTRSlotAlignmentReward;
import lotr.common.world.LOTRTravellingTraderSpawner;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S3FPacketCustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import com.google.common.base.Charsets;

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
	public static int hasGollum;
	public static int gollumRespawnTime;
	public static int fastTravelCooldown;
	
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
	
	private static Map alignments = new HashMap();
	private static Map achievements = new HashMap();
	private static Map capes = new HashMap();
	private static Map capesEnabled = new HashMap();
	private static Map friendlyFire = new HashMap();
	private static Map hiredDeathMessages = new HashMap();
	public static Map playerDeathPoints = new HashMap();
	public static Map fastTravelTimers = new HashMap();
	public static Map viewingFactions = new HashMap();
	
	public static Set playersCheckedAchievements = new HashSet();
	public static Set playersCheckedAlignments = new HashSet();
	public static Set bannedStructurePlayers = new HashSet();
	private static Set melonFinders = new HashSet();
	private static Set hiddenAlignments = new HashSet();
	private static Set hiddenMapPlayers = new HashSet();
	
	public static boolean needsLoad = true;
	public static boolean needsSave = false;
	
	private static Random rand = new Random();
	
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
			levelData.setInteger("HasGollum", hasGollum);
			levelData.setInteger("GollumRespawnTime", gollumRespawnTime);
			levelData.setInteger("FastTravel", fastTravelCooldown);
			
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
			levelData.setTag("StructureLocations", structureLocations);
			
			NBTTagList alignmentTags = new NBTTagList();
			Iterator iAlignments = alignments.keySet().iterator();
			while (iAlignments.hasNext())
			{
				Object obj = iAlignments.next();
				if (obj instanceof UUID && alignments.get(obj) instanceof Map)
				{
					NBTTagCompound nbt = new NBTTagCompound();
					nbt.setLong("UUIDMost", ((UUID)obj).getMostSignificantBits());
					nbt.setLong("UUIDLeast", ((UUID)obj).getLeastSignificantBits());
					
					Map playerAlignmments = (Map)alignments.get(obj);
					NBTTagList list = new NBTTagList();
					Iterator iPlayerAlignments = playerAlignmments.keySet().iterator();
					while (iPlayerAlignments.hasNext())
					{
						LOTRFaction faction = (LOTRFaction)iPlayerAlignments.next();
						int alignment = (Integer)playerAlignmments.get(faction);
						NBTTagCompound nbt1 = new NBTTagCompound();
						nbt1.setString("Faction", faction.name());
						nbt1.setInteger("Alignment", alignment);
						list.appendTag(nbt1);
					}
		
					nbt.setTag("AlignmentMap", list);
					alignmentTags.appendTag(nbt);
				}
			}
			levelData.setTag("Alignments", alignmentTags);
			
			NBTTagList achievementTags = new NBTTagList();
			Iterator iAchievements = achievements.keySet().iterator();
			while (iAchievements.hasNext())
			{
				Object obj = iAchievements.next();
				if (obj instanceof UUID && achievements.get(obj) instanceof ArrayList)
				{
					NBTTagCompound nbt = new NBTTagCompound();
					
					nbt.setLong("UUIDMost", ((UUID)obj).getMostSignificantBits());
					nbt.setLong("UUIDLeast", ((UUID)obj).getLeastSignificantBits());
					
					ArrayList playerAchievements = (ArrayList)achievements.get(obj);
					NBTTagList list = new NBTTagList();
					Iterator iPlayerAchievements = playerAchievements.iterator();
					while (iPlayerAchievements.hasNext())
					{
						LOTRAchievement achievement = (LOTRAchievement)iPlayerAchievements.next();
						NBTTagCompound nbt1 = new NBTTagCompound();
						nbt1.setString("Category", achievement.category.name());
						nbt1.setInteger("ID", achievement.ID);
						list.appendTag(nbt1);
					}
					
					nbt.setTag("List", list);
					achievementTags.appendTag(nbt);
				}
			}
			levelData.setTag("Achievements", achievementTags);
			
			NBTTagList capeTags = new NBTTagList();
			Iterator iCapes = capes.keySet().iterator();
			while (iCapes.hasNext())
			{
				Object obj = iCapes.next();
				if (obj instanceof UUID && capes.get(obj) instanceof LOTRCapes)
				{
					NBTTagCompound nbt = new NBTTagCompound();
					
					nbt.setLong("UUIDMost", ((UUID)obj).getMostSignificantBits());
					nbt.setLong("UUIDLeast", ((UUID)obj).getLeastSignificantBits());
					
					nbt.setString("Cape", ((LOTRCapes)capes.get(obj)).name());
					boolean flag = true;
					if (capesEnabled.get(obj) instanceof Boolean)
					{
						flag = (Boolean)capesEnabled.get(obj);
					}
					nbt.setBoolean("Enabled", flag);
					capeTags.appendTag(nbt);
				}
			}
			levelData.setTag("Capes", capeTags);
			
			NBTTagList friendlyFireTags = new NBTTagList();
			Iterator iFriendlyFire = friendlyFire.keySet().iterator();
			while (iFriendlyFire.hasNext())
			{
				Object obj = iFriendlyFire.next();
				if (obj instanceof UUID && friendlyFire.get(obj) instanceof Boolean)
				{
					NBTTagCompound nbt = new NBTTagCompound();
					nbt.setLong("UUIDMost", ((UUID)obj).getMostSignificantBits());
					nbt.setLong("UUIDLeast", ((UUID)obj).getLeastSignificantBits());
					nbt.setBoolean("Enabled", (Boolean)friendlyFire.get(obj));
					friendlyFireTags.appendTag(nbt);
				}
			}
			levelData.setTag("FriendlyFire", friendlyFireTags);
			
			NBTTagList hiredDeathMessagesTags = new NBTTagList();
			Iterator iHiredDeathMessages = hiredDeathMessages.keySet().iterator();
			while (iHiredDeathMessages.hasNext())
			{
				Object obj = iHiredDeathMessages.next();
				if (obj instanceof UUID && hiredDeathMessages.get(obj) instanceof Boolean)
				{
					NBTTagCompound nbt = new NBTTagCompound();
					nbt.setLong("UUIDMost", ((UUID)obj).getMostSignificantBits());
					nbt.setLong("UUIDLeast", ((UUID)obj).getLeastSignificantBits());
					nbt.setBoolean("Enabled", (Boolean)hiredDeathMessages.get(obj));
					hiredDeathMessagesTags.appendTag(nbt);
				}
			}
			levelData.setTag("HiredDeathMessages", hiredDeathMessagesTags);
			
			NBTTagList playerDeathPointsTags = new NBTTagList();
			Iterator iPlayerDeathPoints = playerDeathPoints.keySet().iterator();
			while (iPlayerDeathPoints.hasNext())
			{
				Object obj = iPlayerDeathPoints.next();
				if (obj instanceof UUID && playerDeathPoints.get(obj) instanceof ChunkCoordinates)
				{
					ChunkCoordinates coords = (ChunkCoordinates)playerDeathPoints.get(obj);
					NBTTagCompound nbt = new NBTTagCompound();
					nbt.setLong("UUIDMost", ((UUID)obj).getMostSignificantBits());
					nbt.setLong("UUIDLeast", ((UUID)obj).getLeastSignificantBits());
					nbt.setInteger("XPos", coords.posX);
					nbt.setInteger("YPos", coords.posY);
					nbt.setInteger("ZPos", coords.posZ);
					playerDeathPointsTags.appendTag(nbt);
				}
			}
			levelData.setTag("PlayerDeathPoints", playerDeathPointsTags);
			
			NBTTagList fastTravelTimersTags = new NBTTagList();
			Iterator iFastTravelTimers = fastTravelTimers.keySet().iterator();
			while (iFastTravelTimers.hasNext())
			{
				Object obj = iFastTravelTimers.next();
				if (obj instanceof UUID && fastTravelTimers.get(obj) instanceof Integer)
				{
					int timer = (Integer)fastTravelTimers.get(obj);
					if (timer < 0)
					{
						timer = 0;
					}
					NBTTagCompound nbt = new NBTTagCompound();
					nbt.setLong("UUIDMost", ((UUID)obj).getMostSignificantBits());
					nbt.setLong("UUIDLeast", ((UUID)obj).getLeastSignificantBits());
					nbt.setInteger("Timer", timer);
					fastTravelTimersTags.appendTag(nbt);
				}
			}
			levelData.setTag("FastTravelTimers", fastTravelTimersTags);
			
			NBTTagList viewingFactionsTags = new NBTTagList();
			Iterator iViewingFactions = viewingFactions.keySet().iterator();
			while (iViewingFactions.hasNext())
			{
				Object obj = iViewingFactions.next();
				if (obj instanceof UUID && viewingFactions.get(obj) instanceof LOTRFaction)
				{
					LOTRFaction faction = (LOTRFaction)viewingFactions.get(obj);
					if (faction.allowPlayer)
					{
						NBTTagCompound nbt = new NBTTagCompound();
						nbt.setLong("UUIDMost", ((UUID)obj).getMostSignificantBits());
						nbt.setLong("UUIDLeast", ((UUID)obj).getLeastSignificantBits());
						nbt.setInteger("Faction", faction.ordinal());
						viewingFactionsTags.appendTag(nbt);
					}
				}
			}
			levelData.setTag("ViewingFactions", viewingFactionsTags);
			
			levelData.setTag("PlayersCheckedAchievements", savePlayerSet(playersCheckedAchievements));
			levelData.setTag("PlayersCheckedAlignments", savePlayerSet(playersCheckedAlignments));
			levelData.setTag("BannedStructurePlayers", savePlayerSet(bannedStructurePlayers));
			levelData.setTag("MelonFinders", savePlayerSet(melonFinders));
			levelData.setTag("HiddenAlignments", savePlayerSet(hiddenAlignments));
			levelData.setTag("HiddenMapPlayers", savePlayerSet(hiddenMapPlayers));
			
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
			
			CompressedStreamTools.writeCompressed(levelData, outputStream);
			outputStream.close();
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
	
	private static NBTTagList savePlayerSet(Set playerSet)
	{
		NBTTagList tags = new NBTTagList();
		Iterator i = playerSet.iterator();
		while (i.hasNext())
		{
			Object obj = i.next();
			if (obj instanceof UUID)
			{
				NBTTagCompound nbt = new NBTTagCompound();
				nbt.setLong("UUIDMost", ((UUID)obj).getMostSignificantBits());
				nbt.setLong("UUIDLeast", ((UUID)obj).getLeastSignificantBits());
				tags.appendTag(nbt);
			}
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
			hasGollum = levelData.getInteger("HasGollum");
			if (levelData.hasKey("GollumRespawnTime"))
			{
				gollumRespawnTime = levelData.getInteger("GollumRespawnTime");
			}
			else
			{
				gollumRespawnTime = 12000;
			}
			if (levelData.hasKey("FastTravel"))
			{
				fastTravelCooldown = levelData.getInteger("FastTravel");
			}
			else
			{
				fastTravelCooldown = FAST_TRAVEL_COOLDOWN_DEFAULT;
			}
			
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

			alignments.clear();
			NBTTagList alignmentTags = levelData.getTagList("Alignments", new NBTTagCompound().getId());
			if (alignmentTags != null)
			{
				for (int i = 0; i < alignmentTags.tagCount(); i++)
				{
					NBTTagCompound nbt = (NBTTagCompound)alignmentTags.getCompoundTagAt(i);
					Map playerAlignmments = new HashMap();
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
								playerAlignmments.put(faction, alignment);
							}
						}
					}
					
					alignments.put(new UUID(nbt.getLong("UUIDMost"), nbt.getLong("UUIDLeast")), playerAlignmments);
				}
			}
			
			achievements.clear();
			NBTTagList achievementTags = levelData.getTagList("Achievements", new NBTTagCompound().getId());
			if (achievementTags != null)
			{
				for (int i = 0; i < achievementTags.tagCount(); i++)
				{
					NBTTagCompound nbt = (NBTTagCompound)achievementTags.getCompoundTagAt(i);
					ArrayList playerAchievements = new ArrayList();
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
									playerAchievements.add(achievement);
								}
							}
						}
					}
					
					achievements.put(new UUID(nbt.getLong("UUIDMost"), nbt.getLong("UUIDLeast")), playerAchievements);
				}
			}
			
			capes.clear();
			NBTTagList capeTags = levelData.getTagList("Capes", new NBTTagCompound().getId());
			if (capeTags != null)
			{
				for (int i = 0; i < capeTags.tagCount(); i++)
				{
					NBTTagCompound nbt = (NBTTagCompound)capeTags.getCompoundTagAt(i);
					UUID player = new UUID(nbt.getLong("UUIDMost"), nbt.getLong("UUIDLeast"));
					String capeName = nbt.getString("Cape");
					LOTRCapes cape = LOTRCapes.capeForName(capeName);
					if (cape != null)
					{
						capes.put(player, cape);
						if (nbt.hasKey("Enabled"))
						{
							capesEnabled.put(player, nbt.getBoolean("Enabled"));
						}
					}
				}
			}
			
			friendlyFire.clear();
			NBTTagList friendlyFireTags = levelData.getTagList("FriendlyFire", new NBTTagCompound().getId());
			if (friendlyFireTags != null)
			{
				for (int i = 0; i < friendlyFireTags.tagCount(); i++)
				{
					NBTTagCompound nbt = (NBTTagCompound)friendlyFireTags.getCompoundTagAt(i);
					friendlyFire.put(new UUID(nbt.getLong("UUIDMost"), nbt.getLong("UUIDLeast")), nbt.getBoolean("Enabled"));
				}
			}
			
			hiredDeathMessages.clear();
			NBTTagList hiredDeathMessagesTags = levelData.getTagList("HiredDeathMessages", new NBTTagCompound().getId());
			if (hiredDeathMessagesTags != null)
			{
				for (int i = 0; i < hiredDeathMessagesTags.tagCount(); i++)
				{
					NBTTagCompound nbt = (NBTTagCompound)hiredDeathMessagesTags.getCompoundTagAt(i);
					hiredDeathMessages.put(new UUID(nbt.getLong("UUIDMost"), nbt.getLong("UUIDLeast")), nbt.getBoolean("Enabled"));
				}
			}
			
			playerDeathPoints.clear();
			NBTTagList playerDeathPointsTags = levelData.getTagList("PlayerDeathPoints", new NBTTagCompound().getId());
			if (playerDeathPointsTags != null)
			{
				for (int i = 0; i < playerDeathPointsTags.tagCount(); i++)
				{
					NBTTagCompound nbt = (NBTTagCompound)playerDeathPointsTags.getCompoundTagAt(i);
					ChunkCoordinates coords = new ChunkCoordinates(nbt.getInteger("XPos"), nbt.getInteger("YPos"), nbt.getInteger("ZPos"));
					playerDeathPoints.put(new UUID(nbt.getLong("UUIDMost"), nbt.getLong("UUIDLeast")), coords);
				}
			}
			
			fastTravelTimers.clear();
			NBTTagList fastTravelTimersTags = levelData.getTagList("FastTravelTimers", new NBTTagCompound().getId());
			if (fastTravelTimersTags != null)
			{
				for (int i = 0; i < fastTravelTimersTags.tagCount(); i++)
				{
					NBTTagCompound nbt = (NBTTagCompound)fastTravelTimersTags.getCompoundTagAt(i);
					int timer = nbt.getInteger("Timer");
					if (timer < 0)
					{
						timer = 0;
					}
					fastTravelTimers.put(new UUID(nbt.getLong("UUIDMost"), nbt.getLong("UUIDLeast")), timer);
				}
			}
			
			viewingFactions.clear();
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
						viewingFactions.put(new UUID(nbt.getLong("UUIDMost"), nbt.getLong("UUIDLeast")), faction);
					}
				}
			}
			
			loadPlayerSet(levelData, playersCheckedAchievements, "PlayersCheckedAchievements");
			loadPlayerSet(levelData, playersCheckedAlignments, "PlayersCheckedAlignments");
			loadPlayerSet(levelData, bannedStructurePlayers, "BannedStructurePlayers");
			loadPlayerSet(levelData, melonFinders, "MelonFinders");
			loadPlayerSet(levelData, hiddenAlignments, "HiddenAlignments");
			loadPlayerSet(levelData, hiddenMapPlayers, "HiddenMapPlayers");
			
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
			
			inputStream.close();
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
	
	private static void loadPlayerSet(NBTTagCompound levelData, Set playerSet, String name)
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
		needsSave = true;
	}
	
	public static void setMadeMiddleEarthPortal(int i)
	{
		madeMiddleEarthPortal = i;
		needsSave = true;
	}
	
	public static void markOverworldPortalLocation(int i, int j, int k)
	{
		overworldPortalX = i;
		overworldPortalY = j;
		overworldPortalZ = k;
		needsSave = true;
	}
	
	public static void markMiddleEarthPortalLocation(int i, int j, int k)
	{
    	ByteBuf data = Unpooled.buffer();
    	
    	data.writeInt(i);
    	data.writeInt(j);
    	data.writeInt(k);
    	
    	S3FPacketCustomPayload packet = new S3FPacketCustomPayload("lotr.portalPos", data);
    	MinecraftServer.getServer().getConfigurationManager().sendPacketToAllPlayers(packet);

		needsSave = true;
	}
	
	public static void setBeaconState(int i)
	{
		beaconState = i;
		needsSave = true;
	}
	
	public static Packet getLoginPacket(EntityPlayer entityplayer)
	{
    	ByteBuf data = Unpooled.buffer();
    	
    	data.writeInt(middleEarthPortalX);
    	data.writeInt(middleEarthPortalY);
    	data.writeInt(middleEarthPortalZ);
    	data.writeByte((byte)beaconState);
    	data.writeBoolean(getPlayerFoundMelon(entityplayer));
    	data.writeBoolean(getFriendlyFire(entityplayer));
    	data.writeBoolean(getEnableHiredDeathMessages(entityplayer));
    	data.writeInt(getFastTravelTimer(entityplayer));
    	data.writeInt(getViewingFactionID(entityplayer));
    	data.writeBoolean(getShowMapLocation(entityplayer));
    	
    	return new S3FPacketCustomPayload("lotr.login", data);
	}
	
	private static Map getAlignmentMap(EntityPlayer entityplayer)
	{
		return getAlignmentMap(entityplayer.getUniqueID());
	}
	
	private static Map getAlignmentMap(UUID player)
	{
		Map map = (Map)alignments.get(player);
		if (map == null)
		{
			map = new HashMap();
			alignments.put(player, map);
		}
		return map;
	}
	
	public static int getAlignment(EntityPlayer entityplayer, LOTRFaction faction)
	{
		if (faction == LOTRFaction.UNALIGNED)
		{
			return 0;
		}
		else if (!faction.allowPlayer)
		{
			return -1;
		}
		
		Object obj = getAlignmentMap(entityplayer).get(faction);
		return obj != null ? (Integer)obj : 0;
	}
	
	public static void addAlignment(EntityPlayer entityplayer, LOTRAlignmentValues.Bonus alignmentSource, LOTRFaction faction, Entity entity)
	{
		addAlignment(entityplayer, alignmentSource, faction, entity.posX, entity.boundingBox.minY + (double)entity.height / 1.5D, entity.posZ);
	}
	
	public static void addAlignment(EntityPlayer entityplayer, LOTRAlignmentValues.Bonus alignmentSource, LOTRFaction faction, double posX, double posY, double posZ)
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
				int alignment = getAlignment(entityplayer, nextFaction);
				int prevAlignment = alignment;
				int factionBonus = Math.abs(bonus);
				
				alignment += factionBonus;
				getAlignmentMap(entityplayer).put(nextFaction, alignment);
				needsSave = true;
				
				checkAlignmentAchievements(entityplayer, nextFaction, prevAlignment);
				
				sendAlignmentBonusPacket(entityplayer, alignmentSource, factionBonus, nextFaction, posX, posY, posZ);
			}
			
			it = faction.killNegatives.iterator();
			while (it.hasNext())
			{
				LOTRFaction nextFaction = (LOTRFaction)it.next();
				int alignment = getAlignment(entityplayer, nextFaction);
				int prevAlignment = alignment;
				int factionBonus = -Math.abs(bonus);
				
				factionBonus = LOTRAlignmentValues.Bonus.scalePenalty(factionBonus, alignment);
				
				alignment += factionBonus;
				getAlignmentMap(entityplayer).put(nextFaction, alignment);
				needsSave = true;
				
				checkAlignmentAchievements(entityplayer, nextFaction, prevAlignment);
				
				sendAlignmentBonusPacket(entityplayer, alignmentSource, factionBonus, nextFaction, posX, posY, posZ);
			}
		}
		else
		{
			int alignment = getAlignment(entityplayer, faction);
			int prevAlignment = alignment;
			alignment += bonus;
			getAlignmentMap(entityplayer).put(faction, alignment);
			needsSave = true;
			
			checkAlignmentAchievements(entityplayer, faction, prevAlignment);
			
			sendAlignmentBonusPacket(entityplayer, alignmentSource, bonus, faction, posX, posY, posZ);
		}
		
		sendAlignmentToAllPlayersInWorld(entityplayer, entityplayer.worldObj);
	}
	
	private static void sendAlignmentBonusPacket(EntityPlayer entityplayer, LOTRAlignmentValues.Bonus alignmentSource, int bonus, LOTRFaction faction, double posX, double posY, double posZ)
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
	
	public static void addAlignmentFromCommand(EntityPlayer entityplayer, LOTRFaction faction, int add)
	{
		if (!faction.allowPlayer)
		{
			return;
		}
		
		int alignment = getAlignment(entityplayer, faction);
		int prevAlignment = alignment;
		alignment += add;
		getAlignmentMap(entityplayer).put(faction, alignment);
		needsSave = true;
		
		sendAlignmentToAllPlayersInWorld(entityplayer, entityplayer.worldObj);
		
		checkAlignmentAchievements(entityplayer, faction, prevAlignment);
	}
	
	private static void checkAlignmentAchievements(EntityPlayer entityplayer, LOTRFaction faction, int prevAlignment)
	{
		int alignment = getAlignment(entityplayer, faction);
		
		if (faction.createAlignmentReward() != null)
		{
			if (alignment >= LOTRSlotAlignmentReward.ALIGNMENT_REQUIRED && prevAlignment < LOTRSlotAlignmentReward.ALIGNMENT_REQUIRED && !hasTakenAlignmentRewardItem(entityplayer, faction))
			{
				entityplayer.addChatMessage(new ChatComponentTranslation("chat.lotr.alignmentRewardItem", new Object[] {LOTRSlotAlignmentReward.ALIGNMENT_REQUIRED, faction.factionName()}));
			}
		}
		
		if (alignment >= 10)
		{
			if (faction == LOTRFaction.HOBBIT)
			{
				addAchievement(entityplayer, LOTRAchievement.alignmentGood10_HOBBIT);
			}
			if (faction == LOTRFaction.RANGER_NORTH)
			{
				addAchievement(entityplayer, LOTRAchievement.alignmentGood10_RANGER_NORTH);
			}
			if (faction == LOTRFaction.BLUE_MOUNTAINS)
			{
				addAchievement(entityplayer, LOTRAchievement.alignmentGood10_BLUE_MOUNTAINS);
			}
			if (faction == LOTRFaction.HIGH_ELF)
			{
				addAchievement(entityplayer, LOTRAchievement.alignmentGood10_HIGH_ELF);
			}
			if (faction == LOTRFaction.GUNDABAD)
			{
				addAchievement(entityplayer, LOTRAchievement.alignmentGood10_GUNDABAD);
			}
			if (faction == LOTRFaction.ANGMAR)
			{
				addAchievement(entityplayer, LOTRAchievement.alignmentGood10_ANGMAR);
			}
			if (faction == LOTRFaction.WOOD_ELF)
			{
				addAchievement(entityplayer, LOTRAchievement.alignmentGood10_WOOD_ELF);
			}
			if (faction == LOTRFaction.DOL_GULDUR)
			{
				addAchievement(entityplayer, LOTRAchievement.alignmentGood10_DOL_GULDUR);
			}
			if (faction == LOTRFaction.DWARF)
			{
				addAchievement(entityplayer, LOTRAchievement.alignmentGood10_DWARF);
			}
			if (faction == LOTRFaction.GALADHRIM)
			{
				addAchievement(entityplayer, LOTRAchievement.alignmentGood10_GALADHRIM);
			}
			if (faction == LOTRFaction.DUNLAND)
			{
				addAchievement(entityplayer, LOTRAchievement.alignmentGood10_DUNLAND);
			}
			if (faction == LOTRFaction.FANGORN)
			{
				addAchievement(entityplayer, LOTRAchievement.alignmentGood10_FANGORN);
			}
			if (faction == LOTRFaction.ROHAN)
			{
				addAchievement(entityplayer, LOTRAchievement.alignmentGood10_ROHAN);
			}
			if (faction == LOTRFaction.URUK_HAI)
			{
				addAchievement(entityplayer, LOTRAchievement.alignmentGood10_URUK_HAI);
			}
			if (faction == LOTRFaction.GONDOR)
			{
				addAchievement(entityplayer, LOTRAchievement.alignmentGood10_GONDOR);
			}
			if (faction == LOTRFaction.MORDOR)
			{
				addAchievement(entityplayer, LOTRAchievement.alignmentGood10_MORDOR);
			}
			if (faction == LOTRFaction.NEAR_HARAD)
			{
				addAchievement(entityplayer, LOTRAchievement.alignmentGood10_NEAR_HARAD);
			}
			if (faction == LOTRFaction.FAR_HARAD)
			{
				addAchievement(entityplayer, LOTRAchievement.alignmentGood10_FAR_HARAD);
			}
			if (faction == LOTRFaction.HALF_TROLL)
			{
				addAchievement(entityplayer, LOTRAchievement.alignmentGood10_HALF_TROLL);
			}
		}
		
		if (alignment >= 100)
		{
			if (faction == LOTRFaction.HOBBIT)
			{
				addAchievement(entityplayer, LOTRAchievement.alignmentGood100_HOBBIT);
			}
			if (faction == LOTRFaction.RANGER_NORTH)
			{
				addAchievement(entityplayer, LOTRAchievement.alignmentGood100_RANGER_NORTH);
			}
			if (faction == LOTRFaction.BLUE_MOUNTAINS)
			{
				addAchievement(entityplayer, LOTRAchievement.alignmentGood100_BLUE_MOUNTAINS);
			}
			if (faction == LOTRFaction.HIGH_ELF)
			{
				addAchievement(entityplayer, LOTRAchievement.alignmentGood100_HIGH_ELF);
			}
			if (faction == LOTRFaction.GUNDABAD)
			{
				addAchievement(entityplayer, LOTRAchievement.alignmentGood100_GUNDABAD);
			}
			if (faction == LOTRFaction.ANGMAR)
			{
				addAchievement(entityplayer, LOTRAchievement.alignmentGood100_ANGMAR);
			}
			if (faction == LOTRFaction.WOOD_ELF)
			{
				addAchievement(entityplayer, LOTRAchievement.alignmentGood100_WOOD_ELF);
			}
			if (faction == LOTRFaction.DOL_GULDUR)
			{
				addAchievement(entityplayer, LOTRAchievement.alignmentGood100_DOL_GULDUR);
			}
			if (faction == LOTRFaction.DWARF)
			{
				addAchievement(entityplayer, LOTRAchievement.alignmentGood100_DWARF);
			}
			if (faction == LOTRFaction.GALADHRIM)
			{
				addAchievement(entityplayer, LOTRAchievement.alignmentGood100_GALADHRIM);
			}
			if (faction == LOTRFaction.DUNLAND)
			{
				addAchievement(entityplayer, LOTRAchievement.alignmentGood100_DUNLAND);
			}
			if (faction == LOTRFaction.FANGORN)
			{
				addAchievement(entityplayer, LOTRAchievement.alignmentGood100_FANGORN);
			}
			if (faction == LOTRFaction.ROHAN)
			{
				addAchievement(entityplayer, LOTRAchievement.alignmentGood100_ROHAN);
			}
			if (faction == LOTRFaction.URUK_HAI)
			{
				addAchievement(entityplayer, LOTRAchievement.alignmentGood100_URUK_HAI);
			}
			if (faction == LOTRFaction.GONDOR)
			{
				addAchievement(entityplayer, LOTRAchievement.alignmentGood100_GONDOR);
			}
			if (faction == LOTRFaction.MORDOR)
			{
				addAchievement(entityplayer, LOTRAchievement.alignmentGood100_MORDOR);
			}
			if (faction == LOTRFaction.NEAR_HARAD)
			{
				addAchievement(entityplayer, LOTRAchievement.alignmentGood100_NEAR_HARAD);
			}
			if (faction == LOTRFaction.FAR_HARAD)
			{
				addAchievement(entityplayer, LOTRAchievement.alignmentGood100_FAR_HARAD);
			}
			if (faction == LOTRFaction.HALF_TROLL)
			{
				addAchievement(entityplayer, LOTRAchievement.alignmentGood100_HALF_TROLL);
			}
		}
		
		if (alignment >= 1000)
		{
			if (faction == LOTRFaction.HOBBIT)
			{
				addAchievement(entityplayer, LOTRAchievement.alignmentGood1000_HOBBIT);
			}
			if (faction == LOTRFaction.RANGER_NORTH)
			{
				addAchievement(entityplayer, LOTRAchievement.alignmentGood1000_RANGER_NORTH);
			}
			if (faction == LOTRFaction.BLUE_MOUNTAINS)
			{
				addAchievement(entityplayer, LOTRAchievement.alignmentGood1000_BLUE_MOUNTAINS);
			}
			if (faction == LOTRFaction.HIGH_ELF)
			{
				addAchievement(entityplayer, LOTRAchievement.alignmentGood1000_HIGH_ELF);
			}
			if (faction == LOTRFaction.GUNDABAD)
			{
				addAchievement(entityplayer, LOTRAchievement.alignmentGood1000_GUNDABAD);
			}
			if (faction == LOTRFaction.ANGMAR)
			{
				addAchievement(entityplayer, LOTRAchievement.alignmentGood1000_ANGMAR);
			}
			if (faction == LOTRFaction.WOOD_ELF)
			{
				addAchievement(entityplayer, LOTRAchievement.alignmentGood1000_WOOD_ELF);
			}
			if (faction == LOTRFaction.DOL_GULDUR)
			{
				addAchievement(entityplayer, LOTRAchievement.alignmentGood1000_DOL_GULDUR);
			}
			if (faction == LOTRFaction.DWARF)
			{
				addAchievement(entityplayer, LOTRAchievement.alignmentGood1000_DWARF);
			}
			if (faction == LOTRFaction.GALADHRIM)
			{
				addAchievement(entityplayer, LOTRAchievement.alignmentGood1000_GALADHRIM);
			}
			if (faction == LOTRFaction.DUNLAND)
			{
				addAchievement(entityplayer, LOTRAchievement.alignmentGood1000_DUNLAND);
			}
			if (faction == LOTRFaction.FANGORN)
			{
				addAchievement(entityplayer, LOTRAchievement.alignmentGood1000_FANGORN);
			}
			if (faction == LOTRFaction.ROHAN)
			{
				addAchievement(entityplayer, LOTRAchievement.alignmentGood1000_ROHAN);
			}
			if (faction == LOTRFaction.URUK_HAI)
			{
				addAchievement(entityplayer, LOTRAchievement.alignmentGood1000_URUK_HAI);
			}
			if (faction == LOTRFaction.GONDOR)
			{
				addAchievement(entityplayer, LOTRAchievement.alignmentGood1000_GONDOR);
			}
			if (faction == LOTRFaction.MORDOR)
			{
				addAchievement(entityplayer, LOTRAchievement.alignmentGood1000_MORDOR);
			}
			if (faction == LOTRFaction.NEAR_HARAD)
			{
				addAchievement(entityplayer, LOTRAchievement.alignmentGood1000_NEAR_HARAD);
			}
			if (faction == LOTRFaction.FAR_HARAD)
			{
				addAchievement(entityplayer, LOTRAchievement.alignmentGood1000_FAR_HARAD);
			}
			if (faction == LOTRFaction.HALF_TROLL)
			{
				addAchievement(entityplayer, LOTRAchievement.alignmentGood1000_HALF_TROLL);
			}
		}
	}

	public static void setClientAlignment(UUID player, int alignment, LOTRFaction faction)
	{
		getAlignmentMap(player).put(faction, alignment);
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
				int alignment = getAlignment(entityplayer, faction);
				data.writeInt(alignment);
			}
			
			data.writeBoolean(getHideAlignment(entityplayer));
			
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
				int alignment = getAlignment(worldPlayer, faction);
				data.writeInt(alignment);
			}
			
			data.writeBoolean(getHideAlignment(worldPlayer));
			
			S3FPacketCustomPayload packet = new S3FPacketCustomPayload("lotr.alignment", data);
			((EntityPlayerMP)entityplayer).playerNetServerHandler.sendPacket(packet);
		}
	}
	
	public static void clearClientAlignments()
	{
		alignments.clear();
		hiddenAlignments.clear();
	}
	
	public static Packet getLoginAchievementsPacket(EntityPlayer entityplayer)
	{
		int achievementCount = 0;
		ArrayList achievementsToSend = new ArrayList();
		if (achievements.get(entityplayer.getUniqueID()) == null)
		{
			return null;
		}
		else
		{
			ArrayList playerAchievements = (ArrayList)achievements.get(entityplayer.getUniqueID());
			Iterator i = playerAchievements.iterator();
			while (i.hasNext())
			{
				LOTRAchievement achievement = (LOTRAchievement)i.next();
				achievementCount++;
				achievementsToSend.add(achievement);
			}
		}
		
		if (achievementCount == 0)
		{
			return null;
		}
		
		ByteBuf data = Unpooled.buffer();
		
		for (int i = 0; i < achievementCount; i++)
		{
			LOTRAchievement achievement = (LOTRAchievement)achievementsToSend.get(i);
			data.writeByte((byte)achievement.category.ordinal());
			data.writeByte((byte)achievement.ID);
		}
		
		return new S3FPacketCustomPayload("lotr.loginAch", data);
	}
	
	public static void addAchievement(EntityPlayer entityplayer, LOTRAchievement achievement)
	{
		if (hasAchievement(entityplayer, achievement))
		{
			return;
		}
		if (achievements.get(entityplayer.getUniqueID()) == null)
		{
			achievements.put(entityplayer.getUniqueID(), new ArrayList());
		}
		
		if (achievement.canPlayerEarn(entityplayer))
		{
			ArrayList playerAchievements = (ArrayList)achievements.get(entityplayer.getUniqueID());
			playerAchievements.add(achievement);
			needsSave = true;
			
			ByteBuf data = Unpooled.buffer();
			
			data.writeByte((byte)achievement.category.ordinal());
			data.writeByte((byte)achievement.ID);
			
			S3FPacketCustomPayload packet = new S3FPacketCustomPayload("lotr.achieve", data);
			((EntityPlayerMP)entityplayer).playerNetServerHandler.sendPacket(packet);
			
			MinecraftServer.getServer().getConfigurationManager().sendChatMsg(new ChatComponentTranslation("chat.lotr.achievement", new Object[] {entityplayer.func_145748_c_(), achievement.getChatComponentForEarn()}));
			
			List earnedAchievements = getPlayerEarnedAchievements(entityplayer);
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
				addAchievement(entityplayer, LOTRAchievement.travel10);
			}
			if (biomes >= 20)
			{
				addAchievement(entityplayer, LOTRAchievement.travel20);
			}
			if (biomes >= 30)
			{
				addAchievement(entityplayer, LOTRAchievement.travel30);
			}
			if (biomes >= 40)
			{
				addAchievement(entityplayer, LOTRAchievement.travel40);
			}
			if (biomes >= 50)
			{
				addAchievement(entityplayer, LOTRAchievement.travel50);
			}
		}
	}
	
	public static ArrayList getPlayerAchievements(EntityPlayer entityplayer)
	{
		if (achievements.get(entityplayer.getUniqueID()) == null)
		{
			achievements.put(entityplayer.getUniqueID(), new ArrayList());
		}
		return (ArrayList)achievements.get(entityplayer.getUniqueID());
	}
	
	public static ArrayList getPlayerEarnedAchievements(EntityPlayer entityplayer)
	{
		ArrayList earnedAchievements = new ArrayList();
		Iterator it = getPlayerAchievements(entityplayer).iterator();
		while (it.hasNext())
		{
			LOTRAchievement achievement = (LOTRAchievement)it.next();
			if (achievement.canPlayerEarn(entityplayer))
			{
				earnedAchievements.add(achievement);
			}
		}
		return earnedAchievements;
	}
	
	public static void addClientAchievement(EntityPlayer entityplayer, LOTRAchievement achievement)
	{
		if (hasAchievement(entityplayer, achievement))
		{
			return;
		}
		if (achievements.get(entityplayer.getUniqueID()) == null)
		{
			achievements.put(entityplayer.getUniqueID(), new ArrayList());
		}
		ArrayList playerAchievements = (ArrayList)achievements.get(entityplayer.getUniqueID());
		playerAchievements.add(achievement);
	}
	
	public static boolean hasAchievement(EntityPlayer entityplayer, LOTRAchievement achievement)
	{
		if (achievements.get(entityplayer.getUniqueID()) != null)
		{
			ArrayList playerAchievements = (ArrayList)achievements.get(entityplayer.getUniqueID());
			Iterator i = playerAchievements.iterator();
			while (i.hasNext())
			{
				LOTRAchievement a = (LOTRAchievement)i.next();
				if (a.category == achievement.category && a.ID == achievement.ID)
				{
					return true;
				}
			}
		}
		return false;
	}
	
	public static void clearClientAchievements()
	{
		achievements.clear();
	}
	
	public static LOTRCapes getCape(EntityPlayer entityplayer)
	{
		return (LOTRCapes)capes.get(entityplayer.getUniqueID());
	}
	
	public static void setCape(EntityPlayer entityplayer, LOTRCapes cape)
	{
		capes.put(entityplayer.getUniqueID(), cape);
		needsSave = true;
	}
	
	public static void setClientCape(UUID player, LOTRCapes cape)
	{
		capes.put(player, cape);
	}
	
	public static boolean getEnableCape(EntityPlayer entityplayer)
	{
		Object obj = capesEnabled.get(entityplayer.getUniqueID());
		return obj == null ? true : (Boolean)obj;
	}
	
	public static void setEnableCape(EntityPlayer entityplayer, boolean flag)
	{
		capesEnabled.put(entityplayer.getUniqueID(), flag);
		needsSave = true;
	}
	
	public static void setClientEnableCape(UUID player, boolean flag)
	{
		capesEnabled.put(player, flag);
	}
	
	public static void selectDefaultCapeForPlayer(EntityPlayer entityplayer)
	{
		if (getCape(entityplayer) == null)
		{
			for (LOTRCapes cape : LOTRCapes.values())
			{
				if (cape.canPlayerWearCape(entityplayer))
				{
					setCape(entityplayer, cape);
					return;
				}
			}
		}
	}
	
	public static void sendCapeToAllPlayersInWorld(EntityPlayer entityplayer, World world)
	{
		LOTRCapes cape = getCape(entityplayer);
		if (cape != null)
		{
			for (int i = 0; i < world.playerEntities.size(); i++)
			{
				EntityPlayer worldPlayer = (EntityPlayer)world.playerEntities.get(i);
				
				ByteBuf data = Unpooled.buffer();
				
				data.writeLong(entityplayer.getUniqueID().getMostSignificantBits());
				data.writeLong(entityplayer.getUniqueID().getLeastSignificantBits());
				
				data.writeByte((byte)cape.capeID);
				data.writeByte((byte)cape.capeType.ordinal());
				data.writeBoolean(getEnableCape(entityplayer));
				
				S3FPacketCustomPayload packet = new S3FPacketCustomPayload("lotr.updateCape", data);
				((EntityPlayerMP)worldPlayer).playerNetServerHandler.sendPacket(packet);
			}
		}
	}
	
	public static void sendAllCapesInWorldToPlayer(EntityPlayer entityplayer, World world)
	{
		for (int i = 0; i < world.playerEntities.size(); i++)
		{
			EntityPlayer worldPlayer = (EntityPlayer)world.playerEntities.get(i);
			LOTRCapes cape = getCape(worldPlayer);
			if (cape != null)
			{
				ByteBuf data = Unpooled.buffer();
				
				data.writeLong(worldPlayer.getUniqueID().getMostSignificantBits());
				data.writeLong(worldPlayer.getUniqueID().getLeastSignificantBits());
				
				data.writeByte((byte)cape.capeID);
				data.writeByte((byte)cape.capeType.ordinal());
				data.writeBoolean(getEnableCape(worldPlayer));
				
				S3FPacketCustomPayload packet = new S3FPacketCustomPayload("lotr.updateCape", data);
				((EntityPlayerMP)entityplayer).playerNetServerHandler.sendPacket(packet);
			}
		}
	}
	
	public static void clearClientCapes()
	{
		capes.clear();
		capesEnabled.clear();
	}
	
	public static boolean getFriendlyFire(EntityPlayer entityplayer)
	{
		Object obj = friendlyFire.get(entityplayer.getUniqueID());
		return obj != null ? (Boolean)obj : false;
	}
	
	public static void setFriendlyFire(EntityPlayer entityplayer, boolean flag)
	{
		friendlyFire.put(entityplayer.getUniqueID(), flag);
		
		if (!entityplayer.worldObj.isRemote)
		{
			needsSave = true;
			sendOptionsPacket(entityplayer, LOTROptions.FRIENDLY_FIRE, flag);
		}
	}
	
	public static boolean getEnableHiredDeathMessages(EntityPlayer entityplayer)
	{
		Object obj = hiredDeathMessages.get(entityplayer.getUniqueID());
		return obj != null ? (Boolean)obj : true;
	}
	
	public static void setEnableHiredDeathMessages(EntityPlayer entityplayer, boolean flag)
	{
		hiredDeathMessages.put(entityplayer.getUniqueID(), flag);
		
		if (!entityplayer.worldObj.isRemote)
		{
			needsSave = true;
			sendOptionsPacket(entityplayer, LOTROptions.HIRED_DEATH_MESSAGES, flag);
		}
	}
	
	private static void sendOptionsPacket(EntityPlayer entityplayer, int option, boolean flag)
	{
		ByteBuf data = Unpooled.buffer();
		
		data.writeByte((byte)option);
		data.writeBoolean(flag);
		
		S3FPacketCustomPayload packet = new S3FPacketCustomPayload("lotr.options", data);
		((EntityPlayerMP)entityplayer).playerNetServerHandler.sendPacket(packet);
	}
	
	public static boolean getPlayerFoundMelon(EntityPlayer entityplayer)
	{
		return melonFinders.contains(entityplayer.getUniqueID());
	}
	
	public static void setPlayerFoundMelon(EntityPlayer entityplayer)
	{
		melonFinders.add(entityplayer.getUniqueID());
		
		if (!entityplayer.worldObj.isRemote)
		{
			needsSave = true;
			S3FPacketCustomPayload packet = new S3FPacketCustomPayload("lotr.findMelon", Unpooled.buffer(0));
			((EntityPlayerMP)entityplayer).playerNetServerHandler.sendPacket(packet);
		}
	}
	
	public static boolean getHideAlignment(EntityPlayer entityplayer)
	{
		return hiddenAlignments.contains(entityplayer.getUniqueID());
	}
	
	public static void setHideAlignment(EntityPlayer entityplayer, boolean flag)
	{
		if (flag)
		{
			hiddenAlignments.add(entityplayer.getUniqueID());
		}
		else
		{
			hiddenAlignments.remove(entityplayer.getUniqueID());
		}
		
		needsSave = true;
		sendAlignmentToAllPlayersInWorld(entityplayer, entityplayer.worldObj);
	}
	
	public static void setClientHideAlignment(UUID player, boolean flag)
	{
		if (flag)
		{
			hiddenAlignments.add(player);
		}
		else
		{
			hiddenAlignments.remove(player);
		}
	}
	
	public static int getFastTravelTimer(EntityPlayer entityplayer)
	{
		Object obj = fastTravelTimers.get(entityplayer.getUniqueID());
		return obj != null ? (Integer)obj : 0;
	}
	
	public static void setFastTravelTimer(EntityPlayer entityplayer, int i)
	{
		if (i < 0)
		{
			i = 0;
		}
		fastTravelTimers.put(entityplayer.getUniqueID(), i);
		
		if (!entityplayer.worldObj.isRemote)
		{
			needsSave = true;
			
			ByteBuf data = Unpooled.buffer();
			
			data.writeInt(i);

			S3FPacketCustomPayload packet = new S3FPacketCustomPayload("lotr.ftTimer", data);
			((EntityPlayerMP)entityplayer).playerNetServerHandler.sendPacket(packet);
		}
	}
	
	public static void setFastTravelCooldown(int i)
	{
		if (i < 0)
		{
			i = 0;
		}
		fastTravelCooldown = i;
		needsSave = true;
	}
	
	public static int getViewingFactionID(EntityPlayer entityplayer)
	{
		Object obj = viewingFactions.get(entityplayer.getUniqueID());
		return obj != null ? ((LOTRFaction)obj).ordinal() : -1;
	}
	
	public static void setViewingFaction(EntityPlayer entityplayer, LOTRFaction faction)
	{
		if (!faction.allowPlayer)
		{
			return;
		}
		viewingFactions.put(entityplayer.getUniqueID(), faction);
		
		if (!entityplayer.worldObj.isRemote)
		{
			needsSave = true;
		}
	}
	
	public static boolean getShowMapLocation(EntityPlayer entityplayer)
	{
		return !hiddenMapPlayers.contains(entityplayer.getUniqueID());
	}
	
	public static void setShowMapLocation(EntityPlayer entityplayer, boolean flag)
	{
		if (!flag)
		{
			hiddenMapPlayers.add(entityplayer.getUniqueID());
		}
		else
		{
			hiddenMapPlayers.remove(entityplayer.getUniqueID());
		}
		
		if (!entityplayer.worldObj.isRemote)
		{
			needsSave = true;
			
			ByteBuf data = Unpooled.buffer();
			
			data.writeBoolean(flag);

			S3FPacketCustomPayload packet = new S3FPacketCustomPayload("lotr.showPos", data);
			((EntityPlayerMP)entityplayer).playerNetServerHandler.sendPacket(packet);
		}
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
			
			if (isOp || getShowMapLocation(worldPlayer))
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
			needsSave = true;
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
