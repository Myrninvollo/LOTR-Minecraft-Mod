package lotr.common.block;

import java.util.Random;

import lotr.common.LOTRMod;
import lotr.common.world.feature.*;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class LOTRBlockSapling extends LOTRBlockSaplingBase
{
    public LOTRBlockSapling()
    {
        super();
		setSaplingNames("shirePine", "mallorn", "mirkOak", "mirkOakRed");
    }
	
	@Override
    public void growTree(World world, int i, int j, int k, Random random)
    {
        int metadata = world.getBlockMetadata(i, j, k) & 3;
        WorldGenerator treeGen = null;
        int trunkWidth = 0;
		int xOffset = 0;
		int zOffset = 0;
		
		if (metadata == 0)
		{
			treeGen = new LOTRWorldGenShirePine(true);
		}
		
		if (metadata == 1)
		{
			if (world.getBlock(i, j - 1, k) == LOTRMod.quenditeGrass)
			{
				saplingSearch:
	            for (int i1 = 2; i1 >= -2; i1--)
	            {
	                for (int k1 = 2; k1 >= -2; k1--)
	                {
	                	boolean canGenerate = true;
	                	subSearch:
	                	for (int i2 = -1; i2 <= 1; i2++)
	                	{
	                		for (int k2 = -1; k2 <= 1; k2++)
	                    	{
	                			int i3 = i + i1 + i2;
	                			int k3 = k + k1 + k2;
	                			if (!isSameSapling(world, i3, j, k3, metadata) || world.getBlock(i3, j - 1, k3) != LOTRMod.quenditeGrass)
	                			{
	                				canGenerate = false;
	                				break subSearch;
	                			}
	                    	}
	                	}
	                	
	                    if (canGenerate)
	                    {
	                    	treeGen = new LOTRWorldGenMallornLarge(true).setSaplingGrowth();
	                    	trunkWidth = 1;
							xOffset = i1;
							zOffset = k1;
	                        break saplingSearch;
	                    }
	                }
	            }
			}

            if (treeGen == null)
            {
				xOffset = 0;
				zOffset = 0;
				treeGen = LOTRWorldGenSimpleTrees.newMallorn(true);
            }
		}
		
		if (metadata == 2)
		{
			treeGen = new LOTRWorldGenMirkOak(true, 6, 3, 0, 2);
		}
		
		if (metadata == 3)
		{
			treeGen = new LOTRWorldGenMirkOak(true, 6, 4, 0, 2).setRed();
		}
		
		for (int i1 = -trunkWidth; i1 <= trunkWidth; i1++)
		{
			for (int k1 = -trunkWidth; k1 <= trunkWidth; k1++)
			{
				world.setBlock(i + xOffset + i1, j, k + zOffset + k1, Blocks.air, 0, 4);
			}
		}
		
		if (treeGen != null && !treeGen.generate(world, random, i + xOffset, j, k + zOffset))
		{
			for (int i1 = -trunkWidth; i1 <= trunkWidth; i1++)
			{
				for (int k1 = -trunkWidth; k1 <= trunkWidth; k1++)
				{
					world.setBlock(i + xOffset + i1, j, k + zOffset + k1, this, metadata, 4);
				}
			}
		}
    }
	
	@Override
    public boolean canReplace(World world, int i, int j, int k, int side, ItemStack item)
    {
    	if (super.canReplace(world, i, j, k, side, item))
    	{
    		return true;
    	}
    	else
    	{
    		if (item.getItemDamage() == 1 && world.getBlock(i, j - 1, k) == LOTRMod.quenditeGrass)
    		{
    			return true;
    		}
    		return false;
    	}
    }
	
	@Override
    public boolean canBlockStay(World world, int i, int j, int k)
    {
    	if (super.canBlockStay(world, i, j, k))
    	{
    		return true;
    	}
    	else
    	{
    		int meta = world.getBlockMetadata(i, j, k) & 3;
    		if (meta == 1 && world.getBlock(i, j - 1, k) == LOTRMod.quenditeGrass)
    		{
    			return true;
    		}
    		return false;
    	}
    }
}
