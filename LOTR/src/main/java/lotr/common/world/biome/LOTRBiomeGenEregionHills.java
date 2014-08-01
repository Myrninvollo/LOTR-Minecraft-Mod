package lotr.common.world.biome;

public class LOTRBiomeGenEregionHills extends LOTRBiomeGenEregion
{
	public LOTRBiomeGenEregionHills(int i)
	{
		super(i);
		
		decorator.treesPerChunk = 0;
        decorator.flowersPerChunk = 1;
        decorator.grassPerChunk = 2;
	}

	@Override
	public float getTreeIncreaseChance()
	{
		return 0.1F;
	}
}
