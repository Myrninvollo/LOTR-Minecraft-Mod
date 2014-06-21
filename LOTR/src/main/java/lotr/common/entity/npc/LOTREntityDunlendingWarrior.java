package lotr.common.entity.npc;

import lotr.common.LOTRAlignmentValues;
import lotr.common.LOTRMod;
import lotr.common.entity.ai.LOTREntityAIAttackOnCollide;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTREntityDunlendingWarrior extends LOTREntityDunlending
{
	public LOTREntityDunlendingWarrior(World world)
	{
		super(world);
	}
	
	@Override
	public EntityAIBase getDunlendingAttackAI()
	{
		return new LOTREntityAIAttackOnCollide(this, 1.45D, false).setSpearReplacement(Items.iron_sword);
	}
	
	@Override
    public IEntityLivingData onSpawnWithEgg(IEntityLivingData data)
    {
		data = super.onSpawnWithEgg(data);
		
		int i = rand.nextInt(3);
		if (i == 0)
		{
			setCurrentItemOrArmor(0, new ItemStack(Items.iron_sword));
		}
		else if (i == 1)
		{
			setCurrentItemOrArmor(0, new ItemStack(LOTRMod.daggerIron));
		}
		else if (i == 2)
		{
			setCurrentItemOrArmor(0, new ItemStack(LOTRMod.spearIron));
		}
		
		setCurrentItemOrArmor(1, new ItemStack(LOTRMod.bootsDunlending));
		setCurrentItemOrArmor(2, new ItemStack(LOTRMod.legsDunlending));
		setCurrentItemOrArmor(3, new ItemStack(LOTRMod.bodyDunlending));
		
		if (rand.nextInt(10) != 0)
		{
			setCurrentItemOrArmor(4, new ItemStack(LOTRMod.helmetDunlending));
		}
		
		return data;
    }
	
	@Override
	public boolean canPickUpLoot()
	{
		return false;
	}
	
	@Override
	public int getAlignmentBonus()
	{
		return LOTRAlignmentValues.DUNLENDING_WARRIOR_BONUS;
	}
	
	@Override
	public void dropDunlendingItems(boolean flag, int i) {}
}
