package lotr.common.world;

import lotr.common.LOTRDimension;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.chunk.IChunkProvider;

public class LOTRWorldProviderUtumno extends LOTRWorldProvider
{
	public static interface UtumnoBlock
	{
	}
	
	public LOTRWorldProviderUtumno()
	{
		super();
		hasNoSky = true;
	}
	
	@Override
	public LOTRDimension getLOTRDimension()
	{
		return LOTRDimension.UTUMNO;
	}
	
	@Override
    public IChunkProvider createChunkGenerator()
    {
        return new LOTRChunkProviderUtumno(worldObj, worldObj.getSeed());
    }
	
	@Override
    protected void generateLightBrightnessTable()
    {
        for (int i = 0; i <= 15; i++)
        {
            float f = (float)i / 15F;
            lightBrightnessTable[i] = (float)Math.pow(f, 5D);
        }
    }
	
	@Override
    public float calculateCelestialAngle(long time, float f)
    {
        return 0.5F;
    }
	
	@Override
    public boolean canRespawnHere()
    {
        return false;
    }
	
	@Override
    public int getRespawnDimension(EntityPlayerMP entityplayer)
    {
        return LOTRDimension.MIDDLE_EARTH.dimensionID;
    }
	
	@Override
	public boolean isSurfaceWorld()
	{
		return false;
	}

	@Override
    public void setSpawnPoint(int i, int j, int k) {}
}
