package lotr.common.entity.ai;

import java.util.Random;

import lotr.common.entity.npc.LOTREntityNPC;
import lotr.common.entity.npc.LOTRNPCMount;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.item.ItemBow;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class LOTREntityAIHorseMoveToRiderTarget extends EntityAIBase
{
	private LOTRNPCMount theHorse;
	private EntityCreature livingHorse;
    private double speed;
    private PathEntity entityPathEntity;
    private int pathCheckTimer;

    public LOTREntityAIHorseMoveToRiderTarget(LOTRNPCMount horse)
    {
        theHorse = horse;
		livingHorse = (EntityCreature)theHorse;
		setMutexBits(3);
    }

    @Override
    public boolean shouldExecute()
    {
		if (!theHorse.getBelongsToNPC())
		{
			return false;
		}
		Entity rider = livingHorse.riddenByEntity;
		if (rider == null || !rider.isEntityAlive() || !(rider instanceof LOTREntityNPC))
		{
			return false;
		}
		
        EntityLivingBase riderTarget = ((LOTREntityNPC)rider).getAttackTarget();
        if (riderTarget == null || !riderTarget.isEntityAlive())
        {
            return false;
        }
        else
        {
            entityPathEntity = livingHorse.getNavigator().getPathToEntityLiving(riderTarget);
            return entityPathEntity != null;
        }
    }

    @Override
    public boolean continueExecuting()
    {
		if (livingHorse.riddenByEntity == null || !livingHorse.riddenByEntity.isEntityAlive() || !(livingHorse.riddenByEntity instanceof LOTREntityNPC))
		{
			return false;
		}
		LOTREntityNPC rider = ((LOTREntityNPC)livingHorse.riddenByEntity);
        EntityLivingBase riderTarget = rider.getAttackTarget();
		return riderTarget != null && riderTarget.isEntityAlive() && !livingHorse.getNavigator().noPath();
    }

    @Override
    public void startExecuting()
    {
		speed = ((LOTREntityNPC)livingHorse.riddenByEntity).getEntityAttribute(LOTREntityNPC.horseAttackSpeed).getAttributeValue();
        livingHorse.getNavigator().setPath(entityPathEntity, speed);
        pathCheckTimer = 0;
    }

    @Override
    public void resetTask()
    {
        livingHorse.getNavigator().clearPathEntity();
    }

    @Override
    public void updateTask()
    {
		LOTREntityNPC rider = (LOTREntityNPC)livingHorse.riddenByEntity;
        EntityLivingBase riderTarget = rider.getAttackTarget();
		boolean aimingBow = rider.getEquipmentInSlot(0) != null && rider.getEquipmentInSlot(0).getItem() instanceof ItemBow && rider.getDistanceSqToEntity(riderTarget) < 100D && livingHorse.getEntitySenses().canSee(riderTarget);
		
		if (!aimingBow)
		{
			livingHorse.getLookHelper().setLookPositionWithEntity(riderTarget, 30F, 30F);
			rider.rotationYaw = livingHorse.rotationYaw;
			rider.rotationYawHead = livingHorse.rotationYawHead;
		}

        if (--pathCheckTimer <= 0)
        {
            pathCheckTimer = 4 + livingHorse.getRNG().nextInt(7);
            livingHorse.getNavigator().tryMoveToEntityLiving(riderTarget, speed);
        }
		
		if (aimingBow)
		{
			if (rider.getDistanceSqToEntity(riderTarget) < 16D)
			{
				Vec3 vec = findPositionAwayFrom(rider, riderTarget, 4, 16);
				if (vec != null)
				{
					livingHorse.getNavigator().tryMoveToXYZ(vec.xCoord, vec.yCoord, vec.zCoord, speed);
				}
			}
			else
			{
				livingHorse.getNavigator().clearPathEntity();
			}
		}
    }
	
    private Vec3 findPositionAwayFrom(EntityLivingBase rider, EntityLivingBase target, int min, int max)
    {
        Random random = rider.getRNG();
        for (int l = 0; l < 24; l++)
        {
            int i = MathHelper.floor_double(rider.posX) - max + random.nextInt(max * 2 + 1);
            int j = MathHelper.floor_double(rider.boundingBox.minY) - 4 + random.nextInt(9);
            int k = MathHelper.floor_double(rider.posZ) - max + random.nextInt(max * 2 + 1);

			double d = target.getDistanceSq(i, j, k);
            if (d > (double)(min * min) && d < (double)(max * max))
            {
                return Vec3.createVectorHelper((double)i, (double)j, (double)k);
            }
        }

        return null;
    }
}
