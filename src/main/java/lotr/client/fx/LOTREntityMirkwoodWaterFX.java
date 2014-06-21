package lotr.client.fx;

import net.minecraft.client.particle.EntitySpellParticleFX;
import net.minecraft.world.World;

public class LOTREntityMirkwoodWaterFX extends EntitySpellParticleFX
{
	public LOTREntityMirkwoodWaterFX(World world, double d, double d1, double d2, double d3, double d4, double d5)
	{
		super(world, d, d1, d2, d3, d4, d5);
		particleRed = 0.1F + rand.nextFloat() * 0.3F;
		particleGreen = 0.1F + rand.nextFloat() * 0.3F;
		particleBlue = 0.6F + rand.nextFloat() * 0.4F;
		particleScale = 0.5F + rand.nextFloat() * 0.5F;
		particleMaxAge = 20 + rand.nextInt(20);
		motionX = d3;
		motionY = d4;
		motionZ = d5;
	}
	
	@Override
    public void onUpdate()
    {
        super.onUpdate();
		particleAlpha = 0.5F + 0.5F * ((float)particleAge / (float)particleMaxAge);
    }
}
