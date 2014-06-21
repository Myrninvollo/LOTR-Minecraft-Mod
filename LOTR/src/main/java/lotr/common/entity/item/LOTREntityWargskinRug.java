package lotr.common.entity.item;

import lotr.common.LOTRMod;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class LOTREntityWargskinRug extends Entity
{
	private int timeSinceLastGrowl = 1200;
	
	public LOTREntityWargskinRug(World world)
	{
		super(world);
		setSize(1.8F, 0.3F);
	}
	
	@Override
	protected void entityInit()
	{
		dataWatcher.addObject(18, Byte.valueOf((byte)0));
	}
	
	public int getRugType()
	{
		return dataWatcher.getWatchableObjectByte(18);
	}
	
	public void setRugType(int i)
	{
		dataWatcher.updateObject(18, Byte.valueOf((byte)i));
	}
	
	@Override
	public boolean canBeCollidedWith()
    {
        return true;
    }
	
	@Override
    public AxisAlignedBB getBoundingBox()
    {
        return boundingBox;
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
            Block i = worldObj.getBlock(MathHelper.floor_double(posX), MathHelper.floor_double(boundingBox.minY) - 1, MathHelper.floor_double(posZ));
            if (i.getMaterial() != Material.air)
            {
                f = i.slipperiness * 0.98F;
            }
        }

        motionX *= (double)f;
        motionY *= 0.98D;
        motionZ *= (double)f;

        if (onGround)
        {
            motionY *= -0.5D;
        }
		
		if (!worldObj.isRemote)
		{
			timeSinceLastGrowl--;
			if (timeSinceLastGrowl == 0 && worldObj.getClosestPlayerToEntity(this, 8D) != null)
			{
				worldObj.playSoundAtEntity(this, "lotr:warg.growl", 1F, (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.2F + 1F);
				timeSinceLastGrowl = 20 * (30 + rand.nextInt(90));
			}
		}
    }
	
	@Override
    public void writeEntityToNBT(NBTTagCompound nbt)
    {
        nbt.setByte("RugType", (byte)getRugType());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt)
    {
		setRugType(nbt.getByte("RugType"));
    }
	
	public void dropRugAsItem(boolean creative)
	{
		worldObj.playSoundAtEntity(this, Blocks.wool.stepSound.getBreakSound(), (Blocks.wool.stepSound.getVolume() + 1F) / 2F, Blocks.wool.stepSound.getPitch() * 0.8F);
		if (!creative)
		{
			entityDropItem(new ItemStack(LOTRMod.wargskinRug, 1, getRugType()), 0F);
		}
		setDead();
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource damage, float f)
	{
		return false;
	}
	
	@Override
    public ItemStack getPickedResult(MovingObjectPosition target)
    {
        return new ItemStack(LOTRMod.wargskinRug, 1, getRugType());
    }
}
