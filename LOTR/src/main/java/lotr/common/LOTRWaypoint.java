package lotr.common;

import static lotr.common.LOTRFaction.UNALIGNED;
import static lotr.common.LOTRWaypoint.Region.*;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import lotr.common.world.genlayer.LOTRGenLayerWorld;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S3FPacketCustomPayload;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;

import com.google.common.base.Charsets;

public enum LOTRWaypoint implements LOTRAbstractWaypoint
{
	HIMLING(OCEAN, UNALIGNED, 485, 523),
	TOL_FUIN(OCEAN, UNALIGNED, 357, 542),
	TOL_MORWEN(OCEAN, UNALIGNED, 87, 698),
	
	HOBBITON(SHIRE, LOTRFaction.HOBBIT, 815, 730),
	BRANDYWINE_BRIDGE(SHIRE, LOTRFaction.HOBBIT, 850, 723),
	SARN_FORD(SHIRE, LOTRFaction.HOBBIT, 882, 801),
	
	WITHYWINDLE_VALLEY(OLD_FOREST, UNALIGNED, 881, 749),
	
	FORLOND(LINDON, LOTRFaction.HIGH_ELF, 526, 718),
	HARLOND(LINDON, LOTRFaction.HIGH_ELF, 605, 783),
	MITHLOND(LINDON, LOTRFaction.HIGH_ELF, 679, 729),
	FORLINDON(LINDON, LOTRFaction.HIGH_ELF, 493, 688),
	HARLINDON(LINDON, LOTRFaction.HIGH_ELF, 611, 878),
	
	BELEGOST(BLUE_MOUNTAINS, UNALIGNED, 622, 600),
	NOGROD(BLUE_MOUNTAINS, UNALIGNED, 626, 636),
	
	NORTH_DOWNS(ERIADOR, UNALIGNED, 930, 626),
	FAR_DOWNS(ERIADOR, UNALIGNED, 756, 745),
	SOUTH_DOWNS(ERIADOR, UNALIGNED, 960, 768),
	ERYN_VORN(ERIADOR, UNALIGNED, 747, 957),
	THARBAD(ERIADOR, UNALIGNED, 979, 878),
	FORNOST(ERIADOR, UNALIGNED, 897, 652),
	ANNUMINAS(ERIADOR, UNALIGNED, 814, 661),
	TOWER_HILLS(ERIADOR, UNALIGNED, 722, 742),
	
	BREE(BREE_LAND, UNALIGNED, 920, 737),
	CHETWOOD(BREE_LAND, UNALIGNED, 929, 724),
	
	MIDGEWATER_MARSHES(MIDGEWATER, UNALIGNED, 951, 719),
	
	WEATHERTOP(LONE_LANDS, UNALIGNED, 998, 723),
	LAST_BRIDGE(LONE_LANDS, UNALIGNED, 1086, 715),
	RIVENDELL(LONE_LANDS, LOTRFaction.HIGH_ELF, 1174, 717),
	
	THE_TROLLSHAWS(TROLLSHAWS, UNALIGNED, 1130, 709),
	
	CARN_DUM(ANGMAR, LOTRFaction.ANGMAR, 1000, 510),
	
	WEST_GATE(EREGION, UNALIGNED, 1131, 869),
	
	NORTH_DUNLAND(DUNLAND, LOTRFaction.DUNLAND, 1073, 946),
	SOUTH_DUNLAND(DUNLAND, LOTRFaction.DUNLAND, 1070, 1027),
	
	LOND_DAER(ENEDWAITH, UNALIGNED, 867, 1004),
	DRUWAITH_IAUR(ENEDWAITH, UNALIGNED, 880, 1204),
	ISENGARD(ENEDWAITH, UNALIGNED, 1102, 1058),
	
