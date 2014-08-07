package lotr.common.entity.projectile;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.List;

import lotr.common.LOTRFaction;
import lotr.common.LOTRLevelData;
import lotr.common.LOTRMod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S3FPacketCustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class LOTREntityGandalfFireball extends EntityThrowable
{
	public int animationTick;
	
	public LOTREntityGandalfFireball(World world)
	{
		super(world);
	}
	
	public LOTREntityGandalfFireball(World world, EntityLivingBase entityliving)
	{
		super(world, entityliving);
	}
	
	public LOTREntityGandalfFireball(World world, double d, double d1, double d2)
	{
		super(world, d, d1, d2);
	}
	
	@Override
	protected void entityInit()
	{
		super.entityInit();
		dataWatcher.addObject(16, Integer.valueOf(0));
	}
	
	public int getFireballAge()
	{
		return dataWatcher.getWatchableObjectInt(16);
	}
	
	public void setFireballAge(int age)
	{
		dataWatcher.updateObject(16, Integer.valueOf(age));
	}
	
	@Override
    public void writeEntityToNBT(NBTTagCompound nbt)
    {
        super.writeEntityToNBT(nbt);
		nbt.setInteger("FireballAge", getFireballAge());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt)
    {
        super.readEntityFromNBT(nbt);
        setFireballAge(nbt.getInteger("FireballAge"));
    }
	
	@Override
	public void onUpdate()
	{
		super.onUpdate();
		
		if (worldObj.getWorldTime() % 5L == 0)
		{
			animationTick++;
			if (animationTick >= 4)
			{
				animationTick = 0;
			}
		}
		
		if (!worldObj.isRemote)
		{
			setFireballAge(getFireballAge() + 1);
			if (getFireballAge() >= 200)
			{
				explode(null);
			}
		}
	}
	
	@Override
	protected void onImpact(MovingObjectPosition m)
	{
		if (!worldObj.isRemote)
		{
			if (m.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
			{
				explode(null);
			}
			else if (m.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY)
			{
				Entity entity = m.entityHit;
				if (isEntityVulnerable(entity))
				{
					explode(entity);
				}
			}
		}
	}
	
	private void explode(Entity target)
	{
		if (worldObj.isRemote)
		{
			return;
		}
		
		worldObj.playSoundAtEntity(this, "lotr:item.gandalfFireball", 4F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
		
		ByteBuf data = Unpooled.buffer();
		
		data.writeDouble(posX);
		data.writeDouble(posY);
		data.writeDouble(posZ);

		S3FPacketCustomPayload packet = new S3FPacketCustomPayload("lotr.fireball", data);
		MinecraftServer.getServer().getConfigurationManager().sendToAllNear(posX, posY, posZ, 64D, dimension, packet);
		
		if (target != null && isEntityVulnerable(target))
		{
			target.attackEntityFrom(DamageSource.causeMobDamage(getThrower()), 10F);
		}
		
		List entities = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, boundingBox.expand(6D, 6D, 6D));
		if (!entities.isEmpty())
		{
			for (int i = 0; i < entities.size(); i++)
			{
				EntityLivingBase entity = (EntityLivingBase)entities.get(i);
				if (entity != target && isEntityVulnerable(entity))
				{
					float damage = 10F - ((float)getDistanceToEntity(entity) * 0.5F);
					if (damage > 0F)
					{
						entity.attackEntityFrom(DamageSource.causeMobDamage(getThrower()), damage);
					}
				}
			}
		}

		setDead();
	}
	
	private boolean isEntityVulnerable(Entity entity)
	{
		if (entity == getThrower())
		{
			return false;
		}
		else if (!(entity instanceof EntityLivingBase))
		{
			return false;
		}
		else
		{
			if (entity instanceof EntityPlayer)
			{
				return LOTRLevelData.getData((EntityPlayer)entity).getAlignment(LOTRFaction.HIGH_ELF) < 0;
			}
			return LOTRMod.getNPCFaction(entity).isEnemy(LOTRFaction.HIGH_ELF);
		}
	}
	
	@Override
    protected float func_70182_d()
    {
        return 1.5F;
    }
	
	@Override
    protected float getGravityVelocity()
    {
        return 0F;
    }
}
