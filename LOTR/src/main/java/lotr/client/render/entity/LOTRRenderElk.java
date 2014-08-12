package lotr.client.render.entity;

import lotr.client.model.LOTRModelCamel;
import lotr.client.model.LOTRModelElk;
import lotr.common.entity.animal.LOTREntityCamel;
import lotr.common.entity.animal.LOTREntityElk;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

public class LOTRRenderElk extends RenderLiving
{
	private static ResourceLocation[] elkTextures = new ResourceLocation[]
	{
		new ResourceLocation("lotr:mob/elk/elk_white.png"),
		new ResourceLocation("lotr:mob/elk/elk_pale.png"),
		new ResourceLocation("lotr:mob/elk/elk_dark.png")
	};
	
	private static ResourceLocation saddleTexture = new ResourceLocation("lotr:mob/elk/saddle.png");
	
    public LOTRRenderElk()
    {
        super(new LOTRModelElk(), 0.5F);
        setRenderPassModel(new LOTRModelElk(0.5F));
    }
	
	@Override
	public ResourceLocation getEntityTexture(Entity entity)
	{
		LOTREntityElk elk = (LOTREntityElk)entity;
		int i = elk.getHorseVariant() % 3;
		return elkTextures[i];
	}
	
	@Override
    protected int shouldRenderPass(EntityLivingBase entity, int pass, float f)
    {
		if (pass == 0 && ((LOTREntityElk)entity).isMountSaddled())
		{
			bindTexture(saddleTexture);
			return 1;
		}
		return super.shouldRenderPass(entity, pass, f);
    }
}
