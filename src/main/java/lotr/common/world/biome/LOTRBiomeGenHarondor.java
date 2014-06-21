package lotr.common.world.biome;

import java.util.Random;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRWaypoint;
import lotr.common.entity.npc.LOTREntityNearHaradrimArcher;
import lotr.common.entity.npc.LOTREntityNearHaradrimWarrior;
import lotr.common.entity.npc.LOTREntityRangerIthilien;
import lotr.common.world.feature.LOTRWorldGenBoulder;
import lotr.common.world.feature.LOTRWorldGenDesertTrees;
import lotr.common.world.structure.LOTRWorldGenHaradObelisk;
import lotr.common.world.structure2.LOTRWorldGenNearHaradDesertCamp;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;

public class LOTRBiomeGenHarondor extends LOTRBiome
{
	protected WorldGenerator harondorDirtPatchGen = new WorldGenMinable(Blocks.dirt, 1, 50, Blocks.sand);
	private WorldGenerator boulderGen = new LOTRWorldGenBoulder(Blocks.stone, 0, 1, 3).setSpawnBlock(Blocks.sand, 0);
	
	public LOTRBiomeGenHarondor(int i)
	{
		super(i);
		
		setDisableRain();
		
		topBlock = Blocks.sand;
		fillerBlock = Blocks.sand;
		
		spawnableGoodList.add(new SpawnListEntry(LOTREntityRangerIthilien.class, 10, 4, 4));
		
		spawnableEvilList.clear();
		spawnableEvilList.add(new SpawnListEntry(LOTREntityNearHaradrimWarrior.class, 4, 4, 4));
		spawnableEvilList.add(new SpawnListEntry(LOTREntityNearHaradrimArcher.class, 4, 4, 4));
		
		setGoodEvilWeight(50, 50);
		
		decorator.grassPerChunk = 4;
		decorator.doubleGrassPerChunk = 1;
		decorator.cactiPerChunk = 1;
		decorator.deadBushPerChunk = 1;
		
		decorator.addRandomStructure(new LOTRWorldGenHaradObelisk(false), 1000);
		decorator.addRandomStructure(new LOTRWorldGenNearHaradDesertCamp(false), 1200);
	}
	
	@Override
	public LOTRAchievement getBiomeAchievement()
	{
		return LOTRAchievement.enterHarondor;
	}
	
	@Override
	public LOTRWaypoint.Region getBiomeWaypoints()
	{
		return LOTRWaypoint.Region.HARONDOR;
	}
	
	@Override
    public void decorate(World world, Random random, int i, int k)
    {
		genStandardOre(world, random, i, k, 6, harondorDirtPatchGen, 60, 80);
		
		super.decorate(world, random, i, k);
		
		if (random.nextInt(16) == 0)
		{
			int boulders = 1 + random.nextInt(4);
			for (int l = 0; l < boulders; l++)
			{
				int i1 = i + random.nextInt(16) + 8;
				int k1 = k + random.nextInt(16) + 8;
				boulderGen.generate(world, random, i1, world.getHeightValue(i1, k1), k1);
			}
		}
		
		if (random.nextInt(8) == 0)
		{
			int trees = 1 + random.nextInt(8);
			for (int l = 0; l < trees; l++)
			{
				int i1 = i + random.nextInt(8) + 8;
				int k1 = k + random.nextInt(8) + 8;
				func_150567_a(random).generate(world, random, i1, world.getHeightValue(i1, k1), k1);
			}
		}
    }
	
	@Override
    public WorldGenAbstractTree func_150567_a(Random random)
    {
		return new LOTRWorldGenDesertTrees();
    }
	
	@Override
	public float getChanceToSpawnAnimals()
	{
		return 0.05F;
	}
	
	@Override
	public float getChanceToSpawnLakes()
	{
		return 0.05F;
	}
	
	@Override
	public int spawnCountMultiplier()
	{
		return 5;
	}
}
