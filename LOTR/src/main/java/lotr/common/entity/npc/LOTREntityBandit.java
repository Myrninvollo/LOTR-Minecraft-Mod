package lotr.common.entity.npc;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRFaction;
import lotr.common.LOTRLevelData;
import lotr.common.LOTRMod;
import lotr.common.entity.ai.LOTREntityAIAttackOnCollide;
import lotr.common.entity.ai.LOTREntityAIBanditFlee;
import lotr.common.entity.ai.LOTREntityAIBanditSteal;
import lotr.common.entity.ai.LOTREntityAINearestAttackableTargetBandit;
import lotr.common.inventory.LOTRInventoryNPC;
import lotr.common.item.LOTRItemLeatherHat;
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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class LOTREntityBandit extends LOTREntityNPC
{
	public static int MAX_THEFTS = 3;
	
	public LOTRInventoryNPC banditInventory = new LOTRInventoryNPC("BanditInventory", this, MAX_THEFTS);
	
	public LOTREntityBandit(World world)
	{
		super(world);
		setSize(0.6F, 1.8F);
		getNavigator().setAvoidsWater(true);
		getNavigator().setBreakDoors(true);
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(1, new LOTREntityAIAttackOnCollide(this, 1.1D, false));
        tasks.addTask(2, new LOTREntityAIBanditSteal(this, 1.2D));
        tasks.addTask(3, new LOTREntityAIBanditFlee(this, 1.3D));
		tasks.addTask(4, new EntityAIOpenDoor(this, true));
        tasks.addTask(5, new EntityAIWander(this, 1D));
        tasks.addTask(6, new EntityAIWatchClosest2(this, EntityPlayer.class, 8F, 0.1F));
        tasks.addTask(6, new EntityAIWatchClosest2(this, LOTREntityNPC.class, 5F, 0.05F));
        tasks.addTask(7, new EntityAIWatchClosest(this, EntityLiving.class, 8F, 0.02F));
        tasks.addTask(8, new EntityAILookIdle(this));
        targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        targetTasks.addTask(2, new LOTREntityAINearestAttackableTargetBandit(this, EntityPlayer.class, 0, true));
	}
	
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20D);
		getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(40D);
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.3D);
    }
	
	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData data)
	{
		data = super.onSpawnWithEgg(data);
		
		if (rand.nextInt(3) == 0)
		{
			ItemStack hat = new ItemStack(LOTRMod.leatherHat);
			LOTRItemLeatherHat.setHatColor(hat, LOTRItemLeatherHat.HAT_BLACK);
			LOTRItemLeatherHat.setFeatherColor(hat, LOTRItemLeatherHat.FEATHER_WHITE);
			setCurrentItemOrArmor(4, hat);
		}
		
		if (rand.nextBoolean())
		{
			setCurrentItemOrArmor(0, new ItemStack(LOTRMod.daggerBronze));
		}
		else
		{
			setCurrentItemOrArmor(0, new ItemStack(LOTRMod.daggerIron));
		}
		
		return data;
	}
	
	@Override
	public int getTotalArmorValue()
	{
		return 10;
	}
	
	@Override
    public boolean isAIEnabled()
    {
        return true;
    }
	
	@Override
	public LOTRFaction getFaction()
	{
		return LOTRFaction.BANDIT;
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound nbt)
	{
		super.writeEntityToNBT(nbt);
		banditInventory.writeToNBT(nbt);
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound nbt)
	{
		super.readEntityFromNBT(nbt);
		banditInventory.readFromNBT(nbt);
	}
	
	@Override
	protected void dropFewItems(boolean flag, int i)
	{
		super.dropFewItems(flag, i);
		
		int j = rand.nextInt(2) + rand.nextInt(i + 1);
		for (int k = 0; k < j; k++)
		{
			dropItem(Items.bone, 1);
		}
		
		int coins = 10 + rand.nextInt(10) + rand.nextInt((i + 1) * 10);
		for (int k = 0; k < coins; k++)
		{
			dropItem(LOTRMod.silverCoin, 1);
		}
	}

	@Override
	public void onDeath(DamageSource damagesource)
	{
		super.onDeath(damagesource);
		
		if (!worldObj.isRemote && damagesource.getEntity() instanceof EntityPlayer && !LOTRMod.isInventoryEmpty(banditInventory))
		{
			EntityPlayer entityplayer = (EntityPlayer)damagesource.getEntity();
			LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.killThievingBandit);
		}
		
		if (!worldObj.isRemote)
		{
			banditInventory.dropAllItems();
		}
	}
	
	@Override
	public String getSpeechBank(EntityPlayer entityplayer)
	{
		return "bandit";
	}
}
