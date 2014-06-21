package lotr.common.block;

import java.util.Random;

import lotr.common.LOTRCreativeTabs;
import lotr.common.LOTRMod;
import net.minecraft.block.BlockTorch;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRBlockElvenTorch extends BlockTorch
{
	public LOTRBlockElvenTorch()
	{
		super();
		setCreativeTab(LOTRCreativeTabs.tabDeco);
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int i, int j, int k, Random random)
    {
		if (random.nextInt(3) != 0)
		{
			return;
		}
		
        int l = world.getBlockMetadata(i, j, k);
        double d0 = (double)((float)i + 0.5F);
        double d1 = (double)((float)j + 0.7F);
        double d2 = (double)((float)k + 0.5F);
        double d3 = 0.2199999988079071D;
        double d4 = 0.27000001072883606D;

        if (l == 1)
        {
            LOTRMod.proxy.spawnParticle("elvenGlow", d0 - d4, d1 + d3, d2, 0.0D, 0.0D, 0.0D);
        }
        else if (l == 2)
        {
            LOTRMod.proxy.spawnParticle("elvenGlow", d0 + d4, d1 + d3, d2, 0.0D, 0.0D, 0.0D);
        }
        else if (l == 3)
        {
            LOTRMod.proxy.spawnParticle("elvenGlow", d0, d1 + d3, d2 - d4, 0.0D, 0.0D, 0.0D);
        }
        else if (l == 4)
        {
            LOTRMod.proxy.spawnParticle("elvenGlow", d0, d1 + d3, d2 + d4, 0.0D, 0.0D, 0.0D);
        }
        else
        {
            LOTRMod.proxy.spawnParticle("elvenGlow", d0, d1, d2, 0.0D, 0.0D, 0.0D);
        }
    }
}
