package lotr.common;

import io.netty.buffer.Unpooled;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lotr.common.block.LOTRBlockCraftingTable;
import lotr.common.block.LOTRBlockPortal;
import lotr.common.entity.item.LOTREntityPortal;
import lotr.common.entity.npc.LOTREntityBlueDwarf;
import lotr.common.entity.npc.LOTREntityBlueDwarfAxeThrower;
import lotr.common.entity.npc.LOTREntityBlueDwarfMerchant;
import lotr.common.entity.npc.LOTREntityBlueDwarfWarrior;
import lotr.common.entity.npc.LOTREntityDunlending;
import lotr.common.entity.npc.LOTREntityDunlendingArcher;
import lotr.common.entity.npc.LOTREntityDunlendingWarrior;
import lotr.common.entity.npc.LOTREntityDwarf;
import lotr.common.entity.npc.LOTREntityDwarfAxeThrower;
import lotr.common.entity.npc.LOTREntityDwarfWarrior;
import lotr.common.entity.npc.LOTREntityElvenTrader;
import lotr.common.entity.npc.LOTREntityGondorArcher;
import lotr.common.entity.npc.LOTREntityGondorSoldier;
import lotr.common.entity.npc.LOTREntityGundabadOrc;
import lotr.common.entity.npc.LOTREntityGundabadOrcArcher;
import lotr.common.entity.npc.LOTREntityMordorOrc;
import lotr.common.entity.npc.LOTREntityNearHaradrim;
import lotr.common.entity.npc.LOTREntityNearHaradrimArcher;
import lotr.common.entity.npc.LOTREntityNearHaradrimWarrior;
import lotr.common.entity.npc.LOTREntityNurnSlave;
import lotr.common.entity.npc.LOTREntityRanger;
import lotr.common.entity.npc.LOTREntityRangerNorth;
import lotr.common.entity.npc.LOTREntityRohirrim;
import lotr.common.entity.npc.LOTREntityRohirrimArcher;
import lotr.common.entity.npc.LOTREntityUrukHai;
import lotr.common.entity.npc.LOTREntityUrukHaiCrossbower;
import lotr.common.entity.npc.LOTREntityWoodElf;
import lotr.common.entity.npc.LOTREntityWoodElfScout;
import lotr.common.entity.npc.LOTREntityWoodElfWarrior;
import lotr.common.item.LOTRItemStructureSpawner;
import lotr.common.world.LOTRBanditSpawner;
import lotr.common.world.LOTRDerivedWorldInfo;
import lotr.common.world.LOTRInvasionSpawner;
import lotr.common.world.LOTRSpawnerNPCs;
import lotr.common.world.LOTRStructureSpawningInfo;
import lotr.common.world.LOTRTeleporter;
import lotr.common.world.LOTRTravellingTraderSpawner;
import lotr.common.world.biome.LOTRBiome;
import lotr.common.world.biome.LOTRBiomeGenMistyMountains;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.S3FPacketCustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.DimensionManager;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;

public class LOTRTickHandlerServer
{
	public static List structureSpawning = new ArrayList();
	public static List travellingTraders = new ArrayList();
	
