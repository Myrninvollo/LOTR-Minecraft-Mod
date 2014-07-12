package lotr.common.entity.npc;

import lotr.common.LOTRFaction;
import lotr.common.entity.ai.LOTREntityAIAttackOnCollide;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFleeSun;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIRestrictSun;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public abstract class LOTREntitySkeletalWraith extends LOTREntityNPC
{
	public LOTREntitySkeletalWraith(World world)
	{
		super(world);
		setSize(0.6F, 1.8F);
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(1, new EntityAIRestrictSun(this));
        tasks.addTask(2, new EntityAIFleeSun(this, 1D));
		tasks.addTask(3, new LOTREntityAIAttackOnCollide(this, 1.2D, false));
        tasks.addTask(4, new EntityAIWander(this, 1D));
        tasks.addTask(5, new EntityAIWatchClosest(this, EntityPlayer.class, 8F, 0.02F));
        tasks.addTask(6, new EntityAILookIdle(this));
        targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        addTargetTasks(2);
	}
	
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(24D);
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
    }
	
	@Override
	public LOTRFaction getFaction()
	{
		return LOTRFaction.HOSTILE;
	}
	
	@Override
    public boolean isAIEnabled()
    {
        return true;
    }
	
	@Override
    public void onLivingUpdate()
    {
        if (worldObj.isDaytime() && !worldObj.isRemote)
        {
            float f = getBrightness(1F);

            if (f > 0.5F && rand.nextFloat() * 30F < (f - 0.4F) * 2F && worldObj.canBlockSeeTheSky(MathHelper.floor_double(posX), MathHelper.floor_double(posY), MathHelper.floor_double(posZ)))
            {
                boolean flag = true;
                ItemStack itemstack = getEquipmentInSlot(4);

                if (itemstack != null)
                {
                    if (itemstack.isItemStackDamageable())
                    {
                        itemstack.setItemDamage(itemstack.getItemDamageForDisplay() + rand.nextInt(2));

                        if (itemstack.getItemDamageForDisplay() >= itemstack.getMaxDamage())
                        {
                            renderBrokenItemStack(itemstack);
                            setCurrentItemOrArmor(4, (ItemStack)null);
                        }
                    }

                    flag = false;
                }

                if (flag)
                {
                    setFire(8);
                }
            }
        }

        super.onLivingUpdate();
		
        if (rand.nextBoolean())
        {
            worldObj.spawnParticle("smoke", posX + (rand.nextDouble() - 0.5D) * (double)width, posY + rand.nextDouble() * (double)height, posZ + (rand.nextDouble() - 0.5D) * (double)width, 0.0D, 0.0D, 0.0D);
        }
    }
	
	@Override
	public Item getDropItem()
	{
		return Items.bone;
	}
	
	@Override
	public boolean canDropPouch()
	{
		return false;
	}
	
	@Override
    protected String getLivingSound()
    {
        return "mob.skeleton.say";
    }

	@Override
    protected String getHurtSound()
    {
        return "mob.skeleton.hurt";
    }

	@Override
    protected String getDeathSound()
    {
        return "mob.skeleton.death";
    }
	
	@Override
    public EnumCreatureAttribute getCreatureAttribute()
    {
        return EnumCreatureAttribute.UNDEAD;
    }
}
