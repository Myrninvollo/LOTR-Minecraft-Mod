package lotr.common.entity.npc;

import java.util.ArrayList;
import java.util.List;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRAlignmentValues;
import lotr.common.LOTRFaction;
import lotr.common.LOTRLevelData;
import lotr.common.LOTRMod;
import lotr.common.entity.ai.LOTREntityAIMTCJumpAttack;
import lotr.common.entity.ai.LOTRNPCTargetSelector;
import lotr.common.entity.projectile.LOTREntityThrownRock;
import lotr.common.world.structure.LOTRChestContents;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTREntityMountainTrollChieftain extends LOTREntityMountainTroll implements IBossDisplayData
{
	private static int SPAWN_TIME = 100;
	private int trollDeathTick;
	public boolean jumpAttack;
	private int healAmount;
	private EntityPlayer lastAttackingPlayer;
	
	public LOTREntityMountainTrollChieftain(World world)
	{
		super(world);
		setSize(3.2F, 6.4F);
		tasks.addTask(2, new LOTREntityAIMTCJumpAttack(this));
	}
	
	@Override
	protected EntityAIBase getTrollRangedAttackAI()
	{
		return new EntityAIArrowAttack(this, 1.2D, 20, 50, 24F);
	}
	
	@Override
	public void entityInit()
	{
		super.entityInit();
		dataWatcher.addObject(22, Integer.valueOf(0));
		dataWatcher.addObject(23, Integer.valueOf(-1));
		dataWatcher.addObject(24, Integer.valueOf(2));
	}
	
	public int getTrollSpawnTick()
	{
		return dataWatcher.getWatchableObjectInt(22);
	}
	
	public void setTrollSpawnTick(int i)
	{
		dataWatcher.updateObject(22, Integer.valueOf(i));
	}
	
	public int getHealingEntityID()
	{
		return dataWatcher.getWatchableObjectInt(23);
	}
	
	public void setHealingEntityID(int i)
	{
		dataWatcher.updateObject(23, Integer.valueOf(i));
	}
	
	public int getTrollArmorLevel()
	{
		return dataWatcher.getWatchableObjectInt(24);
	}
	
	public void setTrollArmorLevel(int i)
	{
		dataWatcher.updateObject(24, Integer.valueOf(i));
	}
	
	@Override
	public int getTotalArmorValue()
	{
		return 12;
	}
	
	public float getArmorLevelChanceModifier()
	{
		int i = 3 - getTrollArmorLevel();
		if (i < 1)
		{
			i = 1;
		}
		return (float)i;
	}
	
	public float getHealthChanceModifier()
	{
		float f = 1F - (getHealth() / getMaxHealth());
		return MathHelper.sqrt_float(f);
	}
	
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(50D);
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.2D);
        getEntityAttribute(npcAttackDamage).setBaseValue(8D);
		getEntityAttribute(thrownRockDamage).setBaseValue(8D);
    }
	
	public float getSpawningOffset(float f)
	{
		float f1 = ((float)getTrollSpawnTick() + f) / (float)SPAWN_TIME;
		if (f1 > 1F)
		{
			f1 = 1F;
		}
		return (1F - f1) * -5F;
	}
	
	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		
		if (getTrollSpawnTick() < SPAWN_TIME)
		{
			if (!worldObj.isRemote)
			{
				setTrollSpawnTick(getTrollSpawnTick() + 1);
				if (getTrollSpawnTick() == SPAWN_TIME)
				{
					doJumpAttack();
				}
			}
			else
			{
				for (int l = 0; l < 32; l++)
				{
					LOTRMod.proxy.spawnParticle("mtcSpawn", posX + rand.nextGaussian() * (double)width * 0.5D, posY + rand.nextDouble() * (double)height + (double)getSpawningOffset(0F), posZ + rand.nextGaussian() * (double)width * 0.5D, 0D, 0D, 0D);
				}
			}
		}
		
		if (!worldObj.isRemote && jumpAttack && worldObj.getWorldTime() % 5L == 0L && worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing"))
		{
			int xzRange = MathHelper.ceiling_float_int(width / 2F * 1.5F);
			int yRange = MathHelper.ceiling_float_int(height * 1.5F);
			int xzDist = xzRange * xzRange + xzRange * xzRange;
			int i = MathHelper.floor_double(posX);
			int j = MathHelper.floor_double(boundingBox.minY);
			int k = MathHelper.floor_double(posZ);
			for (int i1 = i - xzRange; i1 <= i + xzRange; i1++)
			{
				for (int j1 = j; j1 <= j + yRange; j1++)
				{
					for (int k1 = k - xzRange; k1 <= k + xzRange; k1++)
					{
						int i2 = i1 - i;
						int k2 = k1 - k;
						int dist = i2 * i2 + k2 * k2;
						if (dist < xzDist)
						{
							Block block = worldObj.getBlock(i1, j1, k1);
							if (block != null && !block.getMaterial().isLiquid())
							{
								float resistance = block.getExplosionResistance(this, worldObj, i1, j1, k1, posX, boundingBox.minY + (double)(height / 2F), posZ);
								if (resistance < 2000F)
								{
									block.dropBlockAsItemWithChance(worldObj, i1, j1, k1, worldObj.getBlockMetadata(i1, j1, k1), resistance / 100F, 0);
									worldObj.setBlockToAir(i1, j1, k1);
								}
							}
						}
					}
				}
			}
		}
		
		if (worldObj.isRemote && getTrollArmorLevel() == 0)
		{
			for (int i = 0; i < 4; i++)
			{
				worldObj.spawnParticle("largesmoke", posX + (rand.nextDouble() - 0.5D) * (double)width, posY + rand.nextDouble() * (double)height, posZ + (rand.nextDouble() - 0.5D) * (double)width, 0D, 0D, 0D);
			}
		}
	
		if (!worldObj.isRemote && (getTrollBurnTime() >= 0 || trollDeathTick > 0))
		{
			if (trollDeathTick == 0)
			{
				worldObj.playSoundAtEntity(this, "lotr:troll.transform", getSoundVolume(), getSoundPitch());
			}
			if (trollDeathTick % 5 == 0)
			{
				worldObj.setEntityState(this, (byte)15);
			}
			trollDeathTick++;
			rotationYaw += 60F * (rand.nextFloat() - 0.5F);
			rotationYawHead += 60F * (rand.nextFloat() - 0.5F);
			rotationPitch += 60F * (rand.nextFloat() - 0.5F);
			limbSwingAmount += 60F * (rand.nextFloat() - 0.5F);
			if (trollDeathTick >= 200)
			{
				setDead();
			}
		}
		
		if (!worldObj.isRemote && getHealth() < getMaxHealth())
		{
			float f = getHealthChanceModifier();
			f *= 0.02F;
			f *= getArmorLevelChanceModifier();
			if (rand.nextFloat() < f)
			{
				List nearbyTrolls = worldObj.getEntitiesWithinAABB(LOTREntityTroll.class, boundingBox.expand(24D, 8D, 24D));
				if (!nearbyTrolls.isEmpty())
				{
					LOTREntityTroll troll = (LOTREntityTroll)nearbyTrolls.get(rand.nextInt(nearbyTrolls.size()));
					if (!(troll instanceof LOTREntityMountainTrollChieftain) && troll.isEntityAlive())
					{
						setHealingEntityID(troll.getEntityId());
						healAmount = 5 + rand.nextInt(5);
					}
				}
			}
		}
		
		if (getHealingEntityID() != -1)
		{
			Entity entity = worldObj.getEntityByID(getHealingEntityID());
			if (entity != null && entity instanceof LOTREntityTroll && entity.isEntityAlive())
			{
				if (!worldObj.isRemote)
				{
					if (worldObj.getWorldTime() % 20L == 0L)
					{
						heal(5F);
						entity.attackEntityFrom(DamageSource.generic, 5F);
						healAmount--;
						if (!entity.isEntityAlive() || getHealth() >= getMaxHealth() || healAmount <= 0)
						{
							setHealingEntityID(-1);
						}
					}
				}
				else
				{
					double d = entity.posX;
					double d1 = entity.posY + ((double)entity.height / 2D);
					double d2 = entity.posZ;
					double d3 = posX - d;
					double d4 = posY + ((double)height / 2D) - d1;
					double d5 = posZ - d2;
					d3 /= 30D;
					d4 /= 30D;
					d5 /= 30D;
					LOTRMod.proxy.spawnParticle("mtcHeal", d, d1, d2, d3, d4, d5);
				}
			}
			else
			{
				if (!worldObj.isRemote)
				{
					setHealingEntityID(-1);
				}
			}
		}
		
		if (!worldObj.isRemote && getHealth() < getMaxHealth() && rand.nextInt(50) == 0 && !isThrowingRocks())
		{
			LOTREntityThrownRock rock = getThrownRock();
			if (rock.getSpawnsTroll())
			{
				rock.setLocationAndAngles(posX, posY + (double)height, posZ, 0F, 0F);
				rock.motionX = 0D;
				rock.motionY = 1.5D;
				rock.motionZ = 0D;
				worldObj.spawnEntityInWorld(rock);
				swingItem();
			}
		}
		
		if (!worldObj.isRemote && lastAttackingPlayer != null && (lastAttackingPlayer.posY - posY > 10D || getDistanceSqToEntity(lastAttackingPlayer) > 400F) && onGround)
		{
			float f = getHealthChanceModifier();
			f *= 0.05F;
			f *= getArmorLevelChanceModifier();
			if (rand.nextFloat() < f)
			{
				doJumpAttack();
				motionX = lastAttackingPlayer.posX - posX;
				motionY = lastAttackingPlayer.posY - posY;
				motionZ = lastAttackingPlayer.posZ - posZ;
				motionX /= 10D;
				motionY /= 10D;
				motionZ /= 10D;
				if (motionY < 1.5D)
				{
					motionY = 1.5D;
				}
				getLookHelper().setLookPositionWithEntity(lastAttackingPlayer, 100F, 100F);
				getLookHelper().onUpdateLook();
				rotationYaw = rotationYawHead;
			}
		}
		
		if (lastAttackingPlayer != null && (!lastAttackingPlayer.isEntityAlive() || lastAttackingPlayer.capabilities.isCreativeMode))
		{
			lastAttackingPlayer = null;
		}
	}
	
	@Override
	protected boolean isMovementBlocked()
	{
		if (getTrollSpawnTick() < SPAWN_TIME || trollDeathTick > 0)
		{
			return true;
		}
		return super.isMovementBlocked();
	}
	
	public void doJumpAttack()
	{
		jumpAttack = true;
		motionY = 1.5D;
	}
	
	@Override
	protected void fall(float f)
	{
		if (jumpAttack)
		{
			f = 0F;
			jumpAttack = false;
			if (!worldObj.isRemote)
			{
				List enemies = getNearbyEnemies();
				float attackDamage = (float)getEntityAttribute(LOTREntityNPC.npcAttackDamage).getAttributeValue();
				for (int i = 0; i < enemies.size(); i++)
				{
					EntityLivingBase entity = (EntityLivingBase)enemies.get(i);
					float strength = 12F - (getDistanceToEntity(entity) / 3F);
					strength /= 12F;
					entity.attackEntityFrom(DamageSource.causeMobDamage(this), strength * attackDamage * 3F);
					float knockback = strength * 3F;
					entity.addVelocity(-MathHelper.sin((rotationYaw * (float)Math.PI) / 180F) * knockback * 0.5F, 0.25D * knockback, MathHelper.cos((rotationYaw * (float)Math.PI) / 180F) * knockback * 0.5F);
				}
				worldObj.setEntityState(this, (byte)20);
				playSound("lotr:troll.rockSmash", 1.5F, 0.75F);
			}
		}
		super.fall(f);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void handleHealthUpdate(byte b)
	{
		if (b == 20)
		{
			for (int i = 0; i < 360; i += 2)
			{
				float angle = (float)Math.toRadians(i);
				double distance = 2D;
				double d = distance * MathHelper.sin(angle);
				double d1 = distance * MathHelper.cos(angle);
				LOTRMod.proxy.spawnParticle("largeStone", posX + d, boundingBox.minY + 0.1D, posZ + d1, d * 0.2D, 0.2D, d1 * 0.2D);
			}
		}
		else if (b == 21)
		{
			for (int i = 0; i < 64; i++)
			{
				LOTRMod.proxy.spawnParticle("mtcArmor", posX + (rand.nextDouble() - 0.5D) * (double)width, posY + rand.nextDouble() * (double)height, posZ + (rand.nextDouble() - 0.5D) * (double)width, 0D, 0D, 0D);
			}
		}
		else
		{
			super.handleHealthUpdate(b);
		}
	}
	
	public List getNearbyEnemies()
	{
		List enemies = new ArrayList();
		List players = worldObj.getEntitiesWithinAABB(EntityPlayer.class, boundingBox.expand(12D, 6D, 12D));
		for (int i = 0; i < players.size(); i++)
		{
			EntityPlayer entityplayer = (EntityPlayer)players.get(i);
			if (entityplayer.capabilities.isCreativeMode)
			{
				continue;
			}
			if (LOTRLevelData.getAlignment(entityplayer, LOTRFaction.ANGMAR) < 0)
			{
				enemies.add(entityplayer);
			}
		}
		enemies.addAll(worldObj.selectEntitiesWithinAABB(EntityLiving.class, boundingBox.expand(12D, 6D, 12D), new LOTRNPCTargetSelector(this)));
		return enemies;
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound nbt)
	{
		super.writeEntityToNBT(nbt);
		nbt.setInteger("TrollSpawnTick", getTrollSpawnTick());
		nbt.setInteger("TrollDeathTick", trollDeathTick);
		nbt.setBoolean("JumpAttack", jumpAttack);
		nbt.setInteger("TrollArmorLevel", getTrollArmorLevel());
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound nbt)
	{
		super.readEntityFromNBT(nbt);
		setTrollSpawnTick(nbt.getInteger("TrollSpawnTick"));
		trollDeathTick = nbt.getInteger("TrollDeathTick");
		jumpAttack = nbt.getBoolean("JumpAttack");
		setTrollArmorLevel(nbt.getInteger("TrollArmorLevel"));
	}
	
	@Override
	protected LOTREntityThrownRock getThrownRock()
	{
		LOTREntityThrownRock rock = super.getThrownRock();
		float f = 0.05F;
		List nearbyTrolls = worldObj.getEntitiesWithinAABB(LOTREntityTroll.class, boundingBox.expand(24D, 8D, 24D));
		int i = nearbyTrolls.size();
		i = 5 - i;
		f *= (float)i;
		f *= (float)getArmorLevelChanceModifier();
		if (rand.nextFloat() < f)
		{
			rock.setSpawnsTroll(true);
		}
		return rock;
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource damagesource, float f)
	{
		if (getTrollSpawnTick() < SPAWN_TIME || trollDeathTick > 0)
		{
			return false;
		}
		if (LOTRMod.playerSourceOfDamage(damagesource) == null && f > 1F)
		{
			f = 1F;
		}
		boolean flag = super.attackEntityFrom(damagesource, f);
		if (flag && damagesource.getEntity() instanceof EntityPlayer)
		{
			EntityPlayer entityplayer = (EntityPlayer)damagesource.getEntity();
			if (!entityplayer.capabilities.isCreativeMode)
			{
				lastAttackingPlayer = entityplayer;
			}
		}
		return flag;
	}
	
	@Override
	protected void damageEntity(DamageSource damagesource, float f)
    {
		super.damageEntity(damagesource, f);
		if (!worldObj.isRemote && getTrollArmorLevel() > 0 && getHealth() <= 0F)
		{
			setTrollArmorLevel(getTrollArmorLevel() - 1);
			if (getTrollArmorLevel() == 0)
			{
				double speed = getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue();
				speed *= 1.4D;
				getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(speed);
			}
			double maxHealth = getEntityAttribute(SharedMonsterAttributes.maxHealth).getAttributeValue();
			maxHealth *= 2D;
			getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(maxHealth);
			setHealth(getMaxHealth());
			worldObj.setEntityState(this, (byte)21);
		}
	}
	
	@Override
	public void onDeath(DamageSource damagesource)
	{
		super.onDeath(damagesource);
		
		if (!worldObj.isRemote)
		{
			EntityPlayer entityplayer = LOTRMod.playerSourceOfDamage(damagesource);
			if (entityplayer != null)
			{
				LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.killMountainTrollChieftain);
			}
		}
	}
	
	@Override
	public void dropFewItems(boolean flag, int i)
	{
		int drops = 3 + rand.nextInt(4);
		for (int j = 0; j < drops; j++)
		{
			dropTrollItems(flag, i);
		}
		
		int bones = 3 + rand.nextInt(4);
		for (int j = 0; j < bones; j++)
		{
			dropItem(LOTRMod.trollBone, 1);
		}
		
		int coins = 3 + rand.nextInt(4);
		for (int j = 0; j < coins; j++)
		{
			dropItem(LOTRMod.silverCoin, 10);
		}
		
		int valuables = 12 + rand.nextInt(8);
		IInventory valuableDrops = new InventoryBasic("temp", true, 100);
		LOTRChestContents.fillInventory(valuableDrops, rand, LOTRChestContents.TROLL_HOARD, valuables);
		for (int j = 0; j < valuableDrops.getSizeInventory(); j++)
		{
			ItemStack itemstack = valuableDrops.getStackInSlot(j);
			if (itemstack != null)
			{
				entityDropItem(itemstack, 0F);
			}
		}
	}
	
	@Override
	public int getAlignmentBonus()
	{
		return LOTRAlignmentValues.MOUNTAIN_TROLL_CHIEFTAIN_BONUS;
	}
	
	@Override
	protected int getExperiencePoints(EntityPlayer entityplayer)
    {
        return 500;
    }
}
