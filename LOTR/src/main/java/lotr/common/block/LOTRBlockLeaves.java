package lotr.common.block;

import java.util.Random;

import lotr.common.LOTRMod;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRBlockLeaves extends LOTRBlockLeavesBase
{
    public LOTRBlockLeaves()
    {
        super();
		setLeafNames("shirePine", "mallorn", "mirkOak", "mirkOakRed");
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int i, int j, int k, Random random)
    {
        super.randomDisplayTick(world, i, j, k, random);

		String s = null;
		int metadata = world.getBlockMetadata(i, j, k) & 3;
		
		if (metadata == 1 && random.nextInt(75) == 0)
		{
			s = "leafGold";
		}
		else if (metadata == 2 && random.nextInt(250) == 0)
		{
			s = "leafGreen";
		}
		else if (metadata == 3 && random.nextInt(40) == 0)
		{
			s = "leafRed";
		}
		
		if (s != null)
		{
			double d = i + random.nextFloat();
			double d1 = j - 0.05D;
			double d2 = k + random.nextFloat();
			double d3 = -0.1D + (double)(random.nextFloat() * 0.2F);
			double d4 = -0.03D - (double)(random.nextFloat() * 0.02F);
			double d5 = -0.1D + (double)(random.nextFloat() * 0.2F);
			LOTRMod.proxy.spawnParticle(s, d, d1, d2, d3, d4, d5);
		}
    }
	
    @Override
    public Item getItemDropped(int i, Random random, int j)
    {
        return Item.getItemFromBlock(LOTRMod.sapling);
    }

    @Override
    public void dropBlockAsItemWithChance(World world, int i, int j, int k, int meta, float f, int fortune)
    {
		super.dropBlockAsItemWithChance(world, i, j, k, meta, f, fortune);
		
        if (!world.isRemote)
        {
			if ((meta & 3) == 1 && world.rand.nextInt(100) == 0)
			{
				dropBlockAsItem(world, i, j, k, new ItemStack(LOTRMod.mallornNut));
			}
        }
    }
	
	@Override
    public int getLightOpacity(IBlockAccess world, int i, int j, int k)
    {
		int l = world.getBlockMetadata(i, j, k) & 3;
		if (l == 2 || l == 3)
		{
			return 255;
		}
		return super.getLightOpacity(world, i, j, k);
    }
}
