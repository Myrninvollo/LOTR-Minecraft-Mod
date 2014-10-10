package lotr.common.world.biome;

import java.util.Random;

import lotr.common.world.feature.LOTRWorldGenHugeTrees;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.world.biome.BiomeGenBase.SpawnListEntry;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenBigTree;

public class LOTRBiomeGenAnduinWoodlandsDense extends LOTRBiomeGenAnduinWoodlands
{
	public LOTRBiomeGenAnduinWoodlandsDense(int i)
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
			return new WorldGenBigTree(false);
		}
		else
		{
			return super.func_150567_a(random);
		}
    }
}
