package lotr.client.fx;

import net.minecraft.client.particle.EntitySmokeFX;
import net.minecraft.world.World;

public class LOTREntityQuenditeSmokeFX extends EntitySmokeFX
{
	public LOTREntityQuenditeSmokeFX(World world, double d, double d1, double d2, double d3, double d4, double d5)
	{
		super(world, d, d1, d2, d3, d4, d5);
		particleRed = rand.nextFloat() * 0.3F;
		particleGreen = 0.5F + rand.nextFloat() * 0.5F;
		particleBlue = 1F;
	}
}