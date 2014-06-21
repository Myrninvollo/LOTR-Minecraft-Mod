package lotr.common.world.biome;

public class LOTRBiomeGenGondorHills extends LOTRBiomeGenGondor
{
	public LOTRBiomeGenGondorHills(int i)
	{
		super(i);
		
		decorator.treesPerChunk = 1;
		decorator.flowersPerChunk = 2;
		decorator.grassPerChunk = 3;
		decorator.doubleGrassPerChunk = 1;
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
