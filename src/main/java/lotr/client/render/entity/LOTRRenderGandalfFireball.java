package lotr.client.render.entity;

import lotr.client.LOTRClientProxy;
import lotr.common.entity.projectile.LOTREntityGandalfFireball;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class LOTRRenderGandalfFireball extends Render
{
	@Override
	protected ResourceLocation getEntityTexture(Entity entity)
	{
		return LOTRClientProxy.particlesTexture;
	}
	
	@Override
    public void doRender(Entity entity, double d, double d1, double d2, float f, float f1)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)d, (float)d1, (float)d2);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        bindEntityTexture(entity);
        Tessellator tessellator = Tessellator.instance;
        drawSprite(tessellator, 24 + ((LOTREntityGandalfFireball)entity).animationTick);
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glColor4f(1F, 1F, 1F, 1F);
        GL11.glPopMatrix();
    }

    private void drawSprite(Tessellator tessellator, int index)
    {
        float f = (float)(index % 8 * 16 + 0) / 128F;
        float f1 = (float)(index % 8 * 16 + 16) / 128F;
        float f2 = (float)(index / 8 * 16 + 0) / 128F;
        float f3 = (float)(index / 8 * 16 + 16) / 128F;
        float f4 = 1F;
        float f5 = 0.5F;
        float f6 = 0.25F;
        GL11.glRotatef(180F - renderManager.playerViewY, 0F, 1F, 0F);
        GL11.glRotatef(-renderManager.playerViewX, 1F, 0F, 0F);
        tessellator.startDrawingQuads();
        tessellator.setNormal(0F, 1F, 0F);
		tessellator.setBrightness(15728880);
        tessellator.addVertexWithUV((double)(0F - f5), (double)(0F - f6), 0D, (double)f, (double)f3);
        tessellator.addVertexWithUV((double)(f4 - f5), (double)(0F - f6), 0D, (double)f1, (double)f3);
        tessellator.addVertexWithUV((double)(f4 - f5), (double)(f4 - f6), 0D, (double)f1, (double)f2);
        tessellator.addVertexWithUV((double)(0F - f5), (double)(f4 - f6), 0D, (double)f, (double)f2);
        tessellator.draw();
    }
}
