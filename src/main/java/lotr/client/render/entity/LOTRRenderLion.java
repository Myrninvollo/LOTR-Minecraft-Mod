package lotr.client.render.entity;

import lotr.client.model.LOTRModelLion;
import lotr.common.entity.animal.LOTREntityLioness;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class LOTRRenderLion extends RenderLiving
{
	private static ResourceLocation textureLion = new ResourceLocation("lotr:mob/lion/lion.png");
	private static ResourceLocation textureLioness = new ResourceLocation("lotr:mob/lion/lioness.png");
	
    public LOTRRenderLion()
    {
        super(new LOTRModelLion(), 0.5F);
    }
	
	@Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return entity instanceof LOTREntityLioness ? textureLioness : textureLion;
    }
}
