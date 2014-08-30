package lotr.common.entity.npc;

import java.util.List;
import java.util.UUID;

import lotr.common.*;
import lotr.common.entity.ai.*;
import lotr.common.inventory.LOTRInventoryNPC;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTREntityGollum extends LOTREntityNPC implements LOTRCharacter
{
	private int eatingTick;
	public int prevFishTime = 400;
	public boolean isFishing;
	public LOTRInventoryNPC inventory = new LOTRInventoryNPC("gollum", this, 27);
	public int prevFishRequired = 20;
	public int fishRequired = prevFishRequired;

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
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(30D);
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
    }
	
	public String getGollumOwnerUUID()
	{
		return dataWatcher.getWatchableObjectString(17);
	}
	
	public void setGollumOwnerUUID(String s)
	{
		dataWatcher.updateObject(17, s);
	}
	
	public EntityPlayer getGollumOwner()
	{
		try
		{
			UUID uuid = UUID.fromString(getGollumOwnerUUID());
			return uuid == null ? null : worldObj.func_152378_a(uuid);
		}
		catch (IllegalArgumentException e)
		{
			return null;
		}
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
		nbt.setString("GollumOwnerUUID", getGollumOwnerUUID());
		nbt.setBoolean("GollumSitting", isGollumSitting());
		nbt.setInteger("GollumFishTime", prevFishTime);
		nbt.setInteger("FishReq", fishRequired);
		nbt.setInteger("FishReqPrev", prevFishRequired);
    }
	
    @Override
    public void readEntityFromNBT(NBTTagCompound nbt)
    {
        super.readEntityFromNBT(nbt);
		inventory.readFromNBT(nbt);
		if (nbt.hasKey("GollumOwnerUUID"))
		{
			setGollumOwnerUUID(nbt.getString("GollumOwnerUUID"));
		}
		setGollumSitting(nbt.getBoolean("GollumSitting"));
		prevFishTime = nbt.getInteger("GollumFishTime");
		if (nbt.hasKey("FishReq"))
		{
			fishRequired = nbt.getInteger("FishReq");
			prevFishRequired = nbt.getInteger("FishReqPrev");
		}
    }
	
	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		
		if (!worldObj.isRemote && rand.nextInt(500) == 0)
		{
			heal(1F);
		}
		
		if (eatingTick > 0)
		{
			if (eatingTick % 4 == 0)
			{
				worldObj.playSoundAtEntity(this, "random.eat", 0.5F + 0.5F * (float)rand.nextInt(2), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1F);
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
		
		if (!worldObj.isRemote && StringUtils.isNullOrEmpty(getGollumOwnerUUID()))
		{
			if (rand.nextInt(40) == 0)
			{
				List<EntityPlayer> nearbyPlayers = worldObj.getEntitiesWithinAABB(EntityPlayer.class, boundingBox.expand(80D, 80D, 80D));
				for (EntityPlayer entityplayer : nearbyPlayers)
				{
					double d = getDistanceToEntity(entityplayer);
					int chance = (int)(d / 8D);
					chance = Math.max(2, chance);
							
					if (rand.nextInt(chance) == 0)
					{
						worldObj.playSoundAtEntity(entityplayer, getLivingSound(), getSoundVolume(), getSoundPitch());
					}
				}
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
						if (itemstack.stackSize <= 0)
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
			else
			{
				ItemStack itemstack = entityplayer.inventory.getCurrentItem();
				if (itemstack != null && itemstack.getItem() == Items.fish)
				{
					boolean tamed = false;
					
					if (itemstack.stackSize >= fishRequired)
					{
						if (!entityplayer.capabilities.isCreativeMode)
						{
							itemstack.stackSize -= fishRequired;
							if (itemstack.stackSize <= 0)
							{
								entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, null);
							}
						}
						fishRequired = 0;
					}
					else
					{
						fishRequired -= itemstack.stackSize;
						if (!entityplayer.capabilities.isCreativeMode)
						{
							entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, null);
						}
					}
					
					eatingTick = 20;
					
					if (fishRequired <= 0)
					{
						setGollumOwnerUUID(entityplayer.getUniqueID().toString());
						entityplayer.addChatMessage(LOTRSpeech.getNamedSpeechForPlayer(this, "gollum_tame", entityplayer));
						LOTRSpeech.messageAllPlayers(new ChatComponentTranslation("chat.lotr.tameGollum", new Object[] {entityplayer.getCommandSenderName(), getCommandSenderName()}));
						spawnHearts();
						
						fishRequired = Math.round((float)prevFishRequired * (1.5F + rand.nextFloat() * 0.25F));
						prevFishRequired = fishRequired;
					}
					else
					{
						entityplayer.addChatMessage(LOTRSpeech.getNamedSpeechForPlayer(this, "gollum_tameProgress", entityplayer));
					}
					
					return true;
				}
			}
		}
		return super.interact(entityplayer);
	}
	
	@Override
	public String getSpeechBank(EntityPlayer entityplayer)
	{
		if (!isGollumFleeing())
		{
			return "gollum";
		}
		return super.getSpeechBank(entityplayer);
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
		EntityPlayer owner = getGollumOwner();
		if (owner != null && damagesource.getEntity() == owner)
		{
			f = 0F;
			if (!worldObj.isRemote)
			{
				owner.addChatMessage(LOTRSpeech.getNamedSpeechForPlayer(this, "gollum_hurt", owner));
			}
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
		if (!worldObj.isRemote && !StringUtils.isNullOrEmpty(getGollumOwnerUUID()))
		{
			LOTRSpeech.messageAllPlayers(func_110142_aN().func_151521_b());
		}
		
		super.onDeath(damagesource);
		
		if (!worldObj.isRemote)
		{
			inventory.dropAllItems();
			
			LOTRLevelData.gollumSpawned = false;
			LOTRLevelData.markDirty();
		}
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
	public String getSplashSound()
	{
		return super.getSplashSound();
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
