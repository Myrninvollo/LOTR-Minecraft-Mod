package lotr.common.world.structure;

import net.minecraft.init.Blocks;

public class LOTRWorldGenGundabadForgeTent extends LOTRWorldGenGundabadTent
{
	public LOTRWorldGenGundabadForgeTent(boolean flag)
	{
		super(flag);
		tentBlock = Blocks.cobblestone;
		tentMeta = 0;
		supportsBlock = Blocks.cobblestone_wall;
		supportsMeta = 0;
		hasOrcForge = true;
	}
}
