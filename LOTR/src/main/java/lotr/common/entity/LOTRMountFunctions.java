package lotr.common.entity;

import java.util.Random;

import lotr.common.entity.npc.LOTREntityNPC;
import lotr.common.entity.npc.LOTRNPCMount;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class LOTRMountFunctions
{
	public static void setNavigatorRangeFromNPC(LOTRNPCMount mount, LOTREntityNPC npc)
	{
		EntityLiving entity = (EntityLiving)mount;
		double d = npc.getEntityAttribute(SharedMonsterAttributes.followRange).getAttributeValue();
		entity.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(d);
	}
	
	public static void update(LOTRNPCMount mount)
	{
		EntityLiving entity = (EntityLiving)mount;
		World world = entity.worldObj;
		Random rand = entity.getRNG();
		
		if (!world.isRemote)
		{
			if (rand.nextInt(900) == 0 && entity.isEntityAlive())
            {
				entity.heal(1F);
            }

			if (!(entity instanceof LOTREntityNPC))
			{
				if (entity.getAttackTarget() != null)
				{
					EntityLivingBase target = entity.getAttackTarget();
					if (!target.isEntityAlive() || (target instanceof EntityPlayer && ((EntityPlayer)target).capabilities.isCreativeMode))
					{
						entity.setAttackTarget(null);
					}
				}
				
				if (entity.riddenByEntity instanceof EntityLiving)
				{
					EntityLivingBase target = ((EntityLiving)entity.riddenByEntity).getAttackTarget();
					entity.setAttackTarget(target);
				}
				else if (entity.riddenByEntity instanceof EntityPlayer)
				{
					entity.setAttackTarget(null);
				}
			}
		}
	}
	
	public static boolean interact(LOTRNPCMount mount, EntityPlayer entityplayer)
	{
		EntityLiving entity = (EntityLiving)mount;
		
		if (mount.getBelongsToNPC() && entity.riddenByEntity == null)
		{
			if (!entity.worldObj.isRemote)
			{
				entityplayer.addChatMessage(new ChatComponentTranslation("chat.lotr.mountOwnedByNPC"));
			}
			return true;
		}
		return false;
	}
	
	public static void move(LOTRNPCMount mount, float strafe, float forward)
	{
		EntityLiving entity = (EntityLiving)mount;
		
		if (entity.riddenByEntity != null && entity.riddenByEntity instanceof EntityPlayer && mount.getSaddled())
        {
			entity.prevRotationYaw = entity.rotationYaw = entity.riddenByEntity.rotationYaw;
            entity.rotationPitch = entity.riddenByEntity.rotationPitch * 0.5F;
            entity.rotationYaw = entity.rotationYaw % 360F;
            entity.rotationPitch = entity.rotationPitch % 360F;
            entity.rotationYawHead = entity.renderYawOffset = entity.rotationYaw;
            strafe = ((EntityLivingBase)entity.riddenByEntity).moveStrafing * 0.5F;
            forward = ((EntityLivingBase)entity.riddenByEntity).moveForward;

            if (forward <= 0F)
            {
            	forward *= 0.25F;
            }

            entity.stepHeight = 1F;
            entity.jumpMovementFactor = entity.getAIMoveSpeed() * 0.1F;

            if (!entity.worldObj.isRemote)
            {
            	entity.setAIMoveSpeed((float)entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue());
            	mount.super_moveEntityWithHeading(strafe, forward);
            }

            entity.prevLimbSwingAmount = entity.limbSwingAmount;
            double d0 = entity.posX - entity.prevPosX;
            double d1 = entity.posZ - entity.prevPosZ;
            float f4 = MathHelper.sqrt_double(d0 * d0 + d1 * d1) * 4F;

            if (f4 > 1F)
            {
                f4 = 1F;
            }

            entity.limbSwingAmount += (f4 - entity.limbSwingAmount) * 0.4F;
            entity.limbSwing += entity.limbSwingAmount;
        }
        else
        {
        	entity.stepHeight = 0.5F;
        	entity.jumpMovementFactor = 0.02F;
        	mount.super_moveEntityWithHeading(strafe, forward);
        }
	}
}
