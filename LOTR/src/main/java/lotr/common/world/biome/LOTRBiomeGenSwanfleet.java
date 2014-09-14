package lotr.common.world.biome;

import java.util.Random;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRFaction;
import lotr.common.world.LOTRBanditSpawner;
import lotr.common.world.LOTRInvasionSpawner;
import lotr.common.world.LOTRInvasionSpawner.BiomeInvasionListEntry;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenBigTree;
import net.minecraft.world.gen.feature.WorldGenForest;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraft.world.gen.feature.WorldGenSwamp;
import net.minecraft.world.gen.feature.WorldGenTrees;

public class LOTRBiomeGenSwanfleet extends LOTRBiomeGenEriador
{
	public LOTRBiomeGenSwanfleet(int i)
	{
		super(i);
		
		spawnableGoodList.clear();
		
		setGoodEvilWeight(0, 100);

		decorator.quagmirePerChunk = 1;
		decorator.treesPerChunk = 1;
		decorator.flowersPerChunk = 4;
		decorator.grassPerChunk = 6;
		decorator.doubleGrassPerChunk = 6;
		decorator.enableFern = true;
		decorator.waterlilyPerChunk = 2;
		decorator.reedsPerChunk = 10;
		
		registerSwampFlowers();
		
		setBanditChance(LOTRBanditSpawner.RARE);
		
		invasionSpawns.add(new BiomeInvasionListEntry(LOTRFaction.GUNDABAD, LOTRInvasionSpawner.RARE));
	}
	
	@Override
	public LOTRAchievement getBiomeAchievement()
	{
		return LOTRAchievement.enterSwanfleet;
	}
	
	@Override
    public WorldGenAbstractTree func_150567_a(Random random)
    {
		if (random.nextInt(3) == 0)
		{
			return new WorldGenBigTree(false);
		}
		else if (random.nextBoolean())
		{
			return new WorldGenSwamp();
		}
		else if (random.nextInt(8) == 0)
		{
			return new WorldGenForest(false, false);
		}
		else
		{
			return new WorldGenTrees(false, 4 + random.nextInt(4), 0, 0, false);
		}
    }
	
	@Override
    public void decorate(World world, Random random, int i, int k)
    {
        super.decorate(world, random, i, k);
		
		for (int l = 0; l < 6; l++)
		{
            int i1 = i + random.nextInt(16) + 8;
            int k1 = k + random.nextInt(16) + 8;
			int j1 = world.getHeightValue(i1, k1);
			new WorldGenLakes(Blocks.water).generate(world, random, i1, j1, k1);
		}
	}
}
