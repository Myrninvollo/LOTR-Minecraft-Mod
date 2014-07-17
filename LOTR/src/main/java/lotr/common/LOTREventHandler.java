package lotr.common;

import io.netty.buffer.Unpooled;

import java.util.List;

import lotr.common.block.LOTRBlockFlowerPot;
import lotr.common.block.LOTRBlockSaplingBase;
import lotr.common.entity.LOTREntityRegistry;
import lotr.common.entity.LOTREntityRegistry.RegistryInfo;
import lotr.common.entity.ai.LOTREntityAINearestAttackableTargetBasic;
import lotr.common.entity.animal.LOTREntityButterfly;
import lotr.common.entity.animal.LOTREntityZebra;
import lotr.common.entity.item.LOTREntityBanner;
import lotr.common.entity.item.LOTREntityTraderRespawn;
import lotr.common.entity.item.LOTREntityWargskinRug;
import lotr.common.entity.npc.*;
import lotr.common.entity.projectile.LOTREntityCrossbowBolt;
import lotr.common.entity.projectile.LOTREntitySpear;
import lotr.common.entity.projectile.LOTREntityThrowingAxe;
import lotr.common.inventory.*;
import lotr.common.item.LOTRItemPouch;
import lotr.common.world.biome.LOTRBiomeGenDeadMarshes;
import lotr.common.world.biome.LOTRBiomeGenFangorn;
import lotr.common.world.biome.LOTRBiomeGenForodwaith;
import lotr.common.world.biome.LOTRBiomeGenMirkwood;
import lotr.common.world.biome.LOTRBiomeGenNearHarad;
import lotr.common.world.biome.LOTRBiomeGenShire;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S3FPacketCustomPayload;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent.AllowDespawn;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.IFuelHandler;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemSmeltedEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import cpw.mods.fml.common.registry.GameRegistry;

public class LOTREventHandler implements IFuelHandler
{
	public LOTREventHandler()
	{
		FMLCommonHandler.instance().bus().register(this);
		MinecraftForge.EVENT_BUS.register(this);
		GameRegistry.registerFuelHandler(this);
	}
	
	@SubscribeEvent
	public void onCrafting(ItemCraftedEvent event)
	{
		EntityPlayer entityplayer = event.player;
		ItemStack itemstack = event.crafting;
		IInventory craftingInventory = event.craftMatrix;
		
		if (!entityplayer.worldObj.isRemote)
		{
			if (itemstack.getItem() == LOTRMod.pouch && craftingInventory instanceof InventoryCrafting)
			{
				ItemStack[] stackList = LOTRReflection.getStackList((InventoryCrafting)craftingInventory);
				for (int i = 0; i < stackList.length; i++)
				{
					stackList[i] = null;
				}
			}
			
			if (entityplayer.openContainer instanceof LOTRContainerElvenTable)
			{
				LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.useElvenTable);
			}
			
			if (entityplayer.openContainer instanceof LOTRContainerUrukTable)
			{
				LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.useUrukTable);
			}
			
