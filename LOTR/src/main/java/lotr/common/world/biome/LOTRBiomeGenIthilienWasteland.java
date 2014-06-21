package lotr.common.world.biome;

import java.util.Random;

import lotr.common.world.feature.LOTRWorldGenDeadTrees;
import lotr.common.world.feature.LOTRWorldGenLogs;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public class LOTRBiomeGenIthilienWasteland extends LOTRBiomeGenIthilien
{
	public LOTRBiomeGenIthilienWasteland(int i)
	{
		super(i);
		
		spawnableCreatureList.clear();
		
		spawnableGoodList.clear();
		
		setGoodEvilWeight(0, 100);
		
		decorator.treesPerChunk = 4;
		decorator.logsPerChunk = 2;
        decorator.flowersPerChunk = 1;
        decorator.grassPerChunk = 6;
		decorator.doubleGrassPerChunk = 2;
	}

	@Override
    public WorldGenAbstractTree func_150567_a(Random random)
    {
		if (random.nextInt(3) != 0)
		{
			if (random.nextInt(40) == 0)
			{
				return LOTRWorldGenDeadTrees.newBirch();
			}
			else if (random.nextInt(5) == 0)
			{
				return LOTRWorldGenDeadTrees.newLebethron();
			}
			else
			{
				return LOTRWorldGenDeadTrees.newOak();
			}
		}
		return super.func_150567_a(random);
	}
}
