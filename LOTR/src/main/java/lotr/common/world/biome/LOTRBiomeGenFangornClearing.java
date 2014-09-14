package lotr.common.world.biome;

import lotr.common.entity.npc.LOTREntityEnt;

public class LOTRBiomeGenFangornClearing extends LOTRBiomeGenFangorn
{
	public LOTRBiomeGenFangornClearing(int i)
	{
		super(i);
		
		spawnableGoodList.clear();
		spawnableGoodList.add(new SpawnListEntry(LOTREntityEnt.class, 10, 4, 4));

		decorator.treesPerChunk = 0;
		decorator.flowersPerChunk = 4;
		decorator.grassPerChunk = 10;
		decorator.doubleGrassPerChunk = 8;
	}

	@Override
	public float getTreeIncreaseChance()
	{
		return 0.1F;
	}
}
