package lotr.common.entity.animal;

import java.util.List;

import lotr.common.LOTRMod;
import lotr.common.LOTRReflection;
import lotr.common.entity.ai.LOTREntityAIAttackOnCollide;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.World;

public class LOTREntityRhino extends LOTREntityHorse
{
    public LOTREntityRhino(World world)
    {
        super(world);
        setSize(1.5F, 1.6F);
    }
    
	@Override
	protected boolean isMountHostile()
	{
		return true;
	}
	
	@Override
    protected EntityAIBase createMountAttackAI()
    {
    	return new LOTREntityAIAttackOnCollide(this, 1D, true);
    }
	
	@Override
	public int getHorseType()
	{
		return 0;
	}

	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(4D);
    }
	
	@Override
	protected void onLOTRHorseSpawn()
	{
		double maxHealth = getEntityAttribute(SharedMonsterAttributes.maxHealth).getAttributeValue();
		maxHealth *= 1.25D;
		maxHealth = Math.max(maxHealth, 30D);
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(maxHealth);
		
		double speed = getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue();
		speed *= 1.25D;
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(speed);
		
		double jumpStrength = getEntityAttribute(LOTRReflection.getHorseJumpStrength()).getAttributeValue();
		jumpStrength *= 0.5D;
		getEntityAttribute(LOTRReflection.getHorseJumpStrength()).setBaseValue(jumpStrength);
	}
	
	@Override
    public boolean isBreedingItem(ItemStack itemstack)
    {
        return itemstack != null && itemstack.getItem() == Items.wheat;
    }
	
	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();

		if (!worldObj.isRemote)
		{
			if (riddenByEntity instanceof EntityLivingBase)
			{
				EntityLivingBase rhinoRider = (EntityLivingBase)riddenByEntity;
				
				float momentum = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
				if (momentum > 0.2F)
				{
					setSprinting(true);
				}
				else
				{
					setSprinting(false);
				}
				
				if (momentum >= 0.32F)
				{
					float strength = momentum * 15F;
					
					Vec3 position = Vec3.createVectorHelper(posX, posY, posZ);
					Vec3 look = getLookVec();
					float sightWidth = 1F;
					
					double range = 0.5D;
					List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.contract(1D, 1D, 1D).addCoord(look.xCoord * range, look.yCoord * range, look.zCoord * range).expand((double)sightWidth, (double)sightWidth, (double)sightWidth));
					boolean hitAnyEntities = false;
					for (int i = 0; i < list.size(); i++)
					{
						Entity obj = (Entity)list.get(i);
						if (!(obj instanceof EntityLivingBase))
						{
							continue;
						}
						
						EntityLivingBase entity = (EntityLivingBase)obj;
						if (entity == rhinoRider)
						{
							continue;
						}
						
						if (rhinoRider instanceof EntityPlayer && !LOTRMod.canPlayerAttackEntity((EntityPlayer)rhinoRider, entity, false))
						{
							continue;
						}
						
						if (rhinoRider instanceof EntityCreature && !LOTRMod.canNPCAttackEntity((EntityCreature)rhinoRider, entity))
						{
							continue;
						}
						
						boolean flag = entity.attackEntityFrom(DamageSource.causeMobDamage(this), strength);
						if (flag)
						{
							float knockback = strength * 0.05F;
							entity.addVelocity(-MathHelper.sin((rotationYaw * (float)Math.PI) / 180F) * knockback, knockback, MathHelper.cos((rotationYaw * (float)Math.PI) / 180F) * knockback);
							hitAnyEntities = true;
							
							if (entity instanceof EntityLiving)
							{
								EntityLiving entityliving = (EntityLiving)entity;
								if (entityliving.getAttackTarget() == this)
								{
									entityliving.getNavigator().clearPathEntity();
									entityliving.setAttackTarget(rhinoRider);
								}
							}
						}
					}
					
					if (hitAnyEntities)
					{
						worldObj.playSoundAtEntity(this, "lotr:troll.ologHai_hammer", 1F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1F);
					}
				}
			}
			else
			{
				if (getAttackTarget() != null)
				{
					float momentum = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
					if (momentum > 0.2F)
					{
						setSprinting(true);
					}
					else
					{
						setSprinting(false);
					}
				}
				else
				{
					setSprinting(false);
				}
			}
		}
	}
	
	@Override
    protected void dropFewItems(boolean flag, int i)
    {
        int j = rand.nextInt(2) + rand.nextInt(1 + i);
        for (int k = 0; k < j; k++)
        {
			dropItem(LOTRMod.rhinoHorn, 1);
        }
        
        j = rand.nextInt(2) + 1 + rand.nextInt(1 + i);
        for (int l = 0; l < j; l++)
        {
			if (isBurning())
			{
				dropItem(LOTRMod.rhinoCooked, 1);
			}
			else
			{
				dropItem(LOTRMod.rhinoRaw, 1);
			}
        }
    }
	
	@Override
	protected String getLivingSound()
    {
        return "lotr:rhino.say";
    }

	@Override
    protected String getHurtSound()
    {
        return "lotr:rhino.hurt";
    }

	@Override
    protected String getDeathSound()
    {
        return "lotr:rhino.death";
    }
	
	@Override
    protected String getAngrySoundName()
    {
        return "lotr:rhino.say";
    }
}
