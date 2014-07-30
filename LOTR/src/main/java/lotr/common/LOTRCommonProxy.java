package lotr.common;

import lotr.client.gui.LOTRGui;
import lotr.client.gui.LOTRGuiAlloyForge;
import lotr.client.gui.LOTRGuiAngmarTable;
import lotr.client.gui.LOTRGuiArmorStand;
import lotr.client.gui.LOTRGuiBarrel;
import lotr.client.gui.LOTRGuiBlueDwarvenTable;
import lotr.client.gui.LOTRGuiCamel;
import lotr.client.gui.LOTRGuiDunlendingTable;
import lotr.client.gui.LOTRGuiDwarvenTable;
import lotr.client.gui.LOTRGuiElvenTable;
import lotr.client.gui.LOTRGuiGollum;
import lotr.client.gui.LOTRGuiGondorianTable;
import lotr.client.gui.LOTRGuiHighElvenTable;
import lotr.client.gui.LOTRGuiHiredFarmerInventory;
import lotr.client.gui.LOTRGuiHiredInteract;
import lotr.client.gui.LOTRGuiHobbitOven;
import lotr.client.gui.LOTRGuiHornSelect;
import lotr.client.gui.LOTRGuiMobSpawner;
import lotr.client.gui.LOTRGuiMorgulTable;
import lotr.client.gui.LOTRGuiNearHaradTable;
import lotr.client.gui.LOTRGuiPouch;
import lotr.client.gui.LOTRGuiRohirricTable;
import lotr.client.gui.LOTRGuiTrade;
import lotr.client.gui.LOTRGuiTradeInteract;
import lotr.client.gui.LOTRGuiTradeUnitTradeInteract;
import lotr.client.gui.LOTRGuiUnitTrade;
import lotr.client.gui.LOTRGuiUnitTradeInteract;
import lotr.client.gui.LOTRGuiUrukTable;
import lotr.client.gui.LOTRGuiWoodElvenTable;
import lotr.common.block.LOTRBlockFlowerPot;
import lotr.common.entity.animal.LOTREntityCamel;
import lotr.common.entity.npc.LOTREntityGollum;
import lotr.common.entity.npc.LOTREntityNPC;
import lotr.common.entity.npc.LOTRHiredNPCInfo.Task;
import lotr.common.entity.npc.LOTRTradeable;
import lotr.common.entity.npc.LOTRUnitTradeable;
import lotr.common.inventory.LOTRContainerAlloyForge;
import lotr.common.inventory.LOTRContainerAngmarTable;
import lotr.common.inventory.LOTRContainerArmorStand;
import lotr.common.inventory.LOTRContainerBarrel;
import lotr.common.inventory.LOTRContainerBlueDwarvenTable;
import lotr.common.inventory.LOTRContainerCamel;
import lotr.common.inventory.LOTRContainerDunlendingTable;
import lotr.common.inventory.LOTRContainerDwarvenTable;
import lotr.common.inventory.LOTRContainerElvenTable;
import lotr.common.inventory.LOTRContainerGollum;
import lotr.common.inventory.LOTRContainerGondorianTable;
import lotr.common.inventory.LOTRContainerHighElvenTable;
import lotr.common.inventory.LOTRContainerHiredFarmerInventory;
import lotr.common.inventory.LOTRContainerHobbitOven;
import lotr.common.inventory.LOTRContainerMorgulTable;
import lotr.common.inventory.LOTRContainerNearHaradTable;
import lotr.common.inventory.LOTRContainerPouch;
import lotr.common.inventory.LOTRContainerRohirricTable;
import lotr.common.inventory.LOTRContainerTrade;
import lotr.common.inventory.LOTRContainerUnitTrade;
import lotr.common.inventory.LOTRContainerUrukTable;
import lotr.common.inventory.LOTRContainerWoodElvenTable;
import lotr.common.tileentity.LOTRTileEntityAlloyForge;
import lotr.common.tileentity.LOTRTileEntityArmorStand;
import lotr.common.tileentity.LOTRTileEntityBarrel;
import lotr.common.tileentity.LOTRTileEntityHobbitOven;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class LOTRCommonProxy implements IGuiHandler
{
	public static int GUI_ID_HOBBIT_OVEN = 0;
	public static int GUI_ID_MORGUL_TABLE = 1;
	public static int GUI_ID_ELVEN_TABLE = 2;
	public static int GUI_ID_TRADE = 3;
	public static int GUI_ID_DWARVEN_TABLE = 4;
	public static int GUI_ID_ALLOY_FORGE = 5;
	public static int GUI_ID_MOB_SPAWNER = 6;
	public static int GUI_ID_UNIT_TRADE = 7;
	public static int GUI_ID_URUK_TABLE = 8;
	public static int GUI_ID_HORN_SELECT = 9;
	public static int GUI_ID_GOLLUM = 10;
	public static int GUI_ID_LOTR = 11;
	public static int GUI_ID_WOOD_ELVEN_TABLE = 12;
	public static int GUI_ID_GONDORIAN_TABLE = 13;
	public static int GUI_ID_ROHIRRIC_TABLE = 14;
	public static int GUI_ID_POUCH = 15;
	public static int GUI_ID_BARREL = 16;
	public static int GUI_ID_ARMOR_STAND = 17;
	public static int GUI_ID_DUNLENDING_TABLE = 18;
	public static int GUI_ID_TRADE_INTERACT = 19;
	public static int GUI_ID_UNIT_TRADE_INTERACT = 20;
	public static int GUI_ID_HIRED_INTERACT = 21;
	public static int GUI_ID_HIRED_FARMER_INVENTORY = 22;
	public static int GUI_ID_ANGMAR_TABLE = 23;
	public static int GUI_ID_TRADE_UNIT_TRADE_INTERACT = 24;
	public static int GUI_ID_NEAR_HARAD_TABLE = 25;
	public static int GUI_ID_HIGH_ELVEN_TABLE = 26;
	public static int GUI_ID_CAMEL = 27;
	public static int GUI_ID_BLUE_DWARVEN_TABLE = 28;
	
	public boolean isClient()
	{
		return false;
	}
	
	public void onPreload() {}
	
	public void onLoad() {}
	
	public void onPostload() {}
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer entityplayer, World world, int i, int j, int k)
	{
		if (ID == GUI_ID_HOBBIT_OVEN)
		{
			TileEntity oven = world.getTileEntity(i, j, k);
			if (oven != null && oven instanceof LOTRTileEntityHobbitOven)
			{
				return new LOTRContainerHobbitOven(entityplayer.inventory, (LOTRTileEntityHobbitOven)oven);
			}
		}
		if (ID == GUI_ID_MORGUL_TABLE)
		{
			return new LOTRContainerMorgulTable(entityplayer.inventory, world, i, j, k);
		}
		if (ID == GUI_ID_ELVEN_TABLE)
		{
			return new LOTRContainerElvenTable(entityplayer.inventory, world, i, j, k);
		}
		if (ID == GUI_ID_TRADE)
		{
			Entity entity = world.getEntityByID(i);
			if (entity != null && entity instanceof LOTRTradeable)
			{
				return new LOTRContainerTrade(entityplayer.inventory, (LOTRTradeable)entity, world);
			}
		}
		if (ID == GUI_ID_DWARVEN_TABLE)
		{
			return new LOTRContainerDwarvenTable(entityplayer.inventory, world, i, j, k);
		}
		if (ID == GUI_ID_ALLOY_FORGE)
		{
			TileEntity forge = world.getTileEntity(i, j, k);
			if (forge != null && forge instanceof LOTRTileEntityAlloyForge)
			{
				return new LOTRContainerAlloyForge(entityplayer.inventory, (LOTRTileEntityAlloyForge)forge);
			}
		}
		if (ID == GUI_ID_UNIT_TRADE)
		{
			Entity entity = world.getEntityByID(i);
			if (entity != null && entity instanceof LOTRUnitTradeable)
			{
				return new LOTRContainerUnitTrade(entityplayer, (LOTRUnitTradeable)entity, world);
			}
		}
		if (ID == GUI_ID_URUK_TABLE)
		{
			return new LOTRContainerUrukTable(entityplayer.inventory, world, i, j, k);
		}
		if (ID == GUI_ID_GOLLUM)
		{
			Entity entity = world.getEntityByID(i);
			if (entity != null && entity instanceof LOTREntityGollum)
			{
				return new LOTRContainerGollum(entityplayer.inventory, (LOTREntityGollum)entity);
			}
		}
		if (ID == GUI_ID_WOOD_ELVEN_TABLE)
		{
			return new LOTRContainerWoodElvenTable(entityplayer.inventory, world, i, j, k);
		}
		if (ID == GUI_ID_GONDORIAN_TABLE)
		{
			return new LOTRContainerGondorianTable(entityplayer.inventory, world, i, j, k);
		}
		if (ID == GUI_ID_ROHIRRIC_TABLE)
		{
			return new LOTRContainerRohirricTable(entityplayer.inventory, world, i, j, k);
		}
		if (ID == GUI_ID_POUCH)
		{
			if (entityplayer.inventory.getCurrentItem() != null && entityplayer.inventory.getCurrentItem().getItem() == LOTRMod.pouch)
			{
				return new LOTRContainerPouch(entityplayer);
			}
		}
		if (ID == GUI_ID_BARREL)
		{
			TileEntity barrel = world.getTileEntity(i, j, k);
			if (barrel != null && barrel instanceof LOTRTileEntityBarrel)
			{
				return new LOTRContainerBarrel(entityplayer.inventory, (LOTRTileEntityBarrel)barrel);
			}
		}
		if (ID == GUI_ID_ARMOR_STAND)
		{
			TileEntity stand = world.getTileEntity(i, j, k);
			if (stand != null && stand instanceof LOTRTileEntityArmorStand)
			{
				return new LOTRContainerArmorStand(entityplayer.inventory, (LOTRTileEntityArmorStand)stand);
			}
		}
		if (ID == GUI_ID_DUNLENDING_TABLE)
		{
			return new LOTRContainerDunlendingTable(entityplayer.inventory, world, i, j, k);
		}
		if (ID == GUI_ID_HIRED_FARMER_INVENTORY)
		{
			Entity entity = world.getEntityByID(i);
			if (entity != null && entity instanceof LOTREntityNPC)
			{
				LOTREntityNPC npc = (LOTREntityNPC)entity;
				if (npc.hiredNPCInfo.isActive && npc.hiredNPCInfo.getHiringPlayer() == entityplayer && npc.hiredNPCInfo.getTask() == Task.FARMER)
				{
					return new LOTRContainerHiredFarmerInventory(entityplayer.inventory, npc);
				}
			}
		}
		if (ID == GUI_ID_ANGMAR_TABLE)
		{
			return new LOTRContainerAngmarTable(entityplayer.inventory, world, i, j, k);
		}
		if (ID == GUI_ID_NEAR_HARAD_TABLE)
		{
			return new LOTRContainerNearHaradTable(entityplayer.inventory, world, i, j, k);
		}
		if (ID == GUI_ID_HIGH_ELVEN_TABLE)
		{
			return new LOTRContainerHighElvenTable(entityplayer.inventory, world, i, j, k);
		}
		if (ID == GUI_ID_CAMEL)
		{
			Entity entity = world.getEntityByID(i);
			if (entity != null && entity instanceof LOTREntityCamel)
			{
				return new LOTRContainerCamel(entityplayer.inventory, (LOTREntityCamel)entity);
			}
		}
		if (ID == GUI_ID_BLUE_DWARVEN_TABLE)
		{
			return new LOTRContainerBlueDwarvenTable(entityplayer.inventory, world, i, j, k);
		}
		return null;
	}
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer entityplayer, World world, int i, int j, int k)
	{
		if (ID == GUI_ID_HOBBIT_OVEN)
		{
			TileEntity oven = world.getTileEntity(i, j, k);
			if (oven != null && oven instanceof LOTRTileEntityHobbitOven)
			{
				return new LOTRGuiHobbitOven(entityplayer.inventory, (LOTRTileEntityHobbitOven)oven);
			}
		}
		if (ID == GUI_ID_MORGUL_TABLE)
		{
			return new LOTRGuiMorgulTable(entityplayer.inventory, world, i, j, k);
		}
		if (ID == GUI_ID_ELVEN_TABLE)
		{
			return new LOTRGuiElvenTable(entityplayer.inventory, world, i, j, k);
		}
		if (ID == GUI_ID_TRADE)
		{
			Entity entity = world.getEntityByID(i);
			if (entity != null && entity instanceof LOTRTradeable)
			{
				return new LOTRGuiTrade(entityplayer.inventory, (LOTRTradeable)entity, world);
			}
		}
		if (ID == GUI_ID_DWARVEN_TABLE)
		{
			return new LOTRGuiDwarvenTable(entityplayer.inventory, world, i, j, k);
		}
		if (ID == GUI_ID_ALLOY_FORGE)
		{
			TileEntity forge = world.getTileEntity(i, j, k);
			if (forge != null && forge instanceof LOTRTileEntityAlloyForge)
			{
				return new LOTRGuiAlloyForge(entityplayer.inventory, (LOTRTileEntityAlloyForge)forge);
			}
		}
		if (ID == GUI_ID_MOB_SPAWNER)
		{
			return new LOTRGuiMobSpawner(world, i, j, k);
		}
		if (ID == GUI_ID_UNIT_TRADE)
		{
			Entity entity = world.getEntityByID(i);
			if (entity != null && entity instanceof LOTRUnitTradeable)
			{
				return new LOTRGuiUnitTrade(entityplayer, (LOTRUnitTradeable)entity, world);
			}
		}
		if (ID == GUI_ID_URUK_TABLE)
		{
			return new LOTRGuiUrukTable(entityplayer.inventory, world, i, j, k);
		}
		if (ID == GUI_ID_HORN_SELECT)
		{
			return new LOTRGuiHornSelect();
		}
		if (ID == GUI_ID_GOLLUM)
		{
			Entity entity = world.getEntityByID(i);
			if (entity != null && entity instanceof LOTREntityGollum)
			{
				return new LOTRGuiGollum(entityplayer.inventory, (LOTREntityGollum)entity);
			}
		}
		if (ID == GUI_ID_LOTR)
		{
			try
			{
				return LOTRGui.guiClasses[LOTRGui.guiIndex].newInstance();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		if (ID == GUI_ID_WOOD_ELVEN_TABLE)
		{
			return new LOTRGuiWoodElvenTable(entityplayer.inventory, world, i, j, k);
		}
		if (ID == GUI_ID_GONDORIAN_TABLE)
		{
			return new LOTRGuiGondorianTable(entityplayer.inventory, world, i, j, k);
		}
		if (ID == GUI_ID_ROHIRRIC_TABLE)
		{
			return new LOTRGuiRohirricTable(entityplayer.inventory, world, i, j, k);
		}
		if (ID == GUI_ID_POUCH)
		{
			if (entityplayer.inventory.getCurrentItem() != null && entityplayer.inventory.getCurrentItem().getItem() == LOTRMod.pouch)
			{
				return new LOTRGuiPouch(entityplayer);
			}
		}
		if (ID == GUI_ID_BARREL)
		{
			TileEntity barrel = world.getTileEntity(i, j, k);
			if (barrel != null && barrel instanceof LOTRTileEntityBarrel)
			{
				return new LOTRGuiBarrel(entityplayer.inventory, (LOTRTileEntityBarrel)barrel);
			}
		}
		if (ID == GUI_ID_ARMOR_STAND)
		{
			TileEntity stand = world.getTileEntity(i, j, k);
			if (stand != null && stand instanceof LOTRTileEntityArmorStand)
			{
				return new LOTRGuiArmorStand(entityplayer.inventory, (LOTRTileEntityArmorStand)stand);
			}
		}
		if (ID == GUI_ID_DUNLENDING_TABLE)
		{
			return new LOTRGuiDunlendingTable(entityplayer.inventory, world, i, j, k);
		}
		if (ID == GUI_ID_TRADE_INTERACT)
		{
			Entity entity = world.getEntityByID(i);
			if (entity != null && entity instanceof LOTRTradeable)
			{
				return new LOTRGuiTradeInteract((LOTREntityNPC)entity);
			}
		}
		if (ID == GUI_ID_UNIT_TRADE_INTERACT)
		{
			Entity entity = world.getEntityByID(i);
			if (entity != null && entity instanceof LOTRUnitTradeable)
			{
				return new LOTRGuiUnitTradeInteract((LOTREntityNPC)entity);
			}
		}
		if (ID == GUI_ID_HIRED_INTERACT)
		{
			Entity entity = world.getEntityByID(i);
			if (entity != null && entity instanceof LOTREntityNPC)
			{
				return new LOTRGuiHiredInteract((LOTREntityNPC)entity);
			}
		}
		if (ID == GUI_ID_HIRED_FARMER_INVENTORY)
		{
			Entity entity = world.getEntityByID(i);
			if (entity != null && entity instanceof LOTREntityNPC)
			{
				LOTREntityNPC npc = (LOTREntityNPC)entity;
				if (npc.hiredNPCInfo.isActive && npc.hiredNPCInfo.getHiringPlayer() == entityplayer && npc.hiredNPCInfo.getTask() == Task.FARMER)
				{
					return new LOTRGuiHiredFarmerInventory(entityplayer.inventory, npc);
				}
			}
		}
		if (ID == GUI_ID_ANGMAR_TABLE)
		{
			return new LOTRGuiAngmarTable(entityplayer.inventory, world, i, j, k);
		}
		if (ID == GUI_ID_TRADE_UNIT_TRADE_INTERACT)
		{
			Entity entity = world.getEntityByID(i);
			if (entity != null && entity instanceof LOTRTradeable)
			{
				return new LOTRGuiTradeUnitTradeInteract((LOTREntityNPC)entity);
			}
		}
		if (ID == GUI_ID_NEAR_HARAD_TABLE)
		{
			return new LOTRGuiNearHaradTable(entityplayer.inventory, world, i, j, k);
		}
		if (ID == GUI_ID_HIGH_ELVEN_TABLE)
		{
			return new LOTRGuiHighElvenTable(entityplayer.inventory, world, i, j, k);
		}
		if (ID == GUI_ID_CAMEL)
		{
			Entity entity = world.getEntityByID(i);
			if (entity != null && entity instanceof LOTREntityCamel)
			{
				return new LOTRGuiCamel(entityplayer.inventory, (LOTREntityCamel)entity);
			}
		}
		if (ID == GUI_ID_BLUE_DWARVEN_TABLE)
		{
			return new LOTRGuiBlueDwarvenTable(entityplayer.inventory, world, i, j, k);
		}
		return null;
	}
	
	public void setInPortal(EntityPlayer entityplayer)
	{
		if (!LOTRTickHandlerServer.playersInPortals.containsKey(entityplayer))
		{
			LOTRTickHandlerServer.playersInPortals.put(entityplayer, Integer.valueOf(0));
		}
	}
	
	public void setInElvenPortal(EntityPlayer entityplayer)
	{
		if (!LOTRTickHandlerServer.playersInElvenPortals.containsKey(entityplayer))
		{
			LOTRTickHandlerServer.playersInElvenPortals.put(entityplayer, Integer.valueOf(0));
		}
	}
	
	public void setInMorgulPortal(EntityPlayer entityplayer)
	{
		if (!LOTRTickHandlerServer.playersInMorgulPortals.containsKey(entityplayer))
		{
			LOTRTickHandlerServer.playersInMorgulPortals.put(entityplayer, Integer.valueOf(0));
		}
	}
	
	public void spawnParticle(String type, double d, double d1, double d2, double d3, double d4, double d5) {}
	
	public void placeFlowerInPot(World world, int i, int j, int k, int side, ItemStack itemstack)
	{
		world.setBlock(i, j, k, LOTRMod.flowerPot, 0, 3);
		LOTRBlockFlowerPot.setPlant(world, i, j, k, itemstack);
	}
	
	public void fillMugFromCauldron(World world, int i, int j, int k, int side, ItemStack itemstack)
	{
		world.setBlockMetadataWithNotify(i, j, k, world.getBlockMetadata(i, j, k) - 1, 3);
	}
	
	public int getBeaconRenderID()
	{
		return 0;
	}
	
	public int getBarrelRenderID()
	{
		return 0;
	}
	
	public int getOrcBombRenderID()
	{
		return 0;
	}
	
	public int getOrcTorchRenderID()
	{
		return 0;
	}
	
	public int getMobSpawnerRenderID()
	{
		return 0;
	}
	
	public int getPlateRenderID()
	{
		return 0;
	}
	
	public int getStalactiteRenderID()
	{
		return 0;
	}
	
	public int getFlowerPotRenderID()
	{
		return 0;
	}
	
	public int getCloverRenderID()
	{
		return 0;
	}
	
	public int getEntJarRenderID()
	{
		return 0;
	}
	
	public int getTrollTotemRenderID()
	{
		return 0;
	}
	
	public int getFenceRenderID()
	{
		return 0;
	}
}
