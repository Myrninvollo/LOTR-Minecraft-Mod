package lotr.common.world.biome;

import java.util.Random;

import lotr.common.world.feature.LOTRWorldGenBoulder;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class LOTRBiomeGenAnduinVale extends LOTRBiomeGenAnduin
{
	private WorldGenerator valeBoulders = new LOTRWorldGenBoulder(Blocks.stone, 0, 1, 5);
	
	public LOTRBiomeGenAnduinVale(int i)
	{
		super(i);
		
		decorator.setTreeCluster(16, 8);
		decorator.flowersPerChunk = 6;
		decorator.doubleFlowersPerChunk = 2;
		decorator.grassPerChunk = 10;
		decorator.doubleGrassPerChunk = 4;
	}
	
	@Override
    public void decorate(World world, Random random, int i, int k)
    {
        super.decorate(world, random, i, k);
		
		if (random.nextInt(3) == 0)
		{
			for (int l = 0; l < 4; l++)
			{
				int i1 = i + random.nextInt(16) + 8;
				int k1 = k + random.nextInt(16) + 8;
				valeBoulders.generate(world, random, i1, world.getHeightValue(i1, k1), k1);
			}
		}
	}
	
	@Override
	public float getTreeIncreaseChance()
	{
		return 0.5F;
	}
	
	@Override
	public float getChanceToSpawnLakes()
	{
		return 1F;
	}
	
	@Override
	public float getChanceToSpawnAnimals()
	{
		return 0.5F;
	}
}
