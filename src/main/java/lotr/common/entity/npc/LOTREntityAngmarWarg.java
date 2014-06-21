package lotr.common.entity.npc;

import lotr.common.LOTRAlignmentValues;
import lotr.common.LOTRFaction;
import net.minecraft.world.World;

public class LOTREntityAngmarWarg extends LOTREntityWarg
{
	public LOTREntityAngmarWarg(World world)
	{
		super(world);
	}
	
	@Override
	public LOTREntityNPC createWargRider()
	{
		if (worldObj.rand.nextInt(3) == 0)
		{
			return new LOTREntityAngmarOrcWarrior(worldObj);
		}
		return worldObj.rand.nextBoolean() ? new LOTREntityAngmarOrcArcher(worldObj) : new LOTREntityAngmarOrc(worldObj);
	}
	
	@Override
	public LOTRFaction getFaction()
	{
		return LOTRFaction.ANGMAR;
	}
	
	@Override
	public int getAlignmentBonus()
	{
		return LOTRAlignmentValues.ANGMAR_WARG_BONUS;
	}
}
