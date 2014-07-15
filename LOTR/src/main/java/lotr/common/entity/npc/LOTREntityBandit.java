package lotr.common.entity.npc;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRLevelData;
import lotr.common.LOTRMod;
import lotr.common.entity.ai.LOTREntityAIAttackOnCollide;
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
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class LOTREntityBandit extends LOTREntityNPC
{
	public static int MAX_THEFTS = 9;
	
	private LOTRInventoryNPC banditInventory = new LOTRInventoryNPC("BanditInventory", this, MAX_THEFTS);
	
	public LOTREntityBandit(World world)
	{
		super(world);
		setSize(0.6F, 1.8F);
		getNavigator().setAvoidsWater(true);
		getNavigator().setBreakDoors(true);
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(1, new LOTREntityAIAttackOnCollide(this, 1.5D, false));
		//tasks.addTask(2, new LOTREntityAIBandit(this, 1.5D));
		tasks.addTask(3, new EntityAIOpenDoor(this, true));
        tasks.addTask(4, new EntityAIWander(this, 1D));
        tasks.addTask(5, new EntityAIWatchClosest2(this, EntityPlayer.class, 8F, 0.1F));
        tasks.addTask(5, new EntityAIWatchClosest2(this, LOTREntityNPC.class, 5F, 0.05F));
        tasks.addTask(6, new EntityAIWatchClosest(this, EntityLiving.class, 8F, 0.02F));
        tasks.addTask(7, new EntityAILookIdle(this));
        targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
	}
	
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20D);
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
		
		int coins = 10 + rand.nextInt(30) + rand.nextInt(i * 5);
		for (int k = 0; k < coins; k++)
		{
			dropItem(LOTRMod.silverCoin, 1);
		}
		
		int drops = 1 + rand.nextInt(3) + rand.nextInt(i + 1);
		for (int k = 0; k < drops; k++)
		{
			int randomDrop = rand.nextInt(4);
			switch (randomDrop)
			{
				case 0:
					entityDropItem(new ItemStack(Items.bread, 1 + rand.nextInt(3)), 0F);
					break;
				case 1:
					entityDropItem(new ItemStack(LOTRMod.mugAle, 1, rand.nextInt(5)), 0F);
					break;
				case 2: case 3:
					entityDropItem(new ItemStack(Items.bread, 1 + rand.nextInt(3)), 0F);
					break;
			}
		}
	}

	@Override
	public void onDeath(DamageSource damagesource)
	{
		super.onDeath(damagesource);
		
		if (!worldObj.isRemote)
		{
			banditInventory.dropAllItems();
		}
		
		if (!worldObj.isRemote) // && hasStolenStuff && damagesource.getEntity() instanceof EntityPlayer)
		{
			EntityPlayer entityplayer = (EntityPlayer)damagesource.getEntity();
			LOTRLevelData.addAchievement(entityplayer, LOTRAchievement.killThievingBandit);
		}
	}
	
	@Override
	public String getSpeechBank(EntityPlayer entityplayer)
	{
		return "bandit";
	}
}
