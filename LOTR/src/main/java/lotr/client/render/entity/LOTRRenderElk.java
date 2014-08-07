package lotr.client.render.entity;

import lotr.client.model.LOTRModelElk;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class LOTRRenderElk extends RenderLiving
{
	private static ResourceLocation elkTexture = new ResourceLocation("lotr:mob/elk/elk.png");
	
    public LOTRRenderElk()
    {
        super(new LOTRModelElk(), 0.5F);
    }
	
	@Override
	public ResourceLocation getEntityTexture(Entity entity)
	{
		return elkTexture;
	}
}
