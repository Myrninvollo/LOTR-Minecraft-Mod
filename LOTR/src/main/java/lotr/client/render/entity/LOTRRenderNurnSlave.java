package lotr.client.render.entity;

import lotr.client.model.LOTRModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class LOTRRenderNurnSlave extends LOTRRenderBiped
{
	private static ResourceLocation skin = new ResourceLocation("lotr:mob/nurn/slave.png");
	
	public LOTRRenderNurnSlave()
	{
		super(new LOTRModelBiped(), 0.5F);
	}
	
	@Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return skin;
    }
}
