package lotr.common.entity.ai;

import java.util.List;

import lotr.common.LOTRLevelData;
import lotr.common.LOTRMod;
import lotr.common.entity.npc.LOTREntitySauron;
import lotr.common.item.LOTRItemSauronMace;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class LOTREntityAISauronUseMace extends EntityAIBase
{
	private LOTREntitySauron theSauron;
	private int attackTick = 0;
	private World theWorld;
	
	public LOTREntityAISauronUseMace(LOTREntitySauron sauron)
	{
		theSauron = sauron;
		theWorld = theSauron.worldObj;
		setMutexBits(3);
	}
	
	@Override
    public boolean shouldExecute()
    {
		int targets = 0;
		List nearbyEntities = theWorld.getEntitiesWithinAABB(EntityLivingBase.class, theSauron.boundingBox.expand(6D, 6D, 6D));
		for (int i = 0; i < nearbyEntities.size(); i++)
		{
			EntityLivingBase entity = (EntityLivingBase)nearbyEntities.get(i);
			if (entity.isEntityAlive())
			{
				if (entity instanceof EntityPlayer)
				{
					EntityPlayer entityplayer = (EntityPlayer)entity;
					if (!entityplayer.capabilities.isCreativeMode && (LOTRLevelData.getData(entityplayer).getAlignment(theSauron.getFaction()) < 0 || theSauron.getAttackTarget() == entityplayer))
					{
						targets++;
					}
				}
				else if (LOTRMod.getNPCFaction(entity).isEnemy(theSauron.getFaction()))
				{
					targets++;
				}
				else if (theSauron.getAttackTarget() == entity || (entity instanceof EntityLiving && ((EntityLiving)entity).getAttackTarget() == theSauron))
				{
					targets++;
				}
			}
		}
		if (targets >= 4)
		{
			return true;
		}
		else if (targets > 0 && theSauron.getRNG().nextInt(100) == 0)
		{
			return true;
		}
		return false;
    }
	
	@Override
    public boolean continueExecuting()
    {
		return theSauron.getIsUsingMace();
    }
	
	@Override
    public void startExecuting()
    {
        attackTick = 40;
		theSauron.setIsUsingMace(true);
    }

    @Override
    public void resetTask()
    {
        attackTick = 40;
		theSauron.setIsUsingMace(false);
    }
	
	@Override
    public void updateTask()
    {
        attackTick = Math.max(attackTick - 1, 0);
        if (attackTick <= 0)
		{
			attackTick = 40;
			LOTRItemSauronMace.useSauronMace(theSauron.getEquipmentInSlot(0), theWorld, theSauron);
			theSauron.setIsUsingMace(false);
		}
    }
}
