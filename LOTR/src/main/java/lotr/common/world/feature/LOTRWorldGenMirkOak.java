package lotr.common.world.feature;

import java.util.Random;

import lotr.common.LOTRMod;
import lotr.common.world.structure.LOTRWorldGenWoodElfPlatform;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public class LOTRWorldGenMirkOak extends WorldGenAbstractTree
{
	private int trunkHeight;
	private int randomTrunkIncrease;
	private int trunkWidth;
	private int canopyRadius;
	private boolean isRed;
	private boolean disableDecay;
	private boolean hasVines;
	private boolean restrictions = true;
	
    public LOTRWorldGenMirkOak(boolean flag, int i, int j, int k, int l)
    {
		super(flag);
		trunkHeight = i;
		randomTrunkIncrease = j;
		trunkWidth = k;
		canopyRadius = l;
	}
	
	public LOTRWorldGenMirkOak setRed()
	{
		isRed = true;
		return this;
	}
	
	public LOTRWorldGenMirkOak disableDecay()
	{
		disableDecay = true;
		return this;
	}
	
	public LOTRWorldGenMirkOak setHasVines()
	{
		hasVines = true;
		return this;
	}
	
	public LOTRWorldGenMirkOak disableRestrictions()
	{
		restrictions = false;
		return this;
	}

	@Override
    public boolean generate(World world, Random random, int i, int j, int k)
    {
        int height = random.nextInt(randomTrunkIncrease) + trunkHeight;
        boolean flag = true;

        if (!restrictions || (j >= 1 && j + height + 1 <= 256))
        {
			if (restrictions)
			{
				for (int j1 = j; j1 <= j + 1 + height; j1++)
				{
					int range = trunkWidth + 1;
					
					if (j1 == j)
					{
						range = trunkWidth;
					}

					if (j1 >= j + 1 + height - 2)
					{
						range = canopyRadius + 1;
					}

					for (int i1 = i - range; i1 <= i + range && flag; i1++)
					{
						for (int k1 = k - range; k1 <= k + range && flag; k1++)
						{
							if (j1 >= 0 && j1 < 256)
							{
								Block block = world.getBlock(i1, j1, k1);
								if (block != Blocks.air && !block.isLeaves(world, i1, j1, k1) && block != Blocks.grass && block != Blocks.dirt && block != Blocks.vine && !block.isWood(world, i1, j1, k1))
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
			
				for (int i1 = i - trunkWidth; i1 <= i + trunkWidth && flag; i1++)
				{
					for (int k1 = k - trunkWidth; k1 <= k + trunkWidth && flag; k1++)
					{
						Block block = world.getBlock(i1, j - 1, k1);
						if (block != Blocks.grass && block != Blocks.dirt)
						{
							flag = false;
						}
					}
				}

				if (!flag)
				{
					return false;
				}
			}

			if (restrictions)
			{
				for (int i1 = i - trunkWidth; i1 <= i + trunkWidth; i1++)
				{
					for (int k1 = k - trunkWidth; k1 <= k + trunkWidth; k1++)
					{
						setBlockAndNotifyAdequately(world, i1, j - 1, k1, Blocks.dirt, 0);
					}
				}
			}
			
			for (int j1 = 0; j1 < height; j1++)
			{
				for (int i1 = i - trunkWidth; i1 <= i + trunkWidth; i1++)
				{
					for (int k1 = k - trunkWidth; k1 <= k + trunkWidth; k1++)
					{
						setBlockAndNotifyAdequately(world, i1, j + j1, k1, LOTRMod.wood, 2);
					}
				}
			}
			
			if (trunkWidth >= 2)
			{
				int angle = 0;
				while (angle < 360)
				{
					angle += 15 + random.nextInt(25);
					float angleR = (float)angle / 180F * (float)Math.PI;
					float sin = MathHelper.sin(angleR);
					float cos = MathHelper.cos(angleR);
					int boughLength = 8 + random.nextInt(8);
					int boughThickness = Math.round((float)boughLength / 20F * 1.5F);
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
										setBlockAndNotifyAdequately(world, i2, j2, k2, LOTRMod.wood, 14);
									}
								}
							}
						}
						
						int branch_angle = angle + random.nextInt(360);
						float branch_angleR = (float)branch_angle / 180F * (float)Math.PI;
						float branch_sin = MathHelper.sin(branch_angleR);
						float branch_cos = MathHelper.cos(branch_angleR);
						int branchLength = 5 + random.nextInt(5);
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
									setBlockAndNotifyAdequately(world, i2, j3, k2, LOTRMod.wood, 14);
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
													setBlockAndNotifyAdequately(world, i3, j3, k3, LOTRMod.leaves, (isRed ? 3 : 2) | (disableDecay ? 4 : 0));
												}
											}
										}
									}
								}
							}
						}
					}
				}
				
				int platforms = 0;
				if (random.nextInt(3) != 0)
				{
					if (random.nextBoolean())
					{
						platforms = 1 + random.nextInt(3);
					}
					else
					{
						platforms = 4 + random.nextInt(5);
					}
				}
				
				for (int l = 0; l < platforms; l++)
				{
					int j1 = j + 10 + random.nextInt(height - 10);
					new LOTRWorldGenWoodElfPlatform(false).generate(world, random, i, j1, k);
				}
			}
			else
			{
				byte leafStart = 3;
				for (int j1 = j + height - leafStart; j1 <= j + height; j1++)
				{
					int j2 = j1 - (j + height);
					int range = canopyRadius - j2;

					for (int i1 = i - range; i1 <= i + range; i1++)
					{
						for (int k1 = k - range; k1 <= k + range; k1++)
						{
							int i2 = i1 - i;
							int k2 = k1 - k;
							if (i2 * i2 + k2 * k2 < range * range)
							{
								Block block = world.getBlock(i1, j1, k1);
								if (block.getMaterial() == Material.air || block.canBeReplacedByLeaves(world, i1, j1, k1))
								{
									setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.leaves, (isRed ? 3 : 2) | (disableDecay ? 4 : 0));
									
									if (hasVines)
									{
										if (random.nextInt(20) == 0 && world.isAirBlock(i1 - 1, j1, k1))
										{
											growVines(world, random, i1 - 1, j1, k1, 8);
										}

										if (random.nextInt(20) == 0 && world.isAirBlock(i1 + 1, j1, k1))
										{
											growVines(world, random, i1 + 1, j1, k1, 2);
										}

										if (random.nextInt(20) == 0 && world.isAirBlock(i1, j1, k1 - 1))
										{
											growVines(world, random, i1, j1, k1 - 1, 1);
										}

										if (random.nextInt(20) == 0 && world.isAirBlock(i1, j1, k1 + 1))
										{
											growVines(world, random, i1, j1, k1 + 1, 4);
										}
									}
								}
							}
						}
					}
				}
				
				for (int j1 = 0; j1 < trunkWidth + 2; j1++)
				{
					int range = j1 + trunkWidth + 2;
					for (int i1 = i - trunkWidth - range; i1 <= i + trunkWidth + range; i1++)
					{
						for (int k1 = k - trunkWidth - range; k1 <= k + trunkWidth + range; k1++)
						{
							int i2 = Math.abs(i1 - i) - trunkWidth - j1;
							int k2 = Math.abs(k1 - k) - trunkWidth - j1;
							if (i2 * i2 + k2 * k2 <= range)
							{
								int j2 = j + j1 + height - 4 - trunkWidth;
								
								Block block = world.getBlock(i1, j2, k1);
								if (block.getMaterial() == Material.air || block.isLeaves(world, i1, j2, k1))
								{
									setBlockAndNotifyAdequately(world, i1, j2, k1, LOTRMod.wood, 14);
								}
							}
						}
					}
				}
			}
			
			if (trunkWidth > 0)
			{
				int roots = 4 + random.nextInt(5 * trunkWidth);
				for (int l = 0; l < roots; l++)
				{
					int i1 = i;
					int j1 = j + 1 + random.nextInt(5);
					int k1 = k;
					int xDirection = 0;
					int zDirection = 0;
					int rootLength = 1 + random.nextInt(4);
					
					if (random.nextBoolean())
					{
						if (random.nextBoolean())
						{
							i1 -= trunkWidth + 1;
							xDirection = -1;
						}
						else
						{
							i1 += trunkWidth + 1;
							xDirection = 1;
						}
						k1 -= trunkWidth + 1;
						k1 += random.nextInt(trunkWidth * 2 + 2);
					}
					else
					{
						if (random.nextBoolean())
						{
							k1 -= trunkWidth + 1;
							zDirection = -1;
						}
						else
						{
							k1 += trunkWidth + 1;
							zDirection = 1;
						}
						i1 -= trunkWidth + 1;
						i1 += random.nextInt(trunkWidth * 2 + 2);
					}
					
					for (int l1 = 0; l1 < rootLength; l1++)
					{
						int rootBlocks = 0;
						for (int j2 = j1; !LOTRMod.isOpaque(world, i1, j2, k1); j2--)
						{
							setBlockAndNotifyAdequately(world, i1, j2, k1, LOTRMod.wood, 14);
							if (world.getBlock(i1, j2 - 1, k1) == Blocks.grass)
							{
								setBlockAndNotifyAdequately(world, i1, j2 - 1, k1, Blocks.dirt, 0);
							}
							rootBlocks++;
							if (rootBlocks > 5)
							{
								break;
							}
						}
						
						j1--;
						if (random.nextBoolean())
						{
							if (xDirection == -1)
							{
								i1--;
							}
							else if (xDirection == 1)
							{
								i1++;
							}
							else if (zDirection == -1)
							{
								k1--;
							}
							else if (zDirection == 1)
							{
								k1++;
							}
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
	
    private void growVines(World world, Random random, int i, int j, int k, int meta)
    {
        setBlockAndNotifyAdequately(world, i, j, k, Blocks.vine, meta);
        int length = 4 + random.nextInt(8);
        while (true)
        {
            --j;
            if (!world.isAirBlock(i, j, k) || length <= 0)
            {
                return;
            }
            setBlockAndNotifyAdequately(world, i, j, k, Blocks.vine, meta);
            length--;
        }
    }
}
