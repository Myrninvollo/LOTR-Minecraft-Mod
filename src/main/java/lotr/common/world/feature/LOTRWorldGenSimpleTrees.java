package lotr.common.world.feature;

import java.util.Random;

import lotr.common.LOTRMod;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public class LOTRWorldGenSimpleTrees extends WorldGenAbstractTree
{
	private int minHeight;
	private int maxHeight;
	private Block woodID;
	private int woodMeta;
	private Block leafID;
	private int leafMeta;
	private int extraTrunkWidth;
	
    private LOTRWorldGenSimpleTrees(boolean flag, int i, int j, Block k, int l, Block i1, int j1)
    {
		super(flag);
		minHeight = i;
		maxHeight = j;
		woodID = k;
		woodMeta = l;
		leafID = i1;
		leafMeta = j1;
	}
	
	public LOTRWorldGenSimpleTrees setTrunkWidth(int i)
	{
		extraTrunkWidth = i - 1;
		return this;
	}
	
	public static LOTRWorldGenSimpleTrees newMallorn(boolean flag)
	{
		return new LOTRWorldGenSimpleTrees(flag, 9, 15, LOTRMod.wood, 1, LOTRMod.leaves, 1);
	}
	
	public static LOTRWorldGenSimpleTrees newApple(boolean flag)
	{
		return new LOTRWorldGenSimpleTrees(flag, 4, 7, LOTRMod.fruitWood, 0, LOTRMod.fruitLeaves, 0);
	}
	
	public static LOTRWorldGenSimpleTrees newPear(boolean flag)
	{
		return new LOTRWorldGenSimpleTrees(flag, 4, 5, LOTRMod.fruitWood, 1, LOTRMod.fruitLeaves, 1);
	}
	
	public static LOTRWorldGenSimpleTrees newCherry(boolean flag)
	{
		return new LOTRWorldGenSimpleTrees(flag, 4, 8, LOTRMod.fruitWood, 2, LOTRMod.fruitLeaves, 2);
	}
	
	public static LOTRWorldGenSimpleTrees newMango(boolean flag)
	{
		return new LOTRWorldGenSimpleTrees(flag, 4, 7, LOTRMod.fruitWood, 3, LOTRMod.fruitLeaves, 3);
	}
	
	public static LOTRWorldGenSimpleTrees newLebethron(boolean flag)
	{
		return new LOTRWorldGenSimpleTrees(flag, 5, 9, LOTRMod.wood2, 0, LOTRMod.leaves2, 0);
	}
	
	public static LOTRWorldGenSimpleTrees newLebethronLarge(boolean flag)
	{
		return new LOTRWorldGenSimpleTrees(flag, 11, 18, LOTRMod.wood2, 0, LOTRMod.leaves2, 0).setTrunkWidth(2);
	}
	
	public static LOTRWorldGenSimpleTrees newBeech(boolean flag)
	{
		return new LOTRWorldGenSimpleTrees(flag, 5, 9, LOTRMod.wood2, 1, LOTRMod.leaves2, 1);
	}
	
	public static LOTRWorldGenSimpleTrees newMaple(boolean flag)
	{
		return new LOTRWorldGenSimpleTrees(flag, 4, 8, LOTRMod.wood3, 0, LOTRMod.leaves3, 0);
	}

	@Override
    public boolean generate(World world, Random random, int i, int j, int k)
    {
        int height = MathHelper.getRandomIntegerInRange(random, minHeight, maxHeight);
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
                if (j1 >= j + 1 + height - 2)
                {
                    range = 2;
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
					
                    byte leafStart = 3;
                    byte leafRangeMin = 0;
                    for (int j1 = j - leafStart + height; j1 <= j + height; j1++)
                    {
                        int j2 = j1 - (j + height);
                        int leafRange = leafRangeMin + 1 - j2 / 2;
                        for (int i1 = i - leafRange; i1 <= i + leafRange + extraTrunkWidth; i1++)
                        {
                            int i2 = i1 - i;
							if (i2 > 0)
							{
								i2 -= extraTrunkWidth;
							}
                            for (int k1 = k - leafRange; k1 <= k + leafRange + extraTrunkWidth; k1++)
                            {
                                int k2 = k1 - k;
								if (k2 > 0)
								{
									k2 -= extraTrunkWidth;
								}
                                Block block = world.getBlock(i1, j1, k1);
                                if ((Math.abs(i2) != leafRange || Math.abs(k2) != leafRange || random.nextInt(2) != 0 && j2 != 0) && block.canBeReplacedByLeaves(world, i1, j1, k1))
                                {
                                    setBlockAndNotifyAdequately(world, i1, j1, k1, leafID, leafMeta);
                                }
                            }
                        }
                    }

                    for (int j1 = 0; j1 < height; j1++)
                    {
						for (int i1 = i; i1 <= i + extraTrunkWidth; i1++)
						{
							for (int k1 = k; k1 <= k + extraTrunkWidth; k1++)
							{
								Block block = world.getBlock(i1, j + j1, k1);
								if (block.getMaterial() == Material.air || block.isLeaves(world, i1, j + j1, k1))
								{
									setBlockAndNotifyAdequately(world, i1, j + j1, k1, woodID, woodMeta);
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
