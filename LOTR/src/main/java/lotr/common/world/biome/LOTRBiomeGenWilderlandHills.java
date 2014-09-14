package lotr.common.world.biome;

public class LOTRBiomeGenWilderlandHills extends LOTRBiomeGenWilderland
{
	public LOTRBiomeGenWilderlandHills(int i)
	{
		super(i);
		
		decorator.treesPerChunk = 1;
		decorator.flowersPerChunk = 1;
		decorator.grassPerChunk = 8;
		decorator.doubleGrassPerChunk = 2;
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
