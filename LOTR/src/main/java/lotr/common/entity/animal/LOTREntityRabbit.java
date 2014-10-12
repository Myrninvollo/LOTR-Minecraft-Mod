package lotr.common.entity.animal;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRLevelData;
import lotr.common.LOTRMod;
import lotr.common.entity.LOTREntities;
import lotr.common.entity.ai.LOTREntityAIAvoidWithChance;
import lotr.common.entity.ai.LOTREntityAIFlee;
import lotr.common.entity.ai.LOTREntityAIRabbitEatCrops;
import lotr.common.entity.npc.LOTREntityNPC;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class LOTREntityRabbit extends EntityCreature implements LOTRAmbientCreature
{
    public LOTREntityRabbit(World world)
    {
        super(world);
        setSize(0.5F, 0.5F);
        getNavigator().setAvoidsWater(true);
        tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(1, new LOTREntityAIFlee(this, 2D));
		tasks.addTask(2, new LOTREntityAIAvoidWithChance(this, EntityPlayer.class, 4F, 1.3D, 1.5D, 0.05F));
		tasks.addTask(2, new LOTREntityAIAvoidWithChance(this, LOTREntityNPC.class, 4F, 1.3D, 1.5D, 0.05F));
		tasks.addTask(3, new LOTREntityAIRabbitEatCrops(this, 1.2D));
        tasks.addTask(4, new EntityAIWander(this, 1D));
        tasks.addTask(5, new EntityAIWatchClosest(this, EntityLivingBase.class, 8F, 0.05F));
        tasks.addTask(6, new EntityAILookIdle(this));
    }

	@Override
    public void entityInit()
    {
        super.entityInit();
        dataWatcher.addObject(16, Byte.valueOf((byte)rand.nextInt(5)));
        dataWatcher.addObject(17, Byte.valueOf((byte)0));
    }
	
	public int getRabbitType()
	{
		return dataWatcher.getWatchableObjectByte(16);
	}
	
	public void setRabbitType(int i)
	{
		dataWatcher.updateObject(16, Byte.valueOf((byte)i));
	}
	
	public boolean isRabbitEating()
	{
		return dataWatcher.getWatchableObjectByte(17) == (byte)1;
	}
	
	public void setRabbitEating(boolean flag)
	{
		dataWatcher.updateObject(17, Byte.valueOf(flag ? (byte)1 : (byte)0));
	}

	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(4D);
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
    }

	@Override
    protected boolean isAIEnabled()
    {
        return true;
    }
	
	@Override
    public void readEntityFromNBT(NBTTagCompound nbt)
    {
        super.readEntityFromNBT(nbt);
		setRabbitType(nbt.getByte("RabbitType"));
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt)
    {
        super.writeEntityToNBT(nbt);
        nbt.setByte("RabbitType", (byte)getRabbitType());
    }
	
	@Override
	public boolean attackEntityFrom(DamageSource damagesource, float f)
	{
		boolean flag = super.attackEntityFrom(damagesource, f);
		if (flag && !worldObj.isRemote && damagesource.getEntity() instanceof EntityPlayer && isRabbitEating())
		{
			EntityPlayer entityplayer = (EntityPlayer)damagesource.getEntity();
			LOTRLevelData.getData(entityplayer).addAchievement(LOTRAchievement.attackRabbit);
		}
		return flag;
	}
	
	@Override
	public void dropFewItems(boolean flag, int i)
	{
        int j = rand.nextInt(3) + rand.nextInt(1 + i);
        for (int k = 0; k < j; k++)
        {
            if (isBurning())
            {
                dropItem(LOTRMod.rabbitCooked, 1);
            }
            else
            {
                dropItem(LOTRMod.rabbitRaw, 1);
            }
        }
	}
	
	@Override
    protected boolean canDespawn()
    {
        return true;
    }

    @Override
    public boolean getCanSpawnHere()
    {
		int i = MathHelper.floor_double(posX);
		int j = MathHelper.floor_double(posY);
		int k = MathHelper.floor_double(posZ);

        if (j < 62)
        {
            return false;
        }
		Block l = worldObj.getBlock(i, j - 1, k);
		if (l == Blocks.grass)
		{
            int light = worldObj.getBlockLightValue(i, j, k);
			return light < rand.nextInt(8) ? false : super.getCanSpawnHere();
        }
		return false;
    }
	
	@Override
    public float getBlockPathWeight(int i, int j, int k)
    {
        return worldObj.getBlock(i, j - 1, k) == Blocks.grass ? 10F : worldObj.getLightBrightness(i, j, k) - 0.5F;
    }
	
	@Override
    protected int getExperiencePoints(EntityPlayer entityplayer)
    {
        return 1 + worldObj.rand.nextInt(2);
    }
	
	@Override
    public ItemStack getPickedResult(MovingObjectPosition target)
    {
		int id = LOTREntities.getEntityID(this);
		if (id > 0 && LOTREntities.creatures.containsKey(id))
		{
			return new ItemStack(LOTRMod.spawnEgg, 1, id);
		}
		return null;
    }
}
