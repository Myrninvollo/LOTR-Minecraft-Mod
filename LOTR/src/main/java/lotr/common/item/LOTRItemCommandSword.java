package lotr.common.item;

import java.util.List;

import lotr.common.LOTRMod;
import lotr.common.entity.npc.LOTREntityNPC;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class LOTRItemCommandSword extends LOTRItemSword
{
	public LOTRItemCommandSword()
	{
		super(ToolMaterial.IRON);
        setMaxDamage(0);
	}
	
	@Override
    public boolean isDamageable()
    {
        return false;
    }
	
	@Override
	public int getItemEnchantability()
    {
        return 0;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
		entityplayer.swingItem();
		
		if (!world.isRemote)
		{
			Entity entity = getEntityTarget(entityplayer);
			if (entity instanceof EntityLivingBase)
			{
				EntityLivingBase target = (EntityLivingBase)entity;
				if (LOTRMod.canPlayerAttackEntity(entityplayer, target, true))
				{
					entityplayer.setRevengeTarget(null);
					
					List nearbyHiredNPCs = world.getEntitiesWithinAABB(LOTREntityNPC.class, entityplayer.boundingBox.expand(8D, 8D, 8D));
					for (int i = 0; i < nearbyHiredNPCs.size(); i++)
					{
						LOTREntityNPC npc = (LOTREntityNPC)nearbyHiredNPCs.get(i);
						if (npc.hiredNPCInfo.isActive && npc.hiredNPCInfo.getHiringPlayer() == entityplayer && npc.hiredNPCInfo.getObeyCommandSword())
						{
							if (LOTRMod.canNPCAttackEntity(npc, target))
							{
								npc.getNavigator().clearPathEntity();
								npc.setAttackTarget(target);
							}
						}
					}
				}
			}
		}
		
        return itemstack;
    }
	
	private Entity getEntityTarget(EntityPlayer entityplayer)
	{
		double range = 64D;
		Vec3 position = Vec3.createVectorHelper(entityplayer.posX, entityplayer.posY, entityplayer.posZ);
		Vec3 look = entityplayer.getLookVec();
		Vec3 sight = position.addVector(look.xCoord * range, look.yCoord * range, look.zCoord * range);
		float sightWidth = 1F;
		List list = entityplayer.worldObj.getEntitiesWithinAABBExcludingEntity(entityplayer, entityplayer.boundingBox.addCoord(look.xCoord * range, look.yCoord * range, look.zCoord * range).expand((double)sightWidth, (double)sightWidth, (double)sightWidth));
		
		Entity pointedEntity = null;
		double entityDist = range;

		for (int i = 0; i < list.size(); i++)
		{
			Entity entity = (Entity)list.get(i);
			
			if (!(entity instanceof EntityLivingBase))
			{
				continue;
			}
			
			if (!LOTRMod.canPlayerAttackEntity(entityplayer, (EntityLivingBase)entity, false))
			{
				continue;
			}

			if (entity.canBeCollidedWith())
			{
				float width = 1F;
				AxisAlignedBB axisalignedbb = entity.boundingBox.expand((double)width, (double)width, (double)width);
				MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(position, sight);

				if (axisalignedbb.isVecInside(position))
				{
					if (0D < entityDist || entityDist == 0D)
					{
						pointedEntity = entity;
						entityDist = 0D;
					}
				}
				else if (movingobjectposition != null)
				{
					double d = position.distanceTo(movingobjectposition.hitVec);
					if (d < entityDist || entityDist == 0D)
					{
						if (entity == entityplayer.ridingEntity && !entity.canRiderInteract())
						{
							if (entityDist == 0D)
							{
								pointedEntity = entity;
							}
						}
						else
						{
							pointedEntity = entity;
							entityDist = d;
						}
					}
				}
			}
		}

		if (pointedEntity != null)
		{
			return pointedEntity;
		}
		
		return null;
	}
}
