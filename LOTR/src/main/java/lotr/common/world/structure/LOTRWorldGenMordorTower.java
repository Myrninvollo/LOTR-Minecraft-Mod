package lotr.common.world.structure;

import java.util.List;
import java.util.Random;

import lotr.common.LOTRLevelData;
import lotr.common.LOTRMod;
import lotr.common.entity.npc.LOTREntityMordorOrcMercenaryCaptain;
import lotr.common.world.biome.LOTRBiomeGenMordor;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

public class LOTRWorldGenMordorTower extends LOTRWorldGenStructureBase
{
	public LOTRWorldGenMordorTower(boolean flag)
	{
		super(flag);
	}
	
	@Override
	public boolean generate(World world, Random random, int i, int j, int k)
	{
		if (restrictions)
		{
			if (!(world.getBiomeGenForCoords(i, k) instanceof LOTRBiomeGenMordor))
			{
				return false;
			}
		}
		
		j--;
		
		int rotation = random.nextInt(4);
		if (!restrictions && usingPlayer != null)
		{
			rotation = usingPlayerRotation();
		}
		
		switch (rotation)
		{
			case 0:
				k += 7;
				break;
			case 1:
				i -= 7;
				break;
			case 2:
				k -= 7;
				break;
			case 3:
				i += 7;
				break;
		}
		
		int sections = 2 + random.nextInt(3);
		int equipmentSection = 1 + random.nextInt(sections);
		
		if (restrictions)
		{
			for (int i1 = i - 7; i1 <= i + 7; i1++)
			{
				for (int k1 = k - 7; k1 <= k + 7; k1++)
				{
					int j1 = world.getHeightValue(i1, k1) - 1;
					if (world.getBlock(i1, j1, k1) != LOTRMod.rock || world.getBlockMetadata(i1, j1, k1) != 0)
					{
						return false;
					}
				}
			}
			
			List intersectingCreatures = world.getEntitiesWithinAABB(EntityLiving.class, AxisAlignedBB.getAABBPool().getAABB(i - 7, j, k - 7, i + 7, j + ((sections + 1) * 8) + 10, k + 7));
			for (Object obj : intersectingCreatures)
			{
				((EntityLiving)obj).setDead();
			}
		}
		
		for (int k1 = k - 2; k1 <= k + 2; k1++)
		{
			for (int j1 = j; !LOTRMod.isOpaque(world, i - 6, j1, k1) && j1 >= 0; j1--)
			{
				setBlockAndNotifyAdequately(world, i - 6, j1, k1, LOTRMod.brick, 0);
			}
			for (int j1 = j; !LOTRMod.isOpaque(world, i + 6, j1, k1) && j1 >= 0; j1--)
			{
				setBlockAndNotifyAdequately(world, i + 6, j1, k1, LOTRMod.brick, 0);
			}
		}
		
		for (int k1 = k - 4; k1 <= k + 4; k1++)
		{
			for (int j1 = j; !LOTRMod.isOpaque(world, i - 5, j1, k1) && j1 >= 0; j1--)
			{
				setBlockAndNotifyAdequately(world, i - 5, j1, k1, LOTRMod.brick, 0);
			}
			for (int j1 = j; !LOTRMod.isOpaque(world, i + 5, j1, k1) && j1 >= 0; j1--)
			{
				setBlockAndNotifyAdequately(world, i + 5, j1, k1, LOTRMod.brick, 0);
			}
		}
		
		for (int k1 = k - 5; k1 <= k + 5; k1++)
		{
			for (int i1 = i - 4; i1 <= i - 3; i1++)
			{
				for (int j1 = j; !LOTRMod.isOpaque(world, i1, j1, k1) && j1 >= 0; j1--)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.brick, 0);
				}
			}
			
