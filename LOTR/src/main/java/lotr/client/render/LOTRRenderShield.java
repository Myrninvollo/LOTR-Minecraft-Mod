package lotr.client.render;

import lotr.common.LOTRShields;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class LOTRRenderShield
{
	private static int SHIELD_WIDTH = 32;
	private static int SHIELD_HEIGHT = 32;

	public static void renderShield(LOTRShields shield, ModelBiped model)
	{
		Minecraft mc = Minecraft.getMinecraft();
		ResourceLocation shieldTexture = shield.shieldTexture;
		
		GL11.glPushMatrix();
		GL11.glColor4f(1F, 1F, 1F, 1F);
		
		float modelScale = 0.0625F;
		model.bipedLeftArm.postRender(modelScale);
		
		GL11.glScalef(-1.5F, -1.5F, 1.5F);
		GL11.glRotatef(60F, 0F, 1F, 0F);
		GL11.glTranslatef(0F, -0.75F, 0F);
		GL11.glTranslatef(-0.5F, 0F, 0F);
		GL11.glTranslatef(0F, 0F, -0.15F);
		GL11.glRotatef(-15F, 0F, 0F, 1F);
		
		mc.getTextureManager().bindTexture(shieldTexture);
		
		float minU = 0F;
		float maxU = 1F;
		float minV = 0F;
		float maxV = 1F;

		int width = SHIELD_WIDTH;
		int height = SHIELD_HEIGHT;
		
		Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setNormal(0F, 0F, 1F);
        tessellator.addVertexWithUV(0D, 0D, 0D, (double)maxU, (double)maxV);
        tessellator.addVertexWithUV(1D, 0D, 0D, (double)minU, (double)maxV);
        tessellator.addVertexWithUV(1D, 1D, 0D, (double)minU, (double)minV);
        tessellator.addVertexWithUV(0D, 1D, 0D, (double)maxU, (double)minV);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0F, 0F, -1F);
        tessellator.addVertexWithUV(0D, 1D, (double)(0F - modelScale), (double)maxU, (double)minV);
        tessellator.addVertexWithUV(1D, 1D, (double)(0F - modelScale), (double)minU, (double)minV);
        tessellator.addVertexWithUV(1D, 0D, (double)(0F - modelScale), (double)minU, (double)maxV);
        tessellator.addVertexWithUV(0D, 0D, (double)(0F - modelScale), (double)maxU, (double)maxV);
        tessellator.draw();
        float f5 = 0.5F * (maxU - minU) / (float)width;
        float f6 = 0.5F * (maxV - minV) / (float)height;
        tessellator.startDrawingQuads();
        tessellator.setNormal(-1F, 0F, 0F);
        int k;
        float f7;
        float f8;
        float f9;

        for (k = 0; k < width; k++)
        {
            f7 = (float)k / (float)width;
            f8 = maxU + (minU - maxU) * f7 - f5;
            tessellator.addVertexWithUV((double)f7, 0D, (double)(0F - modelScale), (double)f8, (double)maxV);
            tessellator.addVertexWithUV((double)f7, 0D, 0D, (double)f8, (double)maxV);
            tessellator.addVertexWithUV((double)f7, 1D, 0D, (double)f8, (double)minV);
            tessellator.addVertexWithUV((double)f7, 1D, (double)(0F - modelScale), (double)f8, (double)minV);
        }

        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(1F, 0F, 0F);

        for (k = 0; k < width; k++)
        {
            f7 = (float)k / (float)width;
            f8 = maxU + (minU - maxU) * f7 - f5;
            f9 = f7 + 1F / (float)width;
            tessellator.addVertexWithUV((double)f9, 1D, (double)(0F - modelScale), (double)f8, (double)minV);
            tessellator.addVertexWithUV((double)f9, 1D, 0D, (double)f8, (double)minV);
            tessellator.addVertexWithUV((double)f9, 0D, 0D, (double)f8, (double)maxV);
            tessellator.addVertexWithUV((double)f9, 0D, (double)(0F - modelScale), (double)f8, (double)maxV);
        }

        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0F, 1F, 0F);

        for (k = 0; k < height; k++)
        {
            f7 = (float)k / (float)height;
            f8 = maxV + (minV - maxV) * f7 - f6;
            f9 = f7 + 1F / (float)height;
            tessellator.addVertexWithUV(0D, (double)f9, 0D, (double)maxU, (double)f8);
            tessellator.addVertexWithUV(1D, (double)f9, 0D, (double)minU, (double)f8);
            tessellator.addVertexWithUV(1D, (double)f9, (double)(0F - modelScale), (double)minU, (double)f8);
            tessellator.addVertexWithUV(0D, (double)f9, (double)(0F - modelScale), (double)maxU, (double)f8);
        }

        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0F, -1F, 0F);

        for (k = 0; k < height; k++)
        {
            f7 = (float)k / (float)height;
            f8 = maxV + (minV - maxV) * f7 - f6;
            tessellator.addVertexWithUV(1D, (double)f7, 0D, (double)minU, (double)f8);
            tessellator.addVertexWithUV(0D, (double)f7, 0D, (double)maxU, (double)f8);
            tessellator.addVertexWithUV(0D, (double)f7, (double)(0F - modelScale), (double)maxU, (double)f8);
            tessellator.addVertexWithUV(1D, (double)f7, (double)(0F - modelScale), (double)minU, (double)f8);
        }

        tessellator.draw();
		
		GL11.glPopMatrix();
	}
}
