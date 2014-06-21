package lotr.client.fx;

import net.minecraft.client.particle.EntityFireworkOverlayFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTREntityGandalfFireballExplodeFX extends EntityFireworkOverlayFX
{
	public LOTREntityGandalfFireballExplodeFX(World world, double d, double d1, double d2)
	{
		super(world, d, d1, d2);
		particleRed = 0.33F;
		particleGreen = 1F;
		particleBlue = 1F;
		particleMaxAge = 32;
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
    public void renderParticle(Tessellator tessellator, float f, float f1, float f2, float f3, float f4, float f5)
    {
        float var8 = 0.25F;
        float var9 = var8 + 0.25F;
        float var10 = 0.125F;
        float var11 = var10 + 0.25F;
        float var12 = 16F - particleAge * 0.2F;
        particleAlpha = 0.9F - ((float)particleAge + f - 1.0F) * 0.15F;
        float var13 = (float)(prevPosX + (posX - prevPosX) * (double)f - interpPosX);
        float var14 = (float)(prevPosY + (posY - prevPosY) * (double)f - interpPosY);
        float var15 = (float)(prevPosZ + (posZ - prevPosZ) * (double)f - interpPosZ);
        tessellator.setColorRGBA_F(particleRed, particleGreen, particleBlue, particleAlpha);
        tessellator.addVertexWithUV((double)(var13 - f1 * var12 - f4 * var12), (double)(var14 - f2 * var12), (double)(var15 - f3 * var12 - f5 * var12), (double)var9, (double)var11);
        tessellator.addVertexWithUV((double)(var13 - f1 * var12 + f4 * var12), (double)(var14 + f2 * var12), (double)(var15 - f3 * var12 + f5 * var12), (double)var9, (double)var10);
        tessellator.addVertexWithUV((double)(var13 + f1 * var12 + f4 * var12), (double)(var14 + f2 * var12), (double)(var15 + f3 * var12 + f5 * var12), (double)var8, (double)var10);
        tessellator.addVertexWithUV((double)(var13 + f1 * var12 - f4 * var12), (double)(var14 - f2 * var12), (double)(var15 + f3 * var12 - f5 * var12), (double)var8, (double)var11);
    }
}
