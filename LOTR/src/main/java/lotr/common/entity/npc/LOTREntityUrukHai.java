package lotr.common.entity.npc;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRAlignmentValues;
import lotr.common.LOTRFaction;
import lotr.common.LOTRLevelData;
import lotr.common.LOTRMod;
import lotr.common.entity.ai.LOTREntityAIAttackOnCollide;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class LOTREntityUrukHai extends LOTREntityOrc
{
	public LOTREntityUrukHai(World world)
	{
		super(world);
		setSize(0.6F, 1.8F);
		isWeakOrc = false;
	}
	
	@Override
	public EntityAIBase createOrcAttackAI()
	{
		return new LOTREntityAIAttackOnCollide(this, 1.4D, false);
	}
	
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(24D);
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.22D);
    }
	
	@Override
    public IEntityLivingData onSpawnWithEgg(IEntityLivingData data)
    {
		data = super.onSpawnWithEgg(data);
		int i = rand.nextInt(8);
		
		if (i == 0 || i == 1 || i == 2)
		{
			setCurrentItemOrArmor(0, new ItemStack(LOTRMod.scimitarUruk));
		}
		else if (i == 3)
		{
			setCurrentItemOrArmor(0, new ItemStack(LOTRMod.battleaxeUruk));
		}
		else if (i == 4)
		{
			setCurrentItemOrArmor(0, new ItemStack(LOTRMod.daggerUruk));
		}
		else if (i == 5)
		{
			setCurrentItemOrArmor(0, new ItemStack(LOTRMod.daggerUrukPoisoned));
		}
		else if (i == 6)
		{
			setCurrentItemOrArmor(0, new ItemStack(LOTRMod.spearUruk));
		}
		else if (i == 7)
		{
			setCurrentItemOrArmor(0, new ItemStack(LOTRMod.hammerUruk));
		}
		
		setCurrentItemOrArmor(1, new ItemStack(LOTRMod.bootsUruk));
		setCurrentItemOrArmor(2, new ItemStack(LOTRMod.legsUruk));
		setCurrentItemOrArmor(3, new ItemStack(LOTRMod.bodyUruk));
		
		if (rand.nextInt(10) != 0)
		{
			setCurrentItemOrArmor(4, new ItemStack(LOTRMod.helmetUruk));
		}
		
		return data;
    }
	
	@Override
	public void onDeath(DamageSource damagesource)
	{
		super.onDeath(damagesource);
		
		if (!worldObj.isRemote && damagesource.getEntity() instanceof EntityPlayer)
		{
			EntityPlayer entityplayer = (EntityPlayer)damagesource.getEntity();
			for (int i = 0; i < LOTRLevelData.urukCampLocations.size(); i++)
			{
				ChunkCoordinates coords = (ChunkCoordinates)LOTRLevelData.urukCampLocations.get(i);
				if (getDistanceSq(coords.posX, coords.posY, coords.posZ) < 144D)
				{
					LOTRLevelData.getData(entityplayer).addAchievement(LOTRAchievement.raidUrukCamp);
					break;
				}
			}
		}
	}
	
	@Override
	public LOTRFaction getFaction()
	{
		return LOTRFaction.URUK_HAI;
	}
	
	@Override
	public int getAlignmentBonus()
	{
		return LOTRAlignmentValues.URUK_HAI_BONUS;
	}
	
	@Override
	protected float getSoundPitch()
	{
		return super.getSoundPitch() * 0.75F;
	}
	
	@Override
	public boolean canPickUpLoot()
	{
		return false;
	}
}
