package lotr.common.world.feature;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public class LOTRWorldGenBaobab extends WorldGenAbstractTree
{
	private Block woodBlock;
	private int woodMeta;
	private Block leafBlock;
	private int leafMeta;
	
    public LOTRWorldGenBaobab(boolean flag)
    {
		super(flag);
		woodBlock = Blocks.log;
		woodMeta = 0;
		leafBlock = Blocks.leaves;
		leafMeta = 0;
	}

	@Override
    public boolean generate(World world, Random random, int i, int j, int k)
    {
		int trunkCircleWidth = 4;
        int height = 16 + random.nextInt(16);
        boolean flag = true;

        if (j >= 1 && j + height + 1 <= 256)
        {
			for (int i1 = i - trunkCircleWidth - 1; i1 <= i + trunkCircleWidth + 1 && flag; i1++)
			{
				for (int k1 = k - trunkCircleWidth - 1; k1 <= k + trunkCircleWidth + 1 && flag; k1++)
				{
					int i2 = Math.abs(i1 - i);
					int k2 = Math.abs(k1 - k);
					if (i2 * i2 + k2 * k2 <= trunkCircleWidth * trunkCircleWidth)
					{
						for (int j1 = j; j1 <= j + 1 + height; j1++)
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
						
						Block block = world.getBlock(i1, j - 1, k1);
						if (block != Blocks.grass && block != Blocks.dirt)
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
			
			for (int i1 = i - trunkCircleWidth - 1; i1 <= i + trunkCircleWidth + 1 && flag; i1++)
			{
				for (int k1 = k - trunkCircleWidth - 1; k1 <= k + trunkCircleWidth + 1 && flag; k1++)
				{
					int i2 = Math.abs(i1 - i);
					int k2 = Math.abs(k1 - k);
					if (i2 * i2 + k2 * k2 <= trunkCircleWidth * trunkCircleWidth)
					{
						setBlockAndNotifyAdequately(world, i1, j - 1, k1, Blocks.dirt, 0);
						
						for (int j1 = 0; j1 < height; j1++)
						{
							setBlockAndNotifyAdequately(world, i1, j + j1, k1, woodBlock, woodMeta);
						}
					}
				}
			}
			
			int angle = 0;
			while (angle < 360)
			{
				angle += 30 + random.nextInt(30);
				float angleR = (float)Math.toRadians(angle);
				float sin = MathHelper.sin(angleR);
				float cos = MathHelper.cos(angleR);
				int boughLength = 8 + random.nextInt(8);
				int boughThickness = Math.round((float)boughLength / 30F);
				int boughBaseHeight = j + MathHelper.floor_double(height * (0.75F + random.nextFloat() * 0.25F));
				int boughHeight = 5 + random.nextInt(7);
				
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
					
					if (l < boughLength / 2)
					{
						continue;
					}
					
					int branch_angle = angle + random.nextInt(360);
					float branch_angleR = (float)Math.toRadians(branch_angle);
					float branch_sin = MathHelper.sin(branch_angleR);
					float branch_cos = MathHelper.cos(branch_angleR);
					int branchLength = 6 + random.nextInt(6);
					int branchHeight = 5 + random.nextInt(5);
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
			
			return true;
        }
        else
        {
        	return false;
        }
    }
}
