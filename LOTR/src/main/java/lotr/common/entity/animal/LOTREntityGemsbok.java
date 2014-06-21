package lotr.common.entity.animal;

import lotr.common.LOTRMod;
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
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTREntityGemsbok extends EntityAnimal
{
    public LOTREntityGemsbok(World world)
    {
        super(world);
        setSize(0.9F, 1.4F);
        getNavigator().setAvoidsWater(true);
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(1, new EntityAIPanic(this, 1.3D));
        tasks.addTask(2, new EntityAIMate(this, 1D));
        tasks.addTask(3, new EntityAITempt(this, 1.2D, Items.wheat, false));
        tasks.addTask(4, new EntityAIFollowParent(this, 1.1D));
        tasks.addTask(5, new EntityAIWander(this, 1D));
        tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 6F));
        tasks.addTask(7, new EntityAILookIdle(this));
    }

	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(16D);
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
    }
	
	@Override
    public boolean isAIEnabled()
    {
        return true;
    }
	
	@Override
    public boolean isBreedingItem(ItemStack itemstack)
    {
        return itemstack.getItem() == Items.wheat;
    }
	
	@Override
    protected void dropFewItems(boolean flag, int i)
    {
        int j = 1 + rand.nextInt(4) + rand.nextInt(1 + i);
        for (int k = 0; k < j; k++)
        {
			dropItem(LOTRMod.gemsbokHide, 1);
        }
        
		if (rand.nextBoolean())
        {
			dropItem(LOTRMod.gemsbokHorn, 1);
        }
    }

	@Override
    public EntityAgeable createChild(EntityAgeable entity)
    {
        return new LOTREntityGemsbok(worldObj);
    }
	
	@Override
	protected String getLivingSound()
    {
        return "lotr:zebra.say";
    }

	@Override
    protected String getHurtSound()
    {
        return "lotr:zebra.hurt";
    }

	@Override
    protected String getDeathSound()
    {
        return "lotr:zebra.death";
    }
}
