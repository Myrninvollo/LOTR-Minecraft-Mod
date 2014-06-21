package lotr.common.entity.npc;

import lotr.common.LOTRAlignmentValues;
import lotr.common.LOTRMod;
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
		int i = rand.nextInt(4);
		
		if (i == 0 || i == 1)
		{
			setCurrentItemOrArmor(0, new ItemStack(LOTRMod.swordDwarven));
		}
		else if (i == 2)
		{
			setCurrentItemOrArmor(0, new ItemStack(LOTRMod.battleaxeDwarven));
		}
		else if (i == 3)
		{
			setCurrentItemOrArmor(0, new ItemStack(LOTRMod.hammerDwarven));
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
	public void onDwarfUpdate() {}
	
	@Override
	public int getAlignmentBonus()
	{
		return LOTRAlignmentValues.DWARF_WARRIOR_BONUS;
	}
}