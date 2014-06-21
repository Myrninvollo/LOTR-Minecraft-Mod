package lotr.client.render.entity;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class LOTRRenderMordorSpider extends LOTRRenderSpiderBase
{
	private static ResourceLocation spiderSkin = new ResourceLocation("lotr:mob/spider/spider_mordor.png");
	
    public LOTRRenderMordorSpider()
    {
        super();
    }
	
	@Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
		return spiderSkin;
	}
}
