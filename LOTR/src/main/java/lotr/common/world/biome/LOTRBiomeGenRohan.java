package lotr.common.world.biome;

import java.util.Random;

import lotr.common.*;
import lotr.common.entity.animal.LOTREntityHorse;
import lotr.common.entity.npc.*;
import lotr.common.world.*;
import lotr.common.world.LOTRInvasionSpawner.BiomeInvasionListEntry;
import lotr.common.world.feature.*;
import lotr.common.world.structure.*;
import lotr.common.world.structure2.LOTRWorldGenMeadHall;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.*;

public class LOTRBiomeGenRohan extends LOTRBiome
{
	private WorldGenerator boulderGen = new LOTRWorldGenBoulder(LOTRMod.rock, 2, 1, 4);
	private WorldGenerator deadMoundGen = new LOTRWorldGenBoulder(Blocks.soul_sand, 0, 1, 3);
	private WorldGenerator rohanRockVein = new WorldGenMinable(LOTRMod.rock, 2, 60, Blocks.stone);
	private boolean corrupted;
	
	public LOTRBiomeGenRohan(int i, boolean flag)
	{
		super(i);
		corrupted = flag;
		
		spawnableEvilList.clear();
		spawnableEvilList.add(new SpawnListEntry(LOTREntityUrukHai.class, 20, 4, 6));
		spawnableEvilList.add(new SpawnListEntry(LOTREntityUrukHaiCrossbower.class, 10, 4, 6));
		spawnableEvilList.add(new SpawnListEntry(LOTREntityUrukHaiBerserker.class, 5, 1, 2));
		spawnableEvilList.add(new SpawnListEntry(LOTREntityUrukWarg.class, 5, 4, 4));
		
		if (corrupted)
		{
			spawnableCreatureList.clear();
			
			setGoodEvilWeight(0, 100);
			
			decorator.addRandomStructure(new LOTRWorldGenUrukCamp(), 120);
			decorator.addRandomStructure(new LOTRWorldGenUrukWargPit(false), 300);
			decorator.addRandomStructure(new LOTRWorldGenRuinedRohanWatchtower(false), 300);
			decorator.addRandomStructure(new LOTRWorldGenBlastedLand(), 24);
			
			setBanditChance(LOTRBanditSpawner.UNCOMMON);
			
			invasionSpawns.add(new BiomeInvasionListEntry(LOTRFaction.ROHAN, LOTRInvasionSpawner.COMMON));
		}
		else
		{
			spawnableCreatureList.add(new SpawnListEntry(LOTREntityHorse.class, 50, 2, 6));
			
			spawnableGoodList.add(new SpawnListEntry(LOTREntityRohirrim.class, 20, 4, 4));
			spawnableGoodList.add(new SpawnListEntry(LOTREntityRohirrimArcher.class, 10, 4, 4));
			
			setGoodEvilWeight(70, 30);
			
			decorator.addRandomStructure(new LOTRWorldGenRohanWatchtower(false), 300);
			decorator.addRandomStructure(new LOTRWorldGenMeadHall(false), 600);
			decorator.addRandomStructure(new LOTRWorldGenRohanFortress(false), 800);
			
			registerTravellingTrader(LOTREntityElvenTrader.class);
			registerTravellingTrader(LOTREntityBlueDwarfMerchant.class);
			
			setBanditChance(LOTRBanditSpawner.RARE);
			
			invasionSpawns.add(new BiomeInvasionListEntry(LOTRFaction.DUNLAND, LOTRInvasionSpawner.UNCOMMON));
			invasionSpawns.add(new BiomeInvasionListEntry(LOTRFaction.URUK_HAI, LOTRInvasionSpawner.UNCOMMON));
		}
		
		decorator.flowersPerChunk = 4;
        decorator.grassPerChunk = 6;
		decorator.doubleGrassPerChunk = 5;
		
        registerPlainsFlowers();
		
		decorator.addRandomStructure(new LOTRWorldGenRohanBarrow(false), 500);
	}
	
	@Override
	public LOTRAchievement getBiomeAchievement()
	{
		if (corrupted)
		{
			return LOTRAchievement.enterRohanUrukHighlands;
		}
		else
		{
			return LOTRAchievement.enterRohan;
		}
	}
	
	@Override
	public LOTRWaypoint.Region getBiomeWaypoints()
	{
		return LOTRWaypoint.Region.ROHAN;
	}
	
	@Override
	public boolean getEnableRiver()
	{
		return !corrupted;
	}

	@Override
    public void decorate(World world, Random random, int i, int k)
    {
		genStandardOre(world, random, i, k, 2, rohanRockVein, 0, 64);
		
		if (corrupted)
		{
			if (random.nextInt(30) == 0)
			{
				WorldGenerator treeGen = random.nextInt(3) == 0 ? LOTRWorldGenDeadTrees.newOak() : new LOTRWorldGenCharredTrees();
				int trees = 3 + random.nextInt(5);
				for (int l = 0; l < trees; l++)
				{
					int i1 = i + random.nextInt(16) + 8;
					int k1 = k + random.nextInt(16) + 8;
					treeGen.generate(world, random, i1, world.getHeightValue(i1, k1), k1);
				}
			}
		}
		
        super.decorate(world, random, i, k);
		
		if (random.nextInt(16) == 0)
		{
			for (int l = 0; l < 3; l++)
			{
				int i1 = i + random.nextInt(16) + 8;
				int k1 = k + random.nextInt(16) + 8;
				boulderGen.generate(world, random, i1, world.getHeightValue(i1, k1), k1);
			}
		}
		
		if (corrupted)
		{
			if (random.nextInt(32) == 0)
			{
				for (int l = 0; l < 3; l++)
				{
					int i1 = i + random.nextInt(16) + 8;
					int k1 = k + random.nextInt(16) + 8;
					deadMoundGen.generate(world, random, i1, world.getHeightValue(i1, k1), k1);
					for (int l1 = 0; l1 < 4; l1++)
					{
						int i2 = i1 - 4 + random.nextInt(9);
						int k2 = k1 - 4 + random.nextInt(9);
						int j2 = world.getHeightValue(i2, k2);
						if (LOTRMod.isOpaque(world, i2, j2 - 1, k2) && (world.isAirBlock(i2, j2, k2) || world.getBlock(i2, j2, k2) == Blocks.tallgrass))
						{
							world.setBlock(i2, j2, k2, Blocks.skull, 1, 2);
							TileEntity tileentity = world.getTileEntity(i2, j2, k2);
							if (tileentity != null && tileentity instanceof TileEntitySkull)
							{
								TileEntitySkull skull = (TileEntitySkull)tileentity;
								skull.func_145903_a(random.nextInt(16));
							}
						}
					}
				}
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
        return random.nextInt(3) > 0 ? new WorldGenBigTree(false) : new WorldGenTrees(false);
    }
	
	@Override
	public float getChanceToSpawnAnimals()
	{
		return corrupted ? 0F : 0.5F;
	}
	
	@Override
	public float getChanceToSpawnLakes()
	{
		return 0.1F;
	}
	
	@Override
	public float getChanceToSpawnLavaLakes()
	{
		return 0.05F;
	}
	
	@Override
	public float getTreeIncreaseChance()
	{
		return 0.05F;
	}
	
	@Override
	public boolean canSpawnHostilesInDay()
	{
		return corrupted;
	}
	
	@Override
	public int spawnCountMultiplier()
	{
		return corrupted ? 2 : super.spawnCountMultiplier();
	}
}
