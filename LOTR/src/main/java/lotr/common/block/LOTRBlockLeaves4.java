package lotr.common.block;

import java.util.Random;

import lotr.common.LOTRMod;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTRBlockLeaves4 extends LOTRBlockLeavesBase
{
    public LOTRBlockLeaves4()
    {
        super();
		setLeafNames("chestnut", "baobab", "cedar");
    }
    
    @Override
    public void updateTick(World world, int i, int j, int k, Random random)
    {
    	super.updateTick(world, i, j, k, random);

        if (!world.isRemote && world.isAirBlock(i, j - 1, k))
        {
            int meta = world.getBlockMetadata(i, j, k);
            int leafType = meta & 3;
            boolean playerPlaced = (meta & 4) != 0;
            if (leafType == 0 && !playerPlaced && random.nextInt(50) == 0)
            {
                double d = i + random.nextDouble();
                double d1 = j - 0.2D;
                double d2 = k + random.nextDouble();
                EntityItem conker = new EntityItem(world, d, d1, d2, new ItemStack(LOTRMod.chestnut));
                conker.delayBeforeCanPickup = 10;
                conker.motionX = conker.motionY = conker.motionZ = 0D;
                world.spawnEntityInWorld(conker);
            }
        }
    }

    @Override
    public Item getItemDropped(int i, Random random, int j)
    {
        return Item.getItemFromBlock(LOTRMod.sapling4);
    }
    
    @Override
    public void dropBlockAsItemWithChance(World world, int i, int j, int k, int meta, float f, int fortune)
    {
        super.dropBlockAsItemWithChance(world, i, j, k, meta, f, fortune);
		
		if (!world.isRemote)
		{
			if ((meta & 3) == 0 && world.rand.nextInt(20) == 0)
			{
				dropBlockAsItem(world, i, j, k, new ItemStack(LOTRMod.chestnut));
			}
		}
    }
}
