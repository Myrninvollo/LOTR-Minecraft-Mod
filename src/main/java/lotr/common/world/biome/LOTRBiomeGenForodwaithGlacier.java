package lotr.common.world.biome;

import net.minecraft.init.Blocks;

public class LOTRBiomeGenForodwaithGlacier extends LOTRBiomeGenForodwaithMountains
{
	public LOTRBiomeGenForodwaithGlacier(int i)
	{
		super(i);
		
		topBlock = Blocks.ice;
		fillerBlock = Blocks.ice;
	}
}
