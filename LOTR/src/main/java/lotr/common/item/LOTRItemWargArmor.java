package lotr.common.item;

import lotr.common.LOTRCreativeTabs;
import net.minecraft.item.*;
import net.minecraft.item.ItemArmor.ArmorMaterial;

public class LOTRItemWargArmor extends Item
{
	private ArmorMaterial material;
	public int armorType;
	public int damageReduceAmount;
	
	public LOTRItemWargArmor(ArmorMaterial m, int j)
	{
		super();
		material = m;
		armorType = j;
		damageReduceAmount = m.getDamageReductionAmount(j);
        setMaxDamage(m.getDurability(j));
		setMaxStackSize(1);
		setCreativeTab(LOTRCreativeTabs.tabCombat);
	}

	@Override
    public int getItemEnchantability()
    {
        return material.getEnchantability();
    }
	
	@Override
    public boolean getIsRepairable(ItemStack itemstack, ItemStack repairItem)
    {
        return material.func_151685_b() == repairItem.getItem() ? true : super.getIsRepairable(itemstack, repairItem);
    }
}
