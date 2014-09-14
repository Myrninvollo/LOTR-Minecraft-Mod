package lotr.common.world.genlayer;

import lotr.common.LOTRDimension;
import lotr.common.world.biome.*;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class LOTRGenLayerBeach extends GenLayer
{
	private BiomeGenBase targetBiome = LOTRBiome.ocean;
	
    public LOTRGenLayerBeach(long l, GenLayer layer)
    {
        super(l);
        parent = layer;
    }
    
    @Override
    public int[] getInts(int i, int k, int xSize, int zSize)
    {
        int[] biomes = parent.getInts(i - 1, k - 1, xSize + 2, zSize + 2);
        int[] ints = IntCache.getIntCache(xSize * zSize);

        for (int k1 = 0; k1 < zSize; k1++)
        {
            for (int i1 = 0; i1 < xSize; ++i1)
            {
                initChunkSeed((long)(i1 + i), (long)(k1 + k));
                int biomeID = biomes[i1 + 1 + (k1 + 1) * (xSize + 2)];
                BiomeGenBase biome = LOTRDimension.MIDDLE_EARTH.biomeList[biomeID];
                
                int newBiomeID = biomeID;
                
                if (biomeID != targetBiome.biomeID)
                {
                    int biome1 = biomes[i1 + 1 + (k1 + 1 - 1) * (xSize + 2)];
                    int biome2 = biomes[i1 + 1 + 1 + (k1 + 1) * (xSize + 2)];
                    int biome3 = biomes[i1 + 1 - 1 + (k1 + 1) * (xSize + 2)];
                    int biome4 = biomes[i1 + 1 + (k1 + 1 + 1) * (xSize + 2)];

                    if (biome1 != targetBiome.biomeID && biome2 != targetBiome.biomeID && biome3 != targetBiome.biomeID && biome4 != targetBiome.biomeID)
                    {
                    	newBiomeID = biomeID;
                    }
                    else
                    {
                    	if (biome instanceof LOTRBiomeGenLindon || biome instanceof LOTRBiomeGenForodwaith)
                    	{
                    		newBiomeID = LOTRBiome.beachStone.biomeID;
                    	}
                    	else if (!(biome instanceof LOTRBiomeGenBeach))
                    	{
	                    	if (nextInt(20) == 0)
	                    	{
	                    		newBiomeID = LOTRBiome.beachGravel.biomeID;
	                    	}
	                    	else
	                    	{
	                    		newBiomeID = LOTRBiome.beach.biomeID;
	                    	}
                    	}
                    }
                }
                
                ints[i1 + k1 * xSize] = newBiomeID;
            }
        }

        return ints;
    }
}
