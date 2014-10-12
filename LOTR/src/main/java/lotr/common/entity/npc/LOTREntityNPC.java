package lotr.common.entity.npc;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.*;

import lotr.common.*;
import lotr.common.entity.LOTREntities;
import lotr.common.entity.LOTRMountFunctions;
import lotr.common.entity.ai.*;
import lotr.common.entity.animal.LOTREntityHorse;
import lotr.common.entity.item.LOTREntityTraderRespawn;
import lotr.common.entity.projectile.LOTREntityPebble;
import lotr.common.entity.projectile.LOTREntityPlate;
import lotr.common.inventory.LOTRContainerTrade;
import lotr.common.inventory.LOTRContainerUnitTrade;
import lotr.common.item.*;
import lotr.common.quest.LOTRMiniQuest;
import lotr.common.world.biome.LOTRBiome;
import lotr.common.world.structure.LOTRChestContents;
import net.minecraft.command.IEntitySelector;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.ai.attributes.*;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.S3FPacketCustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.*;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

import com.google.common.collect.Multimap;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class LOTREntityNPC extends EntityCreature
{
	public static IAttribute npcAttackDamage = new RangedAttribute("lotr.npcAttackDamage", 2D, 0D, Double.MAX_VALUE).setDescription("LOTR NPC Attack Damage");
	public static IAttribute horseAttackSpeed = new RangedAttribute("lotr.horseAttackSpeed", 1.7D, 0D, Double.MAX_VALUE).setDescription("LOTR Horse Attack Speed");

	private float npcWidth = -1F;
	private float npcHeight;
	
	public boolean isPassive = false;
	public boolean isImmuneToFrost = false;
	protected boolean spawnsInDarkness = false;
	
	public boolean isNPCPersistent = false;
	public boolean liftSpawnRestrictions = false;
	
	public String npcLocationName;
	private boolean hasSpecificLocationName;
	
	public boolean spawnRidingHorse;
	private boolean ridingHorse;
	
	public LOTRHiredNPCInfo hiredNPCInfo;
	public LOTRTraderNPCInfo traderNPCInfo;
	public LOTRFamilyInfo familyInfo;
	public LOTRTravellingTraderInfo travellingTraderInfo;
	
	public boolean isOfferingMiniQuest = false;
	public boolean hasMiniQuest = false;
	
	protected enum AttackMode
	{
		MELEE,
		RANGED,
		IDLE
	}
	private AttackMode prevAttackMode = AttackMode.IDLE;
	
	private boolean hasDefaultHeldItem = false;
	private ItemStack defaultHeldItem;
	
	private ItemStack[] festiveItems = new ItemStack[5];
	private Random festiveRand = new Random();
	private boolean initFestiveItems = false;
	
	public LOTRShields npcShield;
	public ResourceLocation npcCape;
	
	private EntityLivingBase prevAttackTarget;
	private boolean hurtOnlyByPlates = true;
	public int npcTalkTick = 0;
	
	private List<ItemStack> enpouchedDrops = new ArrayList();
	private boolean enpouchNPCDrops = false;
	
	public LOTREntityNPC(World world)
	{
		super(world);
		if (isTrader() || this instanceof IBossDisplayData || this instanceof LOTRCharacter)
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
		
		if (this instanceof LOTRTravellingTrader)
		{
			travellingTraderInfo = new LOTRTravellingTraderInfo((LOTRTravellingTrader)this);
		}
	}
	
	public void startTraderVisiting(EntityPlayer entityplayer)
	{
		travellingTraderInfo.startVisiting(entityplayer);
	}

	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getAttributeMap().registerAttribute(npcAttackDamage);
		getAttributeMap().registerAttribute(horseAttackSpeed);
    }
	
	public void setUniqueID(UUID uuid)
	{
		entityUniqueID = uuid;
	}
	
	public int addTargetTasks(boolean seekTargets)
	{
		return addTargetTasks(seekTargets, LOTREntityAINearestAttackableTargetBasic.class);
	}
	
	public int addTargetTasks(boolean seekTargets, Class c)
	{
		targetTasks.taskEntries.clear();
        targetTasks.addTask(1, new LOTREntityAIHiringPlayerHurtByTarget(this));
        targetTasks.addTask(2, new LOTREntityAIHiringPlayerHurtTarget(this));
        targetTasks.addTask(3, new LOTREntityAINPCHurtByTarget(this, false));
        if (seekTargets)
        {
        	return addTargetTasks(this, 4, c);
        }
        else
        {
        	return 3;
        }
	}
	
	public static int addTargetTasks(EntityCreature entity, int index, Class c)
	{
		try
		{
			Constructor constructor = c.getConstructor(new Class[]{EntityCreature.class, Class.class, int.class, boolean.class, IEntitySelector.class});
			entity.targetTasks.addTask(index, (EntityAIBase)constructor.newInstance(new Object[]{entity, EntityPlayer.class, 0, true, null}));
			entity.targetTasks.addTask(index, (EntityAIBase)constructor.newInstance(new Object[]{entity, EntityLiving.class, 0, true, new LOTRNPCTargetSelector(entity)}));
		}
		catch (Exception e)
		{
			System.out.println("Error adding LOTR target tasks to entity " + entity.toString());
			e.printStackTrace();
		}
		return index;
	}
	
	protected void removeTasksOfType(Class c)
	{
		for (int i = 0; i < tasks.taskEntries.size(); i++)
		{
			EntityAITaskEntry taskEntry = (EntityAITaskEntry)tasks.taskEntries.get(i);
			if (c.isAssignableFrom(taskEntry.action.getClass()))
			{
				tasks.removeTask(taskEntry.action);
			}
		}
		for (int i = 0; i < targetTasks.taskEntries.size(); i++)
		{
			EntityAITaskEntry taskEntry = (EntityAITaskEntry)targetTasks.taskEntries.get(i);
			if (c.isAssignableFrom(taskEntry.action.getClass()))
			{
				targetTasks.removeTask(taskEntry.action);
			}
		}
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public boolean isInRangeToRenderDist(double dist)
    {
		LOTRPlayerData data = LOTRLevelData.getData(LOTRMod.proxy.getClientPlayer());
        if (!data.getMiniQuestsForEntity(this, true).isEmpty())
        {
        	return true;
        }
        return super.isInRangeToRenderDist(dist);
    }
	
	@Override
	public void onChunkLoad()
	{
		super.onChunkLoad();
		removeTasksOfType(LOTREntityAIBurningPanic.class);
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
					if (!(mount instanceof LOTREntityNPC))
					{
						setRidingHorse(true);
						mount.setBelongsToNPC(true);
						LOTRMountFunctions.setNavigatorRangeFromNPC(mount, this);
					}
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
		return isEntityAlive() && npcTalkTick >= getNPCTalkInterval();
	}
	
	private void markNPCSpoken()
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
		return !isNPCPersistent && !hiredNPCInfo.isActive && !hasMiniQuest;
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
		
		rescaleNPC(getNPCScale());

		if (!worldObj.isRemote && getAttackTarget() != null)
		{
			EntityLivingBase entity = getAttackTarget();
			if (!entity.isEntityAlive() || (entity instanceof EntityPlayer && ((EntityPlayer)entity).capabilities.isCreativeMode))
			{
				setAttackTarget(null);
			}
		}
		
		familyInfo.onUpdate();
		hiredNPCInfo.onUpdate();
		
		if (travellingTraderInfo != null)
		{
			travellingTraderInfo.onUpdate();
		}
		
		if (!worldObj.isRemote && isEntityAlive() && (isTrader() || hiredNPCInfo.isActive))
		{
			if (getAttackTarget() == null && getHealth() < getMaxHealth())
			{
				boolean timeHeal =  worldObj.getWorldTime() % 80L == 40L;
				boolean bannersHeal = false;
				
				if (hiredNPCInfo.isActive)
				{
					int banners = nearbyBanners();
					if (banners > 0)
					{
						bannersHeal = worldObj.getWorldTime() % (240L - (long)(banners * 40L)) == 0L;
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
		
		if (!worldObj.isRemote && isEntityAlive() && getAttackTarget() == null)
		{
			boolean guiOpen = false;
			
			if (this instanceof LOTRTradeable)
			{
				for (int i = 0; i < worldObj.playerEntities.size(); i++)
				{
					EntityPlayer entityplayer = (EntityPlayer)worldObj.playerEntities.get(i);
					Container container = entityplayer.openContainer;
					if (container != null && container instanceof LOTRContainerTrade && ((LOTRContainerTrade)container).theTrader == this)
					{
						guiOpen = true;
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
						guiOpen = true;
						break;
					}
				}
			}
			
			if (hiredNPCInfo.isActive && hiredNPCInfo.isGuiOpen)
			{
				guiOpen = true;
			}
			
			if (isOfferingMiniQuest)
			{
				guiOpen = true;
			}
			
			if (guiOpen)
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
		}
		
		updateArmSwingProgress();
		
		if (npcTalkTick < getNPCTalkInterval())
		{
			npcTalkTick++;
		}
		
		if (!worldObj.isRemote && hasHome())
		{
			if (!isWithinHomeDistanceCurrentPosition() && getAttackTarget() == null)
			{
				int homeX = getHomePosition().posX;
				int homeY = getHomePosition().posY;
				int homeZ = getHomePosition().posZ;
				int homeRange = (int)func_110174_bM();
				detachHome();
				
				Vec3 path = null;
				for (int l = 0; l < 16 && path == null; l++)
				{
					path = RandomPositionGenerator.findRandomTargetBlockTowards(this, 16, 7, Vec3.createVectorHelper(homeX, homeY, homeZ));
				}
				if (path != null)
				{
					getNavigator().tryMoveToXYZ(path.xCoord, path.yCoord, path.zCoord, 1.25D);
				}
				
				setHomeArea(homeX, homeY, homeZ, homeRange);
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
		
		if (!worldObj.isRemote && !isChild())
		{
			ItemStack weapon = getEquipmentInSlot(0);
			boolean carryingSpear = weapon != null && weapon.getItem() instanceof LOTRItemSpear;
			if (!carryingSpear)
			{
				if (getAttackTarget() != null)
				{
					double d = getDistanceSqToEntity(getAttackTarget());
					if (d < getMeleeRangeSq())
					{
						if (prevAttackMode != AttackMode.MELEE)
						{
							prevAttackMode = AttackMode.MELEE;
							onAttackModeChange(AttackMode.MELEE);
						}
					}
					else if (d < getMaxCombatRangeSq())
					{
						if (prevAttackMode != AttackMode.RANGED)
						{
							prevAttackMode = AttackMode.RANGED;
							onAttackModeChange(AttackMode.RANGED);
						}
					}
				}
				else
				{
					if (prevAttackMode != AttackMode.IDLE)
					{
						prevAttackMode = AttackMode.IDLE;
						onAttackModeChange(AttackMode.IDLE);
					}
				}
			}
		}
		
		if (!worldObj.isRemote)
		{
			List players = worldObj.playerEntities;
			for (Object obj : players)
			{
				EntityPlayer entityplayer = (EntityPlayer)obj;
				List<LOTRMiniQuest> miniquests = LOTRLevelData.getData(entityplayer).getMiniQuestsForEntity(this, true);
				for (LOTRMiniQuest quest : miniquests)
				{
					quest.updateLocation(this);
				}
			}
		}
	}
	
	protected void onAttackModeChange(AttackMode mode) {}
	
	protected double getMeleeRange()
	{
		return 4D;
	}
	
	protected final double getMeleeRangeSq()
	{
		double d = getMeleeRange();
		return d * d;
	}
	
	protected final double getMaxCombatRange()
	{
		double d = getEntityAttribute(SharedMonsterAttributes.followRange).getAttributeValue();
		return d * 0.95D;
	}
	
	protected final double getMaxCombatRangeSq()
	{
		double d = getMaxCombatRange();
		return d * d;
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound nbt)
	{
		super.writeEntityToNBT(nbt);
		hiredNPCInfo.writeToNBT(nbt);
		traderNPCInfo.writeToNBT(nbt);
		familyInfo.writeToNBT(nbt);
		if (travellingTraderInfo != null)
		{
			travellingTraderInfo.writeToNBT(nbt);
		}
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
		nbt.setBoolean("NPCHasMiniQuest", hasMiniQuest);
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound nbt)
	{
		super.readEntityFromNBT(nbt);
		hiredNPCInfo.readFromNBT(nbt);
		traderNPCInfo.readFromNBT(nbt);
		familyInfo.readFromNBT(nbt);
		if (travellingTraderInfo != null)
		{
			travellingTraderInfo.readFromNBT(nbt);
		}
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
		
		onAttackModeChange(AttackMode.IDLE);
		
		hasMiniQuest = nbt.getBoolean("NPCHasMiniQuest");
	}
	
	@Override
    public ItemStack getPickedResult(MovingObjectPosition target)
    {
		return new ItemStack(LOTRMod.spawnEgg, 1, LOTREntities.getEntityID(this));
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
							chance = Math.max(chance, 1);
							
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
	
	protected void dropChestContents(LOTRChestContents items, int min, int max)
	{
		IInventory drops = new InventoryBasic("drops", false, max * 10);
		LOTRChestContents.fillInventory(drops, rand, items, MathHelper.getRandomIntegerInRange(rand, min, max));
		for (int i = 0; i < drops.getSizeInventory(); i++)
		{
			ItemStack item = drops.getStackInSlot(i);
			if (item != null)
			{
				entityDropItem(item, 0F);
			}
		}
	}
	
	@Override
	public final void dropEquipment(boolean flag, int i) {}
	
	@Override
	public EntityItem entityDropItem(ItemStack item, float offset)
    {
		return npcDropItem(item, offset, true);
    }
	
	public EntityItem npcDropItem(ItemStack item, float offset, boolean flag)
	{
		if (flag)
		{
			if (item != null && item.getItem() != null && item.getMaxStackSize() == 1)
			{
				if (!item.hasTagCompound())
				{
					item.setTagCompound(new NBTTagCompound());
				}
				
				NBTTagCompound nbt = item.getTagCompound();
				nbt.setString("LOTROwner", getCommandSenderName());
			}
			
			if (enpouchNPCDrops && item != null)
			{
				enpouchedDrops.add(item);
				return null;
			}
		}
		
		return super.entityDropItem(item, offset);
	}
	
	@Override
	public void onDeath(DamageSource damagesource)
	{
		enpouchNPCDrops = true;
		
		hiredNPCInfo.onDeath(damagesource);
		if (travellingTraderInfo != null)
		{
			travellingTraderInfo.onDeath();
		}
		
		if (!worldObj.isRemote && recentlyHit > 0 && canDropPouch() && LOTRMod.canDropLoot(worldObj))
		{
			if (rand.nextInt(10) == 0)
			{
				int coins = MathHelper.getRandomIntegerInRange(rand, 1, 4);
				if (rand.nextInt(3) == 0)
				{
					coins *= MathHelper.getRandomIntegerInRange(rand, 2, 4);
				}
				dropItem(LOTRMod.silverCoin, coins);
			}
		}
		
		super.onDeath(damagesource);
		
		if (!worldObj.isRemote && recentlyHit > 0 && canDropPouch() && LOTRMod.canDropLoot(worldObj))
		{
			if (rand.nextInt(10) == 0)
			{
				ItemStack pouch = new ItemStack(LOTRMod.pouch, 1, LOTRItemPouch.getRandomPouchSize(rand));
				List<ItemStack> pouchContents = new ArrayList();
					
				while (!enpouchedDrops.isEmpty())
				{
					pouchContents.add(enpouchedDrops.remove(0));
					
					if (pouchContents.size() >= LOTRItemPouch.getCapacity(pouch))
					{
						break;
					}
				}
				
				for (ItemStack itemstack : pouchContents)
				{
					if (!LOTRItemPouch.tryAddItemToPouch(pouch, itemstack, false))
					{
						enpouchedDrops.add(itemstack);
					}
				}
				
				enpouchNPCDrops = false;
				entityDropItem(pouch, 0F);
			}
		}
		
		if (!enpouchedDrops.isEmpty())
		{
			enpouchNPCDrops = false;
			for (ItemStack item : enpouchedDrops)
			{
				entityDropItem(item, 0F);
			}
		}
		
		if (!worldObj.isRemote && damagesource.getEntity() instanceof EntityPlayer)
		{
			EntityPlayer entityplayer = (EntityPlayer)damagesource.getEntity();
			
			if (hurtOnlyByPlates && damagesource.getSourceOfDamage() instanceof LOTREntityPlate)
			{
				if (LOTRLevelData.getData(entityplayer).getAlignment(getFaction()) < 0);
				{
					LOTRLevelData.getData(entityplayer).addAchievement(LOTRAchievement.killUsingOnlyPlates);
				}
			}
			
			if (damagesource.getSourceOfDamage() instanceof LOTREntityPebble)
			{
				float size = width * width * height;
				if (size > 5F)
				{
					int alignment = LOTRLevelData.getData(entityplayer).getAlignment(getFaction());
					if (alignment < 0)
					{
						LOTRLevelData.getData(entityplayer).addAchievement(LOTRAchievement.killLargeMobWithSlingshot);
					}
				}
			}
			
			if (getKillAchievement() != null)
			{
				LOTRLevelData.getData(entityplayer).addAchievement(getKillAchievement());
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
		
		if (!worldObj.isRemote)
		{
			for (LOTRPlayerData playerData : LOTRLevelData.getPlayerDataEntries())
			{
				for (LOTRMiniQuest quest : playerData.getMiniQuests())
				{
					if (quest.isActive() && quest.entityUUID.equals(getUniqueID()))
					{
						quest.setEntityDead();
					}
				}
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
        	if (LOTRBannerProtection.isProtectedByBanner(worldObj, this, LOTRBannerProtection.forNPC(this), false))
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
		return multiplier;
    }
	
	@Override
	public boolean interact(EntityPlayer entityplayer)
	{
		if (!worldObj.isRemote && canNPCTalk())
		{
			if (!isTrader() && !isChild() && !hiredNPCInfo.isActive && isFriendly(entityplayer) && getAttackTarget() == null)
			{
				LOTRPlayerData playerData = LOTRLevelData.getData(entityplayer);
				
				List<LOTRMiniQuest> questsInProgress = playerData.getMiniQuestsForEntity(this, true);

				if (!questsInProgress.isEmpty())
				{
					LOTRMiniQuest currentQuest = questsInProgress.get(0);
					currentQuest.onInteract(entityplayer, this);

					if (currentQuest.isCompleted())
					{
						sendSpeechBank(entityplayer, currentQuest.speechBankComplete, currentQuest);
						hasMiniQuest = false;
					}
					else
					{
						sendSpeechBank(entityplayer, currentQuest.speechBankProgress, currentQuest);
					}
					
					return true;
				}
				else
				{
					List<LOTRMiniQuest> questsForFaction = playerData.getMiniQuestsForFaction(getFaction(), true);
					if (rand.nextInt(5) == 0 && questsForFaction.size() < LOTRMiniQuest.MAX_MINIQUESTS_PER_FACTION)
					{
						LOTRMiniQuest quest = createMiniQuest(entityplayer);
						if (quest != null)
						{
							quest.entityUUID = getUniqueID();
							quest.entityName = getNPCName();
							quest.entityFaction = getFaction();
							
							if (quest.isValidQuest())
							{
								if (sendMiniQuestOffer(entityplayer, quest))
								{
									isOfferingMiniQuest = true;
								}
								
								return true;
							}
							else
							{
								FMLLog.severe("Created an invalid LOTR miniquest " + quest.speechBankStart);
							}
						}
					}
				}
			}
			
			String speechBank = getSpeechBank(entityplayer);
			if (speechBank != null)
			{
				sendSpeechBank(entityplayer, speechBank);
				
				if (getTalkAchievement() != null)
				{
					LOTRLevelData.getData(entityplayer).addAchievement(getTalkAchievement());
				}
				
				return true;
			}
		}
		return super.interact(entityplayer);
	}
	
	public void sendSpeechBank(EntityPlayer entityplayer, String speechBank)
	{
		sendSpeechBank(entityplayer, speechBank, null);
	}
		
	public void sendSpeechBank(EntityPlayer entityplayer, String speechBank, LOTRMiniQuest miniquest)
	{
		String location = null;
		String objective = null;
		
		if (npcLocationName != null)
		{
			if (!hasSpecificLocationName)
			{
				location = StatCollector.translateToLocalFormatted(npcLocationName, new Object[] {getNPCName()});
			}
			else
			{
				location = npcLocationName;
			}
		}
		
		if (miniquest != null)
		{
			objective = miniquest.getObjectiveInSpeech();
		}
			
		entityplayer.addChatMessage(LOTRSpeech.getNamedSpeechForPlayer(this, speechBank, entityplayer, location, objective));
		
		markNPCSpoken();
	}
	
	private boolean sendMiniQuestOffer(EntityPlayer entityplayer, LOTRMiniQuest quest)
	{
		try
		{
			ByteBuf data = Unpooled.buffer();
			
			data.writeInt(getEntityId());
			
			NBTTagCompound nbt = new NBTTagCompound();
			quest.writeToNBT(nbt);
			new PacketBuffer(data).writeNBTTagCompoundToBuffer(nbt);
	
			S3FPacketCustomPayload packet = new S3FPacketCustomPayload("lotr.mqOffer", data);
			((EntityPlayerMP)entityplayer).playerNetServerHandler.sendPacket(packet);
			return true;
		}
		catch (IOException e)
		{
			System.out.println("Could not offer miniquest to player");
			e.printStackTrace();
			return false;
		}
	}
	
	protected LOTRAchievement getTalkAchievement()
	{
		return null;
	}
	
	public boolean isFriendly(EntityPlayer entityplayer)
	{
		return LOTRLevelData.getData(entityplayer).getAlignment(getFaction()) >= 0 && getAttackTarget() != entityplayer && attackingPlayer != entityplayer;
	}
	
	public String getSpeechBank(EntityPlayer entityplayer)
	{
		return null;
	}
	
	public LOTRMiniQuest createMiniQuest(EntityPlayer entityplayer)
	{
		return null;
	}
	
	public void onArtificalSpawn() {}
	
	public boolean isDrunkard()
	{
		return false;
	}
	
	public boolean shouldRenderNPCHair()
	{
		return true;
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
		
		List entities = worldObj.getEntitiesWithinAABB(LOTRBannerBearer.class, boundingBox.expand(16D, 16D, 16D));
		for (int i = 0; i < entities.size(); i++)
		{
			EntityLivingBase entity = (EntityLivingBase)entities.get(i);
			if (entity != this && entity.isEntityAlive() && LOTRMod.getNPCFaction(entity) == getFaction())
			{
				banners++;
			}
		}
		
		return Math.min(banners, 5);
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
			for (int i = 0; i < 8; i++)
			{
				double d = rand.nextGaussian() * 0.02D;
				double d1 = rand.nextGaussian() * 0.02D;
				double d2 = rand.nextGaussian() * 0.02D;
				worldObj.spawnParticle("heart", posX + (double)(rand.nextFloat() * width * 2F) - (double)width, posY + 0.5D + (double)(rand.nextFloat() * height), posZ + (double)(rand.nextFloat() * width * 2F) - (double)width, d, d1, d2);
			}
		}
	}
	
	public void spawnSmokes()
	{
		if (!worldObj.isRemote)
		{
			ByteBuf data = Unpooled.buffer();
			
			data.writeInt(getEntityId());
			
			S3FPacketCustomPayload packet = new S3FPacketCustomPayload("lotr.smokes", data);
			MinecraftServer.getServer().getConfigurationManager().sendToAllNear(posX, posY, posZ, 32D, dimension, packet);
		}
		else
		{
			for (int i = 0; i < 8; i++)
			{
				double d = rand.nextGaussian() * 0.02D;
				double d1 = rand.nextGaussian() * 0.02D;
				double d2 = rand.nextGaussian() * 0.02D;
				worldObj.spawnParticle("smoke", posX + (double)(rand.nextFloat() * width * 2F) - (double)width, posY + 0.5D + (double)(rand.nextFloat() * height), posZ + (double)(rand.nextFloat() * width * 2F) - (double)width, d, d1, d2);
			}
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
	
	public ItemStack getHeldItemLeft()
	{
		if (this instanceof LOTRBannerBearer)
		{
			return new ItemStack(LOTRMod.banner, 1, LOTRItemBanner.getSubtypeForFaction(getFaction()));
		}
		if (isTrader())
		{
			return new ItemStack(LOTRMod.silverCoin);
		}
		return null;
	}
}
