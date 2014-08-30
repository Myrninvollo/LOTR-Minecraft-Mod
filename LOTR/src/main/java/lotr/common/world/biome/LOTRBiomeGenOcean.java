package lotr.common.world.biome;

import java.util.Random;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRWaypoint;
import lotr.common.world.LOTRBanditSpawner;
import lotr.common.world.feature.LOTRWorldGenBigTrees;
import lotr.common.world.feature.LOTRWorldGenSimpleTrees;
import lotr.common.world.structure.LOTRWorldGenUnderwaterElvenRuin;
import lotr.common.world.structure2.LOTRWorldGenNumenorRuin;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenForest;
import net.minecraft.world.gen.feature.WorldGenTaiga2;

public class LOTRBiomeGenOcean extends LOTRBiome
{
	private static Random iceRand = new Random();
	
	public LOTRBiomeGenOcean(int i)
	{
		super(i);
		
		spawnableEvilList.clear();
		
		decorator.treesPerChunk = 1;
		decorator.flowersPerChunk = 2;
		decorator.doubleFlowersPerChunk = 1;
		decorator.grassPerChunk = 4;
		decorator.doubleGrassPerChunk = 1;
		
		biomeColors.setWater(0x1565C1);
		
		decorator.addRandomStructure(new LOTRWorldGenNumenorRuin(false), 500);
		
		setBanditChance(LOTRBanditSpawner.NEVER);
	}
	
	@Override
	public LOTRWaypoint.Region getBiomeWaypoints()
	{
		return LOTRWaypoint.Region.OCEAN;
	}
	
	@Override
	public LOTRAchievement getBiomeAchievement()
	{
		return LOTRAchievement.enterOcean;
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
		
		if (i < LOTRWaypoint.MITHLOND.getXCoord() && k > LOTRWaypoint.SOUTH_FOROCHEL.getZCoord() && k < LOTRWaypoint.ERYN_VORN.getZCoord())
		{
			if (random.nextInt(200) == 0)
			{
				int i1 = i + random.nextInt(16) + 8;
				int k1 = k + random.nextInt(16) + 8;
				int j1 = world.getTopSolidOrLiquidBlock(i1, k1);
				new LOTRWorldGenUnderwaterElvenRuin(false).generate(world, random, i1, j1, k1);
			}
		}
	}
	
	@Override
    public WorldGenAbstractTree func_150567_a(Random random)
    {
		if (random.nextInt(300) == 0)
		{
			return random.nextBoolean() ? LOTRWorldGenSimpleTrees.newApple(false) : LOTRWorldGenSimpleTrees.newPear(false);
		}
		else if (random.nextInt(20) == 0)
		{
			return new WorldGenForest(false, false);
		}
		else if (random.nextInt(3) == 0)
		{
			return new WorldGenTaiga2(false);
		}
		else if (random.nextInt(40) == 0)
		{
			return random.nextInt(10) == 0 ? LOTRWorldGenBigTrees.newBeech(false) : LOTRWorldGenSimpleTrees.newBeech(false);
		}
		else
		{
			return super.func_150567_a(random);
		}
    }
	
	@Override
	public float getChanceToSpawnAnimals()
	{
		return 0.25F;
	}
	
	public static boolean isFrozen(int i, int k)
	{
		if (k > -30000)
		{
			return false;
		}
		else
		{
			int l = -60000 - k;
			l *= -1;
			if (l < 1)
			{
				return true;
			}
			else
			{
				iceRand.setSeed((long)i * 341873128712L + (long)k * 132897987541L);
				l -= 15000;
				if (l < 0)
				{
					l *= -1;
					l = (int)Math.sqrt(l);
					if (l < 2)
					{
						l = 2;
					}
					return iceRand.nextInt(l) != 0;
				}
				else
				{
					l = (int)Math.sqrt(l);
					if (l < 2)
					{
						l = 2;
					}
					return iceRand.nextInt(l) == 0;
				}
			}
		}
	}
}
