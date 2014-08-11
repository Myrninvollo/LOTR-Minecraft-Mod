package lotr.common.item;

import lotr.common.LOTRCreativeTabs;
import lotr.common.entity.animal.LOTREntityElk;
import lotr.common.entity.animal.LOTREntityHorse;
import net.minecraft.item.*;
import net.minecraft.item.ItemArmor.ArmorMaterial;

public class LOTRItemMountArmor extends Item
{
	public static enum Mount
	{
		HORSE,
		ELK,
		BOAR,
		CAMEL;
	}
	
	private ArmorMaterial armorMaterial;
	private Mount mountType;
	private int damageReduceAmount;
	
	public LOTRItemMountArmor(ArmorMaterial material, Mount mount)
	{
		super();
		armorMaterial = material;
		damageReduceAmount = material.getDamageReductionAmount(1) + material.getDamageReductionAmount(2);
		mountType = mount;
		setMaxStackSize(1);
		setCreativeTab(LOTRCreativeTabs.tabCombat);
	}
	
	public boolean isValid(LOTREntityHorse horse)
	{
		if (horse instanceof LOTREntityElk)
		{
			return mountType == Mount.ELK;
		}
		return mountType == Mount.HORSE;
	}
	
	public int getDamageReduceAmount()
	{
		return damageReduceAmount;
	}

	@Override
    public int getItemEnchantability()
    {
        return 0;
    }
	
	@Override
    public boolean getIsRepairable(ItemStack itemstack, ItemStack repairItem)
    {
        return armorMaterial.func_151685_b() == repairItem.getItem() ? true : super.getIsRepairable(itemstack, repairItem);
    }
}
