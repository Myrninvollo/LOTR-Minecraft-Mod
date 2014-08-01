package lotr.common.world.biome;

public class LOTRBiomeGenIthilienHills extends LOTRBiomeGenIthilien
{
	public LOTRBiomeGenIthilienHills(int i)
	{
		super(i);
		
		decorator.treesPerChunk = 0;
		decorator.logsPerChunk = 0;
        decorator.flowersPerChunk = 2;
        decorator.grassPerChunk = 4;
	}

	@Override
	public float getTreeIncreaseChance()
	{
		return 0.25F;
	}
}
