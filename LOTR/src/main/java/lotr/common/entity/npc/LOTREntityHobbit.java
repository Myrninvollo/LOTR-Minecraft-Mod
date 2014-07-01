package lotr.common.entity.npc;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRAlignmentValues;
import lotr.common.LOTRFaction;
import lotr.common.LOTRFoods;
import lotr.common.LOTRLevelData;
import lotr.common.LOTRMod;
import lotr.common.entity.ai.LOTREntityAIHobbitChildFollowGoodPlayer;
import lotr.common.entity.ai.LOTREntityAINPCAvoidEvilPlayer;
import lotr.common.entity.ai.LOTREntityAINPCFollowParent;
import lotr.common.entity.ai.LOTREntityAINPCFollowSpouse;
import lotr.common.entity.ai.LOTREntityAINPCMarry;
import lotr.common.entity.ai.LOTREntityAINPCMate;
import lotr.common.entity.projectile.LOTREntitySmokeRing;
import lotr.common.world.biome.LOTRBiomeGenShire;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAIPanic;
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
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTREntityHobbit extends LOTREntityNPC
{
	public LOTREntityHobbit(World world)
	{
		super(world);
        setSize(0.45F, 0.9F);
        getNavigator().setAvoidsWater(true);
		getNavigator().setBreakDoors(true);
        tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(1, new EntityAIAvoidEntity(this, LOTREntityOrc.class, 12F, 1.5D, 1.8D));
		tasks.addTask(1, new EntityAIAvoidEntity(this, LOTREntityWarg.class, 12F, 1.5D, 1.8D));
		tasks.addTask(1, new EntityAIAvoidEntity(this, LOTREntityTroll.class, 12F, 1.5D, 1.8D));
		tasks.addTask(1, new EntityAIAvoidEntity(this, LOTREntitySpiderBase.class, 12F, 1.5D, 1.8D));
		tasks.addTask(1, new EntityAIAvoidEntity(this, LOTREntityDarkHuorn.class, 12F, 1.5D, 1.8D));
		tasks.addTask(2, new EntityAIPanic(this, 1.6D));
		tasks.addTask(3, new LOTREntityAINPCAvoidEvilPlayer(this, 8F, 1.5D, 1.8D));
		tasks.addTask(4, new LOTREntityAIHobbitChildFollowGoodPlayer(this, 12F, 1.5D));
		tasks.addTask(5, new LOTREntityAINPCMarry(this, 1.3D));
		tasks.addTask(6, new LOTREntityAINPCMate(this, 1.3D));
		tasks.addTask(7, new LOTREntityAINPCFollowParent(this, 1.4D));
		tasks.addTask(8, new LOTREntityAINPCFollowSpouse(this, 1.1D));
		tasks.addTask(9, new EntityAIOpenDoor(this, true));
        tasks.addTask(10, new EntityAIWander(this, 1.1D));
        tasks.addTask(11, new EntityAIWatchClosest2(this, EntityPlayer.class, 8F, 0.2F));
        tasks.addTask(11, new EntityAIWatchClosest2(this, LOTREntityNPC.class, 5F, 0.05F));
        tasks.addTask(12, new EntityAIWatchClosest(this, EntityLiving.class, 8F, 0.02F));
        tasks.addTask(13, new EntityAILookIdle(this));
        
        familyInfo.marriageEntityClass = LOTREntityHobbit.class;
        familyInfo.marriageRing = LOTRMod.hobbitRing;
        familyInfo.marriageAlignmentRequired = LOTRAlignmentValues.HOBBIT_MARRY;
        familyInfo.marriageAchievement = LOTRAchievement.marryHobbit;
        familyInfo.potentialMaxChildren = 4;
        familyInfo.timeToMature = 48000;
        familyInfo.breedingDelay = 24000;
	}
	
	@Override
	protected void entityInit()
	{
		super.entityInit();
		familyInfo.setNPCMale(rand.nextBoolean());
		dataWatcher.addObject(16, Byte.valueOf((byte)0));
		dataWatcher.addObject(17, Byte.valueOf((byte)0));
		dataWatcher.addObject(18, Byte.valueOf((byte)0));
		dataWatcher.addObject(19, LOTRNames.getRandomHobbitName(familyInfo.isNPCMale(), rand));
	}
	
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(16D);
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.2D);
    }
	
	@Override
	public LOTRFaction getFaction()
	{
		return LOTRFaction.HOBBIT;
	}
	
	@Override
	public String getNPCName()
	{
		return getHobbitName();
	}
	
	@Override
    public boolean isAIEnabled()
    {
        return true;
    }
	
	public String getHobbitName()
	{
		return dataWatcher.getWatchableObjectString(19);
	}
	
	public void setHobbitName(String name)
	{
		dataWatcher.updateObject(19, name);
	}
	
	public int getEatingTick()
	{
		return dataWatcher.getWatchableObjectByte(16);
	}
	
	public void setEatingTick(int i)
	{
		dataWatcher.updateObject(16, Byte.valueOf((byte)i));
	}
	
	public int getDrinkingTick()
	{
		return dataWatcher.getWatchableObjectByte(17);
	}
	
	public void setDrinkingTick(int i)
	{
		dataWatcher.updateObject(17, Byte.valueOf((byte)i));
	}
	
	public int getSmokingTick()
	{
		return dataWatcher.getWatchableObjectByte(18);
	}
	
	public void setSmokingTick(int i)
	{
		dataWatcher.updateObject(18, Byte.valueOf((byte)i));
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound nbt)
	{
		super.writeEntityToNBT(nbt);
		nbt.setByte("HobbitEating", (byte)getEatingTick());
		nbt.setByte("HobbitDrinking", (byte)getDrinkingTick());
		nbt.setByte("HobbitSmoking", (byte)getSmokingTick());
		nbt.setString("HobbitName", getHobbitName());
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound nbt)
	{
		super.readEntityFromNBT(nbt);
		setEatingTick(nbt.getByte("HobbitEating"));
		setDrinkingTick(nbt.getByte("HobbitDrinking"));
		setSmokingTick(nbt.getByte("HobbitSmoking"));
		if (nbt.hasKey("HobbitName"))
		{
			setHobbitName(nbt.getString("HobbitName"));
		}
		
		if (nbt.hasKey("HobbitGender"))
		{
			familyInfo.setNPCMale(nbt.getBoolean("HobbitGender"));
		}
		if (nbt.hasKey("HobbitAge"))
		{
			familyInfo.setNPCAge(nbt.getInteger("HobbitAge"));
		}
	}
	
	@Override
	public void changeNPCNameForMarriage(LOTREntityNPC spouse)
	{
		if (familyInfo.isNPCMale())
		{
			LOTRNames.changeHobbitSurnameForMarriage(this, (LOTREntityHobbit)spouse);
		}
		else if (spouse.familyInfo.isNPCMale())
		{
			LOTRNames.changeHobbitSurnameForMarriage((LOTREntityHobbit)spouse, this);
		}
	}
	
	@Override
	public void createNPCChildName(LOTREntityNPC maleParent, LOTREntityNPC femaleParent)
	{
		setHobbitName(LOTRNames.getRandomHobbitChildNameForParent(familyInfo.isNPCMale(), rand, (LOTREntityHobbit)maleParent));
	}
	
	@Override
	public boolean interact(EntityPlayer entityplayer)
	{
		if (familyInfo.interact(entityplayer))
		{
			return true;
		}
		return super.interact(entityplayer);
	}
	
	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		onHobbitUpdate();
	}
	
	public void onHobbitUpdate()
	{
		if (familyInfo.getNPCAge() < 0)
		{
			return;
		}
		
		if (!worldObj.isRemote && getEquipmentInSlot(0) == null && rand.nextInt(4000) == 0)
		{
			setCurrentItemOrArmor(0, LOTRFoods.HOBBIT.getRandomFood(rand));
			setEatingTick(32);
		}
		
		if (getEatingTick() > 0)
		{
			if (!worldObj.isRemote)
			{
				setEatingTick(getEatingTick() - 1);
			}
			
			if (getEquipmentInSlot(0) != null && getEatingTick() % 4 == 0)
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
					worldObj.spawnParticle("iconcrack_" + Item.getIdFromItem(getEquipmentInSlot(0).getItem()), vec2.xCoord, vec2.yCoord, vec2.zCoord, vec1.xCoord, vec1.yCoord + 0.05D, vec1.zCoord);
				}
				
				playSound("random.eat", 0.5F + 0.5F * (float)rand.nextInt(2), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1F);
			}
			
			if (getEatingTick() == 0)
			{
				setCurrentItemOrArmor(0, null);
				heal(4F);
			}
		}
		
		if (!worldObj.isRemote && getEquipmentInSlot(0) == null && rand.nextInt(4000) == 0)
		{
			Item drink = LOTRFoods.HOBBIT_DRINK.getRandomFood(rand).getItem();
			setCurrentItemOrArmor(0, new ItemStack(drink, 1, 1 + rand.nextInt(3)));
			setDrinkingTick(32);
		}
		
		if (getDrinkingTick() > 0)
		{
			if (!worldObj.isRemote)
			{
				setDrinkingTick(getDrinkingTick() - 1);
			}
			
			if (getEquipmentInSlot(0) != null && getDrinkingTick() % 4 == 0)
			{
				playSound("random.drink", 0.5F, rand.nextFloat() * 0.1F + 0.9F);
			}
			
			if (getDrinkingTick() == 0)
			{
				setCurrentItemOrArmor(0, null);
				heal(2F);
			}
		}
		
		if (!worldObj.isRemote && getEquipmentInSlot(0) == null && rand.nextInt(4000) == 0)
		{
			setCurrentItemOrArmor(0, new ItemStack(LOTRMod.hobbitPipe));
			setSmokingTick(32);
		}
		
		if (getSmokingTick() > 0)
		{
			if (!worldObj.isRemote)
			{
				setSmokingTick(getSmokingTick() - 1);
			}
			
			if (getSmokingTick() == 0)
			{
				setCurrentItemOrArmor(0, null);
				if (!worldObj.isRemote)
				{
					worldObj.spawnEntityInWorld(new LOTREntitySmokeRing(worldObj, this));
				}
				playSound("lotr:item.puff", 1F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1F);
				heal(2F);
			}
		}
	}
	
	@Override
	protected LOTRAchievement getKillAchievement()
	{
		return LOTRAchievement.killHobbit;
	}
	
	@Override
	public int getAlignmentBonus()
	{
		return LOTRAlignmentValues.HOBBIT_BONUS;
	}
	
	@Override
	protected void dropFewItems(boolean flag, int i)
	{
		int j = rand.nextInt(2) + rand.nextInt(i + 1);
		for (int k = 0; k < j; k++)
		{
			dropItem(LOTRMod.hobbitBone, 1);
		}
		
		dropHobbitItems(flag, i);
	}
		
	public void dropHobbitItems(boolean flag, int i)
	{
		int count = rand.nextInt(3) + rand.nextInt(i + 1);
		for (int k = 0; k < count; k++)
		{
			int j = rand.nextInt(14);
			switch(j)
			{
				case 0: case 1: case 2: case 3:
					entityDropItem(LOTRFoods.HOBBIT.getRandomFood(rand), 0F);
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
					entityDropItem(new ItemStack(Items.wooden_hoe, 1, rand.nextInt(30)), 0F);
					break;
				case 9:
					entityDropItem(new ItemStack(LOTRMod.hobbitPipe, 1, rand.nextInt(100)), 0F);
					break;
				case 10: case 11:
					entityDropItem(new ItemStack(LOTRMod.pipeweed, 1 + rand.nextInt(2)), 0F);
					break;
				case 12:
					entityDropItem(new ItemStack(LOTRMod.mug), 0F);
					break;
				case 13:
					Item drink = LOTRFoods.HOBBIT_DRINK.getRandomFood(rand).getItem();
					entityDropItem(new ItemStack(drink, 1, rand.nextInt(4)), 0F);
					break;
			}
		}
	}
	
	@Override
	protected int getExperiencePoints(EntityPlayer entityplayer)
    {
        return 1 + rand.nextInt(3);
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
				return biome instanceof LOTRBiomeGenShire && j > 62 && worldObj.getBlock(i, j - 1, k) == Blocks.grass;
			}
		}
		return false;
	}
	
	@Override
	public float getBlockPathWeight(int i, int j, int k)
	{
		float f = 0F;
		BiomeGenBase biome = worldObj.getBiomeGenForCoords(i, k);
		if (biome instanceof LOTRBiomeGenShire)
		{
			f += 20F;
		}
		return f;
	}
	
	public boolean isConsuming()
	{
		return getEatingTick() > 0 || getDrinkingTick() > 0 || getSmokingTick() > 0;
	}
	
	@Override
	public String getSpeechBank(EntityPlayer entityplayer)
	{
		if (isFriendly(entityplayer))
		{
			return isChild() ? "hobbitChild_friendly" : "hobbit_friendly";
		}
		else
		{
			return isChild() ? "hobbitChild_unfriendly" : "hobbit_unfriendly";
		}
	}
	
	@Override
	public void onArtificalSpawn()
	{
		if (getClass() == familyInfo.marriageEntityClass && rand.nextInt(10) == 0)
		{
			familyInfo.setChild();
		}
	}
}
