package lotr.common.world.biome;

import java.util.Random;

import lotr.common.world.feature.LOTRWorldGenBoulder;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class LOTRBiomeGenNearHaradBoulderFields extends LOTRBiomeGenNearHarad
{
	private WorldGenerator extraBoulderGen = new LOTRWorldGenBoulder(Blocks.stone, 0, 1, 4).setSpawnBlock(Blocks.sand, 0);
	
	public LOTRBiomeGenNearHaradBoulderFields(int i)
	{
		super(i);
	}
	
	@Override
    public void decorate(World world, Random random, int i, int k)
    {
        super.decorate(world, random, i, k);
		
		for (int j = 0; j < 2; j++)
		{
			int boulders = 2 + random.nextInt(4);
			for (int l = 0; l < boulders; l++)
			{
				int i1 = i + random.nextInt(16) + 8;
				int k1 = k + random.nextInt(16) + 8;
				extraBoulderGen.generate(world, random, i1, world.getHeightValue(i1, k1), k1);
			}
		}
	}
}
