package lotr.client.fx;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.client.particle.EntityDiggingFX;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class LOTREntityMTCSpawnFX extends EntityDiggingFX
{
	private static Random blockRand = new Random();
	
	public LOTREntityMTCSpawnFX(World world, double d, double d1, double d2, double d3, double d4, double d5)
	{
		super(world, d, d1, d2, d3, d4, d5, getRandomBlock(), 0);
		particleScale *= 2F;
	}
	
	private static Block getRandomBlock()
	{
		if (blockRand.nextBoolean())
		{
			return Blocks.stone;
		}
		else if (blockRand.nextBoolean())
		{
			return Blocks.dirt;
		}
		else if (blockRand.nextBoolean())
		{
			return Blocks.gravel;
		}
		else
		{
			return Blocks.sand;
		}
	}
}
