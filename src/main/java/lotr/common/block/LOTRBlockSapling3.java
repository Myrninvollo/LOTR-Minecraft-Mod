package lotr.common.block;

import java.util.Random;

import lotr.common.world.feature.*;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class LOTRBlockSapling3 extends LOTRBlockSaplingBase
{
    public LOTRBlockSapling3()
    {
        super();
		setSaplingNames("maple", "larch", "datePalm");
    }
	
	@Override
    public void growTree(World world, int i, int j, int k, Random random)
    {
        int metadata = world.getBlockMetadata(i, j, k) & 3;
        WorldGenerator treeGen = null;
		
		if (metadata == 0)
		{
			if (random.nextInt(10) == 0)
			{
				treeGen = LOTRWorldGenBigTrees.newMaple(true);
			}
			else
			{
				treeGen = LOTRWorldGenSimpleTrees.newMaple(true);
			}
		}
		else if (metadata == 1)
		{
			treeGen = new LOTRWorldGenLarch(true);
		}
		else if (metadata == 2)
		{
			treeGen = new LOTRWorldGenDatePalm(true);
		}
		
		world.setBlock(i, j, k, Blocks.air, 0, 4);
		
		if (treeGen != null && !treeGen.generate(world, random, i, j, k))
		{
			world.setBlock(i, j, k, this, metadata, 4);
		}
    }
}
