package lotr.common.entity.npc;

import lotr.common.*;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTREntityHighElfLord extends LOTREntityHighElfWarrior implements LOTRUnitTradeable
{
	public LOTREntityHighElfLord(World world)
	{
		super(world);
		targetTasks.taskEntries.clear();
		targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
		npcCape = LOTRCapes.HIGH_ELF;
	}
	
	@Override
    public IEntityLivingData onSpawnWithEgg(IEntityLivingData data)
    {
		data = super.onSpawnWithEgg(data);
		setCurrentItemOrArmor(0, new ItemStack(getElfSwordId(), 1, 0));
		setCurrentItemOrArmor(1, new ItemStack(LOTRMod.bootsHighElven));
		setCurrentItemOrArmor(2, new ItemStack(LOTRMod.legsHighElven));
		setCurrentItemOrArmor(3, new ItemStack(LOTRMod.bodyHighElven));
		setCurrentItemOrArmor(4, null);
		return data;
    }
	
	@Override
	public int getAlignmentBonus()
	{
		return LOTRAlignmentValues.Bonuses.HIGH_ELF_LORD;
	}
	
	@Override
	public LOTRUnitTradeEntry[] getUnits()
	{
		return LOTRUnitTradeEntry.HIGH_ELF_LORD;
	}
	
	@Override
	public boolean canTradeWith(EntityPlayer entityplayer)
	{
		return LOTRLevelData.getData(entityplayer).getAlignment(getFaction()) >= LOTRAlignmentValues.Levels.HIGH_ELF_LORD_TRADE && isFriendly(entityplayer);
	}
	
	@Override
	public void onUnitTrade(EntityPlayer entityplayer)
	{
		LOTRLevelData.getData(entityplayer).addAchievement(LOTRAchievement.tradeHighElfLord);
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
				return "highElfLord_friendly";
			}
			else
			{
				return "highElfLord_neutral";
			}
		}
		else
		{
			return "highElfWarrior_hostile";
		}
	}
}
