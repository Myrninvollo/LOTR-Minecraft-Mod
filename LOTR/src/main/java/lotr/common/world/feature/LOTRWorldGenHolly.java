package lotr.common.world.feature;

import java.util.Random;

import lotr.common.LOTRMod;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public class LOTRWorldGenHolly extends WorldGenAbstractTree
{
	private int extraTrunkWidth = 0;
	
    public LOTRWorldGenHolly(boolean flag)
    {
		super(flag);
	}
	
	public LOTRWorldGenHolly setLarge()
	{
		extraTrunkWidth = 1;
		return this;
	}
	
	@Override
    public boolean generate(World world, Random random, int i, int j, int k)
    {
        int height = 9 + random.nextInt(6);
		if (extraTrunkWidth > 0)
		{
			height += 10 + random.nextInt(4);
		}
        boolean flag = true;

        if (j >= 1 && j + height + 1 <= 256)
        {
            for (int j1 = j; j1 <= j + 1 + height; j1++)
            {
                int range = 1;
				if (j1 == j)
				{
					range = 0;
				}
                if (j1 > j + 2 && j1 < j + height - 2)
				{
					range = 2;
					if (extraTrunkWidth > 0)
					{
						range++;
					}
				}

                for (int i1 = i - range; i1 <= i + range + extraTrunkWidth && flag; i1++)
                {
                    for (int k1 = k - range; k1 <= k + range + extraTrunkWidth && flag; k1++)
                    {
                        if (j1 >= 0 && j1 < 256)
                        {
                            Block block = world.getBlock(i1, j1, k1);
                            if (block.getMaterial() != Material.air && !block.isLeaves(world, i1, j1, k1) && block != Blocks.grass && block != Blocks.dirt && !block.isWood(world, i1, j1, k1))
                            {
                                flag = false;
                            }
                        }
                        else
                        {
                            flag = false;
                        }
                    }
                }
            }

            if (!flag)
            {
                return false;
            }
            else
            {
				boolean flag1 = true;
				for (int i1 = i; i1 <= i + extraTrunkWidth && flag1; i1++)
				{
					for (int k1 = k; k1 <= k + extraTrunkWidth && flag1; k1++)
					{
						Block block = world.getBlock(i1, j - 1, k1);
						flag1 = ((block == Blocks.grass || block == Blocks.dirt) && j < 256 - height - 1);
					}
				}
				
				if (flag1)
				{
					for (int i1 = i; i1 <= i + extraTrunkWidth; i1++)
					{
						for (int k1 = k; k1 <= k + extraTrunkWidth; k1++)
						{
							setBlockAndNotifyAdequately(world, i1, j - 1, k1, Blocks.dirt, 0);
						}
					}
					
					int leafStop = 2 + random.nextInt(2);
					for (int j1 = height; j1 > leafStop; j1--)
					{
						if (j1 == height)
						{
							for (int i1 = 0; i1 <= extraTrunkWidth; i1++)
							{
								for (int k1 = 0; k1 <= extraTrunkWidth; k1++)
								{
									setBlockAndNotifyAdequately(world, i + i1, j + j1, k + k1, LOTRMod.leaves2, 2);
								}
							}
						}
						else if (j1 > height - 3 || j1 == leafStop + 1)
						{
							for (int i1 = -1; i1 <= 1 + extraTrunkWidth; i1++)
							{
								for (int k1 = -1; k1 <= 1 + extraTrunkWidth; k1++)
								{
									int i2 = i1;
									if (i2 > 0)
									{
										i2 -= extraTrunkWidth;
									}
									int k2 = k1;
									if (k2 > 0)
									{
										k2 -= extraTrunkWidth;
									}
									if (j1 != height - 1 || Math.abs(i2) != 1 || Math.abs(k2) != 1)
									{
										setBlockAndNotifyAdequately(world, i + i1, j + j1, k + k1, LOTRMod.leaves2, 2);
									}
								}
							}
						}
						else
						{
							for (int i1 = -3; i1 <= 3 + extraTrunkWidth; i1++)
							{
								for (int k1 = -3; k1 <= 3 + extraTrunkWidth; k1++)
								{
									int i2 = i1;
									if (i2 > 0)
									{
										i2 -= extraTrunkWidth;
									}
									int k2 = k1;
									if (k2 > 0)
									{
										k2 -= extraTrunkWidth;
									}
									if (j1 % 2 == 0 || Math.abs(i2) != 2 || Math.abs(k2) != 2)
									{
										if ((Math.abs(i2) < 3 && Math.abs(k2) < 3) || (extraTrunkWidth > 0 && j1 % 2 == 0 && (i2 == 0 || k2 == 0)))
										{
											setBlockAndNotifyAdequately(world, i + i1, j + j1, k + k1, LOTRMod.leaves2, 2);
										}
									}
								}
							}
						}
					}

                    for (int j1 = 0; j1 < height; j1++)
                    {
						for (int i1 = 0; i1 <= extraTrunkWidth; i1++)
						{
							for (int k1 = 0; k1 <= extraTrunkWidth; k1++)
							{
								Block block = world.getBlock(i + i1, j + j1, k + k1);
								if (block.getMaterial() == Material.air || block.isLeaves(world, i + i1, j + j1, k + k1))
								{
									setBlockAndNotifyAdequately(world, i + i1, j + j1, k + k1, LOTRMod.wood2, 2);
								}
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
        else
        {
            return false;
        }
    }
}
