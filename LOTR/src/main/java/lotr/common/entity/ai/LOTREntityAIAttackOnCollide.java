package lotr.common.entity.ai;

import lotr.common.entity.npc.LOTREntityNPC;
import lotr.common.entity.projectile.LOTREntitySpear;
import lotr.common.item.LOTRItemSpear;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class LOTREntityAIAttackOnCollide extends EntityAIBase
{
    private World worldObj;
    private EntityCreature theOwner;
    private EntityLivingBase entityTarget;
    private int attackTick;
    private double moveSpeed;
    private boolean sightNotRequired;
    private PathEntity entityPathEntity;
    private int pathCheckTimer;
	private float rangeFactor;
	private boolean avoidsWater;
	private Item spearReplacement;
	
    public LOTREntityAIAttackOnCollide(EntityCreature entity, double speed, boolean flag)
    {
		this(entity, speed, flag, 1F);
	}

    public LOTREntityAIAttackOnCollide(EntityCreature entity, double speed, boolean flag, float f)
    {
        attackTick = 0;
        theOwner = entity;
        worldObj = entity.worldObj;
        moveSpeed = speed;
        sightNotRequired = flag;
		rangeFactor = f;
		avoidsWater = entity.getNavigator().getAvoidsWater();
        setMutexBits(3);
    }
	
	public LOTREntityAIAttackOnCollide setSpearReplacement(Item item)
	{
		spearReplacement = item;
		return this;
	}

    @Override
    public boolean shouldExecute()
    {
		if (theOwner instanceof LOTREntityNPC && ((LOTREntityNPC)theOwner).isPassive)
		{
			return false;
		}
        EntityLivingBase entity = theOwner.getAttackTarget();
        if (entity == null)
        {
            return false;
        }
        else
        {
            entityTarget = entity;
			theOwner.getNavigator().setAvoidsWater(false);
			entityPathEntity = getPathEntity();
			if (entityPathEntity != null)
			{
				return true;
			}
			else
			{
				theOwner.getNavigator().setAvoidsWater(avoidsWater);
				return false;
			}
        }
    }

    @Override
    public boolean continueExecuting()
    {
        EntityLivingBase entity = theOwner.getAttackTarget();
		if (entity == null)
		{
			return false;
		}
		else if (entityTarget == null || !entityTarget.isEntityAlive())
		{
			return false;
		}
		else
		{
			if (sightNotRequired)
			{
				return theOwner.isWithinHomeDistance(MathHelper.floor_double(entityTarget.posX), MathHelper.floor_double(entityTarget.posY), MathHelper.floor_double(entityTarget.posZ));
			}
			else
			{
				return !theOwner.getNavigator().noPath();
			}
		}
    }

    @Override
    public void startExecuting()
    {
        theOwner.getNavigator().setPath(entityPathEntity, moveSpeed);
        pathCheckTimer = 0;
    }

    @Override
    public void resetTask()
    {
        entityTarget = null;
        theOwner.getNavigator().clearPathEntity();
		theOwner.getNavigator().setAvoidsWater(avoidsWater);
    }

    @Override
    public void updateTask()
    {
        theOwner.getLookHelper().setLookPositionWithEntity(entityTarget, 30F, 30F);
		if (theOwner.riddenByEntity != null && theOwner.riddenByEntity instanceof EntityLiving)
		{
			((EntityLiving)theOwner.riddenByEntity).rotationYaw = theOwner.rotationYaw;
			((EntityLiving)theOwner.riddenByEntity).rotationYawHead = theOwner.rotationYawHead;
		}
		
        if ((sightNotRequired || theOwner.getEntitySenses().canSee(entityTarget)) && --pathCheckTimer <= 0)
        {
            pathCheckTimer = 10 + theOwner.getRNG().nextInt(10);
			PathEntity path = getPathEntity();
			if (path != null)
			{
				theOwner.getNavigator().setPath(path, moveSpeed);
			}
        }
		
		ItemStack heldItem = theOwner.getEquipmentInSlot(0);
		if (heldItem != null && heldItem.getItem() instanceof LOTRItemSpear)
		{
			double d = theOwner.getDistanceToEntity(entityTarget);
			double range = (double)theOwner.getNavigator().getPathSearchRange();
			if (d > 4D && d < range * 0.75D)
			{
				LOTREntitySpear spear = new LOTREntitySpear(worldObj, theOwner, entityTarget, heldItem.getItem(), 0, 0.75F + ((float)d * 0.025F), 0.5F);
				worldObj.playSoundAtEntity(theOwner, "random.bow", 1F, 1F / (worldObj.rand.nextFloat() * 0.4F + 1.2F) + 0.25F);
				theOwner.setCurrentItemOrArmor(0, new ItemStack(spearReplacement));
				worldObj.spawnEntityInWorld(spear);
				return;
			}
		}

		attackTick = Math.max(attackTick - 1, 0);
		float f = rangeFactor;
		if (theOwner.ridingEntity != null)
		{
			f *= 1.5F;
		}
		AxisAlignedBB aabb = theOwner.boundingBox.copy().expand((double)f, (double)f, (double)f);
		if (aabb.intersectsWith(entityTarget.boundingBox))
		{
			if (attackTick <= 0)
			{
				attackTick = 20;
				theOwner.attackEntityAsMob(entityTarget);
				theOwner.swingItem();
			}
		}
    }
	
	private PathEntity getPathEntity()
	{
		if (theOwner.ridingEntity != null)
		{
			return worldObj.getPathEntityToEntity(theOwner, entityTarget, theOwner.getNavigator().getPathSearchRange(), true, theOwner.getNavigator().getCanBreakDoors(), theOwner.getNavigator().getAvoidsWater(), false);
		}
		else
		{
			return theOwner.getNavigator().getPathToEntityLiving(entityTarget);
		}
	}
}
