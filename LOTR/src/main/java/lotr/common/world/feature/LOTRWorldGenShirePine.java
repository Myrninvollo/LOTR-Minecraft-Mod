package lotr.common.world.feature;

import java.util.Random;

import lotr.common.LOTRMod;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public class LOTRWorldGenShirePine extends WorldGenAbstractTree
{
	public LOTRWorldGenShirePine(boolean flag)
	{
		super(flag);
	}
	
	@Override
    public boolean generate(World world, Random random, int i, int j, int k)
    {
        int height = random.nextInt(10) + 10;
        int var7 = 6 + random.nextInt(4);
        int var8 = height - var7;
        int var9 = 2 + random.nextInt(2);
        boolean var10 = true;

        if (j >= 1 && j + height + 1 <= 256)
        {
            int var11;
            int var13;
            int var15;
            int var21;

            for (var11 = j; var11 <= j + 1 + height && var10; ++var11)
            {
                boolean var12 = true;

                if (var11 - j < var7)
                {
                    var21 = 0;
                }
                else
                {
                    var21 = var9;
                }

                for (var13 = i - var21; var13 <= i + var21 && var10; ++var13)
                {
                    for (int var14 = k - var21; var14 <= k + var21 && var10; ++var14)
                    {
                        if (var11 >= 0 && var11 < 256)
                        {
                            Block block = world.getBlock(var13, var11, var14);
                            
                            if (block != Blocks.air && !block.isLeaves(world, var13, var11, var14))
                            {
                                var10 = false;
                            }
                        }
                        else
                        {
                            var10 = false;
                        }
                    }
                }
            }

            if (!var10)
            {
                return false;
            }
            else
            {
                Block block = world.getBlock(i, j - 1, k);

                if ((block == Blocks.grass || block == Blocks.dirt) && j < 256 - height - 1)
                {
                	setBlockAndNotifyAdequately(world, i, j - 1, k, Blocks.dirt, 0);
                    var21 = random.nextInt(2);
                    var13 = 1;
                    byte var22 = 0;
                    int var17;
                    int var16;

                    for (var15 = 0; var15 <= var8; ++var15)
                    {
                        var16 = j + height - var15;

                        for (var17 = i - var21; var17 <= i + var21; ++var17)
                        {
                            int var18 = var17 - i;

                            for (int var19 = k - var21; var19 <= k + var21; ++var19)
                            {
                                int var20 = var19 - k;

                                Block block1 = world.getBlock(var17, var16, var19);
                                if ((Math.abs(var18) != var21 || Math.abs(var20) != var21 || var21 <= 0) && block1.canBeReplacedByLeaves(world, var17, var16, var19))
                                {
                                    setBlockAndNotifyAdequately(world, var17, var16, var19, LOTRMod.leaves, 0);
                                }
                            }
                        }

                        if (var21 >= var13)
                        {
                            var21 = var22;
                            var22 = 1;
                            ++var13;

                            if (var13 > var9)
                            {
                                var13 = var9;
                            }
                        }
                        else
                        {
                            ++var21;
                        }
                    }

                    for (var16 = 0; var16 < height; var16++)
                    {
                        Block block1 = world.getBlock(i, j + var16, k);

                        if (block1.getMaterial() == Material.air || block1.isLeaves(world, i, j + var16, k))
                        {
                            setBlockAndNotifyAdequately(world, i, j + var16, k, LOTRMod.wood, 0);
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
