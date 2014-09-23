package lotr.common.world.feature;

import java.util.Random;

import lotr.common.LOTRMod;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

public class LOTRWorldGenCedar extends WorldGenAbstractTree
{
	private Block woodBlock = LOTRMod.wood4;
	private int woodMeta = 2;
	
	private Block leafBlock = LOTRMod.leaves4;
	private int leafMeta = 2;

	private int minHeight = 10;
	private int maxHeight = 16;
	
	public LOTRWorldGenCedar(boolean flag)
	{
		super(flag);
	}
	
	public LOTRWorldGenCedar setMinMaxHeight(int min, int max)
	{
		minHeight = min;
		maxHeight = max;
		return this;
	}

	@Override
    public boolean generate(World world, Random random, int i, int j, int k)
    {
        int height = MathHelper.getRandomIntegerInRange(random, minHeight, maxHeight);

        boolean flag = true;
        
        if (j >= 1 && + height + 1 <= 256)
        {
            for (int j1 = j; j1 <= j + height + 1; j1++)
            {
                int range = 2;

                if (j1 == j)
                {
                	range = 1;
                }

                if (j1 >= j + height - 1)
                {
                	range = 2;
                }

                for (int i1 = i - range; i1 <= i + range && flag; i1++)
                {
                    for (int k1 = k - range; k1 <= k + range && flag; k1++)
                    {
                        if (j1 >= 0 && j1 < 256)
                        {
                            if (!isReplaceable(world, i1, j1, k1))
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
        }
        else
        {
        	flag = false;
        }
        
        Block below = world.getBlock(i, j - 1, k);
        boolean isSoil = below.canSustainPlant(world, i, j - 1, k, ForgeDirection.UP, (IPlantable)Blocks.sapling);
        if (!isSoil)
        {
        	flag = false;
        }
        
        if (!flag)
        {
            return false;
        }
        else
        {
        	below.onPlantGrow(world, i, j - 1, k, i, j, k);
        	
        	int canopyMin = 2;
            for (int j1 = j + height - canopyMin; j1 <= j + height; j1++)
            {
                int leafRange = 2 - (j1 - (j + height));
                spawnLeaves(world, i, j1, k, leafRange);
            }

            for (int j1 = j + height - 1; j1 > j + height / 2; j1 -= 1 + random.nextInt(3))
            {
            	int branches = 1 + random.nextInt(3);
            	for (int l = 0; l < branches; l++)
            	{
	                float angle = random.nextFloat() * (float)Math.PI * 2F;
	                int i1 = i + (int)(0.5F + MathHelper.cos(angle) * 4F);
	                int k1 = k + (int)(0.5F + MathHelper.sin(angle) * 4F);
	                int j2 = j1;
	
	                int length = MathHelper.getRandomIntegerInRange(random, 4, 7);
	                for (int l1 = 0; l1 < length; l1++)
	                {
	                	i1 = i + (int)(1.5F + MathHelper.cos(angle) * (float)l1);
	                    k1 = k + (int)(1.5F + MathHelper.sin(angle) * (float)l1);
	                    j2 = j1 - 3 + l1 / 2;
	                    if (isReplaceable(world, i1, j2, k1))
	                    {
	                    	setBlockAndNotifyAdequately(world, i1, j2, k1, woodBlock, woodMeta);
	                    }
	                    else
	                    {
	                    	break;
	                    }
	                }
	
	                int leafMin = 1 + random.nextInt(2);
	                for (int j3 = j2 - leafMin; j3 <= j2; j3++)
	                {
	                    int leafRange = 1 - (j3 - j2);
	                    spawnLeaves(world, i1, j3, k1, leafRange);
	                }
            	}
            }

            for (int j1 = 0; j1 < height; j1++)
            {
            	setBlockAndNotifyAdequately(world, i, j + j1, k, woodBlock, woodMeta);
            }
            
            for (int i1 = i - 1; i1 <= i + 1; i1++)
            {
            	for (int k1 = k - 1; k1 <= k + 1; k1++)
                {
            		int i2 = i1 - i;
            		int k2 = k1 - k;
            		if (Math.abs(i2) != Math.abs(k2))
            		{
            			int rootX = i1;
            			int rootY = j + 1 + random.nextInt(2);
            			int rootZ = k1;

            			int roots = 0;
                    	while (world.getBlock(rootX, rootY, k1).isReplaceable(world, rootX, rootY, rootZ))
            			{
                    		setBlockAndNotifyAdequately(world, rootX, rootY, rootZ, woodBlock, woodMeta | 12);
                    		if (world.getBlock(rootX, rootY - 1, rootZ) == Blocks.grass)
                    		{
                    			setBlockAndNotifyAdequately(world, rootX, rootY - 1, rootZ, Blocks.dirt, 0);
                    		}
                    		
                    		rootY--;
                    		
                    		roots++;
                    		if (roots > 4 + random.nextInt(3))
                    		{
                    			break;
                    		}
            			}
            		}
                }
            }

            return true;
        }
    }
	
	private void spawnLeaves(World world, int i, int j, int k, int leafRange)
	{
        int leafRangeSq = leafRange * leafRange;

        for (int i1 = i - leafRange; i1 <= i + leafRange; i1++)
        {
            for (int k1 = k - leafRange; k1 <= k + leafRange; k1++)
            {
            	int i2 = i1 - i;
                int k2 = k1 - k;

                if (i2 * i2 + k2 * k2 <= leafRangeSq)
                {
                	Block block = world.getBlock(i1, j, k1);
                    if (block.isReplaceable(world, i1, j, k1) || block.isLeaves(world, i1, j, k1))
                    {
                        setBlockAndNotifyAdequately(world, i1, j, k1, leafBlock, leafMeta);
                    }
                }
            }
        }
	}
}
