package lotr.common.entity.npc;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRAlignmentValues;
import lotr.common.LOTRLevelData;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTREntityRangerNorthCaptain extends LOTREntityRangerNorth implements LOTRUnitTradeable
{
	public LOTREntityRangerNorthCaptain(World world)
	{
		super(world);
		addTargetTasks(false);
	}
	
	@Override
	public Item getRangerSwordId()
	{
		return Items.iron_sword;
	}
	
	@Override
    public IEntityLivingData onSpawnWithEgg(IEntityLivingData data)
    {
		data = super.onSpawnWithEgg(data);
		setCurrentItemOrArmor(4, null);
		return data;
	}
	
	@Override
	public int getAlignmentBonus()
	{
		return LOTRAlignmentValues.Bonuses.RANGER_NORTH_CAPTAIN;
	}
	
	@Override
	public LOTRUnitTradeEntry[] getUnits()
	{
		return LOTRUnitTradeEntry.RANGER_NORTH_CAPTAIN;
	}
	
	@Override
	public boolean canTradeWith(EntityPlayer entityplayer)
	{
		return LOTRLevelData.getData(entityplayer).getAlignment(getFaction()) >= LOTRAlignmentValues.Levels.RANGER_NORTH_CAPTAIN_TRADE && isFriendly(entityplayer);
	}
	
	@Override
	public void onUnitTrade(EntityPlayer entityplayer)
	{
		LOTRLevelData.getData(entityplayer).addAchievement(LOTRAchievement.tradeRangerNorthCaptain);
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
				return "rangerNorthCaptain_friendly";
			}
			else
			{
				return "rangerNorthCaptain_neutral";
			}
		}
		else
		{
			return "rangerNorth_hostile";
		}
	}
}
