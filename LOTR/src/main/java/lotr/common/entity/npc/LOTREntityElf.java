package lotr.common.entity.npc;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRAlignmentValues;
import lotr.common.LOTRFaction;
import lotr.common.LOTRFoods;
import lotr.common.LOTRMod;
import lotr.common.entity.ai.LOTREntityAIAttackOnCollide;
import lotr.common.entity.ai.LOTREntityAIDrink;
import lotr.common.entity.ai.LOTREntityAIEat;
import lotr.common.entity.ai.LOTREntityAIFollowHiringPlayer;
import lotr.common.entity.ai.LOTREntityAIHiredRemainStill;
import lotr.common.entity.ai.LOTREntityAIHiringPlayerHurtByTarget;
import lotr.common.entity.ai.LOTREntityAIHiringPlayerHurtTarget;
import lotr.common.entity.animal.LOTREntityHorse;
import lotr.common.entity.npc.LOTREntityNPC.AttackMode;
import lotr.common.world.biome.LOTRBiomeGenLothlorien;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIArrowAttack;
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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

public abstract class LOTREntityElf extends LOTREntityNPC implements IRangedAttackMob
{
	protected EntityAIBase rangedAttackAI = createElfRangedAttackAI();
	protected EntityAIBase meleeAttackAI = createElfMeleeAttackAI();
	
	public LOTREntityElf(World world)
	{
		super(world);
		setSize(0.6F, 1.8F);
		getNavigator().setAvoidsWater(true);
		getNavigator().setBreakDoors(true);
        tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(1, new LOTREntityAIHiredRemainStill(this));
		tasks.addTask(3, new LOTREntityAIFollowHiringPlayer(this));
		tasks.addTask(4, new EntityAIOpenDoor(this, true));
        tasks.addTask(5, new EntityAIWander(this, 1D));
        tasks.addTask(6, new LOTREntityAIEat(this, LOTRFoods.ELF, 12000));
        tasks.addTask(6, new LOTREntityAIDrink(this, (this instanceof LOTREntityWoodElf ? LOTRFoods.WOOD_ELF_DRINK : LOTRFoods.ELF_DRINK), 8000));
        tasks.addTask(7, new EntityAIWatchClosest2(this, EntityPlayer.class, 8F, 0.1F));
        tasks.addTask(7, new EntityAIWatchClosest2(this, LOTREntityNPC.class, 5F, 0.05F));
        tasks.addTask(8, new EntityAIWatchClosest(this, EntityLiving.class, 8F, 0.02F));
        tasks.addTask(9, new EntityAILookIdle(this));
        addTargetTasks(true);
	}
	
	public EntityAIBase createElfRangedAttackAI()
	{
		return new EntityAIArrowAttack(this, 1.25D, 40, 60, 16F);
	}
	
	public EntityAIBase createElfMeleeAttackAI()
	{
		return new LOTREntityAIAttackOnCollide(this, 1.5D, false);
	}

	@Override
	protected void entityInit()
	{
		super.entityInit();
		familyInfo.setNPCMale(rand.nextBoolean());
		dataWatcher.addObject(15, LOTRNames.getRandomElfName(familyInfo.isNPCMale(), rand));
	}
	
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(30D);
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.2D);
    }
	
	public String getElfName()
	{
		return dataWatcher.getWatchableObjectString(15);
	}
	
	public void setElfName(String name)
	{
		dataWatcher.updateObject(15, name);
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound nbt)
	{
		super.writeEntityToNBT(nbt);
		nbt.setString("ElfName", getElfName());
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound nbt)
	{
		super.readEntityFromNBT(nbt);
		if (nbt.hasKey("ElfName"))
		{
			setElfName(nbt.getString("ElfName"));
		}
		
		if (nbt.hasKey("ElfGender"))
		{
			familyInfo.setNPCMale(nbt.getBoolean("ElfGender"));
		}
	}
	
	@Override
    public boolean isAIEnabled()
    {
        return true;
    }
	
	@Override
	public String getNPCName()
	{
		return getElfName();
	}
	
	public abstract Item getElfSwordId();
	
	public abstract Item getElfBowId();
	
	@Override
	public void onAttackModeChange(AttackMode mode)
	{
		if (mode == AttackMode.IDLE)
		{
			tasks.removeTask(rangedAttackAI);
			tasks.removeTask(meleeAttackAI);
			setCurrentItemOrArmor(0, null);
		}
		
		if (mode == AttackMode.MELEE)
		{
			tasks.removeTask(rangedAttackAI);
			tasks.addTask(2, meleeAttackAI);
			setCurrentItemOrArmor(0, new ItemStack(getElfSwordId(), 1, 0));
		}
		
		if (mode == AttackMode.RANGED)
		{
			tasks.removeTask(meleeAttackAI);
			tasks.addTask(2, rangedAttackAI);
			setCurrentItemOrArmor(0, new ItemStack(getElfBowId(), 1, 0));
		}
	}
	
	@Override
    public void attackEntityWithRangedAttack(EntityLivingBase target, float f)
    {
        EntityArrow arrow = new EntityArrow(worldObj, this, target, 1.5F, 0.5F);
        playSound("random.bow", 1F, 1F / (rand.nextFloat() * 0.4F + 0.8F));
        worldObj.spawnEntityInWorld(arrow);
    }
	
	@Override
	protected void dropFewItems(boolean flag, int i)
	{
		super.dropFewItems(flag, i);
		
		int j = rand.nextInt(2) + rand.nextInt(i + 1);
		for (int k = 0; k < j; k++)
		{
			dropItem(LOTRMod.elfBone, 1);
		}
		
		if (rand.nextBoolean())
		{
			j = rand.nextInt(3) + rand.nextInt(i + 1);
			for (int k = 0; k < j; k++)
			{
				dropItem(Items.arrow, 1);
			}
		}
		
		if (flag)
		{
			int dropChance = 40 - i * 8;
			dropChance = Math.max(dropChance, 1);
			if (rand.nextInt(dropChance) == 0)
			{
				dropItem(LOTRMod.lembas, 1);
			}
		}
	}
	
	@Override
	public boolean getCanSpawnHere()
	{
		if (super.getCanSpawnHere())
		{
			return liftSpawnRestrictions || canElfSpawnHere();
		}
		return false;
	}
	
	public abstract boolean canElfSpawnHere();
	
	@Override
	public void addPotionEffect(PotionEffect effect)
	{
		if (effect.getPotionID() == Potion.poison.id)
		{
			return;
		}
		else
		{
			super.addPotionEffect(effect);
		}
	}
	
	@Override
	public boolean shouldRenderNPCHair()
	{
		return getEquipmentInSlot(4) == null;
	}

	@Override
	public String getLivingSound()
	{
		if (getAttackTarget() == null && rand.nextInt(10) == 0)
		{
			if (familyInfo.isNPCMale())
			{
				return "lotr:elf.male.say";
			}
		}
		return super.getLivingSound();
	}
	
	@Override
	public String getAttackSound()
	{
		return familyInfo.isNPCMale() ? "lotr:elf.male.attack" : super.getAttackSound();
	}
}
