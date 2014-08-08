package lotr.common.world.biome;

import java.util.Random;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRFaction;
import lotr.common.LOTRWaypoint;
import lotr.common.entity.animal.LOTREntityHorse;
import lotr.common.entity.npc.LOTREntityBlueDwarfMerchant;
import lotr.common.entity.npc.LOTREntityElvenTrader;
import lotr.common.entity.npc.LOTREntityRangerNorth;
import lotr.common.world.LOTRBanditSpawner;
import lotr.common.world.LOTRInvasionSpawner;
import lotr.common.world.LOTRInvasionSpawner.BiomeInvasionListEntry;
import lotr.common.world.feature.LOTRWorldGenBigTrees;
import lotr.common.world.feature.LOTRWorldGenSimpleTrees;
import lotr.common.world.structure.LOTRWorldGenGundabadCamp;
import lotr.common.world.structure.LOTRWorldGenRangerCamp;
import lotr.common.world.structure.LOTRWorldGenRuinedDunedainTower;
import lotr.common.world.structure.LOTRWorldGenStoneRuin;
import lotr.common.world.structure2.LOTRWorldGenRangerWatchtower;
import lotr.common.world.structure2.LOTRWorldGenRuinedHouse;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenForest;
import net.minecraft.world.gen.feature.WorldGenTaiga2;

public class LOTRBiomeGenEriador extends LOTRBiome
{
	public LOTRBiomeGenEriador(int i)
	{
		super(i);
		
		spawnableCreatureList.add(new SpawnListEntry(LOTREntityHorse.class, 5, 2, 6));
		
		spawnableGoodList.add(new SpawnListEntry(LOTREntityRangerNorth.class, 10, 4, 4));
		
		setGoodEvilWeight(20, 80);
		
		decorator.grassPerChunk = 3;
		decorator.doubleGrassPerChunk = 4;
		decorator.generateAthelas = true;
		
        registerPlainsFlowers();
		
		decorator.generateOrcDungeon = true;
		
		decorator.addRandomStructure(new LOTRWorldGenGundabadCamp(), 1500);
		decorator.addRandomStructure(new LOTRWorldGenRuinedDunedainTower(false), 500);
		decorator.addRandomStructure(new LOTRWorldGenRuinedHouse(false), 1500);
		decorator.addRandomStructure(new LOTRWorldGenRangerCamp(), 1500);
		decorator.addRandomStructure(new LOTRWorldGenStoneRuin.STONE(1, 3), 800);
		decorator.addRandomStructure(new LOTRWorldGenStoneRuin.ARNOR(1, 3), 800);
		decorator.addRandomStructure(new LOTRWorldGenRangerWatchtower(false), 1500);
		
		registerTravellingTrader(LOTREntityElvenTrader.class);
		registerTravellingTrader(LOTREntityBlueDwarfMerchant.class);
		
		setBanditChance(LOTRBanditSpawner.UNCOMMON);
		
		invasionSpawns.add(new BiomeInvasionListEntry(LOTRFaction.GUNDABAD, LOTRInvasionSpawner.UNCOMMON));
		invasionSpawns.add(new BiomeInvasionListEntry(LOTRFaction.RANGER_NORTH, LOTRInvasionSpawner.UNCOMMON));
		invasionSpawns.add(new BiomeInvasionListEntry(LOTRFaction.ANGMAR, LOTRInvasionSpawner.UNCOMMON));
	}
	
	@Override
	public LOTRAchievement getBiomeAchievement()
	{
		return LOTRAchievement.enterEriador;
	}
	
	@Override
	public LOTRWaypoint.Region getBiomeWaypoints()
	{
		return LOTRWaypoint.Region.ERIADOR;
	}
	
	@Override
    public WorldGenAbstractTree func_150567_a(Random random)
    {
		if (random.nextInt(300) == 0)
		{
			return random.nextBoolean() ? LOTRWorldGenSimpleTrees.newApple(false) : LOTRWorldGenSimpleTrees.newPear(false);
		}
		else if (random.nextInt(10) == 0)
		{
			return new WorldGenForest(false, false);
		}
		else if (random.nextInt(5) == 0)
		{
			return new WorldGenTaiga2(false);
		}
		else if (random.nextInt(20) == 0)
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
		return 0.1F;
	}
	
	@Override
	public float getTreeIncreaseChance()
	{
		return 0.05F;
	}
	
	@Override
	public float getChanceToSpawnLakes()
	{
		return 0.2F;
	}
	
	@Override
	public int spawnCountMultiplier()
	{
		return 5;
	}
}