			for (int i1 = i + 3; i1 <= i + 4; i1++)
			{
				for (int j1 = j; !LOTRMod.isOpaque(world, i1, j1, k1) && j1 >= 0; j1--)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.brick, 0);
				}
			}
		}
		
		for (int k1 = k - 6; k1 <= k + 6; k1++)
		{
			for (int i1 = i - 2; i1 <= i + 2; i1++)
			{
				for (int j1 = j; !LOTRMod.isOpaque(world, i1, j1, k1) && j1 >= 0; j1--)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.brick, 0);
				}
			}
		}
		
		for (int l = 0; l <= sections; l++)
		{
			generateTowerSection(world, random, i, j, k, l, false, l == equipmentSection);
		}
		
		generateTowerSection(world, random, i, j, k, sections + 1, true, false);
		
		LOTREntityMordorOrcMercenaryCaptain trader = new LOTREntityMordorOrcMercenaryCaptain(world);
		trader.setLocationAndAngles(i + 0.5D, j + (sections + 1) * 8 + 1, k - 4 + 0.5D, world.rand.nextFloat() * 360F, 0F);
		trader.onSpawnWithEgg(null);
		trader.setHomeArea(i, j + (sections + 1) * 8, k, 24);
		world.spawnEntityInWorld(trader);
		
		switch (rotation)
		{
			case 0:
				for (int i1 = i - 1; i1 <= i + 1; i1++)
				{
					setBlockAndNotifyAdequately(world, i1, j, k - 6, LOTRMod.slabDouble, 0);
					for (int j1 = j + 1; j1 <= j + 3; j1++)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k - 6, Blocks.air, 0);
					}
				}
				placeWallBanner(world, i, j + 7, k - 6, 2, 2);
				break;
			case 1:
				for (int k1 = k - 1; k1 <= k + 1; k1++)
				{
					setBlockAndNotifyAdequately(world, i + 6, j, k1, LOTRMod.slabDouble, 0);
					for (int j1 = j + 1; j1 <= j + 3; j1++)
					{
						setBlockAndNotifyAdequately(world, i + 6, j1, k1, Blocks.air, 0);
					}
				}
				placeWallBanner(world, i + 6, j + 7, k, 3, 2);
				break;
			case 2:
				for (int i1 = i - 1; i1 <= i + 1; i1++)
				{
					setBlockAndNotifyAdequately(world, i1, j, k + 6, LOTRMod.slabDouble, 0);
					for (int j1 = j + 1; j1 <= j + 3; j1++)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k + 6, Blocks.air, 0);
					}
				}
				placeWallBanner(world, i, j + 7, k + 6, 0, 2);
				break;
			case 3:
				for (int k1 = k - 1; k1 <= k + 1; k1++)
				{
					setBlockAndNotifyAdequately(world, i - 6, j, k1, LOTRMod.slabDouble, 0);
					for (int j1 = j + 1; j1 <= j + 3; j1++)
					{
						setBlockAndNotifyAdequately(world, i - 6, j1, k1, Blocks.air, 0);
					}
				}
				placeWallBanner(world, i - 6, j + 7, k, 1, 2);
				break;
		}
		
		if (usingPlayer == null)
		{
			LOTRLevelData.mordorTowerLocations.add(new ChunkCoordinates(i, j, k));
		}
		
		return true;
	}
	
	private void generateTowerSection(World world, Random random, int i, int j, int k, int section, boolean isTop, boolean isEquipmentSection)
	{
		j += section * 8;
		
		for (int j1 = (section == 0 ? j : j + 1); j1 <= (isTop ? j + 10 : j + 8); j1++)
		{
			Block fillBlock = Blocks.air;
			int fillMeta = 0;
			
			if (j1 == j)
			{
				fillBlock = LOTRMod.slabDouble;
				fillMeta = 0;
			}
			else if (j1 == j + 8 && !isTop)
			{
				fillBlock = LOTRMod.slabSingle;
				fillMeta = 8;
			}
			else
			{
				fillBlock = Blocks.air;
				fillMeta = 0;
			}
			
			for (int k1 = k - 2; k1 <= k + 2; k1++)
			{
				setBlockAndNotifyAdequately(world, i - 5, j1, k1, fillBlock, fillMeta);
				setBlockAndNotifyAdequately(world, i + 5, j1, k1, fillBlock, fillMeta);
			}
			
			for (int k1 = k - 4; k1 <= k + 4; k1++)
			{
				for (int i1 = i - 4; i1 <= i - 3; i1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, fillBlock, fillMeta);
				}
				for (int i1 = i + 3; i1 <= i + 4; i1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, fillBlock, fillMeta);
				}
			}
			
			for (int k1 = k - 5; k1 <= k + 5; k1++)
			{
				for (int i1 = i - 2; i1 <= i + 2; i1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, fillBlock, fillMeta);
				}
			}
		}
		
		for (int j1 = j + 1; j1 <= (isTop ? j + 1 : j + 8); j1++)
		{
			for (int k1 = k - 2; k1 <= k + 2; k1++)
			{
				setBlockAndNotifyAdequately(world, i - 6, j1, k1, LOTRMod.brick, 0);
				setBlockAndNotifyAdequately(world, i + 6, j1, k1, LOTRMod.brick, 0);
			}
			
			for (int i1 = i - 2; i1 <= i + 2; i1++)
			{
				setBlockAndNotifyAdequately(world, i1, j1, k - 6, LOTRMod.brick, 0);
				setBlockAndNotifyAdequately(world, i1, j1, k + 6, LOTRMod.brick, 0);
			}
			
			setBlockAndNotifyAdequately(world, i - 5, j1, k - 4, LOTRMod.brick, 0);
			setBlockAndNotifyAdequately(world, i - 5, j1, k - 3, LOTRMod.brick, 0);
			setBlockAndNotifyAdequately(world, i - 5, j1, k + 3, LOTRMod.brick, 0);
			setBlockAndNotifyAdequately(world, i - 5, j1, k + 4, LOTRMod.brick, 0);
			setBlockAndNotifyAdequately(world, i - 4, j1, k - 5, LOTRMod.brick, 0);
			setBlockAndNotifyAdequately(world, i - 4, j1, k + 5, LOTRMod.brick, 0);
			setBlockAndNotifyAdequately(world, i - 3, j1, k - 5, LOTRMod.brick, 0);
			setBlockAndNotifyAdequately(world, i - 3, j1, k + 5, LOTRMod.brick, 0);
			setBlockAndNotifyAdequately(world, i + 3, j1, k - 5, LOTRMod.brick, 0);
			setBlockAndNotifyAdequately(world, i + 3, j1, k + 5, LOTRMod.brick, 0);
			setBlockAndNotifyAdequately(world, i + 4, j1, k - 5, LOTRMod.brick, 0);
			setBlockAndNotifyAdequately(world, i + 4, j1, k + 5, LOTRMod.brick, 0);
			setBlockAndNotifyAdequately(world, i + 5, j1, k - 4, LOTRMod.brick, 0);
			setBlockAndNotifyAdequately(world, i + 5, j1, k - 3, LOTRMod.brick, 0);
			setBlockAndNotifyAdequately(world, i + 5, j1, k + 3, LOTRMod.brick, 0);
			setBlockAndNotifyAdequately(world, i + 5, j1, k + 4, LOTRMod.brick, 0);
		}
		
		placeOrcTorch(world, i - 5, j + 1, k - 2);
		placeOrcTorch(world, i - 5, j + 1, k + 2);
		placeOrcTorch(world, i + 5, j + 1, k - 2);
		placeOrcTorch(world, i + 5, j + 1, k + 2);
		placeOrcTorch(world, i - 2, j + 1, k - 5);
		placeOrcTorch(world, i + 2, j + 1, k - 5);
		placeOrcTorch(world, i - 2, j + 1, k + 5);
		placeOrcTorch(world, i + 2, j + 1, k + 5);
		
		if (!isTop)
		{
			for (int j1 = j + 2; j1 <= j + 4; j1++)
			{
				for (int k1 = k - 1; k1 <= k + 1; k1++)
				{
					setBlockAndNotifyAdequately(world, i - 6, j1, k1, LOTRMod.orcSteelBars, 0);
					setBlockAndNotifyAdequately(world, i + 6, j1, k1, LOTRMod.orcSteelBars, 0);
				}
				
				for (int i1 = i - 1; i1 <= i + 1; i1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k - 6, LOTRMod.orcSteelBars, 0);
					setBlockAndNotifyAdequately(world, i1, j1, k + 6, LOTRMod.orcSteelBars, 0);
				}
			}

		
			for (int i1 = i - 2; i1 <= i + 2; i1++)
			{
				for (int k1 = k - 2; k1 <= k + 2; k1++)
				{
					setBlockAndNotifyAdequately(world, i1, j + 8, k1, Blocks.air, 0);
				}
			}
		
			setBlockAndNotifyAdequately(world, i - 2, j + 1, k + 1, LOTRMod.slabSingle, 0);
			setBlockAndNotifyAdequately(world, i - 2, j + 1, k + 2, LOTRMod.slabSingle, 8);
			setBlockAndNotifyAdequately(world, i - 1, j + 2, k + 2, LOTRMod.slabSingle, 0);
			setBlockAndNotifyAdequately(world, i, j + 2, k + 2, LOTRMod.slabSingle, 8);
			setBlockAndNotifyAdequately(world, i + 1, j + 3, k + 2, LOTRMod.slabSingle, 0);
			setBlockAndNotifyAdequately(world, i + 2, j + 3, k + 2, LOTRMod.slabSingle, 8);
			setBlockAndNotifyAdequately(world, i + 2, j + 4, k + 1, LOTRMod.slabSingle, 0);
			setBlockAndNotifyAdequately(world, i + 2, j + 4, k, LOTRMod.slabSingle, 8);
			setBlockAndNotifyAdequately(world, i + 2, j + 5, k - 1, LOTRMod.slabSingle, 0);
			setBlockAndNotifyAdequately(world, i + 2, j + 5, k - 2, LOTRMod.slabSingle, 8);
			setBlockAndNotifyAdequately(world, i + 1, j + 6, k - 2, LOTRMod.slabSingle, 0);
			setBlockAndNotifyAdequately(world, i, j + 6, k - 2, LOTRMod.slabSingle, 8);
			setBlockAndNotifyAdequately(world, i - 1, j + 7, k - 2, LOTRMod.slabSingle, 0);
			setBlockAndNotifyAdequately(world, i - 2, j + 7, k - 2, LOTRMod.slabSingle, 8);
			setBlockAndNotifyAdequately(world, i - 2, j + 8, k - 1, LOTRMod.slabSingle, 0);
			setBlockAndNotifyAdequately(world, i - 2, j + 8, k, LOTRMod.slabSingle, 8);
		}
		
		for (int i1 = i - 1; i1 <= i + 1; i1++)
		{
			for (int k1 = k - 1; k1 <= k + 1; k1++)
			{
				for (int j1 = j + 1; j1 <= (isTop ? j + 3: j + 8); j1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.brick, 0);
				}
			}
		}

		if (isEquipmentSection)
		{
			int l = random.nextInt(4);
			switch (l)
			{
				case 0:
					for (int i1 = i - 1; i1 <= i + 1; i1++)
					{
						setBlockAndNotifyAdequately(world, i1, j + 1, k - 5, LOTRMod.orcBomb, 0);
						setBlockAndNotifyAdequately(world, i1, j + 1, k + 5, LOTRMod.slabSingle, 9);
						placeBarrel(world, random, i1, j + 2, k + 5, 2, LOTRMod.mugOrcDraught);
					}
					break;
				case 1:
					for (int k1 = k - 1; k1 <= k + 1; k1++)
					{
						setBlockAndNotifyAdequately(world, i + 5, j + 1, k1, LOTRMod.orcBomb, 0);
						setBlockAndNotifyAdequately(world, i - 5, j + 1, k1, LOTRMod.slabSingle, 9);
						placeBarrel(world, random, i - 5, j + 2, k1, 5, LOTRMod.mugOrcDraught);
					}
					break;
				case 2:
					for (int i1 = i - 1; i1 <= i + 1; i1++)
					{
						setBlockAndNotifyAdequately(world, i1, j + 1, k + 5, LOTRMod.orcBomb, 0);
						setBlockAndNotifyAdequately(world, i1, j + 1, k - 5, LOTRMod.slabSingle, 9);
						placeBarrel(world, random, i1, j + 2, k - 5, 3, LOTRMod.mugOrcDraught);
					}
					break;
				case 3:
					for (int k1 = k - 1; k1 <= k + 1; k1++)
					{
						setBlockAndNotifyAdequately(world, i - 5, j + 1, k1, LOTRMod.orcBomb, 0);
						setBlockAndNotifyAdequately(world, i + 5, j + 1, k1, LOTRMod.slabSingle, 9);
						placeBarrel(world, random, i + 5, j + 2, k1, 4, LOTRMod.mugOrcDraught);
					}
					break;
			}
		}

		if (isTop)
		{
			for (int j1 = j + 1; j1 <= j + 8; j1++)
			{
				for (int k1 = k - 1; k1 <= k + 1; k1++)
				{
					setBlockAndNotifyAdequately(world, i - 7, j1, k1, LOTRMod.brick, 0);
					setBlockAndNotifyAdequately(world, i + 7, j1, k1, LOTRMod.brick, 0);
				}
				
				for (int i1 = i - 1; i1 <= i + 1; i1++)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k - 7, LOTRMod.brick, 0);
					setBlockAndNotifyAdequately(world, i1, j1, k + 7, LOTRMod.brick, 0);
				}
			}
			
			for (int k1 = k - 1; k1 <= k + 1; k1++)
			{
				setBlockAndNotifyAdequately(world, i - 7, j, k1, LOTRMod.stairsMordorBrick, 4);
				setBlockAndNotifyAdequately(world, i - 6, j + 2, k1, LOTRMod.stairsMordorBrick, 1);
				setBlockAndNotifyAdequately(world, i - 7, j + 9, k1, LOTRMod.stairsMordorBrick, 0);
				setBlockAndNotifyAdequately(world, i - 6, j + 9, k1, LOTRMod.stairsMordorBrick, 5);
				setBlockAndNotifyAdequately(world, i - 6, j + 10, k1, LOTRMod.stairsMordorBrick, 0);
				
				setBlockAndNotifyAdequately(world, i + 7, j, k1, LOTRMod.stairsMordorBrick, 5);
				setBlockAndNotifyAdequately(world, i + 6, j + 2, k1, LOTRMod.stairsMordorBrick, 0);
				setBlockAndNotifyAdequately(world, i + 7, j + 9, k1, LOTRMod.stairsMordorBrick, 1);
				setBlockAndNotifyAdequately(world, i + 6, j + 9, k1, LOTRMod.stairsMordorBrick, 4);
				setBlockAndNotifyAdequately(world, i + 6, j + 10, k1, LOTRMod.stairsMordorBrick, 1);
			}
			
			for (int i1 = i - 1; i1 <= i + 1; i1++)
			{
				setBlockAndNotifyAdequately(world, i1, j, k - 7, LOTRMod.stairsMordorBrick, 6);
				setBlockAndNotifyAdequately(world, i1, j + 2, k - 6, LOTRMod.stairsMordorBrick, 3);
				setBlockAndNotifyAdequately(world, i1, j + 9, k - 7, LOTRMod.stairsMordorBrick, 2);
				setBlockAndNotifyAdequately(world, i1, j + 9, k - 6, LOTRMod.stairsMordorBrick, 7);
				setBlockAndNotifyAdequately(world, i1, j + 10, k - 6, LOTRMod.stairsMordorBrick, 2);
				
				setBlockAndNotifyAdequately(world, i1, j, k + 7, LOTRMod.stairsMordorBrick, 7);
				setBlockAndNotifyAdequately(world, i1, j + 2, k + 6, LOTRMod.stairsMordorBrick, 2);
				setBlockAndNotifyAdequately(world, i1, j + 9, k + 7, LOTRMod.stairsMordorBrick, 3);
				setBlockAndNotifyAdequately(world, i1, j + 9, k + 6, LOTRMod.stairsMordorBrick, 6);
				setBlockAndNotifyAdequately(world, i1, j + 10, k + 6, LOTRMod.stairsMordorBrick, 3);
			}
			
			for (int j1 = j; j1 <= j + 4; j1++)
			{
				setBlockAndNotifyAdequately(world, i - 5, j1, k - 5, LOTRMod.brick, 0);
				setBlockAndNotifyAdequately(world, i - 5, j1, k + 5, LOTRMod.brick, 0);
				setBlockAndNotifyAdequately(world, i + 5, j1, k - 5, LOTRMod.brick, 0);
				setBlockAndNotifyAdequately(world, i + 5, j1, k + 5, LOTRMod.brick, 0);
			}
			
			placeBanner(world, i - 5, j + 5, k - 5, 0, 2);
			placeBanner(world, i - 5, j + 5, k + 5, 0, 2);
			placeBanner(world, i + 5, j + 5, k - 5, 0, 2);
			placeBanner(world, i + 5, j + 5, k + 5, 0, 2);
			
			setBlockAndNotifyAdequately(world, i - 5, j + 2, k - 4, LOTRMod.stairsMordorBrick, 3);
			setBlockAndNotifyAdequately(world, i - 4, j + 2, k - 5, LOTRMod.stairsMordorBrick, 1);
			
			setBlockAndNotifyAdequately(world, i - 5, j + 2, k + 4, LOTRMod.stairsMordorBrick, 2);
			setBlockAndNotifyAdequately(world, i - 4, j + 2, k + 5, LOTRMod.stairsMordorBrick, 1);
			
			setBlockAndNotifyAdequately(world, i + 5, j + 2, k - 4, LOTRMod.stairsMordorBrick, 3);
			setBlockAndNotifyAdequately(world, i + 4, j + 2, k - 5, LOTRMod.stairsMordorBrick, 0);
			
			setBlockAndNotifyAdequately(world, i + 5, j + 2, k + 4, LOTRMod.stairsMordorBrick, 2);
			setBlockAndNotifyAdequately(world, i + 4, j + 2, k + 5, LOTRMod.stairsMordorBrick, 0);
		}
	}
}
