package lotr.common.world.biome;


public class LOTRBiomeGenMirkwoodMountains extends LOTRBiomeGenMirkwood
{
	public LOTRBiomeGenMirkwoodMountains(int i)
	{
		super(i, true);
		
		decorator.treesPerChunk = 3;
		decorator.vinesPerChunk = 4;
	}
	
	@Override
	public boolean getEnableRiver()
	{
		return false;
	}
}
