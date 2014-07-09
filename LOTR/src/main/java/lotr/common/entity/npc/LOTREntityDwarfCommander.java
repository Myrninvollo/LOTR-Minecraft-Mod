package lotr.common.entity.npc;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRAlignmentValues;
import lotr.common.LOTRLevelData;
import lotr.common.LOTRMod;
import lotr.common.entity.ai.LOTREntityAIAttackOnCollide;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTREntityDwarfCommander extends LOTREntityDwarf implements LOTRUnitTradeable
{
	public LOTREntityDwarfCommander(World world)
	{
		super(world);
		targetTasks.taskEntries.clear();
		targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
	}
	
	@Override
	public EntityAIBase getDwarfAttackAI()
	{
		return new LOTREntityAIAttackOnCollide(this, 1.6D, false);
	}
	
	@Override
    public IEntityLivingData onSpawnWithEgg(IEntityLivingData data)
    {
		data = super.onSpawnWithEgg(data);
		setCurrentItemOrArmor(0, new ItemStack(LOTRMod.swordDwarven));
		setCurrentItemOrArmor(1, new ItemStack(LOTRMod.bootsDwarven));
		setCurrentItemOrArmor(2, new ItemStack(LOTRMod.legsDwarven));
		setCurrentItemOrArmor(3, new ItemStack(LOTRMod.bodyDwarven));
		setCurrentItemOrArmor(4, null);
		return data;
    }
	
	@Override
	public int getAlignmentBonus()
	{
		return LOTRAlignmentValues.DWARF_COMMANDER_BONUS;
	}
	
	@Override
	public LOTRUnitTradeEntry[] getUnits()
	{
		return LOTRUnitTradeEntry.DWARF_COMMANDER;
	}
	
	@Override
	public boolean canTradeWith(EntityPlayer entityplayer)
	{
		return LOTRLevelData.getAlignment(entityplayer, getFaction()) >= LOTRAlignmentValues.DWARF_COMMANDER_TRADE && isFriendly(entityplayer);
	}
	
	@Override
	public void onUnitTrade(EntityPlayer entityplayer)
	{
		LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.tradeDwarfCommander);
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
				return "dwarfCommander_friendly";
			}
			else
			{
				return "dwarfCommander_neutral";
			}
		}
		else
		{
			return "dwarf_hostile";
		}
	}
}
