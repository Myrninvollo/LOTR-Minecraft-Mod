package lotr.common.entity.npc;

import lotr.common.*;
import lotr.common.entity.ai.LOTREntityAIAttackOnCollide;
import lotr.common.entity.animal.LOTREntityElk;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTREntityWoodElfWarrior extends LOTREntityWoodElf
{
	public LOTREntityWoodElfWarrior(World world)
	{
		super(world);
		tasks.removeTask(rangedAttackAI);
		tasks.addTask(2, meleeAttackAI);
		spawnRidingHorse = rand.nextInt(4) == 0;
		npcShield = LOTRShields.ALIGNMENT_WOOD_ELF;
	}
	
	@Override
	public LOTRNPCMount createMountToRide()
	{
		LOTREntityElk elk = new LOTREntityElk(worldObj);
		elk.setMountArmor(new ItemStack(LOTRMod.elkArmorWoodElven));
		return elk;
	}
	
	@Override
	public EntityAIBase createElfRangedAttackAI()
	{
		return new EntityAIArrowAttack(this, 1.25D, 25, 35, 24F);
	}
	
	@Override
	public EntityAIBase createElfMeleeAttackAI()
	{
		return new LOTREntityAIAttackOnCollide(this, 1.4D, false);
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
			setCurrentItemOrArmor(0, new ItemStack(LOTRMod.spearWoodElven));
		}
		else
		{
			setCurrentItemOrArmor(0, new ItemStack(getElfSwordId(), 1, 0));
		}
		setCurrentItemOrArmor(1, new ItemStack(LOTRMod.bootsWoodElven));
		setCurrentItemOrArmor(2, new ItemStack(LOTRMod.legsWoodElven));
		setCurrentItemOrArmor(3, new ItemStack(LOTRMod.bodyWoodElven));
		if (rand.nextInt(10) != 0)
		{
			setCurrentItemOrArmor(4, new ItemStack(LOTRMod.helmetWoodElven));
		}
		else
		{
			setCurrentItemOrArmor(4, null);
		}
		return data;
    }
	
	@Override
	public Item getElfSwordId()
	{
		return LOTRMod.swordWoodElven;
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
    public void attackEntityWithRangedAttack(EntityLivingBase target, float f)
    {
        EntityArrow arrow = new EntityArrow(worldObj, this, target, 1.3F + (getDistanceToEntity(target) / 24F * 0.3F), 0.5F);
		arrow.setDamage(arrow.getDamage() + 0.75D);
        playSound("random.bow", 1F, 1F / (rand.nextFloat() * 0.4F + 0.8F));
        worldObj.spawnEntityInWorld(arrow);
    }
	
	@Override
	public int getAlignmentBonus()
	{
		return LOTRAlignmentValues.Bonuses.WOOD_ELF_WARRIOR;
	}
	
	@Override
	public String getSpeechBank(EntityPlayer entityplayer)
	{
		if (isFriendly(entityplayer))
		{
			if (hiredNPCInfo.getHiringPlayer() == entityplayer)
			{
				return "woodElf_hired";
			}
			else
			{
				if (LOTRLevelData.getData(entityplayer).getAlignment(getFaction()) >= LOTRAlignmentValues.Levels.WOOD_ELF_TRUST)
				{
					return "woodElfWarrior_friendly";
				}
				else
				{
					return "woodElf_neutral";
				}
			}
		}
		else
		{
			return "woodElfWarrior_hostile";
		}
	}
}
