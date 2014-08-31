package lotr.common.quest;

import java.util.Random;

import lotr.common.*;
import lotr.common.quest.LOTRMiniQuest.QuestFactoryBase;
import lotr.common.quest.LOTRMiniQuestCollect.QuestFactory;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;

public class LOTRMiniQuestKillFaction extends LOTRMiniQuestKill
{
	public LOTRFaction killFaction;
	
	public LOTRMiniQuestKillFaction(LOTRPlayerData pd)
	{
		super(pd);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setString("KillFaction", killFaction.name());
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		killFaction = LOTRFaction.forName(nbt.getString("KillFaction"));
	}
	
	@Override
	public boolean isValidQuest()
	{
		return super.isValidQuest() && killFaction != null;
	}

	@Override
	protected String getKillTargetName()
	{
		return killFaction.factionEntityName();
	}
	
	@Override
	public void onKill(EntityPlayer entityplayer, EntityLivingBase entity)
	{
		if (LOTRMod.getNPCFaction(entity) == killFaction)
		{
			killCount++;
			updateQuest();
		}
	}
	
	public static class QuestFactory extends QuestFactoryBase<LOTRMiniQuestKillFaction>
	{
		private LOTRFaction killFaction;
		private int minTarget;
		private int maxTarget;
		
		public QuestFactory(String name)
		{
			super(name);
		}
		
		public QuestFactory setKillFaction(LOTRFaction faction, int min, int max)
		{
			killFaction = faction;
			minTarget = min;
			maxTarget = max;
			return this;
		}
		
		@Override
		public Class getQuestClass()
		{
			return LOTRMiniQuestKillFaction.class;
		}

		@Override
		public LOTRMiniQuest createQuest(EntityPlayer entityplayer, Random rand)
		{
			LOTRMiniQuestKillFaction quest = createQuestBase(entityplayer);
			quest.killFaction = killFaction;
			quest.killTarget = MathHelper.getRandomIntegerInRange(rand, minTarget, maxTarget);
			return quest;
		}
	}
}
