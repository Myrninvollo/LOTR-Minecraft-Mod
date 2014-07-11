package lotr.common.entity.item;

import lotr.common.LOTRFaction;
import lotr.common.LOTRMod;
import lotr.common.item.LOTRItemBanner;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class LOTREntityBanner extends Entity
{
	public static double PROTECTION_RANGE = 32D;
	
	public LOTREntityBanner(World world)
	{
		super(world);
		setSize(1F, 3F);
	}
	
	@Override
	protected void entityInit()
	{
		dataWatcher.addObject(18, Byte.valueOf((byte)0));
	}
	
	public int getBannerType()
	{
		return dataWatcher.getWatchableObjectByte(18);
	}
	
	public void setBannerType(int i)
	{
		dataWatcher.updateObject(18, Byte.valueOf((byte)i));
	}
	
	public LOTRFaction getBannerFaction()
	{
		int i = getBannerType();
		if (i < 0 || i >= LOTRItemBanner.factions.length)
		{
			i = 0;
		}
		return LOTRItemBanner.factions[i];
	}
	
	public boolean isProtectingTerritory()
	{
		int i = MathHelper.floor_double(posX);
		int j = MathHelper.floor_double(boundingBox.minY);
		int k = MathHelper.floor_double(posZ);
		return worldObj.getBlock(i, j - 1, k) == Blocks.gold_block;
	}
	
	@Override
	public boolean canBeCollidedWith()
	{
		return true;
	}
	
	@Override
    public AxisAlignedBB getBoundingBox()
    {
        return null;
    }
	
	@Override
    public void onUpdate()
    {
        super.onUpdate();
		
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        motionY -= 0.04D;

        func_145771_j(posX, (boundingBox.minY + boundingBox.maxY) / 2.0D, posZ);
        moveEntity(motionX, motionY, motionZ);
        float f = 0.98F;

        if (onGround)
        {
            f = 0.588F;
            Block block = worldObj.getBlock(MathHelper.floor_double(posX), MathHelper.floor_double(boundingBox.minY) - 1, MathHelper.floor_double(posZ));
            f = block.slipperiness * 0.98F;
        }

        motionX *= (double)f;
        motionY *= 0.98D;
        motionZ *= (double)f;

        if (onGround)
        {
            motionY *= -0.5D;
        }
    }
	
	@Override
    public void writeEntityToNBT(NBTTagCompound nbt)
    {
        nbt.setByte("BannerType", (byte)getBannerType());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt)
    {
		setBannerType(nbt.getByte("BannerType"));
    }
	
	@Override
    public boolean hitByEntity(Entity entity)
    {
		if (entity instanceof EntityPlayer)
		{
			return attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer)entity), 0F);
		}
		return false;
    }

    @Override
    public boolean attackEntityFrom(DamageSource damagesource, float f)
    {
		if (!isDead && !worldObj.isRemote)
		{
			setDead();
			setBeenAttacked();
			worldObj.playSoundAtEntity(this, Blocks.planks.stepSound.getBreakSound(), (Blocks.planks.stepSound.getVolume() + 1F) / 2F, Blocks.planks.stepSound.getPitch() * 0.8F);
			
			boolean flag = true;
			if (damagesource.getEntity() instanceof EntityPlayer && ((EntityPlayer)damagesource.getEntity()).capabilities.isCreativeMode)
			{
				flag = false;
			}
			
			if (flag)
			{
				entityDropItem(new ItemStack(LOTRMod.banner, 1, getBannerType()), 0F);
			}
		}
		return true;
    }
	
	@Override
    public ItemStack getPickedResult(MovingObjectPosition target)
    {
        return new ItemStack(LOTRMod.banner, 1, getBannerType());
    }
}
