package lotr.common.entity.npc;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRAlignmentValues;
import lotr.common.LOTRLevelData;
import lotr.common.LOTRMod;
import lotr.common.entity.ai.LOTREntityAIAttackOnCollide;
import lotr.common.item.LOTRItemSpear;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class LOTREntityElfWarrior extends LOTREntityElf
{
	public boolean isDefendingTree;
	
	public LOTREntityElfWarrior(World world)
	{
		super(world);
		tasks.addTask(2, meleeAttackAI);
		spawnRidingHorse = rand.nextInt(4) == 0;
	}
	
	@Override
	public EntityAIBase createElfRangedAttackAI()
	{
		return new EntityAIArrowAttack(this, 1.25D, 30, 40, 24F);
	}
	
	@Override
	public EntityAIBase createElfMeleeAttackAI()
	{
		return new LOTREntityAIAttackOnCollide(this, 1.4D, false).setSpearReplacement(LOTRMod.swordElven);
	}
	
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(24D);
    }
	
	@Override
    public IEntityLivingData onSpawnWithEgg(IEntityLivingData data)
    {
		data = super.onSpawnWithEgg(data);
		if (rand.nextInt(5) == 0)
		{
			setCurrentItemOrArmor(0, new ItemStack(LOTRMod.spearElven));
		}
		else
		{
			setCurrentItemOrArmor(0, new ItemStack(getElfSwordId(), 1, 0));
		}
		setCurrentItemOrArmor(1, new ItemStack(LOTRMod.bootsElven));
		setCurrentItemOrArmor(2, new ItemStack(LOTRMod.legsElven));
		setCurrentItemOrArmor(3, new ItemStack(LOTRMod.bodyElven));
		if (rand.nextInt(10) != 0)
		{
			setCurrentItemOrArmor(4, new ItemStack(LOTRMod.helmetElven));
		}
		return data;
    }
	
	@Override
	public Item getElfSwordId()
	{
		return LOTRMod.swordElven;
	}
	
	@Override
	public Item getElfBowId()
	{
		return LOTRMod.elvenBow;
	}
	
	@Override
	public void onElfUpdate()
	{
		if (!worldObj.isRemote)
		{
			ItemStack weapon = getEquipmentInSlot(0);
			if (weapon != null && weapon.getItem() instanceof LOTRItemSpear)
			{
				return;
			}
			
			if (getAttackTarget() != null)
			{
				double d = getDistanceSqToEntity(getAttackTarget());
				if (d < 16D)
				{
					if (weapon == null || weapon.getItem() != getElfSwordId())
					{
						tasks.removeTask(rangedAttackAI);
						tasks.addTask(2, meleeAttackAI);
						setCurrentItemOrArmor(0, new ItemStack(getElfSwordId(), 1, 0));
						weaponChangeCooldown = 20;
					}
				}
				else if (d < getWeaponChangeThresholdRangeSq())
				{
					if (weapon == null || weapon.getItem() != getElfBowId())
					{
						tasks.removeTask(meleeAttackAI);
						tasks.addTask(2, rangedAttackAI);
						setCurrentItemOrArmor(0, new ItemStack(getElfBowId(), 1, 0));
						weaponChangeCooldown = 20;
					}
				}
			}
			else
			{
				if (weapon == null || weapon.getItem() != getElfSwordId())
				{
					if (weaponChangeCooldown > 0)
					{
						weaponChangeCooldown--;
					}
					else
					{
						tasks.removeTask(rangedAttackAI);
						tasks.addTask(2, meleeAttackAI);
						setCurrentItemOrArmor(0, new ItemStack(getElfSwordId(), 1, 0));
					}
				}
			}
		}
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound nbt)
	{
		super.writeEntityToNBT(nbt);
		nbt.setBoolean("DefendingTree", isDefendingTree);
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound nbt)
	{
		super.readEntityFromNBT(nbt);
		isDefendingTree = nbt.getBoolean("DefendingTree");
	}
	
	@Override
	public int getAlignmentBonus()
	{
		return LOTRAlignmentValues.GALADHRIM_WARRIOR_BONUS;
	}
	
	@Override
    public void attackEntityWithRangedAttack(EntityLivingBase target, float f)
    {
        EntityArrow arrow = new EntityArrow(worldObj, this, target, 1.3F + (getDistanceToEntity(target) / 24F * 0.3F), 0.5F);
		arrow.setDamage(arrow.getDamage() + 1D);
        playSound("random.bow", 1F, 1F / (rand.nextFloat() * 0.4F + 0.8F));
        worldObj.spawnEntityInWorld(arrow);
    }
	
	@Override
	public void onDeath(DamageSource damagesource)
	{
		super.onDeath(damagesource);
		if (!worldObj.isRemote && isDefendingTree && damagesource.getEntity() instanceof EntityPlayer)
		{
			EntityPlayer entityplayer = (EntityPlayer)damagesource.getEntity();
			LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.takeMallornWood);
		}
	}
	
	@Override
	public String getSpeechBank(EntityPlayer entityplayer)
	{
		if (isFriendly(entityplayer))
		{
			if (hiredNPCInfo.getHiringPlayer() == entityplayer)
			{
				return "elf_hired";
			}
			return "elfWarrior_friendly";
		}
		else
		{
			return "elfWarrior_hostile";
		}
	}
}
