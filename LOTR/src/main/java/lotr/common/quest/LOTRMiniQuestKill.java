package lotr.common.quest;

import lotr.common.LOTRPlayerData;
import lotr.common.entity.npc.LOTREntityNPC;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;

public abstract class LOTRMiniQuestKill extends LOTRMiniQuest
{
	public int killTarget;
	public int killCount;
	
	public LOTRMiniQuestKill(LOTRPlayerData pd)
	{
		super(pd);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setInteger("Target", killTarget);
		nbt.setInteger("Count", killCount);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		killTarget = nbt.getInteger("Target");
		killCount = nbt.getInteger("Count");
	}
	
	@Override
	public boolean isValidQuest()
	{
		return super.isValidQuest() && killTarget > 0;
	}
	
	@Override
	public String getQuestObjective()
	{
		return StatCollector.translateToLocalFormatted("lotr.miniquest.kill", new Object[] {killTarget, getKillTargetName()});
	}
	
	protected abstract String getKillTargetName();
	
	@Override
	public String getObjectiveInSpeech()
	{
		return (killTarget - killCount) + " " + getKillTargetName();
	}
	
	@Override
	public String getQuestProgress()
	{
		return StatCollector.translateToLocalFormatted("lotr.miniquest.kill.progress", new Object[] {killCount, killTarget});
	}
	
	@Override
	public ItemStack getQuestIcon()
	{
		return new ItemStack(Items.iron_sword);
	}
	
	@Override
	public void onInteract(EntityPlayer entityplayer, LOTREntityNPC npc)
	{
		if (killCount >= killTarget)
		{
			complete(entityplayer, npc);
		}
	}
	
	@Override
	public int getAlignmentBonus()
	{
		return killTarget;
	}
}
