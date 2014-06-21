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
        int height = 12 + random.nextInt(12);
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
				int boughLength = 4 + random.nextInt(8);
				int boughThickness = Math.round((float)boughLength / 30F);
				int boughBaseHeight = j + MathHelper.floor_double(height * (0.85F + random.nextFloat() * 0.15F));
				int boughHeight = 1 + random.nextInt(4);
				
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

					if (l == boughLength - 1)
					{
						int leafRange = 3;
						
						for (int i2 = i1 - leafRange; i2 <= i1 + leafRange; i2++)
						{
							for (int j2 = j1 - leafRange; j2 <= j1 + leafRange; j2++)
							{
								for (int k2 = k1 - leafRange; k2 <= k1 + leafRange; k2++)
								{
									int i3 = i2 - i1;
									int j3 = j2 - j1;
									int k3 = k2 - k1;
									int dist = i3 * i3 + j3 * j3 + k3 * k3;
									if (dist < (leafRange - 1) * (leafRange - 1) || (dist < leafRange * leafRange && random.nextInt(3) != 0))
									{
										Block block2 = world.getBlock(i2, j2, k2);
										if (block2.getMaterial() == Material.air || block2.isLeaves(world, i2, j2, k2))
										{
											setBlockAndNotifyAdequately(world, i2, j2, k2, leafBlock, leafMeta);
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
