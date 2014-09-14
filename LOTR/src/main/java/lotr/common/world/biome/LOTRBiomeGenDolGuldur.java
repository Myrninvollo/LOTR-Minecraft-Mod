package lotr.common.world.biome;

import java.util.Random;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRMod;
import lotr.common.entity.npc.*;
import lotr.common.world.feature.LOTRWorldGenDeadTrees;
import lotr.common.world.feature.LOTRWorldGenMirkOak;
import lotr.common.world.structure.LOTRWorldGenStoneRuin;
import lotr.common.world.structure2.LOTRWorldGenDolGuldurAltar;
import lotr.common.world.structure2.LOTRWorldGenDolGuldurTower;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.*;

public class LOTRBiomeGenDolGuldur extends LOTRBiomeGenMirkwood
{
    private WorldGenerator morgulIronGen = new WorldGenMinable(LOTRMod.oreMorgulIron, 8);
	private WorldGenerator guldurilGen = new WorldGenMinable(LOTRMod.oreGulduril, 8);
	
	public LOTRBiomeGenDolGuldur(int i)
	{
		super(i, true);
		
		spawnableEvilList.clear();
		spawnableEvilList.add(new SpawnListEntry(LOTREntityMirkwoodSpider.class, 20, 4, 6));
		spawnableEvilList.add(new SpawnListEntry(LOTREntityDolGuldurOrc.class, 20, 4, 6));
		spawnableEvilList.add(new SpawnListEntry(LOTREntityDolGuldurOrcArcher.class, 10, 4, 6));
		spawnableEvilList.add(new SpawnListEntry(LOTREntityMirkTroll.class, 5, 1, 3));
		
		hasPodzol = false;
		decorator.treesPerChunk = 1;
		decorator.vinesPerChunk = 2;
		decorator.flowersPerChunk = 0;
		decorator.grassPerChunk = 6;
		decorator.doubleGrassPerChunk = 1;
		
		biomeColors.setGrass(0x2E4431);
		biomeColors.setSky(0x424751);
		biomeColors.setClouds(0x282C35);
		biomeColors.setFoggy(true);
		
		decorator.clearRandomStructures();
		decorator.addRandomStructure(new LOTRWorldGenStoneRuin.DOL_GULDUR(1, 4), 5);
		decorator.addRandomStructure(new LOTRWorldGenDolGuldurAltar(false), 200);
		decorator.addRandomStructure(new LOTRWorldGenDolGuldurTower(false), 100);
	}
	
	@Override
	public LOTRAchievement getBiomeAchievement()
	{
		return LOTRAchievement.enterDolGuldur;
	}
	
	@Override
    public void decorate(World world, Random random, int i, int k)
    {
        genStandardOre(world, random, i, k, 20, morgulIronGen, 0, 64);
		genStandardOre(world, random, i, k, 8, guldurilGen, 0, 32);
		
        super.decorate(world, random, i, k);
    }
	
	@Override
	public float getTreeIncreaseChance()
	{
		return 0.25F;
	}
	
	@Override
    public WorldGenAbstractTree func_150567_a(Random random)
    {
		if (random.nextInt(6) > 0)
		{
			return LOTRWorldGenDeadTrees.newMirkOak();
		}
		return new LOTRWorldGenMirkOak(false, 8, 4, 0, 3);
    }
	
	@Override
	public boolean canSpawnHostilesInDay()
	{
		return true;
	}
}
