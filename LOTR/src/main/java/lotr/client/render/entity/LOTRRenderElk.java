package lotr.client.render.entity;

import lotr.client.model.LOTRModelElk;
import lotr.common.entity.animal.LOTREntityElk;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

public class LOTRRenderElk extends RenderLiving
{
	private static ResourceLocation elkTexture = new ResourceLocation("lotr:mob/elk/elk.png");
	private static ResourceLocation saddleTexture = new ResourceLocation("lotr:mob/elk/saddle.png");
	
    public LOTRRenderElk()
    {
        super(new LOTRModelElk(), 0.5F);
    }
	
	@Override
	public ResourceLocation getEntityTexture(Entity entity)
	{
		return elkTexture;
	}
	
	@Override
	public void renderModel(EntityLivingBase entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		LOTREntityElk elk = (LOTREntityElk)entity;
		super.renderModel(elk, f, f1, f2, f3, f4, f5);
		
		if (elk.isMountSaddled())
		{
			bindTexture(saddleTexture);
			((LOTRModelElk)mainModel).renderSaddle(0.0625F);
		}
	}
}
