package lotr.common.quest;

import java.util.Random;

import lotr.common.*;
import lotr.common.entity.LOTREntities;
import lotr.common.quest.LOTRMiniQuest.QuestFactoryBase;
import lotr.common.quest.LOTRMiniQuestKillFaction.QuestFactory;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;

public class LOTRMiniQuestKillEntity extends LOTRMiniQuestKill
{
	public Class entityType;
	
	public LOTRMiniQuestKillEntity(LOTRPlayerData pd)
	{
		super(pd);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setString("KillClass", LOTREntities.getStringFromClass(entityType));
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		entityType = LOTREntities.getClassFromString(nbt.getString("KillClass"));
	}
	
	@Override
	public boolean isValidQuest()
	{
		return super.isValidQuest() && entityType != null && EntityLivingBase.class.isAssignableFrom(entityType);
	}

	@Override
	protected String getKillTargetName()
	{
		String s = String.format("%s.%s", LOTRMod.getModID(), LOTREntities.getStringFromClass(entityType));
		return StatCollector.translateToLocal("entity." + s + ".name");
	}
	
	@Override
	public void onKill(EntityPlayer entityplayer, EntityLivingBase entity)
	{
		if (entity.getClass().isAssignableFrom(entityType))
		{
			killCount++;
			updateQuest();
		}
	}
	
	public static class QuestFactory extends QuestFactoryBase<LOTRMiniQuestKillEntity>
	{
		private Class entityType;
		private int minTarget;
		private int maxTarget;
		
		public QuestFactory(String name)
		{
			super(name);
		}
		
		public QuestFactory setKillEntity(Class entityClass, int min, int max)
		{
			entityType = entityClass;
			minTarget = min;
			maxTarget = max;
			return this;
		}
		
		@Override
		public Class getQuestClass()
		{
			return LOTRMiniQuestKillEntity.class;
		}

		@Override
		public LOTRMiniQuest createQuest(EntityPlayer entityplayer, Random rand)
		{
			LOTRMiniQuestKillEntity quest = createQuestBase(entityplayer);
			quest.entityType = entityType;
			quest.killTarget = MathHelper.getRandomIntegerInRange(rand, minTarget, maxTarget);
			return quest;
		}
	}
}
