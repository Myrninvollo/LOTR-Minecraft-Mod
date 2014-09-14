package lotr.common.world.biome;

import java.util.Random;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRFaction;
import lotr.common.LOTRMod;
import lotr.common.LOTRWaypoint;
import lotr.common.entity.animal.LOTREntityWildBoar;
import lotr.common.entity.npc.LOTREntityBlueDwarfMerchant;
import lotr.common.entity.npc.LOTREntityDwarf;
import lotr.common.entity.npc.LOTREntityDwarfAxeThrower;
import lotr.common.entity.npc.LOTREntityDwarfMiner;
import lotr.common.entity.npc.LOTREntityDwarfWarrior;
import lotr.common.world.LOTRBanditSpawner;
import lotr.common.world.LOTRInvasionSpawner;
import lotr.common.world.LOTRInvasionSpawner.BiomeInvasionListEntry;
import lotr.common.world.structure.LOTRWorldGenDwarfHouse;
import lotr.common.world.structure.LOTRWorldGenDwarvenTower;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenMegaPineTree;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenTaiga2;
import net.minecraft.world.gen.feature.WorldGenerator;

public class LOTRBiomeGenIronHills extends LOTRBiome
{
	private WorldGenerator additionalIronGen = new WorldGenMinable(Blocks.iron_ore, 4);
	private WorldGenerator additionalGoldGen = new WorldGenMinable(Blocks.gold_ore, 4);
	private WorldGenerator additionalSilverGen = new WorldGenMinable(LOTRMod.oreSilver, 4);
	
	public LOTRBiomeGenIronHills(int i)
	{
		super(i);
		
		spawnableCreatureList.add(new SpawnListEntry(LOTREntityWildBoar.class, 50, 4, 4));
		
		spawnableGoodList.add(new SpawnListEntry(LOTREntityDwarf.class, 100, 4, 4));
		spawnableGoodList.add(new SpawnListEntry(LOTREntityDwarfMiner.class, 15, 1, 3));
		spawnableGoodList.add(new SpawnListEntry(LOTREntityDwarfWarrior.class, 20, 4, 4));
		spawnableGoodList.add(new SpawnListEntry(LOTREntityDwarfAxeThrower.class, 10, 4, 4));
		
		setGoodEvilWeight(60, 40);
		
		decorator.treesPerChunk = 0;
		decorator.flowersPerChunk = 1;
        decorator.grassPerChunk = 8;
		decorator.doubleGrassPerChunk = 1;
		decorator.generateWater = false;
		decorator.generateLava = false;
		decorator.generateCobwebs = false;
		
		registerMountainsFlowers();
		addFlower(LOTRMod.dwarfHerb, 0, 1);
		
		decorator.addRandomStructure(new LOTRWorldGenDwarvenTower(false), 300);
		
		registerTravellingTrader(LOTREntityBlueDwarfMerchant.class);
		
		setBanditChance(LOTRBanditSpawner.RARE);
		
		invasionSpawns.add(new BiomeInvasionListEntry(LOTRFaction.GUNDABAD, LOTRInvasionSpawner.UNCOMMON));
	}
	
	@Override
	public LOTRAchievement getBiomeAchievement()
	{
		return LOTRAchievement.enterIronHills;
	}
	
	@Override
	public LOTRWaypoint.Region getBiomeWaypoints()
	{
		return LOTRWaypoint.Region.IRON_HILLS;
	}
	
	@Override
    public void decorate(World world, Random random, int i, int k)
    {
		genStandardOre(world, random, i, k, 10, additionalIronGen, 0, 96);
		genStandardOre(world, random, i, k, 1, additionalGoldGen, 0, 48);
		genStandardOre(world, random, i, k, 1, additionalSilverGen, 0, 48);

        super.decorate(world, random, i, k);
		
		for (int l = 0; l < 4; l++)
		{
			int i1 = i + random.nextInt(16) + 8;
			int j1 = 70 + random.nextInt(60);
			int k1 = k + random.nextInt(16) + 8;
			new LOTRWorldGenDwarfHouse(false).generate(world, random, i1, j1, k1);
		}
		
		for (int l = 0; l < 8; l++)
		{
            int i1 = i + random.nextInt(16) + 8;
            int k1 = k + random.nextInt(16) + 8;
			int j1 = world.getHeightValue(i1, k1);
			if (j1 > 80)
			{
				func_150567_a(random).generate(world, random, i1, j1, k1);
			}
		}
	}
	
	@Override
    public WorldGenAbstractTree func_150567_a(Random random)
    {
		if (random.nextInt(5) == 0)
		{
			if (random.nextInt(8) == 0)
			{
				return new WorldGenMegaPineTree(false, true);
			}
			return new WorldGenMegaPineTree(false, false);
		}
        return new WorldGenTaiga2(false);
    }
	
	@Override
	public float getChanceToSpawnAnimals()
	{
		return 0.25F;
	}
	
	@Override
	public float getTreeIncreaseChance()
	{
		return 0.1F;
	}
}
