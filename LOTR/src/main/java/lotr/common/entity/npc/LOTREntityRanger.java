package lotr.common.entity.npc;

import lotr.common.LOTRMod;
import lotr.common.entity.ai.LOTREntityAIAttackOnCollide;
import lotr.common.entity.ai.LOTREntityAIFollowHiringPlayer;
import lotr.common.entity.ai.LOTREntityAIHiredRemainStill;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWatchClosest2;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public abstract class LOTREntityRanger extends LOTREntityNPC implements IRangedAttackMob
{
	public EntityAIBase rangedAttackAI = createRangerRangedAttackAI();
	public EntityAIBase meleeAttackAI = createRangerMeleeAttackAI();
	public int weaponChangeCooldown = 0;
	private int sneakCooldown = 0;
	private EntityLivingBase prevRangerTarget;
	
	public LOTREntityRanger(World world)
	{
		super(world);
		setSize(0.6F, 1.8F);
		getNavigator().setAvoidsWater(true);
		getNavigator().setBreakDoors(true);
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(1, new LOTREntityAIHiredRemainStill(this));
        tasks.addTask(3, new LOTREntityAIFollowHiringPlayer(this));
		tasks.addTask(4, new EntityAIOpenDoor(this, true));
        tasks.addTask(5, new EntityAIWander(this, 1D));
        tasks.addTask(6, new EntityAIWatchClosest2(this, EntityPlayer.class, 10F, 0.1F));
        tasks.addTask(6, new EntityAIWatchClosest2(this, LOTREntityNPC.class, 5F, 0.05F));
        tasks.addTask(7, new EntityAIWatchClosest(this, EntityLiving.class, 8F, 0.02F));
        tasks.addTask(8, new EntityAILookIdle(this));
        targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        addTargetTasks(2);
		spawnCountValue = 5;
	}
	
	public EntityAIBase createRangerRangedAttackAI()
	{
		return new EntityAIArrowAttack(this, 1.25D, 20, 40, 20F);
	}
	
	public EntityAIBase createRangerMeleeAttackAI()
	{
		return new LOTREntityAIAttackOnCollide(this, 1.5D, true);
	}
	
	public abstract Item getRangerSwordId();
	
	public abstract Item getRangerBowId();
	
	@Override
	public void entityInit()
	{
		super.entityInit();
		dataWatcher.addObject(16, LOTRNames.getRandomGondorName(rand));
		dataWatcher.addObject(17, Byte.valueOf((byte)0));
	}
	
	public String getRangerName()
	{
		return dataWatcher.getWatchableObjectString(16);
	}
	
	public void setRangerName(String name)
	{
		dataWatcher.updateObject(16, name);
	}
	
	public boolean isRangerSneaking()
	{
		return dataWatcher.getWatchableObjectByte(17) == (byte)1;
	}
	
	public void setRangerSneaking(boolean flag)
	{
		dataWatcher.updateObject(17, Byte.valueOf(flag ? (byte)1 : (byte)0));
		if (flag)
		{
			sneakCooldown = 20;
		}
	}
	
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(25D);
		getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(24D);
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
    }
	
	@Override
    public IEntityLivingData onSpawnWithEgg(IEntityLivingData data)
    {
		data = super.onSpawnWithEgg(data);
		setCurrentItemOrArmor(1, new ItemStack(LOTRMod.bootsRanger));
		setCurrentItemOrArmor(2, new ItemStack(LOTRMod.legsRanger));
		setCurrentItemOrArmor(3, new ItemStack(LOTRMod.bodyRanger));
		setCurrentItemOrArmor(4, new ItemStack(LOTRMod.helmetRanger));
		return data;
    }
	
	@Override
    public boolean isAIEnabled()
    {
        return true;
    }
	
	@Override
	public String getNPCName()
	{
		return getRangerName();
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound nbt)
	{
		super.writeEntityToNBT(nbt);
		nbt.setString("RangerName", getRangerName());
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound nbt)
	{
		super.readEntityFromNBT(nbt);
		if (nbt.hasKey("RangerName"))
		{
			setRangerName(nbt.getString("RangerName"));
		}
	}
	
	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		
		if (!worldObj.isRemote)
		{
			ItemStack weapon = getEquipmentInSlot(0);
			if (getAttackTarget() != null)
			{
				double d = getDistanceSqToEntity(getAttackTarget());
				if (d < 16D)
				{
					if (weapon == null || weapon.getItem() != getRangerSwordId())
					{
						tasks.removeTask(rangedAttackAI);
						tasks.addTask(2, meleeAttackAI);
						setCurrentItemOrArmor(0, new ItemStack(getRangerSwordId()));
						weaponChangeCooldown = 20;
					}
				}
				else if (d < getWeaponChangeThresholdRangeSq())
				{
					if (weapon == null || weapon.getItem() != getRangerBowId())
					{
						tasks.removeTask(meleeAttackAI);
						tasks.addTask(2, rangedAttackAI);
						setCurrentItemOrArmor(0, new ItemStack(getRangerBowId()));
						weaponChangeCooldown = 20;
					}
				}
			}
			else
			{
				if (weapon != null)
				{
					if (weaponChangeCooldown > 0)
					{
						weaponChangeCooldown--;
					}
					else
					{
						tasks.removeTask(rangedAttackAI);
						tasks.removeTask(meleeAttackAI);
						setCurrentItemOrArmor(0, null);
					}
				}
			}
			
			if (isRangerSneaking())
			{
				if (getAttackTarget() == null)
				{
					if (sneakCooldown > 0)
					{
						sneakCooldown--;
					}
					else
					{
						setRangerSneaking(false);
					}
				}
				else
				{
					sneakCooldown = 20;
				}
			}
			else
			{
				sneakCooldown = 0;
			}
		}
	}
	
	@Override
	public void setAttackTarget(EntityLivingBase target)
	{
		super.setAttackTarget(target);
		if (target != null && target != prevRangerTarget)
		{
			prevRangerTarget = target;
			if (!worldObj.isRemote && !isRangerSneaking())
			{
				setRangerSneaking(true);
			}
		}
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource damagesource, float f)
	{
		boolean flag = super.attackEntityFrom(damagesource, f);
		if (flag && !worldObj.isRemote && isRangerSneaking())
		{
			setRangerSneaking(false);
		}
		return flag;
	}
	
	@Override
	public void swingItem()
	{
		super.swingItem();
		if (!worldObj.isRemote && isRangerSneaking())
		{
			setRangerSneaking(false);
		}
	}
	
	@Override
    public void attackEntityWithRangedAttack(EntityLivingBase target, float f)
    {
        EntityArrow arrow = new EntityArrow(worldObj, this, target, 1.3F + (getDistanceToEntity(target) / 24F * 0.3F), 0.5F);
        playSound("random.bow", 1F, 1F / (rand.nextFloat() * 0.4F + 0.8F));
        worldObj.spawnEntityInWorld(arrow);
		if (!worldObj.isRemote && isRangerSneaking() && getDistanceSqToEntity(target) < 100D)
		{
			setRangerSneaking(false);
		}
    }
	
	@Override
	protected void dropFewItems(boolean flag, int i)
	{
		super.dropFewItems(flag, i);
		
		int j = rand.nextInt(2) + rand.nextInt(i + 1);
		for (int k = 0; k < j; k++)
		{
			dropItem(Items.bone, 1);
		}
		
		if (rand.nextBoolean())
		{
			j = rand.nextInt(3) + rand.nextInt(i + 1);
			for (int k = 0; k < j; k++)
			{
				dropItem(Items.arrow, 1);
			}
		}
	}
	
	@Override
	public boolean getCanSpawnHere()
	{
		if (super.getCanSpawnHere())
		{
			if (liftSpawnRestrictions)
			{
				return true;
			}
			else
			{
				int i = MathHelper.floor_double(posX);
				int j = MathHelper.floor_double(boundingBox.minY);
				int k = MathHelper.floor_double(posZ);
				Block block = worldObj.getBlock(i, j - 1, k);
				return j > 62 && (block == Blocks.grass ||block == Blocks.sand);
			}
		}
		return false;
	}
	
	@Override
	protected void func_145780_a(int i, int j, int k, Block block)
	{
		if (!isRangerSneaking())
		{
			super.func_145780_a(i, j, k, block);
		}
	}
	
	public String getRangerCape()
	{
		return null;
	}
}
