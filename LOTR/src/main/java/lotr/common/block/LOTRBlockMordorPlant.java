package lotr.common.block;

import lotr.common.LOTRMod;
import net.minecraft.world.World;

public class LOTRBlockMordorPlant extends LOTRBlockFlower
{
    public LOTRBlockMordorPlant()
    {
        super();
    }

    @Override
    public boolean canPlaceBlockAt(World world, int i, int j, int k)
    {
        return super.canPlaceBlockAt(world, i, j, k) && canBlockStay(world, i, j, k);
    }

    @Override
    public boolean canBlockStay(World world, int i, int j, int k)
    {
        if (j >= 0 && j < 256)
        {
            return world.getBlock(i, j - 1, k) == LOTRMod.rock && world.getBlockMetadata(i, j - 1, k) == 0;
        }
        else
        {
            return false;
        }
    }
}