	public static void createSpawningLists()
	{
		structureSpawning.add(new LOTRStructureSpawningInfo(LOTRLevelData.beaconTowerLocations).setCheckInfo(16, -12, 12, LOTREntityGondorSoldier.class, 4).setSpawnInfo(2, -2, 2, LOTREntityGondorSoldier.class, LOTREntityGondorArcher.class, 16).addSpawnBlock(LOTRMod.slabDouble, 2).addSpecialEquipment(4, new ItemStack(LOTRMod.helmetGondorWinged)));
		structureSpawning.add(new LOTRStructureSpawningInfo(LOTRLevelData.gondorFortressLocations).setCheckInfo(24, -8, 18, LOTREntityGondorSoldier.class, 12).setSpawnInfo(4, 2, 17, LOTREntityGondorSoldier.class, LOTREntityGondorArcher.class, 32).addSpawnBlock(LOTRMod.brick, 1).addSpawnBlock(LOTRMod.brick, 2).addSpawnBlock(LOTRMod.brick, 3));
		structureSpawning.add(new LOTRStructureSpawningInfo(LOTRLevelData.dwarvenTowerLocations).setCheckInfo(12, -8, 42, LOTREntityDwarf.class, 16).setSpawnInfo(4, 1, 41, LOTREntityDwarfWarrior.class, LOTREntityDwarfAxeThrower.class, 12).setHomePosFromSpawn().addSpawnBlock(Blocks.planks, 1));
		structureSpawning.add(new LOTRStructureSpawningInfo(LOTRLevelData.urukCampLocations).setCheckInfo(24, -12, 12, LOTREntityUrukHai.class, 12).setSpawnInfo(8, -4, 4, LOTREntityUrukHai.class, LOTREntityUrukHaiCrossbower.class, 16).addSpawnBlock(Blocks.grass));
		structureSpawning.add(new LOTRStructureSpawningInfo(LOTRLevelData.rohanFortressLocations).setCheckInfo(16, -8, 10, LOTREntityRohirrim.class, 12).setSpawnInfo(11, 1, 6, LOTREntityRohirrim.class, LOTREntityRohirrimArcher.class, 20).addSpawnBlock(Blocks.grass).addSpawnBlock(Blocks.planks, 0).addSpawnBlock(LOTRMod.brick, 4));
		structureSpawning.add(new LOTRStructureSpawningInfo(LOTRLevelData.woodElvenTowerLocations).setCheckInfo(12, -16, 40, LOTREntityWoodElf.class, 12).setSpawnInfo(5, 1, 40, LOTREntityWoodElfWarrior.class, LOTREntityWoodElfScout.class, 12).setHomePosFromSpawn().addSpawnBlock(LOTRMod.planks, 2));
		structureSpawning.add(new LOTRStructureSpawningInfo(LOTRLevelData.mordorTowerLocations).setCheckInfo(12, -8, 50, LOTREntityMordorOrc.class, 20).setSpawnInfo(5, 1, 40, LOTREntityMordorOrc.class, LOTREntityMordorOrc.class, 16).setHomePosFromSpawn().addSpawnBlock(LOTRMod.slabSingle, 8));
		structureSpawning.add(new LOTRStructureSpawningInfo(LOTRLevelData.nurnFarmLocations).setCheckInfo(12, -8, 8, LOTREntityNurnSlave.class, 8).setSpawnInfo(6, -2, 2, LOTREntityNurnSlave.class, LOTREntityNurnSlave.class, 8).addSpawnBlock(LOTRMod.slabSingle, 1));
		structureSpawning.add(new LOTRStructureSpawningInfo(LOTRLevelData.gundabadCampLocations).setCheckInfo(24, -12, 12, LOTREntityGundabadOrc.class, 12).setSpawnInfo(8, -4, 4, LOTREntityGundabadOrc.class, LOTREntityGundabadOrcArcher.class, 16).addSpawnBlock(Blocks.grass));
		structureSpawning.add(new LOTRStructureSpawningInfo(LOTRLevelData.dunlandHillFortLocations).setCheckInfo(16, -8, 10, LOTREntityDunlending.class, 8).setSpawnInfo(6, 1, 6, LOTREntityDunlendingWarrior.class, LOTREntityDunlendingArcher.class, 16).addSpawnBlock(Blocks.grass).addSpawnBlock(Blocks.planks, 0).addSpawnBlock(Blocks.planks, 1));
		structureSpawning.add(new LOTRStructureSpawningInfo(LOTRLevelData.blueMountainsStrongholdLocations).setCheckInfo(8, -8, 16, LOTREntityBlueDwarf.class, 8).setSpawnInfo(8, 1, 10, LOTREntityBlueDwarfWarrior.class, LOTREntityBlueDwarfAxeThrower.class, 16).setHomePosFromSpawn().addSpawnBlock(Blocks.planks, 1).addSpawnBlock(LOTRMod.slabSingle3, 0));
		structureSpawning.add(new LOTRStructureSpawningInfo(LOTRLevelData.rangerCampLocations).setCheckInfo(24, -12, 12, LOTREntityRanger.class, 12).setSpawnInfo(8, -4, 4, LOTREntityRangerNorth.class, LOTREntityRangerNorth.class, 16).addSpawnBlock(Blocks.grass));
		structureSpawning.add(new LOTRStructureSpawningInfo(LOTRLevelData.nearHaradTowerLocations).setCheckInfo(20, -16, 40, LOTREntityNearHaradrim.class, 12).setSpawnInfo(10, 1, 6, LOTREntityNearHaradrimWarrior.class, LOTREntityNearHaradrimArcher.class, 12).setHomePosFromSpawn().addSpawnBlock(Blocks.sandstone, 2));
		structureSpawning.add(new LOTRStructureSpawningInfo(LOTRLevelData.nearHaradFortressLocations).setCheckInfo(20, -8, 12, LOTREntityNearHaradrim.class, 12).setSpawnInfo(8, 1, 2, LOTREntityNearHaradrimWarrior.class, LOTREntityNearHaradrimArcher.class, 12).addSpawnBlock(Blocks.grass).addSpawnBlock(Blocks.sand));
		structureSpawning.add(new LOTRStructureSpawningInfo(LOTRLevelData.nearHaradCampLocations).setCheckInfo(20, -12, 12, LOTREntityNearHaradrim.class, 4).setSpawnInfo(8, -4, 4, LOTREntityNearHaradrimWarrior.class, LOTREntityNearHaradrimWarrior.class, 16).addSpawnBlock(Blocks.sand));
		structureSpawning.add(new LOTRStructureSpawningInfo(LOTRLevelData.rangerWatchtowerLocations).setCheckInfo(24, -12, 20, LOTREntityRangerNorth.class, 8).setSpawnInfo(4, -4, 4, LOTREntityRangerNorth.class, LOTREntityRangerNorth.class, 16).addSpawnBlock(Blocks.grass));

		travellingTraders.add(new LOTRTravellingTraderSpawner(LOTREntityElvenTrader.class));
		travellingTraders.add(new LOTRTravellingTraderSpawner(LOTREntityBlueDwarfMerchant.class));
	}
	
