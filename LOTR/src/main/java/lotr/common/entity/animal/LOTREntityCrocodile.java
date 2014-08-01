package lotr.common.entity.animal;

import java.util.List;

import lotr.common.LOTRMod;
import lotr.common.entity.LOTREntities;
import lotr.common.entity.ai.LOTREntityAIAttackOnCollide;
import lotr.common.entity.npc.LOTREntityNPC;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class LOTREntityCrocodile extends EntityMob
{
	private EntityAIBase targetAI;
	private boolean prevCanTarget = true;
	
    public LOTREntityCrocodile(World world)
    {
        super(world);
        setSize(2.1F, 0.7F);
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(1, new LOTREntityAIAttackOnCollide(this, 1.5D, false));
        tasks.addTask(2, new EntityAIWander(this, 1D));
        tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 8F));
        tasks.addTask(3, new EntityAIWatchClosest(this, EntityLiving.class, 12F));
        tasks.addTask(4, new EntityAILookIdle(this));
        targetTasks.addTask(0, new EntityAIHurtByTarget(this, false));
        targetTasks.addTask(1, (targetAI = new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true)));
        targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, LOTREntityNPC.class, 400, true));
    }
	
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20D);
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.2D);
		getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(4D);
    }
	
	@Override
	protected void entityInit()
	{
		super.entityInit();
		dataWatcher.addObject(20, Integer.valueOf(0));
	}
	
	public int getSnapTime()
	{
		return dataWatcher.getWatchableObjectInt(20);
	}
	
	public void setSnapTime(int i)
	{
		dataWatcher.updateObject(20, Integer.valueOf(i));
	}
	
	@Override
	public boolean isAIEnabled()
	{
		return true;
	}
	
	@Override
    public boolean canBreatheUnderwater()
    {
        return true;
    }

	@Override
    protected String getLivingSound()
    {
        return "lotr:crocodile.say";
    }
	@Override
    protected String getDeathSound()
    {
        return "lotr:crocodile.death";
    }
	
	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		
        if (!worldObj.isRemote && isInWater())
        {
        	motionY += 0.02D;
        }
		
		if (!worldObj.isRemote && getAttackTarget() != null)
		{
			EntityLivingBase entity = getAttackTarget();
			if (!entity.isEntityAlive() || (entity instanceof EntityPlayer && ((EntityPlayer)entity).capabilities.isCreativeMode))
			{
				setAttackTarget(null);
			}
		}
		
		if (!worldObj.isRemote)
		{
	        boolean canTarget = getBrightness(1F) < 0.5F;
		    if (canTarget != prevCanTarget)
		    {
		    	if (canTarget)
		    	{
		    		targetTasks.addTask(1, targetAI);
		    	}
		    	else
		    	{
		    		targetTasks.removeTask(targetAI);
		    	}
		    }
	        prevCanTarget = canTarget;
		}
		
		if (!worldObj.isRemote)
		{
			int i = getSnapTime();
			if (i > 0)
			{
				setSnapTime(i - 1);
			}
		}
		
		if (getAttackTarget() == null && worldObj.rand.nextInt(1000) == 0)
		{
            List list = worldObj.getEntitiesWithinAABB(EntityAnimal.class, boundingBox.expand(12D, 6D, 12D));
            if (!list.isEmpty())
            {
                EntityAnimal entityanimal = (EntityAnimal)list.get(rand.nextInt(list.size()));
				if (entityanimal.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.attackDamage) == null)
				{
					setAttackTarget(entityanimal);
				}
            }
		}
	}

	@Override
    protected Item getDropItem()
    {
        return Items.rotten_flesh;
    }
	
	@Override
    protected void dropFewItems(boolean flag, int i)
    {
		super.dropFewItems(flag, i);
		
		int count = rand.nextInt(3) + rand.nextInt(i + 1);
		for (int j = 0; j < count; j++)
		{
			int drop = rand.nextInt(5);
			switch(drop)
			{
				case 0:
					dropItem(Items.bone, 1);
					break;
				case 1:
					dropItem(Items.fish, 1);
					break;
				case 2:
					dropItem(Items.leather, 1);
					break;
				case 3:
					dropItem(LOTRMod.zebraRaw, 1);
					break;
				case 4:
					dropItem(LOTRMod.gemsbokHide, 1);
					break;
			}
		}
    }
	
	@Override
	public boolean attackEntityAsMob(Entity entity)
    {
        boolean flag = super.attackEntityAsMob(entity);
        if (flag)
        {
        	if (!worldObj.isRemote)
        	{
        		setSnapTime(20);
        	}
        	worldObj.playSoundAtEntity(this, "lotr:crocodile.snap", getSoundVolume(), getSoundPitch());
        }
        return flag;
    }
	
	@Override
    public boolean getCanSpawnHere()
    {
		List nearbyCrocodiles = worldObj.getEntitiesWithinAABB(getClass(), boundingBox.expand(24D, 12D, 24D));
		if (nearbyCrocodiles.size() > 3)
		{
			return false;
		}
		
        if (worldObj.checkNoEntityCollision(boundingBox) && isValidLightLevel() && worldObj.getCollidingBoundingBoxes(this, boundingBox).size() == 0)
        {
			for (int i = -8; i <= 8; i++)
			{
				for (int j = -8; j <= 8; j++)
				{
					for (int k = -8; k <= 8; k++)
					{
						int i1 = MathHelper.floor_double(posX) + i;
						int j1 = MathHelper.floor_double(posY) + j;
						int k1 = MathHelper.floor_double(posZ) + k;
						Block block = worldObj.getBlock(i1, j1, k1);
						if (block.getMaterial() == Material.water)
						{
							if (posY > 60)
							{
								return true;
							}
							else if (rand.nextInt(3) == 0)
							{
								return true;
							}
						}
					}
				}
			}
		}
        
		return false;
    }
	
	@Override
    public void moveEntityWithHeading(float f, float f1)
    {
        if (!worldObj.isRemote && isInWater() && getAttackTarget() != null)
        {
        	moveFlying(f, f1, 0.1F);
        }
        
        super.moveEntityWithHeading(f, f1);
    }
	
	@Override
    public ItemStack getPickedResult(MovingObjectPosition target)
    {
		return new ItemStack(LOTRMod.spawnEgg, 1, LOTREntities.getEntityID(this));
    }
}
