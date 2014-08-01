package lotr.common.world.biome;

import java.util.Random;

import lotr.common.entity.animal.LOTREntityFlamingo;
import lotr.common.entity.animal.LOTREntityJungleScorpion;
import lotr.common.world.LOTRBanditSpawner;
import lotr.common.world.feature.LOTRWorldGenSimpleTrees;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenBigTree;
import net.minecraft.world.gen.feature.WorldGenMegaJungle;
import net.minecraft.world.gen.feature.WorldGenMelon;
import net.minecraft.world.gen.feature.WorldGenShrub;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.world.gen.feature.WorldGenVines;

public class LOTRBiomeGenFarHaradJungle extends LOTRBiomeGenFarHarad
{
    public LOTRBiomeGenFarHaradJungle(int i)
    {
        super(i);
        
        spawnableCreatureList.clear();
        spawnableCreatureList.add(new SpawnListEntry(LOTREntityFlamingo.class, 10, 4, 4));
        
        spawnableMonsterList.add(new SpawnListEntry(LOTREntityJungleScorpion.class, 30, 4, 4));

        decorator.treesPerChunk = 50;
        decorator.flowersPerChunk = 4;
        decorator.doubleFlowersPerChunk = 4;
        decorator.grassPerChunk = 10;
		decorator.doubleGrassPerChunk = 10;
        decorator.enableFern = true;
		
		registerJungleFlowers();
		
		setBanditChance(LOTRBanditSpawner.NEVER);
    }

    @Override
    public WorldGenAbstractTree func_150567_a(Random random)
    {
		if (random.nextInt(100) == 0)
		{
			return LOTRWorldGenSimpleTrees.newMango(false);
		}
    	if (random.nextInt(10) == 0)
    	{
    		return new WorldGenBigTree(false);
    	}
    	else if (random.nextBoolean())
    	{
    		return new WorldGenShrub(3, 0);
    	}
    	else if (random.nextInt(3) == 0)
    	{
    		return new WorldGenMegaJungle(false, 10, 20, 3, 3);
    	}
    	return new WorldGenTrees(false, 4 + random.nextInt(7), 3, 3, true);
    }

    @Override
    public void decorate(World world, Random random, int i, int k)
    {
        super.decorate(world, random, i, k);
        
        WorldGenVines vines = new WorldGenVines();
        for (int l = 0; l < 50; l++)
        {
            int i1 = i + random.nextInt(16) + 8;
            int j1 = 64;
            int k1 = k + random.nextInt(16) + 8;
            vines.generate(world, random, i1, j1, k1);
        }
        
        if (random.nextInt(3) == 0)
        {
            int i1 = i + random.nextInt(16) + 8;
            int k1 = k + random.nextInt(16) + 8;
			int j1 = world.getHeightValue(i1, k1);
			new WorldGenMelon().generate(world, random, i1, j1, k1);
        }
    }
	
	@Override
	public float getChanceToSpawnLakes()
	{
		return 1F;
	}
}
