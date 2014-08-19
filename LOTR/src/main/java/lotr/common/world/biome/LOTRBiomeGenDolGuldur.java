package lotr.common.world.biome;

public class LOTRBiomeGenDolGuldur extends LOTRBiomeGenMirkwood
{
	public LOTRBiomeGenDolGuldur(int i)
	{
		super(i, true);
		
		decorator.clearRandomStructures();
		
		hasPodzol = false;
		decorator.treesPerChunk = 2;
		decorator.vinesPerChunk = 2;
		decorator.flowersPerChunk = 0;
		decorator.grassPerChunk = 3;
		decorator.doubleGrassPerChunk = 1;
		
		biomeColors.setFoggy(false);
	}
}
