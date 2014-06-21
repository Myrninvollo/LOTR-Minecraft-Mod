package lotr.common.entity.npc;

import lotr.common.LOTRAlignmentValues;
import lotr.common.LOTRMod;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTREntityNearHaradrimWarrior extends LOTREntityNearHaradrim
{
	public LOTREntityNearHaradrimWarrior(World world)
	{
		super(world);
		spawnRidingHorse = rand.nextInt(6) == 0;
	}
	
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(24D);
    }
	
	@Override
    public IEntityLivingData onSpawnWithEgg(IEntityLivingData data)
    {
		data = super.onSpawnWithEgg(data);
		
		int i = rand.nextInt(5);
		if (i == 0)
		{
			setCurrentItemOrArmor(0, new ItemStack(LOTRMod.daggerNearHarad));
		}
		else if (i == 1)
		{
			setCurrentItemOrArmor(0, new ItemStack(LOTRMod.spearNearHarad));
		}
		else
		{
			setCurrentItemOrArmor(0, new ItemStack(LOTRMod.scimitarNearHarad));
		}
		
		setCurrentItemOrArmor(1, new ItemStack(LOTRMod.bootsNearHarad));
		setCurrentItemOrArmor(2, new ItemStack(LOTRMod.legsNearHarad));
		setCurrentItemOrArmor(3, new ItemStack(LOTRMod.bodyNearHarad));
		setCurrentItemOrArmor(4, new ItemStack(LOTRMod.helmetNearHarad));
		return data;
    }
	
	@Override
	public int getAlignmentBonus()
	{
		return LOTRAlignmentValues.NEAR_HARADRIM_WARRIOR_BONUS;
	}
	
	@Override
	protected void dropHaradrimItems(boolean flag, int i) {}
}
