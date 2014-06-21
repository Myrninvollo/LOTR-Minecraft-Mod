package lotr.common.world.biome;


public class LOTRBiomeGenRiver extends LOTRBiome
{
	public LOTRBiomeGenRiver(int i)
	{
		super(i);
		
		spawnableCreatureList.clear();
		
		spawnableEvilList.clear();
	}
	
	@Override
	public float getChanceToSpawnLakes()
	{
		return 0F;
	}
	
	@Override
	public float getChanceToSpawnLavaLakes()
	{
		return 0F;
	}
	
	@Override
	public float getTreeIncreaseChance()
	{
		return 0.5F;
	}
}
