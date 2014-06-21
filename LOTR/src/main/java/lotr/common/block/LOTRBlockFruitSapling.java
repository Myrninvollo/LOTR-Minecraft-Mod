package lotr.common.block;

import java.util.Random;

import lotr.common.world.feature.LOTRWorldGenSimpleTrees;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class LOTRBlockFruitSapling extends LOTRBlockSaplingBase
{
    public LOTRBlockFruitSapling()
    {
        super();
		setSaplingNames("apple", "pear", "cherry", "mango");
    }

	@Override
    public void growTree(World world, int i, int j, int k, Random random)
    {
        int meta = world.getBlockMetadata(i, j, k) & 3;
        WorldGenerator treeGen = null;
		
		if (meta == 0)
		{
			treeGen = LOTRWorldGenSimpleTrees.newApple(true);
		}
		else if (meta == 1)
		{
			treeGen = LOTRWorldGenSimpleTrees.newPear(true);
		}
		else if (meta == 2)
		{
			treeGen = LOTRWorldGenSimpleTrees.newCherry(true);
		}
		else if (meta == 3)
		{
			treeGen = LOTRWorldGenSimpleTrees.newMango(true);
		}
		
		world.setBlock(i, j, k, Blocks.air, 0, 4);
		
		if (treeGen != null && !treeGen.generate(world, random, i, j, k))
		{
			world.setBlock(i, j, k, this, meta, 4);
		}
    }
}
