package lotr.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.List;
import java.util.UUID;

import lotr.client.fx.LOTREntityAlignmentBonus;
import lotr.client.fx.LOTREntityGandalfFireballExplodeFX;
import lotr.client.gui.LOTRGuiFastTravel;
import lotr.client.gui.LOTRGuiHiredFarmer;
import lotr.client.gui.LOTRGuiHiredWarrior;
import lotr.client.gui.LOTRGuiMap;
import lotr.client.gui.LOTRGuiMessage;
import lotr.client.gui.LOTRGuiTrade;
import lotr.common.LOTRAbstractWaypoint;
import lotr.common.LOTRAchievement;
import lotr.common.LOTRCapes;
import lotr.common.LOTRCapes.CapeType;
import lotr.common.LOTRFaction;
import lotr.common.LOTRGuiMessageTypes;
import lotr.common.LOTRLevelData;
import lotr.common.LOTRMod;
import lotr.common.LOTROptions;
import lotr.common.LOTRWaypoint;
import lotr.common.entity.npc.LOTREntityNPC;
import lotr.common.entity.npc.LOTRHiredNPCInfo.Task;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.S3FPacketCustomPayload;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import com.google.common.base.Charsets;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;

@ChannelHandler.Sharable
public class LOTRPacketHandlerClient extends SimpleChannelInboundHandler<FMLProxyPacket>
{
	private LOTRClientProxy theProxy;
	
