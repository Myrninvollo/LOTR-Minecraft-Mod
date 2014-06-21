package lotr.common.world.structure;

import lotr.common.entity.npc.LOTREntityNPC;
import lotr.common.entity.npc.LOTREntityUrukHai;
import lotr.common.entity.npc.LOTREntityUrukWarg;
import lotr.common.world.biome.LOTRBiome;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class LOTRWorldGenUrukWargPit extends LOTRWorldGenWargPitBase
{
	public LOTRWorldGenUrukWargPit(boolean flag)
	{
		super(flag);
		wallBlock = Blocks.stonebrick;
		wallMeta = 0;
		groundBlock = Blocks.cobblestone;
		groundMeta = 0;
	}
	
	@Override
	protected boolean canGenerateAt(World world, int i, int j, int k)
	{
		return world.getBlock(i, j - 1, k) == Blocks.grass && world.getBiomeGenForCoords(i, k) == LOTRBiome.rohanUrukHighlands;
	}
	
	@Override
	protected LOTREntityNPC getOrc(World world)
	{
		return new LOTREntityUrukHai(world);
	}
	
	@Override
	protected LOTREntityNPC getWarg(World world)
	{
		return new LOTREntityUrukWarg(world);
	}
}
