package lotr.common.entity.animal;

import lotr.common.LOTRMod;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTREntityElk extends LOTREntityHorse
{
	public LOTREntityElk(World world)
	{
		super(world);
		setSize(1.6F, 1.8F);
	}
	
	@Override
	public int getHorseType()
	{
		return 0;
	}
	
	@Override
	protected void onLOTRHorseSpawn()
	{
		double maxHealth = getEntityAttribute(SharedMonsterAttributes.maxHealth).getAttributeValue();
		maxHealth = (double)(maxHealth * (1F + rand.nextFloat() * 0.5F));
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(maxHealth);
	}
	
	@Override
	public boolean isBreedingItem(ItemStack itemstack)
    {
        return itemstack != null && itemstack.getItem() == Items.wheat;
    }
}
