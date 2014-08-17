package lotr.common.entity.animal;

import lotr.common.entity.npc.LOTREntityScorpion;
import lotr.common.world.biome.LOTRBiomeGenNearHarad;
import net.minecraft.world.World;

public class LOTREntityDesertScorpion extends LOTREntityScorpion implements LOTRBiomeGenNearHarad.ImmuneToHeat
{
	public LOTREntityDesertScorpion(World world)
	{
		super(world);
	}
	
	@Override
	protected int getRandomScorpionScale()
	{
		return rand.nextInt(2);
	}
	
	@Override
	public boolean getCanSpawnHere()
	{
		return super.getCanSpawnHere() && (posY < 60D || rand.nextInt(20) == 0);
	}
}
