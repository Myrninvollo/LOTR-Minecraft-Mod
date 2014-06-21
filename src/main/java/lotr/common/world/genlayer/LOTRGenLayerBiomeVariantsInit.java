package lotr.common.world.genlayer;

import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class LOTRGenLayerBiomeVariantsInit extends GenLayer
{
    public LOTRGenLayerBiomeVariantsInit(long l)
    {
        super(l);
    }

    @Override
    public int[] getInts(int i, int k, int xSize, int zSize)
    {
        int[] ints = IntCache.getIntCache(xSize * zSize);

        for (int k1 = 0; k1 < zSize; k1++)
        {
            for (int i1 = 0; i1 < xSize; i1++)
            {
                initChunkSeed((long)(i1 + i), (long)(k1 + k));
				ints[i1 + k1 * xSize] = nextInt(100);
            }
        }

        return ints;
    }
}
