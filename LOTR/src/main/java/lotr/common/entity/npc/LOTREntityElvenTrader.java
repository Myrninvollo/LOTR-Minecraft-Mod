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
	protected void entityInit()
	{
		super.entityInit();
		dataWatcher.addObject(20, Integer.valueOf(0));
		dataWatcher.addObject(21, Integer.valueOf(0));
	}
	
	private int getEatingTick()
	{
		return dataWatcher.getWatchableObjectInt(20);
	}
	
	private void setEatingTick(int i)
	{
		dataWatcher.updateObject(20, Integer.valueOf(i));
	}
	
	private int getDrinkingTick()
	{
		return dataWatcher.getWatchableObjectInt(21);
	}
	
	private void setDrinkingTick(int i)
	{
		dataWatcher.updateObject(21, Integer.valueOf(i));
	}
	
	@Override
    public int getTotalArmorValue()
    {
        return 10;
    }
	
	@Override
	public void onElfUpdate()
	{
		if (isEntityAlive())
		{
			travellingTraderInfo.onUpdate();
			
			if (!worldObj.isRemote)
			{
				if (travellingTraderInfo.timeUntilDespawn == 0)
				{
					worldObj.setEntityState(this, (byte)15);
				}
			
				ItemStack heldItem = getEquipmentInSlot(0);
				if (getAttackTarget() != null)
				{
					if (heldItem == null || heldItem.getItem() != getElfSwordId())
					{
						setCurrentItemOrArmor(0, new ItemStack(getElfSwordId(), 1, 0));
						setEatingTick(0);
						setDrinkingTick(0);
						weaponChangeCooldown = 20;
					}
				}
				else
				{
					if (heldItem != null && heldItem.getItem() == getElfSwordId())
					{
						if (weaponChangeCooldown > 0)
						{
							weaponChangeCooldown--;
						}
						else
						{
							setCurrentItemOrArmor(0, null);
						}
					}
				}
				
				if (getHealth() < getMaxHealth() && getAttackTarget() == null && rand.nextInt(80) == 0 && getEquipmentInSlot(0) == null)
				{
					if (rand.nextBoolean())
					{
						setCurrentItemOrArmor(0, new ItemStack(LOTRMod.lembas));
						setEatingTick(20);
					}
					else
					{
						setCurrentItemOrArmor(0, new ItemStack(LOTRMod.mugMiruvor));
						setDrinkingTick(20);
					}
				}
			}
			
			if (getEatingTick() > 0)
			{
				if (getEatingTick() % 4 == 0)
				{
					ItemStack itemstack = getEquipmentInSlot(0);
					if (itemstack != null)
					{
						for (int i = 0; i < 5; i++)
						{
							Vec3 vec1 = Vec3.createVectorHelper(((double)rand.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);
							vec1.rotateAroundX(-rotationPitch * (float)Math.PI / 180F);
							vec1.rotateAroundY(-rotationYaw * (float)Math.PI / 180F);
							Vec3 vec2 = Vec3.createVectorHelper(((double)rand.nextFloat() - 0.5D) * 0.3D, (double)(-rand.nextFloat()) * 0.6D - 0.3D, 0.6D);
							vec2.rotateAroundX(-rotationPitch * (float)Math.PI / 180F);
							vec2.rotateAroundY(-rotationYaw * (float)Math.PI / 180F);
							vec2 = vec2.addVector(posX, posY + (double)getEyeHeight(), posZ);
							worldObj.spawnParticle("iconcrack_" + Item.getIdFromItem(itemstack.getItem()), vec2.xCoord, vec2.yCoord, vec2.zCoord, vec1.xCoord, vec1.yCoord + 0.05D, vec1.zCoord);
						}
					}
					
					playSound("random.eat", 0.5F + 0.5F * (float)rand.nextInt(2), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1F);
				}
				
				if (!worldObj.isRemote)
				{
					setEatingTick(getEatingTick() - 1);
				}
				
				if (getEatingTick() == 0)
				{
					setCurrentItemOrArmor(0, null);
					heal(10F);
				}
			}
			
			if (getDrinkingTick() > 0)
			{
				if (getDrinkingTick() % 4 == 0)
				{	
					playSound("random.drink", 0.5F, rand.nextFloat() * 0.1F + 0.9F);
				}
				
				if (!worldObj.isRemote)
				{
					setDrinkingTick(getDrinkingTick() - 1);
				}
				
				if (getDrinkingTick() == 0)
				{
					setCurrentItemOrArmor(0, null);
					heal(6F);
					if (!worldObj.isRemote)
					{
						addPotionEffect(new PotionEffect(Potion.damageBoost.id, 200));
					}
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
		nbt.setInteger("ElfEating", getEatingTick());
		nbt.setInteger("ElfDrinking", getDrinkingTick());
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound nbt)
	{
		super.readEntityFromNBT(nbt);
		travellingTraderInfo.readFromNBT(nbt);
		setEatingTick(nbt.getInteger("ElfEating"));
		setDrinkingTick(nbt.getInteger("ElfDrinking"));
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
