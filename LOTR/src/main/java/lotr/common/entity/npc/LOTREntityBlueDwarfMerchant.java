package lotr.common.entity.npc;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRAlignmentValues;
import lotr.common.LOTRLevelData;
import lotr.common.entity.ai.LOTREntityAIAttackOnCollide;
import lotr.common.world.biome.LOTRBiome;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTREntityBlueDwarfMerchant extends LOTREntityBlueDwarf implements LOTRTradeable, LOTRTravellingTrader
{
	public LOTREntityBlueDwarfMerchant(World world)
	{
		super(world);
		targetTasks.taskEntries.clear();
		targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
		
		if (!worldObj.isRemote)
		{
			traderNPCInfo.setBuyTrades(LOTRTradeEntry.getRandomTrades(LOTRTradeEntry.BLUE_DWARF_MERCHANT_BUY, rand, true));
			traderNPCInfo.setSellTrades(LOTRTradeEntry.getRandomTrades(LOTRTradeEntry.BLUE_DWARF_MERCHANT_SELL, rand, false));
		}
	}

	@Override
	public LOTREntityNPC createTravellingEscort()
	{
		return new LOTREntityBlueDwarfWarrior(worldObj);
	}
	
	@Override
	public String getDepartureSpeech()
	{
		return "blueDwarfMerchant_departure";
	}

	@Override
	public int getAlignmentBonus()
	{
		return LOTRAlignmentValues.BLUE_DWARF_MERCHANT_BONUS;
	}
	
	@Override
	public boolean canTradeWith(EntityPlayer entityplayer)
	{
		return LOTRLevelData.getData(entityplayer).getAlignment(getFaction()) >= LOTRAlignmentValues.BLUE_DWARF_MERCHANT_TRADE && isFriendly(entityplayer);
	}
	
	@Override
	public void onPlayerBuyItem(EntityPlayer entityplayer, ItemStack itemstack)
	{
		LOTRLevelData.getData(entityplayer).addAchievement(LOTRAchievement.tradeBlueDwarfMerchant);
	}
	
	@Override
	public void onPlayerSellItem(EntityPlayer entityplayer, ItemStack itemstack)
	{
		LOTRLevelData.getData(entityplayer).addAchievement(LOTRAchievement.tradeBlueDwarfMerchant);
	}
	
	@Override
	public boolean shouldTraderRespawn()
	{
		return false;
	}
	
	@Override
	public String getSpeechBank(EntityPlayer entityplayer)
	{
		if (isFriendly(entityplayer))
		{
			return "blueDwarfMerchant_friendly";
		}
		else
		{
			return "blueDwarf_hostile";
		}
	}
}
