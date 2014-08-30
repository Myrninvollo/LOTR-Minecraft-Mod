package lotr.common.world.biome;

import java.util.Random;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRWaypoint;
import lotr.common.world.feature.LOTRWorldGenCharredTrees;
import lotr.common.world.feature.LOTRWorldGenDeadTrees;
import lotr.common.world.feature.LOTRWorldGenDesertTrees;
import lotr.common.world.structure.LOTRWorldGenNurnWheatFarm;
import lotr.common.world.structure.LOTRWorldGenOrcSlaverTower;
import net.minecraft.init.Blocks;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRBiomeGenNurn extends LOTRBiomeGenMordor
{
	public LOTRBiomeGenNurn(int i)
	{
		super(i);
		
		topBlock = Blocks.grass;
		fillerBlock = Blocks.dirt;
		
		enableRain = true;

		decorator.flowersPerChunk = 0;
		decorator.grassPerChunk = 4;
		decorator.generateWater = true;
		
		decorator.clearRandomStructures();
		decorator.addRandomStructure(new LOTRWorldGenNurnWheatFarm(false), 40);
		decorator.addRandomStructure(new LOTRWorldGenOrcSlaverTower(false), 200);
		
		biomeColors.setSky(0x564637);
		biomeColors.resetClouds();
		biomeColors.resetFog();
	}
	
	@Override
	public LOTRAchievement getBiomeAchievement()
	{
		return LOTRAchievement.enterNurn;
	}
	
	@Override
	public LOTRWaypoint.Region getBiomeWaypoints()
	{
		return LOTRWaypoint.Region.NURN;
	}
	
	@Override
	public boolean isGorgoroth()
	{
		return false;
	}
	
	@Override
    public WorldGenAbstractTree func_150567_a(Random random)
    {
		if (random.nextBoolean())
		{
			return new LOTRWorldGenDesertTrees();
		}
		else
		{
			return random.nextBoolean() ? new LOTRWorldGenCharredTrees() : LOTRWorldGenDeadTrees.newOak();
		}
    }
	
	@Override
	public float getTreeIncreaseChance()
	{
		return 0.2F;
	}
	
	@Override
	public float getChanceToSpawnLakes()
	{
		return 0.5F;
	}
	
	@Override
	public float getChanceToSpawnLavaLakes()
	{
		return 0.2F;
	}

	@Override
	public int spawnCountMultiplier()
	{
		return 3;
	}
}
