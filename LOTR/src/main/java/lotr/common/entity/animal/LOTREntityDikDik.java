package lotr.common.entity.animal;

import lotr.common.LOTRMod;
import lotr.common.entity.LOTREntities;
import lotr.common.entity.ai.LOTREntityAIAvoidWithChance;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class LOTREntityDikDik extends EntityCreature
{
	public LOTREntityDikDik(World world)
	{
		super(world);
        setSize(0.6F, 1F);
        getNavigator().setAvoidsWater(true);
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(1, new EntityAIAvoidEntity(this, LOTREntityLionBase.class, 12F, 1.5D, 2D));
		tasks.addTask(1, new LOTREntityAIAvoidWithChance(this, EntityPlayer.class, 12F, 1.5D, 2D, 0.1F));
        tasks.addTask(2, new EntityAIPanic(this, 2D));
        tasks.addTask(3, new EntityAIWander(this, 1.2D));
        tasks.addTask(4, new EntityAIWatchClosest(this, EntityPlayer.class, 8F));
        tasks.addTask(5, new EntityAILookIdle(this));
	}
	
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(12D);
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
    }
	
	@Override
	protected void entityInit()
	{
		super.entityInit();
		dataWatcher.addObject(20, Byte.valueOf((byte)getRNG().nextInt(3)));
	}
	
	@Override
    public boolean isAIEnabled()
    {
        return true;
    }
	
	public byte getDikdikType()
	{
		return dataWatcher.getWatchableObjectByte(20);
	}
	
	public void setDikdikType(byte b)
	{
		dataWatcher.updateObject(20, Byte.valueOf(b));
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound nbt)
	{
		super.writeEntityToNBT(nbt);
		nbt.setByte("DikdikType", getDikdikType());
	}
	
	@Override
    public void readEntityFromNBT(NBTTagCompound nbt)
    {
       	super.readEntityFromNBT(nbt);
		setDikdikType(nbt.getByte("DikdikType"));
	}
	
	@Override
    public float getBlockPathWeight(int i, int j, int k)
    {
		float f = -0.5F + worldObj.getLightBrightness(i, j, k);
		Block block = worldObj.getBlock(i, j - 1, k);
		if (block == Blocks.grass || block == Blocks.dirt)
		{
			f *= 2F;
		}
		return f;
    }
	
	@Override
    public ItemStack getPickedResult(MovingObjectPosition target)
    {
		return new ItemStack(LOTRMod.spawnEgg, 1, LOTREntities.getEntityID(this));
    }
}