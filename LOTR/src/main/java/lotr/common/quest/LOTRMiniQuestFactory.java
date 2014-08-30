package lotr.common.quest;

import java.util.*;

import lotr.common.LOTRMod;
import lotr.common.quest.LOTRMiniQuest.QuestFactoryBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.FMLLog;

public enum LOTRMiniQuestFactory
{
	HOBBIT("hobbit"),
	GALADHRIM("galadhrim");
	
	private static Random rand = new Random();
	
	private List<QuestFactoryBase> questFactories = new ArrayList();
	private String baseName;
	
	private LOTRMiniQuestFactory(String s)
	{
		baseName = s;
	}

	public void addQuest(QuestFactoryBase factory)
	{
		factory.setQuestBaseName(baseName);
		questFactories.add(factory);
	}

	public LOTRMiniQuest createQuest(EntityPlayer entityplayer)
	{
		QuestFactoryBase factory = questFactories.get(rand.nextInt(questFactories.size()));
		LOTRMiniQuest quest = factory.createQuest(entityplayer, rand);
		return quest;
	}
	
	public static void createMiniQuests()
	{
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
	}
}
