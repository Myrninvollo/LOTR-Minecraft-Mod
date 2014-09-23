package lotr.common.world.feature;

import java.util.Random;

import lotr.common.LOTRMod;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class LOTRWorldGenMarshLights extends WorldGenerator
{
	@Override
    public boolean generate(World world, Random random, int i, int j, int k)
    {
        for (int l = 0; l < 4; l++)
        {
            int i1 = i + random.nextInt(8) - random.nextInt(8);
            int j1 = j;
            int k1 = k + random.nextInt(8) - random.nextInt(8);

            if (world.isAirBlock(i1, j1, k1) && LOTRMod.marshLights.canPlaceBlockAt(world, i1, j1, k1))
            {
                world.setBlock(i1, j1, k1, LOTRMod.marshLights, 0, 2);
            }
        }

        return true;
    }
}
