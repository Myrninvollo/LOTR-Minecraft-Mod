package lotr.common.world.feature;

import java.util.Random;

import lotr.common.LOTRMod;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class LOTRWorldGenMordorMoss extends WorldGenerator
{
	@Override
    public boolean generate(World world, Random random, int i, int j, int k)
    {
		int numberOfMoss = 32 + random.nextInt(80);
        float f = random.nextFloat() * (float)Math.PI;
        double d = (double)((float)(i + 8) + MathHelper.sin(f) * (float)numberOfMoss / 8F);
        double d1 = (double)((float)(i + 8) - MathHelper.sin(f) * (float)numberOfMoss / 8F);
        double d2 = (double)((float)(k + 8) + MathHelper.cos(f) * (float)numberOfMoss / 8F);
        double d3 = (double)((float)(k + 8) - MathHelper.cos(f) * (float)numberOfMoss / 8F);

        for (int l = 0; l <= numberOfMoss; l++)
        {
            double d5 = d + (d1 - d) * (double)l / (double)numberOfMoss;
            double d6 = d2 + (d3 - d2) * (double)l / (double)numberOfMoss;
            double d7 = random.nextDouble() * (double)numberOfMoss / 16D;
            double d8 = (double)(MathHelper.sin((float)l * (float)Math.PI / (float)numberOfMoss) + 1F) * d7 + 1D;
            int i1 = MathHelper.floor_double(d5 - d8 / 2D);
            int k1 = MathHelper.floor_double(d6 - d8 / 2D);
            int i2 = MathHelper.floor_double(d5 + d8 / 2D);
            int k2 = MathHelper.floor_double(d6 + d8 / 2D);

            for (int i3 = i1; i3 <= i2; i3++)
            {
                double d9 = ((double)i3 + 0.5D - d5) / (d8 / 2D);
                if (d9 * d9 < 1D)
                {
					for (int k3 = k1; k3 <= k2; k3++)
					{
						int j1 = world.getHeightValue(i3, k3);
						if (j1 == j)
						{
							double d10 = ((double)k3 + 0.5D - d6) / (d8 / 2D);
							
							if (d9 * d9 + d10 * d10 < 1D && world.getBlock(i3, j1 - 1, k3) == LOTRMod.rock && world.getBlockMetadata(i3, j1 - 1, k3) == 0 && world.isAirBlock(i3, j1, k3))
							{
								world.setBlock(i3, j1, k3, LOTRMod.mordorMoss, 0, 2);
							}
						}
					}
				}
			}
        }

        return true;
    }
}
