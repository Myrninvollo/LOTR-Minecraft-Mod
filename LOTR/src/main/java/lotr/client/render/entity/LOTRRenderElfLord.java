package lotr.client.render.entity;

import lotr.common.LOTRShields;

public class LOTRRenderElfLord extends LOTRRenderElf
{
	public LOTRRenderElfLord()
	{
		super();
		setCapeTexture(LOTRShields.ALIGNMENT_GALADHRIM.capeTexture);
	}
}
