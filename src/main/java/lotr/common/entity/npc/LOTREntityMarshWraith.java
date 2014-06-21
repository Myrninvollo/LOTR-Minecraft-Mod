package lotr.common.entity.npc;

import java.util.UUID;

import lotr.common.LOTRFaction;
import lotr.common.entity.projectile.LOTREntityMarshWraithBall;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class LOTREntityMarshWraith extends LOTREntityNPC implements IRangedAttackMob
{
	public static double VERTICAL_OFFSET = 2D;
	public UUID attackTargetUUID;
	private boolean checkedForAttackTarget;
	private int timeSinceTargetLeftWater = -1;
	
	public LOTREntityMarshWraith(World world)
	{
		super(world);
		setSize(0.6F, 1.8F);
		tasks.addTask(0, new EntityAIArrowAttack(this, 1.6D, 40, 6F));
        tasks.addTask(1, new EntityAIWander(this, 1D));
        tasks.addTask(2, new EntityAIWatchClosest(this, EntityPlayer.class, 8F, 0.02F));
        tasks.addTask(3, new EntityAILookIdle(this));
		ignoreFrustumCheck = true;
	}
	
	@Override
	protected void entityInit()
	{
		super.entityInit();
		dataWatcher.addObject(16, Integer.valueOf(0));
		dataWatcher.addObject(17, Integer.valueOf(0));
	}
	
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20D);
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.2D);
    }
	
	public int getSpawnFadeTime()
	{
		return dataWatcher.getWatchableObjectInt(16);
	}
	
	public void setSpawnFadeTime(int i)
	{
		dataWatcher.updateObject(16, Integer.valueOf(i));
	}
	
	public int getDeathFadeTime()
	{
		return dataWatcher.getWatchableObjectInt(17);
	}
	
	public void setDeathFadeTime(int i)
	{
		dataWatcher.updateObject(17, Integer.valueOf(i));
	}
	
	@Override
	public LOTRFaction getFaction()
	{
		return LOTRFaction.HOSTILE;
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound nbt)
	{
		super.writeEntityToNBT(nbt);
		nbt.setInteger("SpawnFadeTime", getSpawnFadeTime());
		nbt.setInteger("DeathFadeTime", getDeathFadeTime());
		if (attackTargetUUID != null)
		{
			nbt.setLong("TargetUUIDMost", attackTargetUUID.getMostSignificantBits());
			nbt.setLong("TargetUUIDLeast", attackTargetUUID.getLeastSignificantBits());
		}
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound nbt)
	{
		super.readEntityFromNBT(nbt);
		setSpawnFadeTime(nbt.getInteger("SpawnFadeTime"));
		setDeathFadeTime(nbt.getInteger("DeathFadeTime"));
		if (nbt.hasKey("TargetUUIDMost") && nbt.hasKey("TargetUUIDLeast"))
		{
			attackTargetUUID = new UUID(nbt.getLong("TargetUUIDMost"), nbt.getLong("TargetUUIDLeast"));
		}
	}
	
	@Override
    public boolean isAIEnabled()
    {
        return true;
    }
	
    @Override
    public void setInWeb() {}
	
	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		
		if (!worldObj.isRemote && isInWater())
		{
			posY += 0.05D;
		}
		
        if (rand.nextBoolean())
        {
            worldObj.spawnParticle("smoke", posX + (rand.nextDouble() - 0.5D) * (double)width, posY + VERTICAL_OFFSET + rand.nextDouble() * (double)height, posZ + (rand.nextDouble() - 0.5D) * (double)width, 0.0D, 0.0D, 0.0D);
        }
		
		if (!worldObj.isRemote)
		{
			if (getAttackTarget() == null && attackTargetUUID != null && !checkedForAttackTarget)
			{
				for (int i = 0; i < worldObj.loadedEntityList.size(); i++)
				{
					Entity entity = (Entity)worldObj.loadedEntityList.get(i);
					if (entity instanceof EntityLiving && entity.getPersistentID().equals(attackTargetUUID))
					{
						setAttackTarget((EntityLiving)entity);
						break;
					}
				}
				checkedForAttackTarget = true;
			}
			
			if (getSpawnFadeTime() < 30)
			{
				setSpawnFadeTime(getSpawnFadeTime() + 1);
			}
			if (getDeathFadeTime() > 0)
			{
				setDeathFadeTime(getDeathFadeTime() - 1);
			}
			
			if (getSpawnFadeTime() == 30 && getDeathFadeTime() == 0)
			{
				if (getAttackTarget() == null || getAttackTarget().isDead)
				{
					setDeathFadeTime(30);
				}
				else
				{
					int i = MathHelper.floor_double(getAttackTarget().posX);
					int j = MathHelper.floor_double(getAttackTarget().boundingBox.minY);
					int k = MathHelper.floor_double(getAttackTarget().posZ);
					if (worldObj.getBlock(i, j, k).getMaterial() != Material.water && (!worldObj.isAirBlock(i, j, k) || worldObj.getBlock(i, j - 1, k).getMaterial() != Material.water))
					{
						if (timeSinceTargetLeftWater == -1)
						{
							timeSinceTargetLeftWater = 80;
						}
						else
						{
							timeSinceTargetLeftWater--;
							if (timeSinceTargetLeftWater == 0)
							{
								setDeathFadeTime(30);
								setAttackTarget(null);
							}
						}
					}
					else
					{
						timeSinceTargetLeftWater = -1;
					}
				}
			}
			
			if (getDeathFadeTime() == 1)
			{
				setDead();
			}
		}
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource damagesource, float f)
	{
		return false;
	}
	
	@Override
    public EnumCreatureAttribute getCreatureAttribute()
    {
        return EnumCreatureAttribute.UNDEAD;
    }
	
	@Override
    public void attackEntityWithRangedAttack(EntityLivingBase target, float f)
    {
		if (getSpawnFadeTime() == 30 && getDeathFadeTime() == 0)
		{
			LOTREntityMarshWraithBall ball = new LOTREntityMarshWraithBall(worldObj, this, target);
			playSound("lotr:wraith.marshWraith_shoot", 1F, 1F / (rand.nextFloat() * 0.4F + 0.8F));
			worldObj.spawnEntityInWorld(ball);
		}
    }
	
	@Override
	public boolean handleWaterMovement()
	{
		return false;
	}
	
	@Override
    protected void func_145780_a(int i, int j, int k, Block block) {}
}
