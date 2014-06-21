package lotr.common.block;

import java.util.Random;

import lotr.common.world.feature.LOTRWorldGenMirkOak;
import lotr.common.world.feature.LOTRWorldGenShirePine;
import lotr.common.world.feature.LOTRWorldGenSimpleTrees;
import net.minecraft.init.Blocks;
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
		
		if (metadata == 0)
		{
			treeGen = new LOTRWorldGenShirePine(true);
		}
		else if (metadata == 1)
		{
			treeGen = LOTRWorldGenSimpleTrees.newMallorn(true);
		}
		else if (metadata == 2)
		{
			treeGen = new LOTRWorldGenMirkOak(true, 6, 3, 0, 2);
		}
		else if (metadata == 3)
		{
			treeGen = new LOTRWorldGenMirkOak(true, 6, 4, 0, 2).setRed();
		}
		
		world.setBlock(i, j, k, Blocks.air, 0, 4);
		
		if (treeGen != null && !treeGen.generate(world, random, i, j, k))
		{
			world.setBlock(i, j, k, this, metadata, 4);
		}
    }
}
