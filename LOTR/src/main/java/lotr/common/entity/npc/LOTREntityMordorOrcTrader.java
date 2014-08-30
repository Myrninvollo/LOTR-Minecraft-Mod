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

public class LOTREntityMordorOrcTrader extends LOTREntityMordorOrc implements LOTRTradeable
{
	public LOTREntityMordorOrcTrader(World world)
	{
		super(world);
		addTargetTasks(false);
		
		if (!worldObj.isRemote)
		{
			traderNPCInfo.setBuyTrades(LOTRTradeEntry.getRandomTrades(LOTRTradeEntry.ORC_TRADER_BUY, rand, true));
			traderNPCInfo.setSellTrades(LOTRTradeEntry.getRandomTrades(LOTRTradeEntry.ORC_TRADER_SELL, rand, false));
		}
	}
	
	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData data)
	{
		data = super.onSpawnWithEgg(data);
		setCurrentItemOrArmor(0, new ItemStack(LOTRMod.daggerOrcPoisoned));
		setCurrentItemOrArmor(1, new ItemStack(LOTRMod.bootsWarg));
		setCurrentItemOrArmor(2, new ItemStack(LOTRMod.legsWarg));
		setCurrentItemOrArmor(3, new ItemStack(LOTRMod.bodyWarg));
		setCurrentItemOrArmor(4, new ItemStack(LOTRMod.helmetWarg));
		return data;
	}
	
	@Override
	public int getAlignmentBonus()
	{
		return LOTRAlignmentValues.Bonuses.MORDOR_ORC_TRADER;
	}
	
	@Override
	public boolean canTradeWith(EntityPlayer entityplayer)
	{
		return LOTRLevelData.getData(entityplayer).getAlignment(getFaction()) >= LOTRAlignmentValues.Levels.MORDOR_ORC_TRADER_TRADE && isFriendly(entityplayer);
	}
	
	@Override
	public void onPlayerBuyItem(EntityPlayer entityplayer, ItemStack itemstack)
	{
		LOTRLevelData.getData(entityplayer).addAchievement(LOTRAchievement.tradeOrcTrader);
	}
	
	@Override
	public void onPlayerSellItem(EntityPlayer entityplayer, ItemStack itemstack)
	{
		LOTRLevelData.getData(entityplayer).addAchievement(LOTRAchievement.tradeOrcTrader);
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
				return "orcTrader_friendly";
			}
			else
			{
				return "orcTrader_neutral";
			}
		}
		else
		{
			return "orc_hostile";
		}
	}
}
