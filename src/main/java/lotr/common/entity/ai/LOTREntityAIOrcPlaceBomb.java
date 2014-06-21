package lotr.common.entity.ai;

import lotr.common.LOTRMod;
import lotr.common.entity.item.LOTREntityOrcBomb;
import lotr.common.entity.npc.LOTREntityOrc;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.world.World;

public class LOTREntityAIOrcPlaceBomb extends EntityAIBase
{
    private World worldObj;
    private LOTREntityOrc attacker;
    private EntityLivingBase entityTarget;
    private double moveSpeed;
    private PathEntity entityPathEntity;
    private int rePathDelay;
	private Item weaponItem;

    public LOTREntityAIOrcPlaceBomb(LOTREntityOrc entity, double speed, Item item)
    {
        attacker = entity;
        worldObj = entity.worldObj;
        moveSpeed = speed;
		weaponItem = item;
        setMutexBits(3);
    }

    @Override
    public boolean shouldExecute()
    {
        EntityLivingBase entity = attacker.getAttackTarget();
        if (entity == null)
        {
            return false;
        }
		else if (attacker.getEquipmentInSlot(0) == null || attacker.getEquipmentInSlot(0).getItem() != LOTRMod.orcTorchItem)
		{
			return false;
		}
        else
        {
            entityTarget = entity;
            entityPathEntity = attacker.getNavigator().getPathToEntityLiving(entityTarget);
            return entityPathEntity != null;
        }
    }

    @Override
    public boolean continueExecuting()
    {
		if (attacker.getEquipmentInSlot(0) == null || attacker.getEquipmentInSlot(0).getItem() != LOTRMod.orcTorchItem)
		{
			return false;
		}
        EntityLivingBase entity = attacker.getAttackTarget();
        return entity == null ? false : (!entityTarget.isEntityAlive() ? false : !attacker.getNavigator().noPath());
    }

    @Override
    public void startExecuting()
    {
        attacker.getNavigator().setPath(entityPathEntity, moveSpeed);
        rePathDelay = 0;
    }

    @Override
    public void resetTask()
    {
        entityTarget = null;
        attacker.getNavigator().clearPathEntity();
    }

    @Override
    public void updateTask()
    {
        attacker.getLookHelper().setLookPositionWithEntity(entityTarget, 30.0F, 30.0F);
        if (attacker.getEntitySenses().canSee(entityTarget) && --rePathDelay <= 0)
        {
            rePathDelay = 4 + attacker.getRNG().nextInt(7);
            attacker.getNavigator().tryMoveToEntityLiving(entityTarget, moveSpeed);
        }
        if (attacker.getDistanceToEntity(entityTarget) < 3D)
        {
            LOTREntityOrcBomb bomb = new LOTREntityOrcBomb(worldObj, attacker.posX, attacker.posY + 1D, attacker.posZ, attacker);
			int meta = attacker.getBombStrength();
			
			bomb.setBombStrengthLevel(meta);
			bomb.setFuseFromHiredUnit();
			bomb.droppedByHiredUnit = attacker.hiredNPCInfo.isActive;
			bomb.droppedTargetingPlayer = entityTarget instanceof EntityPlayer;
			
			worldObj.spawnEntityInWorld(bomb);
			attacker.setCurrentItemOrArmor(0, new ItemStack(weaponItem));
			worldObj.playSoundAtEntity(attacker, "random.fuse", 1F, 1F);
			worldObj.playSoundAtEntity(attacker, "lotr:orc.fire", 1F, (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.2F + 1.0F);
        }
    }
}
