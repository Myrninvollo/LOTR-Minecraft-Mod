package lotr.common.world.structure;

import lotr.common.LOTRMod;
import lotr.common.entity.npc.LOTREntityBlueDwarf;
import lotr.common.entity.npc.LOTREntityDwarf;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class LOTRWorldGenBlueMountainsHouse extends LOTRWorldGenDwarfHouse
{
	public LOTRWorldGenBlueMountainsHouse(boolean flag)
	{
		super(flag);
		
		stoneBlock = Blocks.stone;
		stoneMeta = 0;
		fillerBlock = LOTRMod.rock;
		fillerMeta = 3;
		topBlock = LOTRMod.rock;
		topMeta = 3;
		
		brick2Block = LOTRMod.brick;
		brick2Meta = 14;
		pillarBlock = LOTRMod.pillar;
		pillarMeta = 3;
		chandelierMeta = 11;
		tableBlock = LOTRMod.blueDwarvenTable;
	}
	
	@Override
	protected LOTREntityDwarf createDwarf(World world)
	{
		return new LOTREntityBlueDwarf(world);
	}
}
