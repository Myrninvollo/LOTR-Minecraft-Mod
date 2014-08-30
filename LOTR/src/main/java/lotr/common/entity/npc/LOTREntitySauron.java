package lotr.common.entity.npc;

import lotr.common.LOTRFaction;
import lotr.common.LOTRMod;
import lotr.common.entity.ai.LOTREntityAIAttackOnCollide;
import lotr.common.entity.ai.LOTREntityAISauronUseMace;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class LOTREntitySauron extends LOTREntityNPC
{
	public LOTREntitySauron(World world)
	{
		super(world);
		setSize(0.8F, 2.2F);
		isImmuneToFire = true;
		getNavigator().setAvoidsWater(true);
        tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(1, new LOTREntityAISauronUseMace(this));
		tasks.addTask(2, new LOTREntityAIAttackOnCollide(this, 2D, false));
        tasks.addTask(3, new EntityAIWander(this, 1D));
        tasks.addTask(4, new EntityAIWatchClosest(this, EntityPlayer.class, 10F, 0.02F));
        tasks.addTask(5, new EntityAILookIdle(this));
        addTargetTasks(true);
	}

	@Override
	protected void entityInit()
	{
		super.entityInit();
		dataWatcher.addObject(17, Byte.valueOf((byte)0));
	}
	
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(200D);
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.18D);
        getEntityAttribute(npcAttackDamage).setBaseValue(8D);
    }
	
	@Override
    public IEntityLivingData onSpawnWithEgg(IEntityLivingData data)
    {
		data = super.onSpawnWithEgg(data);
		setCurrentItemOrArmor(0, new ItemStack(LOTRMod.sauronMace));
		return data;
    }
	
	@Override
	public LOTRFaction getFaction()
	{
		return LOTRFaction.MORDOR;
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
    public void onLivingUpdate()
    {
		super.onLivingUpdate();
		
		if (!worldObj.isRemote && getHealth() < getMaxHealth() && worldObj.getWorldTime() % 10L == 0)
		{
			heal(2F);
		}
		
		if (getIsUsingMace())
		{
			if (worldObj.isRemote)
			{
				for (int i = 0; i < 6; i++)
				{
					double d = posX - 2D + (double)(rand.nextFloat() * 4F);
					double d1 = posY + (double)(rand.nextFloat() * 3F);
					double d2 = posZ - 2D + (double)(rand.nextFloat() * 4F);
					double d3 = (posX - d) / 8D;
					double d4 = ((posY + 0.5D) - d1) / 8D;
					double d5 = (posZ - d2) / 8D;
					double d6 = Math.sqrt(d3 * d3 + d4 * d4 + d5 * d5);
					double d7 = 1.0D - d6;
					double d8 = 0D;
					double d9 = 0D;
					double d10 = 0D;
					if (d7 > 0.0D)
					{
						d7 *= d7;
						d8 += d3 / d6 * d7 * 0.2D;
						d9 += d4 / d6 * d7 * 0.2D;
						d10 += d5 / d6 * d7 * 0.2D;
					}
					worldObj.spawnParticle("smoke", d, d1, d2, d8, d9, d10);
				}
			}
		}
	}
	
	@Override
	protected void fall(float f) {}
	
	@Override
	public void addPotionEffect(PotionEffect effect) {}
	
    @Override
    protected int decreaseAirSupply(int i)
    {
        return i;
    }
	
	@Override
	public void onDeath(DamageSource damagesource)
	{
		super.onDeath(damagesource);
		if (!worldObj.isRemote)
		{
			worldObj.createExplosion(this, posX, posY, posZ, 3F, false);
			setDead();
		}
	}
	
	public boolean getIsUsingMace()
	{
		return dataWatcher.getWatchableObjectByte(17) == (byte)1;
	}
	
	public void setIsUsingMace(boolean flag)
	{
		dataWatcher.updateObject(17, Byte.valueOf(flag ? (byte)1 : (byte)0));
	}
}
