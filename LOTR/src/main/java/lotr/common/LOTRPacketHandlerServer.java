package lotr.common;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.*;

import java.util.*;

import lotr.common.LOTRShields.ShieldType;
import lotr.common.entity.LOTRMountFunctions;
import lotr.common.entity.item.LOTREntityBanner;
import lotr.common.entity.npc.*;
import lotr.common.entity.npc.LOTRHiredNPCInfo.Task;
import lotr.common.inventory.*;
import lotr.common.quest.LOTRMiniQuest;
import lotr.common.tileentity.LOTRTileEntityMobSpawner;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.S3FPacketCustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import com.google.common.base.Charsets;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;

@Sharable
public class LOTRPacketHandlerServer extends SimpleChannelInboundHandler<FMLProxyPacket>
{
	public LOTRPacketHandlerServer()
	{
		NetworkRegistry.INSTANCE.newChannel("lotr.sell", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.mobSpawner", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.buyUnit", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.hornSelect", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.checkMenu", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.checkAl", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.brewing", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.selectShld", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.tInteract", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.utInteract", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.hInteract", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.setOption", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.namePouch", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.commandGui", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.mapTp", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.isOpReq", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.fastTravel", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.hDismiss", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.clientInfo", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.createCWP", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.deleteCWP", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.mountInv", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.editBanner", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.mqAccept", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.mqDelete", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.titleSelect", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.editBannerAlignment", this);
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FMLProxyPacket packet) throws Exception
	{
		ByteBuf data = packet.payload();
		String channel = packet.channel();

		if (channel.equals("lotr.sell"))
		{
			int id = data.readInt();
			World world = DimensionManager.getWorld(data.readByte());
			if (world != null)
			{
				Entity entity = world.getEntityByID(id);
				if (entity instanceof EntityPlayer)
				{
					EntityPlayer entityplayer = (EntityPlayer)entity;
					Container container = entityplayer.openContainer;
					if (container != null && container instanceof LOTRContainerTrade)
					{
						int totalCoins = 0;
						for (int i = 9; i < 18; i++)
						{
							Slot slot = (Slot)container.inventorySlots.get(i);
							ItemStack itemstack = slot.getStack();
							if (itemstack != null)
							{
								int[] ints = LOTRTradeEntry.getSellPrice_TradeCount_SellAmount(itemstack, ((LOTRContainerTrade)container).theEntity);
								int sellPrice = ints[0];
								int tradeCount = ints[1];
								int sellAmount = ints[2];
								if (sellPrice > 0)
								{
									totalCoins += sellPrice * tradeCount;
									((LOTRContainerTrade)container).theTrader.onPlayerSellItem(entityplayer, itemstack);
									
									itemstack.stackSize -= sellAmount;
									if (itemstack.stackSize <= 0)
									{
										slot.putStack(null);
									}
								}
							}
						}
						
						if (totalCoins >= 1000)
						{
							LOTRLevelData.getData(entityplayer).addAchievement(LOTRAchievement.earnManyCoins);
						}
						
						for (int i = 0; i < totalCoins; i++)
						{
							if (!entityplayer.inventory.addItemStackToInventory(new ItemStack(LOTRMod.silverCoin)))
							{
								int j = totalCoins - i;
								while (j > 64)
								{
									j -= 64;
									entityplayer.dropPlayerItemWithRandomChoice(new ItemStack(LOTRMod.silverCoin, 64), false);
								}
								entityplayer.dropPlayerItemWithRandomChoice(new ItemStack(LOTRMod.silverCoin, j), false);
								break;
							}
						}
					}
				}
			}
		}
		
		else if (channel.equals("lotr.mobSpawner"))
		{
			int i = data.readInt();
			int j = data.readInt();
			int k = data.readInt();
			World world = DimensionManager.getWorld(data.readByte());
			if (world != null)
			{
				TileEntity tileentity = world.getTileEntity(i, j, k);
				if (tileentity instanceof LOTRTileEntityMobSpawner)
				{
					LOTRTileEntityMobSpawner mobSpawner = ((LOTRTileEntityMobSpawner)tileentity);
					mobSpawner.active = data.readByte();
					mobSpawner.spawnsPersistentNPCs = data.readBoolean();
					mobSpawner.minSpawnDelay = data.readInt();
					mobSpawner.maxSpawnDelay = data.readInt();
					mobSpawner.nearbyMobLimit = data.readByte();
					mobSpawner.nearbyMobCheckRange = data.readByte();
					mobSpawner.requiredPlayerRange = data.readByte();
					mobSpawner.maxSpawnCount = data.readByte();
					mobSpawner.maxSpawnRange = data.readByte();
					mobSpawner.maxSpawnRangeVertical = data.readByte();
					mobSpawner.maxHealth = ((int)data.readByte() + 256) & 255;
					mobSpawner.navigatorRange = data.readByte();
					mobSpawner.moveSpeed = data.readFloat();
					mobSpawner.attackDamage = data.readByte();
					world.markBlockForUpdate(i, j, k);
					mobSpawner.delay = -1;
					world.addBlockEvent(i, j, k, LOTRMod.mobSpawner, 2, 0);
				}
			}
		}
		
		else if (channel.equals("lotr.buyUnit"))
		{
			int id = data.readInt();
			World world = DimensionManager.getWorld(data.readByte());
			if (world != null)
			{
				Entity entity = world.getEntityByID(id);
				if (entity instanceof EntityPlayer)
				{
					EntityPlayer entityplayer = (EntityPlayer)entity;
					Container container = entityplayer.openContainer;
					if (container != null && container instanceof LOTRContainerUnitTrade)
					{
						LOTRUnitTradeable unitTrader = ((LOTRContainerUnitTrade)container).theUnitTrader;
						int tradeIndex = data.readByte();
						LOTRUnitTradeEntry trade = ((LOTRContainerUnitTrade)container).theUnitTrader.getUnits()[tradeIndex];
						if (trade.hasRequiredCostAndAlignment(entityplayer, unitTrader))
						{
							unitTrader.onUnitTrade(entityplayer);
							int cost = trade.getCost(entityplayer, unitTrader);
							for (int i = 0; i < cost; i++)
							{
								entityplayer.inventory.consumeInventoryItem(LOTRMod.silverCoin);
							}
							
							EntityLiving hiredEntity = trade.createHiredNPC(world);
							if (hiredEntity != null)
							{
								Task task = trade.task;
								
								EntityLiving hiredMount = null;
								if (trade.mountClass != null)
								{
									hiredMount = trade.createHiredMount(world);
								}
								
								if (hiredMount != null)
								{
									hiredMount.setLocationAndAngles(entityplayer.posX, entityplayer.boundingBox.minY, entityplayer.posZ, world.rand.nextFloat() * 360F, 0F);
									if (hiredMount instanceof LOTREntityNPC)
									{
										LOTREntityNPC hiredMountNPC = (LOTREntityNPC)hiredMount;
										hiredMountNPC.hiredNPCInfo.isActive = true;
										hiredMountNPC.hiredNPCInfo.alignmentRequiredToCommand = trade.alignmentRequired;
										hiredMountNPC.hiredNPCInfo.setHiringPlayerUUID(entityplayer.getUniqueID().toString());
										hiredMountNPC.hiredNPCInfo.setTask(task);
									}
									world.spawnEntityInWorld(hiredMount);
								}
								
								LOTREntityNPC hiredNPC = (LOTREntityNPC)hiredEntity;
								hiredNPC.setLocationAndAngles(entityplayer.posX, entityplayer.boundingBox.minY, entityplayer.posZ, world.rand.nextFloat() * 360F, 0F);
								hiredNPC.hiredNPCInfo.isActive = true;
								hiredNPC.hiredNPCInfo.alignmentRequiredToCommand = trade.alignmentRequired;
								hiredNPC.hiredNPCInfo.setHiringPlayerUUID(entityplayer.getUniqueID().toString());
								hiredNPC.hiredNPCInfo.setTask(task);
								world.spawnEntityInWorld(hiredNPC);
								
								if (hiredMount != null)
								{
									hiredNPC.mountEntity(hiredMount);
									if (hiredMount instanceof LOTRNPCMount && !(hiredMount instanceof LOTREntityNPC))
									{
										hiredNPC.setRidingHorse(true);
										LOTRNPCMount hiredHorse = (LOTRNPCMount)hiredMount;
										hiredHorse.setBelongsToNPC(true);
										LOTRMountFunctions.setNavigatorRangeFromNPC(hiredHorse, hiredNPC);
									}
								}
								
								if (task == Task.FARMER)
								{
									LOTRLevelData.getData(entityplayer).addAchievement(LOTRAchievement.hireFarmer);
								}
							}
						}
					}
				}
			}
		}
		
		else if (channel.equals("lotr.hornSelect"))
		{
			int id = data.readInt();
			World world = DimensionManager.getWorld(data.readByte());
			if (world != null)
			{
				Entity entity = world.getEntityByID(id);
				if (entity instanceof EntityPlayer)
				{
					EntityPlayer entityplayer = (EntityPlayer)entity;
					ItemStack itemstack = entityplayer.inventory.getCurrentItem();
					if (itemstack != null && itemstack.getItem() == LOTRMod.commandHorn && itemstack.getItemDamage() == 0)
					{
						itemstack.setItemDamage(data.readByte());
					}
				}
			}
		}
		
		else if (channel.equals("lotr.checkMenu"))
		{
			int id = data.readInt();
			World world = DimensionManager.getWorld(data.readByte());
			if (world != null)
			{
				Entity entity = world.getEntityByID(id);
				if (entity instanceof EntityPlayer)
				{
					EntityPlayer entityplayer = (EntityPlayer)entity;
					LOTRLevelData.getData(entityplayer).setCheckedMenu(true);
				}
			}
		}
		
		else if (channel.equals("lotr.checkAl"))
		{
			int id = data.readInt();
			World world = DimensionManager.getWorld(data.readByte());
			if (world != null)
			{
				Entity entity = world.getEntityByID(id);
				if (entity instanceof EntityPlayer)
				{
					EntityPlayer entityplayer = (EntityPlayer)entity;
					LOTRLevelData.getData(entityplayer).setCheckedAlignments(true);
				}
			}
		}
		
		else if (channel.equals("lotr.brewing"))
		{
			int id = data.readInt();
			World world = DimensionManager.getWorld(data.readByte());
			if (world != null)
			{
				Entity entity = world.getEntityByID(id);
				if (entity instanceof EntityPlayer)
				{
					EntityPlayer entityplayer = (EntityPlayer)entity;
					Container container = entityplayer.openContainer;
					if (container != null && container instanceof LOTRContainerBarrel)
					{
						((LOTRContainerBarrel)container).theBarrel.handleBrewingButtonPress();
						LOTRLevelData.getData(entityplayer).addAchievement(LOTRAchievement.brewDrinkInBarrel);
					}
				}
			}
		}
		
		else if (channel.equals("lotr.selectShld"))
		{
			int id = data.readInt();
			World world = DimensionManager.getWorld(data.readByte());
			if (world != null)
			{
				Entity entity = world.getEntityByID(id);
				if (entity instanceof EntityPlayer)
				{
					EntityPlayer entityplayer = (EntityPlayer)entity;
					int shieldID = data.readByte();
					int shieldTypeID = data.readByte();
					if (shieldTypeID < 0 || shieldTypeID >= ShieldType.values().length)
					{
						System.out.println("Failed to update LOTR shield on server side: There is no shieldtype with ID " + shieldTypeID);
						return;
					}
					ShieldType shieldType = ShieldType.values()[shieldTypeID];
					if (shieldID < 0 || shieldID >= shieldType.list.size())
					{
						System.out.println("Failed to update LOTR shield on server side: There is no shield with ID " + shieldID + " for shieldtype " + shieldTypeID);
						return;
					}
					LOTRShields shield = (LOTRShields)shieldType.list.get(shieldID);
					if (shield.canPlayerWear(entityplayer))
					{
						LOTRLevelData.getData(entityplayer).setShield(shield);
						LOTRLevelData.sendShieldToAllPlayersInWorld(entityplayer, world);
					}
					else
					{
						System.out.println("Failed to update LOTR shield on server side: Player " + entityplayer.getCommandSenderName() + " cannot wear shield " + shield.name());
					}
				}
			}
		}
		
		else if (channel.equals("lotr.tInteract"))
		{
			int id = data.readInt();
			World world = DimensionManager.getWorld(data.readByte());
			if (world != null)
			{
				Entity entity = world.getEntityByID(id);
				if (entity instanceof EntityPlayer)
				{
					EntityPlayer entityplayer = (EntityPlayer)entity;
					int entityId = data.readInt();
					Entity trader = world.getEntityByID(entityId);
					if (trader != null && trader instanceof LOTRTradeable)
					{
						LOTRTradeable tradeableTrader = (LOTRTradeable)trader;
						LOTREntityNPC livingTrader = (LOTREntityNPC)trader;
						int action = data.readByte();
						boolean flag = false;
						
						if (action == 0)
						{
							livingTrader.npcTalkTick = livingTrader.getNPCTalkInterval();
							flag = livingTrader.interactFirst(entityplayer);
						}
						else if (action == 1 && tradeableTrader.canTradeWith(entityplayer))
						{
							entityplayer.openGui(LOTRMod.instance, LOTRCommonProxy.GUI_ID_TRADE, world, livingTrader.getEntityId(), 0, 0);
						}
						
						if (flag)
						{
							((EntityPlayerMP)entityplayer).closeScreen();
						}
					}
				}
			}
		}
		
		else if (channel.equals("lotr.utInteract"))
		{
			int id = data.readInt();
			World world = DimensionManager.getWorld(data.readByte());
			if (world != null)
			{
				Entity entity = world.getEntityByID(id);
				if (entity instanceof EntityPlayer)
				{
					EntityPlayer entityplayer = (EntityPlayer)entity;
					int entityId = data.readInt();
					Entity trader = world.getEntityByID(entityId);
					if (trader != null && trader instanceof LOTRUnitTradeable)
					{
						LOTRUnitTradeable tradeableTrader = (LOTRUnitTradeable)trader;
						LOTREntityNPC livingTrader = (LOTREntityNPC)trader;
						int action = data.readByte();
						boolean flag = false;
						
						if (action == 0)
						{
							livingTrader.npcTalkTick = livingTrader.getNPCTalkInterval();
							flag = livingTrader.interactFirst(entityplayer);
						}
						else if (action == 1 && tradeableTrader.canTradeWith(entityplayer))
						{
							entityplayer.openGui(LOTRMod.instance, LOTRCommonProxy.GUI_ID_UNIT_TRADE, world, livingTrader.getEntityId(), 0, 0);
						}
						
						if (flag)
						{
							((EntityPlayerMP)entityplayer).closeScreen();
						}
					}
				}
			}
		}
		
		else if (channel.equals("lotr.hInteract"))
		{
			int id = data.readInt();
			World world = DimensionManager.getWorld(data.readByte());
			if (world != null)
			{
				Entity entity = world.getEntityByID(id);
				if (entity instanceof EntityPlayer)
				{
					EntityPlayer entityplayer = (EntityPlayer)entity;
					int entityId = data.readInt();
					Entity npc = world.getEntityByID(entityId);
					if (npc != null && npc instanceof LOTREntityNPC)
					{
						LOTREntityNPC hiredNPC = (LOTREntityNPC)npc;
						if (hiredNPC.hiredNPCInfo.isActive && hiredNPC.hiredNPCInfo.getHiringPlayer() == entityplayer)
						{
							int action = data.readByte();
							boolean flag = false;
							
							if (action == 0)
							{
								hiredNPC.npcTalkTick = hiredNPC.getNPCTalkInterval();
								flag = hiredNPC.interactFirst(entityplayer);
							}
							else if (action == 1)
							{
								hiredNPC.hiredNPCInfo.sendClientPacket(true);
							}
							else if (action == 2) {}
							
							if (flag)
							{
								((EntityPlayerMP)entityplayer).closeScreen();
							}
						}
					}
				}
			}
		}
		
		else if (channel.equals("lotr.setOption"))
		{
			int id = data.readInt();
			World world = DimensionManager.getWorld(data.readByte());
			if (world != null)
			{
				Entity entity = world.getEntityByID(id);
				if (entity instanceof EntityPlayer)
				{
					EntityPlayer entityplayer = (EntityPlayer)entity;
					int option = data.readByte();

					if (option == LOTROptions.FRIENDLY_FIRE)
					{
						boolean flag = LOTRLevelData.getData(entityplayer).getFriendlyFire();
						LOTRLevelData.getData(entityplayer).setFriendlyFire(!flag);
					}
					else if (option == LOTROptions.HIRED_DEATH_MESSAGES)
					{
						boolean flag = LOTRLevelData.getData(entityplayer).getEnableHiredDeathMessages();
						LOTRLevelData.getData(entityplayer).setEnableHiredDeathMessages(!flag);
					}
					else if (option == LOTROptions.ENABLE_SHIELD)
					{
						boolean flag = LOTRLevelData.getData(entityplayer).getEnableShield();
						LOTRLevelData.getData(entityplayer).setEnableShield(!flag);
						LOTRLevelData.sendShieldToAllPlayersInWorld(entityplayer, world);
					}
					else if (option == LOTROptions.SHOW_ALIGNMENT)
					{
						boolean flag = LOTRLevelData.getData(entityplayer).getHideAlignment();
						LOTRLevelData.getData(entityplayer).setHideAlignment(!flag);
					}
					else if (option == LOTROptions.SHOW_MAP_LOCATION)
					{
						boolean flag = LOTRLevelData.getData(entityplayer).getHideMapLocation();
						LOTRLevelData.getData(entityplayer).setHideMapLocation(!flag);
					}
				}
			}
		}
		
		else if (channel.equals("lotr.namePouch"))
		{
			int id = data.readInt();
			World world = DimensionManager.getWorld(data.readByte());
			if (world != null)
			{
				Entity entity = world.getEntityByID(id);
				if (entity instanceof EntityPlayer)
				{
					EntityPlayer entityplayer = (EntityPlayer)entity;
					Container container = entityplayer.openContainer;
					if (container != null && container instanceof LOTRContainerPouch)
					{
						LOTRContainerPouch pouch = (LOTRContainerPouch)container;
						int length = data.readShort();
						String name = data.toString(data.readerIndex(), length, Charsets.UTF_8);
						pouch.renamePouch(name);
					}
				}
			}
		}
		
		else if (channel.equals("lotr.commandGui"))
		{
			int id = data.readInt();
			World world = DimensionManager.getWorld(data.readByte());
			if (world != null)
			{
				Entity entity = world.getEntityByID(id);
				if (entity instanceof EntityPlayer)
				{
					EntityPlayer entityplayer = (EntityPlayer)entity;
					int entityId = data.readInt();
					Entity npc = world.getEntityByID(entityId);
					if (npc != null && npc instanceof LOTREntityNPC)
					{
						LOTREntityNPC hiredNPC = (LOTREntityNPC)npc;
						if (hiredNPC.hiredNPCInfo.isActive && hiredNPC.hiredNPCInfo.getHiringPlayer() == entityplayer)
						{
							int page = data.readByte();
							int action = data.readByte();
							int value = data.readByte();
							if (action == -1)
							{
								hiredNPC.hiredNPCInfo.isGuiOpen = false;
							}
							else
							{
								Task task = hiredNPC.hiredNPCInfo.getTask();
								if (task == Task.WARRIOR)
								{
									if (page == 1)
									{
										if (action == 0)
										{
											hiredNPC.hiredNPCInfo.obeyHornHaltReady = !hiredNPC.hiredNPCInfo.obeyHornHaltReady;
										}
										else if (action == 1)
										{
											hiredNPC.hiredNPCInfo.obeyHornSummon = !hiredNPC.hiredNPCInfo.obeyHornSummon;
										}
										else if (action == 2)
										{
											hiredNPC.hiredNPCInfo.teleportAutomatically = !hiredNPC.hiredNPCInfo.teleportAutomatically;
										}
										else if (action == 3)
										{
											hiredNPC.hiredNPCInfo.setGuardMode(!hiredNPC.hiredNPCInfo.isGuardMode());
										}
										else if (action == 4)
										{
											hiredNPC.hiredNPCInfo.setGuardRange(value);
										}
									}
								}
								else if (task == Task.FARMER)
								{
									if (action == 0)
									{
										hiredNPC.hiredNPCInfo.setGuardMode(!hiredNPC.hiredNPCInfo.isGuardMode());
									}
									else if (action == 1)
									{
										hiredNPC.hiredNPCInfo.setGuardRange(value);
									}
									else if (action == 2)
									{
										entityplayer.openGui(LOTRMod.instance, LOTRCommonProxy.GUI_ID_HIRED_FARMER_INVENTORY, world, hiredNPC.getEntityId(), 0, 0);
									}
								}
		
								hiredNPC.hiredNPCInfo.sendClientPacket(false);
							}
						}
					}
				}
			}
		}
		
		else if (channel.equals("lotr.mapTp"))
		{
			int id = data.readInt();
			World world = DimensionManager.getWorld(data.readByte());
			if (world != null)
			{
				Entity entity = world.getEntityByID(id);
				if (entity instanceof EntityPlayer)
				{
					EntityPlayer entityplayer = (EntityPlayer)entity;
					int i = data.readInt();
					int k = data.readInt();
					int j = world.getTopSolidOrLiquidBlock(i, k);
					String command = "/tp " + i + " " + j + " " + k;
					MinecraftServer.getServer().getCommandManager().executeCommand(entityplayer, command);
				}
			}
		}
		
		else if (channel.equals("lotr.isOpReq"))
		{
			int id = data.readInt();
			World world = DimensionManager.getWorld(data.readByte());
			if (world != null)
			{
				Entity entity = world.getEntityByID(id);
				if (entity instanceof EntityPlayer)
				{
					EntityPlayer entityplayer = (EntityPlayer)entity;
					boolean isOp = MinecraftServer.getServer().getConfigurationManager().func_152596_g(entityplayer.getGameProfile());
					
					ByteBuf opData = Unpooled.buffer();
					
					opData.writeBoolean(isOp);
					
					Packet opPacket = new S3FPacketCustomPayload("lotr.isOp", opData);
					((EntityPlayerMP)entityplayer).playerNetServerHandler.sendPacket(opPacket);
				}
			}
		}
		
		else if (channel.equals("lotr.fastTravel"))
		{
			int id = data.readInt();
			World world = DimensionManager.getWorld(data.readByte());
			if (world != null)
			{
				Entity entity = world.getEntityByID(id);
				if (entity instanceof EntityPlayerMP)
				{
					EntityPlayerMP entityplayer = (EntityPlayerMP)entity;
					if (LOTRLevelData.getData(entityplayer).getFTTimer() <= 0)
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
						
						if (waypoint != null && waypoint.hasPlayerUnlocked(entityplayer))
						{
							boolean canTravel = true;
							
							if (!entityplayer.capabilities.isCreativeMode)
							{
								double range = 16D;
								List entities = world.getEntitiesWithinAABB(EntityLiving.class, entityplayer.boundingBox.expand(range, range, range));
								for (int l = 0; l < entities.size(); l++)
								{
									EntityLiving entityliving = (EntityLiving)entities.get(l);
									if (entityliving.getAttackTarget() == entityplayer)
									{
										canTravel = false;
										break;
									}
								}
							}
							
							if (!canTravel)
							{
								entityplayer.closeScreen();
								entityplayer.addChatMessage(new ChatComponentTranslation("chat.lotr.fastTravel.underAttack"));
							}
							else
							{
								List entities = world.getEntitiesWithinAABB(LOTREntityNPC.class, entityplayer.boundingBox.expand(256D, 256D, 256D));
								List hiredUnitsToTransport = new ArrayList();
								List hiredMountsToTransport = new ArrayList();
								
								for (int l = 0; l < entities.size(); l++)
								{
									LOTREntityNPC npc = (LOTREntityNPC)entities.get(l);
									if (npc.hiredNPCInfo.isActive && npc.hiredNPCInfo.getHiringPlayer() == entityplayer && npc.hiredNPCInfo.shouldFollowPlayer())
									{
										if (npc.ridingEntity instanceof EntityLiving)
										{
											hiredMountsToTransport.add(npc.ridingEntity);
										}
										else
										{
											hiredUnitsToTransport.add(npc);
										}
									}
									if (npc instanceof LOTREntityGollum && ((LOTREntityGollum)npc).getGollumOwner() == entityplayer && !((LOTREntityGollum)npc).isGollumSitting())
									{
										hiredUnitsToTransport.add(npc);
									}
								}
								
								int i = waypoint.getXCoord();
								int k = waypoint.getZCoord();
								int j = world.getTopSolidOrLiquidBlock(i, k);
								
								Entity playerMount = entityplayer.ridingEntity;
								if (playerMount != null)
								{
									entityplayer.mountEntity(null);
									playerMount.setLocationAndAngles(i + 0.5D, j, k + 0.5D, playerMount.rotationYaw, playerMount.rotationPitch);
								}
								entityplayer.setPositionAndUpdate(i + 0.5D, j, k + 0.5D);
								
								for (int l = 0; l < hiredUnitsToTransport.size(); l++)
								{
									LOTREntityNPC npc = (LOTREntityNPC)hiredUnitsToTransport.get(l);
									npc.setLocationAndAngles(i + 0.5D, j, k + 0.5D, npc.rotationYaw, npc.rotationPitch);
									npc.fallDistance = 0F;
									npc.getNavigator().clearPathEntity();
									npc.setAttackTarget(null);
								}
								
								for (int l = 0; l < hiredMountsToTransport.size(); l++)
								{
									EntityLiving mount = (EntityLiving)hiredMountsToTransport.get(l);
									mount.setLocationAndAngles(i + 0.5D, j, k + 0.5D, mount.rotationYaw, mount.rotationPitch);
									mount.fallDistance = 0F;
									mount.getNavigator().clearPathEntity();
									mount.setAttackTarget(null);
									
									if (mount.riddenByEntity instanceof LOTREntityNPC)
									{
										LOTREntityNPC npc = (LOTREntityNPC)mount.riddenByEntity;
										npc.fallDistance = 0F;
										npc.getNavigator().clearPathEntity();
										npc.setAttackTarget(null);
									}
								}
								
								if (!entityplayer.capabilities.isCreativeMode)
								{
									LOTRLevelData.getData(entityplayer).setFTTimer(LOTRLevelData.fastTravelCooldown);
								}
								
								ByteBuf data1 = Unpooled.buffer();
								
								data1.writeBoolean(isCustom);
								data1.writeInt(waypointID);
								
								entityplayer.playerNetServerHandler.sendPacket(new S3FPacketCustomPayload("lotr.ftGui", data1));
							}
						}
					}
				}
			}
		}
		
