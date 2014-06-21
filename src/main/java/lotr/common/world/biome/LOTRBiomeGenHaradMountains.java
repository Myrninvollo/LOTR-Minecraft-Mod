package lotr.common.world.biome;

public class LOTRBiomeGenHaradMountains extends LOTRBiomeGenFarHarad
{
	public LOTRBiomeGenHaradMountains(int i)
	{
		super(i);
	}
	
	@Override
	public boolean getEnableRiver()
	{
		return false;
	}
	
	@Override
	public float getChanceToSpawnAnimals()
	{
		return 0F;
	}
}