			if (entityplayer.openContainer instanceof LOTRContainerRohirricTable)
			{
				LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.useRohirricTable);
			}
			
			if (entityplayer.openContainer instanceof LOTRContainerGondorianTable)
			{
				LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.useGondorianTable);
			}
			
			if (entityplayer.openContainer instanceof LOTRContainerWoodElvenTable)
			{
				LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.useWoodElvenTable);
			}
			
			if (entityplayer.openContainer instanceof LOTRContainerDwarvenTable)
			{
				LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.useDwarvenTable);
			}
			
			if (entityplayer.openContainer instanceof LOTRContainerMorgulTable)
			{
				LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.useMorgulTable);
			}
			
			if (entityplayer.openContainer instanceof LOTRContainerDunlendingTable)
			{
				LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.useDunlendingTable);
			}
			
			if (entityplayer.openContainer instanceof LOTRContainerAngmarTable)
			{
				LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.useAngmarTable);
			}
			
			if (entityplayer.openContainer instanceof LOTRContainerNearHaradTable)
			{
				LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.useNearHaradTable);
			}
			
			if (entityplayer.openContainer instanceof LOTRContainerHighElvenTable)
			{
				LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.useHighElvenTable);
			}
			
			if (entityplayer.openContainer instanceof LOTRContainerBlueDwarvenTable)
			{
				LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.useBlueDwarvenTable);
			}
			
			if (itemstack.getItem() == Items.saddle)
			{
				LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.craftSaddle);
			}
			
			if (itemstack.getItem() == LOTRMod.bronze)
			{
				LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.craftBronze);
			}
			
			if (itemstack.getItem() == LOTRMod.appleCrumbleItem)
			{
				LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.craftAppleCrumble);
			}
			
			if (itemstack.getItem() == LOTRMod.rabbitStew)
			{
				LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.craftRabbitStew);
			}
			
			if (itemstack.getItem() == Item.getItemFromBlock(LOTRMod.brick) && itemstack.getItemDamage() == 10)
			{
				LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.craftMithrilDwarvenBrick);
			}
			
			if (itemstack.getItem() == LOTRMod.ancientItem)
			{
				LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.craftAncientItem);
			}
			
			if (itemstack.getItem() == Item.getItemFromBlock(LOTRMod.orcBomb))
			{
				LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.craftOrcBomb);
			}
		}
	}
	
	@SubscribeEvent
	public void onSmelting(ItemSmeltedEvent event)
	{
		EntityPlayer entityplayer = event.player;
		ItemStack itemstack = event.smelting;
		
		if (!entityplayer.worldObj.isRemote)
		{
			if (itemstack.getItem() == LOTRMod.blueDwarfSteel)
			{
				LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.smeltBlueDwarfSteel);
			}
			
			if (itemstack.getItem() == LOTRMod.dwarfSteel)
			{
				LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.smeltDwarfSteel);
			}
			
			if (itemstack.getItem() == LOTRMod.urukSteel)
			{
				LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.smeltUrukSteel);
			}
			
			if (itemstack.getItem() == LOTRMod.orcSteel)
			{
				LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.smeltOrcSteel);
			}
		}
	}
	
	@Override
	public int getBurnTime(ItemStack itemstack)
	{
		if (itemstack.getItem() instanceof ItemBlock && ((ItemBlock)itemstack.getItem()).field_150939_a instanceof LOTRBlockSaplingBase)
		{
			return 100;
		}
		if (itemstack.getItem() == LOTRMod.nauriteGem)
		{
			return 600;
		}
		if (itemstack.getItem() == LOTRMod.mallornStick)
		{
			return 100;
		}
		if (itemstack.getItem() instanceof ItemTool && ((ItemTool)itemstack.getItem()).getToolMaterialName().equals("LOTR_MALLORN"))
		{
			return 200;
		}
		if (itemstack.getItem() instanceof ItemSword && ((ItemSword)itemstack.getItem()).getToolMaterialName().equals("LOTR_MALLORN"))
		{
			return 200;
		}
		if (itemstack.getItem() instanceof ItemHoe && ((ItemHoe)itemstack.getItem()).getToolMaterialName().equals("LOTR_MALLORN"))
		{
			return 200;
		}
		return 0;
	}
	
	@SubscribeEvent
    public void onPlayerLogin(PlayerLoggedInEvent event)
	{
		EntityPlayer entityplayer = event.player;
		
		if (!entityplayer.worldObj.isRemote)
		{
			EntityPlayerMP entityplayermp = (EntityPlayerMP)entityplayer;
			
			Packet loginPacket = LOTRLevelData.getLoginPacket(entityplayer);
			entityplayermp.playerNetServerHandler.sendPacket(loginPacket);
			
			LOTRLevelData.sendAlignmentToAllPlayersInWorld(entityplayer, entityplayer.worldObj);
			LOTRLevelData.sendAllAlignmentsInWorldToPlayer(entityplayer, entityplayer.worldObj);
			
			Packet achievementPacket = LOTRLevelData.getLoginAchievementsPacket(entityplayer);
			if (achievementPacket != null)
			{
				entityplayermp.playerNetServerHandler.sendPacket(achievementPacket);
			}
			
			LOTRLevelData.selectDefaultCapeForPlayer(entityplayer);
			LOTRLevelData.sendCapeToAllPlayersInWorld(entityplayer, entityplayer.worldObj);
			LOTRLevelData.sendAllCapesInWorldToPlayer(entityplayer, entityplayer.worldObj);
			
			Packet waypointsPacket = LOTRWaypoint.getLoginWaypointsPacket(entityplayer);
			if (waypointsPacket != null)
			{
				entityplayermp.playerNetServerHandler.sendPacket(waypointsPacket);
			}
			
			LOTRWaypoint.Custom.sendLoginCustomWaypointsPackets(entityplayermp);
			
			LOTRLevelData.sendTakenAlignmentRewardsToPlayer(entityplayermp);
		}
	}

	@SubscribeEvent
    public void onPlayerChangedDimension(PlayerChangedDimensionEvent event)
	{
		EntityPlayer entityplayer = event.player;
		
		if (!entityplayer.worldObj.isRemote)
		{
			LOTRLevelData.sendAlignmentToAllPlayersInWorld(entityplayer, entityplayer.worldObj);
			LOTRLevelData.sendAllAlignmentsInWorldToPlayer(entityplayer, entityplayer.worldObj);
			LOTRLevelData.sendCapeToAllPlayersInWorld(entityplayer, entityplayer.worldObj);
			LOTRLevelData.sendAllCapesInWorldToPlayer(entityplayer, entityplayer.worldObj);
		}
	}

	@SubscribeEvent
    public void onPlayerRespawn(PlayerRespawnEvent event)
	{
		EntityPlayer entityplayer = event.player;
		World world = entityplayer.worldObj;
		
		if (!world.isRemote && entityplayer instanceof EntityPlayerMP && world instanceof WorldServer)
		{
			EntityPlayerMP entityplayermp = (EntityPlayerMP)entityplayer;
			WorldServer worldserver = (WorldServer)world;
			
			if (entityplayermp.dimension == LOTRMod.idDimension && worldserver.getGameRules().getGameRuleBooleanValue("enableMiddleEarthRespawning"))
			{
				ChunkCoordinates bedLocation = entityplayermp.getBedLocation(entityplayermp.dimension);
				boolean hasBed = bedLocation != null;
				if (hasBed)
				{
					hasBed = entityplayermp.verifyRespawnCoordinates(worldserver, bedLocation, entityplayermp.isSpawnForced(entityplayermp.dimension)) != null;
				}

				Object obj = LOTRLevelData.playerDeathPoints.get(entityplayermp.getUniqueID());
				if (!hasBed && obj instanceof ChunkCoordinates)
				{
					ChunkCoordinates deathPoint = (ChunkCoordinates)obj;
					float respawnThreshold = 2000F * 2000F;
					boolean flag = deathPoint.getDistanceSquaredToChunkCoordinates(worldserver.getSpawnPoint()) > respawnThreshold;
					if (flag)
					{
						double randomDistance = 500D + worldserver.rand.nextDouble() * 1500D;
						float angle = worldserver.rand.nextFloat() * (float)Math.PI * 2F;
						
						int i = deathPoint.posX + (int)(randomDistance * MathHelper.sin(angle));
						int k = deathPoint.posZ + (int)(randomDistance * MathHelper.cos(angle));

						worldserver.theChunkProviderServer.loadChunk(i >> 4, k >> 4);
						int j = worldserver.getHeightValue(i, k);

						entityplayermp.setLocationAndAngles(i + 0.5D, j, k + 0.5D, entityplayermp.rotationYaw, entityplayermp.rotationPitch);
						entityplayermp.playerNetServerHandler.setPlayerLocation(i + 0.5D, j, k + 0.5D, entityplayermp.rotationYaw, entityplayermp.rotationPitch);
					}
				}
			}
		}
	}
	
	@SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event)
	{
		Entity entity = event.entity;
		World world = entity.worldObj;
		
		if (!world.isRemote && entity instanceof EntityCreature)
		{
			EntityCreature entitycreature = (EntityCreature)entity;
			String s = EntityList.getEntityString(entitycreature);
			Object obj = LOTREntityRegistry.registeredNPCs.get(s);
			if (obj != null)
			{
				RegistryInfo info = (RegistryInfo)obj;
				if (info.shouldTargetEnemies)
				{
					LOTREntityNPC.addTargetTasks(entitycreature, 100, LOTREntityAINearestAttackableTargetBasic.class);
				}
			}
		}
	}
	
	@SubscribeEvent
    public void onEntityAttackedByPlayer(AttackEntityEvent event)
	{
		Entity entity = event.target;
		World world = entity.worldObj;
		EntityPlayer entityplayer = event.entityPlayer;
		
		if (!world.isRemote && entity instanceof LOTREntityWargskinRug)
		{
			((LOTREntityWargskinRug)entity).dropRugAsItem(entityplayer.capabilities.isCreativeMode);
		}
		
		if (!world.isRemote && entity instanceof LOTREntityTraderRespawn && entityplayer.capabilities.isCreativeMode)
		{
			LOTREntityTraderRespawn traderRespawn = (LOTREntityTraderRespawn)entity;
			if (traderRespawn.getScale() >= 40)
			{
				traderRespawn.onBreak();
			}
		}
	}
	
	@SubscribeEvent
	public void onUseBonemeal(BonemealEvent event)
	{
		World world = event.world;
		int i = event.x;
		int j = event.y;
		int k = event.z;
		if (!world.isRemote)
		{
			if (event.block instanceof LOTRBlockSaplingBase)
			{
				LOTRBlockSaplingBase blockSapling = (LOTRBlockSaplingBase)event.block;
				if (world.rand.nextFloat() < 0.45D)
				{
					blockSapling.incrementGrowth(world, i, j, k, world.rand);
				}
				event.setResult(Result.ALLOW);
			}
		}
	}
	
	public static boolean isProtectedByBanner(World world, int i, int j, int k, EntityLivingBase entity)
	{
		String protector = null;
		double range = LOTREntityBanner.PROTECTION_RANGE;
		double distanceToBanner = 0D;
		boolean playerHoldingBanner = false;
		
		if (entity instanceof EntityPlayer)
		{
			EntityPlayer entityplayer = (EntityPlayer)entity;
			if (entityplayer.getHeldItem().getItem().equals(LOTRMod.banner))
			{
				playerHoldingBanner = true;
			}
		}
		if (playerHoldingBanner)
		{
			range *= 2D;
		}
		
		List banners = world.getEntitiesWithinAABB(LOTREntityBanner.class, AxisAlignedBB.getBoundingBox(i, j, k, i + 1, j + 1, k + 1).expand(range, range, range));
		if (!banners.isEmpty())
		{
			bannerSearch:
			for (int l = 0; l < banners.size(); l++)
			{
				LOTREntityBanner banner = (LOTREntityBanner)banners.get(l);
				if (banner.isProtectingTerritory())
				{
					LOTRFaction faction = banner.getBannerFaction();
					if (entity instanceof EntityPlayer)
					{
						EntityPlayer entityplayer = (EntityPlayer)entity;
						int alignment = LOTRLevelData.getAlignment(entityplayer, faction);
						if (alignment <= 0)
						{
							protector = faction.factionName();
							distanceToBanner = banner.getDistanceToEntity(entityplayer);
							break bannerSearch;
						}
					}
					else if (LOTRMod.getNPCFaction(entity).isEnemy(faction))
					{
						protector = faction.factionName();
						distanceToBanner = banner.getDistanceToEntity(entity);
						break bannerSearch;
					}
				}
			}
		}
		
		if (protector != null)
		{
			if (entity instanceof EntityPlayer)
			{
				if (playerHoldingBanner && distanceToBanner > range)
				{
					((EntityPlayer)entity).addChatMessage(new ChatComponentTranslation("chat.lotr.protectedLandBanner", new Object[] {protector}));
				}
				else
				{
					((EntityPlayer)entity).addChatMessage(new ChatComponentTranslation("chat.lotr.protectedLand", new Object[] {protector}));
				}
			}
			return true;
		}
		else
		{
			return false;
		}
	}
	
	@SubscribeEvent
	public void onBlockInteract(PlayerInteractEvent event)
	{
		EntityPlayer entityplayer = event.entityPlayer;
		World world = entityplayer.worldObj;
		ItemStack itemstack = entityplayer.inventory.getCurrentItem();
		int i = event.x;
		int j = event.y;
		int k = event.z;
		int side = event.face;
		
		if (!world.canMineBlock(entityplayer, i, j, k))
		{
			return;
		}
		if (!entityplayer.canPlayerEdit(i, j, k, side, itemstack))
		{
			return;
		}
		
		if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK)
		{
			if (!world.isRemote && isProtectedByBanner(world, i, j, k, entityplayer))
			{
				event.setCanceled(true);
				return;
			}
			
			if (world.getBlock(i, j, k) == Blocks.flower_pot && world.getBlockMetadata(i, j, k) == 0 && itemstack != null && LOTRBlockFlowerPot.canAcceptPlant(itemstack))
			{
				LOTRMod.proxy.placeFlowerInPot(world, i, j, k, side, itemstack);
				if (!entityplayer.capabilities.isCreativeMode)
				{
					itemstack.stackSize--;
				}
				event.setCanceled(true);
				return;
			}
			
			if (itemstack != null && itemstack.getItem() == LOTRMod.mug && world.getBlock(i, j, k) == Blocks.cauldron && world.getBlockMetadata(i, j, k) > 0)
			{
				LOTRMod.proxy.fillMugFromCauldron(world, i, j, k, side, itemstack);
				itemstack.stackSize--;
				if (itemstack.stackSize <= 0)
				{
					entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, new ItemStack(LOTRMod.mugWater));
				}
				else if (!entityplayer.inventory.addItemStackToInventory(new ItemStack(LOTRMod.mugWater)))
				{
					entityplayer.dropPlayerItemWithRandomChoice(new ItemStack(LOTRMod.mugWater), false);
				}
				event.setCanceled(true);
				return;
			}
		}
	}
	
	@SubscribeEvent
	public void onBlockBreak(BlockEvent.BreakEvent event)
	{
		EntityPlayer entityplayer = event.getPlayer();
		Block block = event.block;
		World world = event.world;
		int i = event.x;
		int j = event.y;
		int k = event.z;
		
		if (!world.isRemote && isProtectedByBanner(world, i, j, k, entityplayer))
		{
			event.setCanceled(true);
			return;
		}
		
		if (!world.isRemote && entityplayer != null && !entityplayer.capabilities.isCreativeMode && block.isWood(world, i, j, k))
		{
			List trees = world.getEntitiesWithinAABB(LOTREntityTree.class, AxisAlignedBB.getBoundingBox(i, j, k, i + 1, j + 1, k + 1).expand(16D, 16D, 16D));
			if (!trees.isEmpty())
			{
				boolean sentMessage = false;
				boolean penalty = false;
				for (int l = 0; l < trees.size(); l++)
				{
					LOTREntityTree tree = (LOTREntityTree)trees.get(l);
					
					if (tree.hiredNPCInfo.isActive && tree.hiredNPCInfo.getHiringPlayer() == entityplayer)
					{
						tree.hiredNPCInfo.dismissUnit();
					}
					
					tree.setAttackTarget(entityplayer);
					
					if (tree instanceof LOTREntityEnt && !sentMessage)
					{
						entityplayer.addChatMessage(LOTRSpeech.getNamedSpeechForPlayer(tree, "ent_defendTrees", entityplayer));
						tree.markNPCSpoken();
						sentMessage = true;
					}
					
					if (world.getBiomeGenForCoords(i, k) instanceof LOTRBiomeGenFangorn)
					{
						if (!penalty)
						{
							LOTRLevelData.addAlignment(entityplayer, LOTRAlignmentValues.FANGORN_TREE_PENALTY, LOTRFaction.FANGORN, i + 0.5D, j + 0.5D, k + 0.5D);
							penalty = true;
						}
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onItemPickup(EntityItemPickupEvent event)
	{
		EntityPlayer entityplayer = event.entityPlayer;
		ItemStack itemstack = event.item.getEntityItem();
		
		if (!entityplayer.worldObj.isRemote)
		{
			if (itemstack.stackSize > 0)
			{
				pouchSearchingLoop:
				for (int i = 0; i < entityplayer.inventory.getSizeInventory(); i++)
				{
					ItemStack itemInSlot = entityplayer.inventory.getStackInSlot(i);
					if (itemInSlot != null && itemInSlot.getItem() == LOTRMod.pouch)
					{
						LOTRItemPouch.tryAddItemToPouch(itemInSlot, itemstack);
						if (itemstack.stackSize <= 0)
						{
							break pouchSearchingLoop;
						}
					}
				}
				if (itemstack.stackSize <= 0)
				{
					event.setResult(Event.Result.ALLOW);
				}
			}
			
			if (itemstack.getItem() == Item.getItemFromBlock(LOTRMod.athelas))
			{
				LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.findAthelas);
			}
			
			if (itemstack.getItem() == Item.getItemFromBlock(LOTRMod.clover) && itemstack.getItemDamage() == 1)
			{
				LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.findFourLeafClover);
			}
		}
	}
	
	@SubscribeEvent
	public void onEntityUpdate(LivingEvent.LivingUpdateEvent event)
	{
		EntityLivingBase entity = event.entityLiving;
		World world = entity.worldObj;
		
		if (!world.isRemote && world.getGameRules().getGameRuleBooleanValue("doMobSpawning") && entity.isEntityAlive() && entity.isInWater() && entity.ridingEntity == null)
		{
			boolean flag = true;
			if (entity instanceof EntityPlayer && ((EntityPlayer)entity).capabilities.isCreativeMode)
			{
				flag = false;
			}
			
			if (entity instanceof EntityWaterMob || entity instanceof LOTREntityMarshWraith)
			{
				flag = false;
			}
			
			if (flag)
			{
				int i = MathHelper.floor_double(entity.posX);
				int k = MathHelper.floor_double(entity.posZ);
				int j = world.getTopSolidOrLiquidBlock(i, k);
				
				while (world.getBlock(i, j + 1, k).getMaterial().isLiquid() || world.getBlock(i, j + 1, k).getMaterial().isSolid())
				{
					j++;
				}
				
				if (j - entity.boundingBox.minY < 2D && world.getBlock(i, j, k).getMaterial() == Material.water && world.getBiomeGenForCoords(i, k) instanceof LOTRBiomeGenDeadMarshes)
				{
					List nearbyWraiths = world.getEntitiesWithinAABB(LOTREntityMarshWraith.class, entity.boundingBox.expand(16D, 8D, 16D));
					boolean anyNearbyWraiths = false;
					for (int l = 0; l < nearbyWraiths.size(); l++)
					{
						LOTREntityMarshWraith wraith = (LOTREntityMarshWraith)nearbyWraiths.get(l);
						if (wraith.getAttackTarget() == entity && wraith.getDeathFadeTime() == 0)
						{
							anyNearbyWraiths = true;
							break;
						}
					}
					
					if (!anyNearbyWraiths)
					{
						LOTREntityMarshWraith wraith = new LOTREntityMarshWraith(world);
						double d = i + 0.5D - 3D + (double)world.rand.nextInt(7);
						double d2 = k + 0.5D - 3D + (double)world.rand.nextInt(7);
						double d1 = world.getTopSolidOrLiquidBlock(MathHelper.floor_double(d), MathHelper.floor_double(d2));
						wraith.setLocationAndAngles(d, d1, d2, world.rand.nextFloat() * 360F, 0F);
						world.spawnEntityInWorld(wraith);
						wraith.setAttackTarget(entity);
						wraith.attackTargetUUID = entity.getPersistentID();
						world.playSoundAtEntity(wraith, "lotr:wraith.spawn", 1F, 0.7F + (world.rand.nextFloat() * 0.6F));
					}
				}
			}
		}
		
		if (!world.isRemote && world.getGameRules().getGameRuleBooleanValue("doMobSpawning") && entity.isEntityAlive())
		{
			float f = 0F;
			int shirriffs = 0;
			if (LOTRMod.getNPCFaction(entity).isEnemy(LOTRFaction.HOBBIT))
			{
				float health = entity.getMaxHealth() + (float)entity.getTotalArmorValue();
				f = health * 2.5F;
				int i = (int)(health / 15F);
				shirriffs = 2 + world.rand.nextInt(i + 1);
			}
			else if (entity instanceof EntityPlayer)
			{
				EntityPlayer entityplayer = (EntityPlayer)entity;
				int alignment = LOTRLevelData.getAlignment(entityplayer, LOTRFaction.HOBBIT);
				if (!entityplayer.capabilities.isCreativeMode && alignment < 0)
				{
					f = (float)alignment * -1F;
					int i = (int)(f / 50F);
					shirriffs = 2 + world.rand.nextInt(i + 1);
				}
			}
			
			if (f > 0F)
			{
				if (f > 500F)
				{
					f = 500F;
				}
				int chance = (int)(1000000F / f);
				if (shirriffs > 5)
				{
					shirriffs = 5;
				}
				
				int i = MathHelper.floor_double(entity.posX);
				int k = MathHelper.floor_double(entity.posZ);
				int j = world.getTopSolidOrLiquidBlock(i, k);
				
				if (world.rand.nextInt(chance) == 0 && world.getBiomeGenForCoords(i, k) instanceof LOTRBiomeGenShire)
				{
					List nearbyShirriffs = world.getEntitiesWithinAABB(LOTREntityHobbitShirriff.class, entity.boundingBox.expand(12D, 6D, 12D));
					if (nearbyShirriffs.isEmpty())
					{
						boolean sentMessage = false;
						boolean playedHorn = false;
						shirriffSpawningLoop:
						for (int l = 0; l < shirriffs; l++)
						{
							LOTREntityHobbitShirriff shirriff = new LOTREntityHobbitShirriff(world);
							for (int l1 = 0; l1 < 16; l1++)
							{
								int i1 = i - 8 + world.rand.nextInt(17);
								int k1 = k - 8 + world.rand.nextInt(17);
								int j1 = world.getTopSolidOrLiquidBlock(i1, k1);
								if (world.getBlock(i1, j1 - 1, k1).isSideSolid(world, i1, j1 - 1, k1, ForgeDirection.UP) && !world.getBlock(i1, j1, k1).isNormalCube() && !world.getBlock(i1, j1 + 1, k1).isNormalCube())
								{
									shirriff.setLocationAndAngles(i1 + 0.5D, j1, k1 + 0.5D, 0F, 0F);
									if (shirriff.getCanSpawnHere())
									{
										shirriff.onSpawnWithEgg(null);
										world.spawnEntityInWorld(shirriff);
										shirriff.setAttackTarget(entity);
										if (!sentMessage && entity instanceof EntityPlayer)
										{
											EntityPlayer entityplayer = (EntityPlayer)entity;
											entityplayer.addChatMessage(LOTRSpeech.getNamedSpeechForPlayer(shirriff, "hobbitShirriff_hostile", entityplayer));
											shirriff.markNPCSpoken();
											sentMessage = true;
										}
										if (!playedHorn)
										{
											world.playSoundAtEntity(shirriff, "lotr:item.horn", 2F, 2F);
											playedHorn = true;
										}
										continue shirriffSpawningLoop;
									}
								}
							}
						}
					}
				}
			}
		}
		
		if (!world.isRemote && entity.isEntityAlive() && entity.isInWater() && entity.ridingEntity == null && world.getWorldTime() % 10L == 0L)
		{
			boolean flag = true;
			if (entity instanceof EntityPlayer && ((EntityPlayer)entity).capabilities.isCreativeMode)
			{
				flag = false;
			}
			
			if (entity instanceof LOTREntityMirkwoodSpider)
			{
				flag = false;
			}
			
			if (flag)
			{
				int i = MathHelper.floor_double(entity.posX);
				int k = MathHelper.floor_double(entity.posZ);
				BiomeGenBase biome = world.getBiomeGenForCoords(i, k);
				if (biome instanceof LOTRBiomeGenMirkwood && ((LOTRBiomeGenMirkwood)biome).corrupted)
				{
					entity.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 600, 1));
					entity.addPotionEffect(new PotionEffect(Potion.digSlowdown.id, 600, 1));
					entity.addPotionEffect(new PotionEffect(Potion.weakness.id, 600));
					entity.addPotionEffect(new PotionEffect(Potion.blindness.id, 600));
				}
			}
		}
		
		if (!world.isRemote && entity.isEntityAlive() && world.getWorldTime() % 10L == 0L)
		{
			boolean wearingAllWoodElvenScout = true;
			for (int i = 0; i < 4; i++)
			{
				ItemStack armour = entity.getEquipmentInSlot(i + 1);
				if (!(armour != null && armour.getItem() instanceof ItemArmor && ((ItemArmor)armour.getItem()).getArmorMaterial() == LOTRMod.armorWoodElvenScout))
				{
					wearingAllWoodElvenScout = false;
					break;
				}
			}
			
			if (wearingAllWoodElvenScout)
			{
				entity.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, 100, 1));
			}
		}
		
		if (!world.isRemote && entity.isEntityAlive() && world.getWorldTime() % 20L == 0L)
		{
			boolean flag = true;
			if (entity instanceof LOTREntityNPC)
			{
				flag = ((LOTREntityNPC)entity).isImmuneToFrost;
			}
			if (entity instanceof EntityPlayer)
			{
				flag = !((EntityPlayer)entity).capabilities.isCreativeMode;
			}
			
			if (flag)
			{
				int i = MathHelper.floor_double(entity.posX);
				int j = MathHelper.floor_double(entity.boundingBox.minY);
				int k = MathHelper.floor_double(entity.posZ);
				BiomeGenBase biome = world.getBiomeGenForCoords(i, k);
				if (biome instanceof LOTRBiomeGenForodwaith && (world.canBlockSeeTheSky(i, j, k) || entity.isInWater()) && world.getSavedLightValue(EnumSkyBlock.Block, i, j, k) < 10)
				{
					int frostChance = 50;
					int frostProtection = 0;
					
					for (int l = 0; l < 4; l++)
					{
						ItemStack armour = entity.getEquipmentInSlot(l + 1);
						if (armour != null && armour.getItem() instanceof ItemArmor)
						{
							ArmorMaterial material = ((ItemArmor)armour.getItem()).getArmorMaterial();
							if (material.customCraftingMaterial == Items.leather)
							{
								frostProtection += 50;
							}
							else if (material.customCraftingMaterial == LOTRMod.wargFur)
							{
								frostProtection += 100;
							}
						}
					}
					
					frostChance += frostProtection;
					
					if (world.isRaining())
					{
						frostChance /= 3;
					}
					
					if (entity.isInWater())
					{
						frostChance /= 20;
					}
					
					if (frostChance < 1)
					{
						frostChance = 1;
					}
					
					if (world.rand.nextInt(frostChance) == 0)
					{
						boolean attacked = entity.attackEntityFrom(LOTRBiomeGenForodwaith.frost, 1F);
						if (flag && entity instanceof EntityPlayer)
						{
							((EntityPlayerMP)entity).playerNetServerHandler.sendPacket(new S3FPacketCustomPayload("lotr.frost", Unpooled.buffer(0)));
						}
					}
				}
			}
		}
		
		if (!world.isRemote && entity.isEntityAlive() && world.getWorldTime() % 20L == 0L)
		{
			boolean flag = true;
			if (entity instanceof LOTREntityNPC)
			{
				flag = true;
			}
			if (entity instanceof EntityPlayer)
			{
				flag = !((EntityPlayer)entity).capabilities.isCreativeMode;
			}
			if (entity instanceof LOTRBiomeGenNearHarad.ImmuneToHeat)
			{
				flag = false;
			}
			
			if (flag)
			{
				int i = MathHelper.floor_double(entity.posX);
				int j = MathHelper.floor_double(entity.boundingBox.minY);
				int k = MathHelper.floor_double(entity.posZ);
				BiomeGenBase biome = world.getBiomeGenForCoords(i, k);
				if (biome instanceof LOTRBiomeGenNearHarad && !entity.isInWater() && world.canBlockSeeTheSky(i, j, k) && world.isDaytime())
				{
					int burnChance = 100;
					int burnProtection = 0;
					
					for (int l = 0; l < 4; l++)
					{
						ItemStack armour = entity.getEquipmentInSlot(l + 1);
						if (armour != null && armour.getItem() instanceof ItemArmor)
						{
							ArmorMaterial material = ((ItemArmor)armour.getItem()).getArmorMaterial();
							if (material.customCraftingMaterial == Items.leather)
							{
								burnProtection += 80;
							}
						}
					}
					
					burnChance += burnProtection;
					
					if (burnChance < 1)
					{
						burnChance = 1;
					}
					
					if (world.rand.nextInt(burnChance) == 0)
					{
						boolean attacked = entity.attackEntityFrom(DamageSource.onFire, 1F);
						if (flag && entity instanceof EntityPlayer)
						{
							((EntityPlayerMP)entity).playerNetServerHandler.sendPacket(new S3FPacketCustomPayload("lotr.burn", Unpooled.buffer(0)));
						}
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onEntityInteract(EntityInteractEvent event)
	{
		EntityPlayer entityplayer = event.entityPlayer;
		ItemStack itemstack = entityplayer.inventory.getCurrentItem();
		Entity entity = event.target;
		
		if ((entity instanceof EntityCow || entity instanceof LOTREntityZebra) && itemstack != null && itemstack.getItem() == LOTRMod.mug && !entityplayer.capabilities.isCreativeMode)
		{
			itemstack.stackSize--;
			if (itemstack.stackSize <= 1)
			{
				entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, new ItemStack(LOTRMod.mugMilk));
			}
			else if (!entityplayer.inventory.addItemStackToInventory(new ItemStack(LOTRMod.mugMilk)))
			{
				entityplayer.dropPlayerItemWithRandomChoice(new ItemStack(LOTRMod.mugMilk), false);
			}
			event.setCanceled(true);
			return;
		}
		
		if (entity instanceof LOTREntityOrc)
		{
			LOTREntityOrc orc = (LOTREntityOrc)entity;
			
			if (orc.isBombardier && orc.hiredNPCInfo.getHiringPlayer() == entityplayer)
			{
				ItemStack heldItem = orc.getEquipmentInSlot(0);
				if (heldItem == null || heldItem.getItem() != LOTRMod.orcTorchItem)
				{
					if (itemstack != null && itemstack.getItem() == Item.getItemFromBlock(LOTRMod.orcBomb))
					{
						orc.setCurrentItemOrArmor(0, new ItemStack(LOTRMod.orcTorchItem));
						
						int meta = itemstack.getItemDamage();
						orc.setBombStrength(meta);
						
						if (!entityplayer.capabilities.isCreativeMode)
						{
							itemstack.stackSize--;
							if (itemstack.stackSize <= 0)
							{
								entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, null);
							}
						}
						
						event.setCanceled(true);
						return;
					}
				}
			}
		}
		
		if (entity instanceof LOTRTradeable && ((LOTRTradeable)entity).canTradeWith(entityplayer))
		{
			if (entity instanceof LOTRUnitTradeable)
			{
				entityplayer.openGui(LOTRMod.instance, LOTRCommonProxy.GUI_ID_TRADE_UNIT_TRADE_INTERACT, entityplayer.worldObj, entity.getEntityId(), 0, 0);
			}
			else
			{
				entityplayer.openGui(LOTRMod.instance, LOTRCommonProxy.GUI_ID_TRADE_INTERACT, entityplayer.worldObj, entity.getEntityId(), 0, 0);
			}
			event.setCanceled(true);
			return;
		}
		
		if (entity instanceof LOTRUnitTradeable && ((LOTRUnitTradeable)entity).canTradeWith(entityplayer))
		{
			entityplayer.openGui(LOTRMod.instance, LOTRCommonProxy.GUI_ID_UNIT_TRADE_INTERACT, entityplayer.worldObj, entity.getEntityId(), 0, 0);
			event.setCanceled(true);
			return;
		}
		
		if (entity instanceof LOTREntityNPC && ((LOTREntityNPC)entity).hiredNPCInfo.getHiringPlayer() == entityplayer)
		{
			entityplayer.openGui(LOTRMod.instance, LOTRCommonProxy.GUI_ID_HIRED_INTERACT, entityplayer.worldObj, entity.getEntityId(), 0, 0);
			event.setCanceled(true);
			return;
		}
	}
	
	@SubscribeEvent
	public void onEntitySetAttackTarget(LivingSetAttackTargetEvent event)
	{
		if (event.entityLiving instanceof EntityLiving && event.target instanceof LOTREntityRanger && ((LOTREntityRanger)event.target).isRangerSneaking())
		{
			((EntityLiving)event.entityLiving).setAttackTarget(null);
		}
		if (event.entityLiving instanceof EntityLiving && event.target instanceof LOTREntityHuornBase && !((LOTREntityHuornBase)event.target).isHuornActive())
		{
			((EntityLiving)event.entityLiving).setAttackTarget(null);
		}
	}
	
	@SubscribeEvent
	public void onEntityAttacked(LivingAttackEvent event)
	{
		EntityLivingBase entity = event.entityLiving;
		EntityLivingBase attacker = event.source.getEntity() instanceof EntityLivingBase ? (EntityLivingBase)event.source.getEntity() : null;
		World world = entity.worldObj;
		
		if (attacker instanceof EntityPlayer && !LOTRMod.canPlayerAttackEntity((EntityPlayer)attacker, entity, true))
		{
			event.setCanceled(true);
			return;
		}

		if (attacker instanceof EntityCreature && !LOTRMod.canNPCAttackEntity((EntityCreature)attacker, entity))
		{
			event.setCanceled(true);
			return;
		}
		
		if (event.source instanceof EntityDamageSourceIndirect)
		{
			boolean wearingAllGalvorn = true;
			for (int i = 0; i < 4; i++)
			{
				ItemStack armour = entity.getEquipmentInSlot(i + 1);
				if (!(armour != null && armour.getItem() instanceof ItemArmor && ((ItemArmor)armour.getItem()).getArmorMaterial() == LOTRMod.armorGalvorn))
				{
					wearingAllGalvorn = false;
					break;
				}
			}
			
			if (wearingAllGalvorn)
			{
				event.setCanceled(true);
				if (!world.isRemote && entity instanceof EntityPlayer)
				{
					((EntityPlayer)entity).inventory.damageArmor(event.ammount + 1);
					LOTRLevelData.addAchievement((EntityPlayer)entity, LOTRAchievement.wearFullGalvorn);
				}
				return;
			}
			
			if (!world.isRemote && entity instanceof EntityPlayer && event.source.getSourceOfDamage() instanceof LOTREntitySpear)
			{
				LOTREntitySpear spear = (LOTREntitySpear)event.source.getSourceOfDamage();
				if (spear.getItemID() == Item.getIdFromItem(LOTRMod.spearOrc))
				{
					ItemStack chestplate = entity.getEquipmentInSlot(3);
					if (chestplate != null && chestplate.getItem() == LOTRMod.bodyMithril)
					{
						LOTRLevelData.addAchievement((EntityPlayer)entity, LOTRAchievement.hitByOrcSpear);
					}
				}
			}
		}
		
		if (attacker != null && event.source.getSourceOfDamage() == attacker)
		{
			boolean wearingAllMorgul = true;
			for (int i = 0; i < 4; i++)
			{
				ItemStack armour = entity.getEquipmentInSlot(i + 1);
				if (!(armour != null && armour.getItem() instanceof ItemArmor && ((ItemArmor)armour.getItem()).getArmorMaterial() == LOTRMod.armorMorgul))
				{
					wearingAllMorgul = false;
					break;
				}
			}
			
			if (wearingAllMorgul && !world.isRemote)
			{
				attacker.addPotionEffect(new PotionEffect(Potion.wither.id, 80));
				if (entity instanceof EntityPlayer)
				{
					LOTRLevelData.addAchievement((EntityPlayer)entity, LOTRAchievement.wearFullMorgul);
				}
			}
		}
		
		if (attacker != null && event.source.getSourceOfDamage() == attacker)
		{
			ItemStack weapon = attacker.getHeldItem();
			if (weapon != null)
			{
				String materialName = null;
				if (weapon.getItem() instanceof ItemTool)
				{
					materialName = ((ItemTool)weapon.getItem()).getToolMaterialName();
				}
				else if (weapon.getItem() instanceof ItemSword)
				{
					materialName = ((ItemSword)weapon.getItem()).getToolMaterialName();
				}
				
				if (materialName != null)
				{
					if (materialName.equals(LOTRMod.toolMorgul.toString()) && !world.isRemote)
					{
						entity.addPotionEffect(new PotionEffect(Potion.wither.id, 160));
						ItemStack[] equipment = entity.getLastActiveItems();
						for (int i = 0; i < equipment.length; i++)
						{
							ItemStack itemstack = equipment[i];
							if (itemstack != null)
							{
								itemstack.damageItem(4, entity);
							}
						}
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onEntityDeath(LivingDeathEvent event)
	{
		EntityLivingBase entity = event.entityLiving;
		World world = entity.worldObj;
		DamageSource source = event.source;
		
		if (!world.isRemote && entity instanceof EntityPlayer)
		{
			EntityPlayer entityplayer = (EntityPlayer)entity;
			int i = MathHelper.floor_double(entityplayer.posX);
			int j = MathHelper.floor_double(entityplayer.posY);
			int k = MathHelper.floor_double(entityplayer.posZ);
			LOTRLevelData.playerDeathPoints.put(entityplayer.getUniqueID(), new ChunkCoordinates(i, j, k));
		}
		
		if (!world.isRemote)
		{
			EntityPlayer entityplayer = null;
			if (source.getEntity() instanceof EntityPlayer)
			{
				entityplayer = (EntityPlayer)source.getEntity();
			}
			else if (entity.func_94060_bK() instanceof EntityPlayer)
			{
				entityplayer = (EntityPlayer)entity.func_94060_bK();
			}
			if (entityplayer != null)
			{
				LOTRAlignmentValues.Bonus alignmentBonus = null;
				if (entity instanceof LOTREntityNPC)
				{
					LOTREntityNPC npc = (LOTREntityNPC)entity;
					alignmentBonus = new LOTRAlignmentValues.Bonus(npc.getAlignmentBonus(), npc.getEntityClassName());
					alignmentBonus.needsTranslation = true;
				}
				else
				{
					String s = EntityList.getEntityString(entity);
					Object obj = LOTREntityRegistry.registeredNPCs.get(s);
					if (obj != null)
					{
						RegistryInfo info = (RegistryInfo)obj;
						alignmentBonus = info.alignmentBonus;
					}
				}
				
				if (alignmentBonus != null)
				{
					alignmentBonus.isKill = true;
					LOTRLevelData.addAlignment(entityplayer, alignmentBonus, LOTRMod.getNPCFaction(entity), entity);
				}
				
				if (!entityplayer.capabilities.isCreativeMode && LOTRMod.getNPCFaction(entity) != LOTRFaction.UNALIGNED)
				{
					List nearbyAlliedNPCs = world.getEntitiesWithinAABB(EntityLiving.class, entity.boundingBox.expand(8D, 8D, 8D));
					for (int i = 0; i < nearbyAlliedNPCs.size(); i++)
					{
						EntityLiving npc = (EntityLiving)nearbyAlliedNPCs.get(i);
						if (npc instanceof LOTREntityNPC && ((LOTREntityNPC)npc).hiredNPCInfo.isActive)
						{
							continue;
						}
						
						if (!LOTRMod.getNPCFaction(entity).isAllied(LOTRMod.getNPCFaction(npc)))
						{
							continue;
						}
						
						npc.setAttackTarget(entityplayer);
					}
				}
			}
		}
		
		if (!world.isRemote && source.getEntity() instanceof EntityPlayer && source.getSourceOfDamage() != null && source.getSourceOfDamage().getClass() == LOTREntitySpear.class)
		{
			EntityPlayer entityplayer = (EntityPlayer)source.getEntity();
			if (entity != entityplayer && entityplayer.getDistanceSqToEntity(entity) >= 2500D)
			{
				LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.useSpearFromFar);
			}
		}

		if (!world.isRemote && entity instanceof LOTREntityButterfly && source.getEntity() instanceof EntityPlayer)
		{
			EntityPlayer entityplayer = (EntityPlayer)source.getEntity();
			LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.killButterfly);
		}
		
		if (!world.isRemote && entity instanceof EntityHorse && source.getEntity() instanceof EntityPlayer)
		{
			EntityPlayer entityplayer = (EntityPlayer)source.getEntity();
			if (!entityplayer.capabilities.isCreativeMode)
			{
				List rohirrimList = world.getEntitiesWithinAABB(LOTREntityRohirrim.class, entityplayer.boundingBox.expand(16D, 16D, 16D));
				if (!rohirrimList.isEmpty())
				{
					boolean sentMessage = false;
					boolean penalty = false;
					for (int l = 0; l < rohirrimList.size(); l++)
					{
						LOTREntityRohirrim rohirrim = (LOTREntityRohirrim)rohirrimList.get(l);
						if (!rohirrim.hiredNPCInfo.isActive || rohirrim.hiredNPCInfo.getHiringPlayer() != entityplayer)
						{
							rohirrim.setAttackTarget(entityplayer);
							if (!sentMessage)
							{
								entityplayer.addChatMessage(LOTRSpeech.getNamedSpeechForPlayer(rohirrim, "rohirrim_avengeHorse", entityplayer));
								rohirrim.markNPCSpoken();
								sentMessage = true;
							}
							
							if (!penalty)
							{
								LOTRLevelData.addAlignment(entityplayer, LOTRAlignmentValues.ROHAN_HORSE_PENALTY, LOTRFaction.ROHAN, entity);
								penalty = true;
							}
						}
					}
				}
			}
		}
		
		if (!world.isRemote)
		{
			EntityPlayer attackingPlayer = null;
			LOTREntityNPC attackingHiredUnit = null;
			if (source.getEntity() instanceof EntityPlayer)
			{
				attackingPlayer = (EntityPlayer)source.getEntity();
			}
			else if (source.getEntity() instanceof LOTREntityNPC)
			{
				LOTREntityNPC npc = (LOTREntityNPC)source.getEntity();
				if (npc.hiredNPCInfo.isActive && npc.hiredNPCInfo.getHiringPlayer() != null)
				{
					attackingPlayer = npc.hiredNPCInfo.getHiringPlayer();
					attackingHiredUnit = npc;
				}
			}
			
			if (attackingPlayer != null)
			{
				boolean isFoe = LOTRLevelData.getAlignment(attackingPlayer, LOTRMod.getNPCFaction(entity)) < 0;
				
				if (isFoe)
				{
					if (attackingHiredUnit != null)
					{
						if (attackingHiredUnit instanceof LOTREntityMordorSpider && entity instanceof LOTREntityMirkwoodSpider)
						{
							LOTRLevelData.addAchievement(attackingPlayer, LOTRAchievement.killMirkwoodSpiderMordorSpider);
						}
						
						if (attackingHiredUnit instanceof LOTREntityWargBombardier)
						{
							LOTRLevelData.addAchievement(attackingPlayer, LOTRAchievement.hireWargBombardier);
						}
						
						if (attackingHiredUnit instanceof LOTREntityOlogHai)
						{
							LOTRLevelData.addAchievement(attackingPlayer, LOTRAchievement.hireOlogHai);
						}
					}
					else
					{
						if (attackingPlayer.isPotionActive(Potion.confusion.id))
						{
							LOTRLevelData.addAchievement(attackingPlayer, LOTRAchievement.killWhileDrunk);
						}
						
						if (entity instanceof LOTREntityOrc)
						{
							LOTREntityOrc orc = (LOTREntityOrc)entity;
							if (orc.isBombardier && orc.getEquipmentInSlot(0) != null && orc.getEquipmentInSlot(0).getItem() == LOTRMod.orcTorchItem)
							{
								LOTRLevelData.addAchievement(attackingPlayer, LOTRAchievement.killBombardier);
							}
						}
						
						if (source.getSourceOfDamage() instanceof LOTREntityCrossbowBolt)
						{
							LOTRLevelData.addAchievement(attackingPlayer, LOTRAchievement.useCrossbow);
						}
						
						if (source.getSourceOfDamage() instanceof LOTREntityThrowingAxe)
						{
							LOTREntityThrowingAxe axe = (LOTREntityThrowingAxe)source.getSourceOfDamage();
							if (axe.getItemID() == Item.getIdFromItem(LOTRMod.throwingAxeDwarven))
							{
								LOTRLevelData.addAchievement(attackingPlayer, LOTRAchievement.useDwarvenThrowingAxe);
							}
						}
					}
				}
			}
		}
	}
}
