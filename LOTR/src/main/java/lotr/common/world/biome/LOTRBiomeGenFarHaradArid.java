package lotr.common.world.biome;

import java.util.Random;

import lotr.common.LOTRMod;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.*;

public class LOTRBiomeGenFarHaradArid extends LOTRBiomeGenFarHarad
{
	private WorldGenerator sandPatch = new WorldGenMinable(Blocks.sand, 0, 50, Blocks.grass);
	private WorldGenerator dirtPatch = new WorldGenMinable(Blocks.dirt, 1, 50, Blocks.grass);
	
	public LOTRBiomeGenFarHaradArid(int i)
	{
		super(i);
		
		decorator.flowersPerChunk = 1;
		decorator.doubleFlowersPerChunk = 1;
		
		spawnableLOTRAmbientList.clear();
		
		biomeColors.setGrass(0xD6BF5C);
	}

	@Override
    public void decorate(World world, Random random, int i, int k)
    {
		genStandardOre(world, random, i, k, 9, sandPatch, 60, 150);
		genStandardOre(world, random, i, k, 9, dirtPatch, 60, 150);
		
        super.decorate(world, random, i, k);
	}
	
	@Override
    public WorldGenerator getRandomWorldGenForGrass(Random random)
    {
        return random.nextBoolean() ? new WorldGenTallGrass(LOTRMod.aridGrass, 0) : super.getRandomWorldGenForGrass(random);
    }
	
	@Override
	public float getChanceToSpawnAnimals()
	{
		return super.getChanceToSpawnAnimals() * 0.5F;
	}
}
