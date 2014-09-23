package lotr.common.world.biome;

import java.util.Random;

import lotr.common.*;
import lotr.common.entity.animal.LOTREntityHorse;
import lotr.common.entity.npc.*;
import lotr.common.world.*;
import lotr.common.world.LOTRInvasionSpawner.BiomeInvasionListEntry;
import lotr.common.world.feature.*;
import lotr.common.world.structure.LOTRWorldGenGundabadCamp;
import lotr.common.world.structure.LOTRWorldGenStoneRuin;
import lotr.common.world.structure2.LOTRWorldGenRuinedHouse;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.*;

public class LOTRBiomeGenValesOfAnduin extends LOTRBiome
{
	private WorldGenerator boulderGen = new LOTRWorldGenBoulder(Blocks.stone, 0, 2, 4);
	
	public LOTRBiomeGenValesOfAnduin(int i)
	{
		super(i);
		
		spawnableCreatureList.add(new SpawnListEntry(LOTREntityHorse.class, 5, 2, 6));

		spawnableEvilList.add(new SpawnListEntry(LOTREntityGundabadWarg.class, 6, 4, 4));
		
		decorator.treesPerChunk = 0;
		decorator.setTreeCluster(8, 10);
		decorator.flowersPerChunk = 3;
		decorator.doubleFlowersPerChunk = 1;
		decorator.grassPerChunk = 8;
		decorator.doubleGrassPerChunk = 2;
		
        registerPlainsFlowers();
		
		decorator.generateOrcDungeon = true;
		
		decorator.addRandomStructure(new LOTRWorldGenGundabadCamp(), 1500);
		decorator.addRandomStructure(new LOTRWorldGenRuinedHouse(false), 1500);
		decorator.addRandomStructure(new LOTRWorldGenStoneRuin.STONE(1, 3), 400);
		decorator.addRandomStructure(new LOTRWorldGenStoneRuin.ELVEN(1, 4), 1500);
		
		registerTravellingTrader(LOTREntityElvenTrader.class);
		registerTravellingTrader(LOTREntityBlueDwarfMerchant.class);
		registerTravellingTrader(LOTREntityNearHaradMerchant.class);
		
		setBanditChance(LOTRBanditSpawner.UNCOMMON);
		
		invasionSpawns.add(new BiomeInvasionListEntry(LOTRFaction.GUNDABAD, LOTRInvasionSpawner.UNCOMMON));
		invasionSpawns.add(new BiomeInvasionListEntry(LOTRFaction.DOL_GULDUR, LOTRInvasionSpawner.RARE));
	}
	
	@Override
	public LOTRAchievement getBiomeAchievement()
	{
		return LOTRAchievement.enterValesOfAnduin;
	}
	
	@Override
	public LOTRWaypoint.Region getBiomeWaypoints()
	{
		return LOTRWaypoint.Region.VALES_OF_ANDUIN;
	}
	
	@Override
    public void decorate(World world, Random random, int i, int k)
    {
        super.decorate(world, random, i, k);
		
		if (random.nextInt(24) == 0)
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
		if (random.nextInt(300) == 0)
		{
			return random.nextBoolean() ? LOTRWorldGenSimpleTrees.newApple(false) : LOTRWorldGenSimpleTrees.newPear(false);
		}
		else if (random.nextInt(6) == 0)
		{
			return new LOTRWorldGenLarch(false);
		}
		else if (random.nextInt(6) == 0)
		{
			return new WorldGenForest(false, false);
		}
		else if (random.nextBoolean())
		{
			return new WorldGenTaiga2(false);
		}
		else if (random.nextInt(10) == 0)
		{
			return random.nextInt(10) == 0 ? LOTRWorldGenBigTrees.newChestnut(false) : LOTRWorldGenSimpleTrees.newChestnut(false);
		}
		else
		{
			return super.func_150567_a(random);
		}
	}
	
	@Override
	public float getTreeIncreaseChance()
	{
		return 0.25F;
	}
	
	@Override
	public float getChanceToSpawnAnimals()
	{
		return 0.1F;
	}
	
	@Override
	public int spawnCountMultiplier()
	{
		return 3;
	}
}
