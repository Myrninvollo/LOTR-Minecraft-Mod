package lotr.common.entity.npc;

import lotr.common.*;
import net.minecraft.item.ItemStack;
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
		if (rand.nextBoolean())
		{
			setWargArmor(new ItemStack(LOTRMod.wargArmorAngmar));
		}
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
		return LOTRAlignmentValues.Bonuses.ANGMAR_WARG;
	}
}
