package lotr.common.entity.npc;

import lotr.common.*;
import lotr.common.item.LOTRItemLeatherHat;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTREntityHobbitShirriffChief extends LOTREntityHobbitShirriff implements LOTRUnitTradeable
{
	public LOTREntityHobbitShirriffChief(World world)
	{
		super(world);
		addTargetTasks(false);
	}
	
	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData data)
	{
		data = super.onSpawnWithEgg(data);
		ItemStack hat = new ItemStack(LOTRMod.leatherHat);
		LOTRItemLeatherHat.setHatColor(hat, LOTRItemLeatherHat.HAT_SHIRRIFF_CHIEF);
		LOTRItemLeatherHat.setFeatherColor(hat, LOTRItemLeatherHat.FEATHER_SHIRRIFF_CHIEF);
		setCurrentItemOrArmor(4, hat);
		return data;
	}
	
	@Override
	public Item getHobbitMeleeWeaponId()
	{
		return Items.iron_sword;
	}
	
	@Override
	public int getAlignmentBonus()
	{
		return LOTRAlignmentValues.Bonuses.HOBBIT_SHIRRIFF_CHIEF;
	}
	
	@Override
	public LOTRUnitTradeEntry[] getUnits()
	{
		return LOTRUnitTradeEntry.HOBBIT_SHIRRIFF_CHIEF;
	}
	
	@Override
	public boolean canTradeWith(EntityPlayer entityplayer)
	{
		return LOTRLevelData.getData(entityplayer).getAlignment(getFaction()) >= LOTRAlignmentValues.Levels.HOBBIT_SHIRRIFF_CHIEF_TRADE && isFriendly(entityplayer);
	}
	
	@Override
	public void onUnitTrade(EntityPlayer entityplayer)
	{
		LOTRLevelData.getData(entityplayer).addAchievement(LOTRAchievement.tradeHobbitShirriffChief);
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
				return "hobbitShirriffChief_friendly";
			}
			else
			{
				return "hobbitShirriffChief_neutral";
			}
		}
		else
		{
			return "hobbitShirriff_hostile";
		}
	}
}
