package lotr.common.world.feature;

import java.util.Random;

import lotr.common.world.biome.LOTRBiome;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase.FlowerEntry;
import net.minecraft.world.gen.feature.WorldGenerator;

public class LOTRWorldGenFlowers extends WorldGenerator
{
	@Override
    public boolean generate(World world, Random random, int i, int j, int k)
    {
		FlowerEntry flower = ((LOTRBiome)world.getBiomeGenForCoords(i, k)).getRandomFlower(random);
		
		Block block = flower.block;
		int meta = flower.metadata;
		
        for (int l = 0; l < 64; l++)
        {
            int i1 = i + random.nextInt(8) - random.nextInt(8);
            int j1 = j + random.nextInt(4) - random.nextInt(4);
            int k1 = k + random.nextInt(8) - random.nextInt(8);

            if (world.isAirBlock(i1, j1, k1) && (!world.provider.hasNoSky || j1 < 255) && block.canBlockStay(world, i1, j1, k1))
            {
               	world.setBlock(i1, j1, k1, block, meta, 2);
            }
        }

        return true;
    }
}