package lotr.common.world.structure;

import lotr.common.LOTRMod;
import lotr.common.entity.npc.*;
import lotr.common.world.biome.LOTRBiomeGenMordor;
import net.minecraft.world.World;

public class LOTRWorldGenMordorWargPit extends LOTRWorldGenWargPitBase
{
	public LOTRWorldGenMordorWargPit(boolean flag)
	{
		super(flag);
		wallBlock = LOTRMod.brick;
		wallMeta = 0;
		groundBlock = LOTRMod.rock;
		groundMeta = 0;
	}
	
	@Override
	protected boolean canGenerateAt(World world, int i, int j, int k)
	{
		return world.getBlock(i, j - 1, k) == LOTRMod.rock && world.getBlockMetadata(i, j - 1, k) == 0 && world.getBiomeGenForCoords(i, k) instanceof LOTRBiomeGenMordor;
	}
	
	@Override
	protected LOTREntityNPC getOrc(World world)
	{
		return new LOTREntityMordorOrc(world);
	}

	@Override
	protected LOTREntityNPC getWarg(World world)
	{
		return new LOTREntityMordorWarg(world);
	}
}
