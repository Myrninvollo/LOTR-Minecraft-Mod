package lotr.common.world.biome;

import java.util.Random;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRMod;
import lotr.common.LOTRWaypoint;
import lotr.common.entity.animal.LOTREntityBird;
import lotr.common.entity.animal.LOTREntityButterfly;
import lotr.common.entity.npc.LOTREntityElf;
import lotr.common.entity.npc.LOTREntityElfWarrior;
import lotr.common.world.feature.LOTRWorldGenMallornLarge;
import lotr.common.world.feature.LOTRWorldGenSimpleTrees;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.world.gen.feature.WorldGenerator;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRBiomeGenLothlorien extends LOTRBiome
{
	private WorldGenerator quenditeGen = new WorldGenMinable(LOTRMod.oreQuendite, 6);

	public LOTRBiomeGenLothlorien(int i)
	{
		super(i);
		
		spawnableCreatureList.clear();
		spawnableWaterCreatureList.clear();
		spawnableCaveCreatureList.clear();
		
		spawnableGoodList.add(new SpawnListEntry(LOTREntityElf.class, 10, 4, 4));
		spawnableGoodList.add(new SpawnListEntry(LOTREntityElfWarrior.class, 1, 4, 8));
		
		spawnableEvilList.clear();
		
		setGoodEvilWeight(100, 0);
		
		spawnableLOTRAmbientList.add(new SpawnListEntry(LOTREntityButterfly.class, 10, 4, 4));
		spawnableLOTRAmbientList.add(new SpawnListEntry(LOTREntityBird.class, 5, 4, 4));
		
		decorator.treesPerChunk = 10;
		decorator.flowersPerChunk = 6;
		decorator.grassPerChunk = 4;
		decorator.doubleGrassPerChunk = 2;
		decorator.generateLava = false;
		decorator.generateCobwebs = false;
		
		flowers.clear();
		addFlower(LOTRMod.elanor, 0, 20);
		addFlower(LOTRMod.niphredil, 0, 10);
	}
	
	@Override
	public LOTRAchievement getBiomeAchievement()
	{
		return LOTRAchievement.enterLothlorien;
	}
	
	@Override
	public LOTRWaypoint.Region getBiomeWaypoints()
	{
		return LOTRWaypoint.Region.LOTHLORIEN;
	}
	
	@Override
    public void decorate(World world, Random random, int i, int k)
    {
		genStandardOre(world, random, i, k, 6, quenditeGen, 0, 48);
		
		super.decorate(world, random, i, k);
		
		for (int l = 0; l < 100; l++)
		{
			int i1 = i + random.nextInt(16) + 8;
			int j1 = 60 + random.nextInt(50);
			int k1 = k + random.nextInt(16) + 8;
			if (!world.isAirBlock(i1, j1 - 1, k1) || !world.isAirBlock(i1, j1, k1) || !world.isAirBlock(i1, j1 + 1, k1))
			{
				continue;
			}
			
			if (world.getBlock(i1 - 1, j1, k1) == LOTRMod.wood && world.getBlockMetadata(i1 - 1, j1, k1) == 1)
			{
				if (world.isAirBlock(i1 - 1, j1, k1 - 1) && world.isAirBlock(i1 - 1, j1, k1 + 1))
				{
					world.setBlock(i1, j1, k1, LOTRMod.mallornTorch, 1, 2);
					continue;
				}
			}
			if (world.getBlock(i1 + 1, j1, k1) == LOTRMod.wood && world.getBlockMetadata(i1 + 1, j1, k1) == 1)
			{
				if (world.isAirBlock(i1 + 1, j1, k1 - 1) && world.isAirBlock(i1 + 1, j1, k1 + 1))
				{
					world.setBlock(i1, j1, k1, LOTRMod.mallornTorch, 2, 2);
					continue;
				}
			}
			if (world.getBlock(i1, j1, k1 - 1) == LOTRMod.wood && world.getBlockMetadata(i1, j1, k1 - 1) == 1)
			{
				if (world.isAirBlock(i1 - 1, j1, k1 - 1) && world.isAirBlock(i1 + 1, j1, k1 - 1))
				{
					world.setBlock(i1, j1, k1, LOTRMod.mallornTorch, 3, 2);
					continue;
				}
			}
			if (world.getBlock(i1, j1, k1 + 1) == LOTRMod.wood && world.getBlockMetadata(i1, j1, k1 + 1) == 1)
			{
				if (world.isAirBlock(i1 - 1, j1, k1 + 1) && world.isAirBlock(i1 + 1, j1, k1 + 1))
				{
					world.setBlock(i1, j1, k1, LOTRMod.mallornTorch, 4, 2);
					continue;
				}
			}
		}
    }
	
	@Override
    public WorldGenAbstractTree func_150567_a(Random random)
    {
		if (random.nextInt(3) == 0)
		{
			return new WorldGenTrees(false);
		}
		return random.nextInt(35) == 0 ? new LOTRWorldGenMallornLarge(false) : LOTRWorldGenSimpleTrees.newMallorn(false);
    }
	
	@Override
	public float getChanceToSpawnLavaLakes()
	{
		return 0F;
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public int getBiomeGrassColor(int i, int j, int k)
    {
		return 0xAFE51B;
    }
	
	@Override
	public Vec3 getFogColor(Vec3 fog)
	{
		fog.xCoord *= 1D;
		fog.yCoord *= 0.9D;
		fog.zCoord *= 0.4D;
		return fog;
	}
}
