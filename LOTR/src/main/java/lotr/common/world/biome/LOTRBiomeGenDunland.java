package lotr.common.world.biome;

import java.util.Random;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRFaction;
import lotr.common.LOTRWaypoint;
import lotr.common.entity.animal.LOTREntityCrebain;
import lotr.common.entity.animal.LOTREntityRabbit;
import lotr.common.entity.npc.LOTREntityDunlending;
import lotr.common.entity.npc.LOTREntityDunlendingArcher;
import lotr.common.entity.npc.LOTREntityDunlendingWarrior;
import lotr.common.world.LOTRBanditSpawner;
import lotr.common.world.LOTRInvasionSpawner;
import lotr.common.world.LOTRInvasionSpawner.BiomeInvasionListEntry;
import lotr.common.world.feature.LOTRWorldGenBoulder;
import lotr.common.world.structure.LOTRWorldGenDunlandHillFort;
import lotr.common.world.structure.LOTRWorldGenDunlendingCampfire;
import lotr.common.world.structure.LOTRWorldGenDunlendingHouse;
import lotr.common.world.structure.LOTRWorldGenDunlendingTavern;
import lotr.common.world.structure.LOTRWorldGenStoneRuin;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenBigTree;
import net.minecraft.world.gen.feature.WorldGenTaiga2;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.world.gen.feature.WorldGenerator;

public class LOTRBiomeGenDunland extends LOTRBiome
{
	private WorldGenerator boulderGen = new LOTRWorldGenBoulder(Blocks.stone, 0, 1, 3);
	
	public LOTRBiomeGenDunland(int i)
	{
		super(i);
		
		spawnableCreatureList.add(new SpawnListEntry(EntityWolf.class, 8, 4, 8));
		
		spawnableEvilList.clear();
		spawnableEvilList.add(new SpawnListEntry(LOTREntityDunlending.class, 15, 4, 6));
		spawnableEvilList.add(new SpawnListEntry(LOTREntityDunlendingWarrior.class, 2, 4, 6));
		spawnableEvilList.add(new SpawnListEntry(LOTREntityDunlendingArcher.class, 1, 4, 6));
		
		spawnableLOTRAmbientList.add(new SpawnListEntry(LOTREntityRabbit.class, 6, 4, 4));
		spawnableLOTRAmbientList.add(new SpawnListEntry(LOTREntityCrebain.class, 10, 4, 4));
		
		decorator.treesPerChunk = 0;
        decorator.grassPerChunk = 6;
		decorator.doubleGrassPerChunk = 1;
		
		registerForestFlowers();
		
		decorator.addRandomStructure(new LOTRWorldGenDunlendingHouse(false), 25);
		decorator.addRandomStructure(new LOTRWorldGenDunlendingTavern(false), 80);
		decorator.addRandomStructure(new LOTRWorldGenDunlendingCampfire(false), 100);
		decorator.addRandomStructure(new LOTRWorldGenDunlandHillFort(false), 600);
		decorator.addRandomStructure(new LOTRWorldGenStoneRuin.STONE(1, 3), 500);
		
		setBanditChance(LOTRBanditSpawner.COMMON);
		
		invasionSpawns.add(new BiomeInvasionListEntry(LOTRFaction.ROHAN, LOTRInvasionSpawner.UNCOMMON));
		invasionSpawns.add(new BiomeInvasionListEntry(LOTRFaction.GUNDABAD, LOTRInvasionSpawner.RARE));
	}
	
	@Override
	public LOTRAchievement getBiomeAchievement()
	{
		return LOTRAchievement.enterDunland;
	}
	
	@Override
	public LOTRWaypoint.Region getBiomeWaypoints()
	{
		return LOTRWaypoint.Region.DUNLAND;
	}
	
	@Override
    public void decorate(World world, Random random, int i, int k)
    {
        super.decorate(world, random, i, k);

		for (int count = 0; count < 6; count++)
		{
            int i1 = i + random.nextInt(16) + 8;
            int k1 = k + random.nextInt(16) + 8;
			int j1 = world.getHeightValue(i1, k1);
			if (j1 > 85)
			{
				WorldGenerator tree = func_150567_a(random);
				tree.generate(world, random, i1, j1, k1);
			}
		}
		
		if (random.nextInt(8) == 0)
		{
			for (int l = 0; l < 4; l++)
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
		if (random.nextInt(3) == 0)
		{
			return random.nextInt(10) == 0 ? new WorldGenBigTree(false) : new WorldGenTrees(false, 4 + random.nextInt(5), 0, 0, false);
		}
		else
		{
			return new WorldGenTaiga2(false);
		}
    }
	
	@Override
	public float getTreeIncreaseChance()
	{
		return 0.75F;
	}
	
	@Override
	public float getChanceToSpawnAnimals()
	{
		return 0.5F;
	}
	
	@Override
	public int spawnCountMultiplier()
	{
		return 2;
	}
}
