package lotr.common.entity.npc;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRAlignmentValues;
import lotr.common.LOTRFaction;
import lotr.common.LOTRFoods;
import lotr.common.LOTRMod;
import lotr.common.entity.ai.LOTREntityAIAttackOnCollide;
import lotr.common.entity.ai.LOTREntityAIFollowHiringPlayer;
import lotr.common.entity.ai.LOTREntityAIHiredRemainStill;
import lotr.common.entity.ai.LOTREntityAIHiringPlayerHurtByTarget;
import lotr.common.entity.ai.LOTREntityAIHiringPlayerHurtTarget;
import lotr.common.entity.ai.LOTREntityAINearestAttackableTargetBasic;
import lotr.common.world.biome.LOTRBiomeGenDunland;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWatchClosest2;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

public class LOTREntityDunlending extends LOTREntityNPC
{
	public LOTREntityDunlending(World world)
	{
		super(world);
		setSize(0.6F, 1.8F);
		getNavigator().setAvoidsWater(true);
		getNavigator().setBreakDoors(true);
        tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(1, new LOTREntityAIHiredRemainStill(this));
		tasks.addTask(2, getDunlendingAttackAI());
		tasks.addTask(3, new LOTREntityAIFollowHiringPlayer(this));
		tasks.addTask(4, new EntityAIOpenDoor(this, true));
        tasks.addTask(5, new EntityAIWander(this, 1D));
        tasks.addTask(6, new EntityAIWatchClosest2(this, EntityPlayer.class, 8F, 0.1F));
        tasks.addTask(6, new EntityAIWatchClosest2(this, LOTREntityNPC.class, 5F, 0.02F));
        tasks.addTask(7, new EntityAIWatchClosest(this, EntityLiving.class, 8F, 0.02F));
        tasks.addTask(8, new EntityAILookIdle(this));
        targetTasks.addTask(1, new LOTREntityAIHiringPlayerHurtByTarget(this));
        targetTasks.addTask(2, new LOTREntityAIHiringPlayerHurtTarget(this));
        targetTasks.addTask(3, new EntityAIHurtByTarget(this, false));
		targetTasks.addTask(4, new LOTREntityAINearestAttackableTargetBasic(this, LOTREntityRohirrim.class, 0, true));
        addTargetTasks(4);
	}
	
	public EntityAIBase getDunlendingAttackAI()
	{
		return new LOTREntityAIAttackOnCollide(this, 1.4D, false);
	}
	
	@Override
	public void entityInit()
	{
		super.entityInit();
		dataWatcher.addObject(16, LOTRNames.getRandomRohanName(rand));
	}
	
	public String getDunlendingName()
	{
		return dataWatcher.getWatchableObjectString(16);
	}
	
	public void setDunlendingName(String name)
	{
		dataWatcher.updateObject(16, name);
	}
	
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20D);
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.22D);
    }
	
	@Override
    public IEntityLivingData onSpawnWithEgg(IEntityLivingData data)
    {
		data = super.onSpawnWithEgg(data);
		
		int i = rand.nextInt(8);
		if (i == 0 || i == 1)
		{
			setCurrentItemOrArmor(0, new ItemStack(LOTRMod.dunlendingClub));
		}
		else if (i == 2 || i == 3)
		{
			setCurrentItemOrArmor(0, new ItemStack(LOTRMod.dunlendingTrident));
		}
		else if (i == 4)
		{
			setCurrentItemOrArmor(0, new ItemStack(Items.wooden_sword));
		}
		else if (i == 5)
		{
			setCurrentItemOrArmor(0, new ItemStack(Items.stone_sword));
		}
		else if (i == 6)
		{
			setCurrentItemOrArmor(0, new ItemStack(Items.stone_axe));
		}
		else if (i == 7)
		{
			setCurrentItemOrArmor(0, new ItemStack(Items.stone_hoe));
		}
		
		return data;
    }
	
	@Override
	public LOTRFaction getFaction()
	{
		return LOTRFaction.DUNLAND;
	}
	
	@Override
    public boolean isAIEnabled()
    {
        return true;
    }
	
	@Override
	public String getNPCName()
	{
		return getDunlendingName();
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound nbt)
	{
		super.writeEntityToNBT(nbt);
		nbt.setString("DunlendingName", getDunlendingName());
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound nbt)
	{
		super.readEntityFromNBT(nbt);
		if (nbt.hasKey("DunlendingName"))
		{
			setDunlendingName(nbt.getString("DunlendingName"));
		}
	}
	
	@Override
	public boolean canPickUpLoot()
	{
		return true;
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
		
		dropDunlendingItems(flag, i);
	}
		
	public void dropDunlendingItems(boolean flag, int i)
	{
		if (rand.nextInt(5) == 0)
		{
			int j = 1 + rand.nextInt(2) + rand.nextInt(i + 1);
			for (int k = 0; k < j; k++)
			{
				int l = rand.nextInt(9);
				switch(l)
				{
					case 0: case 1: case 2:
						entityDropItem(LOTRFoods.DUNLENDING.getRandomFood(rand), 0F);
						break;
					case 3:
						Item drink = LOTRFoods.DUNLENDING_DRINK.getRandomFood(rand).getItem();
						entityDropItem(new ItemStack(drink, 1, 1 + rand.nextInt(3)), 0F);
						break;
					case 4:
						entityDropItem(new ItemStack(Items.leather, 1 + rand.nextInt(2)), 0F);
						break;
					case 5:
						entityDropItem(new ItemStack(Items.feather, 1 + rand.nextInt(2)), 0F);
						break;
					case 6:
						entityDropItem(new ItemStack(Items.flint, 1 + rand.nextInt(3)), 0F);
						break;
					case 7:
						entityDropItem(new ItemStack(Items.flint_and_steel, 1, rand.nextInt(40)), 0F);
						break;
					case 8:
						entityDropItem(new ItemStack(Items.stick, 1 + rand.nextInt(4)), 0F);
						break;
				}
			}
		}
	}
	
	@Override
	protected LOTRAchievement getKillAchievement()
	{
		return LOTRAchievement.killDunlending;
	}
	
	@Override
	public int getAlignmentBonus()
	{
		return LOTRAlignmentValues.DUNLENDING_BONUS;
	}

	@Override
	public boolean getCanSpawnHere()
	{
		if (super.getCanSpawnHere())
		{
			if (liftSpawnRestrictions)
			{
				return true;
			}
			else
			{
				int i = MathHelper.floor_double(posX);
				int j = MathHelper.floor_double(boundingBox.minY);
				int k = MathHelper.floor_double(posZ);
				BiomeGenBase biome = worldObj.getBiomeGenForCoords(i, k);
				return biome instanceof LOTRBiomeGenDunland && j > 62 && worldObj.getBlock(i, j - 1, k) == Blocks.grass;
			}
		}
		return false;
	}

	@Override
	public float getBlockPathWeight(int i, int j, int k)
	{
		float f = 0F;
		BiomeGenBase biome = worldObj.getBiomeGenForCoords(i, k);
		if (biome instanceof LOTRBiomeGenDunland)
		{
			f += 20F;
		}
		return f;
	}
	
	@Override
	public String getSpeechBank(EntityPlayer entityplayer)
	{
		if (isFriendly(entityplayer))
		{
			if (hiredNPCInfo.getHiringPlayer() == entityplayer)
			{
				return "dunlending_hired";
			}
			return "dunlending_friendly";
		}
		else
		{
			return "dunlending_hostile";
		}
	}
}
