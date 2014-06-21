package lotr.client.render.entity;

import lotr.client.model.LOTRModelHuorn;
import lotr.common.entity.npc.LOTREntityHuornBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class LOTRRenderHuorn extends RenderLiving
{
	private static ResourceLocation faceTexture = new ResourceLocation("lotr:mob/huorn/face.png");
	
    public LOTRRenderHuorn()
    {
        super(new LOTRModelHuorn(), 0F);
    }
	
	@Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return faceTexture;
    }
	
	@Override
    protected void renderLivingAt(EntityLivingBase entity, double d, double d1, double d2)
    {
		LOTREntityHuornBase huorn = (LOTREntityHuornBase)entity;
		if (huorn.isResizedForRender())
		{
			huorn.resizeForRender(false);
		}
		
		if (!huorn.isHuornActive())
		{
			int i = MathHelper.floor_double(huorn.posX);
			int j = MathHelper.floor_double(huorn.posY);
			int k = MathHelper.floor_double(huorn.posZ);
			d = (double)i + 0.5D - renderManager.renderPosX;
			d1 = (double)j - renderManager.renderPosY;
			d2 = (double)k + 0.5D - renderManager.renderPosZ;
		}
		
		d1 -= 0.0078125F;
				
        super.renderLivingAt(entity, d, d1, d2);
		huorn.hurtTime = 0;
    }
	
	@Override
    protected void rotateCorpse(EntityLivingBase entity, float f, float f1, float f2)
    {
		LOTREntityHuornBase huorn = (LOTREntityHuornBase)entity;
		if (!huorn.isHuornActive())
		{
			f1 = 0F;
		}
		super.rotateCorpse(entity, f, f1, f2);
	}
	
	@Override
    protected float handleRotationFloat(EntityLivingBase entity, float f)
    {
        return f;
    }
}
