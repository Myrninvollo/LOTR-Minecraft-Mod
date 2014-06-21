package lotr.common.world.biome;

import java.util.Random;

import lotr.common.LOTRAchievement;
import lotr.common.world.feature.LOTRWorldGenBoulder;
import lotr.common.world.feature.LOTRWorldGenDeadTrees;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenerator;

public class LOTRBiomeGenMinhiriath extends LOTRBiomeGenEriador
{
	protected WorldGenerator boulderGen = new LOTRWorldGenBoulder(Blocks.stone, 0, 1, 3);
	
	public LOTRBiomeGenMinhiriath(int i)
	{
		super(i);

		decorator.grassPerChunk = 3;
		decorator.doubleGrassPerChunk = 3;
	}
	
	@Override
	public LOTRAchievement getBiomeAchievement()
	{
		return LOTRAchievement.enterMinhiriath;
	}
	
	@Override
    public void decorate(World world, Random random, int i, int k)
    {
        super.decorate(world, random, i, k);
		
		if (random.nextInt(16) == 0)
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
		if (random.nextBoolean())
		{
			return super.func_150567_a(random);
		}
		else
		{
			if (random.nextInt(30) == 0)
			{
				return LOTRWorldGenDeadTrees.newBirch();
			}
			else if (random.nextInt(8) == 0)
			{
				return LOTRWorldGenDeadTrees.newBeech();
			}
			else if (random.nextInt(4) == 0)
			{
				return LOTRWorldGenDeadTrees.newSpruce();
			}
			return LOTRWorldGenDeadTrees.newOak();
		}
	}
	
	@Override
	public float getTreeIncreaseChance()
	{
		return 0.1F;
	}
	
	@Override
	public float getChanceToSpawnAnimals()
	{
		return 0.05F;
	}
}
