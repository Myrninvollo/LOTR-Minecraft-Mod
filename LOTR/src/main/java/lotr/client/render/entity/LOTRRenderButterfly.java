package lotr.client.render.entity;

import lotr.client.model.LOTRModelButterfly;
import lotr.common.entity.animal.LOTREntityButterfly;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class LOTRRenderButterfly extends RenderLiving
{
    public LOTRRenderButterfly()
    {
        super(new LOTRModelButterfly(), 0.2F);
    }
	
	@Override
	public void doRender(Entity entity, double d, double d1, double d2, float f, float f1)
	{
		LOTREntityButterfly butterfly = ((LOTREntityButterfly)entity);
		if (butterfly.getButterflyType() == LOTREntityButterfly.ButterflyType.LORIEN)
		{
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F, 240F);
			GL11.glColor4f(1F, 1F, 1F, 1F);
			GL11.glColor4f(1F, 1F, 1F, 1F);
			GL11.glDisable(GL11.GL_LIGHTING);
		}
		super.doRender(entity, d, d1, d2, f, f1);
		GL11.glEnable(GL11.GL_LIGHTING);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity)
    {
        return ((LOTREntityButterfly)entity).getButterflyType().texture;
    }

	@Override
    protected void preRenderCallback(EntityLivingBase entity, float f)
    {
        GL11.glScalef(0.3F, 0.3F, 0.3F);
    }
	
	@Override
    protected float handleRotationFloat(EntityLivingBase entity, float f)
    {
		LOTREntityButterfly butterfly = ((LOTREntityButterfly)entity);
		if (butterfly.isButterflyStill() && butterfly.flapTime > 0)
		{
			return butterfly.flapTime - f;
		}
		else
		{
			return super.handleRotationFloat(entity, f);
		}
    }
}
