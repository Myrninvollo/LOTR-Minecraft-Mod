package lotr.common.world.biome;

import java.util.Random;

import lotr.common.entity.npc.LOTREntityUrukHai;
import lotr.common.entity.npc.LOTREntityUrukHaiBerserker;
import lotr.common.entity.npc.LOTREntityUrukHaiCrossbower;
import lotr.common.entity.npc.LOTREntityUrukWarg;
import lotr.common.world.feature.LOTRWorldGenBlastedLand;
import lotr.common.world.feature.LOTRWorldGenCharredTrees;
import lotr.common.world.feature.LOTRWorldGenDeadTrees;
import lotr.common.world.feature.LOTRWorldGenFangornTrees;
import lotr.common.world.feature.LOTRWorldGenLogs;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public class LOTRBiomeGenFangornWasteland extends LOTRBiome
{
	public LOTRBiomeGenFangornWasteland(int i)
	{
		super(i);
		
		spawnableCreatureList.clear();
		
		spawnableEvilList.clear();
		spawnableEvilList.add(new SpawnListEntry(LOTREntityUrukHai.class, 20, 4, 6));
		spawnableEvilList.add(new SpawnListEntry(LOTREntityUrukHaiCrossbower.class, 10, 4, 6));
		spawnableEvilList.add(new SpawnListEntry(LOTREntityUrukHaiBerserker.class, 3, 1, 2));
		spawnableEvilList.add(new SpawnListEntry(LOTREntityUrukWarg.class, 5, 4, 4));
		
		setGoodEvilWeight(0, 100);
		
		decorator.treesPerChunk = 1;
		decorator.logsPerChunk = 3;
		decorator.flowersPerChunk = 1;
		decorator.grassPerChunk = 3;
		decorator.doubleGrassPerChunk = 3;
		decorator.enableFern = true;
	}
	
	@Override
    public void decorate(World world, Random random, int i, int k)
    {
        super.decorate(world, random, i, k);
		
		if (random.nextInt(60) == 0)
		{
			int i1 = i + random.nextInt(16) + 8;
			int k1 = k + random.nextInt(16) + 8;
			new LOTRWorldGenBlastedLand().generate(world, random, i1, world.getHeightValue(i1, k1), k1);
		}
	}
	
	@Override
    public WorldGenAbstractTree func_150567_a(Random random)
    {
		if (random.nextInt(10) == 0)
		{
			if (random.nextInt(3) == 0)
			{
				return LOTRWorldGenFangornTrees.newCharred(false);
			}
			else
			{
				return random.nextInt(6) == 0 ? LOTRWorldGenFangornTrees.newBeech(false).setNoLeaves() : LOTRWorldGenFangornTrees.newOak(false).setNoLeaves();
			}
		}
		else if (random.nextInt(3) == 0)
		{
			return new LOTRWorldGenCharredTrees();
		}
		else 
		{
			if (random.nextInt(20) == 0)
			{
				return LOTRWorldGenDeadTrees.newBirch();
			}
			else if (random.nextInt(5) == 0)
			{
				return LOTRWorldGenDeadTrees.newBeech();
			}
			return LOTRWorldGenDeadTrees.newOak();
		}
    }
	
	@Override
	public float getTreeIncreaseChance()
	{
		return 0.25F;
	}
	
	@Override
	public boolean canSpawnHostilesInDay()
	{
		return true;
	}
	
	@Override
	public int spawnCountMultiplier()
	{
		return 3;
	}
}
