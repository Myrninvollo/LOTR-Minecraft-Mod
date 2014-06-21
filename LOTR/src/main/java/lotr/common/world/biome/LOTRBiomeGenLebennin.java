package lotr.common.world.biome;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase.SpawnListEntry;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenForest;
import net.minecraft.world.gen.feature.WorldGenerator;
import lotr.common.LOTRAchievement;
import lotr.common.LOTRMod;
import lotr.common.entity.animal.LOTREntityBird;
import lotr.common.entity.animal.LOTREntityButterfly;
import lotr.common.entity.animal.LOTREntityRabbit;
import lotr.common.world.feature.LOTRWorldGenBigTrees;
import lotr.common.world.feature.LOTRWorldGenBoulder;
import lotr.common.world.feature.LOTRWorldGenSimpleTrees;

public class LOTRBiomeGenLebennin extends LOTRBiomeGenGondor
{
	private WorldGenerator boulderGen = new LOTRWorldGenBoulder(LOTRMod.rock, 1, 2, 5);
	
	public LOTRBiomeGenLebennin(int i)
	{
		super(i);
		
		spawnableLOTRAmbientList.add(new SpawnListEntry(LOTREntityButterfly.class, 10, 4, 4));
		spawnableLOTRAmbientList.add(new SpawnListEntry(LOTREntityBird.class, 10, 4, 4));
		
		setGoodEvilWeight(90, 10);
		
		decorator.treesPerChunk = 1;
		decorator.flowersPerChunk = 5;
		decorator.grassPerChunk = 10;
		decorator.doubleGrassPerChunk = 6;
	}
	
	@Override
	public LOTRAchievement getBiomeAchievement()
	{
		return LOTRAchievement.enterLebennin;
	}
	
	@Override
    public void decorate(World world, Random random, int i, int k)
    {
        super.decorate(world, random, i, k);
		
		if (random.nextInt(30) == 0)
		{
			for (int l = 0; l < 3; l++)
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
		if (random.nextInt(8) == 0)
		{
			return random.nextInt(10) == 0 ? LOTRWorldGenBigTrees.newBeech(false) : LOTRWorldGenSimpleTrees.newBeech(false);
		}
		if (random.nextInt(4) == 0)
		{
			return new WorldGenForest(false, false);
		}
        return super.func_150567_a(random);
    }
}