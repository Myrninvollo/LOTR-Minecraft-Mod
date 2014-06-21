package lotr.common.entity.animal;

import lotr.common.LOTRMod;
import lotr.common.entity.LOTREntities;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class LOTREntityFlamingo extends EntityAnimal
{
    public boolean field_753_a = false;
    public float field_752_b;
    public float destPos;
    public float field_757_d;
    public float field_756_e;
    public float field_755_h = 5F;

    public LOTREntityFlamingo(World world)
    {
        super(world);
        setSize(0.6F, 1.8F);
        getNavigator().setAvoidsWater(false);
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(1, new EntityAIPanic(this, 1.3D));
        tasks.addTask(2, new EntityAIMate(this, 1D));
        tasks.addTask(3, new EntityAITempt(this, 1.2D, Items.fish, false));
        tasks.addTask(4, new EntityAIFollowParent(this, 1.2D));
        tasks.addTask(5, new EntityAIWander(this, 1D));
        tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 6F));
        tasks.addTask(7, new EntityAILookIdle(this));
    }
    
    @Override
    public void entityInit()
    {
    	super.entityInit();
    	dataWatcher.addObject(16, Integer.valueOf(0));
    }
    
    public int getFishingTick()
    {
    	return dataWatcher.getWatchableObjectInt(16);
    }
	
    public void setFishingTick(int i)
    {
    	dataWatcher.updateObject(16, Integer.valueOf((0)));
    }
    
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(8D);
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
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
        field_756_e = field_752_b;
        field_757_d = destPos;
        destPos += (double)((onGround || inWater) ? -1 : 4) * 0.3D;
        if (destPos < 0F)
        {
            destPos = 0F;
        }
        if (destPos > 5F)
        {
            destPos = 1F;
        }
        if (!(onGround || inWater) && field_755_h < 1F)
        {
            field_755_h = 1F;
        }
        field_755_h *= 0.9D;
        if (!(onGround || inWater) && motionY < 0.0D)
        {
            motionY *= 0.6D;
        }
        field_752_b += field_755_h * 2F;
		
		if (!worldObj.isRemote && !isChild() && !isInLove() && getFishingTick() == 0 && rand.nextInt(600) == 0)
		{
			Block block = worldObj.getBlock(MathHelper.floor_double(posX), MathHelper.floor_double(boundingBox.minY), MathHelper.floor_double(posZ));
			if (block == Blocks.water)
			{
				setFishingTick(200);
			}
		}
		
		if (getFishingTick() > 0)
		{
			if (!worldObj.isRemote)
			{
				setFishingTick(getFishingTick() - 1);
			}
			else
			{
				for (int i = 0; i < 3; i++)
				{
					double d = posX - 0.3D + (double)(getRNG().nextFloat() * 0.6F);
					double d1 = boundingBox.minY - 0.3D + (double)(getRNG().nextFloat() * 0.6F);
					double d2 = posZ - 0.3D + (double)(getRNG().nextFloat() * 0.6F);
					worldObj.spawnParticle("bubble", d, d1, d2, 0, 0, 0);
				}
			}
		}
		
		if (!worldObj.isRemote && isInLove() && getFishingTick() > 20)
		{
			setFishingTick(20);
		}
    }
	
	@Override
	public boolean attackEntityFrom(DamageSource source, float f)
	{
		boolean flag = super.attackEntityFrom(source, f);
		if (flag && !worldObj.isRemote && getFishingTick() > 20)
		{
			setFishingTick(20);
		}
		return flag;
	}

	@Override
    protected void fall(float f) {}

	@Override
    protected String getLivingSound()
    {
        return "lotr:flamingo.say";
    }

	@Override
    protected String getHurtSound()
    {
        return "lotr:flamingo.hurt";
    }

	@Override
    protected String getDeathSound()
    {
        return "lotr:flamingo.death";
    }

	@Override
    protected Item getDropItem()
    {
		return Items.feather;
    }

	@Override
    public boolean isBreedingItem(ItemStack itemstack)
    {
        return itemstack.getItem() == Items.fish;
    }

	@Override
    public EntityAgeable createChild(EntityAgeable entity)
    {
        return new LOTREntityFlamingo(worldObj);
    }
	
	@Override
    public ItemStack getPickedResult(MovingObjectPosition target)
    {
		return new ItemStack(LOTRMod.spawnEgg, 1, LOTREntities.getEntityID(this));
    }
}
