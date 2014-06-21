package lotr.client.fx;

import net.minecraft.client.particle.EntityFlameFX;
import net.minecraft.world.World;

public class LOTREntityMarshLightFX extends EntityFlameFX
{
	public LOTREntityMarshLightFX(World world, double d, double d1, double d2, double d3, double d4, double d5)
	{
		super(world, d, d1, d2, d3, d4, d5);
		motionX = d3;
		motionY = d4;
		motionZ = d5;
		setParticleTextureIndex(49);
		particleMaxAge = 40 + rand.nextInt(20);
		particleRed = particleGreen = particleBlue = 0.75F + rand.nextFloat() * 0.25F;
	}
}
