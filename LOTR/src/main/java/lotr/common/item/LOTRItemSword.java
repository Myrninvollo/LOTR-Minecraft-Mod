package lotr.common.item;

import com.google.common.collect.Multimap;

import lotr.common.LOTRCreativeTabs;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ItemSword;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRItemSword extends ItemSword
{
	@SideOnly(Side.CLIENT)
	public IIcon glowingIcon;
	private boolean isElvenBlade = false;
	protected float lotrWeaponDamage;
	
	public LOTRItemSword(ToolMaterial material)
	{
		super(material);
		setCreativeTab(LOTRCreativeTabs.tabCombat);
		lotrWeaponDamage = material.getDamageVsEntity() + 4F;
	}
	
	public float getLOTRWeaponDamage()
	{
		return lotrWeaponDamage;
	}
	
	public LOTRItemSword setIsElvenBlade()
	{
		isElvenBlade = true;
		return this;
	}
	
	public boolean isElvenBlade()
	{
		return isElvenBlade;
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconregister)
    {
		super.registerIcons(iconregister);
		if (isElvenBlade)
		{
			glowingIcon = iconregister.registerIcon(getIconString() + "_glowing");
		}
    }
	
	@Override
    public Multimap getItemAttributeModifiers()
    {
        Multimap multimap = super.getItemAttributeModifiers();
        multimap.removeAll(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName());
        multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(field_111210_e, "LOTR Weapon modifier", (double)lotrWeaponDamage, 0));
        return multimap;
    }
}