	public static HashMap playersInPortals = new HashMap();
	public static HashMap playersInElvenPortals = new HashMap();
	public static HashMap playersInMorgulPortals = new HashMap();
	private int fireworkDisplay;
	
	public LOTRTickHandlerServer()
	{
		FMLCommonHandler.instance().bus().register(this);
	}
	
	@SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event)
	{
		World world = event.world;
		
		if (world.isRemote || event.phase != Phase.END)
		{
			return;
		}
		
		if (world == DimensionManager.getWorld(0))
		{
			if (LOTRLevelData.needsLoad)
			{
				LOTRLevelData.load();
				LOTRLevelData.needsLoad = false;
			}
			
			if (LOTRLevelData.needsSave)
			{
				LOTRLevelData.save();
				LOTRLevelData.needsSave = false;
			}
			
			if (world.getWorldTime() % 600L == 0L)
			{
				LOTRLevelData.save();
			}
			
			if (LOTRItemStructureSpawner.lastStructureSpawnTick > 0)
			{
				LOTRItemStructureSpawner.lastStructureSpawnTick--;
			}
		}
		
		if (world == DimensionManager.getWorld(LOTRMod.idDimension))
		{
			if (world.getWorldInfo().getClass() != LOTRDerivedWorldInfo.class)
			{
				LOTRReflection.setWorldInfo((WorldServer)world, new LOTRDerivedWorldInfo(DimensionManager.getWorld(0).getWorldInfo()));
				System.out.println("Successfully replaced LOTR world info");
			}
			
			if (!world.playerEntities.isEmpty())
			{
				for (int i = 0; i < travellingTraders.size(); i++)
				{
					LOTRTravellingTraderSpawner obj = (LOTRTravellingTraderSpawner)travellingTraders.get(i);
					obj.performSpawning(world);
				}
				
				LOTRBanditSpawner.performSpawning(world);
				
				LOTRInvasionSpawner.performSpawning(world);
				
				if (LOTRMod.isNewYearsDay())
				{
					if (fireworkDisplay == 0 && world.rand.nextInt(4000) == 0)
					{
						fireworkDisplay = 100 + world.rand.nextInt(300);
					}

					if (fireworkDisplay > 0)
					{
						fireworkDisplay--;
						if (world.rand.nextInt(50) == 0)
						{
							int launches = 1 + world.rand.nextInt(7 + (world.playerEntities.size() / 2));
							for (int l = 0; l < launches; l++)
							{
								EntityPlayer entityplayer = (EntityPlayer)world.playerEntities.get(world.rand.nextInt(world.playerEntities.size()));
								int i = MathHelper.floor_double(entityplayer.posX) - 64 + world.rand.nextInt(129);
								int k = MathHelper.floor_double(entityplayer.posZ) - 64 + world.rand.nextInt(129);
								int j = world.getHeightValue(i, k);
								if (world.getBlock(i, j - 1, k).isNormalCube())
								{
									int fireworks = 1 + world.rand.nextInt(4);
									for (int l1 = 0; l1 < fireworks; l1++)
									{
										int i1 = i - world.rand.nextInt(8) + world.rand.nextInt(8);
										int k1 = k - world.rand.nextInt(8) + world.rand.nextInt(8);
										int j1 = world.getHeightValue(i1, k1);
										if (world.getBlock(i1, j1 - 1, k1).isNormalCube())
										{
											ItemStack itemstack = new ItemStack(Items.fireworks);
											NBTTagCompound itemData = new NBTTagCompound();
											NBTTagCompound fireworkData = new NBTTagCompound();
											NBTTagList explosionsList = new NBTTagList();
											int explosions = 1 + world.rand.nextInt(3);
											for (int l2 = 0; l2 < explosions; l2++)
											{
												NBTTagCompound explosionData = new NBTTagCompound();
												if (world.rand.nextBoolean())
												{
													explosionData.setBoolean("Flicker", true);
												}
												if (world.rand.nextBoolean())
												{
													explosionData.setBoolean("Trail", true);
												}
												int[] colors = new int[1 + world.rand.nextInt(3)];
												for (int l3 = 0; l3 < colors.length; l3++)
												{
													colors[l3] = ItemDye.field_150922_c[world.rand.nextInt(ItemDye.field_150922_c.length)];
												}
												explosionData.setIntArray("Colors", colors);
												int effectType = world.rand.nextInt(5);
												if (effectType == 3)
												{
													effectType = 0;
												}
												explosionData.setByte("Type", (byte)effectType);
												explosionsList.appendTag(explosionData);
											}
											fireworkData.setTag("Explosions", explosionsList);
											int flight = 1 + world.rand.nextInt(3);
											fireworkData.setByte("Flight", (byte)flight);
											itemData.setTag("Fireworks", fireworkData);
											itemstack.setTagCompound(itemData);
											EntityFireworkRocket firework = new EntityFireworkRocket(world, i1 + 0.5D, j1 + 0.5D, k1 + 0.5D, itemstack);
											world.spawnEntityInWorld(firework);
										}
									}
								}
							}
						}
					}
				}
				
				if (world.getWorldTime() % 20L == 0L)
				{
					for (int i = 0; i < world.playerEntities.size(); i++)
					{
						EntityPlayer entityplayer = (EntityPlayer)world.playerEntities.get(i);
						LOTRLevelData.sendPlayerLocationsToPlayer(entityplayer, world);
					}
				}
			}
			
			if (world.getGameRules().getGameRuleBooleanValue("doMobSpawning"))
			{
				LOTRSpawnerNPCs.performSpawning(world);
				
				for (int i = 0; i < structureSpawning.size(); i++)
				{
					LOTRStructureSpawningInfo obj = (LOTRStructureSpawningInfo)structureSpawning.get(i);
					obj.performSpawning(world);
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onPlayerTick(TickEvent.PlayerTickEvent event)
	{
		EntityPlayer player = (EntityPlayer)event.player;
		World world = player.worldObj;
		
		if (world == null || world.isRemote || event.phase != Phase.END)
		{
			return;
		}
		
		if (player != null && player instanceof EntityPlayerMP)
		{
			EntityPlayerMP entityplayer = (EntityPlayerMP)player;
			
			runAchievementChecks(entityplayer);
			
			int fastTravelTimer = LOTRLevelData.getFastTravelTimer(entityplayer);
			if (fastTravelTimer > 0)
			{
				fastTravelTimer--;
				LOTRLevelData.setFastTravelTimer(entityplayer, fastTravelTimer);
			}
			
			if (entityplayer.dimension == LOTRMod.idDimension)
			{
				if (!LOTRLevelData.playersCheckedAchievements.contains(entityplayer.getUniqueID()))
				{
					entityplayer.playerNetServerHandler.sendPacket(new S3FPacketCustomPayload("lotr.promptAch", Unpooled.buffer(0)));
				}
				else if (!LOTRLevelData.playersCheckedAlignments.contains(entityplayer.getUniqueID()))
				{
					entityplayer.playerNetServerHandler.sendPacket(new S3FPacketCustomPayload("lotr.promptAl", Unpooled.buffer(0)));
				}
			}
			
			if (entityplayer.dimension == 0 && LOTRLevelData.madePortal == 0)
			{
				List items = world.getEntitiesWithinAABB(EntityItem.class, entityplayer.boundingBox.expand(16D, 16D, 16D));
				for (Object obj : items)
				{
					EntityItem item = (EntityItem)obj;
					if (LOTRLevelData.madePortal == 0 && item.getEntityItem() != null && item.getEntityItem().getItem() == LOTRMod.goldRing && item.isBurning())
					{
						LOTRLevelData.setMadePortal(1);
						LOTRLevelData.markOverworldPortalLocation(MathHelper.floor_double(item.posX), MathHelper.floor_double(item.posY), MathHelper.floor_double(item.posZ));
						item.setDead();
						world.createExplosion(entityplayer, item.posX, item.posY + 3D, item.posZ, 3F, true);
						Entity portal = new LOTREntityPortal(world);
						portal.setLocationAndAngles(item.posX, item.posY + 3D, item.posZ, 0F, 0F);
						world.spawnEntityInWorld(portal);
					}
				}
			}
			
			if ((entityplayer.dimension == 0 || entityplayer.dimension == LOTRMod.idDimension))
			{
				List items = world.getEntitiesWithinAABB(EntityItem.class, entityplayer.boundingBox.expand(16D, 16D, 16D));
				for (Object obj : items)
				{
					EntityItem item = (EntityItem)obj;
					if (item.getEntityItem() == null)
					{
						continue;
					}
					
					int i = MathHelper.floor_double(item.posX);
					int j = MathHelper.floor_double(item.posY);
					int k = MathHelper.floor_double(item.posZ);
					ItemStack itemstack = item.getEntityItem();
					
					if (LOTRLevelData.getAlignment(entityplayer, LOTRFaction.GALADHRIM) > LOTRAlignmentValues.USE_PORTAL)
					{
						if (itemstack.getItem() == Item.getItemFromBlock(LOTRMod.elanor) || itemstack.getItem() == Item.getItemFromBlock(LOTRMod.niphredil))
						{
							boolean foundPortalLocation = false;
							int[] portalLocation = new int[3];
							for (int i1 = i - 2; !foundPortalLocation && i1 <= i + 2; i1++)
							{
								for (int k1 = k - 2; !foundPortalLocation && k1 <= k + 2; k1++)
								{
									if (((LOTRBlockPortal)LOTRMod.elvenPortal).isValidPortalLocation(world, i1, j, k1, false))
									{
										foundPortalLocation = true;
										portalLocation = new int[] {i1, j, k1};
									}
								}
							}
							
							if (foundPortalLocation)
							{
								item.setDead();
								for (int i1 = -1; i1 <= 1; i1++)
								{
									for (int k1 = -1; k1 <= 1; k1++)
									{
										world.setBlock(portalLocation[0] + i1, portalLocation[1], portalLocation[2] + k1, LOTRMod.elvenPortal, 0, 2);
									}
								}
							}
						}
					}
					
					if (LOTRLevelData.getAlignment(entityplayer, LOTRFaction.MORDOR) > LOTRAlignmentValues.USE_PORTAL || LOTRLevelData.getAlignment(entityplayer, LOTRFaction.ANGMAR) > LOTRAlignmentValues.USE_PORTAL)
					{
						if (LOTRMod.isOreNameEqual(itemstack, "bone"))
						{
							boolean foundPortalLocation = false;
							int[] portalLocation = new int[3];
							for (int i1 = i - 2; !foundPortalLocation && i1 <= i + 2; i1++)
							{
								for (int k1 = k - 2; !foundPortalLocation && k1 <= k + 2; k1++)
								{
									if (((LOTRBlockPortal)LOTRMod.morgulPortal).isValidPortalLocation(world, i1, j, k1, false))
									{
										foundPortalLocation = true;
										portalLocation = new int[] {i1, j, k1};
									}
								}
							}
							
							if (foundPortalLocation)
							{
								item.setDead();
								for (int i1 = -1; i1 <= 1; i1++)
								{
									for (int k1 = -1; k1 <= 1; k1++)
									{
										world.setBlock(portalLocation[0] + i1, portalLocation[1], portalLocation[2] + k1, LOTRMod.morgulPortal, 0, 2);
									}
								}
							}
						}
					}
				}
			}
			
			if ((entityplayer.dimension == 0 || entityplayer.dimension == LOTRMod.idDimension) && playersInPortals.containsKey(entityplayer))
			{
				List portals = world.getEntitiesWithinAABB(LOTREntityPortal.class, entityplayer.boundingBox.expand(8D, 8D, 8D));
				boolean inPortal = false;
				for (int i = 0; i < portals.size(); i++)
				{
					LOTREntityPortal portal = (LOTREntityPortal)portals.get(i);
					if (portal.boundingBox.intersectsWith(entityplayer.boundingBox))
					{
						inPortal = true;
						break;
					}
				}
				if (inPortal)
				{
					int i = (Integer)playersInPortals.get(entityplayer);
					i++;
					playersInPortals.put(entityplayer, Integer.valueOf(i));
					if (i >= 100)
					{
						int dimension = 0;
						if (entityplayer.dimension == 0)
						{
							dimension = LOTRMod.idDimension;
						}
						else if (entityplayer.dimension == LOTRMod.idDimension)
						{
							dimension = 0;
						}
						if (world instanceof WorldServer)
						{
							MinecraftServer.getServer().getConfigurationManager().transferPlayerToDimension(entityplayer, dimension, new LOTRTeleporter(DimensionManager.getWorld(dimension)));
						}
						playersInPortals.remove(entityplayer);
					}
				}
				else
				{
					playersInPortals.remove(entityplayer);
				}
			}
			
			updatePlayerInPortal(entityplayer, playersInElvenPortals, ((LOTRBlockPortal)LOTRMod.elvenPortal));
			updatePlayerInPortal(entityplayer, playersInMorgulPortals, ((LOTRBlockPortal)LOTRMod.morgulPortal));
		}
	}
	
	private void updatePlayerInPortal(EntityPlayerMP entityplayer, HashMap players, LOTRBlockPortal portalBlock)
	{
		if ((entityplayer.dimension == 0 || entityplayer.dimension == LOTRMod.idDimension) && players.containsKey(entityplayer))
		{
			boolean inPortal = entityplayer.worldObj.getBlock(MathHelper.floor_double(entityplayer.posX), MathHelper.floor_double(entityplayer.boundingBox.minY), MathHelper.floor_double(entityplayer.posZ)) == portalBlock;
			if (inPortal)
			{
				int i = (Integer)players.get(entityplayer);
				i++;
				players.put(entityplayer, Integer.valueOf(i));
				if (i >= entityplayer.getMaxInPortalTime())
				{
					int dimension = 0;
					if (entityplayer.dimension == 0)
					{
						dimension = LOTRMod.idDimension;
					}
					else if (entityplayer.dimension == LOTRMod.idDimension)
					{
						dimension = 0;
					}
					WorldServer newWorld = MinecraftServer.getServer().worldServerForDimension(dimension);
					MinecraftServer.getServer().getConfigurationManager().transferPlayerToDimension(entityplayer, dimension, portalBlock.getPortalTeleporter(newWorld));
					players.remove(entityplayer);
				}
			}
			else
			{
				players.remove(entityplayer);
			}
		}
	}
	
	private void runAchievementChecks(EntityPlayer entityplayer)
	{
		World world = entityplayer.worldObj;
		int i = MathHelper.floor_double(entityplayer.posX);
		int j = MathHelper.floor_double(entityplayer.posY);
		int k = MathHelper.floor_double(entityplayer.posZ);
		
		BiomeGenBase biome = world.getBiomeGenForCoords(i, k);
		
		if (biome instanceof LOTRBiome)
		{
			LOTRBiome lotrbiome = (LOTRBiome)biome;
			if (lotrbiome.getBiomeAchievement() != null)
			{
				LOTRLevelData.addAchievement(entityplayer, lotrbiome.getBiomeAchievement());
			}
			
			if (lotrbiome.getBiomeWaypoints() != null)
			{
				lotrbiome.getBiomeWaypoints().unlockForPlayer(entityplayer);
			}
		}
		
		if (entityplayer.dimension == LOTRMod.idDimension)
		{
			LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.enterMiddleEarth);
		}
		
		if (entityplayer.inventory.hasItem(LOTRMod.pouch))
		{
			LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.getPouch);
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
			LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.collectCraftingTables);
		}
		
		if (crossbowBolts >= 128)
		{
			LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.collectCrossbowBolts);
		}
		
		if (isPlayerWearingFull(entityplayer, LOTRMod.armorMithril))
		{
			LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.wearFullMithril);
		}
		
		if (isPlayerWearingFull(entityplayer, LOTRMod.armorWarg))
		{
			LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.wearFullWargFur);
		}
		
		if (isPlayerWearingFull(entityplayer, LOTRMod.armorBlueDwarven))
		{
			LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.wearFullBlueDwarven);
		}
		
		if (isPlayerWearingFull(entityplayer, LOTRMod.armorHighElven))
		{
			LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.wearFullHighElven);
		}
		
		if (isPlayerWearingFull(entityplayer, LOTRMod.armorRanger))
		{
			LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.wearFullRanger);
		}
		
		if (isPlayerWearingFull(entityplayer, LOTRMod.armorAngmar))
		{
			LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.wearFullAngmar);
		}
		
		if (isPlayerWearingFull(entityplayer, LOTRMod.armorWoodElvenScout))
		{
			LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.wearFullWoodElvenScout);
		}
		
		if (isPlayerWearingFull(entityplayer, LOTRMod.armorWoodElven))
		{
			LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.wearFullWoodElven);
		}
		
		if (isPlayerWearingFull(entityplayer, LOTRMod.armorDwarven))
		{
			LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.wearFullDwarven);
		}
		
		if (biome instanceof LOTRBiomeGenMistyMountains && entityplayer.posY > 192D)
		{
			LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.climbMistyMountains);
		}
		
		if (isPlayerWearingFull(entityplayer, LOTRMod.armorElven))
		{
			LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.wearFullElven);
		}
		
		if (isPlayerWearingFull(entityplayer, LOTRMod.armorUruk))
		{
			LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.wearFullUruk);
		}
		
		if (isPlayerWearingFull(entityplayer, LOTRMod.armorRohan))
		{
			LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.wearFullRohirric);
		}
		
		if (isPlayerWearingFull(entityplayer, LOTRMod.armorDunlending))
		{
			LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.wearFullDunlending);
		}
		
		if (isPlayerWearingFull(entityplayer, LOTRMod.armorGondor))
		{
			LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.wearFullGondorian);
		}
		
		if (isPlayerWearingFull(entityplayer, LOTRMod.armorOrc))
		{
			LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.wearFullOrc);
		}
		
		if (isPlayerWearingFull(entityplayer, LOTRMod.armorNearHarad))
		{
			LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.wearFullNearHarad);
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
}