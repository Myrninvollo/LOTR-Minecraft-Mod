package lotr.common.world.genlayer;

import lotr.common.world.biome.LOTRBiome;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class LOTRGenLayerBiomeVariants extends GenLayer
{
	private GenLayer biomeLayer;
	private GenLayer variantsLayer;
	
    public LOTRGenLayerBiomeVariants(long l, GenLayer layer, GenLayer layer1)
    {
        super(l);
        parent = layer;
		biomeLayer = layer;
		variantsLayer = layer1;
    }

	@Override
    public int[] getInts(int i, int k, int xSize, int zSize)
    {
        int[] biomes = biomeLayer.getInts(i - 1, k - 1, xSize + 2, zSize + 2);
        int[] variants = variantsLayer.getInts(i - 1, k - 1, xSize + 2, zSize + 2);
        int[] ints = IntCache.getIntCache(xSize * zSize);

        for (int l = 0; l < zSize; l++)
        {
            for (int l1 = 0; l1 < xSize; l1++)
            {
                initChunkSeed((long)(l1 + i), (long)(l + k));
                int biome = biomes[l1 + 1 + (l + 1) * (xSize + 2)];
                int variant = variants[l1 + 1 + (l + 1) * (xSize + 2)];

				int newBiome = biome;

				if (biome == LOTRBiome.shire.biomeID && variant < 15 && variant != 0)
				{
					newBiome = LOTRBiome.shireWoodlands.biomeID;
				}
				
				else if (biome == LOTRBiome.eriador.biomeID && variant < 25)
				{
					if (variant < 10)
					{
						newBiome = LOTRBiome.eriadorWoodlandsDense.biomeID;
					}
					else
					{
						newBiome = LOTRBiome.eriadorWoodlands.biomeID;
					}
				}
				
				else if (biome == LOTRBiome.tundra.biomeID && variant < 20)
				{
					newBiome = LOTRBiome.taiga.biomeID;
				}
				
				else if (biome == LOTRBiome.lindon.biomeID && variant < 20)
				{
					newBiome = LOTRBiome.lindonWoodlands.biomeID;
				}
				
				else if (biome == LOTRBiome.minhiriath.biomeID && variant < 25)
				{
					if (variant < 10)
					{
						newBiome = LOTRBiome.minhiriathWasteland.biomeID;
					}
					else
					{
						newBiome = LOTRBiome.minhiriathWoodlands.biomeID;
					}
				}
				
				else if (biome == LOTRBiome.loneLands.biomeID && variant < 15)
				{
					newBiome = LOTRBiome.loneLandsHills.biomeID;
				}
				
				else if (biome == LOTRBiome.enedwaith.biomeID && variant < 30)
				{
					if (variant < 15)
					{
						newBiome = LOTRBiome.enedwaithWoodlandsDense.biomeID;
					}
					else
					{
						newBiome = LOTRBiome.enedwaithWoodlands.biomeID;
					}
				}
				
				else if (biome == LOTRBiome.eregion.biomeID && variant < 25)
				{
					if (variant < 10)
					{
						newBiome = LOTRBiome.eregionForest.biomeID;
					}
					else
					{
						newBiome = LOTRBiome.eregionHills.biomeID;
					}
				}
				
				else if (biome == LOTRBiome.mistyMountains.biomeID && variant < 10)
				{
					newBiome = LOTRBiome.mistyMountainsForest.biomeID;
				}
				
				else if (biome == LOTRBiome.forodwaithMountains.biomeID && variant < 5)
				{
					newBiome = LOTRBiome.forodwaithGlacier.biomeID;
				}
				
				else if (biome == LOTRBiome.anduinHills.biomeID && variant < 25)
				{
					if (variant < 10)
					{
						newBiome = LOTRBiome.anduinHillsWoodlandsDense.biomeID;
					}
					else
					{
						newBiome = LOTRBiome.anduinHillsWoodlands.biomeID;
					}
				}
				
				else if (biome == LOTRBiome.anduinVale.biomeID && variant < 20)
				{
					newBiome = LOTRBiome.anduinValeWoodlands.biomeID;
				}
				
				else if (biome == LOTRBiome.rohan.biomeID && variant < 20)
				{
					if (variant < 5)
					{
						newBiome = LOTRBiome.rohanBoulderFields.biomeID;
					}
					else
					{
						newBiome = LOTRBiome.rohanWoodlands.biomeID;
					}
				}
				
				else if (biome == LOTRBiome.fangorn.biomeID && variant < 5)
				{
					newBiome = LOTRBiome.fangornBirchForest.biomeID;
				}
				
				else if (biome == LOTRBiome.dunland.biomeID && variant < 25)
				{
					newBiome = LOTRBiome.dunlandForest.biomeID;
				}
				
				else if (biome == LOTRBiome.gondor.biomeID && variant < 20)
				{
					if (variant < 5)
					{
						newBiome = LOTRBiome.gondorHills.biomeID;
					}
					else
					{
						newBiome = LOTRBiome.gondorWoodlands.biomeID;
					}
				}
				
				else if (biome == LOTRBiome.ithilien.biomeID && variant < 15)
				{
					newBiome = LOTRBiome.ithilienHills.biomeID;
				}
				
				else if (biome == LOTRBiome.wilderland.biomeID && variant < 35)
				{
					if (variant < 10)
					{
						newBiome = LOTRBiome.wilderlandHills.biomeID;
					}
					else if (variant < 20)
					{
						newBiome = LOTRBiome.wilderlandForestDense.biomeID;
					}
					else
					{
						newBiome = LOTRBiome.wilderlandForest.biomeID;
					}
				}
				
				else if (biome == LOTRBiome.harondor.biomeID && variant < 15)
				{
					newBiome = LOTRBiome.harondorShrubland.biomeID;
				}
				
				else if (biome == LOTRBiome.nearHarad.biomeID && variant < 15)
				{
					if (variant < 5)
					{
						newBiome = LOTRBiome.nearHaradBoulderFields.biomeID;
					}
					else
					{
						newBiome = LOTRBiome.nearHaradDunes.biomeID;
					}
				}
				
				else if (biome == LOTRBiome.nearHaradFertile.biomeID && variant < 20)
				{
					newBiome = LOTRBiome.nearHaradFertileForest.biomeID;
				}
				
				else if (biome == LOTRBiome.farHarad.biomeID && variant < 20)
				{
					newBiome = LOTRBiome.farHaradForest.biomeID;
				}
				
				else if (biome == LOTRBiome.farHaradJungle.biomeID && variant < 25)
				{
					newBiome = LOTRBiome.farHaradJungleHills.biomeID;
				}
				
				else if (biome == LOTRBiome.rhun.biomeID && variant < 20)
				{
					newBiome = LOTRBiome.rhunForest.biomeID;
				}
				
				else if (biome == LOTRBiome.ocean.biomeID && variant < 2)
				{
					newBiome = LOTRBiome.island.biomeID;
				}

				if (newBiome != biome)
				{
					ints[l1 + l * xSize] = newBiome;
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
