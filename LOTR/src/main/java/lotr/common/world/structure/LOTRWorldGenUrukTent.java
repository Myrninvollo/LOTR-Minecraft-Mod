package lotr.common.world.structure;

import lotr.common.world.biome.LOTRBiome;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class LOTRWorldGenUrukTent extends LOTRWorldGenTentBase
{
	public LOTRWorldGenUrukTent(boolean flag)
	{
		super(flag);
		tentBlock = Blocks.wool;
		tentMeta = 15;
		floorBlock = Blocks.grass;
		floorMeta = 0;
		chestContents = LOTRChestContents.URUK_TENT;
	}
	
	@Override
	protected boolean canTentGenerateAt(World world, int i, int j, int k)
	{
		return world.getBiomeGenForCoords(i, k) == LOTRBiome.rohanUrukHighlands && world.getBlock(i, j - 1, k) == Blocks.grass;
	}
}
