package lotr.common.world.feature;

import java.util.Random;

import lotr.common.LOTRMod;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraftforge.common.util.ForgeDirection;

public class LOTRWorldGenDatePalm extends WorldGenAbstractTree
{
	public LOTRWorldGenDatePalm(boolean flag)
	{
		super(flag);
	}
	
	@Override
	public boolean generate(World world, Random random, int i, int j, int k)
	{
		int height = 5 + random.nextInt(4);
		
        if (j < 1 || j + height + 2 > 256)
        {
            return false;
        }
		
		if (!isReplaceable(world, i, j, k) && world.getBlock(i, j, k) != LOTRMod.sapling3)
        {
            return false;
        }
		
        Block below = world.getBlock(i, j - 1, k);
		if (below != Blocks.grass && below != Blocks.dirt)
		{
			return false;
		}
		
		for (int l = 1; l < height + 2; l++)
		{
			for (int i1 = i - 1; i1 <= i + 1; i1++)
			{
				for (int k1 = k - 1; k1 <= k + 1; k1++)
				{
					if (!isReplaceable(world, i1, j + l, k1))
					{
						return false;
					}
				}
			}
		}
		
		int leafAngle = 0;
		while (leafAngle < 360)
		{
			leafAngle += 10 + random.nextInt(20);
			float angleR = (float)leafAngle / 180F * (float)Math.PI;
			float sin = MathHelper.sin(angleR);
			float cos = MathHelper.cos(angleR);
			int branchLength = 4 + random.nextInt(3);
			int branchHeight = 2 + random.nextInt(3);
			
			for (int l = 0; l < branchLength; l++)
			{
				int i1 = i + Math.round(sin * l);
				int k1 = k + Math.round(cos * l);
				int j1 = j + height - 2 + Math.round((float)l / (float)branchLength * (float)branchHeight);
				if (isReplaceable(world, i1, j1, k1))
				{
					setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.leaves3, 2 | 4);
				}
			}
		}
		
		for (int l = 0; l < height; l++)
		{
			setBlockAndNotifyAdequately(world, i, j + l, k, LOTRMod.wood3, 2);
		}
		
		for (int l = 0; l < 4; l++)
		{
			ForgeDirection dir = ForgeDirection.getOrientation(l + 2);
			setBlockAndNotifyAdequately(world, i + dir.getOpposite().offsetX, j + height - 3, k + dir.getOpposite().offsetZ, LOTRMod.dateBlock, l);
		}
		
		world.setBlock(i, j - 1, k, Blocks.dirt, 0, 2);
		return true;
	}
}
