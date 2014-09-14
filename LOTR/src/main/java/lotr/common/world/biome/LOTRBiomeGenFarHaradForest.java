package lotr.common.world.biome;

import java.util.Random;

import lotr.common.entity.animal.LOTREntityBird;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenMelon;

public class LOTRBiomeGenFarHaradForest extends LOTRBiomeGenFarHarad
{
	public LOTRBiomeGenFarHaradForest(int i)
	{
		super(i);
		
		spawnableLOTRAmbientList.add(new SpawnListEntry(LOTREntityBird.class, 10, 4, 4));
		
		decorator.treesPerChunk = 10;
		decorator.vinesPerChunk = 10;
		decorator.logsPerChunk = 3;
		decorator.grassPerChunk = 8;
		decorator.flowersPerChunk = 4;
		decorator.doubleFlowersPerChunk = 3;
		
		biomeColors.setGrass(0xB1EA48);
		biomeColors.setFoliage(0x7FD13C);
	}
	
	@Override
    public void decorate(World world, Random random, int i, int k)
    {
        super.decorate(world, random, i, k);
        
        if (random.nextInt(16) == 0)
        {
            int i1 = i + random.nextInt(16) + 8;
            int k1 = k + random.nextInt(16) + 8;
			int j1 = world.getHeightValue(i1, k1);
			new WorldGenMelon().generate(world, random, i1, j1, k1);
        }
	}
}
