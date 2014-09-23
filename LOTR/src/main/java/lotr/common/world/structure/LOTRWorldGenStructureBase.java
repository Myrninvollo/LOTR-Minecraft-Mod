package lotr.common.world.structure;

import java.util.Random;

import lotr.common.LOTRFoods;
import lotr.common.LOTRMod;
import lotr.common.block.LOTRBlockFlowerPot;
import lotr.common.block.LOTRBlockMug;
import lotr.common.entity.LOTREntities;
import lotr.common.entity.item.LOTREntityBanner;
import lotr.common.entity.item.LOTREntityBannerWall;
import lotr.common.item.LOTRItemBanner;
import lotr.common.recipe.LOTRBrewingRecipes;
import lotr.common.tileentity.LOTRTileEntityArmorStand;
import lotr.common.tileentity.LOTRTileEntityBarrel;
import lotr.common.tileentity.LOTRTileEntityMobSpawner;
import lotr.common.tileentity.LOTRTileEntityPlate;
import lotr.common.tileentity.LOTRTileEntitySpawnerChest;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public abstract class LOTRWorldGenStructureBase extends WorldGenerator
{
	public boolean restrictions = true;
	public EntityPlayer usingPlayer = null;
	protected boolean notifyChanges;
	
	public LOTRWorldGenStructureBase(boolean flag)
	{
		super(flag);
		notifyChanges = flag;
	}
	
	protected int usingPlayerRotation()
	{
		return MathHelper.floor_double((double)(usingPlayer.rotationYaw * 4F / 360F) + 0.5D) & 3;
	}
	
	protected void setBlockMetadata(World world, int i, int j, int k, int meta)
	{
		world.setBlockMetadataWithNotify(i, j, k, meta, notifyChanges ? 3 : 2);
	}
	
	protected void placeOrcTorch(World world, int i, int j, int k)
	{
		setBlockAndNotifyAdequately(world, i, j, k, LOTRMod.orcTorch, 0);
		setBlockAndNotifyAdequately(world, i, j + 1, k, LOTRMod.orcTorch, 1);
	}
	
	protected void placeMobSpawner(World world, int i, int j, int k, Class entityClass)
	{
		setBlockAndNotifyAdequately(world, i, j, k, LOTRMod.mobSpawner, 0);
		TileEntity tileentity = world.getTileEntity(i, j, k);
		if (tileentity != null && tileentity instanceof LOTRTileEntityMobSpawner)
		{
			((LOTRTileEntityMobSpawner)tileentity).setMobID(LOTREntities.getEntityIDFromClass(entityClass));
		}
	}
	
	protected void placeSpawnerChest(World world, int i, int j, int k, int meta, Class entityClass)
	{
		setBlockAndNotifyAdequately(world, i, j, k, LOTRMod.spawnerChest, 0);
		setBlockMetadata(world, i, j, k, meta);
		TileEntity tileentity = world.getTileEntity(i, j, k);
		if (tileentity != null && tileentity instanceof LOTRTileEntitySpawnerChest)
		{
			((LOTRTileEntitySpawnerChest)tileentity).setMobID(entityClass);
		}
	}
	
	protected void placePlate(World world, int i, int j, int k, Random random, LOTRFoods foodList)
	{
		placePlate_do(world, i, j, k, random, foodList, false);
	}
	
	protected void placePlateWithCertainty(World world, int i, int j, int k, Random random, LOTRFoods foodList)
	{
		placePlate_do(world, i, j, k, random, foodList, true);
	}

	private void placePlate_do(World world, int i, int j, int k, Random random, LOTRFoods foodList, boolean certain)
	{
		if (!certain && random.nextBoolean())
		{
			return;
		}
		
		setBlockAndNotifyAdequately(world, i, j, k, LOTRMod.plateBlock, 0);
		if (certain || random.nextBoolean())
		{
			TileEntity tileentity = world.getTileEntity(i, j, k);
			if (tileentity != null && tileentity instanceof LOTRTileEntityPlate)
			{
				LOTRTileEntityPlate plate = (LOTRTileEntityPlate)tileentity;
				ItemStack food = foodList.getRandomFood(random);
				plate.setFoodItem(food);
			}
		}
	}
	
	protected void placeBarrel(World world, Random random, int i, int j, int k, int meta, Item drink)
	{
		setBlockAndNotifyAdequately(world, i, j, k, LOTRMod.barrel, meta);
		TileEntity tileentity = world.getTileEntity(i, j, k);
		if (tileentity != null && tileentity instanceof LOTRTileEntityBarrel)
		{
			LOTRTileEntityBarrel barrel = (LOTRTileEntityBarrel)tileentity;
			barrel.barrelMode = LOTRTileEntityBarrel.FULL;
			barrel.setInventorySlotContents(9, new ItemStack(drink, LOTRBrewingRecipes.BARREL_CAPACITY - random.nextInt(LOTRBrewingRecipes.BARREL_CAPACITY / 2), 1 + random.nextInt(3)));
		}
	}
	
	protected void placeMug(World world, Random random, int i, int j, int k, int meta, Item drink)
	{
		setBlockAndNotifyAdequately(world, i, j, k, LOTRMod.mugBlock, meta);
		if (random.nextInt(3) != 0)
		{
			LOTRBlockMug.setMugItem(world, i, j, k, new ItemStack(drink, 1, 1 + random.nextInt(3)));
		}
	}
	
	protected void placeFlowerPot(World world, int i, int j, int k, ItemStack itemstack)
	{
		setBlockAndNotifyAdequately(world, i, j, k, LOTRMod.flowerPot, 0);
		LOTRBlockFlowerPot.setPlant(world, i, j, k, itemstack);
	}
	
	protected void spawnItemFrame(World world, int i, int j, int k, int direction, ItemStack itemstack)
	{
		EntityItemFrame frame = new EntityItemFrame(world, i, j, k, direction);
		frame.setDisplayedItem(itemstack);
		world.spawnEntityInWorld(frame);
	}
	
	protected void placeArmorStand(World world, int i, int j, int k, int direction, ItemStack[] armor)
	{
		setBlockAndNotifyAdequately(world, i, j, k, LOTRMod.armorStand, direction);
		setBlockAndNotifyAdequately(world, i, j + 1, k, LOTRMod.armorStand, direction | 4);
		TileEntity tileentity = world.getTileEntity(i, j, k);
		if (tileentity != null && tileentity instanceof LOTRTileEntityArmorStand)
		{
			LOTRTileEntityArmorStand armorStand = (LOTRTileEntityArmorStand)tileentity;
			for (int l = 0; l < armor.length; l++)
			{
				armorStand.setInventorySlotContents(l, armor[l]);
			}
		}
	}
	
	protected void placeBanner(World world, int i, int j, int k, int direction, int type)
	{
		LOTREntityBanner banner = new LOTREntityBanner(world);
		banner.setLocationAndAngles(i + 0.5D, j, k + 0.5D, (float)direction * 90F, 0F);
		banner.setBannerFaction(LOTRItemBanner.getFaction(type));
		world.spawnEntityInWorld(banner);
	}
	
	protected void placeWallBanner(World world, int i, int j, int k, int direction, int type)
	{
		LOTREntityBannerWall banner = new LOTREntityBannerWall(world, i, j, k, direction);
		banner.setBannerFaction(LOTRItemBanner.getFaction(type));
		world.spawnEntityInWorld(banner);
	}
	
	protected void setGrassToDirt(World world, int i, int j, int k)
	{
		if (world.getBlock(i, j, k) == Blocks.grass)
		{
			setBlockAndNotifyAdequately(world, i, j, k, Blocks.dirt, 0);
		}
	}
	
	protected void setAir(World world, int i, int j, int k)
	{
		setBlockAndNotifyAdequately(world, i, j, k, Blocks.air, 0);
	}
	
	protected void placeSkull(World world, Random random, int i, int j, int k)
	{
		setBlockAndNotifyAdequately(world, i, j, k, Blocks.skull, 1);
		TileEntity tileentity = world.getTileEntity(i, j, k);
		if (tileentity != null && tileentity instanceof TileEntitySkull)
		{
			TileEntitySkull skull = (TileEntitySkull)tileentity;
			skull.func_145903_a(random.nextInt(16));
		}
	}
}
