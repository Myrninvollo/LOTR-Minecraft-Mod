package lotr.client.render.entity;

import lotr.client.model.LOTRModelMidge;
import lotr.common.entity.animal.LOTREntityMidges;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class LOTRRenderMidges extends RenderLiving
{
	private static ResourceLocation midgeTexture = new ResourceLocation("lotr:mob/midge.png");
	private float renderTick;
	
	public LOTRRenderMidges()
	{
		super(new LOTRModelMidge(), 0F);
	}
	
	@Override
	public void doRender(Entity entity, double d, double d1, double d2, float f, float f1)
	{
		renderTick = f1;
		super.doRender(entity, d, d1, d2, f, f1);
	}
	
	@Override
	protected void renderModel(EntityLivingBase entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		bindEntityTexture(entity);
		
		mainModel.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		
		LOTREntityMidges midges = (LOTREntityMidges)entity;
		for (int l = 0; l < midges.midges.length; l++)
		{
			LOTREntityMidges.Midge midge = midges.midges[l];
			GL11.glPushMatrix();
			GL11.glTranslatef(midge.midge_prevPosX + (midge.midge_posX - midge.midge_prevPosX) * renderTick, midge.midge_prevPosY + (midge.midge_posY - midge.midge_prevPosY) * renderTick, midge.midge_prevPosZ + (midge.midge_posZ - midge.midge_prevPosZ) * renderTick);
			GL11.glRotatef(midge.midge_rotation, 0F, 1F, 0F);
			GL11.glScalef(0.2F, 0.2F, 0.2F);
			mainModel.render(entity, f, f1, f2, f3, f4, f5);
			GL11.glPopMatrix();
		}
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity)
    {
        return midgeTexture;
    }
}
