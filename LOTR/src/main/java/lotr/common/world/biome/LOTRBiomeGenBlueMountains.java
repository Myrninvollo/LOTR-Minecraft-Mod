package lotr.common.world.biome;

import java.util.Random;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRFaction;
import lotr.common.LOTRMod;
import lotr.common.LOTRWaypoint;
import lotr.common.entity.npc.LOTREntityBlueDwarf;
import lotr.common.entity.npc.LOTREntityBlueDwarfAxeThrower;
import lotr.common.entity.npc.LOTREntityBlueDwarfMiner;
import lotr.common.entity.npc.LOTREntityBlueDwarfWarrior;
import lotr.common.entity.npc.LOTREntityElvenTrader;
import lotr.common.world.LOTRBanditSpawner;
import lotr.common.world.LOTRInvasionSpawner;
import lotr.common.world.LOTRInvasionSpawner.BiomeInvasionListEntry;
import lotr.common.world.structure.LOTRWorldGenBlueMountainsHouse;
import lotr.common.world.structure.LOTRWorldGenBlueMountainsStronghold;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenForest;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenTaiga2;
import net.minecraft.world.gen.feature.WorldGenerator;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRBiomeGenBlueMountains extends LOTRBiome
{
	private WorldGenerator blueRockVein = new WorldGenMinable(LOTRMod.rock, 3, 60, Blocks.stone);
	private WorldGenerator additionalIronGen = new WorldGenMinable(Blocks.iron_ore, 4);
	private WorldGenerator additionalCoalGen = new WorldGenMinable(Blocks.coal_ore, 8);
	
	public LOTRBiomeGenBlueMountains(int i)
	{
		super(i);

		spawnableGoodList.add(new SpawnListEntry(LOTREntityBlueDwarf.class, 100, 4, 4));
		spawnableGoodList.add(new SpawnListEntry(LOTREntityBlueDwarfMiner.class, 15, 1, 3));
		spawnableGoodList.add(new SpawnListEntry(LOTREntityBlueDwarfWarrior.class, 20, 4, 4));
		spawnableGoodList.add(new SpawnListEntry(LOTREntityBlueDwarfAxeThrower.class, 10, 4, 4));
		
		spawnableEvilList.clear();
		
		setGoodEvilWeight(100, 0);
		
		decorator.treesPerChunk = 1;
		decorator.flowersPerChunk = 1;
		decorator.grassPerChunk = 6;
		decorator.doubleGrassPerChunk = 1;
		decorator.generateWater = false;
		decorator.generateLava = false;
		decorator.generateCobwebs = false;
		
		registerMountainsFlowers();
		addFlower(LOTRMod.dwarfHerb, 0, 1);
		
		biomeColors.setSky(0x7289F9);
		
		decorator.addRandomStructure(new LOTRWorldGenBlueMountainsStronghold(false), 500);
		
		registerTravellingTrader(LOTREntityElvenTrader.class);
		
		setBanditChance(LOTRBanditSpawner.RARE);
		
		invasionSpawns.add(new BiomeInvasionListEntry(LOTRFaction.GUNDABAD, LOTRInvasionSpawner.UNCOMMON));
	}
	
	@Override
	public LOTRAchievement getBiomeAchievement()
	{
		return LOTRAchievement.enterBlueMountains;
	}
	
	@Override
	public LOTRWaypoint.Region getBiomeWaypoints()
	{
		return LOTRWaypoint.Region.BLUE_MOUNTAINS;
	}
	
	@Override
	public boolean getEnableRiver()
	{
		return false;
	}

	@Override
    public void decorate(World world, Random random, int i, int k)
    {
		genStandardOre(world, random, i, k, 6, blueRockVein, 0, 96);
		genStandardOre(world, random, i, k, 10, additionalCoalGen, 0, 128);
		genStandardOre(world, random, i, k, 10, additionalIronGen, 0, 96);

        super.decorate(world, random, i, k);

		for (int l = 0; l < 4; l++)
		{
			int i1 = i + random.nextInt(16) + 8;
			int j1 = 70 + random.nextInt(80);
			int k1 = k + random.nextInt(16) + 8;
			new LOTRWorldGenBlueMountainsHouse(false).generate(world, random, i1, j1, k1);
		}
    }
	
	@Override
    public WorldGenAbstractTree func_150567_a(Random random)
    {
		if (random.nextInt(4) == 0)
		{
			return new WorldGenForest(false, false);
		}
		else if (random.nextInt(3) == 0)
		{
			return new WorldGenTaiga2(false);
		}
		else
		{
			return super.func_150567_a(random);
		}
    }
	
	@Override
	public float getChanceToSpawnAnimals()
	{
		return 0.2F;
	}
}
