package lotr.common.entity.npc;

import lotr.common.LOTRMod;
import lotr.common.entity.projectile.LOTREntityCrossbowBolt;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTREntityUrukHaiCrossbower extends LOTREntityUrukHai implements IRangedAttackMob
{
	public LOTREntityUrukHaiCrossbower(World world)
	{
		super(world);
	}
	
	@Override
	public EntityAIBase createOrcAttackAI()
	{
		return new EntityAIArrowAttack(this, 1.4D, 30, 40, 16F);
	}
	
	@Override
    public IEntityLivingData onSpawnWithEgg(IEntityLivingData data)
    {
		data = super.onSpawnWithEgg(data);
		setCurrentItemOrArmor(0, new ItemStack(LOTRMod.urukCrossbow));
		return data;
	}
	
	@Override
    public void attackEntityWithRangedAttack(EntityLivingBase target, float f)
    {
		double d = getDistanceToEntity(target);
        LOTREntityCrossbowBolt bolt = new LOTREntityCrossbowBolt(worldObj, this, target, 1F + ((float)d / 16F * 0.015F), 1F);
        playSound("lotr:item.crossbow", 1F, 1F / (rand.nextFloat() * 0.4F + 0.8F));
        worldObj.spawnEntityInWorld(bolt);
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
				dropItem(LOTRMod.crossbowBolt, 1);
			}
		}
	}
}
