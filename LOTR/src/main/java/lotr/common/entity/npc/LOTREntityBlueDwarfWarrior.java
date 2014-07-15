package lotr.common.entity.npc;

import lotr.common.LOTRAlignmentValues;
import lotr.common.LOTRMod;
import lotr.common.entity.npc.LOTREntityNPC.AttackMode;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTREntityBlueDwarfWarrior extends LOTREntityBlueDwarf
{
	public LOTREntityBlueDwarfWarrior(World world)
	{
		super(world);
	}
	
	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData data)
	{
		data = super.onSpawnWithEgg(data);
		int i = rand.nextInt(4);
		
		if (i == 0 || i == 1)
		{
			setCurrentItemOrArmor(0, new ItemStack(LOTRMod.swordBlueDwarven));
		}
		else if (i == 2)
		{
			setCurrentItemOrArmor(0, new ItemStack(LOTRMod.battleaxeBlueDwarven));
		}
		else if (i == 3)
		{
			setCurrentItemOrArmor(0, new ItemStack(LOTRMod.hammerBlueDwarven));
		}
		
		setCurrentItemOrArmor(1, new ItemStack(LOTRMod.bootsBlueDwarven));
		setCurrentItemOrArmor(2, new ItemStack(LOTRMod.legsBlueDwarven));
		setCurrentItemOrArmor(3, new ItemStack(LOTRMod.bodyBlueDwarven));
		
		if (rand.nextInt(10) != 0)
		{
			setCurrentItemOrArmor(4, new ItemStack(LOTRMod.helmetBlueDwarven));
		}
		
		return data;
	}
	
	@Override
	public void onAttackModeChange(AttackMode mode) {}
	
	@Override
	public int getAlignmentBonus()
	{
		return LOTRAlignmentValues.BLUE_DWARF_WARRIOR_BONUS;
	}
}
