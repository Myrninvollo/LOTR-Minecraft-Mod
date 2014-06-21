package lotr.common.world.biome;

import java.util.Random;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRMod;
import lotr.common.LOTRWaypoint;
import lotr.common.entity.animal.LOTREntityBird;
import lotr.common.entity.animal.LOTREntityButterfly;
import lotr.common.entity.animal.LOTREntityHorse;
import lotr.common.entity.animal.LOTREntityRabbit;
import lotr.common.entity.npc.LOTREntityElvenTrader;
import lotr.common.entity.npc.LOTREntityMordorOrc;
import lotr.common.entity.npc.LOTREntityMordorOrcArcher;
import lotr.common.entity.npc.LOTREntityMordorOrcBombardier;
import lotr.common.entity.npc.LOTREntityRangerIthilien;
import lotr.common.world.feature.LOTRWorldGenSimpleTrees;
import lotr.common.world.structure.LOTRWorldGenGondorObelisk;
import lotr.common.world.structure.LOTRWorldGenGondorRuins;
import lotr.common.world.structure.LOTRWorldGenRuinedBeaconTower;
import lotr.common.world.structure.LOTRWorldGenRuinedGondorTower;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenBigTree;
import net.minecraft.world.gen.feature.WorldGenDoublePlant;
import net.minecraft.world.gen.feature.WorldGenForest;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.world.gen.feature.WorldGenerator;

public class LOTRBiomeGenIthilien extends LOTRBiome
{
	private WorldGenerator gondorRockVein = new WorldGenMinable(LOTRMod.rock, 1, 60, Blocks.stone);
	
	public LOTRBiomeGenIthilien(int i)
	{
		super(i);
		
		spawnableCreatureList.add(new SpawnListEntry(LOTREntityHorse.class, 5, 2, 6));
		
		spawnableGoodList.add(new SpawnListEntry(LOTREntityRangerIthilien.class, 10, 4, 4));
		
		spawnableEvilList.clear();
		spawnableEvilList.add(new SpawnListEntry(LOTREntityMordorOrc.class, 20, 4, 6));
		spawnableEvilList.add(new SpawnListEntry(LOTREntityMordorOrcArcher.class, 7, 4, 6));
		spawnableEvilList.add(new SpawnListEntry(LOTREntityMordorOrcBombardier.class, 3, 1, 2));
		
		setGoodEvilWeight(5, 95);
		
		spawnableLOTRAmbientList.add(new SpawnListEntry(LOTREntityButterfly.class, 10, 4, 4));
		spawnableLOTRAmbientList.add(new SpawnListEntry(LOTREntityRabbit.class, 6, 4, 4));
		spawnableLOTRAmbientList.add(new SpawnListEntry(LOTREntityBird.class, 10, 4, 4));
		
		decorator.treesPerChunk = 6;
		decorator.logsPerChunk = 1;
		decorator.flowersPerChunk = 4;
		decorator.doubleFlowersPerChunk = 4;
        decorator.grassPerChunk = 6;
		decorator.doubleGrassPerChunk = 4;
		decorator.waterlilyPerChunk = 2;
		decorator.generateAthelas = true;
		
		registerForestFlowers();
		addFlower(LOTRMod.asphodel, 0, 10);
		addFlower(Blocks.red_flower, 2, 5);
		
		decorator.generateOrcDungeon = true;
		
		decorator.addRandomStructure(new LOTRWorldGenRuinedBeaconTower(false), 200);
		decorator.addRandomStructure(new LOTRWorldGenGondorRuins(), 500);
		decorator.addRandomStructure(new LOTRWorldGenRuinedGondorTower(false), 500);
		decorator.addRandomStructure(new LOTRWorldGenGondorObelisk(false), 800);
		
		registerTravellingTrader(LOTREntityElvenTrader.class);
	}
	
	@Override
	public LOTRAchievement getBiomeAchievement()
	{
		return LOTRAchievement.enterIthilien;
	}
	
	@Override
	public LOTRWaypoint.Region getBiomeWaypoints()
	{
		return LOTRWaypoint.Region.GONDOR;
	}
	
	@Override
    public void decorate(World world, Random random, int i, int k)
    {
		genStandardOre(world, random, i, k, 2, gondorRockVein, 0, 64);
		
		super.decorate(world, random, i, k);
		
		if (random.nextInt(3) == 0)
        {
            int i1 = i + random.nextInt(16) + 8;
            int j1 = random.nextInt(128);
            int k1 = k + random.nextInt(16) + 8;
            WorldGenDoublePlant doubleFlowerGen = new WorldGenDoublePlant();
            doubleFlowerGen.func_150548_a(0);
            doubleFlowerGen.generate(world, random, i1, j1, k1);
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
		else if (random.nextInt(5) == 0)
		{
			return random.nextInt(8) == 0 ? LOTRWorldGenSimpleTrees.newLebethronLarge(false) : LOTRWorldGenSimpleTrees.newLebethron(false);
		}
		else
		{
			return random.nextInt(8) == 0 ? new WorldGenBigTree(false) : new WorldGenTrees(false, 4 + random.nextInt(4), 0, 0, false);
		}
    }
	
	@Override
	public float getChanceToSpawnAnimals()
	{
		return 0.5F;
	}
	
	@Override
	public float getChanceToSpawnLakes()
	{
		return 0.5F;
	}
	
	@Override
	public float getChanceToSpawnLavaLakes()
	{
		return 0.05F;
	}
}
