package lotr.common.entity.npc;

import lotr.common.LOTRAlignmentValues;
import lotr.common.LOTRFaction;
import net.minecraft.world.World;

public class LOTREntityGundabadWarg extends LOTREntityWarg
{
	public LOTREntityGundabadWarg(World world)
	{
		super(world);
	}
	
	@Override
	public LOTREntityNPC createWargRider()
	{
		return worldObj.rand.nextBoolean() ? new LOTREntityGundabadOrcArcher(worldObj) : new LOTREntityGundabadOrc(worldObj);
	}
	
	@Override
	public LOTRFaction getFaction()
	{
		return LOTRFaction.GUNDABAD;
	}
	
	@Override
	public int getAlignmentBonus()
	{
		return LOTRAlignmentValues.GUNDABAD_WARG_BONUS;
	}
}
