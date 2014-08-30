package lotr.common.world.biome;

public class LOTRBiomeGenAngmarMountains extends LOTRBiomeGenAngmar
{
	public LOTRBiomeGenAngmarMountains(int i)
	{
		super(i);
		
		spawnableCreatureList.clear();
		
		decorator.generateWater = false;
	}
	
	@Override
	public boolean getEnableRiver()
	{
		return false;
	}
	
	@Override
	public int spawnCountMultiplier()
	{
		return super.spawnCountMultiplier() * 2;
	}
}
