package lotr.common.entity.npc;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import lotr.common.LOTRAchievement;
import lotr.common.LOTREventHandler;
import lotr.common.LOTRFaction;
import lotr.common.LOTRLevelData;
import lotr.common.LOTRMod;
import lotr.common.entity.LOTREntities;
import lotr.common.entity.ai.LOTREntityAIBurningPanic;
import lotr.common.entity.ai.LOTREntityAINearestAttackableTargetBasic;
import lotr.common.entity.ai.LOTRNPCTargetSelector;
import lotr.common.entity.animal.LOTREntityHorse;
import lotr.common.entity.item.LOTREntityTraderRespawn;
import lotr.common.entity.projectile.LOTREntityPebble;
import lotr.common.entity.projectile.LOTREntityPlate;
import lotr.common.inventory.LOTRContainerTrade;
import lotr.common.inventory.LOTRContainerUnitTrade;
import lotr.common.item.LOTRItemBanner;
import lotr.common.item.LOTRItemLeatherHat;
import lotr.common.item.LOTRItemPouch;
import lotr.common.world.biome.LOTRBiome;
import net.minecraft.command.IEntitySelector;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S3FPacketCustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

import com.google.common.collect.Multimap;

public abstract class LOTREntityNPC extends EntityCreature
{
	public static IAttribute npcAttackDamage = new RangedAttribute("lotr.npcAttackDamage", 2D, 0D, Double.MAX_VALUE).setDescription("LOTR NPC Attack Damage");
	public static IAttribute horseAttackSpeed = new RangedAttribute("lotr.horseAttackSpeed", 1.7D, 0D, Double.MAX_VALUE).setDescription("LOTR Horse Attack Speed");

	private float npcWidth = -1F;
	private float npcHeight;
	public boolean isNPCPersistent = false;
	public int npcTalkTick = 0;
	public boolean liftSpawnRestrictions = false;
	public LOTRHiredNPCInfo hiredNPCInfo;
	private EntityLivingBase prevAttackTarget;
	public String npcLocationName;
	private boolean hasSpecificLocationName;
	private boolean hurtOnlyByPlates = true;
	public boolean spawnRidingHorse;
	private boolean ridingHorse;
	public int spawnCountValue = 1;
	private ItemStack[] festiveItems = new ItemStack[5];
	private Random festiveRand = new Random();
	private boolean initFestiveItems = false;
	public LOTRTraderNPCInfo traderNPCInfo;
	public boolean isPassive = false;
	public boolean isImmuneToFrost = false;
	protected boolean spawnsInDarkness = false;
	public LOTRFamilyInfo familyInfo;
	private boolean hasDefaultHeldItem = false;
	private ItemStack defaultHeldItem;
	
	public LOTREntityNPC(World world)
	{
		super(world);
		if (isTrader() || this instanceof IBossDisplayData)
		{
			isNPCPersistent = true;
		}
	}
	
	public final boolean isTrader()
	{
		return this instanceof LOTRTradeable || this instanceof LOTRUnitTradeable;
	}
	
