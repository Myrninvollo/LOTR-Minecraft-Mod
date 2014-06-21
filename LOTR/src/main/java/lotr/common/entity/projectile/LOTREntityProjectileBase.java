package lotr.common.entity.projectile;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import cpw.mods.fml.common.registry.IThrowableEntity;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class LOTREntityProjectileBase extends Entity implements IThrowableEntity, IProjectile
{
    private int xTile = -1;
    private int yTile = -1;
    private int zTile = -1;
    private Block inTile;
    private int inData = 0;
    public boolean inGround = false;
    public int shake = 0;
    public Entity shootingEntity;
    private int ticksInGround;
    private int ticksInAir = 0;
	private int itemDamage;
	public int canBePickedUp = 0;
	public int knockbackStrength;

    public LOTREntityProjectileBase(World world)
    {
        super(world);
        setSize(0.5F, 0.5F);
    }
	
    public LOTREntityProjectileBase(World world, Item item, int damage, double d, double d1, double d2)
    {
        super(world);
		setItemID(Item.getIdFromItem(item));
		itemDamage = damage;
        setSize(0.5F, 0.5F);
        setPosition(d, d1, d2);
        yOffset = 0F;
    }

    public LOTREntityProjectileBase(World world, EntityLivingBase entityliving, Item item, int damage, float charge)
    {
        super(world);
		setItemID(Item.getIdFromItem(item));
		itemDamage = damage;
        shootingEntity = entityliving;
		if (entityliving instanceof EntityPlayer)
		{
			canBePickedUp = 1;
		}
        setSize(0.5F, 0.5F);
        setLocationAndAngles(entityliving.posX, entityliving.posY + (double)entityliving.getEyeHeight(), entityliving.posZ, entityliving.rotationYaw, entityliving.rotationPitch);
        posX -= MathHelper.cos((rotationYaw / 180F) * (float)Math.PI) * 0.16F;
        posY -= 0.1D;
        posZ -= MathHelper.sin((rotationYaw / 180F) * (float)Math.PI) * 0.16F;
        setPosition(posX, posY, posZ);
        yOffset = 0F;
        motionX = -MathHelper.sin((rotationYaw / 180F) * (float)Math.PI) * MathHelper.cos((rotationPitch / 180F) * (float)Math.PI);
        motionZ = MathHelper.cos((rotationYaw / 180F) * (float)Math.PI) * MathHelper.cos((rotationPitch / 180F) * (float)Math.PI);
        motionY = -MathHelper.sin((rotationPitch / 180F) * (float)Math.PI);
        setThrowableHeading(motionX, motionY, motionZ, charge * 1.5F, 1F);
    }
	
    public LOTREntityProjectileBase(World world, EntityLivingBase entityliving, EntityLivingBase target, Item item, int damage, float charge, float inaccuracy)
    {
        super(world);
		setItemID(Item.getIdFromItem(item));
		itemDamage = damage;
        shootingEntity = entityliving;
		if (entityliving instanceof EntityPlayer)
		{
			canBePickedUp = 1;
		}
        setSize(0.5F, 0.5F);
        posY = entityliving.posY + (double)entityliving.getEyeHeight() - 0.1D;
        double d = target.posX - entityliving.posX;
        double d1 = target.posY + (double)target.getEyeHeight() - 0.7D - posY;
        double d2 = target.posZ - entityliving.posZ;
        double d3 = (double)MathHelper.sqrt_double(d * d + d2 * d2);
        if (d3 >= 1.0E-7D)
        {
            float f = (float)(Math.atan2(d2, d) * 180D / Math.PI) - 90F;
            float f1 = (float)(-(Math.atan2(d1, d3) * 180D / Math.PI));
            double d4 = d / d3;
            double d5 = d2 / d3;
            setLocationAndAngles(entityliving.posX + d4, posY, entityliving.posZ + d5, f, f1);
            yOffset = 0F;
            float d6 = (float)d3 * 0.2F;
            setThrowableHeading(d, d1 + (double)d6, d2, charge * 1.5F, inaccuracy);
        }
    }
	
	@Override
    public Entity getThrower()
	{
		return shootingEntity;
	}

    @Override
    public void setThrower(Entity entity)
	{
		shootingEntity = entity;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public boolean isInRangeToRenderDist(double d)
    {
        double d1 = boundingBox.getAverageEdgeLength() * 4D;
        d1 *= 64D;
        return d < d1 * d1;
    }

	@Override
	protected void entityInit()
	{
		dataWatcher.addObject(16, Integer.valueOf(0));
		dataWatcher.addObject(17, Byte.valueOf((byte)0));
	}
	
	public int getItemID()
	{
		return dataWatcher.getWatchableObjectInt(16);
	}
	
	public void setItemID(int i)
	{
		if (Item.getItemById(i) == null)
		{
			setDead();
		}
		else
		{
			dataWatcher.updateObject(16, Integer.valueOf(i));
		}
	}

	@Override
    public void setThrowableHeading(double d, double d1, double d2, float f, float f1)
    {
        float f2 = MathHelper.sqrt_double(d * d + d1 * d1 + d2 * d2);
        d /= f2;
        d1 /= f2;
        d2 /= f2;
        d += rand.nextGaussian() * 0.0075D * (double)f1;
        d1 += rand.nextGaussian() * 0.0075D * (double)f1;
        d2 += rand.nextGaussian() * 0.0075D * (double)f1;
        d *= f;
        d1 *= f;
        d2 *= f;
        motionX = d;
        motionY = d1;
        motionZ = d2;
        float f3 = MathHelper.sqrt_double(d * d + d2 * d2);
        prevRotationYaw = rotationYaw = (float)((Math.atan2(d, d2) * 180D) / Math.PI);
        prevRotationPitch = rotationPitch = (float)((Math.atan2(d1, f3) * 180D) / Math.PI);
        ticksInGround = 0;
    }

	@Override
    public void setVelocity(double d, double d1, double d2)
    {
        motionX = d;
        motionY = d1;
        motionZ = d2;
        if (prevRotationPitch == 0F && prevRotationYaw == 0F)
        {
            float f = MathHelper.sqrt_double(d * d + d2 * d2);
            prevRotationYaw = rotationYaw = (float)((Math.atan2(d, d2) * 180D) / Math.PI);
            prevRotationPitch = rotationPitch = (float)((Math.atan2(d1, f) * 180D) / Math.PI);
            prevRotationPitch = rotationPitch;
            prevRotationYaw = rotationYaw;
            setLocationAndAngles(posX, posY, posZ, rotationYaw, rotationPitch);
            ticksInGround = 0;
        }
    }

	@Override
    public void onUpdate()
    {
        super.onUpdate();
        if (prevRotationPitch == 0F && prevRotationYaw == 0F)
        {
            float f = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
            prevRotationYaw = rotationYaw = (float)((Math.atan2(motionX, motionZ) * 180D) / Math.PI);
            prevRotationPitch = rotationPitch = (float)((Math.atan2(motionY, f) * 180D) / Math.PI);
        }
        Block block = worldObj.getBlock(xTile, yTile, zTile);
        if (block != Blocks.air)
        {
        	block.setBlockBoundsBasedOnState(worldObj, xTile, yTile, zTile);
            AxisAlignedBB axisalignedbb = block.getCollisionBoundingBoxFromPool(worldObj, xTile, yTile, zTile);
            if (axisalignedbb != null && axisalignedbb.isVecInside(worldObj.getWorldVec3Pool().getVecFromPool(posX, posY, posZ)))
            {
                inGround = true;
            }
        }
        if (shake > 0)
        {
            shake--;
        }
        if (inGround)
        {
            Block j = worldObj.getBlock(xTile, yTile, zTile);
            int k = worldObj.getBlockMetadata(xTile, yTile, zTile);

            if (j == inTile && k == inData)
            {
                ticksInGround++;
				if (ticksInGround >= maxTicksInGround())
				{
					setDead();
				}
				if (isDamageable() && Item.getItemById(getItemID()) != null && itemDamage == Item.getItemById(getItemID()).getMaxDamage())
				{
					setDead();
				}
            }
            else
            {
                inGround = false;
                motionX *= (double)(rand.nextFloat() * 0.2F);
                motionY *= (double)(rand.nextFloat() * 0.2F);
                motionZ *= (double)(rand.nextFloat() * 0.2F);
                ticksInGround = 0;
                ticksInAir = 0;
            }
        }
		else
		{
			ticksInAir++;
			Vec3 vec3d = worldObj.getWorldVec3Pool().getVecFromPool(posX, posY, posZ);
			Vec3 vec3d1 = worldObj.getWorldVec3Pool().getVecFromPool(posX + motionX, posY + motionY, posZ + motionZ);
			MovingObjectPosition movingobjectposition = worldObj.func_147447_a(vec3d, vec3d1, false, true, false);
			vec3d = worldObj.getWorldVec3Pool().getVecFromPool(posX, posY, posZ);
			vec3d1 = worldObj.getWorldVec3Pool().getVecFromPool(posX + motionX, posY + motionY, posZ + motionZ);
			if (movingobjectposition != null)
			{
				vec3d1 = worldObj.getWorldVec3Pool().getVecFromPool(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
			}
			Entity entity = null;
			List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.addCoord(motionX, motionY, motionZ).expand(1D, 1D, 1D));
			double d = 0D;
			for (int l = 0; l < list.size(); l++)
			{
				Entity entity1 = (Entity)list.get(l);
				if (!entity1.canBeCollidedWith() || entity1 == shootingEntity && ticksInAir < 5)
				{
					continue;
				}
				float f5 = 0.3F;
				AxisAlignedBB axisalignedbb1 = entity1.boundingBox.expand(f5, f5, f5);
				MovingObjectPosition movingobjectposition1 = axisalignedbb1.calculateIntercept(vec3d, vec3d1);
				if (movingobjectposition1 == null)
				{
					continue;
				}
				double d1 = vec3d.distanceTo(movingobjectposition1.hitVec);
				if (d1 < d || d == 0D)
				{
					entity = entity1;
					d = d1;
				}
			}

			if (entity != null)
			{
				movingobjectposition = new MovingObjectPosition(entity);
			}
			if (movingobjectposition != null)
			{
				if (movingobjectposition.entityHit != null && movingobjectposition.entityHit != shootingEntity)
				{
					float damage = getDamageVsEntity(movingobjectposition.entityHit);
					
                    if (getIsCritical())
                    {
                        damage += rand.nextFloat() * (damage / 2F + 1F);
                    }

                    DamageSource damagesource = getDamageSource();
					
                    if (isBurning())
                    {
                        movingobjectposition.entityHit.setFire(5);
                    }
					
					if (movingobjectposition.entityHit.attackEntityFrom(damagesource, damage))
					{
                        if (movingobjectposition.entityHit instanceof EntityLiving)
                        {
                            EntityLivingBase entityliving = (EntityLiving)movingobjectposition.entityHit;
							
                            if (knockbackStrength > 0)
                            {
                                float knockback = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
                                if (knockback > 0F)
                                {
                                    movingobjectposition.entityHit.addVelocity(motionX * (double)knockbackStrength * 0.6D / (double)knockback, 0.1D, motionZ * (double)knockbackStrength * 0.6D / (double)knockback);
                                }
                            }

                            if (shootingEntity != null)
                            {
                                EnchantmentHelper.func_151384_a(entityliving, shootingEntity);
                                EnchantmentHelper.func_151385_b((EntityLivingBase)shootingEntity, entityliving);
                            }

                            if (shootingEntity != null && movingobjectposition.entityHit != shootingEntity && movingobjectposition.entityHit instanceof EntityPlayer && shootingEntity instanceof EntityPlayerMP)
                            {
                                ((EntityPlayerMP)shootingEntity).playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(6, 0F));
                            }
                        }
						
						worldObj.playSoundAtEntity(this, getImpactSound(), 1F, 1.2F / (rand.nextFloat() * 0.2F + 0.9F));
						onCollideWithLiving(movingobjectposition.entityHit);
					}
					else
					{
						motionX *= -0.1D;
						motionY *= -0.1D;
						motionZ *= -0.1D;
						rotationYaw += 180F;
						prevRotationYaw += 180F;
						ticksInAir = 0;
					}
				}
				else
				{
					xTile = movingobjectposition.blockX;
					yTile = movingobjectposition.blockY;
					zTile = movingobjectposition.blockZ;
					inTile = worldObj.getBlock(xTile, yTile, zTile);
					inData = worldObj.getBlockMetadata(xTile, yTile, zTile);
					motionX = (float)(movingobjectposition.hitVec.xCoord - posX);
					motionY = (float)(movingobjectposition.hitVec.yCoord - posY);
					motionZ = (float)(movingobjectposition.hitVec.zCoord - posZ);
					float f2 = MathHelper.sqrt_double(motionX * motionX + motionY * motionY + motionZ * motionZ);
					posX -= motionX / (double)f2 * 0.05D;
					posY -= motionY / (double)f2 * 0.05D;
					posZ -= motionZ / (double)f2 * 0.05D;
					worldObj.playSoundAtEntity(this, getImpactSound(), 1F, 1.2F / (rand.nextFloat() * 0.2F + 0.9F));
					inGround = true;
					shake = 7;
					setIsCritical(false);
                    if (inTile.getMaterial() != Material.air)
                    {
                        inTile.onEntityCollidedWithBlock(worldObj, xTile, yTile, zTile, this);
                    }
				}
			}
			
            if (getIsCritical())
            {
                for (int l = 0; l < 4; ++l)
                {
                    worldObj.spawnParticle("crit", posX + motionX * (double)l / 4D, posY + motionY * (double)l / 4D, posZ + motionZ * (double)l / 4D, -motionX, -motionY + 0.2D, -motionZ);
                }
            }
			
			posX += motionX;
			posY += motionY;
			posZ += motionZ;
			float f3 = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
			rotationYaw = (float)((Math.atan2(motionX, motionZ) * 180D) / Math.PI);
			for (rotationPitch = (float)((Math.atan2(motionY, f3) * 180D) / Math.PI); rotationPitch - prevRotationPitch < -180F; prevRotationPitch -= 360F) { }
			for (; rotationPitch - prevRotationPitch >= 180F; prevRotationPitch += 360F) { }
			for (; rotationYaw - prevRotationYaw < -180F; prevRotationYaw -= 360F) { }
			for (; rotationYaw - prevRotationYaw >= 180F; prevRotationYaw += 360F) { }
			rotationPitch = prevRotationPitch + (rotationPitch - prevRotationPitch) * 0.2F;
			rotationYaw = prevRotationYaw + (rotationYaw - prevRotationYaw) * 0.2F;
			float f4 = getSpeedReduction();
			if (isInWater())
			{
				for (int k1 = 0; k1 < 4; k1++)
				{
					float f7 = 0.25F;
					worldObj.spawnParticle("bubble", posX - motionX * (double)f7, posY - motionY * (double)f7, posZ - motionZ * (double)f7, motionX, motionY, motionZ);
				}
				f4 = 0.8F;
			}
			motionX *= f4;
			motionY *= f4;
			motionZ *= f4;
			motionY -= 0.05F;
			setPosition(posX, posY, posZ);
			func_145775_I();
		}
    }

	@Override
    public void writeEntityToNBT(NBTTagCompound nbt)
    {
        nbt.setInteger("xTile", xTile);
        nbt.setInteger("yTile", yTile);
        nbt.setInteger("zTile", zTile);
        nbt.setByte("inTile", (byte)Block.getIdFromBlock(inTile));
        nbt.setByte("inData", (byte)inData);
        nbt.setByte("shake", (byte)shake);
        nbt.setByte("inGround", (byte)(inGround ? 1 : 0));
		nbt.setInteger("itemID", getItemID());
		nbt.setInteger("itemDamage", itemDamage);
		nbt.setByte("pickup", (byte)canBePickedUp);
    }

	@Override
    public void readEntityFromNBT(NBTTagCompound nbt)
    {
        xTile = nbt.getInteger("xTile");
        yTile = nbt.getInteger("yTile");
        zTile = nbt.getInteger("zTile");
        inTile = Block.getBlockById(nbt.getByte("inTile") & 0xff);
        inData = nbt.getByte("inData") & 0xff;
        shake = nbt.getByte("shake") & 0xff;
        inGround = nbt.getByte("inGround") == 1;
        setItemID(nbt.getInteger("itemID"));
		itemDamage = nbt.getInteger("itemDamage");
		canBePickedUp = nbt.getByte("pickup");
    }
	
	public abstract boolean isDamageable();

	@Override
    public void onCollideWithPlayer(EntityPlayer entityplayer)
    {
        if (!worldObj.isRemote)
		{
			if (isDamageable())
			{
				boolean canPickUp = itemDamage < Item.getItemById(getItemID()).getMaxDamage() && canBePickedUp == 1;
				if (inGround && shake <= 0 && canPickUp)
				{
					ItemStack itemstack = new ItemStack(Item.getItemById(getItemID()), 1, itemDamage + 1);
					if (entityplayer.inventory.addItemStackToInventory(itemstack.copy()))
					{
						playSound("random.pop", 0.2F, ((rand.nextFloat() - rand.nextFloat()) * 0.7F + 1F) * 2F);
						setDead();
					}
					else
					{
						EntityItem entityitem = new EntityItem(worldObj, posX, posY, posZ, itemstack);
						entityitem.delayBeforeCanPickup = 0;
						worldObj.spawnEntityInWorld(entityitem);
						setDead();
					}
				}
			}
			else
			{
				boolean canPickUp = canBePickedUp == 1 || (canBePickedUp == 2 && entityplayer.capabilities.isCreativeMode);
				if (inGround && shake <= 0 && canPickUp)
				{
					ItemStack itemstack = new ItemStack(Item.getItemById(getItemID()));
					if (entityplayer.inventory.addItemStackToInventory(itemstack.copy()));
					{
						playSound("random.pop", 0.2F, ((rand.nextFloat() - rand.nextFloat()) * 0.7F + 1F) * 2F);
						setDead();
					}
				}
			}
		}
    }
	
    public void onCollideWithLiving(Entity entity)
    {
		if (isDamageable())
		{
			if (!worldObj.isRemote && shake <= 0 && itemDamage < Item.getItemById(getItemID()).getMaxDamage() && canBePickedUp == 1)
			{
				EntityItem entityitem = new EntityItem(worldObj, posX, posY, posZ, new ItemStack(Item.getItemById(getItemID()), 1, itemDamage + 1));
				entityitem.delayBeforeCanPickup = 0;
				worldObj.spawnEntityInWorld(entityitem);
				setDead();
			}
		}
		else
		{
			if (!worldObj.isRemote)
			{
				setDead();
			}
		}
    }
	
	@Override
    protected boolean canTriggerWalking()
    {
        return false;
    }
	
	@Override
    public float getShadowSize()
    {
        return 0F;
    }
	
	@Override
    public boolean canAttackWithItem()
    {
        return false;
    }
	
	public abstract float getDamageVsEntity(Entity entity);
	
	public DamageSource getDamageSource()
	{
		if (shootingEntity == null)
		{
			return DamageSource.causeThrownDamage(this, this);
		}
		else
		{
			return DamageSource.causeThrownDamage(this, shootingEntity);
		}
	}

    public void setIsCritical(boolean flag)
    {
        byte b0 = dataWatcher.getWatchableObjectByte(17);

        if (flag)
        {
            dataWatcher.updateObject(17, Byte.valueOf((byte)(b0 | 1)));
        }
        else
        {
            dataWatcher.updateObject(17, Byte.valueOf((byte)(b0 & -2)));
        }
    }
	
    public boolean getIsCritical()
    {
        byte b0 = dataWatcher.getWatchableObjectByte(17);
        return (b0 & 1) != 0;
    }
	
	public String getImpactSound()
	{
		return "random.bowhit";
	}
	
	public float getSpeedReduction()
	{
		return 0.99F;
	}
	
	public int maxTicksInGround()
	{
		return canBePickedUp == 1 ? 6000 : 1200;
	}
}
