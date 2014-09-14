package lotr.common.world.biome;

import java.util.Random;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRFaction;
import lotr.common.LOTRWaypoint;
import lotr.common.entity.animal.LOTREntityMidges;
import lotr.common.world.LOTRBanditSpawner;
import lotr.common.world.LOTRInvasionSpawner;
import lotr.common.world.LOTRInvasionSpawner.BiomeInvasionListEntry;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenBigTree;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraft.world.gen.feature.WorldGenSwamp;
import net.minecraft.world.gen.feature.WorldGenTrees;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRBiomeGenMidgewater extends LOTRBiome
{
	public LOTRBiomeGenMidgewater(int i)
	{
		super(i);
		
		spawnableCreatureList.clear();
		spawnableWaterCreatureList.clear();
		
		spawnableEvilList.clear();
		
		spawnableLOTRAmbientList.add(new SpawnListEntry(LOTREntityMidges.class, 10, 4, 4));
		
		decorator.quagmirePerChunk = 1;
		decorator.treesPerChunk = 1;
		decorator.logsPerChunk = 3;
		decorator.flowersPerChunk = 0;
		decorator.grassPerChunk = 8;
		decorator.doubleGrassPerChunk = 4;
		decorator.enableFern = true;
		decorator.mushroomsPerChunk = 3;
		decorator.waterlilyPerChunk = 2;
		decorator.reedsPerChunk = 10;
		decorator.generateAthelas = true;
		
		registerSwampFlowers();
		
		biomeColors.setGrass(0x82824C);
		biomeColors.setFoliage(0x826A3B);
		biomeColors.setWater(0x564F3C);
		
		setBanditChance(LOTRBanditSpawner.UNCOMMON);
		
		invasionSpawns.add(new BiomeInvasionListEntry(LOTRFaction.GUNDABAD, LOTRInvasionSpawner.RARE));
		invasionSpawns.add(new BiomeInvasionListEntry(LOTRFaction.RANGER_NORTH, LOTRInvasionSpawner.RARE));
	}
	
	@Override
	public LOTRAchievement getBiomeAchievement()
	{
		return LOTRAchievement.enterMidgewater;
	}
	
	@Override
	public LOTRWaypoint.Region getBiomeWaypoints()
	{
		return LOTRWaypoint.Region.MIDGEWATER;
	}
	
	@Override
    public void decorate(World world, Random random, int i, int k)
    {
        super.decorate(world, random, i, k);
		
		for (int l = 0; l < 6; l++)
		{
            int i1 = i + random.nextInt(16) + 8;
            int k1 = k + random.nextInt(16) + 8;
			int j1 = world.getHeightValue(i1, k1);
			new WorldGenLakes(Blocks.water).generate(world, random, i1, j1, k1);
		}
	}
	
	@Override
    public WorldGenAbstractTree func_150567_a(Random random)
    {
		if (random.nextInt(4) == 0)
		{
			return new WorldGenBigTree(false);
		}
		else if (random.nextBoolean())
		{
			return new WorldGenSwamp();
		}
		else
		{
			return new WorldGenTrees(false, 4 + random.nextInt(6), 0, 0, false);
		}
    }
	
	@Override
	public float getTreeIncreaseChance()
	{
		return 0.25F;
	}
}
