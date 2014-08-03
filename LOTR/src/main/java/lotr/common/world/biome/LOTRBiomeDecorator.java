package lotr.common.world.biome;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import lotr.common.LOTRMod;
import lotr.common.world.feature.LOTRWorldGenBiomeFlowers;
import lotr.common.world.feature.LOTRWorldGenCaveCobwebs;
import lotr.common.world.feature.LOTRWorldGenLogs;
import lotr.common.world.feature.LOTRWorldGenMordorLava;
import lotr.common.world.feature.LOTRWorldGenStalactites;
import lotr.common.world.feature.LOTRWorldGenTrollHoard;
import lotr.common.world.structure.LOTRWorldGenOrcDungeon;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenCactus;
import net.minecraft.world.gen.feature.WorldGenClay;
import net.minecraft.world.gen.feature.WorldGenDeadBush;
import net.minecraft.world.gen.feature.WorldGenFlowers;
import net.minecraft.world.gen.feature.WorldGenLiquids;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenPumpkin;
import net.minecraft.world.gen.feature.WorldGenReed;
import net.minecraft.world.gen.feature.WorldGenSand;
import net.minecraft.world.gen.feature.WorldGenVines;
import net.minecraft.world.gen.feature.WorldGenWaterlily;
import net.minecraft.world.gen.feature.WorldGenerator;

public class LOTRBiomeDecorator
{
    private World worldObj;
    private Random rand;
    private int chunkX;
    private int chunkZ;
    private LOTRBiome biome;

    private WorldGenerator clayGen = new WorldGenClay(4);
    private WorldGenerator sandGen = new WorldGenSand(Blocks.sand, 7);
    private WorldGenerator quagmireGen = new WorldGenSand(LOTRMod.quagmire, 7);
	
    private WorldGenerator dirtGen = new WorldGenMinable(Blocks.dirt, 32);
    private WorldGenerator gravelGen = new WorldGenMinable(Blocks.gravel, 32);
    private WorldGenerator coalGen = new WorldGenMinable(Blocks.coal_ore, 16);
    private WorldGenerator ironGen = new WorldGenMinable(Blocks.iron_ore, 8);
    private WorldGenerator goldGen = new WorldGenMinable(Blocks.gold_ore, 8);
    private WorldGenerator diamondGen = new WorldGenMinable(Blocks.diamond_ore, 7);
	private WorldGenerator lapisGen = new WorldGenMinable(Blocks.lapis_ore, 6);
	private WorldGenerator copperGen = new WorldGenMinable(LOTRMod.oreCopper, 8);
	private WorldGenerator tinGen = new WorldGenMinable(LOTRMod.oreTin, 8);
	private WorldGenerator silverGen = new WorldGenMinable(LOTRMod.oreSilver, 7);
	private WorldGenerator oreGlowstoneGen = new WorldGenMinable(LOTRMod.oreGlowstone, 4);
	private WorldGenerator sulfurGen = new WorldGenMinable(LOTRMod.oreSulfur, 8);
	private WorldGenerator saltpeterGen = new WorldGenMinable(LOTRMod.oreSaltpeter, 8);
	
	private WorldGenerator flowerGen = new LOTRWorldGenBiomeFlowers();
	private WorldGenerator logGen = new LOTRWorldGenLogs();
    private WorldGenerator mushroomBrownGen = new WorldGenFlowers(Blocks.brown_mushroom);
    private WorldGenerator mushroomRedGen = new WorldGenFlowers(Blocks.red_mushroom);
    private WorldGenerator reedGen = new WorldGenReed();
	private WorldGenerator pumpkinGen = new WorldGenPumpkin();
    private WorldGenerator waterlilyGen = new WorldGenWaterlily();
	private WorldGenerator cobwebGen = new LOTRWorldGenCaveCobwebs();
	private WorldGenerator stalactiteGen = new LOTRWorldGenStalactites();
	private WorldGenerator vinesGen = new WorldGenVines();
	private WorldGenerator cactusGen = new WorldGenCactus();
	
    public int sandPerChunk = 1;
    public int sandPerChunk2 = 3;
    public int clayPerChunk = 1;
    public int quagmirePerChunk = 0;
    public int treesPerChunk = 0;
    public int logsPerChunk = 0;
	public int vinesPerChunk = 0;
    public int flowersPerChunk = 2;
    public int doubleFlowersPerChunk = 0;
    public int grassPerChunk = 1;
    public int doubleGrassPerChunk = 0;
	public boolean enableFern = false;
    public int deadBushPerChunk = 0;
	public int waterlilyPerChunk = 0;
    public int mushroomsPerChunk = 0;
    public int reedsPerChunk = 0;
    public int cactiPerChunk = 0;
    public boolean generateWater = true;
	public boolean generateLava = true;
	public boolean generateCobwebs = true;
	public boolean generateAthelas = false;
	