	public LOTRPacketHandlerClient(LOTRClientProxy proxy)
	{
		theProxy = proxy;
		
		NetworkRegistry.INSTANCE.newChannel("lotr.login", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.loginAch", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.promptAch", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.promptAl", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.updateCape", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.portalPos", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.alignment", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.alignBonus", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.achieve", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.mace", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.staffWhite", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.fireball", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.trades", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.findMelon", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.options", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.message", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.hiredGui", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.isOp", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.WP", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.loginWP", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.ftTimer", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.ftGui", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.frost", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.burn", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.showPos", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.clearMap", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.playerPos", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.CWP", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.clearCWP", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.removeCWP", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.rewardItem", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.hearts", this);
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FMLProxyPacket packet) throws Exception
	{
		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayer entityplayer = mc.thePlayer;
		World world = entityplayer.worldObj;
		
		ByteBuf data = packet.payload();
		String channel = packet.channel();
		
		if (channel.equals("lotr.login"))
		{
			LOTRLevelData.middleEarthPortalX = data.readInt();
			LOTRLevelData.middleEarthPortalY = data.readInt();
			LOTRLevelData.middleEarthPortalZ = data.readInt();
			LOTRLevelData.beaconState = data.readByte();
			if (data.readBoolean())
			{
				LOTRLevelData.setPlayerFoundMelon(entityplayer);
			}
			LOTRLevelData.setFriendlyFire(entityplayer, data.readBoolean());
			LOTRLevelData.setEnableHiredDeathMessages(entityplayer, data.readBoolean());
			LOTRLevelData.setFastTravelTimer(entityplayer, data.readInt());

			int factionID = data.readInt();
			LOTRFaction faction = LOTRFaction.forID(factionID);
			if (faction != null && faction.allowPlayer)
			{
				LOTRTickHandlerClient.currentAlignmentFaction = faction;
			}
			
			LOTRLevelData.setShowMapLocation(entityplayer, data.readBoolean());
			
			if (!mc.isSingleplayer())
			{
				LOTRLevelData.clearClientAlignments();
				LOTRLevelData.clearClientAchievements();
				LOTRLevelData.clearClientCapes();
				LOTRWaypoint.clearAllWaypoints();
				LOTRGuiMap.playerLocations.clear();
			}
		}
		
		else if (channel.equals("lotr.loginAch"))
		{
			int achievements = data.array().length / 2;
			for (int i = 0; i < achievements; i++)
			{
				int categoryID = data.readByte();
				int achievementID = data.readByte();
				
				LOTRAchievement.Category c = LOTRAchievement.Category.values()[categoryID];
				LOTRAchievement achievement = LOTRAchievement.achievementForCategoryAndID(c, achievementID);
				if (achievement != null)
				{
					LOTRLevelData.addClientAchievement(entityplayer, achievement);
				}
			}
		}
		
		else if (channel.equals("lotr.promptAch"))
		{
			LOTRTickHandlerClient.renderMenuPrompt = true;
		}
		
		else if (channel.equals("lotr.promptAl"))
		{
			LOTRTickHandlerClient.renderAlignmentPrompt = true;
		}
		
		else if (channel.equals("lotr.portalPos"))
		{
			LOTRLevelData.middleEarthPortalX = data.readInt();
			LOTRLevelData.middleEarthPortalY = data.readInt();
			LOTRLevelData.middleEarthPortalZ = data.readInt();
		}
		
		else if (channel.equals("lotr.alignment"))
		{
			UUID player = new UUID(data.readLong(), data.readLong());
			
			for (LOTRFaction faction : LOTRFaction.values())
			{
				int alignment = data.readInt();
				LOTRLevelData.setClientAlignment(player, alignment, faction);
			}
			
			boolean hide = data.readBoolean();
			LOTRLevelData.setClientHideAlignment(player, hide);
		}
		
		else if (channel.equals("lotr.alignBonus"))
		{
			int bonus = data.readInt();
			if (bonus == 0)
			{
				return;
			}
			
			LOTRTickHandlerClient.alignmentChange = 30;
			double posX = data.readDouble();
			double posY = data.readDouble();
			double posZ = data.readDouble();
			LOTRFaction faction = LOTRFaction.forID(data.readByte());
			boolean needsTranslation = data.readBoolean();
			boolean isKill = data.readBoolean();
			
			int length = data.readShort();
			String name = data.toString(data.readerIndex(), length, Charsets.UTF_8);
			if (needsTranslation)
			{
				name = StatCollector.translateToLocal(name);
			}
			
			LOTREntityAlignmentBonus entity = new LOTREntityAlignmentBonus(world, posX, posY, posZ, name, bonus, faction, isKill);
			world.spawnEntityInWorld(entity);
		}
		
		else if (channel.equals("lotr.achieve"))
		{
			LOTRAchievement.Category c = LOTRAchievement.Category.values()[data.readByte()];
			LOTRAchievement achievement = LOTRAchievement.achievementForCategoryAndID(c, data.readByte());
			if (achievement != null)
			{
				LOTRLevelData.addClientAchievement(entityplayer, achievement);
				LOTRTickHandlerClient.achievementDisplay.queueAchievement(achievement);
			}
		}
		
		else if (channel.equals("lotr.mace"))
		{
			Entity entity = world.getEntityByID(data.readInt());
			if (entity != null)
			{
				for (int i = 0; i < 360; i += 2)
				{
					float angle = (float)Math.toRadians(i);
					double distance = 1.5D;
					double d = distance * MathHelper.sin(angle);
					double d1 = distance * MathHelper.cos(angle);
					world.spawnParticle("smoke", entity.posX + d, entity.boundingBox.minY + 0.1D, entity.posZ + d1, d * 0.2D, 0D, d1 * 0.2D);
				}
			}
		}
		
		else if (channel.equals("lotr.staffWhite"))
		{
			Entity entity = world.getEntityByID(data.readInt());
			if (entity != null)
			{
				for (int i = 0; i < 360; i += 2)
				{
					float angle = (float)Math.toRadians(i);
					double distance = 1.5D;
					double d = distance * MathHelper.sin(angle);
					double d1 = distance * MathHelper.cos(angle);
					LOTRMod.proxy.spawnParticle("blueFlame", entity.posX + d, entity.boundingBox.minY + 0.1D, entity.posZ + d1, d * 0.2D, 0D, d1 * 0.2D);
				}
			}
		}
		
		else if (channel.equals("lotr.fireball"))
		{
			double d = data.readDouble();
			double d1 = data.readDouble();
			double d2 = data.readDouble();
			mc.effectRenderer.addEffect(new LOTREntityGandalfFireballExplodeFX(world, d, d1, d2));
		}
		
		else if (channel.equals("lotr.trades"))
		{
			GuiScreen gui = mc.currentScreen;
			if (gui instanceof LOTRGuiTrade)
			{
				LOTRGuiTrade tradeGui = (LOTRGuiTrade)gui;
				tradeGui.theEntity.traderNPCInfo.receiveClientPacket((S3FPacketCustomPayload)packet.toS3FPacket());
			}
		}
		
		else if (channel.equals("lotr.updateCape"))
		{
			UUID player = new UUID(data.readLong(), data.readLong());
			
			int capeID = data.readByte();
			int capeTypeID = data.readByte();
			if (capeTypeID < 0 || capeTypeID >= CapeType.values().length)
			{
				System.out.println("Failed to update LOTR cape on client side: There is no capetype with ID " + capeTypeID);
				return;
			}
			CapeType capeType = CapeType.values()[capeTypeID];
			if (capeID < 0 || capeID >= capeType.list.size())
			{
				System.out.println("Failed to update LOTR cape on client side: There is no cape with ID " + capeID + " for capetype " + capeTypeID);
				return;
			}
			LOTRCapes cape = (LOTRCapes)capeType.list.get(capeID);
			
			boolean enable = data.readBoolean();
			
			LOTRLevelData.setClientCape(player, cape);
			LOTRLevelData.setClientEnableCape(player, enable);
		}
		
		else if (channel.equals("lotr.findMelon"))
		{
			LOTRLevelData.setPlayerFoundMelon(entityplayer);
		}
		
		else if (channel.equals("lotr.options"))
		{
			int option = data.readByte();
			boolean flag = data.readBoolean();
			
			if (option == LOTROptions.FRIENDLY_FIRE)
			{
				LOTRLevelData.setFriendlyFire(entityplayer, flag);
			}
			else if (option == LOTROptions.HIRED_DEATH_MESSAGES)
			{
				LOTRLevelData.setEnableHiredDeathMessages(entityplayer, flag);
			}
			else if (option == LOTROptions.SHOW_MAP_LOCATION)
			{
				LOTRLevelData.setShowMapLocation(entityplayer, flag);
			}
		}
		
		else if (channel.equals("lotr.message"))
		{
			int messageID = data.readByte();
			if (messageID < 0 || messageID >= LOTRGuiMessageTypes.values().length)
			{
				System.out.println("Failed to display LOTR message: There is no message with ID " + messageID);
				return;
			}
			LOTRGuiMessageTypes type = LOTRGuiMessageTypes.values()[messageID];
			mc.displayGuiScreen(new LOTRGuiMessage(type));
		}
		
		else if (channel.equals("lotr.hiredGui"))
		{
			Entity entity = world.getEntityByID(data.readInt());
			if (entity instanceof LOTREntityNPC && ((LOTREntityNPC)entity).hiredNPCInfo.getHiringPlayer() == entityplayer)
			{
				LOTREntityNPC npc = (LOTREntityNPC)entity;
				boolean openGui = data.readBoolean();
				npc.hiredNPCInfo.receiveClientPacket(data);
				if (openGui)
				{
					if (npc.hiredNPCInfo.getTask() == Task.WARRIOR)
					{
						mc.displayGuiScreen(new LOTRGuiHiredWarrior(npc));
					}
					else if (npc.hiredNPCInfo.getTask() == Task.FARMER)
					{
						mc.displayGuiScreen(new LOTRGuiHiredFarmer(npc));
					}
				}
			}
		}
		
		else if (channel.equals("lotr.isOp"))
		{
			GuiScreen gui = mc.currentScreen;
			if (gui instanceof LOTRGuiMap)
			{
				LOTRGuiMap map = (LOTRGuiMap)gui;
				map.isPlayerOp = data.readBoolean();
			}
		}
		
		else if (channel.equals("lotr.WP"))
		{
			LOTRWaypoint.Region region = LOTRWaypoint.regionForID(data.readByte());
			if (region != null)
			{
				region.unlockForPlayer(entityplayer);
			}
		}
		
		else if (channel.equals("lotr.loginWP"))
		{
			for (int i = 0; i < data.array().length; i++)
			{
				LOTRWaypoint.Region region = LOTRWaypoint.regionForID(data.readByte());
				if (region != null)
				{
					region.unlockForPlayer(entityplayer);
				}
			}
		}
		
		else if (channel.equals("lotr.ftTimer"))
		{
			LOTRLevelData.setFastTravelTimer(entityplayer, data.readInt());
		}
		
		else if (channel.equals("lotr.ftGui"))
		{
			boolean isCustom = data.readBoolean();
			int waypointID = data.readInt();
			LOTRAbstractWaypoint waypoint = null;
			if (!isCustom && waypointID >= 0 && waypointID < LOTRWaypoint.values().length)
			{
				waypoint = LOTRWaypoint.values()[waypointID];
			}
			else if (isCustom)
			{
				waypoint = LOTRWaypoint.Custom.waypointForPlayerForID(entityplayer, waypointID);
			}
			
			if (waypoint != null)
			{
				mc.displayGuiScreen(new LOTRGuiFastTravel(waypoint));
			}
		}
		
		else if (channel.equals("lotr.frost"))
		{
			theProxy.tickHandler.onFrostDamage();
		}
		
		else if (channel.equals("lotr.burn"))
		{
			theProxy.tickHandler.onBurnDamage();
		}
		
		else if (channel.equals("lotr.showPos"))
		{
			LOTRLevelData.setShowMapLocation(entityplayer, data.readBoolean());
		}
		
		else if (channel.equals("lotr.clearMap"))
		{
			LOTRGuiMap.playerLocations.clear();
		}
		
		else if (channel.equals("lotr.playerPos"))
		{
			UUID player = new UUID(data.readLong(), data.readLong());
			
			double posX = data.readDouble();
			double posZ = data.readDouble();
			
			int length = data.readShort();
			String name = data.toString(data.readerIndex(), length, Charsets.UTF_8);

			LOTRGuiMap.addPlayerLocationInfo(player, name, posX, posZ);
		}
		
		else if (channel.equals("lotr.CWP"))
		{
			if (!mc.isSingleplayer())
			{
				int x = data.readInt();
				int y = data.readInt();
				int xCoord = data.readInt();
				int zCoord = data.readInt();
				int ID = data.readInt();
				int length = data.readShort();
				String name = data.toString(data.readerIndex(), length, Charsets.UTF_8);
	
				LOTRWaypoint.Custom.addCustomWaypoint(entityplayer, new LOTRWaypoint.Custom(name, x, y, xCoord, zCoord, ID));
			}
		}
		
		else if (channel.equals("lotr.clearCWP"))
		{
			if (!mc.isSingleplayer())
			{
				LOTRWaypoint.Custom.clearCustomWaypoints(entityplayer);
			}
		}
		
		else if (channel.equals("lotr.removeCWP"))
		{
			if (!mc.isSingleplayer())
			{
				int waypointID = data.readInt();
				List waypoints = LOTRWaypoint.Custom.getWaypointList(entityplayer);
				for (int i = 0; i < waypoints.size(); i++)
				{
					LOTRWaypoint.Custom waypoint = (LOTRWaypoint.Custom)waypoints.get(i);
					if (waypoint.getID() == waypointID)
					{
						LOTRWaypoint.Custom.removeCustomWaypoint(entityplayer, waypoint);
						break;
					}
				}
			}
		}
		
		else if (channel.equals("lotr.rewardItem"))
		{
			LOTRFaction faction = LOTRFaction.forID(data.readByte());
			boolean flag = data.readBoolean();
			LOTRLevelData.setTakenAlignmentRewardItem(entityplayer, faction, flag);
		}
		
		else if (channel.equals("lotr.hearts"))
		{
			Entity entity = world.getEntityByID(data.readInt());
			if (entity instanceof LOTREntityNPC)
			{
				((LOTREntityNPC)entity).spawnHearts();
			}
		}
	}
}
