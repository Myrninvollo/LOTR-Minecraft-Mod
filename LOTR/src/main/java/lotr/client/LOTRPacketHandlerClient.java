package lotr.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;

import java.util.List;
import java.util.UUID;

import lotr.client.fx.LOTREntityAlignmentBonus;
import lotr.client.fx.LOTREntityGandalfFireballExplodeFX;
import lotr.client.gui.*;
import lotr.common.*;
import lotr.common.LOTRShields.ShieldType;
import lotr.common.entity.item.LOTREntityBanner;
import lotr.common.entity.npc.LOTREntityNPC;
import lotr.common.entity.npc.LOTRHiredNPCInfo.Task;
import lotr.common.quest.LOTRMiniQuest;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.S3FPacketCustomPayload;
import net.minecraft.util.*;
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
		NetworkRegistry.INSTANCE.newChannel("lotr.loginPD", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.promptAch", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.promptAl", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.updateShld", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.portalPos", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.alignment", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.alignBonus", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.achieve", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.mace", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.staffWhite", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.fireball", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.trades", this);
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
		NetworkRegistry.INSTANCE.newChannel("lotr.clearMap", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.playerPos", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.CWP", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.clearCWP", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.removeCWP", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.rewardItem", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.hearts", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.eatFood", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.bannerGui", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.npcUUID", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.smokes", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.miniquest", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.mqOffer", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.mqRemove", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.time", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.title", this);
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
			if (!mc.isSingleplayer())
			{
				LOTRLevelData.clearAllPlayerData();
				LOTRWaypoint.clearAllWaypoints();
				LOTRGuiMap.playerLocations.clear();
			}
			
			LOTRLevelData.middleEarthPortalX = data.readInt();
			LOTRLevelData.middleEarthPortalY = data.readInt();
			LOTRLevelData.middleEarthPortalZ = data.readInt();
			LOTRLevelData.beaconState = data.readByte();
		}
		
		else if (channel.equals("lotr.loginPD"))
		{
			NBTTagCompound nbt = new PacketBuffer(data).readNBTTagCompoundFromBuffer();
			LOTRLevelData.getData(entityplayer).load(nbt);
			
			LOTRGuiMap.WaypointMode.setWaypointMode(LOTRLevelData.getData(entityplayer).getWaypointToggleMode());
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
			if (!mc.isSingleplayer())
			{
				UUID player = new UUID(data.readLong(), data.readLong());
	
				for (LOTRFaction faction : LOTRFaction.values())
				{
					int alignment = data.readInt();
					LOTRLevelData.getData(player).setAlignment(faction, alignment);
				}
				
				boolean hide = data.readBoolean();
				LOTRLevelData.getData(player).setHideAlignment(hide);
			}
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
			boolean display = data.readBoolean();
			
			if (achievement != null)
			{
				if (!mc.isSingleplayer())
				{
					LOTRLevelData.getData(entityplayer).addAchievement(achievement);
				}
				
				if (display)
				{
					LOTRTickHandlerClient.achievementDisplay.queueAchievement(achievement);
				}
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
		
		else if (channel.equals("lotr.updateShld"))
		{
			UUID player = new UUID(data.readLong(), data.readLong());
			
			int shieldID = data.readByte();
			int shieldTypeID = data.readByte();
			if (shieldTypeID < 0 || shieldTypeID >= ShieldType.values().length)
			{
				System.out.println("Failed to update LOTR shield on client side: There is no shieldtype with ID " + shieldTypeID);
				return;
			}
			ShieldType shieldType = ShieldType.values()[shieldTypeID];
			if (shieldID < 0 || shieldID >= shieldType.list.size())
			{
				System.out.println("Failed to update LOTR shield on client side: There is no shield with ID " + shieldID + " for shieldtype " + shieldTypeID);
				return;
			}
			LOTRShields shield = (LOTRShields)shieldType.list.get(shieldID);
			
			boolean enable = data.readBoolean();
			
			LOTRLevelData.getData(player).setShield(shield);
			LOTRLevelData.getData(player).setEnableShield(enable);
		}
		
		else if (channel.equals("lotr.options"))
		{
			if (!mc.isSingleplayer())
			{
				int option = data.readByte();
				boolean flag = data.readBoolean();
				
				if (option == LOTROptions.FRIENDLY_FIRE)
				{
					LOTRLevelData.getData(entityplayer).setFriendlyFire(flag);
				}
				else if (option == LOTROptions.HIRED_DEATH_MESSAGES)
				{
					LOTRLevelData.getData(entityplayer).setEnableHiredDeathMessages(flag);
				}
				else if (option == LOTROptions.SHOW_MAP_LOCATION)
				{
					LOTRLevelData.getData(entityplayer).setHideMapLocation(flag);
				}
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
			int length = data.readByte();
			for (int i = 0; i < length; i++)
			{
				int ID = data.readByte();
				LOTRWaypoint.Region region = LOTRWaypoint.regionForID(ID);
				if (region != null)
				{
					region.unlockForPlayer(entityplayer);
				}
			}
		}
		
		else if (channel.equals("lotr.ftTimer"))
		{
			LOTRLevelData.getData(entityplayer).setFTTimer(data.readInt());
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
			LOTRLevelData.getData(entityplayer).setTakenAlignmentReward(faction, flag);
		}
		
		else if (channel.equals("lotr.hearts"))
		{
			Entity entity = world.getEntityByID(data.readInt());
			if (entity instanceof LOTREntityNPC)
			{
				((LOTREntityNPC)entity).spawnHearts();
			}
		}
		
		else if (channel.equals("lotr.eatFood"))
		{
			Entity entity = world.getEntityByID(data.readInt());
			if (entity instanceof LOTREntityNPC)
			{
				((LOTREntityNPC)entity).spawnFoodParticles();
			}
		}
		
		else if (channel.equals("lotr.bannerGui"))
		{
			Entity entity = world.getEntityByID(data.readInt());
			if (entity instanceof LOTREntityBanner)
			{
				LOTREntityBanner banner = (LOTREntityBanner)entity;
				banner.playerSpecificProtection = data.readBoolean();
				banner.setAlignmentProtection(data.readInt());
				
				LOTRGuiBanner gui = new LOTRGuiBanner(banner);
				
				int index = 0;
				while ((index = data.readInt()) >= 0)
				{
					int length = data.readByte();
					String name = data.readBytes(length).toString(Charsets.UTF_8);
					gui.usernamesReceived[index] = name;
				}
				
				mc.displayGuiScreen(gui);
			}
		}
		
		else if (channel.equals("lotr.npcUUID"))
		{
			Entity entity = world.getEntityByID(data.readInt());
			if (entity instanceof LOTREntityNPC)
			{
				LOTREntityNPC npc = (LOTREntityNPC)entity;
				UUID uuid = new UUID(data.readLong(), data.readLong());
				npc.setUniqueID(uuid);
			}
		}
		
		else if (channel.equals("lotr.smokes"))
		{
			Entity entity = world.getEntityByID(data.readInt());
			if (entity instanceof LOTREntityNPC)
			{
				((LOTREntityNPC)entity).spawnSmokes();
			}
		}
		
		else if (channel.equals("lotr.miniquest"))
		{
			if (!mc.isSingleplayer())
			{
				LOTRPlayerData playerData = LOTRLevelData.getData(entityplayer);
				NBTTagCompound nbt = new PacketBuffer(data).readNBTTagCompoundFromBuffer();
				LOTRMiniQuest miniquest = LOTRMiniQuest.loadQuestFromNBT(nbt, playerData);
				if (miniquest != null)
				{
					LOTRMiniQuest existingQuest = null;
					for (LOTRMiniQuest quest : playerData.getMiniQuests())
					{
						if (quest.entityUUID.equals(miniquest.entityUUID))
						{
							existingQuest = quest;
							break;
						}
					}
					
					if (existingQuest == null)
					{
						playerData.addMiniQuest(miniquest);
					}
					else
					{
						existingQuest.readFromNBT(nbt);
					}
				}
			}
		}
		
		else if (channel.equals("lotr.mqOffer"))
		{
			LOTRPlayerData playerData = LOTRLevelData.getData(entityplayer);
			
			int entityId = data.readInt();
			LOTREntityNPC npc = (LOTREntityNPC)world.getEntityByID(entityId);

			NBTTagCompound nbt = new PacketBuffer(data).readNBTTagCompoundFromBuffer();
			LOTRMiniQuest quest = LOTRMiniQuest.loadQuestFromNBT(nbt, playerData);
			if (quest != null)
			{
				mc.displayGuiScreen(new LOTRGuiMiniquestOffer(quest, npc));
			}
			else
			{
				LOTRGuiMiniquestOffer.sendClosePacket(null, npc, false);
			}
		}
		
		else if (channel.equals("lotr.mqRemove"))
		{
			if (!mc.isSingleplayer())
			{
				LOTRPlayerData playerData = LOTRLevelData.getData(entityplayer);
				
				UUID questUUID = new UUID(data.readLong(), data.readLong());
				boolean addToCompleted = data.readBoolean();
				LOTRMiniQuest removeQuest = null;
				
				for (LOTRMiniQuest quest : playerData.getMiniQuests())
				{
					if (quest.entityUUID.equals(questUUID))
					{
						removeQuest = quest;
						break;
					}
				}
				
				playerData.removeMiniQuest(removeQuest, addToCompleted);
			}
		}
		
		else if (channel.equals("lotr.time"))
		{
			boolean update = data.readBoolean();
			
			NBTTagCompound nbt = new PacketBuffer(data).readNBTTagCompoundFromBuffer();
			LOTRTime.loadDates(nbt);

			if (update)
			{
				LOTRClientProxy.tickHandler.updateDate();
			}
		}
		
		else if (channel.equals("lotr.title"))
		{
			int titleID = data.readInt();
			int colorCode = data.readInt();
			
			LOTRTitle title = LOTRTitle.forID(titleID);
			if (title == null)
			{
				LOTRLevelData.getData(entityplayer).setPlayerTitle(null);
			}
			else
			{
				EnumChatFormatting color = LOTRTitle.PlayerTitle.colorForID(colorCode);
				LOTRLevelData.getData(entityplayer).setPlayerTitle(new LOTRTitle.PlayerTitle(title, color));
			}
		}
	}
}