	private WorldGenerator orcDungeonGen = new LOTRWorldGenOrcDungeon(false);
	private WorldGenerator trollHoardGen = new LOTRWorldGenTrollHoard();
	public boolean generateOrcDungeon = false;
	public boolean generateTrollHoard = false;
	
	private List randomStructures = new ArrayList();

    public LOTRBiomeDecorator(LOTRBiome lotrbiome)
    {
        biome = lotrbiome;
    }
	
	public void addRandomStructure(WorldGenerator structure, int chunkChance)
	{
		randomStructures.add(new RandomStructure(structure, chunkChance));
	}
	
	public void clearRandomStructures()
	{
		randomStructures.clear();
	}

    public void decorate(World world, Random random, int i, int j)
    {
		worldObj = world;
		rand = random;
		chunkX = i;
		chunkZ = j;
		decorate();
    }

    private void decorate()
    {
        generateOres();
		
		if (rand.nextBoolean() && generateCobwebs)
		{
            int i = chunkX + rand.nextInt(16) + 8;
            int j = rand.nextInt(60);
            int k = chunkZ + rand.nextInt(16) + 8;
            cobwebGen.generate(worldObj, rand, i, j, k);
		}
		
		for (int l = 0; l < 3; l++)
		{
            int i = chunkX + rand.nextInt(16) + 8;
            int j = rand.nextInt(60);
            int k = chunkZ + rand.nextInt(16) + 8;
            stalactiteGen.generate(worldObj, rand, i, j, k);
		}
		
        for (int l = 0; l < quagmirePerChunk; l++)
        {
            int i = chunkX + rand.nextInt(16) + 8;
            int k = chunkZ + rand.nextInt(16) + 8;
            quagmireGen.generate(worldObj, rand, i, worldObj.getTopSolidOrLiquidBlock(i, k), k);
        }

        for (int l = 0; l < sandPerChunk2; l++)
        {
            int i = chunkX + rand.nextInt(16) + 8;
            int k = chunkZ + rand.nextInt(16) + 8;
            sandGen.generate(worldObj, rand, i, worldObj.getTopSolidOrLiquidBlock(i, k), k);
        }

        for (int l = 0; l < clayPerChunk; l++)
        {
            int i = chunkX + rand.nextInt(16) + 8;
            int k = chunkZ + rand.nextInt(16) + 8;
            clayGen.generate(worldObj, rand, i, worldObj.getTopSolidOrLiquidBlock(i, k), k);
        }

        for (int l = 0; l < sandPerChunk; l++)
        {
            int i = chunkX + rand.nextInt(16) + 8;
            int k = chunkZ + rand.nextInt(16) + 8;
            sandGen.generate(worldObj, rand, i, worldObj.getTopSolidOrLiquidBlock(i, k), k);
        }
		
		if (Math.abs(chunkX) > 4 && Math.abs(chunkZ) > 4)
		{
			for (Object obj : randomStructures)
			{
				RandomStructure randomstructure = (RandomStructure)obj;
				if (rand.nextInt(randomstructure.chunkChance) == 0)
				{
					int i = chunkX + rand.nextInt(16) + 8;
					int k = chunkZ + rand.nextInt(16) + 8;
					int j = worldObj.getTopSolidOrLiquidBlock(i, k);
					randomstructure.structure.generate(worldObj, rand, i, j, k);
				}
			}
		}

        int trees = treesPerChunk;

        if (rand.nextFloat() < biome.getTreeIncreaseChance())
        {
            trees++;
        }

        for (int l = 0; l < trees; l++)
        {
            int i = chunkX + rand.nextInt(16) + 8;
            int k = chunkZ + rand.nextInt(16) + 8;
            WorldGenerator treeGen = biome.func_150567_a(rand);
            treeGen.generate(worldObj, rand, i, worldObj.getHeightValue(i, k), k);
        }
        
        for (int l = 0; l < logsPerChunk; l++)
        {
            int i = chunkX + rand.nextInt(16) + 8;
            int k = chunkZ + rand.nextInt(16) + 8;
            logGen.generate(worldObj, rand, i, worldObj.getHeightValue(i, k), k);
        }
		
        for (int l = 0; l < vinesPerChunk; l++)
        {
            int i = chunkX + rand.nextInt(16) + 8;
			int j = 64;
            int k = chunkZ + rand.nextInt(16) + 8;
            vinesGen.generate(worldObj, rand, i, j, k);
        }

        for (int l = 0; l < flowersPerChunk; l++)
        {
            int i = chunkX + rand.nextInt(16) + 8;
            int j = rand.nextInt(128);
            int k = chunkZ + rand.nextInt(16) + 8;
            flowerGen.generate(worldObj, rand, i, j, k);
        }
        
        for (int l = 0; l < doubleFlowersPerChunk; l++)
        {
            int i = chunkX + rand.nextInt(16) + 8;
            int j = rand.nextInt(128);
            int k = chunkZ + rand.nextInt(16) + 8;
            WorldGenerator doubleFlowerGen = biome.getRandomWorldGenForDoubleFlower(rand);
            doubleFlowerGen.generate(worldObj, rand, i, j, k);
        }

        for (int l = 0; l < grassPerChunk; l++)
        {
            int i = chunkX + rand.nextInt(16) + 8;
            int j = rand.nextInt(128);
            int k = chunkZ + rand.nextInt(16) + 8;
            WorldGenerator grassGen = biome.getRandomWorldGenForGrass(rand);
            grassGen.generate(worldObj, rand, i, j, k);
        }
        
        for (int l = 0; l < doubleGrassPerChunk; l++)
        {
            int i = chunkX + rand.nextInt(16) + 8;
            int j = rand.nextInt(128);
            int k = chunkZ + rand.nextInt(16) + 8;
            WorldGenerator grassGen = biome.getRandomWorldGenForDoubleGrass(rand);
            grassGen.generate(worldObj, rand, i, j, k);
        }

        for (int l = 0; l < deadBushPerChunk; l++)
        {
            int i = chunkX + rand.nextInt(16) + 8;
            int j = rand.nextInt(128);
            int k = chunkZ + rand.nextInt(16) + 8;
            (new WorldGenDeadBush(Blocks.deadbush)).generate(worldObj, rand, i, j, k);
        }

        for (int l = 0; l < waterlilyPerChunk; l++)
        {
            int i = chunkX + rand.nextInt(16) + 8;
            int k = chunkZ + rand.nextInt(16) + 8;
			int j;
			
            for (j = rand.nextInt(128); j > 0 && worldObj.getBlock(i, j - 1, k) == Blocks.air; j--)
            {
                ;
            }

            waterlilyGen.generate(worldObj, rand, i, j, k);
        }

        for (int l = 0; l < mushroomsPerChunk; l++)
        {
            if (rand.nextInt(4) == 0)
            {
                int i = chunkX + rand.nextInt(16) + 8;
                int k = chunkZ + rand.nextInt(16) + 8;
                int j = worldObj.getHeightValue(i, k);
                mushroomBrownGen.generate(worldObj, rand, i, j, k);
            }

            if (rand.nextInt(8) == 0)
            {
                int i = chunkX + rand.nextInt(16) + 8;
                int k = chunkZ + rand.nextInt(16) + 8;
                int j = worldObj.getHeightValue(i, k);
                mushroomRedGen.generate(worldObj, rand, i, j, k);
            }
        }

        if (rand.nextInt(4) == 0)
        {
            int i = chunkX + rand.nextInt(16) + 8;
            int j = rand.nextInt(128);
            int k = chunkZ + rand.nextInt(16) + 8;
            mushroomBrownGen.generate(worldObj, rand, i, j, k);
        }

        if (rand.nextInt(8) == 0)
        {
            int i = chunkX + rand.nextInt(16) + 8;
            int j = rand.nextInt(128);
            int k = chunkZ + rand.nextInt(16) + 8;
            mushroomRedGen.generate(worldObj, rand, i, j, k);
        }

        for (int l = 0; l < reedsPerChunk; l++)
        {
            int i = chunkX + rand.nextInt(16) + 8;
			int j = rand.nextInt(128);
            int k = chunkZ + rand.nextInt(16) + 8;
            reedGen.generate(worldObj, rand, i, j, k);
        }

        for (int l = 0; l < 10; l++)
        {
            int i = chunkX + rand.nextInt(16) + 8;
			int j = rand.nextInt(128);
            int k = chunkZ + rand.nextInt(16) + 8;
            reedGen.generate(worldObj, rand, i, j, k);
        }
		
        for (int l = 0; l < cactiPerChunk; l++)
        {
            int i = chunkX + rand.nextInt(16) + 8;
			int j = rand.nextInt(128);
            int k = chunkZ + rand.nextInt(16) + 8;
            cactusGen.generate(worldObj, rand, i, j, k);
        }
		
        if (flowersPerChunk > 0 && rand.nextInt(32) == 0)
        {
            int i = chunkX + rand.nextInt(16) + 8;
			int j = rand.nextInt(128);
            int k = chunkZ + rand.nextInt(16) + 8;
            pumpkinGen.generate(worldObj, rand, i, j, k);
        }
		
		if (generateAthelas && rand.nextInt(40) == 0)
		{
            int i = chunkX + rand.nextInt(16) + 8;
			int j = rand.nextInt(128);
            int k = chunkZ + rand.nextInt(16) + 8;
            new WorldGenFlowers(LOTRMod.athelas).generate(worldObj, rand, i, j, k);
		}

        if (generateWater)
        {
			WorldGenerator waterGen = new WorldGenLiquids(Blocks.flowing_water);
            for (int l = 0; l < 50; l++)
            {
                int i = chunkX + rand.nextInt(16) + 8;
                int j = rand.nextInt(rand.nextInt(120) + 8);
                int k = chunkZ + rand.nextInt(16) + 8;
                waterGen.generate(worldObj, rand, i, j, k);
            }
		}
		
		if (generateLava)
		{
			WorldGenerator lavaGen;
			if (biome instanceof LOTRBiomeGenMordor)
			{
				lavaGen = new LOTRWorldGenMordorLava();
				for (int l = 0; l < 50; l++)
				{
					int i = chunkX + rand.nextInt(16) + 8;
					int j = rand.nextInt(rand.nextInt(rand.nextInt(112) + 8) + 8);
					int k = chunkZ + rand.nextInt(16) + 8;
					lavaGen.generate(worldObj, rand, i, j, k);
				}
			}
			else
			{
				lavaGen = new WorldGenLiquids(Blocks.flowing_lava);
				for (int l = 0; l < 20; l++)
				{
					int i = chunkX + rand.nextInt(16) + 8;
					int j = rand.nextInt(rand.nextInt(rand.nextInt(112) + 8) + 8);
					int k = chunkZ + rand.nextInt(16) + 8;
					lavaGen.generate(worldObj, rand, i, j, k);
				}
			}
        }
		
		if (generateOrcDungeon)
		{
			for (int l = 0; l < 6; l++)
			{
                int i = chunkX + rand.nextInt(16) + 8;
                int j = rand.nextInt(128);
                int k = chunkZ + rand.nextInt(16) + 8;
				orcDungeonGen.generate(worldObj, rand, i, j, k);
			}
		}
		
		if (generateTrollHoard)
		{
			if (rand.nextInt(3) == 0)
			{
                int i = chunkX + rand.nextInt(16) + 8;
                int j = 40 + rand.nextInt(20);
                int k = chunkZ + rand.nextInt(16) + 8;
				trollHoardGen.generate(worldObj, rand, i, j, k);
			}
		}
    }

