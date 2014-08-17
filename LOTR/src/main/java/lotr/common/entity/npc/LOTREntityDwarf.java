package lotr.common.entity.npc;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRAlignmentValues;
import lotr.common.LOTRFaction;
import lotr.common.LOTRFoods;
import lotr.common.LOTRLevelData;
import lotr.common.LOTRMod;
import lotr.common.entity.ai.LOTREntityAIAttackOnCollide;
import lotr.common.entity.ai.LOTREntityAIDrink;
import lotr.common.entity.ai.LOTREntityAIEat;
import lotr.common.entity.ai.LOTREntityAIFollowHiringPlayer;
import lotr.common.entity.ai.LOTREntityAIHiredRemainStill;
import lotr.common.entity.ai.LOTREntityAIHiringPlayerHurtByTarget;
import lotr.common.entity.ai.LOTREntityAIHiringPlayerHurtTarget;
import lotr.common.entity.ai.LOTREntityAIHobbitChildFollowGoodPlayer;
import lotr.common.entity.ai.LOTREntityAINPCAvoidEvilPlayer;
import lotr.common.entity.ai.LOTREntityAINPCFollowParent;
import lotr.common.entity.ai.LOTREntityAINPCFollowSpouse;
import lotr.common.entity.ai.LOTREntityAINPCMarry;
import lotr.common.entity.ai.LOTREntityAINPCMate;
import lotr.common.entity.npc.LOTREntityNPC.AttackMode;
import lotr.common.world.biome.LOTRBiomeGenBlueMountains;
import lotr.common.world.biome.LOTRBiomeGenIronHills;
import lotr.common.world.biome.LOTRBiomeGenRedMountains;
import lotr.common.world.structure.LOTRChestContents;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
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
import net.minecraft.util.Vec3;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

public class LOTREntityDwarf extends LOTREntityNPC
{
	public LOTREntityDwarf(World world)
	{
		super(world);
        setSize(0.5F, 1.5F);
        getNavigator().setAvoidsWater(true);
		getNavigator().setBreakDoors(true);
        tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(1, new LOTREntityAIHiredRemainStill(this));
		tasks.addTask(2, new LOTREntityAINPCAvoidEvilPlayer(this, 8F, 1.5D, 1.8D));
		tasks.addTask(3, getDwarfAttackAI());
		tasks.addTask(4, new LOTREntityAIFollowHiringPlayer(this));
		tasks.addTask(5, new LOTREntityAINPCMarry(this, 1.3D));
		tasks.addTask(6, new LOTREntityAINPCMate(this, 1.3D));
		tasks.addTask(7, new LOTREntityAINPCFollowParent(this, 1.4D));
		tasks.addTask(8, new LOTREntityAINPCFollowSpouse(this, 1.1D));
		tasks.addTask(9, new EntityAIOpenDoor(this, true));
        tasks.addTask(10, new EntityAIWander(this, 1D));
        tasks.addTask(11, new LOTREntityAIEat(this, LOTRFoods.DWARF, 6000));
        tasks.addTask(11, new LOTREntityAIDrink(this, LOTRFoods.DWARF_DRINK, 6000));
        tasks.addTask(12, new EntityAIWatchClosest2(this, EntityPlayer.class, 8F, 0.2F));
        tasks.addTask(12, new EntityAIWatchClosest2(this, LOTREntityNPC.class, 5F, 0.1F));
        tasks.addTask(13, new EntityAIWatchClosest(this, EntityLiving.class, 8F, 0.02F));
        tasks.addTask(14, new EntityAILookIdle(this));
        targetTasks.addTask(1, new LOTREntityAIHiringPlayerHurtByTarget(this));
        targetTasks.addTask(2, new LOTREntityAIHiringPlayerHurtTarget(this));
        targetTasks.addTask(3, new EntityAIHurtByTarget(this, false));
        addTargetTasks(4);
        
        familyInfo.marriageEntityClass = LOTREntityDwarf.class;
        familyInfo.marriageRing = LOTRMod.dwarvenRing;
        familyInfo.marriageAlignmentRequired = LOTRAlignmentValues.Levels.DWARF_MARRY;
        familyInfo.marriageAchievement = LOTRAchievement.marryDwarf;
        familyInfo.potentialMaxChildren = 3;
        familyInfo.timeToMature = 72000;
        familyInfo.breedingDelay = 48000;
	}
	
	public EntityAIBase getDwarfAttackAI()
	{
		return new LOTREntityAIAttackOnCollide(this, 1.4D, false);
	}
	
