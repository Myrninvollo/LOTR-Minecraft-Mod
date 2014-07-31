package lotr.client.fx;

import net.minecraft.client.particle.EntitySpellParticleFX;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTREntityMorgulPortalFX extends EntitySpellParticleFX
{
	public LOTREntityMorgulPortalFX(World world, double d, double d1, double d2, double d3, double d4, double d5)
	{
		super(world, d, d1, d2, d3, d4, d5);
		particleRed = 0.2F;
		particleGreen = 0.8F;
		particleBlue = 0.4F;
		particleScale = 0.5F + rand.nextFloat() * 0.5F;
		particleMaxAge = 20 + rand.nextInt(20);
		motionX = -0.05D + (double)(rand.nextFloat() * 0.1D);
		motionY = 0.1D + (double)(rand.nextFloat() * 0.1D);
		motionZ = -0.05D + (double)(rand.nextFloat() * 0.1D);
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
	
	@Override
    public void onUpdate()
    {
        super.onUpdate();
		particleAlpha = 0.5F + 0.5F * ((float)particleAge / (float)particleMaxAge);
		motionX *= 1.1D;
		motionZ *= 1.1D;
    }
}