	CAPE_OF_FOROCHEL(FORODWAITH, UNALIGNED, 786, 390),
	SOUTH_FOROCHEL(FORODWAITH, UNALIGNED, 825, 459),
	WITHERED_HEATH(FORODWAITH, UNALIGNED, 1441, 556),

	MOUNT_GUNDABAD(MISTY_MOUNTAINS, LOTRFaction.GUNDABAD, 1195, 592),
	MOUNT_GRAM(MISTY_MOUNTAINS, UNALIGNED, 1106, 589),
	HIGH_PASS(MISTY_MOUNTAINS, UNALIGNED, 1222, 706),
	MOUNT_CARADHRAS(MISTY_MOUNTAINS, UNALIGNED, 1175, 840),
	MOUNT_CELEBDIL(MISTY_MOUNTAINS, UNALIGNED, 1162, 849),
	MOUNT_FANUIDHOL(MISTY_MOUNTAINS, UNALIGNED, 1188, 850),
	MOUNT_METHEDRAS(MISTY_MOUNTAINS, UNALIGNED, 1111, 1031),
	
	CARROCK(VALES_OF_ANDUIN, UNALIGNED, 1295, 675),
	OLD_FORD(VALES_OF_ANDUIN, UNALIGNED, 1283, 702),
	GLADDEN_FIELDS(VALES_OF_ANDUIN, UNALIGNED, 1292, 792),
	DIMRILL_DALE(VALES_OF_ANDUIN, UNALIGNED, 1177, 864),
	FIELD_OF_CELEBRANT(VALES_OF_ANDUIN, UNALIGNED, 1281, 960),
	NORTH_UNDEEP(VALES_OF_ANDUIN, UNALIGNED, 1319, 988),
	SOUTH_UNDEEP(VALES_OF_ANDUIN, UNALIGNED, 1335, 1024),
	
	WOODLAND_REALM(MIRKWOOD, LOTRFaction.WOOD_ELF, 1415, 640),
	
	DOL_GULDUR(MIRKWOOD_CORRUPTED, LOTRFaction.DOL_GULDUR, 1339, 894),
	MIRKWOOD_MOUNTAINS(MIRKWOOD_CORRUPTED, UNALIGNED, 1430, 672),
	
	EREBOR(WILDERLAND, UNALIGNED, 1464, 606),
	LONG_LAKE(WILDERLAND, UNALIGNED, 1470, 637),
	EAST_BIGHT(WILDERLAND, UNALIGNED, 1437, 824),
	EMYN_RHUNEN(WILDERLAND, UNALIGNED, 1733, 950),
	
	WEST_PEAK(IRON_HILLS, UNALIGNED, 1588, 608),
	EAST_PEAK(IRON_HILLS, UNALIGNED, 1729, 610),
	
	CARAS_GALADHON(LOTHLORIEN, LOTRFaction.GALADHRIM, 1242, 902),
	
	DERNDINGLE(FANGORN, LOTRFaction.FANGORN, 1163, 1030),
	
	WOLD(ROHAN, UNALIGNED, 1285, 1020),
	EDORAS(ROHAN, LOTRFaction.ROHAN, 1190, 1148),
	HELMS_DEEP(ROHAN, UNALIGNED, 1130, 1112),
	URUK_HIGHLANDS(ROHAN, LOTRFaction.URUK_HAI, 1131, 1057),
	FORDS_OF_ISEN(ROHAN, UNALIGNED, 1107, 1087),
	MERING_STREAM(ROHAN, UNALIGNED, 1297, 1202),
	
	MINAS_TIRITH(GONDOR, LOTRFaction.GONDOR, 1420, 1247),
	OSGILIATH(GONDOR, UNALIGNED, 1437, 1245),
	CAIR_ANDROS(GONDOR, LOTRFaction.GONDOR, 1427, 1207),
	EMYN_ARNEN(GONDOR, UNALIGNED, 1437, 1267),
	DOL_AMROTH(GONDOR, LOTRFaction.GONDOR, 1162, 1333),
	ERECH(GONDOR, UNALIGNED, 1188, 1204),
	PINNATH_GELIN(GONDOR, UNALIGNED, 1045, 1273),
	ANDUIN_MOUTH(GONDOR, UNALIGNED, 1273, 1369),
	EDHELLOND(GONDOR, UNALIGNED, 1191, 1291),
	PELARGIR(GONDOR, UNALIGNED, 1390, 1348),
	
