package lotr.common.world.feature;

import java.util.Random;

import lotr.common.LOTRMod;
import lotr.common.world.structure.LOTRWorldGenElfHouse;
import lotr.common.world.structure.LOTRWorldGenElfLordHouse;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public class LOTRWorldGenMallornLarge extends WorldGenAbstractTree
{
	private boolean notify;
	
    public LOTRWorldGenMallornLarge(boolean flag)
    {
		super(flag);
		notify = flag;
	}

	@Override
    public boolean generate(World world, Random random, int i, int j, int k)
    {
		return generateWithOptionalForceAndReturnHeight(world, random, i, j, k, false) > 0;
	}
	
	public int generateWithOptionalForceAndReturnHeight(World world, Random random, int i, int j, int k, boolean forceGeneration)
	{
        int height = random.nextInt(20) + 35;
        boolean flag = true;

        if ((j >= 1 && j + height + 5 <= 256) || forceGeneration)
        {
			for (int i1 = i - 1; i1 <= i + 1; i1++)
			{
				for (int k1 = k - 1; k1 <= k + 1; k1++)
				{
					for (int j1 = j; j1 <= j + 1 + height; j1++)
					{
						int width = 1;

						if (j1 == j)
						{
							width = 0;
						}

						if (j1 >= j + 1 + height - 2)
						{
							width = 2;
						}

						for (int i2 = i1 - width; i2 <= i1 + width && flag; i2++)
						{
							for (int k2 = k1 - width; k2 <= k1 + width && flag; k2++)
							{
								if (j1 >= 0 && j1 < 256)
								{
									Block block = world.getBlock(i2, j1, k2);

									if (!forceGeneration && block.getMaterial() != Material.air && !block.isLeaves(world, i2, j1, k2) && block != Blocks.grass && block != Blocks.dirt && !block.isWood(world, i2, j1, k2))
									{
										flag = false;
									}
								}
								else if (!forceGeneration)
								{
									flag = false;
								}
							}
						}
					}
				}
			}

            if (!flag && !forceGeneration)
            {
                return 0;
            }
            else
            {
				for (int i1 = i - 1; i1 <= i + 1; i1++)
				{
					for (int k1 = k - 1; k1 <= k + 1; k1++)
					{
						Block block = world.getBlock(i1, j - 1, k1);
						if ((block == Blocks.grass || block == Blocks.dirt) && j < 256 - height - 5)
						{
							continue;
						}
						else if (!forceGeneration)
						{
							return 0;
						}
					}
				}
				
				for (int i1 = i - 1; i1 <= i + 1; i1++)
				{
					for (int k1 = k - 1; k1 <= k + 1; k1++)
					{
						if (!forceGeneration)
						{
							setBlockAndNotifyAdequately(world, i1, j - 1, k1, Blocks.dirt, 0);
						}

						for (int j1 = 0; j1 < height; j1++)
						{
							Block block = world.getBlock(i1, j + j1, k1);
							if (block.getMaterial() == Material.air || block.isLeaves(world, i1, j + j1, k1))
							{
								setBlockAndNotifyAdequately(world, i1, j + j1, k1, LOTRMod.wood, 1);
							}
						}
					}
				}
				
				int angle = 0;
				while (angle < 360)
				{
					angle += 10 + random.nextInt(20);
					float angleR = (float)angle / 180F * (float)Math.PI;
					float sin = MathHelper.sin(angleR);
					float cos = MathHelper.cos(angleR);
					int boughLength = 12 + random.nextInt(10);
					int boughThickness = Math.round((float)boughLength / 25F * 1.5F);
					int boughBaseHeight = j + MathHelper.floor_double(height * (0.9F + random.nextFloat() * 0.1F));
					int boughHeight = 3 + random.nextInt(4);
					
					for (int l = 0; l < boughLength; l++)
					{
						int i1 = i + Math.round(sin * l);
						int k1 = k + Math.round(cos * l);
						int j1 = boughBaseHeight + Math.round((float)l / (float)boughLength * (float)boughHeight);
						int range = boughThickness - Math.round((float)l / (float)boughLength * (float)boughThickness * 0.5F);
						
						for (int i2 = i1 - range; i2 <= i1 + range; i2++)
						{
							for (int j2 = j1 - range; j2 <= j1 + range; j2++)
							{
								for (int k2 = k1 - range; k2 <= k1 + range; k2++)
								{
									Block block = world.getBlock(i2, j2, k2);
									if (block.getMaterial() == Material.air || block.isLeaves(world, i2, j2, k2))
									{
										setBlockAndNotifyAdequately(world, i2, j2, k2, LOTRMod.wood, 13);
									}
								}
							}
						}
						
						int branch_angle = angle + random.nextInt(360);
						float branch_angleR = (float)branch_angle / 180F * (float)Math.PI;
						float branch_sin = MathHelper.sin(branch_angleR);
						float branch_cos = MathHelper.cos(branch_angleR);
						int branchLength = 7 + random.nextInt(6);
						int branchHeight = random.nextInt(6);
						int leafRange = 3;
						
						for (int l1 = 0; l1 < branchLength; l1++)
						{
							int i2 = i1 + Math.round(branch_sin * l1);
							int k2 = k1 + Math.round(branch_cos * l1);
							int j2 = j1 + Math.round((float)l1 / (float)branchLength * (float)branchHeight);
							
							for (int j3 = j2; j3 >= j2 - 1; j3--)
							{
								Block block = world.getBlock(i2, j3, k2);
								if (block.getMaterial() == Material.air || block.isLeaves(world, i2, j3, k2))
								{
									setBlockAndNotifyAdequately(world, i2, j3, k2, LOTRMod.wood, 13);
								}
							}
							
							if (l1 == branchLength - 1)
							{
								for (int i3 = i2 - leafRange; i3 <= i2 + leafRange; i3++)
								{
									for (int j3 = j2 - leafRange; j3 <= j2 + leafRange; j3++)
									{
										for (int k3 = k2 - leafRange; k3 <= k2 + leafRange; k3++)
										{
											int i4 = i3 - i2;
											int j4 = j3 - j2;
											int k4 = k3 - k2;
											int dist = i4 * i4 + j4 * j4 + k4 * k4;
											if (dist < (leafRange - 1) * (leafRange - 1) || (dist < leafRange * leafRange && random.nextInt(3) != 0))
											{
												Block block2 = world.getBlock(i3, j3, k3);
												if (block2.getMaterial() == Material.air || block2.isLeaves(world, i3, j3, k3))
												{
													setBlockAndNotifyAdequately(world, i3, j3, k3, LOTRMod.leaves, 1);
												}
											}
										}
									}
								}
							}
						}
					}
				}
				
				if (!notify && !forceGeneration)
				{
					int houseHeight = MathHelper.floor_double(height * (0.4F + random.nextFloat() * 0.3F));
					boolean isElfLordTree = random.nextInt(10) == 0;
					if (!isElfLordTree || !new LOTRWorldGenElfLordHouse(false).generate(world, random, i, j + houseHeight, k))
					{
						new LOTRWorldGenElfHouse(false).generate(world, random, i, j + houseHeight, k);
					}
				}
				
				return height;
            }
        }
        else
        {
            return 0;
        }
	}
}