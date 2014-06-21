package lotr.common.entity.npc;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRAlignmentValues;
import lotr.common.LOTRLevelData;
import lotr.common.LOTRMod;
import lotr.common.item.LOTRItemLeatherHat;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
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
		targetTasks.taskEntries.clear();
		targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
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
		return LOTRAlignmentValues.HOBBIT_SHIRRIFF_CHIEF_BONUS;
	}
	
	@Override
	public LOTRUnitTradeEntry[] getUnits()
	{
		return LOTRUnitTradeEntry.HOBBIT_SHIRRIFF_CHIEF;
	}
	
	@Override
	public boolean canTradeWith(EntityPlayer entityplayer)
	{
		return LOTRLevelData.getAlignment(entityplayer, getFaction()) >= LOTRAlignmentValues.HOBBIT_SHIRRIFF_CHIEF_TRADE && isFriendly(entityplayer);
	}
	
	@Override
	public void onUnitTrade(EntityPlayer entityplayer)
	{
		LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.tradeHobbitShirriffChief);
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
