package lotr.common.entity.ai;

import lotr.common.LOTRMod;
import lotr.common.entity.npc.LOTREntityBandit;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.player.EntityPlayer;

public class LOTREntityAINearestAttackableTargetBandit extends LOTREntityAINearestAttackableTargetBasic
{
    public LOTREntityAINearestAttackableTargetBandit(LOTREntityBandit entity, Class targetClass, int chance, boolean flag)
    {
        super(entity, targetClass, chance, flag);
    }
	
    public LOTREntityAINearestAttackableTargetBandit(LOTREntityBandit entity, Class targetClass, int chance, boolean flag, IEntitySelector selector)
    {
        super(entity, targetClass, chance, flag, selector);
    }
    
    @Override
    public boolean shouldExecute()
    {
    	LOTREntityBandit bandit = (LOTREntityBandit)taskOwner;
    	if (!LOTRMod.isInventoryEmpty(bandit.banditInventory))
    	{
    		return false;
    	}
    	return super.shouldExecute();
    }

	@Override
    protected boolean isPlayerSuitableTarget(EntityPlayer entityplayer)
    {
		LOTREntityBandit bandit = (LOTREntityBandit)taskOwner;
		if (!LOTRMod.isInventoryEmpty(entityplayer.inventory))
		{
			return false;
		}
		return super.isPlayerSuitableTarget(entityplayer);
	}
}
