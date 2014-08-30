package lotr.common.entity.ai;

import lotr.common.LOTRLevelData;
import lotr.common.LOTRMod;
import lotr.common.entity.npc.*;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.player.EntityPlayer;

public class LOTREntityAINearestAttackableTargetBasic extends EntityAINearestAttackableTarget
{
    public LOTREntityAINearestAttackableTargetBasic(EntityCreature entity, Class targetClass, int chance, boolean flag)
    {
        super(entity, targetClass, chance, flag);
    }
	
    public LOTREntityAINearestAttackableTargetBasic(EntityCreature entity, Class targetClass, int chance, boolean flag, IEntitySelector selector)
    {
        super(entity, targetClass, chance, flag, false, selector);
    }
	
    @Override
    public boolean shouldExecute()
    {
		if (taskOwner instanceof LOTREntityNPC)
		{
			LOTREntityNPC npc = (LOTREntityNPC)taskOwner;
			if (npc.hiredNPCInfo.isActive && npc.hiredNPCInfo.isHalted())
			{
				return false;
			}
		}
		if (taskOwner instanceof LOTREntityNPCRideable)
		{
			LOTREntityNPCRideable rideable = (LOTREntityNPCRideable)taskOwner;
			if (rideable.isNPCTamed() || rideable.riddenByEntity instanceof EntityPlayer)
			{
				return false;
			}
		}
		return super.shouldExecute();
	}
	
	@Override
    protected boolean isSuitableTarget(EntityLivingBase entity, boolean flag)
    {
		if (entity == taskOwner.ridingEntity || entity == taskOwner.riddenByEntity)
		{
			return false;
		}
		
		if (super.isSuitableTarget(entity, flag))
		{
			if (entity instanceof EntityPlayer)
			{
				return isPlayerSuitableTarget((EntityPlayer)entity);
			}
			if (entity instanceof LOTREntityBandit)
			{
				return taskOwner instanceof LOTREntityNPC && ((LOTREntityNPC)taskOwner).hiredNPCInfo.isActive;
			}
			return true;
		}
		else
		{
			return false;
		}
	}
	
	protected boolean isPlayerSuitableTarget(EntityPlayer entityplayer)
	{
		int alignment = LOTRLevelData.getData(entityplayer).getAlignment(LOTRMod.getNPCFaction(taskOwner));
		return alignment < 0;
	}
}
