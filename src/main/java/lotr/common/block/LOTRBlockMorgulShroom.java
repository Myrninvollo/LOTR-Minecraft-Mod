package lotr.common.block;

import java.util.Random;

import lotr.common.LOTRCreativeTabs;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;

public class LOTRBlockMorgulShroom extends LOTRBlockMordorPlant
{
    public LOTRBlockMorgulShroom()
    {
        super();
        float f = 0.2F;
        setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f * 2.0F, 0.5F + f);
        setTickRandomly(true);
		setCreativeTab(LOTRCreativeTabs.tabFood);
    }

    @Override
    public void updateTick(World world, int i, int j, int k, Random random)
    {
        if (random.nextInt(10) == 0)
        {
			boolean nearbyWater = false;
			for (int i1 = i - 2; i1 <= i + 2 && !nearbyWater; i1++)
			{
				for (int j1 = j - 2; j1 <= j + 2 && !nearbyWater; j1++)
				{
					for (int k1 = k - 2; k1 <= k + 2 && !nearbyWater; k1++)
					{
						if (world.getBlock(i1, j - 1, k1).getMaterial() == Material.water)
						{
							nearbyWater = true;
						}
					}
				}
			}
			
			if (nearbyWater)
			{
				int i1 = i - 1 + random.nextInt(3);
				int j1 = j - 1 + random.nextInt(3);
				int k1 = k - 1 + random.nextInt(3);
				if (world.isAirBlock(i1, j1, k1) && canBlockStay(world, i1, j1, k1))
				{
					world.setBlock(i1, j1, k1, this);
				}
			}
        }
    }
}
