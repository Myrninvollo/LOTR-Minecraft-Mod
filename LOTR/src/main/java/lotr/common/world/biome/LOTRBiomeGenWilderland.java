package lotr.common.world.biome;

import java.util.Random;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRWaypoint;
import lotr.common.entity.animal.LOTREntityHorse;
import lotr.common.entity.npc.LOTREntityBlueDwarfMerchant;
import lotr.common.world.feature.LOTRWorldGenBoulder;
import lotr.common.world.feature.LOTRWorldGenDeadTrees;
import lotr.common.world.structure.LOTRWorldGenGundabadCamp;
import lotr.common.world.structure2.LOTRWorldGenRuinedHouse;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenerator;

public class LOTRBiomeGenWilderland extends LOTRBiome
{
	private WorldGenerator boulderGen = new LOTRWorldGenBoulder(Blocks.stone, 2, 1, 3);
	
	public LOTRBiomeGenWilderland(int i)
	{
		super(i);
		
		spawnableCreatureList.add(new SpawnListEntry(LOTREntityHorse.class, 5, 2, 6));
		
		decorator.flowersPerChunk = 2;
		decorator.doubleFlowersPerChunk = 1;
        decorator.grassPerChunk = 7;
		decorator.doubleGrassPerChunk = 8;
		
		registerPlainsFlowers();
		
		decorator.addRandomStructure(new LOTRWorldGenGundabadCamp(), 2000);
		decorator.addRandomStructure(new LOTRWorldGenRuinedHouse(false), 1500);
		
		registerTravellingTrader(LOTREntityBlueDwarfMerchant.class);
	}
	
	@Override
	public LOTRAchievement getBiomeAchievement()
	{
		return LOTRAchievement.enterWilderland;
	}
	
	@Override
	public LOTRWaypoint.Region getBiomeWaypoints()
	{
		return LOTRWaypoint.Region.WILDERLAND;
	}
	
	@Override
    public void decorate(World world, Random random, int i, int k)
    {
        super.decorate(world, random, i, k);
		
		if (random.nextInt(24) == 0)
		{
			for (int l = 0; l < 4; l++)
			{
				int i1 = i + random.nextInt(16) + 8;
				int k1 = k + random.nextInt(16) + 8;
				boulderGen.generate(world, random, i1, world.getHeightValue(i1, k1), k1);
			}
		}
	}
	
	@Override
    public WorldGenAbstractTree func_150567_a(Random random)
    {
        return random.nextBoolean() ? LOTRWorldGenDeadTrees.newOak() : super.func_150567_a(random);
    }
	
	@Override
	public float getChanceToSpawnAnimals()
	{
		return 0.1F;
	}
	
	@Override
	public float getChanceToSpawnLakes()
	{
		return 0.1F;
	}
	
	@Override
	public float getTreeIncreaseChance()
	{
		return 0.05F;
	}
}
