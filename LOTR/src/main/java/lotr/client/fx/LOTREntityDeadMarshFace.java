package lotr.client.fx;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class LOTREntityDeadMarshFace extends EntityFX
{
	public float faceAlpha;
	
	public LOTREntityDeadMarshFace(World world, double d, double d1, double d2)
	{
		super(world, d, d1, d2, 0D, 0D, 0D);
		particleMaxAge = 120 + rand.nextInt(120);
		rotationYaw = world.rand.nextFloat() * 360F;
		rotationPitch = -60F + world.rand.nextFloat() * 120F;
	}
	
	@Override
    public void onUpdate()
    {
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
		
		particleAge++;
		faceAlpha = MathHelper.sin(((float)particleAge / (float)particleMaxAge) * (float)Math.PI);
		if (particleAge > particleMaxAge)
		{
			setDead();
		}
    }
}