	TOLFALAS_ISLAND(TOLFALAS, UNALIGNED, 1240, 1414),
	
	RAUROS(EMYN_MUIL, UNALIGNED, 1357, 1135),
	
	MORANNON(DAGORLAD, UNALIGNED, 1465, 1131),
	
	UDUN(MORDOR, LOTRFaction.MORDOR, 1483, 1150),
	MOUNT_DOOM(MORDOR, LOTRFaction.MORDOR, 1533, 1204),
	BARAD_DUR(MORDOR, LOTRFaction.MORDOR, 1573, 1196),
	MINAS_MORGUL(MORDOR, LOTRFaction.MORDOR, 1463, 1238),
	
	NURNEN_NORTHERN_SHORE(NURN, LOTRFaction.MORDOR, 1696, 1324),
	NURNEN_SOUTHERN_SHORE(NURN, LOTRFaction.MORDOR, 1726, 1369),
	NURNEN_WESTERN_SHORE(NURN, LOTRFaction.MORDOR, 1650, 1363),
	NURNEN_EASTERN_SHORE(NURN, LOTRFaction.MORDOR, 1758, 1316),
	
	VALLEY_OF_SPIDERS(NAN_UNGOL, LOTRFaction.MORDOR, 1512, 1400),
	
	CROSSINGS_OF_POROS(HARONDOR, UNALIGNED, 1443, 1372),
	
	CROSSINGS_OF_HARAD(NEAR_HARAD, UNALIGNED, 1504, 1546),
	
	GULF_OF_HARAD(NEAR_HARAD_FERTILE, UNALIGNED, 1634, 1915),
	ISLAND_THREE_RIVERS(NEAR_HARAD_FERTILE, UNALIGNED, 1362, 1770),
	FERTILE_VALLEY(NEAR_HARAD_FERTILE, UNALIGNED, 1530, 1811),
	
	UMBAR_WAYPOINT(UMBAR, UNALIGNED, 1183, 1679),
	
	MOUNT_SAND(FAR_HARAD, UNALIGNED, 959, 1899),
	MOUNT_GREEN(FAR_HARAD, UNALIGNED, 884, 2372),
	MOUNT_THUNDER(FAR_HARAD, UNALIGNED, 1019, 2590),
	GREAT_PLAINS_NORTH(FAR_HARAD, UNALIGNED, 1387, 1984),
	GREAT_PLAINS_SOUTH(FAR_HARAD, UNALIGNED, 1462, 2497),
	GREAT_PLAINS_WEST(FAR_HARAD, UNALIGNED, 1048, 2205),
	GREAT_PLAINS_EAST(FAR_HARAD, UNALIGNED, 1637, 2176),
	GREEN_VALLEY(FAR_HARAD, UNALIGNED, 1613, 2588),
	HARAD_LAKES(FAR_HARAD, UNALIGNED, 1774, 2310),
	LAKE_HARAD(FAR_HARAD, UNALIGNED, 1100, 2592),
	HARADUIN_MOUTH(FAR_HARAD, UNALIGNED, 1858, 2847),
	ISLE_MIST(FAR_HARAD, UNALIGNED, 1533, 3573),
	
	TROLL_ISLAND(PERTOROGWAITH, UNALIGNED, 1966, 2342),
	BLACK_COAST(PERTOROGWAITH, UNALIGNED, 1936, 2496),
	BLOOD_RIVER(PERTOROGWAITH, UNALIGNED, 1897, 2605),
	SHADOW_POINT(PERTOROGWAITH, UNALIGNED, 1952, 2863);
	
