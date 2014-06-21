package lotr.common.world.biome;

import java.util.Random;

import lotr.common.entity.animal.LOTREntityBird;
import lotr.common.world.feature.LOTRWorldGenLogs;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenMelon;
import net.minecraft.world.gen.feature.WorldGenVines;

public class LOTRBiomeGenFarHaradForest extends LOTRBiomeGenFarHarad
{
	public LOTRBiomeGenFarHaradForest(int i)
	{
		super(i);
		
		spawnableLOTRAmbientList.add(new SpawnListEntry(LOTREntityBird.class, 10, 4, 4));
		
		decorator.treesPerChunk = 10;
		decorator.logsPerChunk = 3;
		decorator.grassPerChunk = 4;
		decorator.flowersPerChunk = 4;
		decorator.doubleFlowersPerChunk = 3;
	}
	
	@Override
    public void decorate(World world, Random random, int i, int k)
    {
        super.decorate(world, random, i, k);

        WorldGenVines vines = new WorldGenVines();
        for (int l = 0; l < 10; l++)
        {
            int i1 = i + random.nextInt(16) + 8;
            int j1 = 64;
            int k1 = k + random.nextInt(16) + 8;
            vines.generate(world, random, i1, j1, k1);
        }
        
        if (random.nextInt(16) == 0)
        {
            int i1 = i + random.nextInt(16) + 8;
            int k1 = k + random.nextInt(16) + 8;
			int j1 = world.getHeightValue(i1, k1);
			new WorldGenMelon().generate(world, random, i1, j1, k1);
        }
	}
}
