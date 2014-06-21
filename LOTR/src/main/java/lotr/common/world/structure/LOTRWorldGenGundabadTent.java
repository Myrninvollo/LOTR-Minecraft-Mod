package lotr.common.world.structure;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class LOTRWorldGenGundabadTent extends LOTRWorldGenTentBase
{
	public LOTRWorldGenGundabadTent(boolean flag)
	{
		super(flag);
		tentBlock = Blocks.wool;
		tentMeta = 15;
		floorBlock = Blocks.grass;
		floorMeta = 0;
		chestContents = LOTRChestContents.GUNDABAD_TENT;
	}
	
	@Override
	protected boolean canTentGenerateAt(World world, int i, int j, int k)
	{
		return world.getBlock(i, j - 1, k) == Blocks.grass;
	}
}
