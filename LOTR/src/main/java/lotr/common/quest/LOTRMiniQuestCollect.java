package lotr.common.quest;

import java.util.Random;

import lotr.common.LOTRPlayerData;
import lotr.common.entity.npc.LOTREntityNPC;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;

public class LOTRMiniQuestCollect extends LOTRMiniQuest
{
	public ItemStack collectItem;
	public int collectTarget;
	public int amountGiven;
	public float rewardFactor;
	public static float defaultRewardFactor = 1F;
	
	public LOTRMiniQuestCollect(LOTRPlayerData pd)
	{
		super(pd);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		
		if (collectItem != null)
		{
			NBTTagCompound itemData = new NBTTagCompound();
			collectItem.writeToNBT(itemData);
			nbt.setTag("Item", itemData);
		}
		nbt.setInteger("Target", collectTarget);
		nbt.setInteger("Given", amountGiven);
		nbt.setFloat("RewardFactor", rewardFactor);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		
		if (nbt.hasKey("Item"))
		{
			NBTTagCompound itemData = nbt.getCompoundTag("Item");
			collectItem = ItemStack.loadItemStackFromNBT(itemData);
		}
		collectTarget = nbt.getInteger("Target");
		amountGiven = nbt.getInteger("Given");
		if (nbt.hasKey("RewardFactor"))
		{
			rewardFactor = nbt.getFloat("RewardFactor");
		}
		else
		{
			rewardFactor = defaultRewardFactor;
		}
	}
	
	@Override
	public boolean isValidQuest()
	{
		return super.isValidQuest() && collectItem != null && collectTarget > 0;
	}
	
	@Override
	public String getQuestObjective()
	{
		return StatCollector.translateToLocalFormatted("lotr.miniquest.collect", new Object[] {collectTarget, collectItem.getDisplayName()});
	}
	
	@Override
	public String getObjectiveInSpeech()
	{
		return (collectTarget - amountGiven) + " " + collectItem.getDisplayName();
	}

	@Override
	public String getQuestProgress()
	{
		return StatCollector.translateToLocalFormatted("lotr.miniquest.collect.progress", new Object[] {amountGiven, collectTarget});
	}
	
	@Override
	public void onInteract(EntityPlayer entityplayer, LOTREntityNPC npc)
	{
		for (int slot = 0; slot < entityplayer.inventory.mainInventory.length; slot++)
		{
			ItemStack itemstack = entityplayer.inventory.mainInventory[slot];
			if (itemstack == null)
			{
				continue;
			}
				
			if (collectItem.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(collectItem, itemstack))
			{
				int amountRemaining = collectTarget - amountGiven;
				
				if (itemstack.stackSize >= amountRemaining)
				{
					itemstack.stackSize -= amountRemaining;
					if (itemstack.stackSize <= 0)
					{
						itemstack = null;
					}
					entityplayer.inventory.setInventorySlotContents(slot, itemstack);
					amountGiven += amountRemaining;
				}
				else
				{
					amountGiven += itemstack.stackSize;
					entityplayer.inventory.setInventorySlotContents(slot, null);
				}
			}
			
			if (amountGiven >= collectTarget)
			{
				complete(entityplayer, npc);
				break;
			}
		}
	}
	
	@Override
	public int getAlignmentBonus()
	{
		float f = (float)collectTarget;
		f *= rewardFactor;
		int bonus = Math.round(f);
		return Math.max(bonus, 1);
	}

	public static class QuestFactory extends QuestFactoryBase
	{
		private ItemStack collectItem;
		private int minTarget;
		private int maxTarget;
		private float rewardFactor = defaultRewardFactor;
		
		public QuestFactory(String name)
		{
			super(name);
		}
		
		public QuestFactory setCollectItem(ItemStack itemstack, int min, int max)
		{
			collectItem = itemstack;
			minTarget = min;
			maxTarget = max;
			return this;
		}
		
		public QuestFactory setRewardFactor(float f)
		{
			rewardFactor = f;
			return this;
		}

		@Override
		public LOTRMiniQuest createQuest(EntityPlayer entityplayer, Random rand)
		{
			LOTRMiniQuestCollect quest = createQuestBase(LOTRMiniQuestCollect.class, entityplayer);
			quest.collectItem = collectItem.copy();
			quest.collectTarget = MathHelper.getRandomIntegerInRange(rand, minTarget, maxTarget);
			quest.rewardFactor = rewardFactor;
			return quest;
		}
	}
}
