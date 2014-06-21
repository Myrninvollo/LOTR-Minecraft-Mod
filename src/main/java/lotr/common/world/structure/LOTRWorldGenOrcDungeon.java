package lotr.common.world.structure;

import java.util.Iterator;
import java.util.Random;

import lotr.common.entity.npc.LOTREntityGundabadOrc;
import lotr.common.entity.npc.LOTREntityOrc;
import lotr.common.world.biome.LOTRBiome;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenBase.SpawnListEntry;

public class LOTRWorldGenOrcDungeon extends LOTRWorldGenStructureBase
{
	public LOTRWorldGenOrcDungeon(boolean flag)
	{
		super(flag);
	}
	
	@Override
    public boolean generate(World world, Random random, int i, int j, int k)
    {
        int xSize = random.nextInt(3) + 2;
		int ySize = 3;
        int zSize = random.nextInt(3) + 2;
        int height = 0;
		
		if (!restrictions && usingPlayer != null)
		{
			int rotation = usingPlayerRotation();
			switch (rotation)
			{
				case 0:
					k += zSize + 2;
					break;
				case 1:
					i -= xSize + 2;
					break;
				case 2:
					k -= zSize + 2;
					break;
				case 3:
					i += xSize + 2;
					break;
			}
		}

		if (restrictions)
		{
			for (int i1 = i - xSize - 1; i1 <= i + xSize + 1; i1++)
			{
				for (int j1 = j - 1; j1 <= j + ySize + 1; j1++)
				{
					for (int k1 = k - zSize - 1; k1 <= k + zSize + 1; k1++)
					{
						Material material = world.getBlock(i1, j1, k1).getMaterial();

						if (j1 == j - 1 && !material.isSolid())
						{
							return false;
						}

						if (j1 == j + ySize + 1 && !material.isSolid())
						{
							return false;
						}

						if ((i1 == i - xSize - 1 || i1 == i + xSize + 1 || k1 == k - zSize - 1 || k1 == k + zSize + 1) && j1 == j && world.isAirBlock(i1, j1, k1) && world.isAirBlock(i1, j1 + 1, k1))
						{
							height++;
						}
					}
				}
			}
		}
		else
		{
			height = 3;
		}

        if (height >= 1 && height <= 5)
        {
            for (int i1 = i - xSize - 1; i1 <= i + xSize + 1; i1++)
            {
                for (int j1 = j + ySize; j1 >= j - 1; j1--)
                {
                    for (int k1 = k - zSize - 1; k1 <= k + zSize + 1; k1++)
                    {
                        if (i1 != i - xSize - 1 && j1 != j - 1 && k1 != k - zSize - 1 && i1 != i + xSize + 1 && j1 != j + ySize + 1 && k1 != k + zSize + 1)
                        {
                            setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.air, 0);
                        }
                        else if (!restrictions || world.getBlock(i1, j1, k1).getMaterial().isSolid())
                        {
                            if (random.nextInt(4) != 0)
                            {
                                setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.stonebrick, 1 + random.nextInt(2));
                            }
                            else
                            {
                                setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.stonebrick, 0);
                            }
                        }
                    }
                }
            }

            int chestAttempts = 0;
            while (chestAttempts < 2)
            {
                int thisChestAttempts = 0;
                while (true)
                {
                    if (thisChestAttempts < 3)
                    {
                        chestLoop:
                        {
                            int i1 = i + random.nextInt(xSize * 2 + 1) - xSize;
                            int k1 = k + random.nextInt(zSize * 2 + 1) - zSize;

                            if (world.isAirBlock(i1, j, k1))
                            {
                                boolean flag = false;

                                if (world.getBlock(i1 - 1, j, k1).getMaterial().isSolid())
                                {
                                    flag = true;
                                }

                                if (world.getBlock(i1 + 1, j, k1).getMaterial().isSolid())
                                {
									flag = true;
                                }

                                if (world.getBlock(i1, j, k1 - 1).getMaterial().isSolid())
                                {
                                    flag = true;
                                }

                                if (world.getBlock(i1, j, k1 + 1).getMaterial().isSolid())
                                {
                                    flag = true;
                                }

                                if (flag)
                                {
                                    setBlockAndNotifyAdequately(world, i1, j, k1, Blocks.chest, 0);
                                    LOTRChestContents.fillChest(world, random, i1, j, k1, LOTRChestContents.ORC_DUNGEON);

                                    break chestLoop;
                                }
                            }
                            ++thisChestAttempts;
                            continue;
                        }
                    }
                    ++chestAttempts;
                    break;
                }
            }
			
			Class entityClass = LOTREntityGundabadOrc.class;
			BiomeGenBase biome = world.getBiomeGenForCoords(i, k);
			if (biome instanceof LOTRBiome)
			{
				Iterator it = ((LOTRBiome)biome).spawnableEvilList.iterator();
				while (it.hasNext())
				{
					SpawnListEntry next = (SpawnListEntry)it.next();
					if (LOTREntityOrc.class.isAssignableFrom(next.entityClass))
					{
						entityClass = next.entityClass;
						break;
					}
				}
			}
			
			placeMobSpawner(world, i, j, k, entityClass);
			
			int pillars = random.nextInt(6);
			pillarLoop:
			for (int l = 0; l < pillars; l++)
			{
				int i1 = i + random.nextInt(xSize * 2 + 1) - xSize;
				int k1 = k + random.nextInt(zSize * 2 + 1) - zSize;
				for (int j1 = j + ySize; j1 >= j; j1--)
				{
					if (!world.isAirBlock(i1, j1, k1))
					{
						continue pillarLoop;
					}
				}
				for (int j1 = j + ySize; j1 >= j; j1--)
				{
					if (random.nextInt(4) != 0)
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.stonebrick, 1 + random.nextInt(2));
					}
					else
					{
						setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.stonebrick, 0);
					}
				}
			}

            return true;
        }
        else
        {
            return false;
        }
    }
}
