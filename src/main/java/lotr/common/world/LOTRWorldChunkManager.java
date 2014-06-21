package lotr.common.world;

import java.util.List;
import java.util.Random;

import lotr.common.LOTRMod;
import lotr.common.world.biome.LOTRBiome;
import lotr.common.world.genlayer.LOTRGenLayerWorld;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeCache;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRWorldChunkManager extends WorldChunkManager
{
	private World worldObj;
    private GenLayer genBiomes;
    private GenLayer biomeIndexLayer;
    private BiomeCache biomeCache;

    public LOTRWorldChunkManager(World world)
    {
		worldObj = world;
		biomeCache = new BiomeCache(this);
        GenLayer[] layers = LOTRGenLayerWorld.createWorld(world.getSeed(), world.getWorldInfo().getTerrainType());
        genBiomes = layers[0];
        biomeIndexLayer = layers[1];
    }

	@Override
    public BiomeGenBase getBiomeGenAt(int i, int k)
    {
        return biomeCache.getBiomeGenAt(i, k);
    }

	@Override
    public float[] getRainfall(float[] rainfall, int i, int k, int xSize, int zSize)
    {
        IntCache.resetIntCache();

        if (rainfall == null || rainfall.length < xSize * zSize)
        {
            rainfall = new float[xSize * zSize];
        }

        int[] ints = biomeIndexLayer.getInts(i, k, xSize, zSize);
        for (int l = 0; l < xSize * zSize; l++)
        {
            float f = (float)LOTRBiome.lotrBiomeList[ints[l]].getIntRainfall() / 65536F;
            if (f > 1F)
            {
                f = 1F;
            }
            rainfall[l] = f;
        }

        return rainfall;
    }

	@Override
	@SideOnly(Side.CLIENT)
    public float getTemperatureAtHeight(float f, int height)
    {
		if (worldObj.isRemote && LOTRMod.isChristmas())
		{
			return 0F;
		}
        return f;
    }

	@Override
    public BiomeGenBase[] getBiomesForGeneration(BiomeGenBase[] biomes, int i, int k, int xSize, int zSize)
    {
        IntCache.resetIntCache();

        if (biomes == null || biomes.length < xSize * zSize)
        {
            biomes = new BiomeGenBase[xSize * zSize];
        }

        int[] ints = genBiomes.getInts(i, k, xSize, zSize);
        for (int l = 0; l < xSize * zSize; l++)
        {
            biomes[l] = LOTRBiome.lotrBiomeList[ints[l]];
        }

        return biomes;
    }

	@Override
    public BiomeGenBase[] loadBlockGeneratorData(BiomeGenBase[] biomes, int i, int k, int xSize, int zSize)
    {
        return getBiomeGenAt(biomes, i, k, xSize, zSize, true);
    }

	@Override
    public BiomeGenBase[] getBiomeGenAt(BiomeGenBase[] biomes, int i, int k, int xSize, int zSize, boolean useCache)
    {
        IntCache.resetIntCache();

        if (biomes == null || biomes.length < xSize * zSize)
        {
            biomes = new BiomeGenBase[xSize * zSize];
        }

        if (useCache && xSize == 16 && zSize == 16 && (i & 15) == 0 && (k & 15) == 0)
        {
            BiomeGenBase[] cachedBiomes = biomeCache.getCachedBiomes(i, k);
            System.arraycopy(cachedBiomes, 0, biomes, 0, xSize * zSize);
            return biomes;
        }
        else
        {
            int[] ints = biomeIndexLayer.getInts(i, k, xSize, zSize);
            for (int l = 0; l < xSize * zSize; l++)
            {
                biomes[l] = LOTRBiome.lotrBiomeList[ints[l]];
            }
            return biomes;
        }
    }

	@Override
    public boolean areBiomesViable(int i, int k, int range, List list)
    {
        int i1 = i - range >> 2;
        int k1 = k - range >> 2;
        int i2 = i + range >> 2;
        int k2 = k + range >> 2;
        int i3 = i2 - i1 + 1;
        int k3 = k2 - k1 + 1;
        int[] ints = genBiomes.getInts(i1, k1, i3, k3);
        for (int l = 0; l < i3 * k3; l++)
        {
            BiomeGenBase biome = LOTRBiome.lotrBiomeList[ints[l]];
            if (!list.contains(biome))
            {
                return false;
            }
        }

        return true;
    }

	@Override
    public ChunkPosition findBiomePosition(int i, int k, int range, List list, Random random)
    {
        int i1 = i - range >> 2;
        int k1 = k - range >> 2;
        int i2 = i + range >> 2;
        int k2 = k + range >> 2;
        int i3 = i2 - i1 + 1;
        int k3 = k2 - k1 + 1;
        int[] ints = genBiomes.getInts(i1, k1, i3, k3);
        ChunkPosition chunkpos = null;
        int j = 0;
        for (int l = 0; l < ints.length; l++)
        {
            int xPos = i1 + l % i3 << 2;
            int zPos = k1 + l / i3 << 2;
            BiomeGenBase biome = LOTRBiome.lotrBiomeList[ints[l]];
            if (list.contains(biome) && (chunkpos == null || random.nextInt(j + 1) == 0))
            {
                chunkpos = new ChunkPosition(xPos, 0, zPos);
                j++;
            }
        }

        return chunkpos;
    }

	@Override
    public void cleanupCache()
    {
        biomeCache.cleanupCache();
    }
}
