package lotr.client.fx;

import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTREntityLorienButterflyFX extends LOTREntityElvenGlowFX
{
	public LOTREntityLorienButterflyFX(World world, double d, double d1, double d2, double d3, double d4, double d5)
	{
		super(world, d, d1, d2, d3, d4, d5);
		particleMaxAge /= 5;
		particleScale /= 2;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public int getBrightnessForRender(float f)
    {
        return 15728880;
    }

    @Override
    public float getBrightness(float f)
    {
        return 1F;
    }
}
