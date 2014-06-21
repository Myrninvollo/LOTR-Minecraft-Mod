package lotr.common.world.structure;

import java.util.Random;

import lotr.common.LOTRLevelData;
import lotr.common.LOTRMod;
import lotr.common.entity.npc.LOTREntityGundabadOrcMercenaryCaptain;
import lotr.common.entity.npc.LOTREntityOrc;
import net.minecraft.init.Blocks;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

public class LOTRWorldGenGundabadCamp extends LOTRWorldGenStructureBase
{
	private LOTRWorldGenTentBase orcTentGen = new LOTRWorldGenGundabadTent(false);
	private LOTRWorldGenTentBase orcForgeTentGen = new LOTRWorldGenGundabadForgeTent(false);
	
	public LOTRWorldGenGundabadCamp()
	{
		super(false);
	}
	
	@Override
	public boolean generate(World world, Random random, int i, int j, int k)
	{
		if (world.getBlock(i, j - 1, k) != Blocks.grass)
		{
			return false;
		}

		int highestHeight = j;
		for (int i1 = i - 1; i1 <= i + 1; i1++)
		{
			for (int k1 = k - 1; k1 <= k + 1; k1++)
			{
				int j1 = world.getHeightValue(i1, k1);
				if (j1 > highestHeight)
				{
					highestHeight = j1;
				}
			}
		}
		
		for (int i1 = i - 1; i1 <= i + 1; i1++)
		{
			for (int k1 = k - 1; k1 <= k + 1; k1++)
			{
				for (int j1 = highestHeight - 1; !LOTRMod.isOpaque(world, i1, j1, k1) && j1 >= 0; j1--)
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.dirt, 1);
				}
				setBlockAndNotifyAdequately(world, i1, highestHeight, k1, Blocks.stone_slab, 3);
			}
		}
		
		setBlockAndNotifyAdequately(world, i, highestHeight, k, Blocks.crafting_table, 0);
		
		LOTREntityOrc orc = new LOTREntityGundabadOrcMercenaryCaptain(world);
		orc.setLocationAndAngles(i + 0.5D, highestHeight + 1D, k + 0.5D, world.rand.nextFloat() * 360F, 0F);
		orc.onSpawnWithEgg(null);
		orc.setHomeArea(i, highestHeight, k, 24);
		world.spawnEntityInWorld(orc);
		
		placeOrcTorch(world, i - 2, world.getHeightValue(i - 2, k - 2), k - 2);
		placeOrcTorch(world, i - 2, world.getHeightValue(i - 2, k + 2), k + 2);
		placeOrcTorch(world, i + 2, world.getHeightValue(i + 2, k - 2), k - 2);
		placeOrcTorch(world, i + 2, world.getHeightValue(i + 2, k + 2), k + 2);
		
		for (int l = 0; l < 4; l++)
		{
			int i1 = 0;
			int k1 = 0;
			switch (l)
			{
				case 0:
					i1 = i - 3 + random.nextInt(7);
					k1 = k + 6 + random.nextInt(3);
					break;
				case 1:
					i1 = i - 6 - random.nextInt(3);
					k1 = k - 3 + random.nextInt(7);
					break;
				case 2:
					i1 = i - 3 + random.nextInt(7);
					k1 = k - 6 - random.nextInt(3);
					break;
				case 3:
					i1 = i + 6 + random.nextInt(3);
					k1 = k - 3 + random.nextInt(7);
					break;
			}
			int j1 = world.getHeightValue(i1, k1);
			if (random.nextInt(6) == 0)
			{
				orcForgeTentGen.generateWithSetRotation(world, random, i1, j1, k1, l);
			}
			else
			{
				orcTentGen.generateWithSetRotation(world, random, i1, j1, k1, l);
			}
		}
		
		int[] farmCoords = null;
		farmSearchingLoop:
		for (int l = 0; l < 32; l++)
		{
			int i1 = i - 12 + random.nextInt(25);
			int k1 = k - 12 + random.nextInt(25);
			int i2 = i1 - i;
			int k2 = k1 - k;
			if (i2 * i2 + k2 * k2 > 20)
			{
				for (int i3 = i1 - 2; i3 <= i1 + 2; i3++)
				{
					for (int k3 = k1 - 2; k3 <= k1 + 2; k3++)
					{
						int j3 = world.getHeightValue(i3, k3) - 1;
						if (world.getBlock(i3, j3, k3) != Blocks.grass || (!world.isAirBlock(i3, j3 + 1, k3) && world.getBlock(i3, j3 + 1, k3) != Blocks.tallgrass))
						{
							continue farmSearchingLoop;
						}
					}
				}
				
				farmCoords = new int[] {i1, k1};
				break farmSearchingLoop;
			}
		}
		
		if (farmCoords != null)
		{
			int i1 = farmCoords[0];
			int k1 = farmCoords[1];
			int highestFarmHeight = world.getHeightValue(i1, k1);
			for (int i2 = i1 - 2; i2 <= i1 + 2; i2++)
			{
				for (int k2 = k1 - 2; k2 <= k1 + 2; k2++)
				{
					int j2 = world.getHeightValue(i2, k2);
					if (j2 > highestFarmHeight)
					{
						highestFarmHeight = j2;
					}
				}
			}
			
			for (int i2 = i1 - 2; i2 <= i1 + 2; i2++)
			{
				for (int k2 = k1 - 2; k2 <= k1 + 2; k2++)
				{
					for (int j2 = highestFarmHeight - 2; !LOTRMod.isOpaque(world, i2, j2, k2) && j2 >= 0; j2--)
					{
						setBlockAndNotifyAdequately(world, i2, j2, k2, Blocks.dirt, 0);
					}
					
					if (i2 == i1 - 2 || i2 == i1 + 2 || k2 == k1 - 2 || k2 == k1 + 2)
					{
						setBlockAndNotifyAdequately(world, i2, highestFarmHeight, k2, LOTRMod.fence, 3);
						setBlockAndNotifyAdequately(world, i2, highestFarmHeight - 1, k2, Blocks.grass, 0);
					}
					else
					{
						if (i2 == i1 && k2 == k1)
						{
							setBlockAndNotifyAdequately(world, i2, highestFarmHeight - 1, k2, Blocks.water, 0);
						}
						else
						{
							setBlockAndNotifyAdequately(world, i2, highestFarmHeight, k2, Blocks.wheat, 7);
							setBlockAndNotifyAdequately(world, i2, highestFarmHeight - 1, k2, Blocks.farmland, 7);
						}
					}
				}
			}
			
			int gate = random.nextInt(4);
			switch (gate)
			{
				case 0:
					setBlockAndNotifyAdequately(world, i1, highestFarmHeight, k1 + 2, Blocks.fence_gate, 0);
					break;
				case 1:
					setBlockAndNotifyAdequately(world, i1 - 2, highestFarmHeight, k1, Blocks.fence_gate, 1);
					break;
				case 2:
					setBlockAndNotifyAdequately(world, i1, highestFarmHeight, k1 - 2, Blocks.fence_gate, 2);
					break;
				case 3:
					setBlockAndNotifyAdequately(world, i1 + 2, highestFarmHeight, k1, Blocks.fence_gate, 3);
					break;
			}
		}
		
		for (int l = 0; l < 6; l++)
		{
			int i1 = i - 8 + random.nextInt(17);
			int k1 = k - 8 + random.nextInt(17);
			int i2 = i1 - i;
			int k2 = k1 - k;
			if (i2 * i2 + k2 * k2 > 20)
			{
				int j1 = world.getHeightValue(i1, k1);
				if (world.getBlock(i1, j1 - 1, k1) == Blocks.grass && (world.isAirBlock(i1, j1, k1) || world.getBlock(i1, j1, k1) == Blocks.tallgrass) && world.isAirBlock(i1, j1 + 1, k1))
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.fence, 3);
					placeSkull(world, random, i1, j1 + 1, k1);
				}
			}
		}
		
		for (int l = 0; l < 6; l++)
		{
			int i1 = i - 12 + random.nextInt(25);
			int k1 = k - 12 + random.nextInt(25);
			int i2 = i1 - i;
			int k2 = k1 - k;
			if (i2 * i2 + k2 * k2 > 20)
			{
				int j1 = world.getHeightValue(i1, k1);
				if (world.getBlock(i1, j1 - 1, k1) == Blocks.grass && (world.isAirBlock(i1, j1, k1) || world.getBlock(i1, j1, k1) == Blocks.tallgrass))
				{
					placeSkull(world, random, i1, j1, k1);
				}
			}
		}
		
		if (usingPlayer == null)
		{
			LOTRLevelData.gundabadCampLocations.add(new ChunkCoordinates(i, j, k));
		}
		
		return true;
	}
}
