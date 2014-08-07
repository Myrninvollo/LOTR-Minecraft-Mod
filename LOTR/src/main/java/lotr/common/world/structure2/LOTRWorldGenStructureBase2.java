package lotr.common.world.structure2;

import java.util.Random;

import lotr.common.LOTRFoods;
import lotr.common.LOTRMod;
import lotr.common.block.LOTRBlockArmorStand;
import lotr.common.block.LOTRBlockBarrel;
import lotr.common.block.LOTRBlockDwarvenForge;
import lotr.common.block.LOTRBlockFlowerPot;
import lotr.common.block.LOTRBlockHobbitOven;
import lotr.common.block.LOTRBlockMug;
import lotr.common.block.LOTRBlockOrcForge;
import lotr.common.entity.LOTREntities;
import lotr.common.entity.item.LOTREntityBanner;
import lotr.common.entity.item.LOTREntityBannerWall;
import lotr.common.entity.npc.LOTREntityNPC;
import lotr.common.recipe.LOTRBrewingRecipes;
import lotr.common.tileentity.LOTRTileEntityArmorStand;
import lotr.common.tileentity.LOTRTileEntityBarrel;
import lotr.common.tileentity.LOTRTileEntityMobSpawner;
import lotr.common.tileentity.LOTRTileEntityPlate;
import lotr.common.tileentity.LOTRTileEntitySpawnerChest;
import lotr.common.world.structure.LOTRChestContents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.Direction;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public abstract class LOTRWorldGenStructureBase2 extends WorldGenerator
{
	public boolean restrictions = true;
	public EntityPlayer usingPlayer = null;
	protected boolean notifyChanges;
	private int rotationMode;
	
	protected int originX;
	protected int originY;
	protected int originZ;
	
	public LOTRWorldGenStructureBase2(boolean flag)
	{
		super(flag);
		notifyChanges = flag;
	}
	
	@Override
	public boolean generate(World world, Random random, int i, int j, int k)
	{
		return generateWithSetRotation(world, random, i, j, k, random.nextInt(4));
	}
	
	public abstract boolean generateWithSetRotation(World world, Random random, int i, int j, int k, int rotation);
	
	public int usingPlayerRotation()
	{
		return MathHelper.floor_double((double)(usingPlayer.rotationYaw * 4F / 360F) + 0.5D) & 3;
	}
	
	protected void setRotationMode(int i)
	{
		rotationMode = i;
	}
	
	public int getRotationMode()
	{
		return rotationMode;
	}
	
	protected void setOrigin(int i, int j, int k)
	{
		originX = i;
		originY = j;
		originZ = k;
	}
	
	protected void setBlockAndMetadata(World world, int i, int j, int k, Block block, int meta)
	{
		int i1 = i;
		int k1 = k;
		i = getX(i1, k1);
		k = getZ(i1, k1);
		j = getY(j);

		meta = rotateMeta(block, meta);
		super.setBlockAndNotifyAdequately(world, i, j, k, block, meta);
		
		if (meta != 0 && (block instanceof BlockChest || block instanceof BlockFurnace))
		{
			world.setBlockMetadataWithNotify(i, j, k, meta, notifyChanges ? 3 : 2);
		}
	}
	
	private int rotateMeta(Block block, int meta)
	{
		if (block instanceof BlockRotatedPillar)
		{
			int i = meta & 3;
			int j = meta & 12;
			
			if (j == 0)
			{
				return meta;
			}
			
			else
			{
				if (rotationMode == 0 || rotationMode == 2)
				{
					return meta;
				}
				else
				{
					if (j == 4)
					{
						j = 8;
					}
					else if (j == 8)
					{
						j = 4;
					}
					
					return j | i;
				}
			}
		}
		
		if (block instanceof BlockStairs)
		{
			int i = meta & 3;
			int j = meta & 4;
					
			for (int l = 0; l < rotationMode; l++)
			{
				if (i == 2)
				{
					i = 1;
				}
				else if (i == 1)
				{
					i = 3;
				}
				else if (i == 3)
				{
					i = 0;
				}
				else if (i == 0)
				{
					i = 2;
				}
			}
			
			return j | i;
		}
			
		if (block instanceof LOTRBlockMug)
		{
			int i = meta;
					
			for (int l = 0; l < rotationMode; l++)
			{
				i = Direction.rotateRight[i];
			}
			
			return i;
		}
		
		if (block instanceof LOTRBlockArmorStand)
		{
			int i = meta & 3;
			int j = meta & 4;
					
			for (int l = 0; l < rotationMode; l++)
			{
				i = Direction.rotateRight[i];
			}
			
			return j | i;
		}
		
		if (block == Blocks.wall_sign || block instanceof BlockLadder || block instanceof BlockFurnace || block instanceof BlockChest || block instanceof LOTRBlockBarrel || block instanceof LOTRBlockHobbitOven || block instanceof LOTRBlockDwarvenForge || block instanceof LOTRBlockOrcForge)
		{
			if (meta == 0 && (block instanceof BlockFurnace || block instanceof BlockChest))
			{
				return meta;
			}
			
			int i = meta;
			
			for (int l = 0; l < rotationMode; l++)
			{
				if (i == 2)
				{
					i = 5;
				}
				else if (i == 5)
				{
					i = 3;
				}
				else if (i == 3)
				{
					i = 4;
				}
				else if (i == 4)
				{
					i = 2;
				}
			}
			
			return i;
		}
		
		if (block instanceof BlockBed)
		{
			int i = meta;
			boolean flag = meta >= 8;
			if (flag)
			{
				i -= 8;
			}

			for (int l = 0; l < rotationMode; l++)
			{
				i = Direction.rotateRight[i];
			}
			
			if (flag)
			{
				i += 8;
			}
			
			return i;
		}
		
		if (block instanceof BlockTorch)
		{
			if (meta == 5)
			{
				return 5;
			}
			else
			{
				int i = meta;
				
				for (int l = 0; l < rotationMode; l++)
				{
					if (i == 4)
					{
						i = 1;
					}
					else if (i == 1)
					{
						i = 3;
					}
					else if (i == 3)
					{
						i = 2;
					}
					else if (i == 2)
					{
						i = 4;
					}
				}
				
				return i;
			}
		}
		
		if (block instanceof BlockDoor)
		{
			if ((meta & 8) != 0)
			{
				return meta;
			}
			else
			{
				int i = meta;
				
				for (int l = 0; l < rotationMode; l++)
				{
					i = Direction.rotateRight[i];
				}
				
				return i;
			}
		}
		
		if (block instanceof BlockTrapDoor)
		{
			int i = meta & 3;
			int j = meta & 8;
			
			for (int l = 0; l < rotationMode; l++)
			{
				if (i == 0)
				{
					i = 3;
				}
				else if (i == 1)
				{
					i = 2;
				}
				else if (i == 2)
				{
					i = 0;
				}
				else if (i == 3)
				{
					i = 1;
				}
			}
			
			return j | i;
		}
		
		if (block instanceof BlockFenceGate)
		{
			int i = meta;
			
			for (int l = 0; l < rotationMode; l++)
			{
				i = Direction.rotateRight[i];
			}
			
			return i;
		}
		
		if (block instanceof BlockSkull)
		{
			if (meta < 2)
			{
				return meta;
			}
			else
			{
				int i = Direction.facingToDirection[meta];
				
				for (int l = 0; l < rotationMode; l++)
				{
					i = Direction.rotateRight[i];
				}
				
				return Direction.directionToFacing[i];
			}
		}
		
		return meta;
	}
	
	private int indexInArray(int value, int[] array)
	{
		for (int i = 0; i < array.length; i++)
		{
			if (array[i] == value)
			{
				return i;
			}
		}
		return -1;
	}
	
	protected Block getBlock(World world, int i, int j, int k)
	{
		int i1 = i;
		int k1 = k;
		i = getX(i1, k1);
		k = getZ(i1, k1);
		j = getY(j);
		return world.getBlock(i, j, k);
	}

	protected int getMeta(World world, int i, int j, int k)
	{
		int i1 = i;
		int k1 = k;
		i = getX(i1, k1);
		k = getZ(i1, k1);
		j = getY(j);
		return world.getBlockMetadata(i, j, k);
	}
	
	protected int getTopBlock(World world, int i, int k)
	{
		int i1 = i;
		int k1 = k;
		i = getX(i1, k1);
		k = getZ(i1, k1);
		return world.getTopSolidOrLiquidBlock(i, k) - originY;
	}
	
	protected boolean isAir(World world, int i, int j, int k)
	{
		return getBlock(world, i, j, k).getMaterial() == Material.air;
	}
	
	protected boolean isOpaque(World world, int i, int j, int k)
	{
		return getBlock(world, i, j, k).isOpaqueCube();
	}
	
	protected TileEntity getTileEntity(World world, int i, int j, int k)
	{
		int i1 = i;
		int k1 = k;
		i = getX(i1, k1);
		k = getZ(i1, k1);
		j = getY(j);
		return world.getTileEntity(i, j, k);
	}
	
	protected void fillChest(World world, Random random, int i, int j, int k, LOTRChestContents contents)
	{
		int i1 = i;
		int k1 = k;
		i = getX(i1, k1);
		k = getZ(i1, k1);
		j = getY(j);
		LOTRChestContents.fillChest(world, random, i, j, k, contents);
	}
	
	protected void placeOrcTorch(World world, int i, int j, int k)
	{
		setBlockAndMetadata(world, i, j, k, LOTRMod.orcTorch, 0);
		setBlockAndMetadata(world, i, j, k, LOTRMod.orcTorch, 1);
	}
	
	protected void placeMobSpawner(World world, int i, int j, int k, Class entityClass)
	{
		setBlockAndMetadata(world, i, j, k, LOTRMod.mobSpawner, 0);
		TileEntity tileentity = getTileEntity(world, i, j, k);
		if (tileentity != null && tileentity instanceof LOTRTileEntityMobSpawner)
		{
			((LOTRTileEntityMobSpawner)tileentity).setMobID(LOTREntities.getEntityIDFromClass(entityClass));
		}
	}
	
	protected void placeSpawnerChest(World world, int i, int j, int k, int meta, Class entityClass)
	{
		setBlockAndMetadata(world, i, j, k, LOTRMod.spawnerChest, 0);
		setBlockAndMetadata(world, i, j, k, LOTRMod.spawnerChest, meta);
		TileEntity tileentity = getTileEntity(world, i, j, k);
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
		
		setBlockAndMetadata(world, i, j, k, LOTRMod.plateBlock, 0);
		if (certain || random.nextBoolean())
		{
			TileEntity tileentity = getTileEntity(world, i, j, k);
			if (tileentity != null && tileentity instanceof LOTRTileEntityPlate)
			{
				LOTRTileEntityPlate plate = (LOTRTileEntityPlate)tileentity;
				ItemStack food = foodList.getRandomFood(random);
				plate.foodItem = food.getItem();
				plate.foodDamage = food.getItemDamage();
			}
		}
	}
	
	protected void placeBarrel(World world, Random random, int i, int j, int k, int meta, Item drink)
	{
		setBlockAndMetadata(world, i, j, k, LOTRMod.barrel, meta);
		TileEntity tileentity = getTileEntity(world, i, j, k);
		if (tileentity != null && tileentity instanceof LOTRTileEntityBarrel)
		{
			LOTRTileEntityBarrel barrel = (LOTRTileEntityBarrel)tileentity;
			barrel.barrelMode = LOTRTileEntityBarrel.FULL;
			barrel.setInventorySlotContents(9, new ItemStack(drink, LOTRBrewingRecipes.BARREL_CAPACITY - random.nextInt(LOTRBrewingRecipes.BARREL_CAPACITY / 2), 1 + random.nextInt(3)));
		}
	}
	
	protected void placeMug(World world, Random random, int i, int j, int k, int meta, Item drink)
	{
		setBlockAndMetadata(world, i, j, k, LOTRMod.mugBlock, meta);
		if (random.nextInt(3) != 0)
		{
			int i1 = i;
			int k1 = k;
			i = getX(i1, k1);
			k = getZ(i1, k1);
			j = getY(j);
			LOTRBlockMug.setMugItem(world, i, j, k, new ItemStack(drink, 1, 1 + random.nextInt(3)));
		}
	}
	
	protected void placeFlowerPot(World world, int i, int j, int k, ItemStack itemstack)
	{
		setBlockAndMetadata(world, i, j, k, LOTRMod.flowerPot, 0);
		
		int i1 = i;
		int k1 = k;
		i = getX(i1, k1);
		k = getZ(i1, k1);
		j = getY(j);
		LOTRBlockFlowerPot.setPlant(world, i, j, k, itemstack);
	}
	
	protected void spawnItemFrame(World world, int i, int j, int k, int direction, ItemStack itemstack)
	{
		int i1 = i;
		int k1 = k;
		i = getX(i1, k1);
		k = getZ(i1, k1);
		j = getY(j);
		
		for (int l = 0; l < rotationMode; l++)
		{
			direction = Direction.rotateRight[direction];
		}
		
		EntityItemFrame frame = new EntityItemFrame(world, i, j, k, direction);
		frame.setDisplayedItem(itemstack);
		world.spawnEntityInWorld(frame);
	}
	
	protected void placeArmorStand(World world, int i, int j, int k, int direction, ItemStack[] armor)
	{
		setBlockAndMetadata(world, i, j, k, LOTRMod.armorStand, direction);
		setBlockAndMetadata(world, i, j + 1, k, LOTRMod.armorStand, direction | 4);
		TileEntity tileentity = getTileEntity(world, i, j, k);
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
		int i1 = i;
		int k1 = k;
		i = getX(i1, k1);
		k = getZ(i1, k1);
		j = getY(j);
		
		for (int l = 0; l < rotationMode; l++)
		{
			direction = Direction.rotateRight[direction];
		}
		
		LOTREntityBanner banner = new LOTREntityBanner(world);
		banner.setLocationAndAngles(i + 0.5D, j, k + 0.5D, (float)direction * 90F, 0F);
		banner.setBannerType(type);
		world.spawnEntityInWorld(banner);
	}
	
	protected void placeWallBanner(World world, int i, int j, int k, int direction, int type)
	{
		int i1 = i;
		int k1 = k;
		i = getX(i1, k1);
		k = getZ(i1, k1);
		j = getY(j);
		
		for (int l = 0; l < rotationMode; l++)
		{
			direction = Direction.rotateRight[direction];
		}
		
		LOTREntityBannerWall banner = new LOTREntityBannerWall(world, i, j, k, direction);
		banner.setBannerType(type);
		world.spawnEntityInWorld(banner);
	}
	
	protected void setGrassToDirt(World world, int i, int j, int k)
	{
		if (getBlock(world, i, j, k) == Blocks.grass)
		{
			setBlockAndMetadata(world, i, j, k, Blocks.dirt, 0);
		}
	}
	
	protected void setAir(World world, int i, int j, int k)
	{
		setBlockAndMetadata(world, i, j, k, Blocks.air, 0);
	}
	
	protected void placeSkull(World world, Random random, int i, int j, int k)
	{
		setBlockAndMetadata(world, i, j, k, Blocks.skull, 1);
		TileEntity tileentity = getTileEntity(world, i, j, k);
		if (tileentity != null && tileentity instanceof TileEntitySkull)
		{
			TileEntitySkull skull = (TileEntitySkull)tileentity;
			skull.func_145903_a(random.nextInt(16));
		}
	}
	
	protected void spawnNPCAndSetHome(EntityCreature entity, World world, int i, int j, int k, int homeDistance)
	{
		int i1 = i;
		int k1 = k;
		i = getX(i1, k1);
		k = getZ(i1, k1);
		j = getY(j);
		
		entity.setLocationAndAngles(i + 0.5D, j, k + 0.5D, 0F, 0F);
		entity.onSpawnWithEgg(null);
		if (entity instanceof LOTREntityNPC)
		{
			((LOTREntityNPC)entity).isNPCPersistent = true;
		}
		world.spawnEntityInWorld(entity);
		entity.setHomeArea(i, j, k, homeDistance);
	}

	protected int getX(int x, int z)
	{
		switch (rotationMode)
		{
			case 0:
				return originX - x;
			case 1:
				return originX - z;
			case 2:
				return originX + x;
			case 3:
				return originX + z;
		}
		return originX;
	}
	
	protected int getZ(int x, int z)
	{
		switch (rotationMode)
		{
			case 0:
				return originZ + z;
			case 1:
				return originZ - x;
			case 2:
				return originZ - z;
			case 3:
				return originZ + x;
		}
		return originZ;
	}
	
	protected int getY(int y)
	{
		return originY + y;
	}
}
