package lotr.common.block;

import java.util.Random;

import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRBlockPipeweedPlant extends LOTRBlockFlower
{
	public LOTRBlockPipeweedPlant()
	{
		super();
		setFlowerBounds(0.1F, 0F, 0.1F, 0.9F, 0.8F, 0.9F);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, int i, int j, int k, Random random)
	{
		if (random.nextInt(4) == 0)
		{
			double d = i + 0.1D + (double)(random.nextFloat() * 0.8F);
			double d1 = j + 0.5D + (double)(random.nextFloat() * 0.25F);
			double d2 = k + 0.1D + (double)(random.nextFloat() * 0.8F);
			world.spawnParticle("smoke", d, d1, d2, 0D, 0D, 0D);
		}
	}
}
