package lotr.common.world.biome;

import java.util.Random;

import lotr.common.entity.npc.LOTREntityHobbitOrcharder;
import lotr.common.world.feature.LOTRWorldGenSimpleTrees;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public class LOTRBiomeGenShireOrchard extends LOTRBiomeGenShire
{
	public LOTRBiomeGenShireOrchard(int i)
	{
		super(i);
		
		spawnableGoodList.add(new SpawnListEntry(LOTREntityHobbitOrcharder.class, 20, 1, 1));
		
		decorator.treesPerChunk = 0;
		decorator.flowersPerChunk = 6;
		decorator.doubleFlowersPerChunk = 3;
		decorator.grassPerChunk = 6;
		decorator.doubleGrassPerChunk = 1;
	}

	@Override
	public boolean hasShireStructures()
	{
		return false;
	}
	
	@Override
    public void decorate(World world, Random random, int i, int k)
    {
		for (int i1 = i + 3; i1 < i + 16; i1 += 8)
		{
			int k1 = k + 8;
			int j1 = world.getHeightValue(i1, k1);
			func_150567_a(random).generate(world, random, i1, j1, k1);
		}
		
		super.decorate(world, random, i, k);
	}
	
	@Override
    public WorldGenAbstractTree func_150567_a(Random random)
    {
		if (random.nextInt(20) == 0)
		{
			return LOTRWorldGenSimpleTrees.newCherry(false);
		}
		else
		{
			return random.nextBoolean() ? LOTRWorldGenSimpleTrees.newApple(false) : LOTRWorldGenSimpleTrees.newPear(false);
		}
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
		return 0F;
	}
	
	@Override
	public float getChanceToSpawnLavaLakes()
	{
		return 0F;
	}

	@Override
	public int spawnCountMultiplier()
	{
		return 5;
	}
}
