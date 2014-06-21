package lotr.client.render.tileentity;

import java.nio.FloatBuffer;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class LOTRRenderElvenPortal extends TileEntitySpecialRenderer
{
    private FloatBuffer floatBuffer = GLAllocation.createDirectFloatBuffer(16);
	private ResourceLocation portalTexture0 = new ResourceLocation("lotr:misc/elvenportal_0.png");
	private ResourceLocation portalTexture1 = new ResourceLocation("lotr:misc/elvenportal_1.png");

	@Override
    public void renderTileEntityAt(TileEntity tileentity, double d, double d1, double d2, float f)
    {
		float f0 = (float)(tileentity.getWorldObj().getWorldTime() % 16L) + f;
        float f1 = (float)field_147501_a.staticPlayerX + f0 * 0.25F;
        float f2 = (float)field_147501_a.staticPlayerY;
        float f3 = (float)field_147501_a.staticPlayerZ + f0 * 0.25F;
        GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glColor3f(0.2F, 0.6F, 1F);
        Random random = new Random(31100L);
        float f4 = 0.75F;

        for (int i = 0; i < 16; i++)
        {
            GL11.glPushMatrix();
            float f5 = (float)(16 - i);
            float f6 = 0.0625F;
            float f7 = 1F / (f5 + 1F);

            if (i == 0)
            {
                bindTexture(portalTexture0);
                f7 = 0.1F;
                f5 = 65F;
                f6 = 0.125F;
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            }

            if (i == 1)
            {
                bindTexture(portalTexture1);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
                f6 = 0.5F;
            }

            float f8 = (float)(-(d1 + (double)f4));
            float f9 = f8 + ActiveRenderInfo.objectY;
            float f10 = f8 + f5 + ActiveRenderInfo.objectY;
            float f11 = f9 / f10;
            f11 += (float)(d1 + (double)f4);
            GL11.glTranslatef(f1, f11, f3);
            GL11.glTexGeni(GL11.GL_S, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_OBJECT_LINEAR);
            GL11.glTexGeni(GL11.GL_T, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_OBJECT_LINEAR);
            GL11.glTexGeni(GL11.GL_R, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_OBJECT_LINEAR);
            GL11.glTexGeni(GL11.GL_Q, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_EYE_LINEAR);
            GL11.glTexGen(GL11.GL_S, GL11.GL_OBJECT_PLANE, getFloatBuffer(1F, 0F, 0F, 0F));
            GL11.glTexGen(GL11.GL_T, GL11.GL_OBJECT_PLANE, getFloatBuffer(0F, 0F, 1F, 0F));
            GL11.glTexGen(GL11.GL_R, GL11.GL_OBJECT_PLANE, getFloatBuffer(0F, 0F, 0F, 1F));
            GL11.glTexGen(GL11.GL_Q, GL11.GL_EYE_PLANE, getFloatBuffer(0F, 1F, 0F, 0F));
            GL11.glEnable(GL11.GL_TEXTURE_GEN_S);
            GL11.glEnable(GL11.GL_TEXTURE_GEN_T);
            GL11.glEnable(GL11.GL_TEXTURE_GEN_R);
            GL11.glEnable(GL11.GL_TEXTURE_GEN_Q);
            GL11.glPopMatrix();
            GL11.glMatrixMode(GL11.GL_TEXTURE);
            GL11.glPushMatrix();
            GL11.glLoadIdentity();
            GL11.glTranslatef(0F, (float)(Minecraft.getSystemTime() % 700000L) / 700000F, 0F);
            GL11.glScalef(f6, f6, f6);
            GL11.glTranslatef(0.5F, 0.5F, 0F);
            GL11.glRotatef((float)(i * i * 4321 + i * 9) * 2F, 0F, 0F, 1F);
            GL11.glTranslatef(-0.5F, -0.5F, 0F);
            GL11.glTranslatef(-f1, -f3, -f2);
            f9 = f8 + ActiveRenderInfo.objectY;
            GL11.glTranslatef(ActiveRenderInfo.objectX * f5 / f9, ActiveRenderInfo.objectZ * f5 / f9, -f2);
            Tessellator tessellator = Tessellator.instance;
            tessellator.startDrawingQuads();
            f11 = random.nextFloat() * 0.5F + 0.1F;
            float f12 = random.nextFloat() * 0.5F + 0.4F;
            float f13 = random.nextFloat() * 0.5F + 0.5F;

            if (i == 0)
            {
                f13 = 1F;
                f12 = 1F;
                f11 = 1F;
            }

            tessellator.setColorRGBA_F(f11 * f7, f12 * f7, f13 * f7, 1F);
            tessellator.addVertex(d, d1 + (double)f4, d2);
            tessellator.addVertex(d, d1 + (double)f4, d2 + 1D);
            tessellator.addVertex(d + 1D, d1 + (double)f4, d2 + 1D);
            tessellator.addVertex(d + 1D, d1 + (double)f4, d2);
            tessellator.draw();
            GL11.glPopMatrix();
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
        }

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_GEN_S);
        GL11.glDisable(GL11.GL_TEXTURE_GEN_T);
        GL11.glDisable(GL11.GL_TEXTURE_GEN_R);
        GL11.glDisable(GL11.GL_TEXTURE_GEN_Q);
		GL11.glColor3f(1F, 1F, 1F);
        GL11.glEnable(GL11.GL_LIGHTING);
    }

    private FloatBuffer getFloatBuffer(float f, float f1, float f2, float f3)
    {
        floatBuffer.clear();
        floatBuffer.put(f).put(f1).put(f2).put(f3);
        floatBuffer.flip();
        return floatBuffer;
    }
}
