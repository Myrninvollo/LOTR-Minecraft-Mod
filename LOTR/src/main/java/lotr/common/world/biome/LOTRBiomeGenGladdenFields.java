package lotr.common.world.biome;

import java.util.Random;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRFaction;
import lotr.common.entity.animal.LOTREntityRabbit;
import lotr.common.world.LOTRBanditSpawner;
import lotr.common.world.LOTRInvasionSpawner;
import lotr.common.world.LOTRInvasionSpawner.BiomeInvasionListEntry;
import lotr.common.world.feature.LOTRWorldGenDoubleFlower;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraft.world.gen.feature.WorldGenSwamp;
import net.minecraft.world.gen.feature.WorldGenerator;

public class LOTRBiomeGenGladdenFields extends LOTRBiomeGenAnduin
{
	public LOTRBiomeGenGladdenFields(int i)
	{
		super(i);
		
		spawnableLOTRAmbientList.add(new SpawnListEntry(LOTREntityRabbit.class, 6, 4, 4));

		decorator.quagmirePerChunk = 1;
		decorator.treesPerChunk = 2;
		decorator.flowersPerChunk = 2;
		decorator.doubleFlowersPerChunk = 10;
		decorator.grassPerChunk = 8;
		decorator.doubleGrassPerChunk = 5;
		decorator.waterlilyPerChunk = 1;
		decorator.reedsPerChunk = 10;
		
		registerSwampFlowers();
		
		setBanditChance(LOTRBanditSpawner.UNCOMMON);
		
		invasionSpawns.clear();
		invasionSpawns.add(new BiomeInvasionListEntry(LOTRFaction.GUNDABAD, LOTRInvasionSpawner.UNCOMMON));
	}
	
	@Override
	public LOTRAchievement getBiomeAchievement()
	{
		return LOTRAchievement.enterGladdenFields;
	}
	
	@Override
    public void decorate(World world, Random random, int i, int k)
    {
        super.decorate(world, random, i, k);
		
		for (int l = 0; l < 6; l++)
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
		if (random.nextInt(3) == 0)
		{
			return new WorldGenSwamp();
		}
		return super.func_150567_a(random);
    }
	
	@Override
	public WorldGenerator getRandomWorldGenForDoubleFlower(Random random)
	{
		LOTRWorldGenDoubleFlower doubleFlowerGen = new LOTRWorldGenDoubleFlower();
        doubleFlowerGen.setFlowerType(1);
        return doubleFlowerGen;
	}
	
	@Override
	public int spawnCountMultiplier()
	{
		return 2;
	}
}
