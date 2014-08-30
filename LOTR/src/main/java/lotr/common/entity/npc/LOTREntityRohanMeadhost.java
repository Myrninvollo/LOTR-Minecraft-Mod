package lotr.common.entity.npc;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRAlignmentValues;
import lotr.common.LOTRFoods;
import lotr.common.LOTRLevelData;
import lotr.common.LOTRMod;
import lotr.common.entity.ai.LOTREntityAIAttackOnCollide;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTREntityRohanMeadhost extends LOTREntityRohanMan implements LOTRTradeable
{
	public LOTREntityRohanMeadhost(World world)
	{
		super(world);
		addTargetTasks(false);
		
		npcLocationName = "entity.lotr.RohanMeadhost.locationName";
		
		if (!worldObj.isRemote)
		{
			traderNPCInfo.setBuyTrades(LOTRTradeEntry.getRandomTrades(LOTRTradeEntry.ROHAN_MEADHOST_BUY, rand, true));
			traderNPCInfo.setSellTrades(LOTRTradeEntry.getRandomTrades(LOTRTradeEntry.ROHAN_MEADHOST_SELL, rand, false));
		}
	}
	
	@Override
	public EntityAIBase createRohanAttackAI()
	{
		return new LOTREntityAIAttackOnCollide(this, 1.3D, false);
	}
	
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.2D);
    }
	
	@Override
    public IEntityLivingData onSpawnWithEgg(IEntityLivingData data)
    {
		data = super.onSpawnWithEgg(data);
		setCurrentItemOrArmor(0, new ItemStack(LOTRMod.mugMead));
		return data;
    }

	@Override
	public int getAlignmentBonus()
	{
		return LOTRAlignmentValues.Bonuses.ROHAN_MEADHOST;
	}
	
	@Override
	protected void dropFewItems(boolean flag, int i)
	{
		super.dropFewItems(flag, i);
		
		int j = rand.nextInt(3) + rand.nextInt(i + 1);
		for (int k = 0; k < j; k++)
		{
			int l = rand.nextInt(11);
			switch(l)
			{
				case 0: case 1: case 2:
					Item food = LOTRFoods.ROHAN.getRandomFood(rand).getItem();
					entityDropItem(new ItemStack(food), 0F);
					break;
				case 3:
					entityDropItem(new ItemStack(Items.gold_nugget, 2 + rand.nextInt(3)), 0F);
					break;
				case 4:
					entityDropItem(new ItemStack(Items.wheat, 1 + rand.nextInt(4)), 0F);
					break;
				case 5:
					entityDropItem(new ItemStack(Items.sugar, 1 + rand.nextInt(3)), 0F);
					break;
				case 6:
					entityDropItem(new ItemStack(Items.paper, 1 + rand.nextInt(2)), 0F);
					break;
				case 7: case 8:
					entityDropItem(new ItemStack(LOTRMod.mug), 0F);
					break;
				case 9: case 10:
					Item drink = LOTRMod.mugMead;
					entityDropItem(new ItemStack(drink, 1, 1 + rand.nextInt(3)), 0F);
					break;
			}
		}
	}
	
	@Override
	public boolean canTradeWith(EntityPlayer entityplayer)
	{
		return isFriendly(entityplayer);
	}
	
	@Override
	public void onPlayerBuyItem(EntityPlayer entityplayer, ItemStack itemstack)
	{
		if (itemstack.getItem() == LOTRMod.mugMead)
		{
			LOTRLevelData.getData(entityplayer).addAchievement(LOTRAchievement.buyRohanMead);
		}
	}
	
	@Override
	public void onPlayerSellItem(EntityPlayer entityplayer, ItemStack itemstack) {}
	
	@Override
	public boolean shouldTraderRespawn()
	{
		return true;
	}
	
	@Override
	public String getSpeechBank(EntityPlayer entityplayer)
	{
		if (isFriendly(entityplayer))
		{
			return "rohanMeadhost_friendly";
		}
		else
		{
			return "rohanMeadhost_unfriendly";
		}
	}
}
