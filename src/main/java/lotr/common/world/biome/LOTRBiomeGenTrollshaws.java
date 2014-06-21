package lotr.common.world.biome;

import java.util.Random;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRWaypoint;
import lotr.common.entity.animal.LOTREntityBird;
import lotr.common.entity.animal.LOTREntityButterfly;
import lotr.common.entity.animal.LOTREntityHorse;
import lotr.common.entity.animal.LOTREntityRabbit;
import lotr.common.entity.npc.LOTREntityRangerNorth;
import lotr.common.entity.npc.LOTREntityTroll;
import lotr.common.world.feature.LOTRWorldGenBigTrees;
import lotr.common.world.feature.LOTRWorldGenBoulder;
import lotr.common.world.feature.LOTRWorldGenSimpleTrees;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenBigTree;
import net.minecraft.world.gen.feature.WorldGenTaiga2;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.world.gen.feature.WorldGenerator;

public class LOTRBiomeGenTrollshaws extends LOTRBiome
{
	private WorldGenerator boulderGen = new LOTRWorldGenBoulder(Blocks.stone, 0, 2, 6);
	
	public LOTRBiomeGenTrollshaws(int i)
	{
		super(i);
		
		spawnableCreatureList.add(new SpawnListEntry(LOTREntityHorse.class, 5, 2, 6));
		spawnableCreatureList.add(new SpawnListEntry(EntityWolf.class, 10, 4, 8));
		
		spawnableGoodList.add(new SpawnListEntry(LOTREntityRangerNorth.class, 10, 4, 4));
		
		spawnableEvilList.add(new SpawnListEntry(LOTREntityTroll.class, 15, 1, 3));
		
		setGoodEvilWeight(1, 99);
		
		spawnableLOTRAmbientList.add(new SpawnListEntry(LOTREntityButterfly.class, 4, 4, 4));
		spawnableLOTRAmbientList.add(new SpawnListEntry(LOTREntityRabbit.class, 6, 4, 4));
		spawnableLOTRAmbientList.add(new SpawnListEntry(LOTREntityBird.class, 12, 4, 4));
		
		decorator.treesPerChunk = 1;
		decorator.flowersPerChunk = 3;
		decorator.doubleFlowersPerChunk = 2;
        decorator.grassPerChunk = 3;
		decorator.doubleGrassPerChunk = 2;
		decorator.generateAthelas = true;
		
		registerForestFlowers();
		
		decorator.generateOrcDungeon = true;
		decorator.generateTrollHoard = true;
	}
	
	@Override
	public LOTRAchievement getBiomeAchievement()
	{
		return LOTRAchievement.enterTrollshaws;
	}
	
	@Override
	public LOTRWaypoint.Region getBiomeWaypoints()
	{
		return LOTRWaypoint.Region.TROLLSHAWS;
	}
	
	@Override
    public void decorate(World world, Random random, int i, int k)
    {
        super.decorate(world, random, i, k);
		
		if (random.nextInt(8) == 0)
		{
			for (int l = 0; l < 3; l++)
			{
				int i1 = i + random.nextInt(16) + 8;
				int k1 = k + random.nextInt(16) + 8;
				boulderGen.generate(world, random, i1, world.getHeightValue(i1, k1), k1);
			}
		}
		
		for (int l = 0; l < 10; l++)
		{
            int i1 = i + random.nextInt(16) + 8;
            int k1 = k + random.nextInt(16) + 8;
			int j1 = world.getHeightValue(i1, k1);
			if (j1 < 82)
			{
				func_150567_a(random).generate(world, random, i1, j1, k1);
			}
		}
	}
	
	@Override
    public WorldGenAbstractTree func_150567_a(Random random)
    {
		if (random.nextInt(30) == 0)
		{
			return random.nextInt(8) == 0 ? LOTRWorldGenBigTrees.newMaple(false) : LOTRWorldGenSimpleTrees.newMaple(false);
		}
		else if (random.nextInt(3) == 0)
		{
			return new WorldGenTaiga2(false);
		}
		else if (random.nextBoolean())
		{
			return random.nextInt(8) == 0 ? LOTRWorldGenBigTrees.newBeech(false) : LOTRWorldGenSimpleTrees.newBeech(false);
		}
		else
		{
			return random.nextInt(8) == 0 ? new WorldGenBigTree(false) : new WorldGenTrees(false, 4 + random.nextInt(3), 0, 0, false);
		}
    }
}
