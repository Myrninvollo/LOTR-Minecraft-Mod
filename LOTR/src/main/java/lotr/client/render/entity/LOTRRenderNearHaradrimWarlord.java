package lotr.client.render.entity;

import lotr.common.LOTRShields;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class LOTRRenderNearHaradrimWarlord extends LOTRRenderNearHaradrim
{
	private static ResourceLocation skin = new ResourceLocation("lotr:mob/nearHarad/warlord.png");
	
	public LOTRRenderNearHaradrimWarlord()
	{
		super();
		setCapeTexture(LOTRShields.ALIGNMENT_NEAR_HARAD.capeTexture);
	}
	
	@Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
		return skin;
    }
}
