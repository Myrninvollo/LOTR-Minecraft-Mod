package lotr.common.world.structure;

import java.util.Random;

import lotr.common.LOTRMod;
import lotr.common.entity.npc.LOTREntityMordorOrcSpiderKeeper;
import lotr.common.entity.npc.LOTREntityMordorSpider;
import lotr.common.world.biome.LOTRBiomeGenNanUngol;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class LOTRWorldGenMordorSpiderPit extends LOTRWorldGenStructureBase
{
	public LOTRWorldGenMordorSpiderPit(boolean flag)
	{
		super(flag);
	}
	
	@Override
	public boolean generate(World world, Random random, int i, int j, int k)
	{
		if (restrictions)
		{
			if (world.getBlock(i, j - 1, k) != LOTRMod.rock || !(world.getBiomeGenForCoords(i, k) instanceof LOTRBiomeGenNanUngol))
			{
				return false;
			}
		}
		
		int rotation = random.nextInt(4);
		
		int radius = 8;
		int radiusPlusOne = radius + 1;
		
		if (!restrictions && usingPlayer != null)
		{
			rotation = usingPlayerRotation();
			switch (rotation)
			{
				case 0:
					k += radiusPlusOne;
					break;
				case 1:
					i -= radiusPlusOne;
					break;
				case 2:
					k -= radiusPlusOne;
					break;
				case 3:
					i += radiusPlusOne;
					break;
			}
		}
		
		if (restrictions)
		{
			int minHeight = j;
			int maxHeight = j;
			
			for (int i1 = i - radiusPlusOne; i1 <= i + radiusPlusOne; i1++)
			{
				for (int k1 = k - radiusPlusOne; k1 <= k + radiusPlusOne; k1++)
				{
					int i2 = i1 - i;
					int k2 = k1 - k;
					if (i2 * i2 + k2 * k2 > radiusPlusOne * radiusPlusOne)
					{
						continue;
					}
					
					int j1 = world.getHeightValue(i1, k1);
					Block block = world.getBlock(i1, j1 - 1, k1);
					if (block != LOTRMod.rock)
					{
						return false;
					}
					
					if (j1 < minHeight)
					{
						minHeight = j1;
					}
					if (j1 > maxHeight)
					{
						maxHeight = j1;
					}
				}
			}

			if (maxHeight - minHeight > 5)
			{
				return false;
			}
			
			j = maxHeight;
		}
		
		int base = j - 8;
		
		int wallThresholdMin = (int)(((double)radius - 0.25D) * ((double)radius - 0.25D));
		int wallThresholdMax = radiusPlusOne * radiusPlusOne;
		
		for (int i1 = i - radiusPlusOne; i1 <= i + radiusPlusOne; i1++)
		{
			for (int k1 = k - radiusPlusOne; k1 <= k + radiusPlusOne; k1++)
			{
				int i2 = i1 - i;
				int k2 = k1 - k;
				int distSq = i2 * i2 + k2 * k2;
				if (distSq >= wallThresholdMax)
				{
					continue;
				}
				
				if (distSq >= wallThresholdMin)
				{
					setBlockAndNotifyAdequately(world, i1, j + 3, k1, LOTRMod.fence, 3);
					setBlockAndNotifyAdequately(world, i1, j + 2, k1, LOTRMod.planks, 3);
					
					for (int j1 = j + 1; j1 >= base && j1 >= 0; j1--)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.brick, 0);
					}
				}
				else
				{
					for (int j1 = j + 3; j1 >= base && j1 >= 0; j1--)
					{
						if (j1 == base)
						{
							setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.rock, 0);
						}
						else
						{
							setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
						}
					}
				}
			}
		}
		
		placeFenceAndTorch(world, i - 1, j - 3, k - radius + 1);
		placeFenceAndTorch(world, i + 1, j - 3, k - radius + 1);
		placeFenceAndTorch(world, i - 1, j - 3, k + radius - 1);
		placeFenceAndTorch(world, i + 1, j - 3, k + radius - 1);
		
		placeFenceAndTorch(world, i - radius + 1, j - 3, k - 1);
		placeFenceAndTorch(world, i - radius + 1, j - 3, k + 1);
		placeFenceAndTorch(world, i + radius - 1, j - 3, k - 1);
		placeFenceAndTorch(world, i + radius - 1, j - 3, k + 1);
		
		if (rotation == 0)
		{
			int k1 = k - radius;
			
			setBlockAndNotifyAdequately(world, i, j + 3, k1, Blocks.fence_gate, 0);
			
			for (int j1 = j + 2; !LOTRMod.isOpaque(world, i, j1, k1 - 1) && j1 >= 0; j1--)
			{
				setBlockAndNotifyAdequately(world, i, j1, k1 - 1, Blocks.ladder, 2);
			}
			
			for (int j1 = j + 2; !LOTRMod.isOpaque(world, i, j1, k1 + 1) && j1 >= 0; j1--)
			{
				setBlockAndNotifyAdequately(world, i, j1, k1 + 1, Blocks.ladder, 3);
			}
		}
		
		if (rotation == 1)
		{
			int i1 = i + radius;
			
			setBlockAndNotifyAdequately(world, i1, j + 3, k, Blocks.fence_gate, 1);
			
			for (int j1 = j + 2; !LOTRMod.isOpaque(world, i1 - 1, j1, k) && j1 >= 0; j1--)
			{
				setBlockAndNotifyAdequately(world, i1 - 1, j1, k, Blocks.ladder, 4);
			}
			
			for (int j1 = j + 2; !LOTRMod.isOpaque(world, i1 + 1, j1, k) && j1 >= 0; j1--)
			{
				setBlockAndNotifyAdequately(world, i1 + 1, j1, k, Blocks.ladder, 5);
			}
		}
		
		if (rotation == 2)
		{
			int k1 = k + radius;
			
			setBlockAndNotifyAdequately(world, i, j + 3, k1, Blocks.fence_gate, 2);
			
			for (int j1 = j + 2; !LOTRMod.isOpaque(world, i, j1, k1 - 1) && j1 >= 0; j1--)
			{
				setBlockAndNotifyAdequately(world, i, j1, k1 - 1, Blocks.ladder, 2);
			}
			
			for (int j1 = j + 2; !LOTRMod.isOpaque(world, i, j1, k1 + 1) && j1 >= 0; j1--)
			{
				setBlockAndNotifyAdequately(world, i, j1, k1 + 1, Blocks.ladder, 3);
			}
		}
		
		if (rotation == 3)
		{
			int i1 = i - radius;
			
			setBlockAndNotifyAdequately(world, i1, j + 3, k, Blocks.fence_gate, 3);
			
			for (int j1 = j + 2; !LOTRMod.isOpaque(world, i1 - 1, j1, k) && j1 >= 0; j1--)
			{
				setBlockAndNotifyAdequately(world, i1 - 1, j1, k, Blocks.ladder, 4);
			}
			
			for (int j1 = j + 2; !LOTRMod.isOpaque(world, i1 + 1, j1, k) && j1 >= 0; j1--)
			{
				setBlockAndNotifyAdequately(world, i1 + 1, j1, k, Blocks.ladder, 5);
			}
		}
		
		LOTREntityMordorOrcSpiderKeeper spiderKeeper = new LOTREntityMordorOrcSpiderKeeper(world);
		spiderKeeper.setLocationAndAngles(i + 0.5D, base + 1, k + 0.5D, 0F, 0F);
		spiderKeeper.liftSpawnRestrictions = true;
		spiderKeeper.onSpawnWithEgg(null);
		spiderKeeper.liftSpawnRestrictions = false;
		spiderKeeper.setHomeArea(i, base, k, 8);
		world.spawnEntityInWorld(spiderKeeper);
		
		int spiders = 2 + random.nextInt(4);
		for (int l = 0; l < spiders; l++)
		{
			LOTREntityMordorSpider spider = new LOTREntityMordorSpider(world);
			spider.setLocationAndAngles(i + 0.5D, base + 1, k + 0.5D, 0F, 0F);
			spider.isNPCPersistent = true;
			spider.onSpawnWithEgg(null);
			spider.setHomeArea(i, base, k, 8);
			world.spawnEntityInWorld(spider);
		}
		
		return true;
	}
	
	private void placeFenceAndTorch(World world, int i, int j, int k)
	{
		setBlockAndNotifyAdequately(world, i, j, k, LOTRMod.fence, 3);
		placeOrcTorch(world, i, j + 1, k);
	}
}