	private Region region;
	public LOTRFaction faction;
	private int x;
	private int y;
	private int xCoord;
	private int zCoord;
	
	private LOTRWaypoint(Region r, LOTRFaction f, int i, int j)
	{
		region = r;
		region.waypoints.add(this);
		faction = f;
		x = i;
		y = j;
		xCoord = (int)(((double)(x - LOTRGenLayerWorld.originX) + 0.5D) * LOTRGenLayerWorld.scale);
		zCoord = (int)(((double)(y - LOTRGenLayerWorld.originZ) + 0.5D) * LOTRGenLayerWorld.scale);
	}
	
	public enum Region
	{
		OCEAN,
		SHIRE,
		OLD_FOREST,
		LINDON,
		BLUE_MOUNTAINS,
		ERIADOR,
		BREE_LAND,
		MIDGEWATER,
		LONE_LANDS,
		TROLLSHAWS,
		COLDFELLS,
		ETTENMOORS,
		ANGMAR,
		EREGION,
		DUNLAND,
		ENEDWAITH,
		FORODWAITH,
		MISTY_MOUNTAINS,
		GREY_MOUNTAINS,
		VALES_OF_ANDUIN,
		MIRKWOOD,
		MIRKWOOD_CORRUPTED,
		WILDERLAND,
		IRON_HILLS,
		LOTHLORIEN,
		FANGORN,
		ROHAN,
		GONDOR,
		TOLFALAS,
		EMYN_MUIL,
		NINDALF,
		BROWN_LANDS,
		DAGORLAD,
		MORDOR,
		NURN,
		NAN_UNGOL,
		HARONDOR,
		NEAR_HARAD,
		NEAR_HARAD_FERTILE,
		UMBAR,
		FAR_HARAD,
		PERTOROGWAITH,
		RHUN,
		RED_MOUNTAINS;
		
		public List waypoints = new ArrayList();
		public List playersUnlocked = new ArrayList();
		
		public void unlockForPlayer(EntityPlayer entityplayer)
		{
			if (!playersUnlocked.contains(entityplayer.getUniqueID()))
			{
				playersUnlocked.add(entityplayer.getUniqueID());
				
				if (!entityplayer.worldObj.isRemote)
				{
					LOTRLevelData.needsSave = true;
					
					ByteBuf data = Unpooled.buffer();
					
					data.writeByte((byte)ordinal());
					
					Packet packet = new S3FPacketCustomPayload("lotr.WP", data);
					((EntityPlayerMP)entityplayer).playerNetServerHandler.sendPacket(packet);
				}
			}
		}
		
		public boolean hasPlayerUnlocked(EntityPlayer entityplayer)
		{
			return playersUnlocked.contains(entityplayer.getUniqueID());
		}
	}
	
	@Override
	public int getX()
	{
		return x;
	}
	
	@Override
	public int getY()
	{
		return y;
	}
	
	@Override
	public int getXCoord()
	{
		return xCoord;
	}
	
	@Override
	public int getZCoord()
	{
		return zCoord;
	}
	
	@Override
	public String getDisplayName()
	{
		return StatCollector.translateToLocal("lotr.waypoint." + name());
	}
	
	@Override
	public boolean hasPlayerUnlocked(EntityPlayer entityplayer)
	{
		return region.hasPlayerUnlocked(entityplayer) && isUnlockable(entityplayer);
	}
	
	@Override
	public boolean isUnlockable(EntityPlayer entityplayer)
	{
		return faction == LOTRFaction.UNALIGNED || LOTRLevelData.getAlignment(entityplayer, faction) >= 0;
	}
	
	@Override
	public int getID()
	{
		return ordinal();
	}
	
