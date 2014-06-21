package lotr.common.entity.npc;

import lotr.common.LOTRMod;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTREntityGundabadOrcArcher extends LOTREntityGundabadOrc implements IRangedAttackMob
{
	public LOTREntityGundabadOrcArcher(World world)
	{
		super(world);
	}
	
	@Override
	public EntityAIBase getOrcAttackAI()
	{
		return new EntityAIArrowAttack(this, 1.25D, 30, 60, 12F);
	}
	
	@Override
    public IEntityLivingData onSpawnWithEgg(IEntityLivingData data)
    {
		data = super.onSpawnWithEgg(data);
		setCurrentItemOrArmor(0, new ItemStack(LOTRMod.orcBow));
		return data;
	}
	
	@Override
    public void attackEntityWithRangedAttack(EntityLivingBase target, float f)
    {
        orcArrowAttack(target, f);
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
