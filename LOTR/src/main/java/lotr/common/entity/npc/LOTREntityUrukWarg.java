package lotr.common.entity.npc;

import lotr.common.LOTRAlignmentValues;
import lotr.common.LOTRFaction;
import net.minecraft.world.World;

public class LOTREntityUrukWarg extends LOTREntityWarg
{
	public LOTREntityUrukWarg(World world)
	{
		super(world);
	}
	
	@Override
	public LOTREntityNPC createWargRider()
	{
		return worldObj.rand.nextBoolean() ? new LOTREntityUrukHaiCrossbower(worldObj) : new LOTREntityUrukHai(worldObj);
	}
	
	@Override
	public LOTRFaction getFaction()
	{
		return LOTRFaction.URUK_HAI;
	}
	
	@Override
	public int getAlignmentBonus()
	{
		return LOTRAlignmentValues.URUK_WARG_BONUS;
	}
}
