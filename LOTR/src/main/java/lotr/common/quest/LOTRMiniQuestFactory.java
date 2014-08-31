package lotr.common.quest;

import java.util.*;
import java.util.Map.Entry;

import lotr.common.LOTRFaction;
import lotr.common.LOTRMod;
import lotr.common.entity.npc.*;
import lotr.common.quest.LOTRMiniQuest.QuestFactoryBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandom;

public enum LOTRMiniQuestFactory
{
	HOBBIT("hobbit"),
	GALADHRIM("galadhrim");
	
	private static Random rand = new Random();
	private static Map<Class<? extends LOTRMiniQuest>, Integer> questClassWeights = new HashMap();
	
	private Map<Class<? extends LOTRMiniQuest>, List<QuestFactoryBase>> questFactories = new HashMap();
	private String baseName;
	
	private LOTRMiniQuestFactory(String s)
	{
		baseName = s;
	}

	public void addQuest(QuestFactoryBase factory)
	{
		Class questClass = factory.getQuestClass();
		Class<? extends LOTRMiniQuest> registryClass = null;
		for (Class c : questClassWeights.keySet())
		{
			if (questClass.equals(c))
			{
				registryClass = c;
				break;
			}
		}
		if (registryClass == null)
		{
			for (Class c : questClassWeights.keySet())
			{
				if (c.isAssignableFrom(questClass))
				{
					registryClass = c;
					break;
				}
			}
		}
		
		if (registryClass == null)
		{
			throw new IllegalArgumentException("Could not find registered quest class for " + questClass.toString());
		}
		
		factory.setQuestBaseName(baseName);
		
		List list = questFactories.get(registryClass);
		if (list == null)
		{
			list = new ArrayList();
			questFactories.put(registryClass, list);
		}
		
		list.add(factory);
	}

	public LOTRMiniQuest createQuest(EntityPlayer entityplayer)
	{
        int totalWeight = getTotalQuestClassWeight(this);
        int randomWeight = rand.nextInt(totalWeight);
        
        int i = randomWeight;
        Iterator<Entry<Class<? extends LOTRMiniQuest>, List<QuestFactoryBase>>> iterator = questFactories.entrySet().iterator();
        List<QuestFactoryBase> chosenFactoryList = null;
        do
        {
            if (!iterator.hasNext())
            {
                break;
            }
            Entry<Class<? extends LOTRMiniQuest>, List<QuestFactoryBase>> next = iterator.next();
            chosenFactoryList = next.getValue();
            i -= getQuestClassWeight(next.getKey());
        }
        while (i >= 0);
        
		QuestFactoryBase factory = chosenFactoryList.get(rand.nextInt(chosenFactoryList.size()));
		LOTRMiniQuest quest = factory.createQuest(entityplayer, rand);
		return quest;
	}
	
	private static void registerQuestClass(Class <? extends LOTRMiniQuest> questClass, int weight)
	{
		questClassWeights.put(questClass, weight);
	}
	
	private static int getQuestClassWeight(Class<? extends LOTRMiniQuest> questClass)
	{
		Integer i = questClassWeights.get(questClass);
		if (i == null)
		{
			throw new RuntimeException("Encountered a registered quest class " + questClass.toString() + " which is not assigned a weight");
		}
		return i;
	}
	
	private static int getTotalQuestClassWeight(LOTRMiniQuestFactory factory)
	{
		Set<Class<? extends LOTRMiniQuest>> registeredQuestTypes = new HashSet();
		for (Entry<Class<? extends LOTRMiniQuest>, List<QuestFactoryBase>> entry : factory.questFactories.entrySet())
		{
			Class questType = entry.getKey();
			registeredQuestTypes.add(questType);
		}
		
		int totalWeight = 0;
		for (Class<? extends LOTRMiniQuest> c : registeredQuestTypes)
		{
			totalWeight += getQuestClassWeight(c);
		}

		return totalWeight;
	}
	