	@Override
	protected void entityInit()
	{
		super.entityInit();
		hiredNPCInfo = new LOTRHiredNPCInfo(this);
		traderNPCInfo = new LOTRTraderNPCInfo(this);
		familyInfo = new LOTRFamilyInfo(this);
	}

	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getAttributeMap().registerAttribute(npcAttackDamage);
		getAttributeMap().registerAttribute(horseAttackSpeed);
    }
	
	public void addTargetTasks(int i)
	{
		addTargetTasks(i, LOTREntityAINearestAttackableTargetBasic.class);
	}
	
	public void addTargetTasks(int i, Class c)
	{
		addTargetTasks(this, i, c);
	}
	
	public static void addTargetTasks(EntityCreature entity, int i, Class c)
	{
		try
		{
			Constructor constructor = c.getConstructor(new Class[]{EntityCreature.class, Class.class, int.class, boolean.class, IEntitySelector.class});
			entity.targetTasks.addTask(i, (EntityAIBase)constructor.newInstance(new Object[]{entity, EntityPlayer.class, 0, true, null}));
			entity.targetTasks.addTask(i, (EntityAIBase)constructor.newInstance(new Object[]{entity, EntityLiving.class, 0, true, new LOTRNPCTargetSelector(entity)}));
		}
		catch (Exception e)
		{
			System.out.println("Error adding LOTR target tasks to entity " + entity.toString());
			e.printStackTrace();
		}
	}
	
	@Override
	public void onChunkLoad()
	{
		super.onChunkLoad();
		tasks.addTask(0, new LOTREntityAIBurningPanic(this, 1.5D));
	}
	
	public IEntityLivingData initCreatureForHire(IEntityLivingData data)
	{
		spawnRidingHorse = false;
		return onSpawnWithEgg(data);
	}
	
	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData data)
	{
		if (!worldObj.isRemote)
		{
			if (spawnRidingHorse && !(this instanceof LOTRBannerBearer))
			{
				LOTRNPCMount mount = createMountToRide();
				EntityCreature livingMount = (EntityCreature)mount;
				livingMount.setLocationAndAngles(posX, posY, posZ, rotationYaw, 0F);
				if (worldObj.func_147461_a(livingMount.boundingBox).isEmpty())
				{
					livingMount.onSpawnWithEgg(null);
					worldObj.spawnEntityInWorld(livingMount);
					mountEntity(livingMount);
					setRidingHorse(true);
					mount.setBelongsToNPC(true);
					mount.setNavigatorRangeFrom(this);
				}
			}
		}
		
		return data;
	}
	
	public LOTRNPCMount createMountToRide()
	{
		return new LOTREntityHorse(worldObj);
	}
	
	public void setRidingHorse(boolean flag)
	{
		ridingHorse = flag;
		double d = getEntityAttribute(SharedMonsterAttributes.followRange).getAttributeValue();
		if (flag)
		{
			d *= 1.5D;
		}
		else
		{
			d /= 1.5D;
		}
		getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(d);
	}
	
	@Override
	public String getCommandSenderName()
	{
		String entityName = getEntityClassName();
		String npcName = getNPCName();
		
		if (LOTRMod.isAprilFools())
		{
			npcName = "Gandalf";
		}
		
		if (npcName.equals(entityName))
		{
			return entityName;
		}
		else
		{
			return StatCollector.translateToLocalFormatted("entity.lotr.generic.entityName", new Object[] {npcName, entityName});
		}
	}
	
	public final String getEntityClassName()
	{
		return super.getCommandSenderName();
	}
	
	public String getNPCName()
	{
		return super.getCommandSenderName();
	}
	
	public LOTRFaction getFaction()
	{
		return LOTRFaction.UNALIGNED;
	}
	
	public int getNPCTalkInterval()
	{
		return 40;
	}
	
	public boolean canNPCTalk()
	{
		return isEntityAlive() && npcTalkTick == getNPCTalkInterval();
	}
	
	public void markNPCSpoken()
	{
		npcTalkTick = 0;
	}
	
	@Override
	public void setAttackTarget(EntityLivingBase target)
	{
		super.setAttackTarget(target);
		if (target != null && target != prevAttackTarget)
		{
			prevAttackTarget = target;
			if (getAttackSound() != null)
			{
				worldObj.playSoundAtEntity(this, getAttackSound(), getSoundVolume(), getSoundPitch());
			}
		}
	}
	
	public String getAttackSound()
	{
		return null;
	}
	
	@Override
    public int getTalkInterval()
    {
        return 200;
    }
	
	@Override
	public boolean isChild()
	{
		return familyInfo.getNPCAge() < 0;
	}
	
	public void changeNPCNameForMarriage(LOTREntityNPC spouse) {}
	
	public void createNPCChildName(LOTREntityNPC maleParent, LOTREntityNPC femaleParent) {}
	
	@Override
	public boolean canDespawn()
	{
		return !isNPCPersistent && !hiredNPCInfo.isActive;
	}
	
	@Override
    protected final void setSize(float f, float f1)
    {
        boolean flag = npcWidth > 0F;
        npcWidth = f;
        npcHeight = f1;

        if (!flag)
        {
        	rescaleNPC(1F);
        }
    }
	
	protected void rescaleNPC(float f)
	{
		super.setSize(npcWidth * f, npcHeight * f);
	}
	
	protected float getNPCScale()
	{
		return isChild() ? 0.5F : 1F;
	}
	
	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		
		familyInfo.onUpdate();
		rescaleNPC(getNPCScale());
		
		if (!worldObj.isRemote && isEntityAlive() && (isTrader() || hiredNPCInfo.isActive))
		{
			if ((getAttackTarget() == null || !getAttackTarget().isEntityAlive()) && getHealth() < getMaxHealth())
			{
				boolean timeHeal =  worldObj.getWorldTime() % 40L == 20L;
				boolean bannersHeal = false;
				
				if (hiredNPCInfo.isActive)
				{
					int banners = nearbyBanners();
					if (banners > 0)
					{
						bannersHeal = worldObj.getWorldTime() % (200L - (long)(banners * 30L)) == 0L;
					}
				}
				
				if (timeHeal || bannersHeal)
				{
					heal(1F);
					if (ridingEntity instanceof EntityLivingBase && !(ridingEntity instanceof LOTREntityNPC))
					{
						((EntityLivingBase)ridingEntity).heal(1F);
					}
				}
			}
		}
		
		hiredNPCInfo.onUpdate();
		
		if (!worldObj.isRemote && isEntityAlive() && (getAttackTarget() == null || !getAttackTarget().isEntityAlive()))
		{
			boolean flag = false;
			
			if (this instanceof LOTRTradeable)
			{
				for (int i = 0; i < worldObj.playerEntities.size(); i++)
				{
					EntityPlayer entityplayer = (EntityPlayer)worldObj.playerEntities.get(i);
					Container container = entityplayer.openContainer;
					if (container != null && container instanceof LOTRContainerTrade && ((LOTRContainerTrade)container).theTrader == this)
					{
						flag = true;
						break;
					}
				}
			}
			if (this instanceof LOTRUnitTradeable)
			{
				for (int i = 0; i < worldObj.playerEntities.size(); i++)
				{
					EntityPlayer entityplayer = (EntityPlayer)worldObj.playerEntities.get(i);
					Container container = entityplayer.openContainer;
					if (container != null && container instanceof LOTRContainerUnitTrade && ((LOTRContainerUnitTrade)container).theUnitTrader == this)
					{
						flag = true;
						break;
					}
				}
			}
			if (hiredNPCInfo.isActive && hiredNPCInfo.isGuiOpen)
			{
				flag = true;
			}
			
			if (flag)
			{
				getNavigator().clearPathEntity();
				if (ridingEntity instanceof LOTRNPCMount)
				{
					((EntityLiving)ridingEntity).getNavigator().clearPathEntity();
				}
			}
		}

		if (!isNPCPersistent && !hiredNPCInfo.isActive && spawnsInDarkness)
		{
			float f = getBrightness(1F);
			if (f > 0.5F)
			{
				entityAge += 2;
			}

			if (!worldObj.isRemote && worldObj.difficultySetting == EnumDifficulty.PEACEFUL)
			{
				setDead();
			}
		}
		
		updateArmSwingProgress();
		
		if (npcTalkTick < getNPCTalkInterval())
		{
			npcTalkTick++;
		}
		
		if (!worldObj.isRemote && hasHome())
		{
			if (hiredNPCInfo.isActive && hiredNPCInfo.isGuardMode())
			{
				if (!isWithinHomeDistanceCurrentPosition() && getAttackTarget() == null)
				{
					int homeX = getHomePosition().posX;
					int homeY = getHomePosition().posY;
					int homeZ = getHomePosition().posZ;
					int homeRange = hiredNPCInfo.getGuardRange();
					detachHome();
					Vec3 vec3 = null;
					for (int i = 0; i < 16 && vec3 == null; i++)
					{
						vec3 = RandomPositionGenerator.findRandomTargetBlockTowards(this, 16, 7, Vec3.createVectorHelper(homeX, homeY, homeZ));
					}
					if (vec3 != null)
					{
						getNavigator().tryMoveToXYZ(vec3.xCoord, vec3.yCoord, vec3.zCoord, 1D);
					}
					setHomeArea(homeX, homeY, homeZ, homeRange);
				}
			}
			else
			{
				double distanceSq = getHomePosition().getDistanceSquared(MathHelper.floor_double(posX), MathHelper.floor_double(boundingBox.minY), MathHelper.floor_double(posZ));
				double maxDistance = (double)func_110174_bM() + 8D;
				if (distanceSq > maxDistance * maxDistance)
				{
					detachHome();
				}
			}
		}
		
		if (!worldObj.isRemote && getAttackTarget() != null)
		{
			EntityLivingBase entity = getAttackTarget();
			if (!entity.isEntityAlive() || (entity instanceof EntityPlayer && ((EntityPlayer)entity).capabilities.isCreativeMode))
			{
				setAttackTarget(null);
			}
		}
	
		if (!worldObj.isRemote)
		{
			boolean isRidingHorseNow = ridingEntity instanceof EntityLiving && ridingEntity.isEntityAlive() && !(ridingEntity instanceof LOTREntityNPC);
			if (ridingHorse != isRidingHorseNow)
			{
				setRidingHorse(isRidingHorseNow);
			}
		}
		
		if (!worldObj.isRemote && this instanceof LOTRBannerBearer)
		{
			LOTRBannerBearer thisAsBannerBearer = (LOTRBannerBearer)this;
			ItemStack weapon = getEquipmentInSlot(0);
			if (getAttackTarget() == null)
			{
				if (weapon == null || weapon.getItem() != LOTRMod.banner)
				{
					setCurrentItemOrArmor(0, new ItemStack(LOTRMod.banner, 1, LOTRItemBanner.getSubtypeForFaction(getFaction())));
				}
			}
			else
			{
				if (weapon == null || weapon.getItem() != thisAsBannerBearer.getWeaponItem())
				{
					setCurrentItemOrArmor(0, new ItemStack(thisAsBannerBearer.getWeaponItem()));
				}
			}
		}
	}
	
	protected double getWeaponChangeThresholdRange()
	{
		double d = getEntityAttribute(SharedMonsterAttributes.followRange).getAttributeValue();
		return d * 0.95D;
	}
	
	protected double getWeaponChangeThresholdRangeSq()
	{
		double d = getWeaponChangeThresholdRange();
		return d * d;
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound nbt)
	{
		super.writeEntityToNBT(nbt);
		hiredNPCInfo.writeToNBT(nbt);
		traderNPCInfo.writeToNBT(nbt);
		familyInfo.writeToNBT(nbt);
		nbt.setInteger("NPCHomeX", getHomePosition().posX);
		nbt.setInteger("NPCHomeY", getHomePosition().posY);
		nbt.setInteger("NPCHomeZ", getHomePosition().posZ);
		nbt.setInteger("NPCHomeRadius", (int)func_110174_bM());
		nbt.setBoolean("NPCPersistent", isNPCPersistent);
		if (npcLocationName != null)
		{
			nbt.setString("NPCLocationName", npcLocationName);
		}
		nbt.setBoolean("SpecificLocationName", hasSpecificLocationName);
		nbt.setBoolean("HurtOnlyByPlates", hurtOnlyByPlates);
		nbt.setBoolean("RidingHorse", ridingHorse);
		nbt.setBoolean("NPCPassive", isPassive);
		nbt.setBoolean("HasDefaultHeldItem", hasDefaultHeldItem);
		if (hasDefaultHeldItem)
		{
			NBTTagCompound itemData = new NBTTagCompound();
			if (defaultHeldItem != null)
			{
				defaultHeldItem.writeToNBT(itemData);
			}
			nbt.setTag("DefaultHeldItem", itemData);
		}
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound nbt)
	{
		super.readEntityFromNBT(nbt);
		hiredNPCInfo.readFromNBT(nbt);
		traderNPCInfo.readFromNBT(nbt);
		familyInfo.readFromNBT(nbt);
		setHomeArea(nbt.getInteger("NPCHomeX"), nbt.getInteger("NPCHomeY"), nbt.getInteger("NPCHomeZ"), nbt.getInteger("NPCHomeRadius"));
		isNPCPersistent = nbt.getBoolean("NPCPersistent");
		if (nbt.hasKey("NPCLocationName"))
		{
			npcLocationName = nbt.getString("NPCLocationName");
		}
		hasSpecificLocationName = nbt.getBoolean("SpecificLocationName");
		hurtOnlyByPlates = nbt.getBoolean("HurtOnlyByPlates");
		ridingHorse = nbt.getBoolean("RidingHorse");
		isPassive = nbt.getBoolean("NPCPassive");
		hasDefaultHeldItem = nbt.getBoolean("HasDefaultHeldItem");
		if (hasDefaultHeldItem)
		{
			NBTTagCompound itemData = nbt.getCompoundTag("DefaultHeldItem");
			if (itemData.hasNoTags())
			{
				setCurrentItemOrArmor(0, null);
			}
			else
			{
				defaultHeldItem = ItemStack.loadItemStackFromNBT(itemData);
				setCurrentItemOrArmor(0, defaultHeldItem);
			}
			clearDefaultHeldItem();
		}
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
	
	@Override
    public boolean attackEntityAsMob(Entity entity)
    {
        float damage = (float)getEntityAttribute(npcAttackDamage).getAttributeValue();
        float weaponDamage = 0F;
        ItemStack weapon = getEquipmentInSlot(0);
        
        if (weapon != null)
        {
        	Item weaponItem = weapon.getItem();
        	Multimap weaponAttributes = weaponItem.getItemAttributeModifiers();
        	if (weaponAttributes != null)
        	{
                Iterator iterator = weaponAttributes.keySet().iterator();
                while (iterator.hasNext())
                {
                    Object key = iterator.next();
                    if (key.equals(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName()))
                    {
                    	Collection values = weaponAttributes.get(key);
                		for (Object obj : values)
                		{
                			if (obj instanceof AttributeModifier)
                			{
                				AttributeModifier mod = (AttributeModifier)obj;
                				weaponDamage += (float)mod.getAmount() * 0.75F;
                			}
                		}
                    }
                }
        	}
        }
        
        if (weaponDamage > 0F)
        {
        	damage = weaponDamage;
        }

        int knockbackModifier = 0;

        if (entity instanceof EntityLivingBase)
        {
            damage += EnchantmentHelper.getEnchantmentModifierLiving(this, (EntityLivingBase)entity);
            knockbackModifier += EnchantmentHelper.getKnockbackModifier(this, (EntityLivingBase)entity);
        }
        
        int banners = nearbyBanners();
        damage += (float)banners * 0.5F;

        boolean flag = entity.attackEntityFrom(DamageSource.causeMobDamage(this), damage);

        if (flag)
        {
            if (weapon != null && entity instanceof EntityLivingBase)
            {
				int weaponItemDamage = weapon.getItemDamage();
                weapon.getItem().hitEntity(weapon, (EntityLivingBase)entity, this);
				weapon.setItemDamage(weaponItemDamage);
            }
			
            if (knockbackModifier > 0)
            {
                entity.addVelocity((double)(-MathHelper.sin(rotationYaw * (float)Math.PI / 180.0F) * (float)knockbackModifier * 0.5F), 0.1D, (double)(MathHelper.cos(rotationYaw * (float)Math.PI / 180.0F) * (float)knockbackModifier * 0.5F));
                motionX *= 0.6D;
                motionZ *= 0.6D;
            }

            int fireAspectModifier = EnchantmentHelper.getFireAspectModifier(this);

            if (fireAspectModifier > 0)
            {
                entity.setFire(fireAspectModifier * 4);
            }

            if (entity instanceof EntityLivingBase)
            {
                EnchantmentHelper.func_151384_a((EntityLivingBase)entity, this);
            }

            EnchantmentHelper.func_151385_b(this, entity);
        }

        return flag;
    }
	
	@Override
	public void onKillEntity(EntityLivingBase entity)
	{
		super.onKillEntity(entity);
		hiredNPCInfo.onKillEntity(entity);
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource damagesource, float f)
	{
		if (riddenByEntity != null && damagesource.getEntity() == riddenByEntity)
		{
			return false;
		}
		
		int banners = nearbyBanners();
		if (banners > 0)
		{
            int i = 12 - banners;
            float f1 = f * (float)i;
            f = f1 / 12F;
		}
		
		boolean flag = super.attackEntityFrom(damagesource, f);
		if (flag && damagesource.getEntity() instanceof LOTREntityNPC)
		{
			LOTREntityNPC attacker = (LOTREntityNPC)damagesource.getEntity();
			if (attacker.hiredNPCInfo.isActive && attacker.hiredNPCInfo.getHiringPlayer() != null)
			{
				recentlyHit = 100;
				attackingPlayer = null;
			}
		}
		
		if (flag && !worldObj.isRemote && hurtOnlyByPlates)
		{
			hurtOnlyByPlates = damagesource.getSourceOfDamage() instanceof LOTREntityPlate;
		}
		
		return flag;
	}
	
	@Override
	protected void dropFewItems(boolean flag, int i)
	{
		dropNPCEquipment(flag, i);
	}
	
	public void dropNPCEquipment(boolean flag, int i)
	{
		if (flag)
		{
			int equipmentCount = 0;
			for (int j = 0; j < 5; j++)
			{
				if (getEquipmentInSlot(j) != null)
				{
					equipmentCount++;
				}
			}
			
			if (equipmentCount > 0)
			{
				equipmentDropLoop:
				for (int j = 0; j < 5; j++)
				{
					ItemStack equipmentDrop = getEquipmentInSlot(j);
					if (equipmentDrop != null)
					{
						boolean dropGuaranteed = equipmentDropChances[j] >= 1F;

						if (!dropGuaranteed)
						{
							int chance = (20 * equipmentCount) - (i * 4 * equipmentCount);
							if (chance < 1)
							{
								chance = 1;
							}
							
							if (rand.nextInt(chance) != 0)
							{
								continue equipmentDropLoop;
							}
						}
						
						
						if (!dropGuaranteed)
						{
							int dropDamage = MathHelper.floor_double(equipmentDrop.getItem().getMaxDamage() * (0.5F + rand.nextFloat() * 0.25F));
							equipmentDrop.setItemDamage(dropDamage);
						}
						
						entityDropItem(equipmentDrop, 0F);
						setCurrentItemOrArmor(j, null);
					}
				}
			}
		}
	}
	
	@Override
	public void onDeath(DamageSource damagesource)
	{
		super.onDeath(damagesource);
		
		hiredNPCInfo.onDeath(damagesource);
		
		if (!worldObj.isRemote && damagesource.getEntity() instanceof EntityPlayer)
		{
			EntityPlayer entityplayer = (EntityPlayer)damagesource.getEntity();
			
			if (canDropPouch())
			{
				if (worldObj.getGameRules().getGameRuleBooleanValue("doMobLoot") && rand.nextInt(50) == 0)
				{
					int pouchSize = LOTRItemPouch.getRandomPouchSize(rand);
					entityDropItem(new ItemStack(LOTRMod.pouch, 1, pouchSize), 0F);
				}
			}
			
			if (hurtOnlyByPlates && damagesource.getSourceOfDamage() instanceof LOTREntityPlate)
			{
				if (LOTRLevelData.getAlignment(entityplayer, getFaction()) < 0);
				{
					LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.killUsingOnlyPlates);
				}
			}
			
			if (damagesource.getSourceOfDamage() instanceof LOTREntityPebble)
			{
				float size = width * width * height;
				if (size > 5F)
				{
					int alignment = LOTRLevelData.getAlignment(entityplayer, getFaction());
					if (alignment < 0)
					{
						LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.killLargeMobWithSlingshot);
					}
				}
			}
			
			if (getKillAchievement() != null)
			{
				LOTRLevelData.addAchievement(entityplayer, getKillAchievement());
			}
		}
		
		if (!worldObj.isRemote)
		{
			boolean flag = false;
			if (this instanceof LOTRTradeable)
			{
				flag = ((LOTRTradeable)this).shouldTraderRespawn();
			}
			else if (this instanceof LOTRUnitTradeable)
			{
				flag = ((LOTRUnitTradeable)this).shouldTraderRespawn();
			}
			
			if (flag)
			{
				LOTREntityTraderRespawn entity = new LOTREntityTraderRespawn(worldObj);
				entity.setLocationAndAngles(posX, boundingBox.minY + (double)(height / 2F), posZ, 0F, 0F);
				entity.copyTraderDataFrom(this);
				worldObj.spawnEntityInWorld(entity);
				entity.onSpawn();
			}
		}
	}
	
	protected LOTRAchievement getKillAchievement()
	{
		return null;
	}
	
	@Override
	public void setDead()
	{
		super.setDead();
		if (deathTime == 0 && ridingEntity != null)
		{
			ridingEntity.setDead();
		}
	}
	
	public boolean canDropPouch()
	{
		return !hiredNPCInfo.isActive;
	}
	
	public int getAlignmentBonus()
	{
		return 0;
	}
	
	@Override
	protected int getExperiencePoints(EntityPlayer entityplayer)
    {
        return 4 + rand.nextInt(3);
    }
	
	@Override
    public float getBlockPathWeight(int i, int j, int k)
    {
		if (liftSpawnRestrictions)
		{
			return 1F;
		}
		
		if (spawnsInDarkness)
		{
			BiomeGenBase biome = worldObj.getBiomeGenForCoords(i, k);
			if (biome instanceof LOTRBiome && ((LOTRBiome)biome).canSpawnHostilesInDay())
			{
				return 1F;
			}
		}
		
		if (spawnsInDarkness)
		{
			return 0.5F - worldObj.getLightBrightness(i, j, k);
		}
		
		return super.getBlockPathWeight(i, j, k);
    }

    protected boolean isValidLightLevel()
    {
        int i = MathHelper.floor_double(posX);
        int j = MathHelper.floor_double(boundingBox.minY);
        int k = MathHelper.floor_double(posZ);
		
		if (spawnsInDarkness)
		{
			BiomeGenBase biome = worldObj.getBiomeGenForCoords(i, k);
			if (biome instanceof LOTRBiome && ((LOTRBiome)biome).canSpawnHostilesInDay())
			{
				return true;
			}
		}

        if (worldObj.getSavedLightValue(EnumSkyBlock.Sky, i, j, k) > rand.nextInt(32))
        {
            return false;
        }
        else
        {
            int l = worldObj.getBlockLightValue(i, j, k);

            if (worldObj.isThundering())
            {
                int i1 = worldObj.skylightSubtracted;
                worldObj.skylightSubtracted = 10;
                l = worldObj.getBlockLightValue(i, j, k);
                worldObj.skylightSubtracted = i1;
            }

            return l <= rand.nextInt(8);
        }
    }

	@Override
    public boolean getCanSpawnHere()
    {
        if ((!spawnsInDarkness || liftSpawnRestrictions || isValidLightLevel()) && super.getCanSpawnHere())
		{
        	int i = MathHelper.floor_double(posX);
            int j = MathHelper.floor_double(boundingBox.minY);
            int k = MathHelper.floor_double(posZ);
        	if (LOTREventHandler.isProtectedByBanner(worldObj, i, j, k, this))
        	{
        		return false;
        	}
        	else
        	{
        		return true;
        	}
		}
		return false;
    }
	
    public final int getSpawnCountValue()
    {
		if (isNPCPersistent || hiredNPCInfo.isActive)
		{
			return 0;
		}
		
		int multiplier = 1;
		BiomeGenBase biome = worldObj.getBiomeGenForCoords(MathHelper.floor_double(posX), MathHelper.floor_double(posZ));
		if (biome instanceof LOTRBiome)
		{
			multiplier = ((LOTRBiome)biome).spawnCountMultiplier();
		}
		return spawnCountValue * multiplier;
    }
	
	@Override
	public boolean interact(EntityPlayer entityplayer)
	{
		if (!worldObj.isRemote && canNPCTalk())
		{
			String speechBank = getSpeechBank(entityplayer);
			if (speechBank != null)
			{
				if (npcLocationName != null)
				{
					entityplayer.addChatMessage(LOTRSpeech.getNamedLocationSpeechForPlayer(this, npcLocationName, speechBank, entityplayer));
				}
				else
				{
					entityplayer.addChatMessage(LOTRSpeech.getNamedSpeechForPlayer(this, speechBank, entityplayer));
				}

				markNPCSpoken();
				
				if (getTalkAchievement() != null)
				{
					LOTRLevelData.addAchievement(entityplayer, getTalkAchievement());
				}
				
				return true;
			}
		}
		return super.interact(entityplayer);
	}
	
	protected LOTRAchievement getTalkAchievement()
	{
		return null;
	}
	
	public boolean isFriendly(EntityPlayer entityplayer)
	{
		return LOTRLevelData.getAlignment(entityplayer, getFaction()) >= 0 && getAttackTarget() != entityplayer && attackingPlayer != entityplayer;
	}
	
	public String getSpeechBank(EntityPlayer entityplayer)
	{
		return null;
	}
	
	public void onArtificalSpawn() {}
	
	public boolean isDrunkard()
	{
		return false;
	}
	
	public void setSpecificLocationName(String name)
	{
		npcLocationName = name;
		hasSpecificLocationName = true;
	}
	
	public boolean getHasSpecificLocationName()
	{
		return hasSpecificLocationName;
	}
	
	public int nearbyBanners()
	{
		if (getFaction() == LOTRFaction.UNALIGNED)
		{
			return 0;
		}
		
		int banners = 0;
		
		List entities = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, boundingBox.expand(16D, 16D, 16D));
		for (int i = 0; i < entities.size(); i++)
		{
			EntityLivingBase entity = (EntityLivingBase)entities.get(i);
			if (entity.isEntityAlive() && entity.getEquipmentInSlot(0) != null)
			{
				ItemStack item = entity.getEquipmentInSlot(0);
				LOTRFaction faction = LOTRItemBanner.getFaction(item);
				if (faction == getFaction())
				{
					banners++;
				}
			}
		}
		
		return Math.min(banners, 4);
	}
	
	public ItemStack createAlignmentReward()
	{
		if (getFaction() == null)
		{
			return null;
		}
		return getFaction().createAlignmentReward();
	}
	
	@Override
    public ItemStack getEquipmentInSlot(int i)
    {
        if (worldObj.isRemote)
		{
			if (!initFestiveItems)
			{
				festiveRand.setSeed((long)getEntityId() * 341873128712L);
				
				if (LOTRMod.isHalloween())
				{
					if (festiveRand.nextInt(4) == 0)
					{
						festiveItems[4] = festiveRand.nextInt(10) == 0 ? new ItemStack(Blocks.lit_pumpkin) : new ItemStack(Blocks.pumpkin);
					}
				}
				else if (LOTRMod.isChristmas())
				{
					ItemStack hat = new ItemStack(LOTRMod.leatherHat);
					LOTRItemLeatherHat.setHatColor(hat, 0xFF0000);
					LOTRItemLeatherHat.setFeatherColor(hat, 0xFFFFFF);
					festiveItems[4] = hat;
				}
				
				initFestiveItems = true;
			}
			
			if (festiveItems[i] != null)
			{
				return festiveItems[i];
			}
		}
		return super.getEquipmentInSlot(i);
    }
	
	@Override
	public boolean allowLeashing()
	{
		return false;
	}

	@Override
    public void setCustomNameTag(String name) {}
	
	@Override
    public void func_110163_bv() {}
	
	@Override
	public boolean shouldDismountInWater(Entity rider)
	{
        return false;
    }
	
	public void spawnHearts()
	{
		if (!worldObj.isRemote)
		{
			ByteBuf data = Unpooled.buffer();
			
			data.writeInt(getEntityId());
			
			S3FPacketCustomPayload packet = new S3FPacketCustomPayload("lotr.hearts", data);
			MinecraftServer.getServer().getConfigurationManager().sendToAllNear(posX, posY, posZ, 32D, dimension, packet);
		}
		else
		{
			double d = rand.nextGaussian() * 0.02D;
			double d1 = rand.nextGaussian() * 0.02D;
			double d2 = rand.nextGaussian() * 0.02D;
			worldObj.spawnParticle("heart", posX + (double)(rand.nextFloat() * width * 2F) - (double)width, posY + 0.5D + (double)(rand.nextFloat() * height), posZ + (double)(rand.nextFloat() * width * 2F) - (double)width, d, d1, d2);
		}
	}
	
	public void spawnFoodParticles()
	{
		if (getHeldItem() == null)
		{
			return;
		}
		
		if (!worldObj.isRemote)
		{
			ByteBuf data = Unpooled.buffer();
			
			data.writeInt(getEntityId());
			
			S3FPacketCustomPayload packet = new S3FPacketCustomPayload("lotr.eatFood", data);
			MinecraftServer.getServer().getConfigurationManager().sendToAllNear(posX, posY, posZ, 32D, dimension, packet);
		}
		else
		{
			for (int i = 0; i < 5; i++)
			{
				Vec3 vec1 = Vec3.createVectorHelper(((double)rand.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);
				vec1.rotateAroundX(-rotationPitch * (float)Math.PI / 180F);
				vec1.rotateAroundY(-rotationYaw * (float)Math.PI / 180F);
				Vec3 vec2 = Vec3.createVectorHelper(((double)rand.nextFloat() - 0.5D) * 0.3D, (double)(-rand.nextFloat()) * 0.6D - 0.3D, 0.6D);
				vec2.rotateAroundX(-rotationPitch * (float)Math.PI / 180F);
				vec2.rotateAroundY(-rotationYaw * (float)Math.PI / 180F);
				vec2 = vec2.addVector(posX, posY + (double)getEyeHeight(), posZ);
				worldObj.spawnParticle("iconcrack_" + Item.getIdFromItem(getHeldItem().getItem()), vec2.xCoord, vec2.yCoord, vec2.zCoord, vec1.xCoord, vec1.yCoord + 0.05D, vec1.zCoord);
			}
		}
	}
	
	public void setDefaultHeldItem(ItemStack itemstack)
	{
		hasDefaultHeldItem = true;
		defaultHeldItem = itemstack;
	}

	public void clearDefaultHeldItem()
	{
		hasDefaultHeldItem = false;
		defaultHeldItem = null;
	}
	
	public ItemStack getDefaultHeldItem()
	{
		return defaultHeldItem;
	}
	
	public boolean hasDefaultHeldItem()
	{
		return hasDefaultHeldItem;
	}
}
