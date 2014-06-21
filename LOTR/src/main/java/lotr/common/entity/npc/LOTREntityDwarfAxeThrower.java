package lotr.common.entity.npc;

import lotr.common.LOTRMod;
import lotr.common.entity.projectile.LOTREntityThrowingAxe;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTREntityDwarfAxeThrower extends LOTREntityDwarfWarrior implements IRangedAttackMob
{
	public LOTREntityDwarfAxeThrower(World world)
	{
		super(world);
	}
	
	@Override
	public EntityAIBase getDwarfAttackAI()
	{
		return new EntityAIArrowAttack(this, 1.25D, 30, 12F);
	}
	
	@Override
    public IEntityLivingData onSpawnWithEgg(IEntityLivingData data)
    {
		data = super.onSpawnWithEgg(data);
		setCurrentItemOrArmor(0, new ItemStack(LOTRMod.throwingAxeDwarven));
		return data;
	}

	@Override
    public void attackEntityWithRangedAttack(EntityLivingBase target, float f)
    {
        LOTREntityThrowingAxe axe = new LOTREntityThrowingAxe(worldObj, this, target, LOTRMod.throwingAxeDwarven, 0, 1F, 0.75F);
        playSound("random.bow", 1F, 1F / (rand.nextFloat() * 0.4F + 0.8F));
        worldObj.spawnEntityInWorld(axe);
		swingItem();
    }
}