	@Override
	protected void entityInit()
	{
		super.entityInit();
		familyInfo.setNPCMale(true);
		dataWatcher.addObject(18, LOTRNames.getRandomDwarfName(familyInfo.isNPCMale(), rand));
	}
	
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(26D);
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.2D);
    }
	
	@Override
	public IEntityLivingData initCreatureForHire(IEntityLivingData data)
	{
		data = super.initCreatureForHire(data);
		data = onSpawnWithEgg(data);
		
		if (canDwarfSpawnAsWoman() && rand.nextInt(3) == 0)
		{
			setDwarfFemale();
		}
		
		return data;
	}
	
	@Override
	public LOTRFaction getFaction()
	{
		return LOTRFaction.DWARF;
	}
	
	protected Item getDwarfDagger()
	{
		return LOTRMod.daggerDwarven;
	}
	
	protected Item getDwarfSteelDrop()
	{
		return LOTRMod.dwarfSteel;
	}
	
	@Override
    public boolean isAIEnabled()
    {
        return true;
    }
	
	@Override
	public String getNPCName()
	{
		return getDwarfName();
	}
	
	public String getDwarfName()
	{
		return dataWatcher.getWatchableObjectString(18);
	}
	
	public void setDwarfName(String name)
	{
		dataWatcher.updateObject(18, name);
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound nbt)
	{
		super.writeEntityToNBT(nbt);
		nbt.setString("DwarfName", getDwarfName());
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound nbt)
	{
		super.readEntityFromNBT(nbt);
		
		if (nbt.hasKey("DwarfName"))
		{
			setDwarfName(nbt.getString("DwarfName"));
		}
		
		if (nbt.hasKey("DwarfGender"))
		{
			familyInfo.setNPCMale(nbt.getBoolean("DwarfGender"));
		}
	}
	
	@Override
	public void createNPCChildName(LOTREntityNPC maleParent, LOTREntityNPC femaleParent)
	{
		setDwarfName(LOTRNames.getRandomDwarfChildNameForParent(familyInfo.isNPCMale(), rand, (LOTREntityDwarf)maleParent));
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
	public void onAttackModeChange(AttackMode mode)
	{
		if (mode == AttackMode.IDLE)
		{
			setCurrentItemOrArmor(0, null);
		}
		else
		{
			setCurrentItemOrArmor(0, new ItemStack(getDwarfDagger()));
		}
	}
	
	@Override
	protected LOTRAchievement getKillAchievement()
	{
		return LOTRAchievement.killDwarf;
	}
	
	@Override
	public int getAlignmentBonus()
	{
		return LOTRAlignmentValues.Bonuses.DWARF;
	}
	
	@Override
	protected void dropFewItems(boolean flag, int i)
	{
		super.dropFewItems(flag, i);
		
		int j = rand.nextInt(2) + rand.nextInt(i + 1);
		for (int k = 0; k < j; k++)
		{
			dropItem(LOTRMod.dwarfBone, 1);
		}

		if (rand.nextBoolean())
		{
			dropChestContents(LOTRChestContents.DWARF_HOUSE_LARDER, 0, 2 + i);
		}
		
		if (flag)
		{
			int rareDropChance = 20 - i * 4;
			if (rareDropChance < 1)
			{
				rareDropChance = 1;
			}
			
			if (rand.nextInt(rareDropChance) == 0)
			{
				j = rand.nextInt(4);
				switch (j)
				{
					case 0:
						entityDropItem(new ItemStack(Items.iron_ingot), 0F);
						break;
					case 1:
						entityDropItem(new ItemStack(getDwarfSteelDrop()), 0F);
						break;
					case 2:
						entityDropItem(new ItemStack(Items.gold_nugget, 1 + rand.nextInt(3)), 0F);
						break;
					case 3:
						entityDropItem(new ItemStack(LOTRMod.silverNugget, 1 + rand.nextInt(3)), 0F);
						break;
				}
			}
		}
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
				return (biome instanceof LOTRBiomeGenIronHills || biome instanceof LOTRBiomeGenBlueMountains || biome instanceof LOTRBiomeGenRedMountains) && j < 60 && worldObj.getBlock(i, j - 1, k).getMaterial() == Material.rock && !worldObj.canBlockSeeTheSky(i, j, k) && worldObj.getSavedLightValue(EnumSkyBlock.Block, i, j, k) >= 10;
			}
		}
		return false;
	}
	
	@Override
	public float getBlockPathWeight(int i, int j, int k)
	{
		float f = 0F;
		BiomeGenBase biome = worldObj.getBiomeGenForCoords(i, k);
		if (biome instanceof LOTRBiomeGenIronHills || biome instanceof LOTRBiomeGenBlueMountains || biome instanceof LOTRBiomeGenRedMountains)
		{
			f += 20F;
		}
		return f;
	}
	
	@Override
	public int getMaxSpawnedInChunk()
	{
		return 6;
	}
	
	@Override
	protected float getSoundPitch()
	{
		float f = super.getSoundPitch();
		if (!familyInfo.isNPCMale())
		{
			f *= 1.4F;
		}
		return f;
	}
	
	@Override
	public String getHurtSound()
	{
		return "lotr:dwarf.hurt";
	}
	
	@Override
	public String getDeathSound()
	{
		return "lotr:dwarf.hurt";
	}
	
	@Override
	public String getAttackSound()
	{
		return "lotr:dwarf.attack";
	}
	
	@Override
	public void onKillEntity(EntityLivingBase entity)
	{
		super.onKillEntity(entity);
		playSound("lotr:dwarf.kill", getSoundVolume(), getSoundPitch());
	}
	
	@Override
	protected LOTRAchievement getTalkAchievement()
	{
		if (!familyInfo.isNPCMale())
		{
			return LOTRAchievement.talkDwarfWoman;
		}
		return super.getTalkAchievement();
	}
	
	@Override
	public String getSpeechBank(EntityPlayer entityplayer)
	{
		if (isFriendly(entityplayer))
		{
			if (hiredNPCInfo.getHiringPlayer() == entityplayer)
			{
				return "dwarf_hired";
			}
			return isChild() ? "dwarfChild_friendly" : "dwarf_friendly";
		}
		else
		{
			return isChild() ? "dwarfChild_unfriendly" : "dwarf_hostile";
		}
	}
	
	@Override
	public void onArtificalSpawn()
	{
		if (canDwarfSpawnAsWoman() && rand.nextInt(3) == 0)
		{
			setDwarfFemale();
		}
		
		if (getClass() == familyInfo.marriageEntityClass && rand.nextInt(20) == 0)
		{
			familyInfo.setChild();
		}
	}
	
	protected void setDwarfFemale()
	{
		familyInfo.setNPCMale(false);
		setDwarfName(LOTRNames.getRandomDwarfName(familyInfo.isNPCMale(), rand));
	}
	
	protected boolean canDwarfSpawnAsWoman()
	{
		return getClass() == familyInfo.marriageEntityClass;
	}
}
