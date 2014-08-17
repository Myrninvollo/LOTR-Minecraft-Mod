package lotr.common.entity.animal;

import lotr.common.entity.ai.LOTREntityAIAttackOnCollide;
import net.minecraft.block.Block;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTREntityWildBoar extends LOTREntityHorse
{
	public LOTREntityWildBoar(World world)
	{
		super(world);
		setSize(0.9F, 0.9F);
	}
	
	@Override
	protected boolean isMountHostile()
	{
		return true;
	}
	
	@Override
    protected EntityAIBase createMountAttackAI()
    {
    	return new LOTREntityAIAttackOnCollide(this, 1.25D, true, 0.8F);
    }
	
	@Override
	public int getHorseType()
	{
		return 0;
	}

	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(3D);
	}

	@Override
	protected void onLOTRHorseSpawn()
	{
		double maxHealth = getEntityAttribute(SharedMonsterAttributes.maxHealth).getAttributeValue();
		maxHealth = Math.min(maxHealth, 25D);
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(maxHealth);
		
		double speed = getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue();
		speed *= 1.25D;
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(speed);
	}
	
	@Override
	public boolean isBreedingItem(ItemStack itemstack)
	{
		return itemstack != null && itemstack.getItem() == Items.carrot;
	}

	@Override
    protected void dropFewItems(boolean flag, int i)
    {
        int j = rand.nextInt(3) + 1 + rand.nextInt(1 + i);

        for (int k = 0; k < j; k++)
        {
            if (isBurning())
            {
                dropItem(Items.cooked_porkchop, 1);
            }
            else
            {
                dropItem(Items.porkchop, 1);
            }
        }
    }
	
	@Override
    protected String getLivingSound()
    {
        return "mob.pig.say";
    }

	@Override
    protected String getHurtSound()
    {
        return "mob.pig.say";
    }

	@Override
    protected String getDeathSound()
    {
        return "mob.pig.death";
    }
	
	@Override
    protected String getAngrySoundName()
    {
        return "mob.pig.say";
    }
	
	@Override
    protected void func_145780_a(int i, int j, int k, Block block)
    {
        playSound("mob.pig.step", 0.15F, 1F);
    }
}
