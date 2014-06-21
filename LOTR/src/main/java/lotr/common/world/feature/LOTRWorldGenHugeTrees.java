package lotr.common.world.feature;

import java.util.Random;

import lotr.common.LOTRMod;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public class LOTRWorldGenHugeTrees extends WorldGenAbstractTree
{
	private Block woodBlock;
	private int woodMeta;
	private Block leafBlock;
	private int leafMeta;
	
    private LOTRWorldGenHugeTrees(Block block, int i, Block block1, int j)
    {
		super(false);
		woodBlock = block;
		woodMeta = i;
		leafBlock = block1;
		leafMeta = j;
	}
    
    public static LOTRWorldGenHugeTrees newOak()
    {
    	return new LOTRWorldGenHugeTrees(Blocks.log, 0, Blocks.leaves, 0);
    }
    
    public static LOTRWorldGenHugeTrees newBirch()
    {
    	return new LOTRWorldGenHugeTrees(Blocks.log, 2, Blocks.leaves, 2);
    }

	@Override
    public boolean generate(World world, Random random, int i, int j, int k)
    {
		int trunkWidth = 1;
        int height = random.nextInt(12) + 12;
        boolean flag = true;

        if (j >= 1 && j + height + 1 <= 256)
        {
			for (int j1 = j; j1 <= j + 1 + height; j1++)
			{
				int range = trunkWidth + 1;
				
				if (j1 == j)
				{
					range = trunkWidth;
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
			
			for (int i1 = i - trunkWidth; i1 <= i + trunkWidth; i1++)
			{
				for (int k1 = k - trunkWidth; k1 <= k + trunkWidth; k1++)
				{
					setBlockAndNotifyAdequately(world, i1, j - 1, k1, Blocks.dirt, 0);
				}
			}
			
			for (int j1 = 0; j1 < height; j1++)
			{
				for (int i1 = i - trunkWidth; i1 <= i + trunkWidth; i1++)
				{
					for (int k1 = k - trunkWidth; k1 <= k + trunkWidth; k1++)
					{
						setBlockAndNotifyAdequately(world, i1, j + j1, k1, woodBlock, woodMeta);
					}
				}
			}
			
			int angle = 0;
			while (angle < 360)
			{
				angle += 20 + random.nextInt(25);
				float angleR = (float)angle / 180F * (float)Math.PI;
				float sin = MathHelper.sin(angleR);
				float cos = MathHelper.cos(angleR);
				int boughLength = 6 + random.nextInt(6);
				int boughThickness = Math.round((float)boughLength / 20F * 1.5F);
				int boughBaseHeight = j + MathHelper.floor_double(height * (0.75F + random.nextFloat() * 0.25F));
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
									setBlockAndNotifyAdequately(world, i2, j2, k2, woodBlock, woodMeta | 12);
								}
							}
						}
					}
					
					int branch_angle = angle + random.nextInt(360);
					float branch_angleR = (float)branch_angle / 180F * (float)Math.PI;
					float branch_sin = MathHelper.sin(branch_angleR);
					float branch_cos = MathHelper.cos(branch_angleR);
					int branchLength = 4 + random.nextInt(4);
					int branchHeight = random.nextInt(5);
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
								setBlockAndNotifyAdequately(world, i2, j3, k2, woodBlock, woodMeta | 12);
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
												setBlockAndNotifyAdequately(world, i3, j3, k3, leafBlock, leafMeta);
											}
										}
									}
								}
							}
						}
					}
				}
			}
		
			int roots = 5 + random.nextInt(5);
			for (int l = 0; l < roots; l++)
			{
				int i1 = i;
				int j1 = j + 1 + random.nextInt(5);
				int k1 = k;
				int xDirection = 0;
				int zDirection = 0;
				int rootLength = 2 + random.nextInt(4);
				
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
						setBlockAndNotifyAdequately(world, i1, j2, k1, woodBlock, woodMeta | 12);
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
			
			return true;
        }
        else
        {
        	return false;
        }
    }
}
