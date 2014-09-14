package lotr.common.world.biome;

import java.util.Random;

import lotr.common.entity.animal.LOTREntityBird;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase.SpawnListEntry;
import net.minecraft.world.gen.feature.*;

public class LOTRBiomeGenFarHaradSwamp extends LOTRBiomeGenFarHarad
{
	public LOTRBiomeGenFarHaradSwamp(int i)
	{
		super(i);
		
		spawnableCreatureList.clear();
		spawnableWaterCreatureList.clear();
		spawnableLOTRAmbientList.clear();

		decorator.quagmirePerChunk = 1;
		decorator.treesPerChunk = 1;
		decorator.vinesPerChunk = 20;
		decorator.logsPerChunk = 3;
		decorator.flowersPerChunk = 0;
		decorator.grassPerChunk = 10;
		decorator.doubleGrassPerChunk = 6;
		decorator.enableFern = true;
		decorator.mushroomsPerChunk = 3;
		decorator.waterlilyPerChunk = 2;
		decorator.reedsPerChunk = 10;
		
		registerSwampFlowers();
		
		biomeColors.setWater(0x558E7E);
	}
	
	@Override
    public void decorate(World world, Random random, int i, int k)
    {
        super.decorate(world, random, i, k);
		
		for (int l = 0; l < 4; l++)
		{
            int i1 = i + random.nextInt(16) + 8;
            int k1 = k + random.nextInt(16) + 8;
			int j1 = world.getHeightValue(i1, k1);
			new WorldGenLakes(Blocks.water).generate(world, random, i1, j1, k1);
		}
	}
	
	@Override
    public WorldGenAbstractTree func_150567_a(Random random)
    {
		if (random.nextBoolean())
		{
			return new WorldGenSwamp();
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
		return super.getChanceToSpawnAnimals() * 0.25F;
	}
}
