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

public class LOTREntityDunlendingWarlord extends LOTREntityDunlendingWarrior implements LOTRUnitTradeable
{
	public LOTREntityDunlendingWarlord(World world)
	{
		super(world);
		targetTasks.taskEntries.clear();
		targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
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
		setCurrentItemOrArmor(4, new ItemStack(LOTRMod.helmetWarg));
		return data;
	}
	
	@Override
	public int getAlignmentBonus()
	{
		return LOTRAlignmentValues.DUNLENDING_WARLORD_BONUS;
	}
	
	@Override
	public LOTRUnitTradeEntry[] getUnits()
	{
		return LOTRUnitTradeEntry.DUNLENDING_WARLORD;
	}
	
	@Override
	public boolean canTradeWith(EntityPlayer entityplayer)
	{
		return LOTRLevelData.getAlignment(entityplayer, getFaction()) >= LOTRAlignmentValues.DUNLENDING_WARLORD_TRADE && isFriendly(entityplayer);
	}
	
	@Override
	public void onUnitTrade(EntityPlayer entityplayer)
	{
		LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.tradeDunlendingWarlord);
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
				return "dunlendingWarlord_friendly";
			}
			else
			{
				return "dunlendingWarlord_neutral";
			}
		}
		else
		{
			return "dunlending_hostile";
		}
	}
}
