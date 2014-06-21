package lotr.common.entity.npc;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRAlignmentValues;
import lotr.common.LOTRLevelData;
import lotr.common.LOTRMod;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTREntityBlueDwarfMiner extends LOTREntityBlueDwarf implements LOTRTradeable
{
	public LOTREntityBlueDwarfMiner(World world)
	{
		super(world);
		isNPCPersistent = false;
		
		if (!worldObj.isRemote)
		{
			traderNPCInfo.setBuyTrades(LOTRTradeEntry.getRandomTrades(LOTRTradeEntry.BLUE_DWARF_MINER_BUY, rand, true));
			traderNPCInfo.setSellTrades(LOTRTradeEntry.getRandomTrades(LOTRTradeEntry.BLUE_DWARF_MINER_SELL, rand, false));
		}
	}
	
	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData data)
	{
		data = super.onSpawnWithEgg(data);
		setCurrentItemOrArmor(0, new ItemStack(LOTRMod.pickaxeBlueDwarven));
		return data;
	}
	
	@Override
	public void onDwarfUpdate() {}
	
	@Override
	public int getAlignmentBonus()
	{
		return LOTRAlignmentValues.BLUE_DWARF_MINER_BONUS;
	}
	
	@Override
	public boolean canTradeWith(EntityPlayer entityplayer)
	{
		return LOTRLevelData.getAlignment(entityplayer, getFaction()) >= LOTRAlignmentValues.BLUE_DWARF_MINER_TRADE && isFriendly(entityplayer);
	}
	
	@Override
	public void onPlayerBuyItem(EntityPlayer entityplayer, ItemStack itemstack)
	{
		LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.tradeBlueDwarfMiner);
	}
	
	@Override
	public void onPlayerSellItem(EntityPlayer entityplayer, ItemStack itemstack)
	{
		LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.tradeBlueDwarfMiner);
	}
	
	@Override
	public boolean shouldTraderRespawn()
	{
		return false;
	}
	
	@Override
	protected void dropFewItems(boolean flag, int i)
	{
		super.dropFewItems(flag, i);
		
		if (flag)
		{
			int rareDropChance = 15 - i * 3;
			if (rareDropChance < 1)
			{
				rareDropChance = 1;
			}
			
			if (rand.nextInt(rareDropChance) == 0)
			{
				int j = rand.nextInt(7);
				switch (j)
				{
					case 0:
						entityDropItem(new ItemStack(Blocks.iron_ore, 1 + rand.nextInt(3)), 0F);
						break;
					case 1:
						entityDropItem(new ItemStack(Blocks.gold_ore, 1 + rand.nextInt(2)), 0F);
						break;
					case 2:
						entityDropItem(new ItemStack(LOTRMod.oreSilver, 1 + rand.nextInt(2)), 0F);
						break;
					case 3:
						entityDropItem(new ItemStack(Items.dye, 3 + rand.nextInt(7), 4), 0F);
						break;
					case 4:
						entityDropItem(new ItemStack(Items.gold_ingot), 0F);
						break;
					case 5:
						entityDropItem(new ItemStack(LOTRMod.silver), 0F);
						break;
					case 6:
						entityDropItem(new ItemStack(Items.glowstone_dust, 1 + rand.nextInt(8)), 0F);
						break;
				}
			}
			
			if (rand.nextInt(100) == 0)
			{
				entityDropItem(new ItemStack(LOTRMod.mithrilNugget), 0F);
			}
		}
	}
	
	@Override
	public String getSpeechBank(EntityPlayer entityplayer)
	{
		if (isFriendly(entityplayer))
		{
			if (canTradeWith(entityplayer))
			{
				return "blueDwarfMiner_friendly";
			}
			else
			{
				return "blueDwarfMiner_neutral";
			}
		}
		else
		{
			return "blueDwarf_hostile";
		}
	}
}
