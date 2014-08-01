package lotr.common.entity.animal;

import java.util.List;

import lotr.common.LOTRMod;
import lotr.common.entity.LOTREntities;
import lotr.common.entity.LOTRMountFunctions;
import lotr.common.entity.ai.LOTREntityAIAttackOnCollide;
import lotr.common.entity.npc.LOTRNPCMount;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
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
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class LOTREntityRhino extends EntityAnimal implements LOTRNPCMount
{
	private int hostileTick = 0;
	
	private EntityAIBase attackAI = new LOTREntityAIAttackOnCollide(this, 1D, false);
	private EntityAIBase panicAI = new EntityAIPanic(this, 0.8D);
	private boolean prevIsChild = true;
	
    public LOTREntityRhino(World world)
    {
        super(world);
        setSize(1.5F, 1.6F);
        getNavigator().setAvoidsWater(true);
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(2, panicAI);
        tasks.addTask(3, new EntityAIMate(this, 0.5D));
        tasks.addTask(4, new EntityAITempt(this, 0.5D, Items.wheat, false));
        tasks.addTask(5, new EntityAIFollowParent(this, 0.7D));
        tasks.addTask(6, new EntityAIWander(this, 0.5D));
        tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        tasks.addTask(8, new EntityAILookIdle(this));
        targetTasks.addTask(0, new EntityAIHurtByTarget(this, false));
        targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
    }
    
    @Override
    public void entityInit()
    {
    	super.entityInit();
    	dataWatcher.addObject(16, Byte.valueOf((byte)0));
    	dataWatcher.addObject(20, Byte.valueOf((byte)0));
    }
    
    @Override
	public boolean getBelongsToNPC()
	{
		return false;
	}
	
	@Override
	public void setBelongsToNPC(boolean flag) {}
    
    public boolean isHostile()
    {
    	return dataWatcher.getWatchableObjectByte(20) == (byte)1;
    }

	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(30D);
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.35D);
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
		
		LOTRMountFunctions.update(this);
		
		if (!worldObj.isRemote)
		{
			if (riddenByEntity != null && riddenByEntity instanceof EntityLivingBase)
			{
				EntityLivingBase rhinoRider = (EntityLivingBase)riddenByEntity;
				
				hostileTick = 0;
				
				float momentum = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
				if (momentum > 0.2F)
				{
					setSprinting(true);
				}
				else
				{
					setSprinting(false);
				}
				
				if (momentum >= 0.32F)
				{
					float strength = momentum * 15F;
					
					Vec3 position = Vec3.createVectorHelper(posX, posY, posZ);
					Vec3 look = getLookVec();
					float sightWidth = 1F;
					
					double range = 0.5D;
					List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.contract(1D, 1D, 1D).addCoord(look.xCoord * range, look.yCoord * range, look.zCoord * range).expand((double)sightWidth, (double)sightWidth, (double)sightWidth));
					boolean hitAnyEntities = false;
					for (int i = 0; i < list.size(); i++)
					{
						Entity obj = (Entity)list.get(i);
						if (!(obj instanceof EntityLivingBase))
						{
							continue;
						}
						
						EntityLivingBase entity = (EntityLivingBase)obj;
						if (entity == rhinoRider)
						{
							continue;
						}
						
						if (rhinoRider instanceof EntityPlayer && !LOTRMod.canPlayerAttackEntity((EntityPlayer)rhinoRider, entity, false))
						{
							continue;
						}
						
						if (rhinoRider instanceof EntityCreature && !LOTRMod.canNPCAttackEntity((EntityCreature)rhinoRider, entity))
						{
							continue;
						}
						
						boolean flag = entity.attackEntityFrom(DamageSource.causeMobDamage(this), strength);
						if (flag)
						{
							float knockback = strength * 0.05F;
							entity.addVelocity(-MathHelper.sin((rotationYaw * (float)Math.PI) / 180F) * knockback, knockback, MathHelper.cos((rotationYaw * (float)Math.PI) / 180F) * knockback);
							hitAnyEntities = true;
							
							if (entity instanceof EntityLiving)
							{
								EntityLiving entityliving = (EntityLiving)entity;
								if (entityliving.getAttackTarget() == this)
								{
									entityliving.getNavigator().clearPathEntity();
									entityliving.setAttackTarget(rhinoRider);
								}
							}
						}
					}
					
					if (hitAnyEntities)
					{
						worldObj.playSoundAtEntity(this, "lotr:troll.ologHai_hammer", 1F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1F);
					}
				}
			}
			else
			{
				if (hostileTick > 0 && getAttackTarget() == null)
				{
					hostileTick--;
				}
				
				if (getAttackTarget() != null)
				{
					float momentum = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
					if (momentum > 0.2F)
					{
						setSprinting(true);
					}
					else
					{
						setSprinting(false);
					}
				}
			}
			
			dataWatcher.updateObject(20, hostileTick > 0 ? (byte)1 : (byte)0);
		}
	}
	
	@Override
    public void moveEntityWithHeading(float strafe, float forward)
    {
		LOTRMountFunctions.move(this, strafe, forward);
    }
	
	@Override
    public void super_moveEntityWithHeading(float strafe, float forward)
    {
		super.moveEntityWithHeading(strafe, forward);
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
		boolean flag = super.attackEntityFrom(damagesource, f);
		if (flag && riddenByEntity == null)
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
		}
        return flag;
    }
	
	private void becomeAngryAt(EntityLivingBase entity)
    {
        setAttackTarget(entity);
        hostileTick = 200;
    }
	
	@Override
    public boolean getSaddled()
    {
        return (dataWatcher.getWatchableObjectByte(16) & 1) != 0;
    }

    public void setSaddled(boolean flag)
    {
        if (flag)
        {
            dataWatcher.updateObject(16, Byte.valueOf((byte)1));
        }
        else
        {
            dataWatcher.updateObject(16, Byte.valueOf((byte)0));
        }
    }
	
	@Override
    public void writeEntityToNBT(NBTTagCompound nbt)
    {
        super.writeEntityToNBT(nbt);
        nbt.setInteger("Angry", hostileTick);
        nbt.setBoolean("Saddled", getSaddled());
    }
	
	@Override
    public void readEntityFromNBT(NBTTagCompound nbt)
    {
        super.readEntityFromNBT(nbt);
        hostileTick = nbt.getInteger("Angry");
        setSaddled(nbt.getBoolean("Saddled"));
    }
	
	@Override
    public boolean interact(EntityPlayer entityplayer)
    {
        if (isHostile())
		{
			return false;
		}
        
        if (super.interact(entityplayer))
        {
            return true;
        }
        
        if (LOTRMountFunctions.interact(this, entityplayer))
		{
			return true;
		}
        
        ItemStack itemstack = entityplayer.inventory.getCurrentItem();
        if (!isChild() && !getSaddled() && riddenByEntity == null && itemstack != null && itemstack.getItem() == Items.saddle)
		{
			if (!entityplayer.capabilities.isCreativeMode)
			{
				entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, null);
			}
			setSaddled(true);
			playSound("mob.horse.leather", 0.5F, 1F);
			return true;
		}
        else if (getSaddled() && !worldObj.isRemote && (riddenByEntity == null || riddenByEntity == entityplayer))
        {
            entityplayer.mountEntity(this);
            return true;
        }
        else
        {
            return false;
        }
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
	public void onDeath(DamageSource damagesource)
    {
        super.onDeath(damagesource);
        
        if (!worldObj.isRemote && getSaddled() && !getBelongsToNPC())
        {
			dropItem(Items.saddle, 1);
			setSaddled(false);
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
