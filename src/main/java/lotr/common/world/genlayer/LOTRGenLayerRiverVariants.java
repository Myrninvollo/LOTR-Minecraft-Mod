package lotr.common.world.genlayer;

import lotr.common.world.biome.LOTRBiome;
import lotr.common.world.biome.LOTRBiomeGenDeadMarshes;
import lotr.common.world.biome.LOTRBiomeGenFarHarad;
import lotr.common.world.biome.LOTRBiomeGenMirkwood;
import lotr.common.world.biome.LOTRBiomeGenMordor;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class LOTRGenLayerRiverVariants extends GenLayer
{
    private GenLayer biomeGenLayer;
    private GenLayer riverGenLayer;

    public LOTRGenLayerRiverVariants(long l, GenLayer layer, GenLayer layer1)
    {
        super(l);
        biomeGenLayer = layer;
        riverGenLayer = layer1;
    }

    @Override
    public void initWorldGenSeed(long l)
    {
        biomeGenLayer.initWorldGenSeed(l);
        riverGenLayer.initWorldGenSeed(l);
        super.initWorldGenSeed(l);
    }

    @Override
    public int[] getInts(int i, int k, int xSize, int zSize)
    {
        int[] biomes = biomeGenLayer.getInts(i, k, xSize, zSize);
        int[] rivers = riverGenLayer.getInts(i, k, xSize, zSize);
        int[] ints = IntCache.getIntCache(xSize * zSize);

        for (int l = 0; l < xSize * zSize; l++)
        {
			if (rivers[l] >= 0)
            {
				BiomeGenBase biome = LOTRBiome.lotrBiomeList[biomes[l]];
				if (biome instanceof LOTRBiome && !((LOTRBiome)biome).getEnableRiver())
				{
					ints[l] = biomes[l];
				}
				else
				{
					if (biome instanceof LOTRBiomeGenMordor)
					{
						ints[l] = LOTRBiome.mordorRiver.biomeID;
					}
					else if (biome instanceof LOTRBiomeGenDeadMarshes)
					{
						ints[l] = LOTRBiome.deadMarshesRiver.biomeID;
					}
					else if (biome instanceof LOTRBiomeGenMirkwood && ((LOTRBiomeGenMirkwood)biome).corrupted)
					{
						ints[l] = LOTRBiome.mirkwoodRiver.biomeID;
					}
					else if (biome instanceof LOTRBiomeGenFarHarad)
					{
						ints[l] = LOTRBiome.farHaradRiver.biomeID;
					}
					else
					{
						ints[l] = rivers[l];
					}
				}
            }
            else
            {
                ints[l] = biomes[l];
            }
        }

        return ints;
    }
}
