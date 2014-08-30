package lotr.client.render.entity;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class LOTRRenderNearHaradrimWarlord extends LOTRRenderNearHaradrim
{
	private static ResourceLocation skin = new ResourceLocation("lotr:mob/nearHarad/warlord.png");
	
	public LOTRRenderNearHaradrimWarlord()
	{
		super();
	}
	
	@Override
	public ResourceLocation getEntityTexture(Entity entity)
    {
		return skin;
    }
}
