package lotr.common.quest;

import java.util.*;

import lotr.common.*;
import lotr.common.LOTRAlignmentValues.AlignmentBonus;
import lotr.common.entity.npc.LOTREntityNPC;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.FMLLog;

public abstract class LOTRMiniQuest
{
	private static Map<String, Class<? extends LOTRMiniQuest>> nameToQuestMapping = new HashMap();
	private static Map<Class<? extends LOTRMiniQuest>, String> questToNameMapping = new HashMap();
	
	private static void registerQuestType(String name, Class<? extends LOTRMiniQuest> questType)
	{
		nameToQuestMapping.put(name, questType);
		questToNameMapping.put(questType, name);
	}
	
	static
	{
		registerQuestType("Collect", LOTRMiniQuestCollect.class);
		registerQuestType("KillFaction", LOTRMiniQuestKillFaction.class);
		registerQuestType("KillEntity", LOTRMiniQuestKillEntity.class);
	}
	
	public static int MAX_MINIQUESTS_PER_FACTION = 3;
	
	public LOTRMiniQuest(LOTRPlayerData pd)
	{
		playerData = pd;
	}
	
	private LOTRPlayerData playerData;
	public UUID entityUUID;
	public String entityName;
	public LOTRFaction entityFaction;
	public boolean entityDead;
	private boolean completed;
	public String speechBankStart;
	public String speechBankProgress;
	public String speechBankComplete;
	
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setString("QuestType", questToNameMapping.get(getClass()));
		nbt.setLong("UUIDMost", entityUUID.getMostSignificantBits());
		nbt.setLong("UUIDLeast", entityUUID.getLeastSignificantBits());
		nbt.setString("Owner", entityName);
		nbt.setString("Faction", entityFaction.name());
		nbt.setBoolean("OwnerDead", entityDead);
		nbt.setBoolean("Completed", completed);
		nbt.setString("SpeechStart", speechBankStart);
		nbt.setString("SpeechProgress", speechBankProgress);
		nbt.setString("SpeechComplete", speechBankComplete);
	}

	public void readFromNBT(NBTTagCompound nbt)
	{
		entityUUID = new UUID(nbt.getLong("UUIDMost"), nbt.getLong("UUIDLeast"));
		entityName = nbt.getString("Owner");
		entityFaction = LOTRFaction.forName(nbt.getString("Faction"));
		entityDead = nbt.getBoolean("OwnerDead");
		completed = nbt.getBoolean("Completed");
		speechBankStart = nbt.getString("SpeechStart");
		speechBankProgress = nbt.getString("SpeechProgress");
		speechBankComplete = nbt.getString("SpeechComplete");
	}
	
	public static LOTRMiniQuest loadQuestFromNBT(NBTTagCompound nbt, LOTRPlayerData playerData)
	{
		String questTypeName = nbt.getString("QuestType");
		Class questType = nameToQuestMapping.get(questTypeName);
		if (questType == null)
		{
			System.out.println("Could not instantiate miniquest of type " + questTypeName);
			return null;
		}
		else
		{
			LOTRMiniQuest quest = newQuestInstance(questType, playerData);
			quest.readFromNBT(nbt);
			if (quest.isValidQuest())
			{
				return quest;
			}
			else
			{
				FMLLog.severe("Loaded an invalid LOTR miniquest " + quest.speechBankStart);
				return null;
			}
		}
	}
	
	private static <Q extends LOTRMiniQuest> Q newQuestInstance(Class<Q> questType, LOTRPlayerData playerData)
	{
		try
		{
			Q quest = questType.getConstructor(new Class[] {LOTRPlayerData.class}).newInstance(new Object[] {playerData});
			quest.playerData = playerData;
			return quest;
		}
		catch (ReflectiveOperationException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean isValidQuest()
	{
		return entityUUID != null && entityFaction != null;
	}
	
	public abstract String getQuestObjective();
	
	public abstract String getObjectiveInSpeech();
	
	public abstract String getQuestProgress();
	
	public void onInteract(EntityPlayer entityplayer, LOTREntityNPC npc) {}
	
	public void onKill(EntityPlayer entityplayer, EntityLivingBase entity) {}
	
	public final boolean isActive()
	{
		return !isCompleted() && !entityDead;
	}
	
	public boolean isCompleted()
	{
		return completed;
	}
	
	public void complete(EntityPlayer entityplayer, LOTREntityNPC npc)
	{
		completed = true;
		playerData.updateMiniQuest(this);
		
		AlignmentBonus bonus = LOTRAlignmentValues.createMiniquestBonus(getAlignmentBonus());
		playerData.addAlignment(bonus, entityFaction, npc);
	}
	
	public abstract int getAlignmentBonus();
	
	public static abstract class QuestFactoryBase
	{
		private String questBaseName;
		private String questName;
		
		public QuestFactoryBase(String name)
		{
			questName = name;
		}
		
		public void setQuestBaseName(String name)
		{
			questBaseName = name;
		}
		
		public abstract LOTRMiniQuest createQuest(EntityPlayer entityplayer, Random rand);
		
		public <Q extends LOTRMiniQuest> Q createQuestBase(Class<Q> questType, EntityPlayer entityplayer)
		{
			Q quest = newQuestInstance(questType, LOTRLevelData.getData(entityplayer));
			String name = "miniquest/" + getQuestBaseName() + "/" + questName + "_";
			quest.speechBankStart = name + "start";
			quest.speechBankProgress = name + "progress";
			quest.speechBankComplete = name + "complete";
			return quest;
		}

		public String getQuestBaseName()
		{
			return questBaseName;
		}
	}
}
