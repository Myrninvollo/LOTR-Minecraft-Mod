package lotr.common.world.biome;

import java.util.Random;

import lotr.common.LOTRWaypoint;
import lotr.common.entity.npc.LOTREntityGundabadWarg;
import lotr.common.world.feature.LOTRWorldGenDeadTrees;
import lotr.common.world.structure2.LOTRWorldGenRuinedHouse;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenTaiga1;
import net.minecraft.world.gen.feature.WorldGenTaiga2;

public class LOTRBiomeGenTundra extends LOTRBiome
{
	public LOTRBiomeGenTundra(int i)
	{
		super(i);
		
		setEnableSnow();
		
		spawnableCreatureList.clear();
		spawnableCreatureList.add(new SpawnListEntry(EntityWolf.class, 10, 4, 8));
		
		spawnableGoodList.clear();
		
		spawnableEvilList.add(new SpawnListEntry(LOTREntityGundabadWarg.class, 12, 4, 4));
		
		setGoodEvilWeight(0, 100);

		decorator.treesPerChunk = 0;
		decorator.flowersPerChunk = 1;
		decorator.grassPerChunk = 2;
		decorator.doubleGrassPerChunk = 1;
		decorator.generateOrcDungeon = true;
		
		registerTaigaFlowers();
		
		decorator.addRandomStructure(new LOTRWorldGenRuinedHouse(false), 1500);
	}
	
	@Override
	public LOTRWaypoint.Region getBiomeWaypoints()
	{
		return LOTRWaypoint.Region.FORODWAITH;
	}
	
	@Override
    public WorldGenAbstractTree func_150567_a(Random random)
    {
		if (random.nextInt(3) == 0)
		{
			return LOTRWorldGenDeadTrees.newSpruce();
		}
        return random.nextInt(3) == 0 ? new WorldGenTaiga1() : new WorldGenTaiga2(false);
    }
	
	@Override
	public float getTreeIncreaseChance()
	{
		return 0.04F;
	}
	
	@Override
	public float getChanceToSpawnAnimals()
	{
		return 0.02F;
	}
	
	@Override
	public int getSnowHeight()
	{
		return 68;
	}
	
	@Override
	public int spawnCountMultiplier()
	{
		return 2;
	}
}
