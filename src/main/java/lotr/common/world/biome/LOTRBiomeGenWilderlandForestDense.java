package lotr.common.world.biome;

import java.util.Random;

import lotr.common.world.feature.LOTRWorldGenHugeTrees;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenBigTree;
import net.minecraft.world.gen.feature.WorldGenMegaPineTree;

public class LOTRBiomeGenWilderlandForestDense extends LOTRBiomeGenWilderlandForest
{
	public LOTRBiomeGenWilderlandForestDense(int i)
	{
		super(i);
		
		spawnableCreatureList.add(new SpawnListEntry(EntityWolf.class, 16, 4, 8));
		
		spawnableLOTRAmbientList.clear();
		
		hasPodzol = true;
		decorator.treesPerChunk = 12;
		decorator.logsPerChunk = 1;
	}
	
	@Override
    public WorldGenAbstractTree func_150567_a(Random random)
    {
		if (random.nextInt(8) == 0)
		{
			return LOTRWorldGenHugeTrees.newOak();
		}
		else if (random.nextInt(3) > 0)
		{
			if (random.nextInt(5) == 0)
			{
				return new WorldGenBigTree(false);
			}
			else
			{
				return new WorldGenMegaPineTree(false, true);
			}
		}
		else
		{
			return super.func_150567_a(random);
		}
    }
}
