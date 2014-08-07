package lotr.common.entity.npc;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRAlignmentValues;
import lotr.common.LOTRLevelData;
import lotr.common.LOTRMod;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTREntityMordorOrcMercenaryCaptain extends LOTREntityMordorOrc implements LOTRUnitTradeable
{
	public LOTREntityMordorOrcMercenaryCaptain(World world)
	{
		super(world);
		setSize(0.6F, 1.8F);
		targetTasks.taskEntries.clear();
		targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
		isWeakOrc = false;
		hasSkullStaff = true;
	}
	
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(25D);
        getEntityAttribute(npcAttackDamage).setBaseValue(5D);
    }
	
	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData data)
	{
		data = super.onSpawnWithEgg(data);
		setCurrentItemOrArmor(0, null);
		setCurrentItemOrArmor(1, new ItemStack(LOTRMod.bootsOrc));
		setCurrentItemOrArmor(2, new ItemStack(LOTRMod.legsOrc));
		setCurrentItemOrArmor(3, new ItemStack(LOTRMod.bodyOrc));
		setCurrentItemOrArmor(4, null);
		return data;
	}
	
	@Override
	public int getAlignmentBonus()
	{
		return LOTRAlignmentValues.MORDOR_ORC_MERCENARY_CAPTAIN_BONUS;
	}
	
	@Override
	public LOTRUnitTradeEntry[] getUnits()
	{
		return LOTRUnitTradeEntry.MORDOR_ORC_MERCENARY_CAPTAIN;
	}
	
	@Override
	public boolean canTradeWith(EntityPlayer entityplayer)
	{
		return LOTRLevelData.getData(entityplayer).getAlignment(getFaction()) >= LOTRAlignmentValues.MORDOR_ORC_MERCENARY_CAPTAIN_TRADE && isFriendly(entityplayer);
	}
	
	@Override
	public void onUnitTrade(EntityPlayer entityplayer)
	{
		LOTRLevelData.getData(entityplayer).addAchievement(LOTRAchievement.tradeOrcCaptain);
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
				return "orcMercenaryCaptain_friendly";
			}
			else
			{
				return "orcMercenaryCaptain_neutral";
			}
		}
		else
		{
			return "orc_hostile";
		}
	}
}
