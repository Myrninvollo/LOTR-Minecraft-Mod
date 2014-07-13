package lotr.common.entity.npc;

import lotr.common.LOTRAlignmentValues;
import lotr.common.LOTRMod;
import lotr.common.entity.ai.LOTREntityAIAttackOnCollide;
import lotr.common.entity.ai.LOTREntityAIFollowHiringPlayer;
import lotr.common.entity.ai.LOTREntityAIHiredRemainStill;
import lotr.common.entity.ai.LOTREntityAIHiringPlayerHurtByTarget;
import lotr.common.entity.ai.LOTREntityAIHiringPlayerHurtTarget;
import lotr.common.entity.animal.LOTREntityHorse;
import lotr.common.entity.animal.LOTREntityShirePony;
import lotr.common.entity.projectile.LOTREntityPebble;
import lotr.common.item.LOTRItemLeatherHat;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTREntityHobbitShirriff extends LOTREntityHobbit implements IRangedAttackMob
{
	public EntityAIBase rangedAttackAI = createHobbitRangedAttackAI();
	public EntityAIBase meleeAttackAI = createHobbitMeleeAttackAI();
	public int weaponChangeCooldown = 0;
	
	public LOTREntityHobbitShirriff(World world)
	{
		super(world);
		tasks.taskEntries.clear();
        tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(1, new LOTREntityAIHiredRemainStill(this));
		tasks.addTask(3, new LOTREntityAIFollowHiringPlayer(this));
		tasks.addTask(4, new EntityAIOpenDoor(this, true));
        tasks.addTask(5, new EntityAIWander(this, 1D));
        tasks.addTask(6, new EntityAIWatchClosest2(this, EntityPlayer.class, 8F, 0.2F));
        tasks.addTask(6, new EntityAIWatchClosest2(this, LOTREntityNPC.class, 5F, 0.1F));
        tasks.addTask(7, new EntityAIWatchClosest(this, EntityLiving.class, 8F, 0.02F));
        tasks.addTask(8, new EntityAILookIdle(this));
        targetTasks.addTask(1, new LOTREntityAIHiringPlayerHurtByTarget(this));
        targetTasks.addTask(2, new LOTREntityAIHiringPlayerHurtTarget(this));
        targetTasks.addTask(3, new EntityAIHurtByTarget(this, false));
        addTargetTasks(4);
		spawnRidingHorse = rand.nextInt(3) == 0;
	}
	
	public EntityAIBase createHobbitRangedAttackAI()
	{
		return new EntityAIArrowAttack(this, 1.5D, 20, 40, 12F);
	}
	
	public EntityAIBase createHobbitMeleeAttackAI()
	{
		return new LOTREntityAIAttackOnCollide(this, 1.5D, false);
	}
	
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20D);
		getEntityAttribute(horseAttackSpeed).setBaseValue(2D);
    }
	
	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData data)
	{
		data = super.onSpawnWithEgg(data);
		ItemStack hat = new ItemStack(LOTRMod.leatherHat);
		LOTRItemLeatherHat.setHatColor(hat, LOTRItemLeatherHat.HAT_LEATHER);
		LOTRItemLeatherHat.setFeatherColor(hat, LOTRItemLeatherHat.FEATHER_WHITE);
		setCurrentItemOrArmor(4, hat);
		return data;
	}
	
	@Override
	public LOTRNPCMount createMountToRide()
	{
		return new LOTREntityShirePony(worldObj);
	}
	
	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		
		if (!worldObj.isRemote)
		{
			ItemStack weapon = getEquipmentInSlot(0);
			if (getAttackTarget() != null)
			{
				double d = getDistanceSqToEntity(getAttackTarget());
				if (d < 16D)
				{
					if (weapon == null || weapon.getItem() != getHobbitMeleeWeaponId())
					{
						tasks.removeTask(rangedAttackAI);
						tasks.addTask(2, meleeAttackAI);
						setCurrentItemOrArmor(0, new ItemStack(getHobbitMeleeWeaponId(), 1, 0));
						weaponChangeCooldown = 20;
					}
				}
				else if (d < getWeaponChangeThresholdRangeSq())
				{
					if (weapon == null || weapon.getItem() != getHobbitRangedWeaponId())
					{
						tasks.removeTask(meleeAttackAI);
						tasks.addTask(2, rangedAttackAI);
						setCurrentItemOrArmor(0, new ItemStack(getHobbitRangedWeaponId(), 1, 0));
						weaponChangeCooldown = 20;
					}
				}
			}
			else
			{
				if (weapon != null)
				{
					if (weaponChangeCooldown > 0)
					{
						weaponChangeCooldown--;
					}
					else
					{
						tasks.removeTask(rangedAttackAI);
						tasks.removeTask(meleeAttackAI);
						setCurrentItemOrArmor(0, null);
					}
				}
			}
		}
	}
	
	public Item getHobbitMeleeWeaponId()
	{
		return LOTRMod.daggerIron;
	}
	
	public Item getHobbitRangedWeaponId()
	{
		return LOTRMod.sling;
	}
	
	@Override
    public void attackEntityWithRangedAttack(EntityLivingBase target, float f)
    {
		EntityArrow template = new EntityArrow(worldObj, this, target, 1F, 0.5F);
        LOTREntityPebble pebble = new LOTREntityPebble(worldObj, this);
		pebble.setLocationAndAngles(template.posX, template.posY, template.posZ, template.rotationYaw, template.rotationPitch);
		pebble.motionX = template.motionX;
		pebble.motionY = template.motionY;
		pebble.motionZ = template.motionZ;
        playSound("random.bow", 1F, 1F / (rand.nextFloat() * 0.4F + 0.8F));
        worldObj.spawnEntityInWorld(pebble);
    }
	
	@Override
	public int getAlignmentBonus()
	{
		return LOTRAlignmentValues.HOBBIT_SHIRRIFF_BONUS;
	}
	
	@Override
	protected void dropFewItems(boolean flag, int i)
	{
		super.dropFewItems(flag, i);
		
		dropNPCEquipment(flag, i);
		
		int dropChance = 10 - i * 2;
		if (dropChance < 1)
		{
			dropChance = 1;
		}
		if (rand.nextInt(dropChance) == 0)
		{
			dropItem(LOTRMod.pebble, 1 + rand.nextInt(3) + rand.nextInt(i + 1));
		}
	}
	
	@Override
	protected int getExperiencePoints(EntityPlayer entityplayer)
    {
        return 2 + rand.nextInt(3);
    }
	
	@Override
	public String getSpeechBank(EntityPlayer entityplayer)
	{
		if (isFriendly(entityplayer))
		{
			if (hiredNPCInfo.getHiringPlayer() == entityplayer)
			{
				return "hobbitShirriff_hired";
			}
			return "hobbitShirriff_friendly";
		}
		else
		{
			return "hobbitShirriff_hostile";
		}
	}
}
