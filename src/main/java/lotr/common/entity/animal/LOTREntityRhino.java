package lotr.common.entity.animal;

import java.util.List;

import lotr.common.LOTRMod;
import lotr.common.entity.LOTREntities;
import lotr.common.entity.ai.LOTREntityAIAttackOnCollide;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class LOTREntityRhino extends EntityAnimal
{
	private int hostileTick = 0;
	
	private EntityAIBase attackAI = new LOTREntityAIAttackOnCollide(this, 1.8D, false);
	private EntityAIBase panicAI = new EntityAIPanic(this, 1.5D);
	private boolean prevIsChild = true;
	
    public LOTREntityRhino(World world)
    {
        super(world);
        setSize(1.5F, 1.3F);
        getNavigator().setAvoidsWater(true);
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(2, panicAI);
        tasks.addTask(3, new EntityAIMate(this, 1D));
        tasks.addTask(4, new EntityAITempt(this, 1D, Items.wheat, false));
        tasks.addTask(5, new EntityAIFollowParent(this, 1.3D));
        tasks.addTask(6, new EntityAIWander(this, 1D));
        tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        tasks.addTask(8, new EntityAILookIdle(this));
        targetTasks.addTask(0, new EntityAIHurtByTarget(this, false));
        targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
    }
    
    @Override
    public void entityInit()
    {
    	super.entityInit();
    	dataWatcher.addObject(20, Byte.valueOf((byte)0));
    }
    
    public boolean isHostile()
    {
    	return dataWatcher.getWatchableObjectByte(20) == (byte)1;
    }

	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20D);
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.2D);
        getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(4D);
    }
	
	@Override
    public boolean isAIEnabled()
    {
        return true;
    }
	
	@Override
    public boolean isBreedingItem(ItemStack itemstack)
    {
        return itemstack.getItem() == Items.wheat;
    }

	@Override
    public EntityAgeable createChild(EntityAgeable entity)
    {
        return new LOTREntityRhino(worldObj);
    }
	
	@Override
	public void onLivingUpdate()
	{
		if (!worldObj.isRemote)
		{
			boolean isChild = isChild();
			if (isChild != prevIsChild)
			{
				if (isChild)
				{
					tasks.removeTask(attackAI);
					tasks.addTask(2, panicAI);
				}
				else
				{
					tasks.removeTask(panicAI);
					if (hostileTick > 0)
					{
						tasks.addTask(1, attackAI);
					}
					else
					{
						tasks.removeTask(attackAI);
					}
				}
			}
		}
		
		super.onLivingUpdate();
		
		if (!worldObj.isRemote)
		{
			if (hostileTick > 0 && getAttackTarget() == null)
			{
				hostileTick--;
			}
			
			dataWatcher.updateObject(20, hostileTick > 0 ? (byte)1 : (byte)0);
		}
		
		if (!worldObj.isRemote && getAttackTarget() != null)
		{
			EntityLivingBase entity = getAttackTarget();
			if (!entity.isEntityAlive() || (entity instanceof EntityPlayer && ((EntityPlayer)entity).capabilities.isCreativeMode))
			{
				setAttackTarget(null);
			}
		}
	}
	
	@Override
    public boolean attackEntityAsMob(Entity entity)
    {
        float f = (float)getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
        return entity.attackEntityFrom(DamageSource.causeMobDamage(this), f);
    }
	
	@Override
    public boolean attackEntityFrom(DamageSource damagesource, float f)
    {
		Entity attacker = damagesource.getEntity();
		if (isChild())
		{
			fleeingTick = 60;
			List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.expand(12D, 12D, 12D));
            for (int j = 0; j < list.size(); j++)
            {
				Entity entity = (Entity)list.get(j);
				if (entity instanceof LOTREntityRhino)
				{
					LOTREntityRhino rhino = (LOTREntityRhino)entity;
					if (!rhino.isChild() && attacker != null && attacker instanceof EntityLivingBase)
					{
						rhino.becomeAngryAt((EntityLivingBase)attacker);
					}
				}
			}
		}
		else if (attacker != null && attacker instanceof EntityLivingBase)
		{
			becomeAngryAt((EntityLivingBase)attacker);
		}
        return super.attackEntityFrom(damagesource, f);
    }
	
	private void becomeAngryAt(EntityLivingBase entity)
    {
        setAttackTarget(entity);
        hostileTick = 200;
    }
	
	@Override
    public void writeEntityToNBT(NBTTagCompound nbt)
    {
        super.writeEntityToNBT(nbt);
        nbt.setInteger("Angry", hostileTick);
    }
	
	@Override
    public void readEntityFromNBT(NBTTagCompound nbt)
    {
        super.readEntityFromNBT(nbt);
        hostileTick = nbt.getInteger("Angry");
    }
	
	@Override
    public boolean interact(EntityPlayer entityplayer)
    {
        if (isHostile())
		{
			return false;
		}
		return super.interact(entityplayer);
	}
	
	@Override
    protected void dropFewItems(boolean flag, int i)
    {
        int j = rand.nextInt(2) + rand.nextInt(1 + i);
        for (int k = 0; k < j; k++)
        {
			dropItem(LOTRMod.rhinoHorn, 1);
        }
        
        j = rand.nextInt(2) + 1 + rand.nextInt(1 + i);
        for (int l = 0; l < j; l++)
        {
			if (isBurning())
			{
				dropItem(LOTRMod.rhinoCooked, 1);
			}
			else
			{
				dropItem(LOTRMod.rhinoRaw, 1);
			}
        }
    }
	
	@Override
	protected String getLivingSound()
    {
        return "lotr:rhino.say";
    }

	@Override
    protected String getHurtSound()
    {
        return "lotr:rhino.hurt";
    }

	@Override
    protected String getDeathSound()
    {
        return "lotr:rhino.death";
    }
	
	@Override
    public ItemStack getPickedResult(MovingObjectPosition target)
    {
		return new ItemStack(LOTRMod.spawnEgg, 1, LOTREntities.getEntityID(this));
    }
}
