package lotr.client.fx;

import net.minecraft.client.particle.EntitySpellParticleFX;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTREntityMTCHealFX extends EntitySpellParticleFX
{
	private int baseTextureIndex;
	
	public LOTREntityMTCHealFX(World world, double d, double d1, double d2, double d3, double d4, double d5)
	{
		super(world, d, d1, d2, d3, d4, d5);
		particleRed = 1F;
		particleGreen = 0.3F;
		particleBlue = 0.3F;
		particleScale *= 3F;
		particleMaxAge = 30;
		motionX = d3;
		motionY = d4;
		motionZ = d5;
		renderDistanceWeight = 10D;
		noClip = true;
		setBaseSpellTextureIndex(128);
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
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;

        if (particleAge++ >= particleMaxAge)
        {
            setDead();
        }
		
		setParticleTextureIndex(baseTextureIndex + (7 - particleAge * 8 / particleMaxAge));
        moveEntity(motionX, motionY, motionZ);
		
		particleAlpha = 0.5F + 0.5F * ((float)particleAge / (float)particleMaxAge);
    }
	
	@Override
    public void setBaseSpellTextureIndex(int i)
    {
        super.setBaseSpellTextureIndex(i);
		baseTextureIndex = i;
    }
}
