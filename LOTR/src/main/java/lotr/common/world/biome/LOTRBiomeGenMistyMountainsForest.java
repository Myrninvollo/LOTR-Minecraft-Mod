package lotr.common.world.biome;

import net.minecraft.entity.passive.EntityWolf;

public class LOTRBiomeGenMistyMountainsForest extends LOTRBiomeGenMistyMountains
{
	public LOTRBiomeGenMistyMountainsForest(int i)
	{
		super(i);
		
		spawnableCreatureList.add(new SpawnListEntry(EntityWolf.class, 10, 4, 8));

		decorator.treesPerChunk = 7;
		decorator.flowersPerChunk = 1;
		decorator.grassPerChunk = 3;
		decorator.doubleGrassPerChunk = 2;
		decorator.generateWater = true;
	}
	
	@Override
	public boolean isSnowCovered()
	{
		return false;
	}
	
	@Override
	public float getChanceToSpawnAnimals()
	{
		return 0.5F;
	}
}
