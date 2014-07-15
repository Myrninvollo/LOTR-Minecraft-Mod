package lotr.common.entity.npc;

import lotr.common.LOTRAlignmentValues;
import lotr.common.LOTRMod;
import lotr.common.entity.ai.LOTREntityAIHiringPlayerHurtByTarget;
import lotr.common.entity.ai.LOTREntityAIHiringPlayerHurtTarget;
import lotr.common.entity.npc.LOTREntityNPC.AttackMode;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTREntityNearHaradrimWarrior extends LOTREntityNearHaradrim
{
	public LOTREntityNearHaradrimWarrior(World world)
	{
		super(world);
		targetTasks.taskEntries.clear();
		targetTasks.addTask(1, new LOTREntityAIHiringPlayerHurtByTarget(this));
        targetTasks.addTask(2, new LOTREntityAIHiringPlayerHurtTarget(this));
        targetTasks.addTask(3, new EntityAIHurtByTarget(this, false));
        addTargetTasks(4);
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
	public void onAttackModeChange(AttackMode mode) {}
}
