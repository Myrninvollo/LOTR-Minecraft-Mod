package lotr.client.fx;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;

public class LOTREntityLeafFX extends EntityFX
{
	private int leafAge = 600;
	
	public LOTREntityLeafFX(World world, double d, double d1, double d2, double d3, double d4, double d5, int i)
	{
		super(world, d, d1, d2, d3, d4, d5);
		motionX = d3;
		motionY = d4;
		motionZ = d5;
		particleScale = 0.15F + (rand.nextFloat() * 0.5F);
		setParticleTextureIndex(i);
	}
	
	public LOTREntityLeafFX(World world, double d, double d1, double d2, double d3, double d4, double d5, int i, int j)
	{
		this(world, d, d1, d2, d3, d4, d5, i);
		leafAge = j;
	}
	
	@Override
    public void onUpdate()
    {
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
		if (leafAge > 0)
		{
			leafAge--;
		}
        moveEntity(motionX, motionY, motionZ);
        if (onGround || leafAge == 0 || posY < 0D)
        {
            setDead();
        }
    }
	
	@Override
    public void setParticleTextureIndex(int i)
    {
		particleTextureIndexX = i % 8;
		particleTextureIndexY = i / 8;
    }
	
	@Override
	public void renderParticle(Tessellator tessellator, float f, float f1, float f2, float f3, float f4, float f5)
    {
        float f6 = (float)particleTextureIndexX / 8F;
        float f7 = f6 + 0.125F;
        float f8 = (float)particleTextureIndexY / 8F;
        float f9 = f8 + 0.125F;
        float f10 = 0.2F * particleScale;
        float f11 = (float)(prevPosX + (posX - prevPosX) * (double)f - interpPosX);
        float f12 = (float)(prevPosY + (posY - prevPosY) * (double)f - interpPosY);
        float f13 = (float)(prevPosZ + (posZ - prevPosZ) * (double)f - interpPosZ);
        tessellator.setColorRGBA_F(particleRed, particleGreen, particleBlue, particleAlpha);
        tessellator.addVertexWithUV((double)(f11 - f1 * f10 - f4 * f10), (double)(f12 - f2 * f10), (double)(f13 - f3 * f10 - f5 * f10), (double)f7, (double)f9);
        tessellator.addVertexWithUV((double)(f11 - f1 * f10 + f4 * f10), (double)(f12 + f2 * f10), (double)(f13 - f3 * f10 + f5 * f10), (double)f7, (double)f8);
        tessellator.addVertexWithUV((double)(f11 + f1 * f10 + f4 * f10), (double)(f12 + f2 * f10), (double)(f13 + f3 * f10 + f5 * f10), (double)f6, (double)f8);
        tessellator.addVertexWithUV((double)(f11 + f1 * f10 - f4 * f10), (double)(f12 - f2 * f10), (double)(f13 + f3 * f10 - f5 * f10), (double)f6, (double)f9);
	}
}
