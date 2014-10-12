package lotr.common;

import static lotr.common.LOTRBannerProtection.ProtectType.*;

import java.util.List;
import java.util.UUID;

import lotr.common.entity.LOTREntityInvasionSpawner;
import lotr.common.entity.item.LOTREntityBanner;
import net.minecraft.entity.*;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.*;
import net.minecraft.world.World;

import org.apache.commons.lang3.StringUtils;

import com.mojang.authlib.GameProfile;

public class LOTRBannerProtection
{
	public static enum ProtectType
	{
		NONE,
		FACTION,
		PLAYER_SPECIFIC;
	}
	
	public static boolean isProtectedByBanner(World world, Entity entity, IFilter protectFilter, boolean sendMessage)
	{
		int i = MathHelper.floor_double(entity.posX);
		int j = MathHelper.floor_double(entity.boundingBox.minY);
		int k = MathHelper.floor_double(entity.posZ);
		return isProtectedByBanner(world, i, j, k, protectFilter, sendMessage);
	}
	
	public static boolean isProtectedByBanner(World world, int i, int j, int k, IFilter protectFilter, boolean sendMessage)
	{
		return isProtectedByBanner(world, i, j, k, protectFilter, sendMessage, LOTREntityBanner.PROTECTION_RANGE);
	}

	public static boolean isProtectedByBanner(World world, int i, int j, int k, IFilter protectFilter, boolean sendMessage, double range)
	{
		String protectorName = null;
		
		List banners = world.getEntitiesWithinAABB(LOTREntityBanner.class, AxisAlignedBB.getBoundingBox(i, j, k, i + 1, j + 1, k + 1).expand(range, range, range));
		if (!banners.isEmpty())
		{
			bannerSearch:
			for (int l = 0; l < banners.size(); l++)
			{
				LOTREntityBanner banner = (LOTREntityBanner)banners.get(l);
				if (banner.isProtectingTerritory())
				{
					ProtectType result = protectFilter.protects(banner);
					if (result == NONE)
					{
						continue;
					}
					else if (result == FACTION)
					{
						protectorName = banner.getBannerFaction().factionName();
						break bannerSearch;
					}
					else if (result == PLAYER_SPECIFIC)
					{
						UUID placingPlayer = banner.getPlacingPlayer();
						GameProfile profile = MinecraftServer.getServer().func_152358_ax().func_152652_a(placingPlayer);
						if (StringUtils.isEmpty(profile.getName()))
						{
							MinecraftServer.getServer().func_147130_as().fillProfileProperties(profile, true);
						}
						protectorName = profile.getName();
						break bannerSearch;
					}
				}
			}
		}
		
		if (protectorName != null)
		{
			if (sendMessage)
			{
				protectFilter.warnProtection(new ChatComponentTranslation("chat.lotr.protectedLand", new Object[] {protectorName}));
			}
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public static interface IFilter
	{
		public abstract ProtectType protects(LOTREntityBanner banner);
		
		public abstract void warnProtection(IChatComponent message);
	}
	
	public static IFilter anyBanner()
	{
		return new IFilter()
		{
			@Override
			public ProtectType protects(LOTREntityBanner banner)
			{
				return FACTION;
			}

			@Override
			public void warnProtection(IChatComponent message) {}
		};
	}
	
	public static IFilter forPlayer(final EntityPlayer entityplayer)
	{
		return new IFilter()
		{
			@Override
			public ProtectType protects(LOTREntityBanner banner)
			{
				if (entityplayer.capabilities.isCreativeMode)
				{
					return NONE;
				}
				else
				{
					if (banner.playerSpecificProtection)
					{
						if (!banner.isPlayerWhitelisted(entityplayer))
						{
							return PLAYER_SPECIFIC;
						}
						return NONE;
					}
					else
					{
						int alignment = LOTRLevelData.getData(entityplayer).getAlignment(banner.getBannerFaction());
						if (alignment < banner.getAlignmentProtection())
						{
							return FACTION;
						}
						return NONE;
					}
				}
			}

			@Override
			public void warnProtection(IChatComponent message)
			{
				if (entityplayer instanceof EntityPlayerMP)
				{
					EntityPlayerMP entityplayermp = (EntityPlayerMP)entityplayer;
					entityplayermp.addChatMessage(message);
					entityplayermp.sendContainerToPlayer(entityplayer.inventoryContainer);
				}
			}
		};
	}
	
	public static IFilter forNPC(final EntityLiving entity)
	{
		return new IFilter()
		{
			@Override
			public ProtectType protects(LOTREntityBanner banner)
			{
				if (LOTRMod.getNPCFaction(entity).isEnemy(banner.getBannerFaction()))
				{
					return FACTION;
				}
				return NONE;
			}

			@Override
			public void warnProtection(IChatComponent message) {}
		};
	}
	
	public static IFilter forInvasionSpawner(final LOTREntityInvasionSpawner spawner)
	{
		return new IFilter()
		{
			@Override
			public ProtectType protects(LOTREntityBanner banner)
			{
				if (spawner.getFaction().isEnemy(banner.getBannerFaction()))
				{
					return FACTION;
				}
				return NONE;
			}

			@Override
			public void warnProtection(IChatComponent message) {}
		};
	}
	
	public static IFilter forTNT(final EntityTNTPrimed bomb)
	{
		return new IFilter()
		{
			@Override
			public ProtectType protects(LOTREntityBanner banner)
			{
				EntityLivingBase bomber = bomb.getTntPlacedBy();
				
				if (bomber == null)
				{
					return FACTION;
				}
				else if (bomber instanceof EntityPlayer)
				{
					return forPlayer((EntityPlayer)bomber).protects(banner);
				}
				else if (bomber instanceof EntityLiving)
				{
					return forNPC((EntityLiving)bomber).protects(banner);
				}
				
				return NONE;
			}

			@Override
			public void warnProtection(IChatComponent message) {}
		};
	}
	
	public static IFilter forThrown(final EntityThrowable throwable)
	{
		return new IFilter()
		{
			@Override
			public ProtectType protects(LOTREntityBanner banner)
			{
				EntityLivingBase thrower = throwable.getThrower();
				
				if (thrower == null)
				{
					return FACTION;
				}
				else if (thrower instanceof EntityPlayer)
				{
					return forPlayer((EntityPlayer)thrower).protects(banner);
				}
				else if (thrower instanceof EntityLiving)
				{
					return forNPC((EntityLiving)thrower).protects(banner);
				}
				
				return NONE;
			}

			@Override
			public void warnProtection(IChatComponent message) {}
		};
	}
}
