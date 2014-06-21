package lotr.common.world.structure;

import lotr.common.LOTRMod;

public class LOTRWorldGenMordorForgeTent extends LOTRWorldGenMordorTent
{
	public LOTRWorldGenMordorForgeTent(boolean flag)
	{
		super(flag);
		tentBlock = LOTRMod.brick;
		tentMeta = 0;
		supportsBlock = LOTRMod.wall;
		supportsMeta = 1;
		hasOrcForge = true;
	}
}
