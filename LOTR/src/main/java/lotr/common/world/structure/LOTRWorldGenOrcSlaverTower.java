package lotr.common.world.structure;

import java.util.Random;

import lotr.common.LOTRMod;
import lotr.common.entity.npc.LOTREntityMordorOrc;
import lotr.common.entity.npc.LOTREntityMordorOrcSlaver;
import lotr.common.entity.npc.LOTREntityOrc;
import lotr.common.world.biome.LOTRBiomeGenNurn;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class LOTRWorldGenOrcSlaverTower extends LOTRWorldGenStructureBase
{
	public LOTRWorldGenOrcSlaverTower(boolean flag)
	{
		super(flag);
	}
	
	public boolean generate(World world, Random random, int i, int j, int k)
	{
		if (restrictions)
		{
			if (world.getBlock(i, j - 1, k) != Blocks.grass || !(world.getBiomeGenForCoords(i, k) instanceof LOTRBiomeGenNurn))
			{
				return false;
			}
		}
		
		int height = 5 + random.nextInt(4);
		j += height;
		
		int rotation = random.nextInt(4);
		
		if (!restrictions && usingPlayer != null)
		{
			rotation = usingPlayerRotation();
		}
		
		switch (rotation)
		{
			case 0:
				k += 1;
				break;
			case 1:
				i -= 1;
				break;
			case 2:
				k -= 1;
				break;
			case 3:
				i += 1;
				break;
		}
		
		if (restrictions)
		{
			for (int i1 = i - 3; i1 <= i + 3; i1++)
			{
				for (int k1 = k - 3; k1 <= k + 3; k1++)
				{
					int j1 = world.getHeightValue(i1, k1) - 1;
					Block l = world.getBlock(i1, j1, k1);
					if (l == Blocks.grass)
					{
						continue;
					}
					return false;
				}
			}
		}
		
		for (int i1 = i - 3; i1 <= i + 3; i1++)
		{
			for (int k1 = k - 3; k1 <= k + 3; k1++)
			{
				setBlockAndNotifyAdequately(world, i1, j, k1, LOTRMod.planks, 3);
				setBlockAndNotifyAdequately(world, i1, j + 6, k1, LOTRMod.planks, 3);
				
				if (Math.abs(i1 - i) == 3 || Math.abs(k1 - k) == 3)
				{
					setBlockAndNotifyAdequately(world, i1, j + 1, k1, LOTRMod.fence, 3);
					setBlockAndNotifyAdequately(world, i1, j + 5, k1, LOTRMod.fence, 3);
					setBlockAndNotifyAdequately(world, i1, j + 7, k1, LOTRMod.fence, 3);
				}
			}
		}
		
		for (int i1 = i - 3; i1 <= i + 3; i1 += 6)
		{
			for (int k1 = k - 3; k1 <= k + 3; k1 += 6)
			{
				for (int j1 = j + 5; (j1 >= j || !LOTRMod.isOpaque(world, i1, j1, k1)) && j1 >= 0; j1--)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.wood, 3);
					if (world.getBlock(i1, j1 - 1, k1) == Blocks.grass)
					{
						setBlockAndNotifyAdequately(world, i1, j1 - 1, k1, Blocks.dirt, 0);
					}
				}
			}
		}
		
		for (int j1 = j + 2; j1 <= j + 4; j1++)
		{
			setBlockAndNotifyAdequately(world, i - 2, j1, k - 3, LOTRMod.fence, 3);
			setBlockAndNotifyAdequately(world, i - 2, j1, k + 3, LOTRMod.fence, 3);
			setBlockAndNotifyAdequately(world, i + 2, j1, k - 3, LOTRMod.fence, 3);
			setBlockAndNotifyAdequately(world, i + 2, j1, k + 3, LOTRMod.fence, 3);
			setBlockAndNotifyAdequately(world, i - 3, j1, k - 2, LOTRMod.fence, 3);
			setBlockAndNotifyAdequately(world, i + 3, j1, k - 2, LOTRMod.fence, 3);
			setBlockAndNotifyAdequately(world, i - 3, j1, k + 2, LOTRMod.fence, 3);
			setBlockAndNotifyAdequately(world, i + 3, j1, k + 2, LOTRMod.fence, 3);
		}
		
		for (int j1 = j + 11; (j1 >= j || !LOTRMod.isOpaque(world, i, j1, k)) && j1 >= 0; j1--)
		{
			setBlockAndNotifyAdequately(world, i, j1, k, LOTRMod.wood, 3);
			if (world.getBlock(i, j1 - 1, k) == Blocks.grass)
			{
				setBlockAndNotifyAdequately(world, i, j1 - 1, k, Blocks.dirt, 0);
			}
			
			if (j1 <= j + 6)
			{
				setBlockAndNotifyAdequately(world, i, j1, k - 1, Blocks.ladder, 2);
			}
		}
		
		setBlockAndNotifyAdequately(world, i, j + 1, k - 1, Blocks.trapdoor, 0);
		setBlockAndNotifyAdequately(world, i, j + 7, k - 1, Blocks.trapdoor, 0);
		
		placeOrcTorch(world, i - 3, j + 8, k - 3);
		placeOrcTorch(world, i - 3, j + 8, k + 3);
		placeOrcTorch(world, i + 3, j + 8, k - 3);
		placeOrcTorch(world, i + 3, j + 8, k + 3);
		
		setBlockAndNotifyAdequately(world, i, j + 12, k, LOTRMod.fence, 3);
		setBlockAndNotifyAdequately(world, i, j + 13, k, LOTRMod.fence, 3);
		
		setBlockAndNotifyAdequately(world, i, j + 12, k - 1, LOTRMod.fence, 3);
		setBlockAndNotifyAdequately(world, i, j + 12, k + 1, LOTRMod.fence, 3);
		setBlockAndNotifyAdequately(world, i - 1, j + 12, k, LOTRMod.fence, 3);
		setBlockAndNotifyAdequately(world, i + 1, j + 12, k, LOTRMod.fence, 3);
		
		placeOrcTorch(world, i, j + 14, k);
		placeOrcTorch(world, i, j + 13, k - 1);
		placeOrcTorch(world, i, j + 13, k + 1);
		placeOrcTorch(world, i - 1, j + 13, k);
		placeOrcTorch(world, i + 1, j + 13, k);
		
		LOTREntityMordorOrcSlaver slaver = new LOTREntityMordorOrcSlaver(world);
		slaver.setLocationAndAngles(i + 1.5D, j + 7, k + 1.5D, 0F, 0F);
		slaver.onSpawnWithEgg(null);
		world.spawnEntityInWorld(slaver);
		slaver.setHomeArea(i, j + 6, k, 12);
		
		int orcs = 2 + random.nextInt(3);
		for (int l = 0; l < orcs; l++)
		{
			LOTREntityOrc orc = new LOTREntityMordorOrc(world);
			orc.setLocationAndAngles(i + 1.5D, j + 1, k + 1.5D, 0F, 0F);
			orc.onSpawnWithEgg(null);
			orc.isNPCPersistent = true;
			world.spawnEntityInWorld(orc);
			orc.setHomeArea(i, j + 1, k, 8);
		}
		
		return true;
	}
}
