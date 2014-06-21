package lotr.common.entity.npc;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRAlignmentValues;
import lotr.common.LOTRFaction;
import lotr.common.LOTRFoods;
import lotr.common.LOTRMod;
import lotr.common.entity.ai.LOTREntityAIAttackOnCollide;
import lotr.common.entity.ai.LOTREntityAIFollowHiringPlayer;
import lotr.common.entity.ai.LOTREntityAIHiredRemainStill;
import lotr.common.entity.animal.LOTREntityCamel;
import lotr.common.entity.animal.LOTREntityShirePony;
import lotr.common.world.biome.LOTRBiomeGenHarondor;
import lotr.common.world.biome.LOTRBiomeGenNearHarad.ImmuneToHeat;
import lotr.common.world.biome.LOTRBiomeGenNearHaradFertile;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
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
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

public class LOTREntityNearHaradrim extends LOTREntityNPC implements ImmuneToHeat
{
	public LOTREntityNearHaradrim(World world)
	{
		super(world);
		setSize(0.6F, 1.8F);
		getNavigator().setAvoidsWater(true);
		getNavigator().setBreakDoors(true);
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(1, new LOTREntityAIHiredRemainStill(this));
        tasks.addTask(2, createHaradrimAttackAI());
        tasks.addTask(3, new LOTREntityAIFollowHiringPlayer(this));
		tasks.addTask(4, new EntityAIOpenDoor(this, true));
        tasks.addTask(5, new EntityAIWander(this, 1D));
        tasks.addTask(6, new EntityAIWatchClosest2(this, EntityPlayer.class, 10F, 0.1F));
        tasks.addTask(6, new EntityAIWatchClosest2(this, LOTREntityNPC.class, 5F, 0.02F));
        tasks.addTask(7, new EntityAIWatchClosest(this, EntityLiving.class, 8F, 0.02F));
        tasks.addTask(8, new EntityAILookIdle(this));
        targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        addTargetTasks(2);
	}
	
	public EntityAIBase createHaradrimAttackAI()
	{
		return new LOTREntityAIAttackOnCollide(this, 1.5D, true).setSpearReplacement(LOTRMod.scimitarNearHarad);
	}

	@Override
	public void entityInit()
	{
		super.entityInit();
		dataWatcher.addObject(15, Byte.valueOf((byte)1));
		dataWatcher.addObject(16, LOTRNames.getRandomNearHaradName(isHaradrimMale(), rand));
	}
	
	public boolean isHaradrimMale()
	{
		return dataWatcher.getWatchableObjectByte(15) == (byte)1;
	}
	
	public void setHaradrimMale(boolean flag)
	{
		dataWatcher.updateObject(15, Byte.valueOf(flag ? (byte)1 : (byte)0));
	}
	
	public String getHaradName()
	{
		return dataWatcher.getWatchableObjectString(16);
	}
	
	public void setHaradName(String name)
	{
		dataWatcher.updateObject(16, name);
	}
	
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20D);
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.2D);
    }
	
	@Override
	public LOTRNPCMount createMountToRide()
	{
		return new LOTREntityCamel(worldObj);
	}
	
	@Override
	public LOTRFaction getFaction()
	{
		return LOTRFaction.NEAR_HARAD;
	}

	@Override
    public boolean isAIEnabled()
    {
        return true;
    }
	
	@Override
	public String getNPCName()
	{
		return getHaradName();
	}
	
	@Override
	public String getCommandSenderName()
	{
		if (getClass() == LOTREntityNearHaradrim.class)
		{
			return StatCollector.translateToLocalFormatted("entity.lotr.NearHaradrim.entityName", new Object[] {getNPCName()});
		}
		else
		{
			return super.getCommandSenderName();
		}
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound nbt)
	{
		super.writeEntityToNBT(nbt);
		nbt.setBoolean("HaradrimGender", isHaradrimMale());
		nbt.setString("HaradrimName", getHaradName());
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound nbt)
	{
		super.readEntityFromNBT(nbt);
		setHaradrimMale(nbt.getBoolean("HaradrimGender"));
		if (nbt.hasKey("HaradrimName"))
		{
			setHaradName(nbt.getString("HaradrimName"));
		}
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
		
		dropHaradrimItems(flag, i);
	}
	
	protected void dropHaradrimItems(boolean flag, int i)
	{
		int count = rand.nextInt(3) + rand.nextInt(i + 1);
		for (int k = 0; k < count; k++)
		{
			int j = rand.nextInt(10);
			switch(j)
			{
				case 0: case 1: case 2: case 3:
					entityDropItem(LOTRFoods.NEAR_HARAD.getRandomFood(rand), 0F);
					break;
				case 4:
					entityDropItem(new ItemStack(Items.string, 1 + rand.nextInt(3)), 0F);
					break;
				case 5:
					entityDropItem(new ItemStack(Items.paper, 2 + rand.nextInt(4)), 0F);
					break;
				case 6:
					entityDropItem(new ItemStack(Items.book, 1 + rand.nextInt(2)), 0F);
					break;
				case 7:
					entityDropItem(new ItemStack(Items.bowl, 1 + rand.nextInt(4)), 0F);
					break;
				case 8:
					entityDropItem(new ItemStack(LOTRMod.mug), 0F);
					break;
				case 9:
					entityDropItem(new ItemStack(LOTRMod.mugAraq, 1, 1 + rand.nextInt(3)), 0F);
					break;
			}
		}
	}
	
	@Override
	protected LOTRAchievement getKillAchievement()
	{
		return LOTRAchievement.killNearHaradrim;
	}
	
	@Override
	public int getAlignmentBonus()
	{
		return LOTRAlignmentValues.NEAR_HARADRIM_BONUS;
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
				Block block = worldObj.getBlock(i, j - 1, k);
				return j > 62 && (block == Blocks.grass ||block == Blocks.sand);
			}
		}
		return false;
	}
	
	@Override
	public float getBlockPathWeight(int i, int j, int k)
	{
		float f = 0F;
		BiomeGenBase biome = worldObj.getBiomeGenForCoords(i, k);
		if (biome instanceof LOTRBiomeGenNearHaradFertile || biome instanceof LOTRBiomeGenHarondor)
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
			return "nearHaradrim_friendly";
		}
		else
		{
			return "nearHaradrim_hostile";
		}
	}
}
