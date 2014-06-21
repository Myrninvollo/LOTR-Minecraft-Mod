package lotr.common.block;

import java.util.Random;

import lotr.common.LOTRCreativeTabs;
import lotr.common.LOTRMod;
import net.minecraft.block.BlockTorch;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRBlockWoodElvenTorch extends BlockTorch
{
	public LOTRBlockWoodElvenTorch()
	{
		super();
		setCreativeTab(LOTRCreativeTabs.tabDeco);
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int i, int j, int k, Random random)
    {
		int l = world.getBlockMetadata(i, j, k);
		double d0 = (double)((float)i + 0.5F);
		double d1 = (double)((float)j + 0.7F);
		double d2 = (double)((float)k + 0.5F);
		double d3 = 0.2D;
		double d4 = 0.27000001072883606D;
		
		for (int i1 = 0; i1 < 2; i1++)
		{
			String s = "leafRed_" + (20 + random.nextInt(30));
			double d5 = -0.01D + (double)(random.nextFloat() * 0.02F);
			double d6 = -0.01D + (double)(random.nextFloat() * 0.02F);
			double d7 = -0.01D + (double)(random.nextFloat() * 0.02F);

			if (l == 1)
			{
				LOTRMod.proxy.spawnParticle(s, d0 - d4, d1 + d3, d2, d5, d6, d7);
			}
			else if (l == 2)
			{
				LOTRMod.proxy.spawnParticle(s, d0 + d4, d1 + d3, d2, d5, d6, d7);
			}
			else if (l == 3)
			{
				LOTRMod.proxy.spawnParticle(s, d0, d1 + d3, d2 - d4, d5, d6, d7);
			}
			else if (l == 4)
			{
				LOTRMod.proxy.spawnParticle(s, d0, d1 + d3, d2 + d4, d5, d6, d7);
			}
			else
			{
				LOTRMod.proxy.spawnParticle(s, d0, d1, d2, d5, d6, d7);
			}
		}
    }
}
