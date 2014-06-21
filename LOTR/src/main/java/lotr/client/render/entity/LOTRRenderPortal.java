package lotr.client.render.entity;

import lotr.client.model.LOTRModelPortal;
import lotr.common.entity.item.LOTREntityPortal;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class LOTRRenderPortal extends Render
{
	private static ResourceLocation ringTexture = new ResourceLocation("lotr:misc/portal.png");
	private static ResourceLocation writingTexture = new ResourceLocation("lotr:misc/portal_writing.png");
	private static ModelBase ringModel = new LOTRModelPortal(0);
	private static ModelBase writingModel = new LOTRModelPortal(1);
	
	@Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return ringTexture;
    }
	
	@Override
	public void doRender(Entity entity, double d, double d1, double d2, float f, float f1)
	{
		LOTREntityPortal portal = (LOTREntityPortal)entity;
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glTranslatef((float)d, (float)d1 + 0.75F, (float)d2);
		GL11.glNormal3f(0F, 0F, 0F);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glScalef(1F, -1F, 1F);
		float scale = portal.getScale();
		if (scale < (float)LOTREntityPortal.MAX_SCALE)
		{
			scale += f1;
			scale /= (float)LOTREntityPortal.MAX_SCALE;
			GL11.glScalef(scale, scale, scale);
		}
		GL11.glRotatef(portal.getPortalRotation(f1), 0F, 1F, 0F);
		GL11.glRotatef(10F, 1F, 0F, 0F);
		bindTexture(getEntityTexture(portal));
		ringModel.render(null, 0F, 0F, 0F, 0F, 0F, 0.0625F);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		Tessellator.instance.setBrightness(15728880);
		bindTexture(writingTexture);
		writingModel.render(null, 0F, 0F, 0F, 0F, 0F, 0.064F);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glPopMatrix();
	}
}
