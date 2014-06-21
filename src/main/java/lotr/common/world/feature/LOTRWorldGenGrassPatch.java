package lotr.common.world.feature;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class LOTRWorldGenGrassPatch extends WorldGenerator
{
	@Override
    public boolean generate(World world, Random random, int i, int j, int k)
    {
        if (world.getBlock(i, j - 1, k) != Blocks.stone)
        {
            return false;
        }
        else
        {
            int radius = 3 + random.nextInt(3);
			int heightValue = world.getHeightValue(i, k);
			
            for (int i1 = i - radius; i1 <= i + radius; i1++)
            {
                for (int k1 = k - radius; k1 <= k + radius; ++k1)
                {
                    int i2 = i1 - i;
                    int k2 = k1 - k;
                    if (i2 * i2 + k2 * k2 < radius * radius && world.getHeightValue(i1, k1) == heightValue)
                    {
						for (int j1 = heightValue - 1; j1 > heightValue - 5; j1--)
						{
							Block block = world.getBlock(i1, j1, k1);
							if (block == Blocks.dirt || block == Blocks.stone)
							{
								if (j1 == heightValue - 1)
								{
									world.setBlock(i1, j1, k1, Blocks.grass, 0, 2);
								}
								else
								{
									world.setBlock(i1, j1, k1, Blocks.dirt, 0, 2);
								}
							}
							else
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
}
