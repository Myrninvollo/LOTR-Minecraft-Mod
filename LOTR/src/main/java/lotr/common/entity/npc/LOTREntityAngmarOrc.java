package lotr.common.entity.npc;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRAlignmentValues;
import lotr.common.LOTRFaction;
import lotr.common.LOTRMod;
import lotr.common.entity.ai.LOTREntityAIAttackOnCollide;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTREntityAngmarOrc extends LOTREntityOrc
{
	public LOTREntityAngmarOrc(World world)
	{
		super(world);
	}
	
	@Override
	public EntityAIBase createOrcAttackAI()
	{
		return new LOTREntityAIAttackOnCollide(this, 1.4D, false);
	}
	
	@Override
    public IEntityLivingData onSpawnWithEgg(IEntityLivingData data)
    {
		data = super.onSpawnWithEgg(data);
		int i = rand.nextInt(8);
		
		if (i == 0 || i == 1 || i == 2)
		{
			setCurrentItemOrArmor(0, new ItemStack(LOTRMod.swordAngmar));
		}
		else if (i == 3)
		{
			setCurrentItemOrArmor(0, new ItemStack(LOTRMod.battleaxeAngmar));
		}
		else if (i == 4)
		{
			setCurrentItemOrArmor(0, new ItemStack(LOTRMod.daggerAngmar));
		}
		else if (i == 5)
		{
			setCurrentItemOrArmor(0, new ItemStack(LOTRMod.daggerAngmarPoisoned));
		}
		else if (i == 6)
		{
			setCurrentItemOrArmor(0, new ItemStack(LOTRMod.spearAngmar));
		}
		else if (i == 7)
		{
			setCurrentItemOrArmor(0, new ItemStack(LOTRMod.hammerAngmar));
		}
		
		setCurrentItemOrArmor(1, new ItemStack(LOTRMod.bootsAngmar));
		setCurrentItemOrArmor(2, new ItemStack(LOTRMod.legsAngmar));
		setCurrentItemOrArmor(3, new ItemStack(LOTRMod.bodyAngmar));
		
		if (rand.nextInt(5) != 0)
		{
			setCurrentItemOrArmor(4, new ItemStack(LOTRMod.helmetAngmar));
		}
		
		return data;
    }
	
	@Override
	public LOTRFaction getFaction()
	{
		return LOTRFaction.ANGMAR;
	}
	
	@Override
	public int getAlignmentBonus()
	{
		return LOTRAlignmentValues.Bonuses.ANGMAR_ORC;
	}
	
	@Override
	protected LOTRAchievement getKillAchievement()
	{
		return LOTRAchievement.killAngmarOrc;
	}
}
