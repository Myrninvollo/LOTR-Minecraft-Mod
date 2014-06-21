package lotr.common.world.structure;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class LOTRWorldGenRangerTent extends LOTRWorldGenTentBase
{
	public LOTRWorldGenRangerTent(boolean flag)
	{
		super(flag);
		tentBlock = Blocks.wool;
		tentMeta = 13;
		floorBlock = Blocks.grass;
		floorMeta = 0;
		chestContents = LOTRChestContents.RANGER_TENT;
	}
	
	@Override
	public boolean isOrcTent()
	{
		return false;
	}
	
	@Override
	protected boolean canTentGenerateAt(World world, int i, int j, int k)
	{
		return world.getBlock(i, j - 1, k) == Blocks.grass;
	}
}
