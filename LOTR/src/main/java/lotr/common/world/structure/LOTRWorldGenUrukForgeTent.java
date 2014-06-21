package lotr.common.world.structure;

import net.minecraft.init.Blocks;

public class LOTRWorldGenUrukForgeTent extends LOTRWorldGenUrukTent
{
	public LOTRWorldGenUrukForgeTent(boolean flag)
	{
		super(flag);
		tentBlock = Blocks.cobblestone;
		tentMeta = 0;
		supportsBlock = Blocks.cobblestone_wall;
		supportsMeta = 0;
		hasOrcForge = true;
	}
}
