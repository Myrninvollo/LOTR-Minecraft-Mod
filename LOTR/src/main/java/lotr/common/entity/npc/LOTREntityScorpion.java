package lotr.common.entity.npc;

import lotr.common.LOTRMod;
import lotr.common.entity.LOTREntities;
import lotr.common.entity.ai.LOTREntityAIAttackOnCollide;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public abstract class LOTREntityScorpion extends EntityMob
{
	private float scorpionWidth = -1F;
	private float scorpionHeight;
	
	public LOTREntityScorpion(World world)
	{
		super(world);
		setSize(1.4F, 0.9F);
		getNavigator().setAvoidsWater(true);
        tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(1, new LOTREntityAIAttackOnCollide(this, 1.2D, false, 0.8F));
        tasks.addTask(2, new EntityAIWander(this, 1D));
        tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 8F, 0.05F));
        tasks.addTask(4, new EntityAILookIdle(this));
        targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
        targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, LOTREntityNPC.class, 0, true));
	}
	
	protected int getRandomScorpionScale()
	{
		return rand.nextInt(3);
	}
	
	@Override
	protected void entityInit()
	{
		super.entityInit();
		dataWatcher.addObject(18, Byte.valueOf((byte)getRandomScorpionScale()));
		dataWatcher.addObject(19, Integer.valueOf(0));
	}
	
	public int getScorpionScale()
	{
		return dataWatcher.getWatchableObjectByte(18);
	}

	public void setScorpionScale(int i)
	{
		dataWatcher.updateObject(18, Byte.valueOf((byte)i));
	}
	
	public float getScorpionScaleAmount()
	{
		return 0.5F + (float)getScorpionScale() / 2F;
	}
	
	public int getStrikeTime()
	{
		return dataWatcher.getWatchableObjectInt(19);
	}
	
	public void setStrikeTime(int i)
	{
		dataWatcher.updateObject(19, Integer.valueOf(i));
	}
	
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(12D + (double)getScorpionScale() * 6D);
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.35D - (double)getScorpionScale() * 0.05D);
        getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(2D + (double)getScorpionScale());
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
		nbt.setByte("ScorpionScale", (byte)getScorpionScale());
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound nbt)
	{
		super.readEntityFromNBT(nbt);
		setScorpionScale(nbt.getByte("ScorpionScale"));
		getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(2D + (double)getScorpionScale());
	}
	
	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		rescaleScorpion(getScorpionScaleAmount());
		
		if (!worldObj.isRemote)
		{
			int i = getStrikeTime();
			if (i > 0)
			{
				setStrikeTime(i - 1);
			}
		}
	}
	
	@Override
    protected void setSize(float f, float f1)
    {
        boolean flag = scorpionWidth > 0F;
        scorpionWidth = f;
        scorpionHeight = f1;

        if (!flag)
        {
            rescaleScorpion(1F);
        }
    }
	
	private void rescaleScorpion(float f)
	{
		super.setSize(scorpionWidth * f, scorpionHeight * f);
	}
	
	@Override
	public boolean interact(EntityPlayer entityplayer)
	{
        ItemStack itemstack = entityplayer.inventory.getCurrentItem();
        if (itemstack != null && itemstack.getItem() == Items.glass_bottle)
        {
			itemstack.stackSize--;
            if (itemstack.stackSize == 1)
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
        	if (!worldObj.isRemote)
        	{
        		setStrikeTime(20);
        	}
            
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
					((EntityLivingBase)entity).addPotionEffect(new PotionEffect(Potion.poison.id, duration * 20, 0));
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
		int k = 1 + rand.nextInt(3) + rand.nextInt(i + 1);
		for (int j = 0; j < k; j++)
		{
			dropItem(Items.rotten_flesh, 1);
		}
    }
	
	@Override
	protected int getExperiencePoints(EntityPlayer entityplayer)
    {
		int i = getScorpionScale();
        return 2 + i + rand.nextInt(i + 2);
    }
	
	@Override
    public EnumCreatureAttribute getCreatureAttribute()
    {
        return EnumCreatureAttribute.ARTHROPOD;
    }

	@Override
    public boolean isPotionApplicable(PotionEffect effect)
    {
        if (effect.getPotionID() == Potion.poison.id)
		{
			return false;
		}
		return super.isPotionApplicable(effect);
    }
	
	@Override
    public ItemStack getPickedResult(MovingObjectPosition target)
    {
		return new ItemStack(LOTRMod.spawnEgg, 1, LOTREntities.getEntityID(this));
    }
}
