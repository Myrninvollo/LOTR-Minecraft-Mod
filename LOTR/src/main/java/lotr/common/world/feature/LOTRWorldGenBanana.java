package lotr.common.world.feature;

import java.util.Random;

import lotr.common.LOTRMod;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraftforge.common.util.ForgeDirection;

public class LOTRWorldGenBanana extends WorldGenAbstractTree
{
	public LOTRWorldGenBanana(boolean flag)
	{
		super(flag);
	}
	
	@Override
	public boolean generate(World world, Random random, int i, int j, int k)
	{
		int height = 4 + random.nextInt(3);
		int[] leaves = new int[4];
		for (int l = 0; l < 4; l++)
		{
			leaves[l] = 1 + random.nextInt(3);
		}
		
        if (j < 1 || j + height + 5 > 256)
        {
            return false;
        }
		
		if (!isReplaceable(world, i, j, k) && world.getBlock(i, j, k) != LOTRMod.sapling2)
        {
            return false;
        }
		
        Block below = world.getBlock(i, j - 1, k);
		if (below != Blocks.grass && below != Blocks.dirt)
		{
			return false;
		}
		
		for (int l = 0; l < height + 2; l++)
		{
			if (!isReplaceable(world, i, j + l, k))
			{
				return false;
			}
		}
		
		for (int l = 0; l < 4; l++)
		{
			ForgeDirection dir = ForgeDirection.getOrientation(l + 2);
			for (int l1 = -1; l1 < leaves[l]; l1++)
			{
				if (!isReplaceable(world, i + dir.offsetX, j + height + l1, k + dir.offsetZ))
				{
					return false;
				}
			}
			for (int l1 = -1; l1 < 1; l1++)
			{
				if (!isReplaceable(world, i + dir.offsetX * 2, j + height + leaves[l] + l1, k + dir.offsetZ * 2))
				{
					return false;
				}
			}
		}
		
		for (int l = 0; l < height + 2; l++)
		{
			setBlockAndNotifyAdequately(world, i, j + l, k, LOTRMod.wood2, 3);
		}
		
		for (int l = 0; l < 4; l++)
		{
			ForgeDirection dir = ForgeDirection.getOrientation(l + 2);
			for (int l1 = 0; l1 < leaves[l]; l1++)
			{
				setBlockAndNotifyAdequately(world, i + dir.offsetX, j + height + l1, k + dir.offsetZ, LOTRMod.leaves2, 3);
			}
			setBlockAndNotifyAdequately(world, i + dir.getOpposite().offsetX, j + height - 1, k + dir.getOpposite().offsetZ, LOTRMod.bananaBlock, l);
			for (int l1 = -1; l1 < 1; l1++)
			{
				setBlockAndNotifyAdequately(world, i + dir.offsetX * 2, j + height + leaves[l] + l1, k + dir.offsetZ * 2, LOTRMod.leaves2, 3);
			}
		}
		
		world.setBlock(i, j - 1, k, Blocks.dirt, 0, 2);
		return true;
	}
}
