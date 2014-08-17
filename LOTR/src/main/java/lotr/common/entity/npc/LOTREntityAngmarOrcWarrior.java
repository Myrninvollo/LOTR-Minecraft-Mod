package lotr.common.entity.npc;

import lotr.common.LOTRAlignmentValues;
import lotr.common.LOTRMod;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTREntityAngmarOrcWarrior extends LOTREntityAngmarOrc
{
	public LOTREntityAngmarOrcWarrior(World world)
	{
		super(world);
		setSize(0.6F, 1.8F);
		isWeakOrc = false;
	}
	
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(25D);
    }
	
	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData data)
	{
		data = super.onSpawnWithEgg(data);
		setCurrentItemOrArmor(0, new ItemStack(LOTRMod.morgulBlade));
		setCurrentItemOrArmor(1, new ItemStack(LOTRMod.bootsMorgul));
		setCurrentItemOrArmor(2, new ItemStack(LOTRMod.legsMorgul));
		setCurrentItemOrArmor(3, new ItemStack(LOTRMod.bodyMorgul));
		setCurrentItemOrArmor(4, null);
		return data;
	}
	
	@Override
	public int getAlignmentBonus()
	{
		return LOTRAlignmentValues.Bonuses.ANGMAR_ORC_WARRIOR;
	}
}
