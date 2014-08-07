package lotr.common.entity.npc;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRAlignmentValues;
import lotr.common.LOTRFaction;
import lotr.common.LOTRLevelData;
import lotr.common.LOTRMod;
import lotr.common.entity.ai.LOTREntityAIAttackOnCollide;
import lotr.common.entity.ai.LOTREntityAIFollowHiringPlayer;
import lotr.common.entity.ai.LOTREntityAIHiredRemainStill;
import lotr.common.entity.ai.LOTREntityAIHiringPlayerHurtByTarget;
import lotr.common.entity.ai.LOTREntityAIHiringPlayerHurtTarget;
import lotr.common.entity.ai.LOTREntityAINearestAttackableTargetTroll;
import lotr.common.entity.ai.LOTREntityAITrollFleeSun;
import lotr.common.entity.item.LOTREntityStoneTroll;
import lotr.common.world.biome.LOTRBiome;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIRestrictSun;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWatchClosest2;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTREntityTroll extends LOTREntityNPC
{
	private int sneeze;
	public int sniffTime;
	public boolean isImmuneToSun = false;
	
	public LOTREntityTroll(World world)
	{
		super(world);
		setSize(1.6F, 3.2F);
		getNavigator().setAvoidsWater(true);
        tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(1, new EntityAIRestrictSun(this));
		tasks.addTask(2, new LOTREntityAIHiredRemainStill(this));
		tasks.addTask(3, new LOTREntityAITrollFleeSun(this, 2.5D));
		tasks.addTask(4, getTrollAttackAI());
		tasks.addTask(5, new LOTREntityAIFollowHiringPlayer(this));
        tasks.addTask(6, new EntityAIWander(this, 1D));
        tasks.addTask(7, new EntityAIWatchClosest2(this, EntityPlayer.class, 12F, 0.05F));
        tasks.addTask(7, new EntityAIWatchClosest2(this, LOTREntityNPC.class, 8F, 0.05F));
        tasks.addTask(8, new EntityAIWatchClosest(this, EntityLiving.class, 12F, 0.01F));
        tasks.addTask(9, new EntityAILookIdle(this));
        targetTasks.addTask(1, new LOTREntityAIHiringPlayerHurtByTarget(this));
        targetTasks.addTask(2, new LOTREntityAIHiringPlayerHurtTarget(this));
        targetTasks.addTask(3, new EntityAIHurtByTarget(this, false));
        addTargetTasks(4, LOTREntityAINearestAttackableTargetTroll.class);
        spawnsInDarkness = true;
	}
	
	public EntityAIBase getTrollAttackAI()
	{
		return new LOTREntityAIAttackOnCollide(this, 1.4D, false, 0.8F);
	}
	
	@Override
	protected void entityInit()
	{
		super.entityInit();
		dataWatcher.addObject(16, Byte.valueOf((byte)rand.nextInt(3)));
		dataWatcher.addObject(17, LOTRNames.getRandomTrollName(rand));
		dataWatcher.addObject(18, Integer.valueOf(-1));
		dataWatcher.addObject(19, Byte.valueOf((byte)0));
	}
	
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(40D);
        getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(24D);
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.2D);
        getEntityAttribute(npcAttackDamage).setBaseValue(5D);
    }
	
	@Override
	public LOTRFaction getFaction()
	{
		return LOTRFaction.ANGMAR;
	}
	
	@Override
    public boolean isAIEnabled()
    {
        return true;
    }
	
	protected boolean hasTrollName()
	{
		return true;
	}
	
	@Override
	public String getNPCName()
	{
		if (hasTrollName())
		{
			return getTrollName();
		}
		return super.getNPCName();
	}
	
	public int getTrollOutfit()
	{
		return dataWatcher.getWatchableObjectByte(16);
	}
	
	public void setTrollOutfit(int i)
	{
		dataWatcher.updateObject(16, Byte.valueOf((byte)i));
	}
	
	public String getTrollName()
	{
		return dataWatcher.getWatchableObjectString(17);
	}
	
	public void setTrollName(String name)
	{
		dataWatcher.updateObject(17, name);
	}
	
	public int getTrollBurnTime()
	{
		return dataWatcher.getWatchableObjectInt(18);
	}
	
	public void setTrollBurnTime(int i)
	{
		dataWatcher.updateObject(18, Integer.valueOf(i));
	}
	
	public int getSneezingTime()
	{
		return dataWatcher.getWatchableObjectByte(19);
	}
	
	public void setSneezingTime(int i)
	{
		dataWatcher.updateObject(19, Byte.valueOf((byte)i));
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound nbt)
	{
		super.writeEntityToNBT(nbt);
		nbt.setByte("TrollOutfit", (byte)getTrollOutfit());
		nbt.setString("TrollName", getTrollName());
		nbt.setInteger("TrollBurnTime", getTrollBurnTime());
		nbt.setInteger("Sneeze", sneeze);
		nbt.setInteger("SneezeTime", getSneezingTime());
		nbt.setBoolean("ImmuneToSun", isImmuneToSun);
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound nbt)
	{
		super.readEntityFromNBT(nbt);
		setTrollOutfit(nbt.getByte("TrollOutfit"));
		if (nbt.hasKey("TrollName"))
		{
			setTrollName(nbt.getString("TrollName"));
		}
		setTrollBurnTime(nbt.getInteger("TrollBurnTime"));
		sneeze = nbt.getInteger("Sneeze");
		setSneezingTime(nbt.getInteger("SneezeTime"));
		isImmuneToSun = nbt.getBoolean("ImmuneToSun");
	}
	
	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		if (getTrollBurnTime() >= 0 && isEntityAlive())
		{
			if (!worldObj.isRemote)
			{
				BiomeGenBase biome = worldObj.getBiomeGenForCoords(MathHelper.floor_double(posX), MathHelper.floor_double(posZ));
				if (isImmuneToSun || (biome instanceof LOTRBiome && ((LOTRBiome)biome).canSpawnHostilesInDay()) || !worldObj.isDaytime() || !worldObj.canBlockSeeTheSky(MathHelper.floor_double(posX), (int)boundingBox.minY, MathHelper.floor_double(posZ)))
				{
					setTrollBurnTime(-1);
				}
				else
				{
					setTrollBurnTime(getTrollBurnTime() - 1);
					if (getTrollBurnTime() == 0)
					{
						onTrollDeathBySun();
						if (hiredNPCInfo.isActive && hiredNPCInfo.getHiringPlayer() != null)
						{
							hiredNPCInfo.getHiringPlayer().addChatMessage(new ChatComponentTranslation("lotr.hiredNPC.trollStone", new Object[] {getCommandSenderName()}));
						}
					}
				}
			}
			else
			{
				worldObj.spawnParticle("largesmoke", posX + (rand.nextDouble() - 0.5D) * (double)width, posY + rand.nextDouble() * (double)height, posZ + (rand.nextDouble() - 0.5D) * (double)width, 0D, 0D, 0D);
			}
		}
		
		if (sniffTime > 0)
		{
			sniffTime--;
		}
		
		if (!worldObj.isRemote && getSneezingTime() > 0)
		{
			setSneezingTime(getSneezingTime() - 1);
			if (getSneezingTime() == 8)
			{
				worldObj.playSoundAtEntity(this, "lotr:troll.sneeze", getSoundVolume() * 1.5F, getSoundPitch());
			}
			if (getSneezingTime() == 4)
			{
				int slimes = 2 + rand.nextInt(3);
				for (int i = 0; i < slimes; i++)
				{
					EntityItem entityitem = new EntityItem(worldObj, posX, posY + (double)getEyeHeight(), posZ, new ItemStack(Items.slime_ball));
					entityitem.delayBeforeCanPickup = 40;
					float f = 1F;
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
				}
			}
			if (getSneezingTime() == 0)
			{
				sneeze = 0;
			}
		}
	}
	
	public void onTrollDeathBySun()
	{
		worldObj.playSoundAtEntity(this, "lotr:troll.transform", getSoundVolume(), getSoundPitch());
		worldObj.setEntityState(this, (byte)15);
		setDead();
		LOTREntityStoneTroll stoneTroll = new LOTREntityStoneTroll(worldObj);
		stoneTroll.setLocationAndAngles(posX, posY, posZ, rotationYaw, 0F);
		stoneTroll.setTrollOutfit(getTrollOutfit());
		worldObj.spawnEntityInWorld(stoneTroll);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void handleHealthUpdate(byte b)
	{
		if (b == 15)
		{
			spawnExplosionParticle();
		}
		else if (b == 16)
		{
			sniffTime = 16;
		}
		else
		{
			super.handleHealthUpdate(b);
		}
	}
	
	@Override
	public boolean interact(EntityPlayer entityplayer)
	{
		if (!worldObj.isRemote && canTrollBeTickled(entityplayer))
		{
			ItemStack itemstack = entityplayer.inventory.getCurrentItem();
			if (itemstack != null && itemstack.getItem() == Items.feather && getSneezingTime() == 0)
			{
				if (rand.nextBoolean())
				{
					sneeze++;
				}
				if (!entityplayer.capabilities.isCreativeMode)
				{
					itemstack.stackSize--;
				}
				if (itemstack.stackSize <= 0)
				{
					entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, null);
				}
				npcTalkTick = getNPCTalkInterval() / 2;
				if (sneeze >= 3)
				{
					setSneezingTime(16);
					LOTRLevelData.getData(entityplayer).addAchievement(LOTRAchievement.makeTrollSneeze);
				}
				else
				{
					entityplayer.addChatMessage(LOTRSpeech.getNamedSpeechForPlayer(this, "troll_tickle", entityplayer));
					worldObj.playSoundAtEntity(this, "lotr:troll.sniff", getSoundVolume(), getSoundPitch());
					worldObj.setEntityState(this, (byte)16);
				}
			}
		}
		return super.interact(entityplayer);
	}
	
	protected boolean canTrollBeTickled(EntityPlayer entityplayer)
	{
		return canNPCTalk() && isFriendly(entityplayer) && LOTRLevelData.getData(entityplayer).getAlignment(getFaction()) <= LOTRAlignmentValues.TROLL_TRUST && getAttackTarget() == null && getTrollBurnTime() == -1;
	}
	
	@Override
    public void knockBack(Entity entity, float f, double d, double d1)
    {
        super.knockBack(entity, f, d, d1);
		motionX /= 2D;
		motionY /= 2D;
		motionZ /= 2D;
    }
	
	@Override
    public boolean attackEntityAsMob(Entity entity)
	{
		if (super.attackEntityAsMob(entity))
		{
			float attackDamage = (float)getEntityAttribute(LOTREntityNPC.npcAttackDamage).getAttributeValue();
			float knockbackModifier = 0.25F * attackDamage;
			entity.addVelocity((double)(-MathHelper.sin(rotationYaw * (float)Math.PI / 180F) * knockbackModifier * 0.5F), (double)knockbackModifier * 0.1D, (double)(MathHelper.cos(rotationYaw * (float)Math.PI / 180F) * knockbackModifier * 0.5F));
			return true;
		}
		return false;
	}
	
	@Override
	public void onDeath(DamageSource damagesource)
	{
		super.onDeath(damagesource);
		if (!worldObj.isRemote && damagesource.getEntity() instanceof EntityPlayer && getTrollBurnTime() >= 0)
		{
			LOTRLevelData.getData((EntityPlayer)damagesource.getEntity()).addAchievement(LOTRAchievement.killTrollFleeingSun);
		}
	}
	
	@Override
	protected LOTRAchievement getKillAchievement()
	{
		return LOTRAchievement.killTroll;
	}
	
	@Override
	public int getAlignmentBonus()
	{
		return LOTRAlignmentValues.TROLL_BONUS;
	}
	
	@Override
	protected int getExperiencePoints(EntityPlayer entityplayer)
    {
        return 4 + rand.nextInt(5);
    }
	
	@Override
	protected void dropFewItems(boolean flag, int i)
	{
		int j = 1 + rand.nextInt(2) + rand.nextInt(i + 1);
		for (int k = 0; k < j; k++)
		{
			dropItem(LOTRMod.trollBone, 1);
		}
		
		dropTrollItems(flag, i);
	}
	
	public void dropTrollItems(boolean flag, int i)
	{
		if (rand.nextInt(3) == 0)
		{
			int j = 1 + rand.nextInt(3) + rand.nextInt(i + 1);
			for (int k = 0; k < j; k++)
			{
				dropItem(Items.slime_ball, 1);
			}
		}
		
		int j = rand.nextInt(3) + rand.nextInt(i + 1);
		for (int k = 0; k < j; k++)
		{
			int l = rand.nextInt(8);
			switch (l)
			{
				case 0:
					entityDropItem(new ItemStack(Items.leather, 1 + rand.nextInt(3)), 0F);
					break;
				case 1:
					entityDropItem(new ItemStack(Items.beef, 1 + rand.nextInt(2)), 0F);
					break;
				case 2:
					entityDropItem(new ItemStack(Items.chicken, 1 + rand.nextInt(2)), 0F);
					break;
				case 3:
					entityDropItem(new ItemStack(Items.feather, 1 + rand.nextInt(3)), 0F);
					break;
				case 4:
					entityDropItem(new ItemStack(Items.porkchop, 1 + rand.nextInt(2)), 0F);
					break;
				case 5:
					entityDropItem(new ItemStack(Blocks.wool, 1 + rand.nextInt(3)), 0F);
					break;
				case 6:
					entityDropItem(new ItemStack(Items.rotten_flesh, 1 + rand.nextInt(3)), 0F);
					break;
				case 7:
					entityDropItem(new ItemStack(LOTRMod.rabbitRaw, 1 + rand.nextInt(2)), 0F);
					break;
			}
		}
	}
	
	@Override
	public String getLivingSound()
	{
		return "lotr:troll.say";
	}
	
	@Override
	protected float getSoundVolume()
	{
		return 1.5F;
	}
	
	@Override
	protected void func_145780_a(int i, int j, int k, Block block)
	{
		playSound("lotr:troll.step", 0.75F, getSoundPitch());
	}
	
	@Override
	public String getSpeechBank(EntityPlayer entityplayer)
	{
		if (getTrollBurnTime() >= 0)
		{
			return null;
		}
		else if (LOTRLevelData.getData(entityplayer).getAlignment(getFaction()) >= LOTRAlignmentValues.TROLL_TRUST && isFriendly(entityplayer))
		{
			if (hiredNPCInfo.getHiringPlayer() == entityplayer)
			{
				return "troll_hired";
			}
			else
			{
				return "troll_friendly";
			}
		}
		else
		{
			return "troll_hostile";
		}
	}
	
	public boolean shouldRenderHeadHurt()
	{
		return hurtTime > 0 || getSneezingTime() > 0;
	}
}
