package lotr.common.item;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.List;

import lotr.common.*;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S3FPacketCustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class LOTRItemSauronMace extends LOTRItemSword
{
	public LOTRItemSauronMace()
	{
		super(LOTRMod.toolOrc);
		setMaxStackSize(1);
		setMaxDamage(1500);
		setCreativeTab(LOTRCreativeTabs.tabCombat);
		setFull3D();
		lotrWeaponDamage = 8F;
	}
	
	@Override
    public ItemStack onEaten(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
		itemstack.damageItem(2, entityplayer);
		return useSauronMace(itemstack, world, entityplayer);
	}
	
	public static ItemStack useSauronMace(ItemStack itemstack, World world, EntityLivingBase user)
	{
        user.swingItem();
		world.playSoundAtEntity(user, "lotr:item.maceSauron", 2F, (itemRand.nextFloat() - itemRand.nextFloat()) * 0.2F + 1.0F);
		
		if (!world.isRemote)
		{
			List entities = world.getEntitiesWithinAABB(EntityLivingBase.class, user.boundingBox.expand(12D, 8D, 12D));
			if (!entities.isEmpty())
			{
				for (int i = 0; i < entities.size(); i++)
				{
					EntityLivingBase entity = (EntityLivingBase)entities.get(i);
					if (entity != user)
					{
						if (entity instanceof EntityLiving)
						{
							EntityLiving entityliving = (EntityLiving)entity;
							if (LOTRMod.getNPCFaction(entityliving).isAllied(LOTRFaction.MORDOR))
							{
								continue;
							}
						}
						if (entity instanceof EntityPlayer)
						{
							if (user instanceof EntityPlayer)
							{
								if (!MinecraftServer.getServer().isPVPEnabled() || LOTRLevelData.getData((EntityPlayer)entity).getAlignment(LOTRFaction.MORDOR) > 0)
								{
									continue;
								}
							}
							else if (user instanceof EntityLiving)
							{
								if (((EntityLiving)user).getAttackTarget() != entity && LOTRLevelData.getData((EntityPlayer)entity).getAlignment(LOTRFaction.MORDOR) > 0)
								{
									continue;
								}
							}
						}
						float strength = 6F - (user.getDistanceToEntity(entity) * 0.75F);
						if (strength < 1F)
						{
							strength = 1F;
						}
						if (user instanceof EntityPlayer)
						{
							entity.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer)user), 6F * strength);
						}
						else
						{
							entity.attackEntityFrom(DamageSource.causeMobDamage(user), 6F * strength);
						}
						float knockback = strength;
						if (knockback > 4F)
						{
							knockback = 4F;
						}
						entity.addVelocity(-MathHelper.sin((user.rotationYaw * (float)Math.PI) / 180F) * 0.7F * knockback, 0.2D + (0.12D * knockback), MathHelper.cos((user.rotationYaw * (float)Math.PI) / 180F) * 0.7F * knockback);
					}
				}
			}
			
			ByteBuf data = Unpooled.buffer();
			
			data.writeInt(user.getEntityId());

			S3FPacketCustomPayload packet = new S3FPacketCustomPayload("lotr.mace", data);
			MinecraftServer.getServer().getConfigurationManager().sendToAllNear(user.posX, user.posY, user.posZ, 64D, user.dimension, packet);
		}
		
		return itemstack;
	}

    @Override
    public int getMaxItemUseDuration(ItemStack itemstack)
    {
        return 40;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack itemstack)
    {
        return EnumAction.bow;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        entityplayer.setItemInUse(itemstack, getMaxItemUseDuration(itemstack));
        return itemstack;
    }
}
