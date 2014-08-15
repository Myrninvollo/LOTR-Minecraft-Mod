package lotr.common.entity.npc;

import lotr.common.*;
import net.minecraft.item.ItemStack;
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
		if (rand.nextBoolean())
		{
			setCurrentItemOrArmor(4, new ItemStack(LOTRMod.wargArmorUruk));
		}
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
