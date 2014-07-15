package lotr.common.entity.npc;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRAlignmentValues;
import lotr.common.LOTRLevelData;
import lotr.common.LOTRMod;
import lotr.common.entity.ai.LOTREntityAIAttackOnCollide;
import lotr.common.world.biome.LOTRBiome;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTREntityElvenTrader extends LOTREntityElf implements LOTRTradeable, LOTRTravellingTrader
{
	public LOTRTravellingTraderInfo travellingTraderInfo = new LOTRTravellingTraderInfo(this);
	
	public LOTREntityElvenTrader(World world)
	{
		super(world);
		tasks.addTask(2, new LOTREntityAIAttackOnCollide(this, 1.6D, false));
		targetTasks.taskEntries.clear();
		targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
		
		if (!worldObj.isRemote)
		{
			traderNPCInfo.setBuyTrades(LOTRTradeEntry.getRandomTrades(LOTRTradeEntry.ELVEN_TRADER_BUY, rand, true));
			traderNPCInfo.setSellTrades(LOTRTradeEntry.getRandomTrades(LOTRTradeEntry.ELVEN_TRADER_SELL, rand, false));
		}
	}
	
	@Override
	public void startVisiting(EntityPlayer entityplayer)
	{
		travellingTraderInfo.startVisiting(entityplayer);
	}
	
	@Override
	public LOTREntityNPC createTravellingEscort()
	{
		return new LOTREntityElf(worldObj);
	}
	
	@Override
	public String getDepartureSpeech()
	{
		return "elvenTrader_departure";
	}

	@Override
    public int getTotalArmorValue()
    {
        return 10;
    }
	
	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		
		if (isEntityAlive())
		{
			travellingTraderInfo.onUpdate();
			
			if (!worldObj.isRemote)
			{
				if (travellingTraderInfo.timeUntilDespawn == 0)
				{
					worldObj.setEntityState(this, (byte)15);
				}
			}
		}
	}
	
	@Override
	public Item getElfSwordId()
	{
		return LOTRMod.daggerElven;
	}
	
	@Override
	public int getAlignmentBonus()
	{
		return LOTRAlignmentValues.GALADHRIM_TRADER_BONUS;
	}
	
	@Override
	protected int getExperiencePoints(EntityPlayer entityplayer)
    {
        return 5 + rand.nextInt(3);
    }
	
	@Override
	public void onDeath(DamageSource damagesource)
	{
		super.onDeath(damagesource);
		travellingTraderInfo.onDeath();
		if (!worldObj.isRemote)
		{
			worldObj.setEntityState(this, (byte)15);
		}
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void handleHealthUpdate(byte b)
    {
        if (b == 15)
        {
            for (int i = 0; i < 16; i++)
			{
				double d = posX + (rand.nextDouble() - 0.5D) * (double)width;
				double d1 = posY + rand.nextDouble() * (double)height;
				double d2 = posZ + (rand.nextDouble() - 0.5D) * (double)width;
				double d3 = -0.2D + (double)(rand.nextFloat() * 0.4F);
				double d4 = -0.2D + (double)(rand.nextFloat() * 0.4F);
				double d5 = -0.2D + (double)(rand.nextFloat() * 0.4F);
				LOTRMod.proxy.spawnParticle("leafGold_" + (30 + rand.nextInt(30)), d, d1, d2, d3, d4, d5);
			}
        }
        else
        {
            super.handleHealthUpdate(b);
        }
    }
	
	@Override
	public void writeEntityToNBT(NBTTagCompound nbt)
	{
		super.writeEntityToNBT(nbt);
		travellingTraderInfo.writeToNBT(nbt);
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound nbt)
	{
		super.readEntityFromNBT(nbt);
		travellingTraderInfo.readFromNBT(nbt);
	}
	
	@Override
	public boolean canTradeWith(EntityPlayer entityplayer)
	{
		return LOTRLevelData.getAlignment(entityplayer, getFaction()) >= LOTRAlignmentValues.ELVEN_TRADER_TRADE && isFriendly(entityplayer);
	}
	
	@Override
	public void onPlayerBuyItem(EntityPlayer entityplayer, ItemStack itemstack)
	{
		LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.tradeElvenTrader);
	}
	
	@Override
	public void onPlayerSellItem(EntityPlayer entityplayer, ItemStack itemstack)
	{
		LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.tradeElvenTrader);
	}
	
	@Override
	public boolean shouldTraderRespawn()
	{
		return false;
	}
	
	@Override
	public boolean shouldRenderHair()
	{
		return false;
	}
	
	@Override
	public String getSpeechBank(EntityPlayer entityplayer)
	{
		if (isFriendly(entityplayer))
		{
			if (canTradeWith(entityplayer))
			{
				return "elvenTrader_friendly";
			}
			else
			{
				return "elvenTrader_neutral";
			}
		}
		else
		{
			return "elvenTrader_hostile";
		}
	}
}
