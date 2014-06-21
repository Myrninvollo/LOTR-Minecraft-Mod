package lotr.common.entity.npc;

import lotr.common.LOTRAlignmentValues;
import lotr.common.LOTRFaction;
import net.minecraft.world.World;

public class LOTREntityMordorWarg extends LOTREntityWarg
{
	public LOTREntityMordorWarg(World world)
	{
		super(world);
	}
	
	@Override
	protected void entityInit()
	{
		super.entityInit();
		setWargType(2);
	}
	
	@Override
	public LOTREntityNPC createWargRider()
	{
		return worldObj.rand.nextBoolean() ? new LOTREntityMordorOrcArcher(worldObj) : new LOTREntityMordorOrc(worldObj);
	}
	
	@Override
	public LOTRFaction getFaction()
	{
		return LOTRFaction.MORDOR;
	}
	
	@Override
	public int getAlignmentBonus()
	{
		return LOTRAlignmentValues.MORDOR_WARG_BONUS;
	}
}
