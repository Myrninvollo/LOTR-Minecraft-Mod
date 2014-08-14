package lotr.client.render.entity;

import lotr.common.LOTRShields;

public class LOTRRenderGondorianCaptain extends LOTRRenderGondorSoldier
{
	public LOTRRenderGondorianCaptain()
	{
		super();
		setCapeTexture(LOTRShields.ALIGNMENT_GONDOR.capeTexture);
	}
}
