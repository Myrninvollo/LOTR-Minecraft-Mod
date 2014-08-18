package lotr.common.world.biome;

import java.util.Random;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRWaypoint;
import lotr.common.entity.animal.LOTREntityCamel;
import lotr.common.entity.npc.LOTREntityNearHaradrim;
import lotr.common.entity.npc.LOTREntityNearHaradrimArcher;
import lotr.common.entity.npc.LOTREntityNearHaradrimWarrior;
import lotr.common.world.LOTRBanditSpawner;
import lotr.common.world.feature.LOTRWorldGenDatePalm;
import lotr.common.world.feature.LOTRWorldGenDesertTrees;
import lotr.common.world.feature.LOTRWorldGenDoubleFlower;
import lotr.common.world.structure.LOTRWorldGenHaradObelisk;
import lotr.common.world.structure2.LOTRWorldGenNearHaradFortress;
import lotr.common.world.structure2.LOTRWorldGenNearHaradVillage;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;

public class LOTRBiomeGenNearHaradFertile extends LOTRBiome
{
	private WorldGenerator dirtPatchGen = new WorldGenMinable(Blocks.dirt, 1, 50, Blocks.grass);
	private WorldGenerator sandPatchGen = new WorldGenMinable(Blocks.sand, 0, 50, Blocks.grass);
	private WorldGenerator redSandPatchGen = new WorldGenMinable(Blocks.sand, 1, 50, Blocks.grass);
	
	public LOTRBiomeGenNearHaradFertile(int i)
	{
		super(i);
		
		spawnableCreatureList.add(new SpawnListEntry(LOTREntityCamel.class, 6, 4, 4));
		
		spawnableEvilList.clear();
		spawnableEvilList.add(new SpawnListEntry(LOTREntityNearHaradrim.class, 20, 4, 4));
		spawnableEvilList.add(new SpawnListEntry(LOTREntityNearHaradrimWarrior.class, 3, 4, 4));
		spawnableEvilList.add(new SpawnListEntry(LOTREntityNearHaradrimArcher.class, 2, 4, 4));
		
		setGoodEvilWeight(0, 100);
		
		decorator.flowersPerChunk = 1;
		decorator.doubleFlowersPerChunk = 1;
		decorator.grassPerChunk = 4;
		decorator.doubleGrassPerChunk = 2;
		
		registerPlainsFlowers();
		
		decorator.addRandomStructure(new LOTRWorldGenHaradObelisk(false), 3000);
		decorator.addRandomStructure(new LOTRWorldGenNearHaradVillage(false), 40);
		decorator.addRandomStructure(new LOTRWorldGenNearHaradFortress(false), 200);
		
		setBanditChance(LOTRBanditSpawner.NEVER);
	}
	
	@Override
	public LOTRAchievement getBiomeAchievement()
	{
		return LOTRAchievement.enterNearHarad;
	}
	
	@Override
	public LOTRWaypoint.Region getBiomeWaypoints()
	{
		return LOTRWaypoint.Region.NEAR_HARAD_FERTILE;
	}
	
	@Override
    public void decorate(World world, Random random, int i, int k)
    {
		genStandardOre(world, random, i, k, 2, dirtPatchGen, 60, 90);
		genStandardOre(world, random, i, k, 3, sandPatchGen, 60, 90);
		
		if (random.nextInt(10) == 0)
		{
			genStandardOre(world, random, i, k, 2, redSandPatchGen, 60, 90);
		}
		
		super.decorate(world, random, i, k);
    }
	
	
	@Override
    public WorldGenAbstractTree func_150567_a(Random random)
    {
		if (random.nextInt(30) == 0)
		{
			return new LOTRWorldGenDatePalm(false);
		}
		return new LOTRWorldGenDesertTrees();
    }
	
	@Override
	public WorldGenerator getRandomWorldGenForDoubleFlower(Random random)
	{
		LOTRWorldGenDoubleFlower doubleFlowerGen = new LOTRWorldGenDoubleFlower();
		if (random.nextInt(5) == 0)
		{
			doubleFlowerGen.setFlowerType(3);
		}
		else
		{
			doubleFlowerGen.setFlowerType(2);
		}
        return doubleFlowerGen;
	}
	
	@Override
	public float getTreeIncreaseChance()
	{
		return 0.2F;
	}
	
	@Override
	public float getChanceToSpawnAnimals()
	{
		return 0.5F;
	}
	
	@Override
	public float getChanceToSpawnLakes()
	{
		return 0.1F;
	}
	
	@Override
	public int spawnCountMultiplier()
	{
		return 5;
	}
}