	public static Packet getLoginWaypointsPacket(EntityPlayer entityplayer)
	{
		List regionsUnlocked = new ArrayList();
		for (Region region : Region.values())
		{
			if (region.hasPlayerUnlocked(entityplayer))
			{
				regionsUnlocked.add(region);
			}
		}
		
		if (regionsUnlocked.isEmpty())
		{
			return null;
		}
		else
		{
			ByteBuf data = Unpooled.buffer();
			for (int i = 0; i < regionsUnlocked.size(); i++)
			{
				data.writeByte((byte)((Region)regionsUnlocked.get(i)).ordinal());
			}
			Packet packet = new S3FPacketCustomPayload("lotr.loginWP", data);
			return packet;
		}
	}
	
	public static void clearAllWaypoints()
	{
		for (Region region : Region.values())
		{
			region.playersUnlocked.clear();
		}
	}
	
	public static Region regionForName(String name)
	{
		for (Region region : Region.values())
		{
			if (region.name().equals(name))
			{
				return region;
			}
		}
		return null;
	}
	
	public static Region regionForID(int id)
	{
		for (Region region : Region.values())
		{
			if (region.ordinal() == id)
			{
				return region;
			}
		}
		return null;
	}
	
	public static void save(NBTTagCompound levelData)
	{
		NBTTagList waypointData = new NBTTagList();
		
		for (Region region : Region.values())
		{
			if (region.playersUnlocked.isEmpty())
			{
				continue;
			}
			
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setString("Name", region.name());
			
			NBTTagList players = new NBTTagList();
			for (Object obj: region.playersUnlocked)
			{
				if (obj instanceof UUID)
				{
					NBTTagCompound playerData = new NBTTagCompound();
					playerData.setLong("UUIDMost", ((UUID)obj).getMostSignificantBits());
					playerData.setLong("UUIDLeast", ((UUID)obj).getLeastSignificantBits());
					players.appendTag(playerData);
				}
			}
			nbt.setTag("Players", players);
			
			waypointData.appendTag(nbt);
		}
		
		levelData.setTag("Waypoints", waypointData);
		
		Custom.save(levelData);
	}
	
	public static void load(NBTTagCompound levelData)
	{
		clearAllWaypoints();
		
		NBTTagList waypointData = levelData.getTagList("Waypoints", new NBTTagCompound().getId());
		if (waypointData != null)
		{
			for (int i = 0; i < waypointData.tagCount(); i++)
			{
				NBTTagCompound nbt = (NBTTagCompound)waypointData.getCompoundTagAt(i);
				String name = nbt.getString("Name");
				Region region = regionForName(name);
				if (region != null)
				{
					NBTTagList players = nbt.getTagList("Players", new NBTTagCompound().getId());
					if (players != null)
					{
						for (int j = 0; j < players.tagCount(); j++)
						{
							NBTTagCompound playerData = (NBTTagCompound)players.getCompoundTagAt(j);
							UUID player = new UUID(playerData.getLong("UUIDMost"), playerData.getLong("UUIDLeast"));
							region.playersUnlocked.add(player);
						}
					}
				}
			}
		}
		
		Custom.load(levelData);
	}
	
	public static List getListOfAllWaypoints(EntityPlayer entityplayer)
	{
		List waypoints = new ArrayList();
		for (LOTRWaypoint waypoint : LOTRWaypoint.values())
		{
			waypoints.add(waypoint);
		}
		waypoints.addAll(Custom.getWaypointList(entityplayer));
		return waypoints;
	}
	
	public static class Custom implements LOTRAbstractWaypoint
	{
		public static int MAX_CUSTOM = 20;

		public static Map playerCustomWaypoints = new HashMap();
		
		private static int nextWaypointID = 0;
		
		private String name;
		private int x;
		private int y;
		private int xCoord;
		private int zCoord;
		private int ID;
		
