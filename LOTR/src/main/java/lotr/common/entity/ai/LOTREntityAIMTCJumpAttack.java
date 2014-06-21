package lotr.common.entity.ai;

import lotr.common.entity.npc.LOTREntityMountainTrollChieftain;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.world.World;

public class LOTREntityAIMTCJumpAttack extends EntityAIBase
{
	private World theWorld;
	private LOTREntityMountainTrollChieftain theTroll;
	
	public LOTREntityAIMTCJumpAttack(LOTREntityMountainTrollChieftain troll)
	{
		theTroll = troll;
		theWorld = troll.worldObj;
		setMutexBits(3);
	}
	
    @Override
    public boolean shouldExecute()
    {
		if (theTroll.getAttackTarget() == null)
		{
			return false;
		}
		if (theTroll.jumpAttack)
		{
			return false;
		}
		else if (theTroll.getRNG().nextInt(20) == 0)
		{
			int enemies = theTroll.getNearbyEnemies().size();
			float f = theTroll.getHealthChanceModifier();
			f *= 0.05F;
			if (enemies > 1F)
			{
				f *= (float)enemies * 4F;
			}
			f *= theTroll.getArmorLevelChanceModifier();
			float distance = (float)theTroll.getDistanceToEntity(theTroll.getAttackTarget());
			distance = 8F - distance;
			distance /= 2F;
			if (distance > 1F)
			{
				f *= distance;
			}
			return theTroll.getRNG().nextFloat() < f;
		}
		return false;
	}
	
    @Override
    public void startExecuting()
    {
		theTroll.doJumpAttack();
	}
}
