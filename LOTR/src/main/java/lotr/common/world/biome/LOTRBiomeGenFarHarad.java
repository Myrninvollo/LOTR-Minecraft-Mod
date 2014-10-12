package lotr.common.world.biome;

import java.util.Random;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRWaypoint;
import lotr.common.entity.animal.*;
import lotr.common.world.LOTRBanditSpawner;
import lotr.common.world.feature.LOTRWorldGenBoulder;
import lotr.common.world.feature.LOTRWorldGenDesertTrees;
import lotr.common.world.feature.LOTRWorldGenDoubleFlower;
import lotr.common.world.feature.LOTRWorldGenSimpleTrees;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase.SpawnListEntry;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenSavannaTree;
import net.minecraft.world.gen.feature.WorldGenerator;

public class LOTRBiomeGenFarHarad extends LOTRBiome
{
	private WorldGenerator boulderGen = new LOTRWorldGenBoulder(Blocks.stone, 0, 1, 3);
	
	public LOTRBiomeGenFarHarad(int i)
	{
		super(i);
		
		spawnableCreatureList.clear();
		spawnableCreatureList.add(new SpawnListEntry(LOTREntityLion.class, 4, 2, 4));
		spawnableCreatureList.add(new SpawnListEntry(LOTREntityLioness.class, 4, 2, 4));
		spawnableCreatureList.add(new SpawnListEntry(LOTREntityGiraffe.class, 4, 4, 6));
		spawnableCreatureList.add(new SpawnListEntry(LOTREntityZebra.class, 8, 4, 8));
		spawnableCreatureList.add(new SpawnListEntry(LOTREntityRhino.class, 8, 4, 4));
		spawnableCreatureList.add(new SpawnListEntry(LOTREntityGemsbok.class, 8, 4, 8));
		
		spawnableLOTRAmbientList.add(new SpawnListEntry(LOTREntityBird.class, 10, 4, 4));
		spawnableLOTRAmbientList.add(new SpawnListEntry(LOTREntityDikDik.class, 10, 4, 6));
		
		spawnableMonsterList.add(new SpawnListEntry(LOTREntityCrocodile.class, 10, 4, 4));

		spawnableEvilList.clear();
		
		setGoodEvilWeight(0, 100);
		
		decorator.treesPerChunk = 0;
		decorator.setTreeCluster(3, 8);
		decorator.grassPerChunk = 14;
		decorator.doubleGrassPerChunk = 12;
		decorator.flowersPerChunk = 3;
		decorator.doubleFlowersPerChunk = 1;
		
		registerHaradFlowers();
		
		setBanditChance(LOTRBanditSpawner.NEVER);
	}

	@Override
	public LOTRAchievement getBiomeAchievement()
	{
		return LOTRAchievement.enterFarHarad;
	}
	
	@Override
	public LOTRWaypoint.Region getBiomeWaypoints()
	{
		return LOTRWaypoint.Region.FAR_HARAD;
	}
	
	@Override
    public void decorate(World world, Random random, int i, int k)
    {
		super.decorate(world, random, i, k);
		
		if (random.nextInt(32) == 0)
		{
			int boulders = 1 + random.nextInt(4);
			for (int l = 0; l < boulders; l++)
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
		if (random.nextInt(1000) == 0)
		{
			return LOTRWorldGenSimpleTrees.newMango(false);
		}
		if (random.nextInt(3) > 0)
		{
			return new WorldGenSavannaTree(false);
		}
		return new LOTRWorldGenDesertTrees();
    }
	
	@Override
	public float getTreeIncreaseChance()
	{
		return 0.15F;
	}
	
	@Override
	public WorldGenerator getRandomWorldGenForDoubleFlower(Random random)
	{
		LOTRWorldGenDoubleFlower doubleFlowerGen = new LOTRWorldGenDoubleFlower();
		if (random.nextInt(5) == 0)
		{
			doubleFlowerGen.setFlowerType(3);
		}
		else
		{
			doubleFlowerGen.setFlowerType(2);
		}
        return doubleFlowerGen;
	}
	
	@Override
	public float getChanceToSpawnAnimals()
	{
		return 0.75F;
	}
	
	@Override
	public float getChanceToSpawnLakes()
	{
		return 0.1F;
	}
}