		public Custom(String s, double posX, double posZ)
		{
			name = s;
			xCoord = MathHelper.floor_double(posX);
			zCoord = MathHelper.floor_double(posZ);
			x = (int)Math.round(((double)xCoord / (double)LOTRGenLayerWorld.scale) - 0.5D + LOTRGenLayerWorld.originX);
			y = (int)Math.round(((double)zCoord / (double)LOTRGenLayerWorld.scale) - 0.5D + LOTRGenLayerWorld.originZ);
			ID = nextWaypointID;
			nextWaypointID++;
		}

		public Custom(String s, int i, int j, int i1, int j1, int k)
		{
			name = s;
			x = i;
			y = j;
			xCoord = i1;
			zCoord = j1;
			ID = k;
		}
		
		@Override
		public int getX()
		{
			return x;
		}
		
		@Override
		public int getY()
		{
			return y;
		}
		
		@Override
		public int getXCoord()
		{
			return xCoord;
		}
		
		@Override
		public int getZCoord()
		{
			return zCoord;
		}
		
		@Override
		public String getDisplayName()
		{
			return StatCollector.translateToLocalFormatted("lotr.waypoint.custom", new Object[] {name});
		}
		
		@Override
		public boolean hasPlayerUnlocked(EntityPlayer entityplayer)
		{
			return true;
		}
		
		@Override
		public boolean isUnlockable(EntityPlayer entityplayer)
		{
			return true;
		}
		
		@Override
		public int getID()
		{
			return ID;
		}
		
		public static List getWaypointList(EntityPlayer entityplayer)
		{
			List waypoints = (List)playerCustomWaypoints.get(entityplayer.getUniqueID());
			if (waypoints == null)
			{
				waypoints = new ArrayList();
				playerCustomWaypoints.put(entityplayer.getUniqueID(), waypoints);
			}
			return waypoints;
		}
		
		public static void addCustomWaypoint(EntityPlayer entityplayer, Custom waypoint)
		{
			getWaypointList(entityplayer).add(waypoint);
			
			if (!entityplayer.worldObj.isRemote)
			{
				LOTRLevelData.needsSave = true;
				sendWaypointPacket(entityplayer, waypoint);
			}
		}
		
		public static void removeCustomWaypoint(EntityPlayer entityplayer, Custom waypoint)
		{
			if (getWaypointList(entityplayer).remove(waypoint))
			{
				if (!entityplayer.worldObj.isRemote)
				{
					LOTRLevelData.needsSave = true;
					
					ByteBuf data = Unpooled.buffer();
					
					data.writeInt(waypoint.ID);
					
					Packet packet = new S3FPacketCustomPayload("lotr.removeCWP", data);
					((EntityPlayerMP)entityplayer).playerNetServerHandler.sendPacket(packet);
				}
			}
		}
		
		public static void clearCustomWaypoints(EntityPlayer entityplayer)
		{
			getWaypointList(entityplayer).clear();
		}
		
		private static void sendWaypointPacket(EntityPlayer entityplayer, Custom waypoint)
		{
			ByteBuf data = Unpooled.buffer();
			
			data.writeInt(waypoint.x);
			data.writeInt(waypoint.y);
			data.writeInt(waypoint.xCoord);
			data.writeInt(waypoint.zCoord);
			data.writeInt(waypoint.ID);
			byte[] bytes = waypoint.name.getBytes(Charsets.UTF_8);
			data.writeShort(bytes.length);
			data.writeBytes(bytes);
			
			Packet packet = new S3FPacketCustomPayload("lotr.CWP", data);
			((EntityPlayerMP)entityplayer).playerNetServerHandler.sendPacket(packet);
		}
		
		public static void sendLoginCustomWaypointsPackets(EntityPlayer entityplayer)
		{
			Packet packet = new S3FPacketCustomPayload("lotr.clearCWP", Unpooled.buffer());
			((EntityPlayerMP)entityplayer).playerNetServerHandler.sendPacket(packet);

			List waypoints = getWaypointList(entityplayer);
			for (int i = 0; i < waypoints.size(); i++)
			{
				Custom waypoint = (Custom)waypoints.get(i);
				sendWaypointPacket(entityplayer, waypoint);
			}
		}
		
