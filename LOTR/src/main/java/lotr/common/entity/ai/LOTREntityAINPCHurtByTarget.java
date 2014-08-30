package lotr.common.entity.ai;

import lotr.common.entity.npc.LOTREntityNPC;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;

public class LOTREntityAINPCHurtByTarget extends EntityAIHurtByTarget
{
	public LOTREntityAINPCHurtByTarget(LOTREntityNPC npc, boolean flag)
	{
		super(npc, flag);
	}

	@Override
    protected boolean isSuitableTarget(EntityLivingBase entity, boolean flag)
    {
		if (entity == taskOwner.ridingEntity || entity == taskOwner.riddenByEntity)
		{
			return false;
		}
		
		int homeX = taskOwner.getHomePosition().posX;
		int homeY = taskOwner.getHomePosition().posY;
		int homeZ = taskOwner.getHomePosition().posZ;
		int homeRange = (int)taskOwner.func_110174_bM();
		taskOwner.detachHome();
		
		boolean superSuitable = super.isSuitableTarget(entity, flag);
		
		taskOwner.setHomeArea(homeX, homeY, homeZ, homeRange);
		return superSuitable;
	}
}
