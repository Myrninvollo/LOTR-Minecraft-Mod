package lotr.common.block;

import java.util.Random;

import lotr.common.LOTRMod;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTRBlockFruitLeaves extends LOTRBlockLeavesBase
{
    public LOTRBlockFruitLeaves()
    {
        super();
		setLeafNames("apple", "pear", "cherry", "mango");
    }
	
    @Override
    public Item getItemDropped(int i, Random random, int j)
    {
        return Item.getItemFromBlock(LOTRMod.fruitSapling);
    }

    @Override
    public void dropBlockAsItemWithChance(World world, int i, int j, int k, int meta, float f, int fortune)
    {
        super.dropBlockAsItemWithChance(world, i, j, k, meta, f, fortune);
		
		if (!world.isRemote)
		{
			if ((meta & 3) == 0 && world.rand.nextInt(20) == 0)
			{
				dropBlockAsItem(world, i, j, k, world.rand.nextBoolean() ? new ItemStack(Items.apple) : new ItemStack(LOTRMod.appleGreen));
			}
			else if ((meta & 3) == 1 && world.rand.nextInt(20) == 0)
			{
				dropBlockAsItem(world, i, j, k, new ItemStack(LOTRMod.pear));
			}
			else if ((meta & 3) == 2 && world.rand.nextInt(10) == 0)
			{
				dropBlockAsItem(world, i, j, k, new ItemStack(LOTRMod.cherry));
			}
			else if ((meta & 3) == 3 && world.rand.nextInt(20) == 0)
			{
				dropBlockAsItem(world, i, j, k, new ItemStack(LOTRMod.mango));
			}
		}
    }
}
