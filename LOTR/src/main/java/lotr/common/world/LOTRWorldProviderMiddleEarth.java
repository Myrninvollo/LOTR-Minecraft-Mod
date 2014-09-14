package lotr.common.world;

import lotr.common.LOTRDimension;
import lotr.common.LOTRLevelData;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.chunk.IChunkProvider;

public class LOTRWorldProviderMiddleEarth extends LOTRWorldProvider
{
	@Override
	public LOTRDimension getLOTRDimension()
	{
		return LOTRDimension.MIDDLE_EARTH;
	}
	
	@Override
    public IChunkProvider createChunkGenerator()
    {
        return new LOTRChunkProvider(worldObj, worldObj.getSeed());
    }
	
	@Override
    public ChunkCoordinates getSpawnPoint()
    {
        return new ChunkCoordinates(LOTRLevelData.middleEarthPortalX, LOTRLevelData.middleEarthPortalY, LOTRLevelData.middleEarthPortalZ);
    }

	@Override
    public void setSpawnPoint(int i, int j, int k)
    {
		if (!(i == 8 && j == 64 && k == 8) && !worldObj.isRemote)
		{
			LOTRLevelData.markMiddleEarthPortalLocation(i, j, k);
		}
    }
}
