package lotr.client.render.entity;

import lotr.client.model.LOTRModelGemsbok;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class LOTRRenderGemsbok extends RenderLiving
{
	private static ResourceLocation texture = new ResourceLocation("lotr:mob/gemsbok.png");
	
    public LOTRRenderGemsbok()
    {
        super(new LOTRModelGemsbok(), 0.5F);
    }
	
	@Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return texture;
    }
}
