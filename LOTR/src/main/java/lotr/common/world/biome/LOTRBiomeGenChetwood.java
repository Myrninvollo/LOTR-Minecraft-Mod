package lotr.common.world.biome;

import lotr.common.LOTRAchievement;
import lotr.common.entity.animal.LOTREntityBird;
import lotr.common.entity.animal.LOTREntityButterfly;
import lotr.common.entity.animal.LOTREntityRabbit;
import net.minecraft.entity.passive.EntityWolf;

public class LOTRBiomeGenChetwood extends LOTRBiomeGenBreeland
{
	public LOTRBiomeGenChetwood(int i)
	{
		super(i);
		
		spawnableCreatureList.add(new SpawnListEntry(EntityWolf.class, 2, 2, 6));
		
		spawnableLOTRAmbientList.add(new SpawnListEntry(LOTREntityButterfly.class, 8, 4, 4));
		spawnableLOTRAmbientList.add(new SpawnListEntry(LOTREntityRabbit.class, 6, 4, 4));
		spawnableLOTRAmbientList.add(new SpawnListEntry(LOTREntityBird.class, 12, 4, 4));
		
		decorator.treesPerChunk = 4;
		decorator.flowersPerChunk = 4;
		decorator.doubleFlowersPerChunk = 2;
		decorator.grassPerChunk = 3;
		decorator.doubleGrassPerChunk = 1;
		
		registerForestFlowers();
	}
	
	@Override
	public LOTRAchievement getBiomeAchievement()
	{
		return LOTRAchievement.enterChetwood;
	}
}
