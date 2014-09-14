package lotr.client.render.entity;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class LOTRRenderUtumnoIceSpider extends LOTRRenderSpiderBase
{
	private static ResourceLocation spiderSkin = new ResourceLocation("lotr:mob/spider/spider_utumnoIce.png");
	
    public LOTRRenderUtumnoIceSpider()
    {
        super();
    }
	
	@Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
		return spiderSkin;
	}
}
