package lotr.common.entity.npc;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTREntityDunlendingArcher extends LOTREntityDunlendingWarrior implements IRangedAttackMob
{
	public LOTREntityDunlendingArcher(World world)
	{
		super(world);
	}
	
	@Override
	public EntityAIBase getDunlendingAttackAI()
	{
		return new EntityAIArrowAttack(this, 1.3D, 40, 60, 16F);
	}
	
	@Override
    public IEntityLivingData onSpawnWithEgg(IEntityLivingData data)
    {
		data = super.onSpawnWithEgg(data);
		setCurrentItemOrArmor(0, new ItemStack(Items.bow));
		return data;
    }
	
	@Override
    public void attackEntityWithRangedAttack(EntityLivingBase target, float f)
    {
        EntityArrow arrow = new EntityArrow(worldObj, this, target, 1.5F, 1F);
        playSound("random.bow", 1F, 1F / (rand.nextFloat() * 0.4F + 0.8F));
        worldObj.spawnEntityInWorld(arrow);
    }
	
	@Override
	protected void dropFewItems(boolean flag, int i)
	{
		super.dropFewItems(flag, i);
		
		if (rand.nextBoolean())
		{
			int j = rand.nextInt(3) + rand.nextInt(i + 1);
			for (int k = 0; k < j; k++)
			{
				dropItem(Items.arrow, 1);
			}
		}
	}
}
