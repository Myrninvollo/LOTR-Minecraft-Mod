package lotr.common.entity.npc;

import lotr.common.LOTRCommonProxy;
import lotr.common.LOTRLevelData;
import lotr.common.LOTRMod;
import lotr.common.entity.ai.LOTREntityAIGollumAvoidEntity;
import lotr.common.entity.ai.LOTREntityAIGollumFishing;
import lotr.common.entity.ai.LOTREntityAIGollumFollowOwner;
import lotr.common.entity.ai.LOTREntityAIGollumPanic;
import lotr.common.entity.ai.LOTREntityAIGollumRemainStill;
import lotr.common.inventory.LOTRInventoryGollum;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTREntityGollum extends LOTREntityNPC
{
	private int eatingTick;
	public int prevFishTime = 400;
	public boolean isFishing;
	public LOTRInventoryGollum inventory = new LOTRInventoryGollum(this);
	public static String OWNER_NAME = "CaptainGlobox";

	public LOTREntityGollum(World world)
	{
		super(world);
		setSize(0.6F, 1.2F);
		getNavigator().setAvoidsWater(true);
		tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(1, new LOTREntityAIGollumRemainStill(this));
		tasks.addTask(2, new LOTREntityAIGollumPanic(this, 1.4D));
		tasks.addTask(3, new LOTREntityAIGollumAvoidEntity(this, LOTREntityOrc.class, 8F, 1.2D, 1.4D));
		tasks.addTask(3, new LOTREntityAIGollumAvoidEntity(this, LOTREntityElf.class, 8F, 1.2D, 1.4D));
		tasks.addTask(4, new LOTREntityAIGollumFishing(this, 1.5D));
		tasks.addTask(5, new LOTREntityAIGollumFollowOwner(this, 1.2D, 6F, 4F));
        tasks.addTask(6, new EntityAIWander(this, 1D));
        tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 8F, 0.1F));
        tasks.addTask(8, new EntityAILookIdle(this));
		isNPCPersistent = true;
	}
	
	@Override
	protected void entityInit()
	{
		super.entityInit();
		dataWatcher.addObject(17, "");
		dataWatcher.addObject(18, Byte.valueOf((byte)0));
		dataWatcher.addObject(19, Byte.valueOf((byte)0));
	}
	
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20D);
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
    }
	
	public String getGollumOwnerName()
	{
		return dataWatcher.getWatchableObjectString(17);
	}
	
	public void setGollumOwnerName(String s)
	{
		dataWatcher.updateObject(17, s);
	}
	
	public EntityPlayer getGollumOwner()
	{
		return worldObj.getPlayerEntityByName(getGollumOwnerName());
	}
	
	public boolean isGollumFleeing()
	{
		return dataWatcher.getWatchableObjectByte(18) == (byte)1;
	}
	
	public void setGollumFleeing(boolean flag)
	{
		dataWatcher.updateObject(18, Byte.valueOf(flag ? (byte)1 : (byte)0));
	}
	
	public boolean isGollumSitting()
	{
		return dataWatcher.getWatchableObjectByte(19) == (byte)1;
	}
	
	public void setGollumSitting(boolean flag)
	{
		dataWatcher.updateObject(19, Byte.valueOf(flag ? (byte)1 : (byte)0));
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
		inventory.writeToNBT(nbt);
		nbt.setString("GollumOwnerName", getGollumOwnerName());
		nbt.setBoolean("GollumSitting", isGollumSitting());
		nbt.setInteger("GollumFishTime", prevFishTime);
    }
	
    @Override
    public void readEntityFromNBT(NBTTagCompound nbt)
    {
        super.readEntityFromNBT(nbt);
		inventory.readFromNBT(nbt);
		setGollumOwnerName(nbt.getString("GollumOwnerName"));
		setGollumSitting(nbt.getBoolean("GollumSitting"));
		prevFishTime = nbt.getInteger("GollumFishTime");
    }
	
	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		
		if (eatingTick > 0)
		{
			if (eatingTick % 4 == 0)
			{
				worldObj.playSoundAtEntity(this, "random.eat", 0.5F + 0.5F * (float)rand.nextInt(2), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
			}
			eatingTick--;
		}
		
		if (prevFishTime > 0)
		{
			prevFishTime--;
		}
		
		if (isGollumSitting() && !worldObj.isRemote)
		{
			if (onGround)
			{
				getJumpHelper().setJumping();
			}
		}
		
		if (!worldObj.isRemote && getEquipmentInSlot(0) != null && getGollumOwner() != null)
		{
			double d = getDistanceSqToEntity(getGollumOwner());
			if (d < 4D)
			{
				getLookHelper().setLookPositionWithEntity(getGollumOwner(), 100F, 100F);
				getLookHelper().onUpdateLook();
				EntityItem entityitem = new EntityItem(worldObj, posX, posY + (double)getEyeHeight(), posZ, getEquipmentInSlot(0));
				entityitem.delayBeforeCanPickup = 40;
                float f = 0.3F;
                entityitem.motionX = (double)(-MathHelper.sin(rotationYawHead / 180.0F * (float)Math.PI) * MathHelper.cos(rotationPitch / 180.0F * (float)Math.PI) * f);
                entityitem.motionZ = (double)(MathHelper.cos(rotationYawHead / 180.0F * (float)Math.PI) * MathHelper.cos(rotationPitch / 180.0F * (float)Math.PI) * f);
                entityitem.motionY = (double)(-MathHelper.sin(rotationPitch / 180.0F * (float)Math.PI) * f + 0.1F);
                f = 0.02F;
                float f1 = rand.nextFloat() * (float)Math.PI * 2.0F;
                f *= rand.nextFloat();
                entityitem.motionX += Math.cos((double)f1) * (double)f;
                entityitem.motionY += (double)((rand.nextFloat() - rand.nextFloat()) * 0.1F);
                entityitem.motionZ += Math.sin((double)f1) * (double)f;
				worldObj.spawnEntityInWorld(entityitem);
				setCurrentItemOrArmor(0, null);
			}
		}
	}
	
	@Override
    public boolean interact(EntityPlayer entityplayer)
    {
		if (!worldObj.isRemote)
		{
			if (getGollumOwner() != null && entityplayer == getGollumOwner())
			{
				ItemStack itemstack = entityplayer.inventory.getCurrentItem();
				if (itemstack != null && itemstack.getItem() instanceof ItemFood && canGollumEat(itemstack) && getHealth() < getMaxHealth())
				{
					if (!entityplayer.capabilities.isCreativeMode)
					{
						itemstack.stackSize--;
						if (itemstack.stackSize == 0)
						{
							entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, null);
						}
					}
					heal(((ItemFood)itemstack.getItem()).func_150905_g(itemstack));
					eatingTick = 20;
					return true;
				}
				else if (entityplayer.isSneaking())
				{
					entityplayer.openGui(LOTRMod.instance, LOTRCommonProxy.GUI_ID_GOLLUM, worldObj, getEntityId(), 0, 0);
					return true;
				}
				else
				{
					setGollumSitting(!isGollumSitting());
					if (isGollumSitting())
					{
						entityplayer.addChatMessage(LOTRSpeech.getNamedSpeechForPlayer(this, "gollum_stay", getGollumOwner()));
					}
					else
					{
						entityplayer.addChatMessage(LOTRSpeech.getNamedSpeechForPlayer(this, "gollum_follow", getGollumOwner()));
					}
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean canGollumEat(ItemStack itemstack)
	{
		if (itemstack.getItem() == Items.fish || itemstack.getItem() == Items.cooked_fished)
		{
			return true;
		}
		ItemFood food = (ItemFood)itemstack.getItem();
		return food.isWolfsFavoriteMeat();
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource damagesource, float f)
	{
		if (getGollumOwner() != null && damagesource.getEntity() == getGollumOwner())
		{
			f = 0F;
			getGollumOwner().addChatMessage(LOTRSpeech.getNamedSpeechForPlayer(this, "gollum_hurt", getGollumOwner()));
		}
		if (super.attackEntityFrom(damagesource, f))
		{
			setGollumSitting(false);
			return true;
		}
		return false;
	}
	
	@Override
	public void onDeath(DamageSource damagesource)
	{
		super.onDeath(damagesource);
		if (!worldObj.isRemote)
		{
			inventory.dropAllItems();
		}
		if (getGollumOwner() != null)
		{
			getGollumOwner().addChatMessage(func_110142_aN().func_151521_b());
		}
		LOTRLevelData.hasGollum = 0;
		LOTRLevelData.gollumRespawnTime = 12000;
		LOTRLevelData.needsSave = true;
	}
	
	@Override
	public boolean canDropPouch()
	{
		return false;
	}
	
	@Override
	public String getLivingSound()
	{
		return "lotr:gollum.say";
	}
	
	@Override
	public String getHurtSound()
	{
		return "lotr:gollum.hurt";
	}
	
	@Override
	public String getDeathSound()
	{
		return "lotr:gollum.death";
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void handleHealthUpdate(byte b)
	{
		if (b == 15)
		{
			for (int i = 0; i < 4; i++)
			{
				double d = rand.nextGaussian() * 0.02D;
				double d1 = rand.nextGaussian() * 0.02D;
				double d2 = rand.nextGaussian() * 0.02D;
				worldObj.spawnParticle(rand.nextBoolean() ? "bubble" : "splash", posX + (double)(rand.nextFloat() * width * 2.0F) - (double)width, posY + 0.5D + (double)(rand.nextFloat() * height), posZ + (double)(rand.nextFloat() * width * 2.0F) - (double)width, d, d1, d2);
			}
		}
		else
		{
			super.handleHealthUpdate(b);
		}
	}
}
