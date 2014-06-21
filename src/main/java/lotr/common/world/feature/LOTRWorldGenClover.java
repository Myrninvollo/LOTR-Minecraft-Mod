package lotr.common.world.feature;

import java.util.Random;

import lotr.common.LOTRMod;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class LOTRWorldGenClover extends WorldGenerator
{
	@Override
    public boolean generate(World world, Random random, int i, int j, int k)
    {
        Block block = null;
        do 
        {
            block = world.getBlock(i, j, k);
            if (block != null && !block.isLeaves(world, i, j, k))
            {
                break;
            }
            j--;
        }
		while (j > 0);

        for (int l = 0; l < 128; l++)
        {
            int i1 = i + random.nextInt(8) - random.nextInt(8);
            int j1 = j + random.nextInt(4) - random.nextInt(4);
            int k1 = k + random.nextInt(8) - random.nextInt(8);

            if (world.isAirBlock(i1, j1, k1) && LOTRMod.clover.canBlockStay(world, i1, j1, k1))
            {
				if (random.nextInt(1000) == 0)
				{
					world.setBlock(i1, j1, k1, LOTRMod.clover, 1, 2);
				}
				else
				{
					world.setBlock(i1, j1, k1, LOTRMod.clover, 0, 2);
				}
            }
        }

        return true;
    }
}
