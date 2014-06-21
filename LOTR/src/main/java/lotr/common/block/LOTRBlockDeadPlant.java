package lotr.common.block;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.IShearable;

public class LOTRBlockDeadPlant extends LOTRBlockFlower implements IShearable
{
	public LOTRBlockDeadPlant()
	{
		super();
		setBlockBounds(0.1F, 0F, 0.1F, 0.9F, 0.8F, 0.9F);
	}
	
	@Override
    public Item getItemDropped(int i, Random random, int j)
    {
        return null;
    }
	
    @Override
    public boolean isShearable(ItemStack item, IBlockAccess world, int i, int j, int k)
    {
        return true;
    }

    @Override
    public ArrayList onSheared(ItemStack item, IBlockAccess world, int i, int j, int k, int fortune)
    {
        ArrayList drops = new ArrayList();
        drops.add(new ItemStack(this));
        return drops;
    }
}
