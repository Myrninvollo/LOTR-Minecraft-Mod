package lotr.common.world.biome;

import java.util.Random;

import lotr.common.*;
import lotr.common.entity.animal.*;
import lotr.common.entity.npc.*;
import lotr.common.world.*;
import lotr.common.world.LOTRInvasionSpawner.BiomeInvasionListEntry;
import lotr.common.world.feature.LOTRWorldGenBoulder;
import lotr.common.world.feature.LOTRWorldGenHolly;
import lotr.common.world.structure.LOTRWorldGenRuinedElvenTurret;
import lotr.common.world.structure.LOTRWorldGenStoneRuin;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenerator;

public class LOTRBiomeGenEregion extends LOTRBiome
{
	private WorldGenerator boulderGen = new LOTRWorldGenBoulder(Blocks.stone, 0, 1, 3);
	
	public LOTRBiomeGenEregion(int i)
	{
		super(i);
		
		spawnableCreatureList.add(new SpawnListEntry(EntityWolf.class, 2, 4, 8));
		
		spawnableLOTRAmbientList.add(new SpawnListEntry(LOTREntityButterfly.class, 6, 4, 4));
		spawnableLOTRAmbientList.add(new SpawnListEntry(LOTREntityRabbit.class, 6, 4, 4));
		spawnableLOTRAmbientList.add(new SpawnListEntry(LOTREntityBird.class, 12, 4, 4));
		
		spawnableEvilList.add(new SpawnListEntry(LOTREntityGundabadWarg.class, 6, 4, 4));
		
		decorator.treesPerChunk = 1;
        decorator.flowersPerChunk = 3;
		decorator.doubleFlowersPerChunk = 1;
        decorator.grassPerChunk = 6;
		decorator.doubleGrassPerChunk = 3;
		
		registerForestFlowers();
		
		decorator.addRandomStructure(new LOTRWorldGenRuinedElvenTurret(false), 400);
		decorator.addRandomStructure(new LOTRWorldGenStoneRuin.ELVEN(1, 4), 50);
		
		registerTravellingTrader(LOTREntityElvenTrader.class);
		registerTravellingTrader(LOTREntityBlueDwarfMerchant.class);
		registerTravellingTrader(LOTREntityNearHaradMerchant.class);
		
		setBanditChance(LOTRBanditSpawner.RARE);
		
		invasionSpawns.add(new BiomeInvasionListEntry(LOTRFaction.RANGER_NORTH, LOTRInvasionSpawner.RARE));
		invasionSpawns.add(new BiomeInvasionListEntry(LOTRFaction.GUNDABAD, LOTRInvasionSpawner.RARE));
	}
	
	@Override
	public LOTRAchievement getBiomeAchievement()
	{
		return LOTRAchievement.enterEregion;
	}
	
	@Override
	public LOTRWaypoint.Region getBiomeWaypoints()
	{
		return LOTRWaypoint.Region.EREGION;
	}
	
	@Override
    public void decorate(World world, Random random, int i, int k)
    {
		super.decorate(world, random, i, k);
		
		if (random.nextInt(24) == 0)
		{
			int boulders = 1 + random.nextInt(4);
			for (int l = 0; l < boulders; l++)
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
		if (random.nextInt(4) != 0)
		{
			if (random.nextInt(20) == 0)
			{
				return new LOTRWorldGenHolly(false).setLarge();
			}
			return new LOTRWorldGenHolly(false);
		}
		return super.func_150567_a(random);
    }
	
	@Override
	public float getTreeIncreaseChance()
	{
		return 0.25F;
	}
	
	@Override
	public float getChanceToSpawnAnimals()
	{
		return 0.25F;
	}
	
	@Override
	public int spawnCountMultiplier()
	{
		return 2;
	}
}
