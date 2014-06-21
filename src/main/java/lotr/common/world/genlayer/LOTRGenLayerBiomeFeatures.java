package lotr.common.world.genlayer;

import lotr.common.world.biome.LOTRBiome;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class LOTRGenLayerBiomeFeatures extends GenLayer
{
    public LOTRGenLayerBiomeFeatures(long l, GenLayer layer)
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

				if (biome == LOTRBiome.lothlorien.biomeID && nextInt(6) == 0)
				{
					newBiome = LOTRBiome.lothlorienClearing.biomeID;
				}
				
				if (biome == LOTRBiome.fangorn.biomeID && nextInt(10) == 0)
				{
					newBiome = LOTRBiome.fangornClearing.biomeID;
				}
				
				if (biome == LOTRBiome.farHaradJungle.biomeID && nextInt(10) == 0)
				{
					newBiome = LOTRBiome.lake.biomeID;
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
