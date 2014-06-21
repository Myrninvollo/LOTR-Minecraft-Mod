package lotr.client.render.entity;

import lotr.common.entity.projectile.LOTREntityMarshWraithBall;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class LOTRRenderWraithBall extends Render
{
	private static ResourceLocation texture = new ResourceLocation("lotr:mob/wraith/marshWraith_ball.png");
	
	@Override
	protected ResourceLocation getEntityTexture(Entity entity)
	{
		return texture;
	}
	
	@Override
    public void doRender(Entity entity, double d, double d1, double d2, float f, float f1)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)d, (float)d1, (float)d2);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        bindEntityTexture(entity);
        Tessellator tessellator = Tessellator.instance;
        drawSprite(tessellator, ((LOTREntityMarshWraithBall)entity).animationTick);
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glColor4f(1F, 1F, 1F, 1F);
        GL11.glPopMatrix();
    }

    private void drawSprite(Tessellator tessellator, int index)
    {
        float var3 = (float)(index % 4 * 16 + 0) / 64.0F;
        float var4 = (float)(index % 4 * 16 + 16) / 64.0F;
        float var5 = (float)(index / 4 * 16 + 0) / 64.0F;
        float var6 = (float)(index / 4 * 16 + 16) / 64.0F;
        float var7 = 1.0F;
        float var8 = 0.5F;
        float var9 = 0.25F;
        GL11.glRotatef(180.0F - renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        tessellator.addVertexWithUV((double)(0.0F - var8), (double)(0.0F - var9), 0.0D, (double)var3, (double)var6);
        tessellator.addVertexWithUV((double)(var7 - var8), (double)(0.0F - var9), 0.0D, (double)var4, (double)var6);
        tessellator.addVertexWithUV((double)(var7 - var8), (double)(var7 - var9), 0.0D, (double)var4, (double)var5);
        tessellator.addVertexWithUV((double)(0.0F - var8), (double)(var7 - var9), 0.0D, (double)var3, (double)var5);
        tessellator.draw();
    }
}