		public static Custom waypointForPlayerForID(EntityPlayer entityplayer, int ID)
		{
			List list = getWaypointList(entityplayer);
			for (int l = 0; l < list.size(); l++)
			{
				Custom waypoint = (Custom)list.get(l);
				if (waypoint.getID() == ID)
				{
					return waypoint;
				}
			}
			return null;
		}
		
		public static void save(NBTTagCompound levelData)
		{
			levelData.setInteger("NextCustomWaypointID", nextWaypointID);
			
			NBTTagList waypointData = new NBTTagList();
			
			Iterator iterator = playerCustomWaypoints.keySet().iterator();
			while (iterator.hasNext())
			{
				UUID player = (UUID)iterator.next();
				NBTTagCompound playerData = new NBTTagCompound();
				playerData.setLong("UUIDMost", player.getMostSignificantBits());
				playerData.setLong("UUIDLeast", player.getLeastSignificantBits());
				
				NBTTagList waypointList = new NBTTagList();
				List waypoints = (List)playerCustomWaypoints.get(player);
				for (int i = 0; i < waypoints.size(); i++)
				{
					Custom waypoint = (Custom)waypoints.get(i);
					NBTTagCompound nbt = new NBTTagCompound();
					nbt.setString("Name", waypoint.name);
					nbt.setInteger("X", waypoint.x);
					nbt.setInteger("Y", waypoint.y);
					nbt.setInteger("XCoord", waypoint.xCoord);
					nbt.setInteger("ZCoord", waypoint.zCoord);
					nbt.setInteger("ID", waypoint.ID);
					waypointList.appendTag(nbt);
				}
				
				playerData.setTag("Waypoints", waypointList);
				
				waypointData.appendTag(playerData);
			}

			levelData.setTag("CustomWaypoints", waypointData);
		}
		
		public static void load(NBTTagCompound levelData)
		{
			nextWaypointID = levelData.getInteger("NextCustomWaypointID");
			
			playerCustomWaypoints.clear();
			
			NBTTagList waypointData = levelData.getTagList("CustomWaypoints", new NBTTagCompound().getId());
			if (waypointData != null)
			{
				for (int i = 0; i < waypointData.tagCount(); i++)
				{
					NBTTagCompound playerData = waypointData.getCompoundTagAt(i);
					UUID player = new UUID(playerData.getLong("UUIDMost"), playerData.getLong("UUIDLeast"));
					List waypoints = new ArrayList();
					
					NBTTagList waypointList = playerData.getTagList("Waypoints", new NBTTagCompound().getId());
					if (waypointList != null)
					{
						for (int j = 0; j < waypointList.tagCount(); j++)
						{
							NBTTagCompound nbt = waypointList.getCompoundTagAt(j);
							String name = nbt.getString("Name");
							int x = nbt.getInteger("X");
							int y = nbt.getInteger("Y");
							int xCoord = nbt.getInteger("XCoord");
							int zCoord = nbt.getInteger("ZCoord");
							int ID = nbt.getInteger("ID");
							Custom waypoint = new Custom(name, x, y, xCoord, zCoord, ID);
							waypoints.add(waypoint);
						}
					}
					
					playerCustomWaypoints.put(player, waypoints);
				}
			}
		}
		
		public static int getMaxAvailableToPlayer(EntityPlayer entityplayer)
		{
			int achievements = 0;
			Iterator it = LOTRLevelData.getPlayerAchievements(entityplayer).iterator();
			while (it.hasNext())
			{
				LOTRAchievement achievement = (LOTRAchievement)it.next();
				if (achievement.canPlayerEarn(entityplayer))
				{
					achievements++;
				}
			}
			return 20 + achievements / 5;
		}
	}
}
