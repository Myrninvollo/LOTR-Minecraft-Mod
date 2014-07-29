package lotr.common.entity.npc;

import lotr.common.LOTRAlignmentValues;
import lotr.common.LOTRMod;
import lotr.common.entity.npc.LOTREntityNPC.AttackMode;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTREntityDwarfWarrior extends LOTREntityDwarf
{
	public LOTREntityDwarfWarrior(World world)
	{
		super(world);
	}
	
	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData data)
	{
		data = super.onSpawnWithEgg(data);
		int i = rand.nextInt(6);
		
		if (i == 0)
		{
			setCurrentItemOrArmor(0, new ItemStack(LOTRMod.swordDwarven));
		}
		else if (i == 1 || i == 2)
		{
			setCurrentItemOrArmor(0, new ItemStack(LOTRMod.battleaxeDwarven));
		}
		else if (i == 3 || i == 4)
		{
			setCurrentItemOrArmor(0, new ItemStack(LOTRMod.hammerDwarven));
		}
		else if (i == 5)
		{
			setCurrentItemOrArmor(0, new ItemStack(LOTRMod.spearDwarven));
		}
		
		setCurrentItemOrArmor(1, new ItemStack(LOTRMod.bootsDwarven));
		setCurrentItemOrArmor(2, new ItemStack(LOTRMod.legsDwarven));
		setCurrentItemOrArmor(3, new ItemStack(LOTRMod.bodyDwarven));
		
		if (rand.nextInt(10) != 0)
		{
			setCurrentItemOrArmor(4, new ItemStack(LOTRMod.helmetDwarven));
		}
		
		return data;
	}
	
	@Override
	public void onAttackModeChange(AttackMode mode) {}
	
	@Override
	public int getAlignmentBonus()
	{
		return LOTRAlignmentValues.DWARF_WARRIOR_BONUS;
	}
}
