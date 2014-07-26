package lotr.common.world.feature;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class LOTRWorldGenWaterPlant extends WorldGenerator
{
	private Block plant;
	
	public LOTRWorldGenWaterPlant(Block block)
	{
		plant = block;
	}
	
	@Override
    public boolean generate(World world, Random random, int i, int j, int k)
    {
        for (int l = 0; l < 32; ++l)
        {
            int i1 = i + random.nextInt(4) - random.nextInt(4);
            int j1 = j;
            int k1 = k + random.nextInt(4) - random.nextInt(4);

            if (world.isAirBlock(i1, j1, k1) && plant.canPlaceBlockAt(world, i1, j1, k1))
            {
                world.setBlock(i1, j1, k1, plant, 0, 2);
            }
        }

        return true;
    }
}