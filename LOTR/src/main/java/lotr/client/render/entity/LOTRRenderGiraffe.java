package lotr.client.render.entity;

import lotr.client.model.LOTRModelGiraffe;
import lotr.common.entity.animal.LOTREntityGiraffe;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

public class LOTRRenderGiraffe extends RenderLiving
{
	private static ResourceLocation texture = new ResourceLocation("lotr:mob/giraffe/giraffe.png");
	private static ResourceLocation saddleTexture = new ResourceLocation("lotr:mob/giraffe/saddle.png");
	
    public LOTRRenderGiraffe()
    {
        super(new LOTRModelGiraffe(0F), 0.5F);
        setRenderPassModel(new LOTRModelGiraffe(1F));
    }
	
	@Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return texture;
    }
	
	@Override
    protected int shouldRenderPass(EntityLivingBase entity, int i, float f)
    {
		if (i == 0 && ((LOTREntityGiraffe)entity).isMountSaddled())
		{
			bindTexture(saddleTexture);
			return 1;
		}
		return -1;
    }
}
