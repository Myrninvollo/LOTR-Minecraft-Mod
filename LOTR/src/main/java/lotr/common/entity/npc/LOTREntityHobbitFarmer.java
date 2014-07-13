package lotr.common.entity.npc;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRAlignmentValues;
import lotr.common.LOTRLevelData;
import lotr.common.LOTRMod;
import lotr.common.entity.ai.LOTREntityAIAttackOnCollide;
import lotr.common.item.LOTRItemLeatherHat;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTREntityHobbitFarmer extends LOTREntityHobbit implements LOTRTradeable, LOTRUnitTradeable
{
	public LOTREntityHobbitFarmer(World world)
	{
		super(world);
		for (int i = 0; i < tasks.taskEntries.size(); i++)
		{
			EntityAITaskEntry taskEntry = (EntityAITaskEntry)tasks.taskEntries.get(i);
			if (taskEntry.action instanceof EntityAIPanic)
			{
				tasks.removeTask(taskEntry.action);
			}
		}
		tasks.addTask(2, new LOTREntityAIAttackOnCollide(this, 1.2D, false));
		targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));

		if (!worldObj.isRemote)
		{
			traderNPCInfo.setBuyTrades(LOTRTradeEntry.getRandomTrades(LOTRTradeEntry.HOBBIT_FARMER_BUY, rand, true));
			traderNPCInfo.setSellTrades(LOTRTradeEntry.getRandomTrades(LOTRTradeEntry.HOBBIT_FARMER_SELL, rand, false));
		}
	}
	
	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData data)
	{
		data = super.onSpawnWithEgg(data);
		setCurrentItemOrArmor(0, new ItemStack(Items.iron_hoe));
		
		ItemStack hat = new ItemStack(LOTRMod.leatherHat);
		LOTRItemLeatherHat.setHatColor(hat, 0x9E8A73);
		setCurrentItemOrArmor(4, hat);
		
		return data;
	}
	
	@Override
	public int getAlignmentBonus()
	{
		return LOTRAlignmentValues.HOBBIT_FARMER_BONUS;
	}
	
	@Override
	public boolean canTradeWith(EntityPlayer entityplayer)
	{
		return isFriendly(entityplayer);
	}
	
	@Override
	public void onPlayerBuyItem(EntityPlayer entityplayer, ItemStack itemstack)
	{
		if (itemstack.getItem() == Items.potato)
		{
			LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.buyPotatoHobbitFarmer);
		}
	}
	
	@Override
	public void onPlayerSellItem(EntityPlayer entityplayer, ItemStack itemstack) {}
	
	@Override
	public LOTRUnitTradeEntry[] getUnits()
	{
		return LOTRUnitTradeEntry.HOBBIT_FARMER;
	}
	
	@Override
	public void onUnitTrade(EntityPlayer entityplayer) {}
	
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
			return "hobbitFarmer_friendly";
		}
		else
		{
			return "hobbit_unfriendly";
		}
	}
}
