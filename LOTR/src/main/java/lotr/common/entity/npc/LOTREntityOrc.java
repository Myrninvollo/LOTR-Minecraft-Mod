package lotr.common.entity.npc;

import java.util.List;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRAlignmentValues;
import lotr.common.LOTRLevelData;
import lotr.common.LOTRMod;
import lotr.common.entity.ai.LOTREntityAIFollowHiringPlayer;
import lotr.common.entity.ai.LOTREntityAIHiredRemainStill;
import lotr.common.entity.ai.LOTREntityAIHiringPlayerHurtByTarget;
import lotr.common.entity.ai.LOTREntityAIHiringPlayerHurtTarget;
import lotr.common.entity.ai.LOTREntityAINearestAttackableTargetOrc;
import lotr.common.entity.ai.LOTREntityAIOrcAvoidGoodPlayer;
import lotr.common.entity.ai.LOTREntityAIOrcSkirmish;
import lotr.common.entity.animal.LOTREntityRabbit;
import lotr.common.entity.item.LOTREntityOrcBomb;
import lotr.common.world.biome.LOTRBiome;
import lotr.common.world.biome.LOTRBiomeGenIronHills;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWatchClosest2;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

public abstract class LOTREntityOrc extends LOTREntityNPC
{
	public boolean isWeakOrc = true;
	public boolean isBombardier = false;
	public boolean hasSkullStaff = false;
	private int orcSkirmishTick;
	
	public LOTREntityOrc(World world)
	{
		super(world);
		setSize(0.5F, 1.55F);
		getNavigator().setAvoidsWater(true);
		getNavigator().setBreakDoors(true);
        tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(1, new LOTREntityAIHiredRemainStill(this));
		tasks.addTask(2, new EntityAIAvoidEntity(this, LOTREntityOrcBomb.class, 12F, 1.5D, 2D));
		tasks.addTask(3, new LOTREntityAIOrcAvoidGoodPlayer(this, 8F, 1.5D));
		tasks.addTask(4, getOrcAttackAI());
		tasks.addTask(5, new LOTREntityAIFollowHiringPlayer(this));
		tasks.addTask(6, new EntityAIOpenDoor(this, true));
        tasks.addTask(7, new EntityAIWander(this, 1D));
        tasks.addTask(8, new EntityAIWatchClosest2(this, EntityPlayer.class, 8F, 0.1F));
        tasks.addTask(8, new EntityAIWatchClosest2(this, LOTREntityNPC.class, 5F, 0.02F));
        tasks.addTask(9, new EntityAIWatchClosest(this, EntityLiving.class, 8F, 0.02F));
        tasks.addTask(10, new EntityAILookIdle(this));
        targetTasks.addTask(1, new LOTREntityAIHiringPlayerHurtByTarget(this));
        targetTasks.addTask(2, new LOTREntityAIHiringPlayerHurtTarget(this));
        targetTasks.addTask(3, new EntityAIHurtByTarget(this, false));
        addTargetTasks(4, LOTREntityAINearestAttackableTargetOrc.class);
		targetTasks.addTask(5, new LOTREntityAIOrcSkirmish(this, true));
		targetTasks.addTask(6, new LOTREntityAINearestAttackableTargetOrc(this, LOTREntityRabbit.class, 2000, false));
		spawnsInDarkness = true;
	}
	
	public abstract EntityAIBase getOrcAttackAI();
	
	@Override
	public void entityInit()
	{
		super.entityInit();
		dataWatcher.addObject(16, LOTRNames.getRandomOrcName(rand));
	}
	
	public String getOrcName()
	{
		return dataWatcher.getWatchableObjectString(16);
	}
	
