package lotr.common.entity.ai;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import lotr.common.LOTRMod;
import lotr.common.entity.npc.LOTREntityBandit;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.MathHelper;

public class LOTREntityAIBanditSteal extends EntityAIBase
{
	private static List valuableItems = new ArrayList();
	static
	{
		for (ToolMaterial material : ToolMaterial.values())
		{
			if (material.getHarvestLevel() >= 2)
			{
				valuableItems.add(material.func_150995_f());
			}
		}
	}
	
    private LOTREntityBandit theBandit;
    private EntityPlayer targetPlayer;
    private double speed;
    private int chaseTimer;
    private int rePathDelay;

    public LOTREntityAIBanditSteal(LOTREntityBandit bandit, double d)
    {
    	theBandit = bandit;
        speed = d;
        setMutexBits(3);
    }

    @Override
    public boolean shouldExecute()
    {
    	if (!LOTRMod.isInventoryEmpty(theBandit.banditInventory))
        {
            return false;
        }
        else
        {
            List players = theBandit.worldObj.getEntitiesWithinAABB(EntityPlayer.class, theBandit.boundingBox.expand(32D, 32D, 32D));
            List validTargets = new ArrayList();

            for (int i = 0; i < players.size(); i++)
            {
            	EntityPlayer entityplayer = (EntityPlayer)players.get(i);
            	if (entityplayer.capabilities.isCreativeMode)
            	{
            		continue;
            	}
            	if (LOTRMod.isInventoryEmpty(entityplayer.inventory))
            	{
            		continue;
            	}
            	validTargets.add(entityplayer);
            }
            
            if (validTargets.isEmpty())
            {
            	return false;
            }
            
            targetPlayer = (EntityPlayer)validTargets.get(theBandit.getRNG().nextInt(validTargets.size()));
            return true;
        }
    }
    
    @Override
    public void startExecuting()
    {
    	chaseTimer = 600;
    }
    
    @Override
    public void updateTask()
    {
    	chaseTimer--;
    	theBandit.getLookHelper().setLookPositionWithEntity(targetPlayer, 30F, 30F);
    	
    	rePathDelay--;
		if (rePathDelay <= 0)
		{
			rePathDelay = 10;
			theBandit.getNavigator().tryMoveToEntityLiving(targetPlayer, speed);
		}
		
		if (theBandit.getDistanceSqToEntity(targetPlayer) <= 2D)
		{
			chaseTimer = 0;
			steal();
		}
    }
    
    @Override
    public boolean continueExecuting()
    {
    	if (targetPlayer == null || !targetPlayer.isEntityAlive() || targetPlayer.capabilities.isCreativeMode || LOTRMod.isInventoryEmpty(targetPlayer.inventory))
    	{
    		return false;
    	}
        return chaseTimer > 0 && theBandit.getDistanceSqToEntity(targetPlayer) < 256D;
    }
    
    @Override
    public void resetTask()
    {
    	chaseTimer = 0;
    	rePathDelay = 0;
    	targetPlayer = null;
    }

    private void steal()
    {
    	IInventory inv = targetPlayer.inventory;
    	int thefts = MathHelper.getRandomIntegerInRange(theBandit.getRNG(), 1, LOTREntityBandit.MAX_THEFTS);
    	boolean stolenSomething = false;
    	
    	for (int i = 0; i < thefts; i++)
    	{
    		if (tryStealItem(inv, LOTRMod.silverCoin))
    		{
    			stolenSomething = true;
    		}
    		else if (tryStealItem(inv, valuableItems))
    		{
    			stolenSomething = true;
    		}
    		else if (tryStealItem(inv, LOTRMod.pouch))
    		{
    			stolenSomething = true;
    		}
    		else if (tryStealItem(inv, ItemArmor.class))
    		{
    			stolenSomething = true;
    		}
    		else if (tryStealItem(inv, ItemSword.class))
    		{
    			stolenSomething = true;
    		}
    		else if (tryStealItem(inv, ItemTool.class))
    		{
    			stolenSomething = true;
    		}
    		else if (tryStealItem(inv))
    		{
    			stolenSomething = true;
    		}
    	}
    	
    	if (stolenSomething)
		{
    		targetPlayer.addChatMessage(new ChatComponentTranslation("chat.lotr.banditSteal"));
    		theBandit.playSound("mob.horse.leather", 0.5F, 1F);
		}
    }
    
    private boolean tryStealItem(IInventory inv, final Item item)
    {
    	return tryStealItem_do(inv, new BanditItemFilter()
    	{
    		public boolean isApplicable(ItemStack itemstack)
    		{
    			return itemstack.getItem() == item;
    		}
    	});
    }
    
    private boolean tryStealItem(IInventory inv, final Class itemclass)
    {
    	return tryStealItem_do(inv, new BanditItemFilter()
    	{
    		public boolean isApplicable(ItemStack itemstack)
    		{
    			return itemclass.isAssignableFrom(itemstack.getItem().getClass());
    		}
    	});
    }
    
    private boolean tryStealItem(IInventory inv, final List itemList)
    {
    	return tryStealItem_do(inv, new BanditItemFilter()
    	{
    		public boolean isApplicable(ItemStack itemstack)
    		{
    			return itemList.contains(itemstack.getItem());
    		}
    	});
    }
    
    private boolean tryStealItem(IInventory inv)
    {
    	return tryStealItem_do(inv, new BanditItemFilter()
    	{
    		public boolean isApplicable(ItemStack itemstack)
    		{
    			return true;
    		}
    	});
    }
    
    private boolean tryStealItem_do(IInventory inv, BanditItemFilter filter)
    {
    	Integer[] inventoryIndices = new Integer[inv.getSizeInventory()];
		for (int l = 0; l < inventoryIndices.length; l++)
		{
			inventoryIndices[l] = l;
		}
		List<Integer> indicesAsList = Arrays.asList(inventoryIndices);
		Collections.shuffle(indicesAsList);
		inventoryIndices = indicesAsList.toArray(inventoryIndices);
		
		for (int index : inventoryIndices)
    	{
			ItemStack itemstack = inv.getStackInSlot(index);
			if (itemstack == null)
			{
				continue;
			}
			
			if (filter.isApplicable(itemstack))
			{
				stealItem(inv, index);
				return true;
			}
    	}
		
		return false;
    }
    
    private int getRandomTheftAmount(ItemStack itemstack)
    {
    	float f = (float)itemstack.stackSize * (0.25F + theBandit.getRNG().nextFloat() * 0.5F);
    	return Math.max(1, MathHelper.ceiling_float_int(f));
    }
    
    private void stealItem(IInventory inventoryPlayer, int index)
    {
    	ItemStack playerItem = inventoryPlayer.getStackInSlot(index);
    	int theft = getRandomTheftAmount(playerItem);
    	
    	int banditSlot = 0;
    	while (theBandit.banditInventory.getStackInSlot(banditSlot) != null)
    	{
    		banditSlot++;
    	}
    	ItemStack stolenItem = playerItem.copy();
    	stolenItem.stackSize = theft;
    	theBandit.banditInventory.setInventorySlotContents(banditSlot, stolenItem);
    	
    	playerItem.stackSize -= theft;
    	if (playerItem.stackSize <= 0)
    	{
    		inventoryPlayer.setInventorySlotContents(index, null);
    	}
    }
    
    private abstract class BanditItemFilter
    {
    	public abstract boolean isApplicable(ItemStack itemstack);
    }
}
