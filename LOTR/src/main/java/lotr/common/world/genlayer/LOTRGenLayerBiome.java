package lotr.common.world.genlayer;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class LOTRGenLayerBiome extends GenLayer
{
	private BiomeGenBase theBiome;
	
    public LOTRGenLayerBiome(BiomeGenBase biome)
    {
        super(0L);
        theBiome = biome;
    }

	@Override
    public int[] getInts(int i, int k, int xSize, int zSize)
    {
        int i1 = i - 1;
        int k1 = k - 1;
        int i2 = xSize + 2;
        int k2 = zSize + 2;
        int[] ints = IntCache.getIntCache(xSize * zSize);

        for (int l = 0; l < zSize; l++)
        {
            for (int l1 = 0; l1 < xSize; l1++)
            {
            	ints[l1 + l * xSize] = theBiome.biomeID;
            }
        }

        return ints;
    }
}
