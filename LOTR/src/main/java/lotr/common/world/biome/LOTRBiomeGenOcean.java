package lotr.common.world.biome;

import java.awt.Color;
import java.util.*;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRWaypoint;
import lotr.common.world.LOTRBanditSpawner;
import lotr.common.world.feature.LOTRWorldGenBigTrees;
import lotr.common.world.feature.LOTRWorldGenSimpleTrees;
import lotr.common.world.structure.LOTRWorldGenUnderwaterElvenRuin;
import lotr.common.world.structure2.LOTRWorldGenNumenorRuin;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.*;

public class LOTRBiomeGenOcean extends LOTRBiome
{
	private static List<LOTRBiomeGenOcean> allOceanBiomes = new ArrayList();
	
	private static Random iceRand = new Random();
	
	private static int iceLimitSouth = -30000;
	private static int iceLimitNorth = -60000;
	
	private static Color waterColorNorth = new Color(0x093363);
	private static Color waterColorSouth = new Color(0x4BE2ED);
	private static int waterLimitNorth = -40000;
	private static int waterLimitSouth = 150000;
	
	public LOTRBiomeGenOcean(int i)
	{
		super(i);
		
		allOceanBiomes.add(this);
		
		spawnableEvilList.clear();
		
		decorator.treesPerChunk = 1;
		decorator.flowersPerChunk = 2;
		decorator.doubleFlowersPerChunk = 1;
		decorator.grassPerChunk = 4;
		decorator.doubleGrassPerChunk = 1;
		
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
	
	public static void updateWaterColor(int i, int j, int k)
	{
		int min = 0;
		int max = waterLimitSouth - waterLimitNorth;
		float latitude = (float)MathHelper.clamp_int(k - waterLimitNorth, min, max);
		latitude /= (float)max;
		
		float[] northColors = waterColorNorth.getColorComponents(null);
		float[] southColors = waterColorSouth.getColorComponents(null);
		
		float dR = southColors[0] - northColors[0];
		float dG = southColors[1] - northColors[1];
		float dB = southColors[2] - northColors[2];
		
		float r = dR * latitude;
		float g = dG * latitude;
		float b = dB * latitude;
		
		r += northColors[0];
		g += northColors[1];
		b += northColors[2];
		Color water = new Color(r, g, b);
		
		for (LOTRBiome biome : allOceanBiomes)
		{
			biome.biomeColors.setWater(water.getRGB());
		}
	}
	
	public static boolean isFrozen(int i, int k)
	{
		if (k > iceLimitSouth)
		{
			return false;
		}
		else
		{
			int l = iceLimitNorth - k;
			l *= -1;
			if (l < 1)
			{
				return true;
			}
			else
			{
				iceRand.setSeed((long)i * 341873128712L + (long)k * 132897987541L);
				l -= Math.abs(iceLimitNorth - iceLimitSouth) / 2;
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
