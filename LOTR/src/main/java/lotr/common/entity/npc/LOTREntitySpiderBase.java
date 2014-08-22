package lotr.common.entity.npc;

import lotr.common.LOTRMod;
import lotr.common.entity.ai.*;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public abstract class LOTREntitySpiderBase extends LOTREntityNPC
{
	public static int VENOM_NONE = 0;
	public static int VENOM_SLOWNESS = 1;
	public static int VENOM_POISON = 2;
	
	public LOTREntitySpiderBase(World world)
	{
		super(world);
		setSize(1.4F, 0.9F);
		getNavigator().setAvoidsWater(true);
        tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(1, new LOTREntityAIHiredRemainStill(this));
		tasks.addTask(2, new EntityAILeapAtTarget(this, 0.4F));
		tasks.addTask(3, new LOTREntityAIAttackOnCollide(this, 1.2D, false, 0.8F));
		tasks.addTask(4, new LOTREntityAIFollowHiringPlayer(this));
        tasks.addTask(5, new EntityAIWander(this, 1D));
        tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8F, 0.02F));
        tasks.addTask(7, new EntityAILookIdle(this));
        targetTasks.addTask(1, new LOTREntityAIHiringPlayerHurtByTarget(this));
        targetTasks.addTask(2, new LOTREntityAIHiringPlayerHurtTarget(this));
        targetTasks.addTask(3, new EntityAIHurtByTarget(this, false));
        addTargetTasks(4);
        spawnsInDarkness = true;
	}
	
	protected abstract int getRandomSpiderScale();
	
	protected abstract int getRandomSpiderType();
	
	@Override
	protected void entityInit()
	{
		super.entityInit();
		dataWatcher.addObject(16, Byte.valueOf((byte)0));
		dataWatcher.addObject(17, Byte.valueOf((byte)0));
		dataWatcher.addObject(18, Byte.valueOf((byte)getRandomSpiderScale()));
		setSpiderType(getRandomSpiderType());
	}
	
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(12D + (double)getSpiderScale() * 6D);
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.35D - (double)getSpiderScale() * 0.05D);
        getEntityAttribute(npcAttackDamage).setBaseValue(2D + (double)getSpiderScale());
    }
	
    public boolean isBesideClimbableBlock()
    {
        return (dataWatcher.getWatchableObjectByte(16) & 1) != 0;
    }

    public void setBesideClimbableBlock(boolean flag)
    {
        byte b = dataWatcher.getWatchableObjectByte(16);

        if (flag)
        {
            b = (byte)(b | 1);
        }
        else
        {
            b &= -2;
        }

        dataWatcher.updateObject(16, Byte.valueOf(b));
    }
	
	public int getSpiderType()
	{
		return dataWatcher.getWatchableObjectByte(17);
	}

	public void setSpiderType(int i)
	{
		dataWatcher.updateObject(17, Byte.valueOf((byte)i));
	}
	
	public int getSpiderScale()
	{
		return dataWatcher.getWatchableObjectByte(18);
	}

	public void setSpiderScale(int i)
	{
		dataWatcher.updateObject(18, Byte.valueOf((byte)i));
	}
	
	public float getSpiderScaleAmount()
	{
		return 0.5F + (float)getSpiderScale() / 2F;
	}
	
	@Override
    public boolean isAIEnabled()
    {
        return true;
    }
	
	@Override
	public void writeEntityToNBT(NBTTagCompound nbt)
	{
		super.writeEntityToNBT(nbt);
		nbt.setByte("SpiderType", (byte)getSpiderType());
		nbt.setByte("SpiderScale", (byte)getSpiderScale());
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound nbt)
	{
		super.readEntityFromNBT(nbt);
		setSpiderType(nbt.getByte("SpiderType"));
		setSpiderScale(nbt.getByte("SpiderScale"));
		getEntityAttribute(npcAttackDamage).setBaseValue(2D + (double)getSpiderScale());
	}
	
	@Override
	protected float getNPCScale()
	{
		return getSpiderScaleAmount();
	}
	
	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		if (!worldObj.isRemote)
		{
			setBesideClimbableBlock(isCollidedHorizontally);
		}
	}
	
	@Override
    public double getMountedYOffset()
    {
		if (riddenByEntity instanceof EntityPlayer)
		{
			return (double)height * 0.95D;
		}
		else if (riddenByEntity instanceof LOTREntityNPC)
		{
			return (double)height * 0.5D;
		}
		return super.getMountedYOffset();
    }
	
	@Override
	public boolean interact(EntityPlayer entityplayer)
	{
        ItemStack itemstack = entityplayer.inventory.getCurrentItem();
        if (getSpiderType() == VENOM_POISON && itemstack != null && itemstack.getItem() == Items.glass_bottle)
        {
			itemstack.stackSize--;
            if (itemstack.stackSize <= 0)
            {
                entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, new ItemStack(LOTRMod.bottlePoison));
            }
            else if (!entityplayer.inventory.addItemStackToInventory(new ItemStack(LOTRMod.bottlePoison)) && !entityplayer.capabilities.isCreativeMode)
            {
                entityplayer.dropPlayerItemWithRandomChoice(new ItemStack(LOTRMod.bottlePoison), false);
            }
            return true;
        }
        else
        {
            return super.interact(entityplayer);
        }
	}

	@Override
	public boolean attackEntityAsMob(Entity entity)
    {
        if (super.attackEntityAsMob(entity))
        {
            if (entity instanceof EntityLivingBase)
            {
                byte duration = 0;

				if (worldObj.difficultySetting == EnumDifficulty.EASY)
				{
					duration = 3;
				}
				else if (worldObj.difficultySetting == EnumDifficulty.NORMAL)
				{
					duration = 7;
				}
				else if (worldObj.difficultySetting == EnumDifficulty.HARD)
				{
					duration = 15;
				}

                if (duration > 0)
                {
					if (getSpiderType() == VENOM_SLOWNESS)
					{
						((EntityLivingBase)entity).addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, duration * 20, 0));
					}
					else if (getSpiderType() == VENOM_POISON)
					{
						((EntityLivingBase)entity).addPotionEffect(new PotionEffect(Potion.poison.id, duration * 20, 0));
					}
                }
            }

            return true;
        }
        else
        {
            return false;
        }
    }
	
	@Override
    protected String getLivingSound()
    {
        return "mob.spider.say";
    }

   @Override
    protected String getHurtSound()
    {
        return "mob.spider.say";
    }

    @Override
    protected String getDeathSound()
    {
        return "mob.spider.death";
    }
	
	@Override
    protected void func_145780_a(int i, int j, int k, Block block)
    {
        playSound("mob.spider.step", 0.15F, 1F);
    }
	
	@Override
    protected void dropFewItems(boolean flag, int i)
    {
		int string = rand.nextInt(3) + rand.nextInt(i + 1);
		for (int j = 0; j < string; j++)
		{
			dropItem(Items.string, 1);
		}
		
        if (flag && (rand.nextInt(3) == 0 || rand.nextInt(1 + i) > 0))
        {
            dropItem(Items.spider_eye, 1);
        }
    }
	
	@Override
	public boolean canDropPouch()
	{
		return false;
	}
	
	@Override
	protected int getExperiencePoints(EntityPlayer entityplayer)
    {
		int i = getSpiderScale();
        return 2 + i + rand.nextInt(i + 2);
    }
	
	@Override
    public boolean isOnLadder()
    {
        return isBesideClimbableBlock();
    }

    @Override
    public void setInWeb() {}
	
	@Override
    public EnumCreatureAttribute getCreatureAttribute()
    {
        return EnumCreatureAttribute.ARTHROPOD;
    }

	@Override
    public boolean isPotionApplicable(PotionEffect effect)
    {
        if (getSpiderType() == VENOM_SLOWNESS && effect.getPotionID() == Potion.moveSlowdown.id)
		{
			return false;
		}
        else if (getSpiderType() == VENOM_POISON && effect.getPotionID() == Potion.poison.id)
		{
			return false;
		}
		return super.isPotionApplicable(effect);
    }
}
