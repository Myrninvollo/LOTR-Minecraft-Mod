package lotr.common.world.biome;

public class LOTRBiomeGenLothlorienClearing extends LOTRBiomeGenLothlorien
{
	public LOTRBiomeGenLothlorienClearing(int i)
	{
		super(i);

		decorator.treesPerChunk = 0;
		decorator.flowersPerChunk = 8;
		decorator.grassPerChunk = 12;
		decorator.doubleGrassPerChunk = 2;
	}

	@Override
	public float getTreeIncreaseChance()
	{
		return 0F;
	}
}
