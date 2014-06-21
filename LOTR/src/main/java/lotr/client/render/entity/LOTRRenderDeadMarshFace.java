package lotr.client.render.entity;

import lotr.client.fx.LOTREntityDeadMarshFace;
import lotr.client.model.LOTRModelMarshWraith;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class LOTRRenderDeadMarshFace extends Render
{
	private static ResourceLocation skin = new ResourceLocation("lotr:mob/wraith/marshWraith.png");
	private ModelBase model = new LOTRModelMarshWraith();
	
	@Override
	protected ResourceLocation getEntityTexture(Entity entity)
	{
		return skin;
	}
	
	@Override
    public void doRender(Entity entity, double d, double d1, double d2, float f, float f1)
    {
		LOTREntityDeadMarshFace face = (LOTREntityDeadMarshFace)entity;
        GL11.glPushMatrix();
        GL11.glTranslatef((float)d, (float)d1, (float)d2);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(1F, 1F, 1F, face.faceAlpha);
		GL11.glRotatef(90F, 1F, 0F, 0F);
		GL11.glRotatef(face.rotationYaw, 0F, 0F, 1F);
		GL11.glRotatef(face.rotationPitch, 0F, 1F, 0F);
        bindEntityTexture(face);
        model.render(null, 0F, 0F, 0F, 0F, 0F, 0.0625F);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glColor4f(1F, 1F, 1F, 1F);
        GL11.glPopMatrix();
    }
}
