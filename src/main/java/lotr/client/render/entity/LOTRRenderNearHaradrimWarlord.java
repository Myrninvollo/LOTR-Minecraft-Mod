package lotr.client.render.entity;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import lotr.common.LOTRCapes;
import lotr.common.entity.npc.LOTREntityNearHaradrim;

public class LOTRRenderNearHaradrimWarlord extends LOTRRenderNearHaradrim
{
	private static ResourceLocation skin = new ResourceLocation("lotr:mob/nearHarad/warlord.png");
	
	public LOTRRenderNearHaradrimWarlord()
	{
		super();
		setCapeTexture(LOTRCapes.ALIGNMENT_NEAR_HARAD.capeTexture);
	}
	
	@Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
		return skin;
    }
}
