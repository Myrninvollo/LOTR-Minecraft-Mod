package lotr.common.world.biome;

import java.util.Random;

import lotr.common.world.feature.LOTRWorldGenBoulder;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class LOTRBiomeGenEriadorDowns extends LOTRBiomeGenEriador
{
	private WorldGenerator boulderGen = new LOTRWorldGenBoulder(Blocks.stone, 0, 1, 4);
	
	public LOTRBiomeGenEriadorDowns(int i)
	{
		super(i);

		decorator.grassPerChunk = 2;
		decorator.doubleGrassPerChunk = 1;
	}
	
	@Override
    public void decorate(World world, Random random, int i, int k)
    {
        super.decorate(world, random, i, k);
		
		if (random.nextInt(24) == 0)
		{
			for (int l = 0; l < 3; l++)
			{
				int i1 = i + random.nextInt(16) + 8;
				int k1 = k + random.nextInt(16) + 8;
				boulderGen.generate(world, random, i1, world.getHeightValue(i1, k1), k1);
			}
		}
	}
	
	@Override
	public float getTreeIncreaseChance()
	{
		return 0.25F;
	}
}
