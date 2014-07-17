package lotr.client.render.entity;

import lotr.client.LOTRClientProxy;
import lotr.client.model.LOTRModelHuorn;
import lotr.common.entity.npc.LOTREntityHuorn;
import lotr.common.entity.npc.LOTREntityHuornBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
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
	public void doRender(EntityLiving entity, double d, double d1, double d2, float f, float f1)
	{
		super.doRender(entity, d, d1, d2, f, f1);
		
		if (Minecraft.isGuiEnabled() && ((LOTREntityHuornBase)entity).hiredNPCInfo.getHiringPlayer() == renderManager.livingPlayer)
		{
			if (entity.riddenByEntity == null)
			{
				func_147906_a(entity, "Hired", d, d1 + 3.5D, d2, 64);
			}
			LOTRClientProxy.renderHealthBar(entity, d, d1 + 3.5D, d2, 64, renderManager);
		}
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
