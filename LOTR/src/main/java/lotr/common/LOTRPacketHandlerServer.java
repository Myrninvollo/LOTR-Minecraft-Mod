package lotr.common;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.ArrayList;
import java.util.List;

import lotr.common.LOTRCapes.CapeType;
import lotr.common.entity.LOTREntities;
import lotr.common.entity.animal.LOTREntityCamel;
import lotr.common.entity.npc.*;
import lotr.common.entity.npc.LOTRHiredNPCInfo.Task;
import lotr.common.inventory.*;
import lotr.common.tileentity.LOTRTileEntityMobSpawner;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S3FPacketCustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
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
		NetworkRegistry.INSTANCE.newChannel("lotr.checkAch", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.checkAl", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.brewing", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.selectCape", this);
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
		NetworkRegistry.INSTANCE.newChannel("lotr.viewingF", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.createCWP", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.deleteCWP", this);
		NetworkRegistry.INSTANCE.newChannel("lotr.camelGui", this);
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
							LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.earnManyCoins);
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
							
							EntityLiving hiredEntity = (EntityLiving)LOTREntities.createEntityByClass(trade.entityClass, world);
							if (hiredEntity != null)
							{
								Task task = trade.task;
								
								EntityLiving hiredMount = null;
								if (trade.mountClass != null)
								{
									hiredMount = (EntityLiving)LOTREntities.createEntityByClass(trade.mountClass, world);
								}
								
								if (hiredMount != null)
								{
									hiredMount.setLocationAndAngles(entityplayer.posX, entityplayer.boundingBox.minY, entityplayer.posZ, world.rand.nextFloat() * 360F, 0F);
									if (hiredMount instanceof LOTREntityNPC)
									{
										LOTREntityNPC hiredMountNPC = (LOTREntityNPC)hiredMount;
										hiredMountNPC.initCreatureForHire(null);
										hiredMountNPC.hiredNPCInfo.isActive = true;
										hiredMountNPC.hiredNPCInfo.alignmentRequiredToCommand = trade.alignmentRequired;
										hiredMountNPC.hiredNPCInfo.setHiringPlayerName(entityplayer.getCommandSenderName());
										hiredMountNPC.hiredNPCInfo.setTask(task);
									}
									else
									{
										hiredMount.onSpawnWithEgg(null);
									}
									world.spawnEntityInWorld(hiredMount);
								}
								
								LOTREntityNPC hiredNPC = (LOTREntityNPC)hiredEntity;
								hiredNPC.setLocationAndAngles(entityplayer.posX, entityplayer.boundingBox.minY, entityplayer.posZ, world.rand.nextFloat() * 360F, 0F);
								hiredNPC.initCreatureForHire(null);
								hiredNPC.hiredNPCInfo.isActive = true;
								hiredNPC.hiredNPCInfo.alignmentRequiredToCommand = trade.alignmentRequired;
								hiredNPC.hiredNPCInfo.setHiringPlayerName(entityplayer.getCommandSenderName());
								hiredNPC.hiredNPCInfo.setTask(task);
								world.spawnEntityInWorld(hiredNPC);
								
								if (hiredMount != null)
								{
									hiredNPC.mountEntity(hiredMount);
									if (hiredMount instanceof LOTRNPCMount)
									{
										hiredNPC.setRidingHorse(true);
										LOTRNPCMount hiredHorse = (LOTRNPCMount)hiredMount;
										hiredHorse.setBelongsToNPC(true);
										hiredHorse.setNavigatorRangeFrom(hiredNPC);
									}
								}
								
								if (task == Task.FARMER)
								{
									LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.hireFarmer);
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
		
		else if (channel.equals("lotr.checkAch"))
		{
			int id = data.readInt();
			World world = DimensionManager.getWorld(data.readByte());
			if (world != null)
			{
				Entity entity = world.getEntityByID(id);
				if (entity instanceof EntityPlayer)
				{
					EntityPlayer entityplayer = (EntityPlayer)entity;
					LOTRLevelData.playersCheckedAchievements.add(entityplayer.getUniqueID());
					LOTRLevelData.needsSave = true;
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
					LOTRLevelData.playersCheckedAlignments.add(entityplayer.getUniqueID());
					LOTRLevelData.needsSave = true;
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
						LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.brewDrinkInBarrel);
					}
				}
			}
		}
		
		else if (channel.equals("lotr.selectCape"))
		{
			int id = data.readInt();
			World world = DimensionManager.getWorld(data.readByte());
			if (world != null)
			{
				Entity entity = world.getEntityByID(id);
				if (entity instanceof EntityPlayer)
				{
					EntityPlayer entityplayer = (EntityPlayer)entity;
					int capeID = data.readByte();
					int capeTypeID = data.readByte();
					if (capeTypeID < 0 || capeTypeID >= CapeType.values().length)
					{
						System.out.println("Failed to update LOTR cape on server side: There is no capetype with ID " + capeTypeID);
						return;
					}
					CapeType capeType = CapeType.values()[capeTypeID];
					if (capeID < 0 || capeID >= capeType.list.size())
					{
						System.out.println("Failed to update LOTR cape on server side: There is no cape with ID " + capeID + " for capetype " + capeTypeID);
						return;
					}
					LOTRCapes cape = (LOTRCapes)capeType.list.get(capeID);
					if (cape.canPlayerWearCape(entityplayer))
					{
						LOTRLevelData.setCape(entityplayer, cape);
						LOTRLevelData.sendCapeToAllPlayersInWorld(entityplayer, world);
					}
					else
					{
						System.out.println("Failed to update LOTR cape on server side: Player " + entityplayer.getCommandSenderName() + " cannot wear cape " + cape.name());
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
						boolean flag = LOTRLevelData.getFriendlyFire(entityplayer);
						LOTRLevelData.setFriendlyFire(entityplayer, !flag);
					}
					else if (option == LOTROptions.HIRED_DEATH_MESSAGES)
					{
						boolean flag = LOTRLevelData.getEnableHiredDeathMessages(entityplayer);
						LOTRLevelData.setEnableHiredDeathMessages(entityplayer, !flag);
					}
					else if (option == LOTROptions.ENABLE_CAPE)
					{
						boolean flag = LOTRLevelData.getEnableCape(entityplayer);
						LOTRLevelData.setEnableCape(entityplayer, !flag);
						LOTRLevelData.sendCapeToAllPlayersInWorld(entityplayer, world);
					}
					else if (option == LOTROptions.SHOW_ALIGNMENT)
					{
						boolean flag = LOTRLevelData.getHideAlignment(entityplayer);
						LOTRLevelData.setHideAlignment(entityplayer, !flag);
						LOTRLevelData.sendAlignmentToAllPlayersInWorld(entityplayer, world);
					}
					else if (option == LOTROptions.SHOW_MAP_LOCATION)
					{
						boolean flag = LOTRLevelData.getShowMapLocation(entityplayer);
						LOTRLevelData.setShowMapLocation(entityplayer, !flag);
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
					if (LOTRLevelData.getFastTravelTimer(entityplayer) <= 0)
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
								LOTRLevelData.setFastTravelTimer(entityplayer, LOTRLevelData.fastTravelCooldown);
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
		
		else if (channel.equals("lotr.viewingF"))
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
						LOTRLevelData.setViewingFaction(entityplayer, faction);
					}
				}
			}
		}
		
		else if (channel.equals("lotr.createCWP"))
		{
			int id = data.readInt();
			World world = DimensionManager.getWorld(data.readByte());
			if (world != null && world.provider.dimensionId == LOTRMod.idDimension)
			{
				Entity entity = world.getEntityByID(id);
				if (entity instanceof EntityPlayer)
				{
					EntityPlayer entityplayer = (EntityPlayer)entity;
					int stringLength = data.readShort();
					String waypointName = data.toString(data.readerIndex(), stringLength, Charsets.UTF_8);
					
					int waypoints = LOTRWaypoint.Custom.getWaypointList(entityplayer).size();
					if (waypoints <= LOTRWaypoint.Custom.MAX_CUSTOM)
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
			if (world != null && world.provider.dimensionId == LOTRMod.idDimension)
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
		
		else if (channel.equals("lotr.camelGui"))
		{
			int id = data.readInt();
			World world = DimensionManager.getWorld(data.readByte());
			if (world != null)
			{
				Entity entity = world.getEntityByID(id);
				if (entity instanceof EntityPlayer)
				{
					EntityPlayer entityplayer = (EntityPlayer)entity;
					if (entityplayer.ridingEntity instanceof LOTREntityCamel)
					{
						((LOTREntityCamel)entityplayer.ridingEntity).openGUI(entityplayer);
					}
				}
			}
		}	
	}
}