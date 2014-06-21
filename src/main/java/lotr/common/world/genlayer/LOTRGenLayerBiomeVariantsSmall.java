package lotr.common.world.genlayer;

import lotr.common.world.biome.LOTRBiome;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class LOTRGenLayerBiomeVariantsSmall extends GenLayer
{
    public LOTRGenLayerBiomeVariantsSmall(long l, GenLayer layer)
    {
        super(l);
        parent = layer;
    }

	@Override
    public int[] getInts(int i, int k, int xSize, int zSize)
    {
        int[] biomes = parent.getInts(i - 1, k - 1, xSize + 2, zSize + 2);
        int[] ints = IntCache.getIntCache(xSize * zSize);

        for (int l = 0; l < zSize; l++)
        {
            for (int l1 = 0; l1 < xSize; l1++)
            {
                initChunkSeed((long)(l1 + i), (long)(l + k));
                int biome = biomes[l1 + 1 + (l + 1) * (xSize + 2)];

				int newBiome = biome;

				if (biome == LOTRBiome.shire.biomeID && nextInt(25) == 1)
				{
					newBiome = LOTRBiome.shireOrchard.biomeID;
				}
				
				else if (biome == LOTRBiome.brownLands.biomeID && nextInt(8) == 0)
				{
					newBiome = LOTRBiome.brownLandsWoodlands.biomeID;
				}

				if (newBiome != biome)
				{
					int j = biomes[l1 + 1 + (l + 1 - 1) * (xSize + 2)];
					int j1 = biomes[l1 + 1 + 1 + (l + 1) * (xSize + 2)];
					int j2 = biomes[l1 + 1 - 1 + (l + 1) * (xSize + 2)];
					int j3 = biomes[l1 + 1 + (l + 1 + 1) * (xSize + 2)];

					if (j == biome && j1 == biome && j2 == biome && j3 == biome)
					{
						ints[l1 + l * xSize] = newBiome;
					}
					else
					{
						ints[l1 + l * xSize] = biome;
					}
				}
				else
				{
					ints[l1 + l * xSize] = biome;
				}
			}
        }

        return ints;
    }
}
