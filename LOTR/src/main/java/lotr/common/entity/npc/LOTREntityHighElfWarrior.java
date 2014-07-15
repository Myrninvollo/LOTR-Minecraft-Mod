package lotr.common.entity.npc;

import lotr.common.LOTRAlignmentValues;
import lotr.common.LOTRMod;
import lotr.common.entity.ai.LOTREntityAIAttackOnCollide;
import lotr.common.entity.npc.LOTREntityNPC.AttackMode;
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
import net.minecraft.world.World;

public class LOTREntityHighElfWarrior extends LOTREntityHighElf
{
	public LOTREntityHighElfWarrior(World world)
	{
		super(world);
		tasks.addTask(2, meleeAttackAI);
		spawnRidingHorse = rand.nextInt(4) == 0;
	}
	
	@Override
	public EntityAIBase createElfRangedAttackAI()
	{
		return new EntityAIArrowAttack(this, 1.25D, 25, 40, 24F);
	}
	
	@Override
	public EntityAIBase createElfMeleeAttackAI()
	{
		return new LOTREntityAIAttackOnCollide(this, 1.5D, false).setSpearReplacement(LOTRMod.swordHighElven);
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
			setCurrentItemOrArmor(0, new ItemStack(LOTRMod.spearHighElven));
		}
		else
		{
			setCurrentItemOrArmor(0, new ItemStack(getElfSwordId(), 1, 0));
		}
		setCurrentItemOrArmor(1, new ItemStack(LOTRMod.bootsHighElven));
		setCurrentItemOrArmor(2, new ItemStack(LOTRMod.legsHighElven));
		setCurrentItemOrArmor(3, new ItemStack(LOTRMod.bodyHighElven));
		if (rand.nextInt(10) != 0)
		{
			setCurrentItemOrArmor(4, new ItemStack(LOTRMod.helmetHighElven));
		}
		return data;
    }
	
	@Override
	public Item getElfSwordId()
	{
		return LOTRMod.swordHighElven;
	}
	
	@Override
	public Item getElfBowId()
	{
		return LOTRMod.elvenBow;
	}
	
	@Override
	public void onAttackModeChange(AttackMode mode)
	{
		if (mode == AttackMode.IDLE)
		{
			setCurrentItemOrArmor(0, new ItemStack(getElfSwordId(), 1, 0));
		}
		else
		{
			super.onAttackModeChange(mode);
		}
	}
	
	@Override
	public int getAlignmentBonus()
	{
		return LOTRAlignmentValues.HIGH_ELF_WARRIOR_BONUS;
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
	public String getSpeechBank(EntityPlayer entityplayer)
	{
		if (isFriendly(entityplayer))
		{
			if (hiredNPCInfo.getHiringPlayer() == entityplayer)
			{
				return "highElf_hired";
			}
			return "highElfWarrior_friendly";
		}
		else
		{
			return "highElfWarrior_hostile";
		}
	}
}
