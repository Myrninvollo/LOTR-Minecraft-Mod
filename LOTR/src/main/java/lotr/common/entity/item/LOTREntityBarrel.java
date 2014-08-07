package lotr.common.entity.item;

import java.util.List;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRLevelData;
import lotr.common.LOTRMod;
import lotr.common.world.biome.LOTRBiomeGenMirkwoodRiver;
import net.minecraft.block.Block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTREntityBarrel extends Entity
{
    private boolean field_70279_a;
    private double speedMultiplier;
	private static double minSpeedMultiplier = 0.04D;
	private static double maxSpeedMultiplier = 0.25D;
    private int boatPosRotationIncrements;
    private double boatX;
    private double boatY;
    private double boatZ;
    private double boatYaw;
    private double boatPitch;
    private double velocityX;
    private double velocityY;
    private double velocityZ;

    public LOTREntityBarrel(World world)
    {
        super(world);
        field_70279_a = true;
        speedMultiplier = minSpeedMultiplier;
        preventEntitySpawning = true;
        setSize(1F, 1F);
		yOffset = 0F;
    }
	
    public LOTREntityBarrel(World world, double d, double d1, double d2)
    {
        this(world);
        setPosition(d, d1 + (double)yOffset, d2);
        motionX = 0D;
        motionY = 0D;
        motionZ = 0D;
        prevPosX = d;
        prevPosY = d1;
        prevPosZ = d2;
    }

    @Override
    protected boolean canTriggerWalking()
    {
        return false;
    }

	@Override
    protected void entityInit()
    {
        dataWatcher.addObject(17, Integer.valueOf(0));
        dataWatcher.addObject(18, Integer.valueOf(1));
        dataWatcher.addObject(19, Float.valueOf(0F));
    }

    @Override
    public AxisAlignedBB getCollisionBox(Entity par1Entity)
    {
        return par1Entity.boundingBox;
    }

    @Override
    public AxisAlignedBB getBoundingBox()
    {
        return boundingBox;
    }

    @Override
    public boolean canBePushed()
    {
        return true;
    }

	@Override
    public double getMountedYOffset()
    {
        return 0.5D;
    }

    @Override
    public boolean attackEntityFrom(DamageSource damagesource, float f)
    {
        if (isEntityInvulnerable())
        {
            return false;
        }
        else if (!worldObj.isRemote && !isDead)
        {
            setForwardDirection(-getForwardDirection());
            setTimeSinceHit(10);
            setDamageTaken(getDamageTaken() + f * 10F);
			SoundType stepSound = LOTRMod.barrel.stepSound;
			playSound(stepSound.getBreakSound(), (stepSound.getVolume() + 1F) / 2F, stepSound.getPitch() * 0.8F);
            setBeenAttacked();
            boolean isCreative = damagesource.getEntity() instanceof EntityPlayer && ((EntityPlayer)damagesource.getEntity()).capabilities.isCreativeMode;

            if (isCreative || getDamageTaken() > 40F)
            {
                if (riddenByEntity != null)
                {
                    riddenByEntity.mountEntity(this);
                }

                if (!isCreative)
                {
                    entityDropItem(new ItemStack(LOTRMod.barrel), 0F);
                }

                setDead();
            }

            return true;
        }
        else
        {
            return true;
        }
    }

	@Override
    @SideOnly(Side.CLIENT)
    public void performHurtAnimation()
    {
        setForwardDirection(-getForwardDirection());
        setTimeSinceHit(10);
        setDamageTaken(getDamageTaken() * 11F);
    }

    @Override
    public boolean canBeCollidedWith()
    {
        return !isDead;
    }

	@Override
    @SideOnly(Side.CLIENT)
    public void setPositionAndRotation2(double d, double d1, double d2, float f, float f1, int i)
    {
        if (field_70279_a)
        {
            boatPosRotationIncrements = i + 5;
        }
        else
        {
            double d3 = d - posX;
            double d4 = d1 - posY;
            double d5 = d2 - posZ;
            double d6 = d3 * d3 + d4 * d4 + d5 * d5;

            if (d6 <= 1D)
            {
                return;
            }

            boatPosRotationIncrements = 3;
        }

        boatX = d;
        boatY = d1;
        boatZ = d2;
        boatYaw = (double)f;
        boatPitch = (double)f1;
        motionX = velocityX;
        motionY = velocityY;
        motionZ = velocityZ;
    }

	@Override
    @SideOnly(Side.CLIENT)
    public void setVelocity(double par1, double par3, double par5)
    {
        velocityX = motionX = par1;
        velocityY = motionY = par3;
        velocityZ = motionZ = par5;
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();

        if (getTimeSinceHit() > 0)
        {
            setTimeSinceHit(getTimeSinceHit() - 1);
        }

        if (getDamageTaken() > 0F)
        {
            setDamageTaken(getDamageTaken() - 1F);
        }

        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        byte b0 = 5;
        double d0 = 0D;

        for (int i = 0; i < b0; ++i)
        {
            double d1 = boundingBox.minY + (boundingBox.maxY - boundingBox.minY) * (double)(i + 0) / (double)b0 - 0.125D;
            double d2 = boundingBox.minY + (boundingBox.maxY - boundingBox.minY) * (double)(i + 1) / (double)b0 - 0.125D;
            AxisAlignedBB axisalignedbb = AxisAlignedBB.getBoundingBox(boundingBox.minX, d1, boundingBox.minZ, boundingBox.maxX, d2, boundingBox.maxZ);

            if (worldObj.isAABBInMaterial(axisalignedbb, Material.water))
            {
                d0 += 1D / (double)b0;
            }
        }

        double d3 = Math.sqrt(motionX * motionX + motionZ * motionZ);
        double d4;
        double d5;

        if (d3 > 0.2625D)
        {
            d4 = Math.cos((double)rotationYaw * Math.PI / 180D);
            d5 = Math.sin((double)rotationYaw * Math.PI / 180D);

            for (int j = 0; (double)j < 1D + d3 * 60D; ++j)
            {
                double d6 = (double)(rand.nextFloat() * 2F - 1F);
                double d7 = (double)(rand.nextInt(2) * 2 - 1) * 0.7D;
                double d8;
                double d9;

                if (rand.nextBoolean())
                {
                    d8 = posX - d4 * d6 * 0.8D + d5 * d7;
                    d9 = posZ - d5 * d6 * 0.8D - d4 * d7;
                    worldObj.spawnParticle("splash", d8, posY - 0.125D, d9, motionX, motionY, motionZ);
                }
                else
                {
                    d8 = posX + d4 + d5 * d6 * 0.7D;
                    d9 = posZ + d5 - d4 * d6 * 0.7D;
                    worldObj.spawnParticle("splash", d8, posY - 0.125D, d9, motionX, motionY, motionZ);
                }
            }
        }

        double d10;
        double d11;

        if (worldObj.isRemote && field_70279_a)
        {
            if (boatPosRotationIncrements > 0)
            {
                d4 = posX + (boatX - posX) / (double)boatPosRotationIncrements;
                d5 = posY + (boatY - posY) / (double)boatPosRotationIncrements;
                d11 = posZ + (boatZ - posZ) / (double)boatPosRotationIncrements;
                d10 = MathHelper.wrapAngleTo180_double(boatYaw - (double)rotationYaw);
                rotationYaw = (float)((double)rotationYaw + d10 / (double)boatPosRotationIncrements);
                rotationPitch = (float)((double)rotationPitch + (boatPitch - (double)rotationPitch) / (double)boatPosRotationIncrements);
				boatPosRotationIncrements--;
                setPosition(d4, d5, d11);
                setRotation(rotationYaw, rotationPitch);
            }
            else
            {
                d4 = posX + motionX;
                d5 = posY + motionY;
                d11 = posZ + motionZ;
                setPosition(d4, d5, d11);

                if (onGround)
                {
                    motionX *= 0.5D;
                    motionY *= 0.5D;
                    motionZ *= 0.5D;
                }

                motionX *= 0.99D;
                motionY *= 0.95D;
                motionZ *= 0.99D;
            }
        }
        else
        {
            if (d0 < 1D)
            {
                d4 = d0 * 2D - 1D;
                motionY += 0.04D * d4;
            }
            else
            {
                if (motionY < 0D)
                {
                    motionY /= 2D;
                }

                motionY += 0.007D;
            }

            if (riddenByEntity != null && riddenByEntity instanceof EntityLivingBase)
            {
                d4 = (double)((EntityLivingBase)riddenByEntity).moveForward;

                if (d4 > 0D)
                {
                    d5 = -Math.sin((double)(riddenByEntity.rotationYaw * (float)Math.PI / 180F));
                    d11 = Math.cos((double)(riddenByEntity.rotationYaw * (float)Math.PI / 180F));
                    motionX += d5 * speedMultiplier * 0.05D;
                    motionZ += d11 * speedMultiplier * 0.05D;
                }
            }

            d4 = Math.sqrt(motionX * motionX + motionZ * motionZ);

            if (d4 > maxSpeedMultiplier)
            {
                d5 = maxSpeedMultiplier / d4;
                motionX *= d5;
                motionZ *= d5;
                d4 = maxSpeedMultiplier;
            }

            if (d4 > d3 && speedMultiplier < maxSpeedMultiplier)
            {
                speedMultiplier += (maxSpeedMultiplier - speedMultiplier) / (maxSpeedMultiplier / 150D);

                if (speedMultiplier > maxSpeedMultiplier)
                {
                    speedMultiplier = maxSpeedMultiplier;
                }
            }
            else
            {
                speedMultiplier -= (speedMultiplier - minSpeedMultiplier) / (maxSpeedMultiplier / 150D);

                if (speedMultiplier < minSpeedMultiplier)
                {
                    speedMultiplier = minSpeedMultiplier;
                }
            }

            if (onGround)
            {
                motionX *= 0.5D;
                motionY *= 0.5D;
                motionZ *= 0.5D;
            }

            moveEntity(motionX, motionY, motionZ);

			motionX *= 0.99D;
			motionY *= 0.95D;
			motionZ *= 0.99D;

            rotationPitch = 0F;
            d5 = (double)rotationYaw;
            d11 = prevPosX - posX;
            d10 = prevPosZ - posZ;

            if (d11 * d11 + d10 * d10 > 0.001D)
            {
                d5 = (double)((float)(Math.atan2(d10, d11) * 180D / Math.PI));
            }

            double d12 = MathHelper.wrapAngleTo180_double(d5 - (double)rotationYaw);

            if (d12 > 20D)
            {
                d12 = 20D;
            }

            if (d12 < -20D)
            {
                d12 = -20D;
            }

            rotationYaw = (float)((double)rotationYaw + d12);
            setRotation(rotationYaw, rotationPitch);

            if (!worldObj.isRemote)
            {
                List nearbyEntities = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.expand(0.2D, 0D, 0.2D));
                if (nearbyEntities != null && !nearbyEntities.isEmpty())
                {
                    for (int l = 0; l < nearbyEntities.size(); l++)
                    {
                        Entity entity = (Entity)nearbyEntities.get(l);
                        if (entity != riddenByEntity && entity.canBePushed() && entity instanceof LOTREntityBarrel)
                        {
                            entity.applyEntityCollision(this);
                        }
                    }
                }

                if (riddenByEntity != null && riddenByEntity.isDead)
                {
                    riddenByEntity = null;
                }
            }
        }
		
		if (!worldObj.isRemote && riddenByEntity instanceof EntityPlayer && worldObj.isAABBInMaterial(boundingBox, Material.water))
		{
			int i = MathHelper.floor_double(posX);
			int k = MathHelper.floor_double(posZ);
			if (worldObj.getBiomeGenForCoords(i, k) instanceof LOTRBiomeGenMirkwoodRiver)
			{
				LOTRLevelData.getData((EntityPlayer)riddenByEntity).addAchievement(LOTRAchievement.rideBarrelMirkwood);
			}
		}
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {}

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {}

	@Override
    @SideOnly(Side.CLIENT)
    public float getShadowSize()
    {
        return 0F;
    }

    @Override
    public boolean interactFirst(EntityPlayer entityplayer)
    {
        if (riddenByEntity != null && riddenByEntity instanceof EntityPlayer && riddenByEntity != entityplayer)
        {
            return true;
        }
        else
        {
            if (!worldObj.isRemote)
            {
                entityplayer.mountEntity(this);
            }

            return true;
        }
    }

    public void setDamageTaken(float f)
    {
        dataWatcher.updateObject(19, Float.valueOf(f));
    }

    public float getDamageTaken()
    {
        return dataWatcher.getWatchableObjectFloat(19);
    }

    public void setTimeSinceHit(int i)
    {
        dataWatcher.updateObject(17, Integer.valueOf(i));
    }

    public int getTimeSinceHit()
    {
        return dataWatcher.getWatchableObjectInt(17);
    }

    public void setForwardDirection(int i)
    {
        dataWatcher.updateObject(18, Integer.valueOf(i));
    }

    public int getForwardDirection()
    {
        return dataWatcher.getWatchableObjectInt(18);
    }
	
	@Override
    public ItemStack getPickedResult(MovingObjectPosition target)
    {
        return new ItemStack(LOTRMod.barrel);
    }
}
