package lotr.common.entity.npc;

import lotr.common.*;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTREntityUrukHaiMercenaryCaptain extends LOTREntityUrukHai implements LOTRUnitTradeable
{
	public LOTREntityUrukHaiMercenaryCaptain(World world)
	{
		super(world);
		addTargetTasks(false);
	}
	
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(25D);
    }
	
	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData data)
	{
		data = super.onSpawnWithEgg(data);
		setCurrentItemOrArmor(0, new ItemStack(LOTRMod.orcSkullStaff));
		setCurrentItemOrArmor(1, new ItemStack(LOTRMod.bootsUruk));
		setCurrentItemOrArmor(2, new ItemStack(LOTRMod.legsUruk));
		setCurrentItemOrArmor(3, new ItemStack(LOTRMod.bodyUruk));
		setCurrentItemOrArmor(4, null);
		return data;
	}

	@Override
	public int getAlignmentBonus()
	{
		return LOTRAlignmentValues.Bonuses.URUK_HAI_MERCENARY_CAPTAIN;
	}
	
	@Override
	public LOTRUnitTradeEntry[] getUnits()
	{
		return LOTRUnitTradeEntry.URUK_HAI_MERCENARY_CAPTAIN;
	}
	
	@Override
	public boolean canTradeWith(EntityPlayer entityplayer)
	{
		return LOTRLevelData.getData(entityplayer).getAlignment(getFaction()) >= LOTRAlignmentValues.Levels.URUK_HAI_MERCENARY_CAPTAIN_TRADE && isFriendly(entityplayer);
	}
	
	@Override
	public void onUnitTrade(EntityPlayer entityplayer)
	{
		LOTRLevelData.getData(entityplayer).addAchievement(LOTRAchievement.tradeUrukCaptain);
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
				return "urukHaiMercenaryCaptain_friendly";
			}
			else
			{
				return "urukHaiMercenaryCaptain_neutral";
			}
		}
		else
		{
			return "orc_hostile";
		}
	}
}
