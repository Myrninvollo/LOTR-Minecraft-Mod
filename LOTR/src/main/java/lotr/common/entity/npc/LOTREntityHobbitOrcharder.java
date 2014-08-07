package lotr.common.entity.npc;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRAlignmentValues;
import lotr.common.LOTRLevelData;
import lotr.common.LOTRMod;
import lotr.common.entity.ai.LOTREntityAIAttackOnCollide;
import lotr.common.item.LOTRItemLeatherHat;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTREntityHobbitOrcharder extends LOTREntityHobbit implements LOTRTradeable
{
	public LOTREntityHobbitOrcharder(World world)
	{
		super(world);
		removeTasksOfType(EntityAIPanic.class);
		tasks.addTask(2, new LOTREntityAIAttackOnCollide(this, 1.2D, false));
		targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
		isNPCPersistent = false;

		if (!worldObj.isRemote)
		{
			traderNPCInfo.setBuyTrades(LOTRTradeEntry.getRandomTrades(LOTRTradeEntry.HOBBIT_ORCHARDER_BUY, rand, true));
			traderNPCInfo.setSellTrades(LOTRTradeEntry.getRandomTrades(LOTRTradeEntry.HOBBIT_ORCHARDER_SELL, rand, false));
		}
	}
	
	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData data)
	{
		data = super.onSpawnWithEgg(data);
		ItemStack hat = new ItemStack(LOTRMod.leatherHat);
		LOTRItemLeatherHat.setHatColor(hat, 0x49872F);
		setCurrentItemOrArmor(4, hat);
		
		int i = rand.nextInt(3);
		
		if (i == 0)
		{
			setCurrentItemOrArmor(0, new ItemStack(Items.iron_axe));
		}
		else if (i == 1)
		{
			setCurrentItemOrArmor(0, new ItemStack(Items.stone_axe));
		}
		else if (i == 2)
		{
			setCurrentItemOrArmor(0, new ItemStack(LOTRMod.axeBronze));
		}
		
		return data;
	}

	@Override
	public int getAlignmentBonus()
	{
		return LOTRAlignmentValues.HOBBIT_ORCHARDER_BONUS;
	}
	
	@Override
	public boolean canTradeWith(EntityPlayer entityplayer)
	{
		return isFriendly(entityplayer);
	}
	
	@Override
	public void onPlayerBuyItem(EntityPlayer entityplayer, ItemStack itemstack)
	{
		if (itemstack.getItem() instanceof ItemFood)
		{
			LOTRLevelData.getData(entityplayer).addAchievement(LOTRAchievement.buyOrcharderFood);
		}
	}
	
	@Override
	public void onPlayerSellItem(EntityPlayer entityplayer, ItemStack itemstack) {}
	
	@Override
	public boolean shouldTraderRespawn()
	{
		return false;
	}
	
	@Override
	public String getSpeechBank(EntityPlayer entityplayer)
	{
		if (isFriendly(entityplayer))
		{
			return "hobbitOrcharder_friendly";
		}
		else
		{
			return "hobbit_unfriendly";
		}
	}
}
