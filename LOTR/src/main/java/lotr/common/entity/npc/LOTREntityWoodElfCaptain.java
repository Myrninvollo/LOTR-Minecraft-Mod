package lotr.common.entity.npc;

import lotr.common.*;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTREntityWoodElfCaptain extends LOTREntityWoodElfWarrior implements LOTRUnitTradeable
{
	public LOTREntityWoodElfCaptain(World world)
	{
		super(world);
		targetTasks.taskEntries.clear();
		targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
		npcCape = LOTRShields.ALIGNMENT_WOOD_ELF.capeTexture;
	}
	
	@Override
    public IEntityLivingData onSpawnWithEgg(IEntityLivingData data)
    {
		data = super.onSpawnWithEgg(data);
		setCurrentItemOrArmor(0, new ItemStack(getElfSwordId(), 1, 0));
		setCurrentItemOrArmor(1, new ItemStack(LOTRMod.bootsWoodElven));
		setCurrentItemOrArmor(2, new ItemStack(LOTRMod.legsWoodElven));
		setCurrentItemOrArmor(3, new ItemStack(LOTRMod.bodyWoodElven));
		setCurrentItemOrArmor(4, null);
		return data;
    }
	
	@Override
	public int getAlignmentBonus()
	{
		return LOTRAlignmentValues.Bonuses.WOOD_ELF_CAPTAIN;
	}
	
	@Override
	public LOTRUnitTradeEntry[] getUnits()
	{
		return LOTRUnitTradeEntry.WOOD_ELF_CAPTAIN;
	}
	
	@Override
	public boolean canTradeWith(EntityPlayer entityplayer)
	{
		return LOTRLevelData.getData(entityplayer).getAlignment(getFaction()) >= LOTRAlignmentValues.Levels.WOOD_ELF_CAPTAIN_TRADE && isFriendly(entityplayer);
	}
	
	@Override
	public void onUnitTrade(EntityPlayer entityplayer)
	{
		LOTRLevelData.getData(entityplayer).addAchievement(LOTRAchievement.tradeWoodElfCaptain);
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
				return "woodElfCaptain_friendly";
			}
			else
			{
				return "woodElfCaptain_neutral";
			}
		}
		else
		{
			return "woodElfWarrior_hostile";
		}
	}
}
