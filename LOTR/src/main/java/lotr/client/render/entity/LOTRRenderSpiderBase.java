package lotr.client.render.entity;

import lotr.client.LOTRClientProxy;
import lotr.client.model.LOTRModelSpider;
import lotr.common.entity.npc.LOTREntitySpiderBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;

import org.lwjgl.opengl.GL11;

public abstract class LOTRRenderSpiderBase extends RenderLiving
{
    public LOTRRenderSpiderBase()
    {
        super(new LOTRModelSpider(), 1F);
        setRenderPassModel(new LOTRModelSpider());
    }
	
	@Override
	public void doRender(EntityLiving entity, double d, double d1, double d2, float f, float f1)
	{
		super.doRender(entity, d, d1, d2, f, f1);
		
		if (Minecraft.isGuiEnabled() && ((LOTREntitySpiderBase)entity).hiredNPCInfo.getHiringPlayer() == renderManager.livingPlayer)
		{
			if (entity.riddenByEntity == null)
			{
				func_147906_a(entity, "Hired", d, d1 + 0.5D, d2, 64);
			}
			LOTRClientProxy.renderHealthBar(entity, d, d1 + 0.5D, d2, 64, renderManager);
		}
	}

    @Override
    protected void preRenderCallback(EntityLivingBase entity, float f)
    {
        float scale = ((LOTREntitySpiderBase)entity).getSpiderScaleAmount();
		GL11.glScalef(scale, scale, scale);
    }	

	@Override
    protected float getDeathMaxRotation(EntityLivingBase entity)
    {
        return 180F;
    }
}
