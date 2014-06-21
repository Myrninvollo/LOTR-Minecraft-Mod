package lotr.common.entity.npc;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRAlignmentValues;
import lotr.common.LOTRLevelData;
import lotr.common.LOTRMod;
import lotr.common.entity.ai.LOTREntityAIAttackOnCollide;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWatchClosest2;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTREntityGondorBlacksmith extends LOTREntityGondorMan implements LOTRTradeable
{
	public LOTREntityGondorBlacksmith(World world)
	{
		super(world);
		getNavigator().setAvoidsWater(true);
		getNavigator().setBreakDoors(true);
        tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(1, new LOTREntityAIAttackOnCollide(this, 1.25D, false));
		tasks.addTask(2, new EntityAIOpenDoor(this, true));
        tasks.addTask(3, new EntityAIWander(this, 1D));
        tasks.addTask(4, new EntityAIWatchClosest2(this, EntityPlayer.class, 8F, 0.1F));
        tasks.addTask(4, new EntityAIWatchClosest2(this, LOTREntityNPC.class, 5F, 0.02F));
        tasks.addTask(5, new EntityAIWatchClosest(this, EntityLiving.class, 8F, 0.02F));
        tasks.addTask(6, new EntityAILookIdle(this));
		targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
		
		if (!worldObj.isRemote)
		{
			traderNPCInfo.setBuyTrades(LOTRTradeEntry.getRandomTrades(LOTRTradeEntry.GONDOR_BLACKSMITH_BUY, rand, true));
			traderNPCInfo.setSellTrades(LOTRTradeEntry.getRandomTrades(LOTRTradeEntry.GONDOR_BLACKSMITH_SELL, rand, false));
		}
	}
	
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(25D);
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.2D);
    }
	
	@Override
    public IEntityLivingData onSpawnWithEgg(IEntityLivingData data)
    {
		data = super.onSpawnWithEgg(data);
		setCurrentItemOrArmor(0, new ItemStack(LOTRMod.blacksmithHammer));
		return data;
    }

	@Override
	public int getAlignmentBonus()
	{
		return LOTRAlignmentValues.GONDOR_BLACKSMITH_BONUS;
	}
	
	@Override
	protected void dropFewItems(boolean flag, int i)
	{
		super.dropFewItems(flag, i);

		dropItem(Items.iron_ingot, 1 + rand.nextInt(3) + rand.nextInt(i + 1));
	}
	
	@Override
	public boolean canTradeWith(EntityPlayer entityplayer)
	{
		return LOTRLevelData.getAlignment(entityplayer, getFaction()) >= LOTRAlignmentValues.GONDOR_BLACKSMITH_TRADE && isFriendly(entityplayer);
	}
	
	@Override
	public void onPlayerBuyItem(EntityPlayer entityplayer, ItemStack itemstack)
	{
		LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.tradeGondorBlacksmith);
	}
	
	@Override
	public void onPlayerSellItem(EntityPlayer entityplayer, ItemStack itemstack)
	{
		LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.tradeGondorBlacksmith);
	}
	
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
			if (canTradeWith(entityplayer))
			{
				return "gondorBlacksmith_friendly";
			}
			else
			{
				return "gondorBlacksmith_neutral";
			}
		}
		else
		{
			return "gondorBlacksmith_hostile";
		}
	}
}
