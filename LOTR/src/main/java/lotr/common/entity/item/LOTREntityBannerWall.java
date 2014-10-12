package lotr.common.entity.item;

import lotr.common.*;
import lotr.common.item.LOTRItemBanner;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTREntityBannerWall extends EntityHanging
{
    public LOTREntityBannerWall(World world)
    {
        super(world);
        ignoreFrustumCheck = true;
    }

    public LOTREntityBannerWall(World world, int i, int j, int k, int l)
    {
        super(world, i, j, k, l);
        setDirection(l);
        ignoreFrustumCheck = true;
	}
	
	@Override
	public void setDirection(int l)
	{
		super.setDirection(l);
		prevRotationYaw = rotationYaw = (float)(Direction.rotateOpposite[l] * 90);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public boolean isInRangeToRenderDist(double d)
    {
        float f = width;
        float f1 = height;
        setSize(1F, 3F);
        boolean flag = super.isInRangeToRenderDist(d);
        setSize(f, f1);
        return flag;
    }
	
	@Override
	protected void entityInit()
	{
		dataWatcher.addObject(18, Byte.valueOf((byte)0));
	}
	
	private int getBannerType()
	{
		return dataWatcher.getWatchableObjectByte(18);
	}
	
	private void setBannerType(int i)
	{
		dataWatcher.updateObject(18, Byte.valueOf((byte)i));
	}
	
	public void setBannerFaction(LOTRFaction faction)
	{
		setBannerType(LOTRItemBanner.getSubtypeForFaction(faction));
	}
	
	public LOTRFaction getBannerFaction()
	{
		return LOTRItemBanner.getFaction(getBannerType());
	}
	
	@Override
    public void writeEntityToNBT(NBTTagCompound nbt)
    {
		super.writeEntityToNBT(nbt);
		nbt.setByte("BannerType", (byte)getBannerType());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt)
    {
		super.readEntityFromNBT(nbt);
		setBannerType(nbt.getByte("BannerType"));
    }

	@Override
    public int getWidthPixels()
    {
        return 16;
    }
	
	@Override
    public int getHeightPixels()
    {
        return 16;
    }
	
    @Override
    public boolean attackEntityFrom(DamageSource damagesource, float f)
    {
		if (!worldObj.isRemote && damagesource.getEntity() instanceof EntityPlayer)
		{
			EntityPlayer entityplayer = (EntityPlayer)damagesource.getEntity();
			if (LOTRBannerProtection.isProtectedByBanner(worldObj, this, LOTRBannerProtection.forPlayer(entityplayer), true))
			{
				return false;
			}
		}
		return super.attackEntityFrom(damagesource, f);
    }

    @Override
    public void onBroken(Entity entity)
    {
		worldObj.playSoundAtEntity(this, Blocks.planks.stepSound.getBreakSound(), (Blocks.planks.stepSound.getVolume() + 1F) / 2F, Blocks.planks.stepSound.getPitch() * 0.8F);
		
		boolean flag = true;
		if (entity instanceof EntityPlayer && ((EntityPlayer)entity).capabilities.isCreativeMode)
		{
			flag = false;
		}
		
		if (flag)
		{
			entityDropItem(new ItemStack(LOTRMod.banner, 1, getBannerType()), 0F);
		}
    }
	
	@Override
    public ItemStack getPickedResult(MovingObjectPosition target)
    {
        return new ItemStack(LOTRMod.banner, 1, getBannerType());
    }
}
