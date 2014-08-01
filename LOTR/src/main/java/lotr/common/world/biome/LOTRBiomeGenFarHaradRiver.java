package lotr.common.world.biome;

import java.util.Random;

import lotr.common.entity.animal.LOTREntityFlamingo;
import lotr.common.world.LOTRBanditSpawner;
import lotr.common.world.feature.LOTRWorldGenBanana;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class LOTRBiomeGenFarHaradRiver extends LOTRBiomeGenFarHarad
{
	public LOTRBiomeGenFarHaradRiver(int i)
	{
		super(i);
		
		spawnableCreatureList.clear();
		spawnableCreatureList.add(new SpawnListEntry(LOTREntityFlamingo.class, 10, 4, 4));
		
		setBanditChance(LOTRBanditSpawner.NEVER);
	}
	
	@Override
	public boolean getEnableRiver()
	{
		return false;
	}

	@Override
	public void decorate(World world, Random random, int i, int k)
	{
		super.decorate(world, random, i, k);
		
		if (random.nextInt(3) == 0)
		{
			WorldGenerator bananaTree = new LOTRWorldGenBanana(false);
			int bananas = 3 + random.nextInt(8);
			for (int l = 0; l < bananas; l++)
			{
				int i1 = i + random.nextInt(16) + 8;
				int k1 = k + random.nextInt(16) + 8;
				int j1 = world.getTopSolidOrLiquidBlock(i1, k1);
				bananaTree.generate(world, random, i1, j1, k1);
			}
		}
	}
	
	@Override
	public float getChanceToSpawnAnimals()
	{
		return 3F;
	}
}
