package lotr.common.world.feature;

import java.util.Random;

import lotr.common.LOTRMod;
import lotr.common.world.biome.LOTRBiomeGenEttenmoors;
import lotr.common.world.structure.LOTRChestContents;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class LOTRWorldGenTrollHoard extends WorldGenerator
{
	@Override
	public boolean generate(World world, Random random, int i, int j, int k)
	{
		int chests = 1 + random.nextInt(4);
		int chestsGenerated = 0;
		chestLoop:
		for (int l = 0; l < 20; l++)
		{
			int i1 = i - 4 + random.nextInt(9);
			int j1 = j - 1 + random.nextInt(3);
			int k1 = k - 4 + random.nextInt(9);
			if (world.isAirBlock(i1, j1, k1) && world.getBlock(i1, j1 - 1, k1).getMaterial() == Material.rock && LOTRMod.isOpaque(world, i1, j1 - 1, k1))
			{
				setBlockAndNotifyAdequately(world, i1, j1, k1, Blocks.chest, 0);
				LOTRChestContents.fillChest(world, random, i1, j1, k1, LOTRChestContents.TROLL_HOARD);
				if (world.getBiomeGenForCoords(i1, k1) instanceof LOTRBiomeGenEttenmoors && random.nextInt(5) == 0)
				{
					LOTRChestContents.fillChest(world, random, i1, j1, k1, LOTRChestContents.TROLL_HOARD_ETTENMOORS);
				}
				chestsGenerated++;
				if (chestsGenerated > chests)
				{
					break chestLoop; 
				}
			}
		}
		
		return chestsGenerated > 0;
	}
}
