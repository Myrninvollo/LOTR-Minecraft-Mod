package lotr.common.world.biome;

import java.util.Random;

import lotr.common.LOTRMod;
import lotr.common.world.feature.LOTRWorldGenMangrove;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.*;

public class LOTRBiomeGenFarHaradMangrove extends LOTRBiomeGenFarHarad
{
	private WorldGenerator dirtPatch = new WorldGenMinable(Blocks.dirt, 1, 60, Blocks.sand);
	
	public LOTRBiomeGenFarHaradMangrove(int i)
	{
		super(i);
		
		topBlock = Blocks.sand;
		
		spawnableLOTRAmbientList.clear();

		decorator.quagmirePerChunk = 1;
		decorator.treesPerChunk = 4;
		decorator.vinesPerChunk = 20;
		decorator.grassPerChunk = 8;
		decorator.enableFern = true;
		decorator.waterlilyPerChunk = 3;
		
		registerSwampFlowers();
		
		biomeColors.setGrass(0x9FB577);
		biomeColors.setFoliage(0x667746);
		biomeColors.setWater(0x5B533D);
	}
	
	@Override
	public boolean getEnableRiver()
	{
		return false;
	}
	
	@Override
    public void decorate(World world, Random random, int i, int k)
    {
		genStandardOre(world, random, i, k, 12, dirtPatch, 60, 90);
		
        super.decorate(world, random, i, k);
        
        for (int l = 0; l < 2; l++)
        {
            int i1 = i + random.nextInt(16);
            int k1 = k + random.nextInt(16);
            int j1 = world.getTopSolidOrLiquidBlock(i1, k1);
            if (world.getBlock(i1, j1 - 1, k1).isOpaqueCube() && world.getBlock(i1, j1, k1).getMaterial() == Material.water)
            {
            	func_150567_a(random).generate(world, random, i1, j1, k1);
            }
        }
        
        for (int l = 0; l < 20; l++)
        {
            int i1 = i + random.nextInt(16);
            int j1 = 50 + random.nextInt(15);
            int k1 = k + random.nextInt(16);
            
            if (world.getBlock(i1, j1, k1).isOpaqueCube() && world.getBlock(i1, j1 + 1, k1).getMaterial() == Material.water)
            {
            	int height = 2 + random.nextInt(3);
            	for (int j2 = j1; j2 <= j1 + height; j2++)
            	{
            		world.setBlock(i1, j2, k1, LOTRMod.wood3, 3, 2);
            	}
            }
        }
	}
	
	@Override
    public WorldGenAbstractTree func_150567_a(Random random)
    {
		return new LOTRWorldGenMangrove(false);
    }
	
	@Override
	public float getChanceToSpawnAnimals()
	{
		return super.getChanceToSpawnAnimals() * 0.5F;
	}
}
