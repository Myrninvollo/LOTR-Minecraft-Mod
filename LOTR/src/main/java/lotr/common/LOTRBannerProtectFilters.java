package lotr.common;

import static lotr.common.LOTRBannerProtectFilters.BannerProtection.*;
import lotr.common.entity.LOTREntityInvasionSpawner;
import lotr.common.entity.item.LOTREntityBanner;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.IChatComponent;

public class LOTRBannerProtectFilters
{
	public static enum BannerProtection
	{
		NONE,
		FACTION,
		PLAYER_SPECIFIC;
	}
	
	public static interface IFilter
	{
		public abstract BannerProtection protects(LOTREntityBanner banner);
		
		public abstract void warnProtection(IChatComponent message);
	}
	
	public static IFilter forPlayer(final EntityPlayer entityplayer)
	{
		return new IFilter()
		{
			@Override
			public BannerProtection protects(LOTREntityBanner banner)
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
						if (alignment <= 0)
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
			public BannerProtection protects(LOTREntityBanner banner)
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
			public BannerProtection protects(LOTREntityBanner banner)
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
			public BannerProtection protects(LOTREntityBanner banner)
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
			public BannerProtection protects(LOTREntityBanner banner)
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
