package lotr.common.world.biome;


public class LOTRBiomeGenNurnen extends LOTRBiomeGenNurn
{
	public LOTRBiomeGenNurnen(int i)
	{
		super(i);
		
		spawnableEvilList.clear();
	}
	
	@Override
	public boolean getEnableRiver()
	{
		return false;
	}
}