	public void setOrcName(String name)
	{
		dataWatcher.updateObject(16, name);
	}
	
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(18D);
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.2D);
    }
	
	@Override
    public IEntityLivingData onSpawnWithEgg(IEntityLivingData data)
    {
		data = super.onSpawnWithEgg(data);
		return data;
    }
	
	@Override
	public int getTotalArmorValue()
	{
		if (isWeakOrc)
		{
			return MathHelper.floor_double((double)super.getTotalArmorValue() * 0.75D);
		}
		return super.getTotalArmorValue();
	}
	
	@Override
    public boolean isAIEnabled()
    {
        return true;
    }
	
	@Override
	public String getNPCName()
	{
		return getOrcName();
	}
	
	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		
		if (!worldObj.isRemote && isWeakOrc)
		{
			int i = MathHelper.floor_double(posX);
			int j = MathHelper.floor_double(boundingBox.minY);
			int k = MathHelper.floor_double(posZ);
			BiomeGenBase biome = worldObj.getBiomeGenForCoords(i, k);
			boolean flag = worldObj.isDaytime() && worldObj.canBlockSeeTheSky(i, j, k);
			if (biome instanceof LOTRBiome && ((LOTRBiome)biome).canSpawnHostilesInDay())
			{
				flag = false;
			}
			if (flag && worldObj.getWorldTime() % 20L == 0L)
			{
				addPotionEffect(new PotionEffect(Potion.resistance.id, 200, -1));
				addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 200));
			}
		}
		
		if (!worldObj.isRemote && isOrcSkirmishing())
		{
			if (!(getAttackTarget() instanceof LOTREntityOrc))
			{
				orcSkirmishTick--;
			}
			if (!worldObj.getGameRules().getGameRuleBooleanValue("enableOrcSkirmish"))
			{
				orcSkirmishTick = 0;
			}
		}
	}
	
	public boolean isOrcSkirmishing()
	{
		return orcSkirmishTick > 0;
	}
	
	public void setOrcSkirmishing()
	{
		int prevSkirmishTick = orcSkirmishTick;
		orcSkirmishTick = 160;
		
		if (!worldObj.isRemote && prevSkirmishTick == 0)
		{
			List nearbyPlayers = worldObj.getEntitiesWithinAABB(EntityPlayer.class, boundingBox.expand(24D, 24D, 24D));
			for (int i = 0; i < nearbyPlayers.size(); i++)
			{
				EntityPlayer entityplayer = (EntityPlayer)nearbyPlayers.get(i);
				entityplayer.addChatMessage(LOTRSpeech.getNamedSpeechForPlayer(this, "orc_skirmish", entityplayer));
			}
		}
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound nbt)
	{
		super.writeEntityToNBT(nbt);
		nbt.setString("OrcName", getOrcName());
		nbt.setInteger("OrcSkirmish", orcSkirmishTick);
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound nbt)
	{
		super.readEntityFromNBT(nbt);
		if (nbt.hasKey("OrcName"))
		{
			setOrcName(nbt.getString("OrcName"));
		}
		orcSkirmishTick = nbt.getInteger("OrcSkirmish");
	}
	
    protected void orcArrowAttack(EntityLivingBase target, float f)
    {
        EntityArrow arrow = new EntityArrow(worldObj, this, target, 1.5F, 1F);
        playSound("random.bow", 1F, 1F / (rand.nextFloat() * 0.4F + 0.8F));
        worldObj.spawnEntityInWorld(arrow);
    }
	
	@Override
	protected LOTRAchievement getKillAchievement()
	{
		return LOTRAchievement.killOrc;
	}
	
	@Override
	public boolean canPickUpLoot()
	{
		return !(this instanceof IRangedAttackMob);
	}
	
	@Override
	protected void dropFewItems(boolean flag, int i)
	{
		super.dropFewItems(flag, i);
		
		int j = rand.nextInt(3) + rand.nextInt(i + 1);
		for (int k = 0; k < j; k++)
		{
			dropItem(Items.rotten_flesh, 1);
		}
		
		j = rand.nextInt(2) + rand.nextInt(i + 1);
		for (int k = 0; k < j; k++)
		{
			dropItem(LOTRMod.orcBone, 1);
		}
		
		if (rand.nextInt(10) == 0)
		{
			j = 1 + rand.nextInt(2) + rand.nextInt(i + 1);
			for (int k = 0; k < j; k++)
			{
				dropItem(LOTRMod.maggotyBread, 1);
			}
		}
		
		if (flag)
		{
			int rareDropChance = 20 - (i * 4);
			if (rareDropChance < 1)
			{
				rareDropChance = 1;
			}
			
			if (rand.nextInt(rareDropChance) == 0)
			{
				int drop = rand.nextInt(2);
				if (drop == 0)
				{
					entityDropItem(new ItemStack(LOTRMod.mugOrcDraught, 1, 1 + rand.nextInt(3)), 0F);
				}
				else if (drop == 1)
				{
					j = 1 + rand.nextInt(2) + rand.nextInt(i + 1);
					for (int k = 0; k < j; k++)
					{
						if (this instanceof LOTREntityUrukHai)
						{
							dropItem(LOTRMod.urukSteel, 1);
						}
						else
						{
							dropItem(LOTRMod.orcSteel, 1);
						}
					}
				}
			}
		}
	}
	
	@Override
	public boolean getCanSpawnHere()
	{
		if (!liftSpawnRestrictions)
		{
			int i = MathHelper.floor_double(posX);
			int j = MathHelper.floor_double(boundingBox.minY);
			int k = MathHelper.floor_double(posZ);
			
			BiomeGenBase biome = worldObj.getBiomeGenForCoords(i, k);
			if (biome instanceof LOTRBiomeGenIronHills && worldObj.getBlock(i, j - 1, k) != Blocks.grass)
			{
				return false;
			}
		}
		
		return super.getCanSpawnHere();
	}
	
	public int getBombStrength()
	{
		return 0;
	}
	
	public void setBombStrength(int i) {}
	
	@Override
    protected String getLivingSound()
    {
        return "lotr:orc.say";
    }
	
	@Override
    protected String getHurtSound()
    {
        return "lotr:orc.hurt";
    }
	
	@Override
    protected String getDeathSound()
    {
        return "lotr:orc.death";
    }
	
	@Override
	public String getSpeechBank(EntityPlayer entityplayer)
	{
		if (isFriendly(entityplayer))
		{
			if (hiredNPCInfo.getHiringPlayer() == entityplayer)
			{
				return "orc_hired";
			}
			else if (LOTRLevelData.getAlignment(entityplayer, getFaction()) <= LOTRAlignmentValues.ORC_FRIENDLY)
			{
				return "orc_friendly";
			}
			else
			{
				return "orc_neutral";
			}
		}
		else
		{
			return "orc_hostile";
		}
	}
	
	public boolean renderOrcSkullStaff()
	{
		return hasSkullStaff && getEquipmentInSlot(0) == null;
	}
}
