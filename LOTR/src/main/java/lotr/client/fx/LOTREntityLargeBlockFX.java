package lotr.client.fx;

import net.minecraft.block.Block;
import net.minecraft.client.particle.EntityDiggingFX;
import net.minecraft.world.World;

public class LOTREntityLargeBlockFX extends EntityDiggingFX
{
	public LOTREntityLargeBlockFX(World world, double d, double d1, double d2, double d3, double d4, double d5, Block block, int meta)
	{
		super(world, d, d1, d2, d3, d4, d5, block, meta);
		particleScale *= 4F;
	}
}
