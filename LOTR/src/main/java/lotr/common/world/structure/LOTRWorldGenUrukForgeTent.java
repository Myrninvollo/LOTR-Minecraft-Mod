package lotr.common.world.structure;

import lotr.common.LOTRMod;

public class LOTRWorldGenUrukForgeTent extends LOTRWorldGenUrukTent
{
	public LOTRWorldGenUrukForgeTent(boolean flag)
	{
		super(flag);
		tentBlock = LOTRMod.brick2;
		tentMeta = 7;
		supportsBlock = LOTRMod.wall2;
		supportsMeta = 7;
		hasOrcForge = true;
	}
}
