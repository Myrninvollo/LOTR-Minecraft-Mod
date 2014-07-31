package lotr.common.block;

import java.util.Random;

import lotr.common.LOTRCreativeTabs;
import lotr.common.LOTRMod;
import net.minecraft.block.BlockTorch;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class LOTRBlockTorch extends BlockTorch
{
	public LOTRBlockTorch()
	{
		super();
		setCreativeTab(LOTRCreativeTabs.tabDeco);
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int i, int j, int k, Random random)
    {
        int meta = world.getBlockMetadata(i, j, k);
        
        double d = (double)((float)i + 0.5F);
        double d1 = (double)((float)j + 0.7F);
        double d2 = (double)((float)k + 0.5F);
        double particleY = 0.2D;
        double particleX = 0.27D;
        
        TorchParticle particle = createTorchParticle(random);
        if (particle != null)
        {
	        if (meta == 1)
	        {
	        	particle.spawn(d - particleX, d1 + particleY, d2);
	        }
	        else if (meta == 2)
	        {
	        	particle.spawn(d + particleX, d1 + particleY, d2);
	        }
	        else if (meta == 3)
	        {
	        	particle.spawn(d, d1 + particleY, d2 - particleX);
	        }
	        else if (meta == 4)
	        {
	            particle.spawn(d, d1 + particleY, d2 + particleX);
	        }
	        else
	        {
	        	particle.spawn(d, d1, d2);
	        }
        }
    }
	
	protected abstract TorchParticle createTorchParticle(Random random);
	
	public class TorchParticle
	{
		public String name;
		public double motionX;
		public double motionY;
		public double motionZ;
		
		public TorchParticle(String s, double d, double d1, double d2)
		{
			name = s;
			motionX = d;
			motionY = d1;
			motionZ = d2;
		}
		
		public void spawn(double x, double y, double z)
		{
			LOTRMod.proxy.spawnParticle(name, x, y, z, motionX, motionY, motionZ);
		}
	}
}
