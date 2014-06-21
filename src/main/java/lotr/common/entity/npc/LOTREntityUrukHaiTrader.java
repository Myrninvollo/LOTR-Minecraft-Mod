package lotr.common.entity.npc;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRAlignmentValues;
import lotr.common.LOTRLevelData;
import lotr.common.LOTRMod;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTREntityUrukHaiTrader extends LOTREntityUrukHai implements LOTRTradeable
{
	public LOTREntityUrukHaiTrader(World world)
	{
		super(world);
		targetTasks.taskEntries.clear();
		targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
		
		if (!worldObj.isRemote)
		{
			traderNPCInfo.setBuyTrades(LOTRTradeEntry.getRandomTrades(LOTRTradeEntry.URUK_HAI_TRADER_BUY, rand, true));
			traderNPCInfo.setSellTrades(LOTRTradeEntry.getRandomTrades(LOTRTradeEntry.URUK_HAI_TRADER_SELL, rand, false));
		}
	}
	
	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData data)
	{
		data = super.onSpawnWithEgg(data);
		setCurrentItemOrArmor(0, new ItemStack(LOTRMod.daggerUrukPoisoned));
		setCurrentItemOrArmor(1, new ItemStack(LOTRMod.bootsWarg));
		setCurrentItemOrArmor(2, new ItemStack(LOTRMod.legsWarg));
		setCurrentItemOrArmor(3, new ItemStack(LOTRMod.bodyWarg));
		setCurrentItemOrArmor(4, new ItemStack(LOTRMod.helmetWarg));
		return data;
	}
	
	@Override
	public int getAlignmentBonus()
	{
		return LOTRAlignmentValues.URUK_HAI_TRADER_BONUS;
	}
	
	@Override
	public boolean canTradeWith(EntityPlayer entityplayer)
	{
		return LOTRLevelData.getAlignment(entityplayer, getFaction()) >= LOTRAlignmentValues.URUK_HAI_TRADER_TRADE && isFriendly(entityplayer);
	}
	
	@Override
	public void onPlayerBuyItem(EntityPlayer entityplayer, ItemStack itemstack)
	{
		LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.tradeUrukTrader);
	}
	
	@Override
	public void onPlayerSellItem(EntityPlayer entityplayer, ItemStack itemstack)
	{
		LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.tradeUrukTrader);
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
			if (canTradeWith(entityplayer))
			{
				return "urukHaiTrader_friendly";
			}
			else
			{
				return "urukHaiTrader_neutral";
			}
		}
		else
		{
			return "orc_hostile";
		}
	}
}
