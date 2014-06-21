package lotr.common.entity.npc;

import java.util.List;

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
import lotr.common.entity.ai.LOTREntityAINearestAttackableTargetOrc;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWatchClosest2;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class LOTREntityOlogHai extends LOTREntityTroll
{
	public LOTREntityOlogHai(World world)
	{
		super(world);
		setSize(2F, 4F);
		tasks.taskEntries.clear();
		targetTasks.taskEntries.clear();
        tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(1, new LOTREntityAIHiredRemainStill(this));
		tasks.addTask(2, new LOTREntityAIAttackOnCollide(this, 2D, false, 0.8F));
		tasks.addTask(3, new LOTREntityAIFollowHiringPlayer(this));
        tasks.addTask(4, new EntityAIWander(this, 1D));
        tasks.addTask(5, new EntityAIWatchClosest2(this, EntityPlayer.class, 12F, 0.02F));
        tasks.addTask(5, new EntityAIWatchClosest2(this, LOTREntityNPC.class, 8F, 0.01F));
        tasks.addTask(6, new EntityAIWatchClosest(this, EntityLiving.class, 12F, 0.01F));
        tasks.addTask(7, new EntityAILookIdle(this));
        targetTasks.addTask(1, new LOTREntityAIHiringPlayerHurtByTarget(this));
        targetTasks.addTask(2, new LOTREntityAIHiringPlayerHurtTarget(this));
        targetTasks.addTask(3, new EntityAIHurtByTarget(this, false));
		addTargetTasks(4, LOTREntityAINearestAttackableTargetOrc.class);
	}
	
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(70D);
		getEntityAttribute(npcAttackDamage).setBaseValue(7D);
	}
	
	@Override
	public LOTRFaction getFaction()
	{
		return LOTRFaction.MORDOR;
	}
	
	@Override
	protected boolean hasTrollName()
	{
		return false;
	}
	
	@Override
	protected boolean canTrollBeTickled(EntityPlayer entityplayer)
	{
		return false;
	}
	
	@Override
    public boolean attackEntityAsMob(Entity entity)
	{
		if (super.attackEntityAsMob(entity))
		{
			float attackDamage = (float)getEntityAttribute(LOTREntityNPC.npcAttackDamage).getAttributeValue();
			float knockbackModifier = 0.25F * attackDamage;
			entity.addVelocity((double)(-MathHelper.sin(rotationYaw * (float)Math.PI / 180F) * knockbackModifier * 0.5F), 0D, (double)(MathHelper.cos(rotationYaw * (float)Math.PI / 180F) * knockbackModifier * 0.5F));
			worldObj.playSoundAtEntity(entity, "lotr:troll.ologHai_hammer", 1F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
			if (!worldObj.isRemote)
			{
				List entities = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, entity.boundingBox.expand(4D, 4D, 4D));
				if (!entities.isEmpty())
				{
					for (int i = 0; i < entities.size(); i++)
					{
						EntityLivingBase hitEntity = (EntityLivingBase)entities.get(i);
						if (hitEntity != this && hitEntity != entity)
						{
							if (entity instanceof EntityLiving)
							{
								EntityLiving entityliving = (EntityLiving)entity;
								if (LOTRMod.getNPCFaction(entityliving).isAllied(LOTRFaction.MORDOR))
								{
									continue;
								}
							}
							if (hitEntity instanceof EntityPlayer)
							{
								if (getAttackTarget() != hitEntity && LOTRLevelData.getAlignment((EntityPlayer)hitEntity, getFaction()) < 0)
								{
									continue;
								}
							}
							float strength = 4F - (entity.getDistanceToEntity(hitEntity));
							strength += 1F;
							if (strength > 4F)
							{
								strength = 4F;
							}
							hitEntity.attackEntityFrom(DamageSource.causeMobDamage(this), (strength / 4F) * attackDamage);
							float knockback = strength * 0.25F;
							if (knockback < 0.75F)
							{
								knockback = 0.75F;
							}
							hitEntity.addVelocity(-MathHelper.sin((rotationYaw * (float)Math.PI) / 180F) * knockback * 0.5F, 0.2D + (0.12D * knockback), MathHelper.cos((rotationYaw * (float)Math.PI) / 180F) * knockback * 0.5F);
						}
					}
				}
			}	
			return true;
		}
		return false;
	}
	
	@Override
	protected LOTRAchievement getKillAchievement()
	{
		return LOTRAchievement.killOlogHai;
	}
	
	@Override
	public int getAlignmentBonus()
	{
		return LOTRAlignmentValues.OLOG_HAI_BONUS;
	}
	
	public void dropTrollItems(boolean flag, int i)
	{
		if (flag)
		{
			int rareDropChance = 8 - i;
			if (rareDropChance < 1)
			{
				rareDropChance = 1;
			}
			
			if (rand.nextInt(rareDropChance) == 0)
			{
				int drops = 1 + rand.nextInt(2) + rand.nextInt(i + 1);
				for (int j = 0; j < drops; j++)
				{
					dropItem(LOTRMod.orcSteel, 1);
				}
			}
		}
	}
	
	@Override
	protected int getExperiencePoints(EntityPlayer entityplayer)
    {
        return 5 + rand.nextInt(6);
    }
	
	@Override
	public String getSpeechBank(EntityPlayer entityplayer)
	{
		return null;
	}
}
