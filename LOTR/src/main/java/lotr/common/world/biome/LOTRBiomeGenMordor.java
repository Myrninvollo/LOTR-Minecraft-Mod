package lotr.common.world.biome;

import java.util.Random;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRFaction;
import lotr.common.LOTRMod;
import lotr.common.LOTRWaypoint;
import lotr.common.entity.npc.LOTREntityMordorOrc;
import lotr.common.entity.npc.LOTREntityMordorOrcArcher;
import lotr.common.entity.npc.LOTREntityMordorOrcBombardier;
import lotr.common.entity.npc.LOTREntityMordorWarg;
import lotr.common.entity.npc.LOTREntityOlogHai;
import lotr.common.world.LOTRBanditSpawner;
import lotr.common.world.LOTRInvasionSpawner;
import lotr.common.world.LOTRInvasionSpawner.BiomeInvasionListEntry;
import lotr.common.world.feature.LOTRWorldGenBoulder;
import lotr.common.world.feature.LOTRWorldGenCharredTrees;
import lotr.common.world.feature.LOTRWorldGenMordorMoss;
import lotr.common.world.structure.LOTRWorldGenMordorCamp;
import lotr.common.world.structure.LOTRWorldGenMordorTower;
import lotr.common.world.structure.LOTRWorldGenMordorWargPit;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRBiomeGenMordor extends LOTRBiome
{
    private WorldGenerator nauriteGen = new WorldGenMinable(LOTRMod.oreNaurite, 12, LOTRMod.rock);
    private WorldGenerator morgulIronGen = new WorldGenMinable(LOTRMod.oreMorgulIron, 1, 8, LOTRMod.rock);
	private WorldGenerator guldurilGen = new WorldGenMinable(LOTRMod.oreGulduril, 1, 8, LOTRMod.rock);
	private WorldGenerator boulderGen = new LOTRWorldGenBoulder(LOTRMod.rock, 0, 2, 8).setSpawnBlock(LOTRMod.rock, 0);
	
	public LOTRBiomeGenMordor(int i)
	{
		super(i);

		topBlock = LOTRMod.rock;
		fillerBlock = LOTRMod.rock;
		
		spawnableCreatureList.clear();
		spawnableWaterCreatureList.clear();
		
		spawnableEvilList.clear();
		spawnableEvilList.add(new SpawnListEntry(LOTREntityMordorOrc.class, 20, 4, 6));
		spawnableEvilList.add(new SpawnListEntry(LOTREntityMordorOrcArcher.class, 10, 4, 6));
		spawnableEvilList.add(new SpawnListEntry(LOTREntityMordorOrcBombardier.class, 5, 1, 2));
		
		if (isGorgoroth())
		{
			spawnableEvilList.add(new SpawnListEntry(LOTREntityMordorWarg.class, 15, 1, 3));
			spawnableEvilList.add(new SpawnListEntry(LOTREntityOlogHai.class, 15, 1, 3));
		}
		
		decorator.flowersPerChunk = 0;
		decorator.grassPerChunk = 0;
		decorator.generateWater = false;
		
		decorator.addRandomStructure(new LOTRWorldGenMordorCamp(), 60);
		decorator.addRandomStructure(new LOTRWorldGenMordorWargPit(false), 300);
		decorator.addRandomStructure(new LOTRWorldGenMordorTower(false), 400);

		biomeColors.setGrass(0x5B412B);
		biomeColors.setFoliage(0x634F2D);
		biomeColors.setSky(0x602F1F);
		biomeColors.setClouds(0x4B2319);
		biomeColors.setFog(0x322619);
		biomeColors.setWater(0x3F2007);
		
		setBanditChance(LOTRBanditSpawner.NEVER);
	}
	
	@Override
	public boolean hasSky()
	{
		return !isGorgoroth();
	}
	
	@Override
	public LOTRAchievement getBiomeAchievement()
	{
		return LOTRAchievement.enterMordor;
	}
	
	@Override
	public LOTRWaypoint.Region getBiomeWaypoints()
	{
		return LOTRWaypoint.Region.MORDOR;
	}
	
	public boolean isGorgoroth()
	{
		return true;
	}
	
	@Override
    public void decorate(World world, Random random, int i, int k)
    {
		genStandardOre(world, random, i, k, 20, nauriteGen, 0, 64);
        genStandardOre(world, random, i, k, 20, morgulIronGen, 0, 64);
		genStandardOre(world, random, i, k, 8, guldurilGen, 0, 32);
		
		super.decorate(world, random, i, k);
		
		if (isGorgoroth())
		{
			if (random.nextInt(24) == 0)
			{
				for (int l = 0; l < 6; l++)
				{
					int i1 = i + random.nextInt(16) + 8;
					int k1 = k + random.nextInt(16) + 8;
					boulderGen.generate(world, random, i1, world.getHeightValue(i1, k1), k1);
				}
			}
			
			if (random.nextInt(60) == 0)
			{
				for (int l = 0; l < 8; l++)
				{
					WorldGenerator trees = func_150567_a(random);
					int i1 = i + random.nextInt(16) + 8;
					int k1 = k + random.nextInt(16) + 8;
					trees.generate(world, random, i1, world.getHeightValue(i1, k1), k1);
				}
			}
			
			if (random.nextInt(20) == 0)
			{
				for (int l = 0; l < 6; l++)
				{
					int i1 = i + random.nextInt(6) + 8;
					int k1 = k + random.nextInt(6) + 8;
					int j1 = world.getHeightValue(i1, k1);
					if (world.getBlock(i1, j1 - 1, k1) == LOTRMod.rock && world.getBlockMetadata(i1, j1 - 1, k1) == 0 && world.isAirBlock(i1, j1, k1))
					{
						world.setBlock(i1, j1, k1, LOTRMod.mordorThorn, 0, 2);
					}
				}
			}
			
			if (random.nextInt(30) == 0)
			{
				int i1 = i + random.nextInt(16) + 8;
				int k1 = k + random.nextInt(16) + 8;
				int j1 = world.getHeightValue(i1, k1);
				if (world.getBlock(i1, j1 - 1, k1) == LOTRMod.rock && world.getBlockMetadata(i1, j1 - 1, k1) == 0 && world.isAirBlock(i1, j1, k1))
				{
					new LOTRWorldGenMordorMoss().generate(world, random, i1, j1, k1);
				}
			}
		}
    }

	@Override
    public WorldGenAbstractTree func_150567_a(Random random)
    {
		return new LOTRWorldGenCharredTrees();
    }
	
	@Override
	public float getTreeIncreaseChance()
	{
		return 0F;
	}
	
	@Override
	public float getChanceToSpawnAnimals()
	{
		return 0F;
	}
	
	@Override
	public boolean canSpawnHostilesInDay()
	{
		return true;
	}
	
	@Override
	public float getChanceToSpawnLakes()
	{
		return 0F;
	}
	
	@Override
	public float getChanceToSpawnLavaLakes()
	{
		return 1F;
	}
}
