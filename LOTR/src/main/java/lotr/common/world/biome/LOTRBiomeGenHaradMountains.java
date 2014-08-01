package lotr.common.world.biome;

import lotr.common.world.LOTRBanditSpawner;

public class LOTRBiomeGenHaradMountains extends LOTRBiomeGenFarHarad
{
	public LOTRBiomeGenHaradMountains(int i)
	{
		super(i);
		
		setBanditChance(LOTRBanditSpawner.NEVER);
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
