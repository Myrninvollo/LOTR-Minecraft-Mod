package lotr.common.quest;

import java.util.*;
import java.util.Map.Entry;

import lotr.common.LOTRFaction;
import lotr.common.LOTRMod;
import lotr.common.entity.animal.LOTREntityHorse;
import lotr.common.entity.npc.*;
import lotr.common.quest.LOTRMiniQuest.QuestFactoryBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public enum LOTRMiniQuestFactory
{
	HOBBIT("hobbit"),
	RANGER_NORTH("rangerNorth"),
	BLUE_MOUNTAINS("blueMountains"),
	HIGH_ELF("highElf"),
	GUNDABAD("gundabad"),
	ANGMAR("angmar"),
	WOOD_ELF("woodElf"),
	DOL_GULDUR("dolGuldur"),
	DURIN("durin"),
	GALADHRIM("galadhrim"),
	DUNLAND("dunland"),
	URUK("uruk"),
	ENT("ent"),
	ROHAN("rohan"),
	GONDOR("gondor"),
	MORDOR("mordor"),
	NEAR_HARAD("nearHarad");
	
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
		
		HOBBIT.addQuest(new LOTRMiniQuestCollect.QuestFactory("pipeweed").setCollectItem(new ItemStack(LOTRMod.pipeweed), 20, 40).setRewardFactor(0.25F));
		HOBBIT.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectDrink").setCollectItem(new ItemStack(LOTRMod.mugAle), 1, 6).setRewardFactor(3F));
		HOBBIT.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectDrink").setCollectItem(new ItemStack(LOTRMod.mugCider), 1, 6).setRewardFactor(3F));
		HOBBIT.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectDrink").setCollectItem(new ItemStack(LOTRMod.mugPerry), 1, 6).setRewardFactor(3F));
		HOBBIT.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectDrink").setCollectItem(new ItemStack(LOTRMod.mugMead), 1, 6).setRewardFactor(3F));
		HOBBIT.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectDrink").setCollectItem(new ItemStack(LOTRMod.mugCherryLiqueur), 1, 6).setRewardFactor(3F));
		HOBBIT.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectFruit").setCollectItem(new ItemStack(Items.apple), 4, 10).setRewardFactor(2F));
		HOBBIT.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectFruit").setCollectItem(new ItemStack(LOTRMod.appleGreen), 4, 10).setRewardFactor(2F));
		HOBBIT.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectFruit").setCollectItem(new ItemStack(LOTRMod.pear), 4, 10).setRewardFactor(2F));
		HOBBIT.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectFruit").setCollectItem(new ItemStack(LOTRMod.cherry), 4, 10).setRewardFactor(2F));
		HOBBIT.addQuest(new LOTRMiniQuestCollect.QuestFactory("uncleBirthday").setCollectItem(new ItemStack(LOTRMod.appleCrumbleItem), 1, 3).setRewardFactor(5F));
		HOBBIT.addQuest(new LOTRMiniQuestCollect.QuestFactory("uncleBirthday").setCollectItem(new ItemStack(LOTRMod.cherryPieItem), 1, 3).setRewardFactor(5F));
		HOBBIT.addQuest(new LOTRMiniQuestCollect.QuestFactory("uncleBirthday").setCollectItem(new ItemStack(Items.bread), 4, 12).setRewardFactor(1F));
		HOBBIT.addQuest(new LOTRMiniQuestCollect.QuestFactory("uncleBirthday").setCollectItem(new ItemStack(Items.cooked_chicken), 4, 8).setRewardFactor(2F));
		HOBBIT.addQuest(new LOTRMiniQuestCollect.QuestFactory("farmingTool").setCollectItem(new ItemStack(Items.iron_hoe), 1, 3).setRewardFactor(4F));
		HOBBIT.addQuest(new LOTRMiniQuestCollect.QuestFactory("farmingTool").setCollectItem(new ItemStack(Items.bucket), 1, 4).setRewardFactor(3F));
		
		RANGER_NORTH.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectWood").setCollectItem(new ItemStack(Blocks.log, 1, 0), 30, 60).setRewardFactor(0.25F));
		RANGER_NORTH.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectWood").setCollectItem(new ItemStack(Blocks.log, 1, 1), 30, 60).setRewardFactor(0.25F));
		RANGER_NORTH.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectWood").setCollectItem(new ItemStack(LOTRMod.wood2, 1, 1), 30, 60).setRewardFactor(0.25F));
		RANGER_NORTH.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectBricks").setCollectItem(new ItemStack(Blocks.stonebrick), 40, 100).setRewardFactor(0.2F));
		RANGER_NORTH.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectBricks").setCollectItem(new ItemStack(LOTRMod.brick2, 1, 3), 30, 80).setRewardFactor(0.25F));
		RANGER_NORTH.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectFood").setCollectItem(new ItemStack(Items.bread), 10, 30).setRewardFactor(0.5F));
		RANGER_NORTH.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectFood").setCollectItem(new ItemStack(Items.cooked_beef), 5, 20).setRewardFactor(0.75F));
		RANGER_NORTH.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectFood").setCollectItem(new ItemStack(Items.cooked_porkchop), 5, 20).setRewardFactor(0.75F));
		RANGER_NORTH.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectFood").setCollectItem(new ItemStack(LOTRMod.rabbitCooked), 3, 15).setRewardFactor(1F));
		RANGER_NORTH.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectFood").setCollectItem(new ItemStack(LOTRMod.mugAle), 5, 15).setRewardFactor(1F));
		RANGER_NORTH.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectFood").setCollectItem(new ItemStack(LOTRMod.mugCider), 5, 15).setRewardFactor(1F));
		RANGER_NORTH.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectAthelas").setCollectItem(new ItemStack(LOTRMod.athelas), 2, 6).setRewardFactor(3F));
		RANGER_NORTH.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectGondorItem").setCollectItem(new ItemStack(LOTRMod.swordGondor), 1, 1).setRewardFactor(10F));
		RANGER_NORTH.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectGondorItem").setCollectItem(new ItemStack(LOTRMod.helmetGondor), 1, 1).setRewardFactor(10F));
		RANGER_NORTH.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectGondorItem").setCollectItem(new ItemStack(LOTRMod.helmetGondorWinged), 1, 1).setRewardFactor(15F));
		RANGER_NORTH.addQuest(new LOTRMiniQuestCollect.QuestFactory("craftRangerItem").setCollectItem(new ItemStack(LOTRMod.helmetRanger), 2, 5).setRewardFactor(3F));
		RANGER_NORTH.addQuest(new LOTRMiniQuestCollect.QuestFactory("craftRangerItem").setCollectItem(new ItemStack(LOTRMod.bodyRanger), 2, 5).setRewardFactor(4F));
		RANGER_NORTH.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectWeapons").setCollectItem(new ItemStack(Items.iron_sword), 2, 4).setRewardFactor(3F));
		RANGER_NORTH.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectWeapons").setCollectItem(new ItemStack(LOTRMod.daggerIron), 2, 6).setRewardFactor(2F));
		RANGER_NORTH.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectWeapons").setCollectItem(new ItemStack(Items.bow), 3, 7).setRewardFactor(2F));
		RANGER_NORTH.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectWeapons").setCollectItem(new ItemStack(Items.arrow), 20, 40).setRewardFactor(0.25F));
		RANGER_NORTH.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectMaterials").setCollectItem(new ItemStack(Blocks.wool, 1, 0), 6, 15).setRewardFactor(1F));
		RANGER_NORTH.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectMaterials").setCollectItem(new ItemStack(Items.leather), 10, 20).setRewardFactor(0.5F));
		RANGER_NORTH.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectEnemyBones").setCollectItem(new ItemStack(LOTRMod.orcBone), 10, 40).setRewardFactor(0.5F));
		RANGER_NORTH.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectEnemyBones").setCollectItem(new ItemStack(LOTRMod.wargBone), 10, 30).setRewardFactor(0.75F));
		RANGER_NORTH.addQuest(new LOTRMiniQuestKillFaction.QuestFactory("killGundabad").setKillFaction(LOTRFaction.GUNDABAD, 10, 40));
		RANGER_NORTH.addQuest(new LOTRMiniQuestKillFaction.QuestFactory("killAngmar").setKillFaction(LOTRFaction.ANGMAR, 10, 30));
		RANGER_NORTH.addQuest(new LOTRMiniQuestKillEntity.QuestFactory("killTroll").setKillEntity(LOTREntityTroll.class, 10, 30));
		RANGER_NORTH.addQuest(new LOTRMiniQuestKillEntity.QuestFactory("killMountainTroll").setKillEntity(LOTREntityMountainTroll.class, 20, 40));
		RANGER_NORTH.addQuest(new LOTRMiniQuestKillEntity.QuestFactory("killWarg").setKillEntity(LOTREntityGundabadWarg.class, 10, 40));
		RANGER_NORTH.addQuest(new LOTRMiniQuestKillEntity.QuestFactory("killWarg").setKillEntity(LOTREntityAngmarWarg.class, 10, 30));
		RANGER_NORTH.addQuest(new LOTRMiniQuestKillEntity.QuestFactory("killDarkHuorn").setKillEntity(LOTREntityDarkHuorn.class, 20, 30));
		RANGER_NORTH.addQuest(new LOTRMiniQuestKillEntity.QuestFactory("avengeBrother").setKillEntity(LOTREntityGundabadOrc.class, 10, 30));
		RANGER_NORTH.addQuest(new LOTRMiniQuestKillEntity.QuestFactory("avengeBrother").setKillEntity(LOTREntityGundabadWarg.class, 10, 30));
		
		BLUE_MOUNTAINS.addQuest(new LOTRMiniQuestCollect.QuestFactory("mineMithril").setCollectItem(new ItemStack(LOTRMod.oreMithril), 1, 6).setRewardFactor(10F));
		BLUE_MOUNTAINS.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectMineral").setCollectItem(new ItemStack(Items.gold_ingot), 3, 15).setRewardFactor(2F));
		BLUE_MOUNTAINS.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectMineral").setCollectItem(new ItemStack(LOTRMod.silver), 3, 15).setRewardFactor(2F));
		BLUE_MOUNTAINS.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectMineral").setCollectItem(new ItemStack(Items.diamond), 2, 8).setRewardFactor(4F));
		BLUE_MOUNTAINS.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectMineral").setCollectItem(new ItemStack(Items.glowstone_dust), 5, 15).setRewardFactor(2F));
		BLUE_MOUNTAINS.addQuest(new LOTRMiniQuestCollect.QuestFactory("forgeDwarfWeapon").setCollectItem(new ItemStack(LOTRMod.hammerBlueDwarven), 1, 3).setRewardFactor(5F));
		BLUE_MOUNTAINS.addQuest(new LOTRMiniQuestCollect.QuestFactory("forgeDwarfWeapon").setCollectItem(new ItemStack(LOTRMod.battleaxeBlueDwarven), 1, 3).setRewardFactor(5F));
		BLUE_MOUNTAINS.addQuest(new LOTRMiniQuestCollect.QuestFactory("forgeDwarfWeapon").setCollectItem(new ItemStack(LOTRMod.throwingAxeBlueDwarven), 1, 4).setRewardFactor(4F));
		BLUE_MOUNTAINS.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectDrink").setCollectItem(new ItemStack(LOTRMod.mugDwarvenAle), 2, 5).setRewardFactor(3F));
		BLUE_MOUNTAINS.addQuest(new LOTRMiniQuestKillFaction.QuestFactory("killGundabad").setKillFaction(LOTRFaction.GUNDABAD, 20, 40));
		BLUE_MOUNTAINS.addQuest(new LOTRMiniQuestKillEntity.QuestFactory("killOrc").setKillEntity(LOTREntityGundabadOrc.class, 10, 30));
		BLUE_MOUNTAINS.addQuest(new LOTRMiniQuestKillEntity.QuestFactory("killWarg").setKillEntity(LOTREntityGundabadWarg.class, 10, 30));
		BLUE_MOUNTAINS.addQuest(new LOTRMiniQuestKillEntity.QuestFactory("killSpider").setKillEntity(LOTREntityMirkwoodSpider.class, 20, 30));

		HIGH_ELF.addQuest(new LOTRMiniQuestCollect.QuestFactory("collect").setCollectItem(new ItemStack(Blocks.sapling, 1, 2), 5, 20).setRewardFactor(1F));
		HIGH_ELF.addQuest(new LOTRMiniQuestCollect.QuestFactory("collect").setCollectItem(new ItemStack(LOTRMod.sapling2, 1, 1), 5, 20).setRewardFactor(1F));
		HIGH_ELF.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectBirchWood").setCollectItem(new ItemStack(Blocks.log, 1, 1), 10, 50).setRewardFactor(0.5F));
		HIGH_ELF.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectGoldenLeaves").setCollectItem(new ItemStack(LOTRMod.leaves, 1, 1), 10, 20).setRewardFactor(1F));
		HIGH_ELF.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectMallornSapling").setCollectItem(new ItemStack(LOTRMod.sapling, 1, 1), 3, 10).setRewardFactor(2F));
		HIGH_ELF.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectMallornNut").setCollectItem(new ItemStack(LOTRMod.mallornNut), 1, 3).setRewardFactor(5F));
		HIGH_ELF.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectCrystal").setCollectItem(new ItemStack(LOTRMod.quenditeCrystal), 4, 16).setRewardFactor(1F));
		HIGH_ELF.addQuest(new LOTRMiniQuestCollect.QuestFactory("forge").setCollectItem(new ItemStack(LOTRMod.swordHighElven), 1, 4).setRewardFactor(3F));
		HIGH_ELF.addQuest(new LOTRMiniQuestCollect.QuestFactory("forge").setCollectItem(new ItemStack(LOTRMod.spearHighElven), 1, 4).setRewardFactor(2F));
		HIGH_ELF.addQuest(new LOTRMiniQuestCollect.QuestFactory("forge").setCollectItem(new ItemStack(LOTRMod.helmetHighElven), 1, 4).setRewardFactor(3F));
		HIGH_ELF.addQuest(new LOTRMiniQuestCollect.QuestFactory("forge").setCollectItem(new ItemStack(LOTRMod.bodyHighElven), 1, 4).setRewardFactor(4F));
		HIGH_ELF.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectMineral").setCollectItem(new ItemStack(Items.gold_ingot), 3, 10).setRewardFactor(2F));
		HIGH_ELF.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectMineral").setCollectItem(new ItemStack(LOTRMod.silver), 3, 10).setRewardFactor(2F));
		HIGH_ELF.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectMineral").setCollectItem(new ItemStack(Items.diamond), 3, 5).setRewardFactor(4F));
		HIGH_ELF.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectDwarven").setCollectItem(new ItemStack(Blocks.glowstone), 2, 6).setRewardFactor(4F));
		HIGH_ELF.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectNumenorItem").setCollectItem(new ItemStack(LOTRMod.swordGondor), 1, 1).setRewardFactor(10F));
		HIGH_ELF.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectNumenorItem").setCollectItem(new ItemStack(LOTRMod.helmetGondorWinged), 1, 1).setRewardFactor(15F));
		HIGH_ELF.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectNumenorItem").setCollectItem(new ItemStack(LOTRMod.helmetRanger), 1, 1).setRewardFactor(10F));
		HIGH_ELF.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectNumenorItem").setCollectItem(new ItemStack(LOTRMod.brick, 1, 1), 10, 20).setRewardFactor(1F));
		HIGH_ELF.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectNumenorItem").setCollectItem(new ItemStack(LOTRMod.brick, 1, 5), 3, 5).setRewardFactor(4F));
		HIGH_ELF.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectNumenorItem").setCollectItem(new ItemStack(LOTRMod.brick2, 1, 3), 10, 20).setRewardFactor(1F));
		HIGH_ELF.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectNumenorItem").setCollectItem(new ItemStack(LOTRMod.brick2, 1, 6), 3, 5).setRewardFactor(3F));
		HIGH_ELF.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectPlants").setCollectItem(new ItemStack(Blocks.sapling, 1, 0), 2, 5).setRewardFactor(3F));
		HIGH_ELF.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectPlants").setCollectItem(new ItemStack(Blocks.sapling, 1, 1), 2, 5).setRewardFactor(3F));
		HIGH_ELF.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectPlants").setCollectItem(new ItemStack(Blocks.sapling, 1, 2), 2, 5).setRewardFactor(2F));
		HIGH_ELF.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectPlants").setCollectItem(new ItemStack(Blocks.red_flower, 1, 0), 2, 8).setRewardFactor(1F));
		HIGH_ELF.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectPlants").setCollectItem(new ItemStack(Blocks.yellow_flower, 1, 0), 2, 8).setRewardFactor(1F));
		HIGH_ELF.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectString").setCollectItem(new ItemStack(Items.string), 5, 20).setRewardFactor(1F));
		HIGH_ELF.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectOrcItem").setCollectItem(new ItemStack(LOTRMod.helmetOrc), 1, 1).setRewardFactor(10F));
		HIGH_ELF.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectOrcItem").setCollectItem(new ItemStack(LOTRMod.helmetAngmar), 1, 1).setRewardFactor(10F));
		HIGH_ELF.addQuest(new LOTRMiniQuestKillEntity.QuestFactory("killOrc").setKillEntity(LOTREntityGundabadOrc.class, 10, 40));
		HIGH_ELF.addQuest(new LOTRMiniQuestKillEntity.QuestFactory("killOrc").setKillEntity(LOTREntityAngmarOrc.class, 10, 40));
		HIGH_ELF.addQuest(new LOTRMiniQuestKillFaction.QuestFactory("killGundabad").setKillFaction(LOTRFaction.GUNDABAD, 10, 40));
		HIGH_ELF.addQuest(new LOTRMiniQuestKillFaction.QuestFactory("killAngmar").setKillFaction(LOTRFaction.ANGMAR, 10, 30));
		HIGH_ELF.addQuest(new LOTRMiniQuestKillEntity.QuestFactory("killWarg").setKillEntity(LOTREntityGundabadWarg.class, 10, 30));
		HIGH_ELF.addQuest(new LOTRMiniQuestKillEntity.QuestFactory("killWarg").setKillEntity(LOTREntityAngmarWarg.class, 10, 30));
		HIGH_ELF.addQuest(new LOTRMiniQuestKillEntity.QuestFactory("killTroll").setKillEntity(LOTREntityTroll.class, 10, 30));
		
		GUNDABAD.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectWeapon").setCollectItem(new ItemStack(Items.iron_sword), 1, 5).setRewardFactor(3F));
		GUNDABAD.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectWeapon").setCollectItem(new ItemStack(LOTRMod.daggerIron), 1, 6).setRewardFactor(2F));
		GUNDABAD.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectFood").setCollectItem(new ItemStack(Items.cooked_porkchop), 2, 8).setRewardFactor(2F));
		GUNDABAD.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectFood").setCollectItem(new ItemStack(Items.cooked_beef), 2, 8).setRewardFactor(2F));
		GUNDABAD.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectFood").setCollectItem(new ItemStack(Items.bone), 2, 8).setRewardFactor(2F));
		GUNDABAD.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectArmour").setCollectItem(new ItemStack(Items.iron_helmet), 2, 5).setRewardFactor(3F));
		GUNDABAD.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectArmour").setCollectItem(new ItemStack(Items.iron_chestplate), 2, 5).setRewardFactor(4F));
		GUNDABAD.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectArmour").setCollectItem(new ItemStack(LOTRMod.helmetBronze), 2, 5).setRewardFactor(3F));
		GUNDABAD.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectArmour").setCollectItem(new ItemStack(LOTRMod.bodyBronze), 2, 5).setRewardFactor(4F));
		GUNDABAD.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectMineral").setCollectItem(new ItemStack(Items.iron_ingot), 3, 10).setRewardFactor(2F));
		GUNDABAD.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectMineral").setCollectItem(new ItemStack(Items.gold_ingot), 3, 8).setRewardFactor(3F));
		GUNDABAD.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectMineral").setCollectItem(new ItemStack(LOTRMod.silver), 3, 8).setRewardFactor(3F));
		GUNDABAD.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectMineral").setCollectItem(new ItemStack(Items.diamond), 1, 4).setRewardFactor(5F));
		GUNDABAD.addQuest(new LOTRMiniQuestKillFaction.QuestFactory("killEnemy").setKillFaction(LOTRFaction.HOBBIT, 10, 30));
		GUNDABAD.addQuest(new LOTRMiniQuestKillFaction.QuestFactory("killEnemy").setKillFaction(LOTRFaction.RANGER_NORTH, 10, 40));
		GUNDABAD.addQuest(new LOTRMiniQuestKillFaction.QuestFactory("killEnemy").setKillFaction(LOTRFaction.HIGH_ELF, 10, 40));
		GUNDABAD.addQuest(new LOTRMiniQuestKillFaction.QuestFactory("killEnemy").setKillFaction(LOTRFaction.GALADHRIM, 10, 40));
		GUNDABAD.addQuest(new LOTRMiniQuestKillEntity.QuestFactory("killRanger").setKillEntity(LOTREntityRangerNorth.class, 10, 30));
		GUNDABAD.addQuest(new LOTRMiniQuestKillEntity.QuestFactory("killElf").setKillEntity(LOTREntityHighElf.class, 10, 30));
		GUNDABAD.addQuest(new LOTRMiniQuestKillEntity.QuestFactory("killElf").setKillEntity(LOTREntityGaladhrimElf.class, 10, 30));
		GUNDABAD.addQuest(new LOTRMiniQuestKillEntity.QuestFactory("killDwarf").setKillEntity(LOTREntityDwarf.class, 10, 30));
		
		ANGMAR.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectWeapon").setCollectItem(new ItemStack(LOTRMod.swordAngmar), 1, 5).setRewardFactor(3F));
		ANGMAR.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectWeapon").setCollectItem(new ItemStack(LOTRMod.battleaxeAngmar), 1, 5).setRewardFactor(3F));
		ANGMAR.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectWeapon").setCollectItem(new ItemStack(LOTRMod.axeAngmar), 1, 5).setRewardFactor(3F));
		ANGMAR.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectWeapon").setCollectItem(new ItemStack(LOTRMod.daggerAngmar), 1, 6).setRewardFactor(2F));
		ANGMAR.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectFood").setCollectItem(new ItemStack(Items.cooked_porkchop), 2, 8).setRewardFactor(2F));
		ANGMAR.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectFood").setCollectItem(new ItemStack(Items.cooked_beef), 2, 8).setRewardFactor(2F));
		ANGMAR.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectFood").setCollectItem(new ItemStack(Items.bone), 2, 8).setRewardFactor(2F));
		ANGMAR.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectArmour").setCollectItem(new ItemStack(LOTRMod.helmetAngmar), 2, 5).setRewardFactor(3F));
		ANGMAR.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectArmour").setCollectItem(new ItemStack(LOTRMod.bodyAngmar), 2, 5).setRewardFactor(4F));
		ANGMAR.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectArmour").setCollectItem(new ItemStack(LOTRMod.legsAngmar), 2, 5).setRewardFactor(3F));
		ANGMAR.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectArmour").setCollectItem(new ItemStack(LOTRMod.bootsAngmar), 2, 5).setRewardFactor(3F));
		ANGMAR.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectMineral").setCollectItem(new ItemStack(Items.iron_ingot), 3, 10).setRewardFactor(2F));
		ANGMAR.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectMineral").setCollectItem(new ItemStack(Items.gold_ingot), 3, 8).setRewardFactor(3F));
		ANGMAR.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectMineral").setCollectItem(new ItemStack(LOTRMod.silver), 3, 8).setRewardFactor(3F));
		ANGMAR.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectMineral").setCollectItem(new ItemStack(LOTRMod.guldurilCrystal), 3, 6).setRewardFactor(4F));
		ANGMAR.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectMineral").setCollectItem(new ItemStack(Items.diamond), 1, 4).setRewardFactor(5F));
		ANGMAR.addQuest(new LOTRMiniQuestKillFaction.QuestFactory("killEnemy").setKillFaction(LOTRFaction.RANGER_NORTH, 10, 40));
		ANGMAR.addQuest(new LOTRMiniQuestKillFaction.QuestFactory("killEnemy").setKillFaction(LOTRFaction.HIGH_ELF, 10, 40));
		ANGMAR.addQuest(new LOTRMiniQuestKillEntity.QuestFactory("killRanger").setKillEntity(LOTREntityRangerNorth.class, 10, 30));
		ANGMAR.addQuest(new LOTRMiniQuestKillEntity.QuestFactory("killElf").setKillEntity(LOTREntityHighElf.class, 10, 30));

		WOOD_ELF.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectGoldenLeaves").setCollectItem(new ItemStack(LOTRMod.leaves, 1, 1), 10, 20).setRewardFactor(1F));
		WOOD_ELF.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectMallornSapling").setCollectItem(new ItemStack(LOTRMod.sapling, 1, 1), 3, 10).setRewardFactor(2F));
		WOOD_ELF.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectMallornNut").setCollectItem(new ItemStack(LOTRMod.mallornNut), 1, 3).setRewardFactor(5F));
		WOOD_ELF.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectLorienFlowers").setCollectItem(new ItemStack(LOTRMod.mallornNut), 1, 3).setRewardFactor(5F));
		WOOD_ELF.addQuest(new LOTRMiniQuestCollect.QuestFactory("forge").setCollectItem(new ItemStack(LOTRMod.swordWoodElven), 1, 4).setRewardFactor(3F));
		WOOD_ELF.addQuest(new LOTRMiniQuestCollect.QuestFactory("forge").setCollectItem(new ItemStack(LOTRMod.spearWoodElven), 1, 4).setRewardFactor(2F));
		WOOD_ELF.addQuest(new LOTRMiniQuestCollect.QuestFactory("forge").setCollectItem(new ItemStack(LOTRMod.helmetWoodElven), 1, 4).setRewardFactor(3F));
		WOOD_ELF.addQuest(new LOTRMiniQuestCollect.QuestFactory("forge").setCollectItem(new ItemStack(LOTRMod.bodyWoodElven), 1, 4).setRewardFactor(4F));
		WOOD_ELF.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectPlants").setCollectItem(new ItemStack(LOTRMod.sapling, 1, 2), 4, 8).setRewardFactor(2F));
		WOOD_ELF.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectPlants").setCollectItem(new ItemStack(LOTRMod.sapling, 1, 3), 2, 5).setRewardFactor(4F));
		WOOD_ELF.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectString").setCollectItem(new ItemStack(Items.string), 5, 20).setRewardFactor(1F));
		WOOD_ELF.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectOrcItem").setCollectItem(new ItemStack(LOTRMod.helmetOrc), 1, 1).setRewardFactor(10F));
		WOOD_ELF.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectOrcItem").setCollectItem(new ItemStack(LOTRMod.helmetDolGuldur), 1, 1).setRewardFactor(10F));
		WOOD_ELF.addQuest(new LOTRMiniQuestKillFaction.QuestFactory("killDolGuldur").setKillFaction(LOTRFaction.DOL_GULDUR, 10, 40));
		WOOD_ELF.addQuest(new LOTRMiniQuestKillEntity.QuestFactory("killOrc").setKillEntity(LOTREntityGundabadOrc.class, 10, 30));
		WOOD_ELF.addQuest(new LOTRMiniQuestKillEntity.QuestFactory("killOrc").setKillEntity(LOTREntityDolGuldurOrc.class, 10, 40));
		WOOD_ELF.addQuest(new LOTRMiniQuestKillFaction.QuestFactory("killGundabad").setKillFaction(LOTRFaction.GUNDABAD, 10, 30));
		WOOD_ELF.addQuest(new LOTRMiniQuestKillEntity.QuestFactory("killWarg").setKillEntity(LOTREntityGundabadWarg.class, 10, 30));
		WOOD_ELF.addQuest(new LOTRMiniQuestKillEntity.QuestFactory("killSpider").setKillEntity(LOTREntityMirkwoodSpider.class, 10, 40));
		
		DOL_GULDUR.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectWeapon").setCollectItem(new ItemStack(LOTRMod.swordDolGuldur), 1, 5).setRewardFactor(3F));
		DOL_GULDUR.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectWeapon").setCollectItem(new ItemStack(LOTRMod.battleaxeDolGuldur), 1, 5).setRewardFactor(3F));
		DOL_GULDUR.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectWeapon").setCollectItem(new ItemStack(LOTRMod.axeDolGuldur), 1, 5).setRewardFactor(3F));
		DOL_GULDUR.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectWeapon").setCollectItem(new ItemStack(LOTRMod.daggerDolGuldur), 1, 6).setRewardFactor(2F));
		DOL_GULDUR.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectFood").setCollectItem(new ItemStack(Items.cooked_porkchop), 2, 8).setRewardFactor(2F));
		DOL_GULDUR.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectFood").setCollectItem(new ItemStack(Items.cooked_beef), 2, 8).setRewardFactor(2F));
		DOL_GULDUR.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectFood").setCollectItem(new ItemStack(Items.bone), 2, 8).setRewardFactor(2F));
		DOL_GULDUR.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectArmour").setCollectItem(new ItemStack(LOTRMod.helmetDolGuldur), 2, 5).setRewardFactor(3F));
		DOL_GULDUR.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectArmour").setCollectItem(new ItemStack(LOTRMod.bodyDolGuldur), 2, 5).setRewardFactor(4F));
		DOL_GULDUR.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectArmour").setCollectItem(new ItemStack(LOTRMod.legsDolGuldur), 2, 5).setRewardFactor(3F));
		DOL_GULDUR.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectArmour").setCollectItem(new ItemStack(LOTRMod.bootsDolGuldur), 2, 5).setRewardFactor(3F));
		DOL_GULDUR.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectMineral").setCollectItem(new ItemStack(Items.iron_ingot), 3, 10).setRewardFactor(2F));
		DOL_GULDUR.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectMineral").setCollectItem(new ItemStack(Items.gold_ingot), 3, 8).setRewardFactor(3F));
		DOL_GULDUR.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectMineral").setCollectItem(new ItemStack(LOTRMod.silver), 3, 8).setRewardFactor(3F));
		DOL_GULDUR.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectMineral").setCollectItem(new ItemStack(LOTRMod.guldurilCrystal), 3, 6).setRewardFactor(4F));
		DOL_GULDUR.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectMineral").setCollectItem(new ItemStack(Items.diamond), 1, 4).setRewardFactor(5F));
		DOL_GULDUR.addQuest(new LOTRMiniQuestKillFaction.QuestFactory("killEnemy").setKillFaction(LOTRFaction.GALADHRIM, 10, 40));
		DOL_GULDUR.addQuest(new LOTRMiniQuestKillFaction.QuestFactory("killEnemy").setKillFaction(LOTRFaction.WOOD_ELF, 10, 40));
		DOL_GULDUR.addQuest(new LOTRMiniQuestKillEntity.QuestFactory("killElf").setKillEntity(LOTREntityGaladhrimElf.class, 10, 30));
		DOL_GULDUR.addQuest(new LOTRMiniQuestKillEntity.QuestFactory("killElf").setKillEntity(LOTREntityWoodElf.class, 10, 30));

		DURIN.addQuest(new LOTRMiniQuestCollect.QuestFactory("mineMithril").setCollectItem(new ItemStack(LOTRMod.oreMithril), 1, 6).setRewardFactor(10F));
		DURIN.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectMineral").setCollectItem(new ItemStack(Items.gold_ingot), 3, 15).setRewardFactor(2F));
		DURIN.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectMineral").setCollectItem(new ItemStack(LOTRMod.silver), 3, 15).setRewardFactor(2F));
		DURIN.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectMineral").setCollectItem(new ItemStack(Items.diamond), 2, 8).setRewardFactor(4F));
		DURIN.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectMineral").setCollectItem(new ItemStack(Items.glowstone_dust), 5, 15).setRewardFactor(2F));
		DURIN.addQuest(new LOTRMiniQuestCollect.QuestFactory("forgeDwarfWeapon").setCollectItem(new ItemStack(LOTRMod.hammerDwarven), 1, 3).setRewardFactor(5F));
		DURIN.addQuest(new LOTRMiniQuestCollect.QuestFactory("forgeDwarfWeapon").setCollectItem(new ItemStack(LOTRMod.battleaxeDwarven), 1, 3).setRewardFactor(5F));
		DURIN.addQuest(new LOTRMiniQuestCollect.QuestFactory("forgeDwarfWeapon").setCollectItem(new ItemStack(LOTRMod.throwingAxeDwarven), 1, 4).setRewardFactor(4F));
		DURIN.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectDrink").setCollectItem(new ItemStack(LOTRMod.mugDwarvenAle), 2, 5).setRewardFactor(3F));
		DURIN.addQuest(new LOTRMiniQuestKillFaction.QuestFactory("killGundabad").setKillFaction(LOTRFaction.GUNDABAD, 20, 40));
		DURIN.addQuest(new LOTRMiniQuestKillEntity.QuestFactory("killOrc").setKillEntity(LOTREntityGundabadOrc.class, 10, 30));
		DURIN.addQuest(new LOTRMiniQuestKillEntity.QuestFactory("killWarg").setKillEntity(LOTREntityGundabadWarg.class, 10, 30));
		DURIN.addQuest(new LOTRMiniQuestKillEntity.QuestFactory("killSpider").setKillEntity(LOTREntityMirkwoodSpider.class, 20, 30));
		
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
		GALADHRIM.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectOrcItem").setCollectItem(new ItemStack(LOTRMod.helmetDolGuldur), 1, 1).setRewardFactor(10F));
		GALADHRIM.addQuest(new LOTRMiniQuestKillFaction.QuestFactory("killDolGuldur").setKillFaction(LOTRFaction.DOL_GULDUR, 10, 30));
		GALADHRIM.addQuest(new LOTRMiniQuestKillEntity.QuestFactory("killOrc").setKillEntity(LOTREntityGundabadOrc.class, 10, 30));
		GALADHRIM.addQuest(new LOTRMiniQuestKillEntity.QuestFactory("killOrc").setKillEntity(LOTREntityDolGuldurOrc.class, 10, 30));
		GALADHRIM.addQuest(new LOTRMiniQuestKillFaction.QuestFactory("killGundabad").setKillFaction(LOTRFaction.GUNDABAD, 10, 30));
		GALADHRIM.addQuest(new LOTRMiniQuestKillEntity.QuestFactory("killWarg").setKillEntity(LOTREntityGundabadWarg.class, 10, 30));
		
		DUNLAND.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectResources").setCollectItem(new ItemStack(Blocks.log, 1, 0), 30, 80).setRewardFactor(0.25F));
		DUNLAND.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectResources").setCollectItem(new ItemStack(Blocks.log, 1, 1), 30, 80).setRewardFactor(0.25F));
		DUNLAND.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectResources").setCollectItem(new ItemStack(Items.coal), 10, 30).setRewardFactor(0.5F));
		DUNLAND.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectResources").setCollectItem(new ItemStack(Blocks.cobblestone), 30, 80).setRewardFactor(0.25F));
		DUNLAND.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectResources").setCollectItem(new ItemStack(Items.leather), 10, 30).setRewardFactor(0.5F));
		DUNLAND.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectResources").setCollectItem(new ItemStack(Items.iron_ingot), 3, 10).setRewardFactor(1.5F));
		DUNLAND.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectDrink").setCollectItem(new ItemStack(LOTRMod.mugAle), 3, 10).setRewardFactor(2F));
		DUNLAND.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectDrink").setCollectItem(new ItemStack(LOTRMod.mugMead), 3, 10).setRewardFactor(2F));
		DUNLAND.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectDrink").setCollectItem(new ItemStack(LOTRMod.mugCider), 3, 10).setRewardFactor(2F));
		DUNLAND.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectDrink").setCollectItem(new ItemStack(LOTRMod.mugRum), 3, 10).setRewardFactor(2F));
		DUNLAND.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectRohanItem").setCollectItem(new ItemStack(LOTRMod.helmetRohan), 1, 3).setRewardFactor(10F));
		DUNLAND.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectRohanItem").setCollectItem(new ItemStack(LOTRMod.bodyRohan), 1, 3).setRewardFactor(10F));
		DUNLAND.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectRohanItem").setCollectItem(new ItemStack(LOTRMod.swordRohan), 1, 3).setRewardFactor(10F));
		DUNLAND.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectFood").setCollectItem(new ItemStack(Items.cooked_porkchop), 3, 8).setRewardFactor(2F));
		DUNLAND.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectFood").setCollectItem(new ItemStack(Items.cooked_beef), 3, 8).setRewardFactor(2F));
		DUNLAND.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectFood").setCollectItem(new ItemStack(Items.cooked_chicken), 3, 8).setRewardFactor(2F));
		DUNLAND.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectFood").setCollectItem(new ItemStack(LOTRMod.rabbitCooked), 3, 12).setRewardFactor(2F));
		DUNLAND.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectFood").setCollectItem(new ItemStack(Items.bread), 5, 15).setRewardFactor(1F));
		DUNLAND.addQuest(new LOTRMiniQuestKillFaction.QuestFactory("killRohirrim").setKillFaction(LOTRFaction.ROHAN, 10, 40));
		DUNLAND.addQuest(new LOTRMiniQuestKillFaction.QuestFactory("avengeKin").setKillFaction(LOTRFaction.ROHAN, 30, 60));
		DUNLAND.addQuest(new LOTRMiniQuestKillEntity.QuestFactory("killHorse").setKillEntity(LOTREntityHorse.class, 10, 20));
		
		URUK.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectWeapon").setCollectItem(new ItemStack(LOTRMod.scimitarUruk), 1, 5).setRewardFactor(3F));
		URUK.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectWeapon").setCollectItem(new ItemStack(LOTRMod.battleaxeUruk), 1, 5).setRewardFactor(3F));
		URUK.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectWeapon").setCollectItem(new ItemStack(LOTRMod.axeUruk), 1, 5).setRewardFactor(3F));
		URUK.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectWeapon").setCollectItem(new ItemStack(LOTRMod.daggerUruk), 1, 6).setRewardFactor(2F));
		URUK.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectFood").setCollectItem(new ItemStack(Items.cooked_porkchop), 2, 8).setRewardFactor(2F));
		URUK.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectFood").setCollectItem(new ItemStack(Items.cooked_beef), 2, 8).setRewardFactor(2F));
		URUK.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectFood").setCollectItem(new ItemStack(Items.bone), 2, 8).setRewardFactor(2F));
		URUK.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectArmour").setCollectItem(new ItemStack(LOTRMod.helmetUruk), 2, 5).setRewardFactor(3F));
		URUK.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectArmour").setCollectItem(new ItemStack(LOTRMod.bodyUruk), 2, 5).setRewardFactor(4F));
		URUK.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectArmour").setCollectItem(new ItemStack(LOTRMod.legsUruk), 2, 5).setRewardFactor(3F));
		URUK.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectArmour").setCollectItem(new ItemStack(LOTRMod.bootsUruk), 2, 5).setRewardFactor(3F));
		URUK.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectMineral").setCollectItem(new ItemStack(Items.iron_ingot), 3, 10).setRewardFactor(2F));
		URUK.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectMineral").setCollectItem(new ItemStack(Items.gold_ingot), 3, 8).setRewardFactor(3F));
		URUK.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectMineral").setCollectItem(new ItemStack(LOTRMod.silver), 3, 8).setRewardFactor(3F));
		URUK.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectMineral").setCollectItem(new ItemStack(Items.diamond), 1, 4).setRewardFactor(5F));
		URUK.addQuest(new LOTRMiniQuestCollect.QuestFactory("forgeSteel").setCollectItem(new ItemStack(LOTRMod.urukSteel), 3, 10).setRewardFactor(3F));
		URUK.addQuest(new LOTRMiniQuestKillFaction.QuestFactory("killEnemy").setKillFaction(LOTRFaction.ROHAN, 10, 40));
		URUK.addQuest(new LOTRMiniQuestKillFaction.QuestFactory("killEnemy").setKillFaction(LOTRFaction.GONDOR, 10, 40));
		URUK.addQuest(new LOTRMiniQuestKillEntity.QuestFactory("killMen").setKillEntity(LOTREntityRohirrim.class, 10, 30));
		URUK.addQuest(new LOTRMiniQuestKillEntity.QuestFactory("killMen").setKillEntity(LOTREntityGondorSoldier.class, 10, 30));
		
		ROHAN.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectFood").setCollectItem(new ItemStack(Items.cooked_porkchop), 3, 8).setRewardFactor(2F));
		ROHAN.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectFood").setCollectItem(new ItemStack(Items.cooked_beef), 3, 8).setRewardFactor(2F));
		ROHAN.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectFood").setCollectItem(new ItemStack(Items.cooked_chicken), 3, 8).setRewardFactor(2F));
		ROHAN.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectFood").setCollectItem(new ItemStack(LOTRMod.rabbitCooked), 3, 12).setRewardFactor(2F));
		ROHAN.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectFood").setCollectItem(new ItemStack(Items.bread), 5, 15).setRewardFactor(1F));
		ROHAN.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectMead").setCollectItem(new ItemStack(LOTRMod.mugMead), 3, 20).setRewardFactor(1F));
		ROHAN.addQuest(new LOTRMiniQuestCollect.QuestFactory("defences").setCollectItem(new ItemStack(Blocks.log, 1, 0), 20, 60).setRewardFactor(0.25F));
		ROHAN.addQuest(new LOTRMiniQuestCollect.QuestFactory("defences").setCollectItem(new ItemStack(Blocks.log, 1, 1), 20, 60).setRewardFactor(0.25F));
		ROHAN.addQuest(new LOTRMiniQuestCollect.QuestFactory("defences").setCollectItem(new ItemStack(Blocks.planks, 1, 0), 80, 160).setRewardFactor(0.125F));
		ROHAN.addQuest(new LOTRMiniQuestCollect.QuestFactory("defences").setCollectItem(new ItemStack(Blocks.planks, 1, 1), 80, 160).setRewardFactor(0.125F));
		ROHAN.addQuest(new LOTRMiniQuestCollect.QuestFactory("defences").setCollectItem(new ItemStack(Blocks.cobblestone), 30, 80).setRewardFactor(0.25F));
		ROHAN.addQuest(new LOTRMiniQuestCollect.QuestFactory("bringWeapon").setCollectItem(new ItemStack(LOTRMod.swordRohan), 1, 4).setRewardFactor(3F));
		ROHAN.addQuest(new LOTRMiniQuestCollect.QuestFactory("bringWeapon").setCollectItem(new ItemStack(Items.iron_ingot), 3, 8).setRewardFactor(2F));
		ROHAN.addQuest(new LOTRMiniQuestCollect.QuestFactory("stealUruk").setCollectItem(new ItemStack(LOTRMod.urukTable), 1, 2).setRewardFactor(10F));
		ROHAN.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectBones").setCollectItem(new ItemStack(LOTRMod.orcBone), 15, 30).setRewardFactor(0.5F));
		ROHAN.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectBones").setCollectItem(new ItemStack(LOTRMod.wargBone), 15, 30).setRewardFactor(0.75F));
		ROHAN.addQuest(new LOTRMiniQuestKillEntity.QuestFactory("killOrc").setKillEntity(LOTREntityUrukHai.class, 10, 30));
		ROHAN.addQuest(new LOTRMiniQuestKillEntity.QuestFactory("killOrc").setKillEntity(LOTREntityMordorOrc.class, 10, 30));
		ROHAN.addQuest(new LOTRMiniQuestKillEntity.QuestFactory("avengeRiders").setKillEntity(LOTREntityUrukWarg.class, 10, 20));
		ROHAN.addQuest(new LOTRMiniQuestKillFaction.QuestFactory("killDunland").setKillFaction(LOTRFaction.DUNLAND, 10, 40));
		
		GONDOR.addQuest(new LOTRMiniQuestCollect.QuestFactory("defences").setCollectItem(new ItemStack(Blocks.log, 1, 0), 20, 60).setRewardFactor(0.25F));
		GONDOR.addQuest(new LOTRMiniQuestCollect.QuestFactory("defences").setCollectItem(new ItemStack(LOTRMod.rock, 1, 1), 30, 80).setRewardFactor(0.25F));
		GONDOR.addQuest(new LOTRMiniQuestCollect.QuestFactory("defences").setCollectItem(new ItemStack(LOTRMod.brick, 1, 1), 30, 60).setRewardFactor(0.5F));
		GONDOR.addQuest(new LOTRMiniQuestCollect.QuestFactory("lebethron").setCollectItem(new ItemStack(LOTRMod.wood2, 1, 0), 10, 30).setRewardFactor(1F));
		GONDOR.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectIron").setCollectItem(new ItemStack(Blocks.iron_ore), 10, 20).setRewardFactor(1F));
		GONDOR.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectIron").setCollectItem(new ItemStack(Items.iron_ingot), 6, 15).setRewardFactor(1.5F));
		GONDOR.addQuest(new LOTRMiniQuestCollect.QuestFactory("blackMarble").setCollectItem(new ItemStack(LOTRMod.rock, 1, 0), 25, 35).setRewardFactor(1F));
		GONDOR.addQuest(new LOTRMiniQuestCollect.QuestFactory("forge").setCollectItem(new ItemStack(LOTRMod.swordGondor), 1, 4).setRewardFactor(5F));
		GONDOR.addQuest(new LOTRMiniQuestCollect.QuestFactory("forge").setCollectItem(new ItemStack(LOTRMod.spearGondor), 1, 4).setRewardFactor(5F));
		GONDOR.addQuest(new LOTRMiniQuestCollect.QuestFactory("forge").setCollectItem(new ItemStack(LOTRMod.helmetGondor), 1, 4).setRewardFactor(5F));
		GONDOR.addQuest(new LOTRMiniQuestCollect.QuestFactory("forge").setCollectItem(new ItemStack(LOTRMod.helmetGondorWinged), 1, 4).setRewardFactor(5F));
		GONDOR.addQuest(new LOTRMiniQuestCollect.QuestFactory("forge").setCollectItem(new ItemStack(LOTRMod.bodyGondor), 1, 4).setRewardFactor(5F));
		GONDOR.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectRohanItem").setCollectItem(new ItemStack(LOTRMod.swordRohan), 1, 1).setRewardFactor(20F));
		GONDOR.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectRohanItem").setCollectItem(new ItemStack(LOTRMod.helmetRohan), 1, 1).setRewardFactor(20F));
		GONDOR.addQuest(new LOTRMiniQuestKillFaction.QuestFactory("killMordor").setKillFaction(LOTRFaction.MORDOR, 10, 40));
		GONDOR.addQuest(new LOTRMiniQuestKillFaction.QuestFactory("killEnemy").setKillFaction(LOTRFaction.MORDOR, 30, 40));
		GONDOR.addQuest(new LOTRMiniQuestKillFaction.QuestFactory("killEnemy").setKillFaction(LOTRFaction.DUNLAND, 30, 40));
		GONDOR.addQuest(new LOTRMiniQuestKillFaction.QuestFactory("killEnemy").setKillFaction(LOTRFaction.URUK_HAI, 30, 40));
		GONDOR.addQuest(new LOTRMiniQuestKillFaction.QuestFactory("killMordorMany").setKillFaction(LOTRFaction.MORDOR, 60, 90));
		GONDOR.addQuest(new LOTRMiniQuestKillFaction.QuestFactory("killHarad").setKillFaction(LOTRFaction.NEAR_HARAD, 20, 30));
		
		MORDOR.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectWeapon").setCollectItem(new ItemStack(LOTRMod.scimitarOrc), 1, 5).setRewardFactor(3F));
		MORDOR.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectWeapon").setCollectItem(new ItemStack(LOTRMod.battleaxeOrc), 1, 5).setRewardFactor(3F));
		MORDOR.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectWeapon").setCollectItem(new ItemStack(LOTRMod.axeOrc), 1, 5).setRewardFactor(3F));
		MORDOR.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectWeapon").setCollectItem(new ItemStack(LOTRMod.daggerOrc), 1, 6).setRewardFactor(2F));
		MORDOR.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectFood").setCollectItem(new ItemStack(Items.cooked_porkchop), 2, 8).setRewardFactor(2F));
		MORDOR.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectFood").setCollectItem(new ItemStack(Items.cooked_beef), 2, 8).setRewardFactor(2F));
		MORDOR.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectFood").setCollectItem(new ItemStack(Items.bone), 2, 8).setRewardFactor(2F));
		MORDOR.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectArmour").setCollectItem(new ItemStack(LOTRMod.helmetOrc), 2, 5).setRewardFactor(3F));
		MORDOR.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectArmour").setCollectItem(new ItemStack(LOTRMod.bodyOrc), 2, 5).setRewardFactor(4F));
		MORDOR.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectArmour").setCollectItem(new ItemStack(LOTRMod.legsOrc), 2, 5).setRewardFactor(3F));
		MORDOR.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectArmour").setCollectItem(new ItemStack(LOTRMod.bootsOrc), 2, 5).setRewardFactor(3F));
		MORDOR.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectMineral").setCollectItem(new ItemStack(LOTRMod.orcSteel), 3, 10).setRewardFactor(2F));
		MORDOR.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectMineral").setCollectItem(new ItemStack(LOTRMod.nauriteGem), 4, 12).setRewardFactor(2F));
		MORDOR.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectMineral").setCollectItem(new ItemStack(LOTRMod.guldurilCrystal), 3, 6).setRewardFactor(4F));
		MORDOR.addQuest(new LOTRMiniQuestKillFaction.QuestFactory("killEnemy").setKillFaction(LOTRFaction.ROHAN, 10, 40));
		MORDOR.addQuest(new LOTRMiniQuestKillFaction.QuestFactory("killEnemy").setKillFaction(LOTRFaction.GONDOR, 10, 40));
		MORDOR.addQuest(new LOTRMiniQuestKillEntity.QuestFactory("killMen").setKillEntity(LOTREntityRohirrim.class, 10, 30));
		MORDOR.addQuest(new LOTRMiniQuestKillEntity.QuestFactory("killMen").setKillEntity(LOTREntityGondorSoldier.class, 10, 30));
		MORDOR.addQuest(new LOTRMiniQuestKillEntity.QuestFactory("killRanger").setKillEntity(LOTREntityRangerIthilien.class, 10, 30));
		
		NEAR_HARAD.addQuest(new LOTRMiniQuestCollect.QuestFactory("bringWater").setCollectItem(new ItemStack(Items.water_bucket), 3, 5).setRewardFactor(5F));
		NEAR_HARAD.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectDates").setCollectItem(new ItemStack(LOTRMod.date), 8, 15).setRewardFactor(2F));
		NEAR_HARAD.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectRangedWeapon").setCollectItem(new ItemStack(LOTRMod.nearHaradBow), 1, 3).setRewardFactor(5F));
		NEAR_HARAD.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectRangedWeapon").setCollectItem(new ItemStack(Items.arrow), 20, 40).setRewardFactor(0.5F));
		NEAR_HARAD.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectPoison").setCollectItem(new ItemStack(LOTRMod.bottlePoison), 2, 4).setRewardFactor(5F));
		NEAR_HARAD.addQuest(new LOTRMiniQuestCollect.QuestFactory("collectBlackRock").setCollectItem(new ItemStack(LOTRMod.rock, 1, 0), 30, 50).setRewardFactor(0.5F));
		NEAR_HARAD.addQuest(new LOTRMiniQuestKillEntity.QuestFactory("killGondorSoldier").setKillEntity(LOTREntityGondorSoldier.class, 10, 30));
		NEAR_HARAD.addQuest(new LOTRMiniQuestKillEntity.QuestFactory("killRanger").setKillEntity(LOTREntityRangerIthilien.class, 10, 30));
		NEAR_HARAD.addQuest(new LOTRMiniQuestKillFaction.QuestFactory("killGondor").setKillFaction(LOTRFaction.GONDOR, 20, 50));
	}
}
