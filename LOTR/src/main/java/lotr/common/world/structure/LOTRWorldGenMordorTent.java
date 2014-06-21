package lotr.common.world.structure;

import lotr.common.LOTRMod;
import lotr.common.world.biome.LOTRBiomeGenMordor;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class LOTRWorldGenMordorTent extends LOTRWorldGenTentBase
{
	public LOTRWorldGenMordorTent(boolean flag)
	{
		super(flag);
		tentBlock = Blocks.wool;
		tentMeta = 12;
		floorBlock = LOTRMod.rock;
		floorMeta = 0;
		chestContents = LOTRChestContents.ORC_TENT;
	}
	
	@Override
	protected boolean canTentGenerateAt(World world, int i, int j, int k)
	{
		return world.getBiomeGenForCoords(i, k) instanceof LOTRBiomeGenMordor && world.getBlock(i, j - 1, k) == LOTRMod.rock && world.getBlockMetadata(i, j - 1, k) == 0;
	}
}
