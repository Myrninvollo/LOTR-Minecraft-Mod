package lotr.common.entity.npc;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRAlignmentValues;
import lotr.common.LOTRFoods;
import lotr.common.LOTRLevelData;
import lotr.common.LOTRMod;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTREntityHobbitBartender extends LOTREntityHobbit implements LOTRTradeable
{
	public LOTREntityHobbitBartender(World world)
	{
		super(world);

		npcLocationName = "entity.lotr.HobbitBartender.locationName";
		
		if (!worldObj.isRemote)
		{
			traderNPCInfo.setBuyTrades(LOTRTradeEntry.getRandomTrades(LOTRTradeEntry.HOBBIT_BARTENDER_BUY, rand, true));
			traderNPCInfo.setSellTrades(LOTRTradeEntry.getRandomTrades(LOTRTradeEntry.HOBBIT_BARTENDER_SELL, rand, false));
		}
	}
	
	@Override
	public void dropHobbitItems(boolean flag, int i)
	{
		int count = rand.nextInt(3) + rand.nextInt(i + 1);
		for (int k = 0; k < count; k++)
		{
			int j = rand.nextInt(10);
			switch(j)
			{
				case 0: case 1:
					entityDropItem(LOTRFoods.HOBBIT.getRandomFood(rand), 0F);
					break;
				case 2:
					entityDropItem(new ItemStack(Items.gold_nugget, 2 + rand.nextInt(3)), 0F);
					break;
				case 3:
					entityDropItem(new ItemStack(Items.bowl, 1 + rand.nextInt(4)), 0F);
					break;
				case 4:
					entityDropItem(new ItemStack(LOTRMod.hobbitPipe, 1, rand.nextInt(100)), 0F);
					break;
				case 5:
					entityDropItem(new ItemStack(LOTRMod.pipeweed, 1 + rand.nextInt(2)), 0F);
					break;
				case 6: case 7: case 8:
					entityDropItem(new ItemStack(LOTRMod.mug), 0F);
					break;
				case 9:
					Item drink = LOTRFoods.HOBBIT_DRINK.getRandomFood(rand).getItem();
					entityDropItem(new ItemStack(drink, 1, 1 + rand.nextInt(3)), 0F);
					break;
			}
		}
	}
	
	@Override
	public int getAlignmentBonus()
	{
		return LOTRAlignmentValues.HOBBIT_BARTENDER_BONUS;
	}
	
	@Override
	public boolean canTradeWith(EntityPlayer entityplayer)
	{
		return isFriendly(entityplayer);
	}
	
	@Override
	public void onPlayerBuyItem(EntityPlayer entityplayer, ItemStack itemstack)
	{
		LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.tradeBartender);
	}
	
	@Override
	public void onPlayerSellItem(EntityPlayer entityplayer, ItemStack itemstack)
	{
		LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.tradeBartender);
		if (itemstack.getItem() == LOTRMod.pipeweedLeaf)
		{
			LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.sellPipeweedLeaf);
		}
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
			return "hobbitBartender_friendly";
		}
		else
		{
			return "hobbitBartender_unfriendly";
		}
	}
}