	public static void createMiniQuests()
	{
		registerQuestClass(LOTRMiniQuestCollect.class, 10);
		registerQuestClass(LOTRMiniQuestKill.class, 10);
		
		HOBBIT.addQuest(new LOTRMiniQuestCollect.QuestFactory("pipeweed").setCollectItem(new ItemStack(LOTRMod.pipeweed), 10, 40).setRewardFactor(0.25F));
		
		GALADHRIM.addQuest(new LOTRMiniQuestCollect.QuestFactory("collect").setCollectItem(new ItemStack(LOTRMod.sapling, 1, 1), 5, 20).setRewardFactor(0.5F));
		GALADHRIM.addQuest(new LOTRMiniQuestCollect.QuestFactory("collect").setCollectItem(new ItemStack(LOTRMod.elanor), 5, 30).setRewardFactor(0.25F));
		GALADHRIM.addQuest(new LOTRMiniQuestCollect.QuestFactory("collect").setCollectItem(new ItemStack(LOTRMod.niphredil), 5, 30).setRewardFactor(0.25F));
		GALADHRIM.addQuest(new LOTRMiniQuestCollect.QuestFactory("collect").setCollectItem(new ItemStack(LOTRMod.mallornNut), 5, 10).setRewardFactor(2F));
		
		GALADHRIM.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectFangorn").setCollectItem(new ItemStack(LOTRMod.fangornPlant, 1, 0), 4, 10).setRewardFactor(2F));
		GALADHRIM.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectFangorn").setCollectItem(new ItemStack(LOTRMod.fangornPlant, 1, 2), 4, 10).setRewardFactor(2F));
		GALADHRIM.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectFangorn").setCollectItem(new ItemStack(LOTRMod.fangornPlant, 1, 5), 4, 10).setRewardFactor(2F));
		
		GALADHRIM.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectCrystal").setCollectItem(new ItemStack(LOTRMod.quenditeCrystal), 4, 16).setRewardFactor(1F));
		
		GALADHRIM.addQuest(new LOTRMiniQuestCollect.QuestFactory("forge").setCollectItem(new ItemStack(LOTRMod.swordElven), 1, 4).setRewardFactor(3F));
		GALADHRIM.addQuest(new LOTRMiniQuestCollect.QuestFactory("forge").setCollectItem(new ItemStack(LOTRMod.spearElven), 1, 4).setRewardFactor(2F));
		GALADHRIM.addQuest(new LOTRMiniQuestCollect.QuestFactory("forge").setCollectItem(new ItemStack(LOTRMod.helmetElven), 1, 4).setRewardFactor(3F));
		GALADHRIM.addQuest(new LOTRMiniQuestCollect.QuestFactory("forge").setCollectItem(new ItemStack(LOTRMod.bodyElven), 1, 4).setRewardFactor(4F));
		
		GALADHRIM.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectMineral").setCollectItem(new ItemStack(Items.gold_ingot), 3, 10).setRewardFactor(2F));
		GALADHRIM.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectMineral").setCollectItem(new ItemStack(LOTRMod.silver), 3, 10).setRewardFactor(2F));
		GALADHRIM.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectMineral").setCollectItem(new ItemStack(Items.diamond), 3, 5).setRewardFactor(4F));
		
		GALADHRIM.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectDwarven").setCollectItem(new ItemStack(Blocks.glowstone), 2, 6).setRewardFactor(4F));
		
		GALADHRIM.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectPlants").setCollectItem(new ItemStack(Blocks.sapling, 1, 1), 2, 5).setRewardFactor(3F));
		GALADHRIM.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectPlants").setCollectItem(new ItemStack(Blocks.sapling, 1, 2), 2, 5).setRewardFactor(3F));
		GALADHRIM.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectPlants").setCollectItem(new ItemStack(Blocks.red_flower, 1, 0), 2, 8).setRewardFactor(1F));
		GALADHRIM.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectPlants").setCollectItem(new ItemStack(Blocks.yellow_flower, 1, 0), 2, 8).setRewardFactor(1F));
		
		GALADHRIM.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectString").setCollectItem(new ItemStack(Items.string), 5, 20).setRewardFactor(1F));
		
		GALADHRIM.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectOrcItem").setCollectItem(new ItemStack(LOTRMod.helmetOrc), 1, 1).setRewardFactor(10F));
		GALADHRIM.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectOrcItem").setCollectItem(new ItemStack(LOTRMod.helmetAngmar), 1, 1).setRewardFactor(10F));
		GALADHRIM.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectOrcItem").setCollectItem(new ItemStack(LOTRMod.helmetDolGuldur), 1, 1).setRewardFactor(10F));
		
		GALADHRIM.addQuest(new LOTRMiniQuestKillFaction.QuestFactory("killDolGuldur").setKillFaction(LOTRFaction.DOL_GULDUR, 10, 30));
		
		GALADHRIM.addQuest(new LOTRMiniQuestKillEntity.QuestFactory("killOrc").setKillEntity(LOTREntityGundabadOrc.class, 10, 30));
		GALADHRIM.addQuest(new LOTRMiniQuestKillEntity.QuestFactory("killOrc").setKillEntity(LOTREntityAngmarOrc.class, 10, 30));
		GALADHRIM.addQuest(new LOTRMiniQuestKillEntity.QuestFactory("killOrc").setKillEntity(LOTREntityDolGuldurOrc.class, 10, 30));
		
		GALADHRIM.addQuest(new LOTRMiniQuestKillFaction.QuestFactory("killGundabad").setKillFaction(LOTRFaction.GUNDABAD, 10, 30));
		
		GALADHRIM.addQuest(new LOTRMiniQuestKillEntity.QuestFactory("killWarg").setKillEntity(LOTREntityGundabadWarg.class, 10, 30));
		GALADHRIM.addQuest(new LOTRMiniQuestKillEntity.QuestFactory("killWarg").setKillEntity(LOTREntityAngmarWarg.class, 10, 30));
	}
}