		else if (channel.equals("lotr.hDismiss"))
		{
			int id = data.readInt();
			World world = DimensionManager.getWorld(data.readByte());
			if (world != null)
			{
				Entity entity = world.getEntityByID(id);
				if (entity instanceof EntityPlayer)
				{
					EntityPlayer entityplayer = (EntityPlayer)entity;
					int entityId = data.readInt();
					Entity npc = world.getEntityByID(entityId);
					if (npc != null && npc instanceof LOTREntityNPC)
					{
						LOTREntityNPC hiredNPC = (LOTREntityNPC)npc;
						if (hiredNPC.hiredNPCInfo.isActive && hiredNPC.hiredNPCInfo.getHiringPlayer() == entityplayer)
						{
							int action = data.readByte();
							boolean flag = false;
							
							if (action == 0)
							{
								hiredNPC.hiredNPCInfo.dismissUnit();
								flag = true;
							}
							else if (action == 1) {}
							
							if (flag)
							{
								((EntityPlayerMP)entityplayer).closeScreen();
							}
						}
					}
				}
			}
		}
		
		else if (channel.equals("lotr.clientInfo"))
		{
			int id = data.readInt();
			World world = DimensionManager.getWorld(data.readByte());
			if (world != null)
			{
				Entity entity = world.getEntityByID(id);
				if (entity instanceof EntityPlayer)
				{
					EntityPlayer entityplayer = (EntityPlayer)entity;
					
					int factionID = data.readByte();
					LOTRFaction faction = LOTRFaction.forID(factionID);
					if (faction != null)
					{
						LOTRLevelData.getData(entityplayer).setViewingFaction(faction);
					}
					
					int waypointToggleMode = data.readByte();
				}
			}
		}
		
