package lotr.client.fx;

import net.minecraft.client.particle.EntityFlameFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class LOTREntityElvenGlowFX extends EntityFlameFX
{
	public LOTREntityElvenGlowFX(World world, double d, double d1, double d2, double d3, double d4, double d5)
	{
		super(world, d, d1, d2, d3, d4, d5);
		particleRed = 0.15F;
		particleGreen = 0.9F;
		particleBlue = 1F;
		particleMaxAge *= 3;
	}
	
	@Override
    public void renderParticle(Tessellator tessellator, float f, float f1, float f2, float f3, float f4, float f5)
    {
        float var8 = 0.25F;
        float var9 = var8 + 0.25F;
        float var10 = 0.125F;
        float var11 = var10 + 0.25F;
        float var12 = 0.25F + (0.002F * MathHelper.sin(((float)particleAge + f - 1.0F) * 0.25F * (float)Math.PI));
        particleAlpha = 0.9F - ((float)particleAge + f - 1.0F) * 0.02F;
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
