package lotr.common.world.genlayer;

import lotr.common.world.biome.LOTRBiome;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class LOTRGenLayerRiver extends GenLayer
{
	private BiomeGenBase riverBiome;
	
    public LOTRGenLayerRiver(long l, GenLayer layer)
    {
        super(l);
        super.parent = layer;
		riverBiome = LOTRBiome.river;
    }

	@Override
    public int[] getInts(int i, int k, int xSize, int zSize)
    {
        int i1 = i - 1;
        int k1 = k - 1;
        int i2 = xSize + 2;
        int k2 = zSize + 2;
        int[] biomes = parent.getInts(i1, k1, i2, k2);
        int[] ints = IntCache.getIntCache(xSize * zSize);

        for (int l = 0; l < zSize; l++)
        {
            for (int l1 = 0; l1 < xSize; l1++)
            {
                int j = biomes[l1 + 0 + (l + 1) * i2];
                int j1 = biomes[l1 + 2 + (l + 1) * i2];
                int j2 = biomes[l1 + 1 + (l + 0) * i2];
                int j3 = biomes[l1 + 1 + (l + 2) * i2];
                int j4 = biomes[l1 + 1 + (l + 1) * i2];

                if (j4 != 0 && j != 0 && j1 != 0 && j2 != 0 && j3 != 0)
                {
                    if (j4 == j && j4 == j2 && j4 == j1 && j4 == j3)
                    {
                        ints[l1 + l * xSize] = -1;
                    }
                    else
                    {
                        ints[l1 + l * xSize] = riverBiome.biomeID;
                    }
                }
                else
                {
                    ints[l1 + l * xSize] = riverBiome.biomeID;
                }
            }
        }

        return ints;
    }
}
