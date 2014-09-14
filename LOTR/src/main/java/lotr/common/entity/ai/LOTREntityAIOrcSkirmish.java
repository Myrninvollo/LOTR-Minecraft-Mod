package lotr.common.entity.ai;

import java.util.List;

import lotr.common.entity.npc.LOTREntityOrc;
import net.minecraft.entity.EntityLivingBase;

public class LOTREntityAIOrcSkirmish extends LOTREntityAINearestAttackableTargetBasic
{
	private LOTREntityOrc theOrc;
	
    public LOTREntityAIOrcSkirmish(LOTREntityOrc orc, boolean flag)
    {
        super(orc, LOTREntityOrc.class, 0, flag, null);
		theOrc = orc;
    }
	
    @Override
    public boolean shouldExecute()
    {
		/*if (!theOrc.worldObj.getGameRules().getGameRuleBooleanValue("enableOrcSkirmish"))
		{
			return false;
		}*/
		if (!canOrcSkirmish(theOrc))
		{
			return false;
		}
		if (!theOrc.isOrcSkirmishing())
		{
			int chance = 20000;
			List nearbyOrcs = theOrc.worldObj.getEntitiesWithinAABB(LOTREntityOrc.class, theOrc.boundingBox.expand(16D, 8D, 16D));
			for (int i = 0; i < nearbyOrcs.size(); i++)
			{
				LOTREntityOrc orc = (LOTREntityOrc)nearbyOrcs.get(i);
				if (orc.isOrcSkirmishing())
				{
					chance /= 10;
				}
			}
			if (chance < 40)
			{
				chance = 40;
			}
			if (theOrc.getRNG().nextInt(chance) != 0)
			{
				return false;
			}
		}
		return super.shouldExecute();
	}
	
	@Override
    protected boolean isSuitableTarget(EntityLivingBase entity, boolean flag)
    {
		return canOrcSkirmish(entity) && super.isSuitableTarget(entity, flag);
	}
	
	private boolean canOrcSkirmish(EntityLivingBase entity)
	{
		if (entity instanceof LOTREntityOrc)
		{
			LOTREntityOrc orc = (LOTREntityOrc)entity;
			return !orc.isTrader() && !orc.hiredNPCInfo.isActive && orc.ridingEntity == null && orc.canOrcSkirmish();
		}
		return false;
	}
	
	@Override
    public void startExecuting()
	{
		super.startExecuting();
		theOrc.setOrcSkirmishing();
	}
}