		else if (channel.equals("lotr.createCWP"))
		{
			int id = data.readInt();
			World world = DimensionManager.getWorld(data.readByte());
			if (world != null && world.provider.dimensionId == LOTRDimension.MIDDLE_EARTH.dimensionID)
			{
				Entity entity = world.getEntityByID(id);
				if (entity instanceof EntityPlayer)
				{
					EntityPlayer entityplayer = (EntityPlayer)entity;
					int stringLength = data.readShort();
					String waypointName = data.toString(data.readerIndex(), stringLength, Charsets.UTF_8);
					
					int waypoints = LOTRWaypoint.Custom.getWaypointList(entityplayer).size();
					if (waypoints <= LOTRWaypoint.Custom.getMaxAvailableToPlayer(entityplayer))
					{
						LOTRWaypoint.Custom.addCustomWaypoint(entityplayer, new LOTRWaypoint.Custom(waypointName, entityplayer.posX, entityplayer.posZ));
					}
				}
			}
		}
		
		else if (channel.equals("lotr.deleteCWP"))
		{
			int id = data.readInt();
			World world = DimensionManager.getWorld(data.readByte());
			if (world != null && world.provider.dimensionId == LOTRDimension.MIDDLE_EARTH.dimensionID)
			{
				Entity entity = world.getEntityByID(id);
				if (entity instanceof EntityPlayer)
				{
					EntityPlayer entityplayer = (EntityPlayer)entity;
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
		}
		
		else if (channel.equals("lotr.mountInv"))
		{
			int id = data.readInt();
			World world = DimensionManager.getWorld(data.readByte());
			if (world != null)
			{
				Entity entity = world.getEntityByID(id);
				if (entity instanceof EntityPlayer)
				{
					EntityPlayer entityplayer = (EntityPlayer)entity;
					if (entityplayer.ridingEntity instanceof LOTREntityNPCRideable)
					{
						((LOTREntityNPCRideable)entityplayer.ridingEntity).openGUI(entityplayer);
					}
				}
			}
		}
		
		else if (channel.equals("lotr.editBanner"))
		{
			int id = data.readInt();
			World world = DimensionManager.getWorld(data.readByte());
			if (world != null)
			{
				Entity entity = world.getEntityByID(id);
				if (entity instanceof LOTREntityBanner)
				{
					LOTREntityBanner banner = (LOTREntityBanner)entity;
					banner.playerSpecificProtection = data.readBoolean();
					
					int index = 0;
					while ((index = data.readInt()) > 0)
					{
						int length = data.readByte();
						String name = data.readBytes(length).toString(Charsets.UTF_8);
						UUID uuid = UUID.fromString(PreYggdrasilConverter.func_152719_a(name));
						if (uuid != null)
						{
							banner.whitelistPlayer(index, uuid);
						}
					}
				}
			}
		}
		
		else if (channel.equals("lotr.mqAccept"))
		{
			int id = data.readInt();
			World world = DimensionManager.getWorld(data.readByte());
			if (world != null)
			{
				Entity entity = world.getEntityByID(id);
				if (entity instanceof EntityPlayer)
				{
					EntityPlayer entityplayer = (EntityPlayer)entity;
					LOTRPlayerData playerData = LOTRLevelData.getData(entityplayer);
					boolean accept = data.readBoolean();
					
					int npcID = data.readInt();
					Entity npcEntity = world.getEntityByID(npcID);
					if (npcEntity instanceof LOTREntityNPC)
					{
						LOTREntityNPC npc = (LOTREntityNPC)npcEntity;
						npc.isOfferingMiniQuest = false;
						
						if (accept)
						{
							NBTTagCompound nbt = new PacketBuffer(data).readNBTTagCompoundFromBuffer();
							LOTRMiniQuest quest = LOTRMiniQuest.loadQuestFromNBT(nbt, playerData);
							if (quest != null)
							{
								playerData.addMiniQuest(quest);
								npc.hasMiniQuest = true;
							}
						}
					}
				}
			}
		}
		
		else if (channel.equals("lotr.mqDelete"))
		{
			int id = data.readInt();
			World world = DimensionManager.getWorld(data.readByte());
			if (world != null)
			{
				Entity entity = world.getEntityByID(id);
				if (entity instanceof EntityPlayer)
				{
					EntityPlayer entityplayer = (EntityPlayer)entity;
					LOTRPlayerData playerData = LOTRLevelData.getData(entityplayer);
					
					UUID questUUID = new UUID(data.readLong(), data.readLong());
					LOTRMiniQuest removeQuest = null;
					
					for (LOTRMiniQuest quest : playerData.getMiniQuests())
					{
						if (quest.entityUUID.equals(questUUID))
						{
							removeQuest = quest;
							break;
						}
					}
					
					playerData.removeMiniQuest(removeQuest, false);
				}
			}
		}
		
		else if (channel.equals("lotr.titleSelect"))
		{
			int id = data.readInt();
			World world = DimensionManager.getWorld(data.readByte());
			if (world != null)
			{
				Entity entity = world.getEntityByID(id);
				if (entity instanceof EntityPlayer)
				{
					EntityPlayer entityplayer = (EntityPlayer)entity;
					LOTRPlayerData playerData = LOTRLevelData.getData(entityplayer);
					
					int titleID = data.readInt();
					int colorID = data.readInt();
					
					if (titleID == -1)
					{
						playerData.setPlayerTitle(null);
					}
					else
					{
						LOTRTitle title = LOTRTitle.forID(titleID);
						EnumChatFormatting color = LOTRTitle.PlayerTitle.colorForID(colorID);
						if (title != null && title.canPlayerUse(entityplayer))
						{
							playerData.setPlayerTitle(new LOTRTitle.PlayerTitle(title, color));
						}
					}
				}
			}
		}
		
		else if (channel.equals("lotr.editBannerAlignment"))
		{
			int id = data.readInt();
			World world = DimensionManager.getWorld(data.readByte());
			if (world != null)
			{
				Entity entity = world.getEntityByID(id);
				if (entity instanceof LOTREntityBanner)
				{
					LOTREntityBanner banner = (LOTREntityBanner)entity;
					int alignment = data.readInt();
					banner.setAlignmentProtection(alignment);
				}
			}
		}
	}
}