    public void genStandardOre(int ores, WorldGenerator oreGen, int offset, int heightRange)
    {
        for (int l = 0; l < ores; l++)
        {
            int i = chunkX + rand.nextInt(16);
            int j = rand.nextInt(heightRange - offset) + offset;
            int k = chunkZ + rand.nextInt(16);
            oreGen.generate(worldObj, rand, i, j, k);
        }
    }

    public void genErraticOre(int ores, WorldGenerator oreGen, int offset, int heightRange)
    {
        for (int l = 0; l < ores; l++)
        {
            int i = chunkX + rand.nextInt(16);
            int j = rand.nextInt(heightRange) + rand.nextInt(heightRange) + (offset - heightRange);
            int k = chunkZ + rand.nextInt(16);
            oreGen.generate(worldObj, rand, i, j, k);
        }
    }

    private void generateOres()
    {
    	int passes = 1;
    	
    	if (biome instanceof LOTRBiomeGenRedMountains)
    	{
    		passes = 2;
    	}
    	
    	for (int l = 0; l < passes; l++)
	    {
	        genStandardOre(20, dirtGen, 0, 128);
	        genStandardOre(10, gravelGen, 0, 128);
			
	        genStandardOre(20, coalGen, 0, 128);
	        genStandardOre(20, ironGen, 0, 64);
	        genStandardOre(2, goldGen, 0, 32);
	        genStandardOre(1, diamondGen, 0, 16);
			genErraticOre(1, lapisGen, 16, 16);
			
			genStandardOre(8, copperGen, 0, 64);
			genStandardOre(8, tinGen, 0, 64);
			genStandardOre(3, silverGen, 0, 32);
			
			genStandardOre(3, sulfurGen, 0, 64);
			genStandardOre(3, saltpeterGen, 0, 64);
		
			if (biome.rootHeight > 1F)
			{
				genStandardOre(20, dirtGen, 128, 256);
				genStandardOre(10, gravelGen, 128, 256);
				
				genStandardOre(10, coalGen, 128, 256);
				genStandardOre(10, ironGen, 64, 128);
				
				genStandardOre(4, copperGen, 64, 128);
				genStandardOre(4, tinGen, 64, 128);
			}
	    }
		
		if (biome instanceof LOTRBiomeGenIronHills || biome instanceof LOTRBiomeGenBlueMountains)
		{
			genStandardOre(8, oreGlowstoneGen, 0, 48);
		}
    }
	
	private class RandomStructure
	{
		public WorldGenerator structure;
		public int chunkChance;
		
		public RandomStructure(WorldGenerator w, int i)
		{
			structure = w;
			chunkChance = i;
		}
	}
}
