package lotr.client.render.entity;

import lotr.client.model.LOTRModelMarshWraith;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class LOTRRenderMarshWraith extends RenderLiving
{
	private static ResourceLocation skin = new ResourceLocation("lotr:mob/wraith/marshWraith.png");
	
    public LOTRRenderMarshWraith()
    {
        super(new LOTRModelMarshWraith(), 0.5F);
    }
	
	@Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
		return skin;
	}
}
