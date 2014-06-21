package lotr.common.entity.animal;

import lotr.common.LOTRMod;
import lotr.common.entity.LOTREntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeavesBase;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class LOTREntityGiraffe extends EntityAnimal
{
    public LOTREntityGiraffe(World world)
    {
        super(world);
        setSize(1.7F, 2.8F);
        getNavigator().setAvoidsWater(true);
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(1, new EntityAIPanic(this, 1.7D));
        tasks.addTask(2, new EntityAIMate(this, 1D));
        tasks.addTask(3, new EntityAITempt(this, 1.4D, Item.getItemFromBlock(Blocks.leaves), false));
        tasks.addTask(3, new EntityAITempt(this, 1.4D, Item.getItemFromBlock(Blocks.leaves2), false));
        tasks.addTask(4, new EntityAIFollowParent(this, 1.4D));
        tasks.addTask(5, new EntityAIWander(this, 1D));
        tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8F));
        tasks.addTask(7, new EntityAILookIdle(this));
    }
	
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20D);
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
    }
	
	@Override
    protected void entityInit()
    {
        super.entityInit();
		dataWatcher.addObject(16, Byte.valueOf((byte)0));
    }
	
	@Override
    public void writeEntityToNBT(NBTTagCompound nbt)
    {
        super.writeEntityToNBT(nbt);
        nbt.setBoolean("Saddled", getSaddled());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt)
    {
        super.readEntityFromNBT(nbt);
        setSaddled(nbt.getBoolean("Saddled"));
    }
	
	@Override
    public double getMountedYOffset()
    {
        return (double)height * 0.93D;
    }
	
	@Override
    public boolean isAIEnabled()
    {
        return true;
    }
	
	@Override
    public boolean isBreedingItem(ItemStack itemstack)
    {
        return itemstack != null && Block.getBlockFromItem(itemstack.getItem()) instanceof BlockLeavesBase;
    }

	@Override
    public EntityAgeable createChild(EntityAgeable entity)
    {
        return new LOTREntityGiraffe(worldObj);
    }
	
	@Override
    public void moveEntityWithHeading(float f, float f1)
    {
        if (riddenByEntity != null && riddenByEntity instanceof EntityPlayer && getSaddled())
        {
            prevRotationYaw = rotationYaw = riddenByEntity.rotationYaw;
            rotationPitch = riddenByEntity.rotationPitch * 0.5F;
            setRotation(rotationYaw, rotationPitch);
            rotationYawHead = renderYawOffset = rotationYaw;
            f = ((EntityLivingBase)riddenByEntity).moveStrafing * 0.5F;
            f1 = ((EntityLivingBase)riddenByEntity).moveForward;

            if (f1 <= 0F)
            {
                f1 *= 0.25F;
            }

            stepHeight = 1F;
            jumpMovementFactor = getAIMoveSpeed() * 0.1F;

            if (!worldObj.isRemote)
            {
                setAIMoveSpeed((float)getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue());
                super.moveEntityWithHeading(f, f1);
            }

            prevLimbSwingAmount = limbSwingAmount;
            double d0 = posX - prevPosX;
            double d1 = posZ - prevPosZ;
            float f4 = MathHelper.sqrt_double(d0 * d0 + d1 * d1) * 4F;

            if (f4 > 1F)
            {
                f4 = 1F;
            }

            limbSwingAmount += (f4 - limbSwingAmount) * 0.4F;
            limbSwing += limbSwingAmount;
        }
        else
        {
            stepHeight = 0.5F;
            jumpMovementFactor = 0.02F;
            super.moveEntityWithHeading(f, f1);
        }
    }
	
	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		
        if (!worldObj.isRemote)
        {
            if (rand.nextInt(900) == 0 && isEntityAlive())
            {
                heal(1F);
            }
        }
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource damagesource, float f)
	{
		if (riddenByEntity != null && damagesource.getEntity() == riddenByEntity)
		{
			return false;
		}
		return super.attackEntityFrom(damagesource, f);
	}
	
	@Override
    protected Item getDropItem()
    {
        return Items.leather;
    }
	
	@Override
    public boolean interact(EntityPlayer entityplayer)
    {
        if (super.interact(entityplayer))
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
    public void onDeath(DamageSource damagesource)
    {
        super.onDeath(damagesource);
        if (!worldObj.isRemote)
        {
			if (getSaddled())
			{
				dropItem(Items.saddle, 1);
				setSaddled(false);
			}
        }
    }
	
	@Override
    public ItemStack getPickedResult(MovingObjectPosition target)
    {
		return new ItemStack(LOTRMod.spawnEgg, 1, LOTREntities.getEntityID(this));
    }
}
