package lotr.common.world.biome;

import java.util.Random;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRWaypoint;
import lotr.common.entity.animal.LOTREntityCamel;
import lotr.common.entity.animal.LOTREntityDesertScorpion;
import lotr.common.world.LOTRBanditSpawner;
import lotr.common.world.feature.LOTRWorldGenBoulder;
import lotr.common.world.feature.LOTRWorldGenDeadTrees;
import lotr.common.world.structure.LOTRWorldGenHaradObelisk;
import lotr.common.world.structure2.LOTRWorldGenNearHaradDesertCamp;
import lotr.common.world.structure2.LOTRWorldGenNearHaradFortress;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenerator;

public class LOTRBiomeGenNearHarad extends LOTRBiome
{
	public static interface ImmuneToHeat
	{
	}
	
	protected WorldGenerator boulderGen = new LOTRWorldGenBoulder(Blocks.stone, 0, 1, 3).setSpawnBlock(Blocks.sand, 0);
	
	public LOTRBiomeGenNearHarad(int i)
	{
		super(i);
		
		setDisableRain();
		
		topBlock = Blocks.sand;
		fillerBlock = Blocks.sand;
		
		spawnableCreatureList.clear();
		spawnableCreatureList.add(new SpawnListEntry(LOTREntityCamel.class, 10, 2, 6));
		
		spawnableMonsterList.add(new SpawnListEntry(LOTREntityDesertScorpion.class, 10, 4, 4));
		
		spawnableEvilList.clear();
		
		setGoodEvilWeight(0, 100);
		
		decorator.grassPerChunk = 0;
		decorator.doubleGrassPerChunk = 0;
		decorator.cactiPerChunk = 1;
		decorator.deadBushPerChunk = 1;
		
		decorator.addRandomStructure(new LOTRWorldGenHaradObelisk(false), 3000);
		decorator.addRandomStructure(new LOTRWorldGenNearHaradFortress(false), 3000);
		decorator.addRandomStructure(new LOTRWorldGenNearHaradDesertCamp(false), 3000);
		
		setBanditChance(LOTRBanditSpawner.NEVER);
	}
	
	@Override
	public LOTRAchievement getBiomeAchievement()
	{
		return LOTRAchievement.enterNearHarad;
	}
	
	@Override
	public LOTRWaypoint.Region getBiomeWaypoints()
	{
		return LOTRWaypoint.Region.NEAR_HARAD;
	}
	
	@Override
	public boolean getEnableRiver()
	{
		return false;
	}
	
	@Override
    public void decorate(World world, Random random, int i, int k)
    {
		super.decorate(world, random, i, k);
		
		if (random.nextInt(80) == 0)
		{
			int boulders = 1 + random.nextInt(4);
			for (int l = 0; l < boulders; l++)
			{
				int i1 = i + random.nextInt(16) + 8;
				int k1 = k + random.nextInt(16) + 8;
				boulderGen.generate(world, random, i1, world.getHeightValue(i1, k1), k1);
			}
		}
		
		if (random.nextInt(2000) == 0)
		{
			int trees = 1 + random.nextInt(4);
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
		return LOTRWorldGenDeadTrees.newOak();
    }
	
	@Override
	public float getTreeIncreaseChance()
	{
		return 0F;
	}
	
	@Override
	public float getChanceToSpawnAnimals()
	{
		return 0.05F;
	}
	
	@Override
	public float getChanceToSpawnLakes()
	{
		return 0.001F;
	}
}
