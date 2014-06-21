package lotr.common.item;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;

import com.google.common.collect.Multimap;

public class LOTRItemAnduril extends LOTRItemSword
{
	private float weaponDamage = 8F;
	
	public LOTRItemAnduril()
	{
		super(ToolMaterial.IRON);
		setMaxDamage(1500);
	}
	
	@Override
	public Multimap getItemAttributeModifiers()
    {
        Multimap multimap = super.getItemAttributeModifiers();
		multimap.removeAll(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName());
        multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(field_111210_e, "Weapon modifier", (double)weaponDamage, 0));
        return multimap;
    }
}
