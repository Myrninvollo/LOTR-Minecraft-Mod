package lotr.common.entity.npc;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRAlignmentValues;
import lotr.common.LOTRLevelData;
import lotr.common.LOTRMod;
import lotr.common.entity.npc.LOTREntityNPC.AttackMode;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTREntityNearHaradBazaarTrader extends LOTREntityNearHaradrim implements LOTRTradeable
{
	private Item heldItem;
	
	public LOTREntityNearHaradBazaarTrader(World world, LOTRTradeEntry[] buyTrades, LOTRTradeEntry[] sellTrades, Item item)
	{
		super(world);
		heldItem = item;
		
		addTargetTasks(false);

		if (!worldObj.isRemote)
		{
			traderNPCInfo.setBuyTrades(LOTRTradeEntry.getRandomTrades(buyTrades, rand, true));
			traderNPCInfo.setSellTrades(LOTRTradeEntry.getRandomTrades(sellTrades, rand, false));
		}
	}
	
	@Override
    public IEntityLivingData onSpawnWithEgg(IEntityLivingData data)
    {
		data = super.onSpawnWithEgg(data);
		setCurrentItemOrArmor(0, new ItemStack(heldItem));
		return data;
    }
	
	@Override
	public void onAttackModeChange(AttackMode mode)
	{
		if (mode == AttackMode.IDLE)
		{
			setCurrentItemOrArmor(0, new ItemStack(heldItem));
		}
		else
		{
			setCurrentItemOrArmor(0, new ItemStack(LOTRMod.daggerNearHarad));
		}
	}
	
	@Override
	public int getAlignmentBonus()
	{
		return LOTRAlignmentValues.Bonuses.NEAR_HARADRIM_TRADER;
	}
	
	@Override
	public boolean canTradeWith(EntityPlayer entityplayer)
	{
		return LOTRLevelData.getData(entityplayer).getAlignment(getFaction()) >= LOTRAlignmentValues.Levels.NEAR_HARAD_BAZAAR_TRADE && isFriendly(entityplayer);
	}
	
	@Override
	public void onPlayerBuyItem(EntityPlayer entityplayer, ItemStack itemstack)
	{
		LOTRLevelData.getData(entityplayer).addAchievement(LOTRAchievement.tradeBazaarTrader);
	}
	
	@Override
	public void onPlayerSellItem(EntityPlayer entityplayer, ItemStack itemstack)
	{
		LOTRLevelData.getData(entityplayer).addAchievement(LOTRAchievement.tradeBazaarTrader);
	}
	
	@Override
	public boolean shouldTraderRespawn()
	{
		return true;
	}
	
	@Override
	public String getSpeechBank(EntityPlayer entityplayer)
	{
		if (isFriendly(entityplayer))
		{
			return "nearHaradrimBazaarTrader_friendly";
		}
		else
		{
			return "nearHaradrimBazaarTrader_unfriendly";
		}
	}
}
