package lotr.common.item;

import java.util.List;

import lotr.common.LOTRCreativeTabs;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRItemAncientItemParts extends Item
{
	@SideOnly(Side.CLIENT)
	private IIcon[] icons;
	private String[] partNames = {"swordTip", "swordBlade", "swordHilt", "armorPlate"};
	
	public LOTRItemAncientItemParts()
	{
		super();
        setHasSubtypes(true);
        setMaxDamage(0);
		setCreativeTab(LOTRCreativeTabs.tabMisc);
	}

	@Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int i)
    {
        if (i >= icons.length)
		{
			i = 0;
		}
		return icons[i];
    }
	
	@Override
    public String getUnlocalizedName(ItemStack itemstack)
    {
        return super.getUnlocalizedName() + "." + itemstack.getItemDamage();
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconregister)
    {
        icons = new IIcon[partNames.length];
        for (int i = 0; i < partNames.length; i++)
        {
            icons[i] = iconregister.registerIcon(getIconString() + "_" + partNames[i]);
        }
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list)
    {
        for (int j = 0; j <= 3; j++)
        {
            list.add(new ItemStack(item, 1, j));
        }
    }
}
